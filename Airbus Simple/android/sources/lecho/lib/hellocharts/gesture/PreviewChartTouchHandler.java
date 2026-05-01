package lecho.lib.hellocharts.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import lecho.lib.hellocharts.gesture.ChartTouchHandler;
import lecho.lib.hellocharts.view.Chart;

/* loaded from: classes.dex */
public class PreviewChartTouchHandler extends ChartTouchHandler {
    public PreviewChartTouchHandler(Context context, Chart chart) {
        super(context, chart);
        this.gestureDetector = new GestureDetector(context, new PreviewChartGestureListener());
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ChartScaleGestureListener());
        this.isValueTouchEnabled = false;
        this.isValueSelectionEnabled = false;
    }

    protected class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        protected ChartScaleGestureListener() {
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (!PreviewChartTouchHandler.this.isZoomEnabled) {
                return false;
            }
            float currentSpan = scaleGestureDetector.getCurrentSpan() / scaleGestureDetector.getPreviousSpan();
            if (Float.isInfinite(currentSpan)) {
                currentSpan = 1.0f;
            }
            return PreviewChartTouchHandler.this.chartZoomer.scale(PreviewChartTouchHandler.this.computator, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY(), currentSpan);
        }
    }

    protected class PreviewChartGestureListener extends ChartTouchHandler.ChartGestureListener {
        protected PreviewChartGestureListener() {
            super();
        }

        @Override // lecho.lib.hellocharts.gesture.ChartTouchHandler.ChartGestureListener, android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return super.onScroll(motionEvent, motionEvent2, -f, -f2);
        }

        @Override // lecho.lib.hellocharts.gesture.ChartTouchHandler.ChartGestureListener, android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return super.onFling(motionEvent, motionEvent2, -f, -f2);
        }
    }
}
