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
import android.view.MotionEvent;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.LightIconAdapter;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.udp.CONST;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LightType4 extends LinearLayout implements View.OnLongClickListener {
    private Button bt_press;
    EditText cha;
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
    lightcontrol lc;
    Savelight lightcontent;
    public DBManager mgr;
    EditText name;
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
    RadioButton type1;
    RadioButton type2;
    RadioButton type3;
    RadioButton type4;
    LinearLayout type4linear;
    View view;

    public LightType4(Context context) {
        super(context);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.LightType4.1
            {
                add("light_icon1");
                add("light_icon2");
                add("light_icon3");
                add("light_icon4");
                add("light_icon5");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "light_icon1";
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType4.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == LightType4.this.settingalter && i == 0) {
                    Savelight savelight = new Savelight();
                    savelight.room_id = LightType4.this.lightcontent.room_id;
                    savelight.light_id = LightType4.this.lightcontent.light_id;
                    savelight.subnetID = Integer.parseInt(LightType4.this.sub.getText().toString().trim());
                    savelight.deviceID = Integer.parseInt(LightType4.this.dev.getText().toString().trim());
                    savelight.channel = Integer.parseInt(LightType4.this.cha.getText().toString().trim());
                    savelight.light_statement = LightType4.this.name.getText().toString().trim();
                    savelight.light_icon = LightType4.this.iconstring;
                    if (LightType4.this.type1.isChecked()) {
                        savelight.lightType = 1;
                    } else if (LightType4.this.type2.isChecked()) {
                        savelight.lightType = 2;
                    } else if (LightType4.this.type3.isChecked()) {
                        savelight.lightType = 3;
                    } else if (LightType4.this.type4.isChecked()) {
                        savelight.lightType = 4;
                    }
                    MainActivity.mgr.updatelight(savelight);
                    LightType4.this.lightcontent.subnetID = Integer.parseInt(LightType4.this.sub.getText().toString().trim());
                    LightType4.this.lightcontent.deviceID = Integer.parseInt(LightType4.this.dev.getText().toString().trim());
                    LightType4.this.lightcontent.channel = Integer.parseInt(LightType4.this.cha.getText().toString().trim());
                    LightType4.this.lightcontent.light_statement = LightType4.this.name.getText().toString().trim();
                    LightType4.this.lightcontent.light_icon = LightType4.this.iconstring;
                    LightType4.this.setremark(LightType4.this.lightcontent.light_statement);
                    LightType4.this.setimage(LightType4.getResourdIdByResourdName(LightType4.this.rootcontext, LightType4.this.iconstring + "_off"));
                    if (savelight.lightType != 1) {
                        LightType4.this.broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
                    }
                }
            }
        };
        initview(context);
    }

    public LightType4(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.LightType4.1
            {
                add("light_icon1");
                add("light_icon2");
                add("light_icon3");
                add("light_icon4");
                add("light_icon5");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "light_icon1";
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType4.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == LightType4.this.settingalter && i == 0) {
                    Savelight savelight = new Savelight();
                    savelight.room_id = LightType4.this.lightcontent.room_id;
                    savelight.light_id = LightType4.this.lightcontent.light_id;
                    savelight.subnetID = Integer.parseInt(LightType4.this.sub.getText().toString().trim());
                    savelight.deviceID = Integer.parseInt(LightType4.this.dev.getText().toString().trim());
                    savelight.channel = Integer.parseInt(LightType4.this.cha.getText().toString().trim());
                    savelight.light_statement = LightType4.this.name.getText().toString().trim();
                    savelight.light_icon = LightType4.this.iconstring;
                    if (LightType4.this.type1.isChecked()) {
                        savelight.lightType = 1;
                    } else if (LightType4.this.type2.isChecked()) {
                        savelight.lightType = 2;
                    } else if (LightType4.this.type3.isChecked()) {
                        savelight.lightType = 3;
                    } else if (LightType4.this.type4.isChecked()) {
                        savelight.lightType = 4;
                    }
                    MainActivity.mgr.updatelight(savelight);
                    LightType4.this.lightcontent.subnetID = Integer.parseInt(LightType4.this.sub.getText().toString().trim());
                    LightType4.this.lightcontent.deviceID = Integer.parseInt(LightType4.this.dev.getText().toString().trim());
                    LightType4.this.lightcontent.channel = Integer.parseInt(LightType4.this.cha.getText().toString().trim());
                    LightType4.this.lightcontent.light_statement = LightType4.this.name.getText().toString().trim();
                    LightType4.this.lightcontent.light_icon = LightType4.this.iconstring;
                    LightType4.this.setremark(LightType4.this.lightcontent.light_statement);
                    LightType4.this.setimage(LightType4.getResourdIdByResourdName(LightType4.this.rootcontext, LightType4.this.iconstring + "_off"));
                    if (savelight.lightType != 1) {
                        LightType4.this.broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
                    }
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.light_type4, this);
        this.rootcontext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.type4linear = (LinearLayout) this.view.findViewById(R.id.lighttype4linear);
        this.tv_remark = (TextView) this.view.findViewById(R.id.ltype4_remark);
        this.tv_state = (TextView) this.view.findViewById(R.id.ltype4_state);
        this.im_image = (ImageView) this.view.findViewById(R.id.ltype4_icon);
        this.bt_press = (Button) this.view.findViewById(R.id.ltype4button);
        this.deleteview = (CheckBox) this.view.findViewById(R.id.ltype4_checkbox);
        this.bt_press.setOnTouchListener(new View.OnTouchListener() { // from class: com.shima.smartbushome.selflayout.LightType4.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    LightType4.this.setstate(CONST.CONST_STR_OFF);
                    LightType4.this.setimage(LightType4.getResourdIdByResourdName(LightType4.this.rootcontext, LightType4.this.lightcontent.light_icon + "_off"));
                    LightType4.this.HandleLightOnoff(0);
                    Log.v("test", "放开操作");
                }
                if (motionEvent.getAction() == 0) {
                    LightType4.this.setstate(CONST.CONST_STR_ON);
                    LightType4.this.setimage(LightType4.getResourdIdByResourdName(LightType4.this.rootcontext, LightType4.this.lightcontent.light_icon + "_on"));
                    LightType4.this.HandleLightOnoff(100);
                    Log.v("test", "按下操作");
                }
                return false;
            }
        });
        this.tv_remark.setOnLongClickListener(this);
        this.tv_state.setText(EnvironmentCompat.MEDIA_UNKNOWN);
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
        popupMenu.setOnMenuItemClickListener(new AnonymousClass3());
        popupMenu.show();
    }

    /* renamed from: com.shima.smartbushome.selflayout.LightType4$3, reason: invalid class name */
    class AnonymousClass3 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass3() {
        }

        @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_open /* 2131230749 */:
                    LightType4.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, LightType4.this.rootcontext, AlertView.Style.Alert, LightType4.this.settingclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(LightType4.this.rootcontext).inflate(R.layout.setting_lightinfo, (ViewGroup) null);
                    LightType4.this.sub = (EditText) viewGroup.findViewById(R.id.subedit);
                    LightType4.this.dev = (EditText) viewGroup.findViewById(R.id.deviceedit);
                    LightType4.this.cha = (EditText) viewGroup.findViewById(R.id.chanedit);
                    LightType4.this.name = (EditText) viewGroup.findViewById(R.id.lightnameedit);
                    LightType4.this.type1 = (RadioButton) viewGroup.findViewById(R.id.lighttype1);
                    LightType4.this.type2 = (RadioButton) viewGroup.findViewById(R.id.lighttype2);
                    LightType4.this.type3 = (RadioButton) viewGroup.findViewById(R.id.lighttype3);
                    LightType4.this.type4 = (RadioButton) viewGroup.findViewById(R.id.lighttype4);
                    LightType4.this.type4.setChecked(true);
                    final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.imageView10);
                    LightType4.this.sub.setText(String.valueOf(LightType4.this.lightcontent.subnetID));
                    LightType4.this.dev.setText(String.valueOf(LightType4.this.lightcontent.deviceID));
                    LightType4.this.cha.setText(String.valueOf(LightType4.this.lightcontent.channel));
                    LightType4.this.name.setText(LightType4.this.lightcontent.light_statement);
                    imageView.setImageResource(LightType4.getResourdIdByResourdName(LightType4.this.rootcontext, LightType4.this.lightcontent.light_icon + "_on"));
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.LightType4.3.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            LightType4.this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, LightType4.this.rootcontext, AlertView.Style.Alert, LightType4.this.settingclick);
                            View viewInflate = LightType4.this.inflater.inflate(R.layout.mood_icon_select, (ViewGroup) null);
                            GridView gridView = (GridView) viewInflate.findViewById(R.id.gridView2);
                            gridView.setAdapter((ListAdapter) new LightIconAdapter(LightType4.this.rootcontext));
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType4.3.1.1
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                    LightType4.this.iconstring = (String) LightType4.this.iconarray.get(i);
                                    imageView.setImageResource(LightType4.getResourdIdByResourdName(LightType4.this.rootcontext, ((String) LightType4.this.iconarray.get(i)) + "_on"));
                                    LightType4.this.iconalter.dismiss();
                                }
                            });
                            LightType4.this.iconalter.addExtView(viewInflate);
                            LightType4.this.iconalter.show();
                        }
                    });
                    LightType4.this.settingalter.addExtView(viewGroup);
                    LightType4.this.settingalter.show();
                    break;
                case R.id.action_pair /* 2131230750 */:
                    final AlertView alertView = new AlertView("Select Device", null, "CANCEL", null, null, LightType4.this.rootcontext, AlertView.Style.Alert, LightType4.this.settingclick);
                    ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(LightType4.this.rootcontext).inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView = (ListView) viewGroup2.findViewById(R.id.listView4);
                    listView.setAdapter((ListAdapter) new DeviceListAdapter(LightType4.this.rootcontext, MainActivity.netdeviceList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType4.3.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                            Savelight savelight = new Savelight();
                            savelight.room_id = LightType4.this.lightcontent.room_id;
                            savelight.light_id = LightType4.this.lightcontent.light_id;
                            savelight.light_statement = LightType4.this.lightcontent.light_statement;
                            savelight.channel = LightType4.this.lightcontent.channel;
                            savelight.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            savelight.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            savelight.light_icon = LightType4.this.lightcontent.light_icon;
                            savelight.lightType = LightType4.this.lightcontent.lightType;
                            MainActivity.mgr.updatelight(savelight);
                            Toast.makeText(LightType4.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                            LightType4.this.lightcontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            LightType4.this.lightcontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            alertView.dismiss();
                        }
                    });
                    alertView.addExtView(viewGroup2);
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

    public void setimage(int i) {
        this.im_image.setImageResource(i);
    }

    public void setremark(String str) {
        this.tv_remark.setText(str);
    }

    public void setstate(String str) {
        this.tv_state.setText(str);
    }

    public void setcontant(Savelight savelight) {
        this.lightcontent = savelight;
        this.type4linear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.tv_remark.setText(savelight.light_statement);
        this.im_image.setImageDrawable(getResources().getDrawable(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_off")));
    }

    public void setReceiveChange(byte b) {
        this.receiveChange = true;
        if (b == 0) {
            setstate(CONST.CONST_STR_OFF);
            setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_off"));
            this.receiveChange = false;
            return;
        }
        if (b == 100) {
            setstate(CONST.CONST_STR_ON);
            setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_on"));
            this.receiveChange = false;
        }
    }

    public int getType4lightid() {
        return this.lightcontent.light_id;
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public String getstate() {
        return this.tv_state.getText().toString().trim();
    }

    public int getsubid() {
        return this.lightcontent.subnetID;
    }

    public int getdevid() {
        return this.lightcontent.deviceID;
    }

    public int getchannel() {
        return this.lightcontent.channel;
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

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    public void HandleLightOnoff(int i) {
        this.lc = new lightcontrol();
        try {
            if (this.lc.SingleChannelControl((byte) this.lightcontent.subnetID, (byte) this.lightcontent.deviceID, this.lightcontent.channel, i, MainActivity.mydupsocket)) {
                return;
            }
            Log.i("btn", "bytSuccess = 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
