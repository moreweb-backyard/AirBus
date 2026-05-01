package com.ashokvarma.bottomnavigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.ashokvarma.bottomnavigation.behaviour.BottomNavBarFabBehaviour;
import com.ashokvarma.bottomnavigation.behaviour.BottomVerticalScrollBehavior;
import com.ashokvarma.bottomnavigation.utils.Utils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;

@CoordinatorLayout.DefaultBehavior(BottomVerticalScrollBehavior.class)
/* loaded from: classes.dex */
public class BottomNavigationBar extends FrameLayout {
    public static final int BACKGROUND_STYLE_DEFAULT = 0;
    public static final int BACKGROUND_STYLE_RIPPLE = 2;
    public static final int BACKGROUND_STYLE_STATIC = 1;
    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private static final int DEFAULT_SELECTED_POSITION = -1;
    private static final int FAB_BEHAVIOUR_DISAPPEAR = 1;
    private static final int FAB_BEHAVIOUR_TRANSLATE_AND_STICK = 0;
    private static final int FAB_BEHAVIOUR_TRANSLATE_OUT = 2;
    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private static final int MAX_SIZE = 5;
    private static final int MIN_SIZE = 3;
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SHIFTING = 2;
    private int mActiveColor;
    private int mAnimationDuration;
    private boolean mAutoHideEnabled;
    private int mBackgroundColor;
    private FrameLayout mBackgroundOverlay;
    private int mBackgroundStyle;
    ArrayList<BottomNavigationItem> mBottomNavigationItems;
    ArrayList<BottomNavigationTab> mBottomNavigationTabs;
    private FrameLayout mContainer;
    private float mElevation;
    private int mFirstSelectedPosition;
    private int mInActiveColor;
    private boolean mIsHidden;
    private int mMode;
    private int mRippleAnimationDuration;
    private boolean mScrollable;
    private int mSelectedPosition;
    private LinearLayout mTabContainer;
    private OnTabSelectedListener mTabSelectedListener;
    private ViewPropertyAnimatorCompat mTranslationAnimator;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BackgroundStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FabBehaviour {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public interface OnTabSelectedListener {
        void onTabReselected(int i);

        void onTabSelected(int i);

        void onTabUnselected(int i);
    }

    public static class SimpleOnTabSelectedListener implements OnTabSelectedListener {
        @Override // com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener
        public void onTabReselected(int i) {
        }

        @Override // com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener
        public void onTabSelected(int i) {
        }

        @Override // com.ashokvarma.bottomnavigation.BottomNavigationBar.OnTabSelectedListener
        public void onTabUnselected(int i) {
        }
    }

    public BottomNavigationBar(Context context) {
        this(context, null);
    }

    public BottomNavigationBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BottomNavigationBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMode = 0;
        this.mBackgroundStyle = 0;
        this.mScrollable = false;
        this.mBottomNavigationItems = new ArrayList<>();
        this.mBottomNavigationTabs = new ArrayList<>();
        this.mSelectedPosition = -1;
        this.mFirstSelectedPosition = 0;
        this.mAnimationDuration = 200;
        this.mRippleAnimationDuration = 500;
        this.mIsHidden = false;
        parseAttrs(context, attributeSet);
        init();
    }

