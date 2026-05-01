package com.ashokvarma.bottomnavigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

/* loaded from: classes.dex */
class ShiftingBottomNavigationTab extends BottomNavigationTab {
    public ShiftingBottomNavigationTab(Context context) {
        super(context);
    }

    public ShiftingBottomNavigationTab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ShiftingBottomNavigationTab(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public ShiftingBottomNavigationTab(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // com.ashokvarma.bottomnavigation.BottomNavigationTab
    void init() {
        this.paddingTopActive = (int) getResources().getDimension(R.dimen.shifting_height_top_padding_active);
        this.paddingTopInActive = (int) getResources().getDimension(R.dimen.shifting_height_top_padding_inactive);
        View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.shifting_bottom_navigation_item, (ViewGroup) this, true);
        this.containerView = viewInflate.findViewById(R.id.shifting_bottom_navigation_container);
        this.labelView = (TextView) viewInflate.findViewById(R.id.shifting_bottom_navigation_title);
        this.iconView = (ImageView) viewInflate.findViewById(R.id.shifting_bottom_navigation_icon);
        this.badgeView = (TextView) viewInflate.findViewById(R.id.shifting_bottom_navigation_badge);
        super.init();
    }

    @Override // com.ashokvarma.bottomnavigation.BottomNavigationTab
    public void select(boolean z, int i) {
        super.select(z, i);
        ResizeWidthAnimation resizeWidthAnimation = new ResizeWidthAnimation(this, this.mActiveWidth);
        long j = i;
        resizeWidthAnimation.setDuration(j);
        startAnimation(resizeWidthAnimation);
        this.labelView.animate().scaleY(1.0f).scaleX(1.0f).setDuration(j).start();
    }

    @Override // com.ashokvarma.bottomnavigation.BottomNavigationTab
    public void unSelect(boolean z, int i) {
        super.unSelect(z, i);
        ResizeWidthAnimation resizeWidthAnimation = new ResizeWidthAnimation(this, this.mInActiveWidth);
        resizeWidthAnimation.setDuration(i);
        startAnimation(resizeWidthAnimation);
        this.labelView.animate().scaleY(0.0f).scaleX(0.0f).setDuration(0L).start();
    }

    public class ResizeWidthAnimation extends Animation {
        private int mStartWidth;
        private View mView;
        private int mWidth;

        @Override // android.view.animation.Animation
        public boolean willChangeBounds() {
            return true;
        }

        public ResizeWidthAnimation(View view, int i) {
            this.mView = view;
            this.mWidth = i;
            this.mStartWidth = view.getWidth();
        }

        @Override // android.view.animation.Animation
        protected void applyTransformation(float f, Transformation transformation) {
            this.mView.getLayoutParams().width = this.mStartWidth + ((int) ((this.mWidth - this.mStartWidth) * f));
            this.mView.requestLayout();
        }
    }
}
