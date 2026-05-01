package com.ashokvarma.bottomnavigation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class BottomNavigationItem {
    protected boolean inActiveIconAvailable = false;
    protected int mActiveColor;
    protected String mActiveColorCode;
    protected int mActiveColorResource;
    protected BadgeItem mBadgeItem;
    protected Drawable mIcon;
    protected int mIconResource;
    protected int mInActiveColor;
    protected String mInActiveColorCode;
    protected int mInActiveColorResource;
    protected Drawable mInactiveIcon;
    protected int mInactiveIconResource;
    protected String mTitle;
    protected int mTitleResource;

    public BottomNavigationItem(@DrawableRes int i, @NonNull String str) {
        this.mIconResource = i;
        this.mTitle = str;
    }

    public BottomNavigationItem(Drawable drawable, @NonNull String str) {
        this.mIcon = drawable;
        this.mTitle = str;
    }

    public BottomNavigationItem(Drawable drawable, @StringRes int i) {
        this.mIcon = drawable;
        this.mTitleResource = i;
    }

    public BottomNavigationItem(@DrawableRes int i, @StringRes int i2) {
        this.mIconResource = i;
        this.mTitleResource = i2;
    }

    public BottomNavigationItem setInactiveIcon(Drawable drawable) {
        if (drawable != null) {
            this.mInactiveIcon = drawable;
            this.inActiveIconAvailable = true;
        }
        return this;
    }

    public BottomNavigationItem setInactiveIconResource(@DrawableRes int i) {
        this.mInactiveIconResource = i;
        this.inActiveIconAvailable = true;
        return this;
    }

    public BottomNavigationItem setActiveColorResource(@ColorRes int i) {
        this.mActiveColorResource = i;
        return this;
    }

    public BottomNavigationItem setActiveColor(@Nullable String str) {
        this.mActiveColorCode = str;
        return this;
    }

    public BottomNavigationItem setActiveColor(int i) {
        this.mActiveColor = i;
        return this;
    }

    public BottomNavigationItem setInActiveColorResource(@ColorRes int i) {
        this.mInActiveColorResource = i;
        return this;
    }

    public BottomNavigationItem setInActiveColor(@Nullable String str) {
        this.mInActiveColorCode = str;
        return this;
    }

    public BottomNavigationItem setInActiveColor(int i) {
        this.mInActiveColor = i;
        return this;
    }

    public BottomNavigationItem setBadgeItem(@Nullable BadgeItem badgeItem) {
        this.mBadgeItem = badgeItem;
        return this;
    }

    protected Drawable getIcon(Context context) {
        if (this.mIconResource != 0) {
            return ContextCompat.getDrawable(context, this.mIconResource);
        }
        return this.mIcon;
    }

    protected String getTitle(Context context) {
        if (this.mTitleResource != 0) {
            return context.getString(this.mTitleResource);
        }
        return this.mTitle;
    }

    protected Drawable getInactiveIcon(Context context) {
        if (this.mInactiveIconResource != 0) {
            return ContextCompat.getDrawable(context, this.mInactiveIconResource);
        }
        return this.mInactiveIcon;
    }

    protected boolean isInActiveIconAvailable() {
        return this.inActiveIconAvailable;
    }

    protected int getActiveColor(Context context) {
        if (this.mActiveColorResource != 0) {
            return ContextCompat.getColor(context, this.mActiveColorResource);
        }
        if (!TextUtils.isEmpty(this.mActiveColorCode)) {
            return Color.parseColor(this.mActiveColorCode);
        }
        if (this.mActiveColor != 0) {
            return this.mActiveColor;
        }
        return -1;
    }

    protected int getInActiveColor(Context context) {
        if (this.mInActiveColorResource != 0) {
            return ContextCompat.getColor(context, this.mInActiveColorResource);
        }
        if (!TextUtils.isEmpty(this.mInActiveColorCode)) {
            return Color.parseColor(this.mInActiveColorCode);
        }
        if (this.mInActiveColor != 0) {
            return this.mInActiveColor;
        }
        return -1;
    }

    protected BadgeItem getBadgeItem() {
        return this.mBadgeItem;
    }
}
