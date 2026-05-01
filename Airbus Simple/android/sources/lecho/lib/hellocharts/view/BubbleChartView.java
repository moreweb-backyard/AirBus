package lecho.lib.hellocharts.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.DummyBubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.provider.BubbleChartDataProvider;
import lecho.lib.hellocharts.renderer.BubbleChartRenderer;

/* loaded from: classes.dex */
public class BubbleChartView extends AbstractChartView implements BubbleChartDataProvider {
    private static final String TAG = "BubbleChartView";
    protected BubbleChartRenderer bubbleChartRenderer;
    protected BubbleChartData data;
    protected BubbleChartOnValueSelectListener onValueTouchListener;

    public BubbleChartView(Context context) {
        this(context, null, 0);
    }

    public BubbleChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BubbleChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.onValueTouchListener = new DummyBubbleChartOnValueSelectListener();
        this.bubbleChartRenderer = new BubbleChartRenderer(context, this, this);
        setChartRenderer(this.bubbleChartRenderer);
        setBubbleChartData(BubbleChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.BubbleChartDataProvider
    public BubbleChartData getBubbleChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.BubbleChartDataProvider
    public void setBubbleChartData(BubbleChartData bubbleChartData) {
        if (bubbleChartData == null) {
            this.data = BubbleChartData.generateDummyData();
        } else {
            this.data = bubbleChartData;
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
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), this.data.getValues().get(selectedValue.getFirstIndex()));
        } else {
            this.onValueTouchListener.onValueDeselected();
        }
    }

    public BubbleChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(BubbleChartOnValueSelectListener bubbleChartOnValueSelectListener) {
        if (bubbleChartOnValueSelectListener != null) {
            this.onValueTouchListener = bubbleChartOnValueSelectListener;
        }
    }

    public void removeMargins() {
        this.bubbleChartRenderer.removeMargins();
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
