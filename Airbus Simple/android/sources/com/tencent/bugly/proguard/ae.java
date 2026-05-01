package com.tencent.bugly.proguard;

import android.content.Context;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import org.apache.commons.io.IOUtils;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ae {
    private Context a;
    private com.tencent.bugly.crashreport.crash.b b;
    private com.tencent.bugly.crashreport.common.strategy.a c;
    private com.tencent.bugly.crashreport.common.info.a d;

    public ae(Context context, com.tencent.bugly.crashreport.crash.b bVar, com.tencent.bugly.crashreport.common.strategy.a aVar, com.tencent.bugly.crashreport.common.info.a aVar2, BuglyStrategy.a aVar3) {
        this.a = context;
        this.b = bVar;
        this.c = aVar;
        this.d = aVar2;
    }

    public final void a(Thread thread, int i, String str, String str2, String str3) {
        StrategyBean strategyBeanC;
        String[] strArrSplit;
        w.e("Cocos2d-x Crash Happen", new Object[0]);
        try {
            try {
                this.c.c();
                if (!this.c.b()) {
                    w.e("waiting for remote sync", new Object[0]);
                    int i2 = 0;
                    while (!this.c.b()) {
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i2 += 500;
                        if (i2 >= 3000) {
                            break;
                        }
                    }
                }
                if (!this.c.b()) {
                    w.d("no remote but still store!", new Object[0]);
                }
                strategyBeanC = this.c.c();
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
            }
            if (!strategyBeanC.d && this.c.b()) {
                w.e("crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
                com.tencent.bugly.crashreport.crash.b.a("Cocos2dx", a.b(), this.d.d, thread, str + IOUtils.LINE_SEPARATOR_UNIX + str2 + IOUtils.LINE_SEPARATOR_UNIX + str3, null);
            } else if (!strategyBeanC.i) {
                w.e("cocos report is disabled.", new Object[0]);
            } else {
                CrashDetailBean crashDetailBean = new CrashDetailBean();
                crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
                crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
                crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
                crashDetailBean.E = this.d.o();
                crashDetailBean.F = this.d.n();
                crashDetailBean.G = this.d.p();
                crashDetailBean.w = a.a(this.a, com.tencent.bugly.crashreport.crash.c.d, (String) null);
                crashDetailBean.x = x.a(false);
                crashDetailBean.b = i;
                crashDetailBean.e = this.d.g();
                crashDetailBean.f = this.d.i;
                crashDetailBean.g = this.d.v();
                crashDetailBean.m = this.d.f();
                crashDetailBean.n = str;
                crashDetailBean.o = str2;
                String str4 = "";
                if (str3 != null && (strArrSplit = str3.split(IOUtils.LINE_SEPARATOR_UNIX)) != null && strArrSplit.length > 0) {
                    str4 = strArrSplit[0];
                }
                crashDetailBean.p = str4;
                crashDetailBean.q = str3;
                crashDetailBean.r = System.currentTimeMillis();
                crashDetailBean.u = a.c(crashDetailBean.q.getBytes());
                crashDetailBean.y = a.a(com.tencent.bugly.crashreport.crash.c.e, false);
                crashDetailBean.z = this.d.d;
                crashDetailBean.A = thread.getName() + "(" + thread.getId() + ")";
                crashDetailBean.H = this.d.x();
                crashDetailBean.h = this.d.u();
                crashDetailBean.i = this.d.G();
                crashDetailBean.L = this.d.a;
                crashDetailBean.M = this.d.n;
                crashDetailBean.O = this.d.D();
                crashDetailBean.P = this.d.E();
                crashDetailBean.Q = this.d.y();
                crashDetailBean.R = this.d.C();
                this.b.b(crashDetailBean);
                com.tencent.bugly.crashreport.crash.b.a("Cocos2dx", a.b(), this.d.d, thread, str + IOUtils.LINE_SEPARATOR_UNIX + str2 + IOUtils.LINE_SEPARATOR_UNIX + str3, crashDetailBean);
                if (!this.b.a(crashDetailBean)) {
                    this.b.a(crashDetailBean, 3000L, false);
                }
                w.e("handle end", new Object[0]);
                return;
            }
            w.e("handle end", new Object[0]);
        } catch (Throwable th2) {
            w.e("handle end", new Object[0]);
            throw th2;
        }
    }
}
