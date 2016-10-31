package com.example.ivyli.security.presenters;


import android.text.TextUtils;

import com.example.ivyli.security.activity.PhotoPreviewActivity;
import com.example.ivyli.security.db.ImagesTable;
import com.example.ivyli.security.repository.PhotoRepository;

import java.util.List;

import javax.inject.Inject;

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

    public void savePhotos(String password1, String password2) {
        if (!TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2) && password1.equals(password2)) {
            mRepository.savePhotos(password1);

            PhotoPreviewActivity target = getTarget();
            if (target != null) {
                target.finish();
            }
        }else{
            PhotoPreviewActivity target = getTarget();
            if(target == null){
                return;
            }

            target.passwordError();
        }
    }
}
