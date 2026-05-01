package com.pzj.ipcdemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.bean.DateBean;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import java.util.Calendar;
import java.util.TimeZone;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SettingDateActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, CompoundButton.OnCheckedChangeListener, BridgeService.DateTimeInterface {
    private ProgressDialog progressDialog;
    private String strDID;
    private boolean successFlag;
    private final int FAIL = 0;
    private final int SUCCESS = 1;
    private final int PARAMS = 3;
    private final int TIMEOUT = 3000;
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingDateActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 3) {
                SettingDateActivity.this.successFlag = true;
                if (SettingDateActivity.this.progressDialog.isShowing()) {
                    SettingDateActivity.this.progressDialog.cancel();
                    if (SettingDateActivity.this.dateBean.getNtp_enable() == 1) {
                        SettingDateActivity.this.cbxCheck.setChecked(true);
                        SettingDateActivity.this.ntpView.setVisibility(0);
                    } else {
                        SettingDateActivity.this.cbxCheck.setChecked(false);
                        SettingDateActivity.this.ntpView.setVisibility(8);
                    }
                    SettingDateActivity.this.editNtpServer.setText(SettingDateActivity.this.dateBean.getNtp_ser());
                    SettingDateActivity.this.tvDeviceTime.setText("longtime:" + SettingDateActivity.this.dateBean.getNow());
                    setTimeZone();
                }
                return;
            }
            switch (i) {
                case 0:
                    SettingDateActivity.this.showToast(R.string.date_setting_failed);
                    break;
                case 1:
                    SettingDateActivity.this.showToast(R.string.date_setting_success);
                    SettingDateActivity.this.finish();
                    break;
            }
        }

        private void setTimeZone() {
            Long l = new Long(SettingDateActivity.this.dateBean.getNow());
            switch (SettingDateActivity.this.dateBean.getTz()) {
                case -43200:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+12:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_auckland);
                    break;
                case -39600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+11:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_suolumen);
                    break;
                case -36000:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+10:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_guam);
                    break;
                case -34200:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+09:30"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_darwin);
                    break;
                case -32400:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+09:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_seoul);
                    break;
                case -28800:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+08:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_beijing);
                    break;
                case -25200:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+07:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_bangkok);
                    break;
                case -21600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+06:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_alamotu);
                    break;
                case -19800:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+05:30"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_calcutta);
                    break;
                case -18000:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+05:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_islamabad);
                    break;
                case -16200:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+04:30"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_kebuer);
                    break;
                case -14400:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+04:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_baku);
                    break;
                case -12600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+03:30"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_teheran);
                    break;
                case -10800:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+03:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_nairobi);
                    break;
                case -7200:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+02:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_athens);
                    break;
                case -3600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT+01:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_brussels);
                    break;
                case 0:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_greenwich);
                    break;
                case 3600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-01:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_cape_verde_island);
                    break;
                case 7200:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-02:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_center_ocean);
                    break;
                case 10800:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-03:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_brasilia);
                    break;
                case 12600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-03:30"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_newfoundland);
                    break;
                case 14400:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-04:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_ocean_time);
                    break;
                case 18000:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-05:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_eastern_time);
                    break;
                case 21600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-06:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_middle_part_time);
                    break;
                case 25200:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-07:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_mountain_time);
                    break;
                case 28800:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-08:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_pacific_time);
                    break;
                case 32400:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-09:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_alaska);
                    break;
                case 36000:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-10:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_hawaii);
                    break;
                case 39600:
                    SettingDateActivity.this.tvDeviceTime.setText(SettingDateActivity.this.setDeviceTime(l.longValue() * 1000, "GMT-11:00"));
                    SettingDateActivity.this.editTimeZone.setText(R.string.date_middle_island);
                    break;
            }
        }
    };
    private Button tvDeviceTime = null;
    private EditText editTimeZone = null;
    private EditText editNtpServer = null;
    private CheckBox cbxCheck = null;
    private ImageButton imgTimeZoneDown = null;
    private ImageButton imgNtpServerDown = null;
    private View ntpView = null;
    private PopupWindow timeZonePopWindow = null;
    private PopupWindow ntpServerPopWindow = null;
    private ScrollView scrollView = null;
    private DateBean dateBean = null;
    private Button btnOk = null;
    private Button btnCancel = null;
    private Button btnCheckOut = null;
    private Runnable runnable = new Runnable() { // from class: com.pzj.ipcdemo.SettingDateActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (SettingDateActivity.this.successFlag) {
                return;
            }
            SettingDateActivity.this.progressDialog.dismiss();
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public String setDeviceTime(long j, String str) {
        String strValueOf;
        String strValueOf2;
        String strValueOf3;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(str));
        calendar.setTimeInMillis(j);
        int i = calendar.get(1);
        int i2 = calendar.get(2) + 1;
        int i3 = calendar.get(5);
        int i4 = calendar.get(7);
        int i5 = calendar.get(11);
        int i6 = calendar.get(12);
        int i7 = calendar.get(13);
        String str2 = "";
        switch (i4) {
            case 1:
                str2 = "Sun";
                break;
            case 2:
                str2 = "Mon";
                break;
            case 3:
                str2 = "Tues";
                break;
            case 4:
                str2 = "Wed";
                break;
            case 5:
                str2 = "Thur";
                break;
            case 6:
                str2 = "Fri";
                break;
            case 7:
                str2 = "Sat";
                break;
        }
        String str3 = "";
        switch (i2) {
            case 1:
                str3 = "Jan";
                break;
            case 2:
                str3 = "Feb";
                break;
            case 3:
                str3 = "Mar";
                break;
            case 4:
                str3 = "Apr";
                break;
            case 5:
                str3 = "May";
                break;
            case 6:
                str3 = "Jun";
                break;
            case 7:
                str3 = "Jul";
                break;
            case 8:
                str3 = "Aug";
                break;
            case 9:
                str3 = "Sept";
                break;
            case 10:
                str3 = "Oct";
                break;
            case 11:
                str3 = "Nov";
                break;
            case 12:
                str3 = "Dec";
                break;
        }
        if (i5 < 10) {
            strValueOf = "0" + i5;
        } else {
            strValueOf = String.valueOf(i5);
        }
        if (i6 < 10) {
            strValueOf2 = "0" + i6;
        } else {
            strValueOf2 = String.valueOf(i6);
        }
        if (i7 < 10) {
            strValueOf3 = "0" + i7;
        } else {
            strValueOf3 = String.valueOf(i7);
        }
        return str2 + Consts.SECOND_LEVEL_SPLIT + i3 + " " + str3 + i + " " + strValueOf + ":" + strValueOf2 + ":" + strValueOf3 + "    UTC";
    }

    @Override // android.app.Activity
    protected void onPause() {
        overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        super.onPause();
    }

    @Override // com.pzj.ipcdemo.BaseActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getDataFromOther();
        requestWindowFeature(1);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        setContentView(R.layout.settingdate);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage(getString(R.string.date_get_params));
        this.progressDialog.show();
        this.mHandler.postDelayed(this.runnable, 3000L);
        findView();
        setListener();
        this.dateBean = new DateBean();
        BridgeService.setDateTimeInterface(this);
        NativeCaller.PPPPGetSystemParams(this.strDID, 4);
    }

    private void setListener() {
        this.imgTimeZoneDown.setOnClickListener(this);
        this.imgNtpServerDown.setOnClickListener(this);
        this.scrollView.setOnTouchListener(this);
        this.editTimeZone.setOnClickListener(this);
        this.editNtpServer.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
        this.cbxCheck.setOnCheckedChangeListener(this);
        this.btnCheckOut.setOnClickListener(this);
        this.progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.pzj.ipcdemo.SettingDateActivity.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
    }

    private void findView() {
        this.btnOk = (Button) findViewById(R.id.date_ok);
        this.btnCancel = (Button) findViewById(R.id.date_cancel);
        this.btnCheckOut = (Button) findViewById(R.id.date_btn_checkout);
        this.tvDeviceTime = (Button) findViewById(R.id.date_tv_device_time);
        this.editTimeZone = (EditText) findViewById(R.id.date_edit_timezone);
        this.editNtpServer = (EditText) findViewById(R.id.date_edit_ntp_server);
        this.cbxCheck = (CheckBox) findViewById(R.id.date_cbx_check);
        this.imgTimeZoneDown = (ImageButton) findViewById(R.id.date_img_timezone_down);
        this.imgNtpServerDown = (ImageButton) findViewById(R.id.date_img_ntp_server_down);
        this.ntpView = findViewById(R.id.date_ntp_view);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView1);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.top_bg));
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.date_ok) {
            setDate();
            return;
        }
        if (id == R.id.date_cancel) {
            finish();
            return;
        }
        if (id == R.id.date_btn_checkout) {
            checkDeviceAsPhoneTime();
            return;
        }
        if (id == R.id.date_edit_timezone || id == R.id.date_img_timezone_down) {
            if (this.ntpServerPopWindow != null && this.ntpServerPopWindow.isShowing()) {
                this.ntpServerPopWindow.dismiss();
                this.ntpServerPopWindow = null;
            }
            showTimeZonePopWindow();
            return;
        }
        if (id == R.id.date_edit_ntp_server || id == R.id.date_img_ntp_server_down) {
            if (this.timeZonePopWindow != null && this.timeZonePopWindow.isShowing()) {
                this.timeZonePopWindow.dismiss();
                this.timeZonePopWindow = null;
            }
            showNtpServerPopWindow();
            return;
        }
        if (id == R.id.date_ntpserver_kriss) {
            this.ntpServerPopWindow.dismiss();
            this.dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_kriss_re_kr));
            this.editNtpServer.setText(R.string.date_ntp_server_time_kriss_re_kr);
            return;
        }
        if (id == R.id.date_ntpserver_nist) {
            this.ntpServerPopWindow.dismiss();
            this.dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_nist_gov));
            this.editNtpServer.setText(R.string.date_ntp_server_time_nist_gov);
            return;
        }
        if (id == R.id.date_ntpserver_nuri) {
            this.ntpServerPopWindow.dismiss();
            this.dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_nuri_net));
            this.editNtpServer.setText(R.string.date_ntp_server_time_nuri_net);
            return;
        }
        if (id == R.id.date_ntpserver_windows) {
            this.ntpServerPopWindow.dismiss();
            this.dateBean.setNtp_ser(getResources().getString(R.string.date_ntp_server_time_windows_com));
            this.editNtpServer.setText(R.string.date_ntp_server_time_windows_com);
            return;
        }
        if (id == R.id.date_zone_middle_island) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(39600);
            this.editTimeZone.setText(R.string.date_middle_island);
            return;
        }
        if (id == R.id.date_zone_hawaii) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(36000);
            this.editTimeZone.setText(R.string.date_hawaii);
            return;
        }
        if (id == R.id.date_zone_alaska) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(32400);
            this.editTimeZone.setText(R.string.date_alaska);
            return;
        }
        if (id == R.id.date_zone_pacific_time) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(28800);
            this.editTimeZone.setText(R.string.date_pacific_time);
            return;
        }
        if (id == R.id.date_zone_mountain_time) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(25200);
            this.editTimeZone.setText(R.string.date_mountain_time);
            return;
        }
        if (id == R.id.date_zone_middle_part_time) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(21600);
            this.editTimeZone.setText(R.string.date_middle_part_time);
            return;
        }
        if (id == R.id.date_zone_eastern_time) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(18000);
            this.editTimeZone.setText(R.string.date_eastern_time);
            return;
        }
        if (id == R.id.date_zone_ocean_time) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(14400);
            this.editTimeZone.setText(R.string.date_ocean_time);
            return;
        }
        if (id == R.id.date_zone_newfoundland) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(12600);
            this.editTimeZone.setText(R.string.date_newfoundland);
            return;
        }
        if (id == R.id.date_zone_brasilia) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(10800);
            this.editTimeZone.setText(R.string.date_brasilia);
            return;
        }
        if (id == R.id.date_zone_center_ocean) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(7200);
            this.editTimeZone.setText(R.string.date_center_ocean);
            return;
        }
        if (id == R.id.date_zone_cap_verde_island) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(3600);
            this.editTimeZone.setText(R.string.date_cape_verde_island);
            return;
        }
        if (id == R.id.date_zone_greenwich) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(0);
            this.editTimeZone.setText(R.string.date_greenwich);
            return;
        }
        if (id == R.id.date_zone_brussels) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-3600);
            this.editTimeZone.setText(R.string.date_brussels);
            return;
        }
        if (id == R.id.date_zone_athens) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-7200);
            this.editTimeZone.setText(R.string.date_athens);
            return;
        }
        if (id == R.id.date_zone_nairobi) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-10800);
            this.editTimeZone.setText(R.string.date_nairobi);
            return;
        }
        if (id == R.id.date_zone_teheran) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-12600);
            this.editTimeZone.setText(R.string.date_teheran);
            return;
        }
        if (id == R.id.date_zone_baku) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-14400);
            this.editTimeZone.setText(R.string.date_baku);
            return;
        }
        if (id == R.id.date_zone_kebuer) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-16200);
            this.editTimeZone.setText(R.string.date_kebuer);
            return;
        }
        if (id == R.id.date_zone_islamabad) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-18000);
            this.editTimeZone.setText(R.string.date_islamabad);
            return;
        }
        if (id == R.id.date_zone_calcutta) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-19800);
            this.editTimeZone.setText(R.string.date_calcutta);
            return;
        }
        if (id == R.id.date_zone_alamotu) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-21600);
            this.editTimeZone.setText(R.string.date_alamotu);
            return;
        }
        if (id == R.id.date_zone_bangkok) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-25200);
            this.editTimeZone.setText(R.string.date_bangkok);
            return;
        }
        if (id == R.id.date_zone_beijing) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-28800);
            this.editTimeZone.setText(R.string.date_beijing);
            return;
        }
        if (id == R.id.date_zone_seoul) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-32400);
            this.editTimeZone.setText(R.string.date_seoul);
            return;
        }
        if (id == R.id.date_zone_darwin) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-34200);
            this.editTimeZone.setText(R.string.date_darwin);
            return;
        }
        if (id == R.id.date_zone_guam) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-36000);
            this.editTimeZone.setText(R.string.date_guam);
        } else if (id == R.id.date_zone_soulumen) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-39600);
            this.editTimeZone.setText(R.string.date_suolumen);
        } else if (id == R.id.date_zone_auckland) {
            this.timeZonePopWindow.dismiss();
            this.dateBean.setTz(-43200);
            this.editTimeZone.setText(R.string.date_auckland);
        }
    }

    private void setDate() {
        NativeCaller.PPPPDatetimeSetting(this.strDID, 0, this.dateBean.getTz(), this.dateBean.getNtp_enable(), this.dateBean.getNtp_ser());
    }

    private void checkDeviceAsPhoneTime() {
        int i = (-TimeZone.getDefault().getRawOffset()) / 1000;
        NativeCaller.PPPPDatetimeSetting(this.strDID, (int) (Calendar.getInstance().getTimeInMillis() / 1000), i, this.dateBean.getNtp_enable(), this.dateBean.getNtp_ser());
    }

    private void showNtpServerPopWindow() {
        if (this.ntpServerPopWindow == null || !this.ntpServerPopWindow.isShowing()) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.settingdate_ntpserver_popwindow, (ViewGroup) null);
            Button button = (Button) linearLayout.findViewById(R.id.date_ntpserver_kriss);
            Button button2 = (Button) linearLayout.findViewById(R.id.date_ntpserver_nist);
            Button button3 = (Button) linearLayout.findViewById(R.id.date_ntpserver_nuri);
            Button button4 = (Button) linearLayout.findViewById(R.id.date_ntpserver_windows);
            button.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);
            button4.setOnClickListener(this);
            this.ntpServerPopWindow = new PopupWindow(linearLayout, 200, -2);
            this.ntpServerPopWindow.showAsDropDown(this.imgNtpServerDown, -200, 0);
        }
    }

    private void showTimeZonePopWindow() {
        if (this.timeZonePopWindow == null || !this.timeZonePopWindow.isShowing()) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.settingdate_timezone_popwindow, (ViewGroup) null);
            Button button = (Button) linearLayout.findViewById(R.id.date_zone_middle_island);
            Button button2 = (Button) linearLayout.findViewById(R.id.date_zone_hawaii);
            Button button3 = (Button) linearLayout.findViewById(R.id.date_zone_alaska);
            Button button4 = (Button) linearLayout.findViewById(R.id.date_zone_pacific_time);
            Button button5 = (Button) linearLayout.findViewById(R.id.date_zone_mountain_time);
            Button button6 = (Button) linearLayout.findViewById(R.id.date_zone_middle_part_time);
            Button button7 = (Button) linearLayout.findViewById(R.id.date_zone_eastern_time);
            Button button8 = (Button) linearLayout.findViewById(R.id.date_zone_ocean_time);
            Button button9 = (Button) linearLayout.findViewById(R.id.date_zone_newfoundland);
            Button button10 = (Button) linearLayout.findViewById(R.id.date_zone_brasilia);
            Button button11 = (Button) linearLayout.findViewById(R.id.date_zone_center_ocean);
            Button button12 = (Button) linearLayout.findViewById(R.id.date_zone_cap_verde_island);
            Button button13 = (Button) linearLayout.findViewById(R.id.date_zone_greenwich);
            Button button14 = (Button) linearLayout.findViewById(R.id.date_zone_brussels);
            Button button15 = (Button) linearLayout.findViewById(R.id.date_zone_athens);
            Button button16 = (Button) linearLayout.findViewById(R.id.date_zone_nairobi);
            Button button17 = (Button) linearLayout.findViewById(R.id.date_zone_teheran);
            Button button18 = (Button) linearLayout.findViewById(R.id.date_zone_baku);
            Button button19 = (Button) linearLayout.findViewById(R.id.date_zone_kebuer);
            Button button20 = (Button) linearLayout.findViewById(R.id.date_zone_islamabad);
            Button button21 = (Button) linearLayout.findViewById(R.id.date_zone_calcutta);
            Button button22 = (Button) linearLayout.findViewById(R.id.date_zone_alamotu);
            Button button23 = (Button) linearLayout.findViewById(R.id.date_zone_bangkok);
            Button button24 = (Button) linearLayout.findViewById(R.id.date_zone_beijing);
            Button button25 = (Button) linearLayout.findViewById(R.id.date_zone_seoul);
            Button button26 = (Button) linearLayout.findViewById(R.id.date_zone_darwin);
            Button button27 = (Button) linearLayout.findViewById(R.id.date_zone_guam);
            Button button28 = (Button) linearLayout.findViewById(R.id.date_zone_soulumen);
            Button button29 = (Button) linearLayout.findViewById(R.id.date_zone_auckland);
            button.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);
            button4.setOnClickListener(this);
            button5.setOnClickListener(this);
            button6.setOnClickListener(this);
            button7.setOnClickListener(this);
            button8.setOnClickListener(this);
            button9.setOnClickListener(this);
            button10.setOnClickListener(this);
            button11.setOnClickListener(this);
            button12.setOnClickListener(this);
            button13.setOnClickListener(this);
            button14.setOnClickListener(this);
            button15.setOnClickListener(this);
            button16.setOnClickListener(this);
            button17.setOnClickListener(this);
            button18.setOnClickListener(this);
            button19.setOnClickListener(this);
            button20.setOnClickListener(this);
            button21.setOnClickListener(this);
            button22.setOnClickListener(this);
            button23.setOnClickListener(this);
            button24.setOnClickListener(this);
            button25.setOnClickListener(this);
            button26.setOnClickListener(this);
            button27.setOnClickListener(this);
            button28.setOnClickListener(this);
            button29.setOnClickListener(this);
            this.timeZonePopWindow = new PopupWindow(linearLayout, 300, 500);
            this.timeZonePopWindow.showAsDropDown(this.imgTimeZoneDown, -310, 0);
        }
    }

    private void getDataFromOther() {
        this.strDID = getIntent().getStringExtra(ContentCommon.STR_CAMERA_ID);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.timeZonePopWindow != null && this.timeZonePopWindow.isShowing()) {
            this.timeZonePopWindow.dismiss();
            this.timeZonePopWindow = null;
        }
        if (this.ntpServerPopWindow == null || !this.ntpServerPopWindow.isShowing()) {
            return;
        }
        this.ntpServerPopWindow.dismiss();
        this.ntpServerPopWindow = null;
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.timeZonePopWindow != null && this.timeZonePopWindow.isShowing()) {
            this.timeZonePopWindow.dismiss();
            this.timeZonePopWindow = null;
        }
        if (this.ntpServerPopWindow != null && this.ntpServerPopWindow.isShowing()) {
            this.ntpServerPopWindow.dismiss();
            this.ntpServerPopWindow = null;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.timeZonePopWindow != null && this.timeZonePopWindow.isShowing()) {
            this.timeZonePopWindow.dismiss();
            this.timeZonePopWindow = null;
        }
        if (this.ntpServerPopWindow == null || !this.ntpServerPopWindow.isShowing()) {
            return false;
        }
        this.ntpServerPopWindow.dismiss();
        this.ntpServerPopWindow = null;
        return false;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            this.dateBean.setNtp_enable(1);
            this.ntpView.setVisibility(0);
        } else {
            this.dateBean.setNtp_enable(0);
            this.ntpView.setVisibility(8);
        }
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.DateTimeInterface
    public void callBackDatetimeParams(String str, int i, int i2, int i3, String str2) {
        this.dateBean.setNow(i);
        this.dateBean.setTz(i2);
        this.dateBean.setNtp_enable(i3);
        this.dateBean.setNtp_ser(str2);
        this.mHandler.sendEmptyMessage(3);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.DateTimeInterface
    public void callBackSetSystemParamsResult(String str, int i, int i2) {
        this.mHandler.sendEmptyMessage(i2);
    }
}
