package com.example.ivyli.security.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageUtil {
    public static Bitmap getImageBitmap(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }
}
