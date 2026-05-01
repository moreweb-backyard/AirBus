package com.tencent.bugly.proguard;

import android.content.Context;
import android.os.Process;
import android.support.v4.media.session.PlaybackStateCompat;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import java.util.HashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class u implements Runnable {
    private int a;
    private int b;
    private final Context c;
    private final int d;
    private final byte[] e;
    private final com.tencent.bugly.crashreport.common.info.a f;
    private final com.tencent.bugly.crashreport.common.strategy.a g;
    private final r h;
    private final t i;
    private final int j;
    private final s k;
    private final s l;
    private String m;
    private int n;
    private long o;
    private long p;
    private boolean q;
    private boolean r;

    public u(Context context, int i, as asVar, String str, s sVar, boolean z, boolean z2) {
        this(context, i, asVar.g, a.a(asVar), str, sVar, z, z2);
    }

    public u(Context context, int i, int i2, byte[] bArr, String str, s sVar, boolean z) {
        this(context, i, i2, bArr, str, sVar, z, 1, BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH, false);
    }

    private u(Context context, int i, int i2, byte[] bArr, String str, s sVar, boolean z, boolean z2) {
        this(context, i, i2, bArr, str, sVar, z, 1, BuglyStrategy.a.MAX_USERDATA_VALUE_LENGTH, z2);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private u(android.content.Context r4, int r5, int r6, byte[] r7, java.lang.String r8, com.tencent.bugly.proguard.s r9, boolean r10, int r11, int r12, boolean r13) {
        /*
            r3 = this;
            r3.<init>()
            r11 = 1
            r3.a = r11
            r12 = 30000(0x7530, float:4.2039E-41)
            r3.b = r12
            java.lang.String r0 = ""
            r3.m = r0
            r0 = 0
            r3.n = r0
            r1 = 0
            r3.o = r1
            r3.p = r1
            r3.q = r11
            r3.r = r0
            r3.c = r4
            com.tencent.bugly.crashreport.common.info.a r0 = com.tencent.bugly.crashreport.common.info.a.a(r4)
            r3.f = r0
            r3.e = r7
            com.tencent.bugly.crashreport.common.strategy.a r7 = com.tencent.bugly.crashreport.common.strategy.a.a()
            r3.g = r7
            com.tencent.bugly.proguard.r r4 = com.tencent.bugly.proguard.r.a(r4)
            r3.h = r4
            com.tencent.bugly.proguard.t r4 = com.tencent.bugly.proguard.t.a()
            r3.i = r4
            r3.j = r5
            r3.m = r8
            r3.k = r9
            com.tencent.bugly.proguard.t r4 = r3.i
            r4 = 0
            r3.l = r4
            r3.q = r10
            if (r10 == 0) goto L58
            r4 = 510(0x1fe, float:7.15E-43)
            if (r6 == r4) goto L56
            r4 = 630(0x276, float:8.83E-43)
            if (r6 == r4) goto L53
            r4 = 640(0x280, float:8.97E-43)
            if (r6 == r4) goto L56
            goto L58
        L53:
            r6 = 830(0x33e, float:1.163E-42)
            goto L58
        L56:
            r6 = 840(0x348, float:1.177E-42)
        L58:
            r3.d = r6
            r3.a = r11
            r3.b = r12
            r3.r = r13
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.u.<init>(android.content.Context, int, int, byte[], java.lang.String, com.tencent.bugly.proguard.s, boolean, int, int, boolean):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0019  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(com.tencent.bugly.proguard.at r5, boolean r6, int r7, java.lang.String r8, int r9) {
        /*
            r4 = this;
            int r5 = r4.d
            r0 = 630(0x276, float:8.83E-43)
            if (r5 == r0) goto L1c
            r0 = 640(0x280, float:8.97E-43)
            if (r5 == r0) goto L19
            r0 = 830(0x33e, float:1.163E-42)
            if (r5 == r0) goto L1c
            r0 = 840(0x348, float:1.177E-42)
            if (r5 == r0) goto L19
            int r5 = r4.d
            java.lang.String r5 = java.lang.String.valueOf(r5)
            goto L1e
        L19:
            java.lang.String r5 = "userinfo"
            goto L1e
        L1c:
            java.lang.String r5 = "crash"
        L1e:
            r0 = 0
            r1 = 1
            if (r6 == 0) goto L2c
            java.lang.String r7 = "[upload] success: %s"
            java.lang.Object[] r8 = new java.lang.Object[r1]
            r8[r0] = r5
            com.tencent.bugly.proguard.w.a(r7, r8)
            goto L49
        L2c:
            java.lang.String r2 = "[upload] fail! %s %d %s"
            r3 = 3
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r0] = r5
            java.lang.Integer r5 = java.lang.Integer.valueOf(r7)
            r3[r1] = r5
            r5 = 2
            r3[r5] = r8
            com.tencent.bugly.proguard.w.e(r2, r3)
            boolean r5 = r4.q
            if (r5 == 0) goto L49
            com.tencent.bugly.proguard.t r5 = r4.i
            r7 = 0
            r5.a(r9, r7)
        L49:
            long r7 = r4.o
            long r0 = r4.p
            long r7 = r7 + r0
            r0 = 0
            int r5 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
            if (r5 <= 0) goto L69
            com.tencent.bugly.proguard.t r5 = r4.i
            boolean r7 = r4.r
            long r7 = r5.a(r7)
            long r0 = r4.o
            long r7 = r7 + r0
            long r0 = r4.p
            long r7 = r7 + r0
            com.tencent.bugly.proguard.t r5 = r4.i
            boolean r9 = r4.r
            r5.a(r7, r9)
        L69:
            com.tencent.bugly.proguard.s r5 = r4.k
            if (r5 == 0) goto L78
            com.tencent.bugly.proguard.s r5 = r4.k
            int r7 = r4.d
            long r7 = r4.o
            long r7 = r4.p
            r5.a(r6)
        L78:
            com.tencent.bugly.proguard.s r5 = r4.l
            if (r5 == 0) goto L87
            com.tencent.bugly.proguard.s r5 = r4.l
            int r7 = r4.d
            long r7 = r4.o
            long r7 = r4.p
            r5.a(r6)
        L87:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.u.a(com.tencent.bugly.proguard.at, boolean, int, java.lang.String, int):void");
    }

    private static boolean a(at atVar, com.tencent.bugly.crashreport.common.info.a aVar, com.tencent.bugly.crashreport.common.strategy.a aVar2) throws NumberFormatException {
        if (atVar == null) {
            w.d("resp == null!", new Object[0]);
            return false;
        }
        if (atVar.a != 0) {
            w.e("resp result error %d", Byte.valueOf(atVar.a));
            return false;
        }
        try {
            String str = atVar.d;
            if (!(str == null || str.trim().length() <= 0) && com.tencent.bugly.crashreport.common.info.a.a().h() != atVar.d) {
                o.a().a(com.tencent.bugly.crashreport.common.strategy.a.a, "key_ip", atVar.d.getBytes("UTF-8"), (n) null, true);
                aVar.d(atVar.d);
            }
            String str2 = atVar.g;
            if (!(str2 == null || str2.trim().length() <= 0) && com.tencent.bugly.crashreport.common.info.a.a().i() != atVar.g) {
                o.a().a(com.tencent.bugly.crashreport.common.strategy.a.a, "key_imei", atVar.g.getBytes("UTF-8"), (n) null, true);
                aVar.e(atVar.g);
            }
        } catch (Throwable unused) {
        }
        aVar.h = atVar.e;
        if (atVar.b == 510) {
            if (atVar.c == null) {
                w.e("remote data is miss! %d", Integer.valueOf(atVar.b));
                return false;
            }
            av avVar = (av) a.a(atVar.c, av.class);
            if (avVar == null) {
                w.e("remote data is error! %d", Integer.valueOf(atVar.b));
                return false;
            }
            Object[] objArr = new Object[4];
            objArr[0] = Boolean.valueOf(avVar.a);
            objArr[1] = Boolean.valueOf(avVar.c);
            objArr[2] = Boolean.valueOf(avVar.b);
            objArr[3] = Integer.valueOf(avVar.g == null ? -1 : avVar.g.size());
            w.c("en:%b qu:%b uin:%b vm:%d", objArr);
            aVar2.a(avVar);
        }
        return true;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        try {
            byte[] bArrA = this.e;
            int length = 0;
            this.n = 0;
            this.o = 0L;
            this.p = 0L;
            this.i.a(this.j, System.currentTimeMillis());
            if (this.k != null) {
                s sVar = this.k;
                int i2 = this.d;
            }
            if (this.l != null) {
                s sVar2 = this.l;
                int i3 = this.d;
            }
            if (com.tencent.bugly.crashreport.common.info.b.e(this.c) == null) {
                a(null, false, 0, "network is not availiable!", 0);
                return;
            }
            if (bArrA != null && bArrA.length != 0) {
                long jA = this.i.a(this.r);
                if (bArrA.length + jA >= 2097152) {
                    w.e("up too much wait to next time ! %d %d ", Long.valueOf(jA), 2097152L);
                    a(null, false, 0, "[upload] fail, over net consume: " + PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH + "K", 0);
                    return;
                }
                w.c("do upload task %d", Integer.valueOf(this.d));
                if (this.c != null && bArrA != null && this.f != null && this.g != null && this.h != null) {
                    StrategyBean strategyBeanC = this.g.c();
                    if (strategyBeanC == null) {
                        a(null, false, 0, "[upload] fail, illegal local strategy!", 0);
                        return;
                    }
                    HashMap map = new HashMap();
                    map.put("prodId", this.f.e());
                    map.put("bundleId", this.f.c);
                    map.put("appVer", this.f.i);
                    if (this.q) {
                        map.put("cmd", Integer.toString(this.d));
                        map.put("platformId", Byte.toString((byte) 1));
                        this.f.getClass();
                        map.put("sdkVer", "2.2.0");
                        map.put("strategylastUpdateTime", Long.toString(strategyBeanC.m));
                        if (!this.i.a(map)) {
                            a(null, false, 0, "[upload] fail, failed to add security info to HTTP headers", 0);
                            return;
                        }
                        byte[] bArrA2 = a.a(bArrA, 2);
                        if (bArrA2 == null) {
                            a(null, false, 0, "[upload] fail, failed to zip request body", 0);
                            return;
                        }
                        bArrA = this.i.a(bArrA2);
                        if (bArrA == null) {
                            a(null, false, 0, "[upload] fail, failed to encrypt request body", 0);
                            return;
                        }
                    }
                    byte[] bArr = bArrA;
                    int i4 = 0;
                    int i5 = 0;
                    int i6 = -1;
                    while (i4 < this.a) {
                        int i7 = i4 + 1;
                        if (i4 != 0) {
                            w.d("failed to upload last time, wait and try(%d) again", Integer.valueOf(i7));
                            try {
                                Thread.sleep(this.b);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        w.c("send %d", Integer.valueOf(bArr.length));
                        String str = this.m;
                        if (str == null || str.trim().length() <= 0) {
                            if (this.q) {
                                if (this.d == 830) {
                                    this.m = strategyBeanC.p;
                                } else {
                                    this.m = strategyBeanC.o;
                                }
                            } else {
                                this.m = strategyBeanC.q;
                            }
                        }
                        w.c("do upload to %s with cmd %d (pid=%d | tid=%d)", this.m, Integer.valueOf(this.d), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                        byte[] bArrA3 = this.h.a(this.m, bArr, this, map);
                        if (bArrA3 == null) {
                            w.e("try upload fail! %d %s", Integer.valueOf(this.d), "upload fail, no response!");
                        } else {
                            Map<String, String> map2 = this.h.a;
                            if (this.q) {
                                if (map2 == null || map2.size() == 0 || !map2.containsKey("status")) {
                                    w.c("no headers from server, just try again (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                                    w.e("try upload fail! %d %s", Integer.valueOf(this.d), "upload fail, no status header");
                                } else {
                                    try {
                                        i = Integer.parseInt(map2.get("status"));
                                    } catch (Throwable unused) {
                                    }
                                    try {
                                        w.c("status from server is %d (pid=%d | tid=%d)", Integer.valueOf(i), Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                                        if (i != 0) {
                                            if (i == 2) {
                                                if (this.o + this.p > 0) {
                                                    this.i.a(this.i.a(this.r) + this.o + this.p, this.r);
                                                }
                                                this.i.a(i, (at) null);
                                                w.a("Session ID is invalid, will try again immediately (pid=%d | tid=%d)", Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                                                this.i.a(this.j, this.d, this.e, (String) null, this.l);
                                                return;
                                            }
                                            a(null, false, 1, "upload fail, status: " + Integer.toString(i), i);
                                            return;
                                        }
                                        i6 = i;
                                    } catch (Throwable unused2) {
                                        i6 = i;
                                        w.e("try upload fail! %d %s", Integer.valueOf(this.d), "upload fail, format of status header is invalid: " + Integer.toString(i6));
                                        i4 = i7;
                                        i5 = 1;
                                    }
                                }
                            }
                            w.c("recv %d", Integer.valueOf(bArrA3.length));
                            if (this.q) {
                                if (bArrA3.length == 0) {
                                    for (Map.Entry<String, String> entry : map2.entrySet()) {
                                        w.c("[HTTP headers from server] key = %s, value = %s", entry.getKey(), entry.getValue());
                                    }
                                    a(null, false, 1, "upload fail, response data from server is empty", 0);
                                    return;
                                }
                                byte[] bArrB = this.i.b(bArrA3);
                                if (bArrB == null) {
                                    a(null, false, 1, "upload fail, failed to decrypt response from server", 0);
                                    return;
                                }
                                bArrA3 = a.b(bArrB, 2);
                                if (bArrA3 == null) {
                                    a(null, false, 1, "upload fail, failed to unzip(gzip) response from server", 0);
                                    return;
                                }
                            }
                            at atVarA = a.a(bArrA3, this.q);
                            if (atVarA == null) {
                                a(null, false, 1, "upload fail, resp null!", 0);
                                return;
                            }
                            if (this.q) {
                                this.i.a(i6, atVarA);
                            }
                            Object[] objArr = new Object[2];
                            objArr[0] = Integer.valueOf(atVarA.b);
                            if (atVarA.c != null) {
                                length = atVarA.c.length;
                            }
                            objArr[1] = Integer.valueOf(length);
                            w.c("response %d %d", objArr);
                            if (!a(atVarA, this.f, this.g)) {
                                a(atVarA, false, 2, atVarA.f, 0);
                                return;
                            } else {
                                a(atVarA, true, 2, null, 0);
                                return;
                            }
                        }
                        i4 = i7;
                        i5 = 1;
                    }
                    a(null, false, i5, "try OT Fail!", 0);
                    return;
                }
                a(null, false, 0, "[upload] fail, illegal access error!", 0);
                return;
            }
            a(null, false, 0, "[upload] fail, request package is empty!", 0);
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    public final void a(long j) {
        this.n++;
        this.o += j;
    }

    public final void b(long j) {
        this.p += j;
    }
}
