package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class ChartViewportAnimatorV8 implements ChartViewportAnimator {
    final Chart chart;
    long start;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    boolean isAnimationStarted = false;
    private Viewport startViewport = new Viewport();
    private Viewport targetViewport = new Viewport();
    private Viewport newViewport = new Viewport();
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    private final Runnable runnable = new Runnable() { // from class: lecho.lib.hellocharts.animation.ChartViewportAnimatorV8.1
        @Override // java.lang.Runnable
        public void run() {
            long jUptimeMillis = SystemClock.uptimeMillis() - ChartViewportAnimatorV8.this.start;
            if (jUptimeMillis <= ChartViewportAnimatorV8.this.duration) {
                float fMin = Math.min(ChartViewportAnimatorV8.this.interpolator.getInterpolation(jUptimeMillis / ChartViewportAnimatorV8.this.duration), 1.0f);
                ChartViewportAnimatorV8.this.newViewport.set(ChartViewportAnimatorV8.this.startViewport.left + ((ChartViewportAnimatorV8.this.targetViewport.left - ChartViewportAnimatorV8.this.startViewport.left) * fMin), ChartViewportAnimatorV8.this.startViewport.top + ((ChartViewportAnimatorV8.this.targetViewport.top - ChartViewportAnimatorV8.this.startViewport.top) * fMin), ChartViewportAnimatorV8.this.startViewport.right + ((ChartViewportAnimatorV8.this.targetViewport.right - ChartViewportAnimatorV8.this.startViewport.right) * fMin), ChartViewportAnimatorV8.this.startViewport.bottom + ((ChartViewportAnimatorV8.this.targetViewport.bottom - ChartViewportAnimatorV8.this.startViewport.bottom) * fMin));
                ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.newViewport);
                ChartViewportAnimatorV8.this.handler.postDelayed(this, 16L);
                return;
            }
            ChartViewportAnimatorV8.this.isAnimationStarted = false;
            ChartViewportAnimatorV8.this.handler.removeCallbacks(ChartViewportAnimatorV8.this.runnable);
            ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.targetViewport);
            ChartViewportAnimatorV8.this.animationListener.onAnimationFinished();
        }
    };
    private long duration = 300;
    final Handler handler = new Handler();

    public ChartViewportAnimatorV8(Chart chart) {
        this.chart = chart;
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.duration = 300L;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2, long j) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.duration = j;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setCurrentViewport(this.targetViewport);
        this.animationListener.onAnimationFinished();
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        if (chartAnimationListener == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = chartAnimationListener;
        }
    }
}
