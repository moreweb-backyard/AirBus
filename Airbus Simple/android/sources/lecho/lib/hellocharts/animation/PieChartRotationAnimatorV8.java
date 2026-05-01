package lecho.lib.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import lecho.lib.hellocharts.view.PieChartView;

/* loaded from: classes.dex */
public class PieChartRotationAnimatorV8 implements PieChartRotationAnimator {
    private ChartAnimationListener animationListener;
    final PieChartView chart;
    final long duration;
    final Handler handler;
    final Interpolator interpolator;
    boolean isAnimationStarted;
    private final Runnable runnable;
    long start;
    private float startRotation;
    private float targetRotation;

    public PieChartRotationAnimatorV8(PieChartView pieChartView) {
        this(pieChartView, 200L);
    }

    public PieChartRotationAnimatorV8(PieChartView pieChartView, long j) {
        this.interpolator = new AccelerateDecelerateInterpolator();
        this.isAnimationStarted = false;
        this.startRotation = 0.0f;
        this.targetRotation = 0.0f;
        this.animationListener = new DummyChartAnimationListener();
        this.runnable = new Runnable() { // from class: lecho.lib.hellocharts.animation.PieChartRotationAnimatorV8.1
            @Override // java.lang.Runnable
            public void run() {
                long jUptimeMillis = SystemClock.uptimeMillis() - PieChartRotationAnimatorV8.this.start;
                if (jUptimeMillis > PieChartRotationAnimatorV8.this.duration) {
                    PieChartRotationAnimatorV8.this.isAnimationStarted = false;
                    PieChartRotationAnimatorV8.this.handler.removeCallbacks(PieChartRotationAnimatorV8.this.runnable);
                    PieChartRotationAnimatorV8.this.chart.setChartRotation((int) PieChartRotationAnimatorV8.this.targetRotation, false);
                    PieChartRotationAnimatorV8.this.animationListener.onAnimationFinished();
                    return;
                }
                PieChartRotationAnimatorV8.this.chart.setChartRotation((int) ((((PieChartRotationAnimatorV8.this.startRotation + ((PieChartRotationAnimatorV8.this.targetRotation - PieChartRotationAnimatorV8.this.startRotation) * Math.min(PieChartRotationAnimatorV8.this.interpolator.getInterpolation(jUptimeMillis / PieChartRotationAnimatorV8.this.duration), 1.0f))) % 360.0f) + 360.0f) % 360.0f), false);
                PieChartRotationAnimatorV8.this.handler.postDelayed(this, 16L);
            }
        };
        this.chart = pieChartView;
        this.duration = j;
        this.handler = new Handler();
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void startAnimation(float f, float f2) {
        this.startRotation = ((f % 360.0f) + 360.0f) % 360.0f;
        this.targetRotation = ((f2 % 360.0f) + 360.0f) % 360.0f;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setChartRotation((int) this.targetRotation, false);
        this.animationListener.onAnimationFinished();
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        if (chartAnimationListener == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = chartAnimationListener;
        }
    }
}
