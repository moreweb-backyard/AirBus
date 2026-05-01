package per.goweii.anylayer;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

/* loaded from: classes.dex */
class SoftInputHelper implements ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnGlobalFocusChangeListener {
    private final View rootView;
    private final Window window;
    private long duration = 200;
    private View moveView = null;
    private View bottomView = null;
    private EditText[] focusViews = null;
    private OnSoftInputListener onSoftInputListener = null;
    private boolean moveWithScroll = false;
    private boolean isOpened = false;
    private int moveHeight = 0;
    private boolean isFocusChange = false;
    private Runnable moveRunnable = new Runnable() { // from class: per.goweii.anylayer.SoftInputHelper.1
        @Override // java.lang.Runnable
        public void run() {
            if (SoftInputHelper.this.isViewFocus()) {
                Rect rootViewRect = SoftInputHelper.this.getRootViewRect();
                int bottomViewY = SoftInputHelper.this.getBottomViewY();
                if (bottomViewY > rootViewRect.bottom) {
                    SoftInputHelper.this.moveHeight += bottomViewY - rootViewRect.bottom;
                    SoftInputHelper.this.move();
                    return;
                }
                return;
            }
            SoftInputHelper.this.moveHeight = 0;
            SoftInputHelper.this.move();
        }
    };

    public interface OnSoftInputListener {
        void onClose();

        void onOpen();
    }

    private SoftInputHelper(@NonNull Activity activity) {
        this.window = activity.getWindow();
        this.rootView = this.window.getDecorView().getRootView();
        ViewTreeObserver viewTreeObserver = this.rootView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(this);
        viewTreeObserver.addOnGlobalFocusChangeListener(this);
    }

    public static SoftInputHelper attach(@NonNull Activity activity) {
        return new SoftInputHelper(activity);
    }

    public void detach() {
        if (this.rootView.getViewTreeObserver().isAlive()) {
            if (Build.VERSION.SDK_INT >= 16) {
                this.rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                this.rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
            this.rootView.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        }
    }

    public SoftInputHelper init(@NonNull View view, @NonNull View view2, @NonNull EditText... editTextArr) {
        this.moveView = view;
        this.bottomView = view2;
        this.focusViews = editTextArr;
        this.window.setSoftInputMode(0);
        return this;
    }

    public SoftInputHelper listener(OnSoftInputListener onSoftInputListener) {
        this.onSoftInputListener = onSoftInputListener;
        return this;
    }

    public SoftInputHelper duration(long j) {
        this.duration = j;
        return this;
    }

    public SoftInputHelper moveWithScroll() {
        this.moveWithScroll = true;
        return this;
    }

    public SoftInputHelper moveWithTranslation() {
        this.moveWithScroll = false;
        return this;
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        Rect rootViewRect = getRootViewRect();
        if (isSoftOpen(rootViewRect.bottom - rootViewRect.top, this.rootView.getHeight())) {
            if (!this.isOpened) {
                this.isOpened = true;
                if (this.onSoftInputListener != null) {
                    this.onSoftInputListener.onOpen();
                }
            }
            if (this.moveView == null || this.bottomView == null || this.focusViews == null) {
                return;
            }
            if (this.isFocusChange) {
                this.isFocusChange = false;
                this.rootView.removeCallbacks(this.moveRunnable);
            }
            if (isViewFocus()) {
                int bottomViewY = getBottomViewY();
                if (bottomViewY > rootViewRect.bottom) {
                    this.moveHeight += bottomViewY - rootViewRect.bottom;
                    move();
                    return;
                } else {
                    if (bottomViewY < rootViewRect.bottom) {
                        int i = -(bottomViewY - rootViewRect.bottom);
                        if (this.moveHeight > 0) {
                            if (this.moveHeight >= i) {
                                this.moveHeight -= i;
                            } else {
                                this.moveHeight = 0;
                            }
                            move();
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
            this.moveHeight = 0;
            move();
            return;
        }
        if (this.isOpened) {
            this.isOpened = false;
            if (this.onSoftInputListener != null) {
                this.onSoftInputListener.onClose();
            }
        }
        if (this.moveView == null || this.bottomView == null || this.focusViews == null) {
            return;
        }
        this.moveHeight = 0;
        move();
    }

    @Override // android.view.ViewTreeObserver.OnGlobalFocusChangeListener
    public void onGlobalFocusChanged(View view, View view2) {
        if (this.isOpened) {
            if (view2 instanceof EditText) {
                if (this.moveView == null || this.bottomView == null || this.focusViews == null) {
                    return;
                }
                this.isFocusChange = true;
                this.rootView.postDelayed(this.moveRunnable, 100L);
                return;
            }
            InputMethodUtils.hide(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getBottomViewY() {
        int[] iArr = new int[2];
        this.bottomView.getLocationOnScreen(iArr);
        return iArr[1] + this.bottomView.getHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getRootViewRect() {
        Rect rect = new Rect();
        this.rootView.getWindowVisibleDisplayFrame(rect);
        return rect;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void move() {
        if (this.moveWithScroll) {
            scrollTo(this.moveHeight);
        } else {
            translationTo(-this.moveHeight);
        }
    }

    private void translationTo(int i) {
        float translationY = this.moveView.getTranslationY();
        float f = i;
        if (translationY == f) {
            return;
        }
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this.moveView, "translationY", translationY, f);
        objectAnimatorOfFloat.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfFloat.setDuration(this.duration);
        objectAnimatorOfFloat.start();
    }

    private void scrollTo(int i) {
        int scrollY = this.moveView.getScrollY();
        if (scrollY == i) {
            return;
        }
        ObjectAnimator objectAnimatorOfInt = ObjectAnimator.ofInt(this.moveView, "scrollY", scrollY, i);
        objectAnimatorOfInt.setInterpolator(new DecelerateInterpolator());
        objectAnimatorOfInt.setDuration(this.duration);
        objectAnimatorOfInt.start();
    }

    private boolean isSoftOpen(int i, int i2) {
        return i2 - i > i2 / 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isViewFocus() {
        if (this.focusViews == null || this.focusViews.length == 0) {
            return true;
        }
        View currentFocus = this.window.getCurrentFocus();
        for (EditText editText : this.focusViews) {
            if (currentFocus == editText) {
                return true;
            }
        }
        return false;
    }
}
