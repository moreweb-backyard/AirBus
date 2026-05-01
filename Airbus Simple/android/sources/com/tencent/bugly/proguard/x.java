package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class x {
    public static boolean a = true;
    private static SimpleDateFormat b = null;
    private static int c = 5120;
    private static StringBuilder d;
    private static StringBuilder e;
    private static a f;
    private static String g;
    private static String h;
    private static Context i;
    private static String j;
    private static boolean k;
    private static int l;
    private static Object m = new Object();
    private static Object n = null;
    private static Method o;

    static {
        o = null;
        try {
            b = new SimpleDateFormat("MM-dd HH:mm:ss");
            o = Class.forName("com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler").getDeclaredMethod("appendLogToNative", String.class, String.class, String.class);
        } catch (Throwable unused) {
        }
    }

    private static boolean b(String str, String str2, String str3) throws NoSuchMethodException, SecurityException {
        if (o == null) {
            return false;
        }
        if (n == null) {
            Object objA = com.tencent.bugly.proguard.a.a("com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler", "getInstance", null, null, null);
            n = objA;
            if (objA == null) {
                return false;
            }
        }
        try {
            return ((Boolean) o.invoke(n, str, str2, str3)).booleanValue();
        } catch (Throwable th) {
            Log.w(w.a, th.getMessage());
            return false;
        }
    }

    public static synchronized void a(Context context) {
        if (k || context == null || !a) {
            return;
        }
        try {
            e = new StringBuilder(0);
            d = new StringBuilder(0);
            i = context;
            com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(context);
            g = aVarA.d;
            aVarA.getClass();
            h = "";
            j = i.getFilesDir().getPath() + "/buglylog_" + g + "_" + h + ".txt";
            l = Process.myPid();
        } catch (Throwable unused) {
        }
        k = true;
    }

    public static void a(int i2) {
        synchronized (m) {
            c = i2;
            if (i2 < 0) {
                c = 0;
            } else if (i2 > 10240) {
                c = 10240;
            }
        }
    }

    public static void a(String str, String str2, Throwable th) {
        String string;
        if (th == null) {
            return;
        }
        String message = th.getMessage();
        if (message == null) {
            message = "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append('\n');
        if (th == null) {
            string = "";
        } else {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            th.printStackTrace(printWriter);
            printWriter.flush();
            string = stringWriter.toString();
        }
        sb.append(string);
        a(str, str2, sb.toString());
    }

    public static synchronized void a(String str, String str2, String str3) {
        if (k && a) {
            b(str, str2, str3);
            int iMyTid = Process.myTid();
            d.setLength(0);
            if (str3.length() > 30720) {
                str3 = str3.substring(str3.length() - 30720, str3.length() - 1);
            }
            Date date = new Date();
            String string = b != null ? b.format(date) : date.toString();
            StringBuilder sb = d;
            sb.append(string);
            sb.append(" ");
            sb.append(l);
            sb.append(" ");
            sb.append(iMyTid);
            sb.append(" ");
            sb.append(str);
            sb.append(" ");
            sb.append(str2);
            sb.append(": ");
            sb.append(str3);
            sb.append("\u0001\r\n");
            final String string2 = d.toString();
            synchronized (m) {
                e.append(string2);
            }
            if (e.length() <= c) {
                return;
            }
            v.a().a(new Runnable() { // from class: com.tencent.bugly.proguard.x.1
                @Override // java.lang.Runnable
                public final void run() {
                    synchronized (x.m) {
                        if (x.e.length() <= x.c) {
                            return;
                        }
                        if (x.f == null) {
                            a unused = x.f = new a(x.j);
                        } else if (x.f.b.length() + x.e.length() > x.f.e) {
                            x.f.a();
                        }
                        if (x.f.a) {
                            x.f.a(x.e.toString());
                            x.e.setLength(0);
                        } else {
                            x.e.setLength(0);
                            x.e.append(string2);
                        }
                    }
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0020  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] a(boolean r3) {
        /*
            boolean r0 = com.tencent.bugly.proguard.x.a
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            java.lang.Object r0 = com.tencent.bugly.proguard.x.m
            monitor-enter(r0)
            if (r3 == 0) goto L20
            com.tencent.bugly.proguard.x$a r3 = com.tencent.bugly.proguard.x.f     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            if (r3 == 0) goto L20
            com.tencent.bugly.proguard.x$a r3 = com.tencent.bugly.proguard.x.f     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            boolean r3 = com.tencent.bugly.proguard.x.a.d(r3)     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            if (r3 == 0) goto L20
            com.tencent.bugly.proguard.x$a r3 = com.tencent.bugly.proguard.x.f     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            java.io.File r3 = com.tencent.bugly.proguard.x.a.a(r3)     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            goto L21
        L1e:
            r3 = move-exception
            goto L39
        L20:
            r3 = r1
        L21:
            java.lang.StringBuilder r2 = com.tencent.bugly.proguard.x.e     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            int r2 = r2.length()     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            if (r2 != 0) goto L2d
            if (r3 != 0) goto L2d
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L1e
            return r1
        L2d:
            java.lang.StringBuilder r2 = com.tencent.bugly.proguard.x.e     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            byte[] r3 = com.tencent.bugly.proguard.a.a(r3, r2)     // Catch: java.lang.Throwable -> L1e java.lang.Throwable -> L3b
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L1e
            return r3
        L39:
            monitor-exit(r0)
            throw r3
        L3b:
            monitor-exit(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.x.a(boolean):byte[]");
    }

    /* compiled from: BUGLY */
    public static class a {
        private boolean a;
        private File b;
        private String c;
        private long d;
        private long e = 30720;

        public a(String str) {
            if (str == null || str.equals("")) {
                return;
            }
            this.c = str;
            this.a = a();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean a() {
            try {
                this.b = new File(this.c);
            } catch (Throwable unused) {
                this.a = false;
            }
            if (this.b.exists() && !this.b.delete()) {
                this.a = false;
                return false;
            }
            if (!this.b.createNewFile()) {
                this.a = false;
                return false;
            }
            return true;
        }

        public final synchronized boolean a(String str) {
            if (!this.a) {
                return false;
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.b, true);
                fileOutputStream.write(str.getBytes("UTF-8"));
                fileOutputStream.flush();
                fileOutputStream.close();
                this.d += r9.length;
                return true;
            } catch (Throwable unused) {
                this.a = false;
                return false;
            }
        }
    }
}
