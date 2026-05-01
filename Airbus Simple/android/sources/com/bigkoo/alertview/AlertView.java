package com.bigkoo.alertview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class AlertView {
    public static final int CANCELPOSITION = -1;
    public static final int HORIZONTAL_BUTTONS_MAXCOUNT = 2;
    private String cancel;
    private ViewGroup contentContainer;
    private WeakReference<Context> contextWeak;
    private ViewGroup decorView;
    private String[] destructive;
    private Animation inAnim;
    private boolean isShowing;
    private ViewGroup loAlertHeader;
    private List<String> mDestructive;
    private List<String> mOthers;
    private String msg;
    private OnDismissListener onDismissListener;
    private OnItemClickListener onItemClickListener;
    private String[] others;
    private Animation outAnim;
    private ViewGroup rootView;
    private Style style;
    private String title;
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2, 80);
    private ArrayList<String> mDatas = new ArrayList<>();
    private int gravity = 17;
    boolean shoulddismiss = true;
    private Animation.AnimationListener outAnimListener = new Animation.AnimationListener() { // from class: com.bigkoo.alertview.AlertView.2
        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            AlertView.this.dismissImmediately();
        }
    };
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() { // from class: com.bigkoo.alertview.AlertView.3
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            AlertView.this.dismiss();
            return false;
        }
    };

    public enum Style {
        ActionSheet,
        Alert
    }

    protected void initEvents() {
    }

    public AlertView(Builder builder) throws Resources.NotFoundException {
        this.style = Style.Alert;
        this.contextWeak = new WeakReference<>(builder.context);
        this.style = builder.style;
        this.title = builder.title;
        this.msg = builder.msg;
        this.cancel = builder.cancel;
        this.destructive = builder.destructive;
        this.others = builder.others;
        this.onItemClickListener = builder.onItemClickListener;
        initData(this.title, this.msg, this.cancel, this.destructive, this.others);
        initViews();
        init();
        initEvents();
    }

    public AlertView(String str, String str2, String str3, String[] strArr, String[] strArr2, Context context, Style style, OnItemClickListener onItemClickListener) throws Resources.NotFoundException {
        this.style = Style.Alert;
        this.contextWeak = new WeakReference<>(context);
        if (style != null) {
            this.style = style;
        }
        this.onItemClickListener = onItemClickListener;
        initData(str, str2, str3, strArr, strArr2);
        initViews();
        init();
        initEvents();
    }

    protected void initData(String str, String str2, String str3, String[] strArr, String[] strArr2) {
        this.title = str;
        this.msg = str2;
        if (strArr != null) {
            this.mDestructive = Arrays.asList(strArr);
            this.mDatas.addAll(this.mDestructive);
        }
        if (strArr2 != null) {
            this.mOthers = Arrays.asList(strArr2);
            this.mDatas.addAll(this.mOthers);
        }
        if (str3 != null) {
            this.cancel = str3;
            if (this.style != Style.Alert || this.mDatas.size() >= 2) {
                return;
            }
            this.mDatas.add(0, str3);
        }
    }

    protected void initViews() throws Resources.NotFoundException {
        Context context = this.contextWeak.get();
        if (context == null) {
        }
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(context);
        this.decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        this.rootView = (ViewGroup) layoutInflaterFrom.inflate(R.layout.layout_alertview, this.decorView, false);
        this.rootView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.contentContainer = (ViewGroup) this.rootView.findViewById(R.id.content_container);
        switch (this.style) {
            case ActionSheet:
                this.params.gravity = 80;
                int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.margin_actionsheet_left_right);
                this.params.setMargins(dimensionPixelSize, 0, dimensionPixelSize, dimensionPixelSize);
                this.contentContainer.setLayoutParams(this.params);
                this.gravity = 80;
                initActionSheetViews(layoutInflaterFrom);
                break;
            case Alert:
                this.params.gravity = 17;
                int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.margin_alert_left_right);
                this.params.setMargins(dimensionPixelSize2, 0, dimensionPixelSize2, 0);
                this.contentContainer.setLayoutParams(this.params);
                this.gravity = 17;
                initAlertViews(layoutInflaterFrom);
                break;
        }
    }

    protected void initHeaderView(ViewGroup viewGroup) {
        this.loAlertHeader = (ViewGroup) viewGroup.findViewById(R.id.loAlertHeader);
        TextView textView = (TextView) viewGroup.findViewById(R.id.tvAlertTitle);
        TextView textView2 = (TextView) viewGroup.findViewById(R.id.tvAlertMsg);
        if (this.title != null) {
            textView.setText(this.title);
        } else {
            textView.setVisibility(8);
        }
        if (this.msg != null) {
            textView2.setText(this.msg);
        } else {
            textView2.setVisibility(8);
        }
    }

    protected void initListView() {
        Context context = this.contextWeak.get();
        if (context == null) {
            return;
        }
        ListView listView = (ListView) this.contentContainer.findViewById(R.id.alertButtonListView);
        if (this.cancel != null && this.style == Style.Alert) {
            View viewInflate = LayoutInflater.from(context).inflate(R.layout.item_alertbutton, (ViewGroup) null);
            TextView textView = (TextView) viewInflate.findViewById(R.id.tvAlert);
            textView.setText(this.cancel);
            textView.setClickable(true);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_cancel));
            textView.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
            textView.setOnClickListener(new OnTextClickListener(-1));
            listView.addFooterView(viewInflate);
        }
        listView.setAdapter((ListAdapter) new AlertViewAdapter(this.mDatas, this.mDestructive));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.bigkoo.alertview.AlertView.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (AlertView.this.onItemClickListener != null) {
                    AlertView.this.onItemClickListener.onItemClick(AlertView.this, i);
                }
                AlertView.this.dismiss();
            }
        });
    }

    public void setShoulddismiss(boolean z) {
        this.shoulddismiss = z;
    }

    protected void initActionSheetViews(LayoutInflater layoutInflater) {
        initHeaderView((ViewGroup) layoutInflater.inflate(R.layout.layout_alertview_actionsheet, this.contentContainer));
        initListView();
        TextView textView = (TextView) this.contentContainer.findViewById(R.id.tvAlertCancel);
        if (this.cancel != null) {
            textView.setVisibility(0);
            textView.setText(this.cancel);
        }
        textView.setOnClickListener(new OnTextClickListener(-1));
    }

    protected void initAlertViews(LayoutInflater layoutInflater) {
        Context context = this.contextWeak.get();
        if (context == null) {
            return;
        }
        initHeaderView((ViewGroup) layoutInflater.inflate(R.layout.layout_alertview_alert, this.contentContainer));
        if (this.mDatas.size() <= 2) {
            ((ViewStub) this.contentContainer.findViewById(R.id.viewStubHorizontal)).inflate();
            LinearLayout linearLayout = (LinearLayout) this.contentContainer.findViewById(R.id.loAlertButtons);
            int i = 0;
            for (int i2 = 0; i2 < this.mDatas.size(); i2++) {
                if (i2 != 0) {
                    View view = new View(context);
                    view.setBackgroundColor(context.getResources().getColor(R.color.bgColor_divier));
                    linearLayout.addView(view, new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.size_divier), -1));
                }
                View viewInflate = LayoutInflater.from(context).inflate(R.layout.item_alertbutton, (ViewGroup) null);
                TextView textView = (TextView) viewInflate.findViewById(R.id.tvAlert);
                textView.setClickable(true);
                if (this.mDatas.size() == 1) {
                    textView.setBackgroundResource(R.drawable.bg_alertbutton_bottom);
                } else if (i2 == 0) {
                    textView.setBackgroundResource(R.drawable.bg_alertbutton_left);
                } else if (i2 == this.mDatas.size() - 1) {
                    textView.setBackgroundResource(R.drawable.bg_alertbutton_right);
                }
                String str = this.mDatas.get(i2);
                textView.setText(str);
                if (str == this.cancel) {
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    textView.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_cancel));
                    textView.setOnClickListener(new OnTextClickListener(-1));
                    i--;
                } else if (this.mDestructive != null && this.mDestructive.contains(str)) {
                    textView.setTextColor(context.getResources().getColor(R.color.textColor_alert_button_destructive));
                }
                textView.setOnClickListener(new OnTextClickListener(i));
                i++;
                linearLayout.addView(viewInflate, new LinearLayout.LayoutParams(-1, -2, 1.0f));
            }
            return;
        }
        ((ViewStub) this.contentContainer.findViewById(R.id.viewStubVertical)).inflate();
        initListView();
    }

    protected void init() {
        this.inAnim = getInAnimation();
        this.outAnim = getOutAnimation();
    }

    public AlertView addExtView(View view) {
        this.loAlertHeader.addView(view);
        return this;
    }

    private void onAttached(View view) {
        this.isShowing = true;
        this.decorView.addView(view);
        this.contentContainer.startAnimation(this.inAnim);
    }

    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(this.rootView);
    }

    public boolean isShowing() {
        return this.rootView.getParent() != null && this.isShowing;
    }

    public void dismiss() {
        this.outAnim.setAnimationListener(this.outAnimListener);
        this.contentContainer.startAnimation(this.outAnim);
    }

    public void dismissImmediately() {
        this.decorView.removeView(this.rootView);
        this.isShowing = false;
        if (this.onDismissListener != null) {
            this.onDismissListener.onDismiss(this);
        }
    }

    public Animation getInAnimation() {
        Context context = this.contextWeak.get();
        if (context == null) {
            return null;
        }
        return AnimationUtils.loadAnimation(context, AlertAnimateUtil.getAnimationResource(this.gravity, true));
    }

    public Animation getOutAnimation() {
        Context context = this.contextWeak.get();
        if (context == null) {
            return null;
        }
        return AnimationUtils.loadAnimation(context, AlertAnimateUtil.getAnimationResource(this.gravity, false));
    }

    public AlertView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    class OnTextClickListener implements View.OnClickListener {
        private int position;

        public OnTextClickListener(int i) {
            this.position = i;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (AlertView.this.onItemClickListener != null) {
                AlertView.this.onItemClickListener.onItemClick(AlertView.this, this.position);
            }
            if (this.position == -1) {
                AlertView.this.dismiss();
            } else if (AlertView.this.shoulddismiss) {
                AlertView.this.dismiss();
            }
        }
    }

    public void setMarginBottom(int i) throws Resources.NotFoundException {
        Context context = this.contextWeak.get();
        if (context == null) {
            return;
        }
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.margin_alert_left_right);
        this.params.setMargins(dimensionPixelSize, 0, dimensionPixelSize, i);
        this.contentContainer.setLayoutParams(this.params);
    }

    public AlertView setCancelable(boolean z) {
        View viewFindViewById = this.rootView.findViewById(R.id.outmost_container);
        if (z) {
            viewFindViewById.setOnTouchListener(this.onCancelableTouchListener);
        } else {
            viewFindViewById.setOnTouchListener(null);
        }
        return this;
    }

    public static class Builder {
        private String cancel;
        private Context context;
        private String[] destructive;
        private String msg;
        private OnItemClickListener onItemClickListener;
        private String[] others;
        private Style style;
        private String title;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setStyle(Style style) {
            if (style != null) {
                this.style = style;
            }
            return this;
        }

        public Builder setTitle(String str) {
            this.title = str;
            return this;
        }

        public Builder setMessage(String str) {
            this.msg = str;
            return this;
        }

        public Builder setCancelText(String str) {
            this.cancel = str;
            return this;
        }

        public Builder setDestructive(String... strArr) {
            this.destructive = strArr;
            return this;
        }

        public Builder setOthers(String[] strArr) {
            this.others = strArr;
            return this;
        }

        public Builder setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public AlertView build() {
            return new AlertView(this);
        }
    }
}
