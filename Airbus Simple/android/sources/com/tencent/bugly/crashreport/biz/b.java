package com.tencent.bugly.crashreport.biz;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.v;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {
    public static a a = null;
    private static boolean b = false;
    private static int c = 10;
    private static long d = 300000;
    private static long e = 30000;
    private static long f;
    private static int g;
    private static long h;
    private static long i;
    private static long j;
    private static Application.ActivityLifecycleCallbacks k;
    private static Class<?> l;

    static /* synthetic */ int g() {
        int i2 = g;
        g = i2 + 1;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x005e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void c(android.content.Context r14, com.tencent.bugly.BuglyStrategy r15) {
        /*
            Method dump skipped, instructions count: 259
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.biz.b.c(android.content.Context, com.tencent.bugly.BuglyStrategy):void");
    }

    public static void a(final Context context, final BuglyStrategy buglyStrategy) {
        long appReportDelay;
        if (b) {
            return;
        }
        e = com.tencent.bugly.crashreport.common.strategy.a.a().c().n;
        c = com.tencent.bugly.crashreport.common.strategy.a.a().c().t;
        a = new a(context);
        b = true;
        if (buglyStrategy != null) {
            l = buglyStrategy.getUserInfoActivity();
            appReportDelay = buglyStrategy.getAppReportDelay();
        } else {
            appReportDelay = 0;
        }
        if (appReportDelay <= 0) {
            c(context, buglyStrategy);
        } else {
            v.a().a(new Runnable() { // from class: com.tencent.bugly.crashreport.biz.b.1
                @Override // java.lang.Runnable
                public final void run() {
                    b.c(context, buglyStrategy);
                }
            }, appReportDelay);
        }
    }

    public static void a(long j2) {
        if (j2 < 0) {
            j2 = com.tencent.bugly.crashreport.common.strategy.a.a().c().n;
        }
        f = j2;
    }

    public static void a(StrategyBean strategyBean) {
        if (strategyBean == null) {
            return;
        }
        if (strategyBean.n > 0) {
            e = strategyBean.n;
        }
        if (strategyBean.t > 0) {
            c = strategyBean.t;
        }
        if (strategyBean.u > 0) {
            d = strategyBean.u;
        }
    }

    public static void a() {
        if (a != null) {
            a.a(2, false, 0L);
        }
    }

    public static void a(Context context) {
        if (!b || context == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            Application application = context.getApplicationContext() instanceof Application ? (Application) context.getApplicationContext() : null;
            if (application != null) {
                try {
                    if (k != null) {
                        application.unregisterActivityLifecycleCallbacks(k);
                    }
                } catch (Exception unused) {
                }
            }
        }
        b = false;
    }
}
