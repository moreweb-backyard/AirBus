package com.tencent.bugly.crashreport.crash.jni;

import android.content.Context;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.v;
import com.tencent.bugly.proguard.w;
import java.io.File;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class NativeCrashHandler {
    private static NativeCrashHandler a = null;
    private static boolean l = false;
    private static boolean m = false;
    private final Context b;
    private final com.tencent.bugly.crashreport.common.info.a c;
    private final v d;
    private NativeExceptionHandler e;
    private String f;
    private final boolean g;
    private boolean h = false;
    private boolean i = false;
    private boolean j = false;
    private boolean k = false;
    private com.tencent.bugly.crashreport.crash.b n;

    protected native boolean appendNativeLog(String str, String str2, String str3);

    protected native boolean appendWholeNativeLog(String str);

    protected native String getNativeKeyValueList();

    protected native String getNativeLog();

    protected native boolean putNativeKeyValue(String str, String str2);

    protected native String regist(String str, boolean z, int i);

    protected native String removeNativeKeyValue(String str);

    protected native void setNativeInfo(int i, String str);

    protected native void testCrash();

    protected native String unregist();

    /* JADX WARN: Removed duplicated region for block: B:13:0x0026  */
    @android.annotation.SuppressLint({"SdCardPath"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private NativeCrashHandler(android.content.Context r3, com.tencent.bugly.crashreport.common.info.a r4, com.tencent.bugly.crashreport.crash.b r5, com.tencent.bugly.proguard.v r6, boolean r7, java.lang.String r8) {
        /*
            r2 = this;
            r2.<init>()
            r0 = 0
            r2.h = r0
            r2.i = r0
            r2.j = r0
            r2.k = r0
            if (r3 != 0) goto Lf
            goto L15
        Lf:
            android.content.Context r1 = r3.getApplicationContext()
            if (r1 != 0) goto L16
        L15:
            r1 = r3
        L16:
            r2.b = r1
            if (r8 == 0) goto L26
            java.lang.String r1 = r8.trim()     // Catch: java.lang.Throwable -> L35
            int r1 = r1.length()     // Catch: java.lang.Throwable -> L35
            if (r1 <= 0) goto L26
            r1 = 0
            goto L27
        L26:
            r1 = 1
        L27:
            if (r1 != 0) goto L2a
            goto L4e
        L2a:
            java.lang.String r8 = "bugly"
            java.io.File r8 = r3.getDir(r8, r0)     // Catch: java.lang.Throwable -> L35
            java.lang.String r8 = r8.getAbsolutePath()     // Catch: java.lang.Throwable -> L35
            goto L4e
        L35:
            com.tencent.bugly.crashreport.common.info.a r3 = com.tencent.bugly.crashreport.common.info.a.a(r3)
            java.lang.String r3 = r3.c
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r0 = "/data/data/"
            r8.<init>(r0)
            r8.append(r3)
            java.lang.String r3 = "/app_bugly"
            r8.append(r3)
            java.lang.String r8 = r8.toString()
        L4e:
            r2.n = r5
            r2.f = r8
            r2.c = r4
            r2.d = r6
            r2.g = r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.<init>(android.content.Context, com.tencent.bugly.crashreport.common.info.a, com.tencent.bugly.crashreport.crash.b, com.tencent.bugly.proguard.v, boolean, java.lang.String):void");
    }

    public static synchronized NativeCrashHandler getInstance(Context context, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.crash.b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar2, v vVar, boolean z, String str) {
        if (a == null) {
            a = new NativeCrashHandler(context, aVar, bVar, vVar, z, str);
        }
        return a;
    }

    public static synchronized NativeCrashHandler getInstance() {
        return a;
    }

    public synchronized String getDumpFilePath() {
        return this.f;
    }

    public synchronized void setDumpFilePath(String str) {
        this.f = str;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:13|(1:15)(14:17|(1:19)|62|21|(1:23)|24|(1:26)|27|(1:29)(1:30)|31|(1:33)(1:34)|35|36|37)|16|62|21|(0)|24|(0)|27|(0)(0)|31|(0)(0)|35|36|37) */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0093 A[Catch: Throwable -> 0x00a1, all -> 0x0199, TryCatch #1 {Throwable -> 0x00a1, blocks: (B:21:0x0089, B:23:0x0093, B:24:0x0095, B:26:0x009f), top: B:62:0x0089 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x009f A[Catch: Throwable -> 0x00a1, all -> 0x0199, TRY_LEAVE, TryCatch #1 {Throwable -> 0x00a1, blocks: (B:21:0x0089, B:23:0x0093, B:24:0x0095, B:26:0x009f), top: B:62:0x0089 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00a5 A[Catch: Throwable -> 0x00cf, all -> 0x0199, TryCatch #0 {Throwable -> 0x00cf, blocks: (B:11:0x0029, B:13:0x0031, B:15:0x0063, B:16:0x0070, B:27:0x00a1, B:29:0x00a5, B:31:0x00b4, B:33:0x00b8, B:35:0x00c7, B:34:0x00c0, B:30:0x00ad, B:17:0x0075, B:19:0x007b), top: B:60:0x0029, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00ad A[Catch: Throwable -> 0x00cf, all -> 0x0199, TryCatch #0 {Throwable -> 0x00cf, blocks: (B:11:0x0029, B:13:0x0031, B:15:0x0063, B:16:0x0070, B:27:0x00a1, B:29:0x00a5, B:31:0x00b4, B:33:0x00b8, B:35:0x00c7, B:34:0x00c0, B:30:0x00ad, B:17:0x0075, B:19:0x007b), top: B:60:0x0029, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00b8 A[Catch: Throwable -> 0x00cf, all -> 0x0199, TryCatch #0 {Throwable -> 0x00cf, blocks: (B:11:0x0029, B:13:0x0031, B:15:0x0063, B:16:0x0070, B:27:0x00a1, B:29:0x00a5, B:31:0x00b4, B:33:0x00b8, B:35:0x00c7, B:34:0x00c0, B:30:0x00ad, B:17:0x0075, B:19:0x007b), top: B:60:0x0029, outer: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c0 A[Catch: Throwable -> 0x00cf, all -> 0x0199, TryCatch #0 {Throwable -> 0x00cf, blocks: (B:11:0x0029, B:13:0x0031, B:15:0x0063, B:16:0x0070, B:27:0x00a1, B:29:0x00a5, B:31:0x00b4, B:33:0x00b8, B:35:0x00c7, B:34:0x00c0, B:30:0x00ad, B:17:0x0075, B:19:0x007b), top: B:60:0x0029, outer: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized void a(boolean r10) {
        /*
            Method dump skipped, instructions count: 412
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.a(boolean):void");
    }

    public synchronized void startNativeMonitor() {
        if (!this.i && !this.h) {
            String str = this.c.k;
            boolean z = false;
            if (!(str == null || str.trim().length() <= 0)) {
                String str2 = this.c.k;
            }
            String str3 = this.c.k;
            String str4 = str3 == null || str3.trim().length() <= 0 ? "Bugly" : this.c.k;
            String str5 = this.c.k;
            if (str5 == null || str5.trim().length() <= 0) {
                z = true;
            }
            this.i = a(str4, !z);
            if (this.i || this.h) {
                a(this.g);
                this.d.b(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        if (com.tencent.bugly.proguard.a.a(NativeCrashHandler.this.b, "native_record_lock", 10000L)) {
                            NativeCrashHandler.this.setNativeAppVersion(NativeCrashHandler.this.c.i);
                            NativeCrashHandler.this.setNativeUserId(NativeCrashHandler.this.c.f());
                            CrashDetailBean crashDetailBeanA = b.a(NativeCrashHandler.this.b, NativeCrashHandler.this.f, NativeCrashHandler.this.e);
                            if (crashDetailBeanA != null) {
                                if (!NativeCrashHandler.this.n.a(crashDetailBeanA)) {
                                    NativeCrashHandler.this.n.a(crashDetailBeanA, 3000L, false);
                                }
                                b.b(NativeCrashHandler.this.f);
                                w.a("get crash from native record!", new Object[0]);
                            }
                            NativeCrashHandler.this.a();
                            com.tencent.bugly.proguard.a.b(NativeCrashHandler.this.b, "native_record_lock");
                            return;
                        }
                        w.a("Failed to lock file for handling native crash record.", new Object[0]);
                    }
                });
                return;
            }
            return;
        }
        a(this.g);
    }

    private static boolean a(String str, boolean z) {
        boolean z2;
        try {
            w.a("[native] trying to load so: %s", str);
            if (z) {
                System.load(str);
            } else {
                System.loadLibrary(str);
            }
        } catch (Throwable th) {
            th = th;
            z2 = false;
        }
        try {
            w.a("[native] load so success: %s", str);
            return true;
        } catch (Throwable th2) {
            th = th2;
            z2 = true;
            w.d(th.getMessage(), new Object[0]);
            w.d("[native] Failed to load so, please check.", str);
            return z2;
        }
    }

    private synchronized void b() {
        if (!this.j) {
            w.d("native already unregisted!", new Object[0]);
            return;
        }
        try {
        } catch (Throwable unused) {
            w.c("unregist bugly so fail", new Object[0]);
        }
        if (unregist() != null) {
            w.a("Native Crash Report close!", new Object[0]);
            this.j = false;
            return;
        }
        w.c("unregist bugly so success", new Object[0]);
        try {
            com.tencent.bugly.proguard.a.a("com.tencent.feedback.eup.jni.NativeExceptionUpload", "enableHandler", null, new Class[]{Boolean.TYPE}, new Object[]{false});
            this.j = false;
            w.c("unregist rqd so success", new Object[0]);
        } catch (Throwable unused2) {
            w.c("unregist rqd so fail", new Object[0]);
            this.i = false;
            this.h = false;
        }
    }

    public void testNativeCrash() {
        if (!this.i) {
            w.d("libBugly.so has not been load! so fail!", new Object[0]);
        } else {
            testCrash();
        }
    }

    public NativeExceptionHandler getNativeExceptionHandler() {
        return this.e;
    }

    protected final void a() {
        File[] fileArrListFiles;
        int iIndexOf;
        long jC = com.tencent.bugly.proguard.a.c() - c.f;
        File file = new File(this.f);
        if (!file.exists() || !file.isDirectory() || (fileArrListFiles = file.listFiles()) == null || fileArrListFiles.length == 0) {
            return;
        }
        int length = "tomb_".length();
        int i = 0;
        for (File file2 : fileArrListFiles) {
            String name = file2.getName();
            if (name.startsWith("tomb_")) {
                try {
                    iIndexOf = name.indexOf(".txt");
                } catch (Throwable unused) {
                    w.e("tomb format error delete %s", name);
                }
                if (iIndexOf <= 0 || Long.parseLong(name.substring(length, iIndexOf)) < jC) {
                    if (file2.delete()) {
                        i++;
                    }
                }
            }
        }
        w.c("clean tombs %d", Integer.valueOf(i));
    }

    private synchronized void b(boolean z) {
        if (z) {
            startNativeMonitor();
        } else {
            b();
        }
    }

    public synchronized boolean isUserOpened() {
        return this.k;
    }

    private synchronized void c(boolean z) {
        if (this.k != z) {
            w.a("user change native %b", Boolean.valueOf(z));
            this.k = z;
        }
    }

    public void setUserOpened(boolean z) {
        c(z);
        boolean z2 = com.tencent.bugly.crashreport.common.strategy.a.a().c().d && isUserOpened();
        if (z2 != this.j) {
            w.a("native changed to %b", Boolean.valueOf(z2));
            b(z2);
        }
    }

    public synchronized void onStrategyChanged(StrategyBean strategyBean) {
        if (strategyBean != null) {
            try {
                if (strategyBean.d != this.j) {
                    w.d("server native changed to %b", Boolean.valueOf(strategyBean.d));
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        boolean z = com.tencent.bugly.crashreport.common.strategy.a.a().c().d && this.k;
        if (z != this.j) {
            w.a("native changed to %b", Boolean.valueOf(z));
            b(z);
        }
    }

    public boolean appendLogToNative(String str, String str2, String str3) {
        if (!this.i || !l || str == null || str2 == null || str3 == null) {
            return false;
        }
        try {
            return appendNativeLog(str, str2, str3);
        } catch (UnsatisfiedLinkError unused) {
            l = false;
            return false;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    public boolean putKeyValueToNative(String str, String str2) {
        if (!this.i || !l || str == null || str2 == null) {
            return false;
        }
        try {
            return putNativeKeyValue(str, str2);
        } catch (UnsatisfiedLinkError unused) {
            l = false;
            return false;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    private boolean a(int i, String str) {
        if (!this.i || !m) {
            return false;
        }
        try {
            setNativeInfo(i, str);
            return true;
        } catch (UnsatisfiedLinkError unused) {
            m = false;
            return false;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    public boolean setNativeAppVersion(String str) {
        return a(10, str);
    }

    public boolean setNativeUserId(String str) {
        return a(11, str);
    }
}
