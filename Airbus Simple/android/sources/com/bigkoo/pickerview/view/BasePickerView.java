package com.bigkoo.pickerview.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.utils.PickerViewAnimateUtil;

/* loaded from: classes.dex */
public class BasePickerView {
    protected ViewGroup contentContainer;
    private Context context;
    private ViewGroup decorView;
    private ViewGroup dialogView;
    private boolean dismissing;
    private Animation inAnim;
    private boolean isShowing;
    private Dialog mDialog;
    private OnDismissListener onDismissListener;
    private Animation outAnim;
    private ViewGroup rootView;
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2, 80);
    protected int pickerview_timebtn_nor = -16417281;
    protected int pickerview_timebtn_pre = -4007179;
    protected int pickerview_bg_topbar = -657931;
    protected int pickerview_topbar_title = ViewCompat.MEASURED_STATE_MASK;
    protected int bgColor_default = -1;
    private int gravity = 80;
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() { // from class: com.bigkoo.pickerview.view.BasePickerView.3
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            BasePickerView.this.dismiss();
            return false;
        }
    };

    protected void initEvents() {
    }

    public boolean isDialog() {
        return false;
    }

    public BasePickerView(Context context) {
        this.context = context;
    }

    protected void initViews() {
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.context);
        if (isDialog()) {
            this.dialogView = (ViewGroup) layoutInflaterFrom.inflate(R.layout.layout_basepickerview, (ViewGroup) null, false);
            this.dialogView.setBackgroundColor(0);
            this.contentContainer = (ViewGroup) this.dialogView.findViewById(R.id.content_container);
            this.params.leftMargin = 30;
            this.params.rightMargin = 30;
            this.contentContainer.setLayoutParams(this.params);
            createDialog();
            this.dialogView.setOnClickListener(new View.OnClickListener() { // from class: com.bigkoo.pickerview.view.BasePickerView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    BasePickerView.this.dismiss();
                }
            });
            return;
        }
        this.decorView = (ViewGroup) ((Activity) this.context).getWindow().getDecorView().findViewById(android.R.id.content);
        this.rootView = (ViewGroup) layoutInflaterFrom.inflate(R.layout.layout_basepickerview, this.decorView, false);
        this.rootView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.contentContainer = (ViewGroup) this.rootView.findViewById(R.id.content_container);
        this.contentContainer.setLayoutParams(this.params);
    }

    protected void init() {
        this.inAnim = getInAnimation();
        this.outAnim = getOutAnimation();
    }

    private void onAttached(View view) {
        this.decorView.addView(view);
        this.contentContainer.startAnimation(this.inAnim);
    }

    public void show() {
        if (isDialog()) {
            showDialog();
        } else {
            if (isShowing()) {
                return;
            }
            this.isShowing = true;
            onAttached(this.rootView);
            this.rootView.requestFocus();
        }
    }

    public boolean isShowing() {
        if (isDialog()) {
            return false;
        }
        return this.rootView.getParent() != null || this.isShowing;
    }

    public void dismiss() {
        if (isDialog()) {
            dismissDialog();
        } else {
            if (this.dismissing) {
                return;
            }
            this.dismissing = true;
            this.outAnim.setAnimationListener(new Animation.AnimationListener() { // from class: com.bigkoo.pickerview.view.BasePickerView.2
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    BasePickerView.this.decorView.post(new Runnable() { // from class: com.bigkoo.pickerview.view.BasePickerView.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            BasePickerView.this.dismissImmediately();
                        }
                    });
                }
            });
            this.contentContainer.startAnimation(this.outAnim);
        }
    }

    public void dismissImmediately() {
        this.decorView.removeView(this.rootView);
        this.isShowing = false;
        this.dismissing = false;
        if (this.onDismissListener != null) {
            this.onDismissListener.onDismiss(this);
        }
    }

    public Animation getInAnimation() {
        return AnimationUtils.loadAnimation(this.context, PickerViewAnimateUtil.getAnimationResource(this.gravity, true));
    }

    public Animation getOutAnimation() {
        return AnimationUtils.loadAnimation(this.context, PickerViewAnimateUtil.getAnimationResource(this.gravity, false));
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    protected BasePickerView setOutSideCancelable(boolean z) {
        if (this.rootView != null) {
            View viewFindViewById = this.rootView.findViewById(R.id.outmost_container);
            if (z) {
                viewFindViewById.setOnTouchListener(this.onCancelableTouchListener);
            } else {
                viewFindViewById.setOnTouchListener(null);
            }
        }
        return this;
    }

    public View findViewById(int i) {
        return this.contentContainer.findViewById(i);
    }

    public void createDialog() {
        if (this.dialogView != null) {
            this.mDialog = new Dialog(this.context, R.style.custom_dialog2);
            this.mDialog.setCancelable(true);
            this.mDialog.setContentView(this.dialogView);
        }
    }

    public void showDialog() {
        if (this.mDialog != null) {
            this.mDialog.show();
        }
    }

    public void dismissDialog() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
        }
    }
}
