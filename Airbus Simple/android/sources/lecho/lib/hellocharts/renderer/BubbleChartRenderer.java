package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import java.util.Iterator;
import lecho.lib.hellocharts.formatter.BubbleChartValueFormatter;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.BubbleChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class BubbleChartRenderer extends AbstractChartRenderer {
    private static final int DEFAULT_TOUCH_ADDITIONAL_DP = 4;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private PointF bubbleCenter;
    private Paint bubblePaint;
    private RectF bubbleRect;
    private float bubbleScaleX;
    private float bubbleScaleY;
    private BubbleChartDataProvider dataProvider;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private boolean isBubbleScaledByX;
    private float maxRadius;
    private float minRawRadius;
    private Viewport tempMaximumViewport;
    private int touchAdditional;
    private BubbleChartValueFormatter valueFormatter;

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
    }

    public BubbleChartRenderer(Context context, Chart chart, BubbleChartDataProvider bubbleChartDataProvider) {
        super(context, chart);
        this.isBubbleScaledByX = true;
        this.bubbleCenter = new PointF();
        this.bubblePaint = new Paint();
        this.bubbleRect = new RectF();
        this.tempMaximumViewport = new Viewport();
        this.dataProvider = bubbleChartDataProvider;
        this.touchAdditional = ChartUtils.dp2px(this.density, 4);
        this.bubblePaint.setAntiAlias(true);
        this.bubblePaint.setStyle(Paint.Style.FILL);
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartSizeChanged() {
        Rect contentRectMinusAllMargins = this.chart.getChartComputator().getContentRectMinusAllMargins();
        if (contentRectMinusAllMargins.width() < contentRectMinusAllMargins.height()) {
            this.isBubbleScaledByX = true;
        } else {
            this.isBubbleScaledByX = false;
        }
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartDataChanged() {
        super.onChartDataChanged();
        BubbleChartData bubbleChartData = this.dataProvider.getBubbleChartData();
        this.hasLabels = bubbleChartData.hasLabels();
        this.hasLabelsOnlyForSelected = bubbleChartData.hasLabelsOnlyForSelected();
        this.valueFormatter = bubbleChartData.getFormatter();
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
        drawBubbles(canvas);
        if (isTouched()) {
            highlightBubbles(canvas);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean checkTouch(float f, float f2) {
        this.selectedValue.clear();
        int i = 0;
        for (BubbleValue bubbleValue : this.dataProvider.getBubbleChartData().getValues()) {
            float fProcessBubble = processBubble(bubbleValue, this.bubbleCenter);
            if (ValueShape.SQUARE.equals(bubbleValue.getShape())) {
                if (this.bubbleRect.contains(f, f2)) {
                    this.selectedValue.set(i, i, SelectedValue.SelectedValueType.NONE);
                }
            } else if (ValueShape.CIRCLE.equals(bubbleValue.getShape())) {
                float f3 = f - this.bubbleCenter.x;
                float f4 = f2 - this.bubbleCenter.y;
                if (((float) Math.sqrt((f3 * f3) + (f4 * f4))) <= fProcessBubble) {
                    this.selectedValue.set(i, i, SelectedValue.SelectedValueType.NONE);
                }
            } else {
                throw new IllegalArgumentException("Invalid bubble shape: " + bubbleValue.getShape());
            }
            i++;
        }
        return isTouched();
    }

    public void removeMargins() {
        float f;
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        if (contentRectMinusAllMargins.height() == 0 || contentRectMinusAllMargins.width() == 0) {
            return;
        }
        float fComputeRawDistanceX = this.computator.computeRawDistanceX(this.maxRadius * this.bubbleScaleX);
        float fComputeRawDistanceY = this.computator.computeRawDistanceY(this.maxRadius * this.bubbleScaleY);
        float fWidth = this.computator.getMaximumViewport().width() / contentRectMinusAllMargins.width();
        float fHeight = this.computator.getMaximumViewport().height() / contentRectMinusAllMargins.height();
        float f2 = 0.0f;
        if (this.isBubbleScaledByX) {
            f2 = (fComputeRawDistanceY - fComputeRawDistanceX) * fHeight * 0.75f;
            f = 0.0f;
        } else {
            f = (fComputeRawDistanceX - fComputeRawDistanceY) * fWidth * 0.75f;
        }
        Viewport maximumViewport = this.computator.getMaximumViewport();
        maximumViewport.inset(f, f2);
        Viewport currentViewport = this.computator.getCurrentViewport();
        currentViewport.inset(f, f2);
        this.computator.setMaxViewport(maximumViewport);
        this.computator.setCurrentViewport(currentViewport);
    }

    private void drawBubbles(Canvas canvas) {
        Iterator<BubbleValue> it = this.dataProvider.getBubbleChartData().getValues().iterator();
        while (it.hasNext()) {
            drawBubble(canvas, it.next());
        }
    }

    private void drawBubble(Canvas canvas, BubbleValue bubbleValue) {
        float fProcessBubble = processBubble(bubbleValue, this.bubbleCenter) - this.touchAdditional;
        this.bubbleRect.inset(this.touchAdditional, this.touchAdditional);
        this.bubblePaint.setColor(bubbleValue.getColor());
        drawBubbleShapeAndLabel(canvas, bubbleValue, fProcessBubble, 0);
    }

    private void drawBubbleShapeAndLabel(Canvas canvas, BubbleValue bubbleValue, float f, int i) {
        if (ValueShape.SQUARE.equals(bubbleValue.getShape())) {
            canvas.drawRect(this.bubbleRect, this.bubblePaint);
        } else if (ValueShape.CIRCLE.equals(bubbleValue.getShape())) {
            canvas.drawCircle(this.bubbleCenter.x, this.bubbleCenter.y, f, this.bubblePaint);
        } else {
            throw new IllegalArgumentException("Invalid bubble shape: " + bubbleValue.getShape());
        }
        if (1 == i) {
            if (this.hasLabels || this.hasLabelsOnlyForSelected) {
                drawLabel(canvas, bubbleValue, this.bubbleCenter.x, this.bubbleCenter.y);
                return;
            }
            return;
        }
        if (i == 0) {
            if (this.hasLabels) {
                drawLabel(canvas, bubbleValue, this.bubbleCenter.x, this.bubbleCenter.y);
            }
        } else {
            throw new IllegalStateException("Cannot process bubble in mode: " + i);
        }
    }

    private void highlightBubbles(Canvas canvas) {
        highlightBubble(canvas, this.dataProvider.getBubbleChartData().getValues().get(this.selectedValue.getFirstIndex()));
    }

    private void highlightBubble(Canvas canvas, BubbleValue bubbleValue) {
        float fProcessBubble = processBubble(bubbleValue, this.bubbleCenter);
        this.bubblePaint.setColor(bubbleValue.getDarkenColor());
        drawBubbleShapeAndLabel(canvas, bubbleValue, fProcessBubble, 1);
    }

    private float processBubble(BubbleValue bubbleValue, PointF pointF) {
        float fComputeRawDistanceY;
        float fComputeRawX = this.computator.computeRawX(bubbleValue.getX());
        float fComputeRawY = this.computator.computeRawY(bubbleValue.getY());
        double dAbs = Math.abs(bubbleValue.getZ());
        Double.isNaN(dAbs);
        float fSqrt = (float) Math.sqrt(dAbs / 3.141592653589793d);
        if (this.isBubbleScaledByX) {
            fComputeRawDistanceY = this.computator.computeRawDistanceX(fSqrt * this.bubbleScaleX);
        } else {
            fComputeRawDistanceY = this.computator.computeRawDistanceY(fSqrt * this.bubbleScaleY);
        }
        if (fComputeRawDistanceY < this.minRawRadius + this.touchAdditional) {
            fComputeRawDistanceY = this.minRawRadius + this.touchAdditional;
        }
        this.bubbleCenter.set(fComputeRawX, fComputeRawY);
        if (ValueShape.SQUARE.equals(bubbleValue.getShape())) {
            this.bubbleRect.set(fComputeRawX - fComputeRawDistanceY, fComputeRawY - fComputeRawDistanceY, fComputeRawX + fComputeRawDistanceY, fComputeRawY + fComputeRawDistanceY);
        }
        return fComputeRawDistanceY;
    }

    private void drawLabel(Canvas canvas, BubbleValue bubbleValue, float f, float f2) {
        float f3;
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        int chartValue = this.valueFormatter.formatChartValue(this.labelBuffer, bubbleValue);
        if (chartValue == 0) {
            return;
        }
        float fMeasureText = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - chartValue, chartValue);
        int iAbs = Math.abs(this.fontMetrics.ascent);
        float f4 = fMeasureText / 2.0f;
        float f5 = (f - f4) - this.labelMargin;
        float f6 = f4 + f + this.labelMargin;
        float f7 = iAbs / 2;
        float f8 = (f2 - f7) - this.labelMargin;
        float f9 = f7 + f2 + this.labelMargin;
        if (f8 < contentRectMinusAllMargins.top) {
            f9 = iAbs + f2 + (this.labelMargin * 2);
            f8 = f2;
        }
        if (f9 > contentRectMinusAllMargins.bottom) {
            f8 = (f2 - iAbs) - (this.labelMargin * 2);
        } else {
            f2 = f9;
        }
        if (f5 < contentRectMinusAllMargins.left) {
            f3 = f + fMeasureText + (this.labelMargin * 2);
            f5 = f;
        } else {
            f3 = f6;
        }
        if (f3 > contentRectMinusAllMargins.right) {
            f5 = (f - fMeasureText) - (this.labelMargin * 2);
        } else {
            f = f3;
        }
        this.labelBackgroundRect.set(f5, f8, f, f2);
        drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - chartValue, chartValue, bubbleValue.getDarkenColor());
    }

    private void calculateMaxViewport() {
        float fAbs = Float.MIN_VALUE;
        this.tempMaximumViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        BubbleChartData bubbleChartData = this.dataProvider.getBubbleChartData();
        for (BubbleValue bubbleValue : bubbleChartData.getValues()) {
            if (Math.abs(bubbleValue.getZ()) > fAbs) {
                fAbs = Math.abs(bubbleValue.getZ());
            }
            if (bubbleValue.getX() < this.tempMaximumViewport.left) {
                this.tempMaximumViewport.left = bubbleValue.getX();
            }
            if (bubbleValue.getX() > this.tempMaximumViewport.right) {
                this.tempMaximumViewport.right = bubbleValue.getX();
            }
            if (bubbleValue.getY() < this.tempMaximumViewport.bottom) {
                this.tempMaximumViewport.bottom = bubbleValue.getY();
            }
            if (bubbleValue.getY() > this.tempMaximumViewport.top) {
                this.tempMaximumViewport.top = bubbleValue.getY();
            }
        }
        double d = fAbs;
        Double.isNaN(d);
        this.maxRadius = (float) Math.sqrt(d / 3.141592653589793d);
        this.bubbleScaleX = this.tempMaximumViewport.width() / (this.maxRadius * 4.0f);
        if (this.bubbleScaleX == 0.0f) {
            this.bubbleScaleX = 1.0f;
        }
        this.bubbleScaleY = this.tempMaximumViewport.height() / (this.maxRadius * 4.0f);
        if (this.bubbleScaleY == 0.0f) {
            this.bubbleScaleY = 1.0f;
        }
        this.bubbleScaleX *= bubbleChartData.getBubbleScale();
        this.bubbleScaleY *= bubbleChartData.getBubbleScale();
        this.tempMaximumViewport.inset((-this.maxRadius) * this.bubbleScaleX, (-this.maxRadius) * this.bubbleScaleY);
        this.minRawRadius = ChartUtils.dp2px(this.density, this.dataProvider.getBubbleChartData().getMinBubbleRadius());
    }
}
