package com.litesuits.http.utils;

import android.os.Build;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class StringCodingUtils {
    public static byte[] getBytes(String str, Charset charset) {
        if (Build.VERSION.SDK_INT < 9) {
            try {
                return str.getBytes(charset.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        return str.getBytes(charset);
    }
}
