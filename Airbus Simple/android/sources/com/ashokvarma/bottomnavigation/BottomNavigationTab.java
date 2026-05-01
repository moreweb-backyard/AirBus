package com.ashokvarma.bottomnavigation;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/* loaded from: classes.dex */
class BottomNavigationTab extends FrameLayout {
    protected BadgeItem badgeItem;
    TextView badgeView;
    View containerView;
    ImageView iconView;
    boolean isActive;
    protected boolean isInActiveIconSet;
    TextView labelView;
    protected int mActiveColor;
    protected int mActiveWidth;
    protected int mBackgroundColor;
    protected Drawable mCompactIcon;
    protected Drawable mCompactInActiveIcon;
    protected int mInActiveColor;
    protected int mInActiveWidth;
    protected String mLabel;
    protected int mPosition;
    protected int paddingTopActive;
    protected int paddingTopInActive;

    public BottomNavigationTab(Context context) {
        super(context);
        this.isInActiveIconSet = false;
        this.isActive = false;
        init();
    }

    public BottomNavigationTab(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isInActiveIconSet = false;
        this.isActive = false;
        init();
    }

    public BottomNavigationTab(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isInActiveIconSet = false;
        this.isActive = false;
        init();
    }

    @TargetApi(21)
    public BottomNavigationTab(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.isInActiveIconSet = false;
        this.isActive = false;
        init();
    }

    void init() {
        setLayoutParams(new ViewGroup.LayoutParams(-2, -1));
    }

    public void setActiveWidth(int i) {
        this.mActiveWidth = i;
    }

    public void setInactiveWidth(int i) {
        this.mInActiveWidth = i;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = this.mInActiveWidth;
        setLayoutParams(layoutParams);
    }

    public void setIcon(Drawable drawable) {
        this.mCompactIcon = DrawableCompat.wrap(drawable);
    }

    public void setInactiveIcon(Drawable drawable) {
        this.mCompactInActiveIcon = DrawableCompat.wrap(drawable);
        this.isInActiveIconSet = true;
    }

    public void setLabel(String str) {
        this.mLabel = str;
        this.labelView.setText(str);
    }

    public void setActiveColor(int i) {
        this.mActiveColor = i;
    }

    public int getActiveColor() {
        return this.mActiveColor;
    }

    public void setInactiveColor(int i) {
        this.mInActiveColor = i;
        this.labelView.setTextColor(i);
    }

    public void setItemBackgroundColor(int i) {
        this.mBackgroundColor = i;
    }

    public void setPosition(int i) {
        this.mPosition = i;
    }

    public void setBadgeItem(BadgeItem badgeItem) {
        this.badgeItem = badgeItem;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public void select(boolean z, int i) {
        this.isActive = true;
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(this.containerView.getPaddingTop(), this.paddingTopActive);
        valueAnimatorOfInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.ashokvarma.bottomnavigation.BottomNavigationTab.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BottomNavigationTab.this.containerView.setPadding(BottomNavigationTab.this.containerView.getPaddingLeft(), ((Integer) valueAnimator.getAnimatedValue()).intValue(), BottomNavigationTab.this.containerView.getPaddingRight(), BottomNavigationTab.this.containerView.getPaddingBottom());
            }
        });
        valueAnimatorOfInt.setDuration(i);
        valueAnimatorOfInt.start();
        this.iconView.setSelected(true);
        if (z) {
            this.labelView.setTextColor(this.mActiveColor);
        } else {
            this.labelView.setTextColor(this.mBackgroundColor);
        }
        if (this.badgeItem != null) {
            this.badgeItem.select();
        }
    }

    public void unSelect(boolean z, int i) {
        this.isActive = false;
        ValueAnimator valueAnimatorOfInt = ValueAnimator.ofInt(this.containerView.getPaddingTop(), this.paddingTopInActive);
        valueAnimatorOfInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.ashokvarma.bottomnavigation.BottomNavigationTab.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BottomNavigationTab.this.containerView.setPadding(BottomNavigationTab.this.containerView.getPaddingLeft(), ((Integer) valueAnimator.getAnimatedValue()).intValue(), BottomNavigationTab.this.containerView.getPaddingRight(), BottomNavigationTab.this.containerView.getPaddingBottom());
            }
        });
        valueAnimatorOfInt.setDuration(i);
        valueAnimatorOfInt.start();
        this.labelView.setTextColor(this.mInActiveColor);
        this.iconView.setSelected(false);
        if (this.badgeItem != null) {
            this.badgeItem.unSelect();
        }
    }

    public void initialise(boolean z) {
        this.iconView.setSelected(false);
        if (this.isInActiveIconSet) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, this.mCompactIcon);
            stateListDrawable.addState(new int[]{-16842913}, this.mCompactInActiveIcon);
            stateListDrawable.addState(new int[0], this.mCompactInActiveIcon);
            this.iconView.setImageDrawable(stateListDrawable);
            return;
        }
        if (z) {
            DrawableCompat.setTintList(this.mCompactIcon, new ColorStateList(new int[][]{new int[]{android.R.attr.state_selected}, new int[]{-16842913}, new int[0]}, new int[]{this.mActiveColor, this.mInActiveColor, this.mInActiveColor}));
        } else {
            DrawableCompat.setTintList(this.mCompactIcon, new ColorStateList(new int[][]{new int[]{android.R.attr.state_selected}, new int[]{-16842913}, new int[0]}, new int[]{this.mBackgroundColor, this.mInActiveColor, this.mInActiveColor}));
        }
        this.iconView.setImageDrawable(this.mCompactIcon);
    }
}
