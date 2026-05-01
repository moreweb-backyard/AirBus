package com.chad.library.adapter.base.util;

import android.util.SparseArray;
import com.chad.library.adapter.base.provider.BaseItemProvider;

/* loaded from: classes.dex */
public class ProviderDelegate {
    private SparseArray<BaseItemProvider> mItemProviders = new SparseArray<>();

    public void registerProvider(BaseItemProvider baseItemProvider) {
        if (baseItemProvider == null) {
            throw new ItemProviderException("ItemProvider can not be null");
        }
        int iViewType = baseItemProvider.viewType();
        if (this.mItemProviders.get(iViewType) == null) {
            this.mItemProviders.put(iViewType, baseItemProvider);
        }
    }

    public SparseArray<BaseItemProvider> getItemProviders() {
        return this.mItemProviders;
    }
}
