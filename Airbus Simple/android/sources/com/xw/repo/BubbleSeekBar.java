package com.xw.repo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import com.xw.repo.bubbleseekbar.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

/* loaded from: classes.dex */
public class BubbleSeekBar extends View {
    static final int NONE = -1;
    float dx;
    private boolean isAlwaysShowBubble;
    private boolean isAutoAdjustSectionMark;
    private boolean isFloatType;
    private boolean isSeekBySection;
    private boolean isShowProgressInFloat;
    private boolean isShowSectionMark;
    private boolean isShowSectionText;
    private boolean isShowThumbText;
    private boolean isThumbOnDragging;
    private boolean isTouchToSeek;
    private boolean isTouchToSeekAnimEnd;
    private long mAnimDuration;
    private float mBubbleCenterRawSolidX;
    private float mBubbleCenterRawSolidY;
    private float mBubbleCenterRawX;
    private int mBubbleColor;
    private int mBubbleRadius;
    private int mBubbleTextColor;
    private int mBubbleTextSize;
    private BubbleView mBubbleView;
    private BubbleConfigBuilder mConfigBuilder;
    private float mDelta;
    private WindowManager.LayoutParams mLayoutParams;
    private float mLeft;
    private float mMax;
    private float mMin;
    private Paint mPaint;
    private int[] mPoint;
    private float mPreSecValue;
    private float mProgress;
    private OnProgressChangedListener mProgressListener;
    private Rect mRectText;
    private float mRight;
    private int mSecondTrackColor;
    private int mSecondTrackSize;
    private int mSectionCount;
    private float mSectionOffset;
    private int mSectionTextColor;
    private int mSectionTextInterval;
    private int mSectionTextPosition;
    private int mSectionTextSize;
    private float mSectionValue;
    private int mTextSpace;
    private float mThumbCenterX;
    private int mThumbColor;
    private int mThumbRadius;
    private int mThumbRadiusOnDragging;
    private int mThumbTextColor;
    private int mThumbTextSize;
    private int mTrackColor;
    private float mTrackLength;
    private int mTrackSize;
    private WindowManager mWindowManager;
    private boolean triggerBubbleShowing;

    public interface OnProgressChangedListener {
        void getProgressOnActionUp(int i, float f);

        void getProgressOnFinally(int i, float f);

        void onProgressChanged(int i, float f);
    }

    public static abstract class OnProgressChangedListenerAdapter implements OnProgressChangedListener {
        @Override // com.xw.repo.BubbleSeekBar.OnProgressChangedListener
        public void getProgressOnActionUp(int i, float f) {
        }

        @Override // com.xw.repo.BubbleSeekBar.OnProgressChangedListener
        public void getProgressOnFinally(int i, float f) {
        }

        @Override // com.xw.repo.BubbleSeekBar.OnProgressChangedListener
        public void onProgressChanged(int i, float f) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TextPosition {
        public static final int BELOW_SECTION_MARK = 2;
        public static final int BOTTOM_SIDES = 1;
        public static final int SIDES = 0;
    }

    public BubbleSeekBar(Context context) {
        this(context, null);
    }

