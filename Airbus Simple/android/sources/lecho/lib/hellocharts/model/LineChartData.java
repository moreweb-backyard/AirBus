package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class LineChartData extends AbstractChartData {
    public static final float DEFAULT_BASE_VALUE = 0.0f;
    private float baseValue;
    private List<Line> lines;

    public LineChartData() {
        this.lines = new ArrayList();
        this.baseValue = 0.0f;
    }

    public LineChartData(List<Line> list) {
        this.lines = new ArrayList();
        this.baseValue = 0.0f;
        setLines(list);
    }

    public LineChartData(LineChartData lineChartData) {
        super(lineChartData);
        this.lines = new ArrayList();
        this.baseValue = 0.0f;
        this.baseValue = lineChartData.baseValue;
        Iterator<Line> it = lineChartData.lines.iterator();
        while (it.hasNext()) {
            this.lines.add(new Line(it.next()));
        }
    }

    public static LineChartData generateDummyData() {
        LineChartData lineChartData = new LineChartData();
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new PointValue(0.0f, 2.0f));
        arrayList.add(new PointValue(1.0f, 4.0f));
        arrayList.add(new PointValue(2.0f, 3.0f));
        arrayList.add(new PointValue(3.0f, 4.0f));
        Line line = new Line(arrayList);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList2.add(line);
        lineChartData.setLines(arrayList2);
        return lineChartData;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void update(float f) {
        Iterator<Line> it = this.lines.iterator();
        while (it.hasNext()) {
            it.next().update(f);
        }
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void finish() {
        Iterator<Line> it = this.lines.iterator();
        while (it.hasNext()) {
            it.next().finish();
        }
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public LineChartData setLines(List<Line> list) {
        if (list == null) {
            this.lines = new ArrayList();
        } else {
            this.lines = list;
        }
        return this;
    }

    public float getBaseValue() {
        return this.baseValue;
    }

    public LineChartData setBaseValue(float f) {
        this.baseValue = f;
        return this;
    }
}
