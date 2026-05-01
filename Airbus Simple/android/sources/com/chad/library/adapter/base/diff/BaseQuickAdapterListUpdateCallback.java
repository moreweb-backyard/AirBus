package com.chad.library.adapter.base.diff;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.ListUpdateCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;

/* loaded from: classes.dex */
public final class BaseQuickAdapterListUpdateCallback implements ListUpdateCallback {

    @NonNull
    private final BaseQuickAdapter mAdapter;

    public BaseQuickAdapterListUpdateCallback(@NonNull BaseQuickAdapter baseQuickAdapter) {
        this.mAdapter = baseQuickAdapter;
    }

    @Override // android.support.v7.util.ListUpdateCallback
    public void onInserted(int i, int i2) {
        this.mAdapter.notifyItemRangeInserted(i + this.mAdapter.getHeaderLayoutCount(), i2);
    }

    @Override // android.support.v7.util.ListUpdateCallback
    public void onRemoved(int i, int i2) {
        this.mAdapter.notifyItemRangeRemoved(i + this.mAdapter.getHeaderLayoutCount(), i2);
    }

    @Override // android.support.v7.util.ListUpdateCallback
    public void onMoved(int i, int i2) {
        this.mAdapter.notifyItemMoved(i + this.mAdapter.getHeaderLayoutCount(), i2 + this.mAdapter.getHeaderLayoutCount());
    }

    @Override // android.support.v7.util.ListUpdateCallback
    public void onChanged(int i, int i2, @Nullable Object obj) {
        this.mAdapter.notifyItemRangeChanged(i + this.mAdapter.getHeaderLayoutCount(), i2, obj);
    }
}
