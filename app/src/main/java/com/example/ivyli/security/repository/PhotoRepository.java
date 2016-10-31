package com.example.ivyli.security.repository;


import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.example.ivyli.security.db.ImageHash;
import com.example.ivyli.security.db.ImagesTable;
import com.example.ivyli.security.utils.EncryptionUtil;
import com.example.ivyli.security.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import rx.Observable;

public class PhotoRepository {
    private final List<ImagesTable> mPhotos = new ArrayList<>();

    public PhotoRepository(Context context) {
    }

    public void addPhoto(byte[] photo) {
        String uid = UUID.randomUUID().toString();
        ImagesTable image = new ImagesTable(uid);
        image.setBitmap(ImageUtil.getImageBitmap(photo));
        mPhotos.add(image);
    }

    public Observable<Boolean> hasPhotos() {
        if (!mPhotos.isEmpty()) {
            return Observable.just(true);
        }

        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<ImagesTable> images = new Select().from(ImagesTable.class).execute();
                mPhotos.clear();
                mPhotos.addAll(images);
                return !images.isEmpty();
            }
        });
    }

    public List<ImagesTable> getPhotos() {
        return mPhotos;
    }

    public void savePhotos(String password) {
        ActiveAndroid.beginTransaction();
        try {
            for (ImagesTable image : mPhotos) {
                // save the file hash first
                ImageHash hash = new ImageHash(image.getUid(), EncryptionUtil.md5(image.getImageString().toString()));
                hash.save();

                //encrypt the file then save
                image.setImageString(EncryptionUtil.encryptImage(password, image.getImageString()));
                image.save();
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        mPhotos.clear();
    }
}
