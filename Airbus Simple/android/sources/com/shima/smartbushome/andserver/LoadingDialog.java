package com.shima.smartbushome.andserver;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.shima.smartbushome.R;

/* loaded from: classes.dex */
public class LoadingDialog {
    boolean isShow = false;
    Dialog mLoadingDialog;
    LVCircularRing mLoadingView;

    public LoadingDialog(Context context, String str) {
        View viewInflate = LayoutInflater.from(context).inflate(R.layout.loading_dialog_view, (ViewGroup) null);
        LinearLayout linearLayout = (LinearLayout) viewInflate.findViewById(R.id.dialog_view);
        this.mLoadingView = (LVCircularRing) viewInflate.findViewById(R.id.lv_circularring);
        ((TextView) viewInflate.findViewById(R.id.loading_text)).setText(str);
        this.mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        this.mLoadingDialog.setCancelable(false);
        this.mLoadingDialog.setContentView(linearLayout, new LinearLayout.LayoutParams(-1, -1));
    }

    public void show() {
        this.mLoadingDialog.show();
        this.mLoadingView.startAnim();
        this.isShow = true;
    }

    public void close() {
        if (this.mLoadingDialog != null) {
            this.mLoadingView.stopAnim();
            this.mLoadingDialog.dismiss();
            this.mLoadingDialog = null;
        }
        this.isShow = false;
    }

    public boolean isShow() {
        return this.isShow;
    }
}
