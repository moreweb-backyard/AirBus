package com.litesuits.http.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class MD5Util {
    private static final int STREAM_BUFFER_LENGTH = 1024;
    private static final String TAG = "MD5Util";

    public static MessageDigest getDigest(String str) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(str);
    }

    public static byte[] md5(String str) {
        return md5(str.getBytes());
    }

    public static byte[] md5(byte[] bArr) {
        try {
            MessageDigest digest = getDigest("MD5");
            digest.update(bArr);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] md5(InputStream inputStream) throws NoSuchAlgorithmException, IOException {
        return updateDigest(getDigest("MD5"), inputStream).digest();
    }

    public static MessageDigest updateDigest(MessageDigest messageDigest, InputStream inputStream) throws IOException {
        byte[] bArr = new byte[1024];
        int i = inputStream.read(bArr, 0, 1024);
        while (i > -1) {
            messageDigest.update(bArr, 0, i);
            i = inputStream.read(bArr, 0, 1024);
        }
        return messageDigest;
    }
}
