package com.ashokvarma.bottomnavigation;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class BadgeItem {
    private String mBackgroundColorCode;
    private int mBackgroundColorResource;
    private String mBorderColorCode;
    private int mBorderColorResource;
    private boolean mHideOnSelect;
    private CharSequence mText;
    private String mTextColorCode;
    private int mTextColorResource;
    private WeakReference<TextView> mTextViewRef;
    private int mBackgroundColor = SupportMenu.CATEGORY_MASK;
    private int mTextColor = -1;
    private int mBorderColor = -1;
    private int mBorderWidth = 0;
    private int mGravity = 8388661;
    private boolean mIsHidden = false;
    private int mAnimationDuration = 200;

    public BadgeItem setBackgroundColorResource(@ColorRes int i) {
        this.mBackgroundColorResource = i;
        refreshDrawable();
        return this;
    }

    public BadgeItem setBackgroundColor(@Nullable String str) {
        this.mBackgroundColorCode = str;
        refreshDrawable();
        return this;
    }

    public BadgeItem setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        refreshDrawable();
        return this;
    }

    public BadgeItem setTextColorResource(@ColorRes int i) {
        this.mTextColorResource = i;
        setTextColor();
        return this;
    }

    public BadgeItem setTextColor(@Nullable String str) {
        this.mTextColorCode = str;
        setTextColor();
        return this;
    }

    public BadgeItem setTextColor(int i) {
        this.mTextColor = i;
        setTextColor();
        return this;
    }

    public BadgeItem setText(@Nullable CharSequence charSequence) {
        this.mText = charSequence;
        if (isWeakReferenceValid()) {
            TextView textView = this.mTextViewRef.get();
            if (!TextUtils.isEmpty(charSequence)) {
                textView.setText(charSequence);
            }
        }
        return this;
    }

    public BadgeItem setBorderColorResource(@ColorRes int i) {
        this.mBorderColorResource = i;
        refreshDrawable();
        return this;
    }

    public BadgeItem setBorderColor(@Nullable String str) {
        this.mBorderColorCode = str;
        refreshDrawable();
        return this;
    }

    public BadgeItem setBorderColor(int i) {
        this.mBorderColor = i;
        refreshDrawable();
        return this;
    }

    public BadgeItem setBorderWidth(int i) {
        this.mBorderWidth = i;
        refreshDrawable();
        return this;
    }

    public BadgeItem setGravity(int i) {
        this.mGravity = i;
        if (isWeakReferenceValid()) {
            TextView textView = this.mTextViewRef.get();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.gravity = i;
            textView.setLayoutParams(layoutParams);
        }
        return this;
    }

    public BadgeItem setHideOnSelect(boolean z) {
        this.mHideOnSelect = z;
        return this;
    }

    public BadgeItem setAnimationDuration(int i) {
        this.mAnimationDuration = i;
        return this;
    }

    protected BadgeItem setTextView(TextView textView) {
        this.mTextViewRef = new WeakReference<>(textView);
        return this;
    }

    protected int getBackgroundColor(Context context) {
        if (this.mBackgroundColorResource != 0) {
            return ContextCompat.getColor(context, this.mBackgroundColorResource);
        }
        if (!TextUtils.isEmpty(this.mBackgroundColorCode)) {
            return Color.parseColor(this.mBackgroundColorCode);
        }
        return this.mBackgroundColor;
    }

    protected int getTextColor(Context context) {
        if (this.mTextColorResource != 0) {
            return ContextCompat.getColor(context, this.mTextColorResource);
        }
        if (!TextUtils.isEmpty(this.mTextColorCode)) {
            return Color.parseColor(this.mTextColorCode);
        }
        return this.mTextColor;
    }

    protected CharSequence getText() {
        return this.mText;
    }

    protected int getBorderColor(Context context) {
        if (this.mBorderColorResource != 0) {
            return ContextCompat.getColor(context, this.mBorderColorResource);
        }
        if (!TextUtils.isEmpty(this.mBorderColorCode)) {
            return Color.parseColor(this.mBorderColorCode);
        }
        return this.mBorderColor;
    }

    protected int getBorderWidth() {
        return this.mBorderWidth;
    }

    protected int getGravity() {
        return this.mGravity;
    }

    protected boolean isHideOnSelect() {
        return this.mHideOnSelect;
    }

    protected WeakReference<TextView> getTextView() {
        return this.mTextViewRef;
    }

    private void refreshDrawable() {
        if (isWeakReferenceValid()) {
            TextView textView = this.mTextViewRef.get();
            textView.setBackgroundDrawable(BottomNavigationHelper.getBadgeDrawable(this, textView.getContext()));
        }
    }

    private void setTextColor() {
        if (isWeakReferenceValid()) {
            TextView textView = this.mTextViewRef.get();
            textView.setTextColor(getTextColor(textView.getContext()));
        }
    }

    private boolean isWeakReferenceValid() {
        return (this.mTextViewRef == null || this.mTextViewRef.get() == null) ? false : true;
    }

    void select() {
        if (this.mHideOnSelect) {
            hide(true);
        }
    }

    void unSelect() {
        if (this.mHideOnSelect) {
            show(true);
        }
    }

    public BadgeItem toggle() {
        return toggle(true);
    }

    public BadgeItem toggle(boolean z) {
        if (this.mIsHidden) {
            return show(z);
        }
        return hide(z);
    }

    public BadgeItem show() {
        return show(true);
    }

    public BadgeItem show(boolean z) {
        this.mIsHidden = false;
        if (isWeakReferenceValid()) {
            TextView textView = this.mTextViewRef.get();
            if (z) {
                textView.setScaleX(0.0f);
                textView.setScaleY(0.0f);
                textView.setVisibility(0);
                ViewPropertyAnimatorCompat viewPropertyAnimatorCompatAnimate = ViewCompat.animate(textView);
                viewPropertyAnimatorCompatAnimate.cancel();
                viewPropertyAnimatorCompatAnimate.setDuration(this.mAnimationDuration);
                viewPropertyAnimatorCompatAnimate.scaleX(1.0f).scaleY(1.0f);
                viewPropertyAnimatorCompatAnimate.setListener(null);
                viewPropertyAnimatorCompatAnimate.start();
            } else {
                textView.setScaleX(1.0f);
                textView.setScaleY(1.0f);
                textView.setVisibility(0);
            }
        }
        return this;
    }

    public BadgeItem hide() {
        return hide(true);
    }

    public BadgeItem hide(boolean z) {
        this.mIsHidden = true;
        if (isWeakReferenceValid()) {
            TextView textView = this.mTextViewRef.get();
            if (z) {
                ViewPropertyAnimatorCompat viewPropertyAnimatorCompatAnimate = ViewCompat.animate(textView);
                viewPropertyAnimatorCompatAnimate.cancel();
                viewPropertyAnimatorCompatAnimate.setDuration(this.mAnimationDuration);
                viewPropertyAnimatorCompatAnimate.scaleX(0.0f).scaleY(0.0f);
                viewPropertyAnimatorCompatAnimate.setListener(new ViewPropertyAnimatorListener() { // from class: com.ashokvarma.bottomnavigation.BadgeItem.1
                    @Override // android.support.v4.view.ViewPropertyAnimatorListener
                    public void onAnimationStart(View view) {
                    }

                    @Override // android.support.v4.view.ViewPropertyAnimatorListener
                    public void onAnimationEnd(View view) {
                        view.setVisibility(8);
                    }

                    @Override // android.support.v4.view.ViewPropertyAnimatorListener
                    public void onAnimationCancel(View view) {
                        view.setVisibility(8);
                    }
                });
                viewPropertyAnimatorCompatAnimate.start();
            } else {
                textView.setVisibility(8);
            }
        }
        return this;
    }

    public boolean isHidden() {
        return this.mIsHidden;
    }
}
