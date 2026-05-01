package com.shima.smartbushome.assist.scheduleutil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shima.smartbushome.R;

/* loaded from: classes.dex */
public class SimpleDialog extends Dialog {
    public static int TYPE_NO_BT = 0;
    public static int TYPE_TWO_BT = 2;
    protected static int default_height = -2;
    protected static int default_width = -2;
    public Button bt_cancel;
    public Button bt_confirm;
    private View customView;
    public EditText dialog_message;
    public TextView dialog_title;
    ImageView icon;
    private View.OnClickListener listener;
    private LinearLayout ll_button;
    protected Context mContext;

    public SimpleDialog(Context context, int i) {
        super(context, R.style.FullScreenDialog);
        this.mContext = context;
        this.customView = LayoutInflater.from(context).inflate(R.layout.dialog_simple, (ViewGroup) null);
        this.icon = (ImageView) this.customView.findViewById(R.id.icon);
        this.ll_button = (LinearLayout) this.customView.findViewById(R.id.ll_button);
        this.dialog_title = (TextView) this.customView.findViewById(R.id.dialog_title);
        setTitle("提示信息");
        this.dialog_message = (EditText) this.customView.findViewById(R.id.dialog_message);
        this.dialog_message.clearFocus();
        this.bt_confirm = (Button) this.customView.findViewById(R.id.dialog_confirm);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            return false;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(this.customView);
    }

    public SimpleDialog setClickListener(View.OnClickListener onClickListener) {
        this.listener = onClickListener;
        this.bt_confirm.setOnClickListener(onClickListener);
        return this;
    }

    public SimpleDialog setMessage(String str) {
        this.dialog_message.setText(str);
        return this;
    }

    public SimpleDialog setTitle(String str) {
        this.dialog_title.setText(str);
        return this;
    }

    public SimpleDialog setIcon(int i) {
        this.dialog_title.setVisibility(8);
        this.icon.setVisibility(0);
        this.icon.setBackgroundResource(i);
        return this;
    }
}
