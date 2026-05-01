package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.ColumnChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class PreviewColumnChartRenderer extends ColumnChartRenderer {
    private static final int DEFAULT_PREVIEW_STROKE_WIDTH_DP = 2;
    private static final int DEFAULT_PREVIEW_TRANSPARENCY = 64;
    private static final int FULL_ALPHA = 255;
    private Paint previewPaint;

    public PreviewColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider columnChartDataProvider) {
        super(context, chart, columnChartDataProvider);
        this.previewPaint = new Paint();
        this.previewPaint.setAntiAlias(true);
        this.previewPaint.setColor(-3355444);
        this.previewPaint.setStrokeWidth(ChartUtils.dp2px(this.density, 2));
    }

    @Override // lecho.lib.hellocharts.renderer.ColumnChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
        super.drawUnclipped(canvas);
        Viewport currentViewport = this.computator.getCurrentViewport();
        float fComputeRawX = this.computator.computeRawX(currentViewport.left);
        float fComputeRawY = this.computator.computeRawY(currentViewport.top);
        float fComputeRawX2 = this.computator.computeRawX(currentViewport.right);
        float fComputeRawY2 = this.computator.computeRawY(currentViewport.bottom);
        this.previewPaint.setAlpha(64);
        this.previewPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(fComputeRawX, fComputeRawY, fComputeRawX2, fComputeRawY2, this.previewPaint);
        this.previewPaint.setStyle(Paint.Style.STROKE);
        this.previewPaint.setAlpha(255);
        canvas.drawRect(fComputeRawX, fComputeRawY, fComputeRawX2, fComputeRawY2, this.previewPaint);
    }

    public int getPreviewColor() {
        return this.previewPaint.getColor();
    }

    public void setPreviewColor(int i) {
        this.previewPaint.setColor(i);
    }
}
