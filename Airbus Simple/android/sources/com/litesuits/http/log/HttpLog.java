package com.litesuits.http.log;

import android.util.Log;

/* loaded from: classes.dex */
public final class HttpLog {
    public static final String NULL = "NULL";
    public static String defaultTag = "HttpLog";
    public static boolean isPrint = false;

    private HttpLog() {
    }

    public static void setTag(String str) {
        defaultTag = str;
    }

    public static int i(String str, Object obj) {
        if (!isPrint) {
            return -1;
        }
        if (obj == null) {
            Log.w(str, NULL);
            return -1;
        }
        Log.w(str, obj.toString());
        return -1;
    }

    public static int i(Object obj) {
        return i(defaultTag, obj);
    }

    public static int v(String str) {
        return v(defaultTag, str);
    }

    public static int d(String str) {
        return d(defaultTag, str);
    }

    public static int i(String str) {
        return i(defaultTag, str);
    }

    public static int w(String str) {
        return w(defaultTag, str);
    }

    public static int e(String str) {
        return e(defaultTag, str);
    }

    public static int v(String str, String str2) {
        if (!isPrint) {
            return -1;
        }
        if (str2 == null) {
            Log.v(str, NULL);
            return -1;
        }
        Log.v(str, str2);
        return -1;
    }

    public static int d(String str, String str2) {
        if (!isPrint) {
            return -1;
        }
        if (str2 == null) {
            Log.d(str, NULL);
            return -1;
        }
        Log.d(str, str2);
        return -1;
    }

    public static int i(String str, String str2) {
        if (!isPrint) {
            return -1;
        }
        if (str2 == null) {
            Log.i(str, NULL);
            return -1;
        }
        Log.i(str, str2);
        return -1;
    }

    public static int w(String str, String str2) {
        if (!isPrint) {
            return -1;
        }
        if (str2 == null) {
            Log.w(str, NULL);
            return -1;
        }
        Log.w(str, str2);
        return -1;
    }

    public static int e(String str, String str2) {
        if (!isPrint) {
            return -1;
        }
        if (str2 == null) {
            Log.e(str, NULL);
            return -1;
        }
        Log.e(str, str2);
        return -1;
    }

    public static int v(String str, Object... objArr) {
        if (isPrint) {
            return v(str, getLogMessage(objArr));
        }
        return -1;
    }

    public static int d(String str, Object... objArr) {
        if (isPrint) {
            return d(str, getLogMessage(objArr));
        }
        return -1;
    }

    public static int i(String str, Object... objArr) {
        if (isPrint) {
            return i(str, getLogMessage(objArr));
        }
        return -1;
    }

    public static int w(String str, Object... objArr) {
        if (isPrint) {
            return w(str, getLogMessage(objArr));
        }
        return -1;
    }

    public static int e(String str, Object... objArr) {
        if (isPrint) {
            return e(str, getLogMessage(objArr));
        }
        return -1;
    }

    private static String getLogMessage(Object... objArr) {
        if (objArr == null || objArr.length <= 0) {
            return NULL;
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : objArr) {
            sb.append(obj.toString());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static int v(String str, String str2, Throwable th) {
        if (isPrint) {
            return Log.v(str, str2, th);
        }
        return -1;
    }

    public static int d(String str, String str2, Throwable th) {
        if (isPrint) {
            return Log.d(str, str2, th);
        }
        return -1;
    }

    public static int i(String str, String str2, Throwable th) {
        if (isPrint) {
            return Log.i(str, str2, th);
        }
        return -1;
    }

    public static int w(String str, String str2, Throwable th) {
        if (isPrint) {
            return Log.w(str, str2, th);
        }
        return -1;
    }

    public static int e(String str, String str2, Throwable th) {
        if (isPrint) {
            return Log.e(str, str2, th);
        }
        return -1;
    }

    public static int v(Object obj, String str) {
        if (isPrint) {
            return v(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int d(Object obj, String str) {
        if (isPrint) {
            return d(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int i(Object obj, String str) {
        if (isPrint) {
            return i(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int w(Object obj, String str) {
        if (isPrint) {
            return w(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }

    public static int e(Object obj, String str) {
        if (isPrint) {
            return e(obj.getClass().getSimpleName(), str);
        }
        return -1;
    }
}
