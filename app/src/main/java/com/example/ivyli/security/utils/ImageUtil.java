package com.example.ivyli.security.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;


public class ImageUtil {
    public static Bitmap getImageBitmap(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    public static String getImageByteString(Bitmap bitmap){
        int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
        bitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

        return buffer.toString();
    }
}
