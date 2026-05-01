package com.shima.smartbushome.selflayout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.SwipeLayout;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.founction_command.curtaincontrol;

/* loaded from: classes.dex */
public class CurtainType1 extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    Dialog aupairdialog;
    private Button bt_close;
    private Button bt_open;
    private Button bt_stop;
    curtaincontrol cc;
    EditText cha1;
    EditText cha2;
    private String[] colorarray;
    Savecurtain curtaincontent;
    LinearLayout curtainlinear;
    LinearLayout delete;
    boolean deletemode;
    private CheckBox deleteview;
    EditText dev;
    private ImageView im_image;
    LayoutInflater inflater;
    public OnItemClickListener itemclick;
    public DBManager mgr;
    EditText name;
    AlertView pairalter;
    boolean receiveChange;
    Context rootcontext;
    View selfview;
    AlertView settingalter;
    private Space sp1;
    private Space sp2;
    EditText sub;
    private TextView tv_remark;
    private TextView tv_state;
    View view;

    public CurtainType1(Context context) {
        super(context);
        this.deletemode = false;
        this.receiveChange = false;
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.CurtainType1.2
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj != CurtainType1.this.settingalter) {
                    AlertView alertView = CurtainType1.this.pairalter;
                    return;
                }
                if (i == 0) {
                    Savecurtain savecurtain = new Savecurtain();
                    savecurtain.room_id = CurtainType1.this.curtaincontent.room_id;
                    savecurtain.curtain_id = CurtainType1.this.curtaincontent.curtain_id;
                    savecurtain.subnetID = Integer.parseInt(CurtainType1.this.sub.getText().toString().trim());
                    savecurtain.deviceID = Integer.parseInt(CurtainType1.this.dev.getText().toString().trim());
                    savecurtain.channel_1 = Integer.parseInt(CurtainType1.this.cha1.getText().toString().trim());
                    savecurtain.channel_2 = Integer.parseInt(CurtainType1.this.cha2.getText().toString().trim());
                    savecurtain.curtain_remark = CurtainType1.this.name.getText().toString().trim();
                    MainActivity.mgr.updatecurtain(savecurtain);
                    CurtainType1.this.curtaincontent.subnetID = Integer.parseInt(CurtainType1.this.sub.getText().toString().trim());
                    CurtainType1.this.curtaincontent.deviceID = Integer.parseInt(CurtainType1.this.dev.getText().toString().trim());
                    CurtainType1.this.curtaincontent.channel_1 = Integer.parseInt(CurtainType1.this.cha1.getText().toString().trim());
                    CurtainType1.this.curtaincontent.channel_2 = Integer.parseInt(CurtainType1.this.cha2.getText().toString().trim());
                    CurtainType1.this.curtaincontent.curtain_remark = CurtainType1.this.name.getText().toString().trim();
                    CurtainType1.this.setremark(CurtainType1.this.curtaincontent.curtain_remark);
                }
            }
        };
        initview(context);
    }

    public CurtainType1(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.receiveChange = false;
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.CurtainType1.2
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj != CurtainType1.this.settingalter) {
                    AlertView alertView = CurtainType1.this.pairalter;
                    return;
                }
                if (i == 0) {
                    Savecurtain savecurtain = new Savecurtain();
                    savecurtain.room_id = CurtainType1.this.curtaincontent.room_id;
                    savecurtain.curtain_id = CurtainType1.this.curtaincontent.curtain_id;
                    savecurtain.subnetID = Integer.parseInt(CurtainType1.this.sub.getText().toString().trim());
                    savecurtain.deviceID = Integer.parseInt(CurtainType1.this.dev.getText().toString().trim());
                    savecurtain.channel_1 = Integer.parseInt(CurtainType1.this.cha1.getText().toString().trim());
                    savecurtain.channel_2 = Integer.parseInt(CurtainType1.this.cha2.getText().toString().trim());
                    savecurtain.curtain_remark = CurtainType1.this.name.getText().toString().trim();
                    MainActivity.mgr.updatecurtain(savecurtain);
                    CurtainType1.this.curtaincontent.subnetID = Integer.parseInt(CurtainType1.this.sub.getText().toString().trim());
                    CurtainType1.this.curtaincontent.deviceID = Integer.parseInt(CurtainType1.this.dev.getText().toString().trim());
                    CurtainType1.this.curtaincontent.channel_1 = Integer.parseInt(CurtainType1.this.cha1.getText().toString().trim());
                    CurtainType1.this.curtaincontent.channel_2 = Integer.parseInt(CurtainType1.this.cha2.getText().toString().trim());
                    CurtainType1.this.curtaincontent.curtain_remark = CurtainType1.this.name.getText().toString().trim();
                    CurtainType1.this.setremark(CurtainType1.this.curtaincontent.curtain_remark);
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.curtain_type1, this);
        this.rootcontext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.tv_remark = (TextView) this.view.findViewById(R.id.curtainrmark);
        this.curtainlinear = (LinearLayout) this.view.findViewById(R.id.curtainlinear);
        this.tv_state = (TextView) this.view.findViewById(R.id.curtainstate);
        this.im_image = (ImageView) this.view.findViewById(R.id.curtaintype1imageView);
        this.bt_open = (Button) this.view.findViewById(R.id.curtainon);
        this.bt_stop = (Button) this.view.findViewById(R.id.curtainstop);
        this.bt_close = (Button) this.view.findViewById(R.id.curtainoff);
        this.deleteview = (CheckBox) this.view.findViewById(R.id.curtaindelete);
        this.delete = (LinearLayout) this.view.findViewById(R.id.curtaintype1_delete);
        SwipeLayout.addSwipeView((SwipeLayout) findViewById(R.id.curtaintype1swipe));
        this.bt_open.setOnClickListener(this);
        this.bt_stop.setOnClickListener(this);
        this.bt_close.setOnClickListener(this);
        this.im_image.setOnClickListener(this);
        this.delete.setOnClickListener(this);
        this.tv_remark.setOnLongClickListener(this);
        this.cc = new curtaincontrol();
        setimage(R.mipmap.curtain_icon1_off);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.curtainoff /* 2131230968 */:
                this.cc.CurtainControl((byte) this.curtaincontent.subnetID, (byte) this.curtaincontent.deviceID, this.curtaincontent.channel_1, this.curtaincontent.channel_2, "close", MainActivity.mydupsocket);
                setstate("close");
                setimage(R.mipmap.curtain_icon1_off);
                setDBcurtainstate("close");
                break;
            case R.id.curtainon /* 2131230969 */:
                this.cc.CurtainControl((byte) this.curtaincontent.subnetID, (byte) this.curtaincontent.deviceID, this.curtaincontent.channel_1, this.curtaincontent.channel_2, "open", MainActivity.mydupsocket);
                setstate("open");
                setimage(R.mipmap.curtain_icon1_on);
                setDBcurtainstate("open");
                break;
            default:
                switch (id) {
                    case R.id.curtainstop /* 2131230975 */:
                        this.cc.CurtainControl((byte) this.curtaincontent.subnetID, (byte) this.curtaincontent.deviceID, this.curtaincontent.channel_1, this.curtaincontent.channel_2, "stop", MainActivity.mydupsocket);
                        setstate("stop");
                        break;
                    case R.id.curtaintype1_delete /* 2131230976 */:
                        MainActivity.mgr.deletecurtain("curtain", this.curtaincontent.curtain_id, this.curtaincontent.room_id);
                        broadcastUpdate(FounctionActivity.ACTION_DELETECURTAIN);
                        Toast.makeText(this.rootcontext, "delete succeed", 0).show();
                        break;
                    case R.id.curtaintype1imageView /* 2131230977 */:
                        String strTrim = this.tv_state.getText().toString().trim();
                        if (strTrim.equals(EnvironmentCompat.MEDIA_UNKNOWN) || strTrim.equals("close") || strTrim.equals("stop")) {
                            this.cc.CurtainControl((byte) this.curtaincontent.subnetID, (byte) this.curtaincontent.deviceID, this.curtaincontent.channel_1, this.curtaincontent.channel_2, "open", MainActivity.mydupsocket);
                            setstate("open");
                            setimage(R.mipmap.curtain_icon1_on);
                            setDBcurtainstate("open");
                            break;
                        } else {
                            this.cc.CurtainControl((byte) this.curtaincontent.subnetID, (byte) this.curtaincontent.deviceID, this.curtaincontent.channel_1, this.curtaincontent.channel_2, "close", MainActivity.mydupsocket);
                            setstate("close");
                            setimage(R.mipmap.curtain_icon1_off);
                            setDBcurtainstate("close");
                            break;
                        }
                        break;
                }
        }
    }

    private void broadcastUpdate(String str) {
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
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.shima.smartbushome.selflayout.CurtainType1.1
            @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_open /* 2131230749 */:
                        CurtainType1.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, CurtainType1.this.rootcontext, AlertView.Style.Alert, CurtainType1.this.itemclick);
                        View viewInflate = CurtainType1.this.inflater.inflate(R.layout.setting_curtaininfo, (ViewGroup) null);
                        CurtainType1.this.sub = (EditText) viewInflate.findViewById(R.id.subidedit);
                        CurtainType1.this.dev = (EditText) viewInflate.findViewById(R.id.devidedit);
                        CurtainType1.this.cha1 = (EditText) viewInflate.findViewById(R.id.chan1edit);
                        CurtainType1.this.cha2 = (EditText) viewInflate.findViewById(R.id.chan2edit);
                        CurtainType1.this.name = (EditText) viewInflate.findViewById(R.id.curtainremarkedit);
                        CurtainType1.this.sub.setText(String.valueOf(CurtainType1.this.curtaincontent.subnetID));
                        CurtainType1.this.dev.setText(String.valueOf(CurtainType1.this.curtaincontent.deviceID));
                        CurtainType1.this.cha1.setText(String.valueOf(CurtainType1.this.curtaincontent.channel_1));
                        CurtainType1.this.cha2.setText(String.valueOf(CurtainType1.this.curtaincontent.channel_2));
                        CurtainType1.this.name.setText(CurtainType1.this.curtaincontent.curtain_remark);
                        CurtainType1.this.settingalter.addExtView(viewInflate);
                        CurtainType1.this.settingalter.show();
                        break;
                    case R.id.action_pair /* 2131230750 */:
                        CurtainType1.this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, CurtainType1.this.rootcontext, AlertView.Style.Alert, CurtainType1.this.itemclick);
                        View viewInflate2 = CurtainType1.this.inflater.inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                        ListView listView = (ListView) viewInflate2.findViewById(R.id.listView4);
                        listView.setAdapter((ListAdapter) new DeviceListAdapter(CurtainType1.this.rootcontext, MainActivity.netdeviceList));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.CurtainType1.1.1
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                Savecurtain savecurtain = new Savecurtain();
                                savecurtain.room_id = CurtainType1.this.curtaincontent.room_id;
                                savecurtain.curtain_id = CurtainType1.this.curtaincontent.curtain_id;
                                savecurtain.channel_1 = CurtainType1.this.curtaincontent.channel_1;
                                savecurtain.channel_2 = CurtainType1.this.curtaincontent.channel_2;
                                savecurtain.curtain_remark = CurtainType1.this.curtaincontent.curtain_remark;
                                savecurtain.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                                savecurtain.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                                MainActivity.mgr.updatecurtain(savecurtain);
                                Toast.makeText(CurtainType1.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                                CurtainType1.this.curtaincontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                                CurtainType1.this.curtaincontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                                CurtainType1.this.aupairdialog.dismiss();
                            }
                        });
                        CurtainType1.this.pairalter.addExtView(viewInflate2);
                        CurtainType1.this.pairalter.show();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    public void setdeletevisable(boolean z) {
        if (z) {
            this.deleteview.setVisibility(0);
            this.deletemode = true;
            this.deleteview.setChecked(false);
        } else {
            this.deleteview.setVisibility(4);
            this.deletemode = false;
        }
    }

    public void setimage(int i) {
        this.im_image.setImageResource(i);
    }

    public void setremark(String str) {
        this.tv_remark.setText(str);
    }

    public void setstate(String str) {
        this.tv_state.setText(str);
    }

    public void setcontan(Savecurtain savecurtain) {
        this.curtaincontent = savecurtain;
        this.tv_remark.setText(savecurtain.curtain_remark);
        setstate(savecurtain.current_state);
        this.curtainlinear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
    }

    public int getType1curtainid() {
        return this.curtaincontent.curtain_id;
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public int getsubid() {
        return this.curtaincontent.subnetID;
    }

    public int getdevid() {
        return this.curtaincontent.deviceID;
    }

    public int[] getchannel() {
        return new int[]{this.curtaincontent.channel_1, this.curtaincontent.channel_2};
    }

    public void setDBcurtainstate(String str) {
        Savecurtain savecurtain = new Savecurtain();
        savecurtain.room_id = this.curtaincontent.room_id;
        savecurtain.curtain_id = this.curtaincontent.curtain_id;
        savecurtain.current_state = str;
        MainActivity.mgr.updatecurtainstate(savecurtain);
    }

    public void setReceiveChange(int i) {
        switch (i) {
            case 0:
                setstate("open");
                setimage(R.mipmap.curtain_icon1_on);
                setDBcurtainstate("open");
                break;
            case 1:
                setstate("close");
                setimage(R.mipmap.curtain_icon1_off);
                setDBcurtainstate("close");
                break;
        }
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
