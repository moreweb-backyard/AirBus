package com.bigkoo.pickerview.adapter;

/* loaded from: classes.dex */
public class NumericWheelAdapter implements WheelAdapter {
    public static final int DEFAULT_MAX_VALUE = 9;
    private static final int DEFAULT_MIN_VALUE = 0;
    private int maxValue;
    private int minValue;

    public NumericWheelAdapter() {
        this(0, 9);
    }

    public NumericWheelAdapter(int i, int i2) {
        this.minValue = i;
        this.maxValue = i2;
    }

    @Override // com.bigkoo.pickerview.adapter.WheelAdapter
    public Object getItem(int i) {
        if (i >= 0 && i < getItemsCount()) {
            return Integer.valueOf(this.minValue + i);
        }
        return 0;
    }

    @Override // com.bigkoo.pickerview.adapter.WheelAdapter
    public int getItemsCount() {
        return (this.maxValue - this.minValue) + 1;
    }

    @Override // com.bigkoo.pickerview.adapter.WheelAdapter
    public int indexOf(Object obj) {
        try {
            return ((Integer) obj).intValue() - this.minValue;
        } catch (Exception unused) {
            return -1;
        }
    }
}
