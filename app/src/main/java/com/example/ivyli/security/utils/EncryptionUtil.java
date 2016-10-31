package com.example.ivyli.security.utils;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {
    private static final int KEY_LENGTH = 256;
    private static final int INTERATION_COUNT = 1000;

    public static String encryptImage(String password, String plaintext) {
        int saltLength = KEY_LENGTH / 8; // same size as key output
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);

        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                    INTERATION_COUNT, KEY_LENGTH);

            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");

            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            SecretKey key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];

            random.nextBytes(iv);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
            return ciphertext.toString();

        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException ike) {
        } catch (NoSuchPaddingException npd) {
        } catch (InvalidAlgorithmParameterException iape) {
        } catch (InvalidKeyException ikee) {
        } catch (UnsupportedEncodingException usee) {
        } catch (IllegalBlockSizeException ibse) {
        } catch (BadPaddingException bpd) {
        }

        return "";
    }


    public static String decrypt(String password, String ciphertext) {
        String[] fields = ciphertext.split("]");
        byte[] salt = Base64.decode(fields[0], Base64.DEFAULT);
        byte[] iv = Base64.decode(fields[1], Base64.DEFAULT);
        byte[] cipherBytes = Base64.decode((fields[2]), Base64.DEFAULT);

        try {
            SecretKey key = deriveKeyPbkdf2(salt, password);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            byte[] plaintext = cipher.doFinal(cipherBytes);
            String plainrStr = new String(plaintext, "UTF-8");
            return plainrStr;

        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchPaddingException npd) {
        } catch (InvalidAlgorithmParameterException iape) {
        } catch (InvalidKeyException ikee) {
        } catch (UnsupportedEncodingException usee) {
        } catch (IllegalBlockSizeException ibse) {
        } catch (BadPaddingException bpd) {
        }
        return null;
    }

    private static SecretKey deriveKeyPbkdf2(byte[] salt, String password) {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                    INTERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            SecretKey key = new SecretKeySpec(keyBytes, "AES");
            return key;

        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeySpecException idk) {
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
