package com.pzj.ipcdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.adapter.BindSensorListAdapter;
import com.pzj.ipcdemo.adapter.SensorListAdapter;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SensorListActivty extends Activity implements View.OnClickListener, BridgeService.SensorSetCodeInterface, BridgeService.SensorListActivityAllDataInterface, AdapterView.OnItemClickListener {
    public BindSensorListAdapter adapter;
    private LinearLayout existssensor_layout;
    private String pwd;
    private ImageView singleline_top;
    private String strDid;
    private TextView tv_back;
    private SensorListAdapter typeadapter;
    private ArrayList<Map<String, String>> typelistItems;
    public ListView sensorList = null;
    public ListView sensortypelist = null;
    public ArrayList<Map<String, Object>> listItems = new ArrayList<>();
    Handler Msghandler = new Handler() { // from class: com.pzj.ipcdemo.SensorListActivty.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    SensorListActivty.this.existssensor_layout.setVisibility(0);
                    SensorListActivty.this.singleline_top.setVisibility(0);
                    Bundle data = message.getData();
                    int i = data.getInt("sensorid1", -1);
                    int i2 = data.getInt("sensorid2", -1);
                    int i3 = data.getInt("sensorid3", -1);
                    int i4 = data.getInt(DatabaseUtil.KEY_ID, -1);
                    SensorListActivty.this.adapter.addSensorToList(i, i2, i3, data.getInt("sensortype", -1), data.getString("sensorname"), data.getInt("isnew", -1), i4);
                    SensorListActivty.this.adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Bundle data2 = message.getData();
                    SensorListActivty.this.adapter.changeSensorName(data2.getString(DatabaseUtil.KEY_NAME), data2.getInt("pos"));
                    SensorListActivty.this.adapter.notifyDataSetChanged();
                    break;
                case 3:
                    int i5 = message.getData().getInt("pos");
                    SensorListActivty.this.adapter.removeMylist(i5);
                    Log.i("info", "remove:" + i5);
                    SensorListActivty.this.adapter.notifyDataSetChanged();
                    break;
                case 4:
                    SensorListActivty.this.existssensor_layout.setVisibility(8);
                    SensorListActivty.this.singleline_top.setVisibility(8);
                    break;
            }
        }
    };
    public Handler newHandler = new Handler() { // from class: com.pzj.ipcdemo.SensorListActivty.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            SensorListActivty.this.adapter.changeSensorNew(message.arg1);
            SensorListActivty.this.adapter.notifyDataSetChanged();
        }
    };
    private Timer timer = null;
    private TimerTask timerTask = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.settingsensorlist);
        BridgeService.setSensorListInterface(this);
        getDate();
        findView();
        this.adapter = new BindSensorListAdapter(this);
        this.sensorList.setAdapter((ListAdapter) this.adapter);
    }

    private void getDate() {
        Intent intent = getIntent();
        this.strDid = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        this.pwd = intent.getStringExtra(ContentCommon.STR_CAMERA_PWD);
        NativeCaller.TransferMessage(this.strDid, "get_sensorlist.cgi?loginuse=admin&loginpas=" + this.pwd + "&user=admin&pwd=" + this.pwd, 1);
    }

    private void findView() {
        this.existssensor_layout = (LinearLayout) findViewById(R.id.layout1);
        this.singleline_top = (ImageView) findViewById(R.id.singleline_top);
        this.tv_back = (TextView) findViewById(R.id.tv_back);
        this.tv_back.setOnClickListener(this);
        this.sensorList = (ListView) findViewById(R.id.camera_list);
        this.sensorList.setCacheColorHint(0);
        this.sensorList.setOnItemClickListener(this);
        this.sensortypelist = (ListView) findViewById(R.id.sensortype_list);
        this.sensortypelist.setCacheColorHint(0);
        this.typelistItems = new ArrayList<>();
        HashMap map = new HashMap();
        map.put("sensortype", "1");
        this.typelistItems.add(map);
        HashMap map2 = new HashMap();
        map2.put("sensortype", "7");
        this.typelistItems.add(map2);
        HashMap map3 = new HashMap();
        map3.put("sensortype", "3");
        this.typelistItems.add(map3);
        HashMap map4 = new HashMap();
        map4.put("sensortype", "4");
        this.typelistItems.add(map4);
        HashMap map5 = new HashMap();
        map5.put("sensortype", "2");
        this.typelistItems.add(map5);
        HashMap map6 = new HashMap();
        map6.put("sensortype", "8");
        this.typelistItems.add(map6);
        this.typeadapter = new SensorListAdapter(getApplicationContext(), this.typelistItems);
        this.sensortypelist.setAdapter((ListAdapter) this.typeadapter);
        this.sensortypelist.setOnItemClickListener(new sensortypeListAdapter());
    }

    class sensortypeListAdapter implements AdapterView.OnItemClickListener {
        sensortypeListAdapter() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            String str = (String) ((Map) SensorListActivty.this.typelistItems.get(i)).get("sensortype");
            Intent intent = new Intent(SensorListActivty.this, (Class<?>) SensorStartCodeActivity.class);
            intent.putExtra(DatabaseUtil.KEY_TYPE, str);
            intent.putExtra(DatabaseUtil.KEY_DID, SensorListActivty.this.strDid);
            intent.putExtra(DatabaseUtil.KEY_PWD, SensorListActivty.this.pwd);
            SensorListActivty.this.startActivityForResult(intent, 2014);
        }
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
        } else if (i4 == 7 || i4 == 7) {
            string = getString(R.string.sensor_type_remote);
        } else if (i4 == 10) {
            string = getString(R.string.sensor_type_camera);
        } else {
            string = i4 == 11 ? getString(R.string.sensor_type_curtain) : null;
        }
        return string + "-" + (this.adapter.changeTo16(i) + this.adapter.changeTo16(i2) + this.adapter.changeTo16(i3));
    }

    private void setSensorName(String str, int i) {
        NativeCaller.TransferMessage(this.strDid, "set_sensorname.cgi?&sensorid=" + i + "&sensorname=" + str + "&loginuse=admin&loginpas=" + this.pwd, 1);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.tv_back) {
            finish();
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Map<String, Object> map = this.listItems.get(i);
        String strBuildID = BuildID(i);
        String strSensorTag = SensorTag(i);
        int iIntValue = ((Integer) map.get("sensortype")).intValue();
        String str = (String) map.get("sensorname");
        int iIntValue2 = ((Integer) map.get("presetid")).intValue();
        int iIntValue3 = ((Integer) map.get(DatabaseUtil.KEY_ID)).intValue();
        Intent intent = new Intent(this, (Class<?>) EditSensorActivity.class);
        intent.putExtra("sensorid1_list", strBuildID);
        intent.putExtra("sensortype", iIntValue);
        intent.putExtra("sensorname", str);
        intent.putExtra("presetid", iIntValue2);
        intent.putExtra(DatabaseUtil.KEY_DID, this.strDid);
        intent.putExtra(DatabaseUtil.KEY_PWD, this.pwd);
        intent.putExtra(DatabaseUtil.KEY_ID, iIntValue3);
        intent.putExtra(DatabaseUtil.KEY_POSITION, i + "");
        intent.putExtra("sensortag", strSensorTag);
        startActivityForResult(intent, 500);
        Message message = new Message();
        message.arg1 = i;
        this.newHandler.sendMessage(message);
    }

    private String SensorTag(int i) {
        Map<String, Object> map = this.listItems.get(i);
        return ((Integer) map.get("sensorid1")).intValue() + "" + ((Integer) map.get("sensorid2")).intValue() + ((Integer) map.get("sensorid3")).intValue();
    }

    private String BuildID(int i) {
        Map<String, Object> map = this.listItems.get(i);
        int iIntValue = ((Integer) map.get("sensorid1")).intValue();
        int iIntValue2 = ((Integer) map.get("sensorid2")).intValue();
        int iIntValue3 = ((Integer) map.get("sensorid3")).intValue();
        Log.i("info", "id1:" + iIntValue + "--id2:" + iIntValue2 + "--id3;" + iIntValue3);
        return this.adapter.changeTo16(iIntValue) + this.adapter.changeTo16(iIntValue2) + this.adapter.changeTo16(iIntValue3);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) throws NumberFormatException {
        super.onActivityResult(i, i2, intent);
        if (i2 == 501) {
            Log.i("info", "501");
            String stringExtra = intent.getStringExtra(DatabaseUtil.KEY_NAME);
            int i3 = Integer.parseInt(intent.getStringExtra(DatabaseUtil.KEY_POSITION));
            Message message = new Message();
            message.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString(DatabaseUtil.KEY_NAME, stringExtra);
            bundle.putInt("pos", i3);
            message.setData(bundle);
            this.Msghandler.sendMessage(message);
            this.listItems.get(i3).put("sensorname", stringExtra);
        }
        if (i2 == 502) {
            Log.i("info", "502");
            int i4 = Integer.parseInt(intent.getStringExtra(DatabaseUtil.KEY_POSITION));
            Message message2 = new Message();
            message2.what = 3;
            Bundle bundle2 = new Bundle();
            bundle2.putInt("pos", i4);
            message2.setData(bundle2);
            this.Msghandler.sendMessage(message2);
            this.listItems.remove(i4);
            if (this.listItems.size() == 0) {
                this.Msghandler.sendEmptyMessage(4);
            }
        }
        if (i2 == 2013) {
            int intExtra = intent.getIntExtra("sensorid1", -1);
            int intExtra2 = intent.getIntExtra("sensorid2", -1);
            int intExtra3 = intent.getIntExtra("sensorid3", -1);
            if (CheckHaveSame(intExtra, intExtra2, intExtra3)) {
                return;
            }
            int intExtra4 = intent.getIntExtra("sensortype", -1);
            int intExtra5 = intent.getIntExtra("linkpreset", -1);
            String stringExtra2 = intent.getStringExtra("sensorname");
            int intExtra6 = intent.getIntExtra(DatabaseUtil.KEY_ID, -1);
            Log.i("tag", "对码界面返回" + intExtra + Consts.SECOND_LEVEL_SPLIT + intExtra2 + Consts.SECOND_LEVEL_SPLIT + intExtra3 + ",sensortype" + intExtra4 + ",sensorname" + stringExtra2);
            HashMap map = new HashMap();
            map.put("presetid", Integer.valueOf(intExtra5));
            map.put("sensortype", Integer.valueOf(intExtra4));
            map.put("sensorid3", Integer.valueOf(intExtra3));
            map.put("sensorid2", Integer.valueOf(intExtra2));
            map.put("sensorid1", Integer.valueOf(intExtra));
            map.put("sensorname", stringExtra2);
            map.put(DatabaseUtil.KEY_ID, Integer.valueOf(intExtra6));
            this.listItems.add(map);
            Message message3 = new Message();
            Bundle bundle3 = new Bundle();
            bundle3.putInt("sensorid1", intExtra);
            bundle3.putInt("sensorid2", intExtra2);
            bundle3.putInt("sensorid3", intExtra3);
            bundle3.putInt("sensortype", intExtra4);
            bundle3.putString("sensorname", stringExtra2);
            Log.i("info", "3----new name:" + stringExtra2);
            bundle3.putInt(DatabaseUtil.KEY_ID, intExtra6);
            message3.what = 1;
            message3.setData(bundle3);
            this.Msghandler.sendMessage(message3);
            setSensorName(stringExtra2, intExtra6);
            if (intExtra4 != 1) {
                Log.i("info", "不是门磁 无需处理");
            }
        }
    }

    public boolean CheckHaveSame(int i, int i2, int i3) {
        int size = this.listItems.size();
        for (int i4 = 0; i4 < size; i4++) {
            Map<String, Object> map = this.listItems.get(i4);
            if (((Integer) map.get("sensorid1")).intValue() == i && ((Integer) map.get("sensorid2")).intValue() == i2 && ((Integer) map.get("sensorid3")).intValue() == i3) {
                return true;
            }
        }
        return false;
    }

    private void startTimer() {
        if (this.timerTask == null) {
            this.timerTask = new TimerTask() { // from class: com.pzj.ipcdemo.SensorListActivty.3
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    System.out.println("=---------------");
                    SensorListActivty.this.closeTimer();
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
    public void closeTimer() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        if (this.timerTask != null) {
            this.timerTask = null;
        }
        System.out.println("对码完成，退出对码、、、");
        NativeCaller.TransferMessage(this.strDid, "set_sensorstatus.cgi?cmd=3&loginuse=admin&loginpas=" + this.pwd + "&user=admin&pwd=" + this.pwd, 1);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.SensorSetCodeInterface
    public void CallBackReCodeMessage(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        Log.e("重新对码返回", str2 + "--" + i + "--" + i2 + "--" + i3 + "--" + i4 + "--" + i5 + "--" + i6 + "--" + i7 + "--" + i8 + "--" + i9);
        if (i7 == 0 && i8 == 0 && i9 == 0) {
            return;
        }
        if (i7 == 255 && i8 == 255 && i9 == 255) {
            return;
        }
        reStart();
        startTimer();
        if (str2.trim().length() == 0 || str2.trim().equals("")) {
            str2 = getNullSensorName(i7, i8, i9, i4);
            setSensorName(str2, i6);
        }
        HashMap map = new HashMap();
        map.put("presetid", Integer.valueOf(i3));
        map.put("sensortype", Integer.valueOf(i4));
        map.put("sensorid3", Integer.valueOf(i9));
        map.put("sensorid2", Integer.valueOf(i8));
        map.put("sensorid1", Integer.valueOf(i7));
        map.put("sensorname", str2);
        map.put(DatabaseUtil.KEY_ID, Integer.valueOf(i6));
        this.listItems.add(map);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("sensorid1", i7);
        bundle.putInt("sensorid2", i8);
        bundle.putInt("sensorid3", i9);
        bundle.putInt("sensortype", i4);
        bundle.putString("sensorname", str2);
        Log.i("info", "3----new name:" + str2);
        bundle.putInt(DatabaseUtil.KEY_ID, i6);
        bundle.putInt("isnew", 1);
        message.what = 1;
        message.setData(bundle);
        this.Msghandler.sendMessage(message);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.SensorListActivityAllDataInterface
    public void CallBackMessage(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Log.e("添加的传感器", str2 + "--" + i + "--" + i2 + "--" + i3 + "--" + i4 + "--" + i6 + "--" + i7 + "--" + i8);
        if (i2 == 0 && i3 == 0 && i4 == 0) {
            return;
        }
        if (i2 == 255 && i3 == 255 && i4 == 255) {
            return;
        }
        HashMap map = new HashMap();
        map.put("presetid", Integer.valueOf(i7));
        map.put("sensorstatus", Integer.valueOf(i6));
        map.put("sensortype", Integer.valueOf(i5));
        map.put("sensorid3", Integer.valueOf(i4));
        map.put("sensorid2", Integer.valueOf(i3));
        map.put("sensorid1", Integer.valueOf(i2));
        map.put("sensorname", str2);
        map.put(DatabaseUtil.KEY_ID, Integer.valueOf(i8));
        this.listItems.add(map);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("sensorid1", i2);
        bundle.putInt("sensorid2", i3);
        bundle.putInt("sensorid3", i4);
        bundle.putInt("sensortype", i5);
        bundle.putString("sensorname", str2);
        bundle.putInt(DatabaseUtil.KEY_ID, i8);
        message.what = 1;
        message.setData(bundle);
        this.Msghandler.sendMessage(message);
    }
}
