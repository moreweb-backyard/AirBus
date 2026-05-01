package com.bigkoo.pickerview.adapter;

import java.util.List;

/* loaded from: classes.dex */
public class ArrayWheelAdapter<T> implements WheelAdapter {
    public static final int DEFAULT_LENGTH = 4;
    private List<T> items;
    private int length;

    public ArrayWheelAdapter(List<T> list, int i) {
        this.items = list;
        this.length = i;
    }

    public ArrayWheelAdapter(List<T> list) {
        this(list, 4);
    }

    @Override // com.bigkoo.pickerview.adapter.WheelAdapter
    public Object getItem(int i) {
        return (i < 0 || i >= this.items.size()) ? "" : this.items.get(i);
    }

    @Override // com.bigkoo.pickerview.adapter.WheelAdapter
    public int getItemsCount() {
        return this.items.size();
    }

    @Override // com.bigkoo.pickerview.adapter.WheelAdapter
    public int indexOf(Object obj) {
        return this.items.indexOf(obj);
    }
}
