package com.shima.smartbushome.assist.holocolorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.shima.smartbushome.R;

/* loaded from: classes.dex */
public class SVBar extends View {
    private static final boolean ORIENTATION_DEFAULT = true;
    private static final boolean ORIENTATION_HORIZONTAL = true;
    private static final boolean ORIENTATION_VERTICAL = false;
    private static final String STATE_COLOR = "color";
    private static final String STATE_ORIENTATION = "orientation";
    private static final String STATE_PARENT = "parent";
    private static final String STATE_SATURATION = "saturation";
    private static final String STATE_VALUE = "value";
    private int mBarLength;
    private Paint mBarPaint;
    private Paint mBarPointerHaloPaint;
    private int mBarPointerHaloRadius;
    private Paint mBarPointerPaint;
    private int mBarPointerPosition;
    private int mBarPointerRadius;
    private RectF mBarRect;
    private int mBarThickness;
    private int mColor;
    private float[] mHSVColor;
    private boolean mIsMovingPointer;
    private boolean mOrientation;
    private ColorPicker mPicker;
    private float mPosToSVFactor;
    private int mPreferredBarLength;
    private float mSVToPosFactor;
    private Shader shader;

    public SVBar(Context context) {
        super(context);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(null, 0);
    }

    public SVBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attributeSet, 0);
    }

    public SVBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attributeSet, i);
    }

    private void init(AttributeSet attributeSet, int i) {
        TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorBars, i, 0);
        Resources resources = getContext().getResources();
        this.mBarThickness = typedArrayObtainStyledAttributes.getDimensionPixelSize(4, resources.getDimensionPixelSize(R.dimen.bar_thickness));
        this.mBarLength = typedArrayObtainStyledAttributes.getDimensionPixelSize(0, resources.getDimensionPixelSize(R.dimen.bar_length));
        this.mPreferredBarLength = this.mBarLength;
        this.mBarPointerRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(3, resources.getDimensionPixelSize(R.dimen.bar_pointer_radius));
        this.mBarPointerHaloRadius = typedArrayObtainStyledAttributes.getDimensionPixelSize(2, resources.getDimensionPixelSize(R.dimen.bar_pointer_halo_radius));
        this.mOrientation = typedArrayObtainStyledAttributes.getBoolean(1, true);
        typedArrayObtainStyledAttributes.recycle();
        this.mBarPaint = new Paint(1);
        this.mBarPaint.setShader(this.shader);
        this.mBarPointerPosition = (this.mBarLength / 2) + this.mBarPointerHaloRadius;
        this.mBarPointerHaloPaint = new Paint(1);
        this.mBarPointerHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mBarPointerHaloPaint.setAlpha(80);
        this.mBarPointerPaint = new Paint(1);
        this.mBarPointerPaint.setColor(-8257792);
        this.mPosToSVFactor = 1.0f / (this.mBarLength / 2.0f);
        this.mSVToPosFactor = (this.mBarLength / 2.0f) / 1.0f;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int iMin = this.mPreferredBarLength + (this.mBarPointerHaloRadius * 2);
        if (!this.mOrientation) {
            i = i2;
        }
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            iMin = size;
        } else if (mode == Integer.MIN_VALUE) {
            iMin = Math.min(iMin, size);
        }
        int i3 = this.mBarPointerHaloRadius * 2;
        this.mBarLength = iMin - i3;
        if (!this.mOrientation) {
            setMeasuredDimension(i3, this.mBarLength + i3);
        } else {
            setMeasuredDimension(this.mBarLength + i3, i3);
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mOrientation) {
            i5 = this.mBarLength + this.mBarPointerHaloRadius;
            i6 = this.mBarThickness;
            this.mBarLength = i - (this.mBarPointerHaloRadius * 2);
            this.mBarRect.set(this.mBarPointerHaloRadius, this.mBarPointerHaloRadius - (this.mBarThickness / 2), this.mBarLength + this.mBarPointerHaloRadius, this.mBarPointerHaloRadius + (this.mBarThickness / 2));
        } else {
            i5 = this.mBarThickness;
            i6 = this.mBarLength + this.mBarPointerHaloRadius;
            this.mBarLength = i2 - (this.mBarPointerHaloRadius * 2);
            this.mBarRect.set(this.mBarPointerHaloRadius - (this.mBarThickness / 2), this.mBarPointerHaloRadius, this.mBarPointerHaloRadius + (this.mBarThickness / 2), this.mBarLength + this.mBarPointerHaloRadius);
        }
        if (!isInEditMode()) {
            this.shader = new LinearGradient(this.mBarPointerHaloRadius, 0.0f, i5, i6, new int[]{-1, Color.HSVToColor(this.mHSVColor), ViewCompat.MEASURED_STATE_MASK}, (float[]) null, Shader.TileMode.CLAMP);
        } else {
            this.shader = new LinearGradient(this.mBarPointerHaloRadius, 0.0f, i5, i6, new int[]{-1, -8257792, ViewCompat.MEASURED_STATE_MASK}, (float[]) null, Shader.TileMode.CLAMP);
            Color.colorToHSV(-8257792, this.mHSVColor);
        }
        this.mBarPaint.setShader(this.shader);
        this.mPosToSVFactor = 1.0f / (this.mBarLength / 2.0f);
        this.mSVToPosFactor = (this.mBarLength / 2.0f) / 1.0f;
        float[] fArr = new float[3];
        Color.colorToHSV(this.mColor, fArr);
        if (fArr[1] < fArr[2]) {
            this.mBarPointerPosition = Math.round((this.mSVToPosFactor * fArr[1]) + this.mBarPointerHaloRadius);
        } else {
            this.mBarPointerPosition = Math.round((this.mSVToPosFactor * (1.0f - fArr[2])) + this.mBarPointerHaloRadius + (this.mBarLength / 2));
        }
        if (isInEditMode()) {
            this.mBarPointerPosition = (this.mBarLength / 2) + this.mBarPointerHaloRadius;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        int i2;
        canvas.drawRect(this.mBarRect, this.mBarPaint);
        if (this.mOrientation) {
            i = this.mBarPointerPosition;
            i2 = this.mBarPointerHaloRadius;
        } else {
            i = this.mBarPointerHaloRadius;
            i2 = this.mBarPointerPosition;
        }
        float f = i;
        float f2 = i2;
        canvas.drawCircle(f, f2, this.mBarPointerHaloRadius, this.mBarPointerHaloPaint);
        canvas.drawCircle(f, f2, this.mBarPointerRadius, this.mBarPointerPaint);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float y;
        getParent().requestDisallowInterceptTouchEvent(true);
        if (this.mOrientation) {
            y = motionEvent.getX();
        } else {
            y = motionEvent.getY();
        }
        switch (motionEvent.getAction()) {
            case 0:
                this.mIsMovingPointer = true;
                if (y >= this.mBarPointerHaloRadius && y <= this.mBarPointerHaloRadius + this.mBarLength) {
                    this.mBarPointerPosition = Math.round(y);
                    calculateColor(Math.round(y));
                    this.mBarPointerPaint.setColor(this.mColor);
                    invalidate();
                }
                return true;
            case 1:
                this.mIsMovingPointer = false;
                return true;
            case 2:
                if (this.mIsMovingPointer) {
                    if (y >= this.mBarPointerHaloRadius && y <= this.mBarPointerHaloRadius + this.mBarLength) {
                        this.mBarPointerPosition = Math.round(y);
                        calculateColor(Math.round(y));
                        this.mBarPointerPaint.setColor(this.mColor);
                        if (this.mPicker != null) {
                            this.mPicker.setNewCenterColor(this.mColor);
                            this.mPicker.changeOpacityBarColor(this.mColor);
                        }
                        invalidate();
                    } else if (y < this.mBarPointerHaloRadius) {
                        this.mBarPointerPosition = this.mBarPointerHaloRadius;
                        this.mColor = -1;
                        this.mBarPointerPaint.setColor(this.mColor);
                        if (this.mPicker != null) {
                            this.mPicker.setNewCenterColor(this.mColor);
                            this.mPicker.changeOpacityBarColor(this.mColor);
                        }
                        invalidate();
                    } else if (y > this.mBarPointerHaloRadius + this.mBarLength) {
                        this.mBarPointerPosition = this.mBarPointerHaloRadius + this.mBarLength;
                        this.mColor = ViewCompat.MEASURED_STATE_MASK;
                        this.mBarPointerPaint.setColor(this.mColor);
                        if (this.mPicker != null) {
                            this.mPicker.setNewCenterColor(this.mColor);
                            this.mPicker.changeOpacityBarColor(this.mColor);
                        }
                        invalidate();
                    }
                }
                return true;
            default:
                return true;
        }
    }

    public void setSaturation(float f) {
        this.mBarPointerPosition = Math.round((this.mSVToPosFactor * f) + this.mBarPointerHaloRadius);
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            this.mPicker.changeOpacityBarColor(this.mColor);
        }
        invalidate();
    }

    public void setValue(float f) {
        this.mBarPointerPosition = Math.round((this.mSVToPosFactor * (1.0f - f)) + this.mBarPointerHaloRadius + (this.mBarLength / 2));
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            this.mPicker.changeOpacityBarColor(this.mColor);
        }
        invalidate();
    }

    public void setColor(int i, boolean z) {
        int i2;
        int i3;
        if (this.mOrientation) {
            i2 = this.mBarLength + this.mBarPointerHaloRadius;
            i3 = this.mBarThickness;
        } else {
            i2 = this.mBarThickness;
            i3 = this.mBarLength + this.mBarPointerHaloRadius;
        }
        Color.colorToHSV(i, this.mHSVColor);
        this.shader = new LinearGradient(this.mBarPointerHaloRadius, 0.0f, i2, i3, new int[]{-1, i, ViewCompat.MEASURED_STATE_MASK}, (float[]) null, Shader.TileMode.CLAMP);
        this.mBarPaint.setShader(this.shader);
        if (z) {
            this.mBarPointerPosition = (this.mBarLength / 2) + this.mBarPointerHaloRadius;
            calculateColor(this.mBarPointerPosition);
        } else {
            calculateColor(this.mBarPointerPosition);
        }
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            if (this.mPicker.hasOpacityBar()) {
                this.mPicker.changeOpacityBarColor(this.mColor);
            }
        }
        invalidate();
    }

    private void calculateColor(int i) {
        int i2 = i - this.mBarPointerHaloRadius;
        if (i2 > this.mBarLength / 2 && i2 < this.mBarLength) {
            this.mColor = Color.HSVToColor(new float[]{this.mHSVColor[0], 1.0f, 1.0f - (this.mPosToSVFactor * (i2 - (this.mBarLength / 2)))});
            return;
        }
        if (i2 > 0 && i2 < this.mBarLength) {
            this.mColor = Color.HSVToColor(new float[]{this.mHSVColor[0], this.mPosToSVFactor * i2, 1.0f});
            return;
        }
        if (i2 == this.mBarLength / 2) {
            this.mColor = Color.HSVToColor(new float[]{this.mHSVColor[0], 1.0f, 1.0f});
        } else if (i2 <= 0) {
            this.mColor = -1;
        } else if (i2 >= this.mBarLength) {
            this.mColor = ViewCompat.MEASURED_STATE_MASK;
        }
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColorPicker(ColorPicker colorPicker) {
        this.mPicker = colorPicker;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelableOnSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_PARENT, parcelableOnSaveInstanceState);
        bundle.putFloatArray(STATE_COLOR, this.mHSVColor);
        float[] fArr = new float[3];
        Color.colorToHSV(this.mColor, fArr);
        if (fArr[1] < fArr[2]) {
            bundle.putFloat(STATE_SATURATION, fArr[1]);
        } else {
            bundle.putFloat(STATE_VALUE, fArr[2]);
        }
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable(STATE_PARENT));
        setColor(Color.HSVToColor(bundle.getFloatArray(STATE_COLOR)), false);
        if (bundle.containsKey(STATE_SATURATION)) {
            setSaturation(bundle.getFloat(STATE_SATURATION));
        } else {
            setValue(bundle.getFloat(STATE_VALUE));
        }
    }
}
