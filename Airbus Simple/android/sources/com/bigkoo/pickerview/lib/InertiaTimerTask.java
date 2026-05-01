package com.bigkoo.pickerview.lib;

import java.util.TimerTask;

/* loaded from: classes.dex */
final class InertiaTimerTask extends TimerTask {
    float a = 2.1474836E9f;
    final WheelView loopView;
    final float velocityY;

    InertiaTimerTask(WheelView wheelView, float f) {
        this.loopView = wheelView;
        this.velocityY = f;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public final void run() {
        if (this.a == 2.1474836E9f) {
            if (Math.abs(this.velocityY) <= 2000.0f) {
                this.a = this.velocityY;
            } else if (this.velocityY > 0.0f) {
                this.a = 2000.0f;
            } else {
                this.a = -2000.0f;
            }
        }
        if (Math.abs(this.a) >= 0.0f && Math.abs(this.a) <= 20.0f) {
            this.loopView.cancelFuture();
            this.loopView.handler.sendEmptyMessage(2000);
            return;
        }
        int i = (int) ((this.a * 10.0f) / 1000.0f);
        this.loopView.totalScrollY -= i;
        if (!this.loopView.isLoop) {
            float f = this.loopView.itemHeight;
            float f2 = (-this.loopView.initPosition) * f;
            float itemsCount = ((this.loopView.getItemsCount() - 1) - this.loopView.initPosition) * f;
            double d = this.loopView.totalScrollY;
            double d2 = f;
            Double.isNaN(d2);
            double d3 = d2 * 0.3d;
            Double.isNaN(d);
            if (d - d3 < f2) {
                f2 = this.loopView.totalScrollY + i;
            } else {
                double d4 = this.loopView.totalScrollY;
                Double.isNaN(d4);
                if (d4 + d3 > itemsCount) {
                    itemsCount = this.loopView.totalScrollY + i;
                }
            }
            if (this.loopView.totalScrollY <= f2) {
                this.a = 40.0f;
                this.loopView.totalScrollY = (int) f2;
            } else if (this.loopView.totalScrollY >= itemsCount) {
                this.loopView.totalScrollY = (int) itemsCount;
                this.a = -40.0f;
            }
        }
        if (this.a < 0.0f) {
            this.a += 20.0f;
        } else {
            this.a -= 20.0f;
        }
        this.loopView.handler.sendEmptyMessage(1000);
    }
}
