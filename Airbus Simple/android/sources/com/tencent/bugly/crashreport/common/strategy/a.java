package com.tencent.bugly.crashreport.common.strategy;

import android.content.Context;
import android.os.Parcel;
import com.tencent.bugly.crashreport.biz.b;
import com.tencent.bugly.proguard.av;
import com.tencent.bugly.proguard.n;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.q;
import com.tencent.bugly.proguard.v;
import com.tencent.bugly.proguard.w;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a {
    public static int a = 1000;
    private static a b;
    private final List<com.tencent.bugly.a> c;
    private Context g;
    private StrategyBean f = null;
    private final StrategyBean e = new StrategyBean();
    private final v d = v.a();

    private a(Context context, List<com.tencent.bugly.a> list) {
        this.g = context;
        this.c = list;
        this.d.b(new Thread() { // from class: com.tencent.bugly.crashreport.common.strategy.a.1
            @Override // java.lang.Thread, java.lang.Runnable
            public final void run() {
                try {
                    Map<String, byte[]> mapA = o.a().a(a.a, (n) null, true);
                    if (mapA != null) {
                        byte[] bArr = mapA.get("key_imei");
                        byte[] bArr2 = mapA.get("key_ip");
                        if (bArr != null) {
                            com.tencent.bugly.crashreport.common.info.a.a(a.this.g).e(new String(bArr));
                        }
                        if (bArr2 != null) {
                            com.tencent.bugly.crashreport.common.info.a.a(a.this.g).d(new String(bArr2));
                        }
                    }
                    a aVar = a.this;
                    a.this.f = a.d();
                    a.this.a(a.this.f);
                } catch (Throwable th) {
                    if (w.a(th)) {
                        return;
                    }
                    th.printStackTrace();
                }
            }
        });
    }

    public static synchronized a a(Context context, List<com.tencent.bugly.a> list) {
        if (b == null) {
            b = new a(context, list);
        }
        return b;
    }

    public static synchronized a a() {
        return b;
    }

    public final synchronized boolean b() {
        return this.f != null;
    }

    public final StrategyBean c() {
        if (this.f != null) {
            return this.f;
        }
        return this.e;
    }

    protected final void a(StrategyBean strategyBean) {
        for (com.tencent.bugly.a aVar : this.c) {
            try {
                w.c("[strategy] Notify %s", aVar.getClass().getName());
                aVar.onServerStrategyChanged(strategyBean);
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        b.a(strategyBean);
    }

    public final void a(av avVar) throws NumberFormatException {
        if (avVar == null) {
            return;
        }
        if (this.f == null || avVar.h != this.f.m) {
            StrategyBean strategyBean = new StrategyBean();
            strategyBean.d = avVar.a;
            strategyBean.f = avVar.c;
            strategyBean.e = avVar.b;
            String str = avVar.d;
            if (!(str == null || str.trim().length() <= 0)) {
                w.c("upload url changes to %s", avVar.d);
                strategyBean.o = avVar.d;
            }
            String str2 = avVar.e;
            if (!(str2 == null || str2.trim().length() <= 0)) {
                w.c("exception upload url changes to %s", avVar.e);
                strategyBean.p = avVar.e;
            }
            if (avVar.f != null) {
                String str3 = avVar.f.a;
                if (!(str3 == null || str3.trim().length() <= 0)) {
                    strategyBean.r = avVar.f.a;
                }
            }
            if (avVar.h != 0) {
                strategyBean.m = avVar.h;
            }
            if (avVar.g != null && avVar.g.size() > 0) {
                strategyBean.s = avVar.g;
                String str4 = avVar.g.get("B11");
                if (str4 != null && str4.equals("1")) {
                    strategyBean.g = true;
                } else {
                    strategyBean.g = false;
                }
                String str5 = avVar.g.get("B3");
                if (str5 != null) {
                    strategyBean.v = Long.valueOf(str5).longValue();
                }
                strategyBean.n = avVar.i;
                strategyBean.u = avVar.i;
                String str6 = avVar.g.get("B27");
                if (str6 != null && str6.length() > 0) {
                    try {
                        int i = Integer.parseInt(str6);
                        if (i > 0) {
                            strategyBean.t = i;
                        }
                    } catch (Exception e) {
                        if (!w.a(e)) {
                            e.printStackTrace();
                        }
                    }
                }
                String str7 = avVar.g.get("B25");
                if (str7 != null && str7.equals("1")) {
                    strategyBean.i = true;
                } else {
                    strategyBean.i = false;
                }
            }
            w.a("cr:%b,qu:%b,uin:%b,an:%b,bl:%b,ss:%b,ssT:%b,ssOT:%d,cos:%b,lstT:%d", Boolean.valueOf(strategyBean.d), Boolean.valueOf(strategyBean.f), Boolean.valueOf(strategyBean.e), Boolean.valueOf(strategyBean.g), Boolean.valueOf(strategyBean.h), Boolean.valueOf(strategyBean.k), Boolean.valueOf(strategyBean.l), Long.valueOf(strategyBean.n), Boolean.valueOf(strategyBean.i), Long.valueOf(strategyBean.m));
            this.f = strategyBean;
            o.a();
            o.b(2);
            q qVar = new q();
            qVar.b = 2;
            qVar.a = strategyBean.b;
            qVar.e = strategyBean.c;
            Parcel parcelObtain = Parcel.obtain();
            strategyBean.writeToParcel(parcelObtain, 0);
            byte[] bArrMarshall = parcelObtain.marshall();
            parcelObtain.recycle();
            qVar.g = bArrMarshall;
            o.a().a(qVar);
            a(strategyBean);
        }
    }

    public static StrategyBean d() {
        List<q> listA = o.a().a(2);
        if (listA == null || listA.size() <= 0) {
            return null;
        }
        q qVar = listA.get(0);
        if (qVar.g != null) {
            return (StrategyBean) com.tencent.bugly.proguard.a.a(qVar.g, StrategyBean.CREATOR);
        }
        return null;
    }
}
