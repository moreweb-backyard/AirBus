package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class OtherType2 extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    private Button bt_close;
    private Button bt_open;
    private Button bt_stop;
    othercontrol cc;
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
    Saveother othercontent;
    LinearLayout othertype2linear;
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
    View view;

    public OtherType2(Context context) {
        super(context);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.OtherType2.1
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
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType2.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == OtherType2.this.settingalter && i == 0) {
                    Saveother saveother = new Saveother();
                    saveother.room_id = OtherType2.this.othercontent.room_id;
                    saveother.other_id = OtherType2.this.othercontent.other_id;
                    saveother.subnetID = Integer.parseInt(OtherType2.this.sub.getText().toString().trim());
                    saveother.deviceID = Integer.parseInt(OtherType2.this.dev.getText().toString().trim());
                    saveother.channel_1 = Integer.parseInt(OtherType2.this.cha1.getText().toString().trim());
                    saveother.channel_2 = Integer.parseInt(OtherType2.this.cha2.getText().toString().trim());
                    saveother.other_statement = OtherType2.this.name.getText().toString().trim();
                    saveother.other_icon = OtherType2.this.iconstring;
                    MainActivity.mgr.updateother(saveother);
                    OtherType2.this.othercontent.subnetID = Integer.parseInt(OtherType2.this.sub.getText().toString().trim());
                    OtherType2.this.othercontent.deviceID = Integer.parseInt(OtherType2.this.dev.getText().toString().trim());
                    OtherType2.this.othercontent.channel_1 = Integer.parseInt(OtherType2.this.cha1.getText().toString().trim());
                    OtherType2.this.othercontent.channel_2 = Integer.parseInt(OtherType2.this.cha2.getText().toString().trim());
                    OtherType2.this.othercontent.other_statement = OtherType2.this.name.getText().toString().trim();
                    OtherType2.this.othercontent.other_icon = OtherType2.this.iconstring;
                    OtherType2.this.setremark(OtherType2.this.othercontent.other_statement);
                    OtherType2.this.setimage(OtherType2.getResourdIdByResourdName(OtherType2.this.rootcontext, OtherType2.this.iconstring + "_off"));
                }
            }
        };
        initview(context);
    }

    public OtherType2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.OtherType2.1
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
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType2.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == OtherType2.this.settingalter && i == 0) {
                    Saveother saveother = new Saveother();
                    saveother.room_id = OtherType2.this.othercontent.room_id;
                    saveother.other_id = OtherType2.this.othercontent.other_id;
                    saveother.subnetID = Integer.parseInt(OtherType2.this.sub.getText().toString().trim());
                    saveother.deviceID = Integer.parseInt(OtherType2.this.dev.getText().toString().trim());
                    saveother.channel_1 = Integer.parseInt(OtherType2.this.cha1.getText().toString().trim());
                    saveother.channel_2 = Integer.parseInt(OtherType2.this.cha2.getText().toString().trim());
                    saveother.other_statement = OtherType2.this.name.getText().toString().trim();
                    saveother.other_icon = OtherType2.this.iconstring;
                    MainActivity.mgr.updateother(saveother);
                    OtherType2.this.othercontent.subnetID = Integer.parseInt(OtherType2.this.sub.getText().toString().trim());
                    OtherType2.this.othercontent.deviceID = Integer.parseInt(OtherType2.this.dev.getText().toString().trim());
                    OtherType2.this.othercontent.channel_1 = Integer.parseInt(OtherType2.this.cha1.getText().toString().trim());
                    OtherType2.this.othercontent.channel_2 = Integer.parseInt(OtherType2.this.cha2.getText().toString().trim());
                    OtherType2.this.othercontent.other_statement = OtherType2.this.name.getText().toString().trim();
                    OtherType2.this.othercontent.other_icon = OtherType2.this.iconstring;
                    OtherType2.this.setremark(OtherType2.this.othercontent.other_statement);
                    OtherType2.this.setimage(OtherType2.getResourdIdByResourdName(OtherType2.this.rootcontext, OtherType2.this.iconstring + "_off"));
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.other_type2, this);
        this.rootcontext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.tv_remark = (TextView) this.view.findViewById(R.id.othertype2rmark);
        this.othertype2linear = (LinearLayout) this.view.findViewById(R.id.othertype2linear);
        this.tv_state = (TextView) this.view.findViewById(R.id.othertype2state);
        this.bt_open = (Button) this.view.findViewById(R.id.othertype2on);
        this.bt_stop = (Button) this.view.findViewById(R.id.othertype2stop);
        this.bt_close = (Button) this.view.findViewById(R.id.othertype2off);
        this.deleteview = (CheckBox) this.view.findViewById(R.id.othertype2delete);
        this.im_image = (ImageView) this.view.findViewById(R.id.othertype2imageView);
        this.delete = (LinearLayout) this.view.findViewById(R.id.othertype2_delete);
        SwipeLayout.addSwipeView((SwipeLayout) findViewById(R.id.othertype2swipe));
        this.bt_open.setOnClickListener(this);
        this.bt_stop.setOnClickListener(this);
        this.bt_close.setOnClickListener(this);
        this.delete.setOnClickListener(this);
        this.tv_remark.setOnLongClickListener(this);
        this.cc = new othercontrol();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.othertype2_delete /* 2131231567 */:
                MainActivity.mgr.deleteother("other", this.othercontent.other_id, this.othercontent.room_id);
                broadcastUpdate(FounctionActivity.ACTION_DELETEOTHER);
                Toast.makeText(this.rootcontext, "delete succeed", 0).show();
                break;
            case R.id.othertype2off /* 2131231571 */:
                this.cc.CurtainControl((byte) this.othercontent.subnetID, (byte) this.othercontent.deviceID, this.othercontent.channel_1, this.othercontent.channel_2, "close", MainActivity.mydupsocket);
                setstate("close");
                setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_off"));
                break;
            case R.id.othertype2on /* 2131231572 */:
                this.cc.CurtainControl((byte) this.othercontent.subnetID, (byte) this.othercontent.deviceID, this.othercontent.channel_1, this.othercontent.channel_2, "open", MainActivity.mydupsocket);
                setstate("open");
                setimage(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_on"));
                break;
            case R.id.othertype2stop /* 2131231575 */:
                this.cc.CurtainControl((byte) this.othercontent.subnetID, (byte) this.othercontent.deviceID, this.othercontent.channel_1, this.othercontent.channel_2, "stop", MainActivity.mydupsocket);
                setstate("stop");
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

    /* renamed from: com.shima.smartbushome.selflayout.OtherType2$2, reason: invalid class name */
    class AnonymousClass2 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass2() {
        }

        @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_open /* 2131230749 */:
                    OtherType2.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, OtherType2.this.rootcontext, AlertView.Style.Alert, OtherType2.this.settingclick);
                    View viewInflate = OtherType2.this.inflater.inflate(R.layout.setting_otherinfo, (ViewGroup) null);
                    OtherType2.this.sub = (EditText) viewInflate.findViewById(R.id.othersubed);
                    OtherType2.this.dev = (EditText) viewInflate.findViewById(R.id.otherdeved);
                    OtherType2.this.cha1 = (EditText) viewInflate.findViewById(R.id.otherchan1ed);
                    OtherType2.this.cha2 = (EditText) viewInflate.findViewById(R.id.otherchan2ed);
                    OtherType2.this.name = (EditText) viewInflate.findViewById(R.id.othernameed);
                    final ImageView imageView = (ImageView) viewInflate.findViewById(R.id.imageView11);
                    OtherType2.this.sub.setText(String.valueOf(OtherType2.this.othercontent.subnetID));
                    OtherType2.this.dev.setText(String.valueOf(OtherType2.this.othercontent.deviceID));
                    OtherType2.this.cha1.setText(String.valueOf(OtherType2.this.othercontent.channel_1));
                    OtherType2.this.cha2.setText(String.valueOf(OtherType2.this.othercontent.channel_2));
                    OtherType2.this.name.setText(OtherType2.this.othercontent.other_statement);
                    OtherType2.this.iconstring = OtherType2.this.othercontent.other_icon;
                    imageView.setImageResource(OtherType2.getResourdIdByResourdName(OtherType2.this.rootcontext, OtherType2.this.othercontent.other_icon + "_on"));
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType2.2.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            OtherType2.this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, OtherType2.this.rootcontext, AlertView.Style.Alert, OtherType2.this.settingclick);
                            View viewInflate2 = OtherType2.this.inflater.inflate(R.layout.mood_icon_select, (ViewGroup) null);
                            GridView gridView = (GridView) viewInflate2.findViewById(R.id.gridView2);
                            gridView.setAdapter((ListAdapter) new OtherIconAdapter(OtherType2.this.rootcontext));
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType2.2.1.1
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                    OtherType2.this.iconstring = (String) OtherType2.this.iconarray.get(i);
                                    imageView.setImageResource(OtherType2.getResourdIdByResourdName(OtherType2.this.rootcontext, ((String) OtherType2.this.iconarray.get(i)) + "_on"));
                                    OtherType2.this.iconalter.dismiss();
                                }
                            });
                            OtherType2.this.iconalter.addExtView(viewInflate2);
                            OtherType2.this.iconalter.show();
                        }
                    });
                    OtherType2.this.settingalter.addExtView(viewInflate);
                    OtherType2.this.settingalter.show();
                    break;
                case R.id.action_pair /* 2131230750 */:
                    final AlertView alertView = new AlertView("Select Device", null, "CANCEL", null, null, OtherType2.this.rootcontext, AlertView.Style.Alert, OtherType2.this.settingclick);
                    View viewInflate2 = OtherType2.this.inflater.inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView = (ListView) viewInflate2.findViewById(R.id.listView4);
                    listView.setAdapter((ListAdapter) new DeviceListAdapter(OtherType2.this.rootcontext, MainActivity.netdeviceList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.OtherType2.2.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                            Saveother saveother = new Saveother();
                            saveother.room_id = OtherType2.this.othercontent.room_id;
                            saveother.other_id = OtherType2.this.othercontent.other_id;
                            saveother.other_statement = OtherType2.this.othercontent.other_statement;
                            saveother.channel_1 = OtherType2.this.othercontent.channel_1;
                            saveother.channel_2 = OtherType2.this.othercontent.channel_2;
                            saveother.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            saveother.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            saveother.other_icon = OtherType2.this.othercontent.other_icon;
                            MainActivity.mgr.updateother(saveother);
                            Toast.makeText(OtherType2.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                            OtherType2.this.othercontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            OtherType2.this.othercontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            alertView.dismiss();
                        }
                    });
                    alertView.addExtView(viewInflate2);
                    alertView.show();
                    break;
            }
            return false;
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

    public void setremark(String str) {
        this.tv_remark.setText(str);
    }

    public void setstate(String str) {
        this.tv_state.setText(str);
    }

    public void setimage(int i) {
        this.im_image.setImageResource(i);
    }

    public void setcontan(Saveother saveother) {
        this.othercontent = saveother;
        this.tv_remark.setText(saveother.other_statement);
        this.othertype2linear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.im_image.setImageDrawable(getResources().getDrawable(getResourdIdByResourdName(this.rootcontext, this.othercontent.other_icon + "_off")));
    }

    public int getType2otherid() {
        return this.othercontent.other_id;
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public int getsubid() {
        return this.othercontent.subnetID;
    }

    public int getdevid() {
        return this.othercontent.deviceID;
    }

    public int[] getchannel() {
        return new int[]{this.othercontent.channel_1, this.othercontent.channel_2};
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
}
