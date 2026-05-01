package com.pzj.ipcdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class FirmwareUpdateActiviy extends Activity implements View.OnClickListener, BridgeService.Firmware {
    private Button button_back;
    private String download_server;
    private String filePath_sys;
    private String language;
    private String oemID;
    private RelativeLayout service_sysver;
    private TextView tv_service_sysver;
    private TextView tvsysver;
    private String did = null;
    private ProgressDialog progressDialog = null;
    private String LocalSysver = "noinfo";
    private boolean isGetSysData = false;
    private boolean sys_isnew = false;
    private Handler hander = new Handler() { // from class: com.pzj.ipcdemo.FirmwareUpdateActiviy.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            FirmwareUpdateActiviy.this.isGetSysData = true;
            FirmwareUpdateActiviy.this.tvsysver.setText(FirmwareUpdateActiviy.this.LocalSysver);
            FirmwareUpdateActiviy.this.getFirmware();
        }
    };
    private Runnable runnable = new Runnable() { // from class: com.pzj.ipcdemo.FirmwareUpdateActiviy.2
        @Override // java.lang.Runnable
        public void run() {
            if (FirmwareUpdateActiviy.this.isGetSysData) {
                return;
            }
            FirmwareUpdateActiviy.this.isGetSysData = false;
            if (FirmwareUpdateActiviy.this.progressDialog.isShowing()) {
                FirmwareUpdateActiviy.this.progressDialog.dismiss();
            }
        }
    };
    private Handler sysVerhander = new Handler() { // from class: com.pzj.ipcdemo.FirmwareUpdateActiviy.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String str = (String) message.obj;
            Log.e("info", "sys:" + str);
            FirmwareUpdateActiviy.this.tv_service_sysver.setText(str);
        }
    };
    private Handler updateHandler = new Handler() { // from class: com.pzj.ipcdemo.FirmwareUpdateActiviy.4
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            Log.i("info", "did:" + FirmwareUpdateActiviy.this.did + "download_server:" + FirmwareUpdateActiviy.this.download_server + "filePath_sys:" + FirmwareUpdateActiviy.this.filePath_sys);
            new AlertDialog.Builder(FirmwareUpdateActiviy.this).setTitle("New system firmware was detected. Is it updated?").setCancelable(false).setNegativeButton("Not updated", new DialogInterface.OnClickListener() { // from class: com.pzj.ipcdemo.FirmwareUpdateActiviy.4.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton("Update now", new DialogInterface.OnClickListener() { // from class: com.pzj.ipcdemo.FirmwareUpdateActiviy.4.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirmwareUpdateActiviy.this.sys_isnew = false;
                    NativeCaller.UpgradeFirmware(FirmwareUpdateActiviy.this.did, FirmwareUpdateActiviy.this.download_server, FirmwareUpdateActiviy.this.filePath_sys, 0);
                    Toast.makeText(FirmwareUpdateActiviy.this.getApplicationContext(), "The system firmware is being updated and the camera will be restarted later...", 1).show();
                }
            }).show();
        }
    };
    private String serverVer = null;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.update_firmware_view);
        this.language = Locale.getDefault().getCountry();
        getDate();
        findView();
        showDiglog();
        this.hander.postDelayed(this.runnable, 5000L);
        NativeCaller.PPPPGetSystemParams(this.did, 13);
        BridgeService.setFirmware(this);
    }

    private void getDate() {
        this.did = getIntent().getStringExtra(ContentCommon.STR_CAMERA_ID);
    }

    private void findView() {
        this.button_back = (Button) findViewById(R.id.back);
        this.button_back.setOnClickListener(this);
        this.tvsysver = (TextView) findViewById(R.id.sysver);
        this.tv_service_sysver = (TextView) findViewById(R.id.service_sysver_text);
        this.tv_service_sysver.setOnClickListener(this);
        this.service_sysver = (RelativeLayout) findViewById(R.id.service_sysver);
        this.service_sysver.setOnClickListener(this);
    }

    private void showDiglog() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage("正在获取版本信息...");
        this.progressDialog.show();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
            return;
        }
        if (id == R.id.service_sysver) {
            if (this.download_server != null && this.filePath_sys != null) {
                if (this.download_server.length() == 0 || this.filePath_sys.length() == 0) {
                    Toast.makeText(this, "信息不完整，无法升级", 1).show();
                    return;
                } else if (this.LocalSysver.equals(this.serverVer)) {
                    Toast.makeText(this, "版本信息一样，无需升级", 1).show();
                    return;
                } else {
                    this.updateHandler.sendEmptyMessage(1);
                    return;
                }
            }
            Toast.makeText(this, "", 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getFirmware() {
        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
        new Thread(new getFirmwareData()).start();
    }

    class getFirmwareData implements Runnable {
        getFirmwareData() {
        }

        @Override // java.lang.Runnable
        public void run() {
            String strSendHttpFirmwareMessge = FirmwareUpdateActiviy.sendHttpFirmwareMessge("firmware", "firmware", FirmwareUpdateActiviy.this.LocalSysver, FirmwareUpdateActiviy.this.language);
            if (strSendHttpFirmwareMessge == null || strSendHttpFirmwareMessge.equals("")) {
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(strSendHttpFirmwareMessge);
                String strOptString = jSONObject.optString(DatabaseUtil.KEY_NAME);
                if (strOptString == null) {
                    strOptString = "";
                }
                String strOptString2 = jSONObject.optString("download_file");
                if (strOptString2 == null) {
                    strOptString2 = "";
                }
                String strOptString3 = jSONObject.optString("download_server");
                if (strOptString3 == null) {
                    strOptString3 = "";
                }
                if (strOptString.trim().length() != 0 && strOptString2.trim().length() != 0 && strOptString3.trim().length() != 0) {
                    Message message = new Message();
                    message.obj = strOptString;
                    FirmwareUpdateActiviy.this.serverVer = strOptString;
                    FirmwareUpdateActiviy.this.sysVerhander.sendMessage(message);
                    FirmwareUpdateActiviy.this.download_server = strOptString3;
                    FirmwareUpdateActiviy.this.filePath_sys = strOptString2;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static String sendHttpFirmwareMessge(String str, String... strArr) {
        String string;
        if (strArr.length == 0 || str.length() == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String str2 : strArr) {
            stringBuffer.append("/" + str2);
        }
        String str3 = "http://api4.eye4.cn:808" + stringBuffer.toString();
        Log.e("url", str3);
        try {
            URL url = new URL(str3);
            HttpResponse httpResponseExecute = new DefaultHttpClient().execute((HttpUriRequest) new HttpGet(new URI(url.getProtocol(), url.getHost() + ":808", url.getPath(), url.getQuery(), null)));
            if (httpResponseExecute.getStatusLine().getStatusCode() != 200 || (string = EntityUtils.toString(httpResponseExecute.getEntity())) == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(string);
            jSONObject.optInt("ret");
            if (jSONObject.optInt("errcode") == 333) {
                return null;
            }
            return string;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (URISyntaxException e3) {
            e3.printStackTrace();
        } catch (JSONException e4) {
            e4.printStackTrace();
        }
        return null;
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.Firmware
    public void CallBack_UpdateFirmware(String str, String str2, String str3, String str4) {
        Log.i("info", "callback" + str2 + "==appver" + str3 + "oemid" + str4);
        this.LocalSysver = str2;
        this.oemID = str4;
        if (this.oemID == null || this.oemID.equals("")) {
            this.oemID = "OEM";
        }
        if (this.did.equalsIgnoreCase(str)) {
            this.hander.sendEmptyMessage(1);
        }
    }
}
