package per.goweii.anylayer;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/* loaded from: classes.dex */
class LayerManager implements View.OnKeyListener, ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnPreDrawListener {
    private final View mChild;
    private final ViewGroup mParent;
    private LifeListener mLifeListener = null;
    private boolean mCancelableOnClickKeyBack = true;
    private View currentKeyView = null;
    private boolean mOnAnimIn = false;
    private boolean mOnAnimOut = false;

    public interface LifeListener {
        long onAnimIn(View view);

        long onAnimOut(View view);

        void onAttach();

        void onDetach();

        void onRemove();

        void onShow();
    }

    LayerManager(@NonNull ViewGroup viewGroup, @NonNull View view) {
        this.mParent = viewGroup;
        this.mChild = view;
    }

    void add() {
        if (isAdded()) {
            return;
        }
        onAttach();
    }

    void remove() {
        if (isAdded()) {
            onRemove();
        }
    }

    private boolean isAdded() {
        return this.mChild.getParent() != null;
    }

    void setLifeListener(LifeListener lifeListener) {
        this.mLifeListener = lifeListener;
    }

    void setCancelableOnClickKeyBack(boolean z) {
        this.mCancelableOnClickKeyBack = z;
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        if (this.mChild.getViewTreeObserver().isAlive()) {
            this.mChild.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        this.mChild.postDelayed(new Runnable() { // from class: per.goweii.anylayer.LayerManager.1
            @Override // java.lang.Runnable
            public void run() {
                LayerManager.this.mOnAnimIn = false;
                LayerManager.this.onShow();
            }
        }, onAnimIn(this.mChild));
        return true;
    }

    private void onAttach() {
        if (this.mOnAnimIn) {
            return;
        }
        this.mOnAnimIn = true;
        this.mChild.setFocusable(true);
        this.mChild.setFocusableInTouchMode(true);
        this.mChild.requestFocus();
        this.currentKeyView = this.mChild;
        this.currentKeyView.setOnKeyListener(this);
        if (this.mLifeListener != null) {
            this.mLifeListener.onAttach();
        }
        this.mChild.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        this.mChild.getViewTreeObserver().addOnPreDrawListener(this);
        this.mParent.addView(this.mChild);
    }

    private long onAnimIn(View view) {
        if (this.mLifeListener != null) {
            return this.mLifeListener.onAnimIn(view);
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onShow() {
        if (this.mLifeListener != null) {
            this.mLifeListener.onShow();
        }
    }

    private void onRemove() {
        if (this.mOnAnimOut) {
            return;
        }
        this.mOnAnimOut = true;
        this.mChild.postDelayed(new Runnable() { // from class: per.goweii.anylayer.LayerManager.2
            @Override // java.lang.Runnable
            public void run() {
                LayerManager.this.mOnAnimOut = false;
                LayerManager.this.onDetach();
            }
        }, onAnimOut(this.mChild));
        if (this.mLifeListener != null) {
            this.mLifeListener.onRemove();
        }
    }

    private long onAnimOut(View view) {
        if (this.mLifeListener != null) {
            return this.mLifeListener.onAnimOut(view);
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDetach() {
        if (this.currentKeyView != null) {
            this.currentKeyView.setOnKeyListener(null);
        }
        this.mChild.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        this.mParent.removeView(this.mChild);
        if (this.mLifeListener != null) {
            this.mLifeListener.onDetach();
        }
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (this.mChild.getParent() == null || keyEvent.getAction() != 0 || i != 4) {
            return false;
        }
        if (!this.mCancelableOnClickKeyBack) {
            return true;
        }
        remove();
        return true;
    }

    @Override // android.view.ViewTreeObserver.OnGlobalFocusChangeListener
    public void onGlobalFocusChanged(View view, View view2) {
        if (this.currentKeyView != null) {
            this.currentKeyView.setOnKeyListener(null);
        }
        if (view != null) {
            view.setOnKeyListener(null);
        }
        if (view2 != null) {
            this.currentKeyView = view2;
            this.currentKeyView.setOnKeyListener(this);
        }
    }
}
