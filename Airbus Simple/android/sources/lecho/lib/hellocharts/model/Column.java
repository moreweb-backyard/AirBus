package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;

/* loaded from: classes.dex */
public class Column {
    private ColumnChartValueFormatter formatter;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private List<SubcolumnValue> values;

    public Column() {
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.formatter = new SimpleColumnChartValueFormatter();
        this.values = new ArrayList();
    }

    public Column(List<SubcolumnValue> list) {
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.formatter = new SimpleColumnChartValueFormatter();
        this.values = new ArrayList();
        setValues(list);
    }

    public Column(Column column) {
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.formatter = new SimpleColumnChartValueFormatter();
        this.values = new ArrayList();
        this.hasLabels = column.hasLabels;
        this.hasLabelsOnlyForSelected = column.hasLabelsOnlyForSelected;
        this.formatter = column.formatter;
        Iterator<SubcolumnValue> it = column.values.iterator();
        while (it.hasNext()) {
            this.values.add(new SubcolumnValue(it.next()));
        }
    }

    public void update(float f) {
        Iterator<SubcolumnValue> it = this.values.iterator();
        while (it.hasNext()) {
            it.next().update(f);
        }
    }

    public void finish() {
        Iterator<SubcolumnValue> it = this.values.iterator();
        while (it.hasNext()) {
            it.next().finish();
        }
    }

    public List<SubcolumnValue> getValues() {
        return this.values;
    }

    public Column setValues(List<SubcolumnValue> list) {
        if (list == null) {
            this.values = new ArrayList();
        } else {
            this.values = list;
        }
        return this;
    }

    public boolean hasLabels() {
        return this.hasLabels;
    }

    public Column setHasLabels(boolean z) {
        this.hasLabels = z;
        if (z) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public Column setHasLabelsOnlyForSelected(boolean z) {
        this.hasLabelsOnlyForSelected = z;
        if (z) {
            this.hasLabels = false;
        }
        return this;
    }

    public ColumnChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public Column setFormatter(ColumnChartValueFormatter columnChartValueFormatter) {
        if (columnChartValueFormatter != null) {
            this.formatter = columnChartValueFormatter;
        }
        return this;
    }
}
