package com.tencent.bugly.crashreport.biz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.n;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.v;
import com.tencent.bugly.proguard.w;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a {
    private Context a;
    private long b;
    private int c;

    static /* synthetic */ void a(a aVar, UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            long jA = o.a().a("t_ui", a(userInfoBean), (n) null, true);
            if (jA >= 0) {
                w.c("insert %s success! %d", "t_ui", Long.valueOf(jA));
                userInfoBean.a = jA;
            }
        }
    }

    static /* synthetic */ int b(a aVar) {
        int i = aVar.c;
        aVar.c = i + 1;
        return i;
    }

    public a(Context context) {
        this.a = context;
    }

    public final void a(int i, boolean z, long j) {
        StrategyBean strategyBeanC = com.tencent.bugly.crashreport.common.strategy.a.a().c();
        if (strategyBeanC != null && !strategyBeanC.e && i != 1 && i != 3) {
            w.e("UserInfo is disable", new Object[0]);
            return;
        }
        if (i == 1) {
            this.c++;
        }
        Context context = this.a;
        int i2 = i == 1 ? 1 : 0;
        com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(context);
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.b = i;
        userInfoBean.c = aVarA.d;
        userInfoBean.d = aVarA.f();
        userInfoBean.e = System.currentTimeMillis();
        userInfoBean.f = -1L;
        userInfoBean.n = aVarA.i;
        userInfoBean.o = i2;
        userInfoBean.l = aVarA.n;
        userInfoBean.m = aVarA.o;
        userInfoBean.g = aVarA.p;
        userInfoBean.h = aVarA.q;
        userInfoBean.i = aVarA.r;
        userInfoBean.k = aVarA.s;
        userInfoBean.r = aVarA.y();
        userInfoBean.s = aVarA.C();
        userInfoBean.p = aVarA.D();
        userInfoBean.q = aVarA.E();
        v.a().a(new RunnableC0038a(userInfoBean, z), 0L);
    }

    public final void a() {
        this.b = com.tencent.bugly.proguard.a.c() + 86400000;
        v.a().a(new b(), (this.b - System.currentTimeMillis()) + 5000);
    }

    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.biz.a$a, reason: collision with other inner class name */
    class RunnableC0038a implements Runnable {
        private boolean a;
        private UserInfoBean b;

        public RunnableC0038a(UserInfoBean userInfoBean, boolean z) {
            this.b = userInfoBean;
            this.a = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            com.tencent.bugly.crashreport.common.info.a aVarA;
            try {
                if (this.b != null) {
                    UserInfoBean userInfoBean = this.b;
                    if (userInfoBean != null && (aVarA = com.tencent.bugly.crashreport.common.info.a.a()) != null) {
                        userInfoBean.j = aVarA.d();
                    }
                    w.c("record userinfo", new Object[0]);
                    a.a(a.this, this.b);
                }
                if (this.a) {
                    a.this.b();
                }
            } catch (Throwable th) {
                if (w.a(th)) {
                    return;
                }
                th.printStackTrace();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00c7 A[Catch: all -> 0x0131, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0016, B:8:0x001f, B:10:0x0026, B:11:0x0029, B:13:0x002f, B:15:0x0043, B:16:0x0053, B:20:0x005b, B:21:0x0065, B:22:0x006a, B:24:0x0070, B:26:0x007e, B:28:0x008b, B:29:0x008e, B:31:0x009c, B:33:0x00a0, B:35:0x00a5, B:38:0x00ac, B:41:0x00c1, B:43:0x00c7, B:46:0x00ce, B:49:0x00d5, B:53:0x00ec, B:55:0x00f2, B:58:0x00fb, B:60:0x0101, B:63:0x010a, B:65:0x0114, B:68:0x011d, B:39:0x00bb), top: B:76:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00f2 A[Catch: all -> 0x0131, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0016, B:8:0x001f, B:10:0x0026, B:11:0x0029, B:13:0x002f, B:15:0x0043, B:16:0x0053, B:20:0x005b, B:21:0x0065, B:22:0x006a, B:24:0x0070, B:26:0x007e, B:28:0x008b, B:29:0x008e, B:31:0x009c, B:33:0x00a0, B:35:0x00a5, B:38:0x00ac, B:41:0x00c1, B:43:0x00c7, B:46:0x00ce, B:49:0x00d5, B:53:0x00ec, B:55:0x00f2, B:58:0x00fb, B:60:0x0101, B:63:0x010a, B:65:0x0114, B:68:0x011d, B:39:0x00bb), top: B:76:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00fb A[Catch: all -> 0x0131, TRY_ENTER, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0016, B:8:0x001f, B:10:0x0026, B:11:0x0029, B:13:0x002f, B:15:0x0043, B:16:0x0053, B:20:0x005b, B:21:0x0065, B:22:0x006a, B:24:0x0070, B:26:0x007e, B:28:0x008b, B:29:0x008e, B:31:0x009c, B:33:0x00a0, B:35:0x00a5, B:38:0x00ac, B:41:0x00c1, B:43:0x00c7, B:46:0x00ce, B:49:0x00d5, B:53:0x00ec, B:55:0x00f2, B:58:0x00fb, B:60:0x0101, B:63:0x010a, B:65:0x0114, B:68:0x011d, B:39:0x00bb), top: B:76:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized void b() {
        /*
            Method dump skipped, instructions count: 308
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.biz.a.b():void");
    }

    /* compiled from: BUGLY */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (jCurrentTimeMillis < a.this.b) {
                v.a().a(a.this.new b(), (a.this.b - jCurrentTimeMillis) + 5000);
                return;
            }
            a.b(a.this);
            a.this.a(3, false, 0L);
            a.this.a();
        }
    }

    /* compiled from: BUGLY */
    class c implements Runnable {
        private long a;

        public c(long j) {
            this.a = 21600000L;
            this.a = j;
        }

        @Override // java.lang.Runnable
        public final void run() {
            a.this.b();
            a aVar = a.this;
            long j = this.a;
            v.a().a(aVar.new c(j), j);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:11:0x001b  */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r13v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.tencent.bugly.crashreport.biz.UserInfoBean> a(java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 231
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.biz.a.a(java.lang.String):java.util.List");
    }

    private static void a(List<UserInfoBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (UserInfoBean userInfoBean : list) {
            sb.append(" or _id");
            sb.append(" = ");
            sb.append(userInfoBean.a);
        }
        String string = sb.toString();
        if (string.length() > 0) {
            string = string.substring(4);
        }
        String str = string;
        sb.setLength(0);
        try {
            w.c("deleted %s data %d", "t_ui", Integer.valueOf(o.a().a("t_ui", str, (String[]) null, (n) null, true)));
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    private static ContentValues a(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (userInfoBean.a > 0) {
                contentValues.put("_id", Long.valueOf(userInfoBean.a));
            }
            contentValues.put("_tm", Long.valueOf(userInfoBean.e));
            contentValues.put("_ut", Long.valueOf(userInfoBean.f));
            contentValues.put("_tp", Integer.valueOf(userInfoBean.b));
            contentValues.put("_pc", userInfoBean.c);
            Parcel parcelObtain = Parcel.obtain();
            userInfoBean.writeToParcel(parcelObtain, 0);
            byte[] bArrMarshall = parcelObtain.marshall();
            parcelObtain.recycle();
            contentValues.put("_dt", bArrMarshall);
            return contentValues;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static UserInfoBean a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            byte[] blob = cursor.getBlob(cursor.getColumnIndex("_dt"));
            if (blob == null) {
                return null;
            }
            long j = cursor.getLong(cursor.getColumnIndex("_id"));
            UserInfoBean userInfoBean = (UserInfoBean) com.tencent.bugly.proguard.a.a(blob, UserInfoBean.CREATOR);
            if (userInfoBean != null) {
                userInfoBean.a = j;
            }
            return userInfoBean;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }
}
