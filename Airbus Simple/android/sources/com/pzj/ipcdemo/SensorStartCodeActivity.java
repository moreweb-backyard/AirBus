package com.pzj.ipcdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import java.util.Timer;
import java.util.TimerTask;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SensorStartCodeActivity extends Activity implements View.OnClickListener, BridgeService.SensorSetCodeInterface {
    private Button add_sensor_ok;
    private Button cancleCode;
    private RelativeLayout cancle_layout;
    private ImageView code_imageview_type;
    private LinearLayout code_sucess_layout;
    private String did;
    private int id1;
    private int id2;
    private int id3;
    private String pwd;
    private EditText sensor_name_edit;
    private String sensortype;
    private ImageView sucess_code_imageview;
    private TextView tv_note;
    public int type;
    private Intent backdata = null;
    private Timer timer = null;
    private TimerTask timerTask = null;
    private Handler myHandler = new Handler() { // from class: com.pzj.ipcdemo.SensorStartCodeActivity.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            String str = (String) message.obj;
            if (i == 1) {
                SensorStartCodeActivity.this.code_imageview_type.setBackgroundResource(R.drawable.magnetic);
            } else if (i == 3) {
                SensorStartCodeActivity.this.code_imageview_type.setBackgroundResource(R.drawable.somke);
            } else if (i == 4) {
                SensorStartCodeActivity.this.code_imageview_type.setBackgroundResource(R.drawable.gas);
            } else if (i == 7) {
                SensorStartCodeActivity.this.code_imageview_type.setBackgroundResource(R.drawable.control);
            } else if (i == 8) {
                SensorStartCodeActivity.this.code_imageview_type.setBackgroundResource(R.drawable.sensor_siren_icon);
            } else {
                SensorStartCodeActivity.this.code_imageview_type.setBackgroundResource(R.drawable.infrared);
            }
            SensorStartCodeActivity.this.sensor_name_edit.setText(str);
            SensorStartCodeActivity.this.sucess_code_imageview.setVisibility(0);
            SensorStartCodeActivity.this.cancle_layout.setVisibility(8);
            SensorStartCodeActivity.this.code_sucess_layout.setVisibility(0);
        }
    };

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.startcode_layout);
        getData();
        findview();
        this.backdata = new Intent();
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
    }

    private void getData() {
        Intent intent = getIntent();
        this.sensortype = intent.getStringExtra(DatabaseUtil.KEY_TYPE);
        this.did = intent.getStringExtra(DatabaseUtil.KEY_DID);
        this.pwd = intent.getStringExtra(DatabaseUtil.KEY_PWD);
        NativeCaller.TransferMessage(this.did, "set_sensorstatus.cgi?cmd=2&loginuse=admin&loginpas=" + this.pwd + "&user=admin&pwd=" + this.pwd, 1);
        NativeCaller.TransferMessage(this.did, "trans_cmd_string.cgi?loginuse=admin&loginpas=" + this.pwd + "&user=admin&pwd=" + this.pwd + "&cmd=2002&sensor_type=" + this.sensortype, 1);
        startTimer();
        BridgeService.setCodeInterface(this);
    }

    private void findview() {
        this.code_imageview_type = (ImageView) findViewById(R.id.code_imageview_type);
        this.cancle_layout = (RelativeLayout) findViewById(R.id.cancle_code_layout);
        this.code_sucess_layout = (LinearLayout) findViewById(R.id.code_sucess_layout);
        this.sensor_name_edit = (EditText) findViewById(R.id.sensor_name_edit);
        this.add_sensor_ok = (Button) findViewById(R.id.add_sensor_ok);
        this.add_sensor_ok.setOnClickListener(this);
        this.tv_note = (TextView) findViewById(R.id.note);
        this.cancleCode = (Button) findViewById(R.id.cancel_code);
        this.cancleCode.setOnClickListener(this);
        this.sucess_code_imageview = (ImageView) findViewById(R.id.sucess_code);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cancel_code) {
            System.out.println("Complete the alignment, exit the alignment、、、");
            closeTimer(0);
        } else if (id == R.id.add_sensor_ok) {
            goBack();
        }
    }

    private void goBack() {
        String strTrim = this.sensor_name_edit.getText().toString().trim();
        if (strTrim.equals("")) {
            strTrim = getNullSensorName(this.id1, this.id2, this.id3, this.type);
        }
        this.backdata.putExtra("sensorname", strTrim);
        setResult(2013, this.backdata);
        finish();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            closeTimer(0);
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }

    private void startTimer() {
        if (this.timerTask == null) {
            this.timerTask = new TimerTask() { // from class: com.pzj.ipcdemo.SensorStartCodeActivity.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    System.out.println("=---------------");
                    SensorStartCodeActivity.this.closeTimer(0);
                }
            };
            this.timer = new Timer();
            System.out.println("对码中、、、");
            this.timer.schedule(this.timerTask, 30000L);
        }
    }

    private void reStart() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        if (this.timerTask != null) {
            this.timerTask = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeTimer(int i) {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        if (this.timerTask != null) {
            this.timerTask = null;
        }
        System.out.println("时间到对码完成，退出对码、、、");
        stopCode();
        if (i == 0) {
            finish();
        }
    }

    public void stopCode() {
        NativeCaller.TransferMessage(this.did, "set_sensorstatus.cgi?cmd=3&loginuse=admin&loginpas=" + this.pwd + "&user=admin&pwd=" + this.pwd, 1);
    }

    private String getNullSensorName(int i, int i2, int i3, int i4) {
        String string;
        if (i4 == 1) {
            string = getString(R.string.sensor_type_door);
        } else if (i4 == 2) {
            string = getString(R.string.sensor_type_infrared);
        } else if (i4 == 3) {
            string = getString(R.string.sensor_type_smoke);
        } else if (i4 == 4) {
            string = getString(R.string.sensor_type_gas);
        } else if (i4 == 7) {
            string = getString(R.string.sensor_type_remote);
        } else if (i4 == 10) {
            string = getString(R.string.sensor_type_camera);
        } else if (i4 == 11) {
            string = getString(R.string.sensor_type_curtain);
        } else {
            string = i4 == 8 ? getString(R.string.sensor_type_siren) : null;
        }
        return string + "-" + (changeTo16(i) + changeTo16(i2) + changeTo16(i3));
    }

    public String changeTo16(int i) {
        String upperCase = Integer.toHexString(i).toUpperCase();
        if (i >= 10) {
            return upperCase;
        }
        return "0" + upperCase;
    }

    private void setSensorName(String str, int i) {
        Log.i("info", "list 设置默认名称id:" + i);
        NativeCaller.TransferMessage(this.did, "set_sensorname.cgi?&sensorid=" + i + "&sensorname=" + str + "&loginuse=admin&loginpas=" + this.pwd, 1);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.SensorSetCodeInterface
    public void CallBackReCodeMessage(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        if (i7 == 0 && i8 == 0 && i9 == 0) {
            return;
        }
        if (i7 == 255 && i8 == 255 && i9 == 255) {
            return;
        }
        Log.i("info", "SensorStartCodeActivity:" + str);
        System.out.println("重新对码返回---------------!" + str + ",id1" + i7);
        closeTimer(1);
        Log.i("info", "对码返回数据都不为空，有有效的ID");
        if (str2.trim().length() == 0 || str2.trim().equals("")) {
            Log.i("info", "对码返回的名称为空值，设置为默认值");
            str2 = getNullSensorName(i7, i8, i9, i4);
            Log.i("info", "1---new name:" + str2);
        }
        Message message = new Message();
        message.what = i4;
        message.obj = str2;
        this.myHandler.sendMessage(message);
        this.id1 = i7;
        this.id2 = i8;
        this.id3 = i9;
        this.type = i4;
        this.backdata.putExtra("linkpreset", i3);
        this.backdata.putExtra("sensorid1", i7);
        this.backdata.putExtra("sensorid2", i8);
        this.backdata.putExtra("sensorid3", i9);
        this.backdata.putExtra("sensortype", i4);
        this.backdata.putExtra("sensorname", str2);
        this.backdata.putExtra(DatabaseUtil.KEY_ID, i6);
    }
}
