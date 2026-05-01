package com.bigkoo.pickerview.lib;

/* loaded from: classes.dex */
final class OnItemSelectedRunnable implements Runnable {
    final WheelView loopView;

    OnItemSelectedRunnable(WheelView wheelView) {
        this.loopView = wheelView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.loopView.onItemSelectedListener.onItemSelected(this.loopView.getCurrentItem());
    }
}
