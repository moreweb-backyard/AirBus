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
import com.bigkoo.pickerview.view.WheelOptions;
import java.util.List;

/* loaded from: classes.dex */
public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener {
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
    private boolean cyclic1;
    private boolean cyclic2;
    private boolean cyclic3;
    private int dividerColor;
    private WheelView.DividerType dividerType;
    private boolean isDialog;
    private String label1;
    private String label2;
    private String label3;
    private int layoutRes;
    private float lineSpacingMultiplier;
    private boolean linkage;
    private int option1;
    private int option2;
    private int option3;
    private OnOptionsSelectListener optionsSelectListener;
    private int textColorCenter;
    private int textColorOut;
    private TextView tvTitle;
    WheelOptions<T> wheelOptions;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(int i, int i2, int i3, View view);
    }

    public OptionsPickerView(Builder builder) {
        super(builder.context);
        this.lineSpacingMultiplier = 1.6f;
        this.optionsSelectListener = builder.optionsSelectListener;
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
        this.cyclic1 = builder.cyclic1;
        this.cyclic2 = builder.cyclic2;
        this.cyclic3 = builder.cyclic3;
        this.cancelable = builder.cancelable;
        this.linkage = builder.linkage;
        this.label1 = builder.label1;
        this.label2 = builder.label2;
        this.label3 = builder.label3;
        this.option1 = builder.option1;
        this.option2 = builder.option2;
        this.option3 = builder.option3;
        this.textColorCenter = builder.textColorCenter;
        this.textColorOut = builder.textColorOut;
        this.dividerColor = builder.dividerColor;
        this.lineSpacingMultiplier = builder.lineSpacingMultiplier;
        this.customListener = builder.customListener;
        this.layoutRes = builder.layoutRes;
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
        private int dividerColor;
        private WheelView.DividerType dividerType;
        private boolean isDialog;
        private String label1;
        private String label2;
        private String label3;
        private int option1;
        private int option2;
        private int option3;
        private OnOptionsSelectListener optionsSelectListener;
        private int textColorCenter;
        private int textColorOut;
        private int layoutRes = R.layout.pickerview_options;
        private int Size_Submit_Cancel = 17;
        private int Size_Title = 18;
        private int Size_Content = 18;
        private boolean cancelable = true;
        private boolean linkage = true;
        private float lineSpacingMultiplier = 1.6f;
        private boolean cyclic1 = false;
        private boolean cyclic2 = false;
        private boolean cyclic3 = false;

        public Builder(Context context, OnOptionsSelectListener onOptionsSelectListener) {
            this.context = context;
            this.optionsSelectListener = onOptionsSelectListener;
        }

        public Builder setSubmitText(String str) {
            this.Str_Submit = str;
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

        public Builder isDialog(boolean z) {
            this.isDialog = z;
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

        public Builder setLayoutRes(int i, CustomListener customListener) {
            this.layoutRes = i;
            this.customListener = customListener;
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

        public Builder setContentTextSize(int i) {
            this.Size_Content = i;
            return this;
        }

        public Builder setOutSideCancelable(boolean z) {
            this.cancelable = z;
            return this;
        }

        public Builder setLinkage(boolean z) {
            this.linkage = z;
            return this;
        }

        public Builder setLabels(String str, String str2, String str3) {
            this.label1 = str;
            this.label2 = str2;
            this.label3 = str3;
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

        public Builder setCyclic(boolean z, boolean z2, boolean z3) {
            this.cyclic1 = z;
            this.cyclic2 = z2;
            this.cyclic3 = z3;
            return this;
        }

        public Builder setSelectOptions(int i) {
            this.option1 = i;
            return this;
        }

        public Builder setSelectOptions(int i, int i2) {
            this.option1 = i;
            this.option2 = i2;
            return this;
        }

        public Builder setSelectOptions(int i, int i2, int i3) {
            this.option1 = i;
            this.option2 = i2;
            this.option3 = i3;
            return this;
        }

        public OptionsPickerView build() {
            return new OptionsPickerView(this);
        }
    }

    private void initView(Context context) {
        initViews();
        init();
        initEvents();
        if (this.customListener == null) {
            LayoutInflater.from(context).inflate(this.layoutRes, this.contentContainer);
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
            ((RelativeLayout) findViewById(R.id.rv_topbar)).setBackgroundColor(this.Color_Background_Title == 0 ? this.pickerview_bg_topbar : this.Color_Background_Title);
            this.btnSubmit.setTextSize(this.Size_Submit_Cancel);
            this.btnCancel.setTextSize(this.Size_Submit_Cancel);
            this.tvTitle.setTextSize(this.Size_Title);
            this.tvTitle.setText(this.Str_Title);
        } else {
            this.customListener.customLayout(LayoutInflater.from(context).inflate(this.layoutRes, this.contentContainer));
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.optionspicker);
        linearLayout.setBackgroundColor(this.Color_Background_Wheel == 0 ? this.bgColor_default : this.Color_Background_Wheel);
        this.wheelOptions = new WheelOptions<>(linearLayout, Boolean.valueOf(this.linkage));
        this.wheelOptions.setTextContentSize(this.Size_Content);
        this.wheelOptions.setLabels(this.label1, this.label2, this.label3);
        this.wheelOptions.setCyclic(this.cyclic1, this.cyclic2, this.cyclic3);
        setOutSideCancelable(this.cancelable);
        if (this.tvTitle != null) {
            this.tvTitle.setText(this.Str_Title);
        }
        this.wheelOptions.setDividerColor(this.dividerColor);
        this.wheelOptions.setDividerType(this.dividerType);
        this.wheelOptions.setLineSpacingMultiplier(this.lineSpacingMultiplier);
        this.wheelOptions.setTextColorOut(this.textColorOut);
        this.wheelOptions.setTextColorCenter(this.textColorCenter);
    }

    public void setSelectOptions(int i) {
        this.option1 = i;
        SetCurrentItems();
    }

    public void setSelectOptions(int i, int i2) {
        this.option1 = i;
        this.option2 = i2;
        SetCurrentItems();
    }

    public void setSelectOptions(int i, int i2, int i3) {
        this.option1 = i;
        this.option2 = i2;
        this.option3 = i3;
        SetCurrentItems();
    }

    private void SetCurrentItems() {
        if (this.wheelOptions != null) {
            this.wheelOptions.setCurrentItems(this.option1, this.option2, this.option3);
        }
    }

    public void setPicker(List<T> list) {
        this.wheelOptions.setPicker(list, null, null);
        SetCurrentItems();
    }

    public void setPicker(List<T> list, List<List<T>> list2) {
        this.wheelOptions.setPicker(list, list2, null);
        SetCurrentItems();
    }

    public void setPicker(List<T> list, List<List<T>> list2, List<List<List<T>>> list3) {
        this.wheelOptions.setPicker(list, list2, list3);
        SetCurrentItems();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (((String) view.getTag()).equals(TAG_CANCEL)) {
            dismiss();
        } else {
            returnData(view);
        }
    }

    public void returnData(View view) {
        if (this.optionsSelectListener != null) {
            int[] currentItems = this.wheelOptions.getCurrentItems();
            this.optionsSelectListener.onOptionsSelect(currentItems[0], currentItems[1], currentItems[2], view);
        }
        dismiss();
    }

    @Override // com.bigkoo.pickerview.view.BasePickerView
    public boolean isDialog() {
        return this.isDialog;
    }
}
