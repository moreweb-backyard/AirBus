package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.graphics.RectF;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.ActivityChooserView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import lecho.lib.hellocharts.view.PieChartView;

/* loaded from: classes.dex */
public class PieChartTouchHandler extends ChartTouchHandler {
    public static final int FLING_VELOCITY_DOWNSCALE = 4;
    private boolean isRotationEnabled;
    protected PieChartView pieChart;
    protected ScrollerCompat scroller;

    public PieChartTouchHandler(Context context, PieChartView pieChartView) {
        super(context, pieChartView);
        this.isRotationEnabled = true;
        this.pieChart = pieChartView;
        this.scroller = ScrollerCompat.create(context);
        this.gestureDetector = new GestureDetector(context, new ChartGestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ChartScaleGestureListener());
        this.isZoomEnabled = false;
    }

    @Override // lecho.lib.hellocharts.gesture.ChartTouchHandler
    public boolean computeScroll() {
        if (this.isRotationEnabled && this.scroller.computeScrollOffset()) {
            this.pieChart.setChartRotation(this.scroller.getCurrY(), false);
        }
        return false;
    }

    @Override // lecho.lib.hellocharts.gesture.ChartTouchHandler
    public boolean handleTouchEvent(MotionEvent motionEvent) {
        boolean zHandleTouchEvent = super.handleTouchEvent(motionEvent);
        return this.isRotationEnabled ? this.gestureDetector.onTouchEvent(motionEvent) || zHandleTouchEvent : zHandleTouchEvent;
    }

    public boolean isRotationEnabled() {
        return this.isRotationEnabled;
    }

    public void setRotationEnabled(boolean z) {
        this.isRotationEnabled = z;
    }

    private class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            return false;
        }

        private ChartScaleGestureListener() {
        }
    }

    private class ChartGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            return false;
        }

        private ChartGestureListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            PieChartTouchHandler.this.scroller.abortAnimation();
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            RectF circleOval = PieChartTouchHandler.this.pieChart.getCircleOval();
            PieChartTouchHandler.this.pieChart.setChartRotation(PieChartTouchHandler.this.pieChart.getChartRotation() - (((int) vectorToScalarScroll(f, f2, motionEvent2.getX() - circleOval.centerX(), motionEvent2.getY() - circleOval.centerY())) / 4), false);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            RectF circleOval = PieChartTouchHandler.this.pieChart.getCircleOval();
            float fVectorToScalarScroll = vectorToScalarScroll(f, f2, motionEvent2.getX() - circleOval.centerX(), motionEvent2.getY() - circleOval.centerY());
            PieChartTouchHandler.this.scroller.abortAnimation();
            PieChartTouchHandler.this.scroller.fling(0, PieChartTouchHandler.this.pieChart.getChartRotation(), 0, ((int) fVectorToScalarScroll) / 4, 0, 0, Integer.MIN_VALUE, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
            return true;
        }

        private float vectorToScalarScroll(float f, float f2, float f3, float f4) {
            return ((float) Math.sqrt((f * f) + (f2 * f2))) * Math.signum(((-f4) * f) + (f3 * f2));
        }
    }
}
