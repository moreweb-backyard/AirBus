package lecho.lib.hellocharts.view;

import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import lecho.lib.hellocharts.animation.PieChartRotationAnimator;
import lecho.lib.hellocharts.animation.PieChartRotationAnimatorV14;
import lecho.lib.hellocharts.animation.PieChartRotationAnimatorV8;
import lecho.lib.hellocharts.gesture.PieChartTouchHandler;
import lecho.lib.hellocharts.listener.DummyPieChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.provider.PieChartDataProvider;
import lecho.lib.hellocharts.renderer.PieChartRenderer;

/* loaded from: classes.dex */
public class PieChartView extends AbstractChartView implements PieChartDataProvider {
    private static final String TAG = "PieChartView";
    protected PieChartData data;
    protected PieChartOnValueSelectListener onValueTouchListener;
    protected PieChartRenderer pieChartRenderer;
    protected PieChartRotationAnimator rotationAnimator;

    public PieChartView(Context context) {
        this(context, null, 0);
    }

    public PieChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PieChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.onValueTouchListener = new DummyPieChartOnValueSelectListener();
        this.pieChartRenderer = new PieChartRenderer(context, this, this);
        this.touchHandler = new PieChartTouchHandler(context, this);
        setChartRenderer(this.pieChartRenderer);
        if (Build.VERSION.SDK_INT < 14) {
            this.rotationAnimator = new PieChartRotationAnimatorV8(this);
        } else {
            this.rotationAnimator = new PieChartRotationAnimatorV14(this);
        }
        setPieChartData(PieChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.PieChartDataProvider
    public PieChartData getPieChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.PieChartDataProvider
    public void setPieChartData(PieChartData pieChartData) {
        if (pieChartData == null) {
            this.data = PieChartData.generateDummyData();
        } else {
            this.data = pieChartData;
        }
        super.onChartDataChange();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartData getChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), this.data.getValues().get(selectedValue.getFirstIndex()));
        } else {
            this.onValueTouchListener.onValueDeselected();
        }
    }

    public PieChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(PieChartOnValueSelectListener pieChartOnValueSelectListener) {
        if (pieChartOnValueSelectListener != null) {
            this.onValueTouchListener = pieChartOnValueSelectListener;
        }
    }

    public RectF getCircleOval() {
        return this.pieChartRenderer.getCircleOval();
    }

    public void setCircleOval(RectF rectF) {
        this.pieChartRenderer.setCircleOval(rectF);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public int getChartRotation() {
        return this.pieChartRenderer.getChartRotation();
    }

    public void setChartRotation(int i, boolean z) {
        if (z) {
            this.rotationAnimator.cancelAnimation();
            this.rotationAnimator.startAnimation(this.pieChartRenderer.getChartRotation(), i);
        } else {
            this.pieChartRenderer.setChartRotation(i);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public boolean isChartRotationEnabled() {
        if (this.touchHandler instanceof PieChartTouchHandler) {
            return ((PieChartTouchHandler) this.touchHandler).isRotationEnabled();
        }
        return false;
    }

    public void setChartRotationEnabled(boolean z) {
        if (this.touchHandler instanceof PieChartTouchHandler) {
            ((PieChartTouchHandler) this.touchHandler).setRotationEnabled(z);
        }
    }

    public SliceValue getValueForAngle(int i, SelectedValue selectedValue) {
        return this.pieChartRenderer.getValueForAngle(i, selectedValue);
    }

    public float getCircleFillRatio() {
        return this.pieChartRenderer.getCircleFillRatio();
    }

    public void setCircleFillRatio(float f) {
        this.pieChartRenderer.setCircleFillRatio(f);
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
