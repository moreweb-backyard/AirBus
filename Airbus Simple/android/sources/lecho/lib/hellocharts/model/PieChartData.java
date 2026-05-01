package lecho.lib.hellocharts.model;

import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lecho.lib.hellocharts.formatter.PieChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimplePieChartValueFormatter;

/* loaded from: classes.dex */
public class PieChartData extends AbstractChartData {
    public static final float DEFAULT_CENTER_CIRCLE_SCALE = 0.6f;
    public static final int DEFAULT_CENTER_TEXT1_SIZE_SP = 42;
    public static final int DEFAULT_CENTER_TEXT2_SIZE_SP = 16;
    private static final int DEFAULT_SLICE_SPACING_DP = 2;
    private int centerCircleColor;
    private float centerCircleScale;
    private String centerText1;
    private int centerText1Color;
    private int centerText1FontSize;
    private Typeface centerText1Typeface;
    private String centerText2;
    private int centerText2Color;
    private int centerText2FontSize;
    private Typeface centerText2Typeface;
    private PieChartValueFormatter formatter;
    private boolean hasCenterCircle;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private boolean hasLabelsOutside;
    private int slicesSpacing;
    private List<SliceValue> values;

    public PieChartData() {
        this.centerText1FontSize = 42;
        this.centerText2FontSize = 16;
        this.centerCircleScale = 0.6f;
        this.slicesSpacing = 2;
        this.formatter = new SimplePieChartValueFormatter();
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.hasLabelsOutside = false;
        this.hasCenterCircle = false;
        this.centerCircleColor = 0;
        this.centerText1Color = ViewCompat.MEASURED_STATE_MASK;
        this.centerText2Color = ViewCompat.MEASURED_STATE_MASK;
        this.values = new ArrayList();
        setAxisXBottom(null);
        setAxisYLeft(null);
    }

    public PieChartData(List<SliceValue> list) {
        this.centerText1FontSize = 42;
        this.centerText2FontSize = 16;
        this.centerCircleScale = 0.6f;
        this.slicesSpacing = 2;
        this.formatter = new SimplePieChartValueFormatter();
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.hasLabelsOutside = false;
        this.hasCenterCircle = false;
        this.centerCircleColor = 0;
        this.centerText1Color = ViewCompat.MEASURED_STATE_MASK;
        this.centerText2Color = ViewCompat.MEASURED_STATE_MASK;
        this.values = new ArrayList();
        setValues(list);
        setAxisXBottom(null);
        setAxisYLeft(null);
    }

    public PieChartData(PieChartData pieChartData) {
        super(pieChartData);
        this.centerText1FontSize = 42;
        this.centerText2FontSize = 16;
        this.centerCircleScale = 0.6f;
        this.slicesSpacing = 2;
        this.formatter = new SimplePieChartValueFormatter();
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.hasLabelsOutside = false;
        this.hasCenterCircle = false;
        this.centerCircleColor = 0;
        this.centerText1Color = ViewCompat.MEASURED_STATE_MASK;
        this.centerText2Color = ViewCompat.MEASURED_STATE_MASK;
        this.values = new ArrayList();
        this.formatter = pieChartData.formatter;
        this.hasLabels = pieChartData.hasLabels;
        this.hasLabelsOnlyForSelected = pieChartData.hasLabelsOnlyForSelected;
        this.hasLabelsOutside = pieChartData.hasLabelsOutside;
        this.hasCenterCircle = pieChartData.hasCenterCircle;
        this.centerCircleColor = pieChartData.centerCircleColor;
        this.centerCircleScale = pieChartData.centerCircleScale;
        this.centerText1Color = pieChartData.centerText1Color;
        this.centerText1FontSize = pieChartData.centerText1FontSize;
        this.centerText1Typeface = pieChartData.centerText1Typeface;
        this.centerText1 = pieChartData.centerText1;
        this.centerText2Color = pieChartData.centerText2Color;
        this.centerText2FontSize = pieChartData.centerText2FontSize;
        this.centerText2Typeface = pieChartData.centerText2Typeface;
        this.centerText2 = pieChartData.centerText2;
        Iterator<SliceValue> it = pieChartData.values.iterator();
        while (it.hasNext()) {
            this.values.add(new SliceValue(it.next()));
        }
    }

