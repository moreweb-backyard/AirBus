package com.shima.smartbushome.selflayout;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savefloorheat;
import com.shima.smartbushome.founction_command.FloorHeatControl;
import com.shima.smartbushome.util.SharedPreferencesHelper;
import java.util.Calendar;

/* loaded from: classes.dex */
public class FloorHeatLayout extends LinearLayout {
    public String CurTemperType;
    public String FTDevID;
    public String FTSubID;
    public String SensorDevID;
    public String SensorSubID;
    public String ZoneBeastDevID;
    public String ZoneBeastSubID;
    int awayFault;
    int awayTem;
    Button bt_daytime;
    Button bt_nighttime;
    Button bt_off;
    Button bt_on;
    public int curTemperType;
    int dayFault;
    int dayTem;
    EditText et_4tdevid;
    EditText et_4tsubid;
    EditText et_hvacdevid;
    EditText et_hvacsubid;
    EditText et_sensordevid;
    EditText et_sensorsubid;
    EditText et_zbdevid;
    EditText et_zbsubid;
    public int fTDevID;
    public int fTSubID;
    Savefloorheat floorHeat;
    FloorHeatControl floorHeatControl;
    LinearLayout layout_away;
    LinearLayout layout_core;
    LinearLayout layout_curtemper;
    LinearLayout layout_day;
    LinearLayout layout_manual;
    LinearLayout layout_night;
    int manualFault;
    int manualTem;
    int nightFault;
    int nightTem;
    RadioButton rb_4t;
    RadioButton rb_auto;
    RadioButton rb_away;
    RadioButton rb_day;
    RadioButton rb_hvac;
    RadioButton rb_manual;
    RadioButton rb_night;
    RadioButton rb_sensor;
    RadioButton rb_zb;
    RadioGroup.OnCheckedChangeListener rgCheckedChangeListener;
    RadioGroup rg_Temgroup;
    RadioGroup rg_group;
    View rootView;
    public OnItemClickListener saveClick;
    SeekBar sb_away;
    SeekBar sb_day;
    SeekBar sb_manual;
    SeekBar sb_night;
    int sensorChannelNo;
    public int sensorDevID;
    int sensorDeviceId;
    public int sensorSubID;
    int sensorSubnetId;
    TextView tv_away;
    TextView tv_curtemper;
    TextView tv_day;
    TextView tv_devicename;
    TextView tv_manual;
    TextView tv_night;
    public int zoneBeastDevID;
    public int zoneBeastSubID;

