package com.tencent.bugly.proguard;

import android.annotation.TargetApi;
import android.view.Choreographer;
import java.util.concurrent.TimeUnit;

/* compiled from: BUGLY */
@TargetApi(16)
/* loaded from: classes.dex */
public final class aa implements Choreographer.FrameCallback {
    public static long a;
    private static long b;
    private static long c;

    @Override // android.view.Choreographer.FrameCallback
    public final void doFrame(long j) {
        a = System.currentTimeMillis();
        if (b == 0) {
            b = j;
            Choreographer.getInstance().postFrameCallback(this);
            return;
        }
        c = j;
        long j2 = (j - b) / 1000000;
        long jConvert = TimeUnit.MILLISECONDS.convert(c - b, TimeUnit.NANOSECONDS);
        long jRound = Math.round(16.6f);
        int i = jConvert > jRound ? (int) (jConvert / jRound) : 0;
        if (j2 >= z.a) {
            w.a("currentFrameTimeNanos - lastFrameTimeNanos = " + j2, new Object[0]);
            w.a("Skipped " + i + " frames! ", new Object[0]);
        }
        b = c;
        Choreographer.getInstance().postFrameCallback(this);
    }
}
