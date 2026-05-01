package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import java.util.Iterator;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class LineChartRenderer extends AbstractChartRenderer {
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;
    private static final int DEFAULT_TOUCH_TOLERANCE_MARGIN_DP = 4;
    private static final float LINE_SMOOTHNESS = 0.16f;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private float baseValue;
    private int checkPrecision;
    private LineChartDataProvider dataProvider;
    private Paint linePaint;
    private Path path;
    private Paint pointPaint;
    private Bitmap softwareBitmap;
    private Canvas softwareCanvas;
    private Viewport tempMaximumViewport;
    private int touchToleranceMargin;

    public LineChartRenderer(Context context, Chart chart, LineChartDataProvider lineChartDataProvider) {
        super(context, chart);
        this.path = new Path();
        this.linePaint = new Paint();
        this.pointPaint = new Paint();
        this.softwareCanvas = new Canvas();
        this.tempMaximumViewport = new Viewport();
        this.dataProvider = lineChartDataProvider;
        this.touchToleranceMargin = ChartUtils.dp2px(this.density, 4);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.linePaint.setStrokeWidth(ChartUtils.dp2px(this.density, 3));
        this.pointPaint.setAntiAlias(true);
        this.pointPaint.setStyle(Paint.Style.FILL);
        this.checkPrecision = ChartUtils.dp2px(this.density, 2);
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartSizeChanged() {
        int iCalculateContentRectInternalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(iCalculateContentRectInternalMargin, iCalculateContentRectInternalMargin, iCalculateContentRectInternalMargin, iCalculateContentRectInternalMargin);
        if (this.computator.getChartWidth() <= 0 || this.computator.getChartHeight() <= 0) {
            return;
        }
        this.softwareBitmap = Bitmap.createBitmap(this.computator.getChartWidth(), this.computator.getChartHeight(), Bitmap.Config.ARGB_8888);
        this.softwareCanvas.setBitmap(this.softwareBitmap);
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartDataChanged() {
        super.onChartDataChanged();
        int iCalculateContentRectInternalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(iCalculateContentRectInternalMargin, iCalculateContentRectInternalMargin, iCalculateContentRectInternalMargin, iCalculateContentRectInternalMargin);
        this.baseValue = this.dataProvider.getLineChartData().getBaseValue();
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
        LineChartData lineChartData = this.dataProvider.getLineChartData();
        if (this.softwareBitmap != null) {
            canvas2 = this.softwareCanvas;
            canvas2.drawColor(0, PorterDuff.Mode.CLEAR);
        } else {
            canvas2 = canvas;
        }
        for (Line line : lineChartData.getLines()) {
            if (line.hasLines()) {
                if (line.isCubic()) {
                    drawSmoothPath(canvas2, line);
                } else if (line.isSquare()) {
                    drawSquarePath(canvas2, line);
                } else {
                    drawPath(canvas2, line);
                }
            }
        }
        if (this.softwareBitmap != null) {
            canvas.drawBitmap(this.softwareBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
        int i = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                drawPoints(canvas, line, i, 0);
            }
            i++;
        }
        if (isTouched()) {
            highlightPoints(canvas);
        }
    }

    private boolean checkIfShouldDrawPoints(Line line) {
        return line.hasPoints() || line.getValues().size() == 1;
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean checkTouch(float f, float f2) {
        this.selectedValue.clear();
        int i = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                int iDp2px = ChartUtils.dp2px(this.density, line.getPointRadius());
                int i2 = 0;
                for (PointValue pointValue : line.getValues()) {
                    if (isInArea(this.computator.computeRawX(pointValue.getX()), this.computator.computeRawY(pointValue.getY()), f, f2, this.touchToleranceMargin + iDp2px)) {
                        this.selectedValue.set(i, i2, SelectedValue.SelectedValueType.LINE);
                    }
                    i2++;
                }
            }
            i++;
        }
        return isTouched();
    }

    private void calculateMaxViewport() {
        this.tempMaximumViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        Iterator<Line> it = this.dataProvider.getLineChartData().getLines().iterator();
        while (it.hasNext()) {
            for (PointValue pointValue : it.next().getValues()) {
                if (pointValue.getX() < this.tempMaximumViewport.left) {
                    this.tempMaximumViewport.left = pointValue.getX();
                }
                if (pointValue.getX() > this.tempMaximumViewport.right) {
                    this.tempMaximumViewport.right = pointValue.getX();
                }
                if (pointValue.getY() < this.tempMaximumViewport.bottom) {
                    this.tempMaximumViewport.bottom = pointValue.getY();
                }
                if (pointValue.getY() > this.tempMaximumViewport.top) {
                    this.tempMaximumViewport.top = pointValue.getY();
                }
            }
        }
    }

    private int calculateContentRectInternalMargin() {
        int pointRadius;
        int i = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line) && (pointRadius = line.getPointRadius() + 4) > i) {
                i = pointRadius;
            }
        }
        return ChartUtils.dp2px(this.density, i);
    }

    private void drawPath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int i = 0;
        for (PointValue pointValue : line.getValues()) {
            float fComputeRawX = this.computator.computeRawX(pointValue.getX());
            float fComputeRawY = this.computator.computeRawY(pointValue.getY());
            if (i == 0) {
                this.path.moveTo(fComputeRawX, fComputeRawY);
            } else {
                this.path.lineTo(fComputeRawX, fComputeRawY);
            }
            i++;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSquarePath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int i = 0;
        float f = 0.0f;
        for (PointValue pointValue : line.getValues()) {
            float fComputeRawX = this.computator.computeRawX(pointValue.getX());
            float fComputeRawY = this.computator.computeRawY(pointValue.getY());
            if (i == 0) {
                this.path.moveTo(fComputeRawX, fComputeRawY);
            } else {
                this.path.lineTo(fComputeRawX, f);
                this.path.lineTo(fComputeRawX, fComputeRawY);
            }
            i++;
            f = fComputeRawY;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSmoothPath(Canvas canvas, Line line) {
        float f;
        float fComputeRawY;
        prepareLinePaint(line);
        int size = line.getValues().size();
        float f2 = Float.NaN;
        int i = 0;
        float f3 = Float.NaN;
        float fComputeRawY2 = Float.NaN;
        float f4 = Float.NaN;
        float fComputeRawY3 = Float.NaN;
        float fComputeRawY4 = Float.NaN;
        while (i < size) {
            if (Float.isNaN(f2)) {
                PointValue pointValue = line.getValues().get(i);
                float fComputeRawX = this.computator.computeRawX(pointValue.getX());
                fComputeRawY2 = this.computator.computeRawY(pointValue.getY());
                f2 = fComputeRawX;
            }
            if (Float.isNaN(f3)) {
                if (i > 0) {
                    PointValue pointValue2 = line.getValues().get(i - 1);
                    float fComputeRawX2 = this.computator.computeRawX(pointValue2.getX());
                    fComputeRawY3 = this.computator.computeRawY(pointValue2.getY());
                    f3 = fComputeRawX2;
                } else {
                    f3 = f2;
                    fComputeRawY3 = fComputeRawY2;
                }
            }
            if (Float.isNaN(f4)) {
                if (i > 1) {
                    PointValue pointValue3 = line.getValues().get(i - 2);
                    float fComputeRawX3 = this.computator.computeRawX(pointValue3.getX());
                    fComputeRawY4 = this.computator.computeRawY(pointValue3.getY());
                    f4 = fComputeRawX3;
                } else {
                    f4 = f3;
                    fComputeRawY4 = fComputeRawY3;
                }
            }
            if (i < size - 1) {
                PointValue pointValue4 = line.getValues().get(i + 1);
                float fComputeRawX4 = this.computator.computeRawX(pointValue4.getX());
                fComputeRawY = this.computator.computeRawY(pointValue4.getY());
                f = fComputeRawX4;
            } else {
                f = f2;
                fComputeRawY = fComputeRawY2;
            }
            if (i == 0) {
                this.path.moveTo(f2, fComputeRawY2);
            } else {
                this.path.cubicTo(((f2 - f4) * LINE_SMOOTHNESS) + f3, ((fComputeRawY2 - fComputeRawY4) * LINE_SMOOTHNESS) + fComputeRawY3, f2 - ((f - f3) * LINE_SMOOTHNESS), fComputeRawY2 - ((fComputeRawY - fComputeRawY3) * LINE_SMOOTHNESS), f2, fComputeRawY2);
            }
            i++;
            f4 = f3;
            fComputeRawY4 = fComputeRawY3;
            f3 = f2;
            fComputeRawY3 = fComputeRawY2;
            f2 = f;
            fComputeRawY2 = fComputeRawY;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void prepareLinePaint(Line line) {
        this.linePaint.setStrokeWidth(ChartUtils.dp2px(this.density, line.getStrokeWidth()));
        this.linePaint.setColor(line.getColor());
        this.linePaint.setPathEffect(line.getPathEffect());
    }

    private void drawPoints(Canvas canvas, Line line, int i, int i2) {
        this.pointPaint.setColor(line.getPointColor());
        int i3 = 0;
        for (PointValue pointValue : line.getValues()) {
            int iDp2px = ChartUtils.dp2px(this.density, line.getPointRadius());
            float fComputeRawX = this.computator.computeRawX(pointValue.getX());
            float fComputeRawY = this.computator.computeRawY(pointValue.getY());
            if (this.computator.isWithinContentRect(fComputeRawX, fComputeRawY, this.checkPrecision)) {
                if (i2 == 0) {
                    drawPoint(canvas, line, pointValue, fComputeRawX, fComputeRawY, iDp2px);
                    if (line.hasLabels()) {
                        drawLabel(canvas, line, pointValue, fComputeRawX, fComputeRawY, iDp2px + this.labelOffset);
                    }
                } else if (1 == i2) {
                    highlightPoint(canvas, line, pointValue, fComputeRawX, fComputeRawY, i, i3);
                } else {
                    throw new IllegalStateException("Cannot process points in mode: " + i2);
                }
            }
            i3++;
        }
    }

    private void drawPoint(Canvas canvas, Line line, PointValue pointValue, float f, float f2, float f3) {
        if (ValueShape.SQUARE.equals(line.getShape())) {
            canvas.drawRect(f - f3, f2 - f3, f + f3, f2 + f3, this.pointPaint);
            return;
        }
        if (ValueShape.CIRCLE.equals(line.getShape())) {
            canvas.drawCircle(f, f2, f3, this.pointPaint);
            return;
        }
        if (ValueShape.DIAMOND.equals(line.getShape())) {
            canvas.save();
            canvas.rotate(45.0f, f, f2);
            canvas.drawRect(f - f3, f2 - f3, f + f3, f2 + f3, this.pointPaint);
            canvas.restore();
            return;
        }
        throw new IllegalArgumentException("Invalid point shape: " + line.getShape());
    }

    private void highlightPoints(Canvas canvas) {
        int firstIndex = this.selectedValue.getFirstIndex();
        drawPoints(canvas, this.dataProvider.getLineChartData().getLines().get(firstIndex), firstIndex, 1);
    }

    private void highlightPoint(Canvas canvas, Line line, PointValue pointValue, float f, float f2, int i, int i2) {
        if (this.selectedValue.getFirstIndex() == i && this.selectedValue.getSecondIndex() == i2) {
            int iDp2px = ChartUtils.dp2px(this.density, line.getPointRadius());
            this.pointPaint.setColor(line.getDarkenColor());
            drawPoint(canvas, line, pointValue, f, f2, this.touchToleranceMargin + iDp2px);
            if (line.hasLabels() || line.hasLabelsOnlyForSelected()) {
                drawLabel(canvas, line, pointValue, f, f2, iDp2px + this.labelOffset);
            }
        }
    }

    private void drawLabel(Canvas canvas, Line line, PointValue pointValue, float f, float f2, float f3) {
        float f4;
        float f5;
        float f6;
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        int chartValue = line.getFormatter().formatChartValue(this.labelBuffer, pointValue);
        if (chartValue == 0) {
            return;
        }
        float fMeasureText = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - chartValue, chartValue);
        int iAbs = Math.abs(this.fontMetrics.ascent);
        float f7 = fMeasureText / 2.0f;
        float f8 = (f - f7) - this.labelMargin;
        float f9 = f7 + f + this.labelMargin;
        if (pointValue.getY() >= this.baseValue) {
            f5 = f2 - f3;
            f4 = (f5 - iAbs) - (this.labelMargin * 2);
        } else {
            f4 = f2 + f3;
            f5 = iAbs + f4 + (this.labelMargin * 2);
        }
        if (f4 < contentRectMinusAllMargins.top) {
            f4 = f2 + f3;
            f5 = iAbs + f4 + (this.labelMargin * 2);
        }
        if (f5 > contentRectMinusAllMargins.bottom) {
            f5 = f2 - f3;
            f4 = (f5 - iAbs) - (this.labelMargin * 2);
        }
        if (f8 < contentRectMinusAllMargins.left) {
            f6 = f + fMeasureText + (this.labelMargin * 2);
            f8 = f;
        } else {
            f6 = f9;
        }
        if (f6 > contentRectMinusAllMargins.right) {
            f8 = (f - fMeasureText) - (this.labelMargin * 2);
        } else {
            f = f6;
        }
        this.labelBackgroundRect.set(f8, f4, f, f5);
        drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - chartValue, chartValue, line.getDarkenColor());
    }

    private void drawArea(Canvas canvas, Line line) {
        int size = line.getValues().size();
        if (size < 2) {
            return;
        }
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        float fMin = Math.min(contentRectMinusAllMargins.bottom, Math.max(this.computator.computeRawY(this.baseValue), contentRectMinusAllMargins.top));
        float fMax = Math.max(this.computator.computeRawX(line.getValues().get(0).getX()), contentRectMinusAllMargins.left);
        this.path.lineTo(Math.min(this.computator.computeRawX(line.getValues().get(size - 1).getX()), contentRectMinusAllMargins.right), fMin);
        this.path.lineTo(fMax, fMin);
        this.path.close();
        this.linePaint.setStyle(Paint.Style.FILL);
        this.linePaint.setAlpha(line.getAreaTransparency());
        canvas.drawPath(this.path, this.linePaint);
        this.linePaint.setStyle(Paint.Style.STROKE);
    }

    private boolean isInArea(float f, float f2, float f3, float f4, float f5) {
        return Math.pow((double) (f3 - f), 2.0d) + Math.pow((double) (f4 - f2), 2.0d) <= Math.pow((double) f5, 2.0d) * 2.0d;
    }
}
