package lecho.lib.hellocharts.model;

/* loaded from: classes.dex */
public class ComboLineColumnChartData extends AbstractChartData {
    private ColumnChartData columnChartData;
    private LineChartData lineChartData;

    public ComboLineColumnChartData() {
        this.columnChartData = new ColumnChartData();
        this.lineChartData = new LineChartData();
    }

    public ComboLineColumnChartData(ColumnChartData columnChartData, LineChartData lineChartData) {
        setColumnChartData(columnChartData);
        setLineChartData(lineChartData);
    }

    public ComboLineColumnChartData(ComboLineColumnChartData comboLineColumnChartData) {
        super(comboLineColumnChartData);
        setColumnChartData(new ColumnChartData(comboLineColumnChartData.getColumnChartData()));
        setLineChartData(new LineChartData(comboLineColumnChartData.getLineChartData()));
    }

    public static ComboLineColumnChartData generateDummyData() {
        ComboLineColumnChartData comboLineColumnChartData = new ComboLineColumnChartData();
        comboLineColumnChartData.setColumnChartData(ColumnChartData.generateDummyData());
        comboLineColumnChartData.setLineChartData(LineChartData.generateDummyData());
        return comboLineColumnChartData;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void update(float f) {
        this.columnChartData.update(f);
        this.lineChartData.update(f);
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void finish() {
        this.columnChartData.finish();
        this.lineChartData.finish();
    }

    public ColumnChartData getColumnChartData() {
        return this.columnChartData;
    }

    public void setColumnChartData(ColumnChartData columnChartData) {
        if (columnChartData == null) {
            this.columnChartData = new ColumnChartData();
        } else {
            this.columnChartData = columnChartData;
        }
    }

    public LineChartData getLineChartData() {
        return this.lineChartData;
    }

    public void setLineChartData(LineChartData lineChartData) {
        if (lineChartData == null) {
            this.lineChartData = new LineChartData();
        } else {
            this.lineChartData = lineChartData;
        }
    }
}
