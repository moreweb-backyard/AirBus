package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.CircleView;
import com.shima.smartbushome.database.Savemedia;
import com.shima.smartbushome.database.Savemediabutton;
import com.shima.smartbushome.founction_command.mediacontrol;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MediaType1 extends RelativeLayout implements View.OnClickListener, View.OnLongClickListener {
    public static CheckBox isIRMarco;
    public static CheckBox onoroff;
    EditText IRNO;
    public CircleView.OnPressListener controlpress;
    public CircleView cv;
    private ImageView iv_center;
    private ImageView iv_down;
    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView iv_up;
    mediacontrol mc;
    List<Savemediabutton> mediacontent;
    Button mediatype1_back;
    Button mediatype1_down;
    Button mediatype1_home;
    Button mediatype1_left;
    Button mediatype1_min;
    Button mediatype1_mute;
    Button mediatype1_num;
    Button mediatype1_off;
    Button mediatype1_ok;
    Button mediatype1_on;
    Button mediatype1_plus;
    Button mediatype1_right;
    Button mediatype1_setting;
    Button mediatype1_up;
    Button mediatype1_v1;
    Button mediatype1_v2;
    Button mediatype1_v3;
    Button mediatype1_v4;
    TextView name;
    AlertView numpadalter;
    Context rootcontext;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    Savemedia thismedia;
    int updatebuttonnum;
    View view;

    public MediaType1(Context context) {
        super(context);
        this.mc = new mediacontrol();
        this.mediacontent = new ArrayList();
        this.thismedia = new Savemedia();
        this.controlpress = new CircleView.OnPressListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.1
            /* JADX WARN: Removed duplicated region for block: B:26:0x006f  */
            /* JADX WARN: Removed duplicated region for block: B:58:0x00e8  */
            @Override // com.shima.smartbushome.assist.CircleView.OnPressListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onPress(java.lang.String r19, java.lang.String r20) {
                /*
                    Method dump skipped, instructions count: 296
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.MediaType1.AnonymousClass1.onPress(java.lang.String, java.lang.String):void");
            }
        };
        this.updatebuttonnum = 0;
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.26
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == MediaType1.this.settingalter && i == 0) {
                    Savemediabutton savemediabutton = new Savemediabutton();
                    savemediabutton.room_id = MediaType1.this.thismedia.room_id;
                    savemediabutton.media_id = MediaType1.this.thismedia.media_id;
                    savemediabutton.button_num = MediaType1.this.updatebuttonnum;
                    savemediabutton.media_swno = Integer.parseInt(MediaType1.this.IRNO.getText().toString().trim());
                    savemediabutton.media_type = 1;
                    if (MediaType1.onoroff.isChecked()) {
                        savemediabutton.media_controltype = 255;
                    } else {
                        savemediabutton.media_controltype = 0;
                    }
                    if (MediaType1.isIRMarco.isChecked()) {
                        savemediabutton.ifIRmarco = 1;
                    } else {
                        savemediabutton.ifIRmarco = 0;
                    }
                    MainActivity.mgr.updatemediabutton(savemediabutton);
                    MediaType1.this.renewdata();
                }
            }
        };
        initview(context);
    }

    public MediaType1(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mc = new mediacontrol();
        this.mediacontent = new ArrayList();
        this.thismedia = new Savemedia();
        this.controlpress = new CircleView.OnPressListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.1
            @Override // com.shima.smartbushome.assist.CircleView.OnPressListener
            public void onPress(String v, String v2) {
                /*
                    Method dump skipped, instructions count: 296
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.MediaType1.AnonymousClass1.onPress(java.lang.String, java.lang.String):void");
            }
        };
        this.updatebuttonnum = 0;
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.26
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == MediaType1.this.settingalter && i == 0) {
                    Savemediabutton savemediabutton = new Savemediabutton();
                    savemediabutton.room_id = MediaType1.this.thismedia.room_id;
                    savemediabutton.media_id = MediaType1.this.thismedia.media_id;
                    savemediabutton.button_num = MediaType1.this.updatebuttonnum;
                    savemediabutton.media_swno = Integer.parseInt(MediaType1.this.IRNO.getText().toString().trim());
                    savemediabutton.media_type = 1;
                    if (MediaType1.onoroff.isChecked()) {
                        savemediabutton.media_controltype = 255;
                    } else {
                        savemediabutton.media_controltype = 0;
                    }
                    if (MediaType1.isIRMarco.isChecked()) {
                        savemediabutton.ifIRmarco = 1;
                    } else {
                        savemediabutton.ifIRmarco = 0;
                    }
                    MainActivity.mgr.updatemediabutton(savemediabutton);
                    MediaType1.this.renewdata();
                }
            }
        };
        initview(context);
    }

    public MediaType1(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mc = new mediacontrol();
        this.mediacontent = new ArrayList();
        this.thismedia = new Savemedia();
        this.controlpress = new CircleView.OnPressListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.1
            @Override // com.shima.smartbushome.assist.CircleView.OnPressListener
            public void onPress(String v, String v2) {
                /*
                    Method dump skipped, instructions count: 296
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.MediaType1.AnonymousClass1.onPress(java.lang.String, java.lang.String):void");
            }
        };
        this.updatebuttonnum = 0;
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.26
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i2) {
                if (obj == MediaType1.this.settingalter && i2 == 0) {
                    Savemediabutton savemediabutton = new Savemediabutton();
                    savemediabutton.room_id = MediaType1.this.thismedia.room_id;
                    savemediabutton.media_id = MediaType1.this.thismedia.media_id;
                    savemediabutton.button_num = MediaType1.this.updatebuttonnum;
                    savemediabutton.media_swno = Integer.parseInt(MediaType1.this.IRNO.getText().toString().trim());
                    savemediabutton.media_type = 1;
                    if (MediaType1.onoroff.isChecked()) {
                        savemediabutton.media_controltype = 255;
                    } else {
                        savemediabutton.media_controltype = 0;
                    }
                    if (MediaType1.isIRMarco.isChecked()) {
                        savemediabutton.ifIRmarco = 1;
                    } else {
                        savemediabutton.ifIRmarco = 0;
                    }
                    MainActivity.mgr.updatemediabutton(savemediabutton);
                    MediaType1.this.renewdata();
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.media_layout_type1, this);
        this.rootcontext = context;
        this.mediatype1_on = (Button) this.view.findViewById(R.id.mediatype1_on);
        this.mediatype1_on.setOnClickListener(this);
        this.mediatype1_on.setOnLongClickListener(this);
        this.mediatype1_off = (Button) this.view.findViewById(R.id.mediatype1_off);
        this.mediatype1_off.setOnClickListener(this);
        this.mediatype1_off.setOnLongClickListener(this);
        this.mediatype1_min = (Button) this.view.findViewById(R.id.mediatype1_min);
        this.mediatype1_min.setOnClickListener(this);
        this.mediatype1_min.setOnLongClickListener(this);
        this.mediatype1_plus = (Button) this.view.findViewById(R.id.mediatype1_plus);
        this.mediatype1_plus.setOnClickListener(this);
        this.mediatype1_plus.setOnLongClickListener(this);
        this.mediatype1_mute = (Button) this.view.findViewById(R.id.mediatype1_mute);
        this.mediatype1_mute.setOnClickListener(this);
        this.mediatype1_mute.setOnLongClickListener(this);
        this.cv = (CircleView) this.view.findViewById(R.id.mediatype_controlview);
        this.cv.setOnPressListener(this.controlpress);
        this.mediatype1_v1 = (Button) this.view.findViewById(R.id.mediatype1_v1);
        this.mediatype1_v1.setOnClickListener(this);
        this.mediatype1_v1.setOnLongClickListener(this);
        this.mediatype1_v2 = (Button) this.view.findViewById(R.id.mediatype1_v2);
        this.mediatype1_v2.setOnClickListener(this);
        this.mediatype1_v2.setOnLongClickListener(this);
        this.mediatype1_v3 = (Button) this.view.findViewById(R.id.mediatype1_v3);
        this.mediatype1_v3.setOnClickListener(this);
        this.mediatype1_v3.setOnLongClickListener(this);
        this.mediatype1_v4 = (Button) this.view.findViewById(R.id.mediatype1_v4);
        this.mediatype1_v4.setOnClickListener(this);
        this.mediatype1_v4.setOnLongClickListener(this);
        this.mediatype1_num = (Button) this.view.findViewById(R.id.mediatype1_num);
        this.mediatype1_num.setOnClickListener(this);
        this.mediatype1_back = (Button) this.view.findViewById(R.id.mediatype1_back);
        this.mediatype1_back.setOnClickListener(this);
        this.mediatype1_back.setOnLongClickListener(this);
        this.mediatype1_home = (Button) this.view.findViewById(R.id.mediatype1_home);
        this.mediatype1_home.setOnClickListener(this);
        this.mediatype1_home.setOnLongClickListener(this);
        this.mediatype1_setting = (Button) this.view.findViewById(R.id.mediatype1_setting);
        this.mediatype1_setting.setOnClickListener(this);
        this.mediatype1_setting.setOnLongClickListener(this);
        this.name = (TextView) this.view.findViewById(R.id.medianame);
        initKeyView(this.view);
    }

    public void sentcommand(int i) {
        if (getbuttoninfo(i).ifIRmarco == 1) {
            this.mc.IRMarcoControl((byte) this.thismedia.subnetID, (byte) this.thismedia.deviceID, getbuttoninfo(i).media_swno, getbuttoninfo(i).media_controltype, MainActivity.mydupsocket);
        } else {
            this.mc.IRControl((byte) this.thismedia.subnetID, (byte) this.thismedia.deviceID, getbuttoninfo(i).media_swno, getbuttoninfo(i).media_controltype, MainActivity.mydupsocket);
        }
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

    class keyViewTouchListener implements View.OnTouchListener {
        keyViewTouchListener() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case 0:
                    view.setAlpha(0.9f);
                    break;
                case 1:
                    view.setAlpha(0.7f);
                    break;
            }
            return true;
        }
    }

    class keyViewClickListener implements View.OnClickListener {
        keyViewClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_center /* 2131231223 */:
                    MediaType1.this.sentcommand(10);
                    break;
                case R.id.iv_down /* 2131231224 */:
                    MediaType1.this.sentcommand(7);
                    break;
                case R.id.iv_left /* 2131231233 */:
                    MediaType1.this.sentcommand(8);
                    break;
                case R.id.iv_right /* 2131231237 */:
                    MediaType1.this.sentcommand(9);
                    break;
                case R.id.iv_up /* 2131231243 */:
                    MediaType1.this.sentcommand(6);
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
                    MediaType1.this.setbuttoninfo(10);
                    break;
                case R.id.iv_down /* 2131231224 */:
                    MediaType1.this.setbuttoninfo(7);
                    break;
                case R.id.iv_left /* 2131231233 */:
                    MediaType1.this.setbuttoninfo(8);
                    break;
                case R.id.iv_right /* 2131231237 */:
                    MediaType1.this.setbuttoninfo(9);
                    break;
                case R.id.iv_up /* 2131231243 */:
                    MediaType1.this.setbuttoninfo(6);
                    break;
            }
            return true;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mediatype1_back /* 2131231401 */:
                sentcommand(15);
                break;
            case R.id.mediatype1_home /* 2131231402 */:
                sentcommand(16);
                break;
            case R.id.mediatype1_min /* 2131231403 */:
                sentcommand(3);
                break;
            case R.id.mediatype1_mute /* 2131231404 */:
                sentcommand(5);
                break;
            case R.id.mediatype1_num /* 2131231405 */:
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
                button.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(18);
                    }
                });
                button.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.3
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(18);
                        return false;
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(19);
                    }
                });
                button2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.5
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(19);
                        return false;
                    }
                });
                button3.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.6
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(20);
                    }
                });
                button3.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.7
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(20);
                        return false;
                    }
                });
                button4.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.8
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(21);
                    }
                });
                button4.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.9
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(21);
                        return false;
                    }
                });
                button5.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(22);
                    }
                });
                button5.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.11
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(22);
                        return false;
                    }
                });
                button6.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.12
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(23);
                    }
                });
                button6.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.13
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(23);
                        return false;
                    }
                });
                button7.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.14
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(24);
                    }
                });
                button7.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.15
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(24);
                        return false;
                    }
                });
                button8.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.16
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(25);
                    }
                });
                button8.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.17
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(25);
                        return false;
                    }
                });
                button9.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.18
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(26);
                    }
                });
                button9.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.19
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(26);
                        return false;
                    }
                });
                button10.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.20
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(27);
                    }
                });
                button10.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.21
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(27);
                        return false;
                    }
                });
                button11.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.22
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(28);
                    }
                });
                button11.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.23
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(28);
                        return false;
                    }
                });
                button12.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.24
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        MediaType1.this.sentcommand(29);
                    }
                });
                button12.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.shima.smartbushome.selflayout.MediaType1.25
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View view2) {
                        MediaType1.this.setbuttoninfo(29);
                        return false;
                    }
                });
                this.numpadalter.addExtView(viewGroup);
                this.numpadalter.setCancelable(true);
                this.numpadalter.show();
                break;
            case R.id.mediatype1_off /* 2131231406 */:
                sentcommand(2);
                break;
            case R.id.mediatype1_on /* 2131231407 */:
                sentcommand(1);
                break;
            case R.id.mediatype1_plus /* 2131231408 */:
                sentcommand(4);
                break;
            case R.id.mediatype1_setting /* 2131231409 */:
                sentcommand(17);
                break;
            case R.id.mediatype1_v1 /* 2131231410 */:
                sentcommand(11);
                break;
            case R.id.mediatype1_v2 /* 2131231411 */:
                sentcommand(12);
                break;
            case R.id.mediatype1_v3 /* 2131231412 */:
                sentcommand(13);
                break;
            case R.id.mediatype1_v4 /* 2131231413 */:
                sentcommand(14);
                break;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (!MainActivity.islockchangeid) {
            switch (view.getId()) {
                case R.id.mediatype1_back /* 2131231401 */:
                    setbuttoninfo(15);
                    break;
                case R.id.mediatype1_home /* 2131231402 */:
                    setbuttoninfo(16);
                    break;
                case R.id.mediatype1_min /* 2131231403 */:
                    setbuttoninfo(3);
                    break;
                case R.id.mediatype1_mute /* 2131231404 */:
                    setbuttoninfo(5);
                    break;
                case R.id.mediatype1_off /* 2131231406 */:
                    setbuttoninfo(2);
                    break;
                case R.id.mediatype1_on /* 2131231407 */:
                    setbuttoninfo(1);
                    break;
                case R.id.mediatype1_plus /* 2131231408 */:
                    setbuttoninfo(4);
                    break;
                case R.id.mediatype1_setting /* 2131231409 */:
                    setbuttoninfo(17);
                    break;
                case R.id.mediatype1_v1 /* 2131231410 */:
                    setbuttoninfo(11);
                    break;
                case R.id.mediatype1_v2 /* 2131231411 */:
                    setbuttoninfo(12);
                    break;
                case R.id.mediatype1_v3 /* 2131231412 */:
                    setbuttoninfo(13);
                    break;
                case R.id.mediatype1_v4 /* 2131231413 */:
                    setbuttoninfo(14);
                    break;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setbuttoninfo(int i) {
        this.updatebuttonnum = i;
        this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, this.rootcontext, AlertView.Style.Alert, this.settingclick);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.rootcontext).inflate(R.layout.setting_mediabuttoninfo, (ViewGroup) null);
        this.IRNO = (EditText) viewGroup.findViewById(R.id.setmediabutton_no);
        onoroff = (CheckBox) viewGroup.findViewById(R.id.setmediabutton_onoff);
        isIRMarco = (CheckBox) viewGroup.findViewById(R.id.setmediabutton_ismarco);
        this.IRNO.setText(String.valueOf(getbuttoninfo(i).media_swno));
        if (getbuttoninfo(i).media_controltype == 255) {
            onoroff.setChecked(true);
        } else {
            onoroff.setChecked(false);
        }
        if (getbuttoninfo(i).ifIRmarco == 1) {
            isIRMarco.setChecked(true);
        } else {
            isIRMarco.setChecked(false);
        }
        this.settingalter.addExtView(viewGroup);
        this.settingalter.show();
    }

    public void renewdata() {
        List<Savemediabutton> listQuerymediabutton = MainActivity.mgr.querymediabutton();
        if (this.mediacontent.size() > 0) {
            this.mediacontent.clear();
        }
        for (int i = 0; i < listQuerymediabutton.size(); i++) {
            if (listQuerymediabutton.get(i).room_id == this.thismedia.room_id && listQuerymediabutton.get(i).media_id == this.thismedia.media_id) {
                this.mediacontent.add(listQuerymediabutton.get(i));
            }
        }
    }

    public void setcontent(Savemedia savemedia) {
        this.thismedia = savemedia;
        List<Savemediabutton> listQuerymediabutton = MainActivity.mgr.querymediabutton();
        if (this.mediacontent.size() > 0) {
            this.mediacontent.clear();
        }
        for (int i = 0; i < listQuerymediabutton.size(); i++) {
            if (listQuerymediabutton.get(i).room_id == savemedia.room_id && listQuerymediabutton.get(i).media_id == savemedia.media_id) {
                this.mediacontent.add(listQuerymediabutton.get(i));
            }
        }
        this.name.setText(this.thismedia.media_statement);
    }

    public Savemediabutton getbuttoninfo(int i) {
        Savemediabutton savemediabutton = new Savemediabutton();
        for (int i2 = 0; i2 < this.mediacontent.size(); i2++) {
            if (this.mediacontent.get(i2).button_num == i) {
                return this.mediacontent.get(i2);
            }
        }
        return savemediabutton;
    }
}
