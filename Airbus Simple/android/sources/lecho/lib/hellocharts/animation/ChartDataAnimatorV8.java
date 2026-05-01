package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class ChartDataAnimatorV8 implements ChartDataAnimator {
    final Chart chart;
    long duration;
    long start;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    boolean isAnimationStarted = false;
    private final Runnable runnable = new Runnable() { // from class: lecho.lib.hellocharts.animation.ChartDataAnimatorV8.1
        @Override // java.lang.Runnable
        public void run() {
            long jUptimeMillis = SystemClock.uptimeMillis() - ChartDataAnimatorV8.this.start;
            if (jUptimeMillis > ChartDataAnimatorV8.this.duration) {
                ChartDataAnimatorV8.this.isAnimationStarted = false;
                ChartDataAnimatorV8.this.handler.removeCallbacks(ChartDataAnimatorV8.this.runnable);
                ChartDataAnimatorV8.this.chart.animationDataFinished();
            } else {
                ChartDataAnimatorV8.this.chart.animationDataUpdate(Math.min(ChartDataAnimatorV8.this.interpolator.getInterpolation(jUptimeMillis / ChartDataAnimatorV8.this.duration), 1.0f));
                ChartDataAnimatorV8.this.handler.postDelayed(this, 16L);
            }
        }
    };
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    final Handler handler = new Handler();

    public ChartDataAnimatorV8(Chart chart) {
        this.chart = chart;
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public void startAnimation(long j) {
        if (j >= 0) {
            this.duration = j;
        } else {
            this.duration = 500L;
        }
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.animationDataFinished();
        this.animationListener.onAnimationFinished();
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        if (chartAnimationListener == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = chartAnimationListener;
        }
    }
}