    @TargetApi(21)
    public BottomNavigationBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mMode = 0;
        this.mBackgroundStyle = 0;
        this.mScrollable = false;
        this.mBottomNavigationItems = new ArrayList<>();
        this.mBottomNavigationTabs = new ArrayList<>();
        this.mSelectedPosition = -1;
        this.mFirstSelectedPosition = 0;
        this.mAnimationDuration = 200;
        this.mRippleAnimationDuration = 500;
        this.mIsHidden = false;
        parseAttrs(context, attributeSet);
        init();
    }

    private void parseAttrs(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.BottomNavigationBar, 0, 0);
            this.mActiveColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BottomNavigationBar_bnbActiveColor, Utils.fetchContextColor(context, R.attr.colorAccent));
            this.mInActiveColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BottomNavigationBar_bnbInactiveColor, -3355444);
            this.mBackgroundColor = typedArrayObtainStyledAttributes.getColor(R.styleable.BottomNavigationBar_bnbBackgroundColor, -1);
            this.mAutoHideEnabled = typedArrayObtainStyledAttributes.getBoolean(R.styleable.BottomNavigationBar_bnbAutoHideEnabled, true);
            this.mElevation = typedArrayObtainStyledAttributes.getDimension(R.styleable.BottomNavigationBar_bnbElevation, getResources().getDimension(R.dimen.bottom_navigation_elevation));
            setAnimationDuration(typedArrayObtainStyledAttributes.getInt(R.styleable.BottomNavigationBar_bnbAnimationDuration, 200));
            switch (typedArrayObtainStyledAttributes.getInt(R.styleable.BottomNavigationBar_bnbMode, 0)) {
                case 1:
                    this.mMode = 1;
                    break;
                case 2:
                    this.mMode = 2;
                    break;
                default:
                    this.mMode = 0;
                    break;
            }
            switch (typedArrayObtainStyledAttributes.getInt(R.styleable.BottomNavigationBar_bnbBackgroundStyle, 0)) {
                case 1:
                    this.mBackgroundStyle = 1;
                    break;
                case 2:
                    this.mBackgroundStyle = 2;
                    break;
                default:
                    this.mBackgroundStyle = 0;
                    break;
            }
            typedArrayObtainStyledAttributes.recycle();
            return;
        }
        this.mActiveColor = Utils.fetchContextColor(context, R.attr.colorAccent);
        this.mInActiveColor = -3355444;
        this.mBackgroundColor = -1;
        this.mElevation = getResources().getDimension(R.dimen.bottom_navigation_elevation);
    }

    private void init() {
        setLayoutParams(new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(-1, -2)));
        View viewInflate = LayoutInflater.from(getContext()).inflate(R.layout.bottom_navigation_bar_container, (ViewGroup) this, true);
        this.mBackgroundOverlay = (FrameLayout) viewInflate.findViewById(R.id.bottom_navigation_bar_overLay);
        this.mContainer = (FrameLayout) viewInflate.findViewById(R.id.bottom_navigation_bar_container);
        this.mTabContainer = (LinearLayout) viewInflate.findViewById(R.id.bottom_navigation_bar_item_container);
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
        ViewCompat.setElevation(this, this.mElevation);
        setClipToPadding(false);
    }

    public BottomNavigationBar addItem(BottomNavigationItem bottomNavigationItem) {
        this.mBottomNavigationItems.add(bottomNavigationItem);
        return this;
    }

    public BottomNavigationBar removeItem(BottomNavigationItem bottomNavigationItem) {
        this.mBottomNavigationItems.remove(bottomNavigationItem);
        return this;
    }

    public BottomNavigationBar setMode(int i) {
        this.mMode = i;
        return this;
    }

    public BottomNavigationBar setBackgroundStyle(int i) {
        this.mBackgroundStyle = i;
        return this;
    }

    public BottomNavigationBar setActiveColor(@ColorRes int i) {
        this.mActiveColor = ContextCompat.getColor(getContext(), i);
        return this;
    }

    public BottomNavigationBar setActiveColor(String str) {
        this.mActiveColor = Color.parseColor(str);
        return this;
    }

    public BottomNavigationBar setInActiveColor(@ColorRes int i) {
        this.mInActiveColor = ContextCompat.getColor(getContext(), i);
        return this;
    }

    public BottomNavigationBar setInActiveColor(String str) {
        this.mInActiveColor = Color.parseColor(str);
        return this;
    }

    public BottomNavigationBar setBarBackgroundColor(@ColorRes int i) {
        this.mBackgroundColor = ContextCompat.getColor(getContext(), i);
        return this;
    }

    public BottomNavigationBar setBarBackgroundColor(String str) {
        this.mBackgroundColor = Color.parseColor(str);
        return this;
    }

    public BottomNavigationBar setFirstSelectedPosition(int i) {
        this.mFirstSelectedPosition = i;
        return this;
    }

    private BottomNavigationBar setScrollable(boolean z) {
        this.mScrollable = z;
        return this;
    }

    public void initialise() {
        this.mSelectedPosition = -1;
        this.mBottomNavigationTabs.clear();
        if (this.mBottomNavigationItems.isEmpty()) {
            return;
        }
        this.mTabContainer.removeAllViews();
        if (this.mMode == 0) {
            if (this.mBottomNavigationItems.size() <= 3) {
                this.mMode = 1;
            } else {
                this.mMode = 2;
            }
        }
        if (this.mBackgroundStyle == 0) {
            if (this.mMode == 1) {
                this.mBackgroundStyle = 1;
            } else {
                this.mBackgroundStyle = 2;
            }
        }
        if (this.mBackgroundStyle == 1) {
            this.mBackgroundOverlay.setVisibility(8);
            this.mContainer.setBackgroundColor(this.mBackgroundColor);
        }
        int screenWidth = Utils.getScreenWidth(getContext());
        if (this.mMode == 1) {
            int i = BottomNavigationHelper.getMeasurementsForFixedMode(getContext(), screenWidth, this.mBottomNavigationItems.size(), this.mScrollable)[0];
            Iterator<BottomNavigationItem> it = this.mBottomNavigationItems.iterator();
            while (it.hasNext()) {
                setUpTab(new FixedBottomNavigationTab(getContext()), it.next(), i, i);
            }
        } else if (this.mMode == 2) {
            int[] measurementsForShiftingMode = BottomNavigationHelper.getMeasurementsForShiftingMode(getContext(), screenWidth, this.mBottomNavigationItems.size(), this.mScrollable);
            int i2 = measurementsForShiftingMode[0];
            int i3 = measurementsForShiftingMode[1];
            Iterator<BottomNavigationItem> it2 = this.mBottomNavigationItems.iterator();
            while (it2.hasNext()) {
                setUpTab(new ShiftingBottomNavigationTab(getContext()), it2.next(), i2, i3);
            }
        }
        if (this.mBottomNavigationTabs.size() > this.mFirstSelectedPosition) {
            selectTabInternal(this.mFirstSelectedPosition, true, false, false);
        } else {
            if (this.mBottomNavigationTabs.isEmpty()) {
                return;
            }
            selectTabInternal(0, true, false, false);
        }
    }

    public BottomNavigationBar setTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        this.mTabSelectedListener = onTabSelectedListener;
        return this;
    }

    public BottomNavigationBar setAnimationDuration(int i) {
        this.mAnimationDuration = i;
        double d = i;
        Double.isNaN(d);
        this.mRippleAnimationDuration = (int) (d * 2.5d);
        return this;
    }

    public void clearAll() {
        this.mTabContainer.removeAllViews();
        this.mBottomNavigationTabs.clear();
        this.mBottomNavigationItems.clear();
        this.mBackgroundOverlay.setVisibility(8);
        this.mContainer.setBackgroundColor(0);
        this.mSelectedPosition = -1;
    }

    public void selectTab(int i) {
        selectTab(i, true);
    }

    public void selectTab(int i, boolean z) {
        selectTabInternal(i, false, z, z);
    }

    private void setUpTab(BottomNavigationTab bottomNavigationTab, BottomNavigationItem bottomNavigationItem, int i, int i2) {
        bottomNavigationTab.setInactiveWidth(i);
        bottomNavigationTab.setActiveWidth(i2);
        bottomNavigationTab.setPosition(this.mBottomNavigationItems.indexOf(bottomNavigationItem));
        bottomNavigationTab.setOnClickListener(new View.OnClickListener() { // from class: com.ashokvarma.bottomnavigation.BottomNavigationBar.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BottomNavigationBar.this.selectTabInternal(((BottomNavigationTab) view).getPosition(), false, true, false);
            }
        });
        this.mBottomNavigationTabs.add(bottomNavigationTab);
        BottomNavigationHelper.bindTabWithData(bottomNavigationItem, bottomNavigationTab, this);
        bottomNavigationTab.initialise(this.mBackgroundStyle == 1);
        this.mTabContainer.addView(bottomNavigationTab);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectTabInternal(int i, boolean z, boolean z2, boolean z3) {
        int i2 = this.mSelectedPosition;
        if (this.mSelectedPosition != i) {
            if (this.mBackgroundStyle == 1) {
                if (this.mSelectedPosition != -1) {
                    this.mBottomNavigationTabs.get(this.mSelectedPosition).unSelect(true, this.mAnimationDuration);
                }
                this.mBottomNavigationTabs.get(i).select(true, this.mAnimationDuration);
            } else if (this.mBackgroundStyle == 2) {
                if (this.mSelectedPosition != -1) {
                    this.mBottomNavigationTabs.get(this.mSelectedPosition).unSelect(false, this.mAnimationDuration);
                }
                this.mBottomNavigationTabs.get(i).select(false, this.mAnimationDuration);
                final BottomNavigationTab bottomNavigationTab = this.mBottomNavigationTabs.get(i);
                if (z) {
                    this.mContainer.setBackgroundColor(bottomNavigationTab.getActiveColor());
                    this.mBackgroundOverlay.setVisibility(8);
                } else {
                    this.mBackgroundOverlay.post(new Runnable() { // from class: com.ashokvarma.bottomnavigation.BottomNavigationBar.2
                        @Override // java.lang.Runnable
                        public void run() {
                            BottomNavigationHelper.setBackgroundWithRipple(bottomNavigationTab, BottomNavigationBar.this.mContainer, BottomNavigationBar.this.mBackgroundOverlay, bottomNavigationTab.getActiveColor(), BottomNavigationBar.this.mRippleAnimationDuration);
                        }
                    });
                }
            }
            this.mSelectedPosition = i;
        }
        if (z2) {
            sendListenerCall(i2, i, z3);
        }
    }

    private void sendListenerCall(int i, int i2, boolean z) {
        if (this.mTabSelectedListener != null) {
            if (z) {
                this.mTabSelectedListener.onTabSelected(i2);
                return;
            }
            if (i == i2) {
                this.mTabSelectedListener.onTabReselected(i2);
                return;
            }
            this.mTabSelectedListener.onTabSelected(i2);
            if (i != -1) {
                this.mTabSelectedListener.onTabUnselected(i);
            }
        }
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean z) {
        if (this.mIsHidden) {
            show(z);
        } else {
            hide(z);
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean z) {
        this.mIsHidden = true;
        setTranslationY(getHeight(), z);
    }

    public void show() {
        show(true);
    }

    public void show(boolean z) {
        this.mIsHidden = false;
        setTranslationY(0, z);
    }

    private void setTranslationY(int i, boolean z) {
        if (z) {
            animateOffset(i);
            return;
        }
        if (this.mTranslationAnimator != null) {
            this.mTranslationAnimator.cancel();
        }
        setTranslationY(i);
    }

    private void animateOffset(int i) {
        if (this.mTranslationAnimator == null) {
            this.mTranslationAnimator = ViewCompat.animate(this);
            this.mTranslationAnimator.setDuration(this.mRippleAnimationDuration);
            this.mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            this.mTranslationAnimator.cancel();
        }
        this.mTranslationAnimator.translationY(i).start();
    }

    public boolean isHidden() {
        return this.mIsHidden;
    }

    public boolean isAutoHideEnabled() {
        return this.mAutoHideEnabled;
    }

    public void setAutoHideEnabled(boolean z) {
        this.mAutoHideEnabled = z;
    }

    public void setFab(FloatingActionButton floatingActionButton) {
        ViewGroup.LayoutParams layoutParams = floatingActionButton.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            return;
        }
        ((CoordinatorLayout.LayoutParams) layoutParams).setBehavior(new BottomNavBarFabBehaviour());
    }

    private void setFab(FloatingActionButton floatingActionButton, int i) {
        ViewGroup.LayoutParams layoutParams = floatingActionButton.getLayoutParams();
        if (layoutParams == null || !(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            return;
        }
        ((CoordinatorLayout.LayoutParams) layoutParams).setBehavior(new BottomNavBarFabBehaviour());
    }

    public int getActiveColor() {
        return this.mActiveColor;
    }

    public int getInActiveColor() {
        return this.mInActiveColor;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public int getCurrentSelectedPosition() {
        return this.mSelectedPosition;
    }

    public int getAnimationDuration() {
        return this.mAnimationDuration;
    }
}
