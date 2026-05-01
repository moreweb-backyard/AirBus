package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.founction_command.niocontrol;
import com.shima.smartbushome.util.SharedPreferencesHelper;

/* loaded from: classes.dex */
public class HVACLayout extends RelativeLayout implements View.OnClickListener {
    private static final byte const_ac_cmd_type_onoff = 3;
    private static final byte const_ac_cmd_type_set_auto_tmp = 8;
    private static final byte const_ac_cmd_type_set_cold_tmp = 4;
    private static final byte const_ac_cmd_type_set_fan = 5;
    private static final byte const_ac_cmd_type_set_heat_tmp = 7;
    private static final byte const_ac_cmd_type_set_mode = 6;
    private static final byte const_fan_speed_anto = 0;
    private static final byte const_fan_speed_high = 1;
    private static final byte const_fan_speed_low = 3;
    private static final byte const_fan_speed_medium = 2;
    private static final byte const_mode_auto = 3;
    private static final byte const_mode_cool = 0;
    private static final byte const_mode_fan = 2;
    private static final byte const_mode_heat = 1;
    public byte AutoTemp;
    public byte CoolTemp;
    public String CurTemperType;
    public byte CurrentFanMode;
    public byte CurrentMode;
    public byte CurrentTem;
    public byte CurrentUnit;
    public String FTDevID;
    public String FTSubID;
    public byte HeatTemp;
    public String SensorDevID;
    public String SensorSubID;
    public String ZoneBeastDevID;
    public String ZoneBeastSubID;
    ACcontrol acc;
    private Button autofan;
    private Button automode;
    private byte const_auto_temp_max;
    private byte const_auto_temp_min;
    private byte const_cool_temp_max;
    private byte const_cool_temp_min;
    private byte const_heat_temp_max;
    private byte const_heat_temp_min;
    private Button coolmode;
    public int curTemperType;
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
    private byte[] fanArray;
    private Button fanmode;
    TextView fanremark;
    TextView fanstate;
    private Button heatmode;
    private Button highfan;
    private LinearLayout layout_curtemper;
    private Button lowfan;
    private Button mediumfan;
    private byte[] modeArray;
    TextView moderemark;
    TextView modestate;
    TextView name;
    niocontrol niocl;
    private Button off;
    private Button on;
    boolean powerstate;
    RadioButton rb_4t;
    RadioButton rb_hvac;
    RadioButton rb_sensor;
    RadioButton rb_zb;
    boolean readCF;
    boolean readCountfanAndMode;
    boolean readTempRange;
    boolean readcstate;
    Runnable reflashrun;
    int reflashstate;
    Handler reflashuihandler;
    RadioGroup rg_group;
    public OnItemClickListener saveClick;
    public int sensorDevID;
    public int sensorSubID;
    private Button tempdowm;
    TextView tempstate;
    private Button tempup;
    private Savehvac thishvac;
    TextView tv_curtemper;
    int uicount;
    TextView uint;
    public int zoneBeastDevID;
    public int zoneBeastSubID;

