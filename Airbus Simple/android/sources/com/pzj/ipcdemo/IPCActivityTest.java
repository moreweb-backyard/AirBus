package com.pzj.ipcdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.mediatek.demo.smartconnection.MainActivity;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.utils.ImageDispose;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class IPCActivityTest extends AppCompatActivity implements BridgeService.IpcamClientInterface {
    public static final int PPPP_STATUS_CONNECTING = 0;
    public static final int PPPP_STATUS_CONNECT_FAILED = 3;
    public static final int PPPP_STATUS_CONNECT_TIMEOUT = 7;
    public static final int PPPP_STATUS_DEVICE_NOT_ON_LINE = 6;
    public static final int PPPP_STATUS_DISCONNECT = 4;
    public static final int PPPP_STATUS_INITIALING = 1;
    public static final int PPPP_STATUS_INVALID_ID = 5;
    public static final int PPPP_STATUS_ON_LINE = 2;
    public static final int PPPP_STATUS_WRONGPWD_RIGHTUSER = 9;
    public static final int PPPP_STATUS_WRONGPWD_WRONGUSER = 10;
    public static final int PPPP_STATUS_WRONGUSER_RIGHTPWD = 8;
    public static String TAG = "IPCActivity";
    public static String deviceId = "VSTA899484MXNCJ";
    public static String deviceName = "admin";
    public static String devicePass = "12345678";
    private Button button;
    private Button button2;
    private ImageView iv_bimage;

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void CameraStatus(String str, int i) {
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void callBackUserParams(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ipc_test);
        this.button = (Button) findViewById(R.id.button);
        this.button2 = (Button) findViewById(R.id.button2);
        this.iv_bimage = (ImageView) findViewById(R.id.iv_bimage);
        Intent intent = new Intent();
        intent.setClass(this, BridgeService.class);
        startService(intent);
        Log.d(TAG, "启动服务....");
        this.button2.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.IPCActivityTest.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Intent intent2 = new Intent();
                intent2.setClass(IPCActivityTest.this, MainActivity.class);
                IPCActivityTest.this.startActivity(intent2);
            }
        });
        this.button.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.IPCActivityTest.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (IPCActivityTest.this.button.getText().toString().equals("在线")) {
                    Intent intent2 = new Intent("com.pzj.play");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(DatabaseUtil.KEY_NAME, IPCActivityTest.deviceName);
                    bundle2.putString("pass", IPCActivityTest.devicePass);
                    bundle2.putString(DatabaseUtil.KEY_ID, IPCActivityTest.deviceId);
                    intent2.putExtras(bundle2);
                    IPCActivityTest.this.startActivity(intent2);
                    return;
                }
                new Thread(new Runnable() { // from class: com.pzj.ipcdemo.IPCActivityTest.2.1
                    @Override // java.lang.Runnable
                    public void run() throws InterruptedException {
                        try {
                            NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
                            Log.d(IPCActivityTest.TAG, "初始化服务器...");
                            Thread.sleep(3000L);
                            NativeCaller.Init();
                            Log.d(IPCActivityTest.TAG, "初始化回调...");
                            Thread.sleep(200L);
                            IPCActivityTest.this.startCameraPPPP(IPCActivityTest.deviceName, IPCActivityTest.devicePass, IPCActivityTest.deviceId);
                            Log.d(IPCActivityTest.TAG, "开启 p2p 连接 (账号:" + IPCActivityTest.deviceName + "，密码:" + IPCActivityTest.devicePass + "，设备ID:" + IPCActivityTest.deviceId);
                        } catch (Exception unused) {
                        }
                    }
                }).start();
            }
        });
        BridgeService.setIpcamClientInterface(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCameraPPPP(String str, String str2, String str3) {
        if (str3.toLowerCase().startsWith("vsta")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("vstd")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "HZLXSXIALKHYEIEJHUASLMHWEESUEKAUIHPHSWAOSTEMENSQPDLRLNPAPEPGEPERIBLQLKHXELEHHULOEGIAEEHYEIEK-$$", 1);
            return;
        }
        if (str3.toLowerCase().startsWith("vstf")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "HZLXEJIALKHYATPCHULNSVLMEELSHWIHPFIBAOHXIDICSQEHENEKPAARSTELERPDLNEPLKEILPHUHXHZEJEEEHEGEM-$$", 1);
            return;
        }
        if (str3.toLowerCase().startsWith("vste")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EEGDFHBAKKIOGNJHEGHMFEEDGLNOHJMPHAFPBEDLADILKEKPDLBDDNPOHKKCIFKJBNNNKLCPPPNDBFDL", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("pisr")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("vstg")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EEGDFHBOKCIGGFJPECHIFNEBGJNLHOMIHEFJBADPAGJELNKJDKANCBPJGHLAIALAADMDKPDGOENEBECCIK:vstarcam2018", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("vsth")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EEGDFHBLKGJIGEJLEKGOFMEDHAMHHJNAGGFABMCOBGJOLHLJDFAFCPPHGILKIKLMANNHKEDKOINIBNCPJOMK:vstarcam2018", 0);
        } else if (str3.toLowerCase().startsWith("vstb") || str3.toLowerCase().startsWith("vstc")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL", 0);
        } else {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "", 0);
        }
    }

    private void stopCameraPPPP(String str) {
        NativeCaller.StopPPPP(str);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void BSMsgNotifyData(String str, int i, final int i2) {
        Log.d(TAG, "设备状态信息及视频连接流程相关参数回调");
        Log.d(TAG, "did:" + str);
        Log.d(TAG, "type:" + i);
        Log.d(TAG, "param:" + i2);
        runOnUiThread(new Runnable() { // from class: com.pzj.ipcdemo.IPCActivityTest.3
            @Override // java.lang.Runnable
            public void run() {
                switch (i2) {
                    case 0:
                        IPCActivityTest.this.button.setText("连接中");
                        break;
                    case 1:
                        IPCActivityTest.this.button.setText("正在初始化");
                        break;
                    case 2:
                        IPCActivityTest.this.button.setText("在线");
                        break;
                    case 3:
                        IPCActivityTest.this.button.setText("连接失败");
                        break;
                    case 4:
                        IPCActivityTest.this.button.setText("连接已关闭");
                        break;
                    case 5:
                        IPCActivityTest.this.button.setText("无效 UID");
                        break;
                    case 6:
                        IPCActivityTest.this.button.setText("不在线");
                        break;
                    case 7:
                        IPCActivityTest.this.button.setText("连接超时");
                        break;
                    case 8:
                    case 9:
                    case 10:
                        IPCActivityTest.this.button.setText("密码错误");
                        break;
                }
            }
        });
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void BSSnapshotNotify(String str, byte[] bArr, int i) {
        Log.i(TAG, "BSSnapshotNotify---len" + i);
        final Bitmap picFromBytes = ImageDispose.getPicFromBytes(bArr, new BitmapFactory.Options());
        runOnUiThread(new Runnable() { // from class: com.pzj.ipcdemo.IPCActivityTest.4
            @Override // java.lang.Runnable
            public void run() {
                IPCActivityTest.this.iv_bimage.setImageBitmap(picFromBytes);
            }
        });
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.button.setText("获取状态");
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }
}
