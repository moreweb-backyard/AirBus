package com.tencent.bugly.crashreport;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v4.os.EnvironmentCompat;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.litesuits.http.data.Consts;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.CrashModule;
import com.tencent.bugly.b;
import com.tencent.bugly.crashreport.common.info.a;
import com.tencent.bugly.crashreport.crash.BuglyBroadcastRecevier;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.crashreport.crash.h5.H5JavaScriptInterface;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.v;
import com.tencent.bugly.proguard.w;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class CrashReport {
    private static Context a;

    /* compiled from: BUGLY */
    public static class CrashHandleCallback extends BuglyStrategy.a {
    }

    public static void enableBugly(boolean z) {
        b.a = z;
    }

    public static synchronized void initCrashReport(Context context) {
        a = context;
        b.a(CrashModule.getInstance());
        b.a(context);
    }

    public static synchronized void initCrashReport(Context context, UserStrategy userStrategy) {
        a = context;
        b.a(CrashModule.getInstance());
        b.a(context, userStrategy);
    }

    public static synchronized void initCrashReport(Context context, String str, boolean z) {
        initCrashReport(context, str, z, null);
    }

    public static synchronized void initCrashReport(Context context, String str, boolean z, UserStrategy userStrategy) {
        if (context == null) {
            return;
        }
        a = context;
        b.a(CrashModule.getInstance());
        b.a(context, str, z, userStrategy);
    }

    public static String getBuglyVersion(Context context) {
        if (context == null) {
            w.d("Please call with context.", new Object[0]);
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        a.a(context);
        return a.b();
    }

    public static synchronized void testJavaCrash() {
        if (!b.a) {
            Log.w(w.a, "Can not test Java crash because bugly is disable.");
        } else {
            if (!CrashModule.hasInitialized()) {
                Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
                return;
            }
            throw new RuntimeException("This Crash create for Test! You can go to Bugly see more detail!");
        }
    }

    public static synchronized void testNativeCrash() {
        if (!b.a) {
            Log.w(w.a, "Can not test native crash because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            w.a("start to create a native crash for test!", new Object[0]);
            c.a().k();
        }
    }

    public static synchronized void testANRCrash() {
        if (!b.a) {
            Log.w(w.a, "Can not test ANR crash because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            w.a("start to create a anr crash for test!", new Object[0]);
            c.a().l();
        }
    }

    public static synchronized void testBlockCrash() {
        if (!b.a) {
            Log.w(w.a, "Can not test block crash because bugly is disable.");
        } else {
            if (!CrashModule.hasInitialized()) {
                throw new BuglyHintException("Report has not been initialed! pls to call method 'initCrashReport' first!");
            }
            w.a("start to create a anr crash for test!", new Object[0]);
            c.a().m();
        }
    }

    public static synchronized void postCatchedException(Throwable th) {
        postCatchedException(th, false);
    }

    public static synchronized void postCatchedException(Throwable th, boolean z) {
        if (!b.a) {
            Log.w(w.a, "Can not post crash caught because bugly is disable.");
            return;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return;
        }
        if (th == null) {
            w.d("throwable is null, just return", new Object[0]);
            return;
        }
        c.a().a(Thread.currentThread(), th, false, null, null);
        if (z) {
            w.a("clear user datas", new Object[0]);
            a.a(a).z();
        }
    }

    public static synchronized void closeNativeReport() {
        if (!b.a) {
            Log.w(w.a, "Can not close native report because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            c.a().f();
        }
    }

    public static synchronized void startCrashReport() {
        if (!b.a) {
            Log.w(w.a, "Can not start crash report because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.w(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            c.a().c();
        }
    }

    public static synchronized void closeCrashReport() {
        if (!b.a) {
            Log.w(w.a, "Can not close crash report because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.w(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            c.a().d();
        }
    }

    public static void closeBugly() {
        if (!b.a) {
            Log.w(w.a, "Can not close bugly because bugly is disable.");
            return;
        }
        if (!CrashModule.hasInitialized()) {
            Log.w(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return;
        }
        if (a == null) {
            return;
        }
        BuglyBroadcastRecevier buglyBroadcastRecevier = BuglyBroadcastRecevier.getInstance();
        if (buglyBroadcastRecevier != null) {
            buglyBroadcastRecevier.unregist(a);
        }
        closeCrashReport();
        com.tencent.bugly.crashreport.biz.b.a(a);
        v vVarA = v.a();
        if (vVarA != null) {
            vVarA.b();
        }
    }

    public static void setUserSceneTag(Context context, int i) {
        if (!b.a) {
            Log.w(w.a, "Can not set tag caught because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.e(w.a, "setTag args context should not be null");
            return;
        }
        if (i <= 0) {
            w.d("setTag args tagId should > 0", new Object[0]);
        }
        a.a(context).a(i);
        w.b("[param] set user scene tag: %d", Integer.valueOf(i));
    }

    public static int getUserSceneTagId(Context context) {
        if (!b.a) {
            Log.w(w.a, "Can not get user scene tag because bugly is disable.");
            return -1;
        }
        if (context == null) {
            Log.e(w.a, "getUserSceneTagId args context should not be null");
            return -1;
        }
        return a.a(context).D();
    }

    public static String getUserData(Context context, String str) {
        if (!b.a) {
            Log.w(w.a, "Can not get user data because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (context == null) {
            Log.e(w.a, "getUserDataValue args context should not be null");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (str == null || str.trim().length() <= 0) {
            return null;
        }
        return a.a(context).g(str);
    }

    public static void putUserData(Context context, String str, String str2) {
        if (!b.a) {
            Log.w(w.a, "Can not put user data because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(w.a, "putUserData args context should not be null");
            return;
        }
        if (str == null) {
            String str3 = str;
            w.d("putUserData args key should not be null or empty", new Object[0]);
            return;
        }
        if (str2 == null) {
            String str4 = str2;
            w.d("putUserData args value should not be null", new Object[0]);
            return;
        }
        if (!str.matches("[a-zA-Z[0-9]]+")) {
            w.d("putUserData args key should match [a-zA-Z[0-9]]+  {" + str + Consts.KV_ECLOSING_RIGHT, new Object[0]);
            return;
        }
        if (str2.length() > 200) {
            w.d("user data value length over limit %d, it will be cutted!", 200);
            str2 = str2.substring(0, 200);
        }
        a aVarA = a.a(context);
        if (aVarA.B().contains(str)) {
            NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
            if (nativeCrashHandler != null) {
                nativeCrashHandler.putKeyValueToNative(str, str2);
            }
            a.a(context).b(str, str2);
            w.c("replace KV %s %s", str, str2);
            return;
        }
        if (aVarA.A() >= 10) {
            w.d("user data size is over limit %d, it will be cutted!", 10);
            return;
        }
        if (str.length() > 50) {
            w.d("user data key length over limit %d , will drop this new key %s", 50, str);
            str = str.substring(0, 50);
        }
        NativeCrashHandler nativeCrashHandler2 = NativeCrashHandler.getInstance();
        if (nativeCrashHandler2 != null) {
            nativeCrashHandler2.putKeyValueToNative(str, str2);
        }
        a.a(context).b(str, str2);
        w.b("[param] set user data: %s - %s", str, str2);
    }

    public static String removeUserData(Context context, String str) {
        if (!b.a) {
            Log.w(w.a, "Can not remove user data because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (context == null) {
            Log.e(w.a, "removeUserData args context should not be null");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (str == null || str.trim().length() <= 0) {
            return null;
        }
        w.b("[param] remove user data: %s", str);
        return a.a(context).f(str);
    }

    public static Set<String> getAllUserDataKeys(Context context) {
        if (!b.a) {
            Log.w(w.a, "Can not get all keys of user data because bugly is disable.");
            return new HashSet();
        }
        if (context == null) {
            Log.e(w.a, "getAllUserDataKeys args context should not be null");
            return new HashSet();
        }
        return a.a(context).B();
    }

    public static int getUserDatasSize(Context context) {
        if (!b.a) {
            Log.w(w.a, "Can not get size of user data because bugly is disable.");
            return -1;
        }
        if (context == null) {
            Log.e(w.a, "getUserDatasSize args context should not be null");
            return -1;
        }
        return a.a(context).A();
    }

    public static synchronized String getAppID() {
        if (!b.a) {
            Log.w(w.a, "Can not get App ID because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return a.a(a).e();
    }

    public static void setUserId(String str) {
        if (!b.a) {
            Log.w(w.a, "Can not set user ID because bugly is disable.");
        } else if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
        } else {
            setUserId(a, str);
        }
    }

    public static void setUserId(Context context, String str) {
        if (!b.a) {
            Log.w(w.a, "Can not set user ID because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.e(w.a, "Context should not be null when bugly has not been initialed!");
            return;
        }
        if (str == null) {
            w.d("userId should not be null", new Object[0]);
            return;
        }
        if (str.length() > 100) {
            String strSubstring = str.substring(0, 100);
            w.d("userId %s length is over limit %d substring to %s", str, 100, strSubstring);
            str = strSubstring;
        }
        if (str.equals(a.a(context).f())) {
            return;
        }
        a.a(context).b(str);
        w.b("[user] set userId : %s", str);
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeUserId(str);
        }
        if (CrashModule.hasInitialized()) {
            com.tencent.bugly.crashreport.biz.b.a();
        }
    }

    public static synchronized String getUserId() {
        if (!b.a) {
            Log.w(w.a, "Can not get user ID because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return a.a(a).f();
    }

    public static synchronized String getAppVer() {
        if (!b.a) {
            Log.w(w.a, "Can not get app version because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return a.a(a).i;
    }

    public static synchronized String getAppChannel() {
        if (!b.a) {
            Log.w(w.a, "Can not get App channel because bugly is disable.");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
        return a.a(a).j;
    }

    public static synchronized void setContext(Context context) {
        a = context;
    }

    public static synchronized boolean isLastSessionCrash() {
        if (!b.a) {
            Log.w(w.a, "The info 'isLastSessionCrash' is not accurate because bugly is disable.");
            return false;
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return false;
        }
        return c.a().b();
    }

    public static synchronized void setSdkExtraData(Context context, String str, String str2) {
        if (!b.a) {
            Log.w(w.a, "Can not put SDK extra data because bugly is disable.");
            return;
        }
        if (context != null) {
            boolean z = false;
            if (!(str == null || str.trim().length() <= 0)) {
                if (str2 == null || str2.trim().length() <= 0) {
                    z = true;
                }
                if (!z) {
                    a.a(context).a(str, str2);
                }
            }
        }
    }

    public static Map<String, String> getSdkExtraData() {
        if (!b.a) {
            Log.w(w.a, "Can not get SDK extra data because bugly is disable.");
            return new HashMap();
        }
        if (!CrashModule.hasInitialized()) {
            Log.e(w.a, "CrashReport has not been initialed! pls to call method 'initCrashReport' first!");
            return null;
        }
        return a.a(a).z;
    }

    public static Map<String, String> getSdkExtraData(Context context) {
        if (!b.a) {
            Log.w(w.a, "Can not get SDK extra data because bugly is disable.");
            return new HashMap();
        }
        if (context == null) {
            w.d("Context should not be null.", new Object[0]);
            return null;
        }
        return a.a(context).z;
    }

    private static void putSdkData(Context context, String str, String str2) {
        if (context != null) {
            if (str == null || str.trim().length() <= 0) {
                return;
            }
            if (str2 == null || str2.trim().length() <= 0) {
                return;
            }
            String strReplace = str.replace("[a-zA-Z[0-9]]+", "");
            if (strReplace.length() > 100) {
                Log.w(w.a, String.format("putSdkData key length over limit %d, will be cutted.", 50));
                strReplace = strReplace.substring(0, 50);
            }
            if (str2.length() > 500) {
                Log.w(w.a, String.format("putSdkData value length over limit %d, will be cutted!", 200));
                str2 = str2.substring(0, 200);
            }
            a.a(context).c(strReplace, str2);
            w.b(String.format("[param] putSdkData data: %s - %s", strReplace, str2), new Object[0]);
        }
    }

    public static void setIsAppForeground(Context context, boolean z) {
        if (!b.a) {
            Log.w(w.a, "Can not set 'isAppForeground' because bugly is disable.");
            return;
        }
        if (context == null) {
            w.d("Context should not be null.", new Object[0]);
            return;
        }
        if (z) {
            w.c("App is in foreground.", new Object[0]);
        } else {
            w.c("App is in background.", new Object[0]);
        }
        a.a(context).n = z;
    }

    public static void setIsDevelopmentDevice(Context context, boolean z) {
        if (!b.a) {
            Log.w(w.a, "Can not set 'isDevelopmentDevice' because bugly is disable.");
            return;
        }
        if (context == null) {
            w.d("Context should not be null.", new Object[0]);
            return;
        }
        if (z) {
            w.c("This is a development device.", new Object[0]);
        } else {
            w.c("This is not a development device.", new Object[0]);
        }
        a.a(context).x = z;
    }

    public static void setSessionIntervalMills(long j) {
        if (!b.a) {
            Log.w(w.a, "Can not set 'SessionIntervalMills' because bugly is disable.");
        } else {
            com.tencent.bugly.crashreport.biz.b.a(j);
        }
    }

    public static void setAppVersion(Context context, String str) {
        if (!b.a) {
            Log.w(w.a, "Can not set APP version because bugly is disable.");
            return;
        }
        if (context == null) {
            Log.w(w.a, "setAppVersion args context should not be null");
            return;
        }
        if (str == null) {
            Log.w(w.a, "Version is null, will not set");
            return;
        }
        a.a(context).i = str;
        NativeCrashHandler nativeCrashHandler = NativeCrashHandler.getInstance();
        if (nativeCrashHandler != null) {
            nativeCrashHandler.setNativeAppVersion(str);
        }
    }

    public static boolean setJavascriptMonitor(WebView webView, boolean z) {
        return setJavascriptMonitor(webView, z, false);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public static boolean setJavascriptMonitor(WebView webView, boolean z, boolean z2) {
        if (webView == null) {
            Log.w(w.a, "Webview is null.");
            return false;
        }
        if (!CrashModule.hasInitialized()) {
            w.e("CrashReport has not been initialed! please to call method 'initCrashReport' first!", new Object[0]);
            return false;
        }
        w.a("Set Javascript exception monitor of webview.", new Object[0]);
        if (!b.a) {
            Log.w(w.a, "Can not set JavaScript monitor because bugly is disable.");
            return false;
        }
        w.c("URL of webview is %s", webView.getUrl());
        if (webView.getUrl() == null) {
            return false;
        }
        if (!z2 && Build.VERSION.SDK_INT < 19) {
            w.e("This interface is only available for Android 4.4 or later.", new Object[0]);
            return false;
        }
        WebSettings settings = webView.getSettings();
        if (!settings.getJavaScriptEnabled()) {
            w.a("Enable the javascript needed by webview monitor.", new Object[0]);
            settings.setJavaScriptEnabled(true);
        }
        H5JavaScriptInterface h5JavaScriptInterface = H5JavaScriptInterface.getInstance(webView);
        if (h5JavaScriptInterface != null) {
            w.a("Add a secure javascript interface to the webview.", new Object[0]);
            webView.addJavascriptInterface(h5JavaScriptInterface, "exceptionUploader");
        }
        if (z) {
            w.a("Inject bugly.js(v%s) to the webview.", com.tencent.bugly.crashreport.crash.h5.c.b());
            String strA = com.tencent.bugly.crashreport.crash.h5.c.a();
            if (strA == null) {
                w.e("Failed to inject Bugly.js.", com.tencent.bugly.crashreport.crash.h5.c.b());
                return false;
            }
            webView.loadUrl("javascript:" + strA);
        }
        return true;
    }

    /* compiled from: BUGLY */
    public static class UserStrategy extends BuglyStrategy {
        private CrashHandleCallback a;

        public UserStrategy(Context context) {
        }

        @Override // com.tencent.bugly.BuglyStrategy
        public synchronized CrashHandleCallback getCrashHandleCallback() {
            return this.a;
        }

        public synchronized void setCrashHandleCallback(CrashHandleCallback crashHandleCallback) {
            this.a = crashHandleCallback;
        }
    }
}
