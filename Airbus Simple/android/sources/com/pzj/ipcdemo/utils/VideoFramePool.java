package com.pzj.ipcdemo.utils;

import android.opengl.GLSurfaceView;
import android.os.Process;
import android.util.Log;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
public class VideoFramePool extends Thread {
    private long endDate;
    private int mframeSize;
    private MyRender myRender;
    private long startDate;
    private GLSurfaceView surfaceView;
    boolean mExitFlag = false;
    private LinkedBlockingQueue<byte[]> framePool = new LinkedBlockingQueue<>(50);
    private int frameRate = 15;
    private int defaultRate = 15;
    private int delayTime = 66;
    int mHeight = 0;
    int mWidth = 0;

    public VideoFramePool(GLSurfaceView gLSurfaceView, MyRender myRender) {
        this.surfaceView = gLSurfaceView;
        this.myRender = myRender;
    }

    public void exit() {
        this.mExitFlag = true;
        this.framePool.clear();
    }

    public void setFrameRate(int i) {
        this.frameRate = i;
        this.defaultRate = i;
        this.delayTime = 1000 / this.defaultRate;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws InterruptedException, SecurityException, IllegalArgumentException {
        Process.setThreadPriority(-19);
        while (!this.mExitFlag) {
            this.startDate = System.currentTimeMillis();
            byte[] bArrPoll = this.framePool.poll();
            if (this.framePool.size() <= 5) {
                this.frameRate = 10;
            } else if (this.framePool.size() >= 10) {
                this.frameRate = this.defaultRate;
            }
            if (bArrPoll != null) {
                this.myRender.writeSample(bArrPoll, this.mframeSize, this.mWidth, this.mHeight);
            }
            Log.e("pool", "poolsize " + this.framePool.size() + "frameRate***" + this.frameRate);
            try {
                this.endDate = System.currentTimeMillis();
                if (this.frameRate == 10) {
                    Thread.sleep(Math.max(0L, 100 - (this.endDate - this.startDate)), 1000);
                } else if (this.frameRate == this.defaultRate) {
                    Thread.sleep(Math.max(0L, this.delayTime - (this.endDate - this.startDate)), 1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pushBytes(byte[] bArr, int i, int i2, int i3) {
        this.mWidth = i2;
        this.mHeight = i3;
        this.mframeSize = i;
        if (this.mExitFlag) {
            return;
        }
        while (this.framePool.size() > this.defaultRate) {
            this.framePool.poll();
        }
        this.framePool.offer(bArr);
    }

    public int getFramePoolSize() {
        return this.framePool.size();
    }

    public void clearBuffer() {
        this.framePool.clear();
    }
}
