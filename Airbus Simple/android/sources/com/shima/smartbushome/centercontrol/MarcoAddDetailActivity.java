package com.shima.smartbushome.centercontrol;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.WheelView;
import com.shima.smartbushome.assist.holocolorpicker.ColorPicker;
import com.shima.smartbushome.assist.holocolorpicker.SVBar;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.database.Savefan;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.database.Savemarco;
import com.shima.smartbushome.database.Savemedia;
import com.shima.smartbushome.database.Savemediabutton;
import com.shima.smartbushome.database.Savemusic;
import com.shima.smartbushome.database.Saveother;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.database.Savesong;
import com.shima.smartbushome.udp.CONST;
import com.xw.repo.BubbleSeekBar;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MarcoAddDetailActivity extends AppCompatActivity {
    TextView action;
    WheelView actionwva;
    AlertView addactionalter;
    AlertView adddevicealter;
    AlertView addroomalter;
    AlertView addvaluealter;
    AlertView addvaluealter2;
    AlertView addvaluealter3;
    AlertView addvaluealter4;
    Button bt_action;
    Button bt_cancel;
    Button bt_device;
    Button bt_room;
    Button bt_save;
    Button bt_value;
    BubbleSeekBar bubbleSeekBar;
    TextView device;
    WheelView devicewva;
    public DBManager mgr;
    ColorPicker picker;
    TextView room;
    WheelView roomwva;
    SeekBar seekvalue;
    SeekBar tempseekvalue;
    TextView value;
    ListView valuelist;
    String selectedroom = "";
    String selecttype = "";
    String selecteddevice = "";
    String selectedaction = "";
    String selecedtvalue = "";
    String selecedtvalue2 = "";
    String selecedtvalue3 = "";
    String selecedtvalue4 = "";
    List<Saveroom> roomlist = new ArrayList();
    List<Savelight> lightlist = new ArrayList();
    List<Savehvac> hvaclist = new ArrayList();
    List<Savemusic> musiclist = new ArrayList();
    List<Savecurtain> curtainlist = new ArrayList();
    List<Saveother> otherlist = new ArrayList();
    List<Savefan> fanlist = new ArrayList();
    List<Savemedia> medialist = new ArrayList();
    List<Savemediabutton> mediabuttonlist = new ArrayList();
    int marcoID = 0;
    int sentorder = 0;
    String temp_choose_mode = "Cool Mode";
    List<String> stringdevicelist = new ArrayList();
    int roomid = 0;
    int oldroomid = 0;
    List<String> devicetypelist = new ArrayList();
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.11
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == MarcoAddDetailActivity.this.addroomalter) {
                if (i == 0) {
                    MarcoAddDetailActivity.this.device.setText("Select Device: ");
                    MarcoAddDetailActivity.this.selecteddevice = "";
                    MarcoAddDetailActivity.this.action.setText("Select Action: ");
                    MarcoAddDetailActivity.this.value.setText("");
                    MarcoAddDetailActivity.this.value.setBackgroundColor(0);
                    MarcoAddDetailActivity.this.selecttype = "";
                    MarcoAddDetailActivity.this.selectedaction = "";
                    MarcoAddDetailActivity.this.selecedtvalue = "";
                    MarcoAddDetailActivity.this.selecedtvalue2 = "";
                    MarcoAddDetailActivity.this.selecedtvalue3 = "";
                    return;
                }
                return;
            }
            if (obj == MarcoAddDetailActivity.this.adddevicealter) {
                if (i != -1 && i == 0) {
                    MarcoAddDetailActivity.this.value.setText("");
                    MarcoAddDetailActivity.this.value.setBackgroundColor(0);
                    MarcoAddDetailActivity.this.selectedaction = "";
                    MarcoAddDetailActivity.this.selecedtvalue = "";
                    MarcoAddDetailActivity.this.selecedtvalue2 = "";
                    MarcoAddDetailActivity.this.selecedtvalue3 = "";
                    return;
                }
                return;
            }
            if (obj == MarcoAddDetailActivity.this.addactionalter) {
                if (i != -1 && i == 0) {
                    MarcoAddDetailActivity.this.value.setText("");
                    MarcoAddDetailActivity.this.value.setBackgroundColor(0);
                    MarcoAddDetailActivity.this.selecedtvalue = "";
                    MarcoAddDetailActivity.this.selecedtvalue2 = "";
                    MarcoAddDetailActivity.this.selecedtvalue3 = "";
                    return;
                }
                return;
            }
            if (obj != MarcoAddDetailActivity.this.addvaluealter2 && obj == MarcoAddDetailActivity.this.addvaluealter3 && i == 0) {
                MarcoAddDetailActivity.this.value.setText("");
                MarcoAddDetailActivity.this.value.setBackgroundColor(MarcoAddDetailActivity.this.picker.getColor());
                MarcoAddDetailActivity.this.selecedtvalue3 = Integer.toHexString(MarcoAddDetailActivity.this.picker.getColor());
                MarcoAddDetailActivity.this.addvaluealter3.dismiss();
            }
        }
    };
    List<String> lightcommand1 = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.12
        {
            add("Power");
        }
    };
    List<String> lightcommand2 = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.13
        {
            add("Dimmer");
        }
    };
    List<String> lightcommand3 = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.14
        {
            add("LED");
        }
    };
    List<String> accommand = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.15
        {
            add("Power");
            add("Temperature");
            add("Fan Speed");
        }
    };
    List<String> musiccommand = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.16
        {
            add("Source");
            add("SD Song");
            add("Volume");
            add("Radio Channel");
            add("Play control");
        }
    };
    List<String> curtaincommand = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.17
        {
            add("Power");
        }
    };
    List<String> othercommand1 = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.18
        {
            add("Other Type1");
        }
    };
    List<String> othercommand2 = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.19
        {
            add("Other Type2");
        }
    };
    List<String> fancommand = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.20
        {
            add("Fan Power");
        }
    };
    List<String> mediacommand = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.21
        {
            add("Media Control");
        }
    };
    List<String> nothingcommand = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.22
        {
            add("");
        }
    };
    String[] power = {CONST.CONST_STR_ON, CONST.CONST_STR_OFF};
    String[] fanspeed = {"Auto", "High", "Medium", "Low"};
    String[] source = {"Music", "Radio", "Audio-In"};
    String[] playcontrol = {"Play", "Pause", "Back", "Next"};
    String[] fancontrol = {"Fan off", "Fan low Speed", "Fan Middle Speed", "Fan High Speed", "Fan Full Speed"};
    List<String> songlist = new ArrayList();
    String[] radiolist = {"Channel-1", "Channel-2", "Channel-3", "Channel-4", "Channel-5", "Channel-6", "Channel-7", "Channel-8", "Channel-9", "Channel-10", "Channel-11", "Channel-12", "Channel-13", "Channel-14", "Channel-15", "Channel-16", "Channel-17", "Channel-18", "Channel-19", "Channel-20", "Channel-21", "Channel-22", "Channel-23", "Channel-24", "Channel-25"};
    String[] radiobuttonlist = {CONST.CONST_STR_ON, CONST.CONST_STR_OFF, "VOLUME -", "VOLUME +", "VOLUME MUTE", "UP", "DOWN", "LEFT", "RIGHT", "OK", "VIEW1", "VIEW2", "VIEW3", "VIEW4", "BACK", "HOME", "SETTING", "NUM 1", "NUM 2", "NUM 3", "NUM 4", "NUM 5", "NUM 6", "NUM 7", "NUM 8", "NUM 9", "NUM *", "NUM 0", "NUM #"};

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_marco_add_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.marco_add_detail_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Add Mission");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mgr = new DBManager(this);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        Intent intent = getIntent();
        this.marcoID = intent.getIntExtra("marcoID", 0);
        this.sentorder = intent.getIntExtra("marcoOrder", 0);
        this.roomlist = this.mgr.queryroom();
        this.lightlist = this.mgr.querylight();
        this.hvaclist = this.mgr.queryhvac();
        this.musiclist = this.mgr.querymusic();
        this.curtainlist = this.mgr.querycurtain();
        this.otherlist = this.mgr.queryother();
        this.fanlist = this.mgr.queryfan();
        this.medialist = this.mgr.querymedia();
        this.mediabuttonlist = this.mgr.querymediabutton();
        initView();
        initroomalter();
        initdevicealter();
        initactionalter();
        initvaluealter();
        initvaluealter2();
        initvaluealter3();
        initvaluealter4();
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public void initView() {
        this.roomwva = new WheelView(this);
        this.devicewva = new WheelView(this);
        this.actionwva = new WheelView(this);
        this.room = (TextView) findViewById(R.id.marcoadddetailroom);
        this.device = (TextView) findViewById(R.id.marcoadddetaildevice);
        this.action = (TextView) findViewById(R.id.marcoadddetailaction);
        this.value = (TextView) findViewById(R.id.textView60);
        this.bt_room = (Button) findViewById(R.id.button33);
        this.bt_device = (Button) findViewById(R.id.button34);
        this.bt_action = (Button) findViewById(R.id.button35);
        this.bt_value = (Button) findViewById(R.id.button37);
        this.bt_cancel = (Button) findViewById(R.id.button31);
        this.bt_save = (Button) findViewById(R.id.button32);
        this.bt_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MarcoAddDetailActivity.this.cancel();
            }
        });
        this.bt_save.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws NumberFormatException {
                MarcoAddDetailActivity.this.save();
            }
        });
    }

    public void initroomalter() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.roomlist.size(); i++) {
            arrayList.add(this.roomlist.get(i).room_name);
        }
        this.addroomalter = new AlertView("Select Room", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.roomwva.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        relativeLayout.addView(this.roomwva);
        this.roomwva.setOffset(1);
        this.roomwva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.3
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i2, String str) {
                MarcoAddDetailActivity.this.room.setText("Select Room: " + str);
                MarcoAddDetailActivity.this.selectedroom = str;
                MarcoAddDetailActivity.this.device.setText("Select Device: ");
                MarcoAddDetailActivity.this.selecteddevice = "";
                MarcoAddDetailActivity.this.action.setText("Select Action: ");
                MarcoAddDetailActivity.this.selecttype = "";
                MarcoAddDetailActivity.this.selectedaction = "";
            }
        });
        this.roomwva.setItems(arrayList);
        this.addroomalter.addExtView(relativeLayout);
        this.addroomalter.setCancelable(false);
    }

    public void initdevicealter() {
        this.adddevicealter = new AlertView("Select Device", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.devicewva.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        relativeLayout.addView(this.devicewva);
        this.devicewva.setOffset(1);
        this.devicewva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.4
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                if (str.length() > 0) {
                    MarcoAddDetailActivity.this.device.setText("Select Device: " + str);
                    String[] strArrSplit = str.split("-");
                    MarcoAddDetailActivity.this.selecttype = MarcoAddDetailActivity.this.devicetypelist.get(i - 1);
                    MarcoAddDetailActivity.this.selecteddevice = strArrSplit[1];
                    MarcoAddDetailActivity.this.action.setText("Select Action: ");
                    MarcoAddDetailActivity.this.selectedaction = "";
                }
            }
        });
        this.adddevicealter.addExtView(relativeLayout);
        this.adddevicealter.setCancelable(false);
    }

    public void initactionalter() {
        this.addactionalter = new AlertView("Select Action", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        this.actionwva.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        relativeLayout.addView(this.actionwva);
        this.actionwva.setOffset(1);
        this.actionwva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.5
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                if (str.length() > 0) {
                    MarcoAddDetailActivity.this.selectedaction = str;
                    MarcoAddDetailActivity.this.action.setText("Select Action: " + str);
                }
            }
        });
        this.addactionalter.addExtView(relativeLayout);
        this.addactionalter.setCancelable(false);
    }

    public void initvaluealter() {
        this.addvaluealter = new AlertView("Select Value", null, "CANCEL", null, null, this, AlertView.Style.Alert, this.itemclick);
        this.valuelist = new ListView(this);
        this.valuelist.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.6
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                MarcoAddDetailActivity.this.value.setText(((TextView) view.findViewById(R.id.simplelistitemid)).getText().toString());
                MarcoAddDetailActivity.this.selecedtvalue = ((TextView) view.findViewById(R.id.simplelistitemid)).getText().toString();
                MarcoAddDetailActivity.this.addvaluealter.dismiss();
            }
        });
        this.addvaluealter.addExtView(this.valuelist);
        this.addvaluealter.setCancelable(false);
    }

    public void initvaluealter2() {
        this.addvaluealter2 = new AlertView("Select Value", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        View viewInflate = getLayoutInflater().inflate(R.layout.adapter_seekbar, (ViewGroup) null);
        this.bubbleSeekBar = (BubbleSeekBar) viewInflate.findViewById(R.id.bubbleview);
        this.bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() { // from class: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.7
            @Override // com.xw.repo.BubbleSeekBar.OnProgressChangedListenerAdapter, com.xw.repo.BubbleSeekBar.OnProgressChangedListener
            public void getProgressOnActionUp(int i, float f) {
            }

            @Override // com.xw.repo.BubbleSeekBar.OnProgressChangedListenerAdapter, com.xw.repo.BubbleSeekBar.OnProgressChangedListener
            public void getProgressOnFinally(int i, float f) {
            }

            @Override // com.xw.repo.BubbleSeekBar.OnProgressChangedListenerAdapter, com.xw.repo.BubbleSeekBar.OnProgressChangedListener
            public void onProgressChanged(int i, float f) {
                MarcoAddDetailActivity.this.value.setText(i + "%");
                MarcoAddDetailActivity.this.selecedtvalue2 = String.valueOf(i);
            }
        });
        this.addvaluealter2.addExtView(viewInflate);
        this.addvaluealter2.setCancelable(false);
    }

    public void initvaluealter3() {
        this.addvaluealter3 = new AlertView("Color Selection", null, "CANCEL", null, new String[]{"SAVE"}, this, AlertView.Style.Alert, this.itemclick);
        this.addvaluealter3.setCancelable(false);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.view_pickcolor, (ViewGroup) null);
        this.picker = (ColorPicker) viewGroup.findViewById(R.id.view5);
        this.picker.addSVBar((SVBar) viewGroup.findViewById(R.id.view7));
        ColorDrawable colorDrawable = (ColorDrawable) this.value.getBackground();
        if (colorDrawable != null) {
            this.picker.setColor(colorDrawable.getColor());
        }
        this.addvaluealter3.addExtView(viewGroup);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void initvaluealter4() {
        /*
            Method dump skipped, instructions count: 294
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.initvaluealter4():void");
    }

    public void addroom(View view) {
        this.addroomalter.show();
        this.bt_device.setEnabled(true);
        this.bt_device.setTextColor(-1);
    }

    public void adddevice(View view) {
        this.bt_action.setEnabled(true);
        this.bt_action.setTextColor(-1);
        int i = 0;
        while (true) {
            if (i >= this.roomlist.size()) {
                break;
            }
            if (this.selectedroom.equals(this.roomlist.get(i).room_name)) {
                this.roomid = this.roomlist.get(i).room_id;
                break;
            }
            i++;
        }
        if (this.roomid != this.oldroomid) {
            if (this.stringdevicelist.size() > 0) {
                this.stringdevicelist.clear();
            }
            if (this.devicetypelist.size() > 0) {
                this.devicetypelist.clear();
            }
            for (int i2 = 0; i2 < this.lightlist.size(); i2++) {
                if (this.lightlist.get(i2).room_id == this.roomid) {
                    this.stringdevicelist.add("Light-" + this.lightlist.get(i2).light_statement);
                    this.devicetypelist.add("Light" + this.lightlist.get(i2).lightType);
                }
            }
            for (int i3 = 0; i3 < this.hvaclist.size(); i3++) {
                if (this.hvaclist.get(i3).room_id == this.roomid) {
                    this.stringdevicelist.add("HVAC-" + this.hvaclist.get(i3).hvac_remark);
                    this.devicetypelist.add("HVAC");
                }
            }
            for (int i4 = 0; i4 < this.musiclist.size(); i4++) {
                if (this.musiclist.get(i4).room_id == this.roomid) {
                    this.stringdevicelist.add("Music-zaudio");
                    this.devicetypelist.add("Music");
                }
            }
            for (int i5 = 0; i5 < this.curtainlist.size(); i5++) {
                if (this.curtainlist.get(i5).room_id == this.roomid) {
                    this.stringdevicelist.add("Curtain-" + this.curtainlist.get(i5).curtain_remark);
                    this.devicetypelist.add("Curtain");
                }
            }
            for (int i6 = 0; i6 < this.otherlist.size(); i6++) {
                if (this.otherlist.get(i6).room_id == this.roomid) {
                    this.stringdevicelist.add("Other-" + this.otherlist.get(i6).other_statement);
                    this.devicetypelist.add("Other" + this.otherlist.get(i6).other_type);
                }
            }
            for (int i7 = 0; i7 < this.fanlist.size(); i7++) {
                if (this.fanlist.get(i7).room_id == this.roomid) {
                    this.stringdevicelist.add("Fan-" + this.fanlist.get(i7).fan_statement);
                    this.devicetypelist.add("Fan");
                }
            }
            for (int i8 = 0; i8 < this.medialist.size(); i8++) {
                if (this.medialist.get(i8).room_id == this.roomid) {
                    this.stringdevicelist.add("Media-" + this.medialist.get(i8).media_statement);
                    this.devicetypelist.add("Media");
                }
            }
            if (this.stringdevicelist.size() == 0) {
                this.stringdevicelist.add("");
            }
            this.devicewva.setItems(this.stringdevicelist);
            this.devicewva.setSeletion(0);
        }
        this.oldroomid = this.roomid;
        this.adddevicealter.show();
    }

    public void addaction(View view) {
        this.bt_value.setEnabled(true);
        this.bt_value.setTextColor(-1);
        if (this.selecttype.equals("Light1")) {
            this.actionwva.setItems(this.lightcommand1);
        } else if (this.selecttype.equals("Light2")) {
            this.actionwva.setItems(this.lightcommand2);
        } else if (this.selecttype.equals("Light3")) {
            this.actionwva.setItems(this.lightcommand3);
        } else if (this.selecttype.equals("HVAC")) {
            this.actionwva.setItems(this.accommand);
        } else if (this.selecttype.equals("Music")) {
            this.actionwva.setItems(this.musiccommand);
        } else if (this.selecttype.equals("Curtain")) {
            this.actionwva.setItems(this.curtaincommand);
        } else if (this.selecttype.equals("Other1")) {
            this.actionwva.setItems(this.othercommand1);
        } else if (this.selecttype.equals("Other2")) {
            this.actionwva.setItems(this.othercommand2);
        } else if (this.selecttype.equals("Fan")) {
            this.actionwva.setItems(this.fancommand);
        } else if (this.selecttype.equals("Media")) {
            this.actionwva.setItems(this.mediacommand);
        } else {
            this.actionwva.setItems(this.nothingcommand);
        }
        this.actionwva.setSeletion(0);
        this.addactionalter.show();
    }

    public void addvalue(View view) {
        if (this.selectedaction.equals("Power") || this.selectedaction.equals("Other Type1") || this.selectedaction.equals("Other Type2")) {
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.power));
            this.addvaluealter.show();
            return;
        }
        if (this.selectedaction.equals("Fan Speed")) {
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.fanspeed));
            this.addvaluealter.show();
            return;
        }
        if (this.selectedaction.equals("Source")) {
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.source));
            this.addvaluealter.show();
            return;
        }
        if (this.selectedaction.equals("SD Song")) {
            new ArrayList();
            List<Savesong> listQuerysong = this.mgr.querysong();
            if (this.songlist.size() > 0) {
                this.songlist.clear();
            }
            for (int i = 0; i < listQuerysong.size(); i++) {
                if (listQuerysong.get(i).room_id == this.roomid) {
                    this.songlist.add(listQuerysong.get(i).song_name);
                }
            }
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.songlist));
            this.addvaluealter.show();
            return;
        }
        if (this.selectedaction.equals("Radio Channel")) {
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.radiolist));
            this.addvaluealter.show();
            return;
        }
        if (this.selectedaction.equals("Play control")) {
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.playcontrol));
            this.addvaluealter.show();
            return;
        }
        if (this.selectedaction.equals("Dimmer")) {
            this.addvaluealter2.show();
            return;
        }
        if (this.selectedaction.equals("Volume")) {
            this.addvaluealter2.show();
            return;
        }
        if (this.selectedaction.equals("LED")) {
            this.addvaluealter3.show();
            return;
        }
        if (this.selectedaction.equals("Temperature")) {
            this.addvaluealter4.show();
            return;
        }
        if (this.selectedaction.equals("Fan Power")) {
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.fancontrol));
            this.addvaluealter.show();
        } else if (this.selectedaction.equals("Media Control")) {
            this.valuelist.setAdapter((ListAdapter) new ArrayAdapter(this, R.layout.simplelistitem, this.radiobuttonlist));
            this.addvaluealter.show();
        }
    }

    public void cancel() {
        finish();
    }

    public void save() throws NumberFormatException {
        int i;
        int i2 = 0;
        if (this.selecedtvalue != "" || this.selecedtvalue2 != "" || this.selecedtvalue3 != "" || this.selecedtvalue4 != "") {
            int controlType = getControlType();
            new Savelight();
            new Savehvac();
            new Savemusic();
            new Savecurtain();
            new Saveother();
            new Savefan();
            new Savemedia();
            switch (controlType) {
                case 1:
                    Savelight savelight = (Savelight) getdevice(controlType);
                    i2 = savelight.subnetID;
                    i = savelight.deviceID;
                    break;
                case 2:
                    Savehvac savehvac = (Savehvac) getdevice(controlType);
                    i2 = savehvac.subnetID;
                    i = savehvac.deviceID;
                    break;
                case 3:
                    Savecurtain savecurtain = (Savecurtain) getdevice(controlType);
                    i2 = savecurtain.subnetID;
                    i = savecurtain.deviceID;
                    break;
                case 4:
                    Savemusic savemusic = (Savemusic) getdevice(controlType);
                    i2 = savemusic.subnetID;
                    i = savemusic.deviceID;
                    break;
                case 5:
                    Saveother saveother = (Saveother) getdevice(controlType);
                    i2 = saveother.subnetID;
                    i = saveother.deviceID;
                    break;
                case 6:
                    Savefan savefan = (Savefan) getdevice(controlType);
                    i2 = savefan.subnetID;
                    i = savefan.deviceID;
                    break;
                case 7:
                    Savemedia savemedia = (Savemedia) getdevice(controlType);
                    i2 = savemedia.subnetID;
                    i = savemedia.deviceID;
                    break;
                default:
                    i = 0;
                    break;
            }
            int i3 = getvalue1(controlType);
            int i4 = getvalue2(controlType, i3);
            int i5 = getvalue3(i3, controlType);
            Savemarco savemarco = new Savemarco();
            savemarco.marco_id = this.marcoID;
            savemarco.room_id = this.roomid;
            savemarco.room = this.selectedroom;
            savemarco.device = this.selecteddevice;
            savemarco.subnetID = i2;
            savemarco.deviceID = i;
            savemarco.control_type = controlType;
            savemarco.value1 = i3;
            savemarco.value2 = i4;
            savemarco.value3 = i5;
            savemarco.sentorder = this.sentorder;
            this.mgr.addmarco(savemarco);
            setResult(MarcoAddActivity.SAVEMISSION);
            finish();
            return;
        }
        Toast.makeText(this, "Please select a action", 0).show();
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getControlType() {
        /*
            r10 = this;
            java.lang.String r0 = r10.selecttype
            int r1 = r0.hashCode()
            r2 = 7
            r3 = 6
            r4 = 4
            r5 = 3
            r6 = 2
            r7 = 0
            r8 = 5
            r9 = 1
            switch(r1) {
                case -2019010053: goto L6f;
                case -2019010052: goto L65;
                case -2019010051: goto L5b;
                case -1922937023: goto L51;
                case -1922937022: goto L47;
                case -1503318414: goto L3d;
                case 70387: goto L32;
                case 2229680: goto L28;
                case 74219460: goto L1d;
                case 74710533: goto L13;
                default: goto L11;
            }
        L11:
            goto L79
        L13:
            java.lang.String r1 = "Music"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 5
            goto L7a
        L1d:
            java.lang.String r1 = "Media"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 9
            goto L7a
        L28:
            java.lang.String r1 = "HVAC"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 3
            goto L7a
        L32:
            java.lang.String r1 = "Fan"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 8
            goto L7a
        L3d:
            java.lang.String r1 = "Curtain"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 4
            goto L7a
        L47:
            java.lang.String r1 = "Other2"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 7
            goto L7a
        L51:
            java.lang.String r1 = "Other1"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 6
            goto L7a
        L5b:
            java.lang.String r1 = "Light3"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 2
            goto L7a
        L65:
            java.lang.String r1 = "Light2"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 1
            goto L7a
        L6f:
            java.lang.String r1 = "Light1"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L79
            r0 = 0
            goto L7a
        L79:
            r0 = -1
        L7a:
            switch(r0) {
                case 0: goto L8a;
                case 1: goto L8a;
                case 2: goto L8a;
                case 3: goto L89;
                case 4: goto L87;
                case 5: goto L85;
                case 6: goto L83;
                case 7: goto L83;
                case 8: goto L81;
                case 9: goto L7f;
                default: goto L7d;
            }
        L7d:
            r9 = 0
            goto L8a
        L7f:
            r9 = 7
            goto L8a
        L81:
            r9 = 6
            goto L8a
        L83:
            r9 = 5
            goto L8a
        L85:
            r9 = 4
            goto L8a
        L87:
            r9 = 3
            goto L8a
        L89:
            r9 = 2
        L8a:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.getControlType():int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public Object getdevice(int i) {
        new Savelight();
        new Savehvac();
        new Savemusic();
        new Savecurtain();
        new Saveother();
        new Savefan();
        new Savemedia();
        int i2 = 0;
        switch (i) {
            case 1:
                while (i2 < this.lightlist.size()) {
                    if (this.lightlist.get(i2).room_id == this.roomid && this.lightlist.get(i2).light_statement.equals(this.selecteddevice)) {
                        return this.lightlist.get(i2);
                    }
                    i2++;
                }
                return null;
            case 2:
                while (i2 < this.hvaclist.size()) {
                    if (this.hvaclist.get(i2).room_id == this.roomid && this.hvaclist.get(i2).hvac_remark.equals(this.selecteddevice)) {
                        return this.hvaclist.get(i2);
                    }
                    i2++;
                }
                return null;
            case 3:
                while (i2 < this.curtainlist.size()) {
                    if (this.curtainlist.get(i2).room_id == this.roomid && this.curtainlist.get(i2).curtain_remark.equals(this.selecteddevice)) {
                        return this.curtainlist.get(i2);
                    }
                    i2++;
                }
                return null;
            case 4:
                while (i2 < this.musiclist.size()) {
                    if (this.musiclist.get(i2).room_id == this.roomid && "zaudio".equals(this.selecteddevice)) {
                        return this.musiclist.get(i2);
                    }
                    i2++;
                }
                return null;
            case 5:
                while (i2 < this.otherlist.size()) {
                    if (this.otherlist.get(i2).room_id == this.roomid && this.otherlist.get(i2).other_statement.equals(this.selecteddevice)) {
                        return this.otherlist.get(i2);
                    }
                    i2++;
                }
                return null;
            case 6:
                while (i2 < this.fanlist.size()) {
                    if (this.fanlist.get(i2).room_id == this.roomid && this.fanlist.get(i2).fan_statement.equals(this.selecteddevice)) {
                        return this.fanlist.get(i2);
                    }
                    i2++;
                }
                return null;
            case 7:
                while (i2 < this.medialist.size()) {
                    if (this.medialist.get(i2).room_id == this.roomid && this.medialist.get(i2).media_statement.equals(this.selecteddevice)) {
                        return this.medialist.get(i2);
                    }
                    i2++;
                }
                return null;
            default:
                return null;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00b6 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00b8 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0106 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getvalue1(int r9) {
        /*
            Method dump skipped, instructions count: 320
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.getvalue1(int):int");
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:166:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0069 A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006c A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x006f A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0072 A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getvalue2(int r9, int r10) throws java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 852
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.centercontrol.MarcoAddDetailActivity.getvalue2(int, int):int");
    }

    public int getvalue3(int i, int i2) {
        int i3 = 0;
        if (i == 1) {
            if (i2 != 2) {
                return 0;
            }
            String str = this.temp_choose_mode;
            char c = 65535;
            int iHashCode = str.hashCode();
            if (iHashCode != -2111035949) {
                if (iHashCode != -2104347622) {
                    if (iHashCode != -689040012) {
                        if (iHashCode == 812346064 && str.equals("Fan Mode")) {
                            c = 2;
                        }
                    } else if (str.equals("Auto Mode")) {
                        c = 3;
                    }
                } else if (str.equals("Cool Mode")) {
                    c = 0;
                }
            } else if (str.equals("Heat Mode")) {
                c = 1;
            }
            switch (c) {
                case 0:
                default:
                    return 0;
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
            }
        }
        if (i == 3) {
            if (i2 == 4) {
                return Integer.parseInt(this.selecedtvalue.split("-")[1]);
            }
            return 0;
        }
        if (i != 6 || i2 != 4) {
            return 0;
        }
        new ArrayList();
        ArrayList arrayList = new ArrayList();
        Savesong savesong = new Savesong();
        List<Savesong> listQuerysong = this.mgr.querysong();
        for (int i4 = 0; i4 < listQuerysong.size(); i4++) {
            if (listQuerysong.get(i4).room_id == this.roomid) {
                arrayList.add(listQuerysong.get(i4));
            }
        }
        while (true) {
            if (i3 >= arrayList.size()) {
                break;
            }
            if (((Savesong) arrayList.get(i3)).song_name.equals(this.selecedtvalue)) {
                savesong = (Savesong) arrayList.get(i3);
                break;
            }
            i3++;
        }
        return savesong.song_num;
    }
}
