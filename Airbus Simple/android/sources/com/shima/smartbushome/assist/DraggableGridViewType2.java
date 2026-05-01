package com.shima.smartbushome.assist;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Scroller;
import com.litesuits.http.data.Consts;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DraggableGridViewType2 extends ViewGroup {
    private static final long ANIMATION_DURATION = 150;
    private static final int CLOSE_ENOUGH = 2;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_COL_COUNT = 2;
    private static final int DEFAULT_GRID_GAP = 20;
    private static final int DEFAULT_ROW_COUNT = 5;
    private static final long EDGE_HOLD_DURATION = 700;
    private static final int EDGE_LFET = 0;
    private static final int EDGE_RIGHT = 1;
    private static final int INVALID_POINTER = -1;
    private static final long LONG_CLICK_DURATION = 500;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "DraggableGridViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator = new Interpolator() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.1
        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * f2 * f2 * f2 * f2) + 1.0f;
        }
    };
    private int mActivePointerId;
    private Adapter mAdapter;
    private boolean mCalledSuper;
    private int mCloseEnough;
    private int mColCount;
    private int mCurItem;
    private final DataSetObserver mDataSetObserver;
    private int mEdgeSize;
    private final Runnable mEndScrollRunnable;
    private int mFlingDistance;
    private int mGridGap;
    private int mGridHeight;
    private int mGridWidth;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private long mLastDownTime;
    private int mLastDragged;
    private int mLastEdge;
    private long mLastEdgeTime;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mLastPosition;
    private int mLastTarget;
    private int mMaxOverScrollSize;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;
    private OnPageChangeListener mOnPageChangeListener;
    private OnRearrangeListener mOnRearrangeListener;
    private int mPaddingButtom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPageCount;
    private int mPageSize;
    private int mRowCount;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private ArrayList<Integer> newPositions;

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    public interface OnRearrangeListener {
        void onRearrange(int i, int i2);
    }

    public static class SimpleOnPageChangeListener implements OnPageChangeListener {
        @Override // com.shima.smartbushome.assist.DraggableGridViewType2.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
        }

        @Override // com.shima.smartbushome.assist.DraggableGridViewType2.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
        }

        @Override // com.shima.smartbushome.assist.DraggableGridViewType2.OnPageChangeListener
        public void onPageSelected(int i) {
        }
    }

    private static void DEBUG_LOG(String str) {
    }

    public DraggableGridViewType2(Context context) {
        super(context);
        this.mColCount = 2;
        this.mRowCount = 5;
        this.mPageSize = this.mColCount * this.mRowCount;
        this.mDataSetObserver = new DataSetObserver() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.2
            @Override // android.database.DataSetObserver
            public void onChanged() {
                DraggableGridViewType2.this.dataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                DraggableGridViewType2.this.dataSetChanged();
            }
        };
        this.mActivePointerId = -1;
        this.mLastPosition = -1;
        this.mLastDownTime = Long.MAX_VALUE;
        this.mLastDragged = -1;
        this.mLastTarget = -1;
        this.mLastEdge = -1;
        this.mLastEdgeTime = Long.MAX_VALUE;
        this.newPositions = new ArrayList<>();
        this.mEndScrollRunnable = new Runnable() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.3
            @Override // java.lang.Runnable
            public void run() {
                DraggableGridViewType2.this.setScrollState(0);
            }
        };
        this.mScrollState = 0;
        initDraggableGridViewPager();
    }

    public DraggableGridViewType2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mColCount = 2;
        this.mRowCount = 5;
        this.mPageSize = this.mColCount * this.mRowCount;
        this.mDataSetObserver = new DataSetObserver() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.2
            @Override // android.database.DataSetObserver
            public void onChanged() {
                DraggableGridViewType2.this.dataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                DraggableGridViewType2.this.dataSetChanged();
            }
        };
        this.mActivePointerId = -1;
        this.mLastPosition = -1;
        this.mLastDownTime = Long.MAX_VALUE;
        this.mLastDragged = -1;
        this.mLastTarget = -1;
        this.mLastEdge = -1;
        this.mLastEdgeTime = Long.MAX_VALUE;
        this.newPositions = new ArrayList<>();
        this.mEndScrollRunnable = new Runnable() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.3
            @Override // java.lang.Runnable
            public void run() {
                DraggableGridViewType2.this.setScrollState(0);
            }
        };
        this.mScrollState = 0;
        initDraggableGridViewPager();
    }

    public DraggableGridViewType2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColCount = 2;
        this.mRowCount = 5;
        this.mPageSize = this.mColCount * this.mRowCount;
        this.mDataSetObserver = new DataSetObserver() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.2
            @Override // android.database.DataSetObserver
            public void onChanged() {
                DraggableGridViewType2.this.dataSetChanged();
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                DraggableGridViewType2.this.dataSetChanged();
            }
        };
        this.mActivePointerId = -1;
        this.mLastPosition = -1;
        this.mLastDownTime = Long.MAX_VALUE;
        this.mLastDragged = -1;
        this.mLastTarget = -1;
        this.mLastEdge = -1;
        this.mLastEdgeTime = Long.MAX_VALUE;
        this.newPositions = new ArrayList<>();
        this.mEndScrollRunnable = new Runnable() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.3
            @Override // java.lang.Runnable
            public void run() {
                DraggableGridViewType2.this.setScrollState(0);
            }
        };
        this.mScrollState = 0;
        initDraggableGridViewPager();
    }

    private void initDraggableGridViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        setChildrenDrawingOrderEnabled(true);
        Context context = getContext();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mGridGap = (int) (20.0f * f);
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingTop = getPaddingTop();
        this.mPaddingRight = getPaddingRight();
        this.mPaddingButtom = getPaddingBottom();
        super.setPadding(0, 0, 0, 0);
        this.mScroller = new Scroller(context, sInterpolator);
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration);
        this.mMinimumVelocity = (int) (400.0f * f);
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mFlingDistance = (int) (25.0f * f);
        this.mCloseEnough = (int) (f * 2.0f);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        super.onDetachedFromWindow();
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int getColCount() {
        return this.mColCount;
    }

    public void setColCount(int i) {
        if (i < 1) {
            i = 1;
        }
        this.mColCount = i;
        this.mPageSize = this.mColCount * this.mRowCount;
        requestLayout();
    }

    public int getRowCount() {
        return this.mRowCount;
    }

    public void setRowCount(int i) {
        if (i < 1) {
            i = 1;
        }
        this.mRowCount = i;
        this.mPageSize = this.mColCount * this.mRowCount;
        requestLayout();
    }

    public int getGridGap() {
        return this.mGridGap;
    }

    public void setGridGap(int i) {
        if (i < 0) {
            i = 0;
        }
        this.mGridGap = i;
        requestLayout();
    }

    public int getPageCount() {
        return ((getChildCount() + this.mPageSize) - 1) / this.mPageSize;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        this.mPageCount = ((this.mPageSize + childCount) - 1) / this.mPageSize;
        this.mGridWidth = (((getWidth() - this.mPaddingLeft) - this.mPaddingRight) - ((this.mColCount - 1) * this.mGridGap)) / this.mColCount;
        this.mGridHeight = (((getHeight() - this.mPaddingTop) - this.mPaddingButtom) - ((this.mRowCount - 1) * this.mGridGap)) / this.mRowCount;
        this.mMaxOverScrollSize = this.mGridWidth / 2;
        this.mEdgeSize = this.mGridWidth / 2;
        this.newPositions.clear();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            Rect rectByPosition = getRectByPosition(i5);
            childAt.measure(View.MeasureSpec.makeMeasureSpec(rectByPosition.width(), 1073741824), View.MeasureSpec.makeMeasureSpec(rectByPosition.height(), 1073741824));
            DEBUG_LOG("child.layout position=" + i5 + ", rect=" + rectByPosition);
            childAt.layout(rectByPosition.left, rectByPosition.top, rectByPosition.right, rectByPosition.bottom);
            this.newPositions.add(-1);
        }
        if (this.mCurItem <= 0 || this.mCurItem >= this.mPageCount) {
            return;
        }
        int i6 = this.mCurItem;
        this.mCurItem = 0;
        setCurrentItem(i6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScrollState(int i) {
        if (this.mScrollState == i) {
            return;
        }
        this.mScrollState = i;
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(i);
        }
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public void setCurrentItem(int i) {
        setCurrentItemInternal(i, false, false);
    }

    public void setCurrentItem(int i, boolean z) {
        setCurrentItemInternal(i, z, false);
    }

    void setCurrentItemInternal(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2, 0);
    }

    void setCurrentItemInternal(int i, boolean z, boolean z2, int i2) {
        if (this.mPageCount <= 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        if (!z2 && this.mCurItem == i) {
            setScrollingCacheEnabled(false);
            return;
        }
        if (i < 0) {
            i = 0;
        } else if (i >= this.mPageCount) {
            i = this.mPageCount - 1;
        }
        boolean z3 = this.mCurItem != i;
        this.mCurItem = i;
        scrollToItem(i, z, i2, z3);
    }

    private void scrollToItem(int i, boolean z, int i2, boolean z2) {
        int width = getWidth() * i;
        if (z) {
            smoothScrollTo(width, 0, i2);
            if (!z2 || this.mOnPageChangeListener == null) {
                return;
            }
            this.mOnPageChangeListener.onPageSelected(i);
            return;
        }
        if (z2 && this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(i);
        }
        completeScroll(false);
        scrollTo(width, 0);
        pageScrolled(width);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnRearrangeListener(OnRearrangeListener onRearrangeListener) {
        this.mOnRearrangeListener = onRearrangeListener;
    }

    float distanceInfluenceForSnapDuration(float f) {
        Double.isNaN(f - 0.5f);
        return (float) Math.sin((float) (r0 * 0.4712389167638204d));
    }

    void smoothScrollTo(int i, int i2) {
        smoothScrollTo(i, i2, 0);
    }

    void smoothScrollTo(int i, int i2, int i3) {
        int iAbs;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int i4 = i - scrollX;
        int i5 = i2 - scrollY;
        if (i4 == 0 && i5 == 0) {
            completeScroll(false);
            setScrollState(0);
            return;
        }
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int width = getWidth();
        int i6 = width / 2;
        float f = width;
        float f2 = i6;
        float fDistanceInfluenceForSnapDuration = f2 + (distanceInfluenceForSnapDuration(Math.min(1.0f, (Math.abs(i4) * 1.0f) / f)) * f2);
        int iAbs2 = Math.abs(i3);
        if (iAbs2 > 0) {
            iAbs = Math.round(Math.abs(fDistanceInfluenceForSnapDuration / iAbs2) * 1000.0f) * 4;
        } else {
            iAbs = (int) (((Math.abs(i4) / f) + 1.0f) * 100.0f);
        }
        this.mScroller.startScroll(scrollX, scrollY, i4, i5, Math.min(iAbs, MAX_SETTLE_DURATION));
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                scrollTo(currX, currY);
                if (!pageScrolled(currX)) {
                    this.mScroller.abortAnimation();
                    scrollTo(0, currY);
                }
            }
            ViewCompat.postInvalidateOnAnimation(this);
            return;
        }
        completeScroll(true);
    }

    private boolean pageScrolled(int i) {
        if (this.mPageCount <= 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        int width = getWidth();
        int i2 = i / width;
        int i3 = i - (i2 * width);
        this.mCalledSuper = false;
        onPageScrolled(i2, i3 / width, i3);
        if (this.mCalledSuper) {
            return true;
        }
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }

    protected void onPageScrolled(int i, float f, int i2) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(i, f, i2);
        }
        this.mCalledSuper = true;
    }

    private void completeScroll(boolean z) {
        if (this.mScrollState == 2) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                scrollTo(currX, currY);
            }
            if (z) {
                ViewCompat.postOnAnimation(this, this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 3 || action == 1) {
            DEBUG_LOG("Intercept done!");
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged || this.mLastDragged >= 0) {
                DEBUG_LOG("Intercept returning true!");
                return true;
            }
            if (this.mIsUnableToDrag) {
                DEBUG_LOG("Intercept returning false!");
                return false;
            }
        }
        if (action == 0) {
            float x = motionEvent.getX();
            this.mInitialMotionX = x;
            this.mLastMotionX = x;
            float y = motionEvent.getY();
            this.mInitialMotionY = y;
            this.mLastMotionY = y;
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
            this.mIsUnableToDrag = false;
            this.mScroller.computeScrollOffset();
            if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                this.mScroller.abortAnimation();
                this.mIsBeingDragged = true;
                requestParentDisallowInterceptTouchEvent(true);
                setScrollState(1);
            } else {
                completeScroll(false);
                this.mIsBeingDragged = false;
            }
            DEBUG_LOG("***Down at " + this.mLastMotionX + Consts.SECOND_LEVEL_SPLIT + this.mLastMotionY + " mIsBeingDragged=" + this.mIsBeingDragged + " mIsUnableToDrag=" + this.mIsUnableToDrag);
            this.mLastDragged = -1;
        } else if (action == 2) {
            int i = this.mActivePointerId;
            if (i != -1) {
                int iFindPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, i);
                float x2 = MotionEventCompat.getX(motionEvent, iFindPointerIndex);
                float f = x2 - this.mLastMotionX;
                float fAbs = Math.abs(f);
                float y2 = MotionEventCompat.getY(motionEvent, iFindPointerIndex);
                float fAbs2 = Math.abs(y2 - this.mInitialMotionY);
                DEBUG_LOG("***Moved to " + x2 + Consts.SECOND_LEVEL_SPLIT + y2 + " diff=" + fAbs + Consts.SECOND_LEVEL_SPLIT + fAbs2);
                if (fAbs > this.mTouchSlop && fAbs * 0.5f > fAbs2) {
                    DEBUG_LOG("***Starting drag!");
                    this.mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    setScrollState(1);
                    this.mLastMotionX = f > 0.0f ? this.mInitialMotionX + this.mTouchSlop : this.mInitialMotionX - this.mTouchSlop;
                    this.mLastMotionY = y2;
                    setScrollingCacheEnabled(true);
                } else if (fAbs2 > this.mTouchSlop) {
                    DEBUG_LOG("***Unable to drag!");
                    this.mIsUnableToDrag = true;
                }
                if (this.mIsBeingDragged && performDrag(x2)) {
                    ViewCompat.postInvalidateOnAnimation(this);
                }
            }
        } else if (action == 6) {
            onSecondaryPointerUp(motionEvent);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        return this.mIsBeingDragged;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean zPerformDrag = false;
        if ((motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) || this.mPageCount <= 0) {
            return false;
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (motionEvent.getAction() & 255) {
            case 0:
                this.mScroller.abortAnimation();
                float x = motionEvent.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                float y = motionEvent.getY();
                this.mInitialMotionY = y;
                this.mLastMotionY = y;
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                DEBUG_LOG("Down at " + this.mLastMotionX + Consts.SECOND_LEVEL_SPLIT + this.mLastMotionY + " mIsBeingDragged=" + this.mIsBeingDragged + " mIsUnableToDrag=" + this.mIsUnableToDrag);
                if (!this.mIsBeingDragged && this.mScrollState == 0) {
                    this.mLastPosition = getPositionByXY((int) this.mLastMotionX, (int) this.mLastMotionY);
                } else {
                    this.mLastPosition = -1;
                }
                if (this.mLastPosition >= 0) {
                    this.mLastDownTime = System.currentTimeMillis();
                } else {
                    this.mLastDownTime = Long.MAX_VALUE;
                }
                DEBUG_LOG("Down at mLastPosition=" + this.mLastPosition);
                this.mLastDragged = -1;
                break;
            case 1:
                DEBUG_LOG("Touch up!!!");
                int iFindPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                float x2 = MotionEventCompat.getX(motionEvent, iFindPointerIndex);
                float y2 = MotionEventCompat.getY(motionEvent, iFindPointerIndex);
                if (this.mLastDragged >= 0) {
                    rearrange();
                    break;
                } else if (this.mIsBeingDragged) {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                    int xVelocity = (int) VelocityTrackerCompat.getXVelocity(velocityTracker, this.mActivePointerId);
                    int width = getWidth();
                    setCurrentItemInternal(determineTargetPage(getScrollX() / width, (r3 - (r6 * width)) / width, xVelocity, (int) (x2 - this.mInitialMotionX)), true, true, xVelocity);
                    this.mActivePointerId = -1;
                    endDrag();
                    break;
                } else if (this.mLastPosition >= 0) {
                    int positionByXY = getPositionByXY((int) x2, (int) y2);
                    DEBUG_LOG("Touch up!!! currentPosition=" + positionByXY);
                    if (positionByXY == this.mLastPosition) {
                        onItemClick(positionByXY);
                        break;
                    }
                }
                break;
            case 2:
                int iFindPointerIndex2 = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                float x3 = MotionEventCompat.getX(motionEvent, iFindPointerIndex2);
                float y3 = MotionEventCompat.getY(motionEvent, iFindPointerIndex2);
                if (this.mLastDragged < 0 && !this.mIsBeingDragged) {
                    float fAbs = Math.abs(x3 - this.mLastMotionX);
                    float fAbs2 = Math.abs(y3 - this.mLastMotionY);
                    DEBUG_LOG("Moved to " + x3 + Consts.SECOND_LEVEL_SPLIT + y3 + " diff=" + fAbs + Consts.SECOND_LEVEL_SPLIT + fAbs2);
                    if (fAbs > this.mTouchSlop && fAbs > fAbs2) {
                        DEBUG_LOG("Starting drag!");
                        this.mIsBeingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                        this.mLastMotionX = x3 - this.mInitialMotionX > 0.0f ? this.mInitialMotionX + this.mTouchSlop : this.mInitialMotionX - this.mTouchSlop;
                        this.mLastMotionY = y3;
                        setScrollState(1);
                        setScrollingCacheEnabled(true);
                    }
                }
                if (this.mIsBeingDragged) {
                    zPerformDrag = false | performDrag(x3);
                    break;
                } else if (this.mLastPosition >= 0) {
                    int positionByXY2 = getPositionByXY((int) x3, (int) y3);
                    DEBUG_LOG("Moved to currentPosition=" + positionByXY2);
                    if (positionByXY2 != this.mLastPosition) {
                        this.mLastPosition = -1;
                        break;
                    } else if (System.currentTimeMillis() - this.mLastDownTime >= 500) {
                        if (onItemLongClick(positionByXY2)) {
                            performHapticFeedback(0);
                            this.mLastDragged = this.mLastPosition;
                            requestParentDisallowInterceptTouchEvent(true);
                            this.mLastTarget = -1;
                            this.mLastPosition = -1;
                        }
                        this.mLastDownTime = Long.MAX_VALUE;
                        break;
                    }
                }
                break;
            case 3:
                DEBUG_LOG("Touch cancel!!!");
                if (this.mLastDragged >= 0) {
                    rearrange();
                    break;
                } else if (this.mIsBeingDragged) {
                    scrollToItem(this.mCurItem, true, 0, false);
                    this.mActivePointerId = -1;
                    endDrag();
                    break;
                }
                break;
            case 5:
                int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                break;
            case 6:
                onSecondaryPointerUp(motionEvent);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId));
                break;
        }
        if (zPerformDrag) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    private boolean performDrag(float f) {
        float f2 = this.mLastMotionX - f;
        this.mLastMotionX = f;
        float scrollX = getScrollX() + f2;
        int width = getWidth();
        float f3 = width * 0;
        float f4 = width * (this.mPageCount - 1);
        if (scrollX < f3) {
            scrollX = f3 - Math.min(f3 - scrollX, this.mMaxOverScrollSize);
        } else if (scrollX > f4) {
            scrollX = Math.min(scrollX - f4, this.mMaxOverScrollSize) + f4;
        }
        int i = (int) scrollX;
        this.mLastMotionX += scrollX - i;
        scrollTo(i, getScrollY());
        pageScrolled(i);
        return false;
    }

    private int determineTargetPage(int i, float f, int i2, int i3) {
        if (Math.abs(i3) <= this.mFlingDistance || Math.abs(i2) <= this.mMinimumVelocity) {
            return (int) (i + f + (i >= this.mCurItem ? 0.4f : 0.6f));
        }
        return i2 > 0 ? i : i + 1;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, actionIndex) == this.mActivePointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = MotionEventCompat.getX(motionEvent, i);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean z) {
        if (this.mScrollingCacheEnabled != z) {
            this.mScrollingCacheEnabled = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dataSetChanged() {
        DEBUG_LOG("dataSetChanged");
        for (int i = 0; i < getChildCount() && i < this.mAdapter.getCount(); i++) {
            View childAt = getChildAt(i);
            View view = this.mAdapter.getView(i, childAt, this);
            if (view != childAt) {
                removeViewAt(i);
                addView(view, i);
            }
        }
        for (int childCount = getChildCount(); childCount < this.mAdapter.getCount(); childCount++) {
            addView(this.mAdapter.getView(childCount, null, this));
        }
        while (getChildCount() > this.mAdapter.getCount()) {
            removeViewAt(getChildCount() - 1);
        }
    }

    public void setAdapter(Adapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            removeAllViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                addView(this.mAdapter.getView(i, null, this));
            }
        }
    }

    private Rect getRectByPosition(int i) {
        int i2 = i / this.mPageSize;
        int i3 = (i % this.mPageSize) % this.mColCount;
        int i4 = (i % this.mPageSize) / this.mColCount;
        int width = (getWidth() * i2) + this.mPaddingLeft + (i3 * (this.mGridWidth + this.mGridGap));
        int i5 = this.mPaddingTop + (i4 * (this.mGridHeight + this.mGridGap));
        return new Rect(width, i5, this.mGridWidth + width, this.mGridHeight + i5);
    }

    private int getPositionByXY(int i, int i2) {
        int i3;
        int i4 = (i - this.mPaddingLeft) / (this.mGridWidth + this.mGridGap);
        int i5 = (i2 - this.mPaddingTop) / (this.mGridHeight + this.mGridGap);
        if (i < this.mPaddingLeft || i >= this.mPaddingLeft + ((this.mGridWidth + this.mGridGap) * i4) + this.mGridWidth || i2 < this.mPaddingTop || i2 >= this.mPaddingTop + ((this.mGridHeight + this.mGridGap) * i5) + this.mGridHeight || i4 < 0 || i4 >= this.mColCount || i5 < 0 || i5 >= this.mRowCount || (i3 = (this.mCurItem * this.mPageSize) + (i5 * this.mColCount) + i4) < 0 || i3 >= getChildCount()) {
            return -1;
        }
        return i3;
    }

    private int getTargetByXY(int i, int i2) {
        int positionByXY = getPositionByXY(i, i2);
        if (positionByXY < 0) {
            return -1;
        }
        Rect rectByPosition = getRectByPosition(positionByXY);
        int i3 = positionByXY / this.mPageSize;
        rectByPosition.inset(rectByPosition.width() / 4, rectByPosition.height() / 4);
        rectByPosition.offset((-getWidth()) * i3, 0);
        if (rectByPosition.contains(i, i2)) {
            return positionByXY;
        }
        return -1;
    }

    private void onItemClick(int i) {
        DEBUG_LOG("onItemClick position=" + i);
        if (this.mOnItemClickListener != null) {
            this.mOnItemClickListener.onItemClick(null, getChildAt(i), i, i / this.mColCount);
        }
    }

    private boolean onItemLongClick(int i) {
        DEBUG_LOG("onItemLongClick position=" + i);
        if (this.mOnItemLongClickListener != null) {
            return this.mOnItemLongClickListener.onItemLongClick(null, getChildAt(i), i, i / this.mColCount);
        }
        return false;
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i, int i2) {
        if (this.mLastDragged == -1) {
            return i2;
        }
        if (i2 == i - 1) {
            return this.mLastDragged;
        }
        return i2 >= this.mLastDragged ? i2 + 1 : i2;
    }

    private void animateDragged() {
        if (this.mLastDragged >= 0) {
            View childAt = getChildAt(this.mLastDragged);
            Rect rect = new Rect(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
            rect.inset((-rect.width()) / 20, (-rect.height()) / 20);
            childAt.measure(View.MeasureSpec.makeMeasureSpec(rect.width(), 1073741824), View.MeasureSpec.makeMeasureSpec(rect.height(), 1073741824));
            childAt.layout(rect.left, rect.top, rect.right, rect.bottom);
            AnimationSet animationSet = new AnimationSet(true);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.9091f, 1.0f, 0.9091f, 1.0f, childAt.getWidth() / 2, childAt.getHeight() / 2);
            scaleAnimation.setDuration(ANIMATION_DURATION);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
            alphaAnimation.setDuration(ANIMATION_DURATION);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.setFillEnabled(true);
            animationSet.setFillAfter(true);
            childAt.clearAnimation();
            childAt.startAnimation(animationSet);
        }
    }

    private void animateGap(int i) {
        int i2;
        int i3 = 0;
        while (i3 < getChildCount()) {
            View childAt = getChildAt(i3);
            if (i3 != this.mLastDragged) {
                if (this.mLastDragged >= i || i3 < this.mLastDragged + 1 || i3 > i) {
                    i2 = (i >= this.mLastDragged || i3 < i || i3 >= this.mLastDragged) ? i3 : i3 + 1;
                } else {
                    i2 = i3 - 1;
                }
                int iIntValue = this.newPositions.get(i3).intValue() != -1 ? this.newPositions.get(i3).intValue() : i3;
                if (iIntValue != i2) {
                    DEBUG_LOG("animateGap from=" + iIntValue + ", to=" + i2);
                    Rect rectByPosition = getRectByPosition(iIntValue);
                    Rect rectByPosition2 = getRectByPosition(i2);
                    rectByPosition.offset(-childAt.getLeft(), -childAt.getTop());
                    rectByPosition2.offset(-childAt.getLeft(), -childAt.getTop());
                    TranslateAnimation translateAnimation = new TranslateAnimation((float) rectByPosition.left, (float) rectByPosition2.left, (float) rectByPosition.top, (float) rectByPosition2.top);
                    translateAnimation.setDuration(ANIMATION_DURATION);
                    translateAnimation.setFillEnabled(true);
                    translateAnimation.setFillAfter(true);
                    childAt.clearAnimation();
                    childAt.startAnimation(translateAnimation);
                    this.newPositions.set(i3, Integer.valueOf(i2));
                }
            }
            i3++;
        }
    }

    private void rearrange() {
        if (this.mLastDragged >= 0) {
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).clearAnimation();
            }
            if (this.mLastTarget >= 0 && this.mLastDragged != this.mLastTarget) {
                View childAt = getChildAt(this.mLastDragged);
                removeViewAt(this.mLastDragged);
                addView(childAt, this.mLastTarget);
                if (this.mOnRearrangeListener != null) {
                    this.mOnRearrangeListener.onRearrange(this.mLastDragged, this.mLastTarget);
                }
            }
            this.mLastDragged = -1;
            this.mLastTarget = -1;
            requestLayout();
            invalidate();
        }
    }

    private int getEdgeByXY(int i, int i2) {
        if (i < this.mEdgeSize) {
            return 0;
        }
        return i >= getWidth() - this.mEdgeSize ? 1 : -1;
    }

    private void triggerSwipe(int i) {
        if (i == 0 && this.mCurItem > 0) {
            setCurrentItem(this.mCurItem - 1, true);
        } else {
            if (i != 1 || this.mCurItem >= this.mPageCount - 1) {
                return;
            }
            setCurrentItem(this.mCurItem + 1, true);
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCurItem = savedState.curItem;
        requestLayout();
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.curItem = this.mCurItem;
        return savedState;
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.shima.smartbushome.assist.DraggableGridViewType2.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int curItem;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.curItem = parcel.readInt();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.curItem);
        }
    }
}
