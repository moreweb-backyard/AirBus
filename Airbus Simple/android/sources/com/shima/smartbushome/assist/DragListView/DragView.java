package com.shima.smartbushome.assist.DragListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MarcoItemAdapter;
import com.shima.smartbushome.database.Savemarco;

/* loaded from: classes.dex */
public class DragView extends ListView {
    private int downScrollBounce;
    private int dragOffset;
    private int dragPoint;
    private int dragPosition;
    private int dragSrcPosition;
    private ImageView imageView;
    private WindowManager.LayoutParams layoutParams;
    Context rootcontext;
    private int scaledTouchSlop;
    private int upScrollBounce;
    private WindowManager windowManager;

    public DragView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.windowManager = null;
        this.layoutParams = null;
        this.rootcontext = context;
        this.scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int iPointToPosition = pointToPosition(x, y);
            this.dragPosition = iPointToPosition;
            this.dragSrcPosition = iPointToPosition;
            if (this.dragPosition == -1) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            ViewGroup viewGroup = (ViewGroup) getChildAt(this.dragPosition - getFirstVisiblePosition());
            this.dragPoint = y - viewGroup.getTop();
            this.dragOffset = (int) (motionEvent.getRawY() - y);
            View viewFindViewById = viewGroup.findViewById(R.id.marcoitem_type);
            if (viewFindViewById == null || x >= viewFindViewById.getRight() + 20) {
                return false;
            }
            this.upScrollBounce = Math.min(y - this.scaledTouchSlop, getHeight() / 3);
            this.downScrollBounce = Math.max(this.scaledTouchSlop + y, (getHeight() * 2) / 3);
            viewGroup.setDrawingCacheEnabled(true);
            startDrag(Bitmap.createBitmap(viewGroup.getDrawingCache()), y);
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.imageView != null && this.dragPosition != -1) {
            switch (motionEvent.getAction()) {
                case 1:
                    int y = (int) motionEvent.getY();
                    stopDrag();
                    onDrop(y);
                    return true;
                case 2:
                    onDrag((int) motionEvent.getY());
                    return true;
                default:
                    return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void startDrag(Bitmap bitmap, int i) {
        stopDrag();
        this.layoutParams = new WindowManager.LayoutParams();
        this.layoutParams.gravity = 48;
        this.layoutParams.x = 0;
        this.layoutParams.y = (i - this.dragPoint) + this.dragOffset;
        this.layoutParams.width = -2;
        this.layoutParams.height = -2;
        this.layoutParams.flags = 408;
        this.layoutParams.format = -3;
        this.layoutParams.windowAnimations = 0;
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bitmap);
        Context context = getContext();
        Context context2 = this.rootcontext;
        this.windowManager = (WindowManager) context.getSystemService("window");
        this.windowManager.addView(imageView, this.layoutParams);
        this.imageView = imageView;
    }

    public void stopDrag() {
        if (this.imageView != null) {
            this.windowManager.removeView(this.imageView);
            this.imageView = null;
        }
    }

    public void onDrag(int i) {
        if (this.imageView != null) {
            this.layoutParams.alpha = 0.8f;
            this.layoutParams.y = (i - this.dragPoint) + this.dragOffset;
            this.windowManager.updateViewLayout(this.imageView, this.layoutParams);
        }
        int i2 = 0;
        int iPointToPosition = pointToPosition(0, i);
        if (iPointToPosition != -1) {
            this.dragPosition = iPointToPosition;
        }
        if (i < this.upScrollBounce) {
            i2 = 8;
        } else if (i > this.downScrollBounce) {
            i2 = -8;
        }
        if (i2 != 0) {
            setSelectionFromTop(this.dragPosition, getChildAt(this.dragPosition - getFirstVisiblePosition()).getTop() + i2);
        }
    }

    public void onDrop(int i) {
        int iPointToPosition = pointToPosition(0, i);
        if (iPointToPosition != -1) {
            this.dragPosition = iPointToPosition;
        }
        if (i < getChildAt(0).getTop()) {
            this.dragPosition = 0;
        } else if (i > getChildAt(getChildCount() - 1).getBottom()) {
            this.dragPosition = getAdapter().getCount() - 1;
        }
        if (this.dragPosition < 0 || this.dragPosition >= getAdapter().getCount()) {
            return;
        }
        MarcoItemAdapter marcoItemAdapter = (MarcoItemAdapter) getAdapter();
        marcoItemAdapter.update((Savemarco) marcoItemAdapter.getItem(this.dragSrcPosition), this.dragPosition);
    }
}
