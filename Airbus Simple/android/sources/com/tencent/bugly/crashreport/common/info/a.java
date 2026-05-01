package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.v4.os.EnvironmentCompat;
import com.litesuits.http.data.Consts;
import com.tencent.bugly.proguard.w;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a {
    private static a W;
    private final Context A;
    private String B;
    private String C;
    private String S;
    public final byte b;
    public String c;
    public final String d;
    public final String e;
    public final String f;
    public final String g;
    public long h;
    public String i;
    public String j;
    public List<String> m;
    public boolean n;
    public boolean t;
    public String u;
    public String v;
    public String w;
    public boolean y;
    private String D = EnvironmentCompat.MEDIA_UNKNOWN;
    private String E = EnvironmentCompat.MEDIA_UNKNOWN;
    private String F = "";
    private String G = null;
    private String H = null;
    private String I = null;
    private String J = null;
    private long K = -1;
    private long L = -1;
    private long M = -1;
    private String N = null;
    private String O = null;
    private Map<String, PlugInBean> P = null;
    private boolean Q = true;
    private String R = null;
    private Boolean T = null;
    private String U = null;
    public String k = null;
    public String l = null;
    private Map<String, PlugInBean> V = null;
    private int X = -1;
    private int Y = -1;
    private Map<String, String> Z = new HashMap();
    private Map<String, String> aa = new HashMap();
    public String o = EnvironmentCompat.MEDIA_UNKNOWN;
    public long p = 0;
    public long q = 0;
    public long r = 0;
    public long s = 0;
    public boolean x = false;
    public HashMap<String, String> z = new HashMap<>();
    private String ab = null;
    private String ac = null;
    private String ad = null;
    private String ae = null;
    private final Object af = new Object();
    private final Object ag = new Object();
    private final Object ah = new Object();
    public final long a = System.currentTimeMillis();

    public static String b() {
        return "2.2.0";
    }

    private a(Context context) {
        Context applicationContext;
        this.i = null;
        this.S = null;
        this.j = null;
        this.m = null;
        this.t = false;
        this.u = null;
        this.v = null;
        this.w = null;
        this.y = false;
        this.A = (context == null || (applicationContext = context.getApplicationContext()) == null) ? context : applicationContext;
        this.b = (byte) 1;
        PackageInfo packageInfoB = AppInfo.b(context);
        if (packageInfoB != null) {
            this.i = AppInfo.a(packageInfoB);
            this.u = this.i;
            this.v = AppInfo.b(packageInfoB);
        }
        this.c = AppInfo.a(context);
        this.d = AppInfo.c(context);
        this.e = b.l();
        this.f = b.a();
        this.g = "Android " + b.b() + ",level " + b.c();
        String str = this.f + ";" + this.g;
        Map<String, String> mapD = AppInfo.d(context);
        if (mapD != null) {
            try {
                this.m = AppInfo.a(mapD);
                String str2 = mapD.get("BUGLY_APPID");
                if (str2 != null) {
                    this.S = str2;
                }
                String str3 = mapD.get("BUGLY_APP_VERSION");
                if (str3 != null) {
                    this.i = str3;
                }
                String str4 = mapD.get("BUGLY_APP_CHANNEL");
                if (str4 != null) {
                    this.j = str4;
                }
                String str5 = mapD.get("BUGLY_ENABLE_DEBUG");
                if (str5 != null) {
                    this.t = str5.toLowerCase().equals("true");
                }
                String str6 = mapD.get("com.tencent.rdm.uuid");
                if (str6 != null) {
                    this.w = str6;
                }
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        try {
            if (!context.getDatabasePath("bugly_db_").exists()) {
                this.y = true;
                w.c("App is first time to be installed on the device.", new Object[0]);
            }
        } catch (Throwable th2) {
            if (com.tencent.bugly.b.b) {
                th2.printStackTrace();
            }
        }
        w.c("com info create end", new Object[0]);
    }

    public static synchronized a a(Context context) {
        if (W == null) {
            W = new a(context);
        }
        return W;
    }

    public static synchronized a a() {
        return W;
    }

    public final void c() {
        synchronized (this.af) {
            this.B = UUID.randomUUID().toString();
        }
    }

    public final String d() {
        if (this.B == null) {
            synchronized (this.af) {
                if (this.B == null) {
                    this.B = UUID.randomUUID().toString();
                }
            }
        }
        return this.B;
    }

    public final String e() {
        return this.S;
    }

    public final void a(String str) {
        this.S = str;
    }

    public final synchronized String f() {
        return this.D;
    }

    public final synchronized void b(String str) {
        if (str == null) {
            str = "10000";
        }
        try {
            this.D = str;
        } catch (Throwable th) {
            throw th;
        }
    }

    public final synchronized String g() {
        if (this.C != null) {
            return this.C;
        }
        this.C = j() + "|" + l() + "|" + m();
        return this.C;
    }

    public final synchronized void c(String str) {
        this.C = str;
    }

    public final synchronized String h() {
        return this.E;
    }

    public final synchronized void d(String str) {
        this.E = str;
    }

    public final synchronized String i() {
        return this.F;
    }

    public final synchronized void e(String str) {
        this.F = str;
    }

    public final synchronized String j() {
        if (!this.Q) {
            return "";
        }
        if (this.G == null) {
            this.G = b.a(this.A);
        }
        return this.G;
    }

    public final synchronized String k() {
        if (!this.Q) {
            return "";
        }
        if (this.H == null) {
            this.H = b.d(this.A);
        }
        return this.H;
    }

    public final synchronized String l() {
        if (!this.Q) {
            return "";
        }
        if (this.I == null) {
            this.I = b.b(this.A);
        }
        return this.I;
    }

    public final synchronized String m() {
        if (!this.Q) {
            return "";
        }
        if (this.J == null) {
            this.J = b.c(this.A);
        }
        return this.J;
    }

    public final synchronized long n() {
        if (this.K <= 0) {
            this.K = b.e();
        }
        return this.K;
    }

    public final synchronized long o() {
        if (this.L <= 0) {
            this.L = b.g();
        }
        return this.L;
    }

    public final synchronized long p() {
        if (this.M <= 0) {
            this.M = b.i();
        }
        return this.M;
    }

    public final synchronized String q() {
        if (this.N == null) {
            this.N = b.d();
        }
        return this.N;
    }

    public final String r() {
        if (this.O == null) {
            synchronized (this.ag) {
                if (this.O == null) {
                    this.O = b.g(this.A);
                }
            }
        }
        return this.O;
    }

    public final void a(String str, String str2) {
        if (str == null || str2 == null) {
            return;
        }
        synchronized (this.ah) {
            this.z.put(str, str2);
        }
    }

    public final void s() {
        try {
            Map<String, ?> all = this.A.getSharedPreferences("BuglySdkInfos", 0).getAll();
            if (!all.isEmpty()) {
                synchronized (this.ah) {
                    for (Map.Entry<String, ?> entry : all.entrySet()) {
                        try {
                            this.z.put(entry.getKey(), entry.getValue().toString());
                        } catch (Throwable th) {
                            w.a(th);
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            w.a(th2);
        }
        if (this.z.isEmpty()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry2 : this.z.entrySet()) {
            sb.append(Consts.ARRAY_ECLOSING_LEFT);
            sb.append(entry2.getKey());
            sb.append(Consts.SECOND_LEVEL_SPLIT);
            sb.append(entry2.getValue());
            sb.append("] ");
        }
        c("SDK_INFO", sb.toString());
    }

    public final String t() {
        if (this.ae == null) {
            this.ae = AppInfo.e(this.A);
        }
        return this.ae;
    }

    public final synchronized Map<String, PlugInBean> u() {
        return null;
    }

    public final String v() {
        if (this.R == null) {
            this.R = b.k();
        }
        return this.R;
    }

    public final synchronized Boolean w() {
        if (this.T == null) {
            this.T = Boolean.valueOf(b.h(this.A));
        }
        return this.T;
    }

    public final synchronized String x() {
        if (this.U == null) {
            this.U = b.f(this.A);
            w.a("rom:%s", this.U);
        }
        return this.U;
    }

    public final synchronized Map<String, String> y() {
        if (this.Z.size() <= 0) {
            return null;
        }
        return new HashMap(this.Z);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized java.lang.String f(java.lang.String r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            r0 = 0
            r1 = 1
            if (r5 == 0) goto L13
            java.lang.String r2 = r5.trim()     // Catch: java.lang.Throwable -> L11
            int r2 = r2.length()     // Catch: java.lang.Throwable -> L11
            if (r2 <= 0) goto L13
            r2 = 0
            goto L14
        L11:
            r5 = move-exception
            goto L38
        L13:
            r2 = 1
        L14:
            if (r2 == 0) goto L2e
            java.lang.String r2 = "key should not be empty %s"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L11
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L11
            r3.<init>()     // Catch: java.lang.Throwable -> L11
            r3.append(r5)     // Catch: java.lang.Throwable -> L11
            java.lang.String r5 = r3.toString()     // Catch: java.lang.Throwable -> L11
            r1[r0] = r5     // Catch: java.lang.Throwable -> L11
            com.tencent.bugly.proguard.w.d(r2, r1)     // Catch: java.lang.Throwable -> L11
            r5 = 0
            monitor-exit(r4)
            return r5
        L2e:
            java.util.Map<java.lang.String, java.lang.String> r0 = r4.Z     // Catch: java.lang.Throwable -> L11
            java.lang.Object r5 = r0.remove(r5)     // Catch: java.lang.Throwable -> L11
            java.lang.String r5 = (java.lang.String) r5     // Catch: java.lang.Throwable -> L11
            monitor-exit(r4)
            return r5
        L38:
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.a.f(java.lang.String):java.lang.String");
    }

    public final synchronized void z() {
        this.Z.clear();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized java.lang.String g(java.lang.String r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            r0 = 0
            r1 = 1
            if (r5 == 0) goto L13
            java.lang.String r2 = r5.trim()     // Catch: java.lang.Throwable -> L11
            int r2 = r2.length()     // Catch: java.lang.Throwable -> L11
            if (r2 <= 0) goto L13
            r2 = 0
            goto L14
        L11:
            r5 = move-exception
            goto L38
        L13:
            r2 = 1
        L14:
            if (r2 == 0) goto L2e
            java.lang.String r2 = "key should not be empty %s"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L11
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L11
            r3.<init>()     // Catch: java.lang.Throwable -> L11
            r3.append(r5)     // Catch: java.lang.Throwable -> L11
            java.lang.String r5 = r3.toString()     // Catch: java.lang.Throwable -> L11
            r1[r0] = r5     // Catch: java.lang.Throwable -> L11
            com.tencent.bugly.proguard.w.d(r2, r1)     // Catch: java.lang.Throwable -> L11
            r5 = 0
            monitor-exit(r4)
            return r5
        L2e:
            java.util.Map<java.lang.String, java.lang.String> r0 = r4.Z     // Catch: java.lang.Throwable -> L11
            java.lang.Object r5 = r0.get(r5)     // Catch: java.lang.Throwable -> L11
            java.lang.String r5 = (java.lang.String) r5     // Catch: java.lang.Throwable -> L11
            monitor-exit(r4)
            return r5
        L38:
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.a.g(java.lang.String):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized void b(java.lang.String r6, java.lang.String r7) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 0
            r1 = 1
            if (r6 == 0) goto L13
            java.lang.String r2 = r6.trim()     // Catch: java.lang.Throwable -> L11
            int r2 = r2.length()     // Catch: java.lang.Throwable -> L11
            if (r2 <= 0) goto L13
            r2 = 0
            goto L14
        L11:
            r6 = move-exception
            goto L55
        L13:
            r2 = 1
        L14:
            if (r2 != 0) goto L2f
            if (r7 == 0) goto L24
            java.lang.String r2 = r7.trim()     // Catch: java.lang.Throwable -> L11
            int r2 = r2.length()     // Catch: java.lang.Throwable -> L11
            if (r2 <= 0) goto L24
            r2 = 0
            goto L25
        L24:
            r2 = 1
        L25:
            if (r2 == 0) goto L28
            goto L2f
        L28:
            java.util.Map<java.lang.String, java.lang.String> r0 = r5.Z     // Catch: java.lang.Throwable -> L11
            r0.put(r6, r7)     // Catch: java.lang.Throwable -> L11
            monitor-exit(r5)
            return
        L2f:
            java.lang.String r2 = "key&value should not be empty %s %s"
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L11
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L11
            r4.<init>()     // Catch: java.lang.Throwable -> L11
            r4.append(r6)     // Catch: java.lang.Throwable -> L11
            java.lang.String r6 = r4.toString()     // Catch: java.lang.Throwable -> L11
            r3[r0] = r6     // Catch: java.lang.Throwable -> L11
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L11
            r6.<init>()     // Catch: java.lang.Throwable -> L11
            r6.append(r7)     // Catch: java.lang.Throwable -> L11
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> L11
            r3[r1] = r6     // Catch: java.lang.Throwable -> L11
            com.tencent.bugly.proguard.w.d(r2, r3)     // Catch: java.lang.Throwable -> L11
            monitor-exit(r5)
            return
        L55:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.a.b(java.lang.String, java.lang.String):void");
    }

    public final synchronized int A() {
        return this.Z.size();
    }

    public final synchronized Set<String> B() {
        return this.Z.keySet();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized void c(java.lang.String r6, java.lang.String r7) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 0
            r1 = 1
            if (r6 == 0) goto L13
            java.lang.String r2 = r6.trim()     // Catch: java.lang.Throwable -> L11
            int r2 = r2.length()     // Catch: java.lang.Throwable -> L11
            if (r2 <= 0) goto L13
            r2 = 0
            goto L14
        L11:
            r6 = move-exception
            goto L55
        L13:
            r2 = 1
        L14:
            if (r2 != 0) goto L2f
            if (r7 == 0) goto L24
            java.lang.String r2 = r7.trim()     // Catch: java.lang.Throwable -> L11
            int r2 = r2.length()     // Catch: java.lang.Throwable -> L11
            if (r2 <= 0) goto L24
            r2 = 0
            goto L25
        L24:
            r2 = 1
        L25:
            if (r2 == 0) goto L28
            goto L2f
        L28:
            java.util.Map<java.lang.String, java.lang.String> r0 = r5.aa     // Catch: java.lang.Throwable -> L11
            r0.put(r6, r7)     // Catch: java.lang.Throwable -> L11
            monitor-exit(r5)
            return
        L2f:
            java.lang.String r2 = "server key&value should not be empty %s %s"
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L11
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L11
            r4.<init>()     // Catch: java.lang.Throwable -> L11
            r4.append(r6)     // Catch: java.lang.Throwable -> L11
            java.lang.String r6 = r4.toString()     // Catch: java.lang.Throwable -> L11
            r3[r0] = r6     // Catch: java.lang.Throwable -> L11
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L11
            r6.<init>()     // Catch: java.lang.Throwable -> L11
            r6.append(r7)     // Catch: java.lang.Throwable -> L11
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Throwable -> L11
            r3[r1] = r6     // Catch: java.lang.Throwable -> L11
            com.tencent.bugly.proguard.w.d(r2, r3)     // Catch: java.lang.Throwable -> L11
            monitor-exit(r5)
            return
        L55:
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.a.c(java.lang.String, java.lang.String):void");
    }

    public final synchronized Map<String, String> C() {
        if (this.aa.size() <= 0) {
            return null;
        }
        return new HashMap(this.aa);
    }

    public final synchronized void a(int i) {
        int i2 = this.X;
        if (i2 != i) {
            this.X = i;
            w.a("user scene tag %d changed to tag %d", Integer.valueOf(i2), Integer.valueOf(this.X));
        }
    }

    public final synchronized int D() {
        return this.X;
    }

    public final synchronized int E() {
        return this.Y;
    }

    public final synchronized boolean F() {
        return AppInfo.f(this.A);
    }

    public final synchronized Map<String, PlugInBean> G() {
        return null;
    }

    public final synchronized int H() {
        return b.c();
    }

    public final String I() {
        if (this.ab == null) {
            this.ab = b.m();
        }
        return this.ab;
    }

    public final String J() {
        if (this.ac == null) {
            this.ac = b.i(this.A);
        }
        return this.ac;
    }

    public final String K() {
        return b.j(this.A);
    }

    public final String L() {
        Context context = this.A;
        return b.n();
    }

    public final String M() {
        if (this.ad == null) {
            this.ad = b.k(this.A);
        }
        return this.ad;
    }

    public final long N() {
        Context context = this.A;
        return b.o();
    }
}
