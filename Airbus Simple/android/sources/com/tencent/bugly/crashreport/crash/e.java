package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import java.lang.Thread;
import java.util.HashMap;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class e implements Thread.UncaughtExceptionHandler {
    private static Thread.UncaughtExceptionHandler f;
    private static boolean h;
    private static final Object i = new Object();
    private static int j;
    private Context a;
    private b b;
    private com.tencent.bugly.crashreport.common.strategy.a c;
    private com.tencent.bugly.crashreport.common.info.a d;
    private Thread.UncaughtExceptionHandler e;
    private boolean g = false;

    public e(Context context, b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar, com.tencent.bugly.crashreport.common.info.a aVar2) {
        this.a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
    }

    public final synchronized void a() {
        if (j >= 10) {
            w.a("java crash handler over %d, no need set.", 10);
            return;
        }
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler == null) {
            return;
        }
        if (getClass().getName().equals(defaultUncaughtExceptionHandler.getClass().getName())) {
            return;
        }
        if ("com.android.internal.os.RuntimeInit$UncaughtHandler".equals(defaultUncaughtExceptionHandler.getClass().getName())) {
            w.a("backup system java handler: %s", defaultUncaughtExceptionHandler.toString());
            f = defaultUncaughtExceptionHandler;
            this.e = defaultUncaughtExceptionHandler;
        } else {
            w.a("backup java handler: %s", defaultUncaughtExceptionHandler.toString());
            this.e = defaultUncaughtExceptionHandler;
        }
        a(defaultUncaughtExceptionHandler);
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.g = true;
        j++;
        w.a("registered java monitor: %s", toString());
    }

    public final synchronized void b() {
        this.g = false;
        w.a("close java monitor!", new Object[0]);
        if (Thread.getDefaultUncaughtExceptionHandler().getClass().getName().contains("bugly")) {
            w.a("Java monitor to unregister: %s", toString());
            Thread.setDefaultUncaughtExceptionHandler(this.e);
            j--;
        }
    }

    private synchronized void a(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.e = uncaughtExceptionHandler;
    }

    private CrashDetailBean b(Thread thread, Throwable th, boolean z, String str, byte[] bArr) {
        String strA;
        boolean zN = c.a().n();
        String str2 = (zN && z) ? " This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful![Bugly]" : "";
        boolean z2 = false;
        if (zN && z) {
            w.e("This Crash Caused By ANR , PLS To Fix ANR , This Trace May Be Not Useful!", new Object[0]);
        }
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        w.e("3", new Object[0]);
        crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
        crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
        crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
        crashDetailBean.E = this.d.o();
        crashDetailBean.F = this.d.n();
        crashDetailBean.G = this.d.p();
        crashDetailBean.w = com.tencent.bugly.proguard.a.a(this.a, c.d, (String) null);
        crashDetailBean.x = x.a(z);
        Object[] objArr = new Object[1];
        objArr[0] = Integer.valueOf(crashDetailBean.x == null ? 0 : crashDetailBean.x.length);
        w.a("user log size:%d", objArr);
        crashDetailBean.b = z ? 0 : 2;
        crashDetailBean.e = this.d.g();
        crashDetailBean.f = this.d.i;
        crashDetailBean.g = this.d.v();
        crashDetailBean.m = this.d.f();
        if (th == null) {
            return null;
        }
        Throwable cause = th;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        String name = th.getClass().getName();
        String strB = b(th, 1000);
        if (strB == null) {
            strB = "";
        }
        Object[] objArr2 = new Object[2];
        objArr2[0] = Integer.valueOf(th.getStackTrace().length);
        objArr2[1] = Boolean.valueOf(th.getCause() != null);
        w.e("stack frame :%d, has cause %b", objArr2);
        String string = th.getStackTrace().length > 0 ? th.getStackTrace()[0].toString() : "";
        if (cause != th) {
            crashDetailBean.n = cause.getClass().getName();
            crashDetailBean.o = b(cause, 1000);
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = cause.getStackTrace()[0].toString();
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            sb.append(":");
            sb.append(strB);
            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
            sb.append(string);
            sb.append("\n......");
            sb.append("\nCaused by:\n");
            sb.append(crashDetailBean.n);
            sb.append(":");
            sb.append(crashDetailBean.o);
            sb.append(IOUtils.LINE_SEPARATOR_UNIX);
            strA = a(cause, c.e);
            sb.append(strA);
            crashDetailBean.q = sb.toString();
        } else {
            crashDetailBean.n = name;
            crashDetailBean.o = strB + str2;
            if (crashDetailBean.o == null) {
                crashDetailBean.o = "";
            }
            crashDetailBean.p = string;
            strA = a(th, c.e);
            crashDetailBean.q = strA;
        }
        crashDetailBean.r = System.currentTimeMillis();
        crashDetailBean.u = com.tencent.bugly.proguard.a.c(crashDetailBean.q.getBytes());
        try {
            crashDetailBean.y = com.tencent.bugly.proguard.a.a(c.e, false);
            crashDetailBean.z = this.d.d;
            crashDetailBean.A = thread.getName() + "(" + thread.getId() + ")";
            crashDetailBean.y.put(crashDetailBean.A, strA);
            crashDetailBean.H = this.d.x();
            crashDetailBean.h = this.d.u();
            crashDetailBean.i = this.d.G();
            crashDetailBean.L = this.d.a;
            crashDetailBean.M = this.d.n;
            crashDetailBean.O = this.d.D();
            crashDetailBean.P = this.d.E();
            crashDetailBean.Q = this.d.y();
            crashDetailBean.R = this.d.C();
        } catch (Throwable th2) {
            w.e("handle crash error %s", th2.toString());
        }
        if (z) {
            this.b.b(crashDetailBean);
        } else {
            boolean z3 = str != null && str.length() > 0;
            if (bArr != null && bArr.length > 0) {
                z2 = true;
            }
            if (z3) {
                crashDetailBean.N = new HashMap(1);
                crashDetailBean.N.put("UserData", str);
            }
            if (z2) {
                crashDetailBean.S = bArr;
            }
        }
        return crashDetailBean;
    }

    private static boolean c() {
        boolean z;
        synchronized (i) {
            z = h;
            h = true;
        }
        return z;
    }

    /* JADX WARN: Code restructure failed: missing block: B:108:0x01e6, code lost:
    
        if (com.tencent.bugly.crashreport.crash.e.f != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x008a, code lost:
    
        if (com.tencent.bugly.crashreport.crash.e.f != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x008c, code lost:
    
        com.tencent.bugly.proguard.w.e("system handle start!", new java.lang.Object[0]);
        com.tencent.bugly.crashreport.crash.e.f.uncaughtException(r9, r10);
        com.tencent.bugly.proguard.w.e("system handle end!", new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x009f, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00a0, code lost:
    
        com.tencent.bugly.proguard.w.e("crashreport last handle start!", new java.lang.Object[0]);
        com.tencent.bugly.proguard.w.e("current process die", new java.lang.Object[0]);
        android.os.Process.killProcess(android.os.Process.myPid());
        java.lang.System.exit(1);
        com.tencent.bugly.proguard.w.e("crashreport last handle end!", new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00bf, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x013d, code lost:
    
        if (com.tencent.bugly.crashreport.crash.e.f != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0162, code lost:
    
        if (com.tencent.bugly.crashreport.crash.e.f != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01a2, code lost:
    
        if (com.tencent.bugly.crashreport.crash.e.f != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01a6, code lost:
    
        com.tencent.bugly.proguard.w.e("crashreport last handle start!", new java.lang.Object[0]);
        com.tencent.bugly.proguard.w.e("current process die", new java.lang.Object[0]);
        android.os.Process.killProcess(android.os.Process.myPid());
        java.lang.System.exit(1);
        com.tencent.bugly.proguard.w.e("crashreport last handle end!", new java.lang.Object[0]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01c5, code lost:
    
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:118:0x020f A[DONT_GENERATE, FINALLY_INSNS] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a(java.lang.Thread r9, java.lang.Throwable r10, boolean r11, java.lang.String r12, byte[] r13) {
        /*
            Method dump skipped, instructions count: 583
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.e.a(java.lang.Thread, java.lang.Throwable, boolean, java.lang.String, byte[]):void");
    }

    private static boolean b(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        if (uncaughtExceptionHandler == null) {
            return true;
        }
        String name = uncaughtExceptionHandler.getClass().getName();
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            if (name.equals(className) && "uncaughtException".equals(methodName)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final void uncaughtException(Thread thread, Throwable th) {
        a(thread, th, true, null, null);
    }

    public final synchronized void a(StrategyBean strategyBean) {
        if (strategyBean != null) {
            if (strategyBean.d != this.g) {
                w.a("java changed to %b", Boolean.valueOf(strategyBean.d));
                if (strategyBean.d) {
                    a();
                    return;
                }
                b();
            }
        }
    }

    private static String a(Throwable th, int i2) {
        if (th == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            if (th.getStackTrace() != null) {
                for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                    if (i2 > 0 && sb.length() >= i2) {
                        sb.append("\n[Stack over limit size :" + i2 + " , has been cutted !]");
                        return sb.toString();
                    }
                    sb.append(stackTraceElement.toString());
                    sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                }
            }
        } catch (Throwable th2) {
            w.e("gen stack error %s", th2.toString());
        }
        return sb.toString();
    }

    private static String b(Throwable th, int i2) {
        if (th.getMessage() == null) {
            return "";
        }
        if (th.getMessage().length() <= 1000) {
            return th.getMessage();
        }
        return th.getMessage().substring(0, 1000) + "\n[Message over limit size:1000, has been cutted!]";
    }
}
