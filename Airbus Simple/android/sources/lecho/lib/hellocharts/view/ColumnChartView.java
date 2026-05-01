package lecho.lib.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.DummyColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.ColumnChartDataProvider;
import lecho.lib.hellocharts.renderer.ColumnChartRenderer;

/* loaded from: classes.dex */
public class ColumnChartView extends AbstractChartView implements ColumnChartDataProvider {
    private static final String TAG = "ColumnChartView";
    private ColumnChartData data;
    private ColumnChartOnValueSelectListener onValueTouchListener;

    public ColumnChartView(Context context) {
        this(context, null, 0);
    }

    public ColumnChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColumnChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.onValueTouchListener = new DummyColumnChartOnValueSelectListener();
        setChartRenderer(new ColumnChartRenderer(context, this, this));
        setColumnChartData(ColumnChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
    public ColumnChartData getColumnChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
    public void setColumnChartData(ColumnChartData columnChartData) {
        if (columnChartData == null) {
            this.data = ColumnChartData.generateDummyData();
        } else {
            this.data = columnChartData;
        }
        super.onChartDataChange();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ColumnChartData getChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getColumns().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
        } else {
            this.onValueTouchListener.onValueDeselected();
        }
    }

    public ColumnChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(ColumnChartOnValueSelectListener columnChartOnValueSelectListener) {
        if (columnChartOnValueSelectListener != null) {
            this.onValueTouchListener = columnChartOnValueSelectListener;
        }
    }
}
