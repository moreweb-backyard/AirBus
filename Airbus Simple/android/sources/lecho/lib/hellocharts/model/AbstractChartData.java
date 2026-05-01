package lecho.lib.hellocharts.model;

import android.graphics.Typeface;
import lecho.lib.hellocharts.util.ChartUtils;

/* loaded from: classes.dex */
public abstract class AbstractChartData implements ChartData {
    public static final int DEFAULT_TEXT_SIZE_SP = 12;
    protected Axis axisXBottom;
    protected Axis axisXTop;
    protected Axis axisYLeft;
    protected Axis axisYRight;
    protected boolean isValueLabelBackgroundEnabled;
    protected boolean isValueLabelBackgrountAuto;
    protected int valueLabelBackgroundColor;
    protected int valueLabelTextColor;
    protected int valueLabelTextSize;
    protected Typeface valueLabelTypeface;

    public AbstractChartData() {
        this.valueLabelTextColor = -1;
        this.valueLabelTextSize = 12;
        this.isValueLabelBackgroundEnabled = true;
        this.isValueLabelBackgrountAuto = true;
        this.valueLabelBackgroundColor = ChartUtils.darkenColor(ChartUtils.DEFAULT_DARKEN_COLOR);
    }

    public AbstractChartData(AbstractChartData abstractChartData) {
        this.valueLabelTextColor = -1;
        this.valueLabelTextSize = 12;
        this.isValueLabelBackgroundEnabled = true;
        this.isValueLabelBackgrountAuto = true;
        this.valueLabelBackgroundColor = ChartUtils.darkenColor(ChartUtils.DEFAULT_DARKEN_COLOR);
        if (abstractChartData.axisXBottom != null) {
            this.axisXBottom = new Axis(abstractChartData.axisXBottom);
        }
        if (abstractChartData.axisXTop != null) {
            this.axisXTop = new Axis(abstractChartData.axisXTop);
        }
        if (abstractChartData.axisYLeft != null) {
            this.axisYLeft = new Axis(abstractChartData.axisYLeft);
        }
        if (abstractChartData.axisYRight != null) {
            this.axisYRight = new Axis(abstractChartData.axisYRight);
        }
        this.valueLabelTextColor = abstractChartData.valueLabelTextColor;
        this.valueLabelTextSize = abstractChartData.valueLabelTextSize;
        this.valueLabelTypeface = abstractChartData.valueLabelTypeface;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisXBottom() {
        return this.axisXBottom;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisXBottom(Axis axis) {
        this.axisXBottom = axis;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisYLeft() {
        return this.axisYLeft;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisYLeft(Axis axis) {
        this.axisYLeft = axis;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisXTop() {
        return this.axisXTop;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisXTop(Axis axis) {
        this.axisXTop = axis;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisYRight() {
        return this.axisYRight;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisYRight(Axis axis) {
        this.axisYRight = axis;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public int getValueLabelTextColor() {
        return this.valueLabelTextColor;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelsTextColor(int i) {
        this.valueLabelTextColor = i;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public int getValueLabelTextSize() {
        return this.valueLabelTextSize;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelTextSize(int i) {
        this.valueLabelTextSize = i;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Typeface getValueLabelTypeface() {
        return this.valueLabelTypeface;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelTypeface(Typeface typeface) {
        this.valueLabelTypeface = typeface;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public boolean isValueLabelBackgroundEnabled() {
        return this.isValueLabelBackgroundEnabled;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelBackgroundEnabled(boolean z) {
        this.isValueLabelBackgroundEnabled = z;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public boolean isValueLabelBackgroundAuto() {
        return this.isValueLabelBackgrountAuto;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelBackgroundAuto(boolean z) {
        this.isValueLabelBackgrountAuto = z;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public int getValueLabelBackgroundColor() {
        return this.valueLabelBackgroundColor;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelBackgroundColor(int i) {
        this.valueLabelBackgroundColor = i;
    }
}
