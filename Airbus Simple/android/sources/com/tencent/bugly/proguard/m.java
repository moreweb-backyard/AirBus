package com.tencent.bugly.proguard;

import android.content.Context;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class m {
    private static m a;
    private static long b = System.currentTimeMillis();

    private m(Context context) {
    }

    public static synchronized m a(Context context) {
        if (a == null) {
            a = new m(context);
        }
        return a;
    }

    public static synchronized m a() {
        return a;
    }

    private synchronized boolean b(int i) {
        try {
            String str = com.tencent.bugly.crashreport.common.info.a.a().d;
            if (str == null) {
                w.a("process name is null", new Object[0]);
                return false;
            }
            long jD = d(0);
            o oVarA = o.a();
            com.tencent.bugly.crashreport.common.info.a.a().getClass();
            List<l> listA = oVarA.a(i, str, jD, "2.2.0");
            if (listA == null) {
                return false;
            }
            if (listA.size() < 2) {
                return false;
            }
            int i2 = 0;
            while (true) {
                int i3 = i2 + 1;
                if (i3 >= listA.size()) {
                    return false;
                }
                if (listA.get(i3).c - listA.get(i2).c < 86400000) {
                    return true;
                }
                i2 = i3;
            }
        } catch (Exception unused) {
            w.a("FrenquencyCrash failed", new Object[0]);
            return false;
        }
    }

    private synchronized l c(int i) {
        l lVarA;
        String str;
        try {
            str = com.tencent.bugly.crashreport.common.info.a.a().d;
        } catch (Exception unused) {
            w.a("getLatestCrashRecord failed", new Object[0]);
            lVarA = null;
        }
        if (str == null) {
            w.a("process name is null", new Object[0]);
            return null;
        }
        lVarA = o.a().a(i, str);
        return lVarA;
    }

    public final synchronized boolean a(int i, int i2) {
        l lVar;
        try {
            lVar = new l();
            lVar.a = 1004L;
            lVar.b = com.tencent.bugly.crashreport.common.info.a.a().d;
            lVar.f = com.tencent.bugly.crashreport.common.info.a.a().i;
            com.tencent.bugly.crashreport.common.info.a.a().getClass();
            lVar.e = "2.2.0";
            lVar.d = i2;
            lVar.c = System.currentTimeMillis();
            lVar.g = b;
        } catch (Exception unused) {
            w.a("saveCrashRecord failed", new Object[0]);
            return false;
        }
        return o.a().a(lVar);
    }

    private synchronized int b(int i, int i2) {
        try {
        } catch (Exception unused) {
            w.a("clearHistoryCrashRecord failed", new Object[0]);
            return 0;
        }
        return o.a().a(i, com.tencent.bugly.crashreport.common.info.a.a().d, d(1));
    }

    private synchronized long d(int i) {
        long jCurrentTimeMillis;
        jCurrentTimeMillis = 0;
        switch (i) {
            case 1:
                jCurrentTimeMillis = System.currentTimeMillis() - 86400000;
                break;
        }
        return jCurrentTimeMillis;
    }

    public final synchronized int b() {
        o oVarA;
        try {
            oVarA = o.a();
            com.tencent.bugly.crashreport.common.info.a.a().getClass();
        } catch (Exception unused) {
            w.a("clearInvalidCrashRecord failed", new Object[0]);
            return 0;
        }
        return oVarA.b();
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0024 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized boolean a(int r8) {
        /*
            r7 = this;
            monitor-enter(r7)
            r0 = 0
            r1 = 1
            com.tencent.bugly.proguard.l r2 = r7.c(r8)     // Catch: java.lang.Throwable -> L28 java.lang.Exception -> L2a
            if (r2 == 0) goto L21
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L28 java.lang.Exception -> L2a
            long r5 = r2.c     // Catch: java.lang.Throwable -> L28 java.lang.Exception -> L2a
            r2 = 0
            long r3 = r3 - r5
            r5 = 86400000(0x5265c00, double:4.2687272E-316)
            int r2 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r2 > 0) goto L21
            boolean r2 = r7.b(r8)     // Catch: java.lang.Throwable -> L28 java.lang.Exception -> L2a
            if (r2 != 0) goto L1f
            goto L21
        L1f:
            r2 = 0
            goto L22
        L21:
            r2 = 1
        L22:
            if (r2 == 0) goto L32
            r7.b(r8, r1)     // Catch: java.lang.Throwable -> L28 java.lang.Exception -> L2b
            goto L32
        L28:
            r8 = move-exception
            goto L34
        L2a:
            r2 = 1
        L2b:
            java.lang.String r8 = "canInit failed"
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch: java.lang.Throwable -> L28
            com.tencent.bugly.proguard.w.a(r8, r0)     // Catch: java.lang.Throwable -> L28
        L32:
            monitor-exit(r7)
            return r2
        L34:
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.m.a(int):boolean");
    }
}
