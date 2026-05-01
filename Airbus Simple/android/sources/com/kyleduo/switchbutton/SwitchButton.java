package com.kyleduo.switchbutton;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;

/* loaded from: classes.dex */
public class SwitchButton extends CompoundButton {
    public static final int DEFAULT_ANIMATION_DURATION = 250;
    public static final float DEFAULT_BACK_MEASURE_RATIO = 1.8f;
    public static final int DEFAULT_TEXT_MARGIN_DP = 2;
    public static final int DEFAULT_THUMB_MARGIN_DP = 2;
    public static final int DEFAULT_THUMB_SIZE_DP = 20;
    public static final int DEFAULT_TINT_COLOR = 3309506;
    private long mAnimationDuration;
    private boolean mAutoAdjustTextPosition;
    private ColorStateList mBackColor;
    private Drawable mBackDrawable;
    private float mBackMeasureRatio;
    private float mBackRadius;
    private RectF mBackRectF;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private int mClickTimeout;
    private int mCurrBackColor;
    private int mCurrThumbColor;
    private Drawable mCurrentBackDrawable;
    private boolean mDrawDebugRect;
    private boolean mFadeBack;
    private boolean mIsBackUseDrawable;
    private boolean mIsThumbUseDrawable;
    private float mLastX;
    private int mNextBackColor;
    private Drawable mNextBackDrawable;
    private Layout mOffLayout;
    private int mOffTextColor;
    private Layout mOnLayout;
    private int mOnTextColor;
    private Paint mPaint;
    private RectF mPresentThumbRectF;
    private float mProcess;
    private ObjectAnimator mProcessAnimator;
    private Paint mRectPaint;
    private boolean mRestoring;
    private RectF mSafeRectF;
    private float mStartX;
    private float mStartY;
    private float mTextHeight;
    private float mTextMarginH;
    private CharSequence mTextOff;
    private RectF mTextOffRectF;
    private CharSequence mTextOn;
    private RectF mTextOnRectF;
    private TextPaint mTextPaint;
    private float mTextWidth;
    private ColorStateList mThumbColor;
    private Drawable mThumbDrawable;
    private RectF mThumbMargin;
    private float mThumbRadius;
    private RectF mThumbRectF;
    private PointF mThumbSizeF;
    private int mTintColor;
    private int mTouchSlop;
    private static int[] CHECKED_PRESSED_STATE = {android.R.attr.state_checked, android.R.attr.state_enabled, android.R.attr.state_pressed};
    private static int[] UNCHECKED_PRESSED_STATE = {-16842912, android.R.attr.state_enabled, android.R.attr.state_pressed};

