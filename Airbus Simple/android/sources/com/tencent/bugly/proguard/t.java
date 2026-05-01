package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.util.Base64;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import javax.crypto.NoSuchPaddingException;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class t {
    private static t a;
    private final Context c;
    private String g;
    private boolean q;
    private Map<Integer, Long> d = new HashMap();
    private LinkedBlockingQueue<Runnable> e = new LinkedBlockingQueue<>();
    private final Object f = new Object();
    private byte[] h = null;
    private long i = 0;
    private byte[] j = null;
    private long k = 0;
    private String l = null;
    private long m = 0;
    private final Object n = new Object();
    private boolean o = false;
    private final Object p = new Object();
    private final o b = o.a();

    static /* synthetic */ boolean a(t tVar, boolean z) {
        tVar.o = false;
        return false;
    }

    private t(Context context) throws ClassNotFoundException {
        this.g = null;
        this.q = true;
        this.c = context;
        try {
            Class.forName("android.util.Base64");
        } catch (ClassNotFoundException unused) {
            w.a("[UploadManager] can not find Base64 class, will not use stronger security way to upload", new Object[0]);
            this.q = false;
        }
        if (this.q) {
            this.g = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDP9x32s5pPtZBXzJBz2GWM/sbTvVO2+RvW0PH01IdaBxc/fB6fbHZocC9T3nl1+J5eAFjIRVuV8vHDky7Qo82Mnh0PVvcZIEQvMMVKU8dsMQopxgsOs2gkSHJwgWdinKNS8CmWobo6pFwPUW11lMv714jAUZRq2GBOqiO2vQI6iwIDAQAB";
        }
    }

    public static synchronized t a(Context context) {
        if (a == null) {
            a = new t(context);
        }
        return a;
    }

    public static synchronized t a() {
        return a;
    }

    public final void a(int i, as asVar, String str, s sVar, boolean z) {
        a(i, asVar, null, sVar, false, 0L, z);
    }

    public final void a(int i, int i2, byte[] bArr, String str, s sVar) {
        try {
            if (this.q) {
                a((Runnable) new u(this.c, i, i2, bArr, (String) null, sVar, true), false, 0L);
            } else {
                v.a().b(new u(this.c, i, i2, bArr, (String) null, sVar, false));
            }
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    public final void a(int i, as asVar, String str, s sVar, boolean z, long j, boolean z2) {
        try {
            if (this.q) {
                a(new u(this.c, i, asVar, str, sVar, true, z2), z, j);
                return;
            }
            u uVar = new u(this.c, i, asVar, str, sVar, false, z2);
            if (z) {
                a(uVar, j);
            } else {
                v.a().b(uVar);
            }
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final long a(boolean r12) {
        /*
            r11 = this;
            long r0 = com.tencent.bugly.proguard.a.c()
            if (r12 == 0) goto L8
            r12 = 5
            goto L9
        L8:
            r12 = 3
        L9:
            com.tencent.bugly.proguard.o r2 = r11.b
            java.util.List r2 = r2.a(r12)
            r3 = 1
            r4 = 0
            r5 = 0
            if (r2 == 0) goto L4e
            int r7 = r2.size()
            if (r7 <= 0) goto L4e
            java.lang.Object r7 = r2.get(r4)     // Catch: java.lang.Throwable -> L36
            com.tencent.bugly.proguard.q r7 = (com.tencent.bugly.proguard.q) r7     // Catch: java.lang.Throwable -> L36
            long r8 = r7.e     // Catch: java.lang.Throwable -> L36
            int r10 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r10 < 0) goto L33
            byte[] r0 = r7.g     // Catch: java.lang.Throwable -> L36
            long r0 = com.tencent.bugly.proguard.a.d(r0)     // Catch: java.lang.Throwable -> L36
            r2.remove(r7)     // Catch: java.lang.Throwable -> L31
            goto L34
        L31:
            r5 = r0
            goto L36
        L33:
            r0 = r5
        L34:
            r5 = r0
            goto L43
        L36:
            java.lang.String r0 = "error local type %d"
            java.lang.Object[] r1 = new java.lang.Object[r3]
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)
            r1[r4] = r12
            com.tencent.bugly.proguard.w.e(r0, r1)
        L43:
            int r12 = r2.size()
            if (r12 <= 0) goto L4e
            com.tencent.bugly.proguard.o r12 = r11.b
            com.tencent.bugly.proguard.o.a(r2)
        L4e:
            java.lang.String r12 = "consume getted %d"
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.Long r1 = java.lang.Long.valueOf(r5)
            r0[r4] = r1
            com.tencent.bugly.proguard.w.c(r12, r0)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.t.a(boolean):long");
    }

    protected final synchronized void a(long j, boolean z) {
        int i = z ? 5 : 3;
        q qVar = new q();
        qVar.b = i;
        qVar.e = com.tencent.bugly.proguard.a.c();
        qVar.c = "";
        qVar.d = "";
        qVar.g = com.tencent.bugly.proguard.a.a(j);
        o oVar = this.b;
        o.b(i);
        this.b.a(qVar);
        w.c("consume update %d", Long.valueOf(j));
    }

    public final synchronized void a(int i, long j) {
        if (i >= 0) {
            this.d.put(Integer.valueOf(i), Long.valueOf(j));
            w.c("up %d %d", Integer.valueOf(i), Long.valueOf(j));
        } else {
            w.e("unknown up %d", Integer.valueOf(i));
        }
    }

    public final synchronized long a(int i) {
        if (i >= 0) {
            Long l = this.d.get(Integer.valueOf(i));
            if (l == null) {
                return -2L;
            }
            return l.longValue();
        }
        w.e("unknown up %d", Integer.valueOf(i));
        return -2L;
    }

    private static boolean c() {
        w.c("[UploadManager] drop security info of database (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        try {
            o oVarA = o.a();
            if (oVarA == null) {
                w.d("[UploadManager] failed to get Database", new Object[0]);
                return false;
            }
            return oVarA.a(555, "security_info", (n) null, true);
        } catch (Throwable th) {
            w.a(th);
            return false;
        }
    }

    private boolean d() {
        w.c("[UploadManager] record security info to database (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        try {
            o oVarA = o.a();
            if (oVarA == null) {
                w.d("[UploadManager] failed to get Database", new Object[0]);
                return false;
            }
            StringBuilder sb = new StringBuilder();
            if (this.j != null) {
                sb.append(Base64.encodeToString(this.j, 0));
                sb.append("#");
                if (this.m != 0) {
                    sb.append(Long.toString(this.k));
                } else {
                    sb.append("null");
                }
                sb.append("#");
                if (this.l != null) {
                    sb.append(this.l);
                } else {
                    sb.append("null");
                }
                sb.append("#");
                if (this.m != 0) {
                    sb.append(Long.toString(this.m));
                } else {
                    sb.append("null");
                }
                oVarA.a(555, "security_info", sb.toString().getBytes(), (n) null, true);
                return true;
            }
            w.c("[UploadManager] AES key is null, will not record", new Object[0]);
            return false;
        } catch (Throwable th) {
            w.a(th);
            c();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00e6 A[Catch: Throwable -> 0x00ea, TRY_LEAVE, TryCatch #0 {Throwable -> 0x00ea, blocks: (B:3:0x001e, B:5:0x0024, B:7:0x002c, B:9:0x0035, B:11:0x003d, B:13:0x0054, B:15:0x005c, B:20:0x0070, B:23:0x0078, B:25:0x0080, B:30:0x0094, B:32:0x009a, B:34:0x00a2, B:36:0x00ac, B:38:0x00b2, B:40:0x00bb, B:45:0x00cf, B:49:0x00e6, B:46:0x00d3, B:17:0x0066, B:42:0x00c5, B:27:0x008a), top: B:54:0x001e, inners: #1, #2, #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean e() {
        /*
            Method dump skipped, instructions count: 239
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.t.e():boolean");
    }

    protected final boolean b() {
        if (this.l == null || this.m == 0) {
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis() + this.i;
        if (this.m >= jCurrentTimeMillis) {
            return true;
        }
        w.c("[UploadManager] session ID expired time from server is: %d(%s), but now is: %d(%s)", Long.valueOf(this.m), new Date(this.m).toString(), Long.valueOf(jCurrentTimeMillis), new Date(jCurrentTimeMillis).toString());
        return false;
    }

    protected final void b(boolean z) {
        synchronized (this.n) {
            w.c("[UploadManager] clear security context (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            this.j = null;
            this.l = null;
            this.m = 0L;
        }
        if (z) {
            c();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(int i) {
        if (i < 0) {
            w.a("[UploadManager] number of task to execute should >= 0", new Object[0]);
            return false;
        }
        synchronized (this.f) {
            if (this.e.isEmpty()) {
                return true;
            }
            w.c("[UploadManager] execute upload tasks of queue which has %d tasks (pid=%d | tid=%d)", Integer.valueOf(this.e.size()), Integer.valueOf(Process.myPid()), Long.valueOf(Thread.currentThread().getId()));
            if (i == 0 || i > this.e.size()) {
                i = this.e.size();
            }
            v vVarA = v.a();
            if (vVarA == null) {
                w.d("[UploadManager] async task handler has not been initialized", new Object[0]);
                return false;
            }
            for (int i2 = 0; i2 < i; i2++) {
                try {
                    Runnable runnablePoll = this.e.poll();
                    if (runnablePoll != null) {
                        vVarA.c(runnablePoll);
                    } else {
                        w.d("[UploadManager] upload task poll from queue is null", new Object[0]);
                    }
                } catch (Throwable th) {
                    w.e("[UploadManager] failed to execute upload task with message: %s", th.getMessage());
                    return false;
                }
            }
            return true;
        }
    }

    private boolean a(Runnable runnable) {
        if (runnable == null) {
            w.d("[UploadManager] upload task should not be null", new Object[0]);
            return false;
        }
        try {
            w.c("[UploadManager] add upload task to queue (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            synchronized (this.f) {
                this.e.put(runnable);
            }
            return true;
        } catch (Throwable th) {
            w.e("[UploadManager] failed to add upload task to queue: %s", th.getMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Runnable runnable, long j) {
        if (runnable == null) {
            w.d("[UploadManager] upload task should not be null", new Object[0]);
            return;
        }
        w.c("[UploadManager] execute synchronized upload task (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        Thread thread = new Thread(runnable);
        thread.setName("BuglySyncUploadThread");
        thread.start();
        try {
            thread.join(j);
        } catch (Throwable th) {
            w.e("[UploadManager] failed to execute upload synchronized task with message: %s. Add it to queue", th.getMessage());
            a(runnable);
        }
    }

    private void a(Runnable runnable, boolean z, long j) {
        if (runnable == null) {
            w.d("[UploadManager] upload task should not be null", new Object[0]);
        }
        v vVarA = v.a();
        if (vVarA == null) {
            w.d("[UploadManager] async task handler has not been initialized", new Object[0]);
        }
        w.c("[UploadManager] add upload task (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        if (this.l != null) {
            if (b()) {
                w.c("[UploadManager] sucessfully got session ID, try to execute upload task now (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                if (z) {
                    a(runnable, j);
                } else if (vVarA == null) {
                    a(runnable);
                } else {
                    vVarA.c(runnable);
                }
                b(0);
                return;
            }
            w.a("[UploadManager] session ID is expired, drop it (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            b(false);
        }
        synchronized (this.p) {
            if (!this.o) {
                this.o = true;
                w.c("[UploadManager] try to request session ID now (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                if (z) {
                    a(new a(this.c, runnable, j), 0L);
                } else {
                    a(runnable);
                    vVarA.c(new a(this, this.c));
                }
            } else {
                a(runnable);
            }
        }
    }

    public final void a(int i, at atVar) {
        if (this.q) {
            boolean z = true;
            if (i == 2) {
                w.c("[UploadManager] Session ID is invalid, will clear security context (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                b(true);
            } else {
                if (!this.o) {
                    return;
                }
                if (atVar != null) {
                    w.c("[UploadManager] record security context (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                    try {
                        Map<String, String> map = atVar.h;
                        if (map != null && map.containsKey("S1") && map.containsKey("S2")) {
                            this.i = atVar.e - System.currentTimeMillis();
                            w.c("[UploadManager] time lag of server is: %d", Long.valueOf(this.i));
                            this.l = map.get("S1");
                            w.c("[UploadManager] session ID from server is: %s", this.l);
                            if (this.l.length() > 0) {
                                try {
                                    this.m = Long.parseLong(map.get("S2"));
                                    w.c("[UploadManager] session expired time from server is: %d(%s)", Long.valueOf(this.m), new Date(this.m).toString());
                                    if (this.m < 1000) {
                                        w.d("[UploadManager] session expired time from server is less than 1 second, will set to default value", new Object[0]);
                                        this.m = 259200000L;
                                    }
                                } catch (NumberFormatException unused) {
                                    w.d("[UploadManager] session expired time is invalid, will set to default value", new Object[0]);
                                    this.m = 259200000L;
                                }
                                b(0);
                                if (d()) {
                                    z = false;
                                } else {
                                    w.c("[UploadManager] failed to record database", new Object[0]);
                                }
                            } else {
                                w.c("[UploadManager] session ID from server is invalid, try next time", new Object[0]);
                            }
                        }
                    } catch (Throwable th) {
                        w.a(th);
                    }
                    if (z) {
                        b(false);
                    }
                } else {
                    w.c("[UploadManager] fail to init security context and clear local info (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                    b(false);
                }
            }
            synchronized (this.p) {
                if (this.o) {
                    this.o = false;
                    com.tencent.bugly.proguard.a.b(this.c, "security_info");
                }
            }
        }
    }

    /* compiled from: BUGLY */
    class a implements Runnable {
        private final Context a;
        private final Runnable b;
        private final long c;

        public a(t tVar, Context context) {
            this(context, null, 0L);
        }

        public a(Context context, Runnable runnable, long j) {
            this.a = context;
            this.b = runnable;
            this.c = j;
        }

        @Override // java.lang.Runnable
        public final void run() throws InterruptedException {
            if (com.tencent.bugly.proguard.a.a(this.a, "security_info", 30000L)) {
                if (!t.this.e()) {
                    w.d("[UploadManager] failed to load security info from database", new Object[0]);
                    t.this.b(false);
                }
                if (t.this.l != null) {
                    if (t.this.b()) {
                        w.c("[UploadManager] sucessfully got session ID, try to execute upload tasks now (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                        if (this.b != null) {
                            t.this.a(this.b, this.c);
                        }
                        t.this.b(0);
                        com.tencent.bugly.proguard.a.b(this.a, "security_info");
                        synchronized (t.this.p) {
                            t.a(t.this, false);
                        }
                        return;
                    }
                    w.a("[UploadManager] session ID is expired, drop it", new Object[0]);
                    t.this.b(true);
                }
                byte[] bArrA = com.tencent.bugly.proguard.a.a(128);
                if (bArrA != null && (bArrA.length << 3) == 128) {
                    t.this.j = bArrA;
                    w.c("[UploadManager] execute one upload task for requesting session ID (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                    if (this.b != null) {
                        t.this.a(this.b, this.c);
                        return;
                    } else if (t.this.b(1)) {
                        return;
                    } else {
                        w.d("[UploadManager] failed to execute one upload task (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                    }
                } else {
                    w.d("[UploadManager] failed to create AES key (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                }
                t.this.b(false);
                com.tencent.bugly.proguard.a.b(this.a, "security_info");
                synchronized (t.this.p) {
                    t.a(t.this, false);
                }
                return;
            }
            v vVarA = v.a();
            if (vVarA == null) {
                w.e("[UploadManager] async task handler has not been initialized", new Object[0]);
                return;
            }
            w.c("[UploadManager] sleep %d try to lock security file again", 5000);
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            vVarA.c(new a(t.this, t.this.c));
        }
    }

    public final byte[] a(byte[] bArr) {
        if (this.j == null || (this.j.length << 3) != 128) {
            w.d("[UploadManager] AES key is invalid (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            return null;
        }
        return com.tencent.bugly.proguard.a.a(1, bArr, this.j);
    }

    public final byte[] b(byte[] bArr) {
        if (this.j == null || (this.j.length << 3) != 128) {
            w.d("[UploadManager] AES key is invalid (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            return null;
        }
        return com.tencent.bugly.proguard.a.a(2, bArr, this.j);
    }

    public final boolean a(Map<String, String> map) throws InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        if (map == null) {
            return false;
        }
        w.c("[UploadManager] integrate security to HTTP headers (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        if (this.l != null) {
            map.put("secureSessionId", this.l);
            return true;
        }
        if (this.j == null || (this.j.length << 3) != 128) {
            w.d("[UploadManager] AES key is invalid", new Object[0]);
            return false;
        }
        if (this.h == null) {
            this.h = Base64.decode(this.g, 0);
            if (this.h == null) {
                w.d("[UploadManager] failed to decode RSA public key", new Object[0]);
                return false;
            }
        }
        byte[] bArrB = com.tencent.bugly.proguard.a.b(1, this.j, this.h);
        if (bArrB == null) {
            w.d("[UploadManager] failed to encrypt AES key", new Object[0]);
            return false;
        }
        String strEncodeToString = Base64.encodeToString(bArrB, 0);
        if (strEncodeToString == null) {
            w.d("[UploadManager] failed to encode AES key", new Object[0]);
            return false;
        }
        map.put("raKey", strEncodeToString);
        return true;
    }
}
