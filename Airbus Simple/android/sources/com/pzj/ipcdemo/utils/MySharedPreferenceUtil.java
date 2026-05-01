package com.pzj.ipcdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class MySharedPreferenceUtil {
    public static final String STR_CAMERA_SYSTEMFIRM = "system_firm";
    private static SharedPreferences prefer;

    public static void saveDeviceInformation(Context context, String str, String str2, String str3) {
        prefer = context.getSharedPreferences(str, 0);
        prefer.edit().putString(str2, str3).commit();
    }

    public static String getDeviceInformation(Context context, String str, String str2) {
        prefer = context.getSharedPreferences(str, 0);
        return prefer.getString(str2, "");
    }

    public static void saveSystemVer(Context context, String str, String str2) {
        prefer = context.getSharedPreferences(STR_CAMERA_SYSTEMFIRM, 0);
        prefer.edit().putString(str, str2).commit();
    }

    public static String getSystemVer(Context context, String str) {
        prefer = context.getSharedPreferences(STR_CAMERA_SYSTEMFIRM, 0);
        return prefer.getString(str, "0");
    }
}
