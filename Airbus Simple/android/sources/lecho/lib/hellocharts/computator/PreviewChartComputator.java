package lecho.lib.hellocharts.computator;

import lecho.lib.hellocharts.model.Viewport;

/* loaded from: classes.dex */
public class PreviewChartComputator extends ChartComputator {
    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public float computeRawX(float f) {
        return this.contentRectMinusAllMargins.left + ((f - this.maxViewport.left) * (this.contentRectMinusAllMargins.width() / this.maxViewport.width()));
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public float computeRawY(float f) {
        return this.contentRectMinusAllMargins.bottom - ((f - this.maxViewport.bottom) * (this.contentRectMinusAllMargins.height() / this.maxViewport.height()));
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public Viewport getVisibleViewport() {
        return this.maxViewport;
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public void setVisibleViewport(Viewport viewport) {
        setMaxViewport(viewport);
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public void constrainViewport(float f, float f2, float f3, float f4) {
        super.constrainViewport(f, f2, f3, f4);
        this.viewportChangeListener.onViewportChanged(this.currentViewport);
    }
}
