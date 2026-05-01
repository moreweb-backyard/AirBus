package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lecho.lib.hellocharts.formatter.BubbleChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleBubbleChartValueFormatter;

/* loaded from: classes.dex */
public class BubbleChartData extends AbstractChartData {
    public static final float DEFAULT_BUBBLE_SCALE = 1.0f;
    public static final int DEFAULT_MIN_BUBBLE_RADIUS_DP = 6;
    private float bubbleScale;
    private BubbleChartValueFormatter formatter;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private int minBubbleRadius;
    private List<BubbleValue> values;

    public BubbleChartData() {
        this.formatter = new SimpleBubbleChartValueFormatter();
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.minBubbleRadius = 6;
        this.bubbleScale = 1.0f;
        this.values = new ArrayList();
    }

    public BubbleChartData(List<BubbleValue> list) {
        this.formatter = new SimpleBubbleChartValueFormatter();
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.minBubbleRadius = 6;
        this.bubbleScale = 1.0f;
        this.values = new ArrayList();
        setValues(list);
    }

    public BubbleChartData(BubbleChartData bubbleChartData) {
        super(bubbleChartData);
        this.formatter = new SimpleBubbleChartValueFormatter();
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.minBubbleRadius = 6;
        this.bubbleScale = 1.0f;
        this.values = new ArrayList();
        this.formatter = bubbleChartData.formatter;
        this.hasLabels = bubbleChartData.hasLabels;
        this.hasLabelsOnlyForSelected = bubbleChartData.hasLabelsOnlyForSelected;
        this.minBubbleRadius = bubbleChartData.minBubbleRadius;
        this.bubbleScale = bubbleChartData.bubbleScale;
        Iterator<BubbleValue> it = bubbleChartData.getValues().iterator();
        while (it.hasNext()) {
            this.values.add(new BubbleValue(it.next()));
        }
    }

    public static BubbleChartData generateDummyData() {
        BubbleChartData bubbleChartData = new BubbleChartData();
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new BubbleValue(0.0f, 20.0f, 15000.0f));
        arrayList.add(new BubbleValue(3.0f, 22.0f, 20000.0f));
        arrayList.add(new BubbleValue(5.0f, 25.0f, 5000.0f));
        arrayList.add(new BubbleValue(7.0f, 30.0f, 30000.0f));
        arrayList.add(new BubbleValue(11.0f, 22.0f, 10.0f));
        bubbleChartData.setValues(arrayList);
        return bubbleChartData;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void update(float f) {
        Iterator<BubbleValue> it = this.values.iterator();
        while (it.hasNext()) {
            it.next().update(f);
        }
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void finish() {
        Iterator<BubbleValue> it = this.values.iterator();
        while (it.hasNext()) {
            it.next().finish();
        }
    }

    public List<BubbleValue> getValues() {
        return this.values;
    }

    public BubbleChartData setValues(List<BubbleValue> list) {
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

    public BubbleChartData setHasLabels(boolean z) {
        this.hasLabels = z;
        if (z) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public BubbleChartData setHasLabelsOnlyForSelected(boolean z) {
        this.hasLabelsOnlyForSelected = z;
        if (z) {
            this.hasLabels = false;
        }
        return this;
    }

    public int getMinBubbleRadius() {
        return this.minBubbleRadius;
    }

    public void setMinBubbleRadius(int i) {
        this.minBubbleRadius = i;
    }

    public float getBubbleScale() {
        return this.bubbleScale;
    }

    public void setBubbleScale(float f) {
        this.bubbleScale = f;
    }

    public BubbleChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public BubbleChartData setFormatter(BubbleChartValueFormatter bubbleChartValueFormatter) {
        if (bubbleChartValueFormatter != null) {
            this.formatter = bubbleChartValueFormatter;
        }
        return this;
    }
}
