package per.goweii.anylayer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import per.goweii.anylayer.AnyLayer;

/* loaded from: classes.dex */
public abstract class BaseLayer implements AnyLayer.OnVisibleChangeListener {
    protected AnyLayer mAnyLayer = null;

    @Nullable
    protected Context getContext() {
        return null;
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @Nullable
    protected ViewGroup getParent() {
        return null;
    }

    @Nullable
    protected View getTarget() {
        return null;
    }

    protected void onCreate() {
    }

    protected boolean onDestroy() {
        return true;
    }

    @Override // per.goweii.anylayer.AnyLayer.OnVisibleChangeListener
    public void onShow(AnyLayer anyLayer) {
        onCreate();
    }

    @Override // per.goweii.anylayer.AnyLayer.OnVisibleChangeListener
    public void onDismiss(AnyLayer anyLayer) {
        if (onDestroy()) {
            this.mAnyLayer = null;
        }
    }

    public void show() {
        if (this.mAnyLayer == null) {
            createLayer();
        }
        this.mAnyLayer.show();
    }

    public void dismiss() {
        if (this.mAnyLayer != null) {
            this.mAnyLayer.dismiss();
        }
    }

    private void createLayer() {
        if (getTarget() != null) {
            this.mAnyLayer = AnyLayer.target(getTarget());
        } else if (getParent() != null) {
            this.mAnyLayer = AnyLayer.with(getParent());
        } else if (getContext() != null) {
            this.mAnyLayer = AnyLayer.with(getContext());
        } else {
            throw new NullPointerException("创建浮层失败，getTarget()/getParent()/getContext()方法不能同时为null");
        }
        this.mAnyLayer.contentView(getLayoutId());
        this.mAnyLayer.onVisibleChangeListener(this);
    }
}
