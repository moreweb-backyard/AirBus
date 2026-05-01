package com.tencent.bugly.proguard;

import android.os.Handler;
import android.os.HandlerThread;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class ab {
    private static a a = new a("loop");

    public static Handler a() {
        return a.a();
    }

    /* compiled from: BUGLY */
    static class a {
        private Handler a;

        public a(String str) {
            this.a = null;
            HandlerThread handlerThread = new HandlerThread("BuglyBlockThread_" + str);
            handlerThread.start();
            this.a = new Handler(handlerThread.getLooper());
        }

        public final Handler a() {
            return this.a;
        }
    }
}
