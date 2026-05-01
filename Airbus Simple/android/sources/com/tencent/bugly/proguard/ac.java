package com.tencent.bugly.proguard;

import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public abstract class ac {
    long b;
    AtomicBoolean a = new AtomicBoolean(false);
    private Runnable c = new Runnable() { // from class: com.tencent.bugly.proguard.ac.1
        @Override // java.lang.Runnable
        public final void run() {
            ac.this.c();
            if (ac.this.a.get()) {
                ab.a().postDelayed(ac.this.c, ac.this.b);
            }
        }
    };

    abstract void c();

    public ac(long j) {
        this.b = 0 == j ? 50L : j;
    }

    public final void a() {
        if (this.a.get()) {
            return;
        }
        this.a.set(true);
        ab.a().removeCallbacks(this.c);
        ab.a().postDelayed(this.c, this.b);
    }

    public final void b() {
        if (this.a.get()) {
            this.a.set(false);
            ab.a().removeCallbacks(this.c);
        }
    }
}
