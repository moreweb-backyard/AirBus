package com.mediatek.demo.smartconnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import java.util.ArrayList;
import java.util.List;
import voice.encoder.VoicePlayer;

/* loaded from: classes.dex */
public class MainActivity extends Activity {
    private String currentBssid;
    private JniLoader loader;
    private Context mContext;
    private EditText mCustomInfoEdit;
    private EditText mEncryptionKeyEdit;
    private EditText mNameEdit;
    private ProgressBar mProgressBar;
    private EditText mPswEdit;
    private Button mStartButton;
    private Button mStopButton;
    private CheckBox mV1CheckBox;
    private EditText mV1V4IntervalEdit;
    private CheckBox mV4CheckBox;
    private CheckBox mV5CheckBox;
    private EditText mV5IntervalEdit;
    private TextView mWatcher;
    private VoicePlayer player = new VoicePlayer();
    private String sendMac = null;
    private String wifiName;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.activity_smart_link);
        this.mStartButton = (Button) findViewById(R.id.btn_start);
        this.mStopButton = (Button) findViewById(R.id.btn_stop);
        this.mProgressBar = (ProgressBar) findViewById(R.id.sending_indicator);
        this.mNameEdit = (EditText) findViewById(R.id.et_name);
        this.mPswEdit = (EditText) findViewById(R.id.et_psw);
        this.mCustomInfoEdit = (EditText) findViewById(R.id.et_custom_info);
        this.mEncryptionKeyEdit = (EditText) findViewById(R.id.et_encryption_key);
        this.mV1V4IntervalEdit = (EditText) findViewById(R.id.et_v1v4_interval);
        this.mV5IntervalEdit = (EditText) findViewById(R.id.et_v5_interval);
        this.mWatcher = (TextView) findViewById(R.id.watcher);
        this.mV1CheckBox = (CheckBox) findViewById(R.id.checkbox_v1);
        this.mV4CheckBox = (CheckBox) findViewById(R.id.checkbox_v4);
        this.mV5CheckBox = (CheckBox) findViewById(R.id.checkbox_v5);
        this.mContext = this;
        this.mStartButton.setEnabled(true);
        this.mStopButton.setEnabled(false);
        try {
            getWifi();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("SmartConnection", "Load Smart Connection Library Result ：" + JniLoader.LoadLib());
        this.loader = new JniLoader();
        int iGetProtoVersion = this.loader.GetProtoVersion();
        Log.e("SmartConnection", "proV ：" + iGetProtoVersion);
        int iGetLibVersion = this.loader.GetLibVersion();
        Log.e("SmartConnection", "libV ：" + iGetLibVersion);
        String str = "V" + iGetProtoVersion + FileUtil.FILE_EXTENSION_SEPARATOR + iGetLibVersion;
        this.mV1CheckBox.setChecked(false);
        this.mV4CheckBox.setChecked(false);
        this.mV5CheckBox.setChecked(false);
        this.mNameEdit.addTextChangedListener(new EditChangeHandler(1));
        this.mPswEdit.addTextChangedListener(new EditChangeHandler(2));
        this.mCustomInfoEdit.addTextChangedListener(new EditChangeHandler(3));
        WifiInfo connectionInfo = ((WifiManager) getSystemService("wifi")).getConnectionInfo();
        Log.e("SmartConnection", "start info=" + connectionInfo.getSSID());
        if (connectionInfo.getSSID().isEmpty()) {
            return;
        }
        String ssid = connectionInfo.getSSID();
        this.mNameEdit.setText(ssid.substring(1, ssid.length() - 1));
        this.mNameEdit.setSelection(this.mNameEdit.getText().length());
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        this.mStartButton.setOnClickListener(new View.OnClickListener() { // from class: com.mediatek.demo.smartconnection.MainActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws NumberFormatException {
                float f;
                float f2 = 0.0f;
                if (MainActivity.this.mV1V4IntervalEdit.getText().toString().isEmpty()) {
                    f = 0.0f;
                } else {
                    f = Float.parseFloat(MainActivity.this.mV1V4IntervalEdit.getText().toString());
                    Log.e("SmartConnection", "start oI=" + f);
                }
                if (!MainActivity.this.mV5IntervalEdit.getText().toString().isEmpty()) {
                    f2 = Float.parseFloat(MainActivity.this.mV5IntervalEdit.getText().toString());
                    Log.e("SmartConnection", "start nI=" + f2);
                }
                String string = MainActivity.this.mEncryptionKeyEdit.getText().toString();
                if (string == null) {
                    Log.e("SmartConnection", "init Smart key is null");
                } else {
                    Log.e("SmartConnection", "init Smart key-len=" + string.length() + ", key-emp=" + string.isEmpty());
                }
                Log.e("SmartConnection", "init Smart key=" + string + ", sendV1=1, sendV4=1, sendV5=1");
                int iInitSmartConnection = MainActivity.this.loader.InitSmartConnection(string, "0xff 0xff 0xff 0xff 0xff 0xff", 1, 1, 1);
                StringBuilder sb = new StringBuilder();
                sb.append("init return retValue=");
                sb.append(iInitSmartConnection);
                Log.e("SmartConnection", sb.toString());
                if (iInitSmartConnection != 0) {
                    MainActivity.this.showWarningDialog(R.string.init_failed);
                    return;
                }
                Log.e("SmartConnection", "Send Smart oI=" + f + ", nI=" + f2);
                MainActivity.this.loader.SetSendInterval(f, f2);
                String string2 = MainActivity.this.mNameEdit.getText().toString();
                String string3 = MainActivity.this.mPswEdit.getText().toString();
                String string4 = MainActivity.this.mCustomInfoEdit.getText().toString();
                if (string4 == null) {
                    Log.e("SmartConnection", "Start Smart Custom is null");
                } else {
                    Log.e("SmartConnection", "Start Smart Custom-len=" + string4.length() + ", Custom-emp=" + string4.isEmpty());
                }
                Log.e("SmartConnection", "Start Smart SSID=" + string2 + ", Password=" + string3 + ", Custom=" + string4);
                MainActivity.this.sendSonic(MainActivity.this.sendMac, string3.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("start return retValue=");
                sb2.append(iInitSmartConnection);
                Log.e("SmartConnection", sb2.toString());
                if (iInitSmartConnection != 0) {
                    MainActivity.this.showWarningDialog(R.string.start_failed);
                } else {
                    MainActivity.this.updateUIWhileSending();
                }
            }
        });
        this.mStopButton.setOnClickListener(new View.OnClickListener() { // from class: com.mediatek.demo.smartconnection.MainActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int iStopSmartConnection = MainActivity.this.loader.StopSmartConnection();
                Log.e("SmartConnection", "Stop return failed : " + iStopSmartConnection);
                if (iStopSmartConnection != 0) {
                    MainActivity.this.showWarningDialog(R.string.stop_failed);
                }
                MainActivity.this.updateUIWhileStopped();
                MainActivity.this.player.stop();
            }
        });
    }

    private static byte uniteBytes(byte b, byte b2) {
        return (byte) (((byte) (Byte.decode("0x" + new String(new byte[]{b})).byteValue() << 4)) ^ Byte.decode("0x" + new String(new byte[]{b2})).byteValue());
    }

    private static byte[] HexString2Bytes(String str) {
        byte[] bArr = new byte[str.length() / 2];
        byte[] bytes = str.getBytes();
        for (int i = 0; i < str.length() / 2; i++) {
            int i2 = i * 2;
            bArr[i] = uniteBytes(bytes[i2], bytes[i2 + 1]);
        }
        return bArr;
    }

    private static void printHexString(byte[] bArr) {
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            System.out.print("aaa" + hexString.toUpperCase() + " ");
        }
        System.out.println("");
    }

    private void getWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService("wifi");
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        this.wifiName = connectionInfo.getSSID().toString();
        int i = 0;
        if (this.wifiName.length() > 2 && this.wifiName.charAt(0) == '\"' && this.wifiName.charAt(this.wifiName.length() - 1) == '\"') {
            this.wifiName = this.wifiName.substring(1, this.wifiName.length() - 1);
        }
        List<ScanResult> scanResults = wifiManager.getScanResults();
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        for (int i2 = 0; i2 < scanResults.size(); i2++) {
            arrayList.add(scanResults.get(i2).BSSID.toString());
        }
        this.currentBssid = connectionInfo.getBSSID();
        if (this.currentBssid == null) {
            while (true) {
                if (i >= scanResults.size()) {
                    break;
                }
                if (scanResults.get(i).SSID.toString().equals(this.wifiName)) {
                    this.currentBssid = scanResults.get(i).BSSID.toString();
                    break;
                }
                i++;
            }
        } else if (this.currentBssid.equals("00:00:00:00:00:00") || this.currentBssid.equals("")) {
            while (true) {
                if (i >= scanResults.size()) {
                    break;
                }
                if (scanResults.get(i).SSID.toString().equals(this.wifiName)) {
                    this.currentBssid = scanResults.get(i).BSSID.toString();
                    break;
                }
                i++;
            }
        }
        if (this.currentBssid == null) {
            finish();
        }
        String[] strArrSplit = this.currentBssid.split(":");
        for (int length = this.currentBssid.split(":").length - 1; length > -1; length--) {
            for (int size = arrayList.size() - 1; size > -1; size--) {
                if (!this.currentBssid.equals(arrayList.get(size))) {
                    if (!strArrSplit[length].equals(((String) arrayList.get(size)).split(":")[length])) {
                        arrayList.remove(size);
                    }
                }
            }
            if (arrayList.size() == 1 || arrayList.size() == 0) {
                if (length == 5) {
                    this.sendMac = strArrSplit[length].toString();
                    return;
                }
                if (length == 4) {
                    this.sendMac = strArrSplit[length].toString() + strArrSplit[length + 1].toString();
                    return;
                }
                this.sendMac = strArrSplit[5].toString() + strArrSplit[4].toString() + strArrSplit[3].toString();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0015  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x001f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void sendSonic(java.lang.String r10, java.lang.String r11) {
        /*
            Method dump skipped, instructions count: 200
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mediatek.demo.smartconnection.MainActivity.sendSonic(java.lang.String, java.lang.String):void");
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        if (this.player != null) {
            this.player.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIWhileSending() {
        this.mStartButton.setEnabled(false);
        this.mStopButton.setEnabled(true);
        this.mProgressBar.setVisibility(0);
        this.mV1CheckBox.setEnabled(false);
        this.mV4CheckBox.setEnabled(false);
        this.mV5CheckBox.setEnabled(false);
        this.mNameEdit.setEnabled(false);
        this.mPswEdit.setEnabled(false);
        this.mV1V4IntervalEdit.setEnabled(false);
        this.mV5IntervalEdit.setEnabled(false);
        this.mCustomInfoEdit.setEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUIWhileStopped() {
        this.mStartButton.setEnabled(true);
        this.mStopButton.setEnabled(false);
        this.mProgressBar.setVisibility(4);
        this.mV1CheckBox.setEnabled(true);
        this.mV4CheckBox.setEnabled(true);
        this.mV5CheckBox.setEnabled(true);
        this.mNameEdit.setEnabled(true);
        this.mPswEdit.setEnabled(true);
        this.mV1V4IntervalEdit.setEnabled(true);
        this.mV5IntervalEdit.setEnabled(true);
        this.mCustomInfoEdit.setEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showWarningDialog(final int i) {
        runOnUiThread(new Runnable() { // from class: com.mediatek.demo.smartconnection.MainActivity.3
            @Override // java.lang.Runnable
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this.mContext);
                builder.setTitle(R.string.warning);
                builder.setMessage(i);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.mediatek.demo.smartconnection.MainActivity.3.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private class EditChangeHandler implements TextWatcher {
        private int which;

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public EditChangeHandler(int i) {
            this.which = i;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            boolean z;
            String string = charSequence.toString();
            switch (this.which) {
                case 1:
                    if (string.getBytes().length > 32) {
                        MainActivity.this.mWatcher.setText("SSID Exceed Max Length (32)");
                        z = false;
                        break;
                    }
                    z = true;
                    break;
                case 2:
                    if (string.getBytes().length > 64) {
                        MainActivity.this.mWatcher.setText("Password Exceed Max Length (64)");
                        z = false;
                        break;
                    }
                    z = true;
                    break;
                case 3:
                    if (string.getBytes().length > 640) {
                        MainActivity.this.mWatcher.setText("CustomInfo Exceed Max Length (640)");
                        z = false;
                        break;
                    }
                    z = true;
                    break;
                default:
                    z = false;
                    break;
            }
            if (z) {
                MainActivity.this.mWatcher.setVisibility(8);
            } else {
                MainActivity.this.mWatcher.setVisibility(0);
            }
        }
    }
}
