package com.bigkoo.pickerview.lib;

import android.support.v7.widget.ActivityChooserView;
import java.util.TimerTask;

/* loaded from: classes.dex */
final class SmoothScrollTimerTask extends TimerTask {
    final WheelView loopView;
    int offset;
    int realTotalOffset = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    int realOffset = 0;

    SmoothScrollTimerTask(WheelView wheelView, int i) {
        this.loopView = wheelView;
        this.offset = i;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public final void run() {
        if (this.realTotalOffset == Integer.MAX_VALUE) {
            this.realTotalOffset = this.offset;
        }
        this.realOffset = (int) (this.realTotalOffset * 0.1f);
        if (this.realOffset == 0) {
            if (this.realTotalOffset < 0) {
                this.realOffset = -1;
            } else {
                this.realOffset = 1;
            }
        }
        if (Math.abs(this.realTotalOffset) <= 1) {
            this.loopView.cancelFuture();
            this.loopView.handler.sendEmptyMessage(3000);
            return;
        }
        this.loopView.totalScrollY += this.realOffset;
        if (!this.loopView.isLoop) {
            float f = this.loopView.itemHeight;
            float itemsCount = ((this.loopView.getItemsCount() - 1) - this.loopView.initPosition) * f;
            if (this.loopView.totalScrollY <= (-this.loopView.initPosition) * f || this.loopView.totalScrollY >= itemsCount) {
                this.loopView.totalScrollY -= this.realOffset;
                this.loopView.cancelFuture();
                this.loopView.handler.sendEmptyMessage(3000);
                return;
            }
        }
        this.loopView.handler.sendEmptyMessage(1000);
        this.realTotalOffset -= this.realOffset;
    }
}
