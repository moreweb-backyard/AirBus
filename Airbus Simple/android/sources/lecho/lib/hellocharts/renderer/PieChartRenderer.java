package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import java.util.Iterator;
import lecho.lib.hellocharts.formatter.PieChartValueFormatter;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.PieChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class PieChartRenderer extends AbstractChartRenderer {
    private static final float DEFAULT_LABEL_INSIDE_RADIUS_FACTOR = 0.7f;
    private static final float DEFAULT_LABEL_OUTSIDE_RADIUS_FACTOR = 1.0f;
    private static final int DEFAULT_START_ROTATION = 45;
    private static final int DEFAULT_TOUCH_ADDITIONAL_DP = 8;
    private static final float MAX_WIDTH_HEIGHT = 100.0f;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private Paint centerCirclePaint;
    private float centerCircleScale;
    private Paint.FontMetricsInt centerCircleText1FontMetrics;
    private Paint centerCircleText1Paint;
    private Paint.FontMetricsInt centerCircleText2FontMetrics;
    private Paint centerCircleText2Paint;
    private float circleFillRatio;
    private PieChartDataProvider dataProvider;
    private RectF drawCircleOval;
    private boolean hasCenterCircle;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private boolean hasLabelsOutside;
    private float maxSum;
    private RectF originCircleOval;
    private int rotation;
    private Paint separationLinesPaint;
    private Paint slicePaint;
    private PointF sliceVector;
    private Bitmap softwareBitmap;
    private Canvas softwareCanvas;
    private Viewport tempMaximumViewport;
    private int touchAdditional;
    private PieChartValueFormatter valueFormatter;

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
    }

    public PieChartRenderer(Context context, Chart chart, PieChartDataProvider pieChartDataProvider) {
        super(context, chart);
        this.rotation = 45;
        this.slicePaint = new Paint();
        this.originCircleOval = new RectF();
        this.drawCircleOval = new RectF();
        this.sliceVector = new PointF();
        this.circleFillRatio = 1.0f;
        this.centerCirclePaint = new Paint();
        this.centerCircleText1Paint = new Paint();
        this.centerCircleText1FontMetrics = new Paint.FontMetricsInt();
        this.centerCircleText2Paint = new Paint();
        this.centerCircleText2FontMetrics = new Paint.FontMetricsInt();
        this.separationLinesPaint = new Paint();
        this.tempMaximumViewport = new Viewport();
        this.softwareCanvas = new Canvas();
        this.dataProvider = pieChartDataProvider;
        this.touchAdditional = ChartUtils.dp2px(this.density, 8);
        this.slicePaint.setAntiAlias(true);
        this.slicePaint.setStyle(Paint.Style.FILL);
        this.centerCirclePaint.setAntiAlias(true);
        this.centerCirclePaint.setStyle(Paint.Style.FILL);
        this.centerCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.centerCircleText1Paint.setAntiAlias(true);
        this.centerCircleText1Paint.setTextAlign(Paint.Align.CENTER);
        this.centerCircleText2Paint.setAntiAlias(true);
        this.centerCircleText2Paint.setTextAlign(Paint.Align.CENTER);
        this.separationLinesPaint.setAntiAlias(true);
        this.separationLinesPaint.setStyle(Paint.Style.STROKE);
        this.separationLinesPaint.setStrokeCap(Paint.Cap.ROUND);
        this.separationLinesPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.separationLinesPaint.setColor(0);
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartSizeChanged() {
        calculateCircleOval();
        if (this.computator.getChartWidth() <= 0 || this.computator.getChartHeight() <= 0) {
            return;
        }
        this.softwareBitmap = Bitmap.createBitmap(this.computator.getChartWidth(), this.computator.getChartHeight(), Bitmap.Config.ARGB_8888);
        this.softwareCanvas.setBitmap(this.softwareBitmap);
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartDataChanged() {
        super.onChartDataChanged();
        PieChartData pieChartData = this.dataProvider.getPieChartData();
        this.hasLabelsOutside = pieChartData.hasLabelsOutside();
        this.hasLabels = pieChartData.hasLabels();
        this.hasLabelsOnlyForSelected = pieChartData.hasLabelsOnlyForSelected();
        this.valueFormatter = pieChartData.getFormatter();
        this.hasCenterCircle = pieChartData.hasCenterCircle();
        this.centerCircleScale = pieChartData.getCenterCircleScale();
        this.centerCirclePaint.setColor(pieChartData.getCenterCircleColor());
        if (pieChartData.getCenterText1Typeface() != null) {
            this.centerCircleText1Paint.setTypeface(pieChartData.getCenterText1Typeface());
        }
        this.centerCircleText1Paint.setTextSize(ChartUtils.sp2px(this.scaledDensity, pieChartData.getCenterText1FontSize()));
        this.centerCircleText1Paint.setColor(pieChartData.getCenterText1Color());
        this.centerCircleText1Paint.getFontMetricsInt(this.centerCircleText1FontMetrics);
        if (pieChartData.getCenterText2Typeface() != null) {
            this.centerCircleText2Paint.setTypeface(pieChartData.getCenterText2Typeface());
        }
        this.centerCircleText2Paint.setTextSize(ChartUtils.sp2px(this.scaledDensity, pieChartData.getCenterText2FontSize()));
        this.centerCircleText2Paint.setColor(pieChartData.getCenterText2Color());
        this.centerCircleText2Paint.getFontMetricsInt(this.centerCircleText2FontMetrics);
        onChartViewportChanged();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartViewportChanged() {
        if (this.isViewportCalculationEnabled) {
            calculateMaxViewport();
            this.computator.setMaxViewport(this.tempMaximumViewport);
            this.computator.setCurrentViewport(this.computator.getMaximumViewport());
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void draw(Canvas canvas) {
        Canvas canvas2;
        if (this.softwareBitmap != null) {
            canvas2 = this.softwareCanvas;
            canvas2.drawColor(0, PorterDuff.Mode.CLEAR);
        } else {
            canvas2 = canvas;
        }
        drawSlices(canvas2);
        drawSeparationLines(canvas2);
        if (this.hasCenterCircle) {
            drawCenterCircle(canvas2);
        }
        drawLabels(canvas2);
        if (this.softwareBitmap != null) {
            canvas.drawBitmap(this.softwareBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean checkTouch(float f, float f2) {
        this.selectedValue.clear();
        PieChartData pieChartData = this.dataProvider.getPieChartData();
        float fCenterX = this.originCircleOval.centerX();
        float fCenterY = this.originCircleOval.centerY();
        float fWidth = this.originCircleOval.width() / 2.0f;
        this.sliceVector.set(f - fCenterX, f2 - fCenterY);
        int i = 0;
        if (this.sliceVector.length() > this.touchAdditional + fWidth) {
            return false;
        }
        if (pieChartData.hasCenterCircle() && this.sliceVector.length() < fWidth * pieChartData.getCenterCircleScale()) {
            return false;
        }
        float fPointToAngle = ((pointToAngle(f, f2, fCenterX, fCenterY) - this.rotation) + 360.0f) % 360.0f;
        float f3 = 360.0f / this.maxSum;
        float f4 = 0.0f;
        Iterator<SliceValue> it = pieChartData.getValues().iterator();
        while (it.hasNext()) {
            float fAbs = Math.abs(it.next().getValue()) * f3;
            if (fPointToAngle >= f4) {
                this.selectedValue.set(i, i, SelectedValue.SelectedValueType.NONE);
            }
            f4 += fAbs;
            i++;
        }
        return isTouched();
    }

    private void drawCenterCircle(Canvas canvas) {
        PieChartData pieChartData = this.dataProvider.getPieChartData();
        float fWidth = (this.originCircleOval.width() / 2.0f) * pieChartData.getCenterCircleScale();
        float fCenterX = this.originCircleOval.centerX();
        float fCenterY = this.originCircleOval.centerY();
        canvas.drawCircle(fCenterX, fCenterY, fWidth, this.centerCirclePaint);
        if (TextUtils.isEmpty(pieChartData.getCenterText1())) {
            return;
        }
        int iAbs = Math.abs(this.centerCircleText1FontMetrics.ascent);
        if (!TextUtils.isEmpty(pieChartData.getCenterText2())) {
            int iAbs2 = Math.abs(this.centerCircleText2FontMetrics.ascent);
            canvas.drawText(pieChartData.getCenterText1(), fCenterX, fCenterY - (iAbs * 0.2f), this.centerCircleText1Paint);
            canvas.drawText(pieChartData.getCenterText2(), fCenterX, fCenterY + iAbs2, this.centerCircleText2Paint);
            return;
        }
        canvas.drawText(pieChartData.getCenterText1(), fCenterX, fCenterY + (iAbs / 4), this.centerCircleText1Paint);
    }

    private void drawSlices(Canvas canvas) {
        PieChartData pieChartData = this.dataProvider.getPieChartData();
        float f = 360.0f / this.maxSum;
        float f2 = this.rotation;
        int i = 0;
        for (SliceValue sliceValue : pieChartData.getValues()) {
            float fAbs = Math.abs(sliceValue.getValue()) * f;
            if (isTouched() && this.selectedValue.getFirstIndex() == i) {
                drawSlice(canvas, sliceValue, f2, fAbs, 1);
            } else {
                drawSlice(canvas, sliceValue, f2, fAbs, 0);
            }
            f2 += fAbs;
            i++;
        }
    }

    private void drawSeparationLines(Canvas canvas) {
        int iDp2px;
        PieChartData pieChartData = this.dataProvider.getPieChartData();
        if (pieChartData.getValues().size() >= 2 && (iDp2px = ChartUtils.dp2px(this.density, pieChartData.getSlicesSpacing())) >= 1) {
            float f = 360.0f / this.maxSum;
            float f2 = this.rotation;
            float fWidth = this.originCircleOval.width() / 2.0f;
            this.separationLinesPaint.setStrokeWidth(iDp2px);
            Iterator<SliceValue> it = pieChartData.getValues().iterator();
            while (it.hasNext()) {
                float fAbs = Math.abs(it.next().getValue()) * f;
                double d = f2;
                this.sliceVector.set((float) Math.cos(Math.toRadians(d)), (float) Math.sin(Math.toRadians(d)));
                normalizeVector(this.sliceVector);
                canvas.drawLine(this.originCircleOval.centerX(), this.originCircleOval.centerY(), (this.sliceVector.x * (this.touchAdditional + fWidth)) + this.originCircleOval.centerX(), (this.sliceVector.y * (this.touchAdditional + fWidth)) + this.originCircleOval.centerY(), this.separationLinesPaint);
                f2 += fAbs;
            }
        }
    }

    public void drawLabels(Canvas canvas) {
        PieChartData pieChartData = this.dataProvider.getPieChartData();
        float f = 360.0f / this.maxSum;
        float f2 = this.rotation;
        int i = 0;
        for (SliceValue sliceValue : pieChartData.getValues()) {
            float fAbs = Math.abs(sliceValue.getValue()) * f;
            if (isTouched()) {
                if (this.hasLabels) {
                    drawLabel(canvas, sliceValue, f2, fAbs);
                } else if (this.hasLabelsOnlyForSelected && this.selectedValue.getFirstIndex() == i) {
                    drawLabel(canvas, sliceValue, f2, fAbs);
                }
            } else if (this.hasLabels) {
                drawLabel(canvas, sliceValue, f2, fAbs);
            }
            f2 += fAbs;
            i++;
        }
    }

    private void drawSlice(Canvas canvas, SliceValue sliceValue, float f, float f2, int i) {
        double d = (f2 / 2.0f) + f;
        this.sliceVector.set((float) Math.cos(Math.toRadians(d)), (float) Math.sin(Math.toRadians(d)));
        normalizeVector(this.sliceVector);
        this.drawCircleOval.set(this.originCircleOval);
        if (1 == i) {
            this.drawCircleOval.inset(-this.touchAdditional, -this.touchAdditional);
            this.slicePaint.setColor(sliceValue.getDarkenColor());
            canvas.drawArc(this.drawCircleOval, f, f2, true, this.slicePaint);
        } else {
            this.slicePaint.setColor(sliceValue.getColor());
            canvas.drawArc(this.drawCircleOval, f, f2, true, this.slicePaint);
        }
    }

    private void drawLabel(Canvas canvas, SliceValue sliceValue, float f, float f2) {
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        double d = f + (f2 / 2.0f);
        this.sliceVector.set((float) Math.cos(Math.toRadians(d)), (float) Math.sin(Math.toRadians(d)));
        normalizeVector(this.sliceVector);
        int chartValue = this.valueFormatter.formatChartValue(this.labelBuffer, sliceValue);
        if (chartValue == 0) {
            return;
        }
        float fMeasureText = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - chartValue, chartValue);
        int iAbs = Math.abs(this.fontMetrics.ascent);
        float fCenterX = this.originCircleOval.centerX();
        float fCenterY = this.originCircleOval.centerY();
        float fWidth = this.originCircleOval.width() / 2.0f;
        if (this.hasLabelsOutside) {
            f3 = fWidth * 1.0f;
        } else {
            f3 = this.hasCenterCircle ? fWidth - ((fWidth - (this.centerCircleScale * fWidth)) / 2.0f) : fWidth * DEFAULT_LABEL_INSIDE_RADIUS_FACTOR;
        }
        float f8 = (this.sliceVector.x * f3) + fCenterX;
        float f9 = (f3 * this.sliceVector.y) + fCenterY;
        if (this.hasLabelsOutside) {
            if (f8 > fCenterX) {
                f4 = this.labelMargin + f8;
                f5 = f8 + fMeasureText + (this.labelMargin * 3);
            } else {
                f4 = (f8 - fMeasureText) - (this.labelMargin * 3);
                f5 = f8 - this.labelMargin;
            }
            if (f9 > fCenterY) {
                f7 = this.labelMargin + f9;
                f6 = f9 + iAbs + (this.labelMargin * 3);
            } else {
                f7 = (f9 - iAbs) - (this.labelMargin * 3);
                f6 = f9 - this.labelMargin;
            }
        } else {
            float f10 = fMeasureText / 2.0f;
            f4 = (f8 - f10) - this.labelMargin;
            f5 = f8 + f10 + this.labelMargin;
            float f11 = iAbs / 2;
            float f12 = (f9 - f11) - this.labelMargin;
            f6 = f9 + f11 + this.labelMargin;
            f7 = f12;
        }
        this.labelBackgroundRect.set(f4, f7, f5, f6);
        drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - chartValue, chartValue, sliceValue.getDarkenColor());
    }

    private void normalizeVector(PointF pointF) {
        float length = pointF.length();
        pointF.set(pointF.x / length, pointF.y / length);
    }

    private float pointToAngle(float f, float f2, float f3, float f4) {
        double d = f - f3;
        Double.isNaN(d);
        return ((((float) Math.toDegrees(Math.atan2(-d, f2 - f4))) + 360.0f) % 360.0f) + 90.0f;
    }

    private void calculateCircleOval() {
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        float fMin = Math.min(contentRectMinusAllMargins.width() / 2.0f, contentRectMinusAllMargins.height() / 2.0f);
        float fCenterX = contentRectMinusAllMargins.centerX();
        float fCenterY = contentRectMinusAllMargins.centerY();
        this.originCircleOval.set((fCenterX - fMin) + this.touchAdditional, (fCenterY - fMin) + this.touchAdditional, (fCenterX + fMin) - this.touchAdditional, (fCenterY + fMin) - this.touchAdditional);
        float fWidth = this.originCircleOval.width() * 0.5f * (1.0f - this.circleFillRatio);
        this.originCircleOval.inset(fWidth, fWidth);
    }

    private void calculateMaxViewport() {
        this.tempMaximumViewport.set(0.0f, MAX_WIDTH_HEIGHT, MAX_WIDTH_HEIGHT, 0.0f);
        this.maxSum = 0.0f;
        Iterator<SliceValue> it = this.dataProvider.getPieChartData().getValues().iterator();
        while (it.hasNext()) {
            this.maxSum += Math.abs(it.next().getValue());
        }
    }

    public RectF getCircleOval() {
        return this.originCircleOval;
    }

    public void setCircleOval(RectF rectF) {
        this.originCircleOval = rectF;
    }

    public int getChartRotation() {
        return this.rotation;
    }

    public void setChartRotation(int i) {
        this.rotation = ((i % 360) + 360) % 360;
    }

    public SliceValue getValueForAngle(int i, SelectedValue selectedValue) {
        PieChartData pieChartData = this.dataProvider.getPieChartData();
        float f = ((i - this.rotation) + 360.0f) % 360.0f;
        float f2 = 360.0f / this.maxSum;
        float f3 = 0.0f;
        int i2 = 0;
        for (SliceValue sliceValue : pieChartData.getValues()) {
            float fAbs = Math.abs(sliceValue.getValue()) * f2;
            if (f >= f3) {
                if (selectedValue != null) {
                    selectedValue.set(i2, i2, SelectedValue.SelectedValueType.NONE);
                }
                return sliceValue;
            }
            f3 += fAbs;
            i2++;
        }
        return null;
    }

    public float getCircleFillRatio() {
        return this.circleFillRatio;
    }

    public void setCircleFillRatio(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        } else if (f > 1.0f) {
            f = 1.0f;
        }
        this.circleFillRatio = f;
        calculateCircleOval();
    }
}
