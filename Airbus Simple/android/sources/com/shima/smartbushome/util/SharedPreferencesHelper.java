package com.shima.smartbushome.util;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class SharedPreferencesHelper {
    private static final String FILE_NAME = "airbus_data";
    private static SharedPreferencesHelper instance;
    private static SharedPreferences mSharedPreferences;

    private SharedPreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, 0);
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesHelper(context);
        }
    }

    public static SharedPreferencesHelper getInstance() {
        if (instance == null) {
            throw new RuntimeException("class should init!");
        }
        return instance;
    }

    public void saveData(String str, Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
        if ("Integer".equals(simpleName)) {
            editorEdit.putInt(str, ((Integer) obj).intValue());
        } else if ("Boolean".equals(simpleName)) {
            editorEdit.putBoolean(str, ((Boolean) obj).booleanValue());
        } else if ("String".equals(simpleName)) {
            editorEdit.putString(str, (String) obj);
        } else if ("Float".equals(simpleName)) {
            editorEdit.putFloat(str, ((Float) obj).floatValue());
        } else if ("Long".equals(simpleName)) {
            editorEdit.putLong(str, ((Long) obj).longValue());
        }
        editorEdit.commit();
    }

    public Object getData(String str, Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        if ("Integer".equals(simpleName)) {
            return Integer.valueOf(mSharedPreferences.getInt(str, ((Integer) obj).intValue()));
        }
        if ("Boolean".equals(simpleName)) {
            return Boolean.valueOf(mSharedPreferences.getBoolean(str, ((Boolean) obj).booleanValue()));
        }
        if ("String".equals(simpleName)) {
            return mSharedPreferences.getString(str, (String) obj);
        }
        if ("Float".equals(simpleName)) {
            return Float.valueOf(mSharedPreferences.getFloat(str, ((Float) obj).floatValue()));
        }
        if ("Long".equals(simpleName)) {
            return Long.valueOf(mSharedPreferences.getLong(str, ((Long) obj).longValue()));
        }
        return null;
    }

    public void deleteData(String str) {
        try {
            SharedPreferences.Editor editorEdit = mSharedPreferences.edit();
            editorEdit.remove(str);
            editorEdit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
