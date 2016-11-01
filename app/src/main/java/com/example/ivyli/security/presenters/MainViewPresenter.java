package com.example.ivyli.security.presenters;


import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.ivyli.security.R;
import com.example.ivyli.security.activity.MainActivity;
import com.example.ivyli.security.db.ImagesTable;
import com.example.ivyli.security.repository.PhotoRepository;
import com.example.ivyli.security.utils.EncryptionUtil;
import com.example.ivyli.security.utils.FileUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainViewPresenter extends BasePresenter<MainActivity> {

    @Inject PhotoRepository mReposiroty;
    private final Handler handler = new Handler();

    @Inject
    public MainViewPresenter() {
    }

    public void initView() {
        mReposiroty.hasPhotos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        //complete do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("MainViewPresenter", e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        MainActivity target = getTarget();
                        if (target == null) {
                            return;
                        }

                        if (aBoolean) {
                            target.initFacialRecognition();
                        } else {
                            target.initTakePhoto();
                        }
                    }
                });
    }

    public void startFacialRecognition() {
        final MainActivity target = getTarget();
        if (target == null) {
            return;
        }

        final String key = mReposiroty.getPassword();
        if (TextUtils.isEmpty(key)) {
            target.toast(R.string.no_key_error);
            return;
        }

        Observable.combineLatest(mReposiroty.getPhotoFromDb(),
                mReposiroty.getImageHashFromDb(), new Func2<List<ImagesTable>, Map<String, String>, Object>() {
                    @Override
                    public Object call(List<ImagesTable> imagesTables, Map<String, String> stringHash) {
                        int index = 0;
                        for (ImagesTable image : imagesTables) {
                            String filePath = image.getImageString();
                            String content = FileUtil.readFile(filePath);

                            String decryptedString = EncryptionUtil.decrypt(content, key);
                            if (TextUtils.isEmpty(decryptedString)) {
                                postOnUI(R.string.image_file_bad, index, target);
                                return null;
                            }

                            postOnUI(R.string.decrypt_image_success, index, target);

                            String hash = stringHash.get(image.getUid());
                            Log.e("", "&**** the  startFacialRecognition hash uid: " + image.getUid() + " the hash  " + hash);
                            Log.e("", "&**** the  startFacialRecognition image path: " + image.getImageString() + " image hash "
                                    + EncryptionUtil.md5(decryptedString));

                            if (TextUtils.isEmpty(hash) || !hash.equals(EncryptionUtil.md5(decryptedString))) {
                                postOnUI(R.string.image_file_hash_wrong, -1, target);
                                return null;
                            }
                            postOnUI(R.string.image_hash_matched, index, target);
                            index++;
                        }

                        postOnUI(R.string.facial_recognition_success, -1, target);
                        return null;
                    }
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<Object>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("", "&**** the onerror" + e.getLocalizedMessage());
                                postOnUI(R.string.error, -1, getTarget());
                            }

                            @Override
                            public void onNext(Object o) {

                            }
                        }

                );
    }

    private void postOnUI(final int id, final int arg, final MainActivity target) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                target.addStatus(id, arg);
            }
        });
    }

    public void ratakePhoto() {
        mReposiroty.clearDb();
        initView();
    }

}
