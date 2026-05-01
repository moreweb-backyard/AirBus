package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.n;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.v;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class c {
    public static int a = 0;
    public static boolean b = false;
    public static boolean c = true;
    public static int d = 20000;
    public static int e = 20000;
    public static long f = 604800000;
    public static String g = null;
    public static boolean h = false;
    public static String i = null;
    public static int j = 5000;
    private static c n;
    public final b k;
    public BuglyStrategy.a l;
    private final Context m;
    private final e o;
    private final NativeCrashHandler p;
    private com.tencent.bugly.crashreport.common.strategy.a q;
    private v r;
    private final com.tencent.bugly.crashreport.crash.anr.b s;
    private final z t;
    private Boolean u;

    private c(int i2, Context context, v vVar, boolean z, BuglyStrategy.a aVar, n nVar, String str) {
        Context applicationContext;
        a = i2;
        Context context2 = (context == null || (applicationContext = context.getApplicationContext()) == null) ? context : applicationContext;
        this.m = context2;
        this.q = com.tencent.bugly.crashreport.common.strategy.a.a();
        t tVarA = t.a();
        o oVarA = o.a();
        com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(context2);
        this.r = vVar;
        this.l = aVar;
        this.k = new b(i2, context2, tVarA, oVarA, this.q, aVar, nVar);
        this.o = new e(context2, this.k, this.q, aVarA);
        this.p = NativeCrashHandler.getInstance(context2, aVarA, this.k, this.q, vVar, z, str);
        Context context3 = context2;
        this.s = new com.tencent.bugly.crashreport.crash.anr.b(context3, this.q, aVarA, vVar, this.k);
        this.t = new z(context3, this.k, this.q, aVarA, vVar, aVar);
        BuglyBroadcastRecevier buglyBroadcastRecevier = BuglyBroadcastRecevier.getInstance();
        buglyBroadcastRecevier.addFilter("android.net.conn.CONNECTIVITY_CHANGE");
        buglyBroadcastRecevier.regist(context2, this.k);
    }

    public static synchronized void a(int i2, Context context, boolean z, BuglyStrategy.a aVar, n nVar, String str) {
        if (n == null) {
            n = new c(1004, context, v.a(), z, aVar, null, null);
        }
    }

    public static c a() {
        return n;
    }

    public final void a(StrategyBean strategyBean) {
        this.o.a(strategyBean);
        this.p.onStrategyChanged(strategyBean);
        this.s.a(strategyBean);
        this.t.a(strategyBean);
    }

    public final boolean b() {
        Boolean bool = this.u;
        if (bool != null) {
            return bool.booleanValue();
        }
        String str = com.tencent.bugly.crashreport.common.info.a.a().d;
        List<q> listA = o.a().a(1);
        ArrayList arrayList = new ArrayList();
        if (listA != null && listA.size() > 0) {
            for (q qVar : listA) {
                if (str.equals(qVar.c)) {
                    this.u = true;
                    arrayList.add(qVar);
                }
            }
            if (arrayList.size() > 0) {
                o.a();
                o.a(arrayList);
            }
            return true;
        }
        this.u = false;
        return false;
    }

    public final synchronized void c() {
        this.o.a();
        this.p.setUserOpened(true);
        this.s.a(true);
        this.t.a(true, -1L);
    }

    public final synchronized void d() {
        this.o.b();
        this.p.setUserOpened(false);
        this.s.a(false);
        this.t.a(false, -1L);
    }

    public final void e() {
        this.o.a();
    }

    public final void f() {
        this.p.setUserOpened(false);
    }

    public final void g() {
        this.p.setUserOpened(true);
    }

    public final void h() {
        this.s.a(true);
    }

    public final void i() {
        this.s.a(false);
    }

    public final void a(long j2) {
        this.t.a(true, j2);
    }

    public final void j() {
        this.t.a(false, -1L);
    }

    public final synchronized void k() {
        this.p.testNativeCrash();
    }

    public final synchronized void l() {
        com.tencent.bugly.crashreport.crash.anr.b bVar = this.s;
        int i2 = 0;
        while (true) {
            int i3 = i2 + 1;
            if (i2 < 30) {
                try {
                    w.a("try main sleep for make a test anr! try:%d/30 , kill it if you don't want to wait!", Integer.valueOf(i3));
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    i2 = i3;
                } catch (Throwable th) {
                    if (w.a(th)) {
                        return;
                    }
                    th.printStackTrace();
                    return;
                }
            }
        }
    }

    public final synchronized void m() {
        z zVar = this.t;
        z.a();
    }

    public final boolean n() {
        return this.s.a();
    }

    public final void a(final Thread thread, final Throwable th, boolean z, String str, byte[] bArr) {
        final boolean z2 = false;
        final String str2 = null;
        final byte[] bArr2 = null;
        this.r.b(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.c.1
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    w.c("post a throwable %b", Boolean.valueOf(z2));
                    c.this.o.a(thread, th, false, str2, bArr2);
                } catch (Throwable th2) {
                    if (!w.b(th2)) {
                        th2.printStackTrace();
                    }
                    w.e("java catch error: %s", th.toString());
                }
            }
        });
    }

    public final void a(CrashDetailBean crashDetailBean) {
        this.k.c(crashDetailBean);
    }

    public final void b(long j2) {
        v.a().a(new Thread() { // from class: com.tencent.bugly.crashreport.crash.c.2
            @Override // java.lang.Thread, java.lang.Runnable
            public final void run() {
                List<CrashDetailBean> list;
                if (com.tencent.bugly.proguard.a.a(c.this.m, "local_crash_lock", 10000L)) {
                    List<CrashDetailBean> listA = c.this.k.a();
                    if (listA != null && listA.size() > 0) {
                        int size = listA.size();
                        if (size > 100) {
                            ArrayList arrayList = new ArrayList();
                            Collections.sort(listA);
                            for (int i2 = 0; i2 < 100; i2++) {
                                arrayList.add(listA.get((size - 1) - i2));
                            }
                            list = arrayList;
                        } else {
                            list = listA;
                        }
                        c.this.k.a(list, 0L, false, false);
                    }
                    com.tencent.bugly.proguard.a.b(c.this.m, "local_crash_lock");
                }
            }
        }, j2);
    }
}
