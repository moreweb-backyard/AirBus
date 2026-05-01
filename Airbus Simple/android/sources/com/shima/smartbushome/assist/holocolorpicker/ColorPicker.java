package com.shima.smartbushome.assist.holocolorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.shima.smartbushome.R;

/* loaded from: classes.dex */
public class ColorPicker extends View {
    private static final int[] COLORS = {SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
    private static final String STATE_ANGLE = "angle";
    private static final String STATE_OLD_COLOR = "color";
    private static final String STATE_PARENT = "parent";
    private static final String STATE_SHOW_OLD_COLOR = "showColor";
    private float mAngle;
    private Paint mCenterHaloPaint;
    private int mCenterNewColor;
    private Paint mCenterNewPaint;
    private int mCenterOldColor;
    private Paint mCenterOldPaint;
    private RectF mCenterRectangle;
    private int mColor;
    private int mColorCenterHaloRadius;
    private int mColorCenterRadius;
    private int mColorPointerHaloRadius;
    private int mColorPointerRadius;
    private Paint mColorWheelPaint;
    private int mColorWheelRadius;
    private RectF mColorWheelRectangle;
    private int mColorWheelThickness;
    private float[] mHSV;
    private OpacityBar mOpacityBar;
    private Paint mPointerColor;
    private Paint mPointerHaloPaint;
    private int mPreferredColorCenterHaloRadius;
    private int mPreferredColorCenterRadius;
    private int mPreferredColorWheelRadius;
    private SVBar mSVbar;
    private SaturationBar mSaturationBar;
    private boolean mShowCenterOldColor;
    private float mSlopX;
    private float mSlopY;
    private boolean mTouchAnywhereOnColorWheelEnabled;
    private float mTranslationOffset;
    private boolean mUserIsMovingPointer;
    private ValueBar mValueBar;
    private int oldChangedListenerColor;
    private int oldSelectedListenerColor;
    private OnColorChangedListener onColorChangedListener;
    private OnColorSelectedListener onColorSelectedListener;

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int i);
    }

    public ColorPicker(Context context) {
        super(context);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mTouchAnywhereOnColorWheelEnabled = true;
        this.mValueBar = null;
        init(null, 0);
    }

    public ColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mTouchAnywhereOnColorWheelEnabled = true;
        this.mValueBar = null;
        init(attributeSet, 0);
    }

    public ColorPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mTouchAnywhereOnColorWheelEnabled = true;
        this.mValueBar = null;
        init(attributeSet, i);
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.onColorChangedListener = onColorChangedListener;
    }

    public OnColorChangedListener getOnColorChangedListener() {
        return this.onColorChangedListener;
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener;
    }

    public OnColorSelectedListener getOnColorSelectedListener() {
        return this.onColorSelectedListener;
    }

    private void init(AttributeSet attributeSet, int i) {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorPicker, i, 0);
        Resources resources = getContext().getResources();
        this.mColorWheelThickness = typedArrayObtainStyledAttributes.getDimensionPixelSize(5, resources.getDimensionPixelSize(R.dimen.color_wheel_thickness));
        this.mColorWheelRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(4, resources.getDimensionPixelSize(R.dimen.color_wheel_radius));
        this.mPreferredColorWheelRadius = this.mColorWheelRadius;
        this.mColorCenterRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(1, resources.getDimensionPixelSize(R.dimen.color_center_radius));
        this.mPreferredColorCenterRadius = this.mColorCenterRadius;
        this.mColorCenterHaloRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(0, resources.getDimensionPixelSize(R.dimen.color_center_halo_radius));
        this.mPreferredColorCenterHaloRadius = this.mColorCenterHaloRadius;
        this.mColorPointerRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(3, resources.getDimensionPixelSize(R.dimen.color_pointer_radius));
        this.mColorPointerHaloRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(2, resources.getDimensionPixelSize(R.dimen.color_pointer_halo_radius));
        typedArrayObtainStyledAttributes.recycle();
        this.mAngle = -1.5707964f;
        SweepGradient sweepGradient = new SweepGradient(0.0f, 0.0f, COLORS, (float[]) null);
        this.mColorWheelPaint = new Paint(1);
        this.mColorWheelPaint.setShader(sweepGradient);
        this.mColorWheelPaint.setStyle(Paint.Style.STROKE);
        this.mColorWheelPaint.setStrokeWidth(this.mColorWheelThickness);
        this.mPointerHaloPaint = new Paint(1);
        this.mPointerHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPointerHaloPaint.setAlpha(80);
        this.mPointerColor = new Paint(1);
        this.mPointerColor.setColor(calculateColor(this.mAngle));
        this.mCenterNewPaint = new Paint(1);
        this.mCenterNewPaint.setColor(calculateColor(this.mAngle));
        this.mCenterNewPaint.setStyle(Paint.Style.FILL);
        this.mCenterOldPaint = new Paint(1);
        this.mCenterOldPaint.setColor(calculateColor(this.mAngle));
        this.mCenterOldPaint.setStyle(Paint.Style.FILL);
        this.mCenterHaloPaint = new Paint(1);
        this.mCenterHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterHaloPaint.setAlpha(0);
        this.mCenterNewColor = calculateColor(this.mAngle);
        this.mCenterOldColor = calculateColor(this.mAngle);
        this.mShowCenterOldColor = true;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.translate(this.mTranslationOffset, this.mTranslationOffset);
        canvas.drawOval(this.mColorWheelRectangle, this.mColorWheelPaint);
        float[] fArrCalculatePointerPosition = calculatePointerPosition(this.mAngle);
        canvas.drawCircle(fArrCalculatePointerPosition[0], fArrCalculatePointerPosition[1], this.mColorPointerHaloRadius, this.mPointerHaloPaint);
        canvas.drawCircle(fArrCalculatePointerPosition[0], fArrCalculatePointerPosition[1], this.mColorPointerRadius, this.mPointerColor);
        canvas.drawCircle(0.0f, 0.0f, this.mColorCenterHaloRadius, this.mCenterHaloPaint);
        canvas.drawArc(this.mCenterRectangle, 0.0f, 360.0f, true, this.mCenterNewPaint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int iMin = (this.mPreferredColorWheelRadius + this.mColorPointerHaloRadius) * 2;
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode != 1073741824) {
            size = mode == Integer.MIN_VALUE ? Math.min(iMin, size) : iMin;
        }
        if (mode2 == 1073741824) {
            iMin = size2;
        } else if (mode2 == Integer.MIN_VALUE) {
            iMin = Math.min(iMin, size2);
        }
        int iMin2 = Math.min(size, iMin);
        setMeasuredDimension(iMin2, iMin2);
        this.mTranslationOffset = iMin2 * 0.5f;
        this.mColorWheelRadius = ((iMin2 / 2) - this.mColorWheelThickness) - this.mColorPointerHaloRadius;
        this.mColorWheelRectangle.set(-this.mColorWheelRadius, -this.mColorWheelRadius, this.mColorWheelRadius, this.mColorWheelRadius);
        this.mColorCenterRadius = (int) (this.mPreferredColorCenterRadius * (this.mColorWheelRadius / this.mPreferredColorWheelRadius));
        this.mColorCenterHaloRadius = (int) (this.mPreferredColorCenterHaloRadius * (this.mColorWheelRadius / this.mPreferredColorWheelRadius));
        this.mCenterRectangle.set(-this.mColorCenterRadius, -this.mColorCenterRadius, this.mColorCenterRadius, this.mColorCenterRadius);
    }

    private int ave(int i, int i2, float f) {
        return i + Math.round(f * (i2 - i));
    }

    private int calculateColor(float f) {
        double d = f;
        Double.isNaN(d);
        float f2 = (float) (d / 6.283185307179586d);
        if (f2 < 0.0f) {
            f2 += 1.0f;
        }
        if (f2 <= 0.0f) {
            this.mColor = COLORS[0];
            return COLORS[0];
        }
        if (f2 >= 1.0f) {
            this.mColor = COLORS[COLORS.length - 1];
            return COLORS[COLORS.length - 1];
        }
        float length = f2 * (COLORS.length - 1);
        int i = (int) length;
        float f3 = length - i;
        int i2 = COLORS[i];
        int i3 = COLORS[i + 1];
        int iAve = ave(Color.alpha(i2), Color.alpha(i3), f3);
        int iAve2 = ave(Color.red(i2), Color.red(i3), f3);
        int iAve3 = ave(Color.green(i2), Color.green(i3), f3);
        int iAve4 = ave(Color.blue(i2), Color.blue(i3), f3);
        this.mColor = Color.argb(iAve, iAve2, iAve3, iAve4);
        return Color.argb(iAve, iAve2, iAve3, iAve4);
    }

    public int getColor() {
        return this.mCenterNewColor;
    }

    public void setColor(int i) {
        this.mAngle = colorToAngle(i);
        this.mPointerColor.setColor(calculateColor(this.mAngle));
        if (this.mOpacityBar != null) {
            this.mOpacityBar.setColor(this.mColor);
            this.mOpacityBar.setOpacity(Color.alpha(i));
        }
        if (this.mSVbar != null) {
            Color.colorToHSV(i, this.mHSV);
            this.mSVbar.setColor(this.mColor, true);
            if (this.mHSV[1] < this.mHSV[2]) {
                this.mSVbar.setSaturation(this.mHSV[1]);
            } else if (this.mHSV[1] > this.mHSV[2]) {
                this.mSVbar.setValue(this.mHSV[2]);
            }
        }
        if (this.mSaturationBar != null) {
            Color.colorToHSV(i, this.mHSV);
            this.mSaturationBar.setColor(this.mColor);
            this.mSaturationBar.setSaturation(this.mHSV[1]);
        }
        if (this.mValueBar != null && this.mSaturationBar == null) {
            Color.colorToHSV(i, this.mHSV);
            this.mValueBar.setColor(this.mColor);
            this.mValueBar.setValue(this.mHSV[2]);
        } else if (this.mValueBar != null) {
            Color.colorToHSV(i, this.mHSV);
            this.mValueBar.setValue(this.mHSV[2]);
        }
        setNewCenterColor(i);
    }

    private float colorToAngle(int i) {
        Color.colorToHSV(i, new float[3]);
        return (float) Math.toRadians(-r0[0]);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = motionEvent.getX() - this.mTranslationOffset;
        float y = motionEvent.getY() - this.mTranslationOffset;
        switch (motionEvent.getAction()) {
            case 0:
                float[] fArrCalculatePointerPosition = calculatePointerPosition(this.mAngle);
                if (x >= fArrCalculatePointerPosition[0] - this.mColorPointerHaloRadius && x <= fArrCalculatePointerPosition[0] + this.mColorPointerHaloRadius && y >= fArrCalculatePointerPosition[1] - this.mColorPointerHaloRadius && y <= fArrCalculatePointerPosition[1] + this.mColorPointerHaloRadius) {
                    this.mSlopX = x - fArrCalculatePointerPosition[0];
                    this.mSlopY = y - fArrCalculatePointerPosition[1];
                    this.mUserIsMovingPointer = true;
                    invalidate();
                } else if (x < (-this.mColorCenterRadius) || x > this.mColorCenterRadius || y < (-this.mColorCenterRadius) || y > this.mColorCenterRadius || !this.mShowCenterOldColor) {
                    double d = (x * x) + (y * y);
                    if (Math.sqrt(d) <= this.mColorWheelRadius + this.mColorPointerHaloRadius && Math.sqrt(d) >= this.mColorWheelRadius - this.mColorPointerHaloRadius && this.mTouchAnywhereOnColorWheelEnabled) {
                        this.mUserIsMovingPointer = true;
                        invalidate();
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                } else {
                    this.mCenterHaloPaint.setAlpha(80);
                    setColor(getOldCenterColor());
                    invalidate();
                }
                return true;
            case 1:
                this.mUserIsMovingPointer = false;
                this.mCenterHaloPaint.setAlpha(0);
                if (this.onColorSelectedListener != null && this.mCenterNewColor != this.oldSelectedListenerColor) {
                    this.onColorSelectedListener.onColorSelected(this.mCenterNewColor);
                    this.oldSelectedListenerColor = this.mCenterNewColor;
                }
                invalidate();
                return true;
            case 2:
                if (this.mUserIsMovingPointer) {
                    this.mAngle = (float) Math.atan2(y - this.mSlopY, x - this.mSlopX);
                    this.mPointerColor.setColor(calculateColor(this.mAngle));
                    int iCalculateColor = calculateColor(this.mAngle);
                    this.mCenterNewColor = iCalculateColor;
                    setNewCenterColor(iCalculateColor);
                    if (this.mOpacityBar != null) {
                        this.mOpacityBar.setColor(this.mColor);
                    }
                    if (this.mValueBar != null) {
                        this.mValueBar.setColor(this.mColor);
                    }
                    if (this.mSaturationBar != null) {
                        this.mSaturationBar.setColor(this.mColor);
                    }
                    if (this.mSVbar != null) {
                        this.mSVbar.setColor(this.mColor, false);
                    }
                    invalidate();
                    return true;
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            case 3:
                if (this.onColorSelectedListener != null && this.mCenterNewColor != this.oldSelectedListenerColor) {
                    this.onColorSelectedListener.onColorSelected(this.mCenterNewColor);
                    this.oldSelectedListenerColor = this.mCenterNewColor;
                }
                return true;
            default:
                return true;
        }
    }

    private float[] calculatePointerPosition(float f) {
        double d = this.mColorWheelRadius;
        double d2 = f;
        double dCos = Math.cos(d2);
        Double.isNaN(d);
        float f2 = (float) (d * dCos);
        double d3 = this.mColorWheelRadius;
        double dSin = Math.sin(d2);
        Double.isNaN(d3);
        return new float[]{f2, (float) (d3 * dSin)};
    }

    public void addSVBar(SVBar sVBar) {
        this.mSVbar = sVBar;
        this.mSVbar.setColorPicker(this);
        this.mSVbar.setColor(this.mColor, true);
    }

    public void addOpacityBar(OpacityBar opacityBar) {
        this.mOpacityBar = opacityBar;
        this.mOpacityBar.setColorPicker(this);
        this.mOpacityBar.setColor(this.mColor);
    }

    public void addSaturationBar(SaturationBar saturationBar) {
        this.mSaturationBar = saturationBar;
        this.mSaturationBar.setColorPicker(this);
        this.mSaturationBar.setColor(this.mColor);
    }

    public void addValueBar(ValueBar valueBar) {
        this.mValueBar = valueBar;
        this.mValueBar.setColorPicker(this);
        this.mValueBar.setColor(this.mColor);
    }

    public void setNewCenterColor(int i) {
        this.mCenterNewColor = i;
        this.mCenterNewPaint.setColor(i);
        if (this.mCenterOldColor == 0) {
            this.mCenterOldColor = i;
            this.mCenterOldPaint.setColor(i);
        }
        if (this.onColorChangedListener != null && i != this.oldChangedListenerColor) {
            this.onColorChangedListener.onColorChanged(i);
            this.oldChangedListenerColor = i;
        }
        invalidate();
    }

    public void setOldCenterColor(int i) {
        this.mCenterOldColor = i;
        this.mCenterOldPaint.setColor(i);
        invalidate();
    }

    public int getOldCenterColor() {
        return this.mCenterOldColor;
    }

    public void setShowOldCenterColor(boolean z) {
        this.mShowCenterOldColor = z;
        invalidate();
    }

    public boolean getShowOldCenterColor() {
        return this.mShowCenterOldColor;
    }

    public void changeOpacityBarColor(int i) {
        if (this.mOpacityBar != null) {
            this.mOpacityBar.setColor(i);
        }
    }

    public void changeSaturationBarColor(int i) {
        if (this.mSaturationBar != null) {
            this.mSaturationBar.setColor(i);
        }
    }

    public void changeValueBarColor(int i) {
        if (this.mValueBar != null) {
            this.mValueBar.setColor(i);
        }
    }

    public boolean hasOpacityBar() {
        return this.mOpacityBar != null;
    }

    public boolean hasValueBar() {
        return this.mValueBar != null;
    }

    public boolean hasSaturationBar() {
        return this.mSaturationBar != null;
    }

    public boolean hasSVBar() {
        return this.mSVbar != null;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelableOnSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_PARENT, parcelableOnSaveInstanceState);
        bundle.putFloat(STATE_ANGLE, this.mAngle);
        bundle.putInt(STATE_OLD_COLOR, this.mCenterOldColor);
        bundle.putBoolean(STATE_SHOW_OLD_COLOR, this.mShowCenterOldColor);
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable(STATE_PARENT));
        this.mAngle = bundle.getFloat(STATE_ANGLE);
        setOldCenterColor(bundle.getInt(STATE_OLD_COLOR));
        this.mShowCenterOldColor = bundle.getBoolean(STATE_SHOW_OLD_COLOR);
        int iCalculateColor = calculateColor(this.mAngle);
        this.mPointerColor.setColor(iCalculateColor);
        setNewCenterColor(iCalculateColor);
    }

    public void setTouchAnywhereOnColorWheelEnabled(boolean z) {
        this.mTouchAnywhereOnColorWheelEnabled = z;
    }

    public boolean getTouchAnywhereOnColorWheel() {
        return this.mTouchAnywhereOnColorWheelEnabled;
    }
}
