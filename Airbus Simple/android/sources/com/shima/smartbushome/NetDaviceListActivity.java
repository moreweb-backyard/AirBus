package com.shima.smartbushome;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.SmartG4DeviceType;
import com.shima.smartbushome.udp.udp_socket;
import com.shima.smartbushome.util.SystemUIUtil;
import java.util.HashMap;

/* loaded from: classes.dex */
public class NetDaviceListActivity extends AppCompatActivity {
    ListView lv;
    private DeviceListAdapter mLeDeviceListAdapter;
    ProgressDialog progress;
    SmartG4DeviceType sdt;
    Handler runtime = new Handler();
    int time = 0;
    Runnable sentrun = new Runnable() { // from class: com.shima.smartbushome.NetDaviceListActivity.1
        @Override // java.lang.Runnable
        public void run() {
            NetDaviceListActivity.this.time++;
            NetDaviceListActivity.this.progress.setProgress(NetDaviceListActivity.this.time * 20);
            if (NetDaviceListActivity.this.time == 5) {
                NetDaviceListActivity.this.progress.dismiss();
                NetDaviceListActivity.this.runtime.removeCallbacks(NetDaviceListActivity.this.sentrun);
            } else {
                NetDaviceListActivity.this.getdevice();
                NetDaviceListActivity.this.runtime.postDelayed(NetDaviceListActivity.this.sentrun, 1000L);
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.NetDaviceListActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (udp_socket.ACTION_DATA_IN.equals(intent.getAction())) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 20) {
                    NetDaviceListActivity.this.RunReceiveData(byteArrayExtra);
                }
            }
        }
    };

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle("DeviceList");
        setContentView(R.layout.activity_net_davice_list);
        SystemUIUtil.setSystemUIVisible(this, false);
        this.lv = (ListView) findViewById(R.id.listView3);
        this.progress = new ProgressDialog(this);
        this.progress.setCancelable(true);
        this.progress.setCanceledOnTouchOutside(false);
        this.progress.setMessage("Getting Device...");
        this.progress.setProgressStyle(1);
        this.progress.show();
        if (MainActivity.netdeviceList.size() != 0) {
            MainActivity.netdeviceList.clear();
        }
        this.mLeDeviceListAdapter = new DeviceListAdapter(this, MainActivity.netdeviceList);
        this.lv.setAdapter((ListAdapter) this.mLeDeviceListAdapter);
        getdevice();
        this.sdt = new SmartG4DeviceType();
        this.sdt.initdevicetype();
        this.runtime.postDelayed(this.sentrun, 2000L);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        registerReceiver(this.mGattUpdateReceiver, makeUpdateIntentFilter());
        SystemUIUtil.setSystemUIVisible(this, false);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        unregisterReceiver(this.mGattUpdateReceiver);
    }

    public void save(View view) {
        Toast.makeText(this, "save succeed", 0).show();
        finish();
    }

    public void exit(View view) {
        MainActivity.netdeviceList.clear();
        finish();
    }

    public void RunReceiveData(byte[] bArr) {
        if (bArr[21] == 0 && bArr[22] == 15 && !pairifexist(bArr[17], bArr[18])) {
            int i = ((bArr[19] & 255) << 8) + (bArr[20] & 255);
            HashMap<String, String> map = new HashMap<>();
            map.put("subnetID", String.valueOf(bArr[17] & 255));
            map.put("deviceID", String.valueOf(bArr[18] & 255));
            map.put("devicename", this.sdt.getDeviceType(i));
            MainActivity.netdeviceList.add(map);
            this.mLeDeviceListAdapter.addDevice(map);
            this.mLeDeviceListAdapter.notifyDataSetChanged();
        }
    }

    public boolean pairifexist(byte b, byte b2) {
        int i = b & 255;
        int i2 = b2 & 255;
        for (int i3 = 0; i3 < MainActivity.netdeviceList.size(); i3++) {
            if (String.valueOf(i).equals(MainActivity.netdeviceList.get(i3).get("subnetID")) && String.valueOf(i2).equals(MainActivity.netdeviceList.get(i3).get("deviceID"))) {
                return true;
            }
        }
        return false;
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        return intentFilter;
    }

    public void getdevice() {
        try {
            byte[] bArr = new byte[0];
            MainActivity.mydupsocket.SendUDPBuffer(bArr, (short) bArr.length, 14, (byte) -1, (byte) -1, false);
        } catch (Exception unused) {
        }
    }
}
