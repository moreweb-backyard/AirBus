package com.pzj.ipcdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SettingUserActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, BridgeService.UserInterface {
    private Button btnCancel;
    private Button btnOk;
    private String cameraName;
    private CheckBox cbxShowPwd;
    private EditText editName;
    private EditText editPwd;
    private ProgressDialog progressDialog;
    private String strDID;
    private TextView tvCameraName;
    private boolean successFlag = false;
    private int CAMERAPARAM = -1;
    private final int TIMEOUT = 3000;
    private final int FAILED = 0;
    private final int SUCCESS = 1;
    private final int PARAMS = 3;
    private String operatorName = "";
    private String operatorPwd = "";
    private String visitorName = "";
    private String visitorPwd = "";
    private String adminName = "";
    private String adminPwd = "";
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingUserActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 3) {
                SettingUserActivity.this.successFlag = true;
                if (SettingUserActivity.this.progressDialog.isShowing()) {
                    SettingUserActivity.this.progressDialog.cancel();
                }
                SettingUserActivity.this.editName.setText(SettingUserActivity.this.adminName);
                SettingUserActivity.this.editPwd.setText(SettingUserActivity.this.adminPwd);
            }
            switch (i) {
                case 0:
                    SettingUserActivity.this.showToast(R.string.user_set_failed);
                    break;
                case 1:
                    SettingUserActivity.this.showToast(R.string.user_set_success);
                    NativeCaller.PPPPRebootDevice(SettingUserActivity.this.strDID);
                    Log.d("info", "user:" + SettingUserActivity.this.adminName + " pwd:" + SettingUserActivity.this.adminPwd);
                    final Intent intent = new Intent(ContentCommon.STR_CAMERA_INFO_RECEIVER);
                    intent.putExtra(ContentCommon.STR_CAMERA_NAME, SettingUserActivity.this.cameraName);
                    intent.putExtra(ContentCommon.STR_CAMERA_ID, SettingUserActivity.this.strDID);
                    intent.putExtra(ContentCommon.STR_CAMERA_USER, SettingUserActivity.this.adminName);
                    intent.putExtra(ContentCommon.STR_CAMERA_PWD, SettingUserActivity.this.adminPwd);
                    intent.putExtra(ContentCommon.STR_CAMERA_OLD_ID, SettingUserActivity.this.strDID);
                    intent.putExtra(ContentCommon.CAMERA_OPTION, 3);
                    SettingUserActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.pzj.ipcdemo.SettingUserActivity.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SettingUserActivity.this.sendBroadcast(intent);
                        }
                    }, 3000L);
                    SettingUserActivity.this.finish();
                    break;
            }
        }
    };
    private Runnable runnable = new Runnable() { // from class: com.pzj.ipcdemo.SettingUserActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (SettingUserActivity.this.successFlag) {
                return;
            }
            SettingUserActivity.this.successFlag = false;
            SettingUserActivity.this.progressDialog.dismiss();
        }
    };
    private Runnable settingRunnable = new Runnable() { // from class: com.pzj.ipcdemo.SettingUserActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (SettingUserActivity.this.successFlag) {
                return;
            }
            SettingUserActivity.this.showToast(R.string.user_set_failed);
        }
    };

    @Override // com.pzj.ipcdemo.service.BridgeService.UserInterface
    public void callBackPPPPMsgNotifyData(String str, int i, int i2) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getDataFromOther();
        requestWindowFeature(1);
        setContentView(R.layout.settinguser);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage(getString(R.string.user_getparams));
        this.progressDialog.show();
        this.mHandler.postDelayed(this.runnable, 3000L);
        findView();
        setLisetener();
        BridgeService.setUserInterface(this);
        NativeCaller.PPPPGetSystemParams(this.strDID, 4);
        this.tvCameraName.setText(this.cameraName + "  " + getResources().getString(R.string.setting_user));
    }

    private void setLisetener() {
        this.cbxShowPwd.setOnCheckedChangeListener(this);
        this.btnOk.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
        this.editName.addTextChangedListener(new MyTextWatch(R.id.edit_name));
        this.editPwd.addTextChangedListener(new MyTextWatch(R.id.edit_pwd));
    }

    private void getDataFromOther() {
        Intent intent = getIntent();
        this.strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        this.cameraName = intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
        this.adminName = ContentCommon.DEFAULT_USER_NAME;
    }

    @Override // android.app.Activity
    protected void onPause() {
        overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        super.onPause();
    }

    private void findView() {
        this.editName = (EditText) findViewById(R.id.edit_name);
        this.editPwd = (EditText) findViewById(R.id.edit_pwd);
        this.cbxShowPwd = (CheckBox) findViewById(R.id.cbox_show_pwd);
        this.btnOk = (Button) findViewById(R.id.user_ok);
        this.btnCancel = (Button) findViewById(R.id.user_cancel);
        this.tvCameraName = (TextView) findViewById(R.id.tv_camera_setting);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.top);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.top_bg));
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
        relativeLayout.setBackgroundDrawable(bitmapDrawable);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.user_ok) {
            setUser();
        } else if (id == R.id.user_cancel) {
            finish();
        }
    }

    private void setUser() {
        if (this.successFlag) {
            Log.d("info", "adminName:" + this.adminName + " adminPwd:" + this.adminPwd);
            NativeCaller.PPPPUserSetting(this.strDID, this.visitorName, this.visitorPwd, this.operatorName, this.operatorPwd, this.adminName, this.adminPwd);
            return;
        }
        showToast(R.string.user_set_failed);
    }

    public void CallBack_UserParams(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Log.d("info", " did:" + str + " user1:" + str2 + " pwd1:" + str3 + " user2:" + str4 + " pwd2:" + str5 + " user3:" + str6 + " pwd3:" + str7);
        this.adminName = str6;
        this.adminPwd = str7;
        this.mHandler.sendEmptyMessage(3);
    }

    public void CallBack_SetSystemParamsResult(String str, int i, int i2) {
        Log.d("info", "result:" + i2 + " paramType:" + i);
        this.mHandler.sendEmptyMessage(i2);
    }

    public void setPPPPMsgNotifyData(String str, int i, int i2) {
        if (this.strDID.equals(str) && i == 0) {
            this.CAMERAPARAM = i2;
        }
    }

    private class MyTextWatch implements TextWatcher {
        private int id;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public MyTextWatch(int i) {
            this.id = i;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            String string = editable.toString();
            if (this.id == R.id.edit_name) {
                SettingUserActivity.this.adminName = string;
                return;
            }
            if (this.id == R.id.edit_pwd) {
                Log.i("info", "result:" + string);
                SettingUserActivity.this.adminPwd = string;
            }
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            this.editPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            this.editPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void showToast(String str) {
        Toast.makeText(this, str, 0).show();
    }

    public void showToast(int i) {
        Toast.makeText(this, getResources().getString(i), 1).show();
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.UserInterface
    public void callBackUserParams(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Log.e("用户信息", "管理员名称" + this.adminName + "管理员密码" + this.adminPwd);
        this.adminName = str6;
        this.adminPwd = str7;
        this.operatorName = str4;
        this.operatorPwd = str5;
        this.mHandler.sendEmptyMessage(3);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.UserInterface
    public void callBackSetSystemParamsResult(String str, int i, int i2) {
        this.mHandler.sendEmptyMessage(i2);
    }
}