    public BubbleSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BubbleSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSectionTextPosition = -1;
        this.mPoint = new int[2];
        this.isTouchToSeekAnimEnd = true;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.BubbleSeekBar, i, 0);
        this.mMin = typedArrayObtainStyledAttributes.getFloat(R.styleable.BubbleSeekBar_bsb_min, 0.0f);
        this.mMax = typedArrayObtainStyledAttributes.getFloat(R.styleable.BubbleSeekBar_bsb_max, 100.0f);
        this.mProgress = typedArrayObtainStyledAttributes.getFloat(R.styleable.BubbleSeekBar_bsb_progress, this.mMin);
        this.isFloatType = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_is_float_type, false);
        this.mTrackSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_track_size, BubbleUtils.dp2px(2));
        this.mSecondTrackSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_second_track_size, this.mTrackSize + BubbleUtils.dp2px(2));
        this.mThumbRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_radius, this.mSecondTrackSize + BubbleUtils.dp2px(2));
        this.mThumbRadiusOnDragging = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_radius, this.mSecondTrackSize * 2);
        this.mSectionCount = typedArrayObtainStyledAttributes.getInteger(R.styleable.BubbleSeekBar_bsb_section_count, 10);
        this.mTrackColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BubbleSeekBar_bsb_track_color, ContextCompat.getColor(context, R.color.colorPrimary));
        this.mSecondTrackColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BubbleSeekBar_bsb_second_track_color, ContextCompat.getColor(context, R.color.colorAccent));
        this.mThumbColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BubbleSeekBar_bsb_thumb_color, this.mSecondTrackColor);
        this.isShowSectionText = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_show_section_text, false);
        this.mSectionTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_section_text_size, BubbleUtils.sp2px(14));
        this.mSectionTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BubbleSeekBar_bsb_section_text_color, this.mTrackColor);
        this.isSeekBySection = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_seek_by_section, false);
        int integer = typedArrayObtainStyledAttributes.getInteger(R.styleable.BubbleSeekBar_bsb_section_text_position, -1);
        if (integer == 0) {
            this.mSectionTextPosition = 0;
        } else if (integer == 1) {
            this.mSectionTextPosition = 1;
        } else if (integer == 2) {
            this.mSectionTextPosition = 2;
        } else {
            this.mSectionTextPosition = -1;
        }
        this.mSectionTextInterval = typedArrayObtainStyledAttributes.getInteger(R.styleable.BubbleSeekBar_bsb_section_text_interval, 1);
        this.isShowThumbText = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_show_thumb_text, false);
        this.mThumbTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_thumb_text_size, BubbleUtils.sp2px(14));
        this.mThumbTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BubbleSeekBar_bsb_thumb_text_color, this.mSecondTrackColor);
        this.mBubbleColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BubbleSeekBar_bsb_bubble_color, this.mSecondTrackColor);
        this.mBubbleTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.BubbleSeekBar_bsb_bubble_text_size, BubbleUtils.sp2px(14));
        this.mBubbleTextColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BubbleSeekBar_bsb_bubble_text_color, -1);
        this.isShowSectionMark = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_show_section_mark, false);
        this.isAutoAdjustSectionMark = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_auto_adjust_section_mark, false);
        this.isShowProgressInFloat = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_show_progress_in_float, false);
        int integer2 = typedArrayObtainStyledAttributes.getInteger(R.styleable.BubbleSeekBar_bsb_anim_duration, -1);
        this.mAnimDuration = integer2 < 0 ? 200L : integer2;
        this.isTouchToSeek = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_touch_to_seek, false);
        this.isAlwaysShowBubble = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BubbleSeekBar_bsb_always_show_bubble, false);
        typedArrayObtainStyledAttributes.recycle();
        initConfigByPriority();
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mRectText = new Rect();
        this.mTextSpace = BubbleUtils.dp2px(2);
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mBubbleView = new BubbleView(this, context);
        this.mBubbleView.setProgressText(this.isShowProgressInFloat ? String.valueOf(getProgressFloat()) : String.valueOf(getProgress()));
        calculateRadiusOfBubble();
    }

    private void initConfigByPriority() {
        if (this.mMin == this.mMax) {
            this.mMin = 0.0f;
            this.mMax = 100.0f;
        }
        if (this.mMin > this.mMax) {
            float f = this.mMax;
            this.mMax = this.mMin;
            this.mMin = f;
        }
        if (this.mProgress < this.mMin) {
            this.mProgress = this.mMin;
        }
        if (this.mProgress > this.mMax) {
            this.mProgress = this.mMax;
        }
        if (this.mSecondTrackSize < this.mTrackSize) {
            this.mSecondTrackSize = this.mTrackSize + BubbleUtils.dp2px(2);
        }
        if (this.mThumbRadius <= this.mSecondTrackSize) {
            this.mThumbRadius = this.mSecondTrackSize + BubbleUtils.dp2px(2);
        }
        if (this.mThumbRadiusOnDragging <= this.mSecondTrackSize) {
            this.mThumbRadiusOnDragging = this.mSecondTrackSize * 2;
        }
        if (this.mSectionCount <= 0) {
            this.mSectionCount = 10;
        }
        this.mDelta = this.mMax - this.mMin;
        this.mSectionValue = this.mDelta / this.mSectionCount;
        if (this.mSectionValue < 1.0f) {
            this.isFloatType = true;
        }
        if (this.isFloatType) {
            this.isShowProgressInFloat = true;
        }
        if (this.mSectionTextPosition != -1) {
            this.isShowSectionText = true;
        }
        if (this.isShowSectionText) {
            if (this.mSectionTextPosition == -1) {
                this.mSectionTextPosition = 0;
            }
            if (this.mSectionTextPosition == 2) {
                this.isShowSectionMark = true;
            }
        }
        if (this.mSectionTextInterval < 1) {
            this.mSectionTextInterval = 1;
        }
        if (this.isAutoAdjustSectionMark && !this.isShowSectionMark) {
            this.isAutoAdjustSectionMark = false;
        }
        if (this.isSeekBySection) {
            this.mPreSecValue = this.mMin;
            if (this.mProgress != this.mMin) {
                this.mPreSecValue = this.mSectionValue;
            }
            this.isShowSectionMark = true;
            this.isAutoAdjustSectionMark = true;
            this.isTouchToSeek = false;
        }
        this.mThumbTextSize = (this.isFloatType || this.isSeekBySection || (this.isShowSectionText && this.mSectionTextPosition == 2)) ? this.mSectionTextSize : this.mThumbTextSize;
    }

    private void calculateRadiusOfBubble() {
        String minText;
        String maxText;
        this.mPaint.setTextSize(this.mBubbleTextSize);
        if (this.isShowProgressInFloat) {
            minText = float2String(this.mMin);
        } else {
            minText = getMinText();
        }
        this.mPaint.getTextBounds(minText, 0, minText.length(), this.mRectText);
        int iWidth = (this.mRectText.width() + (this.mTextSpace * 2)) >> 1;
        if (this.isShowProgressInFloat) {
            maxText = float2String(this.mMax);
        } else {
            maxText = getMaxText();
        }
        this.mPaint.getTextBounds(maxText, 0, maxText.length(), this.mRectText);
        int iWidth2 = (this.mRectText.width() + (this.mTextSpace * 2)) >> 1;
        this.mBubbleRadius = BubbleUtils.dp2px(14);
        this.mBubbleRadius = Math.max(this.mBubbleRadius, Math.max(iWidth, iWidth2)) + this.mTextSpace;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int iMax = this.mThumbRadiusOnDragging * 2;
        if (this.isShowThumbText) {
            this.mPaint.setTextSize(this.mThumbTextSize);
            this.mPaint.getTextBounds("j", 0, 1, this.mRectText);
            iMax += this.mRectText.height() + this.mTextSpace;
        }
        if (this.isShowSectionText && this.mSectionTextPosition >= 1) {
            this.mPaint.setTextSize(this.mSectionTextSize);
            this.mPaint.getTextBounds("j", 0, 1, this.mRectText);
            iMax = Math.max(iMax, (this.mThumbRadiusOnDragging * 2) + this.mRectText.height() + this.mTextSpace);
        }
        setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), i), iMax);
        this.mLeft = getPaddingLeft() + this.mThumbRadiusOnDragging;
        this.mRight = (getMeasuredWidth() - getPaddingRight()) - this.mThumbRadiusOnDragging;
        if (this.isShowSectionText) {
            this.mPaint.setTextSize(this.mSectionTextSize);
            if (this.mSectionTextPosition == 0) {
                String minText = getMinText();
                this.mPaint.getTextBounds(minText, 0, minText.length(), this.mRectText);
                this.mLeft += this.mRectText.width() + this.mTextSpace;
                String maxText = getMaxText();
                this.mPaint.getTextBounds(maxText, 0, maxText.length(), this.mRectText);
                this.mRight -= this.mRectText.width() + this.mTextSpace;
            } else if (this.mSectionTextPosition >= 1) {
                String minText2 = getMinText();
                this.mPaint.getTextBounds(minText2, 0, minText2.length(), this.mRectText);
                this.mLeft = getPaddingLeft() + Math.max(this.mThumbRadiusOnDragging, this.mRectText.width() / 2.0f) + this.mTextSpace;
                String maxText2 = getMaxText();
                this.mPaint.getTextBounds(maxText2, 0, maxText2.length(), this.mRectText);
                this.mRight = ((getMeasuredWidth() - getPaddingRight()) - Math.max(this.mThumbRadiusOnDragging, this.mRectText.width() / 2.0f)) - this.mTextSpace;
            }
        } else if (this.isShowThumbText && this.mSectionTextPosition == -1) {
            this.mPaint.setTextSize(this.mThumbTextSize);
            String minText3 = getMinText();
            this.mPaint.getTextBounds(minText3, 0, minText3.length(), this.mRectText);
            this.mLeft = getPaddingLeft() + Math.max(this.mThumbRadiusOnDragging, this.mRectText.width() / 2.0f) + this.mTextSpace;
            String maxText3 = getMaxText();
            this.mPaint.getTextBounds(maxText3, 0, maxText3.length(), this.mRectText);
            this.mRight = ((getMeasuredWidth() - getPaddingRight()) - Math.max(this.mThumbRadiusOnDragging, this.mRectText.width() / 2.0f)) - this.mTextSpace;
        }
        this.mTrackLength = this.mRight - this.mLeft;
        this.mSectionOffset = (this.mTrackLength * 1.0f) / this.mSectionCount;
        this.mBubbleView.measure(i, i2);
        locatePositionOnScreen();
    }

    private void locatePositionOnScreen() {
        getLocationOnScreen(this.mPoint);
        this.mBubbleCenterRawSolidX = (this.mPoint[0] + this.mLeft) - (this.mBubbleView.getMeasuredWidth() / 2.0f);
        this.mBubbleCenterRawX = this.mBubbleCenterRawSolidX + ((this.mTrackLength * (this.mProgress - this.mMin)) / this.mDelta);
        this.mBubbleCenterRawSolidY = this.mPoint[1] - this.mBubbleView.getMeasuredHeight();
        this.mBubbleCenterRawSolidY -= BubbleUtils.dp2px(24);
        if (BubbleUtils.isMIUI()) {
            this.mBubbleCenterRawSolidY += BubbleUtils.dp2px(4);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float paddingLeft = getPaddingLeft();
        float measuredWidth = getMeasuredWidth() - getPaddingRight();
        float paddingTop = getPaddingTop() + this.mThumbRadiusOnDragging;
        if (this.isShowSectionText) {
            this.mPaint.setTextSize(this.mSectionTextSize);
            this.mPaint.setColor(this.mSectionTextColor);
            if (this.mSectionTextPosition == 0) {
                float fHeight = (this.mRectText.height() / 2.0f) + paddingTop;
                String minText = getMinText();
                this.mPaint.getTextBounds(minText, 0, minText.length(), this.mRectText);
                canvas.drawText(minText, (this.mRectText.width() / 2.0f) + paddingLeft, fHeight, this.mPaint);
                paddingLeft += this.mRectText.width() + this.mTextSpace;
                String maxText = getMaxText();
                this.mPaint.getTextBounds(maxText, 0, maxText.length(), this.mRectText);
                canvas.drawText(maxText, measuredWidth - (this.mRectText.width() / 2.0f), fHeight, this.mPaint);
                measuredWidth -= this.mRectText.width() + this.mTextSpace;
            } else if (this.mSectionTextPosition >= 1) {
                String minText2 = getMinText();
                this.mPaint.getTextBounds(minText2, 0, minText2.length(), this.mRectText);
                float fHeight2 = this.mThumbRadiusOnDragging + paddingTop + this.mTextSpace + this.mRectText.height();
                float f = this.mLeft;
                if (this.mSectionTextPosition == 1) {
                    canvas.drawText(minText2, f, fHeight2, this.mPaint);
                }
                String maxText2 = getMaxText();
                this.mPaint.getTextBounds(maxText2, 0, maxText2.length(), this.mRectText);
                float f2 = this.mRight;
                if (this.mSectionTextPosition == 1) {
                    canvas.drawText(maxText2, f2, fHeight2, this.mPaint);
                }
                paddingLeft = f;
                measuredWidth = f2;
            }
        } else if (this.isShowThumbText && this.mSectionTextPosition == -1) {
            paddingLeft = this.mLeft;
            measuredWidth = this.mRight;
        }
        if ((!this.isShowSectionText && !this.isShowThumbText) || this.mSectionTextPosition == 0) {
            paddingLeft += this.mThumbRadiusOnDragging;
            measuredWidth -= this.mThumbRadiusOnDragging;
        }
        float f3 = measuredWidth;
        float f4 = paddingLeft;
        boolean z = this.isShowSectionText && this.mSectionTextPosition == 2;
        boolean z2 = this.mSectionCount % 2 == 0;
        if (z || this.isShowSectionMark) {
            float fDp2px = (this.mThumbRadiusOnDragging - BubbleUtils.dp2px(2)) / 2.0f;
            float fAbs = ((this.mTrackLength / this.mDelta) * Math.abs(this.mProgress - this.mMin)) + this.mLeft;
            this.mPaint.setTextSize(this.mSectionTextSize);
            this.mPaint.getTextBounds("0123456789", 0, "0123456789".length(), this.mRectText);
            float fHeight3 = this.mRectText.height() + paddingTop + this.mThumbRadiusOnDragging + this.mTextSpace;
            for (int i = 0; i <= this.mSectionCount; i++) {
                float f5 = i;
                float f6 = (this.mSectionOffset * f5) + f4;
                this.mPaint.setColor(f6 <= fAbs ? this.mSecondTrackColor : this.mTrackColor);
                canvas.drawCircle(f6, paddingTop, fDp2px, this.mPaint);
                if (z) {
                    this.mPaint.setColor(this.mSectionTextColor);
                    if (this.mSectionTextInterval > 1) {
                        if (z2 && i % this.mSectionTextInterval == 0) {
                            float f7 = this.mMin + (this.mSectionValue * f5);
                            canvas.drawText(this.isFloatType ? float2String(f7) : ((int) f7) + "", f6, fHeight3, this.mPaint);
                        }
                    } else {
                        float f8 = this.mMin + (this.mSectionValue * f5);
                        canvas.drawText(this.isFloatType ? float2String(f8) : ((int) f8) + "", f6, fHeight3, this.mPaint);
                    }
                }
            }
        }
        if (!this.isThumbOnDragging || this.isAlwaysShowBubble) {
            this.mThumbCenterX = ((this.mTrackLength / this.mDelta) * (this.mProgress - this.mMin)) + f4;
        }
        if (this.isShowThumbText && !this.isThumbOnDragging && this.isTouchToSeekAnimEnd) {
            this.mPaint.setColor(this.mThumbTextColor);
            this.mPaint.setTextSize(this.mThumbTextSize);
            this.mPaint.getTextBounds("0123456789", 0, "0123456789".length(), this.mRectText);
            float fHeight4 = this.mRectText.height() + paddingTop + this.mThumbRadiusOnDragging + this.mTextSpace;
            if (this.isFloatType || (this.isShowProgressInFloat && this.mSectionTextPosition == 1 && this.mProgress != this.mMin && this.mProgress != this.mMax)) {
                canvas.drawText(String.valueOf(getProgressFloat()), this.mThumbCenterX, fHeight4, this.mPaint);
            } else {
                canvas.drawText(String.valueOf(getProgress()), this.mThumbCenterX, fHeight4, this.mPaint);
            }
        }
        this.mPaint.setColor(this.mSecondTrackColor);
        this.mPaint.setStrokeWidth(this.mSecondTrackSize);
        canvas.drawLine(f4, paddingTop, this.mThumbCenterX, paddingTop, this.mPaint);
        this.mPaint.setColor(this.mTrackColor);
        this.mPaint.setStrokeWidth(this.mTrackSize);
        canvas.drawLine(this.mThumbCenterX, paddingTop, f3, paddingTop, this.mPaint);
        this.mPaint.setColor(this.mThumbColor);
        canvas.drawCircle(this.mThumbCenterX, paddingTop, this.isThumbOnDragging ? this.mThumbRadiusOnDragging : this.mThumbRadius, this.mPaint);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        post(new Runnable() { // from class: com.xw.repo.BubbleSeekBar.1
            @Override // java.lang.Runnable
            public void run() {
                BubbleSeekBar.this.requestLayout();
            }
        });
    }

    @Override // android.view.View
    protected void onVisibilityChanged(@NonNull View view, int i) {
        if (this.isAlwaysShowBubble) {
            if (i != 0) {
                hideBubble();
            } else if (this.triggerBubbleShowing) {
                showBubble();
            }
            super.onVisibilityChanged(view, i);
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        hideBubble();
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 0:
                this.isThumbOnDragging = isThumbTouched(motionEvent);
                if (this.isThumbOnDragging) {
                    if (this.isAlwaysShowBubble) {
                        this.triggerBubbleShowing = true;
                    }
                    showBubble();
                    invalidate();
                } else if (this.isTouchToSeek && isTrackTouched(motionEvent)) {
                    if (this.isAlwaysShowBubble) {
                        hideBubble();
                        this.triggerBubbleShowing = true;
                    }
                    this.mThumbCenterX = motionEvent.getX();
                    if (this.mThumbCenterX < this.mLeft) {
                        this.mThumbCenterX = this.mLeft;
                    }
                    if (this.mThumbCenterX > this.mRight) {
                        this.mThumbCenterX = this.mRight;
                    }
                    this.mProgress = (((this.mThumbCenterX - this.mLeft) * this.mDelta) / this.mTrackLength) + this.mMin;
                    this.mBubbleCenterRawX = this.mBubbleCenterRawSolidX + ((this.mTrackLength * (this.mProgress - this.mMin)) / this.mDelta);
                    showBubble();
                    invalidate();
                }
                this.dx = this.mThumbCenterX - motionEvent.getX();
                break;
            case 1:
            case 3:
                if (this.isAutoAdjustSectionMark) {
                    if (this.isTouchToSeek) {
                        this.mBubbleView.postDelayed(new Runnable() { // from class: com.xw.repo.BubbleSeekBar.2
                            @Override // java.lang.Runnable
                            public void run() {
                                BubbleSeekBar.this.isTouchToSeekAnimEnd = false;
                                BubbleSeekBar.this.autoAdjustSection();
                            }
                        }, this.isThumbOnDragging ? 0L : 300L);
                    } else {
                        autoAdjustSection();
                    }
                } else if (this.isThumbOnDragging || this.isTouchToSeek) {
                    this.mBubbleView.postDelayed(new Runnable() { // from class: com.xw.repo.BubbleSeekBar.3
                        @Override // java.lang.Runnable
                        public void run() {
                            BubbleSeekBar.this.mBubbleView.animate().alpha(BubbleSeekBar.this.isAlwaysShowBubble ? 1.0f : 0.0f).setDuration(BubbleSeekBar.this.mAnimDuration).setListener(new AnimatorListenerAdapter() { // from class: com.xw.repo.BubbleSeekBar.3.1
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    if (!BubbleSeekBar.this.isAlwaysShowBubble) {
                                        BubbleSeekBar.this.hideBubble();
                                    }
                                    BubbleSeekBar.this.isThumbOnDragging = false;
                                    BubbleSeekBar.this.invalidate();
                                    if (BubbleSeekBar.this.mProgressListener != null) {
                                        BubbleSeekBar.this.mProgressListener.onProgressChanged(BubbleSeekBar.this.getProgress(), BubbleSeekBar.this.getProgressFloat());
                                    }
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationCancel(Animator animator) {
                                    if (!BubbleSeekBar.this.isAlwaysShowBubble) {
                                        BubbleSeekBar.this.hideBubble();
                                    }
                                    BubbleSeekBar.this.isThumbOnDragging = false;
                                    BubbleSeekBar.this.invalidate();
                                }
                            }).start();
                        }
                    }, (this.isThumbOnDragging || !this.isTouchToSeek) ? 0L : 300L);
                }
                if (this.mProgressListener != null) {
                    this.mProgressListener.getProgressOnActionUp(getProgress(), getProgressFloat());
                    break;
                }
                break;
            case 2:
                if (this.isThumbOnDragging) {
                    this.mThumbCenterX = motionEvent.getX() + this.dx;
                    if (this.mThumbCenterX < this.mLeft) {
                        this.mThumbCenterX = this.mLeft;
                    }
                    if (this.mThumbCenterX > this.mRight) {
                        this.mThumbCenterX = this.mRight;
                    }
                    this.mProgress = (((this.mThumbCenterX - this.mLeft) * this.mDelta) / this.mTrackLength) + this.mMin;
                    this.mBubbleCenterRawX = this.mBubbleCenterRawSolidX + ((this.mTrackLength * (this.mProgress - this.mMin)) / this.mDelta);
                    this.mLayoutParams.x = (int) (this.mBubbleCenterRawX + 0.5f);
                    this.mWindowManager.updateViewLayout(this.mBubbleView, this.mLayoutParams);
                    this.mBubbleView.setProgressText(this.isShowProgressInFloat ? String.valueOf(getProgressFloat()) : String.valueOf(getProgress()));
                    invalidate();
                    if (this.mProgressListener != null) {
                        this.mProgressListener.onProgressChanged(getProgress(), getProgressFloat());
                        break;
                    }
                }
                break;
        }
        return this.isThumbOnDragging || this.isTouchToSeek || super.onTouchEvent(motionEvent);
    }

    private boolean isThumbTouched(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        float f = ((this.mTrackLength / this.mDelta) * (this.mProgress - this.mMin)) + this.mLeft;
        float measuredHeight = getMeasuredHeight() / 2.0f;
        return ((motionEvent.getX() - f) * (motionEvent.getX() - f)) + ((motionEvent.getY() - measuredHeight) * (motionEvent.getY() - measuredHeight)) <= (this.mLeft + ((float) BubbleUtils.dp2px(8))) * (this.mLeft + ((float) BubbleUtils.dp2px(8)));
    }

    private boolean isTrackTouched(MotionEvent motionEvent) {
        return isEnabled() && motionEvent.getX() >= ((float) getPaddingLeft()) && motionEvent.getX() <= ((float) (getMeasuredWidth() - getPaddingRight())) && motionEvent.getY() >= ((float) getPaddingTop()) && motionEvent.getY() <= ((float) (getPaddingTop() + (this.mThumbRadiusOnDragging * 2)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBubble() {
        if (this.mBubbleView.getParent() != null) {
            return;
        }
        if (this.mLayoutParams == null) {
            this.mLayoutParams = new WindowManager.LayoutParams();
            this.mLayoutParams.gravity = 8388659;
            this.mLayoutParams.width = -2;
            this.mLayoutParams.height = -2;
            this.mLayoutParams.format = -3;
            this.mLayoutParams.flags = 524328;
            if (BubbleUtils.isMIUI() || Build.VERSION.SDK_INT >= 25) {
                this.mLayoutParams.type = 2;
            } else {
                this.mLayoutParams.type = 2005;
            }
        }
        this.mLayoutParams.x = (int) (this.mBubbleCenterRawX + 0.5f);
        this.mLayoutParams.y = (int) (this.mBubbleCenterRawSolidY + 0.5f);
        this.mBubbleView.setAlpha(0.0f);
        this.mBubbleView.setVisibility(0);
        this.mBubbleView.animate().alpha(1.0f).setDuration(this.mAnimDuration).setListener(new AnimatorListenerAdapter() { // from class: com.xw.repo.BubbleSeekBar.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                BubbleSeekBar.this.mWindowManager.addView(BubbleSeekBar.this.mBubbleView, BubbleSeekBar.this.mLayoutParams);
            }
        }).start();
        this.mBubbleView.setProgressText(this.isShowProgressInFloat ? String.valueOf(getProgressFloat()) : String.valueOf(getProgress()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void autoAdjustSection() {
        ValueAnimator valueAnimatorOfFloat;
        int i = 0;
        float f = 0.0f;
        while (i <= this.mSectionCount) {
            f = (i * this.mSectionOffset) + this.mLeft;
            if (f <= this.mThumbCenterX && this.mThumbCenterX - f <= this.mSectionOffset) {
                break;
            } else {
                i++;
            }
        }
        boolean z = BigDecimal.valueOf((double) this.mThumbCenterX).setScale(1, 4).floatValue() == f;
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator valueAnimator = null;
        if (!z) {
            if (this.mThumbCenterX - f <= this.mSectionOffset / 2.0f) {
                valueAnimatorOfFloat = ValueAnimator.ofFloat(this.mThumbCenterX, f);
            } else {
                valueAnimatorOfFloat = ValueAnimator.ofFloat(this.mThumbCenterX, ((i + 1) * this.mSectionOffset) + this.mLeft);
            }
            valueAnimator = valueAnimatorOfFloat;
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.xw.repo.BubbleSeekBar.5
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    BubbleSeekBar.this.mThumbCenterX = ((Float) valueAnimator2.getAnimatedValue()).floatValue();
                    BubbleSeekBar.this.mProgress = (((BubbleSeekBar.this.mThumbCenterX - BubbleSeekBar.this.mLeft) * BubbleSeekBar.this.mDelta) / BubbleSeekBar.this.mTrackLength) + BubbleSeekBar.this.mMin;
                    BubbleSeekBar.this.mBubbleCenterRawX = (BubbleSeekBar.this.mBubbleCenterRawSolidX + BubbleSeekBar.this.mThumbCenterX) - BubbleSeekBar.this.mLeft;
                    BubbleSeekBar.this.mLayoutParams.x = (int) (BubbleSeekBar.this.mBubbleCenterRawX + 0.5f);
                    if (BubbleSeekBar.this.mBubbleView.getParent() != null) {
                        BubbleSeekBar.this.mWindowManager.updateViewLayout(BubbleSeekBar.this.mBubbleView, BubbleSeekBar.this.mLayoutParams);
                    }
                    BubbleSeekBar.this.mBubbleView.setProgressText(BubbleSeekBar.this.isShowProgressInFloat ? String.valueOf(BubbleSeekBar.this.getProgressFloat()) : String.valueOf(BubbleSeekBar.this.getProgress()));
                    BubbleSeekBar.this.invalidate();
                    if (BubbleSeekBar.this.mProgressListener != null) {
                        BubbleSeekBar.this.mProgressListener.onProgressChanged(BubbleSeekBar.this.getProgress(), BubbleSeekBar.this.getProgressFloat());
                    }
                }
            });
        }
        BubbleView bubbleView = this.mBubbleView;
        Property property = View.ALPHA;
        float[] fArr = new float[1];
        fArr[0] = this.isAlwaysShowBubble ? 1.0f : 0.0f;
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(bubbleView, (Property<BubbleView, Float>) property, fArr);
        if (z) {
            animatorSet.setDuration(this.mAnimDuration).play(objectAnimatorOfFloat);
        } else {
            animatorSet.setDuration(this.mAnimDuration).playTogether(valueAnimator, objectAnimatorOfFloat);
        }
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.xw.repo.BubbleSeekBar.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (!BubbleSeekBar.this.isAlwaysShowBubble) {
                    BubbleSeekBar.this.hideBubble();
                }
                BubbleSeekBar.this.mProgress = (((BubbleSeekBar.this.mThumbCenterX - BubbleSeekBar.this.mLeft) * BubbleSeekBar.this.mDelta) / BubbleSeekBar.this.mTrackLength) + BubbleSeekBar.this.mMin;
                BubbleSeekBar.this.isThumbOnDragging = false;
                BubbleSeekBar.this.isTouchToSeekAnimEnd = true;
                BubbleSeekBar.this.invalidate();
                if (BubbleSeekBar.this.mProgressListener != null) {
                    BubbleSeekBar.this.mProgressListener.getProgressOnFinally(BubbleSeekBar.this.getProgress(), BubbleSeekBar.this.getProgressFloat());
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (!BubbleSeekBar.this.isAlwaysShowBubble) {
                    BubbleSeekBar.this.hideBubble();
                }
                BubbleSeekBar.this.mProgress = (((BubbleSeekBar.this.mThumbCenterX - BubbleSeekBar.this.mLeft) * BubbleSeekBar.this.mDelta) / BubbleSeekBar.this.mTrackLength) + BubbleSeekBar.this.mMin;
                BubbleSeekBar.this.isThumbOnDragging = false;
                BubbleSeekBar.this.isTouchToSeekAnimEnd = true;
                BubbleSeekBar.this.invalidate();
            }
        });
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideBubble() {
        this.mBubbleView.setVisibility(8);
        if (this.mBubbleView.getParent() != null) {
            this.mWindowManager.removeViewImmediate(this.mBubbleView);
        }
    }

    public void correctOffsetWhenContainerOnScrolling() {
        locatePositionOnScreen();
        if (this.mBubbleView.getParent() != null) {
            postInvalidate();
        }
    }

    private String getMinText() {
        return this.isFloatType ? float2String(this.mMin) : String.valueOf((int) this.mMin);
    }

    private String getMaxText() {
        return this.isFloatType ? float2String(this.mMax) : String.valueOf((int) this.mMax);
    }

    public float getMin() {
        return this.mMin;
    }

    public float getMax() {
        return this.mMax;
    }

    public void setProgress(float f) {
        this.mProgress = f;
        this.mBubbleCenterRawX = this.mBubbleCenterRawSolidX + ((this.mTrackLength * (this.mProgress - this.mMin)) / this.mDelta);
        if (this.mProgressListener != null) {
            this.mProgressListener.onProgressChanged(getProgress(), getProgressFloat());
            this.mProgressListener.getProgressOnFinally(getProgress(), getProgressFloat());
        }
        if (this.isAlwaysShowBubble) {
            hideBubble();
            int[] iArr = new int[2];
            getLocationOnScreen(iArr);
            postDelayed(new Runnable() { // from class: com.xw.repo.BubbleSeekBar.7
                @Override // java.lang.Runnable
                public void run() {
                    BubbleSeekBar.this.showBubble();
                    BubbleSeekBar.this.triggerBubbleShowing = true;
                }
            }, (iArr[0] == 0 && iArr[1] == 0) ? 300L : 0L);
        }
        postInvalidate();
    }

    public int getProgress() {
        if (this.isSeekBySection) {
            float f = this.mSectionValue / 2.0f;
            if (this.mProgress >= this.mPreSecValue) {
                if (this.mProgress >= this.mPreSecValue + f) {
                    this.mPreSecValue += this.mSectionValue;
                    return Math.round(this.mPreSecValue);
                }
                return Math.round(this.mPreSecValue);
            }
            if (this.mProgress >= this.mPreSecValue - f) {
                return Math.round(this.mPreSecValue);
            }
            this.mPreSecValue -= this.mSectionValue;
            return Math.round(this.mPreSecValue);
        }
        return Math.round(this.mProgress);
    }

    public float getProgressFloat() {
        return formatFloat(this.mProgress);
    }

    public OnProgressChangedListener getOnProgressChangedListener() {
        return this.mProgressListener;
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.mProgressListener = onProgressChangedListener;
    }

    void config(BubbleConfigBuilder bubbleConfigBuilder) {
        this.mMin = bubbleConfigBuilder.min;
        this.mMax = bubbleConfigBuilder.max;
        this.mProgress = bubbleConfigBuilder.progress;
        this.isFloatType = bubbleConfigBuilder.floatType;
        this.mTrackSize = bubbleConfigBuilder.trackSize;
        this.mSecondTrackSize = bubbleConfigBuilder.secondTrackSize;
        this.mThumbRadius = bubbleConfigBuilder.thumbRadius;
        this.mThumbRadiusOnDragging = bubbleConfigBuilder.thumbRadiusOnDragging;
        this.mTrackColor = bubbleConfigBuilder.trackColor;
        this.mSecondTrackColor = bubbleConfigBuilder.secondTrackColor;
        this.mThumbColor = bubbleConfigBuilder.thumbColor;
        this.mSectionCount = bubbleConfigBuilder.sectionCount;
        this.isShowSectionMark = bubbleConfigBuilder.showSectionMark;
        this.isAutoAdjustSectionMark = bubbleConfigBuilder.autoAdjustSectionMark;
        this.isShowSectionText = bubbleConfigBuilder.showSectionText;
        this.mSectionTextSize = bubbleConfigBuilder.sectionTextSize;
        this.mSectionTextColor = bubbleConfigBuilder.sectionTextColor;
        this.mSectionTextPosition = bubbleConfigBuilder.sectionTextPosition;
        this.mSectionTextInterval = bubbleConfigBuilder.sectionTextInterval;
        this.isShowThumbText = bubbleConfigBuilder.showThumbText;
        this.mThumbTextSize = bubbleConfigBuilder.thumbTextSize;
        this.mThumbTextColor = bubbleConfigBuilder.thumbTextColor;
        this.isShowProgressInFloat = bubbleConfigBuilder.showProgressInFloat;
        this.isTouchToSeek = bubbleConfigBuilder.touchToSeek;
        this.isSeekBySection = bubbleConfigBuilder.seekBySection;
        this.mBubbleColor = bubbleConfigBuilder.bubbleColor;
        this.mBubbleTextSize = bubbleConfigBuilder.bubbleTextSize;
        this.mBubbleTextColor = bubbleConfigBuilder.bubbleTextColor;
        this.isAlwaysShowBubble = bubbleConfigBuilder.alwaysShowBubble;
        initConfigByPriority();
        calculateRadiusOfBubble();
        if (this.mProgressListener != null) {
            this.mProgressListener.onProgressChanged(getProgress(), getProgressFloat());
            this.mProgressListener.getProgressOnFinally(getProgress(), getProgressFloat());
        }
        this.mConfigBuilder = null;
        requestLayout();
    }

    public BubbleConfigBuilder getConfigBuilder() {
        if (this.mConfigBuilder == null) {
            this.mConfigBuilder = new BubbleConfigBuilder(this);
        }
        this.mConfigBuilder.min = this.mMin;
        this.mConfigBuilder.max = this.mMax;
        this.mConfigBuilder.progress = this.mProgress;
        this.mConfigBuilder.floatType = this.isFloatType;
        this.mConfigBuilder.trackSize = this.mTrackSize;
        this.mConfigBuilder.secondTrackSize = this.mSecondTrackSize;
        this.mConfigBuilder.thumbRadius = this.mThumbRadius;
        this.mConfigBuilder.thumbRadiusOnDragging = this.mThumbRadiusOnDragging;
        this.mConfigBuilder.trackColor = this.mTrackColor;
        this.mConfigBuilder.secondTrackColor = this.mSecondTrackColor;
        this.mConfigBuilder.thumbColor = this.mThumbColor;
        this.mConfigBuilder.sectionCount = this.mSectionCount;
        this.mConfigBuilder.showSectionMark = this.isShowSectionMark;
        this.mConfigBuilder.autoAdjustSectionMark = this.isAutoAdjustSectionMark;
        this.mConfigBuilder.showSectionText = this.isShowSectionText;
        this.mConfigBuilder.sectionTextSize = this.mSectionTextSize;
        this.mConfigBuilder.sectionTextColor = this.mSectionTextColor;
        this.mConfigBuilder.sectionTextPosition = this.mSectionTextPosition;
        this.mConfigBuilder.sectionTextInterval = this.mSectionTextInterval;
        this.mConfigBuilder.showThumbText = this.isShowThumbText;
        this.mConfigBuilder.thumbTextSize = this.mThumbTextSize;
        this.mConfigBuilder.thumbTextColor = this.mThumbTextColor;
        this.mConfigBuilder.showProgressInFloat = this.isShowProgressInFloat;
        this.mConfigBuilder.touchToSeek = this.isTouchToSeek;
        this.mConfigBuilder.seekBySection = this.isSeekBySection;
        this.mConfigBuilder.bubbleColor = this.mBubbleColor;
        this.mConfigBuilder.bubbleTextSize = this.mBubbleTextSize;
        this.mConfigBuilder.bubbleTextColor = this.mBubbleTextColor;
        this.mConfigBuilder.alwaysShowBubble = this.isAlwaysShowBubble;
        return this.mConfigBuilder;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("save_instance", super.onSaveInstanceState());
        bundle.putFloat("progress", this.mProgress);
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mProgress = bundle.getFloat("progress");
            super.onRestoreInstanceState(bundle.getParcelable("save_instance"));
            this.mBubbleView.setProgressText(this.isShowProgressInFloat ? String.valueOf(getProgressFloat()) : String.valueOf(getProgress()));
            if (this.isAlwaysShowBubble) {
                setProgress(this.mProgress);
                return;
            }
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    private String float2String(float f) {
        return String.valueOf(formatFloat(f));
    }

    private float formatFloat(float f) {
        return BigDecimal.valueOf(f).setScale(1, 4).floatValue();
    }

    private class BubbleView extends View {
        private Paint mBubblePaint;
        private Path mBubblePath;
        private RectF mBubbleRectF;
        private String mProgressText;
        private Rect mRect;

        BubbleView(BubbleSeekBar bubbleSeekBar, Context context) {
            this(bubbleSeekBar, context, null);
        }

        BubbleView(BubbleSeekBar bubbleSeekBar, Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        BubbleView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            this.mProgressText = "";
            this.mBubblePaint = new Paint();
            this.mBubblePaint.setAntiAlias(true);
            this.mBubblePaint.setTextAlign(Paint.Align.CENTER);
            this.mBubblePath = new Path();
            this.mBubbleRectF = new RectF();
            this.mRect = new Rect();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            setMeasuredDimension(BubbleSeekBar.this.mBubbleRadius * 3, BubbleSeekBar.this.mBubbleRadius * 3);
            this.mBubbleRectF.set((getMeasuredWidth() / 2.0f) - BubbleSeekBar.this.mBubbleRadius, 0.0f, (getMeasuredWidth() / 2.0f) + BubbleSeekBar.this.mBubbleRadius, BubbleSeekBar.this.mBubbleRadius * 2);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.mBubblePath.reset();
            float measuredWidth = getMeasuredWidth() / 2.0f;
            float measuredHeight = getMeasuredHeight() - (BubbleSeekBar.this.mBubbleRadius / 3.0f);
            this.mBubblePath.moveTo(measuredWidth, measuredHeight);
            double measuredWidth2 = getMeasuredWidth() / 2.0f;
            double dSqrt = Math.sqrt(3.0d) / 2.0d;
            double d = BubbleSeekBar.this.mBubbleRadius;
            Double.isNaN(d);
            Double.isNaN(measuredWidth2);
            float f = (float) (measuredWidth2 - (dSqrt * d));
            float f2 = BubbleSeekBar.this.mBubbleRadius * 1.5f;
            this.mBubblePath.quadTo(f - BubbleUtils.dp2px(2), f2 - BubbleUtils.dp2px(2), f, f2);
            this.mBubblePath.arcTo(this.mBubbleRectF, 150.0f, 240.0f);
            double measuredWidth3 = getMeasuredWidth() / 2.0f;
            double dSqrt2 = Math.sqrt(3.0d) / 2.0d;
            double d2 = BubbleSeekBar.this.mBubbleRadius;
            Double.isNaN(d2);
            Double.isNaN(measuredWidth3);
            this.mBubblePath.quadTo(((float) (measuredWidth3 + (dSqrt2 * d2))) + BubbleUtils.dp2px(2), f2 - BubbleUtils.dp2px(2), measuredWidth, measuredHeight);
            this.mBubblePath.close();
            this.mBubblePaint.setColor(BubbleSeekBar.this.mBubbleColor);
            canvas.drawPath(this.mBubblePath, this.mBubblePaint);
            this.mBubblePaint.setTextSize(BubbleSeekBar.this.mBubbleTextSize);
            this.mBubblePaint.setColor(BubbleSeekBar.this.mBubbleTextColor);
            this.mBubblePaint.getTextBounds(this.mProgressText, 0, this.mProgressText.length(), this.mRect);
            Paint.FontMetrics fontMetrics = this.mBubblePaint.getFontMetrics();
            canvas.drawText(this.mProgressText, getMeasuredWidth() / 2.0f, (BubbleSeekBar.this.mBubbleRadius + ((fontMetrics.descent - fontMetrics.ascent) / 2.0f)) - fontMetrics.descent, this.mBubblePaint);
        }

        void setProgressText(String str) {
            if (str == null || this.mProgressText.equals(str)) {
                return;
            }
            this.mProgressText = str;
            invalidate();
        }
    }
}
