package com.chad.library.adapter.base;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.util.SparseIntArray;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.SectionMultiEntity;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseSectionMultiItemQuickAdapter<T extends SectionMultiEntity, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {
    private static final int DEFAULT_VIEW_TYPE = -255;
    protected static final int SECTION_HEADER_VIEW = 1092;
    public static final int TYPE_NOT_FOUND = -404;
    private SparseIntArray layouts;
    protected int mSectionHeadResId;

    protected abstract void convertHead(K k, T t);

    public BaseSectionMultiItemQuickAdapter(int i, List<T> list) {
        super(list);
        this.mSectionHeadResId = i;
    }

    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    protected int getDefItemViewType(int i) {
        T t = this.mData.get(i);
        return t != null ? t.isHeader ? SECTION_HEADER_VIEW : t.getItemType() : DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int i) {
        addItemType(DEFAULT_VIEW_TYPE, i);
    }

    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    protected K onCreateDefViewHolder(ViewGroup viewGroup, int i) {
        if (i == SECTION_HEADER_VIEW) {
            return createBaseViewHolder(getItemView(this.mSectionHeadResId, viewGroup));
        }
        return createBaseViewHolder(viewGroup, getLayoutId(i));
    }

    private int getLayoutId(int i) {
        return this.layouts.get(i, -404);
    }

    protected void addItemType(int i, @LayoutRes int i2) {
        if (this.layouts == null) {
            this.layouts = new SparseIntArray();
        }
        this.layouts.put(i, i2);
    }

    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    protected boolean isFixedViewType(int i) {
        return super.isFixedViewType(i) || i == SECTION_HEADER_VIEW;
    }

    @Override // com.chad.library.adapter.base.BaseQuickAdapter, android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(K k, int i) {
        if (k.getItemViewType() == SECTION_HEADER_VIEW) {
            setFullSpan(k);
            convertHead(k, getItem(i - getHeaderLayoutCount()));
        } else {
            super.onBindViewHolder((BaseSectionMultiItemQuickAdapter<T, K>) k, i);
        }
    }

    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void remove(@IntRange(from = 0) int i) {
        if (this.mData == null || i < 0 || i >= this.mData.size()) {
            return;
        }
        T t = this.mData.get(i);
        if (t instanceof IExpandable) {
            removeAllChild((IExpandable) t, i);
        }
        removeDataFromParent(t);
        super.remove(i);
    }

    protected void removeAllChild(IExpandable iExpandable, int i) {
        List subItems;
        if (!iExpandable.isExpanded() || (subItems = iExpandable.getSubItems()) == null || subItems.size() == 0) {
            return;
        }
        int size = subItems.size();
        for (int i2 = 0; i2 < size; i2++) {
            remove(i + 1);
        }
    }

    protected void removeDataFromParent(T t) {
        int parentPosition = getParentPosition(t);
        if (parentPosition >= 0) {
            ((IExpandable) this.mData.get(parentPosition)).getSubItems().remove(t);
        }
    }
}
