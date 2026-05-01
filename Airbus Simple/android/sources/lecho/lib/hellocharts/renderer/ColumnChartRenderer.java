package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import java.util.Iterator;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.ColumnChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class ColumnChartRenderer extends AbstractChartRenderer {
    public static final int DEFAULT_COLUMN_TOUCH_ADDITIONAL_WIDTH_DP = 4;
    public static final int DEFAULT_SUBCOLUMN_SPACING_DP = 1;
    private static final int MODE_CHECK_TOUCH = 1;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 2;
    private float baseValue;
    private Paint columnPaint;
    private ColumnChartDataProvider dataProvider;
    private RectF drawRect;
    private float fillRatio;
    private int subcolumnSpacing;
    private Viewport tempMaximumViewport;
    private int touchAdditionalWidth;
    private PointF touchedPoint;

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartSizeChanged() {
    }

    public ColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider columnChartDataProvider) {
        super(context, chart);
        this.columnPaint = new Paint();
        this.drawRect = new RectF();
        this.touchedPoint = new PointF();
        this.tempMaximumViewport = new Viewport();
        this.dataProvider = columnChartDataProvider;
        this.subcolumnSpacing = ChartUtils.dp2px(this.density, 1);
        this.touchAdditionalWidth = ChartUtils.dp2px(this.density, 4);
        this.columnPaint.setAntiAlias(true);
        this.columnPaint.setStyle(Paint.Style.FILL);
        this.columnPaint.setStrokeCap(Paint.Cap.SQUARE);
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartDataChanged() {
        super.onChartDataChanged();
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        this.fillRatio = columnChartData.getFillRatio();
        this.baseValue = columnChartData.getBaseValue();
        onChartViewportChanged();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartViewportChanged() {
        if (this.isViewportCalculationEnabled) {
            calculateMaxViewport();
            this.computator.setMaxViewport(this.tempMaximumViewport);
            this.computator.setCurrentViewport(this.computator.getMaximumViewport());
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void draw(Canvas canvas) {
        if (this.dataProvider.getColumnChartData().isStacked()) {
            drawColumnForStacked(canvas);
            if (isTouched()) {
                highlightColumnForStacked(canvas);
                return;
            }
            return;
        }
        drawColumnsForSubcolumns(canvas);
        if (isTouched()) {
            highlightColumnsForSubcolumns(canvas);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean checkTouch(float f, float f2) {
        this.selectedValue.clear();
        if (this.dataProvider.getColumnChartData().isStacked()) {
            checkTouchForStacked(f, f2);
        } else {
            checkTouchForSubcolumns(f, f2);
        }
        return isTouched();
    }

    private void calculateMaxViewport() {
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        this.tempMaximumViewport.set(-0.5f, this.baseValue, columnChartData.getColumns().size() - 0.5f, this.baseValue);
        if (columnChartData.isStacked()) {
            calculateMaxViewportForStacked(columnChartData);
        } else {
            calculateMaxViewportForSubcolumns(columnChartData);
        }
    }

    private void calculateMaxViewportForSubcolumns(ColumnChartData columnChartData) {
        Iterator<Column> it = columnChartData.getColumns().iterator();
        while (it.hasNext()) {
            for (SubcolumnValue subcolumnValue : it.next().getValues()) {
                if (subcolumnValue.getValue() >= this.baseValue && subcolumnValue.getValue() > this.tempMaximumViewport.top) {
                    this.tempMaximumViewport.top = subcolumnValue.getValue();
                }
                if (subcolumnValue.getValue() < this.baseValue && subcolumnValue.getValue() < this.tempMaximumViewport.bottom) {
                    this.tempMaximumViewport.bottom = subcolumnValue.getValue();
                }
            }
        }
    }

    private void calculateMaxViewportForStacked(ColumnChartData columnChartData) {
        for (Column column : columnChartData.getColumns()) {
            float value = this.baseValue;
            float value2 = this.baseValue;
            for (SubcolumnValue subcolumnValue : column.getValues()) {
                if (subcolumnValue.getValue() >= this.baseValue) {
                    value += subcolumnValue.getValue();
                } else {
                    value2 += subcolumnValue.getValue();
                }
            }
            if (value > this.tempMaximumViewport.top) {
                this.tempMaximumViewport.top = value;
            }
            if (value2 < this.tempMaximumViewport.bottom) {
                this.tempMaximumViewport.bottom = value2;
            }
        }
    }

    private void drawColumnsForSubcolumns(Canvas canvas) {
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        float fCalculateColumnWidth = calculateColumnWidth();
        Iterator<Column> it = columnChartData.getColumns().iterator();
        int i = 0;
        while (it.hasNext()) {
            processColumnForSubcolumns(canvas, it.next(), fCalculateColumnWidth, i, 0);
            i++;
        }
    }

    private void highlightColumnsForSubcolumns(Canvas canvas) {
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        processColumnForSubcolumns(canvas, columnChartData.getColumns().get(this.selectedValue.getFirstIndex()), calculateColumnWidth(), this.selectedValue.getFirstIndex(), 2);
    }

    private void checkTouchForSubcolumns(float f, float f2) {
        this.touchedPoint.x = f;
        this.touchedPoint.y = f2;
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        float fCalculateColumnWidth = calculateColumnWidth();
        Iterator<Column> it = columnChartData.getColumns().iterator();
        int i = 0;
        while (it.hasNext()) {
            processColumnForSubcolumns(null, it.next(), fCalculateColumnWidth, i, 1);
            i++;
        }
    }

    private void processColumnForSubcolumns(Canvas canvas, Column column, float f, int i, int i2) {
        int i3;
        float size = (f - (this.subcolumnSpacing * (column.getValues().size() - 1))) / column.getValues().size();
        float f2 = size < 1.0f ? 1.0f : size;
        float fComputeRawX = this.computator.computeRawX(i);
        float f3 = f / 2.0f;
        float fComputeRawY = this.computator.computeRawY(this.baseValue);
        float f4 = fComputeRawX - f3;
        int i4 = 0;
        for (SubcolumnValue subcolumnValue : column.getValues()) {
            this.columnPaint.setColor(subcolumnValue.getColor());
            if (f4 > fComputeRawX + f3) {
                return;
            }
            int i5 = i4;
            calculateRectToDraw(subcolumnValue, f4, f4 + f2, fComputeRawY, this.computator.computeRawY(subcolumnValue.getValue()));
            switch (i2) {
                case 0:
                    i3 = i5;
                    drawSubcolumn(canvas, column, subcolumnValue, false);
                    break;
                case 1:
                    i3 = i5;
                    checkRectToDraw(i, i3);
                    break;
                case 2:
                    highlightSubcolumn(canvas, column, subcolumnValue, i5, false);
                    i3 = i5;
                    break;
                default:
                    throw new IllegalStateException("Cannot process column in mode: " + i2);
            }
            f4 += this.subcolumnSpacing + f2;
            i4 = i3 + 1;
        }
    }

    private void drawColumnForStacked(Canvas canvas) {
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        float fCalculateColumnWidth = calculateColumnWidth();
        Iterator<Column> it = columnChartData.getColumns().iterator();
        int i = 0;
        while (it.hasNext()) {
            processColumnForStacked(canvas, it.next(), fCalculateColumnWidth, i, 0);
            i++;
        }
    }

    private void highlightColumnForStacked(Canvas canvas) {
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        processColumnForStacked(canvas, columnChartData.getColumns().get(this.selectedValue.getFirstIndex()), calculateColumnWidth(), this.selectedValue.getFirstIndex(), 2);
    }

    private void checkTouchForStacked(float f, float f2) {
        this.touchedPoint.x = f;
        this.touchedPoint.y = f2;
        ColumnChartData columnChartData = this.dataProvider.getColumnChartData();
        float fCalculateColumnWidth = calculateColumnWidth();
        Iterator<Column> it = columnChartData.getColumns().iterator();
        int i = 0;
        while (it.hasNext()) {
            processColumnForStacked(null, it.next(), fCalculateColumnWidth, i, 1);
            i++;
        }
    }

    private void processColumnForStacked(Canvas canvas, Column column, float f, int i, int i2) {
        float value;
        float value2;
        float fComputeRawX = this.computator.computeRawX(i);
        float f2 = f / 2.0f;
        float f3 = this.baseValue;
        float f4 = this.baseValue;
        float f5 = this.baseValue;
        int i3 = 0;
        for (SubcolumnValue subcolumnValue : column.getValues()) {
            this.columnPaint.setColor(subcolumnValue.getColor());
            if (subcolumnValue.getValue() >= this.baseValue) {
                value2 = f4;
                value = subcolumnValue.getValue() + f3;
            } else {
                value = f3;
                f3 = f4;
                value2 = subcolumnValue.getValue() + f4;
            }
            calculateRectToDraw(subcolumnValue, fComputeRawX - f2, fComputeRawX + f2, this.computator.computeRawY(f3), this.computator.computeRawY(f3 + subcolumnValue.getValue()));
            switch (i2) {
                case 0:
                    drawSubcolumn(canvas, column, subcolumnValue, true);
                    break;
                case 1:
                    checkRectToDraw(i, i3);
                    break;
                case 2:
                    highlightSubcolumn(canvas, column, subcolumnValue, i3, true);
                    break;
                default:
                    throw new IllegalStateException("Cannot process column in mode: " + i2);
            }
            i3++;
            f3 = value;
            f4 = value2;
        }
    }

    private void drawSubcolumn(Canvas canvas, Column column, SubcolumnValue subcolumnValue, boolean z) {
        canvas.drawRect(this.drawRect, this.columnPaint);
        if (column.hasLabels()) {
            drawLabel(canvas, column, subcolumnValue, z, this.labelOffset);
        }
    }

    private void highlightSubcolumn(Canvas canvas, Column column, SubcolumnValue subcolumnValue, int i, boolean z) {
        if (this.selectedValue.getSecondIndex() == i) {
            this.columnPaint.setColor(subcolumnValue.getDarkenColor());
            canvas.drawRect(this.drawRect.left - this.touchAdditionalWidth, this.drawRect.top, this.drawRect.right + this.touchAdditionalWidth, this.drawRect.bottom, this.columnPaint);
            if (column.hasLabels() || column.hasLabelsOnlyForSelected()) {
                drawLabel(canvas, column, subcolumnValue, z, this.labelOffset);
            }
        }
    }

    private void checkRectToDraw(int i, int i2) {
        if (this.drawRect.contains(this.touchedPoint.x, this.touchedPoint.y)) {
            this.selectedValue.set(i, i2, SelectedValue.SelectedValueType.COLUMN);
        }
    }

    private float calculateColumnWidth() {
        float fWidth = (this.fillRatio * this.computator.getContentRectMinusAllMargins().width()) / this.computator.getVisibleViewport().width();
        if (fWidth < 2.0f) {
            return 2.0f;
        }
        return fWidth;
    }

    private void calculateRectToDraw(SubcolumnValue subcolumnValue, float f, float f2, float f3, float f4) {
        this.drawRect.left = f;
        this.drawRect.right = f2;
        if (subcolumnValue.getValue() >= this.baseValue) {
            this.drawRect.top = f4;
            this.drawRect.bottom = f3 - this.subcolumnSpacing;
        } else {
            this.drawRect.bottom = f4;
            this.drawRect.top = f3 + this.subcolumnSpacing;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void drawLabel(android.graphics.Canvas r8, lecho.lib.hellocharts.model.Column r9, lecho.lib.hellocharts.model.SubcolumnValue r10, boolean r11, float r12) {
        /*
            Method dump skipped, instructions count: 261
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: lecho.lib.hellocharts.renderer.ColumnChartRenderer.drawLabel(android.graphics.Canvas, lecho.lib.hellocharts.model.Column, lecho.lib.hellocharts.model.SubcolumnValue, boolean, float):void");
    }
}
