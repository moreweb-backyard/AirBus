package com.pzj.ipcdemo.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/* loaded from: classes.dex */
public class CircularProgressBar extends View {
    private int mAnimationDuration;
    private boolean mDrawText;
    private int mMaxProgress;
    private float mMaxSweepAngle;
    private final Paint mPaint;
    private int mProgressColor;
    private boolean mRoundedCorners;
    private final float mStartAngle;
    private int mStrokeWidth;
    private float mSweepAngle;
    private int mTextColor;
    private int mViewHeight;
    private int mViewWidth;

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mStartAngle = -90.0f;
        this.mSweepAngle = 0.0f;
        this.mMaxSweepAngle = 360.0f;
        this.mStrokeWidth = 20;
        this.mAnimationDuration = 400;
        this.mMaxProgress = 100;
        this.mDrawText = true;
        this.mRoundedCorners = true;
        this.mProgressColor = ViewCompat.MEASURED_STATE_MASK;
        this.mTextColor = ViewCompat.MEASURED_STATE_MASK;
        this.mPaint = new Paint(1);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initMeasurments();
        drawOutlineArc(canvas);
        if (this.mDrawText) {
            drawText(canvas);
        }
    }

    private void initMeasurments() {
        this.mViewWidth = getWidth();
        this.mViewHeight = getHeight();
    }

    private void drawOutlineArc(Canvas canvas) {
        float f = this.mStrokeWidth / 2.0f;
        float fMin = Math.min(this.mViewWidth, this.mViewHeight) - f;
        RectF rectF = new RectF(f, f, fMin, fMin);
        this.mPaint.setColor(this.mProgressColor);
        this.mPaint.setStrokeWidth(this.mStrokeWidth);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(this.mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        this.mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, -90.0f, this.mSweepAngle, false, this.mPaint);
    }

    private void drawText(Canvas canvas) {
        this.mPaint.setTextSize(Math.min(this.mViewWidth, this.mViewHeight) / 5.0f);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setStrokeWidth(0.0f);
        this.mPaint.setColor(this.mTextColor);
        canvas.drawText(calcProgressFromSweepAngle(this.mSweepAngle) + "%", canvas.getWidth() / 2, (int) ((canvas.getHeight() / 2) - ((this.mPaint.descent() + this.mPaint.ascent()) / 2.0f)), this.mPaint);
    }

    private float calcSweepAngleFromProgress(int i) {
        return (this.mMaxSweepAngle / this.mMaxProgress) * i;
    }

    private int calcProgressFromSweepAngle(float f) {
        return (int) ((f * this.mMaxProgress) / this.mMaxSweepAngle);
    }

    public void setProgress(int i) {
        ValueAnimator valueAnimatorOfFloat = ValueAnimator.ofFloat(this.mSweepAngle, calcSweepAngleFromProgress(i));
        valueAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        valueAnimatorOfFloat.setDuration(this.mAnimationDuration);
        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.pzj.ipcdemo.utils.CircularProgressBar.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CircularProgressBar.this.mSweepAngle = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                CircularProgressBar.this.invalidate();
            }
        });
        valueAnimatorOfFloat.start();
    }

    public void setProgressColor(int i) {
        this.mProgressColor = i;
        invalidate();
    }

    public void setProgressWidth(int i) {
        this.mStrokeWidth = i;
        invalidate();
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
        invalidate();
    }

    public void showProgressText(boolean z) {
        this.mDrawText = z;
        invalidate();
    }

    public void useRoundedCorners(boolean z) {
        this.mRoundedCorners = z;
        invalidate();
    }
}
