package com.mediatek.demo.smartconnection;

/* loaded from: classes.dex */
public class JniLoader {
    public static final int ERROR_CODE_ALREADY_STARTED = -7;
    public static final int ERROR_CODE_ALREADY_STOPED = -8;
    public static final int ERROR_CODE_CRYPTED_FAILED = -4;
    public static final int ERROR_CODE_EXCEED_MAX_LENGTH = -6;
    public static final int ERROR_CODE_FAILED_TO_START = -9;
    public static final int ERROR_CODE_NOT_ENOUGH_MEMORY = -5;
    public static final int ERROR_CODE_NOT_INITINED = -1;
    public static final int ERROR_CODE_OK = 0;
    public static final int ERROR_CODE_WRONG_PARAMETER = -3;
    public static final int ERROR_CODE_WRONG_TYPE = -2;

    public native int GetLibVersion();

    public native int GetProtoVersion();

    public native int InitSmartConnection(String str, String str2, int i, int i2, int i3);

    public native int SetSendInterval(float f, float f2);

    public native int StartSmartConnection(String str, String str2, String str3);

    public native int StopSmartConnection();

    public static boolean LoadLib() {
        try {
            System.loadLibrary("smart_connection_jni");
            return true;
        } catch (Exception unused) {
            System.err.println("WARNING: Could not load library");
            return false;
        }
    }
}
