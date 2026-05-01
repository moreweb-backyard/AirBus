package com.shima.smartbushome.assist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class WheelView extends ScrollView {
    public static final int OFF_SET_DEFAULT = 1;
    private static final int SCROLL_DIRECTION_DOWN = 1;
    private static final int SCROLL_DIRECTION_UP = 0;
    public static final String TAG = "WheelView";
    private Context context;
    int displayItemCount;
    int initialY;
    int itemHeight;
    List<String> items;
    int newCheck;
    int offset;
    private OnWheelViewListener onWheelViewListener;
    Paint paint;
    private int scrollDirection;
    Runnable scrollerTask;
    int[] selectedAreaBorder;
    int selectedIndex;
    int viewWidth;
    private LinearLayout views;

    public static class OnWheelViewListener {
        public void onSelected(int i, String str) {
        }
    }

    public WheelView(Context context) {
        super(context);
        this.offset = 1;
        this.selectedIndex = 1;
        this.newCheck = 50;
        this.itemHeight = 0;
        this.scrollDirection = -1;
        init(context);
    }

    public WheelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.offset = 1;
        this.selectedIndex = 1;
        this.newCheck = 50;
        this.itemHeight = 0;
        this.scrollDirection = -1;
        init(context);
    }

    public WheelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.offset = 1;
        this.selectedIndex = 1;
        this.newCheck = 50;
        this.itemHeight = 0;
        this.scrollDirection = -1;
        init(context);
    }

    private List<String> getItems() {
        return this.items;
    }

    public void setItems(List<String> list) {
        if (this.items == null) {
            this.items = new ArrayList();
        }
        this.items.clear();
        this.items.addAll(list);
        this.views.removeAllViews();
        for (int i = 0; i < this.offset; i++) {
            this.items.add(0, "");
            this.items.add("");
        }
        initData();
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int i) {
        this.offset = i;
    }

    private void init(Context context) {
        this.context = context;
        Log.d(TAG, "parent: " + getParent());
        setVerticalScrollBarEnabled(false);
        this.views = new LinearLayout(context);
        this.views.setOrientation(1);
        addView(this.views);
        this.scrollerTask = new Runnable() { // from class: com.shima.smartbushome.assist.WheelView.1
            @Override // java.lang.Runnable
            public void run() {
                if (WheelView.this.initialY - WheelView.this.getScrollY() == 0) {
                    final int i = WheelView.this.initialY % WheelView.this.itemHeight;
                    final int i2 = WheelView.this.initialY / WheelView.this.itemHeight;
                    if (i == 0) {
                        WheelView.this.selectedIndex = i2 + WheelView.this.offset;
                        WheelView.this.onSeletedCallBack();
                        return;
                    } else if (i > WheelView.this.itemHeight / 2) {
                        WheelView.this.post(new Runnable() { // from class: com.shima.smartbushome.assist.WheelView.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                WheelView.this.smoothScrollTo(0, (WheelView.this.initialY - i) + WheelView.this.itemHeight);
                                WheelView.this.selectedIndex = i2 + WheelView.this.offset + 1;
                                WheelView.this.onSeletedCallBack();
                            }
                        });
                        return;
                    } else {
                        WheelView.this.post(new Runnable() { // from class: com.shima.smartbushome.assist.WheelView.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                WheelView.this.smoothScrollTo(0, WheelView.this.initialY - i);
                                WheelView.this.selectedIndex = i2 + WheelView.this.offset;
                                WheelView.this.onSeletedCallBack();
                            }
                        });
                        return;
                    }
                }
                WheelView.this.initialY = WheelView.this.getScrollY();
                WheelView.this.postDelayed(WheelView.this.scrollerTask, WheelView.this.newCheck);
            }
        };
    }

    public void startScrollerTask() {
        this.initialY = getScrollY();
        postDelayed(this.scrollerTask, this.newCheck);
    }

    private void initData() {
        this.displayItemCount = (this.offset * 2) + 1;
        Iterator<String> it = this.items.iterator();
        while (it.hasNext()) {
            this.views.addView(createView(it.next()));
        }
        refreshItemView(0);
    }

    private TextView createView(String str) {
        TextView textView = new TextView(this.context);
        textView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        textView.setSingleLine(true);
        textView.setTextSize(2, 20.0f);
        textView.setText(str);
        textView.setGravity(17);
        int iDip2px = dip2px(15.0f);
        textView.setPadding(iDip2px, iDip2px, iDip2px, iDip2px);
        if (this.itemHeight == 0) {
            this.itemHeight = getViewMeasuredHeight(textView);
            Log.d(TAG, "itemHeight: " + this.itemHeight);
            this.views.setLayoutParams(new FrameLayout.LayoutParams(-1, this.itemHeight * this.displayItemCount));
            setLayoutParams(new RelativeLayout.LayoutParams(((RelativeLayout.LayoutParams) getLayoutParams()).width, this.itemHeight * this.displayItemCount));
        }
        return textView;
    }

    @Override // android.view.View
    protected void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        refreshItemView(i2);
        if (i2 > i4) {
            this.scrollDirection = 1;
        } else {
            this.scrollDirection = 0;
        }
    }

    private void refreshItemView(int i) {
        int i2 = (i / this.itemHeight) + this.offset;
        int i3 = i % this.itemHeight;
        int i4 = i / this.itemHeight;
        if (i3 == 0) {
            i2 = this.offset + i4;
        } else if (i3 > this.itemHeight / 2) {
            i2 = i4 + this.offset + 1;
        }
        int childCount = this.views.getChildCount();
        for (int i5 = 0; i5 < childCount; i5++) {
            TextView textView = (TextView) this.views.getChildAt(i5);
            if (textView == null) {
                return;
            }
            if (i2 == i5) {
                textView.setTextColor(Color.parseColor("#6069fF"));
            } else {
                textView.setTextColor(Color.parseColor("#bbbbbb"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] obtainSelectedAreaBorder() {
        if (this.selectedAreaBorder == null) {
            this.selectedAreaBorder = new int[2];
            this.selectedAreaBorder[0] = this.itemHeight * this.offset;
            this.selectedAreaBorder[1] = this.itemHeight * (this.offset + 1);
        }
        return this.selectedAreaBorder;
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (this.viewWidth == 0) {
            this.viewWidth = ((Activity) this.context).getWindowManager().getDefaultDisplay().getWidth();
            Log.d(TAG, "viewWidth: " + this.viewWidth);
        }
        if (this.paint == null) {
            this.paint = new Paint();
            this.paint.setColor(Color.parseColor("#83cde6"));
            this.paint.setStrokeWidth(dip2px(1.0f));
        }
        super.setBackgroundDrawable(new Drawable() { // from class: com.shima.smartbushome.assist.WheelView.2
            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return 0;
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int i) {
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter colorFilter) {
            }

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                canvas.drawLine((WheelView.this.viewWidth * 1) / 6, WheelView.this.obtainSelectedAreaBorder()[0], (WheelView.this.viewWidth * 5) / 6, WheelView.this.obtainSelectedAreaBorder()[0], WheelView.this.paint);
                canvas.drawLine((WheelView.this.viewWidth * 1) / 6, WheelView.this.obtainSelectedAreaBorder()[1], (WheelView.this.viewWidth * 5) / 6, WheelView.this.obtainSelectedAreaBorder()[1], WheelView.this.paint);
            }
        });
    }

    @Override // android.widget.ScrollView, android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        Log.d(TAG, "w: " + i + ", h: " + i2 + ", oldw: " + i3 + ", oldh: " + i4);
        this.viewWidth = i;
        setBackgroundDrawable(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSeletedCallBack() {
        if (this.onWheelViewListener != null) {
            this.onWheelViewListener.onSelected(this.selectedIndex, this.items.get(this.selectedIndex));
        }
    }

    public void setSeletion(final int i) {
        this.selectedIndex = this.offset + i;
        post(new Runnable() { // from class: com.shima.smartbushome.assist.WheelView.3
            @Override // java.lang.Runnable
            public void run() {
                WheelView.this.smoothScrollTo(0, i * WheelView.this.itemHeight);
            }
        });
    }

    public String getSeletedItem() {
        return this.items.get(this.selectedIndex);
    }

    public int getSeletedIndex() {
        return this.selectedIndex - this.offset;
    }

    @Override // android.widget.ScrollView
    public void fling(int i) {
        super.fling(i / 3);
    }

    @Override // android.widget.ScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            startScrollerTask();
        }
        return super.onTouchEvent(motionEvent);
    }

    public OnWheelViewListener getOnWheelViewListener() {
        return this.onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float f) {
        return (int) ((f * this.context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
        return view.getMeasuredHeight();
    }
}
