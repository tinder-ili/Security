package com.example.ivyli.security.presenters;


import android.util.Log;

import com.example.ivyli.security.activity.MainActivity;
import com.example.ivyli.security.repository.PhotoRepository;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainViewPresenter extends BasePresenter<MainActivity> {

    @Inject PhotoRepository mReposiroty;

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
                            target.setRetakePhotoButton();
                        } else {
                            target.setTakePhotoText();
                        }
                    }
                });
    }

}
