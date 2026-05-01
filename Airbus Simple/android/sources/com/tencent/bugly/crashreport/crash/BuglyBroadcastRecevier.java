package com.tencent.bugly.crashreport.crash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.PointerIconCompat;
import com.tencent.bugly.proguard.t;
import com.tencent.bugly.proguard.v;
import com.tencent.bugly.proguard.w;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class BuglyBroadcastRecevier extends BroadcastReceiver {
    public static String ACTION_PROCESS_CRASHED = "com.tencent.feedback.A02";
    public static String ACTION_PROCESS_LAUNCHED = "com.tencent.feedback.A01";
    public static final long UPLOADLIMITED = 60000;
    private static BuglyBroadcastRecevier d;
    private IntentFilter a = new IntentFilter();
    private Context b;
    private String c;

    public static synchronized BuglyBroadcastRecevier getInstance() {
        if (d == null) {
            d = new BuglyBroadcastRecevier();
        }
        return d;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (this.b != null) {
            this.b.unregisterReceiver(this);
        }
    }

    public synchronized void addFilter(String str) {
        if (!this.a.hasAction(str)) {
            this.a.addAction(str);
        }
        w.c("add action %s", str);
    }

    public synchronized void regist(Context context, b bVar) {
        try {
            w.a("regis BC", new Object[0]);
            this.b = context;
            context.registerReceiver(this, this.a);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public synchronized void unregist(Context context) {
        try {
            w.a("unregis BC", new Object[0]);
            context.unregisterReceiver(this);
            this.b = context;
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        try {
            a(context, intent);
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private synchronized boolean a(Context context, Intent intent) {
        if (context != null && intent != null) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                String strE = com.tencent.bugly.crashreport.common.info.b.e(this.b);
                w.c("is Connect BC " + strE, new Object[0]);
                w.a("network %s changed to %s", this.c, strE);
                if (strE == null) {
                    this.c = null;
                    return true;
                }
                String str = this.c;
                this.c = strE;
                long jCurrentTimeMillis = System.currentTimeMillis();
                com.tencent.bugly.crashreport.common.strategy.a aVarA = com.tencent.bugly.crashreport.common.strategy.a.a();
                t tVarA = t.a();
                com.tencent.bugly.crashreport.common.info.a aVarA2 = com.tencent.bugly.crashreport.common.info.a.a(context);
                if (aVarA != null && tVarA != null && aVarA2 != null) {
                    if (!strE.equals(str)) {
                        if (jCurrentTimeMillis - tVarA.a(c.a) > UPLOADLIMITED) {
                            w.a("try to upload crash on network changed.", new Object[0]);
                            c.a().b(0L);
                        }
                        if (jCurrentTimeMillis - tVarA.a(PointerIconCompat.TYPE_CONTEXT_MENU) > UPLOADLIMITED) {
                            w.a("try to upload userinfo on network changed.", new Object[0]);
                            v.a().b(new Runnable(this) { // from class: com.tencent.bugly.crashreport.crash.BuglyBroadcastRecevier.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    com.tencent.bugly.crashreport.biz.b.a.b();
                                }
                            });
                        }
                    }
                    return true;
                }
                w.d("not inited BC not work", new Object[0]);
                return true;
            }
        }
        return false;
    }
}
