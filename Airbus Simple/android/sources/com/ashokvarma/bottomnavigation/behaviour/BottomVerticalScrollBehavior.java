package com.ashokvarma.bottomnavigation.behaviour;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Interpolator;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes.dex */
public class BottomVerticalScrollBehavior<V extends View> extends VerticalScrollingBehavior<V> {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private int mBottomNavHeight;
    private WeakReference<BottomNavigationBar> mViewRef;

    @Override // com.ashokvarma.bottomnavigation.behaviour.VerticalScrollingBehavior
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2, boolean z, int i) {
        return z;
    }

    @Override // com.ashokvarma.bottomnavigation.behaviour.VerticalScrollingBehavior
    public void onNestedVerticalPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int i, int i2, int[] iArr, int i3) {
    }

    @Override // com.ashokvarma.bottomnavigation.behaviour.VerticalScrollingBehavior
    public void onNestedVerticalScrollUnconsumed(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3) {
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, final V v, int i) {
        coordinatorLayout.onLayoutChild(v, i);
        if (v instanceof BottomNavigationBar) {
            this.mViewRef = new WeakReference<>((BottomNavigationBar) v);
        }
        v.post(new Runnable() { // from class: com.ashokvarma.bottomnavigation.behaviour.BottomVerticalScrollBehavior.1
            @Override // java.lang.Runnable
            public void run() {
                BottomVerticalScrollBehavior.this.mBottomNavHeight = v.getHeight();
            }
        });
        updateSnackBarPosition(coordinatorLayout, v, getSnackBarInstance(coordinatorLayout, v));
        return super.onLayoutChild(coordinatorLayout, v, i);
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, V v, View view) {
        return isDependent(view) || super.layoutDependsOn(coordinatorLayout, v, view);
    }

    private boolean isDependent(View view) {
        return view instanceof Snackbar.SnackbarLayout;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, V v, View view) {
        if (isDependent(view)) {
            updateSnackBarPosition(coordinatorLayout, v, view);
            return false;
        }
        return super.onDependentViewChanged(coordinatorLayout, v, view);
    }

    private void updateSnackBarPosition(CoordinatorLayout coordinatorLayout, V v, View view) {
        updateSnackBarPosition(coordinatorLayout, v, view, ViewCompat.getTranslationY(v) - v.getHeight());
    }

    private void updateSnackBarPosition(CoordinatorLayout coordinatorLayout, V v, View view, float f) {
        if (view == null || !(view instanceof Snackbar.SnackbarLayout)) {
            return;
        }
        ViewCompat.animate(view).setInterpolator(INTERPOLATOR).setDuration(80L).setStartDelay(0L).translationY(f).start();
    }

    private Snackbar.SnackbarLayout getSnackBarInstance(CoordinatorLayout coordinatorLayout, V v) {
        List<View> dependencies = coordinatorLayout.getDependencies(v);
        int size = dependencies.size();
        for (int i = 0; i < size; i++) {
            View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout) {
                return (Snackbar.SnackbarLayout) view;
            }
        }
        return null;
    }

    @Override // com.ashokvarma.bottomnavigation.behaviour.VerticalScrollingBehavior
    public void onNestedVerticalScrollConsumed(CoordinatorLayout coordinatorLayout, V v, int i, int i2, int i3) {
        handleDirection(coordinatorLayout, v, i);
    }

    private void handleDirection(CoordinatorLayout coordinatorLayout, V v, int i) {
        BottomNavigationBar bottomNavigationBar = this.mViewRef.get();
        if (bottomNavigationBar == null || !bottomNavigationBar.isAutoHideEnabled()) {
            return;
        }
        if (i == -1 && bottomNavigationBar.isHidden()) {
            updateSnackBarPosition(coordinatorLayout, v, getSnackBarInstance(coordinatorLayout, v), -this.mBottomNavHeight);
            bottomNavigationBar.show();
        } else {
            if (i != 1 || bottomNavigationBar.isHidden()) {
                return;
            }
            updateSnackBarPosition(coordinatorLayout, v, getSnackBarInstance(coordinatorLayout, v), 0.0f);
            bottomNavigationBar.hide();
        }
    }
}
