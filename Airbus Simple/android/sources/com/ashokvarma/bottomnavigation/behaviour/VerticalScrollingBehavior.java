package com.ashokvarma.bottomnavigation.behaviour;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public abstract class VerticalScrollingBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private int mConsumedScrollDirection;
    private int mPreScrollDirection;
    private int mScrollDirection;
    private int mTotalDy;
    private int mTotalDyConsumed;
    private int mTotalDyUnconsumed;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollDirection {
        public static final int SCROLL_DIRECTION_DOWN = -1;
        public static final int SCROLL_DIRECTION_UP = 1;
        public static final int SCROLL_NONE = 0;
    }

    protected abstract boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, boolean z, int i);

    public abstract void onNestedVerticalPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3);

    public abstract void onNestedVerticalScrollConsumed(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3);

    public abstract void onNestedVerticalScrollUnconsumed(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3);

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int i) {
        return (i & 2) != 0;
    }

    public VerticalScrollingBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTotalDyUnconsumed = -1;
        this.mTotalDyConsumed = -1;
        this.mTotalDy = -1;
        this.mScrollDirection = 0;
        this.mPreScrollDirection = 0;
        this.mConsumedScrollDirection = 0;
    }

    public VerticalScrollingBehavior() {
        this.mTotalDyUnconsumed = -1;
        this.mTotalDyConsumed = -1;
        this.mTotalDy = -1;
        this.mScrollDirection = 0;
        this.mPreScrollDirection = 0;
        this.mConsumedScrollDirection = 0;
    }

    public int getScrollDirection() {
        return this.mScrollDirection;
    }

    public int getConsumedScrollDirection() {
        return this.mConsumedScrollDirection;
    }

    public int getPreScrollDirection() {
        return this.mPreScrollDirection;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int i3, int i4) {
        super.onNestedScroll(coordinatorLayout, v, view, i, i2, i3, i4);
        if (i4 > 0 && this.mTotalDyUnconsumed < 0) {
            this.mTotalDyUnconsumed = 0;
            this.mScrollDirection = 1;
            onNestedVerticalScrollUnconsumed(coordinatorLayout, v, this.mScrollDirection, i2, this.mTotalDyUnconsumed);
        } else if (i4 < 0 && this.mTotalDyUnconsumed > 0) {
            this.mTotalDyUnconsumed = 0;
            this.mScrollDirection = -1;
            onNestedVerticalScrollUnconsumed(coordinatorLayout, v, this.mScrollDirection, i2, this.mTotalDyUnconsumed);
        }
        this.mTotalDyUnconsumed += i4;
        if (i2 > 0 && this.mTotalDyConsumed < 0) {
            this.mTotalDyConsumed = 0;
            this.mConsumedScrollDirection = 1;
            onNestedVerticalScrollConsumed(coordinatorLayout, v, this.mConsumedScrollDirection, i2, this.mTotalDyConsumed);
        } else if (i2 < 0 && this.mTotalDyConsumed > 0) {
            this.mTotalDyConsumed = 0;
            this.mConsumedScrollDirection = -1;
            onNestedVerticalScrollConsumed(coordinatorLayout, v, this.mConsumedScrollDirection, i2, this.mTotalDyConsumed);
        }
        this.mTotalDyConsumed += i2;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr) {
        super.onNestedPreScroll(coordinatorLayout, v, view, i, i2, iArr);
        if (i2 > 0 && this.mTotalDy < 0) {
            this.mTotalDy = 0;
            this.mPreScrollDirection = 1;
            onNestedVerticalPreScroll(coordinatorLayout, v, view, i, i2, iArr, this.mPreScrollDirection);
        } else if (i2 < 0 && this.mTotalDy > 0) {
            this.mTotalDy = 0;
            this.mPreScrollDirection = -1;
            onNestedVerticalPreScroll(coordinatorLayout, v, view, i, i2, iArr, this.mPreScrollDirection);
        }
        this.mTotalDy += i2;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, boolean z) {
        super.onNestedFling(coordinatorLayout, v, view, f, f2, z);
        return onNestedDirectionFling(coordinatorLayout, v, view, f, f2, z, f2 > 0.0f ? 1 : -1);
    }
}
