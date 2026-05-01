package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class ComboChartRenderer extends AbstractChartRenderer {
    protected List<ChartRenderer> renderers;
    protected Viewport unionViewport;

    public ComboChartRenderer(Context context, Chart chart) {
        super(context, chart);
        this.unionViewport = new Viewport();
        this.renderers = new ArrayList();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartSizeChanged() {
        Iterator<ChartRenderer> it = this.renderers.iterator();
        while (it.hasNext()) {
            it.next().onChartSizeChanged();
        }
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartDataChanged() {
        super.onChartDataChanged();
        Iterator<ChartRenderer> it = this.renderers.iterator();
        while (it.hasNext()) {
            it.next().onChartDataChanged();
        }
        onChartViewportChanged();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartViewportChanged() {
        if (this.isViewportCalculationEnabled) {
            int i = 0;
            for (ChartRenderer chartRenderer : this.renderers) {
                chartRenderer.onChartViewportChanged();
                if (i == 0) {
                    this.unionViewport.set(chartRenderer.getMaximumViewport());
                } else {
                    this.unionViewport.union(chartRenderer.getMaximumViewport());
                }
                i++;
            }
            this.computator.setMaxViewport(this.unionViewport);
            this.computator.setCurrentViewport(this.unionViewport);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void draw(Canvas canvas) {
        Iterator<ChartRenderer> it = this.renderers.iterator();
        while (it.hasNext()) {
            it.next().draw(canvas);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
        Iterator<ChartRenderer> it = this.renderers.iterator();
        while (it.hasNext()) {
            it.next().drawUnclipped(canvas);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean checkTouch(float f, float f2) {
        this.selectedValue.clear();
        int size = this.renderers.size() - 1;
        while (true) {
            if (size < 0) {
                break;
            }
            ChartRenderer chartRenderer = this.renderers.get(size);
            if (chartRenderer.checkTouch(f, f2)) {
                this.selectedValue.set(chartRenderer.getSelectedValue());
                break;
            }
            size--;
        }
        while (true) {
            size--;
            if (size >= 0) {
                this.renderers.get(size).clearTouch();
            } else {
                return isTouched();
            }
        }
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void clearTouch() {
        Iterator<ChartRenderer> it = this.renderers.iterator();
        while (it.hasNext()) {
            it.next().clearTouch();
        }
        this.selectedValue.clear();
    }
}
