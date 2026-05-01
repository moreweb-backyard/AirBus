package com.tencent.bugly;

import android.content.Context;
import android.text.TextUtils;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.crashreport.crash.d;
import com.tencent.bugly.crashreport.inner.InnerAPI;
import com.tencent.bugly.proguard.m;
import com.tencent.bugly.proguard.w;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class CrashModule extends a {
    public static final int MODULE_ID = 1004;
    private static int c = 0;
    private static boolean d = false;
    private static CrashModule e = new CrashModule();
    private long a;
    private BuglyStrategy.a b;

    public static CrashModule getInstance() {
        e.id = 1004;
        return e;
    }

    public static boolean hasInitialized() {
        return d;
    }

    @Override // com.tencent.bugly.a
    public void init(Context context, boolean z, BuglyStrategy buglyStrategy) {
        if (context == null || d) {
            return;
        }
        m mVarA = m.a();
        int i = c + 1;
        c = i;
        mVarA.a(1004, i);
        d = true;
        CrashReport.setContext(context);
        a(context, buglyStrategy);
        c.a(1004, context, z, this.b, null, null);
        c cVarA = c.a();
        cVarA.e();
        if (buglyStrategy == null || buglyStrategy.isEnableNativeCrashMonitor()) {
            cVarA.g();
        } else {
            w.a("[crash] Closed native crash monitor!", new Object[0]);
            cVarA.f();
        }
        if (buglyStrategy == null || buglyStrategy.isEnableANRCrashMonitor()) {
            cVarA.h();
        } else {
            w.a("[crash] Closed ANR monitor!", new Object[0]);
            cVarA.i();
        }
        if (buglyStrategy != null && buglyStrategy.isEnableBlockCrashMonitor()) {
            cVarA.a(buglyStrategy == null ? -1L : buglyStrategy.getBlockThresholdTime());
        } else {
            cVarA.j();
        }
        InnerAPI.context = context;
        d.a(context);
        c.a().b(this.a);
        m mVarA2 = m.a();
        int i2 = c - 1;
        c = i2;
        mVarA2.a(1004, i2);
    }

    private synchronized void a(Context context, BuglyStrategy buglyStrategy) {
        if (buglyStrategy == null) {
            return;
        }
        String libBuglySOFilePath = buglyStrategy.getLibBuglySOFilePath();
        if (!TextUtils.isEmpty(libBuglySOFilePath)) {
            com.tencent.bugly.crashreport.common.info.a.a(context).k = libBuglySOFilePath;
            w.a("setted libBugly.so file path :%s", libBuglySOFilePath);
        }
        if (buglyStrategy.getCrashHandleCallback() != null) {
            this.b = buglyStrategy.getCrashHandleCallback();
            w.a("setted CrashHanldeCallback", new Object[0]);
        }
        if (buglyStrategy.getAppReportDelay() > 0) {
            this.a = buglyStrategy.getAppReportDelay();
            w.a("setted delay: %d", Long.valueOf(this.a));
        }
    }

    @Override // com.tencent.bugly.a
    public void onServerStrategyChanged(StrategyBean strategyBean) {
        if (strategyBean == null) {
            return;
        }
        c cVarA = c.a();
        if (cVarA != null) {
            cVarA.a(strategyBean);
        }
        d.a(strategyBean);
    }

    @Override // com.tencent.bugly.a
    public String[] getTables() {
        return new String[]{"t_cr"};
    }
}
