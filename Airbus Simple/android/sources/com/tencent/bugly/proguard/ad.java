package com.tencent.bugly.proguard;

import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ad extends ac {
    private Map<String, String> c;
    private boolean d;
    private z e;
    private long f;
    private long g;

    public ad(long j, z zVar) {
        super(50L);
        this.c = null;
        this.d = false;
        this.f = -1L;
        this.g = -1L;
        this.e = zVar;
    }

    @Override // com.tencent.bugly.proguard.ac
    final void c() {
        try {
            long jCurrentTimeMillis = System.currentTimeMillis();
            long j = aa.a;
            if (j == 0) {
                return;
            }
            if (this.f != j) {
                this.d = false;
                this.f = j;
            }
            if (jCurrentTimeMillis - j < z.a || this.d) {
                return;
            }
            this.d = true;
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            if (this.g == -1) {
                this.g = jCurrentTimeMillis2;
            }
            if (jCurrentTimeMillis2 - this.g == 0 || jCurrentTimeMillis2 - this.g >= 3000) {
                this.g = System.currentTimeMillis();
                this.c = a.a(com.tencent.bugly.crashreport.crash.c.e, false);
                this.e.a(this.g, this.c);
            }
        } catch (Throwable th) {
            w.a(th);
            w.e("get all thread stack fail!", new Object[0]);
        }
    }
}
