package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import java.lang.reflect.Array;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.AxisAutoValues;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.util.FloatUtils;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class AxesRenderer {
    private static final int BOTTOM = 3;
    private static final int DEFAULT_AXIS_MARGIN_DP = 2;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 0;
    private static final char[] labelWidthChars = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
    private int axisMargin;
    private Chart chart;
    private ChartComputator computator;
    private float density;
    private float scaledDensity;
    private Paint[] labelPaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private Paint[] namePaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private Paint[] linePaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private float[] nameBaselineTab = new float[4];
    private float[] labelBaselineTab = new float[4];
    private float[] separationLineTab = new float[4];
    private int[] labelWidthTab = new int[4];
    private int[] labelTextAscentTab = new int[4];
    private int[] labelTextDescentTab = new int[4];
    private int[] labelDimensionForMarginsTab = new int[4];
    private int[] labelDimensionForStepsTab = new int[4];
    private int[] tiltedLabelXTranslation = new int[4];
    private int[] tiltedLabelYTranslation = new int[4];
    private Paint.FontMetricsInt[] fontMetricsTab = {new Paint.FontMetricsInt(), new Paint.FontMetricsInt(), new Paint.FontMetricsInt(), new Paint.FontMetricsInt()};
    private char[] labelBuffer = new char[64];
    private int[] valuesToDrawNumTab = new int[4];
    private float[][] rawValuesTab = (float[][]) Array.newInstance((Class<?>) float.class, 4, 0);
    private float[][] autoValuesToDrawTab = (float[][]) Array.newInstance((Class<?>) float.class, 4, 0);
    private AxisValue[][] valuesToDrawTab = (AxisValue[][]) Array.newInstance((Class<?>) AxisValue.class, 4, 0);
    private float[][] linesDrawBufferTab = (float[][]) Array.newInstance((Class<?>) float.class, 4, 0);
    private AxisAutoValues[] autoValuesBufferTab = {new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues()};

    public AxesRenderer(Context context, Chart chart) {
        this.chart = chart;
        this.computator = chart.getChartComputator();
        this.density = context.getResources().getDisplayMetrics().density;
        this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.axisMargin = ChartUtils.dp2px(this.density, 2);
        for (int i = 0; i < 4; i++) {
            this.labelPaintTab[i].setStyle(Paint.Style.FILL);
            this.labelPaintTab[i].setAntiAlias(true);
            this.namePaintTab[i].setStyle(Paint.Style.FILL);
            this.namePaintTab[i].setAntiAlias(true);
            this.linePaintTab[i].setStyle(Paint.Style.STROKE);
            this.linePaintTab[i].setAntiAlias(true);
        }
    }

    public void onChartSizeChanged() {
        onChartDataOrSizeChanged();
    }

    public void onChartDataChanged() {
        onChartDataOrSizeChanged();
    }

    private void onChartDataOrSizeChanged() {
        initAxis(this.chart.getChartData().getAxisXTop(), 0);
        initAxis(this.chart.getChartData().getAxisXBottom(), 3);
        initAxis(this.chart.getChartData().getAxisYLeft(), 1);
        initAxis(this.chart.getChartData().getAxisYRight(), 2);
    }

    public void resetRenderer() {
        this.computator = this.chart.getChartComputator();
    }

    private void initAxis(Axis axis, int i) {
        if (axis == null) {
            return;
        }
        initAxisAttributes(axis, i);
        initAxisMargin(axis, i);
        initAxisMeasurements(axis, i);
    }

    private void initAxisAttributes(Axis axis, int i) {
        initAxisPaints(axis, i);
        initAxisTextAlignment(axis, i);
        if (axis.hasTiltedLabels()) {
            initAxisDimensionForTiltedLabels(i);
            intiTiltedLabelsTranslation(axis, i);
        } else {
            initAxisDimension(i);
        }
    }

    private void initAxisPaints(Axis axis, int i) {
        Typeface typeface = axis.getTypeface();
        if (typeface != null) {
            this.labelPaintTab[i].setTypeface(typeface);
            this.namePaintTab[i].setTypeface(typeface);
        }
        this.labelPaintTab[i].setColor(axis.getTextColor());
        this.labelPaintTab[i].setTextSize(ChartUtils.sp2px(this.scaledDensity, axis.getTextSize()));
        this.labelPaintTab[i].getFontMetricsInt(this.fontMetricsTab[i]);
        this.namePaintTab[i].setColor(axis.getTextColor());
        this.namePaintTab[i].setTextSize(ChartUtils.sp2px(this.scaledDensity, axis.getTextSize()));
        this.linePaintTab[i].setColor(axis.getLineColor());
        this.labelTextAscentTab[i] = Math.abs(this.fontMetricsTab[i].ascent);
        this.labelTextDescentTab[i] = Math.abs(this.fontMetricsTab[i].descent);
        this.labelWidthTab[i] = (int) this.labelPaintTab[i].measureText(labelWidthChars, 0, axis.getMaxLabelChars());
    }

    private void initAxisTextAlignment(Axis axis, int i) {
        this.namePaintTab[i].setTextAlign(Paint.Align.CENTER);
        if (i == 0 || 3 == i) {
            this.labelPaintTab[i].setTextAlign(Paint.Align.CENTER);
            return;
        }
        if (1 == i) {
            if (axis.isInside()) {
                this.labelPaintTab[i].setTextAlign(Paint.Align.LEFT);
                return;
            } else {
                this.labelPaintTab[i].setTextAlign(Paint.Align.RIGHT);
                return;
            }
        }
        if (2 == i) {
            if (axis.isInside()) {
                this.labelPaintTab[i].setTextAlign(Paint.Align.RIGHT);
            } else {
                this.labelPaintTab[i].setTextAlign(Paint.Align.LEFT);
            }
        }
    }

    private void initAxisDimensionForTiltedLabels(int i) {
        this.labelDimensionForMarginsTab[i] = ((int) Math.sqrt(Math.pow(this.labelTextAscentTab[i], 2.0d) / 2.0d)) + ((int) Math.sqrt(Math.pow(this.labelWidthTab[i], 2.0d) / 2.0d));
        this.labelDimensionForStepsTab[i] = Math.round(this.labelDimensionForMarginsTab[i] * 0.75f);
    }

    private void initAxisDimension(int i) {
        if (1 == i || 2 == i) {
            this.labelDimensionForMarginsTab[i] = this.labelWidthTab[i];
            this.labelDimensionForStepsTab[i] = this.labelTextAscentTab[i];
        } else if (i == 0 || 3 == i) {
            this.labelDimensionForMarginsTab[i] = this.labelTextAscentTab[i] + this.labelTextDescentTab[i];
            this.labelDimensionForStepsTab[i] = this.labelWidthTab[i];
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void intiTiltedLabelsTranslation(lecho.lib.hellocharts.model.Axis r7, int r8) {
        /*
            r6 = this;
            int[] r0 = r6.labelWidthTab
            r0 = r0[r8]
            double r0 = (double) r0
            r2 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r0 = java.lang.Math.pow(r0, r2)
            double r0 = r0 / r2
            double r0 = java.lang.Math.sqrt(r0)
            int r0 = (int) r0
            int[] r1 = r6.labelTextAscentTab
            r1 = r1[r8]
            double r4 = (double) r1
            double r4 = java.lang.Math.pow(r4, r2)
            double r4 = r4 / r2
            double r1 = java.lang.Math.sqrt(r4)
            int r1 = (int) r1
            boolean r7 = r7.isInside()
            r2 = 3
            r3 = 1
            r4 = 0
            r5 = 2
            if (r7 == 0) goto L42
            if (r3 != r8) goto L2d
            goto L49
        L2d:
            if (r5 != r8) goto L32
            int r7 = -r0
            int r7 = r7 / r5
            goto L5b
        L32:
            if (r8 != 0) goto L3d
            int r0 = r0 / r5
            int r1 = r1 + r0
            int[] r7 = r6.labelTextAscentTab
            r7 = r7[r8]
            int r7 = r1 - r7
            goto L5b
        L3d:
            if (r2 != r8) goto L4a
            int r7 = -r0
            int r7 = r7 / r5
            goto L5b
        L42:
            if (r3 != r8) goto L47
            int r7 = -r0
            int r7 = r7 / r5
            goto L5b
        L47:
            if (r5 != r8) goto L4c
        L49:
            r4 = r1
        L4a:
            r7 = 0
            goto L5b
        L4c:
            if (r8 != 0) goto L51
            int r7 = -r0
            int r7 = r7 / r5
            goto L5b
        L51:
            if (r2 != r8) goto L4a
            int r0 = r0 / r5
            int r1 = r1 + r0
            int[] r7 = r6.labelTextAscentTab
            r7 = r7[r8]
            int r7 = r1 - r7
        L5b:
            int[] r0 = r6.tiltedLabelXTranslation
            r0[r8] = r4
            int[] r0 = r6.tiltedLabelYTranslation
            r0[r8] = r7
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: lecho.lib.hellocharts.renderer.AxesRenderer.intiTiltedLabelsTranslation(lecho.lib.hellocharts.model.Axis, int):void");
    }

    private void initAxisMargin(Axis axis, int i) {
        int i2 = 0;
        if (!axis.isInside() && (axis.isAutoGenerated() || !axis.getValues().isEmpty())) {
            i2 = 0 + this.axisMargin + this.labelDimensionForMarginsTab[i];
        }
        insetContentRectWithAxesMargins(i2 + getAxisNameMargin(axis, i), i);
    }

    private int getAxisNameMargin(Axis axis, int i) {
        if (TextUtils.isEmpty(axis.getName())) {
            return 0;
        }
        return this.labelTextAscentTab[i] + 0 + this.labelTextDescentTab[i] + this.axisMargin;
    }

    private void insetContentRectWithAxesMargins(int i, int i2) {
        if (1 == i2) {
            this.chart.getChartComputator().insetContentRect(i, 0, 0, 0);
            return;
        }
        if (2 == i2) {
            this.chart.getChartComputator().insetContentRect(0, 0, i, 0);
        } else if (i2 == 0) {
            this.chart.getChartComputator().insetContentRect(0, i, 0, 0);
        } else if (3 == i2) {
            this.chart.getChartComputator().insetContentRect(0, 0, 0, i);
        }
    }

    private void initAxisMeasurements(Axis axis, int i) {
        if (1 == i) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAllMargins().left + this.axisMargin;
                this.nameBaselineTab[i] = (this.computator.getContentRectMinusAxesMargins().left - this.axisMargin) - this.labelTextDescentTab[i];
            } else {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAxesMargins().left - this.axisMargin;
                this.nameBaselineTab[i] = ((this.labelBaselineTab[i] - this.axisMargin) - this.labelTextDescentTab[i]) - this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().left;
            return;
        }
        if (2 == i) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAllMargins().right - this.axisMargin;
                this.nameBaselineTab[i] = this.computator.getContentRectMinusAxesMargins().right + this.axisMargin + this.labelTextAscentTab[i];
            } else {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAxesMargins().right + this.axisMargin;
                this.nameBaselineTab[i] = this.labelBaselineTab[i] + this.axisMargin + this.labelTextAscentTab[i] + this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().right;
            return;
        }
        if (3 == i) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = (this.computator.getContentRectMinusAllMargins().bottom - this.axisMargin) - this.labelTextDescentTab[i];
                this.nameBaselineTab[i] = this.computator.getContentRectMinusAxesMargins().bottom + this.axisMargin + this.labelTextAscentTab[i];
            } else {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAxesMargins().bottom + this.axisMargin + this.labelTextAscentTab[i];
                this.nameBaselineTab[i] = this.labelBaselineTab[i] + this.axisMargin + this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().bottom;
            return;
        }
        if (i == 0) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAllMargins().top + this.axisMargin + this.labelTextAscentTab[i];
                this.nameBaselineTab[i] = (this.computator.getContentRectMinusAxesMargins().top - this.axisMargin) - this.labelTextDescentTab[i];
            } else {
                this.labelBaselineTab[i] = (this.computator.getContentRectMinusAxesMargins().top - this.axisMargin) - this.labelTextDescentTab[i];
                this.nameBaselineTab[i] = (this.labelBaselineTab[i] - this.axisMargin) - this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().top;
            return;
        }
        throw new IllegalArgumentException("Invalid axis position: " + i);
    }

    public void drawInBackground(Canvas canvas) {
        Axis axisYLeft = this.chart.getChartData().getAxisYLeft();
        if (axisYLeft != null) {
            prepareAxisToDraw(axisYLeft, 1);
            drawAxisLines(canvas, axisYLeft, 1);
        }
        Axis axisYRight = this.chart.getChartData().getAxisYRight();
        if (axisYRight != null) {
            prepareAxisToDraw(axisYRight, 2);
            drawAxisLines(canvas, axisYRight, 2);
        }
        Axis axisXBottom = this.chart.getChartData().getAxisXBottom();
        if (axisXBottom != null) {
            prepareAxisToDraw(axisXBottom, 3);
            drawAxisLines(canvas, axisXBottom, 3);
        }
        Axis axisXTop = this.chart.getChartData().getAxisXTop();
        if (axisXTop != null) {
            prepareAxisToDraw(axisXTop, 0);
            drawAxisLines(canvas, axisXTop, 0);
        }
    }

    private void prepareAxisToDraw(Axis axis, int i) {
        if (axis.isAutoGenerated()) {
            prepareAutoGeneratedAxis(axis, i);
        } else {
            prepareCustomAxis(axis, i);
        }
    }

    public void drawInForeground(Canvas canvas) {
        Axis axisYLeft = this.chart.getChartData().getAxisYLeft();
        if (axisYLeft != null) {
            drawAxisLabelsAndName(canvas, axisYLeft, 1);
        }
        Axis axisYRight = this.chart.getChartData().getAxisYRight();
        if (axisYRight != null) {
            drawAxisLabelsAndName(canvas, axisYRight, 2);
        }
        Axis axisXBottom = this.chart.getChartData().getAxisXBottom();
        if (axisXBottom != null) {
            drawAxisLabelsAndName(canvas, axisXBottom, 3);
        }
        Axis axisXTop = this.chart.getChartData().getAxisXTop();
        if (axisXTop != null) {
            drawAxisLabelsAndName(canvas, axisXTop, 0);
        }
    }

    private void prepareCustomAxis(Axis axis, int i) {
        float fWidth;
        float f;
        float f2;
        float fComputeRawX;
        Viewport maximumViewport = this.computator.getMaximumViewport();
        Viewport visibleViewport = this.computator.getVisibleViewport();
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        boolean zIsAxisVertical = isAxisVertical(i);
        if (zIsAxisVertical) {
            fWidth = (maximumViewport.height() <= 0.0f || visibleViewport.height() <= 0.0f) ? 1.0f : (maximumViewport.height() / visibleViewport.height()) * contentRectMinusAllMargins.height();
            f = visibleViewport.bottom;
            f2 = visibleViewport.top;
        } else {
            fWidth = (maximumViewport.width() <= 0.0f || visibleViewport.width() <= 0.0f) ? 1.0f : (maximumViewport.width() / visibleViewport.width()) * contentRectMinusAllMargins.width();
            f = visibleViewport.left;
            f2 = visibleViewport.right;
        }
        float f3 = f2;
        float f4 = f;
        if (fWidth == 0.0f) {
            fWidth = 1.0f;
        }
        double size = axis.getValues().size() * this.labelDimensionForStepsTab[i];
        Double.isNaN(size);
        double d = fWidth;
        Double.isNaN(d);
        int iMax = (int) Math.max(1.0d, Math.ceil((size * 1.5d) / d));
        if (axis.hasLines() && this.linesDrawBufferTab[i].length < axis.getValues().size() * 4) {
            this.linesDrawBufferTab[i] = new float[axis.getValues().size() * 4];
        }
        if (this.rawValuesTab[i].length < axis.getValues().size()) {
            this.rawValuesTab[i] = new float[axis.getValues().size()];
        }
        if (this.valuesToDrawTab[i].length < axis.getValues().size()) {
            this.valuesToDrawTab[i] = new AxisValue[axis.getValues().size()];
        }
        int i2 = 0;
        int i3 = 0;
        for (AxisValue axisValue : axis.getValues()) {
            float value = axisValue.getValue();
            if (value >= f4 && value <= f3) {
                if (i3 % iMax == 0) {
                    if (zIsAxisVertical) {
                        fComputeRawX = this.computator.computeRawY(value);
                    } else {
                        fComputeRawX = this.computator.computeRawX(value);
                    }
                    float f5 = fComputeRawX;
                    if (checkRawValue(contentRectMinusAllMargins, f5, axis.isInside(), i, zIsAxisVertical)) {
                        this.rawValuesTab[i][i2] = f5;
                        this.valuesToDrawTab[i][i2] = axisValue;
                        i2++;
                    }
                }
                i3++;
            }
        }
        this.valuesToDrawNumTab[i] = i2;
    }

    private void prepareAutoGeneratedAxis(Axis axis, int i) {
        float f;
        float f2;
        int iWidth;
        float fComputeRawX;
        Viewport visibleViewport = this.computator.getVisibleViewport();
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        boolean zIsAxisVertical = isAxisVertical(i);
        if (zIsAxisVertical) {
            f = visibleViewport.bottom;
            f2 = visibleViewport.top;
            iWidth = contentRectMinusAllMargins.height();
        } else {
            f = visibleViewport.left;
            f2 = visibleViewport.right;
            iWidth = contentRectMinusAllMargins.width();
        }
        FloatUtils.computeAutoGeneratedAxisValues(f, f2, (iWidth / this.labelDimensionForStepsTab[i]) / 2, this.autoValuesBufferTab[i]);
        if (axis.hasLines() && this.linesDrawBufferTab[i].length < this.autoValuesBufferTab[i].valuesNumber * 4) {
            this.linesDrawBufferTab[i] = new float[this.autoValuesBufferTab[i].valuesNumber * 4];
        }
        if (this.rawValuesTab[i].length < this.autoValuesBufferTab[i].valuesNumber) {
            this.rawValuesTab[i] = new float[this.autoValuesBufferTab[i].valuesNumber];
        }
        if (this.autoValuesToDrawTab[i].length < this.autoValuesBufferTab[i].valuesNumber) {
            this.autoValuesToDrawTab[i] = new float[this.autoValuesBufferTab[i].valuesNumber];
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.autoValuesBufferTab[i].valuesNumber; i3++) {
            if (zIsAxisVertical) {
                fComputeRawX = this.computator.computeRawY(this.autoValuesBufferTab[i].values[i3]);
            } else {
                fComputeRawX = this.computator.computeRawX(this.autoValuesBufferTab[i].values[i3]);
            }
            float f3 = fComputeRawX;
            if (checkRawValue(contentRectMinusAllMargins, f3, axis.isInside(), i, zIsAxisVertical)) {
                this.rawValuesTab[i][i2] = f3;
                this.autoValuesToDrawTab[i][i2] = this.autoValuesBufferTab[i].values[i3];
                i2++;
            }
        }
        this.valuesToDrawNumTab[i] = i2;
    }

    private boolean checkRawValue(Rect rect, float f, boolean z, int i, boolean z2) {
        if (!z) {
            return true;
        }
        if (z2) {
            return f <= ((float) rect.bottom) - ((float) (this.labelTextAscentTab[3] + this.axisMargin)) && f >= ((float) rect.top) + ((float) (this.labelTextAscentTab[0] + this.axisMargin));
        }
        float f2 = this.labelWidthTab[i] / 2;
        return f >= ((float) rect.left) + f2 && f <= ((float) rect.right) - f2;
    }

    private void drawAxisLines(Canvas canvas, Axis axis, int i) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        Rect contentRectMinusAxesMargins = this.computator.getContentRectMinusAxesMargins();
        boolean zIsAxisVertical = isAxisVertical(i);
        float f8 = 0.0f;
        if (1 == i || 2 == i) {
            float f9 = this.separationLineTab[i];
            float f10 = contentRectMinusAxesMargins.bottom;
            float f11 = contentRectMinusAxesMargins.top;
            float f12 = contentRectMinusAxesMargins.left;
            f = contentRectMinusAxesMargins.right;
            f2 = f9;
            f3 = f2;
            f4 = f10;
            f5 = f11;
            f8 = f12;
            f6 = 0.0f;
            f7 = 0.0f;
        } else if (i == 0 || 3 == i) {
            float f13 = contentRectMinusAxesMargins.left;
            float f14 = contentRectMinusAxesMargins.right;
            float f15 = this.separationLineTab[i];
            f7 = contentRectMinusAxesMargins.top;
            f2 = f13;
            f3 = f14;
            f4 = f15;
            f5 = f4;
            f6 = contentRectMinusAxesMargins.bottom;
            f = 0.0f;
        } else {
            f = 0.0f;
            f6 = 0.0f;
            f7 = 0.0f;
            f2 = 0.0f;
            f4 = 0.0f;
            f3 = 0.0f;
            f5 = 0.0f;
        }
        if (axis.hasSeparationLine()) {
            canvas.drawLine(f2, f4, f3, f5, this.labelPaintTab[i]);
        }
        if (axis.hasLines()) {
            float f16 = f6;
            float f17 = f;
            int i2 = 0;
            while (i2 < this.valuesToDrawNumTab[i]) {
                if (zIsAxisVertical) {
                    f16 = this.rawValuesTab[i][i2];
                    f7 = f16;
                } else {
                    f8 = this.rawValuesTab[i][i2];
                    f17 = f8;
                }
                int i3 = i2 * 4;
                this.linesDrawBufferTab[i][i3 + 0] = f8;
                this.linesDrawBufferTab[i][i3 + 1] = f7;
                this.linesDrawBufferTab[i][i3 + 2] = f17;
                this.linesDrawBufferTab[i][i3 + 3] = f16;
                i2++;
            }
            canvas.drawLines(this.linesDrawBufferTab[i], 0, i2 * 4, this.linePaintTab[i]);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void drawAxisLabelsAndName(android.graphics.Canvas r13, lecho.lib.hellocharts.model.Axis r14, int r15) {
        /*
            Method dump skipped, instructions count: 247
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: lecho.lib.hellocharts.renderer.AxesRenderer.drawAxisLabelsAndName(android.graphics.Canvas, lecho.lib.hellocharts.model.Axis, int):void");
    }

    private boolean isAxisVertical(int i) {
        if (1 == i || 2 == i) {
            return true;
        }
        if (i == 0 || 3 == i) {
            return false;
        }
        throw new IllegalArgumentException("Invalid axis position " + i);
    }
}
