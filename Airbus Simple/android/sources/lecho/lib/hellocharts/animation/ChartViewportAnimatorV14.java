package lecho.lib.hellocharts.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;

@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class ChartViewportAnimatorV14 implements ChartViewportAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private final Chart chart;
    private Viewport startViewport = new Viewport();
    private Viewport targetViewport = new Viewport();
    private Viewport newViewport = new Viewport();
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    private ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    public ChartViewportAnimatorV14(Chart chart) {
        this.chart = chart;
        this.animator.addListener(this);
        this.animator.addUpdateListener(this);
        this.animator.setDuration(300L);
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.animator.setDuration(300L);
        this.animator.start();
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2, long j) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.animator.setDuration(j);
        this.animator.start();
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public void cancelAnimation() {
        this.animator.cancel();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.newViewport.set(this.startViewport.left + ((this.targetViewport.left - this.startViewport.left) * animatedFraction), this.startViewport.top + ((this.targetViewport.top - this.startViewport.top) * animatedFraction), this.startViewport.right + ((this.targetViewport.right - this.startViewport.right) * animatedFraction), this.startViewport.bottom + ((this.targetViewport.bottom - this.startViewport.bottom) * animatedFraction));
        this.chart.setCurrentViewport(this.newViewport);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.chart.setCurrentViewport(this.targetViewport);
        this.animationListener.onAnimationFinished();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.animationListener.onAnimationStarted();
    }

    @Override // lecho.lib.hellocharts.animation.ChartViewportAnimator
    public boolean isAnimationStarted() {
        return this.animator.isStarted();
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
