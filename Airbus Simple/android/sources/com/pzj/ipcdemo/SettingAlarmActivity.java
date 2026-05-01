package com.pzj.ipcdemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import com.pzj.ipcdemo.bean.AlermBean;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SettingAlarmActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GestureDetector.OnGestureListener, View.OnTouchListener, BridgeService.AlarmInterface {
    private ScrollView scrollView;
    private String strDID = null;
    private boolean successFlag = false;
    private final int TIMEOUT = 3000;
    private final int ALERMPARAMS = 3;
    private final int UPLOADTIMETOOLONG = 4;
    private int cameraType = 0;
    private GestureDetector gt = new GestureDetector(this);
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingAlarmActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 3) {
                SettingAlarmActivity.this.successFlag = true;
                SettingAlarmActivity.this.progressDialog.cancel();
                if (SettingAlarmActivity.this.alermBean.getMotion_armed() == 0) {
                    SettingAlarmActivity.this.motionAlermView.setVisibility(8);
                } else {
                    SettingAlarmActivity.this.cbxMotionAlerm.setChecked(true);
                    SettingAlarmActivity.this.motionAlermView.setVisibility(0);
                }
                SettingAlarmActivity.this.tvSensitivity.setText(String.valueOf(SettingAlarmActivity.this.alermBean.getMotion_sensitivity()));
                if (SettingAlarmActivity.this.alermBean.getInput_armed() == 0) {
                    SettingAlarmActivity.this.ioAlermView.setVisibility(8);
                } else {
                    SettingAlarmActivity.this.cbxIOAlerm.setChecked(true);
                    SettingAlarmActivity.this.ioAlermView.setVisibility(0);
                }
                if (SettingAlarmActivity.this.alermBean.getIoin_level() == 0) {
                    SettingAlarmActivity.this.tvTriggerLevel.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_ioin_levellow));
                } else {
                    SettingAlarmActivity.this.tvTriggerLevel.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_ioin_levelhight));
                }
                if (SettingAlarmActivity.this.alermBean.getAlarm_audio() == 0) {
                    SettingAlarmActivity.this.audioAlermView.setVisibility(8);
                    SettingAlarmActivity.this.audioSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levelforbid));
                } else {
                    SettingAlarmActivity.this.cbxAudioAlerm.setChecked(true);
                    SettingAlarmActivity.this.audioAlermView.setVisibility(0);
                    if (1 == SettingAlarmActivity.this.alermBean.getAlarm_audio()) {
                        SettingAlarmActivity.this.audioSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levelhigh));
                    } else if (2 == SettingAlarmActivity.this.alermBean.getAlarm_audio()) {
                        SettingAlarmActivity.this.audioSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levelmiddle));
                    } else if (3 == SettingAlarmActivity.this.alermBean.getAlarm_audio()) {
                        SettingAlarmActivity.this.audioSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levellow));
                    }
                }
                if (SettingAlarmActivity.this.alermBean.getAlarm_temp() == 0) {
                    SettingAlarmActivity.this.tempAlermView.setVisibility(8);
                    SettingAlarmActivity.this.tempSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levelforbid));
                } else {
                    SettingAlarmActivity.this.cbxTempAlerm.setChecked(true);
                    SettingAlarmActivity.this.tempAlermView.setVisibility(0);
                    if (1 == SettingAlarmActivity.this.alermBean.getAlarm_temp()) {
                        SettingAlarmActivity.this.tempSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levelhigh));
                    } else if (2 == SettingAlarmActivity.this.alermBean.getAlarm_temp()) {
                        SettingAlarmActivity.this.tempSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levelmiddle));
                    } else if (3 == SettingAlarmActivity.this.alermBean.getAlarm_temp()) {
                        SettingAlarmActivity.this.tempSensitivity.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_audio_levellow));
                    }
                }
                if (SettingAlarmActivity.this.alermBean.getIolinkage() == 0) {
                    SettingAlarmActivity.this.ioMotionView.setVisibility(8);
                } else {
                    SettingAlarmActivity.this.cbxIOMotion.setChecked(true);
                    SettingAlarmActivity.this.ioMotionView.setVisibility(0);
                }
                if (SettingAlarmActivity.this.alermBean.getIoout_level() == 0) {
                    SettingAlarmActivity.this.tvIoOutLevel.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_ioin_levellow));
                } else {
                    SettingAlarmActivity.this.tvIoOutLevel.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_ioin_levelhight));
                }
                if (SettingAlarmActivity.this.alermBean.getAlermpresetsit() == 0) {
                    SettingAlarmActivity.this.tvPreset.setText(SettingAlarmActivity.this.getResources().getString(R.string.alerm_preset_no));
                } else {
                    SettingAlarmActivity.this.tvPreset.setText(String.valueOf(SettingAlarmActivity.this.alermBean.getAlermpresetsit()));
                }
                if (1 == SettingAlarmActivity.this.alermBean.getMotion_armed() || 1 == SettingAlarmActivity.this.alermBean.getInput_armed() || SettingAlarmActivity.this.alermBean.getAlarm_audio() != 0) {
                    SettingAlarmActivity.this.eventView.setVisibility(0);
                } else {
                    SettingAlarmActivity.this.eventView.setVisibility(8);
                    return;
                }
            }
            switch (i) {
                case 0:
                    SettingAlarmActivity.this.showToast(R.string.alerm_set_failed);
                    break;
                case 1:
                    SettingAlarmActivity.this.showToast(R.string.setting_aler_sucess);
                    SettingAlarmActivity.this.finish();
                    break;
            }
        }
    };
    private Button btnOk = null;
    private Button btnCancel = null;
    private View motionAlermView = null;
    private View ioAlermView = null;
    private View audioAlermView = null;
    private View tempAlermView = null;
    private View ioMotionView = null;
    private View eventView = null;
    private LinearLayout alarm3518eOptionll = null;
    private ImageView imgTriggerLevelDrop = null;
    private ImageView audioImgDrop = null;
    private ImageView tempImgDrop = null;
    private ImageView imgSensitiveDrop = null;
    private ImageView imgPresetDrop = null;
    private ImageView imgIoOutLevelDrop = null;
    private TextView tvIoOutLevel = null;
    private TextView tvPreset = null;
    private TextView tvTriggerLevel = null;
    private TextView tvSensitivity = null;
    private TextView audioSensitivity = null;
    private TextView tempSensitivity = null;
    private CheckBox cbxIOMotion = null;
    private CheckBox cbxIOAlerm = null;
    private CheckBox cbxAudioAlerm = null;
    private CheckBox cbxTempAlerm = null;
    private CheckBox cbxMotionAlerm = null;
    private AlermBean alermBean = null;
    private PopupWindow sensitivePopWindow = null;
    private PopupWindow triggerLevelPopWindow = null;
    private PopupWindow ioOutLevelPopWindow = null;
    private PopupWindow presteMovePopWindow = null;
    private PopupWindow audioPopWindow = null;
    private ProgressDialog progressDialog = null;
    private Runnable runnable = new Runnable() { // from class: com.pzj.ipcdemo.SettingAlarmActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (SettingAlarmActivity.this.successFlag) {
                return;
            }
            SettingAlarmActivity.this.progressDialog.dismiss();
        }
    };
    private boolean isTempAlarm = false;

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override // com.pzj.ipcdemo.BaseActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        getDataFromOther();
        setContentView(R.layout.settingalarm);
        NativeCaller.PPPPGetSystemParams(this.strDID, 4);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage(getString(R.string.alerm_getparams));
        this.progressDialog.show();
        this.mHandler.postDelayed(this.runnable, 3000L);
        this.alermBean = new AlermBean();
        findView();
        setListener();
        BridgeService.setAlarmInterface(this);
        initPopupWindow();
    }

    @Override // android.app.Activity
    protected void onPause() {
        overridePendingTransition(R.anim.out_to_right, R.anim.in_from_left);
        super.onPause();
    }

    private void setListener() {
        this.btnOk.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
        this.imgIoOutLevelDrop.setOnClickListener(this);
        this.imgPresetDrop.setOnClickListener(this);
        this.imgSensitiveDrop.setOnClickListener(this);
        this.imgTriggerLevelDrop.setOnClickListener(this);
        this.audioImgDrop.setOnClickListener(this);
        this.tempImgDrop.setOnClickListener(this);
        this.cbxMotionAlerm.setOnCheckedChangeListener(this);
        this.cbxIOAlerm.setOnCheckedChangeListener(this);
        this.cbxAudioAlerm.setOnCheckedChangeListener(this);
        this.cbxTempAlerm.setOnCheckedChangeListener(this);
        this.cbxIOMotion.setOnCheckedChangeListener(this);
        this.eventView.setOnTouchListener(this);
        this.ioMotionView.setOnTouchListener(this);
        this.scrollView.setOnTouchListener(this);
        this.progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.pzj.ipcdemo.SettingAlarmActivity.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4;
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.alerm_ok) {
            setAlerm();
            return;
        }
        if (id == R.id.alerm_cancel) {
            finish();
            return;
        }
        if (id == R.id.alerm_img_ioout_level_drop) {
            dismissPopupWindow();
            this.ioOutLevelPopWindow.showAsDropDown(this.imgIoOutLevelDrop, -140, 0);
            return;
        }
        if (id == R.id.ioout_hight) {
            this.tvIoOutLevel.setText(getResources().getString(R.string.alerm_ioin_levelhight));
            this.alermBean.setIoout_level(1);
            return;
        }
        if (id == R.id.ioout_low) {
            this.tvIoOutLevel.setText(getResources().getString(R.string.alerm_ioin_levellow));
            this.alermBean.setIoout_level(0);
            return;
        }
        if (id == R.id.alerm_img_preset_drop) {
            dismissPopupWindow();
            this.presteMovePopWindow.showAsDropDown(this.imgPresetDrop, -160, 0);
            return;
        }
        if (id == R.id.alerm_img_sensitive_drop) {
            dismissPopupWindow();
            this.sensitivePopWindow.showAsDropDown(this.imgSensitiveDrop, -120, 10);
            return;
        }
        if (id == R.id.alerm_img_leveldrop) {
            dismissPopupWindow();
            this.triggerLevelPopWindow.showAsDropDown(this.imgTriggerLevelDrop, -140, 0);
            return;
        }
        if (id == R.id.alerm_audio_leveldrop) {
            this.isTempAlarm = false;
            dismissPopupWindow();
            this.audioPopWindow.showAsDropDown(this.audioImgDrop, -140, 0);
            return;
        }
        if (id == R.id.alerm_temp_leveldrop) {
            this.isTempAlarm = true;
            dismissPopupWindow();
            this.audioPopWindow.showAsDropDown(this.tempImgDrop, -140, 0);
            return;
        }
        if (id == R.id.trigger_audio_levelhigh) {
            if (this.isTempAlarm) {
                this.alermBean.setAlarm_temp(1);
                this.tempSensitivity.setText(getResources().getString(R.string.alerm_audio_levelhigh));
            } else {
                this.audioSensitivity.setText(getResources().getString(R.string.alerm_audio_levelhigh));
                this.alermBean.setAlarm_audio(1);
            }
            this.audioPopWindow.dismiss();
            return;
        }
        if (id == R.id.trigger_audio_levelmiddle) {
            if (this.isTempAlarm) {
                this.alermBean.setAlarm_temp(2);
                this.tempSensitivity.setText(getResources().getString(R.string.alerm_audio_levelmiddle));
            } else {
                this.audioSensitivity.setText(getResources().getString(R.string.alerm_audio_levelmiddle));
                this.alermBean.setAlarm_audio(2);
            }
            this.audioPopWindow.dismiss();
            return;
        }
        if (id == R.id.trigger_audio_levellow) {
            if (this.isTempAlarm) {
                this.alermBean.setAlarm_temp(3);
                this.tempSensitivity.setText(getResources().getString(R.string.alerm_audio_levellow));
            } else {
                this.audioSensitivity.setText(getResources().getString(R.string.alerm_audio_levellow));
                this.alermBean.setAlarm_audio(3);
            }
            this.audioPopWindow.dismiss();
            return;
        }
        if (id == R.id.trigger_audio_levelforbid) {
            if (this.isTempAlarm) {
                this.alermBean.setAlarm_temp(0);
                this.tempSensitivity.setText(getResources().getString(R.string.alerm_audio_levelforbid));
            } else {
                this.audioSensitivity.setText(getResources().getString(R.string.alerm_audio_levelforbid));
                this.alermBean.setAlarm_audio(0);
            }
            this.audioPopWindow.dismiss();
            return;
        }
        if (id == R.id.trigger_hight) {
            this.tvTriggerLevel.setText(getResources().getString(R.string.alerm_ioin_levelhight));
            this.triggerLevelPopWindow.dismiss();
            this.alermBean.setIoin_level(1);
            return;
        }
        if (id == R.id.trigger_low) {
            this.tvTriggerLevel.setText(getResources().getString(R.string.alerm_ioin_levellow));
            this.triggerLevelPopWindow.dismiss();
            this.alermBean.setIoin_level(0);
            return;
        }
        if (id == R.id.sensitive_10) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(10);
            this.tvSensitivity.setText(String.valueOf(10));
            return;
        }
        if (id == R.id.sensitive_9) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(9);
            this.tvSensitivity.setText(String.valueOf(9));
            return;
        }
        if (id == R.id.sensitive_8) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(8);
            this.tvSensitivity.setText(String.valueOf(8));
            return;
        }
        if (id == R.id.sensitive_7) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(7);
            this.tvSensitivity.setText(String.valueOf(7));
            return;
        }
        if (id == R.id.sensitive_6) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(6);
            this.tvSensitivity.setText(String.valueOf(6));
            return;
        }
        if (id == R.id.sensitive_5) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(5);
            this.tvSensitivity.setText(String.valueOf(5));
            return;
        }
        if (id == R.id.sensitive_4) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(4);
            this.tvSensitivity.setText(String.valueOf(4));
            return;
        }
        if (id == R.id.sensitive_3) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(3);
            this.tvSensitivity.setText(String.valueOf(3));
            return;
        }
        if (id == R.id.sensitive_2) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(2);
            this.tvSensitivity.setText(String.valueOf(2));
            return;
        }
        if (id == R.id.sensitive_1) {
            this.sensitivePopWindow.dismiss();
            this.alermBean.setMotion_sensitivity(1);
            this.tvSensitivity.setText(String.valueOf(1));
            return;
        }
        if (id == R.id.preset_no) {
            this.alermBean.setAlermpresetsit(0);
            this.tvPreset.setText(getResources().getString(R.string.alerm_preset_no));
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_1) {
            this.alermBean.setAlermpresetsit(1);
            this.tvPreset.setText("1");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_2) {
            this.alermBean.setAlermpresetsit(2);
            this.tvPreset.setText("2");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_3) {
            this.alermBean.setAlermpresetsit(3);
            this.tvPreset.setText("3");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_4) {
            this.alermBean.setAlermpresetsit(4);
            this.tvPreset.setText("4");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_5) {
            this.alermBean.setAlermpresetsit(5);
            this.tvPreset.setText("5");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_6) {
            this.alermBean.setAlermpresetsit(6);
            this.tvPreset.setText("6");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_7) {
            this.alermBean.setAlermpresetsit(7);
            this.tvPreset.setText("7");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_8) {
            this.alermBean.setAlermpresetsit(8);
            this.tvPreset.setText("8");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_9) {
            this.alermBean.setAlermpresetsit(9);
            this.tvPreset.setText("9");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_10) {
            this.alermBean.setAlermpresetsit(10);
            this.tvPreset.setText("10");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_11) {
            this.alermBean.setAlermpresetsit(11);
            this.tvPreset.setText("11");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_12) {
            this.alermBean.setAlermpresetsit(12);
            this.tvPreset.setText("12");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_13) {
            this.alermBean.setAlermpresetsit(13);
            this.tvPreset.setText("13");
            this.presteMovePopWindow.dismiss();
            return;
        }
        if (id == R.id.preset_14) {
            this.alermBean.setAlermpresetsit(14);
            this.tvPreset.setText("14");
            this.presteMovePopWindow.dismiss();
        } else if (id == R.id.preset_15) {
            this.alermBean.setAlermpresetsit(15);
            this.tvPreset.setText("15");
            this.presteMovePopWindow.dismiss();
        } else if (id == R.id.preset_16) {
            this.alermBean.setAlermpresetsit(16);
            this.tvPreset.setText("16");
            this.presteMovePopWindow.dismiss();
        }
    }

    private void setAlerm() {
        if (this.successFlag) {
            Log.e("setAlerm", "setAlermTemp: " + this.alermBean.getAlarm_temp());
            NativeCaller.PPPPAlarmSetting(this.strDID, this.alermBean.getAlarm_audio(), this.alermBean.getMotion_armed(), this.alermBean.getMotion_sensitivity(), this.alermBean.getInput_armed(), this.alermBean.getIoin_level(), this.alermBean.getIoout_level(), this.alermBean.getIolinkage(), this.alermBean.getAlermpresetsit(), this.alermBean.getMail(), this.alermBean.getSnapshot(), this.alermBean.getRecord(), this.alermBean.getUpload_interval(), this.alermBean.getSchedule_enable(), -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
            return;
        }
        showToast(R.string.alerm_set_failed);
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.gt.onTouchEvent(motionEvent);
    }

    private void findView() {
        this.cbxMotionAlerm = (CheckBox) findViewById(R.id.alerm_cbx_move_layout);
        this.cbxIOAlerm = (CheckBox) findViewById(R.id.alerm_cbx_i0_layout);
        this.cbxAudioAlerm = (CheckBox) findViewById(R.id.alerm_cbx_audio_layout);
        this.cbxTempAlerm = (CheckBox) findViewById(R.id.alerm_cbx_temp_layout);
        this.cbxIOMotion = (CheckBox) findViewById(R.id.alerm_cbx_io_move);
        this.tvSensitivity = (TextView) findViewById(R.id.alerm_tv_sensitivity);
        this.audioSensitivity = (TextView) findViewById(R.id.alerm_audio_triggerlevel);
        this.tempSensitivity = (TextView) findViewById(R.id.alerm_temp_triggerlevel);
        this.tvTriggerLevel = (TextView) findViewById(R.id.alerm_tv_triggerlevel);
        this.tvPreset = (TextView) findViewById(R.id.alerm_tv_preset);
        this.tvIoOutLevel = (TextView) findViewById(R.id.alerm_tv_ioout_level_value);
        this.imgIoOutLevelDrop = (ImageView) findViewById(R.id.alerm_img_ioout_level_drop);
        this.imgPresetDrop = (ImageView) findViewById(R.id.alerm_img_preset_drop);
        this.imgSensitiveDrop = (ImageView) findViewById(R.id.alerm_img_sensitive_drop);
        this.imgTriggerLevelDrop = (ImageView) findViewById(R.id.alerm_img_leveldrop);
        this.audioImgDrop = (ImageView) findViewById(R.id.alerm_audio_leveldrop);
        this.tempImgDrop = (ImageView) findViewById(R.id.alerm_temp_leveldrop);
        this.alarm3518eOptionll = (LinearLayout) findViewById(R.id.alarm_3518e_option_view);
        if (this.cameraType == 1) {
            this.alarm3518eOptionll.setVisibility(0);
        }
        this.ioMotionView = findViewById(R.id.alerm_io_move_view);
        this.ioAlermView = findViewById(R.id.alerm_ioview);
        this.audioAlermView = findViewById(R.id.alerm_audio_level);
        this.tempAlermView = findViewById(R.id.alerm_temp_level);
        this.motionAlermView = findViewById(R.id.alerm_moveview);
        this.eventView = findViewById(R.id.alerm_eventview);
        this.btnOk = (Button) findViewById(R.id.alerm_ok);
        this.btnCancel = (Button) findViewById(R.id.alerm_cancel);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView1);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.top_bg));
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        int id = compoundButton.getId();
        if (id == R.id.alerm_cbx_move_layout) {
            if (z) {
                this.alermBean.setMotion_armed(1);
                this.motionAlermView.setVisibility(0);
            } else {
                this.alermBean.setMotion_armed(0);
                this.motionAlermView.setVisibility(8);
            }
        } else if (id == R.id.alerm_cbx_i0_layout) {
            if (z) {
                this.alermBean.setInput_armed(1);
                this.ioAlermView.setVisibility(0);
            } else {
                this.alermBean.setInput_armed(0);
                this.ioAlermView.setVisibility(8);
            }
        } else if (id == R.id.alerm_cbx_audio_layout) {
            if (z) {
                this.alermBean.setAudioArmedCheck(1);
                this.audioAlermView.setVisibility(0);
            } else {
                this.alermBean.setAudioArmedCheck(0);
                this.audioAlermView.setVisibility(8);
            }
        } else if (id == R.id.alerm_cbx_temp_layout) {
            if (z) {
                this.alermBean.setAlarmTempChecked(1);
                this.tempAlermView.setVisibility(0);
            } else {
                this.alermBean.setAlarmTempChecked(0);
                this.tempAlermView.setVisibility(8);
            }
        } else if (id == R.id.alerm_cbx_io_move) {
            if (z) {
                this.alermBean.setIolinkage(1);
                this.ioMotionView.setVisibility(0);
            } else {
                this.alermBean.setIolinkage(0);
                this.ioMotionView.setVisibility(8);
            }
        }
        if (1 == this.alermBean.getMotion_armed() || 1 == this.alermBean.getInput_armed() || this.alermBean.getAudioArmedCheck() == 1 || this.alermBean.getAlarmTempChecked() == 1) {
            this.eventView.setVisibility(0);
        } else {
            this.eventView.setVisibility(8);
        }
    }

    private void getDataFromOther() {
        Intent intent = getIntent();
        this.strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        this.cameraType = intent.getIntExtra(ContentCommon.STR_CAMERA_TYPE, 0);
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        dismissPopupWindow();
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        dismissPopupWindow();
        return false;
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.AlarmInterface
    public void callBackAlarmParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, int i31, int i32, int i33, int i34) {
        this.alermBean.setDid(str);
        this.alermBean.setMotion_armed(i2);
        this.alermBean.setMotion_sensitivity(i3);
        this.alermBean.setInput_armed(i4);
        this.alermBean.setIoin_level(i5);
        this.alermBean.setIolinkage(i6);
        this.alermBean.setIoout_level(i7);
        this.alermBean.setAlermpresetsit(i8);
        this.alermBean.setMail(i9);
        this.alermBean.setSnapshot(i10);
        this.alermBean.setRecord(i11);
        this.alermBean.setUpload_interval(i12);
        this.alermBean.setAlarm_audio(i);
        this.alermBean.setAlarm_temp(i4);
        this.alermBean.setSchedule_enable(i13);
        this.mHandler.sendEmptyMessage(3);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.AlarmInterface
    public void callBackSetSystemParamsResult(String str, int i, int i2) {
        this.mHandler.sendEmptyMessage(i2);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        dismissPopupWindow();
        return false;
    }

    private void dismissPopupWindow() {
        if (this.presteMovePopWindow != null && this.presteMovePopWindow.isShowing()) {
            this.presteMovePopWindow.dismiss();
        }
        if (this.sensitivePopWindow != null && this.sensitivePopWindow.isShowing()) {
            this.sensitivePopWindow.dismiss();
        }
        if (this.triggerLevelPopWindow != null && this.triggerLevelPopWindow.isShowing()) {
            this.triggerLevelPopWindow.dismiss();
        }
        if (this.ioOutLevelPopWindow != null && this.ioOutLevelPopWindow.isShowing()) {
            this.ioOutLevelPopWindow.dismiss();
        }
        if (this.audioPopWindow == null || !this.audioPopWindow.isShowing()) {
            return;
        }
        this.audioPopWindow.dismiss();
    }

    private void initPopupWindow() {
        initAudioPopupWindow();
        initMovePopupWindow();
        initInputPopupWindow();
        initIOlinkMovePopupWindow();
        initPresetPopupWindow();
    }

    private void initPresetPopupWindow() {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.alermpresetmovepopwindow, (ViewGroup) null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.preset_no);
        TextView textView2 = (TextView) linearLayout.findViewById(R.id.preset_1);
        TextView textView3 = (TextView) linearLayout.findViewById(R.id.preset_2);
        TextView textView4 = (TextView) linearLayout.findViewById(R.id.preset_3);
        TextView textView5 = (TextView) linearLayout.findViewById(R.id.preset_4);
        TextView textView6 = (TextView) linearLayout.findViewById(R.id.preset_5);
        TextView textView7 = (TextView) linearLayout.findViewById(R.id.preset_6);
        TextView textView8 = (TextView) linearLayout.findViewById(R.id.preset_7);
        TextView textView9 = (TextView) linearLayout.findViewById(R.id.preset_8);
        TextView textView10 = (TextView) linearLayout.findViewById(R.id.preset_9);
        TextView textView11 = (TextView) linearLayout.findViewById(R.id.preset_10);
        TextView textView12 = (TextView) linearLayout.findViewById(R.id.preset_11);
        TextView textView13 = (TextView) linearLayout.findViewById(R.id.preset_12);
        TextView textView14 = (TextView) linearLayout.findViewById(R.id.preset_13);
        TextView textView15 = (TextView) linearLayout.findViewById(R.id.preset_14);
        TextView textView16 = (TextView) linearLayout.findViewById(R.id.preset_15);
        TextView textView17 = (TextView) linearLayout.findViewById(R.id.preset_16);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        textView7.setOnClickListener(this);
        textView8.setOnClickListener(this);
        textView9.setOnClickListener(this);
        textView10.setOnClickListener(this);
        textView11.setOnClickListener(this);
        textView12.setOnClickListener(this);
        textView13.setOnClickListener(this);
        textView14.setOnClickListener(this);
        textView15.setOnClickListener(this);
        textView16.setOnClickListener(this);
        textView17.setOnClickListener(this);
        this.presteMovePopWindow = new PopupWindow(linearLayout, 160, -2);
    }

    private void initIOlinkMovePopupWindow() {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.alermiooutpopwindow, (ViewGroup) null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.ioout_hight);
        TextView textView2 = (TextView) linearLayout.findViewById(R.id.ioout_low);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        this.ioOutLevelPopWindow = new PopupWindow(linearLayout, 160, -2);
    }

    private void initMovePopupWindow() {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.alermsensitivepopwindow, (ViewGroup) null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.sensitive_10);
        TextView textView2 = (TextView) linearLayout.findViewById(R.id.sensitive_9);
        TextView textView3 = (TextView) linearLayout.findViewById(R.id.sensitive_8);
        TextView textView4 = (TextView) linearLayout.findViewById(R.id.sensitive_7);
        TextView textView5 = (TextView) linearLayout.findViewById(R.id.sensitive_6);
        TextView textView6 = (TextView) linearLayout.findViewById(R.id.sensitive_5);
        TextView textView7 = (TextView) linearLayout.findViewById(R.id.sensitive_4);
        TextView textView8 = (TextView) linearLayout.findViewById(R.id.sensitive_3);
        TextView textView9 = (TextView) linearLayout.findViewById(R.id.sensitive_2);
        TextView textView10 = (TextView) linearLayout.findViewById(R.id.sensitive_1);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView4.setOnClickListener(this);
        textView5.setOnClickListener(this);
        textView6.setOnClickListener(this);
        textView7.setOnClickListener(this);
        textView8.setOnClickListener(this);
        textView9.setOnClickListener(this);
        textView10.setOnClickListener(this);
        this.sensitivePopWindow = new PopupWindow(linearLayout, 160, -2);
    }

    private void initInputPopupWindow() {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.alermtriggerpopwindow, (ViewGroup) null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.trigger_hight);
        ((TextView) linearLayout.findViewById(R.id.trigger_low)).setOnClickListener(this);
        textView.setOnClickListener(this);
        this.triggerLevelPopWindow = new PopupWindow(linearLayout, 160, -2);
    }

    private void initAudioPopupWindow() {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.alermaudiopopwindow, (ViewGroup) null);
        TextView textView = (TextView) linearLayout.findViewById(R.id.trigger_audio_levelhigh);
        TextView textView2 = (TextView) linearLayout.findViewById(R.id.trigger_audio_levelmiddle);
        TextView textView3 = (TextView) linearLayout.findViewById(R.id.trigger_audio_levellow);
        TextView textView4 = (TextView) linearLayout.findViewById(R.id.trigger_audio_levelforbid);
        textView.setOnClickListener(this);
        textView3.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView4.setOnClickListener(this);
        this.audioPopWindow = new PopupWindow(linearLayout, 160, -2);
    }
}
