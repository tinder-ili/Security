package com.example.ivyli.security.presenters;


import android.text.TextUtils;

import com.example.ivyli.security.activity.PhotoPreviewActivity;
import com.example.ivyli.security.db.ImagesTable;
import com.example.ivyli.security.repository.PhotoRepository;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PhotoPreviewPresenter extends BasePresenter<PhotoPreviewActivity> {

    private final PhotoRepository mRepository;

    @Inject
    public PhotoPreviewPresenter(PhotoRepository repository) {
        mRepository = repository;
    }

    public void initView() {
        PhotoPreviewActivity target = getTarget();
        if (target == null) {
            return;
        }

        List<ImagesTable> photos = mRepository.getPhotos();
        target.initView(photos);
    }

    public void savePhotos(final String password1, final String password2) {
        Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (!TextUtils.isEmpty(password1) && password1.length() >= 8 && !TextUtils.isEmpty(password2) && password1.equals(password2)) {
                    mRepository.savePhotos(password1);
                    mRepository.savePassword(password1);
                    return true;

                } else {
                    return false;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        PhotoPreviewActivity target = getTarget();
                        if (target == null) {
                            return;
                        }

                        target.dismissProgress();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        PhotoPreviewActivity target = getTarget();
                        if (target == null) {
                            return;
                        }
                        if (aBoolean) {
                            target.finish();
                            target.dismissProgress();

                        } else {
                            target.passwordError();
                            target.dismissProgress();
                        }
                    }
                });
    }


    public void clearCache(){
        mRepository.clearCache();
    }
}