    public HVACLayout(Context context) {
        super(context);
        this.thishvac = new Savehvac();
        this.reflashuihandler = new Handler();
        this.powerstate = false;
        this.CurrentMode = (byte) 3;
        this.CoolTemp = (byte) 20;
        this.HeatTemp = (byte) 20;
        this.AutoTemp = (byte) 20;
        this.CurrentFanMode = (byte) 2;
        this.CurrentTem = (byte) -1;
        this.CurrentUnit = (byte) 0;
        this.const_cool_temp_min = (byte) 0;
        this.const_cool_temp_max = (byte) 30;
        this.const_heat_temp_min = (byte) 20;
        this.const_heat_temp_max = (byte) 30;
        this.const_auto_temp_min = (byte) 0;
        this.const_auto_temp_max = (byte) 30;
        this.reflashstate = 0;
        this.uicount = 0;
        this.readCF = false;
        this.readTempRange = false;
        this.readCountfanAndMode = false;
        this.readcstate = false;
        this.reflashrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.HVACLayout.1
            @Override // java.lang.Runnable
            public void run() {
                HVACLayout.this.uicount++;
                if (HVACLayout.this.uicount <= 50) {
                    switch (HVACLayout.this.reflashstate) {
                        case 0:
                            HVACLayout.this.acc.ACReadCFFlag((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 1:
                            HVACLayout.this.acc.ACReadTempRange((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 2:
                            HVACLayout.this.acc.ACReadCountFanAndMode((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 3:
                            HVACLayout.this.acc.ACReadCState((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 4:
                            HVACLayout.this.reflashstate = 0;
                            HVACLayout.this.uicount = 0;
                            HVACLayout.this.readCF = false;
                            HVACLayout.this.readTempRange = false;
                            HVACLayout.this.readCountfanAndMode = false;
                            HVACLayout.this.readcstate = false;
                            HVACLayout.this.reflashuihandler.removeCallbacks(HVACLayout.this.reflashrun);
                            break;
                    }
                    return;
                }
                HVACLayout.this.reflashstate = 0;
                HVACLayout.this.uicount = 0;
                HVACLayout.this.readCF = false;
                HVACLayout.this.readTempRange = false;
                HVACLayout.this.readCountfanAndMode = false;
                HVACLayout.this.readcstate = false;
                HVACLayout.this.reflashuihandler.removeCallbacks(HVACLayout.this.reflashrun);
            }
        };
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
        this.saveClick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.HVACLayout.5
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i == 0) {
                    boolean z = true;
                    SharedPreferencesHelper.init(HVACLayout.this.getContext());
                    try {
                        HVACLayout.this.zoneBeastSubID = Integer.parseInt(HVACLayout.this.et_zbsubid.getText().toString().trim());
                        HVACLayout.this.sensorSubID = Integer.parseInt(HVACLayout.this.et_sensorsubid.getText().toString().trim());
                        HVACLayout.this.fTSubID = Integer.parseInt(HVACLayout.this.et_4tsubid.getText().toString().trim());
                        HVACLayout.this.zoneBeastDevID = Integer.parseInt(HVACLayout.this.et_zbdevid.getText().toString().trim());
                        HVACLayout.this.sensorDevID = Integer.parseInt(HVACLayout.this.et_sensordevid.getText().toString().trim());
                        HVACLayout.this.fTDevID = Integer.parseInt(HVACLayout.this.et_4tdevid.getText().toString().trim());
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.CurTemperType, Integer.valueOf(HVACLayout.this.curTemperType));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.ZoneBeastSubID, Integer.valueOf(HVACLayout.this.zoneBeastSubID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.SensorSubID, Integer.valueOf(HVACLayout.this.sensorSubID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.FTSubID, Integer.valueOf(HVACLayout.this.fTSubID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.ZoneBeastDevID, Integer.valueOf(HVACLayout.this.zoneBeastDevID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.SensorDevID, Integer.valueOf(HVACLayout.this.sensorDevID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.FTDevID, Integer.valueOf(HVACLayout.this.fTDevID));
                    } catch (Exception e) {
                        e.printStackTrace();
                        z = false;
                    }
                    if (z) {
                        Toast.makeText(HVACLayout.this.getContext(), "success", 0).show();
                        HVACLayout.this.curTemperType = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.CurTemperType, 0)).intValue();
                        HVACLayout.this.zoneBeastSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.ZoneBeastSubID, 0)).intValue();
                        HVACLayout.this.zoneBeastDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.ZoneBeastDevID, 0)).intValue();
                        HVACLayout.this.sensorSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.SensorSubID, 0)).intValue();
                        HVACLayout.this.sensorDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.SensorDevID, 0)).intValue();
                        HVACLayout.this.fTSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.FTSubID, 0)).intValue();
                        HVACLayout.this.fTDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.FTDevID, 0)).intValue();
                        HVACLayout.this.readCurTemperByType(HVACLayout.this.curTemperType);
                        return;
                    }
                    Toast.makeText(HVACLayout.this.getContext(), "error", 0).show();
                }
            }
        };
        initview(context);
    }

    public HVACLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.thishvac = new Savehvac();
        this.reflashuihandler = new Handler();
        this.powerstate = false;
        this.CurrentMode = (byte) 3;
        this.CoolTemp = (byte) 20;
        this.HeatTemp = (byte) 20;
        this.AutoTemp = (byte) 20;
        this.CurrentFanMode = (byte) 2;
        this.CurrentTem = (byte) -1;
        this.CurrentUnit = (byte) 0;
        this.const_cool_temp_min = (byte) 0;
        this.const_cool_temp_max = (byte) 30;
        this.const_heat_temp_min = (byte) 20;
        this.const_heat_temp_max = (byte) 30;
        this.const_auto_temp_min = (byte) 0;
        this.const_auto_temp_max = (byte) 30;
        this.reflashstate = 0;
        this.uicount = 0;
        this.readCF = false;
        this.readTempRange = false;
        this.readCountfanAndMode = false;
        this.readcstate = false;
        this.reflashrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.HVACLayout.1
            @Override // java.lang.Runnable
            public void run() {
                HVACLayout.this.uicount++;
                if (HVACLayout.this.uicount <= 50) {
                    switch (HVACLayout.this.reflashstate) {
                        case 0:
                            HVACLayout.this.acc.ACReadCFFlag((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 1:
                            HVACLayout.this.acc.ACReadTempRange((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 2:
                            HVACLayout.this.acc.ACReadCountFanAndMode((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 3:
                            HVACLayout.this.acc.ACReadCState((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, MainActivity.mydupsocket);
                            HVACLayout.this.reflashuihandler.postDelayed(HVACLayout.this.reflashrun, 70L);
                            break;
                        case 4:
                            HVACLayout.this.reflashstate = 0;
                            HVACLayout.this.uicount = 0;
                            HVACLayout.this.readCF = false;
                            HVACLayout.this.readTempRange = false;
                            HVACLayout.this.readCountfanAndMode = false;
                            HVACLayout.this.readcstate = false;
                            HVACLayout.this.reflashuihandler.removeCallbacks(HVACLayout.this.reflashrun);
                            break;
                    }
                    return;
                }
                HVACLayout.this.reflashstate = 0;
                HVACLayout.this.uicount = 0;
                HVACLayout.this.readCF = false;
                HVACLayout.this.readTempRange = false;
                HVACLayout.this.readCountfanAndMode = false;
                HVACLayout.this.readcstate = false;
                HVACLayout.this.reflashuihandler.removeCallbacks(HVACLayout.this.reflashrun);
            }
        };
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
        this.saveClick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.HVACLayout.5
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i == 0) {
                    boolean z = true;
                    SharedPreferencesHelper.init(HVACLayout.this.getContext());
                    try {
                        HVACLayout.this.zoneBeastSubID = Integer.parseInt(HVACLayout.this.et_zbsubid.getText().toString().trim());
                        HVACLayout.this.sensorSubID = Integer.parseInt(HVACLayout.this.et_sensorsubid.getText().toString().trim());
                        HVACLayout.this.fTSubID = Integer.parseInt(HVACLayout.this.et_4tsubid.getText().toString().trim());
                        HVACLayout.this.zoneBeastDevID = Integer.parseInt(HVACLayout.this.et_zbdevid.getText().toString().trim());
                        HVACLayout.this.sensorDevID = Integer.parseInt(HVACLayout.this.et_sensordevid.getText().toString().trim());
                        HVACLayout.this.fTDevID = Integer.parseInt(HVACLayout.this.et_4tdevid.getText().toString().trim());
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.CurTemperType, Integer.valueOf(HVACLayout.this.curTemperType));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.ZoneBeastSubID, Integer.valueOf(HVACLayout.this.zoneBeastSubID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.SensorSubID, Integer.valueOf(HVACLayout.this.sensorSubID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.FTSubID, Integer.valueOf(HVACLayout.this.fTSubID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.ZoneBeastDevID, Integer.valueOf(HVACLayout.this.zoneBeastDevID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.SensorDevID, Integer.valueOf(HVACLayout.this.sensorDevID));
                        SharedPreferencesHelper.getInstance().saveData(HVACLayout.this.FTDevID, Integer.valueOf(HVACLayout.this.fTDevID));
                    } catch (Exception e) {
                        e.printStackTrace();
                        z = false;
                    }
                    if (z) {
                        Toast.makeText(HVACLayout.this.getContext(), "success", 0).show();
                        HVACLayout.this.curTemperType = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.CurTemperType, 0)).intValue();
                        HVACLayout.this.zoneBeastSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.ZoneBeastSubID, 0)).intValue();
                        HVACLayout.this.zoneBeastDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.ZoneBeastDevID, 0)).intValue();
                        HVACLayout.this.sensorSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.SensorSubID, 0)).intValue();
                        HVACLayout.this.sensorDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.SensorDevID, 0)).intValue();
                        HVACLayout.this.fTSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.FTSubID, 0)).intValue();
                        HVACLayout.this.fTDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(HVACLayout.this.FTDevID, 0)).intValue();
                        HVACLayout.this.readCurTemperByType(HVACLayout.this.curTemperType);
                        return;
                    }
                    Toast.makeText(HVACLayout.this.getContext(), "error", 0).show();
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        View viewInflate = View.inflate(context, R.layout.hvac_layout, this);
        this.on = (Button) viewInflate.findViewById(R.id.on);
        this.off = (Button) viewInflate.findViewById(R.id.off);
        this.autofan = (Button) viewInflate.findViewById(R.id.autofan);
        this.highfan = (Button) viewInflate.findViewById(R.id.highfan);
        this.mediumfan = (Button) viewInflate.findViewById(R.id.mediumfan);
        this.lowfan = (Button) viewInflate.findViewById(R.id.lowfan);
        this.automode = (Button) viewInflate.findViewById(R.id.automode);
        this.coolmode = (Button) viewInflate.findViewById(R.id.cool);
        this.fanmode = (Button) viewInflate.findViewById(R.id.fan);
        this.heatmode = (Button) viewInflate.findViewById(R.id.heat);
        this.tempup = (Button) viewInflate.findViewById(R.id.up);
        this.tempdowm = (Button) viewInflate.findViewById(R.id.down);
        this.tempstate = (TextView) viewInflate.findViewById(R.id.temp);
        this.fanstate = (TextView) viewInflate.findViewById(R.id.fanstate);
        this.modestate = (TextView) viewInflate.findViewById(R.id.mode);
        this.uint = (TextView) viewInflate.findViewById(R.id.uint);
        this.name = (TextView) viewInflate.findViewById(R.id.hvacremark);
        this.fanremark = (TextView) viewInflate.findViewById(R.id.textView66);
        this.moderemark = (TextView) viewInflate.findViewById(R.id.textView68);
        this.tv_curtemper = (TextView) viewInflate.findViewById(R.id.tv_curtemper);
        this.layout_curtemper = (LinearLayout) viewInflate.findViewById(R.id.layout_curtemper);
        this.layout_curtemper.setOnClickListener(this);
        this.on.setOnClickListener(this);
        this.off.setOnClickListener(this);
        this.autofan.setOnClickListener(this);
        this.highfan.setOnClickListener(this);
        this.mediumfan.setOnClickListener(this);
        this.lowfan.setOnClickListener(this);
        this.automode.setOnClickListener(this);
        this.coolmode.setOnClickListener(this);
        this.fanmode.setOnClickListener(this);
        this.heatmode.setOnClickListener(this);
        this.tempup.setOnClickListener(this);
        this.tempdowm.setOnClickListener(this);
        this.acc = new ACcontrol();
        this.niocl = new niocontrol();
    }

    public void removetimer() {
        this.reflashuihandler.removeCallbacks(this.reflashrun);
    }

    public void ReflashUI() {
        this.reflashuihandler.postDelayed(this.reflashrun, 0L);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.autofan /* 2131230826 */:
                this.CurrentFanMode = (byte) 0;
                SendCommandOfAC(5, 0);
                setFanstate("AUTO");
                setFANStyle(this.autofan);
                break;
            case R.id.automode /* 2131230827 */:
                this.CurrentMode = (byte) 3;
                SendCommandOfAC(6, 3);
                setModestate("AUTO");
                setMODEStyle(this.automode);
                break;
            case R.id.cool /* 2131230961 */:
                this.CurrentMode = (byte) 0;
                SendCommandOfAC(6, 0);
                setModestate("COOL");
                setMODEStyle(this.coolmode);
                break;
            case R.id.down /* 2131231054 */:
                byte b = this.CurrentMode;
                if (b != 3) {
                    switch (b) {
                        case 0:
                            this.CoolTemp = (byte) (this.CoolTemp - 1);
                            if (this.CoolTemp >= this.const_cool_temp_max) {
                                this.CoolTemp = this.const_cool_temp_max;
                            } else if (this.CoolTemp < this.const_cool_temp_min) {
                                this.CoolTemp = this.const_cool_temp_min;
                            }
                            SendCommandOfAC(4, this.CoolTemp);
                            setTempstate(String.valueOf((int) this.CoolTemp));
                            break;
                        case 1:
                            this.HeatTemp = (byte) (this.HeatTemp - 1);
                            if (this.HeatTemp >= this.const_heat_temp_max) {
                                this.HeatTemp = this.const_heat_temp_max;
                            } else if (this.HeatTemp < this.const_heat_temp_min) {
                                this.HeatTemp = this.const_heat_temp_min;
                            }
                            SendCommandOfAC(7, this.HeatTemp);
                            setTempstate(String.valueOf((int) this.HeatTemp));
                            break;
                    }
                } else {
                    this.AutoTemp = (byte) (this.AutoTemp - 1);
                    if (this.AutoTemp >= this.const_auto_temp_max) {
                        this.AutoTemp = this.const_auto_temp_max;
                    } else if (this.AutoTemp < this.const_auto_temp_min) {
                        this.AutoTemp = this.const_auto_temp_min;
                    }
                    SendCommandOfAC(8, this.AutoTemp);
                    setTempstate(String.valueOf((int) this.AutoTemp));
                    break;
                }
            case R.id.fan /* 2131231113 */:
                this.CurrentMode = (byte) 2;
                SendCommandOfAC(6, 2);
                setModestate("FAN");
                setMODEStyle(this.fanmode);
                break;
            case R.id.heat /* 2131231165 */:
                this.CurrentMode = (byte) 1;
                SendCommandOfAC(6, 1);
                setModestate("HEAT");
                setMODEStyle(this.heatmode);
                break;
            case R.id.highfan /* 2131231167 */:
                this.CurrentFanMode = (byte) 1;
                SendCommandOfAC(5, 1);
                setFanstate("HIGH");
                setFANStyle(this.highfan);
                break;
            case R.id.layout_curtemper /* 2131231253 */:
                showCurTemperSettingView();
                break;
            case R.id.lowfan /* 2131231335 */:
                this.CurrentFanMode = (byte) 3;
                SendCommandOfAC(5, 3);
                setFanstate("LOW");
                setFANStyle(this.lowfan);
                break;
            case R.id.mediumfan /* 2131231415 */:
                this.CurrentFanMode = (byte) 2;
                SendCommandOfAC(5, 2);
                setFanstate("MEDIUM");
                setFANStyle(this.mediumfan);
                break;
            case R.id.off /* 2131231537 */:
                setvisable(8);
                this.powerstate = false;
                try {
                    new Thread(new Runnable() { // from class: com.shima.smartbushome.selflayout.HVACLayout.3
                        @Override // java.lang.Runnable
                        public void run() {
                            HVACLayout.this.niocl.IRControl((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, 2, MainActivity.mydupsocket);
                            HVACLayout.this.SendCommandOfAC(3, 0);
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setOFFStyle();
                break;
            case R.id.on /* 2131231540 */:
                setvisable(0);
                this.powerstate = true;
                try {
                    new Thread(new Runnable() { // from class: com.shima.smartbushome.selflayout.HVACLayout.2
                        @Override // java.lang.Runnable
                        public void run() {
                            HVACLayout.this.niocl.IRControl((byte) HVACLayout.this.thishvac.subnetID, (byte) HVACLayout.this.thishvac.deviceID, 1, MainActivity.mydupsocket);
                            HVACLayout.this.SendCommandOfAC(3, 1);
                        }
                    }).start();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                setONStyle();
                break;
            case R.id.up /* 2131232196 */:
                byte b2 = this.CurrentMode;
                if (b2 != 3) {
                    switch (b2) {
                        case 0:
                            this.CoolTemp = (byte) (this.CoolTemp + 1);
                            if (this.CoolTemp >= this.const_cool_temp_max) {
                                this.CoolTemp = this.const_cool_temp_max;
                            } else if (this.CoolTemp < this.const_cool_temp_min) {
                                this.CoolTemp = this.const_cool_temp_min;
                            }
                            SendCommandOfAC(4, this.CoolTemp);
                            setTempstate(String.valueOf((int) this.CoolTemp));
                            break;
                        case 1:
                            this.HeatTemp = (byte) (this.HeatTemp + 1);
                            if (this.HeatTemp >= this.const_heat_temp_max) {
                                this.HeatTemp = this.const_heat_temp_max;
                            } else if (this.HeatTemp < this.const_heat_temp_min) {
                                this.HeatTemp = this.const_heat_temp_min;
                            }
                            SendCommandOfAC(7, this.HeatTemp);
                            setTempstate(String.valueOf((int) this.HeatTemp));
                            break;
                    }
                } else {
                    this.AutoTemp = (byte) (this.AutoTemp + 1);
                    if (this.AutoTemp >= this.const_auto_temp_max) {
                        this.AutoTemp = this.const_auto_temp_max;
                    } else if (this.AutoTemp < this.const_auto_temp_min) {
                        this.AutoTemp = this.const_auto_temp_min;
                    }
                    SendCommandOfAC(8, this.AutoTemp);
                    setTempstate(String.valueOf((int) this.AutoTemp));
                    break;
                }
                break;
        }
    }

    public void setvisable(int i) {
        this.autofan.setVisibility(i);
        this.highfan.setVisibility(i);
        this.mediumfan.setVisibility(i);
        this.lowfan.setVisibility(i);
        this.automode.setVisibility(i);
        this.coolmode.setVisibility(i);
        this.fanmode.setVisibility(i);
        this.tempup.setVisibility(i);
        this.tempdowm.setVisibility(i);
        this.heatmode.setVisibility(i);
        this.tempstate.setVisibility(i);
        this.fanstate.setVisibility(i);
        this.modestate.setVisibility(i);
        this.fanremark.setVisibility(i);
        this.moderemark.setVisibility(i);
    }

    public void setTempstate(String str) {
        if (this.CurrentUnit == 1) {
            this.tempstate.setText(str + "°F");
            return;
        }
        this.tempstate.setText(str + "°C");
    }

    public void setFanstate(String str) {
        this.fanstate.setText(str);
    }

    public void setModestate(String str) {
        this.modestate.setText(str);
    }

    public void setcontant(Savehvac savehvac) {
        this.thishvac = savehvac;
        this.name.setText("AC:  " + this.thishvac.hvac_remark);
        initCurTemperData();
        readCurTemperByType(this.curTemperType);
        ReflashUI();
    }

    public int getsubnetid() {
        return this.thishvac.subnetID;
    }

    public int getdeviceid() {
        return this.thishvac.deviceID;
    }

    public void setReceiveChange(int i, byte b) {
        switch (i) {
            case 0:
                setvisable(0);
                setONStyle();
                this.powerstate = true;
                break;
            case 1:
                setvisable(8);
                this.powerstate = false;
                setOFFStyle();
                break;
            case 2:
                if (this.powerstate) {
                    setFanstate("AUTO");
                    setFANStyle(this.autofan);
                    break;
                } else {
                    setvisable(0);
                    setFanstate("AUTO");
                    setFANStyle(this.autofan);
                    break;
                }
            case 3:
                if (this.powerstate) {
                    setFanstate("HIGH");
                    setFANStyle(this.highfan);
                    break;
                } else {
                    setvisable(0);
                    setFanstate("HIGH");
                    setFANStyle(this.highfan);
                    break;
                }
            case 4:
                if (this.powerstate) {
                    setFanstate("MEDIUM");
                    setFANStyle(this.mediumfan);
                    break;
                } else {
                    setvisable(0);
                    setFANStyle(this.mediumfan);
                    break;
                }
            case 5:
                if (this.powerstate) {
                    setFanstate("LOW");
                    setFANStyle(this.lowfan);
                    break;
                } else {
                    setvisable(0);
                    setFanstate("LOW");
                    setFANStyle(this.lowfan);
                    break;
                }
            case 6:
                if (this.powerstate) {
                    this.CurrentMode = (byte) 3;
                    setTempstate(String.valueOf(this.AutoTemp & 255));
                    setModestate("AUTO");
                    setMODEStyle(this.automode);
                    break;
                } else {
                    this.CurrentMode = (byte) 3;
                    setvisable(0);
                    setTempstate(String.valueOf(this.AutoTemp & 255));
                    setModestate("AUTO");
                    setMODEStyle(this.automode);
                    break;
                }
            case 7:
                if (this.powerstate) {
                    this.CurrentMode = (byte) 0;
                    setTempstate(String.valueOf(this.CoolTemp & 255));
                    setModestate("COOL");
                    setMODEStyle(this.coolmode);
                    break;
                } else {
                    this.CurrentMode = (byte) 0;
                    setvisable(0);
                    setTempstate(String.valueOf(this.CoolTemp & 255));
                    setModestate("COOL");
                    setMODEStyle(this.coolmode);
                    break;
                }
            case 8:
                if (this.powerstate) {
                    this.CurrentMode = (byte) 1;
                    setTempstate(String.valueOf(this.HeatTemp & 255));
                    setModestate("HEAT");
                    setMODEStyle(this.heatmode);
                    break;
                } else {
                    this.CurrentMode = (byte) 1;
                    setvisable(0);
                    setTempstate(String.valueOf(this.HeatTemp & 255));
                    setModestate("HEAT");
                    setMODEStyle(this.heatmode);
                    break;
                }
            case 9:
                if (this.powerstate) {
                    this.CurrentMode = (byte) 2;
                    setModestate("FAN");
                    setMODEStyle(this.fanmode);
                    break;
                } else {
                    this.CurrentMode = (byte) 2;
                    setvisable(0);
                    setModestate("FAN");
                    setMODEStyle(this.fanmode);
                    break;
                }
            case 10:
                if (this.powerstate) {
                    byte b2 = this.CurrentMode;
                    if (b2 != 3) {
                        switch (b2) {
                            case 0:
                                this.CoolTemp = b;
                                setTempstate(String.valueOf(this.CoolTemp & 255));
                                break;
                            case 1:
                                this.HeatTemp = b;
                                setTempstate(String.valueOf(this.HeatTemp & 255));
                                break;
                        }
                    } else {
                        this.AutoTemp = b;
                        setTempstate(String.valueOf(this.AutoTemp & 255));
                        break;
                    }
                } else {
                    setvisable(0);
                    byte b3 = this.CurrentMode;
                    if (b3 != 3) {
                        switch (b3) {
                            case 0:
                                this.CoolTemp = b;
                                setTempstate(String.valueOf(this.CoolTemp & 255));
                                break;
                            case 1:
                                this.HeatTemp = b;
                                setTempstate(String.valueOf(this.HeatTemp & 255));
                                break;
                        }
                    } else {
                        this.AutoTemp = b;
                        setTempstate(String.valueOf(this.AutoTemp & 255));
                        break;
                    }
                }
                break;
        }
    }

    public void setCandF(byte b) {
        if (this.readCF) {
            return;
        }
        this.readCF = true;
        if (b == 0) {
            this.uint.setText("℃");
            this.CurrentUnit = (byte) 0;
        } else if ((b & 255) == 1) {
            this.uint.setText("℉");
            this.CurrentUnit = (byte) 1;
        }
        this.reflashstate = 1;
    }

    public void setACTempRange(byte[] bArr) {
        if (this.readTempRange) {
            return;
        }
        this.readTempRange = true;
        this.const_cool_temp_min = bArr[0];
        this.const_cool_temp_max = bArr[1];
        this.const_heat_temp_min = bArr[2];
        this.const_heat_temp_max = bArr[3];
        this.const_auto_temp_min = bArr[4];
        this.const_auto_temp_max = bArr[5];
        this.reflashstate = 2;
    }

    public void setFanSpeedAndModeCount(byte[] bArr) {
        if (this.readCountfanAndMode) {
            return;
        }
        this.readCountfanAndMode = true;
        this.fanArray = new byte[bArr[25]];
        for (int i = 0; i < this.fanArray.length; i++) {
            this.fanArray[i] = bArr[i + 26];
        }
        int length = this.fanArray.length + 26;
        for (int i2 = 0; i2 < 10; i2++) {
            if (bArr[length] <= 32 && bArr[length] != 0) {
                if (bArr[length] < 6) {
                    break;
                }
            } else {
                length++;
            }
        }
        this.modeArray = new byte[bArr[length]];
        for (int i3 = 0; i3 < this.modeArray.length; i3++) {
            this.modeArray[i3] = bArr[length + 1 + i3];
        }
        this.reflashstate = 3;
    }

    public void setACCurrentState(byte[] bArr) {
        if (this.readcstate) {
            return;
        }
        this.readcstate = true;
        if (bArr[25] == 0) {
            setvisable(8);
            this.powerstate = false;
            setOFFStyle();
        } else {
            setvisable(0);
            this.powerstate = true;
            setONStyle();
        }
        this.CurrentTem = bArr[29];
        this.CoolTemp = bArr[26];
        this.HeatTemp = bArr[30];
        this.AutoTemp = bArr[32];
        if ((bArr[27] & 15) == this.fanArray.length) {
            this.CurrentFanMode = this.fanArray[(bArr[27] & 15) - 1];
        } else {
            this.CurrentFanMode = this.fanArray[bArr[27] & 15];
        }
        int i = this.curTemperType;
        switch (this.CurrentFanMode) {
            case 0:
                setFanstate("AUTO");
                setFANStyle(this.autofan);
                break;
            case 1:
                setFanstate("HIGH");
                setFANStyle(this.highfan);
                break;
            case 2:
                setFanstate("MEDIUM");
                setFANStyle(this.mediumfan);
                break;
            case 3:
                setFanstate("LOW");
                setFANStyle(this.lowfan);
                break;
        }
        if ((bArr[27] >> 4) == this.modeArray.length) {
            this.CurrentMode = this.modeArray[(bArr[27] >> 4) - 1];
        } else {
            this.CurrentMode = this.modeArray[bArr[27] >> 4];
        }
        switch (this.CurrentMode) {
            case 0:
                setTempstate(String.valueOf(this.CoolTemp & 255));
                setModestate("COOL");
                setMODEStyle(this.coolmode);
                break;
            case 1:
                setTempstate(String.valueOf(this.HeatTemp & 255));
                setModestate("HEAT");
                setMODEStyle(this.heatmode);
                break;
            case 2:
                setModestate("FAN");
                setMODEStyle(this.fanmode);
                break;
            case 3:
                setTempstate(String.valueOf(this.AutoTemp & 255));
                setModestate("AUTO");
                setMODEStyle(this.automode);
                break;
        }
        this.reflashstate = 4;
    }

    public void setACCurrentStateBy193B(byte[] bArr) {
        if (this.readcstate) {
            return;
        }
        this.readcstate = true;
        if (bArr[33] == 0) {
            setvisable(8);
            this.powerstate = false;
            setOFFStyle();
        } else {
            setvisable(0);
            this.powerstate = true;
            setONStyle();
        }
        this.CurrentTem = bArr[27];
        this.CoolTemp = bArr[28];
        this.HeatTemp = bArr[29];
        this.AutoTemp = bArr[30];
        setCandF(bArr[26]);
        switch (bArr[35] & 255) {
            case 0:
                this.CurrentFanMode = (byte) 0;
                setFanstate("AUTO");
                setFANStyle(this.autofan);
                break;
            case 1:
                this.CurrentFanMode = (byte) 1;
                setFanstate("HIGH");
                setFANStyle(this.highfan);
                break;
            case 2:
                this.CurrentFanMode = (byte) 2;
                setFanstate("MEDIUM");
                setFANStyle(this.mediumfan);
                break;
            case 3:
                this.CurrentFanMode = (byte) 3;
                setFanstate("LOW");
                setFANStyle(this.lowfan);
                break;
        }
        switch (bArr[34] & 255) {
            case 0:
                this.CurrentMode = (byte) 0;
                setTempstate(String.valueOf(this.CoolTemp & 255));
                setModestate("COOL");
                setMODEStyle(this.coolmode);
                break;
            case 1:
                this.CurrentMode = (byte) 1;
                setTempstate(String.valueOf(this.HeatTemp & 255));
                setModestate("HEAT");
                setMODEStyle(this.heatmode);
                break;
            case 2:
                this.CurrentMode = (byte) 2;
                setModestate("FAN");
                setMODEStyle(this.fanmode);
                break;
            case 3:
                this.CurrentMode = (byte) 3;
                setTempstate(String.valueOf(this.AutoTemp & 255));
                setModestate("AUTO");
                setMODEStyle(this.automode);
                break;
        }
        this.reflashstate = 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean SendCommandOfAC(int i, int i2) {
        try {
            return this.acc.ACControl((byte) this.thishvac.subnetID, (byte) this.thishvac.deviceID, i, i2, MainActivity.mydupsocket);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setONStyle() {
        this.on.setBackground(getResources().getDrawable(R.drawable.control_blue_10));
        this.off.setBackground(getResources().getDrawable(R.drawable.control_back_10));
    }

    public void setOFFStyle() {
        this.on.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.off.setBackground(getResources().getDrawable(R.drawable.control_blue_10));
    }

    public void setFANStyle(View view) {
        this.autofan.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.highfan.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.mediumfan.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.lowfan.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        view.setBackground(getResources().getDrawable(R.drawable.control_blue_10));
    }

    public void setMODEStyle(View view) {
        this.automode.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.coolmode.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.fanmode.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        this.heatmode.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        view.setBackground(getResources().getDrawable(R.drawable.control_blue_10));
    }

    public void initCurTemperData() {
        this.CurTemperType = "CurTemperType" + this.thishvac._id;
        this.ZoneBeastSubID = "ZoneBeastSubID" + this.thishvac._id;
        this.ZoneBeastDevID = "ZoneBeastDevID" + this.thishvac._id;
        this.SensorSubID = "SensorSubID" + this.thishvac._id;
        this.SensorDevID = "SensorDevID" + this.thishvac._id;
        this.FTSubID = "FTSubID" + this.thishvac._id;
        this.FTDevID = "FTDevID" + this.thishvac._id;
        SharedPreferencesHelper.init(getContext());
        this.curTemperType = ((Integer) SharedPreferencesHelper.getInstance().getData(this.CurTemperType, 0)).intValue();
        this.zoneBeastSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.ZoneBeastSubID, 0)).intValue();
        this.zoneBeastDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.ZoneBeastDevID, 0)).intValue();
        this.sensorSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.SensorSubID, 0)).intValue();
        this.sensorDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.SensorDevID, 0)).intValue();
        this.fTSubID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.FTSubID, 0)).intValue();
        this.fTDevID = ((Integer) SharedPreferencesHelper.getInstance().getData(this.FTDevID, 0)).intValue();
    }

    private void showCurTemperSettingView() {
        AlertView alertView = new AlertView("Read temperature settings", null, "CANCEL", new String[]{"SAVE"}, null, getContext(), AlertView.Style.Alert, this.saveClick);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.setting_hvaccurtemper, (ViewGroup) null);
        this.rg_group = (RadioGroup) viewGroup.findViewById(R.id.rg_group);
        this.rb_hvac = (RadioButton) viewGroup.findViewById(R.id.rb_hvac);
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
        this.et_hvacsubid.setText("" + this.thishvac.subnetID);
        this.et_zbsubid.setText("" + SharedPreferencesHelper.getInstance().getData(this.ZoneBeastSubID, 0));
        this.et_sensorsubid.setText("" + SharedPreferencesHelper.getInstance().getData(this.SensorSubID, 0));
        this.et_4tsubid.setText("" + SharedPreferencesHelper.getInstance().getData(this.FTSubID, 0));
        this.et_hvacdevid.setText("" + this.thishvac.deviceID);
        this.et_zbdevid.setText("" + SharedPreferencesHelper.getInstance().getData(this.ZoneBeastDevID, 0));
        this.et_sensordevid.setText("" + SharedPreferencesHelper.getInstance().getData(this.SensorDevID, 0));
        this.et_4tdevid.setText("" + SharedPreferencesHelper.getInstance().getData(this.FTDevID, 0));
        this.rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.HVACLayout.4
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_4t) {
                    HVACLayout.this.curTemperType = 3;
                    return;
                }
                if (i == R.id.rb_hvac) {
                    HVACLayout.this.curTemperType = 0;
                } else if (i == R.id.rb_sensor) {
                    HVACLayout.this.curTemperType = 2;
                } else {
                    if (i != R.id.rb_zb) {
                        return;
                    }
                    HVACLayout.this.curTemperType = 1;
                }
            }
        });
        alertView.addExtView(viewGroup);
        alertView.show();
    }

    public void readCurTemperByType(int i) {
        switch (i) {
            case 0:
                this.acc.ACReadTemperatureValue((byte) this.thishvac.subnetID, (byte) this.thishvac.deviceID, MainActivity.mydupsocket);
                break;
            case 1:
                this.acc.ACReadTemperatureValue((byte) this.zoneBeastSubID, (byte) this.zoneBeastDevID, MainActivity.mydupsocket);
                break;
            case 2:
                this.acc.ACReadTemperatureValue((byte) this.sensorSubID, (byte) this.sensorDevID, MainActivity.mydupsocket);
                break;
            case 3:
                this.acc.ACReadTemperatureValue((byte) this.fTSubID, (byte) this.fTDevID, MainActivity.mydupsocket);
                break;
        }
    }

    public void setCurTemperByData(byte[] bArr) {
        int i;
        int i2 = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        int i3 = 0;
        switch (this.curTemperType) {
            case 0:
                i3 = this.thishvac.subnetID;
                i = this.thishvac.deviceID;
                break;
            case 1:
                i3 = this.zoneBeastSubID;
                i = this.zoneBeastDevID;
                break;
            case 2:
                i3 = this.sensorSubID;
                i = this.sensorDevID;
                break;
            case 3:
                i3 = this.fTSubID;
                i = this.fTDevID;
                break;
            default:
                i = 0;
                break;
        }
        if ((bArr[17] & 255) == i3 && (bArr[18] & 255) == i && i2 == 58344) {
            byte b = bArr[25];
            int i4 = bArr[26];
            if (b == 0) {
                double d = i4 - 32;
                Double.isNaN(d);
                i4 = (int) (d / 1.8d);
            }
            this.tv_curtemper.setText(i4 + "°C");
        }
    }
}
