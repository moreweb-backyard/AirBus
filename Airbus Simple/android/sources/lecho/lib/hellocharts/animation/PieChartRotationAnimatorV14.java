package lecho.lib.hellocharts.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import lecho.lib.hellocharts.view.PieChartView;

@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class PieChartRotationAnimatorV14 implements PieChartRotationAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ChartAnimationListener animationListener;
    private ValueAnimator animator;
    private final PieChartView chart;
    private float startRotation;
    private float targetRotation;

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    public PieChartRotationAnimatorV14(PieChartView pieChartView) {
        this(pieChartView, 200L);
    }

    public PieChartRotationAnimatorV14(PieChartView pieChartView, long j) {
        this.startRotation = 0.0f;
        this.targetRotation = 0.0f;
        this.animationListener = new DummyChartAnimationListener();
        this.chart = pieChartView;
        this.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator.setDuration(j);
        this.animator.addListener(this);
        this.animator.addUpdateListener(this);
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void startAnimation(float f, float f2) {
        this.startRotation = ((f % 360.0f) + 360.0f) % 360.0f;
        this.targetRotation = ((f2 % 360.0f) + 360.0f) % 360.0f;
        this.animator.start();
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void cancelAnimation() {
        this.animator.cancel();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.chart.setChartRotation((int) ((((this.startRotation + ((this.targetRotation - this.startRotation) * valueAnimator.getAnimatedFraction())) % 360.0f) + 360.0f) % 360.0f), false);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.chart.setChartRotation((int) this.targetRotation, false);
        this.animationListener.onAnimationFinished();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.animationListener.onAnimationStarted();
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public boolean isAnimationStarted() {
        return this.animator.isStarted();
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
