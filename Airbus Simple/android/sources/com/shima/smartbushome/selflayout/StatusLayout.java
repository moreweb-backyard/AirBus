package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.PopupMenu;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.StatusIconAdapter;
import com.shima.smartbushome.centercontrol.StatusActivity;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savestatus;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StatusLayout extends LinearLayout implements View.OnLongClickListener {
    RadioButton Celsius;
    RadioButton Fahrenheit;
    EditText cha;
    private String[] colorarray;
    boolean deletemode;
    private CheckBox deleteview;
    EditText dev;
    AlertView iconalter;
    private List<String> iconarray;
    String iconstring;
    LayoutInflater inflater;
    public DBManager mgr;
    EditText name;
    boolean receiveChange;
    Context rootcontext;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    Savestatus statuscontent;
    private ImageView statusicon;
    LinearLayout statuslinear;
    EditText sub;
    private TextView tv_name;
    private TextView tv_status;
    View view;

    public StatusLayout(Context context) {
        super(context);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.StatusLayout.1
            {
                add("room_type1");
                add("room_type2");
                add("room_type3");
                add("room_type4");
                add("room_type5");
                add("room_type6");
                add("room_type7");
                add("room_type8");
                add("room_type9");
                add("room_type10");
                add("room_type11");
                add("room_type12");
                add("room_type13");
                add("room_type14");
                add("room_type15");
                add("light_icon1_on");
                add("light_icon2_on");
                add("light_icon3_on");
                add("light_icon4_on");
                add("light_icon5_on");
                add("light_type3_on");
                add("hvacitem_icon");
                add("hvac");
                add("light");
                add("music");
                add("curtain");
                add("other");
                add("nio");
                add("marco_icon1");
                add("marco_icon2");
                add("marco_icon3");
                add("marco_icon4");
                add("marco_icon5");
                add("marco_icon6");
                add("marco_icon7");
                add("marco_icon8");
                add("marco_icon9");
                add("other_icon1_on");
                add("other_icon2_on");
                add("other_icon3_on");
                add("other_icon4_on");
                add("other_icon5_on");
                add("other_icon6_on");
                add("other_icon7_on");
                add("other_icon8_on");
                add("mood_icon1");
                add("mood_icon2");
                add("mood_icon3");
                add("mood_icon4");
                add("mood_icon5");
                add("mood_icon6");
                add("mood_icon7");
                add("mood_icon8");
                add("mood_icon9");
                add("mood_icon10");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "room_type1";
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.StatusLayout.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == StatusLayout.this.settingalter && i == 0) {
                    Savestatus savestatus = new Savestatus();
                    savestatus.status_id = StatusLayout.this.statuscontent.status_id;
                    savestatus.subnetID = Integer.parseInt(StatusLayout.this.sub.getText().toString().trim());
                    savestatus.deviceID = Integer.parseInt(StatusLayout.this.dev.getText().toString().trim());
                    savestatus.name = StatusLayout.this.name.getText().toString().trim();
                    switch (StatusLayout.this.statuscontent.type) {
                        case 1:
                        case 5:
                            savestatus.unit = StatusLayout.this.statuscontent.unit;
                            savestatus.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            StatusLayout.this.statuscontent.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            break;
                        case 2:
                        case 4:
                            savestatus.unit = StatusLayout.this.statuscontent.unit;
                            savestatus.channel = StatusLayout.this.statuscontent.channel;
                            StatusLayout.this.statuscontent.channel = StatusLayout.this.statuscontent.channel;
                            break;
                        case 3:
                        case 6:
                            if (StatusLayout.this.Celsius.isChecked()) {
                                savestatus.unit = 1;
                            } else if (StatusLayout.this.Fahrenheit.isChecked()) {
                                savestatus.unit = 2;
                            }
                            savestatus.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            StatusLayout.this.statuscontent.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            break;
                    }
                    savestatus.status_icon = StatusLayout.this.iconstring;
                    MainActivity.mgr.updatestatus(savestatus);
                    StatusLayout.this.statuscontent.subnetID = Integer.parseInt(StatusLayout.this.sub.getText().toString().trim());
                    StatusLayout.this.statuscontent.deviceID = Integer.parseInt(StatusLayout.this.dev.getText().toString().trim());
                    StatusLayout.this.statuscontent.name = StatusLayout.this.name.getText().toString().trim();
                    StatusLayout.this.statuscontent.status_icon = StatusLayout.this.iconstring;
                    StatusLayout.this.setname(StatusLayout.this.statuscontent.name);
                    StatusLayout.this.seticon(StatusLayout.this.getResourdIdByResourdName(StatusLayout.this.rootcontext, StatusLayout.this.iconstring));
                    StatusLayout.this.broadcastUpdate(StatusActivity.ACTION_REFLASH);
                }
            }
        };
        initview(context);
    }

    public StatusLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.StatusLayout.1
            {
                add("room_type1");
                add("room_type2");
                add("room_type3");
                add("room_type4");
                add("room_type5");
                add("room_type6");
                add("room_type7");
                add("room_type8");
                add("room_type9");
                add("room_type10");
                add("room_type11");
                add("room_type12");
                add("room_type13");
                add("room_type14");
                add("room_type15");
                add("light_icon1_on");
                add("light_icon2_on");
                add("light_icon3_on");
                add("light_icon4_on");
                add("light_icon5_on");
                add("light_type3_on");
                add("hvacitem_icon");
                add("hvac");
                add("light");
                add("music");
                add("curtain");
                add("other");
                add("nio");
                add("marco_icon1");
                add("marco_icon2");
                add("marco_icon3");
                add("marco_icon4");
                add("marco_icon5");
                add("marco_icon6");
                add("marco_icon7");
                add("marco_icon8");
                add("marco_icon9");
                add("other_icon1_on");
                add("other_icon2_on");
                add("other_icon3_on");
                add("other_icon4_on");
                add("other_icon5_on");
                add("other_icon6_on");
                add("other_icon7_on");
                add("other_icon8_on");
                add("mood_icon1");
                add("mood_icon2");
                add("mood_icon3");
                add("mood_icon4");
                add("mood_icon5");
                add("mood_icon6");
                add("mood_icon7");
                add("mood_icon8");
                add("mood_icon9");
                add("mood_icon10");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "room_type1";
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.StatusLayout.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == StatusLayout.this.settingalter && i == 0) {
                    Savestatus savestatus = new Savestatus();
                    savestatus.status_id = StatusLayout.this.statuscontent.status_id;
                    savestatus.subnetID = Integer.parseInt(StatusLayout.this.sub.getText().toString().trim());
                    savestatus.deviceID = Integer.parseInt(StatusLayout.this.dev.getText().toString().trim());
                    savestatus.name = StatusLayout.this.name.getText().toString().trim();
                    switch (StatusLayout.this.statuscontent.type) {
                        case 1:
                        case 5:
                            savestatus.unit = StatusLayout.this.statuscontent.unit;
                            savestatus.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            StatusLayout.this.statuscontent.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            break;
                        case 2:
                        case 4:
                            savestatus.unit = StatusLayout.this.statuscontent.unit;
                            savestatus.channel = StatusLayout.this.statuscontent.channel;
                            StatusLayout.this.statuscontent.channel = StatusLayout.this.statuscontent.channel;
                            break;
                        case 3:
                        case 6:
                            if (StatusLayout.this.Celsius.isChecked()) {
                                savestatus.unit = 1;
                            } else if (StatusLayout.this.Fahrenheit.isChecked()) {
                                savestatus.unit = 2;
                            }
                            savestatus.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            StatusLayout.this.statuscontent.channel = Integer.parseInt(StatusLayout.this.cha.getText().toString().trim());
                            break;
                    }
                    savestatus.status_icon = StatusLayout.this.iconstring;
                    MainActivity.mgr.updatestatus(savestatus);
                    StatusLayout.this.statuscontent.subnetID = Integer.parseInt(StatusLayout.this.sub.getText().toString().trim());
                    StatusLayout.this.statuscontent.deviceID = Integer.parseInt(StatusLayout.this.dev.getText().toString().trim());
                    StatusLayout.this.statuscontent.name = StatusLayout.this.name.getText().toString().trim();
                    StatusLayout.this.statuscontent.status_icon = StatusLayout.this.iconstring;
                    StatusLayout.this.setname(StatusLayout.this.statuscontent.name);
                    StatusLayout.this.seticon(StatusLayout.this.getResourdIdByResourdName(StatusLayout.this.rootcontext, StatusLayout.this.iconstring));
                    StatusLayout.this.broadcastUpdate(StatusActivity.ACTION_REFLASH);
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.view_statuslayout, this);
        this.rootcontext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.tv_name = (TextView) this.view.findViewById(R.id.status_name);
        this.tv_status = (TextView) this.view.findViewById(R.id.status_detail);
        this.tv_name.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.tv_status.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.deleteview = (CheckBox) this.view.findViewById(R.id.status_delete);
        this.statuslinear = (LinearLayout) this.view.findViewById(R.id.statuslinear);
        this.statusicon = (ImageView) this.view.findViewById(R.id.status_icon);
        this.tv_name.setOnLongClickListener(this);
        this.tv_name.setText(EnvironmentCompat.MEDIA_UNKNOWN);
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (MainActivity.islockchangeid) {
            return true;
        }
        showPopupMenu(this.tv_name);
        return true;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new AnonymousClass2());
        popupMenu.show();
    }

    /* renamed from: com.shima.smartbushome.selflayout.StatusLayout$2, reason: invalid class name */
    class AnonymousClass2 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass2() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_open /* 2131230749 */:
                    StatusLayout.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, StatusLayout.this.rootcontext, AlertView.Style.Alert, StatusLayout.this.settingclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(StatusLayout.this.rootcontext).inflate(R.layout.setting_statusinfo, (ViewGroup) null);
                    StatusLayout.this.sub = (EditText) viewGroup.findViewById(R.id.et_statussub);
                    StatusLayout.this.dev = (EditText) viewGroup.findViewById(R.id.et_statusdev);
                    StatusLayout.this.cha = (EditText) viewGroup.findViewById(R.id.et_statusch);
                    StatusLayout.this.name = (EditText) viewGroup.findViewById(R.id.et_statusname);
                    StatusLayout.this.Celsius = (RadioButton) viewGroup.findViewById(R.id.rb_statusc);
                    StatusLayout.this.Fahrenheit = (RadioButton) viewGroup.findViewById(R.id.rb_statusf);
                    switch (StatusLayout.this.statuscontent.type) {
                        case 1:
                        case 5:
                            StatusLayout.this.Celsius.setChecked(false);
                            StatusLayout.this.Fahrenheit.setChecked(false);
                            StatusLayout.this.Celsius.setEnabled(false);
                            StatusLayout.this.Fahrenheit.setEnabled(false);
                            StatusLayout.this.cha.setText(String.valueOf(StatusLayout.this.statuscontent.channel));
                            break;
                        case 2:
                        case 4:
                            StatusLayout.this.Celsius.setChecked(false);
                            StatusLayout.this.Fahrenheit.setChecked(false);
                            StatusLayout.this.Celsius.setEnabled(false);
                            StatusLayout.this.Fahrenheit.setEnabled(false);
                            StatusLayout.this.cha.setText("no need to set channel");
                            StatusLayout.this.cha.setEnabled(false);
                            break;
                        case 3:
                        case 6:
                            switch (StatusLayout.this.statuscontent.unit) {
                                case 1:
                                    StatusLayout.this.Celsius.setChecked(true);
                                    break;
                                case 2:
                                    StatusLayout.this.Fahrenheit.setChecked(true);
                                    break;
                            }
                            StatusLayout.this.cha.setText(String.valueOf(StatusLayout.this.statuscontent.channel));
                            break;
                    }
                    final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.im_statusicon);
                    StatusLayout.this.sub.setText(String.valueOf(StatusLayout.this.statuscontent.subnetID));
                    StatusLayout.this.dev.setText(String.valueOf(StatusLayout.this.statuscontent.deviceID));
                    StatusLayout.this.name.setText(StatusLayout.this.statuscontent.name);
                    imageView.setImageResource(StatusLayout.this.getResourdIdByResourdName(StatusLayout.this.rootcontext, StatusLayout.this.statuscontent.status_icon));
                    StatusLayout.this.iconstring = StatusLayout.this.statuscontent.status_icon;
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.StatusLayout.2.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            StatusLayout.this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, StatusLayout.this.rootcontext, AlertView.Style.Alert, StatusLayout.this.settingclick);
                            View viewInflate = StatusLayout.this.inflater.inflate(R.layout.mood_icon_select, (ViewGroup) null);
                            GridView gridView = (GridView) viewInflate.findViewById(R.id.gridView2);
                            gridView.setAdapter((ListAdapter) new StatusIconAdapter(StatusLayout.this.rootcontext));
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.StatusLayout.2.1.1
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                    StatusLayout.this.iconstring = (String) StatusLayout.this.iconarray.get(i);
                                    imageView.setImageResource(StatusLayout.this.getResourdIdByResourdName(StatusLayout.this.rootcontext, (String) StatusLayout.this.iconarray.get(i)));
                                    StatusLayout.this.iconalter.dismiss();
                                }
                            });
                            StatusLayout.this.iconalter.addExtView(viewInflate);
                            StatusLayout.this.iconalter.show();
                        }
                    });
                    StatusLayout.this.settingalter.addExtView(viewGroup);
                    StatusLayout.this.settingalter.show();
                    return false;
                case R.id.action_pair /* 2131230750 */:
                    final AlertView alertView = new AlertView("Select Device", null, "CANCEL", null, null, StatusLayout.this.rootcontext, AlertView.Style.Alert, StatusLayout.this.settingclick);
                    ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(StatusLayout.this.rootcontext).inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView = (ListView) viewGroup2.findViewById(R.id.listView4);
                    listView.setAdapter((ListAdapter) new DeviceListAdapter(StatusLayout.this.rootcontext, MainActivity.netdeviceList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.StatusLayout.2.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                            Savestatus savestatus = new Savestatus();
                            savestatus.status_id = StatusLayout.this.statuscontent.status_id;
                            savestatus.name = StatusLayout.this.statuscontent.name;
                            savestatus.type = StatusLayout.this.statuscontent.type;
                            savestatus.unit = StatusLayout.this.statuscontent.unit;
                            savestatus.status_icon = StatusLayout.this.statuscontent.status_icon;
                            savestatus.channel = StatusLayout.this.statuscontent.channel;
                            savestatus.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            savestatus.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            MainActivity.mgr.updatestatus(savestatus);
                            Toast.makeText(StatusLayout.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                            StatusLayout.this.statuscontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            StatusLayout.this.statuscontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            alertView.dismiss();
                        }
                    });
                    alertView.addExtView(viewGroup2);
                    alertView.show();
                    return false;
                default:
                    return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str) {
        this.rootcontext.sendBroadcast(new Intent(str));
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

    public void setname(String str) {
        this.tv_name.setText(str);
    }

    public void setstatus(String str) {
        this.tv_status.setText(str);
    }

    public void seticon(int i) {
        this.statusicon.setImageResource(i);
    }

    public void setstate(String str) {
        this.tv_status.setText(str);
    }

    public void setcontan(Savestatus savestatus) {
        this.statuscontent = savestatus;
        this.statuslinear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        seticon(getResourdIdByResourdName(this.rootcontext, this.statuscontent.status_icon));
        setname(savestatus.name);
    }

    public void setstatuscolor(int i) {
        this.tv_status.setText("LED Color");
        this.tv_status.setTextColor(-5197648);
        this.tv_status.setBackgroundColor(i);
    }

    public int getstatusid() {
        return this.statuscontent.status_id;
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public int getsubid() {
        return this.statuscontent.subnetID;
    }

    public int getdevid() {
        return this.statuscontent.deviceID;
    }

    public int getchannel() {
        return this.statuscontent.channel;
    }

    public int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
