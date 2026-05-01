package com.bigkoo.pickerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTime;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class TimePickerView extends BasePickerView implements View.OnClickListener {
    private static final String TAG_CANCEL = "cancel";
    private static final String TAG_SUBMIT = "submit";
    private int Color_Background_Title;
    private int Color_Background_Wheel;
    private int Color_Cancel;
    private int Color_Submit;
    private int Color_Title;
    private int Size_Content;
    private int Size_Submit_Cancel;
    private int Size_Title;
    private String Str_Cancel;
    private String Str_Submit;
    private String Str_Title;
    private Button btnCancel;
    private Button btnSubmit;
    private boolean cancelable;
    private CustomListener customListener;
    private boolean cyclic;
    private Calendar date;
    private int dividerColor;
    private WheelView.DividerType dividerType;
    private Calendar endDate;
    private int endYear;
    private int gravity;
    private boolean isDialog;
    private String label_day;
    private String label_hours;
    private String label_mins;
    private String label_month;
    private String label_seconds;
    private String label_year;
    private int layoutRes;
    private float lineSpacingMultiplier;
    private Calendar startDate;
    private int startYear;
    private int textColorCenter;
    private int textColorOut;
    private OnTimeSelectListener timeSelectListener;
    private TextView tvTitle;
    private Type type;
    WheelTime wheelTime;

    public interface OnTimeSelectListener {
        void onTimeSelect(Date date, View view);
    }

    public enum Type {
        ALL,
        YEAR_MONTH_DAY,
        HOURS_MINS,
        MONTH_DAY_HOUR_MIN,
        YEAR_MONTH,
        YEAR_MONTH_DAY_HOUR_MIN
    }

    public TimePickerView(Builder builder) {
        super(builder.context);
        this.gravity = 17;
        this.lineSpacingMultiplier = 1.6f;
        this.timeSelectListener = builder.timeSelectListener;
        this.gravity = builder.gravity;
        this.type = builder.type;
        this.Str_Submit = builder.Str_Submit;
        this.Str_Cancel = builder.Str_Cancel;
        this.Str_Title = builder.Str_Title;
        this.Color_Submit = builder.Color_Submit;
        this.Color_Cancel = builder.Color_Cancel;
        this.Color_Title = builder.Color_Title;
        this.Color_Background_Wheel = builder.Color_Background_Wheel;
        this.Color_Background_Title = builder.Color_Background_Title;
        this.Size_Submit_Cancel = builder.Size_Submit_Cancel;
        this.Size_Title = builder.Size_Title;
        this.Size_Content = builder.Size_Content;
        this.startYear = builder.startYear;
        this.endYear = builder.endYear;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.date = builder.date;
        this.cyclic = builder.cyclic;
        this.cancelable = builder.cancelable;
        this.label_year = builder.label_year;
        this.label_month = builder.label_month;
        this.label_day = builder.label_day;
        this.label_hours = builder.label_hours;
        this.label_mins = builder.label_mins;
        this.label_seconds = builder.label_seconds;
        this.textColorCenter = builder.textColorCenter;
        this.textColorOut = builder.textColorOut;
        this.dividerColor = builder.dividerColor;
        this.customListener = builder.customListener;
        this.layoutRes = builder.layoutRes;
        this.lineSpacingMultiplier = builder.lineSpacingMultiplier;
        this.isDialog = builder.isDialog;
        this.dividerType = builder.dividerType;
        initView(builder.context);
    }

    public static class Builder {
        private int Color_Background_Title;
        private int Color_Background_Wheel;
        private int Color_Cancel;
        private int Color_Submit;
        private int Color_Title;
        private String Str_Cancel;
        private String Str_Submit;
        private String Str_Title;
        private Context context;
        private CustomListener customListener;
        private Calendar date;
        private int dividerColor;
        private WheelView.DividerType dividerType;
        private Calendar endDate;
        private int endYear;
        private boolean isDialog;
        private String label_day;
        private String label_hours;
        private String label_mins;
        private String label_month;
        private String label_seconds;
        private String label_year;
        private Calendar startDate;
        private int startYear;
        private int textColorCenter;
        private int textColorOut;
        private OnTimeSelectListener timeSelectListener;
        private int layoutRes = R.layout.pickerview_time;
        private Type type = Type.ALL;
        private int gravity = 17;
        private int Size_Submit_Cancel = 17;
        private int Size_Title = 18;
        private int Size_Content = 18;
        private boolean cyclic = false;
        private boolean cancelable = true;
        private float lineSpacingMultiplier = 1.6f;

        public Builder(Context context, OnTimeSelectListener onTimeSelectListener) {
            this.context = context;
            this.timeSelectListener = onTimeSelectListener;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder gravity(int i) {
            this.gravity = i;
            return this;
        }

        public Builder setSubmitText(String str) {
            this.Str_Submit = str;
            return this;
        }

        public Builder isDialog(boolean z) {
            this.isDialog = z;
            return this;
        }

        public Builder setCancelText(String str) {
            this.Str_Cancel = str;
            return this;
        }

        public Builder setTitleText(String str) {
            this.Str_Title = str;
            return this;
        }

        public Builder setSubmitColor(int i) {
            this.Color_Submit = i;
            return this;
        }

        public Builder setCancelColor(int i) {
            this.Color_Cancel = i;
            return this;
        }

        public Builder setBgColor(int i) {
            this.Color_Background_Wheel = i;
            return this;
        }

        public Builder setTitleBgColor(int i) {
            this.Color_Background_Title = i;
            return this;
        }

        public Builder setTitleColor(int i) {
            this.Color_Title = i;
            return this;
        }

        public Builder setSubCalSize(int i) {
            this.Size_Submit_Cancel = i;
            return this;
        }

        public Builder setTitleSize(int i) {
            this.Size_Title = i;
            return this;
        }

        public Builder setContentSize(int i) {
            this.Size_Content = i;
            return this;
        }

        public Builder setDate(Calendar calendar) {
            this.date = calendar;
            return this;
        }

        public Builder setLayoutRes(int i, CustomListener customListener) {
            this.layoutRes = i;
            this.customListener = customListener;
            return this;
        }

        public Builder setRange(int i, int i2) {
            this.startYear = i;
            this.endYear = i2;
            return this;
        }

        public Builder setRangDate(Calendar calendar, Calendar calendar2) {
            this.startDate = calendar;
            this.endDate = calendar2;
            return this;
        }

        public Builder setLineSpacingMultiplier(float f) {
            this.lineSpacingMultiplier = f;
            return this;
        }

        public Builder setDividerColor(int i) {
            this.dividerColor = i;
            return this;
        }

        public Builder setDividerType(WheelView.DividerType dividerType) {
            this.dividerType = dividerType;
            return this;
        }

        public Builder setTextColorCenter(int i) {
            this.textColorCenter = i;
            return this;
        }

        public Builder setTextColorOut(int i) {
            this.textColorOut = i;
            return this;
        }

        public Builder isCyclic(boolean z) {
            this.cyclic = z;
            return this;
        }

        public Builder setOutSideCancelable(boolean z) {
            this.cancelable = z;
            return this;
        }

        public Builder setLabel(String str, String str2, String str3, String str4, String str5, String str6) {
            this.label_year = str;
            this.label_month = str2;
            this.label_day = str3;
            this.label_hours = str4;
            this.label_mins = str5;
            this.label_seconds = str6;
            return this;
        }

        public TimePickerView build() {
            return new TimePickerView(this);
        }
    }

    private void initView(Context context) {
        initViews();
        init();
        initEvents();
        if (this.customListener == null) {
            LayoutInflater.from(context).inflate(R.layout.pickerview_time, this.contentContainer);
            this.tvTitle = (TextView) findViewById(R.id.tvTitle);
            this.btnSubmit = (Button) findViewById(R.id.btnSubmit);
            this.btnCancel = (Button) findViewById(R.id.btnCancel);
            this.btnSubmit.setTag(TAG_SUBMIT);
            this.btnCancel.setTag(TAG_CANCEL);
            this.btnSubmit.setOnClickListener(this);
            this.btnCancel.setOnClickListener(this);
            this.btnSubmit.setText(TextUtils.isEmpty(this.Str_Submit) ? context.getResources().getString(R.string.pickerview_submit) : this.Str_Submit);
            this.btnCancel.setText(TextUtils.isEmpty(this.Str_Cancel) ? context.getResources().getString(R.string.pickerview_cancel) : this.Str_Cancel);
            this.tvTitle.setText(TextUtils.isEmpty(this.Str_Title) ? "" : this.Str_Title);
            this.btnSubmit.setTextColor(this.Color_Submit == 0 ? this.pickerview_timebtn_nor : this.Color_Submit);
            this.btnCancel.setTextColor(this.Color_Cancel == 0 ? this.pickerview_timebtn_nor : this.Color_Cancel);
            this.tvTitle.setTextColor(this.Color_Title == 0 ? this.pickerview_topbar_title : this.Color_Title);
            this.btnSubmit.setTextSize(this.Size_Submit_Cancel);
            this.btnCancel.setTextSize(this.Size_Submit_Cancel);
            this.tvTitle.setTextSize(this.Size_Title);
            ((RelativeLayout) findViewById(R.id.rv_topbar)).setBackgroundColor(this.Color_Background_Title == 0 ? this.pickerview_bg_topbar : this.Color_Background_Title);
        } else {
            this.customListener.customLayout(LayoutInflater.from(context).inflate(this.layoutRes, this.contentContainer));
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.timepicker);
        linearLayout.setBackgroundColor(this.Color_Background_Wheel == 0 ? this.bgColor_default : this.Color_Background_Wheel);
        this.wheelTime = new WheelTime(linearLayout, this.type, this.gravity, this.Size_Content);
        if (this.startYear != 0 && this.endYear != 0 && this.startYear <= this.endYear) {
            setRange();
        }
        if (this.startDate == null || this.endDate == null) {
            if (this.startDate != null && this.endDate == null) {
                setRangDate();
            } else if (this.startDate == null && this.endDate != null) {
                setRangDate();
            }
        } else if (this.startDate.getTimeInMillis() <= this.endDate.getTimeInMillis()) {
            setRangDate();
        }
        setTime();
        this.wheelTime.setLabels(this.label_year, this.label_month, this.label_day, this.label_hours, this.label_mins, this.label_seconds);
        setOutSideCancelable(this.cancelable);
        this.wheelTime.setCyclic(this.cyclic);
        this.wheelTime.setDividerColor(this.dividerColor);
        this.wheelTime.setDividerType(this.dividerType);
        this.wheelTime.setLineSpacingMultiplier(this.lineSpacingMultiplier);
        this.wheelTime.setTextColorOut(this.textColorOut);
        this.wheelTime.setTextColorCenter(this.textColorCenter);
    }

    public void setDate(Calendar calendar) {
        this.date = calendar;
        setTime();
    }

    private void setRange() {
        this.wheelTime.setStartYear(this.startYear);
        this.wheelTime.setEndYear(this.endYear);
    }

    private void setRangDate() {
        this.wheelTime.setRangDate(this.startDate, this.endDate);
        if (this.startDate != null && this.endDate != null) {
            if (this.date == null || this.date.getTimeInMillis() < this.startDate.getTimeInMillis() || this.date.getTimeInMillis() > this.endDate.getTimeInMillis()) {
                this.date = this.startDate;
                return;
            }
            return;
        }
        if (this.startDate != null) {
            this.date = this.startDate;
        } else if (this.endDate != null) {
            this.date = this.endDate;
        }
    }

    private void setTime() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        Calendar calendar = Calendar.getInstance();
        if (this.date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            i = calendar.get(1);
            i2 = calendar.get(2);
            i3 = calendar.get(5);
            i4 = calendar.get(11);
            i5 = calendar.get(12);
            i6 = calendar.get(13);
        } else {
            i = this.date.get(1);
            i2 = this.date.get(2);
            i3 = this.date.get(5);
            i4 = this.date.get(11);
            i5 = this.date.get(12);
            i6 = this.date.get(13);
        }
        this.wheelTime.setPicker(i, i2, i3, i4, i5, i6);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws ParseException {
        if (((String) view.getTag()).equals(TAG_CANCEL)) {
            dismiss();
        } else {
            returnData(view);
        }
    }

    public void returnData(View view) throws ParseException {
        if (this.timeSelectListener != null) {
            try {
                this.timeSelectListener.onTimeSelect(WheelTime.dateFormat.parse(this.wheelTime.getTime()), view);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        dismiss();
    }

    @Override // com.bigkoo.pickerview.view.BasePickerView
    public boolean isDialog() {
        return this.isDialog;
    }
}
