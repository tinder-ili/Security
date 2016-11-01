package com.example.ivyli.security.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Images")
public class ImagesTable extends Model {

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String mUid;

    @Column(name = "bitmap")
    private String mImage;


    private byte[] mImageByts;

    public ImagesTable() {
        super();
    }

    public ImagesTable(String id) {
        super();
        mUid = id;
    }

    public void setImageByts(byte[] byts) {
        mImageByts = byts;
    }

    public byte[] getImageByts() {
        return mImageByts;
    }

    public void setImageString(String encrytedImage) {
        mImage = encrytedImage;
    }

    public String getUid() {
        return mUid;
    }

    public String getImageString() {
        return mImage;
    }
}
