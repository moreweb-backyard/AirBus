package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.OtherIconAdapter;
import com.shima.smartbushome.assist.SwipeLayout;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Saveother;
import com.shima.smartbushome.founction_command.othercontrol;
import com.shima.smartbushome.udp.CONST;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OtherType1 extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    EditText cha1;
    EditText cha2;
    private String[] colorarray;
    LinearLayout delete;
    boolean deletemode;
    private CheckBox deleteview;
    EditText dev;
    AlertView iconalter;
    private List<String> iconarray;
    String iconstring;
    private ImageView im_image;
    LayoutInflater inflater;
    public DBManager mgr;
    EditText name;
    othercontrol oc;
    Button off;
    Button on;
    Saveother othercontent;
    boolean receiveChange;
    private String remark;
    Context rootcontext;
    View selfview;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    private String state;
    EditText sub;
    private TextView tv_remark;
    private TextView tv_state;
    LinearLayout type1linear;
    View view;

    public OtherType1(Context context) {
        super(context);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.OtherType1.1
            {
                add("other_icon1");
                add("other_icon2");
                add("other_icon3");
                add("other_icon4");
                add("other_icon5");
                add("other_icon6");
                add("other_icon7");
                add("other_icon8");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "other_icon1_off";
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType1.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == OtherType1.this.settingalter && i == 0) {
                    Saveother saveother = new Saveother();
                    saveother.room_id = OtherType1.this.othercontent.room_id;
                    saveother.other_id = OtherType1.this.othercontent.other_id;
                    saveother.subnetID = Integer.parseInt(OtherType1.this.sub.getText().toString().trim());
                    saveother.deviceID = Integer.parseInt(OtherType1.this.dev.getText().toString().trim());
                    saveother.channel_1 = Integer.parseInt(OtherType1.this.cha1.getText().toString().trim());
                    saveother.channel_2 = OtherType1.this.othercontent.channel_2;
                    saveother.other_statement = OtherType1.this.name.getText().toString().trim();
                    saveother.other_icon = OtherType1.this.iconstring;
                    MainActivity.mgr.updateother(saveother);
                    OtherType1.this.othercontent.subnetID = Integer.parseInt(OtherType1.this.sub.getText().toString().trim());
                    OtherType1.this.othercontent.deviceID = Integer.parseInt(OtherType1.this.dev.getText().toString().trim());
                    OtherType1.this.othercontent.channel_1 = Integer.parseInt(OtherType1.this.cha1.getText().toString().trim());
                    OtherType1.this.othercontent.other_statement = OtherType1.this.name.getText().toString().trim();
                    OtherType1.this.othercontent.other_icon = OtherType1.this.iconstring;
                    OtherType1.this.setremark(OtherType1.this.othercontent.other_statement);
                    OtherType1.this.setimage(OtherType1.getResourdIdByResourdName(OtherType1.this.rootcontext, OtherType1.this.iconstring + "_off"));
                }
            }
        };
        initview(context);
    }

    public OtherType1(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.OtherType1.1
            {
                add("other_icon1");
                add("other_icon2");
                add("other_icon3");
                add("other_icon4");
                add("other_icon5");
                add("other_icon6");
                add("other_icon7");
                add("other_icon8");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "other_icon1_off";
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType1.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == OtherType1.this.settingalter && i == 0) {
                    Saveother saveother = new Saveother();
                    saveother.room_id = OtherType1.this.othercontent.room_id;
                    saveother.other_id = OtherType1.this.othercontent.other_id;
                    saveother.subnetID = Integer.parseInt(OtherType1.this.sub.getText().toString().trim());
                    saveother.deviceID = Integer.parseInt(OtherType1.this.dev.getText().toString().trim());
                    saveother.channel_1 = Integer.parseInt(OtherType1.this.cha1.getText().toString().trim());
                    saveother.channel_2 = OtherType1.this.othercontent.channel_2;
                    saveother.other_statement = OtherType1.this.name.getText().toString().trim();
                    saveother.other_icon = OtherType1.this.iconstring;
                    MainActivity.mgr.updateother(saveother);
                    OtherType1.this.othercontent.subnetID = Integer.parseInt(OtherType1.this.sub.getText().toString().trim());
                    OtherType1.this.othercontent.deviceID = Integer.parseInt(OtherType1.this.dev.getText().toString().trim());
                    OtherType1.this.othercontent.channel_1 = Integer.parseInt(OtherType1.this.cha1.getText().toString().trim());
                    OtherType1.this.othercontent.other_statement = OtherType1.this.name.getText().toString().trim();
                    OtherType1.this.othercontent.other_icon = OtherType1.this.iconstring;
                    OtherType1.this.setremark(OtherType1.this.othercontent.other_statement);
                    OtherType1.this.setimage(OtherType1.getResourdIdByResourdName(OtherType1.this.rootcontext, OtherType1.this.iconstring + "_off"));
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.other_type1, this);
        this.rootcontext = context;
        this.type1linear = (LinearLayout) this.view.findViewById(R.id.othertype1linear);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.tv_remark = (TextView) this.view.findViewById(R.id.othertype1remark);
        this.tv_state = (TextView) this.view.findViewById(R.id.othertype1state);
        this.im_image = (ImageView) this.view.findViewById(R.id.othertype1imageView);
        this.deleteview = (CheckBox) this.view.findViewById(R.id.othertype1checkBox);
        this.delete = (LinearLayout) this.view.findViewById(R.id.othertype1_delete);
        SwipeLayout.addSwipeView((SwipeLayout) findViewById(R.id.othertype1swipe));
        this.on = (Button) this.view.findViewById(R.id.othertype1on);
        this.off = (Button) this.view.findViewById(R.id.othertype1off);
        this.on.setOnClickListener(this);
        this.off.setOnClickListener(this);
        this.delete.setOnClickListener(this);
        this.oc = new othercontrol();
        this.im_image.setOnClickListener(this);
        this.tv_remark.setOnLongClickListener(this);
        this.tv_state.setText(EnvironmentCompat.MEDIA_UNKNOWN);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.othertype1_delete /* 2131231558 */:
                MainActivity.mgr.deleteother("other", this.othercontent.other_id, this.othercontent.room_id);
                broadcastUpdate(FounctionActivity.ACTION_DELETEOTHER);
                Toast.makeText(this.rootcontext, "delete succeed", 0).show();
                break;
            case R.id.othertype1imageView /* 2131231560 */:
                if (this.tv_state.getText().toString().trim().equals(EnvironmentCompat.MEDIA_UNKNOWN) || this.tv_state.getText().toString().trim().equals(CONST.CONST_STR_OFF)) {
                    HandleLightOnoff(100);
                    setstate(CONST.CONST_STR_ON);
                    setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_on"));
                    break;
                } else {
                    HandleLightOnoff(0);
                    setstate(CONST.CONST_STR_OFF);
                    setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_off"));
                    break;
                }
                break;
            case R.id.othertype1off /* 2131231562 */:
                HandleLightOnoff(0);
                setstate(CONST.CONST_STR_OFF);
                setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_off"));
                break;
            case R.id.othertype1on /* 2131231563 */:
                HandleLightOnoff(100);
                setstate(CONST.CONST_STR_ON);
                setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_on"));
                break;
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
        popupMenu.setOnMenuItemClickListener(new AnonymousClass2());
        popupMenu.show();
    }

    /* renamed from: com.shima.smartbushome.selflayout.OtherType1$2, reason: invalid class name */
    class AnonymousClass2 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass2() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_open /* 2131230749 */:
                    OtherType1.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, OtherType1.this.rootcontext, AlertView.Style.Alert, OtherType1.this.settingclick);
                    View viewInflate = OtherType1.this.inflater.inflate(R.layout.setting_otherinfo, (ViewGroup) null);
                    OtherType1.this.sub = (EditText) viewInflate.findViewById(R.id.othersubed);
                    OtherType1.this.dev = (EditText) viewInflate.findViewById(R.id.otherdeved);
                    OtherType1.this.cha1 = (EditText) viewInflate.findViewById(R.id.otherchan1ed);
                    OtherType1.this.cha2 = (EditText) viewInflate.findViewById(R.id.otherchan2ed);
                    OtherType1.this.name = (EditText) viewInflate.findViewById(R.id.othernameed);
                    final ImageView imageView = (ImageView) viewInflate.findViewById(R.id.imageView11);
                    OtherType1.this.sub.setText(String.valueOf(OtherType1.this.othercontent.subnetID));
                    OtherType1.this.dev.setText(String.valueOf(OtherType1.this.othercontent.deviceID));
                    OtherType1.this.cha1.setText(String.valueOf(OtherType1.this.othercontent.channel_1));
                    OtherType1.this.cha2.setText("no need to set this channel");
                    OtherType1.this.cha2.setTextSize(15.0f);
                    OtherType1.this.cha2.setTextColor(-6908266);
                    OtherType1.this.cha2.setEnabled(false);
                    OtherType1.this.name.setText(OtherType1.this.othercontent.other_statement);
                    OtherType1.this.iconstring = OtherType1.this.othercontent.other_icon;
                    imageView.setImageResource(OtherType1.getResourdIdByResourdName(OtherType1.this.rootcontext, OtherType1.this.othercontent.other_icon + "_on"));
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType1.2.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            OtherType1.this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, OtherType1.this.rootcontext, AlertView.Style.Alert, OtherType1.this.settingclick);
                            View viewInflate2 = OtherType1.this.inflater.inflate(R.layout.mood_icon_select, (ViewGroup) null);
                            GridView gridView = (GridView) viewInflate2.findViewById(R.id.gridView2);
                            gridView.setAdapter((ListAdapter) new OtherIconAdapter(OtherType1.this.rootcontext));
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType1.2.1.1
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                    OtherType1.this.iconstring = (String) OtherType1.this.iconarray.get(i);
                                    imageView.setImageResource(OtherType1.getResourdIdByResourdName(OtherType1.this.rootcontext, ((String) OtherType1.this.iconarray.get(i)) + "_on"));
                                    OtherType1.this.iconalter.dismiss();
                                }
                            });
                            OtherType1.this.iconalter.addExtView(viewInflate2);
                            OtherType1.this.iconalter.show();
                        }
                    });
                    OtherType1.this.settingalter.addExtView(viewInflate);
                    OtherType1.this.settingalter.show();
                    return false;
                case R.id.action_pair /* 2131230750 */:
                    final AlertView alertView = new AlertView("Select Device", null, "CANCEL", null, null, OtherType1.this.rootcontext, AlertView.Style.Alert, OtherType1.this.settingclick);
                    View viewInflate2 = OtherType1.this.inflater.inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView = (ListView) viewInflate2.findViewById(R.id.listView4);
                    listView.setAdapter((ListAdapter) new DeviceListAdapter(OtherType1.this.rootcontext, MainActivity.netdeviceList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType1.2.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                            Saveother saveother = new Saveother();
                            saveother.room_id = OtherType1.this.othercontent.room_id;
                            saveother.other_id = OtherType1.this.othercontent.other_id;
                            saveother.other_statement = OtherType1.this.othercontent.other_statement;
                            saveother.channel_1 = OtherType1.this.othercontent.channel_1;
                            saveother.channel_2 = OtherType1.this.othercontent.channel_2;
                            saveother.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            saveother.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            saveother.other_icon = OtherType1.this.othercontent.other_icon;
                            MainActivity.mgr.updateother(saveother);
                            Toast.makeText(OtherType1.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                            OtherType1.this.othercontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            OtherType1.this.othercontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            alertView.dismiss();
                        }
                    });
                    alertView.addExtView(viewInflate2);
                    alertView.show();
                    return false;
                default:
                    return false;
            }
        }
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

    public void setcontan(Saveother saveother) {
        this.othercontent = saveother;
        this.tv_remark.setText(saveother.other_statement);
        this.type1linear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.im_image.setImageDrawable(getResources().getDrawable(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_off")));
    }

    public void setReceiveChange(byte b) {
        if (b == 0) {
            setstate(CONST.CONST_STR_OFF);
            setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_off"));
            return;
        }
        if (b == 100) {
            setstate(CONST.CONST_STR_ON);
            setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_on"));
        }
    }

    public int getType1otherid() {
        return this.othercontent.other_id;
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public String getstate() {
        return this.tv_state.getText().toString().trim();
    }

    public int getsubid() {
        return this.othercontent.subnetID;
    }

    public int getdevid() {
        return this.othercontent.deviceID;
    }

    public int getchannel() {
        return this.othercontent.channel_1;
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    public static int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }

    public void setDialogdismiss(boolean z, DialogInterface dialogInterface) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            declaredField.setAccessible(true);
            declaredField.set(dialogInterface, Boolean.valueOf(z));
            dialogInterface.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void HandleLightOnoff(int i) {
        try {
            if (this.oc.SingleChannelControl((byte) this.othercontent.subnetID, (byte) this.othercontent.deviceID, this.othercontent.channel_1, i, MainActivity.mydupsocket)) {
                return;
            }
            Log.i("btn", "bytSuccess = 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
