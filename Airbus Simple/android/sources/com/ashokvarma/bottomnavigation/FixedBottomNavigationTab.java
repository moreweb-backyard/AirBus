package com.ashokvarma.bottomnavigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/* loaded from: classes.dex */
class FixedBottomNavigationTab extends BottomNavigationTab {
    float labelScale;

    public FixedBottomNavigationTab(Context context) {
        super(context);
    }

    public FixedBottomNavigationTab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FixedBottomNavigationTab(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public FixedBottomNavigationTab(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // com.ashokvarma.bottomnavigation.BottomNavigationTab
    void init() {
        this.paddingTopActive = (int) getResources().getDimension(R.dimen.fixed_height_top_padding_active);
        this.paddingTopInActive = (int) getResources().getDimension(R.dimen.fixed_height_top_padding_inactive);
        View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.fixed_bottom_navigation_item, (ViewGroup) this, true);
        this.containerView = viewInflate.findViewById(R.id.fixed_bottom_navigation_container);
        this.labelView = (TextView) viewInflate.findViewById(R.id.fixed_bottom_navigation_title);
        this.iconView = (ImageView) viewInflate.findViewById(R.id.fixed_bottom_navigation_icon);
        this.badgeView = (TextView) viewInflate.findViewById(R.id.fixed_bottom_navigation_badge);
        this.labelScale = getResources().getDimension(R.dimen.fixed_label_inactive) / getResources().getDimension(R.dimen.fixed_label_active);
        super.init();
    }

    @Override // com.ashokvarma.bottomnavigation.BottomNavigationTab
    public void select(boolean z, int i) {
        this.labelView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(i).start();
        super.select(z, i);
    }

    @Override // com.ashokvarma.bottomnavigation.BottomNavigationTab
    public void unSelect(boolean z, int i) {
        this.labelView.animate().scaleX(this.labelScale).scaleY(this.labelScale).setDuration(i).start();
        super.unSelect(z, i);
    }
}
