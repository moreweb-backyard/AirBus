package lecho.lib.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;
import lecho.lib.hellocharts.listener.DummyLineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.renderer.LineChartRenderer;

/* loaded from: classes.dex */
public class LineChartView extends AbstractChartView implements LineChartDataProvider {
    private static final String TAG = "LineChartView";
    protected LineChartData data;
    protected LineChartOnValueSelectListener onValueTouchListener;

    public LineChartView(Context context) {
        this(context, null, 0);
    }

    public LineChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LineChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.onValueTouchListener = new DummyLineChartOnValueSelectListener();
        setChartRenderer(new LineChartRenderer(context, this, this));
        setLineChartData(LineChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
    public LineChartData getLineChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
    public void setLineChartData(LineChartData lineChartData) {
        if (lineChartData == null) {
            this.data = LineChartData.generateDummyData();
        } else {
            this.data = lineChartData;
        }
        super.onChartDataChange();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartData getChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getLines().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
        } else {
            this.onValueTouchListener.onValueDeselected();
        }
    }

    public LineChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(LineChartOnValueSelectListener lineChartOnValueSelectListener) {
        if (lineChartOnValueSelectListener != null) {
            this.onValueTouchListener = lineChartOnValueSelectListener;
        }
    }
}
