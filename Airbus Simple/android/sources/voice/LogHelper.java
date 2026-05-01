package voice;

import android.util.Log;

/* loaded from: classes.dex */
public class LogHelper {
    private static final String ROOT_TAG = "logger";
    private static MyLogger logger = new MyNullLogger();

    interface MyLogger {
        int d(String str, String str2);

        int e(String str, String str2);

        int i(String str, String str2);

        int v(String str, String str2);
    }

    public static class MyNullLogger implements MyLogger {
        @Override // voice.LogHelper.MyLogger
        public int d(String str, String str2) {
            return 0;
        }

        @Override // voice.LogHelper.MyLogger
        public int e(String str, String str2) {
            return 0;
        }

        @Override // voice.LogHelper.MyLogger
        public int i(String str, String str2) {
            return 0;
        }

        @Override // voice.LogHelper.MyLogger
        public int v(String str, String str2) {
            return 0;
        }
    }

    static class MyAndroidLogger implements MyLogger {
        MyAndroidLogger() {
        }

        @Override // voice.LogHelper.MyLogger
        public int d(String str, String str2) {
            return Log.d(str, str2);
        }

        @Override // voice.LogHelper.MyLogger
        public int i(String str, String str2) {
            return Log.i(str, str2);
        }

        @Override // voice.LogHelper.MyLogger
        public int e(String str, String str2) {
            return Log.e(str, str2);
        }

        @Override // voice.LogHelper.MyLogger
        public int v(String str, String str2) {
            return Log.v(str, str2);
        }
    }

    public static class MyConsoleLogger implements MyLogger {
        @Override // voice.LogHelper.MyLogger
        public int d(String str, String str2) {
            System.out.println(str + " debug:" + str2);
            return 0;
        }

        @Override // voice.LogHelper.MyLogger
        public int i(String str, String str2) {
            System.out.println(str + " info:" + str2);
            return 0;
        }

        @Override // voice.LogHelper.MyLogger
        public int e(String str, String str2) {
            System.out.println(str + " error:" + str2);
            return 0;
        }

        @Override // voice.LogHelper.MyLogger
        public int v(String str, String str2) {
            System.out.println(str + " vir:" + str2);
            return 0;
        }
    }

    public static void setLogger(MyLogger myLogger) {
        logger = myLogger;
    }

    public static final int d(String str, String str2, String str3) {
        return logger.d(String.format("%s %s %s", ROOT_TAG, str, str2), str3);
    }

    public static final int d(String str, String str2) {
        return d(str, "", str2);
    }

    public static final int i(String str, String str2, String str3) {
        return logger.i(String.format("%s %s %s", ROOT_TAG, str, str2), str3);
    }

    public static final int i(String str, String str2) {
        return i(str, "", str2);
    }

    public static final int e(String str, String str2, String str3) {
        return logger.e(String.format("%s %s %s", ROOT_TAG, str, str2), str3);
    }

    public static final int e(String str, String str2) {
        return e(str, "", str2);
    }

    public static final int v(String str, String str2, String str3) {
        return logger.v(String.format("%s %s %s", ROOT_TAG, str, str2), str3);
    }

    public static final int v(String str, String str2) {
        return v(str, "", str2);
    }
}
