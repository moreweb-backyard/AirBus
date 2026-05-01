package com.shima.smartbushome.selflayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.SwipeLayout;
import com.shima.smartbushome.assist.holocolorpicker.ColorPicker;
import com.shima.smartbushome.assist.holocolorpicker.SVBar;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.udp.CONST;

/* loaded from: classes.dex */
public class LightType3 extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private String[] colorarray;
    LinearLayout delete;
    private CheckBox deleteview;
    EditText dev;
    ColorDrawable dr;
    AlertView iconalter;
    private ImageView im_image;
    LayoutInflater inflater;
    lightcontrol lc;
    private Savelight lightcontent;
    EditText name;
    int oldcolor;
    Context rootcontext;
    private Button setcolor;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    EditText sub;
    private TextView tv_color;
    private TextView tv_remark;
    private TextView tv_state;
    RadioButton type1;
    RadioButton type2;
    RadioButton type3;
    LinearLayout type3linear;
    RadioButton type4;
    View view;

    public LightType3(Context context) {
        super(context);
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.oldcolor = Color.argb(255, 255, 255, 255);
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType3.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == LightType3.this.settingalter && i != -1 && i == 0) {
                    Savelight savelight = new Savelight();
                    savelight.room_id = LightType3.this.lightcontent.room_id;
                    savelight.light_id = LightType3.this.lightcontent.light_id;
                    savelight.subnetID = Integer.parseInt(LightType3.this.sub.getText().toString().trim());
                    savelight.deviceID = Integer.parseInt(LightType3.this.dev.getText().toString().trim());
                    savelight.channel = LightType3.this.lightcontent.channel;
                    savelight.light_statement = LightType3.this.name.getText().toString().trim();
                    if (LightType3.this.type1.isChecked()) {
                        savelight.lightType = 1;
                    } else if (LightType3.this.type2.isChecked()) {
                        savelight.lightType = 2;
                    } else if (LightType3.this.type3.isChecked()) {
                        savelight.lightType = 3;
                    } else if (LightType3.this.type4.isChecked()) {
                        savelight.lightType = 4;
                    }
                    savelight.light_icon = LightType3.this.lightcontent.light_icon;
                    MainActivity.mgr.updatelight(savelight);
                    LightType3.this.lightcontent.subnetID = Integer.parseInt(LightType3.this.sub.getText().toString().trim());
                    LightType3.this.lightcontent.deviceID = Integer.parseInt(LightType3.this.dev.getText().toString().trim());
                    LightType3.this.lightcontent.light_statement = LightType3.this.name.getText().toString().trim();
                    LightType3.this.setremark(LightType3.this.lightcontent.light_statement);
                    if (savelight.lightType != 3) {
                        LightType3.this.broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
                    }
                }
            }
        };
        initview(context);
    }

    public LightType3(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.oldcolor = Color.argb(255, 255, 255, 255);
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType3.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == LightType3.this.settingalter && i != -1 && i == 0) {
                    Savelight savelight = new Savelight();
                    savelight.room_id = LightType3.this.lightcontent.room_id;
                    savelight.light_id = LightType3.this.lightcontent.light_id;
                    savelight.subnetID = Integer.parseInt(LightType3.this.sub.getText().toString().trim());
                    savelight.deviceID = Integer.parseInt(LightType3.this.dev.getText().toString().trim());
                    savelight.channel = LightType3.this.lightcontent.channel;
                    savelight.light_statement = LightType3.this.name.getText().toString().trim();
                    if (LightType3.this.type1.isChecked()) {
                        savelight.lightType = 1;
                    } else if (LightType3.this.type2.isChecked()) {
                        savelight.lightType = 2;
                    } else if (LightType3.this.type3.isChecked()) {
                        savelight.lightType = 3;
                    } else if (LightType3.this.type4.isChecked()) {
                        savelight.lightType = 4;
                    }
                    savelight.light_icon = LightType3.this.lightcontent.light_icon;
                    MainActivity.mgr.updatelight(savelight);
                    LightType3.this.lightcontent.subnetID = Integer.parseInt(LightType3.this.sub.getText().toString().trim());
                    LightType3.this.lightcontent.deviceID = Integer.parseInt(LightType3.this.dev.getText().toString().trim());
                    LightType3.this.lightcontent.light_statement = LightType3.this.name.getText().toString().trim();
                    LightType3.this.setremark(LightType3.this.lightcontent.light_statement);
                    if (savelight.lightType != 3) {
                        LightType3.this.broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
                    }
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.light_type3, this);
        this.rootcontext = context;
        this.type3linear = (LinearLayout) this.view.findViewById(R.id.type3linear);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.delete = (LinearLayout) this.view.findViewById(R.id.lighttype3_delete);
        SwipeLayout.addSwipeView((SwipeLayout) findViewById(R.id.lighttype3swipe));
        this.tv_remark = (TextView) this.view.findViewById(R.id.ltype3_remark);
        this.tv_color = (TextView) this.view.findViewById(R.id.color);
        this.tv_state = (TextView) this.view.findViewById(R.id.tv_state);
        this.im_image = (ImageView) this.view.findViewById(R.id.ltype3_icon);
        this.setcolor = (Button) this.view.findViewById(R.id.setcolor);
        this.deleteview = (CheckBox) this.view.findViewById(R.id.ltype_checkbox);
        this.lc = new lightcontrol();
        this.im_image.setOnClickListener(this);
        this.setcolor.setOnClickListener(this);
        this.delete.setOnClickListener(this);
        this.tv_remark.setOnLongClickListener(this);
        setimage(R.mipmap.light_type3_off);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.lighttype3_delete) {
            MainActivity.mgr.deletelight("light", this.lightcontent.light_id, this.lightcontent.room_id);
            broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
            Toast.makeText(this.rootcontext, "delete succeed", 0).show();
            return;
        }
        if (id != R.id.ltype3_icon) {
            if (id != R.id.setcolor) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), 3);
            View viewInflate = this.inflater.inflate(R.layout.view_pickcolor, (ViewGroup) null);
            final ColorPicker colorPicker = (ColorPicker) viewInflate.findViewById(R.id.view5);
            colorPicker.addSVBar((SVBar) viewInflate.findViewById(R.id.view7));
            ColorDrawable colorDrawable = (ColorDrawable) this.tv_color.getBackground();
            if (colorDrawable != null) {
                colorPicker.setColor(colorDrawable.getColor());
            }
            builder.setTitle("Color Selection").setView(viewInflate).setCancelable(false).setPositiveButton("RUN", new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.selflayout.LightType3.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    LightType3.this.tv_color.setBackgroundColor(colorPicker.getColor());
                    if (Integer.toHexString(colorPicker.getColor()).equals("ff000000")) {
                        LightType3.this.tv_state.setText(CONST.CONST_STR_OFF);
                        LightType3.this.setimage(R.mipmap.light_type3_off);
                    } else {
                        LightType3.this.tv_state.setText(CONST.CONST_STR_ON);
                        LightType3.this.setimage(R.mipmap.light_type3_on);
                    }
                    LightType3.this.lc.ARGBlightcontrol((byte) LightType3.this.lightcontent.subnetID, (byte) LightType3.this.lightcontent.deviceID, colorPicker.getColor(), MainActivity.mydupsocket);
                    dialogInterface.dismiss();
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.selflayout.LightType3.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
            return;
        }
        String strTrim = this.tv_state.getText().toString().trim();
        if (strTrim.equals(CONST.CONST_STR_ON)) {
            this.oldcolor = ((ColorDrawable) this.tv_color.getBackground()).getColor();
            int iArgb = Color.argb(255, 0, 0, 0);
            setimage(R.mipmap.light_type3_off);
            this.tv_color.setBackgroundColor(iArgb);
            this.lc.ARGBlightcontrol((byte) this.lightcontent.subnetID, (byte) this.lightcontent.deviceID, iArgb, MainActivity.mydupsocket);
            this.tv_state.setText(CONST.CONST_STR_OFF);
            return;
        }
        if (strTrim.equals(CONST.CONST_STR_OFF) || strTrim.equals(EnvironmentCompat.MEDIA_UNKNOWN)) {
            this.tv_color.setBackgroundColor(this.oldcolor);
            setimage(R.mipmap.light_type3_on);
            this.lc.ARGBlightcontrol((byte) this.lightcontent.subnetID, (byte) this.lightcontent.deviceID, this.oldcolor, MainActivity.mydupsocket);
            this.tv_state.setText(CONST.CONST_STR_ON);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str) {
        FounctionActivity.fcontext.sendBroadcast(new Intent(str));
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (MainActivity.islockchangeid) {
            return true;
        }
        showPopupMenu(this.tv_remark);
        return true;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType3.3
            @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_open /* 2131230749 */:
                        LightType3.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, LightType3.this.rootcontext, AlertView.Style.Alert, LightType3.this.settingclick);
                        View viewInflate = LightType3.this.inflater.inflate(R.layout.setting_lighttypeinfo, (ViewGroup) null);
                        LightType3.this.sub = (EditText) viewInflate.findViewById(R.id.lededitTextsub);
                        LightType3.this.dev = (EditText) viewInflate.findViewById(R.id.lededitText2dev);
                        LightType3.this.name = (EditText) viewInflate.findViewById(R.id.lededitTextname);
                        LightType3.this.type1 = (RadioButton) viewInflate.findViewById(R.id.light3type1);
                        LightType3.this.type2 = (RadioButton) viewInflate.findViewById(R.id.light3type2);
                        LightType3.this.type3 = (RadioButton) viewInflate.findViewById(R.id.light3type3);
                        LightType3.this.type4 = (RadioButton) viewInflate.findViewById(R.id.light3type4);
                        LightType3.this.type3.setChecked(true);
                        LightType3.this.sub.setText(String.valueOf(LightType3.this.lightcontent.subnetID));
                        LightType3.this.dev.setText(String.valueOf(LightType3.this.lightcontent.deviceID));
                        LightType3.this.name.setText(LightType3.this.lightcontent.light_statement);
                        LightType3.this.settingalter.addExtView(viewInflate);
                        LightType3.this.settingalter.show();
                        break;
                    case R.id.action_pair /* 2131230750 */:
                        final AlertView alertView = new AlertView("Select Device", null, "CANCEL", null, null, LightType3.this.rootcontext, AlertView.Style.Alert, null);
                        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(LightType3.this.rootcontext).inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                        ListView listView = (ListView) viewGroup.findViewById(R.id.listView4);
                        listView.setAdapter((ListAdapter) new DeviceListAdapter(LightType3.this.rootcontext, MainActivity.netdeviceList));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType3.3.1
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                Savelight savelight = new Savelight();
                                savelight.room_id = LightType3.this.lightcontent.room_id;
                                savelight.light_id = LightType3.this.lightcontent.light_id;
                                savelight.light_statement = LightType3.this.lightcontent.light_statement;
                                savelight.channel = LightType3.this.lightcontent.channel;
                                savelight.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                                savelight.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                                savelight.light_icon = LightType3.this.lightcontent.light_icon;
                                savelight.lightType = LightType3.this.lightcontent.lightType;
                                MainActivity.mgr.updatelight(savelight);
                                Toast.makeText(LightType3.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                                LightType3.this.lightcontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                                LightType3.this.lightcontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                                alertView.dismiss();
                            }
                        });
                        alertView.addExtView(viewGroup);
                        alertView.show();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void setcontant(Savelight savelight) {
        this.lightcontent = savelight;
        this.type3linear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.tv_remark.setText(savelight.light_statement);
    }

    public void setimage(int i) {
        this.im_image.setImageResource(i);
    }

    public void setremark(String str) {
        this.tv_remark.setText(str);
    }

    public void setdeletevisable(boolean z) {
        if (z) {
            this.deleteview.setVisibility(0);
            this.deleteview.setChecked(false);
        } else {
            this.deleteview.setVisibility(4);
        }
    }

    public void setReceiveChange(byte[] bArr) {
        if (bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0) {
            this.tv_state.setText(CONST.CONST_STR_OFF);
            this.tv_color.setBackgroundColor(Color.argb(255, 0, 0, 0));
            setimage(R.mipmap.light_type3_off);
            return;
        }
        this.tv_color.setBackgroundColor(ToColor(bArr));
        this.tv_state.setText(CONST.CONST_STR_ON);
        setimage(R.mipmap.light_type3_on);
    }

    public int ToColor(byte[] bArr) {
        int i = ((bArr[0] & 255) * 255) / 100;
        int i2 = ((bArr[1] & 255) * 255) / 100;
        int i3 = ((bArr[2] & 255) * 255) / 100;
        String hexString = Integer.toHexString(i);
        switch (hexString.length()) {
            case 0:
                hexString = "00";
                break;
            case 1:
                hexString = "0" + hexString;
                break;
        }
        String hexString2 = Integer.toHexString(i2);
        switch (hexString2.length()) {
            case 0:
                hexString2 = "00";
                break;
            case 1:
                hexString2 = "0" + hexString2;
                break;
        }
        String hexString3 = Integer.toHexString(i3);
        switch (hexString3.length()) {
            case 0:
                hexString3 = "00";
                break;
            case 1:
                hexString3 = "0" + hexString3;
                break;
        }
        return Color.argb(255, Integer.parseInt(hexString, 16), Integer.parseInt(hexString2, 16), Integer.parseInt(hexString3, 16));
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public int getType3lightid() {
        return this.lightcontent.light_id;
    }

    public int getsubid() {
        return this.lightcontent.subnetID;
    }

    public int getdevid() {
        return this.lightcontent.deviceID;
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
