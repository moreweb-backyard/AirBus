package com.pzj.ipcdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.pzj.ipcdemo.adapter.WifiScanListAdapter;
import com.pzj.ipcdemo.bean.WifiBean;
import com.pzj.ipcdemo.bean.WifiScanBean;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import java.util.Timer;
import java.util.TimerTask;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SettingWifiActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener, BridgeService.WifiInterface {
    private static final int INITTIMEOUT = 30000;
    private Button btnCancel;
    private Button btnManager;
    private Button btnOk;
    private String cameraName;
    private String cameraPwd;
    private CheckBox cbxShowPwd;
    private EditText editPwd;
    private ImageView imgDropDown;
    private WifiScanListAdapter mAdapter;
    private ListView mListView;
    private Timer mTimerTimeOut;
    private PopupWindow popupWindow;
    private ProgressDialog progressDialog;
    private View pwdView;
    private int result;
    private ProgressDialog scanDialog;
    private View signalView;
    private String strDID;
    private TextView tvCameraName;
    private TextView tvName;
    private TextView tvPrompt;
    private TextView tvSafe;
    private TextView tvSigal;
    private WifiBean wifiBean;
    private String LOG_TAG = "SettingWifiActivity";
    private boolean changeWifiFlag = false;
    private boolean successFlag = false;
    private final int END = 1;
    private final int WIFIPARAMS = 1;
    private final int SCANPARAMS = 2;
    private final int OVER = 3;
    private final int TIMEOUT = 4;
    private int CAMERAPARAM = -1;
    private final int NO = 0;
    private final int WEP = 1;
    private final int WPA_PSK_AES = 2;
    private final int WPA_PSK_TKIP = 3;
    private final int WPA2_PSK_AES = 4;
    private final int WPA2_PSK_TKIP = 5;
    private boolean isTimerOver = false;
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingWifiActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    SettingWifiActivity.this.successFlag = true;
                    if (SettingWifiActivity.this.progressDialog.isShowing()) {
                        SettingWifiActivity.this.progressDialog.cancel();
                    }
                    if (SettingWifiActivity.this.wifiBean.getEnable() == 1) {
                        if (!TextUtils.isEmpty(SettingWifiActivity.this.wifiBean.getSsid())) {
                            SettingWifiActivity.this.tvName.setText(SettingWifiActivity.this.wifiBean.getSsid());
                            SettingWifiActivity.this.tvPrompt.setText(SettingWifiActivity.this.getResources().getString(R.string.connected));
                        } else {
                            SettingWifiActivity.this.tvName.setText(SettingWifiActivity.this.getResources().getString(R.string.wifi_no_safe));
                            SettingWifiActivity.this.tvPrompt.setText(SettingWifiActivity.this.getResources().getString(R.string.wifi_not_connected));
                        }
                        switch (SettingWifiActivity.this.wifiBean.getAuthtype()) {
                            case 0:
                                SettingWifiActivity.this.tvSafe.setText(SettingWifiActivity.this.getResources().getString(R.string.wifi_no_safe));
                                break;
                            case 1:
                                SettingWifiActivity.this.tvSafe.setText("WEP");
                                break;
                            case 2:
                                SettingWifiActivity.this.tvSafe.setText("WPA_PSK(AES)");
                                break;
                            case 3:
                                SettingWifiActivity.this.tvSafe.setText("WPA_PSK(TKIP)");
                                break;
                            case 4:
                                SettingWifiActivity.this.tvSafe.setText("WPA2_PSK(AES)");
                                break;
                            case 5:
                                SettingWifiActivity.this.tvSafe.setText("WPA2_PSK(TKIP)");
                                break;
                        }
                    }
                    break;
                case 2:
                    Log.d(SettingWifiActivity.this.LOG_TAG, "handler  scan wifi");
                    if (SettingWifiActivity.this.scanDialog.isShowing()) {
                        SettingWifiActivity.this.scanDialog.cancel();
                        if (!SettingWifiActivity.this.isTimerOver) {
                            SettingWifiActivity.this.mTimerTimeOut.cancel();
                        }
                        SettingWifiActivity.this.mListView.setAdapter((ListAdapter) SettingWifiActivity.this.mAdapter);
                        SettingWifiActivity.this.setListViewHeight();
                        SettingWifiActivity.this.mListView.setVisibility(0);
                        Log.d(SettingWifiActivity.this.LOG_TAG, "handler  scan wifi  2");
                    }
                    Log.d(SettingWifiActivity.this.LOG_TAG, "handler  scan wifi  3");
                    break;
                case 3:
                    SettingWifiActivity.this.successFlag = true;
                    if (SettingWifiActivity.this.result != 1) {
                        if (SettingWifiActivity.this.result == 0) {
                            SettingWifiActivity.this.showToast(R.string.wifi_set_failed);
                            break;
                        }
                    } else {
                        Log.d("tag", "over");
                        NativeCaller.PPPPRebootDevice(SettingWifiActivity.this.strDID);
                        Toast.makeText(SettingWifiActivity.this, SettingWifiActivity.this.getResources().getString(R.string.wifi_set_success), 1).show();
                        SettingWifiActivity.this.sendBroadcast(new Intent("myback"));
                        SettingWifiActivity.this.finish();
                        break;
                    }
                    break;
                case 4:
                    if (SettingWifiActivity.this.scanDialog.isShowing()) {
                        SettingWifiActivity.this.scanDialog.cancel();
                        break;
                    }
                    break;
            }
        }
    };
    private Handler handler = new Handler() { // from class: com.pzj.ipcdemo.SettingWifiActivity.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SettingWifiActivity.this.tvSigal.setText(SettingWifiActivity.this.wifiBean.getDbm0() + "%");
            SettingWifiActivity.this.tvName.setText(SettingWifiActivity.this.wifiBean.getSsid());
            SettingWifiActivity.this.signalView.setVisibility(0);
            SettingWifiActivity.this.tvPrompt.setText(SettingWifiActivity.this.getResources().getString(R.string.wifi_not_connected));
            switch (SettingWifiActivity.this.wifiBean.getAuthtype()) {
                case 0:
                    SettingWifiActivity.this.pwdView.setVisibility(8);
                    SettingWifiActivity.this.tvSafe.setText(SettingWifiActivity.this.getResources().getString(R.string.wifi_no_safe));
                    break;
                case 1:
                    SettingWifiActivity.this.pwdView.setVisibility(0);
                    SettingWifiActivity.this.tvSafe.setText("WEP");
                    break;
                case 2:
                    SettingWifiActivity.this.pwdView.setVisibility(0);
                    SettingWifiActivity.this.tvSafe.setText("WPA_PSK(AES)");
                    break;
                case 3:
                    SettingWifiActivity.this.pwdView.setVisibility(0);
                    SettingWifiActivity.this.tvSafe.setText("WPA_PSK(TKIP)");
                    break;
                case 4:
                    SettingWifiActivity.this.pwdView.setVisibility(0);
                    SettingWifiActivity.this.tvSafe.setText("WPA2_PSK(AES)");
                    break;
                case 5:
                    SettingWifiActivity.this.pwdView.setVisibility(0);
                    SettingWifiActivity.this.tvSafe.setText("WPA2_PSK(TKIP)");
                    break;
            }
        }
    };
    private Runnable runnable = new Runnable() { // from class: com.pzj.ipcdemo.SettingWifiActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (SettingWifiActivity.this.successFlag) {
                return;
            }
            SettingWifiActivity.this.progressDialog.dismiss();
        }
    };
    private Runnable settingRunnable = new Runnable() { // from class: com.pzj.ipcdemo.SettingWifiActivity.6
        @Override // java.lang.Runnable
        public void run() {
            if (SettingWifiActivity.this.successFlag) {
                return;
            }
            SettingWifiActivity.this.showToast(R.string.wifi_set_failed);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void showToast(int i) {
        Toast.makeText(this, getResources().getString(i), 0).show();
    }

    @Override // android.app.Activity
    protected void onPause() {
        overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        super.onPause();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getDataFromOther();
        requestWindowFeature(1);
        setContentView(R.layout.settingwifi);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage(getString(R.string.wifi_getparams));
        this.progressDialog.show();
        this.mHandler.postDelayed(this.runnable, 30000L);
        this.wifiBean = new WifiBean();
        findView();
        setListener();
        this.mAdapter = new WifiScanListAdapter(this);
        this.mListView.setOnItemClickListener(this);
        BridgeService.setWifiInterface(this);
        NativeCaller.PPPPGetSystemParams(this.strDID, 4);
        NativeCaller.PPPPGetSystemParams(this.strDID, 22);
    }

    private void getDataFromOther() {
        Intent intent = getIntent();
        this.strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        this.cameraName = intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
        this.cameraPwd = intent.getStringExtra(ContentCommon.STR_CAMERA_PWD);
    }

    private void setListener() {
        this.imgDropDown.setOnClickListener(this);
        this.btnManager.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
        this.cbxShowPwd.setOnCheckedChangeListener(this);
    }

    private void findView() {
        this.imgDropDown = (ImageView) findViewById(R.id.wifi_img_drop);
        this.btnOk = (Button) findViewById(R.id.wifi_ok);
        this.btnCancel = (Button) findViewById(R.id.wifi_cancel);
        this.cbxShowPwd = (CheckBox) findViewById(R.id.wifi_cbox_show_pwd);
        this.mListView = (ListView) findViewById(R.id.wifi_listview);
        this.tvName = (TextView) findViewById(R.id.wifi_tv_name);
        this.tvPrompt = (TextView) findViewById(R.id.wifi_tv_prompt);
        this.tvSafe = (TextView) findViewById(R.id.wifi_tv_safe);
        this.tvSigal = (TextView) findViewById(R.id.wifi_tv_sigal);
        this.editPwd = (EditText) findViewById(R.id.wifi_edit_pwd);
        this.btnManager = (Button) findViewById(R.id.wifi_btn_manger);
        this.pwdView = findViewById(R.id.wifi_pwd_view);
        this.signalView = findViewById(R.id.wifi_sigalview);
        this.tvCameraName = (TextView) findViewById(R.id.tv_camera_setting);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.top_bg));
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.popupWindow != null && this.popupWindow.isShowing()) {
            this.popupWindow.dismiss();
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.wifi_btn_manger) {
            this.mAdapter.clearWifi();
            this.mAdapter.notifyDataSetChanged();
            this.mListView.setVisibility(8);
            NativeCaller.PPPPGetSystemParams(this.strDID, 20);
            this.scanDialog = new ProgressDialog(this);
            this.scanDialog.setProgressStyle(0);
            this.scanDialog.setMessage(getResources().getString(R.string.wifi_scanning));
            this.scanDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.pzj.ipcdemo.SettingWifiActivity.4
                @Override // android.content.DialogInterface.OnKeyListener
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    return i == 4;
                }
            });
            this.scanDialog.show();
            setTimeOut();
            return;
        }
        if (id == R.id.wifi_ok) {
            setWifi();
        } else if (id == R.id.wifi_cancel) {
            finish();
            overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        }
    }

    private void setTimeOut() {
        TimerTask timerTask = new TimerTask() { // from class: com.pzj.ipcdemo.SettingWifiActivity.5
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Log.d(SettingWifiActivity.this.LOG_TAG, "isTimeOver");
                SettingWifiActivity.this.isTimerOver = true;
                SettingWifiActivity.this.mHandler.sendEmptyMessage(4);
            }
        };
        this.mTimerTimeOut = new Timer();
        this.mTimerTimeOut.schedule(timerTask, 30000L);
    }

    private void setWifi() {
        if (this.changeWifiFlag) {
            String string = this.editPwd.getText().toString();
            if (this.wifiBean.getAuthtype() == 0) {
                this.wifiBean.setWpa_psk("");
                this.wifiBean.setKey1("");
            } else if (!TextUtils.isEmpty(string)) {
                if (this.wifiBean.getAuthtype() == 1) {
                    this.wifiBean.setKey1(string);
                } else {
                    this.wifiBean.setWpa_psk(string);
                }
            } else {
                showToast(R.string.pwd_no_empty);
                return;
            }
            try {
                NativeCaller.PPPPWifiSetting(this.wifiBean.getDid(), this.wifiBean.getEnable(), this.wifiBean.getSsid(), this.wifiBean.getChannel(), this.wifiBean.getMode(), this.wifiBean.getAuthtype(), this.wifiBean.getEncryp(), this.wifiBean.getKeyformat(), this.wifiBean.getDefkey(), this.wifiBean.getKey1(), this.wifiBean.getKey2(), this.wifiBean.getKey3(), this.wifiBean.getKey4(), this.wifiBean.getKey1_bits(), this.wifiBean.getKey2_bits(), this.wifiBean.getKey3_bits(), this.wifiBean.getKey4_bits(), this.wifiBean.getWpa_psk());
                return;
            } catch (Exception e) {
                showToast(R.string.wifi_scan_failed);
                e.printStackTrace();
                return;
            }
        }
        showToast(R.string.wifi_notchange);
    }

    private void setttingTimeOut() {
        this.successFlag = false;
        this.mHandler.postAtTime(this.settingRunnable, 30000L);
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            this.editPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            this.editPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.changeWifiFlag = true;
        WifiScanBean wifiScan = this.mAdapter.getWifiScan(i);
        this.wifiBean.setSsid(wifiScan.getSsid());
        this.wifiBean.setAuthtype(wifiScan.getSecurity());
        this.wifiBean.setChannel(wifiScan.getChannel());
        this.wifiBean.setDbm0(wifiScan.getDbm0());
        this.handler.sendEmptyMessage(1);
    }

    public void setListViewHeight() {
        ListAdapter adapter = this.mListView.getAdapter();
        if (adapter == null) {
            return;
        }
        int count = adapter.getCount();
        int measuredHeight = 0;
        for (int i = 0; i < count; i++) {
            View view = adapter.getView(i, null, this.mListView);
            view.measure(0, 0);
            measuredHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = this.mListView.getLayoutParams();
        layoutParams.height = measuredHeight + (this.mListView.getDividerHeight() * (adapter.getCount() - 1));
        this.mListView.setLayoutParams(layoutParams);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.WifiInterface
    public void callBackWifiParams(String str, int i, String str2, int i2, int i3, int i4, int i5, int i6, int i7, String str3, String str4, String str5, String str6, int i8, int i9, int i10, int i11, String str7) {
        Log.d("tag", "did:" + str + " enable:" + i + " ssid:" + str2 + " channel:" + i2 + " authtype:" + i4 + " encryp:" + i5 + " wpa_psk:" + str7);
        this.wifiBean.setDid(str);
        this.wifiBean.setEnable(1);
        this.wifiBean.setSsid(str2);
        this.wifiBean.setChannel(i2);
        this.wifiBean.setMode(0);
        this.wifiBean.setAuthtype(i4);
        this.wifiBean.setEncryp(0);
        this.wifiBean.setKeyformat(0);
        this.wifiBean.setDefkey(0);
        this.wifiBean.setKey1(str3);
        this.wifiBean.setKey2("");
        this.wifiBean.setKey3("");
        this.wifiBean.setKey4("");
        this.wifiBean.setKey1_bits(0);
        this.wifiBean.setKey2_bits(0);
        this.wifiBean.setKey3_bits(0);
        this.wifiBean.setKey4_bits(0);
        this.wifiBean.setWpa_psk(str7);
        Log.d(this.LOG_TAG, this.wifiBean.toString());
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.WifiInterface
    public void callBackWifiScanResult(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6) {
        Log.d(this.LOG_TAG, "ssid:" + str2 + " mac:" + str3 + " security:" + i + " dbm0" + i2 + " dbm1:" + i3 + " mode:" + i4 + " channel:" + i5 + " bEnd:" + i6);
        String str4 = this.LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("bEnd=");
        sb.append(i6);
        Log.d(str4, sb.toString());
        if (i6 != 1) {
            Log.d(this.LOG_TAG, "");
            WifiScanBean wifiScanBean = new WifiScanBean();
            wifiScanBean.setDid(str);
            wifiScanBean.setSsid(str2);
            wifiScanBean.setChannel(i5);
            wifiScanBean.setSecurity(i);
            wifiScanBean.setDbm0(i2);
            wifiScanBean.setMac(str3);
            wifiScanBean.setMode(i4);
            wifiScanBean.setDbm1(i3);
            this.mAdapter.addWifiScan(wifiScanBean);
            return;
        }
        Log.d(this.LOG_TAG, " bEnd=" + i6);
        this.mHandler.sendEmptyMessage(2);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.WifiInterface
    public void callBackSetSystemParamsResult(String str, int i, int i2) {
        Log.d("tag", "result:" + i2);
        this.result = i2;
        this.mHandler.sendEmptyMessage(3);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.WifiInterface
    public void callBackPPPPMsgNotifyData(String str, int i, int i2) {
        if (this.strDID.equals(str) && i == 0) {
            this.CAMERAPARAM = i2;
        }
    }
}
