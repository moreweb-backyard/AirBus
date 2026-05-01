package lecho.lib.hellocharts.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import lecho.lib.hellocharts.computator.PreviewChartComputator;
import lecho.lib.hellocharts.gesture.PreviewChartTouchHandler;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.renderer.PreviewColumnChartRenderer;

/* loaded from: classes.dex */
public class PreviewColumnChartView extends ColumnChartView {
    private static final String TAG = "ColumnChartView";
    protected PreviewColumnChartRenderer previewChartRenderer;

    public PreviewColumnChartView(Context context) {
        this(context, null, 0);
    }

    public PreviewColumnChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PreviewColumnChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.chartComputator = new PreviewChartComputator();
        this.previewChartRenderer = new PreviewColumnChartRenderer(context, this, this);
        this.touchHandler = new PreviewChartTouchHandler(context, this);
        setChartRenderer(this.previewChartRenderer);
        setColumnChartData(ColumnChartData.generateDummyData());
    }

    public int getPreviewColor() {
        return this.previewChartRenderer.getPreviewColor();
    }

    public void setPreviewColor(int i) {
        this.previewChartRenderer.setPreviewColor(i);
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
