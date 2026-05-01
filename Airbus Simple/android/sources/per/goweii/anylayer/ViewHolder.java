package per.goweii.anylayer;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import per.goweii.anylayer.AnyLayer;

/* loaded from: classes.dex */
class ViewHolder {
    private final AnyLayer mAnyLayer;
    private ImageView mBackground;
    private FrameLayout mContainer;
    private View mContent;
    private FrameLayout mContentWrapper;
    private SparseArray<View> views = null;
    private SparseArray<AnyLayer.OnLayerClickListener> onClickListeners = null;

    ViewHolder(AnyLayer anyLayer, FrameLayout frameLayout) {
        this.mAnyLayer = anyLayer;
        this.mContainer = frameLayout;
        this.mContentWrapper = (FrameLayout) this.mContainer.findViewById(R.id.fl_content_wrapper);
        this.mBackground = (ImageView) this.mContainer.findViewById(R.id.iv_background);
    }

    void recycle() {
        if (this.mBackground.getDrawable() instanceof BitmapDrawable) {
            ((BitmapDrawable) this.mBackground.getDrawable()).getBitmap().recycle();
        }
    }

    void setContent(View view) {
        this.mContent = view;
    }

    View getContent() {
        return this.mContent;
    }

    FrameLayout getContainer() {
        return this.mContainer;
    }

    FrameLayout getContentWrapper() {
        return this.mContentWrapper;
    }

    ImageView getBackground() {
        return this.mBackground;
    }

    void bindListener() {
        if (this.onClickListeners == null) {
            return;
        }
        for (int i = 0; i < this.onClickListeners.size(); i++) {
            int iKeyAt = this.onClickListeners.keyAt(i);
            final AnyLayer.OnLayerClickListener onLayerClickListenerValueAt = this.onClickListeners.valueAt(i);
            getView(iKeyAt).setOnClickListener(new View.OnClickListener() { // from class: per.goweii.anylayer.ViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    onLayerClickListenerValueAt.onClick(ViewHolder.this.mAnyLayer, view);
                }
            });
        }
    }

    <V extends View> V getView(@IdRes int i) {
        if (this.views == null) {
            this.views = new SparseArray<>();
        }
        if (this.views.indexOfKey(i) < 0) {
            V v = (V) this.mContent.findViewById(i);
            this.views.put(i, v);
            return v;
        }
        return (V) this.views.get(i);
    }

    void addOnClickListener(AnyLayer.OnLayerClickListener onLayerClickListener, @IdRes int i, @IdRes int... iArr) {
        if (this.onClickListeners == null) {
            this.onClickListeners = new SparseArray<>();
        }
        if (this.onClickListeners.indexOfKey(i) < 0) {
            this.onClickListeners.put(i, onLayerClickListener);
        }
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        for (int i2 : iArr) {
            if (this.onClickListeners.indexOfKey(i2) < 0) {
                this.onClickListeners.put(i2, onLayerClickListener);
            }
        }
    }
}
