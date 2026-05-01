package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewParent;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.gesture.ChartScroller;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.renderer.ChartRenderer;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class ChartTouchHandler {
    protected Chart chart;
    protected ChartScroller chartScroller;
    protected ChartZoomer chartZoomer;
    protected ChartComputator computator;
    protected ContainerScrollType containerScrollType;
    protected GestureDetector gestureDetector;
    protected ChartRenderer renderer;
    protected ScaleGestureDetector scaleGestureDetector;
    protected ViewParent viewParent;
    protected boolean isZoomEnabled = true;
    protected boolean isScrollEnabled = true;
    protected boolean isValueTouchEnabled = true;
    protected boolean isValueSelectionEnabled = false;
    protected SelectedValue selectionModeOldValue = new SelectedValue();
    protected SelectedValue selectedValue = new SelectedValue();
    protected SelectedValue oldSelectedValue = new SelectedValue();

    public ChartTouchHandler(Context context, Chart chart) {
        this.chart = chart;
        this.computator = chart.getChartComputator();
        this.renderer = chart.getChartRenderer();
        this.gestureDetector = new GestureDetector(context, new ChartGestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ChartScaleGestureListener());
        this.chartScroller = new ChartScroller(context);
        this.chartZoomer = new ChartZoomer(context, ZoomType.HORIZONTAL_AND_VERTICAL);
    }

    public void resetTouchHandler() {
        this.computator = this.chart.getChartComputator();
        this.renderer = this.chart.getChartRenderer();
    }

    public boolean computeScroll() {
        boolean z = this.isScrollEnabled && this.chartScroller.computeScrollOffset(this.computator);
        if (this.isZoomEnabled && this.chartZoomer.computeZoom(this.computator)) {
            return true;
        }
        return z;
    }

    public boolean handleTouchEvent(MotionEvent motionEvent) {
        boolean z = this.scaleGestureDetector.onTouchEvent(motionEvent) || this.gestureDetector.onTouchEvent(motionEvent);
        if (this.isZoomEnabled && this.scaleGestureDetector.isInProgress()) {
            disallowParentInterceptTouchEvent();
        }
        return this.isValueTouchEnabled ? computeTouch(motionEvent) || z : z;
    }

    public boolean handleTouchEvent(MotionEvent motionEvent, ViewParent viewParent, ContainerScrollType containerScrollType) {
        this.viewParent = viewParent;
        this.containerScrollType = containerScrollType;
        return handleTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disallowParentInterceptTouchEvent() {
        if (this.viewParent != null) {
            this.viewParent.requestDisallowInterceptTouchEvent(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allowParentInterceptTouchEvent(ChartScroller.ScrollResult scrollResult) {
        if (this.viewParent != null) {
            if (ContainerScrollType.HORIZONTAL == this.containerScrollType && !scrollResult.canScrollX && !this.scaleGestureDetector.isInProgress()) {
                this.viewParent.requestDisallowInterceptTouchEvent(false);
            } else {
                if (ContainerScrollType.VERTICAL != this.containerScrollType || scrollResult.canScrollY || this.scaleGestureDetector.isInProgress()) {
                    return;
                }
                this.viewParent.requestDisallowInterceptTouchEvent(false);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private boolean computeTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                boolean zIsTouched = this.renderer.isTouched();
                if (zIsTouched != checkTouch(motionEvent.getX(), motionEvent.getY())) {
                    if (!this.isValueSelectionEnabled) {
                        return true;
                    }
                    this.selectionModeOldValue.clear();
                    if (!zIsTouched || this.renderer.isTouched()) {
                        return true;
                    }
                    this.chart.callTouchListener();
                    return true;
                }
                return false;
            case 1:
                if (this.renderer.isTouched()) {
                    if (checkTouch(motionEvent.getX(), motionEvent.getY())) {
                        if (this.isValueSelectionEnabled) {
                            if (this.selectionModeOldValue.equals(this.selectedValue)) {
                                return true;
                            }
                            this.selectionModeOldValue.set(this.selectedValue);
                            this.chart.callTouchListener();
                            return true;
                        }
                        this.chart.callTouchListener();
                        this.renderer.clearTouch();
                        return true;
                    }
                    this.renderer.clearTouch();
                    return true;
                }
                return false;
            case 2:
                if (this.renderer.isTouched() && !checkTouch(motionEvent.getX(), motionEvent.getY())) {
                    this.renderer.clearTouch();
                    return true;
                }
                return false;
            case 3:
                if (this.renderer.isTouched()) {
                    this.renderer.clearTouch();
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean checkTouch(float f, float f2) {
        this.oldSelectedValue.set(this.selectedValue);
        this.selectedValue.clear();
        if (this.renderer.checkTouch(f, f2)) {
            this.selectedValue.set(this.renderer.getSelectedValue());
        }
        if (this.oldSelectedValue.isSet() && this.selectedValue.isSet() && !this.oldSelectedValue.equals(this.selectedValue)) {
            return false;
        }
        return this.renderer.isTouched();
    }

    public boolean isZoomEnabled() {
        return this.isZoomEnabled;
    }

    public void setZoomEnabled(boolean z) {
        this.isZoomEnabled = z;
    }

    public boolean isScrollEnabled() {
        return this.isScrollEnabled;
    }

    public void setScrollEnabled(boolean z) {
        this.isScrollEnabled = z;
    }

    public ZoomType getZoomType() {
        return this.chartZoomer.getZoomType();
    }

    public void setZoomType(ZoomType zoomType) {
        this.chartZoomer.setZoomType(zoomType);
    }

    public boolean isValueTouchEnabled() {
        return this.isValueTouchEnabled;
    }

    public void setValueTouchEnabled(boolean z) {
        this.isValueTouchEnabled = z;
    }

    public boolean isValueSelectionEnabled() {
        return this.isValueSelectionEnabled;
    }

    public void setValueSelectionEnabled(boolean z) {
        this.isValueSelectionEnabled = z;
    }

    protected class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        protected ChartScaleGestureListener() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (!ChartTouchHandler.this.isZoomEnabled) {
                return false;
            }
            float scaleFactor = 2.0f - scaleGestureDetector.getScaleFactor();
            if (Float.isInfinite(scaleFactor)) {
                scaleFactor = 1.0f;
            }
            return ChartTouchHandler.this.chartZoomer.scale(ChartTouchHandler.this.computator, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY(), scaleFactor);
        }
    }

    protected class ChartGestureListener extends GestureDetector.SimpleOnGestureListener {
        protected ChartScroller.ScrollResult scrollResult = new ChartScroller.ScrollResult();

        protected ChartGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            if (!ChartTouchHandler.this.isScrollEnabled) {
                return false;
            }
            ChartTouchHandler.this.disallowParentInterceptTouchEvent();
            return ChartTouchHandler.this.chartScroller.startScroll(ChartTouchHandler.this.computator);
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (ChartTouchHandler.this.isZoomEnabled) {
                return ChartTouchHandler.this.chartZoomer.startZoom(motionEvent, ChartTouchHandler.this.computator);
            }
            return false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!ChartTouchHandler.this.isScrollEnabled) {
                return false;
            }
            boolean zScroll = ChartTouchHandler.this.chartScroller.scroll(ChartTouchHandler.this.computator, f, f2, this.scrollResult);
            ChartTouchHandler.this.allowParentInterceptTouchEvent(this.scrollResult);
            return zScroll;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (ChartTouchHandler.this.isScrollEnabled) {
                return ChartTouchHandler.this.chartScroller.fling((int) (-f), (int) (-f2), ChartTouchHandler.this.computator);
            }
            return false;
        }
    }
}
