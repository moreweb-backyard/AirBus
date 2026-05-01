package com.yanzhenjie.andserver.util;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;

/* loaded from: classes.dex */
public class Executors {
    private static Executors instance;
    private static Handler mHandler;
    private final ExecutorService mService = java.util.concurrent.Executors.newCachedThreadPool();

    public static Executors getInstance() {
        if (instance == null) {
            synchronized (Executors.class) {
                if (instance == null) {
                    instance = new Executors();
                }
            }
        }
        return instance;
    }

    private Executors() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void executorService(Runnable runnable) {
        this.mService.execute(runnable);
    }

    public void handler(Runnable runnable) {
        mHandler.post(runnable);
    }
}
