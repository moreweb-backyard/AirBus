package com.pzj.ipcdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.pzj.ipcdemo.utils.ContentCommon;

/* loaded from: classes.dex */
public class SettingActivity extends Activity implements View.OnClickListener {
    private RelativeLayout alarm_Relat;
    private Button back_btn;
    private String cameraName;
    private String cameraPwd;
    private RelativeLayout pwd_Relat;
    private RelativeLayout rl_move_inform;
    private RelativeLayout sd_Relat;
    private RelativeLayout sensor;
    private String strDID;
    private RelativeLayout tf_Relat;
    private RelativeLayout time_Relat;
    private RelativeLayout update;
    private RelativeLayout wifi_Relat;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        setContentView(R.layout.setting);
        getDataFromOther();
        initView();
    }

    private void getDataFromOther() {
        Intent intent = getIntent();
        this.strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        this.cameraName = intent.getStringExtra(ContentCommon.STR_CAMERA_NAME);
        this.cameraPwd = intent.getStringExtra(ContentCommon.STR_CAMERA_PWD);
    }

    private void initView() {
        this.wifi_Relat = (RelativeLayout) findViewById(R.id.wifi_setting);
        this.pwd_Relat = (RelativeLayout) findViewById(R.id.pwd_setting);
        this.alarm_Relat = (RelativeLayout) findViewById(R.id.alarm_setting);
        this.time_Relat = (RelativeLayout) findViewById(R.id.time_setting);
        this.sd_Relat = (RelativeLayout) findViewById(R.id.sd_setting);
        this.tf_Relat = (RelativeLayout) findViewById(R.id.tf_setting);
        this.update = (RelativeLayout) findViewById(R.id.update_firmware);
        this.back_btn = (Button) findViewById(R.id.back);
        this.sensor = (RelativeLayout) findViewById(R.id.setting_sensor);
        this.rl_move_inform = (RelativeLayout) findViewById(R.id.rl_move_inform);
        this.wifi_Relat.setOnClickListener(this);
        this.pwd_Relat.setOnClickListener(this);
        this.alarm_Relat.setOnClickListener(this);
        this.time_Relat.setOnClickListener(this);
        this.sd_Relat.setOnClickListener(this);
        this.tf_Relat.setOnClickListener(this);
        this.update.setOnClickListener(this);
        this.back_btn.setOnClickListener(this);
        this.sensor.setOnClickListener(this);
        this.rl_move_inform.setOnClickListener(this);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.wifi_setting) {
            Intent intent = new Intent(this, (Class<?>) SettingWifiActivity.class);
            intent.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            intent.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.pwd_setting) {
            Intent intent2 = new Intent(this, (Class<?>) SettingUserActivity.class);
            intent2.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            intent2.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent2.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            startActivity(intent2);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.alarm_setting) {
            Intent intent3 = new Intent(this, (Class<?>) SettingAlarmActivity.class);
            intent3.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            intent3.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent3.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            startActivity(intent3);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.time_setting) {
            Intent intent4 = new Intent(this, (Class<?>) SettingDateActivity.class);
            intent4.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            intent4.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent4.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            startActivity(intent4);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.sd_setting) {
            Intent intent5 = new Intent(this, (Class<?>) SettingSDCardActivity.class);
            intent5.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            intent5.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent5.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            startActivity(intent5);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.tf_setting) {
            Intent intent6 = new Intent(this, (Class<?>) PlayBackTFActivity.class);
            intent6.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent6.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            intent6.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            intent6.putExtra(ContentCommon.STR_CAMERA_USER, ContentCommon.DEFAULT_USER_NAME);
            startActivity(intent6);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.update_firmware) {
            Intent intent7 = new Intent(this, (Class<?>) FirmwareUpdateActiviy.class);
            intent7.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent7.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            startActivity(intent7);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.setting_sensor) {
            Intent intent8 = new Intent(this, (Class<?>) SensorListActivty.class);
            intent8.putExtra(ContentCommon.STR_CAMERA_NAME, this.cameraName);
            intent8.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            intent8.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            startActivity(intent8);
            return;
        }
        if (id == R.id.rl_move_inform) {
            Intent intent9 = new Intent(this, (Class<?>) MoveNotificationActivity.class);
            intent9.putExtra(ContentCommon.STR_CAMERA_PWD, this.cameraPwd);
            intent9.putExtra(ContentCommon.STR_CAMERA_ID, this.strDID);
            startActivity(intent9);
            return;
        }
        if (id == R.id.back) {
            finish();
            overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        }
    }
}