    public FloorHeatLayout(Context context) {
        super(context);
        this.floorHeat = null;
        this.manualTem = 23;
        this.manualFault = 0;
        this.dayTem = 23;
        this.dayFault = 0;
        this.nightTem = 23;
        this.nightFault = 0;
        this.awayTem = 23;
        this.awayFault = 0;
        this.sensorSubnetId = 0;
        this.sensorDeviceId = 0;
        this.sensorChannelNo = 0;
        this.curTemperType = 0;
        this.zoneBeastSubID = 0;
        this.zoneBeastDevID = 0;
        this.sensorSubID = 0;
        this.sensorDevID = 0;
        this.fTSubID = 0;
        this.fTDevID = 0;
        this.CurTemperType = "";
        this.ZoneBeastSubID = "";
        this.ZoneBeastDevID = "";
        this.SensorSubID = "";
        this.SensorDevID = "";
        this.FTSubID = "";
        this.FTDevID = "";
        this.saveClick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i == 0) {
                    boolean z = true;
                    SharedPreferencesHelper.init(FloorHeatLayout.this.getContext());
                    try {
                        FloorHeatLayout.this.zoneBeastSubID = Integer.parseInt(FloorHeatLayout.this.et_zbsubid.getText().toString().trim());
                        FloorHeatLayout.this.sensorSubID = Integer.parseInt(FloorHeatLayout.this.et_sensorsubid.getText().toString().trim());
                        FloorHeatLayout.this.fTSubID = Integer.parseInt(FloorHeatLayout.this.et_4tsubid.getText().toString().trim());
                        FloorHeatLayout.this.zoneBeastDevID = Integer.parseInt(FloorHeatLayout.this.et_zbdevid.getText().toString().trim());
                        FloorHeatLayout.this.sensorDevID = Integer.parseInt(FloorHeatLayout.this.et_sensordevid.getText().toString().trim());
                        FloorHeatLayout.this.fTDevID = Integer.parseInt(FloorHeatLayout.this.et_4tdevid.getText().toString().trim());
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.CurTemperType, Integer.valueOf(FloorHeatLayout.this.curTemperType));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.ZoneBeastSubID, Integer.valueOf(FloorHeatLayout.this.zoneBeastSubID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.SensorSubID, Integer.valueOf(FloorHeatLayout.this.sensorSubID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.FTSubID, Integer.valueOf(FloorHeatLayout.this.fTSubID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.ZoneBeastDevID, Integer.valueOf(FloorHeatLayout.this.zoneBeastDevID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.SensorDevID, Integer.valueOf(FloorHeatLayout.this.sensorDevID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.FTDevID, Integer.valueOf(FloorHeatLayout.this.fTDevID));
                    } catch (Exception e) {
                        e.printStackTrace();
                        z = false;
                    }
                    if (z) {
                        Toast.makeText(FloorHeatLayout.this.getContext(), "success", 0).show();
                        FloorHeatLayout.this.curTemperType = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.CurTemperType, 0)).intValue();
                        FloorHeatLayout.this.zoneBeastSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.ZoneBeastSubID, 0)).intValue();
                        FloorHeatLayout.this.zoneBeastDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.ZoneBeastDevID, 0)).intValue();
                        FloorHeatLayout.this.sensorSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.SensorSubID, 0)).intValue();
                        FloorHeatLayout.this.sensorDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.SensorDevID, 0)).intValue();
                        FloorHeatLayout.this.fTSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.FTSubID, 0)).intValue();
                        FloorHeatLayout.this.fTDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.FTDevID, 0)).intValue();
                        FloorHeatLayout.this.tv_curtemper.setText("N/A");
                        FloorHeatLayout.this.readCurTemperByType(FloorHeatLayout.this.curTemperType);
                        return;
                    }
                    Toast.makeText(FloorHeatLayout.this.getContext(), "error", 0).show();
                }
            }
        };
        this.rgCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.9
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_auto /* 2131231661 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 5, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(0);
                        FloorHeatLayout.this.layout_night.setVisibility(0);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                    case R.id.rb_away /* 2131231662 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 4, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(8);
                        FloorHeatLayout.this.layout_night.setVisibility(8);
                        FloorHeatLayout.this.layout_away.setVisibility(0);
                        break;
                    case R.id.rb_day /* 2131231663 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 2, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(0);
                        FloorHeatLayout.this.layout_night.setVisibility(8);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                    case R.id.rb_manual /* 2131231666 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 1, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(0);
                        FloorHeatLayout.this.layout_day.setVisibility(8);
                        FloorHeatLayout.this.layout_night.setVisibility(8);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                    case R.id.rb_night /* 2131231667 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 3, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(8);
                        FloorHeatLayout.this.layout_night.setVisibility(0);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                }
            }
        };
        initView();
        this.floorHeatControl = new FloorHeatControl();
    }

    public FloorHeatLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.floorHeat = null;
        this.manualTem = 23;
        this.manualFault = 0;
        this.dayTem = 23;
        this.dayFault = 0;
        this.nightTem = 23;
        this.nightFault = 0;
        this.awayTem = 23;
        this.awayFault = 0;
        this.sensorSubnetId = 0;
        this.sensorDeviceId = 0;
        this.sensorChannelNo = 0;
        this.curTemperType = 0;
        this.zoneBeastSubID = 0;
        this.zoneBeastDevID = 0;
        this.sensorSubID = 0;
        this.sensorDevID = 0;
        this.fTSubID = 0;
        this.fTDevID = 0;
        this.CurTemperType = "";
        this.ZoneBeastSubID = "";
        this.ZoneBeastDevID = "";
        this.SensorSubID = "";
        this.SensorDevID = "";
        this.FTSubID = "";
        this.FTDevID = "";
        this.saveClick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.3
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i == 0) {
                    boolean z = true;
                    SharedPreferencesHelper.init(FloorHeatLayout.this.getContext());
                    try {
                        FloorHeatLayout.this.zoneBeastSubID = Integer.parseInt(FloorHeatLayout.this.et_zbsubid.getText().toString().trim());
                        FloorHeatLayout.this.sensorSubID = Integer.parseInt(FloorHeatLayout.this.et_sensorsubid.getText().toString().trim());
                        FloorHeatLayout.this.fTSubID = Integer.parseInt(FloorHeatLayout.this.et_4tsubid.getText().toString().trim());
                        FloorHeatLayout.this.zoneBeastDevID = Integer.parseInt(FloorHeatLayout.this.et_zbdevid.getText().toString().trim());
                        FloorHeatLayout.this.sensorDevID = Integer.parseInt(FloorHeatLayout.this.et_sensordevid.getText().toString().trim());
                        FloorHeatLayout.this.fTDevID = Integer.parseInt(FloorHeatLayout.this.et_4tdevid.getText().toString().trim());
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.CurTemperType, Integer.valueOf(FloorHeatLayout.this.curTemperType));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.ZoneBeastSubID, Integer.valueOf(FloorHeatLayout.this.zoneBeastSubID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.SensorSubID, Integer.valueOf(FloorHeatLayout.this.sensorSubID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.FTSubID, Integer.valueOf(FloorHeatLayout.this.fTSubID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.ZoneBeastDevID, Integer.valueOf(FloorHeatLayout.this.zoneBeastDevID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.SensorDevID, Integer.valueOf(FloorHeatLayout.this.sensorDevID));
                        SharedPreferencesHelper.getInstance().saveData(FloorHeatLayout.this.FTDevID, Integer.valueOf(FloorHeatLayout.this.fTDevID));
                    } catch (Exception e) {
                        e.printStackTrace();
                        z = false;
                    }
                    if (z) {
                        Toast.makeText(FloorHeatLayout.this.getContext(), "success", 0).show();
                        FloorHeatLayout.this.curTemperType = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.CurTemperType, 0)).intValue();
                        FloorHeatLayout.this.zoneBeastSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.ZoneBeastSubID, 0)).intValue();
                        FloorHeatLayout.this.zoneBeastDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.ZoneBeastDevID, 0)).intValue();
                        FloorHeatLayout.this.sensorSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.SensorSubID, 0)).intValue();
                        FloorHeatLayout.this.sensorDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.SensorDevID, 0)).intValue();
                        FloorHeatLayout.this.fTSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.FTSubID, 0)).intValue();
                        FloorHeatLayout.this.fTDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(FloorHeatLayout.this.FTDevID, 0)).intValue();
                        FloorHeatLayout.this.tv_curtemper.setText("N/A");
                        FloorHeatLayout.this.readCurTemperByType(FloorHeatLayout.this.curTemperType);
                        return;
                    }
                    Toast.makeText(FloorHeatLayout.this.getContext(), "error", 0).show();
                }
            }
        };
        this.rgCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.9
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_auto /* 2131231661 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 5, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(0);
                        FloorHeatLayout.this.layout_night.setVisibility(0);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                    case R.id.rb_away /* 2131231662 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 4, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(8);
                        FloorHeatLayout.this.layout_night.setVisibility(8);
                        FloorHeatLayout.this.layout_away.setVisibility(0);
                        break;
                    case R.id.rb_day /* 2131231663 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 2, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(0);
                        FloorHeatLayout.this.layout_night.setVisibility(8);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                    case R.id.rb_manual /* 2131231666 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 1, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(0);
                        FloorHeatLayout.this.layout_day.setVisibility(8);
                        FloorHeatLayout.this.layout_night.setVisibility(8);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                    case R.id.rb_night /* 2131231667 */:
                        FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 21, 3, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                        FloorHeatLayout.this.layout_manual.setVisibility(8);
                        FloorHeatLayout.this.layout_day.setVisibility(8);
                        FloorHeatLayout.this.layout_night.setVisibility(0);
                        FloorHeatLayout.this.layout_away.setVisibility(8);
                        break;
                }
            }
        };
        initView();
        this.floorHeatControl = new FloorHeatControl();
    }

    public void setNullStateUI() {
        this.tv_curtemper.setText("N/A");
        this.bt_on.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.bt_off.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.layout_core.setVisibility(8);
    }

    public void setFH(final Savefloorheat savefloorheat) {
        this.floorHeat = savefloorheat;
        this.tv_devicename.setText("FH:" + savefloorheat.floorheat_remark);
        setNullStateUI();
        initCurTemperData();
        new Thread(new Runnable() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.1
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                for (int i = 0; i < 5; i++) {
                    switch (i) {
                        case 0:
                            FloorHeatLayout.this.floorHeatControl.ReadOnOrOFFAndModeControl((byte) savefloorheat.subnetID, (byte) savefloorheat.deviceID, (byte) 20, savefloorheat.channel, MainActivity.mydupsocket);
                            break;
                        case 1:
                            FloorHeatLayout.this.floorHeatControl.ReadOnOrOFFAndModeControl((byte) savefloorheat.subnetID, (byte) savefloorheat.deviceID, (byte) 21, savefloorheat.channel, MainActivity.mydupsocket);
                            break;
                        case 2:
                            FloorHeatLayout.this.floorHeatControl.ReadodeModeStateAndSensorAddressControl((byte) savefloorheat.subnetID, (byte) savefloorheat.deviceID, savefloorheat.channel, MainActivity.mydupsocket);
                            break;
                        case 3:
                            FloorHeatLayout.this.floorHeatControl.ReadTimeControl((byte) savefloorheat.subnetID, (byte) savefloorheat.deviceID, savefloorheat.channel, MainActivity.mydupsocket);
                            break;
                        case 4:
                            try {
                                FloorHeatLayout.this.readCurTemperByType(FloorHeatLayout.this.curTemperType);
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                    }
                    Thread.sleep(300L);
                }
            }
        }).start();
    }

    public void initView() {
        this.rootView = View.inflate(getContext(), R.layout.floorheat_layout, this);
        this.tv_devicename = (TextView) this.rootView.findViewById(R.id.tv_devicename);
        this.tv_curtemper = (TextView) this.rootView.findViewById(R.id.tv_curtemper);
        this.bt_on = (Button) this.rootView.findViewById(R.id.bt_on);
        this.bt_off = (Button) this.rootView.findViewById(R.id.bt_off);
        this.layout_core = (LinearLayout) this.rootView.findViewById(R.id.layout_core);
        this.layout_manual = (LinearLayout) this.rootView.findViewById(R.id.layout_manual);
        this.layout_day = (LinearLayout) this.rootView.findViewById(R.id.layout_day);
        this.layout_night = (LinearLayout) this.rootView.findViewById(R.id.layout_night);
        this.layout_away = (LinearLayout) this.rootView.findViewById(R.id.layout_away);
        this.layout_curtemper = (LinearLayout) this.rootView.findViewById(R.id.layout_curtemper);
        this.rg_group = (RadioGroup) this.rootView.findViewById(R.id.rg_group);
        this.rb_manual = (RadioButton) this.rootView.findViewById(R.id.rb_manual);
        this.rb_day = (RadioButton) this.rootView.findViewById(R.id.rb_day);
        this.rb_night = (RadioButton) this.rootView.findViewById(R.id.rb_night);
        this.rb_away = (RadioButton) this.rootView.findViewById(R.id.rb_away);
        this.rb_auto = (RadioButton) this.rootView.findViewById(R.id.rb_auto);
        this.sb_manual = (SeekBar) this.rootView.findViewById(R.id.sb_manual);
        this.sb_day = (SeekBar) this.rootView.findViewById(R.id.sb_day);
        this.sb_night = (SeekBar) this.rootView.findViewById(R.id.sb_night);
        this.sb_away = (SeekBar) this.rootView.findViewById(R.id.sb_away);
        this.tv_manual = (TextView) this.rootView.findViewById(R.id.tv_manual);
        this.tv_day = (TextView) this.rootView.findViewById(R.id.tv_day);
        this.tv_night = (TextView) this.rootView.findViewById(R.id.tv_night);
        this.tv_away = (TextView) this.rootView.findViewById(R.id.tv_away);
        this.bt_daytime = (Button) this.rootView.findViewById(R.id.bt_daytime);
        this.bt_nighttime = (Button) this.rootView.findViewById(R.id.bt_nighttime);
        this.sb_manual.setProgress(5);
        this.sb_day.setProgress(5);
        this.sb_night.setProgress(5);
        this.sb_away.setProgress(5);
        setListener();
    }

    public void initCurTemperData() {
        this.CurTemperType = "floorHeatCurTemperType" + this.floorHeat._id;
        this.ZoneBeastSubID = "floorHeatZoneBeastSubID" + this.floorHeat._id;
        this.ZoneBeastDevID = "floorHeatZoneBeastDevID" + this.floorHeat._id;
        this.SensorSubID = "floorHeatSensorSubID" + this.floorHeat._id;
        this.SensorDevID = "floorHeatSensorDevID" + this.floorHeat._id;
        this.FTSubID = "floorHeatFTSubID" + this.floorHeat._id;
        this.FTDevID = "floorHeatFTDevID" + this.floorHeat._id;
        SharedPreferencesHelper.init(getContext());
        this.curTemperType = ((Integer) SharedPreferencesHelper.getInstance().getData(this.CurTemperType, 0)).intValue();
        this.zoneBeastSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.ZoneBeastSubID, 0)).intValue();
        this.zoneBeastDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.ZoneBeastDevID, 0)).intValue();
        this.sensorSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.SensorSubID, 0)).intValue();
        this.sensorDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.SensorDevID, 0)).intValue();
        this.fTSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.FTSubID, 0)).intValue();
        this.fTDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.FTDevID, 0)).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCurTemperSettingView() {
        AlertView alertView = new AlertView("Read temperature settings", null, "CANCEL", new String[]{"SAVE"}, null, getContext(), AlertView.Style.Alert, this.saveClick);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.setting_hvaccurtemper, (ViewGroup) null);
        this.rg_Temgroup = (RadioGroup) viewGroup.findViewById(R.id.rg_group);
        this.rb_hvac = (RadioButton) viewGroup.findViewById(R.id.rb_hvac);
        this.rb_hvac.setText("Floor Heat");
        this.rb_zb = (RadioButton) viewGroup.findViewById(R.id.rb_zb);
        this.rb_sensor = (RadioButton) viewGroup.findViewById(R.id.rb_sensor);
        this.rb_4t = (RadioButton) viewGroup.findViewById(R.id.rb_4t);
        this.et_hvacsubid = (EditText) viewGroup.findViewById(R.id.et_hvacsubid);
        this.et_zbsubid = (EditText) viewGroup.findViewById(R.id.et_zbsubid);
        this.et_sensorsubid = (EditText) viewGroup.findViewById(R.id.et_sensorsubid);
        this.et_4tsubid = (EditText) viewGroup.findViewById(R.id.et_4tsubid);
        this.et_hvacdevid = (EditText) viewGroup.findViewById(R.id.et_hvacdevid);
        this.et_zbdevid = (EditText) viewGroup.findViewById(R.id.et_zbdevid);
        this.et_sensordevid = (EditText) viewGroup.findViewById(R.id.et_sensordevid);
        this.et_4tdevid = (EditText) viewGroup.findViewById(R.id.et_4tdevid);
        SharedPreferencesHelper.init(getContext());
        switch (((Integer) SharedPreferencesHelper.getInstance().getData(this.CurTemperType, 0)).intValue()) {
            case 0:
                this.rb_hvac.setChecked(true);
                break;
            case 1:
                this.rb_zb.setChecked(true);
                break;
            case 2:
                this.rb_sensor.setChecked(true);
                break;
            case 3:
                this.rb_4t.setChecked(true);
                break;
        }
        this.et_hvacsubid.setText("" + this.sensorSubnetId);
        this.et_zbsubid.setText("" + SharedPreferencesHelper.getInstance().getData(this.ZoneBeastSubID, 0));
        this.et_sensorsubid.setText("" + SharedPreferencesHelper.getInstance().getData(this.SensorSubID, 0));
        this.et_4tsubid.setText("" + SharedPreferencesHelper.getInstance().getData(this.FTSubID, 0));
        this.et_hvacdevid.setText("" + this.sensorDeviceId);
        this.et_zbdevid.setText("" + SharedPreferencesHelper.getInstance().getData(this.ZoneBeastDevID, 0));
        this.et_sensordevid.setText("" + SharedPreferencesHelper.getInstance().getData(this.SensorDevID, 0));
        this.et_4tdevid.setText("" + SharedPreferencesHelper.getInstance().getData(this.FTDevID, 0));
        this.rg_Temgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_4t) {
                    FloorHeatLayout.this.curTemperType = 3;
                    return;
                }
                if (i == R.id.rb_hvac) {
                    FloorHeatLayout.this.curTemperType = 0;
                } else if (i == R.id.rb_sensor) {
                    FloorHeatLayout.this.curTemperType = 2;
                } else {
                    if (i != R.id.rb_zb) {
                        return;
                    }
                    FloorHeatLayout.this.curTemperType = 1;
                }
            }
        });
        alertView.addExtView(viewGroup);
        alertView.show();
    }

    public void readCurTemperByType(int i) {
        switch (i) {
            case 0:
                this.floorHeatControl.ReadTemperatureValue((byte) this.sensorSubnetId, (byte) this.sensorDeviceId, MainActivity.mydupsocket);
                break;
            case 1:
                this.floorHeatControl.ReadTemperatureValue((byte) this.zoneBeastSubID, (byte) this.zoneBeastDevID, MainActivity.mydupsocket);
                break;
            case 2:
                this.floorHeatControl.ReadTemperatureValue((byte) this.sensorSubID, (byte) this.sensorDevID, MainActivity.mydupsocket);
                break;
            case 3:
                this.floorHeatControl.ReadTemperatureValue((byte) this.fTSubID, (byte) this.fTDevID, MainActivity.mydupsocket);
                break;
        }
    }

    public void setListener() {
        this.bt_on.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FloorHeatLayout.this.setONStyle();
                FloorHeatLayout.this.layout_core.setVisibility(0);
                FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 20, 1, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
            }
        });
        this.bt_off.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FloorHeatLayout.this.setOFFStyle();
                FloorHeatLayout.this.layout_core.setVisibility(8);
                FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 20, 0, FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
            }
        });
        this.rg_group.setOnCheckedChangeListener(this.rgCheckedChangeListener);
        this.bt_daytime.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FloorHeatLayout.this.showDayTimeDialog();
            }
        });
        this.bt_nighttime.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FloorHeatLayout.this.showNightTimeDialog();
            }
        });
        setSeekBarChangeListener(this.sb_manual, this.tv_manual);
        setSeekBarChangeListener(this.sb_day, this.tv_day);
        setSeekBarChangeListener(this.sb_night, this.tv_night);
        setSeekBarChangeListener(this.sb_away, this.tv_away);
        this.layout_curtemper.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FloorHeatLayout.this.showCurTemperSettingView();
            }
        });
    }

    public void setONStyle() {
        this.bt_on.setBackground(getResources().getDrawable(R.drawable.control_blue_10));
        this.bt_off.setBackground(getResources().getDrawable(R.drawable.control_back_10));
    }

    public void setOFFStyle() {
        this.bt_on.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.bt_off.setBackground(getResources().getDrawable(R.drawable.control_blue_10));
    }

    public void setSeekBarChangeListener(SeekBar seekBar, final TextView textView) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.10
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                if (i < 5) {
                    seekBar2.setProgress(5);
                    return;
                }
                textView.setText(i + "°C");
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
                if (seekBar2 == FloorHeatLayout.this.sb_manual) {
                    FloorHeatLayout.this.floorHeatControl.ConfigureModeAndSensorAddressControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, FloorHeatLayout.this.floorHeat.channel, seekBar2.getProgress(), FloorHeatLayout.this.manualFault, FloorHeatLayout.this.dayTem, FloorHeatLayout.this.dayFault, FloorHeatLayout.this.nightTem, FloorHeatLayout.this.nightFault, FloorHeatLayout.this.awayTem, FloorHeatLayout.this.awayFault, FloorHeatLayout.this.sensorSubnetId, FloorHeatLayout.this.sensorDeviceId, FloorHeatLayout.this.sensorChannelNo, MainActivity.mydupsocket);
                    FloorHeatLayout.this.floorHeatControl.TypeValueControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, (byte) 23, seekBar2.getProgress(), FloorHeatLayout.this.floorHeat.channel, MainActivity.mydupsocket);
                }
                if (seekBar2 == FloorHeatLayout.this.sb_day) {
                    FloorHeatLayout.this.floorHeatControl.ConfigureModeAndSensorAddressControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, FloorHeatLayout.this.floorHeat.channel, FloorHeatLayout.this.manualTem, FloorHeatLayout.this.manualFault, seekBar2.getProgress(), FloorHeatLayout.this.dayFault, FloorHeatLayout.this.nightTem, FloorHeatLayout.this.nightFault, FloorHeatLayout.this.awayTem, FloorHeatLayout.this.awayFault, FloorHeatLayout.this.sensorSubnetId, FloorHeatLayout.this.sensorDeviceId, FloorHeatLayout.this.sensorChannelNo, MainActivity.mydupsocket);
                }
                if (seekBar2 == FloorHeatLayout.this.sb_night) {
                    FloorHeatLayout.this.floorHeatControl.ConfigureModeAndSensorAddressControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, FloorHeatLayout.this.floorHeat.channel, FloorHeatLayout.this.manualTem, FloorHeatLayout.this.manualFault, FloorHeatLayout.this.dayTem, FloorHeatLayout.this.dayFault, seekBar2.getProgress(), FloorHeatLayout.this.nightFault, FloorHeatLayout.this.awayTem, FloorHeatLayout.this.awayFault, FloorHeatLayout.this.sensorSubnetId, FloorHeatLayout.this.sensorDeviceId, FloorHeatLayout.this.sensorChannelNo, MainActivity.mydupsocket);
                }
                if (seekBar2 == FloorHeatLayout.this.sb_away) {
                    FloorHeatLayout.this.floorHeatControl.ConfigureModeAndSensorAddressControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, FloorHeatLayout.this.floorHeat.channel, FloorHeatLayout.this.manualTem, FloorHeatLayout.this.manualFault, FloorHeatLayout.this.dayTem, FloorHeatLayout.this.dayFault, FloorHeatLayout.this.nightTem, FloorHeatLayout.this.nightFault, seekBar2.getProgress(), FloorHeatLayout.this.awayFault, FloorHeatLayout.this.sensorSubnetId, FloorHeatLayout.this.sensorDeviceId, FloorHeatLayout.this.sensorChannelNo, MainActivity.mydupsocket);
                }
            }
        });
    }

    public void showDayTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.11
            @Override // android.app.TimePickerDialog.OnTimeSetListener
            public void onTimeSet(TimePicker timePicker, int i, int i2) throws NumberFormatException {
                if (i <= 9 && i2 > 9) {
                    FloorHeatLayout.this.bt_daytime.setText("DAY:0" + i + ":" + i2);
                } else if (i > 9 && i2 <= 9) {
                    FloorHeatLayout.this.bt_daytime.setText("DAY:" + i + ":0" + i2);
                } else if (i <= 9 && i2 <= 9) {
                    FloorHeatLayout.this.bt_daytime.setText("DAY:0" + i + ":0" + i2);
                } else {
                    FloorHeatLayout.this.bt_daytime.setText("DAY:" + i + ":" + i2);
                }
                String string = FloorHeatLayout.this.bt_daytime.getText().toString();
                int iIndexOf = string.indexOf(":", 4);
                String strSubstring = string.substring(iIndexOf - 2, iIndexOf);
                String strSubstring2 = string.substring(iIndexOf + 1, iIndexOf + 3);
                String string2 = FloorHeatLayout.this.bt_nighttime.getText().toString();
                int iIndexOf2 = string2.indexOf(":", 6);
                try {
                    FloorHeatLayout.this.floorHeatControl.ConfigureTimeControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, FloorHeatLayout.this.floorHeat.channel, Integer.parseInt(strSubstring), Integer.parseInt(strSubstring2), Integer.parseInt(string2.substring(iIndexOf2 - 2, iIndexOf2)), Integer.parseInt(string2.substring(iIndexOf2 + 1, iIndexOf2 + 3)), MainActivity.mydupsocket);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FloorHeatLayout.this.getContext(), "error", 0).show();
                }
            }
        }, calendar.get(11), calendar.get(12), true).show();
    }

    public void showNightTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() { // from class: com.shima.smartbushome.selflayout.FloorHeatLayout.12
            @Override // android.app.TimePickerDialog.OnTimeSetListener
            public void onTimeSet(TimePicker timePicker, int i, int i2) throws NumberFormatException {
                if (i <= 9 && i2 > 9) {
                    FloorHeatLayout.this.bt_nighttime.setText("NIGHT:0" + i + ":" + i2);
                } else if (i > 9 && i2 <= 9) {
                    FloorHeatLayout.this.bt_nighttime.setText("NIGHT:" + i + ":0" + i2);
                } else if (i <= 9 && i2 <= 9) {
                    FloorHeatLayout.this.bt_nighttime.setText("NIGHT:0" + i + ":0" + i2);
                } else {
                    FloorHeatLayout.this.bt_nighttime.setText("NIGHT:" + i + ":" + i2);
                }
                String string = FloorHeatLayout.this.bt_daytime.getText().toString();
                int iIndexOf = string.indexOf(":", 4);
                String strSubstring = string.substring(iIndexOf - 2, iIndexOf);
                String strSubstring2 = string.substring(iIndexOf + 1, iIndexOf + 3);
                String string2 = FloorHeatLayout.this.bt_nighttime.getText().toString();
                int iIndexOf2 = string2.indexOf(":", 6);
                try {
                    FloorHeatLayout.this.floorHeatControl.ConfigureTimeControl((byte) FloorHeatLayout.this.floorHeat.subnetID, (byte) FloorHeatLayout.this.floorHeat.deviceID, FloorHeatLayout.this.floorHeat.channel, Integer.parseInt(strSubstring), Integer.parseInt(strSubstring2), Integer.parseInt(string2.substring(iIndexOf2 - 2, iIndexOf2)), Integer.parseInt(string2.substring(iIndexOf2 + 1, iIndexOf2 + 3)), MainActivity.mydupsocket);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FloorHeatLayout.this.getContext(), "error", 0).show();
                }
            }
        }, calendar.get(11), calendar.get(12), true).show();
    }

    public void setFeedBackData(byte[] bArr) {
        if (this.floorHeat == null) {
        }
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        int i2 = bArr[17] & 255;
        int i3 = bArr[18] & 255;
        if (i == 968) {
            int i4 = bArr[25] & 255;
            if (this.floorHeat.subnetID == i2 && this.floorHeat.deviceID == i3 && this.floorHeat.channel == i4) {
                this.manualTem = bArr[26] & 255;
                this.manualFault = bArr[27] & 255;
                this.dayTem = bArr[28] & 255;
                this.dayFault = bArr[29] & 255;
                this.nightTem = bArr[30] & 255;
                this.nightFault = bArr[31] & 255;
                this.awayTem = bArr[32] & 255;
                this.awayFault = bArr[33] & 255;
                this.sensorSubnetId = bArr[34] & 255;
                this.sensorDeviceId = bArr[35] & 255;
                this.sensorChannelNo = bArr[36] & 255;
                this.sb_manual.setOnSeekBarChangeListener(null);
                this.sb_day.setOnSeekBarChangeListener(null);
                this.sb_night.setOnSeekBarChangeListener(null);
                this.sb_away.setOnSeekBarChangeListener(null);
                this.sb_manual.setProgress(this.manualTem);
                this.sb_day.setProgress(this.dayTem);
                this.sb_night.setProgress(this.nightTem);
                this.sb_away.setProgress(this.awayTem);
                this.tv_manual.setText(this.manualTem + "°C");
                this.tv_day.setText(this.dayTem + "°C");
                this.tv_night.setText(this.nightTem + "°C");
                this.tv_away.setText(this.awayTem + "°C");
                setSeekBarChangeListener(this.sb_manual, this.tv_manual);
                setSeekBarChangeListener(this.sb_day, this.tv_day);
                setSeekBarChangeListener(this.sb_night, this.tv_night);
                setSeekBarChangeListener(this.sb_away, this.tv_away);
                return;
            }
            return;
        }
        if (i == 972) {
            int i5 = bArr[25] & 255;
            if (this.floorHeat.subnetID == i2 && this.floorHeat.deviceID == i3 && this.floorHeat.channel == i5) {
                int i6 = bArr[26] & 255;
                int i7 = bArr[27] & 255;
                int i8 = bArr[28] & 255;
                int i9 = bArr[29] & 255;
                if (i6 <= 9 && i7 > 9) {
                    this.bt_daytime.setText("DAY:0" + i6 + ":" + i7);
                } else if (i6 > 9 && i7 <= 9) {
                    this.bt_daytime.setText("DAY:" + i6 + ":0" + i7);
                } else if (i6 <= 9 && i7 <= 9) {
                    this.bt_daytime.setText("DAY:0" + i6 + ":0" + i7);
                } else {
                    this.bt_daytime.setText("DAY:" + i6 + ":" + i7);
                }
                if (i8 <= 9 && i9 > 9) {
                    this.bt_nighttime.setText("NIGHT:0" + i8 + ":" + i9);
                    return;
                }
                if (i8 > 9 && i9 <= 9) {
                    this.bt_nighttime.setText("NIGHT:" + i8 + ":0" + i9);
                    return;
                }
                if (i8 <= 9 && i9 <= 9) {
                    this.bt_nighttime.setText("NIGHT:0" + i8 + ":0" + i9);
                    return;
                }
                this.bt_nighttime.setText("NIGHT:" + i8 + ":" + i9);
                return;
            }
            return;
        }
        if (i == 58329 || i == 58331) {
            int i10 = bArr[27] & 255;
            if (this.floorHeat.subnetID == i2 && this.floorHeat.deviceID == i3 && this.floorHeat.channel == i10) {
                byte b = bArr[25];
                int i11 = bArr[26] & 255;
                if (b == 20) {
                    switch (i11) {
                        case 0:
                            setOFFStyle();
                            this.layout_core.setVisibility(8);
                            break;
                        case 1:
                            setONStyle();
                            this.layout_core.setVisibility(0);
                            break;
                    }
                }
                if (b != 21) {
                    if (b == 23) {
                        this.manualTem = i11;
                        this.sb_manual.setOnSeekBarChangeListener(null);
                        this.sb_manual.setProgress(this.manualTem);
                        this.tv_manual.setText(this.manualTem + "°C");
                        setSeekBarChangeListener(this.sb_manual, this.tv_manual);
                        return;
                    }
                    return;
                }
                this.rg_group.setOnCheckedChangeListener(null);
                switch (i11) {
                    case 1:
                        this.rb_manual.setChecked(true);
                        this.layout_manual.setVisibility(0);
                        this.layout_day.setVisibility(8);
                        this.layout_night.setVisibility(8);
                        this.layout_away.setVisibility(8);
                        break;
                    case 2:
                        this.rb_day.setChecked(true);
                        this.layout_manual.setVisibility(8);
                        this.layout_day.setVisibility(0);
                        this.layout_night.setVisibility(8);
                        this.layout_away.setVisibility(8);
                        break;
                    case 3:
                        this.rb_night.setChecked(true);
                        this.layout_manual.setVisibility(8);
                        this.layout_day.setVisibility(8);
                        this.layout_night.setVisibility(0);
                        this.layout_away.setVisibility(8);
                        break;
                    case 4:
                        this.rb_away.setChecked(true);
                        this.layout_manual.setVisibility(8);
                        this.layout_day.setVisibility(8);
                        this.layout_night.setVisibility(8);
                        this.layout_away.setVisibility(0);
                        break;
                    case 5:
                        this.rb_auto.setChecked(true);
                        this.layout_manual.setVisibility(8);
                        this.layout_day.setVisibility(0);
                        this.layout_night.setVisibility(0);
                        this.layout_away.setVisibility(8);
                        break;
                }
                this.rg_group.setOnCheckedChangeListener(this.rgCheckedChangeListener);
                return;
            }
            return;
        }
        if (i != 58344) {
            return;
        }
        switch (this.curTemperType) {
            case 0:
                if (this.sensorSubnetId == i2 && this.sensorDeviceId == i3) {
                    byte b2 = bArr[25];
                    if (bArr.length - 26 >= this.sensorChannelNo) {
                        int i12 = bArr[26];
                        if (this.sensorChannelNo > 1) {
                            i12 = bArr[this.sensorChannelNo + 25];
                        }
                        if (b2 == 0) {
                            double d = i12 - 32;
                            Double.isNaN(d);
                            i12 = (int) (d / 1.8d);
                        }
                        this.tv_curtemper.setText(i12 + "°C");
                        break;
                    }
                }
                break;
            case 1:
                if (this.zoneBeastSubID == i2 && this.zoneBeastDevID == i3) {
                    byte b3 = bArr[25];
                    int i13 = bArr[26];
                    if (b3 == 0) {
                        double d2 = i13 - 32;
                        Double.isNaN(d2);
                        i13 = (int) (d2 / 1.8d);
                    }
                    this.tv_curtemper.setText(i13 + "°C");
                    break;
                }
                break;
            case 2:
                if (this.sensorSubID == i2 && this.sensorDevID == i3) {
                    byte b4 = bArr[25];
                    int i14 = bArr[26];
                    if (b4 == 0) {
                        double d3 = i14 - 32;
                        Double.isNaN(d3);
                        i14 = (int) (d3 / 1.8d);
                    }
                    this.tv_curtemper.setText(i14 + "°C");
                    break;
                }
                break;
            case 3:
                if (this.fTSubID == i2 && this.fTDevID == i3) {
                    byte b5 = bArr[25];
                    int i15 = bArr[26];
                    if (b5 == 0) {
                        double d4 = i15 - 32;
                        Double.isNaN(d4);
                        i15 = (int) (d4 / 1.8d);
                    }
                    this.tv_curtemper.setText(i15 + "°C");
                    break;
                }
                break;
        }
    }
}
