package com.shima.smartbushome.assist.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
import com.shima.smartbushome.assist.BLE.iBeaconClass;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class smartBeaconManager {
    private IbeaconDetectedListener listener;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Context rootcontext;
    private long SCAN_PERIOD = 500;
    boolean stopb = false;
    Runnable stop = new Runnable() { // from class: com.shima.smartbushome.assist.BLE.smartBeaconManager.1
        @Override // java.lang.Runnable
        public void run() {
            if (!smartBeaconManager.this.stopb) {
                smartBeaconManager.this.stopDetect();
                smartBeaconManager.this.stopb = true;
                smartBeaconManager.this.mHandler.postDelayed(smartBeaconManager.this.stop, smartBeaconManager.this.SCAN_PERIOD);
                smartBeaconManager.this.receivelist.clear();
                return;
            }
            smartBeaconManager.this.stopb = false;
            smartBeaconManager.this.startDetect();
        }
    };
    public int limitrssi = 10;
    List<iBeaconClass.iBeacon> receivelist = new ArrayList();
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() { // from class: com.shima.smartbushome.assist.BLE.smartBeaconManager.2
        @Override // android.bluetooth.BluetoothAdapter.LeScanCallback
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
            iBeaconClass.iBeacon ibeaconFromScanData = iBeaconClass.fromScanData(bluetoothDevice, i, bArr);
            if (ibeaconFromScanData != null) {
                boolean z = false;
                int i2 = 0;
                while (true) {
                    if (i2 >= smartBeaconManager.this.receivelist.size()) {
                        break;
                    }
                    if (ibeaconFromScanData == smartBeaconManager.this.receivelist.get(i2)) {
                        if (Math.abs(smartBeaconManager.this.receivelist.get(i2).rssi) - Math.abs(ibeaconFromScanData.rssi) <= smartBeaconManager.this.limitrssi) {
                            smartBeaconManager.this.listener.onBeaconDetect(ibeaconFromScanData, smartBeaconManager.calculateAccuracy(ibeaconFromScanData.txPower, ibeaconFromScanData.rssi));
                            smartBeaconManager.this.receivelist.remove(i2);
                            smartBeaconManager.this.receivelist.add(ibeaconFromScanData);
                        }
                        z = true;
                    } else {
                        i2++;
                    }
                }
                if (z) {
                    return;
                }
                smartBeaconManager.this.receivelist.add(ibeaconFromScanData);
            }
        }
    };
    private Handler mHandler = new Handler();

    public interface IbeaconDetectedListener {
        void onBeaconDetect(iBeaconClass.iBeacon ibeacon, double d);
    }

    public smartBeaconManager(Context context) {
        this.rootcontext = context;
        this.mBluetoothAdapter = ((BluetoothManager) context.getSystemService("bluetooth")).getAdapter();
        if (this.mBluetoothAdapter == null) {
            Toast.makeText(this.rootcontext, "error_bluetooth_not_supported", 0).show();
            return;
        }
        if (!context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this.rootcontext, "ble_not_supported", 0).show();
        }
        this.mBluetoothAdapter.enable();
    }

    public void setIbeaconDetectedListener(IbeaconDetectedListener ibeaconDetectedListener) {
        this.listener = ibeaconDetectedListener;
    }

    public void start() {
        startDetect();
    }

    public void stop() {
        this.mHandler.removeCallbacks(this.stop);
        stopDetect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDetect() {
        this.mScanning = false;
        this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDetect() {
        this.mHandler.postDelayed(this.stop, this.SCAN_PERIOD);
        this.mScanning = true;
        this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
    }

    public void setSavePowerTime(int i) {
        this.SCAN_PERIOD = i;
    }

    protected static double calculateAccuracy(int i, double d) {
        if (d == 0.0d) {
            return -1.0d;
        }
        double d2 = i;
        Double.isNaN(d2);
        double d3 = (d * 1.0d) / d2;
        if (d3 < 1.0d) {
            return Math.pow(d3, 10.0d);
        }
        return (Math.pow(d3, 7.7095d) * 0.89976d) + 0.111d;
    }
}
