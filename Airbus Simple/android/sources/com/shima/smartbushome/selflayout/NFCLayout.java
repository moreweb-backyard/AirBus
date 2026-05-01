package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savenfc;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class NFCLayout extends LinearLayout implements View.OnLongClickListener {
    CheckBox cb_delete;
    private String[] colorarray;
    boolean deletemode;
    LayoutInflater inflater;
    boolean init;
    RelativeLayout nfc_selflayout;
    Switch nfc_switch;
    Savenfc nfccontent;
    Context rootcontext;
    TextView tv_detail;
    TextView tv_name;
    View view;

    public NFCLayout(Context context) {
        super(context);
        this.deletemode = false;
        this.init = false;
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        initview(context);
    }

    public NFCLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.init = false;
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.nfc_layout, this);
        this.rootcontext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        initUI();
    }

    public void initUI() {
        this.tv_detail = (TextView) this.view.findViewById(R.id.nfc_detail);
        this.tv_name = (TextView) this.view.findViewById(R.id.nfc_name);
        this.cb_delete = (CheckBox) this.view.findViewById(R.id.nfc_delete);
        this.nfc_switch = (Switch) this.view.findViewById(R.id.nfc_switch);
        this.nfc_selflayout = (RelativeLayout) this.view.findViewById(R.id.nfc_selflayout);
        this.tv_name.setOnLongClickListener(this);
        this.nfc_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.NFCLayout.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (NFCLayout.this.init) {
                    if (z) {
                        NFCLayout.this.nfccontent.state = 0;
                        MainActivity.mgr.updatenfc_status(NFCLayout.this.nfccontent);
                    } else {
                        NFCLayout.this.nfccontent.state = 1;
                        MainActivity.mgr.updatenfc_status(NFCLayout.this.nfccontent);
                    }
                }
            }
        });
    }

    private void broadcastUpdate(String str) {
        FounctionActivity.fcontext.sendBroadcast(new Intent(str));
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        view.getId();
        return true;
    }

    public void setcontan(Savenfc savenfc) {
        this.nfccontent = savenfc;
        setname(savenfc.nfc_name);
        switch (savenfc.action_type) {
            case 1:
                setcontent("Marco:" + savenfc.marco_name);
                break;
            case 2:
                setcontent("Call:" + savenfc.call_num);
                break;
            case 3:
                setcontent("Message:To " + savenfc.call_num + IOUtils.LINE_SEPARATOR_UNIX + savenfc.message);
                break;
        }
        switch (savenfc.state) {
            case 0:
                this.nfc_switch.setChecked(true);
                break;
            case 1:
                this.nfc_switch.setChecked(false);
                break;
        }
        this.nfc_selflayout.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.init = true;
    }

    public void setname(String str) {
        this.tv_name.setText(str);
    }

    public void setcontent(String str) {
        this.tv_detail.setText(str);
    }

    public void setdeletevisable(boolean z) {
        if (z) {
            this.cb_delete.setVisibility(0);
            this.deletemode = true;
            this.cb_delete.setChecked(false);
        } else {
            this.cb_delete.setVisibility(4);
            this.deletemode = false;
        }
    }

    public boolean getIfneedtoDelete() {
        return this.cb_delete.isChecked();
    }

    public int getnfcid() {
        return this.nfccontent.nfc_id;
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
