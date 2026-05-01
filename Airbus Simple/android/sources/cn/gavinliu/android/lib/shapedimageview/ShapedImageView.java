package cn.gavinliu.android.lib.shapedimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.util.Arrays;

/* loaded from: classes.dex */
public class ShapedImageView extends ImageView {
    public static final int SHAPE_MODE_CIRCLE = 2;
    public static final int SHAPE_MODE_ROUND_RECT = 1;
    private PorterDuffXfermode DST_IN;
    private PorterDuffXfermode DST_OUT;
    private PathExtension mExtension;
    private Paint mPaint;
    private Path mPath;
    private Paint mPathPaint;
    private float mRadius;
    private Shape mShape;
    private Bitmap mShapeBitmap;
    private boolean mShapeChanged;
    private int mShapeMode;
    private Bitmap mStrokeBitmap;
    private int mStrokeColor;
    private Paint mStrokePaint;
    private Shape mStrokeShape;
    private float mStrokeWidth;

    public interface PathExtension {
        void onLayout(Path path, int i, int i2);
    }

    public ShapedImageView(Context context) {
        super(context);
        this.mShapeMode = 0;
        this.mRadius = 0.0f;
        this.mStrokeColor = 637534208;
        this.mStrokeWidth = 0.0f;
        this.DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        this.DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        init(null);
    }

    public ShapedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShapeMode = 0;
        this.mRadius = 0.0f;
        this.mStrokeColor = 637534208;
        this.mStrokeWidth = 0.0f;
        this.DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        this.DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        init(attributeSet);
    }

    public ShapedImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShapeMode = 0;
        this.mRadius = 0.0f;
        this.mStrokeColor = 637534208;
        this.mStrokeWidth = 0.0f;
        this.DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        this.DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        setLayerType(2, null);
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ShapedImageView);
            this.mShapeMode = typedArrayObtainStyledAttributes.getInt(R.styleable.ShapedImageView_shape_mode, 0);
            this.mRadius = typedArrayObtainStyledAttributes.getDimension(R.styleable.ShapedImageView_round_radius, 0.0f);
            this.mStrokeWidth = typedArrayObtainStyledAttributes.getDimension(R.styleable.ShapedImageView_stroke_width, 0.0f);
            this.mStrokeColor = typedArrayObtainStyledAttributes.getColor(R.styleable.ShapedImageView_stroke_color, this.mStrokeColor);
            typedArrayObtainStyledAttributes.recycle();
        }
        this.mPaint = new Paint(1);
        this.mPaint.setFilterBitmap(true);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPaint.setXfermode(this.DST_IN);
        this.mStrokePaint = new Paint(1);
        this.mStrokePaint.setFilterBitmap(true);
        this.mStrokePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPathPaint = new Paint(1);
        this.mPathPaint.setFilterBitmap(true);
        this.mPathPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPathPaint.setXfermode(this.DST_OUT);
        this.mPath = new Path();
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z || this.mShapeChanged) {
            this.mShapeChanged = false;
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            switch (this.mShapeMode) {
                case 2:
                    this.mRadius = Math.min(measuredWidth, measuredHeight) / 2.0f;
                    break;
            }
            if (this.mShape == null || this.mRadius != 0.0f) {
                float[] fArr = new float[8];
                Arrays.fill(fArr, this.mRadius);
                this.mShape = new RoundRectShape(fArr, null, null);
                this.mStrokeShape = new RoundRectShape(fArr, null, null);
            }
            float f = measuredWidth;
            float f2 = measuredHeight;
            this.mShape.resize(f, f2);
            this.mStrokeShape.resize(f - (this.mStrokeWidth * 2.0f), f2 - (this.mStrokeWidth * 2.0f));
            makeStrokeBitmap();
            makeShapeBitmap();
            if (this.mExtension != null) {
                this.mExtension.onLayout(this.mPath, measuredWidth, measuredHeight);
            }
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mStrokeWidth > 0.0f && this.mStrokeShape != null) {
            if (this.mStrokeBitmap == null || this.mStrokeBitmap.isRecycled()) {
                makeStrokeBitmap();
            }
            int iSaveLayer = canvas.saveLayer(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), null, 31);
            this.mStrokePaint.setXfermode(null);
            canvas.drawBitmap(this.mStrokeBitmap, 0.0f, 0.0f, this.mStrokePaint);
            canvas.translate(this.mStrokeWidth, this.mStrokeWidth);
            this.mStrokePaint.setXfermode(this.DST_OUT);
            this.mStrokeShape.draw(canvas, this.mStrokePaint);
            canvas.restoreToCount(iSaveLayer);
        }
        if (this.mExtension != null) {
            canvas.drawPath(this.mPath, this.mPathPaint);
        }
        switch (this.mShapeMode) {
            case 1:
            case 2:
                if (this.mShapeBitmap == null || this.mShapeBitmap.isRecycled()) {
                    makeShapeBitmap();
                }
                canvas.drawBitmap(this.mShapeBitmap, 0.0f, 0.0f, this.mPaint);
                break;
        }
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseBitmap(this.mShapeBitmap);
        releaseBitmap(this.mStrokeBitmap);
    }

    private void makeStrokeBitmap() {
        if (this.mStrokeWidth <= 0.0f) {
            return;
        }
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }
        releaseBitmap(this.mStrokeBitmap);
        this.mStrokeBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mStrokeBitmap);
        Paint paint = new Paint(1);
        paint.setColor(this.mStrokeColor);
        canvas.drawRect(new RectF(0.0f, 0.0f, measuredWidth, measuredHeight), paint);
    }

    private void releaseBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }

    private void makeShapeBitmap() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }
        releaseBitmap(this.mShapeBitmap);
        this.mShapeBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(this.mShapeBitmap);
        Paint paint = new Paint(1);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mShape.draw(canvas, paint);
    }

    public void setExtension(PathExtension pathExtension) {
        this.mExtension = pathExtension;
        requestLayout();
    }

    public void setStroke(int i, float f) {
        if (this.mStrokeWidth <= 0.0f) {
            return;
        }
        if (this.mStrokeWidth != f) {
            this.mStrokeWidth = f;
            this.mStrokeShape.resize(getMeasuredWidth() - (this.mStrokeWidth * 2.0f), getMeasuredHeight() - (this.mStrokeWidth * 2.0f));
            postInvalidate();
        }
        if (this.mStrokeColor != i) {
            this.mStrokeColor = i;
            makeStrokeBitmap();
            postInvalidate();
        }
    }

    public void setStrokeColor(int i) {
        setStroke(i, this.mStrokeWidth);
    }

    public void setStrokeWidth(float f) {
        setStroke(this.mStrokeColor, f);
    }

    public void setShape(int i, float f) {
        this.mShapeChanged = (this.mShapeMode == i && this.mRadius == f) ? false : true;
        if (this.mShapeChanged) {
            this.mShapeMode = i;
            this.mRadius = f;
            this.mShape = null;
            this.mStrokeShape = null;
            requestLayout();
        }
    }

    public void setShapeMode(int i) {
        setShape(i, this.mRadius);
    }

    public void setShapeRadius(float f) {
        setShape(this.mShapeMode, f);
    }
}
