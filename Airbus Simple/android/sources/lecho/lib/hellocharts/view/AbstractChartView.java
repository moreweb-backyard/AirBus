package lecho.lib.hellocharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.animation.ChartDataAnimator;
import lecho.lib.hellocharts.animation.ChartDataAnimatorV14;
import lecho.lib.hellocharts.animation.ChartDataAnimatorV8;
import lecho.lib.hellocharts.animation.ChartViewportAnimator;
import lecho.lib.hellocharts.animation.ChartViewportAnimatorV14;
import lecho.lib.hellocharts.animation.ChartViewportAnimatorV8;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.gesture.ChartTouchHandler;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.renderer.AxesRenderer;
import lecho.lib.hellocharts.renderer.ChartRenderer;
import lecho.lib.hellocharts.util.ChartUtils;

/* loaded from: classes.dex */
public abstract class AbstractChartView extends View implements Chart {
    protected AxesRenderer axesRenderer;
    protected ChartComputator chartComputator;
    protected ChartRenderer chartRenderer;
    protected ContainerScrollType containerScrollType;
    protected ChartDataAnimator dataAnimator;
    protected boolean isContainerScrollEnabled;
    protected boolean isInteractive;
    protected ChartTouchHandler touchHandler;
    protected ChartViewportAnimator viewportAnimator;

    public AbstractChartView(Context context) {
        this(context, null, 0);
    }

