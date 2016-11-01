package com.example.ivyli.security.repository;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.example.ivyli.security.db.ImageHash;
import com.example.ivyli.security.db.ImagesTable;
import com.example.ivyli.security.utils.EncryptionUtil;
import com.example.ivyli.security.utils.FileUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import rx.Observable;

public class PhotoRepository {

    private static final String KEY_PASSWORD = "password_key";

    private final List<ImagesTable> mPhotos = new ArrayList<>();
    private static SharedPreferences sPreferences;
    private static SharedPreferences.Editor sEditor;

    public PhotoRepository(Context context) {
        sPreferences = context.getSharedPreferences("photoSP", Context.MODE_PRIVATE);
        sEditor = sPreferences.edit();
    }

    public void addPhoto(byte[] photo) {
        String uid = UUID.randomUUID().toString();
        ImagesTable image = new ImagesTable(uid);
        image.setImageByts(photo);
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

    public Observable<List<ImagesTable>> getPhotoFromDb() {

        return Observable.fromCallable(new Callable<List<ImagesTable>>() {
            @Override
            public List<ImagesTable> call() throws Exception {
                return new Select().from(ImagesTable.class).execute();
            }
        });
    }

    public Observable<Map<String, String>> getImageHashFromDb() {
        return Observable.fromCallable(new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                List<ImageHash> hashList = new Select().from(ImageHash.class).execute();
                Map<String, String> uidToHash = new HashMap<String, String>();
                for (ImageHash imageHash : hashList) {
                    uidToHash.put(imageHash.getUid(), imageHash.getImageHash());
                }

                return uidToHash;
            }
        });
    }

    public void savePhotos(String password) {
        ActiveAndroid.beginTransaction();
        try {
            for (ImagesTable image : mPhotos) {
                // save the file hash first
                try {
                    String imageString = new String(image.getImageByts(), "UTF-8");
                    String hashs = EncryptionUtil.md5(imageString);
                    ImageHash hash = new ImageHash(image.getUid(), hashs);
                    hash.save();

                    Log.e("", "&**** the hash uid: " + hash.getUid() + " the hahs hash " + hashs);
                    //encrypt the file then save
                    String path = FileUtil.saveImageFile(EncryptionUtil.encrypt(imageString, password), image.getUid());
                    image.setImageString(path);
                    Log.e("", "&**** the image image uid: " + image.getUid() + " the imageimge path " + image.getImageString());
                    image.save();
                } catch (UnsupportedEncodingException e) {
                    Log.e("", "saving image file failed");
                }
            }

            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        mPhotos.clear();
    }


    public void savePassword(String password) {
        // this method should encrypt the password then post to server.
        try {
            String encryptedPassword = EncryptionUtil.encrypt(password, EncryptionUtil.encryption_key);
            //instead of posting to server, we save to the local sharedPref now
            sEditor.putString(KEY_PASSWORD, encryptedPassword).commit();
        } catch (Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
    }

    public String getPassword() {
        String encryptedPassword = sPreferences.getString(KEY_PASSWORD, null);
        if (encryptedPassword == null) {
            return null;
        }
        return EncryptionUtil.decrypt(encryptedPassword, EncryptionUtil.encryption_key);
    }

    public void clearCache(){
        mPhotos.clear();
    }

    public void clearDb() {
        SQLiteUtils.execSql("delete from  ImagesHash");
        SQLiteUtils.execSql("delete from  Images");
        mPhotos.clear();
    }
}
