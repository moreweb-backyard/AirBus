package com.pzj.ipcdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.utils.SensorDoorData;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class EditSensorActivity extends Activity implements View.OnClickListener, BridgeService.EditSensorListActivityInterface, AdapterView.OnItemClickListener {
    private ImageView btnBack;
    private Button btnDelete;
    private TextView btnOK;
    private String did;
    private EditText et_sensorname;
    private int id;
    Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.EditSensorActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String string = message.getData().getString(DatabaseUtil.KEY_ID);
            int i = message.getData().getInt(DatabaseUtil.KEY_TYPE);
            String string2 = message.getData().getString(DatabaseUtil.KEY_NAME);
            message.getData().getInt("setid");
            if (i == 1) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_door));
            } else if (i == 2) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_infrared));
            } else if (i == 3) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_smoke));
            } else if (i == 4) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_gas));
            } else if (i == 7) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_remote));
            } else if (i == 8) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_siren));
            } else if (i == 10) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_camera));
            } else if (i == 11) {
                EditSensorActivity.this.tv_sensortype.setText(EditSensorActivity.this.getString(R.string.sensor_type_curtain));
            }
            EditSensorActivity.this.tv_sensorid.setText(string);
            EditSensorActivity.this.et_sensorname.setText(string2);
            EditSensorActivity.this.et_sensorname.setSelectAllOnFocus(true);
        }
    };
    private String position;
    private int presetid;
    private ProgressDialog progressDialog;
    private String pwd;
    private String sensorid;
    private String sensorname;
    private String sensortag;
    private int sensortype;
    private TextView tv_back;
    private TextView tv_sensorid;
    private TextView tv_sensorname;
    private TextView tv_sensortype;

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.edit_sensor);
        findview();
        getData();
        BridgeService.setSensornameInterface(this);
    }

    private void findview() {
        this.tv_back = (TextView) findViewById(R.id.tv_back);
        this.tv_back.setOnClickListener(this);
        this.tv_sensorid = (TextView) findViewById(R.id.sensorid);
        this.tv_sensortype = (TextView) findViewById(R.id.sensortype);
        this.et_sensorname = (EditText) findViewById(R.id.sensorname);
        this.btnOK = (TextView) findViewById(R.id.edit_ok);
        this.btnOK.setOnClickListener(this);
        this.btnBack = (ImageView) findViewById(R.id.back);
        this.btnBack.setOnClickListener(this);
        this.btnDelete = (Button) findViewById(R.id.btn_delete);
        this.btnDelete.setOnClickListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        this.id = intent.getIntExtra(DatabaseUtil.KEY_ID, -1);
        this.sensorid = intent.getStringExtra("sensorid1_list");
        this.sensortype = intent.getIntExtra("sensortype", -200);
        this.sensorname = intent.getStringExtra("sensorname");
        this.presetid = intent.getIntExtra("presetid", -200);
        this.position = intent.getStringExtra(DatabaseUtil.KEY_POSITION);
        this.did = intent.getStringExtra(DatabaseUtil.KEY_DID);
        this.pwd = intent.getStringExtra(DatabaseUtil.KEY_PWD);
        this.sensortag = intent.getStringExtra("sensortag");
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(DatabaseUtil.KEY_ID, this.sensorid);
        bundle.putInt(DatabaseUtil.KEY_TYPE, this.sensortype);
        bundle.putString(DatabaseUtil.KEY_NAME, this.sensorname);
        bundle.putInt("setid", this.presetid);
        message.setData(bundle);
        this.mHandler.sendMessage(message);
    }

    private void setSensorName(String str) {
        NativeCaller.TransferMessage(this.did, "set_sensorname.cgi?&sensorid=" + this.id + "&sensorname=" + str + "&loginuse=admin&loginpas=" + this.pwd, 1);
        showDia();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.edit_ok) {
            String strTrim = this.et_sensorname.getText().toString().trim();
            if (!strTrim.equals(this.sensorname)) {
                setSensorName(strTrim);
                return;
            } else {
                finish();
                return;
            }
        }
        if (id == R.id.back) {
            finish();
            return;
        }
        if (id == R.id.tv_back) {
            finish();
            return;
        }
        if (id == R.id.btn_delete) {
            Log.i("info", "delete sensor");
            NativeCaller.TransferMessage(this.did, "del_sensor.cgi?&sensorid=" + this.id + "&loginuse=admin&loginpas=" + this.pwd + "&user=admin&pwd=" + this.pwd, 1);
            showDia();
        }
    }

    private void setPreSetMessage(int i) {
        NativeCaller.TransferMessage(this.did, "set_sensor_preset.cgi?sensorid=" + this.id + "&presetid=" + i + "&sensorid=" + this.sensorid + "&loginuse=admin&loginpas=" + this.pwd, 1);
    }

    public void showDia() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage("正在操作，请稍后...");
        this.progressDialog.show();
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.EditSensorListActivityInterface
    public void CallBackMessages(String str, String str2, int i) {
        Log.e("info", "EditSensorActivity callback" + i);
        if (i == 24756) {
            Log.i("info", "CGI_SET_SENSOR_NAME" + str2);
            String strSubstring = str2.substring(str2.indexOf(Consts.EQUALS) + 1, str2.indexOf(";"));
            Log.e("info", "EditSensorActivity isSucess:" + strSubstring);
            if (strSubstring.contains("0")) {
                if (this.progressDialog.isShowing()) {
                    this.progressDialog.dismiss();
                }
                Intent intent = new Intent();
                String strTrim = this.et_sensorname.getText().toString().trim();
                intent.putExtra(DatabaseUtil.KEY_NAME, strTrim);
                intent.putExtra(DatabaseUtil.KEY_POSITION, this.position);
                SensorDoorData.ChangeDoorName(str, this.sensortag, strTrim);
                setResult(501, intent);
                finish();
            } else {
                Toast.makeText(this, "修改失败!", 1).show();
            }
        }
        if (i == 24755) {
            Log.i("info", "CGI_DEL_SENSOR" + str2);
            String strSubstring2 = str2.substring(str2.indexOf(Consts.EQUALS) + 1, str2.indexOf(";"));
            Log.i("info", "EditSensorActivity isSucess:" + strSubstring2);
            if (strSubstring2.contains("0")) {
                Log.i("info", "delete----------0");
                if (this.progressDialog.isShowing()) {
                    this.progressDialog.dismiss();
                }
                Intent intent2 = new Intent();
                intent2.putExtra(DatabaseUtil.KEY_POSITION, this.position);
                setResult(502, intent2);
                SensorDoorData.removeSnesorDoor(str, this.sensortag);
                finish();
            } else {
                Toast.makeText(this, "删除失败!", 1).show();
            }
        }
        if (i == 24759) {
            Log.i("info", "CGI_SET_SENSOR_PRESET" + str2);
        }
    }
}
