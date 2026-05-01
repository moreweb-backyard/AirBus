package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/* loaded from: classes.dex */
public class AnimHelper {
    public static void startAlphaInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.start();
    }

    public static void startAlphaOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), 0.0f);
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.start();
    }

    public static void startZoomInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "scaleX", 0.618f, 1.0f);
        ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(view, "scaleY", 0.618f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2, objectAnimatorOfFloat3);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startZoomOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), 0.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "scaleX", view.getScaleX(), 0.618f);
        ObjectAnimator objectAnimatorOfFloat3 = ObjectAnimator.ofFloat(view, "scaleY", view.getScaleY(), 0.618f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2, objectAnimatorOfFloat3);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startTopInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationY", -view.getBottom(), 0.0f);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startTopOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), -view.getBottom());
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startTopAlphaInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationY", -(view.getMeasuredHeight() * 0.38200003f), 0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startTopAlphaOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), -(view.getMeasuredHeight() * 0.38200003f));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startBottomInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationY", ((ViewGroup) view.getParent()).getMeasuredHeight() - view.getTop(), 0.0f);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startBottomOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), ((ViewGroup) view.getParent()).getMeasuredHeight() - view.getTop());
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startBottomAlphaInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight() * 0.38200003f, 0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startBottomAlphaOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), view.getMeasuredHeight() * 0.38200003f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startLeftInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationX", -view.getRight(), 0.0f);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startLeftOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), -view.getRight());
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startLeftAlphaInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationX", -(view.getMeasuredWidth() * 0.38200003f), 0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startLeftAlphaOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), -(view.getMeasuredWidth() * 0.38200003f));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startRightInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationX", ((ViewGroup) view.getParent()).getMeasuredWidth() - view.getLeft(), 0.0f);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startRightOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), ((ViewGroup) view.getParent()).getMeasuredWidth() - view.getLeft());
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(j);
        objectAnimatorOfFloat.start();
    }

    public static void startRightAlphaInAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationX", view.getMeasuredWidth() * 0.38200003f, 0.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    public static void startRightAlphaOutAnim(View view, long j) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
        ObjectAnimator objectAnimatorOfFloat2 = ObjectAnimator.ofFloat(view, "translationX", view.getTranslationX(), view.getMeasuredWidth() * 0.38200003f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorOfFloat, objectAnimatorOfFloat2);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(j);
        animatorSet.start();
    }

    @RequiresApi(api = 21)
    public static void startCircularRevealInAnim(View view, int i, int i2, long j) {
        Animator animatorCreateCircularReveal = ViewAnimationUtils.createCircularReveal(view, i, i2, 0.0f, (int) Math.sqrt(Math.pow(Math.max(i, view.getMeasuredWidth() - i), 2.0d) + Math.pow(Math.max(i2, view.getMeasuredHeight() - i2), 2.0d)));
        animatorCreateCircularReveal.setInterpolator(new DecelerateInterpolator());
        animatorCreateCircularReveal.setDuration(j);
        animatorCreateCircularReveal.start();
    }

    @RequiresApi(api = 21)
    public static void startCircularRevealOutAnim(View view, int i, int i2, long j) {
        Animator animatorCreateCircularReveal = ViewAnimationUtils.createCircularReveal(view, i, i2, (int) Math.sqrt(Math.pow(Math.max(i, view.getMeasuredWidth() - i), 2.0d) + Math.pow(Math.max(i2, view.getMeasuredHeight() - i2), 2.0d)), 0.0f);
        animatorCreateCircularReveal.setInterpolator(new DecelerateInterpolator());
        animatorCreateCircularReveal.setDuration(j);
        animatorCreateCircularReveal.start();
    }
}
