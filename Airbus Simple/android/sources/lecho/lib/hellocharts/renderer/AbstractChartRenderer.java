package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public abstract class AbstractChartRenderer implements ChartRenderer {
    protected Chart chart;
    protected ChartComputator computator;
    protected float density;
    protected boolean isValueLabelBackgroundAuto;
    protected boolean isValueLabelBackgroundEnabled;
    protected int labelMargin;
    protected int labelOffset;
    protected float scaledDensity;
    public int DEFAULT_LABEL_MARGIN_DP = 4;
    protected Paint labelPaint = new Paint();
    protected Paint labelBackgroundPaint = new Paint();
    protected RectF labelBackgroundRect = new RectF();
    protected Paint.FontMetricsInt fontMetrics = new Paint.FontMetricsInt();
    protected boolean isViewportCalculationEnabled = true;
    protected SelectedValue selectedValue = new SelectedValue();
    protected char[] labelBuffer = new char[64];

    public AbstractChartRenderer(Context context, Chart chart) {
        this.density = context.getResources().getDisplayMetrics().density;
        this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.chart = chart;
        this.computator = chart.getChartComputator();
        this.labelMargin = ChartUtils.dp2px(this.density, this.DEFAULT_LABEL_MARGIN_DP);
        this.labelOffset = this.labelMargin;
        this.labelPaint.setAntiAlias(true);
        this.labelPaint.setStyle(Paint.Style.FILL);
        this.labelPaint.setTextAlign(Paint.Align.LEFT);
        this.labelPaint.setTypeface(Typeface.defaultFromStyle(1));
        this.labelPaint.setColor(-1);
        this.labelBackgroundPaint.setAntiAlias(true);
        this.labelBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void resetRenderer() {
        this.computator = this.chart.getChartComputator();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartDataChanged() {
        ChartData chartData = this.chart.getChartData();
        Typeface valueLabelTypeface = this.chart.getChartData().getValueLabelTypeface();
        if (valueLabelTypeface != null) {
            this.labelPaint.setTypeface(valueLabelTypeface);
        }
        this.labelPaint.setColor(chartData.getValueLabelTextColor());
        this.labelPaint.setTextSize(ChartUtils.sp2px(this.scaledDensity, chartData.getValueLabelTextSize()));
        this.labelPaint.getFontMetricsInt(this.fontMetrics);
        this.isValueLabelBackgroundEnabled = chartData.isValueLabelBackgroundEnabled();
        this.isValueLabelBackgroundAuto = chartData.isValueLabelBackgroundAuto();
        this.labelBackgroundPaint.setColor(chartData.getValueLabelBackgroundColor());
        this.selectedValue.clear();
    }

    protected void drawLabelTextAndBackground(Canvas canvas, char[] cArr, int i, int i2, int i3) {
        float f;
        float f2;
        if (this.isValueLabelBackgroundEnabled) {
            if (this.isValueLabelBackgroundAuto) {
                this.labelBackgroundPaint.setColor(i3);
            }
            canvas.drawRect(this.labelBackgroundRect, this.labelBackgroundPaint);
            f = this.labelBackgroundRect.left + this.labelMargin;
            f2 = this.labelBackgroundRect.bottom - this.labelMargin;
        } else {
            f = this.labelBackgroundRect.left;
            f2 = this.labelBackgroundRect.bottom;
        }
        canvas.drawText(cArr, i, i2, f, f2, this.labelPaint);
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean isTouched() {
        return this.selectedValue.isSet();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void clearTouch() {
        this.selectedValue.clear();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public Viewport getMaximumViewport() {
        return this.computator.getMaximumViewport();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void setMaximumViewport(Viewport viewport) {
        if (viewport != null) {
            this.computator.setMaxViewport(viewport);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public Viewport getCurrentViewport() {
        return this.computator.getCurrentViewport();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void setCurrentViewport(Viewport viewport) {
        if (viewport != null) {
            this.computator.setCurrentViewport(viewport);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean isViewportCalculationEnabled() {
        return this.isViewportCalculationEnabled;
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void setViewportCalculationEnabled(boolean z) {
        this.isViewportCalculationEnabled = z;
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void selectValue(SelectedValue selectedValue) {
        this.selectedValue.set(selectedValue);
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public SelectedValue getSelectedValue() {
        return this.selectedValue;
    }
}
