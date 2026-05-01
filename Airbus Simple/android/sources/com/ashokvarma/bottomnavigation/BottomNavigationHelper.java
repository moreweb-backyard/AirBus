package com.ashokvarma.bottomnavigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
class BottomNavigationHelper {
    private BottomNavigationHelper() {
    }

    public static int[] getMeasurementsForFixedMode(Context context, int i, int i2, boolean z) {
        int[] iArr = new int[2];
        int dimension = (int) context.getResources().getDimension(R.dimen.fixed_min_width_small_views);
        int dimension2 = (int) context.getResources().getDimension(R.dimen.fixed_min_width);
        int i3 = i / i2;
        if (i3 < dimension && z) {
            dimension2 = (int) context.getResources().getDimension(R.dimen.fixed_min_width);
        } else if (i3 <= dimension2) {
            dimension2 = i3;
        }
        iArr[0] = dimension2;
        return iArr;
    }

    public static int[] getMeasurementsForShiftingMode(Context context, int i, int i2, boolean z) {
        int i3;
        int i4;
        int[] iArr = new int[2];
        int dimension = (int) context.getResources().getDimension(R.dimen.shifting_min_width_inactive);
        int dimension2 = (int) context.getResources().getDimension(R.dimen.shifting_max_width_inactive);
        double d = dimension;
        double d2 = i2;
        Double.isNaN(d2);
        double d3 = 0.5d + d2;
        Double.isNaN(d);
        double d4 = d * d3;
        double d5 = dimension2;
        Double.isNaN(d2);
        double d6 = 0.75d + d2;
        Double.isNaN(d5);
        double d7 = d5 * d6;
        double d8 = i;
        if (d8 < d4) {
            if (z) {
                Double.isNaN(d);
                i4 = (int) (d * 1.5d);
                i3 = dimension;
            } else {
                Double.isNaN(d8);
                i3 = (int) (d8 / d3);
                double d9 = i3;
                Double.isNaN(d9);
                i4 = (int) (d9 * 1.5d);
            }
        } else if (d8 > d7) {
            Double.isNaN(d5);
            i4 = (int) (d5 * 1.75d);
            i3 = dimension2;
        } else {
            Double.isNaN(d2);
            double d10 = d2 + 0.625d;
            Double.isNaN(d);
            double d11 = d * d10;
            Double.isNaN(d);
            double d12 = d * d6;
            Double.isNaN(d8);
            int i5 = (int) (d8 / d3);
            double d13 = i5;
            Double.isNaN(d13);
            int i6 = (int) (d13 * 1.5d);
            if (d8 > d11) {
                Double.isNaN(d8);
                int i7 = (int) (d8 / d10);
                double d14 = i7;
                Double.isNaN(d14);
                int i8 = (int) (d14 * 1.625d);
                if (d8 > d12) {
                    Double.isNaN(d8);
                    i3 = (int) (d8 / d6);
                    double d15 = i3;
                    Double.isNaN(d15);
                    i4 = (int) (d15 * 1.75d);
                } else {
                    i3 = i7;
                    i4 = i8;
                }
            } else {
                i3 = i5;
                i4 = i6;
            }
        }
        iArr[0] = i3;
        iArr[1] = i4;
        return iArr;
    }

    public static void bindTabWithData(BottomNavigationItem bottomNavigationItem, BottomNavigationTab bottomNavigationTab, BottomNavigationBar bottomNavigationBar) {
        Drawable inactiveIcon;
        Context context = bottomNavigationBar.getContext();
        bottomNavigationTab.setLabel(bottomNavigationItem.getTitle(context));
        bottomNavigationTab.setIcon(bottomNavigationItem.getIcon(context));
        int activeColor = bottomNavigationItem.getActiveColor(context);
        int inActiveColor = bottomNavigationItem.getInActiveColor(context);
        if (activeColor != -1) {
            bottomNavigationTab.setActiveColor(activeColor);
        } else {
            bottomNavigationTab.setActiveColor(bottomNavigationBar.getActiveColor());
        }
        if (inActiveColor != -1) {
            bottomNavigationTab.setInactiveColor(inActiveColor);
        } else {
            bottomNavigationTab.setInactiveColor(bottomNavigationBar.getInActiveColor());
        }
        if (bottomNavigationItem.isInActiveIconAvailable() && (inactiveIcon = bottomNavigationItem.getInactiveIcon(context)) != null) {
            bottomNavigationTab.setInactiveIcon(inactiveIcon);
        }
        bottomNavigationTab.setItemBackgroundColor(bottomNavigationBar.getBackgroundColor());
        setBadgeForTab(bottomNavigationItem.getBadgeItem(), bottomNavigationTab);
    }

    private static void setBadgeForTab(BadgeItem badgeItem, BottomNavigationTab bottomNavigationTab) {
        if (badgeItem != null) {
            Context context = bottomNavigationTab.getContext();
            bottomNavigationTab.badgeView.setBackgroundDrawable(getBadgeDrawable(badgeItem, context));
            bottomNavigationTab.setBadgeItem(badgeItem);
            badgeItem.setTextView(bottomNavigationTab.badgeView);
            bottomNavigationTab.badgeView.setVisibility(0);
            bottomNavigationTab.badgeView.setTextColor(badgeItem.getTextColor(context));
            bottomNavigationTab.badgeView.setText(badgeItem.getText());
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) bottomNavigationTab.badgeView.getLayoutParams();
            layoutParams.gravity = badgeItem.getGravity();
            bottomNavigationTab.badgeView.setLayoutParams(layoutParams);
            if (badgeItem.isHidden()) {
                badgeItem.hide();
            }
        }
    }

    static GradientDrawable getBadgeDrawable(BadgeItem badgeItem, Context context) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadius(context.getResources().getDimensionPixelSize(R.dimen.badge_corner_radius));
        gradientDrawable.setColor(badgeItem.getBackgroundColor(context));
        gradientDrawable.setStroke(badgeItem.getBorderWidth(), badgeItem.getBorderColor(context));
        return gradientDrawable;
    }

    public static void setBackgroundWithRipple(View view, final View view2, final View view3, final int i, int i2) {
        Animator animatorOfFloat;
        int x = (int) (view.getX() + (view.getMeasuredWidth() / 2));
        int measuredHeight = view.getMeasuredHeight() / 2;
        int width = view2.getWidth();
        view2.clearAnimation();
        view3.clearAnimation();
        if (Build.VERSION.SDK_INT >= 21) {
            animatorOfFloat = ViewAnimationUtils.createCircularReveal(view3, x, measuredHeight, 0.0f, width);
        } else {
            view3.setAlpha(0.0f);
            animatorOfFloat = ObjectAnimator.ofFloat(view3, "alpha", 0.0f, 1.0f);
        }
        animatorOfFloat.setDuration(i2);
        animatorOfFloat.addListener(new AnimatorListenerAdapter() { // from class: com.ashokvarma.bottomnavigation.BottomNavigationHelper.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                onCancel();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                onCancel();
            }

            private void onCancel() {
                view2.setBackgroundColor(i);
                view3.setVisibility(8);
            }
        });
        view3.setBackgroundColor(i);
        view3.setVisibility(0);
        animatorOfFloat.start();
    }
}
