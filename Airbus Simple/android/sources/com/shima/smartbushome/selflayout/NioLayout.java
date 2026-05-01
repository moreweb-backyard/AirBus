package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.CircleView;
import com.shima.smartbushome.database.Savenio;
import com.shima.smartbushome.founction_command.niocontrol;
import java.util.List;

/* loaded from: classes.dex */
public class NioLayout extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;
    Button bt5;
    Button bt6;
    Button bt7;
    Button bt8;
    Button btnum;
    Button btoff;
    Button bton;
    EditText buttonnum;
    EditText buttonremark;
    public CircleView.OnPressListener centerpress;
    int ddpdev;
    int ddpsub;
    int fourTdev;
    int fourTloop;
    int fourTsub;
    Runnable getdatarun;
    Handler getdevicevalue;
    boolean getluxfinish;
    boolean gettempsettingfinish;
    boolean getvalue1finish;
    boolean getvalue2finish;
    boolean getvalue3finish;
    LayoutInflater inflater;
    private ImageView iv_center;
    private ImageView iv_down;
    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView iv_up;
    niocontrol nc;
    CircleView niocenterview;
    AlertView numpadalter;
    TextView remark;
    Context rootcontext;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    int step;
    int[] tempable;
    int tempi;
    Savenio thisdevice;
    int timecount;
    int updatebuttonnum;
    TextView value_4t;
    TextView value_ddp;
    TextView value_exttemp;
    TextView value_lux;
    View view;

    public NioLayout(Context context) {
        super(context);
        this.thisdevice = new Savenio();
        this.nc = new niocontrol();
        this.getdevicevalue = new Handler();
        this.centerpress = new CircleView.OnPressListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.1
            /* JADX WARN: Removed duplicated region for block: B:26:0x0071  */
            /* JADX WARN: Removed duplicated region for block: B:58:0x00ea  */
            @Override // com.shima.smartbushome.assist.CircleView.OnPressListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onPress(java.lang.String r19, java.lang.String r20) {
                /*
                    Method dump skipped, instructions count: 298
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.NioLayout.AnonymousClass1.onPress(java.lang.String, java.lang.String):void");
            }
        };
        this.updatebuttonnum = 0;
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.26
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == NioLayout.this.settingalter && i == 0) {
                    if (NioLayout.this.updatebuttonnum > 15) {
                        MainActivity.mgr.updateniovalue(NioLayout.this.thisdevice.room_id, NioLayout.this.thisdevice.nio_id, "", Integer.parseInt(NioLayout.this.buttonnum.getText().toString().trim()), NioLayout.this.updatebuttonnum);
                    } else {
                        MainActivity.mgr.updateniovalue(NioLayout.this.thisdevice.room_id, NioLayout.this.thisdevice.nio_id, NioLayout.this.buttonremark.getText().toString().trim(), Integer.parseInt(NioLayout.this.buttonnum.getText().toString().trim()), NioLayout.this.updatebuttonnum);
                    }
                    NioLayout.this.renewdata();
                }
            }
        };
        this.getluxfinish = false;
        this.gettempsettingfinish = false;
        this.getvalue1finish = false;
        this.getvalue2finish = false;
        this.getvalue3finish = false;
        this.step = 0;
        this.timecount = 0;
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.NioLayout.27
            @Override // java.lang.Runnable
            public void run() {
                switch (NioLayout.this.step) {
                    case 0:
                        NioLayout.this.nc.Readlux((byte) NioLayout.this.thisdevice.subnetID, (byte) NioLayout.this.thisdevice.deviceID, MainActivity.mydupsocket);
                        break;
                    case 1:
                        NioLayout.this.nc.ReadTempSetting((byte) NioLayout.this.thisdevice.subnetID, (byte) NioLayout.this.thisdevice.deviceID, MainActivity.mydupsocket);
                        break;
                    case 2:
                        if (NioLayout.this.tempi < 3) {
                            if (NioLayout.this.tempable[NioLayout.this.tempi] == 0) {
                                switch (NioLayout.this.tempi) {
                                    case 0:
                                        NioLayout.this.nc.ReadTemp((byte) NioLayout.this.thisdevice.subnetID, (byte) NioLayout.this.thisdevice.deviceID, MainActivity.mydupsocket);
                                        break;
                                    case 1:
                                        NioLayout.this.nc.ReadTemp((byte) NioLayout.this.ddpsub, (byte) NioLayout.this.ddpdev, MainActivity.mydupsocket);
                                        break;
                                    case 2:
                                        NioLayout.this.nc.ReadTemp((byte) NioLayout.this.fourTsub, (byte) NioLayout.this.fourTdev, MainActivity.mydupsocket);
                                        break;
                                }
                            } else {
                                NioLayout.this.tempi++;
                                break;
                            }
                        } else {
                            NioLayout.this.step = 3;
                            break;
                        }
                        break;
                    case 3:
                        NioLayout.this.stoptimer();
                        break;
                }
                if (NioLayout.this.step != 3) {
                    NioLayout.this.getdevicevalue.postDelayed(NioLayout.this.getdatarun, 500L);
                }
                NioLayout.this.timecount++;
                if (NioLayout.this.timecount <= 30 || NioLayout.this.step == 3) {
                    return;
                }
                NioLayout.this.stoptimer();
            }
        };
        this.ddpsub = 0;
        this.ddpdev = 0;
        this.fourTsub = 0;
        this.fourTdev = 0;
        this.fourTloop = 0;
        this.tempable = new int[]{0, 0, 0};
        this.tempi = 0;
        initview(context);
    }

    public NioLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.thisdevice = new Savenio();
        this.nc = new niocontrol();
        this.getdevicevalue = new Handler();
        this.centerpress = new CircleView.OnPressListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.1
            @Override // com.shima.smartbushome.assist.CircleView.OnPressListener
            public void onPress(String v, String v2) {
                /*
                    Method dump skipped, instructions count: 298
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.NioLayout.AnonymousClass1.onPress(java.lang.String, java.lang.String):void");
            }
        };
        this.updatebuttonnum = 0;
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.26
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == NioLayout.this.settingalter && i == 0) {
                    if (NioLayout.this.updatebuttonnum > 15) {
                        MainActivity.mgr.updateniovalue(NioLayout.this.thisdevice.room_id, NioLayout.this.thisdevice.nio_id, "", Integer.parseInt(NioLayout.this.buttonnum.getText().toString().trim()), NioLayout.this.updatebuttonnum);
                    } else {
                        MainActivity.mgr.updateniovalue(NioLayout.this.thisdevice.room_id, NioLayout.this.thisdevice.nio_id, NioLayout.this.buttonremark.getText().toString().trim(), Integer.parseInt(NioLayout.this.buttonnum.getText().toString().trim()), NioLayout.this.updatebuttonnum);
                    }
                    NioLayout.this.renewdata();
                }
            }
        };
        this.getluxfinish = false;
        this.gettempsettingfinish = false;
        this.getvalue1finish = false;
        this.getvalue2finish = false;
        this.getvalue3finish = false;
        this.step = 0;
        this.timecount = 0;
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.NioLayout.27
            @Override // java.lang.Runnable
            public void run() {
                switch (NioLayout.this.step) {
                    case 0:
                        NioLayout.this.nc.Readlux((byte) NioLayout.this.thisdevice.subnetID, (byte) NioLayout.this.thisdevice.deviceID, MainActivity.mydupsocket);
                        break;
                    case 1:
                        NioLayout.this.nc.ReadTempSetting((byte) NioLayout.this.thisdevice.subnetID, (byte) NioLayout.this.thisdevice.deviceID, MainActivity.mydupsocket);
                        break;
                    case 2:
                        if (NioLayout.this.tempi < 3) {
                            if (NioLayout.this.tempable[NioLayout.this.tempi] == 0) {
                                switch (NioLayout.this.tempi) {
                                    case 0:
                                        NioLayout.this.nc.ReadTemp((byte) NioLayout.this.thisdevice.subnetID, (byte) NioLayout.this.thisdevice.deviceID, MainActivity.mydupsocket);
                                        break;
                                    case 1:
                                        NioLayout.this.nc.ReadTemp((byte) NioLayout.this.ddpsub, (byte) NioLayout.this.ddpdev, MainActivity.mydupsocket);
                                        break;
                                    case 2:
                                        NioLayout.this.nc.ReadTemp((byte) NioLayout.this.fourTsub, (byte) NioLayout.this.fourTdev, MainActivity.mydupsocket);
                                        break;
                                }
                            } else {
                                NioLayout.this.tempi++;
                                break;
                            }
                        } else {
                            NioLayout.this.step = 3;
                            break;
                        }
                        break;
                    case 3:
                        NioLayout.this.stoptimer();
                        break;
                }
                if (NioLayout.this.step != 3) {
                    NioLayout.this.getdevicevalue.postDelayed(NioLayout.this.getdatarun, 500L);
                }
                NioLayout.this.timecount++;
                if (NioLayout.this.timecount <= 30 || NioLayout.this.step == 3) {
                    return;
                }
                NioLayout.this.stoptimer();
            }
        };
        this.ddpsub = 0;
        this.ddpdev = 0;
        this.fourTsub = 0;
        this.fourTdev = 0;
        this.fourTloop = 0;
        this.tempable = new int[]{0, 0, 0};
        this.tempi = 0;
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.niolayout, this);
        this.rootcontext = context;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.remark = (TextView) this.view.findViewById(R.id.nioremark);
        this.value_lux = (TextView) this.view.findViewById(R.id.niolayout_luxvalue);
        this.value_exttemp = (TextView) this.view.findViewById(R.id.niolayout_etsvalue);
        this.value_ddp = (TextView) this.view.findViewById(R.id.niolayout_ddpvalue);
        this.value_4t = (TextView) this.view.findViewById(R.id.niolayout_fv);
        this.bt1 = (Button) this.view.findViewById(R.id.niolayout_bt1);
        this.bt2 = (Button) this.view.findViewById(R.id.niolayout_bt2);
        this.bt3 = (Button) this.view.findViewById(R.id.niolayout_bt3);
        this.bt4 = (Button) this.view.findViewById(R.id.niolayout_bt4);
        this.bt5 = (Button) this.view.findViewById(R.id.niolayout_bt5);
        this.bt6 = (Button) this.view.findViewById(R.id.niolayout_bt6);
        this.bt7 = (Button) this.view.findViewById(R.id.niolayout_bt7);
        this.bt8 = (Button) this.view.findViewById(R.id.niolayout_bt8);
        this.niocenterview = (CircleView) this.view.findViewById(R.id.niocenterview);
        this.niocenterview.setOnPressListener(this.centerpress);
        this.bton = (Button) this.view.findViewById(R.id.niolayout_bton);
        this.btoff = (Button) this.view.findViewById(R.id.niolayout_btoff);
        this.btnum = (Button) this.view.findViewById(R.id.niolayout_btpad);
        this.bt1.setOnClickListener(this);
        this.bt2.setOnClickListener(this);
        this.bt3.setOnClickListener(this);
        this.bt4.setOnClickListener(this);
        this.bt5.setOnClickListener(this);
        this.bt6.setOnClickListener(this);
        this.bt7.setOnClickListener(this);
        this.bt8.setOnClickListener(this);
        this.bton.setOnClickListener(this);
        this.btoff.setOnClickListener(this);
        this.btnum.setOnClickListener(this);
        this.bt1.setOnLongClickListener(this);
        this.bt2.setOnLongClickListener(this);
        this.bt3.setOnLongClickListener(this);
        this.bt4.setOnLongClickListener(this);
        this.bt5.setOnLongClickListener(this);
        this.bt6.setOnLongClickListener(this);
        this.bt7.setOnLongClickListener(this);
        this.bt8.setOnLongClickListener(this);
        this.bton.setOnLongClickListener(this);
        this.btoff.setOnLongClickListener(this);
        this.btnum.setOnLongClickListener(this);
        initKeyView(this.view);
    }

    public void initKeyView(View view) {
        this.iv_center = (ImageView) view.findViewById(R.id.iv_center);
        this.iv_up = (ImageView) view.findViewById(R.id.iv_up);
        this.iv_down = (ImageView) view.findViewById(R.id.iv_down);
        this.iv_left = (ImageView) view.findViewById(R.id.iv_left);
        this.iv_right = (ImageView) view.findViewById(R.id.iv_right);
        this.iv_center.setOnClickListener(new keyViewClickListener());
        this.iv_up.setOnClickListener(new keyViewClickListener());
        this.iv_down.setOnClickListener(new keyViewClickListener());
        this.iv_left.setOnClickListener(new keyViewClickListener());
        this.iv_right.setOnClickListener(new keyViewClickListener());
        this.iv_center.setOnLongClickListener(new keyViewLongClickListener());
        this.iv_up.setOnLongClickListener(new keyViewLongClickListener());
        this.iv_down.setOnLongClickListener(new keyViewLongClickListener());
        this.iv_left.setOnLongClickListener(new keyViewLongClickListener());
        this.iv_right.setOnLongClickListener(new keyViewLongClickListener());
    }

    class keyViewClickListener implements View.OnClickListener {
        keyViewClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_center /* 2131231223 */:
                    NioLayout.this.sentcommand(13);
                    break;
                case R.id.iv_down /* 2131231224 */:
                    NioLayout.this.sentcommand(10);
                    break;
                case R.id.iv_left /* 2131231233 */:
                    NioLayout.this.sentcommand(11);
                    break;
                case R.id.iv_right /* 2131231237 */:
                    NioLayout.this.sentcommand(12);
                    break;
                case R.id.iv_up /* 2131231243 */:
                    NioLayout.this.sentcommand(9);
                    break;
            }
        }
    }

    class keyViewLongClickListener implements View.OnLongClickListener {
        keyViewLongClickListener() {
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            switch (view.getId()) {
                case R.id.iv_center /* 2131231223 */:
                    NioLayout.this.setbuttoninfo(13);
                    break;
                case R.id.iv_down /* 2131231224 */:
                    NioLayout.this.setbuttoninfo(10);
                    break;
                case R.id.iv_left /* 2131231233 */:
                    NioLayout.this.setbuttoninfo(11);
                    break;
                case R.id.iv_right /* 2131231237 */:
                    NioLayout.this.setbuttoninfo(12);
                    break;
                case R.id.iv_up /* 2131231243 */:
                    NioLayout.this.setbuttoninfo(9);
                    break;
            }
            return true;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.niolayout_bt1 /* 2131231503 */:
                sentcommand(1);
                break;
            case R.id.niolayout_bt2 /* 2131231504 */:
                sentcommand(2);
                break;
            case R.id.niolayout_bt3 /* 2131231505 */:
                sentcommand(3);
                break;
            case R.id.niolayout_bt4 /* 2131231506 */:
                sentcommand(4);
                break;
            case R.id.niolayout_bt5 /* 2131231507 */:
                sentcommand(5);
                break;
            case R.id.niolayout_bt6 /* 2131231508 */:
                sentcommand(6);
                break;
            case R.id.niolayout_bt7 /* 2131231509 */:
                sentcommand(7);
                break;
            case R.id.niolayout_bt8 /* 2131231510 */:
                sentcommand(8);
                break;
            case R.id.niolayout_btoff /* 2131231511 */:
                sentcommand(15);
                break;
            case R.id.niolayout_bton /* 2131231512 */:
                sentcommand(14);
                break;
            case R.id.niolayout_btpad /* 2131231513 */:
                this.numpadalter = new AlertView(null, null, "CANCEL", null, null, this.rootcontext, AlertView.Style.Alert, this.settingclick);
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.rootcontext).inflate(R.layout.media_numpad, (ViewGroup) null);
                Button button = (Button) viewGroup.findViewById(R.id.medianumpad_1);
                Button button2 = (Button) viewGroup.findViewById(R.id.medianumpad_2);
                Button button3 = (Button) viewGroup.findViewById(R.id.medianumpad_3);
                Button button4 = (Button) viewGroup.findViewById(R.id.medianumpad_4);
                Button button5 = (Button) viewGroup.findViewById(R.id.medianumpad_5);
                Button button6 = (Button) viewGroup.findViewById(R.id.medianumpad_6);
                Button button7 = (Button) viewGroup.findViewById(R.id.medianumpad_7);
                Button button8 = (Button) viewGroup.findViewById(R.id.medianumpad_8);
                Button button9 = (Button) viewGroup.findViewById(R.id.medianumpad_9);
                Button button10 = (Button) viewGroup.findViewById(R.id.medianumpad_x);
                Button button11 = (Button) viewGroup.findViewById(R.id.medianumpad_0);
                Button button12 = (Button) viewGroup.findViewById(R.id.medianumpad_q);
                button.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(16);
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.3
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(16);
                        return false;
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(17);
                    }
                });
                button2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.5
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(17);
                        return false;
                    }
                });
                button3.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.6
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(18);
                    }
                });
                button3.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.7
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(18);
                        return false;
                    }
                });
                button4.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.8
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(19);
                    }
                });
                button4.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.9
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(19);
                        return false;
                    }
                });
                button5.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(20);
                    }
                });
                button5.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.11
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(20);
                        return false;
                    }
                });
                button6.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.12
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(21);
                    }
                });
                button6.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.13
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(21);
                        return false;
                    }
                });
                button7.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.14
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(22);
                    }
                });
                button7.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.15
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(22);
                        return false;
                    }
                });
                button8.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.16
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(23);
                    }
                });
                button8.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.17
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(23);
                        return false;
                    }
                });
                button9.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.18
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(24);
                    }
                });
                button9.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.19
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(24);
                        return false;
                    }
                });
                button10.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.20
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(25);
                    }
                });
                button10.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.21
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(25);
                        return false;
                    }
                });
                button11.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.22
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(26);
                    }
                });
                button11.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.23
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(26);
                        return false;
                    }
                });
                button12.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.24
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        NioLayout.this.sentcommand(27);
                    }
                });
                button12.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.NioLayout.25
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        NioLayout.this.setbuttoninfo(27);
                        return false;
                    }
                });
                this.numpadalter.addExtView(viewGroup);
                this.numpadalter.setCancelable(true);
                this.numpadalter.show();
                break;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.niolayout_bt1 /* 2131231503 */:
                setbuttoninfo(1);
                return true;
            case R.id.niolayout_bt2 /* 2131231504 */:
                setbuttoninfo(2);
                return true;
            case R.id.niolayout_bt3 /* 2131231505 */:
                setbuttoninfo(3);
                return true;
            case R.id.niolayout_bt4 /* 2131231506 */:
                setbuttoninfo(4);
                return true;
            case R.id.niolayout_bt5 /* 2131231507 */:
                setbuttoninfo(5);
                return true;
            case R.id.niolayout_bt6 /* 2131231508 */:
                setbuttoninfo(6);
                return true;
            case R.id.niolayout_bt7 /* 2131231509 */:
                setbuttoninfo(7);
                return true;
            case R.id.niolayout_bt8 /* 2131231510 */:
                setbuttoninfo(8);
                return true;
            case R.id.niolayout_btoff /* 2131231511 */:
                setbuttoninfo(15);
                return true;
            case R.id.niolayout_bton /* 2131231512 */:
                setbuttoninfo(14);
                return true;
            default:
                return true;
        }
    }

    public void renewdata() {
        List<Savenio> listQuerynio = MainActivity.mgr.querynio();
        int i = 0;
        while (true) {
            if (i < listQuerynio.size()) {
                if (listQuerynio.get(i).room_id == this.thisdevice.room_id && listQuerynio.get(i).nio_id == this.thisdevice.nio_id) {
                    this.thisdevice = listQuerynio.get(i);
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        this.remark.setText(this.thisdevice.nio_remark);
        this.bt1.setText(this.thisdevice.name1);
        this.bt2.setText(this.thisdevice.name2);
        this.bt3.setText(this.thisdevice.name3);
        this.bt4.setText(this.thisdevice.name4);
        this.bt5.setText(this.thisdevice.name5);
        this.bt6.setText(this.thisdevice.name6);
        this.bt7.setText(this.thisdevice.name7);
        this.bt8.setText(this.thisdevice.name8);
        this.bton.setText(this.thisdevice.name14);
        this.btoff.setText(this.thisdevice.name15);
    }

    public void setcontent(Savenio savenio) {
        this.thisdevice = savenio;
        this.remark.setText(this.thisdevice.nio_remark);
        this.bt1.setText(this.thisdevice.name1);
        this.bt2.setText(this.thisdevice.name2);
        this.bt3.setText(this.thisdevice.name3);
        this.bt4.setText(this.thisdevice.name4);
        this.bt5.setText(this.thisdevice.name5);
        this.bt6.setText(this.thisdevice.name6);
        this.bt7.setText(this.thisdevice.name7);
        this.bt8.setText(this.thisdevice.name8);
        this.bton.setText(this.thisdevice.name14);
        this.btoff.setText(this.thisdevice.name15);
        this.getluxfinish = false;
        this.gettempsettingfinish = false;
        this.getvalue1finish = false;
        this.getvalue2finish = false;
        this.getvalue3finish = false;
        this.ddpsub = 0;
        this.ddpdev = 0;
        this.fourTsub = 0;
        this.fourTdev = 0;
        this.fourTloop = 0;
        this.tempable[0] = 0;
        this.tempable[1] = 0;
        this.tempable[2] = 0;
        this.tempi = 0;
        this.step = 0;
        this.timecount = 0;
        this.getdevicevalue.postDelayed(this.getdatarun, 30L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setbuttoninfo(int i) {
        this.updatebuttonnum = i;
        this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, this.rootcontext, AlertView.Style.Alert, this.settingclick);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.rootcontext).inflate(R.layout.setting_niobuttoninfo, (ViewGroup) null);
        this.buttonremark = (EditText) viewGroup.findViewById(R.id.settingniobutton_remark);
        this.buttonnum = (EditText) viewGroup.findViewById(R.id.settingniobutton_num);
        if (i > 15) {
            this.buttonremark.setText("you can't set numpad name");
            this.buttonremark.setEnabled(false);
        } else {
            this.buttonremark.setText(getbuttonremark(i));
        }
        this.buttonnum.setText(String.valueOf(getbuttonvalue(i)));
        this.settingalter.addExtView(viewGroup);
        this.settingalter.show();
    }

    public void sentcommand(int i) {
        this.nc.IRControl((byte) this.thisdevice.subnetID, (byte) this.thisdevice.deviceID, getbuttonvalue(i), MainActivity.mydupsocket);
    }

    public int getbuttonvalue(int i) {
        switch (i) {
            case 1:
                return this.thisdevice.value1;
            case 2:
                return this.thisdevice.value2;
            case 3:
                return this.thisdevice.value3;
            case 4:
                return this.thisdevice.value4;
            case 5:
                return this.thisdevice.value5;
            case 6:
                return this.thisdevice.value6;
            case 7:
                return this.thisdevice.value7;
            case 8:
                return this.thisdevice.value8;
            case 9:
                return this.thisdevice.value9;
            case 10:
                return this.thisdevice.value10;
            case 11:
                return this.thisdevice.value11;
            case 12:
                return this.thisdevice.value12;
            case 13:
                return this.thisdevice.value13;
            case 14:
                return this.thisdevice.value14;
            case 15:
                return this.thisdevice.value15;
            case 16:
                return this.thisdevice.value16;
            case 17:
                return this.thisdevice.value17;
            case 18:
                return this.thisdevice.value18;
            case 19:
                return this.thisdevice.value19;
            case 20:
                return this.thisdevice.value20;
            case 21:
                return this.thisdevice.value21;
            case 22:
                return this.thisdevice.value22;
            case 23:
                return this.thisdevice.value23;
            case 24:
                return this.thisdevice.value24;
            case 25:
                return this.thisdevice.value25;
            case 26:
                return this.thisdevice.value26;
            case 27:
                return this.thisdevice.value27;
            default:
                return 0;
        }
    }

    public String getbuttonremark(int i) {
        switch (i) {
            case 1:
                return this.thisdevice.name1;
            case 2:
                return this.thisdevice.name2;
            case 3:
                return this.thisdevice.name3;
            case 4:
                return this.thisdevice.name4;
            case 5:
                return this.thisdevice.name5;
            case 6:
                return this.thisdevice.name6;
            case 7:
                return this.thisdevice.name7;
            case 8:
                return this.thisdevice.name8;
            case 9:
                return this.thisdevice.name9;
            case 10:
                return this.thisdevice.name10;
            case 11:
                return this.thisdevice.name11;
            case 12:
                return this.thisdevice.name12;
            case 13:
                return this.thisdevice.name13;
            case 14:
                return this.thisdevice.name14;
            case 15:
                return this.thisdevice.name15;
            default:
                return "";
        }
    }

    public int getsubnetid() {
        return this.thisdevice.subnetID;
    }

    public int getdeviceid() {
        return this.thisdevice.deviceID;
    }

    public void stoptimer() {
        this.getdevicevalue.removeCallbacks(this.getdatarun);
        this.getluxfinish = false;
        this.gettempsettingfinish = false;
        this.getvalue1finish = false;
        this.getvalue2finish = false;
        this.getvalue3finish = false;
        this.ddpsub = 0;
        this.ddpdev = 0;
        this.fourTsub = 0;
        this.fourTdev = 0;
        this.fourTloop = 0;
        this.tempable[0] = 0;
        this.tempable[1] = 0;
        this.tempable[2] = 0;
        this.tempi = 0;
        this.step = 0;
        this.timecount = 0;
    }

    public void receivedata(byte[] bArr) {
        String str;
        String str2;
        String str3;
        String str4;
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (i == 397) {
            if ((bArr[17] & 255) == getsubnetid() && (bArr[18] & 255) == getdeviceid() && !this.gettempsettingfinish) {
                if (bArr[26] == 1) {
                    this.tempable[0] = 0;
                } else {
                    this.tempable[0] = 1;
                    this.value_exttemp.setText("Extra Tempature is disable");
                }
                if (bArr[28] == 1) {
                    this.tempable[1] = 0;
                    this.ddpsub = bArr[29] & 255;
                    this.ddpdev = bArr[30] & 255;
                } else {
                    this.tempable[1] = 1;
                    this.value_ddp.setText("DDP Temperature is disable");
                }
                if (bArr[32] == 1) {
                    this.tempable[2] = 0;
                    this.fourTsub = bArr[33] & 255;
                    this.fourTdev = bArr[34] & 255;
                    this.fourTloop = bArr[35] & 255;
                } else {
                    this.tempable[2] = 1;
                    this.value_4t.setText("4T Temperature is disable");
                }
                this.gettempsettingfinish = true;
                this.step = 2;
            }
            return;
        }
        if (i == 55699) {
            if ((bArr[17] & 255) == getsubnetid() && (bArr[18] & 255) == getdeviceid() && !this.getluxfinish) {
                int i2 = ((bArr[25] & 255) << 8) + (bArr[26] & 255);
                this.value_lux.setText(i2 + "");
                this.getluxfinish = true;
                this.step = 1;
                return;
            }
            return;
        }
        if (i != 58344) {
            return;
        }
        switch (this.tempi) {
            case 0:
                if (!this.getvalue1finish && (bArr[17] & 255) == getsubnetid() && (bArr[18] & 255) == getdeviceid()) {
                    if (bArr[34] == 0) {
                        this.value_exttemp.setText((bArr[26] & 255) + "℃");
                    } else {
                        this.value_exttemp.setText("-" + (bArr[26] & 255) + "℃");
                    }
                    this.getvalue1finish = true;
                    this.tempi++;
                    break;
                }
                break;
            case 1:
                if (!this.getvalue2finish && (bArr[17] & 255) == this.ddpsub && (bArr[18] & 255) == this.ddpdev) {
                    if (bArr[34] == 0) {
                        this.value_ddp.setText((bArr[26] & 255) + "℃");
                    } else {
                        this.value_ddp.setText("-" + (bArr[26] & 255) + "℃");
                    }
                    this.getvalue2finish = true;
                    this.tempi++;
                    break;
                }
                break;
            case 2:
                if (!this.getvalue3finish && (bArr[17] & 255) == this.fourTsub && (bArr[18] & 255) == this.fourTdev) {
                    if (bArr[34] == 0) {
                        str = String.valueOf(bArr[26] & 255) + "℃";
                    } else {
                        str = "-" + String.valueOf(bArr[26] & 255) + "℃";
                    }
                    if (bArr[35] == 0) {
                        str2 = String.valueOf(bArr[27] & 255) + "℃";
                    } else {
                        str2 = "-" + String.valueOf(bArr[27] & 255) + "℃";
                    }
                    if (bArr[36] == 0) {
                        str3 = String.valueOf(bArr[28] & 255) + "℃";
                    } else {
                        str3 = "-" + String.valueOf(bArr[28] & 255) + "℃";
                    }
                    if (bArr[37] == 0) {
                        str4 = String.valueOf(bArr[29] & 255) + "℃";
                    } else {
                        str4 = "-" + String.valueOf(bArr[29] & 255) + "℃";
                    }
                    this.value_4t.setText(str + "/" + str2 + "/" + str3 + "/" + str4);
                    this.getvalue3finish = true;
                    this.tempi = this.tempi + 1;
                    break;
                }
                break;
        }
    }
}
