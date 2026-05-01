package lecho.lib.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;
import lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.DummyCompoLineColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.ComboLineColumnChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.ColumnChartDataProvider;
import lecho.lib.hellocharts.provider.ComboLineColumnChartDataProvider;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.renderer.ColumnChartRenderer;
import lecho.lib.hellocharts.renderer.ComboLineColumnChartRenderer;
import lecho.lib.hellocharts.renderer.LineChartRenderer;

/* loaded from: classes.dex */
public class ComboLineColumnChartView extends AbstractChartView implements ComboLineColumnChartDataProvider {
    private static final String TAG = "ComboLineColumnChartView";
    protected ColumnChartDataProvider columnChartDataProvider;
    protected ComboLineColumnChartData data;
    protected LineChartDataProvider lineChartDataProvider;
    protected ComboLineColumnChartOnValueSelectListener onValueTouchListener;

    public ComboLineColumnChartView(Context context) {
        this(context, null, 0);
    }

    public ComboLineColumnChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ComboLineColumnChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.columnChartDataProvider = new ComboColumnChartDataProvider();
        this.lineChartDataProvider = new ComboLineChartDataProvider();
        this.onValueTouchListener = new DummyCompoLineColumnChartOnValueSelectListener();
        setChartRenderer(new ComboLineColumnChartRenderer(context, this, this.columnChartDataProvider, this.lineChartDataProvider));
        setComboLineColumnChartData(ComboLineColumnChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.ComboLineColumnChartDataProvider
    public ComboLineColumnChartData getComboLineColumnChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.ComboLineColumnChartDataProvider
    public void setComboLineColumnChartData(ComboLineColumnChartData comboLineColumnChartData) {
        if (comboLineColumnChartData == null) {
            this.data = null;
        } else {
            this.data = comboLineColumnChartData;
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
            if (SelectedValue.SelectedValueType.COLUMN.equals(selectedValue.getType())) {
                this.onValueTouchListener.onColumnValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getColumnChartData().getColumns().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
                return;
            } else if (SelectedValue.SelectedValueType.LINE.equals(selectedValue.getType())) {
                this.onValueTouchListener.onPointValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getLineChartData().getLines().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
                return;
            } else {
                throw new IllegalArgumentException("Invalid selected value type " + selectedValue.getType().name());
            }
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public ComboLineColumnChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(ComboLineColumnChartOnValueSelectListener comboLineColumnChartOnValueSelectListener) {
        if (comboLineColumnChartOnValueSelectListener != null) {
            this.onValueTouchListener = comboLineColumnChartOnValueSelectListener;
        }
    }

    public void setColumnChartRenderer(Context context, ColumnChartRenderer columnChartRenderer) {
        setChartRenderer(new ComboLineColumnChartRenderer(context, this, columnChartRenderer, this.lineChartDataProvider));
    }

    public void setLineChartRenderer(Context context, LineChartRenderer lineChartRenderer) {
        setChartRenderer(new ComboLineColumnChartRenderer(context, this, this.columnChartDataProvider, lineChartRenderer));
    }

    private class ComboLineChartDataProvider implements LineChartDataProvider {
        private ComboLineChartDataProvider() {
        }

        @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
        public LineChartData getLineChartData() {
            return ComboLineColumnChartView.this.data.getLineChartData();
        }

        @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
        public void setLineChartData(LineChartData lineChartData) {
            ComboLineColumnChartView.this.data.setLineChartData(lineChartData);
        }
    }

    private class ComboColumnChartDataProvider implements ColumnChartDataProvider {
        private ComboColumnChartDataProvider() {
        }

        @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
        public ColumnChartData getColumnChartData() {
            return ComboLineColumnChartView.this.data.getColumnChartData();
        }

        @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
        public void setColumnChartData(ColumnChartData columnChartData) {
            ComboLineColumnChartView.this.data.setColumnChartData(columnChartData);
        }
    }
}