    public SwitchButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDrawDebugRect = false;
        this.mAutoAdjustTextPosition = true;
        this.mRestoring = false;
        init(attributeSet);
    }

    public SwitchButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDrawDebugRect = false;
        this.mAutoAdjustTextPosition = true;
        this.mRestoring = false;
        init(attributeSet);
    }

    public SwitchButton(Context context) {
        super(context);
        this.mDrawDebugRect = false;
        this.mAutoAdjustTextPosition = true;
        this.mRestoring = false;
        init(null);
    }

    private void init(AttributeSet attributeSet) {
        float fMax;
        float fMax2;
        float f;
        float f2;
        String str;
        String str2;
        boolean z;
        int i;
        Drawable drawable;
        float f3;
        float f4;
        float dimension;
        ColorStateList colorStateList;
        Drawable drawable2;
        ColorStateList colorStateList2;
        float f5;
        float fMax3;
        int i2;
        boolean z2;
        float f6;
        float f7;
        TypedArray typedArrayObtainStyledAttributes;
        float f8;
        boolean z3;
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        this.mPaint = new Paint(1);
        this.mRectPaint = new Paint(1);
        this.mRectPaint.setStyle(Paint.Style.STROKE);
        this.mRectPaint.setStrokeWidth(getResources().getDisplayMetrics().density);
        this.mTextPaint = getPaint();
        this.mThumbRectF = new RectF();
        this.mBackRectF = new RectF();
        this.mSafeRectF = new RectF();
        this.mThumbSizeF = new PointF();
        this.mThumbMargin = new RectF();
        this.mTextOnRectF = new RectF();
        this.mTextOffRectF = new RectF();
        this.mProcessAnimator = ObjectAnimator.ofFloat(this, "process", 0.0f, 0.0f).setDuration(250L);
        this.mProcessAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mPresentThumbRectF = new RectF();
        float f9 = getResources().getDisplayMetrics().density;
        float dimension2 = f9 * 2.0f;
        float f10 = f9 * 20.0f;
        float f11 = f10 / 2.0f;
        TypedArray typedArrayObtainStyledAttributes2 = attributeSet == null ? null : getContext().obtainStyledAttributes(attributeSet, R.styleable.KswSwitchButton);
        if (typedArrayObtainStyledAttributes2 != null) {
            drawable = typedArrayObtainStyledAttributes2.getDrawable(R.styleable.KswSwitchButton_kswThumbDrawable);
            colorStateList2 = typedArrayObtainStyledAttributes2.getColorStateList(R.styleable.KswSwitchButton_kswThumbColor);
            float dimension3 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbMargin, dimension2);
            float dimension4 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbMarginLeft, dimension3);
            dimension = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbMarginRight, dimension3);
            float dimension5 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbMarginTop, dimension3);
            float dimension6 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbMarginBottom, dimension3);
            fMax2 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbWidth, f10);
            float dimension7 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbHeight, f10);
            float dimension8 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswThumbRadius, Math.min(fMax2, dimension7) / 2.0f);
            float dimension9 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswBackRadius, dimension8 + dimension2);
            drawable2 = typedArrayObtainStyledAttributes2.getDrawable(R.styleable.KswSwitchButton_kswBackDrawable);
            colorStateList = typedArrayObtainStyledAttributes2.getColorStateList(R.styleable.KswSwitchButton_kswBackColor);
            float f12 = typedArrayObtainStyledAttributes2.getFloat(R.styleable.KswSwitchButton_kswBackMeasureRatio, 1.8f);
            int integer = typedArrayObtainStyledAttributes2.getInteger(R.styleable.KswSwitchButton_kswAnimationDuration, 250);
            boolean z4 = typedArrayObtainStyledAttributes2.getBoolean(R.styleable.KswSwitchButton_kswFadeBack, true);
            int color = typedArrayObtainStyledAttributes2.getColor(R.styleable.KswSwitchButton_kswTintColor, 0);
            String string = typedArrayObtainStyledAttributes2.getString(R.styleable.KswSwitchButton_kswTextOn);
            String string2 = typedArrayObtainStyledAttributes2.getString(R.styleable.KswSwitchButton_kswTextOff);
            dimension2 = typedArrayObtainStyledAttributes2.getDimension(R.styleable.KswSwitchButton_kswTextMarginH, Math.max(dimension2, dimension9 / 2.0f));
            boolean z5 = typedArrayObtainStyledAttributes2.getBoolean(R.styleable.KswSwitchButton_kswAutoAdjustTextPosition, true);
            typedArrayObtainStyledAttributes2.recycle();
            f2 = dimension9;
            f5 = dimension4;
            f3 = dimension6;
            fMax = dimension7;
            f = dimension8;
            fMax3 = f12;
            i2 = integer;
            z2 = z4;
            i = color;
            str2 = string2;
            f4 = dimension5;
            z = z5;
            str = string;
        } else {
            fMax = f10;
            fMax2 = fMax;
            f = f11;
            f2 = f;
            str = null;
            str2 = null;
            z = true;
            i = 0;
            drawable = null;
            f3 = 0.0f;
            f4 = 0.0f;
            dimension = 0.0f;
            colorStateList = null;
            drawable2 = null;
            colorStateList2 = null;
            f5 = 0.0f;
            fMax3 = 1.8f;
            i2 = 250;
            z2 = true;
        }
        if (attributeSet == null) {
            f6 = f3;
            f7 = f4;
            typedArrayObtainStyledAttributes = null;
        } else {
            f6 = f3;
            f7 = f4;
            typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, new int[]{android.R.attr.focusable, android.R.attr.clickable});
        }
        if (typedArrayObtainStyledAttributes != null) {
            f8 = dimension;
            boolean z6 = typedArrayObtainStyledAttributes.getBoolean(0, true);
            boolean z7 = typedArrayObtainStyledAttributes.getBoolean(1, z6);
            setFocusable(z6);
            setClickable(z7);
            typedArrayObtainStyledAttributes.recycle();
        } else {
            f8 = dimension;
        }
        this.mTextOn = str;
        this.mTextOff = str2;
        this.mTextMarginH = dimension2;
        this.mAutoAdjustTextPosition = z;
        this.mThumbDrawable = drawable;
        this.mThumbColor = colorStateList2;
        this.mIsThumbUseDrawable = this.mThumbDrawable != null;
        this.mTintColor = i;
        if (this.mTintColor == 0) {
            TypedValue typedValue = new TypedValue();
            z3 = true;
            if (getContext().getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true)) {
                this.mTintColor = typedValue.data;
            } else {
                this.mTintColor = DEFAULT_TINT_COLOR;
            }
        } else {
            z3 = true;
        }
        if (!this.mIsThumbUseDrawable && this.mThumbColor == null) {
            this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
            this.mCurrThumbColor = this.mThumbColor.getDefaultColor();
        }
        if (this.mIsThumbUseDrawable) {
            fMax2 = Math.max(fMax2, this.mThumbDrawable.getMinimumWidth());
            fMax = Math.max(fMax, this.mThumbDrawable.getMinimumHeight());
        }
        this.mThumbSizeF.set(fMax2, fMax);
        this.mBackDrawable = drawable2;
        this.mBackColor = colorStateList;
        if (this.mBackDrawable == null) {
            z3 = false;
        }
        this.mIsBackUseDrawable = z3;
        if (!this.mIsBackUseDrawable && this.mBackColor == null) {
            this.mBackColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
            this.mCurrBackColor = this.mBackColor.getDefaultColor();
            this.mNextBackColor = this.mBackColor.getColorForState(CHECKED_PRESSED_STATE, this.mCurrBackColor);
        }
        this.mThumbMargin.set(f5, f7, f8, f6);
        if (this.mThumbMargin.width() >= 0.0f) {
            fMax3 = Math.max(fMax3, 1.0f);
        }
        this.mBackMeasureRatio = fMax3;
        this.mThumbRadius = f;
        this.mBackRadius = f2;
        this.mAnimationDuration = i2;
        this.mFadeBack = z2;
        this.mProcessAnimator.setDuration(this.mAnimationDuration);
        if (isChecked()) {
            setProcess(1.0f);
        }
    }

    private Layout makeLayout(CharSequence charSequence) {
        return new StaticLayout(charSequence, this.mTextPaint, (int) Math.ceil(Layout.getDesiredWidth(charSequence, this.mTextPaint)), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.mOnLayout == null && this.mTextOn != null) {
            this.mOnLayout = makeLayout(this.mTextOn);
        }
        if (this.mOffLayout == null && this.mTextOff != null) {
            this.mOffLayout = makeLayout(this.mTextOff);
        }
        setMeasuredDimension(measureWidth(i), measureHeight(i2));
    }

    private int measureWidth(int i) {
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        int iCeil = ceil(this.mThumbSizeF.x * this.mBackMeasureRatio);
        if (this.mIsBackUseDrawable) {
            iCeil = Math.max(iCeil, this.mBackDrawable.getMinimumWidth());
        }
        float width = this.mOnLayout != null ? this.mOnLayout.getWidth() : 0.0f;
        float width2 = this.mOffLayout != null ? this.mOffLayout.getWidth() : 0.0f;
        if (width != 0.0f || width2 != 0.0f) {
            this.mTextWidth = Math.max(width, width2) + (this.mTextMarginH * 2.0f);
            float f = iCeil;
            float f2 = f - this.mThumbSizeF.x;
            if (f2 < this.mTextWidth) {
                iCeil = (int) (f + (this.mTextWidth - f2));
            }
        } else {
            this.mTextWidth = 0.0f;
        }
        int iMax = Math.max(iCeil, ceil(iCeil + this.mThumbMargin.left + this.mThumbMargin.right));
        int iMax2 = Math.max(Math.max(iMax, getPaddingLeft() + iMax + getPaddingRight()), getSuggestedMinimumWidth());
        if (mode == 1073741824) {
            return Math.max(iMax2, size);
        }
        return mode == Integer.MIN_VALUE ? Math.min(iMax2, size) : iMax2;
    }

    private int measureHeight(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int iCeil = ceil(Math.max(this.mThumbSizeF.y, this.mThumbSizeF.y + this.mThumbMargin.top + this.mThumbMargin.right));
        float height = this.mOnLayout != null ? this.mOnLayout.getHeight() : 0.0f;
        float height2 = this.mOffLayout != null ? this.mOffLayout.getHeight() : 0.0f;
        if (height != 0.0f || height2 != 0.0f) {
            this.mTextHeight = Math.max(height, height2);
            iCeil = ceil(Math.max(iCeil, this.mTextHeight));
        } else {
            this.mTextHeight = 0.0f;
        }
        int iMax = Math.max(iCeil, getSuggestedMinimumHeight());
        int iMax2 = Math.max(iMax, getPaddingTop() + iMax + getPaddingBottom());
        if (mode == 1073741824) {
            return Math.max(iMax2, size);
        }
        return mode == Integer.MIN_VALUE ? Math.min(iMax2, size) : iMax2;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i == i3 && i2 == i4) {
            return;
        }
        setup();
    }

    private int ceil(double d) {
        return (int) Math.ceil(d);
    }

    private void setup() {
        float paddingTop = getPaddingTop() + Math.max(0.0f, this.mThumbMargin.top);
        float paddingLeft = getPaddingLeft() + Math.max(0.0f, this.mThumbMargin.left);
        if (this.mOnLayout != null && this.mOffLayout != null && this.mThumbMargin.top + this.mThumbMargin.bottom > 0.0f) {
            paddingTop += (((((getMeasuredHeight() - getPaddingBottom()) - getPaddingTop()) - this.mThumbSizeF.y) - this.mThumbMargin.top) - this.mThumbMargin.bottom) / 2.0f;
        }
        if (this.mIsThumbUseDrawable) {
            this.mThumbSizeF.x = Math.max(this.mThumbSizeF.x, this.mThumbDrawable.getMinimumWidth());
            this.mThumbSizeF.y = Math.max(this.mThumbSizeF.y, this.mThumbDrawable.getMinimumHeight());
        }
        this.mThumbRectF.set(paddingLeft, paddingTop, this.mThumbSizeF.x + paddingLeft, this.mThumbSizeF.y + paddingTop);
        float f = this.mThumbRectF.left - this.mThumbMargin.left;
        float fMin = Math.min(0.0f, ((Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth) - this.mThumbRectF.width()) - this.mTextWidth) / 2.0f);
        float fMin2 = Math.min(0.0f, (((this.mThumbRectF.height() + this.mThumbMargin.top) + this.mThumbMargin.bottom) - this.mTextHeight) / 2.0f);
        this.mBackRectF.set(f + fMin, (this.mThumbRectF.top - this.mThumbMargin.top) + fMin2, (((f + this.mThumbMargin.left) + Math.max(this.mThumbSizeF.x * this.mBackMeasureRatio, this.mThumbSizeF.x + this.mTextWidth)) + this.mThumbMargin.right) - fMin, (this.mThumbRectF.bottom + this.mThumbMargin.bottom) - fMin2);
        this.mSafeRectF.set(this.mThumbRectF.left, 0.0f, (this.mBackRectF.right - this.mThumbMargin.right) - this.mThumbRectF.width(), 0.0f);
        this.mBackRadius = Math.min(Math.min(this.mBackRectF.width(), this.mBackRectF.height()) / 2.0f, this.mBackRadius);
        if (this.mBackDrawable != null) {
            this.mBackDrawable.setBounds((int) this.mBackRectF.left, (int) this.mBackRectF.top, ceil(this.mBackRectF.right), ceil(this.mBackRectF.bottom));
        }
        if (this.mOnLayout != null) {
            float fWidth = this.mBackRectF.left + ((((this.mBackRectF.width() - this.mThumbRectF.width()) - this.mThumbMargin.right) - this.mOnLayout.getWidth()) / 2.0f) + (this.mThumbMargin.left < 0.0f ? this.mThumbMargin.left * (-0.5f) : 0.0f);
            if (!this.mIsBackUseDrawable && this.mAutoAdjustTextPosition) {
                fWidth += this.mBackRadius / 4.0f;
            }
            float fHeight = this.mBackRectF.top + ((this.mBackRectF.height() - this.mOnLayout.getHeight()) / 2.0f);
            this.mTextOnRectF.set(fWidth, fHeight, this.mOnLayout.getWidth() + fWidth, this.mOnLayout.getHeight() + fHeight);
        }
        if (this.mOffLayout != null) {
            float fWidth2 = ((this.mBackRectF.right - ((((this.mBackRectF.width() - this.mThumbRectF.width()) - this.mThumbMargin.left) - this.mOffLayout.getWidth()) / 2.0f)) - this.mOffLayout.getWidth()) + (this.mThumbMargin.right < 0.0f ? this.mThumbMargin.right * 0.5f : 0.0f);
            if (!this.mIsBackUseDrawable && this.mAutoAdjustTextPosition) {
                fWidth2 -= this.mBackRadius / 4.0f;
            }
            float fHeight2 = this.mBackRectF.top + ((this.mBackRectF.height() - this.mOffLayout.getHeight()) / 2.0f);
            this.mTextOffRectF.set(fWidth2, fHeight2, this.mOffLayout.getWidth() + fWidth2, this.mOffLayout.getHeight() + fHeight2);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x012c  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x012f  */
    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void onDraw(android.graphics.Canvas r14) {
        /*
            Method dump skipped, instructions count: 501
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kyleduo.switchbutton.SwitchButton.onDraw(android.graphics.Canvas):void");
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (!this.mIsThumbUseDrawable && this.mThumbColor != null) {
            this.mCurrThumbColor = this.mThumbColor.getColorForState(getDrawableState(), this.mCurrThumbColor);
        } else {
            setDrawableState(this.mThumbDrawable);
        }
        int[] iArr = isChecked() ? UNCHECKED_PRESSED_STATE : CHECKED_PRESSED_STATE;
        ColorStateList textColors = getTextColors();
        if (textColors != null) {
            int defaultColor = textColors.getDefaultColor();
            this.mOnTextColor = textColors.getColorForState(CHECKED_PRESSED_STATE, defaultColor);
            this.mOffTextColor = textColors.getColorForState(UNCHECKED_PRESSED_STATE, defaultColor);
        }
        if (!this.mIsBackUseDrawable && this.mBackColor != null) {
            this.mCurrBackColor = this.mBackColor.getColorForState(getDrawableState(), this.mCurrBackColor);
            this.mNextBackColor = this.mBackColor.getColorForState(iArr, this.mCurrBackColor);
            return;
        }
        if ((this.mBackDrawable instanceof StateListDrawable) && this.mFadeBack) {
            this.mBackDrawable.setState(iArr);
            this.mNextBackDrawable = this.mBackDrawable.getCurrent().mutate();
        } else {
            this.mNextBackDrawable = null;
        }
        setDrawableState(this.mBackDrawable);
        if (this.mBackDrawable != null) {
            this.mCurrentBackDrawable = this.mBackDrawable.getCurrent().mutate();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled() || !isClickable() || !isFocusable()) {
            return false;
        }
        int action = motionEvent.getAction();
        float x = motionEvent.getX() - this.mStartX;
        float y = motionEvent.getY() - this.mStartY;
        switch (action) {
            case 0:
                catchView();
                this.mStartX = motionEvent.getX();
                this.mStartY = motionEvent.getY();
                this.mLastX = this.mStartX;
                setPressed(true);
                return true;
            case 1:
            case 3:
                setPressed(false);
                boolean statusBasedOnPos = getStatusBasedOnPos();
                float eventTime = motionEvent.getEventTime() - motionEvent.getDownTime();
                if (x < this.mTouchSlop && y < this.mTouchSlop && eventTime < this.mClickTimeout) {
                    performClick();
                } else if (statusBasedOnPos != isChecked()) {
                    playSoundEffect(0);
                    setChecked(statusBasedOnPos);
                } else {
                    animateToState(statusBasedOnPos);
                }
                return true;
            case 2:
                float x2 = motionEvent.getX();
                setProcess(getProcess() + ((x2 - this.mLastX) / this.mSafeRectF.width()));
                this.mLastX = x2;
                return true;
            default:
                return true;
        }
    }

    private boolean getStatusBasedOnPos() {
        return getProcess() > 0.5f;
    }

    public final float getProcess() {
        return this.mProcess;
    }

    public final void setProcess(float f) {
        if (f > 1.0f) {
            f = 1.0f;
        } else if (f < 0.0f) {
            f = 0.0f;
        }
        this.mProcess = f;
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.view.View
    public boolean performClick() {
        return super.performClick();
    }

    protected void animateToState(boolean z) {
        if (this.mProcessAnimator == null) {
            return;
        }
        if (this.mProcessAnimator.isRunning()) {
            this.mProcessAnimator.cancel();
        }
        this.mProcessAnimator.setDuration(this.mAnimationDuration);
        if (z) {
            this.mProcessAnimator.setFloatValues(this.mProcess, 1.0f);
        } else {
            this.mProcessAnimator.setFloatValues(this.mProcess, 0.0f);
        }
        this.mProcessAnimator.start();
    }

    private void catchView() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        if (isChecked() != z) {
            animateToState(z);
        }
        if (this.mRestoring) {
            setCheckedImmediatelyNoEvent(z);
        } else {
            super.setChecked(z);
        }
    }

    public void setCheckedNoEvent(boolean z) {
        if (this.mChildOnCheckedChangeListener == null) {
            setChecked(z);
            return;
        }
        super.setOnCheckedChangeListener(null);
        setChecked(z);
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void setCheckedImmediatelyNoEvent(boolean z) {
        if (this.mChildOnCheckedChangeListener == null) {
            setCheckedImmediately(z);
            return;
        }
        super.setOnCheckedChangeListener(null);
        setCheckedImmediately(z);
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void toggleNoEvent() {
        if (this.mChildOnCheckedChangeListener == null) {
            toggle();
            return;
        }
        super.setOnCheckedChangeListener(null);
        toggle();
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    public void toggleImmediatelyNoEvent() {
        if (this.mChildOnCheckedChangeListener == null) {
            toggleImmediately();
            return;
        }
        super.setOnCheckedChangeListener(null);
        toggleImmediately();
        super.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    @Override // android.widget.CompoundButton
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        super.setOnCheckedChangeListener(onCheckedChangeListener);
        this.mChildOnCheckedChangeListener = onCheckedChangeListener;
    }

    public void setCheckedImmediately(boolean z) {
        super.setChecked(z);
        if (this.mProcessAnimator != null && this.mProcessAnimator.isRunning()) {
            this.mProcessAnimator.cancel();
        }
        setProcess(z ? 1.0f : 0.0f);
        invalidate();
    }

    public void toggleImmediately() {
        setCheckedImmediately(!isChecked());
    }

    private void setDrawableState(Drawable drawable) {
        if (drawable != null) {
            drawable.setState(getDrawableState());
            invalidate();
        }
    }

    public boolean isDrawDebugRect() {
        return this.mDrawDebugRect;
    }

    public void setDrawDebugRect(boolean z) {
        this.mDrawDebugRect = z;
        invalidate();
    }

    public long getAnimationDuration() {
        return this.mAnimationDuration;
    }

    public void setAnimationDuration(long j) {
        this.mAnimationDuration = j;
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public void setThumbDrawable(Drawable drawable) {
        this.mThumbDrawable = drawable;
        this.mIsThumbUseDrawable = this.mThumbDrawable != null;
        setup();
        refreshDrawableState();
        requestLayout();
        invalidate();
    }

    public void setThumbDrawableRes(int i) {
        setThumbDrawable(ContextCompat.getDrawable(getContext(), i));
    }

    public Drawable getBackDrawable() {
        return this.mBackDrawable;
    }

    public void setBackDrawable(Drawable drawable) {
        this.mBackDrawable = drawable;
        this.mIsBackUseDrawable = this.mBackDrawable != null;
        setup();
        refreshDrawableState();
        requestLayout();
        invalidate();
    }

    public void setBackDrawableRes(int i) {
        setBackDrawable(ContextCompat.getDrawable(getContext(), i));
    }

    public ColorStateList getBackColor() {
        return this.mBackColor;
    }

    public void setBackColor(ColorStateList colorStateList) {
        this.mBackColor = colorStateList;
        if (this.mBackColor != null) {
            setBackDrawable(null);
        }
        invalidate();
    }

    public void setBackColorRes(int i) {
        setBackColor(ContextCompat.getColorStateList(getContext(), i));
    }

    public ColorStateList getThumbColor() {
        return this.mThumbColor;
    }

    public void setThumbColor(ColorStateList colorStateList) {
        this.mThumbColor = colorStateList;
        if (this.mThumbColor != null) {
            setThumbDrawable(null);
        }
    }

    public void setThumbColorRes(int i) {
        setThumbColor(ContextCompat.getColorStateList(getContext(), i));
    }

    public float getBackMeasureRatio() {
        return this.mBackMeasureRatio;
    }

    public void setBackMeasureRatio(float f) {
        this.mBackMeasureRatio = f;
        requestLayout();
    }

    public RectF getThumbMargin() {
        return this.mThumbMargin;
    }

    public void setThumbMargin(RectF rectF) {
        if (rectF == null) {
            setThumbMargin(0.0f, 0.0f, 0.0f, 0.0f);
        } else {
            setThumbMargin(rectF.left, rectF.top, rectF.right, rectF.bottom);
        }
    }

    public void setThumbMargin(float f, float f2, float f3, float f4) {
        this.mThumbMargin.set(f, f2, f3, f4);
        requestLayout();
    }

    public void setThumbSize(float f, float f2) {
        this.mThumbSizeF.set(f, f2);
        setup();
        requestLayout();
    }

    public float getThumbWidth() {
        return this.mThumbSizeF.x;
    }

    public float getThumbHeight() {
        return this.mThumbSizeF.y;
    }

    public void setThumbSize(PointF pointF) {
        if (pointF == null) {
            float f = getResources().getDisplayMetrics().density * 20.0f;
            setThumbSize(f, f);
        } else {
            setThumbSize(pointF.x, pointF.y);
        }
    }

    public PointF getThumbSizeF() {
        return this.mThumbSizeF;
    }

    public float getThumbRadius() {
        return this.mThumbRadius;
    }

    public void setThumbRadius(float f) {
        this.mThumbRadius = f;
        if (this.mIsThumbUseDrawable) {
            return;
        }
        invalidate();
    }

    public PointF getBackSizeF() {
        return new PointF(this.mBackRectF.width(), this.mBackRectF.height());
    }

    public float getBackRadius() {
        return this.mBackRadius;
    }

    public void setBackRadius(float f) {
        this.mBackRadius = f;
        if (this.mIsBackUseDrawable) {
            return;
        }
        invalidate();
    }

    public boolean isFadeBack() {
        return this.mFadeBack;
    }

    public void setFadeBack(boolean z) {
        this.mFadeBack = z;
    }

    public int getTintColor() {
        return this.mTintColor;
    }

    public void setTintColor(int i) {
        this.mTintColor = i;
        this.mThumbColor = ColorUtils.generateThumbColorWithTintColor(this.mTintColor);
        this.mBackColor = ColorUtils.generateBackColorWithTintColor(this.mTintColor);
        this.mIsBackUseDrawable = false;
        this.mIsThumbUseDrawable = false;
        refreshDrawableState();
        invalidate();
    }

    public void setText(CharSequence charSequence, CharSequence charSequence2) {
        this.mTextOn = charSequence;
        this.mTextOff = charSequence2;
        this.mOnLayout = null;
        this.mOffLayout = null;
        requestLayout();
        invalidate();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.onText = this.mTextOn;
        savedState.offText = this.mTextOff;
        return savedState;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        setText(savedState.onText, savedState.offText);
        this.mRestoring = true;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mRestoring = false;
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.kyleduo.switchbutton.SwitchButton.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        CharSequence offText;
        CharSequence onText;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.onText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.offText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            TextUtils.writeToParcel(this.onText, parcel, i);
            TextUtils.writeToParcel(this.offText, parcel, i);
        }
    }
}
