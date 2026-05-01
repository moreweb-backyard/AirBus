package com.shima.smartbushome.assist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SwipeLayout extends LinearLayout {
    public static final int EXPAND = 0;
    public static final int SHRINK = 1;
    static List<SwipeLayout> swipelayouts = new ArrayList();
    float curX;
    float curY;
    Boolean isHorizontalMove;
    float lastX;
    private int mTouchSlop;
    private int rightViewWidth;
    private Scroller scroller;
    float startX;
    float startY;

    public SwipeLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(0);
        this.scroller = new Scroller(context);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.rightViewWidth = getChildAt(1).getMeasuredWidth();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            disallowParentsInterceptTouchEvent(getParent());
            this.startX = motionEvent.getX();
            this.startY = motionEvent.getY();
            this.isHorizontalMove = false;
        } else if (action == 2 && !this.isHorizontalMove.booleanValue() && this.startX > (getWidth() * 8) / 10) {
            this.curX = motionEvent.getX();
            this.curY = motionEvent.getY();
            float f = this.curX - this.startX;
            float f2 = this.curY - this.startY;
            if ((f * f) + (f2 * f2) > this.mTouchSlop * this.mTouchSlop) {
                if (Math.abs(f2) > Math.abs(f)) {
                    allowParentsInterceptTouchEvent(getParent());
                    shrinkAllView();
                } else {
                    this.isHorizontalMove = true;
                    this.lastX = this.curX;
                }
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.isHorizontalMove.booleanValue()) {
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 1:
                if (motionEvent.getX() - this.startX < 0.0f) {
                    SimulateScroll(0);
                } else {
                    SimulateScroll(1);
                }
                return false;
            case 2:
                if (this.isHorizontalMove.booleanValue() && this.startX > (getWidth() * 8) / 10) {
                    this.curX = motionEvent.getX();
                    float f = this.curX - this.lastX;
                    this.lastX = this.curX;
                    int scrollX = getScrollX() + ((int) (-f));
                    if (scrollX < 0) {
                        scrollTo(0, 0);
                    } else if (scrollX > this.rightViewWidth) {
                        scrollTo(this.rightViewWidth, 0);
                    } else {
                        scrollTo(scrollX, 0);
                    }
                }
                return false;
            default:
                return false;
        }
    }

    public void SimulateScroll(int i) {
        int scrollX;
        int i2;
        switch (i) {
            case 0:
                scrollX = this.rightViewWidth - getScrollX();
                i2 = scrollX;
                break;
            case 1:
                scrollX = 0 - getScrollX();
                i2 = scrollX;
                break;
            default:
                i2 = 0;
                break;
        }
        this.scroller.startScroll(getScrollX(), 0, i2, 0, Math.abs(i2) / 2);
        invalidate();
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.scroller.computeScrollOffset()) {
            scrollTo(this.scroller.getCurrX(), 0);
            invalidate();
        }
    }

    public static void addSwipeView(SwipeLayout swipeLayout) {
        if (swipeLayout == null) {
            return;
        }
        swipelayouts.add(swipeLayout);
    }

    public static void removeSwipeView(SwipeLayout swipeLayout) {
        if (swipeLayout == null) {
            return;
        }
        swipeLayout.SimulateScroll(1);
    }

    private void shrinkAllView() {
        for (SwipeLayout swipeLayout : swipelayouts) {
            if (swipeLayout == null) {
                swipelayouts.remove(swipeLayout);
            } else {
                swipeLayout.SimulateScroll(1);
            }
        }
    }

    private void disallowParentsInterceptTouchEvent(ViewParent viewParent) {
        if (viewParent == null) {
            return;
        }
        viewParent.requestDisallowInterceptTouchEvent(true);
        disallowParentsInterceptTouchEvent(viewParent.getParent());
    }

    private void allowParentsInterceptTouchEvent(ViewParent viewParent) {
        if (viewParent == null) {
            return;
        }
        viewParent.requestDisallowInterceptTouchEvent(false);
        allowParentsInterceptTouchEvent(viewParent.getParent());
    }
}