    public static PieChartData generateDummyData() {
        PieChartData pieChartData = new PieChartData();
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new SliceValue(40.0f));
        arrayList.add(new SliceValue(20.0f));
        arrayList.add(new SliceValue(30.0f));
        arrayList.add(new SliceValue(50.0f));
        pieChartData.setValues(arrayList);
        return pieChartData;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void update(float f) {
        Iterator<SliceValue> it = this.values.iterator();
        while (it.hasNext()) {
            it.next().update(f);
        }
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void finish() {
        Iterator<SliceValue> it = this.values.iterator();
        while (it.hasNext()) {
            it.next().finish();
        }
    }

    @Override // lecho.lib.hellocharts.model.AbstractChartData, lecho.lib.hellocharts.model.ChartData
    public void setAxisXBottom(Axis axis) {
        super.setAxisXBottom(null);
    }

    @Override // lecho.lib.hellocharts.model.AbstractChartData, lecho.lib.hellocharts.model.ChartData
    public void setAxisYLeft(Axis axis) {
        super.setAxisYLeft(null);
    }

    public List<SliceValue> getValues() {
        return this.values;
    }

    public PieChartData setValues(List<SliceValue> list) {
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

    public PieChartData setHasLabels(boolean z) {
        this.hasLabels = z;
        if (z) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public PieChartData setHasLabelsOnlyForSelected(boolean z) {
        this.hasLabelsOnlyForSelected = z;
        if (z) {
            this.hasLabels = false;
        }
        return this;
    }

    public boolean hasLabelsOutside() {
        return this.hasLabelsOutside;
    }

    public PieChartData setHasLabelsOutside(boolean z) {
        this.hasLabelsOutside = z;
        return this;
    }

    public boolean hasCenterCircle() {
        return this.hasCenterCircle;
    }

    public PieChartData setHasCenterCircle(boolean z) {
        this.hasCenterCircle = z;
        return this;
    }

    public int getCenterCircleColor() {
        return this.centerCircleColor;
    }

    public PieChartData setCenterCircleColor(int i) {
        this.centerCircleColor = i;
        return this;
    }

    public float getCenterCircleScale() {
        return this.centerCircleScale;
    }

    public PieChartData setCenterCircleScale(float f) {
        this.centerCircleScale = f;
        return this;
    }

    public int getCenterText1Color() {
        return this.centerText1Color;
    }

    public PieChartData setCenterText1Color(int i) {
        this.centerText1Color = i;
        return this;
    }

    public int getCenterText1FontSize() {
        return this.centerText1FontSize;
    }

    public PieChartData setCenterText1FontSize(int i) {
        this.centerText1FontSize = i;
        return this;
    }

    public Typeface getCenterText1Typeface() {
        return this.centerText1Typeface;
    }

    public PieChartData setCenterText1Typeface(Typeface typeface) {
        this.centerText1Typeface = typeface;
        return this;
    }

    public String getCenterText1() {
        return this.centerText1;
    }

    public PieChartData setCenterText1(String str) {
        this.centerText1 = str;
        return this;
    }

    public String getCenterText2() {
        return this.centerText2;
    }

    public PieChartData setCenterText2(String str) {
        this.centerText2 = str;
        return this;
    }

    public int getCenterText2Color() {
        return this.centerText2Color;
    }

    public PieChartData setCenterText2Color(int i) {
        this.centerText2Color = i;
        return this;
    }

    public int getCenterText2FontSize() {
        return this.centerText2FontSize;
    }

    public PieChartData setCenterText2FontSize(int i) {
        this.centerText2FontSize = i;
        return this;
    }

    public Typeface getCenterText2Typeface() {
        return this.centerText2Typeface;
    }

    public PieChartData setCenterText2Typeface(Typeface typeface) {
        this.centerText2Typeface = typeface;
        return this;
    }

    public int getSlicesSpacing() {
        return this.slicesSpacing;
    }

    public PieChartData setSlicesSpacing(int i) {
        this.slicesSpacing = i;
        return this;
    }

    public PieChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public PieChartData setFormatter(PieChartValueFormatter pieChartValueFormatter) {
        if (pieChartValueFormatter != null) {
            this.formatter = pieChartValueFormatter;
        }
        return this;
    }
}
