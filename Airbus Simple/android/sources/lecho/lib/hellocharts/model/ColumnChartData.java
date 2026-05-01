package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ColumnChartData extends AbstractChartData {
    public static final float DEFAULT_BASE_VALUE = 0.0f;
    public static final float DEFAULT_FILL_RATIO = 0.75f;
    private float baseValue;
    private List<Column> columns;
    private float fillRatio;
    private boolean isStacked;

    public ColumnChartData() {
        this.fillRatio = 0.75f;
        this.baseValue = 0.0f;
        this.columns = new ArrayList();
        this.isStacked = false;
    }

    public ColumnChartData(List<Column> list) {
        this.fillRatio = 0.75f;
        this.baseValue = 0.0f;
        this.columns = new ArrayList();
        this.isStacked = false;
        setColumns(list);
    }

    public ColumnChartData(ColumnChartData columnChartData) {
        super(columnChartData);
        this.fillRatio = 0.75f;
        this.baseValue = 0.0f;
        this.columns = new ArrayList();
        this.isStacked = false;
        this.isStacked = columnChartData.isStacked;
        this.fillRatio = columnChartData.fillRatio;
        Iterator<Column> it = columnChartData.columns.iterator();
        while (it.hasNext()) {
            this.columns.add(new Column(it.next()));
        }
    }

    public static ColumnChartData generateDummyData() {
        ColumnChartData columnChartData = new ColumnChartData();
        ArrayList arrayList = new ArrayList(4);
        for (int i = 1; i <= 4; i++) {
            ArrayList arrayList2 = new ArrayList(4);
            arrayList2.add(new SubcolumnValue(i));
            arrayList.add(new Column(arrayList2));
        }
        columnChartData.setColumns(arrayList);
        return columnChartData;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void update(float f) {
        Iterator<Column> it = this.columns.iterator();
        while (it.hasNext()) {
            it.next().update(f);
        }
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void finish() {
        Iterator<Column> it = this.columns.iterator();
        while (it.hasNext()) {
            it.next().finish();
        }
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public ColumnChartData setColumns(List<Column> list) {
        if (list == null) {
            this.columns = new ArrayList();
        } else {
            this.columns = list;
        }
        return this;
    }

    public boolean isStacked() {
        return this.isStacked;
    }

    public ColumnChartData setStacked(boolean z) {
        this.isStacked = z;
        return this;
    }

    public float getFillRatio() {
        return this.fillRatio;
    }

    public ColumnChartData setFillRatio(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f > 1.0f) {
            f = 1.0f;
        }
        this.fillRatio = f;
        return this;
    }

    public float getBaseValue() {
        return this.baseValue;
    }

    public ColumnChartData setBaseValue(float f) {
        this.baseValue = f;
        return this;
    }
}
