package com.example.ivyli.security.utils;


import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class EncryptionUtil {

    //this is bad. But am not sure where I should store the key to do the password encryption.
    public static final String encryption_key = "$fkhIu2OP_d";

    public static String encrypt(String plainText, String pin) {
        try {
            DESKeySpec keySpec = new DESKeySpec(pin.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] cleartext = plainText.getBytes("UTF8");

            Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
            cipher.init(Cipher.ENCRYPT_MODE, key);
            String encryptedPwd = Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);
            return encryptedPwd;
        } catch (Exception e) {
            Log.e("", e.getLocalizedMessage());
        }

        return null;
    }

    public static String decrypt(String encryptedText, String pin) {
        try {
            byte[] encrypedPwdBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            DESKeySpec keySpec = new DESKeySpec(pin.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
            return new String(plainTextPwdBytes, "UTF8");

        } catch (Exception e) {
            Log.e("", e.getLocalizedMessage());
        }

        return null;

    }



    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
