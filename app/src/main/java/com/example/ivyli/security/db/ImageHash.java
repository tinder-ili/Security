package com.example.ivyli.security.db;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ImagesHash")
public class ImageHash extends Model {
    @Column(name = "photo_uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String mUid;

    @Column(name = "hash")
    private String mHash;

    public ImageHash() {
        super();
    }

    public ImageHash(String uid, String hash) {
        super();

        mUid = uid;
        mHash = hash;
    }

    public String getUid(){
        return mUid;
    }

    public String getImageHash(){
        return mHash;
    }
}
