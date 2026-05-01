package com.shima.smartbushome.assist.scheduleutil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.shima.smartbushome.R;

/* loaded from: classes.dex */
public class SelectRemindCyclePopup implements View.OnClickListener {
    private TextView every_day;
    private Context mContext;
    public PopupWindow mPopupWindow;
    private SelectRemindCyclePopupOnClickListener selectRemindCyclePopupListener;
    private TextView tv_drugcycle_once;
    private TextView tv_fri;
    private TextView tv_mon;
    private TextView tv_sat;
    private TextView tv_sun;
    private TextView tv_sure;
    private TextView tv_thu;
    private TextView tv_tue;
    private TextView tv_wed;

    public interface SelectRemindCyclePopupOnClickListener {
        void obtainMessage(int i, String str);
    }

    public PopupWindow getmPopupWindow() {
        return this.mPopupWindow;
    }

    public SelectRemindCyclePopup(Context context) {
        this.mContext = context;
        this.mPopupWindow = new PopupWindow(context);
        this.mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        this.mPopupWindow.setWidth(-1);
        this.mPopupWindow.setHeight(-1);
        this.mPopupWindow.setTouchable(true);
        this.mPopupWindow.setFocusable(true);
        this.mPopupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        this.mPopupWindow.setContentView(initViews());
        this.mPopupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() { // from class: com.shima.smartbushome.assist.scheduleutil.SelectRemindCyclePopup.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SelectRemindCyclePopup.this.mPopupWindow.setFocusable(false);
                return true;
            }
        });
    }

    public View initViews() {
        View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.selectremindcycle_pop_window, (ViewGroup) null);
        this.tv_drugcycle_once = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_once);
        this.every_day = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_0);
        this.tv_mon = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_1);
        this.tv_tue = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_2);
        this.tv_wed = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_3);
        this.tv_thu = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_4);
        this.tv_fri = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_5);
        this.tv_sat = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_6);
        this.tv_sun = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_7);
        this.tv_sure = (TextView) viewInflate.findViewById(R.id.tv_drugcycle_sure);
        this.tv_drugcycle_once.setOnClickListener(this);
        this.tv_mon.setOnClickListener(this);
        this.tv_tue.setOnClickListener(this);
        this.tv_wed.setOnClickListener(this);
        this.tv_thu.setOnClickListener(this);
        this.tv_fri.setOnClickListener(this);
        this.tv_sat.setOnClickListener(this);
        this.tv_sun.setOnClickListener(this);
        this.tv_sure.setOnClickListener(this);
        this.every_day.setOnClickListener(this);
        return viewInflate;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws Resources.NotFoundException {
        Drawable drawable = this.mContext.getResources().getDrawable(R.drawable.cycle_check);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (view.getId()) {
            case R.id.tv_drugcycle_0 /* 2131232144 */:
                this.selectRemindCyclePopupListener.obtainMessage(8, "");
                break;
            case R.id.tv_drugcycle_1 /* 2131232145 */:
                if (this.tv_mon.getCompoundDrawables()[2] == null) {
                    this.tv_mon.setCompoundDrawables(null, null, drawable, null);
                } else {
                    this.tv_mon.setCompoundDrawables(null, null, null, null);
                }
                this.selectRemindCyclePopupListener.obtainMessage(0, "");
                break;
            case R.id.tv_drugcycle_2 /* 2131232146 */:
                if (this.tv_tue.getCompoundDrawables()[2] == null) {
                    this.tv_tue.setCompoundDrawables(null, null, drawable, null);
                } else {
                    this.tv_tue.setCompoundDrawables(null, null, null, null);
                }
                this.selectRemindCyclePopupListener.obtainMessage(1, "");
                break;
            case R.id.tv_drugcycle_3 /* 2131232147 */:
                if (this.tv_wed.getCompoundDrawables()[2] == null) {
                    this.tv_wed.setCompoundDrawables(null, null, drawable, null);
                } else {
                    this.tv_wed.setCompoundDrawables(null, null, null, null);
                }
                this.selectRemindCyclePopupListener.obtainMessage(2, "");
                break;
            case R.id.tv_drugcycle_4 /* 2131232148 */:
                if (this.tv_thu.getCompoundDrawables()[2] == null) {
                    this.tv_thu.setCompoundDrawables(null, null, drawable, null);
                } else {
                    this.tv_thu.setCompoundDrawables(null, null, null, null);
                }
                this.selectRemindCyclePopupListener.obtainMessage(3, "");
                break;
            case R.id.tv_drugcycle_5 /* 2131232149 */:
                if (this.tv_fri.getCompoundDrawables()[2] == null) {
                    this.tv_fri.setCompoundDrawables(null, null, drawable, null);
                } else {
                    this.tv_fri.setCompoundDrawables(null, null, null, null);
                }
                this.selectRemindCyclePopupListener.obtainMessage(4, "");
                break;
            case R.id.tv_drugcycle_6 /* 2131232150 */:
                if (this.tv_sat.getCompoundDrawables()[2] == null) {
                    this.tv_sat.setCompoundDrawables(null, null, drawable, null);
                } else {
                    this.tv_sat.setCompoundDrawables(null, null, null, null);
                }
                this.selectRemindCyclePopupListener.obtainMessage(5, "");
                break;
            case R.id.tv_drugcycle_7 /* 2131232151 */:
                if (this.tv_sun.getCompoundDrawables()[2] == null) {
                    this.tv_sun.setCompoundDrawables(null, null, drawable, null);
                } else {
                    this.tv_sun.setCompoundDrawables(null, null, null, null);
                }
                this.selectRemindCyclePopupListener.obtainMessage(6, "");
                break;
            case R.id.tv_drugcycle_once /* 2131232152 */:
                this.selectRemindCyclePopupListener.obtainMessage(9, "");
                break;
            case R.id.tv_drugcycle_sure /* 2131232153 */:
                this.selectRemindCyclePopupListener.obtainMessage(7, String.valueOf(((this.tv_mon.getCompoundDrawables()[2] == null ? 0 : 1) * 1) + ((this.tv_tue.getCompoundDrawables()[2] == null ? 0 : 1) * 2) + ((this.tv_wed.getCompoundDrawables()[2] == null ? 0 : 1) * 4) + ((this.tv_thu.getCompoundDrawables()[2] == null ? 0 : 1) * 8) + ((this.tv_fri.getCompoundDrawables()[2] == null ? 0 : 1) * 16) + ((this.tv_sat.getCompoundDrawables()[2] == null ? 0 : 1) * 32) + ((this.tv_sun.getCompoundDrawables()[2] != null ? 1 : 0) * 64)));
                dismiss();
                break;
        }
    }

    public void setOnSelectRemindCyclePopupListener(SelectRemindCyclePopupOnClickListener selectRemindCyclePopupOnClickListener) {
        this.selectRemindCyclePopupListener = selectRemindCyclePopupOnClickListener;
    }

    public void dismiss() {
        if (this.mPopupWindow == null || !this.mPopupWindow.isShowing()) {
            return;
        }
        this.mPopupWindow.dismiss();
    }

    public void showPopup(View view) {
        this.mPopupWindow.showAtLocation(view, 80, 0, 0);
    }
}