    public AbstractChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AbstractChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isInteractive = true;
        this.isContainerScrollEnabled = false;
        this.chartComputator = new ChartComputator();
        this.touchHandler = new ChartTouchHandler(context, this);
        this.axesRenderer = new AxesRenderer(context, this);
        if (Build.VERSION.SDK_INT < 14) {
            this.dataAnimator = new ChartDataAnimatorV8(this);
            this.viewportAnimator = new ChartViewportAnimatorV8(this);
        } else {
            this.viewportAnimator = new ChartViewportAnimatorV14(this);
            this.dataAnimator = new ChartDataAnimatorV14(this);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.chartComputator.setContentRect(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        this.chartRenderer.onChartSizeChanged();
        this.axesRenderer.onChartSizeChanged();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled()) {
            this.axesRenderer.drawInBackground(canvas);
            int iSave = canvas.save();
            canvas.clipRect(this.chartComputator.getContentRectMinusAllMargins());
            this.chartRenderer.draw(canvas);
            canvas.restoreToCount(iSave);
            this.chartRenderer.drawUnclipped(canvas);
            this.axesRenderer.drawInForeground(canvas);
            return;
        }
        canvas.drawColor(ChartUtils.DEFAULT_COLOR);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean zHandleTouchEvent;
        super.onTouchEvent(motionEvent);
        if (!this.isInteractive) {
            return false;
        }
        if (this.isContainerScrollEnabled) {
            zHandleTouchEvent = this.touchHandler.handleTouchEvent(motionEvent, getParent(), this.containerScrollType);
        } else {
            zHandleTouchEvent = this.touchHandler.handleTouchEvent(motionEvent);
        }
        if (!zHandleTouchEvent) {
            return true;
        }
        ViewCompat.postInvalidateOnAnimation(this);
        return true;
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.isInteractive && this.touchHandler.computeScroll()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void startDataAnimation() {
        this.dataAnimator.startAnimation(Long.MIN_VALUE);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void startDataAnimation(long j) {
        this.dataAnimator.startAnimation(j);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void cancelDataAnimation() {
        this.dataAnimator.cancelAnimation();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void animationDataUpdate(float f) {
        getChartData().update(f);
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void animationDataFinished() {
        getChartData().finish();
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setDataAnimationListener(ChartAnimationListener chartAnimationListener) {
        this.dataAnimator.setChartAnimationListener(chartAnimationListener);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setViewportAnimationListener(ChartAnimationListener chartAnimationListener) {
        this.viewportAnimator.setChartAnimationListener(chartAnimationListener);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setViewportChangeListener(ViewportChangeListener viewportChangeListener) {
        this.chartComputator.setViewportChangeListener(viewportChangeListener);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartRenderer getChartRenderer() {
        return this.chartRenderer;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setChartRenderer(ChartRenderer chartRenderer) {
        this.chartRenderer = chartRenderer;
        resetRendererAndTouchHandler();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public AxesRenderer getAxesRenderer() {
        return this.axesRenderer;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartComputator getChartComputator() {
        return this.chartComputator;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartTouchHandler getTouchHandler() {
        return this.touchHandler;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isInteractive() {
        return this.isInteractive;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setInteractive(boolean z) {
        this.isInteractive = z;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isZoomEnabled() {
        return this.touchHandler.isZoomEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomEnabled(boolean z) {
        this.touchHandler.setZoomEnabled(z);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isScrollEnabled() {
        return this.touchHandler.isScrollEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setScrollEnabled(boolean z) {
        this.touchHandler.setScrollEnabled(z);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void moveTo(float f, float f2) {
        setCurrentViewport(computeScrollViewport(f, f2));
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void moveToWithAnimation(float f, float f2) {
        setCurrentViewportWithAnimation(computeScrollViewport(f, f2));
    }

    private Viewport computeScrollViewport(float f, float f2) {
        Viewport maximumViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        Viewport viewport = new Viewport(currentViewport);
        if (maximumViewport.contains(f, f2)) {
            float fWidth = currentViewport.width();
            float fHeight = currentViewport.height();
            float fMax = Math.max(maximumViewport.left, Math.min(f - (fWidth / 2.0f), maximumViewport.right - fWidth));
            float fMax2 = Math.max(maximumViewport.bottom + fHeight, Math.min(f2 + (fHeight / 2.0f), maximumViewport.top));
            viewport.set(fMax, fMax2, fWidth + fMax, fMax2 - fHeight);
        }
        return viewport;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isValueTouchEnabled() {
        return this.touchHandler.isValueTouchEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setValueTouchEnabled(boolean z) {
        this.touchHandler.setValueTouchEnabled(z);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ZoomType getZoomType() {
        return this.touchHandler.getZoomType();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomType(ZoomType zoomType) {
        this.touchHandler.setZoomType(zoomType);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public float getMaxZoom() {
        return this.chartComputator.getMaxZoom();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setMaxZoom(float f) {
        this.chartComputator.setMaxZoom(f);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public float getZoomLevel() {
        Viewport maximumViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        return Math.max(maximumViewport.width() / currentViewport.width(), maximumViewport.height() / currentViewport.height());
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomLevel(float f, float f2, float f3) {
        setCurrentViewport(computeZoomViewport(f, f2, f3));
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomLevelWithAnimation(float f, float f2, float f3) {
        setCurrentViewportWithAnimation(computeZoomViewport(f, f2, f3));
    }

    private Viewport computeZoomViewport(float f, float f2, float f3) {
        Viewport maximumViewport = getMaximumViewport();
        Viewport viewport = new Viewport(getMaximumViewport());
        if (maximumViewport.contains(f, f2)) {
            if (f3 < 1.0f) {
                f3 = 1.0f;
            } else if (f3 > getMaxZoom()) {
                f3 = getMaxZoom();
            }
            float fWidth = viewport.width() / f3;
            float fHeight = viewport.height() / f3;
            float f4 = fWidth / 2.0f;
            float f5 = fHeight / 2.0f;
            float f6 = f - f4;
            float f7 = f + f4;
            float f8 = f2 + f5;
            float f9 = f2 - f5;
            if (f6 < maximumViewport.left) {
                f6 = maximumViewport.left;
                f7 = f6 + fWidth;
            } else if (f7 > maximumViewport.right) {
                f7 = maximumViewport.right;
                f6 = f7 - fWidth;
            }
            if (f8 > maximumViewport.top) {
                f8 = maximumViewport.top;
                f9 = f8 - fHeight;
            } else if (f9 < maximumViewport.bottom) {
                f9 = maximumViewport.bottom;
                f8 = f9 + fHeight;
            }
            ZoomType zoomType = getZoomType();
            if (ZoomType.HORIZONTAL_AND_VERTICAL == zoomType) {
                viewport.set(f6, f8, f7, f9);
            } else if (ZoomType.HORIZONTAL == zoomType) {
                viewport.left = f6;
                viewport.right = f7;
            } else if (ZoomType.VERTICAL == zoomType) {
                viewport.top = f8;
                viewport.bottom = f9;
            }
        }
        return viewport;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public Viewport getMaximumViewport() {
        return this.chartRenderer.getMaximumViewport();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setMaximumViewport(Viewport viewport) {
        this.chartRenderer.setMaximumViewport(viewport);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setCurrentViewportWithAnimation(Viewport viewport) {
        if (viewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), viewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setCurrentViewportWithAnimation(Viewport viewport, long j) {
        if (viewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), viewport, j);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public Viewport getCurrentViewport() {
        return getChartRenderer().getCurrentViewport();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setCurrentViewport(Viewport viewport) {
        if (viewport != null) {
            this.chartRenderer.setCurrentViewport(viewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void resetViewports() {
        this.chartRenderer.setMaximumViewport(null);
        this.chartRenderer.setCurrentViewport(null);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isViewportCalculationEnabled() {
        return this.chartRenderer.isViewportCalculationEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setViewportCalculationEnabled(boolean z) {
        this.chartRenderer.setViewportCalculationEnabled(z);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isValueSelectionEnabled() {
        return this.touchHandler.isValueSelectionEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setValueSelectionEnabled(boolean z) {
        this.touchHandler.setValueSelectionEnabled(z);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void selectValue(SelectedValue selectedValue) {
        this.chartRenderer.selectValue(selectedValue);
        callTouchListener();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public SelectedValue getSelectedValue() {
        return this.chartRenderer.getSelectedValue();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isContainerScrollEnabled() {
        return this.isContainerScrollEnabled;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setContainerScrollEnabled(boolean z, ContainerScrollType containerScrollType) {
        this.isContainerScrollEnabled = z;
        this.containerScrollType = containerScrollType;
    }

    protected void onChartDataChange() {
        this.chartComputator.resetContentRect();
        this.chartRenderer.onChartDataChanged();
        this.axesRenderer.onChartDataChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    protected void resetRendererAndTouchHandler() {
        this.chartRenderer.resetRenderer();
        this.axesRenderer.resetRenderer();
        this.touchHandler.resetTouchHandler();
    }
}
