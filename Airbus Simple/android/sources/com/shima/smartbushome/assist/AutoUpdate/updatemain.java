package com.shima.smartbushome.assist.AutoUpdate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import com.shima.smartbushome.BuildConfig;
import com.shima.smartbushome.assist.AutoUpdate.UpdateService;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

/* loaded from: classes.dex */
public class updatemain {
    Handler handler;
    HashMap<String, String> mHashMap;
    private Context thiscontext;

    public updatemain(Context context) {
        this.thiscontext = context;
    }

    public void checkUpdate(final boolean z) {
        this.handler = new Handler() { // from class: com.shima.smartbushome.assist.AutoUpdate.updatemain.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i != 3) {
                    switch (i) {
                        case 0:
                            if (!z) {
                                Toast.makeText(updatemain.this.thiscontext, "already the newest", 1).show();
                                break;
                            }
                            break;
                        case 1:
                            updatemain.this.showNoticeDialog();
                            break;
                    }
                }
                if (z) {
                    return;
                }
                Toast.makeText(updatemain.this.thiscontext, "timeout", 0).show();
            }
        };
        new Thread(new getXMLThread()).start();
    }

    public class getXMLThread extends Thread {
        public getXMLThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws ProtocolException {
            try {
                ParseXmlService parseXmlService = new ParseXmlService();
                int versionCode = updatemain.this.getVersionCode(updatemain.this.thiscontext);
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://www.smartg4.cn/EasyControl/ecupdate.xml").openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                if (httpURLConnection.getResponseCode() == 200) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    updatemain.this.mHashMap = parseXmlService.parseXml(inputStream);
                    if (updatemain.this.mHashMap != null) {
                        if (Integer.valueOf(updatemain.this.mHashMap.get("version")).intValue() > versionCode) {
                            Message message = new Message();
                            message.what = 1;
                            updatemain.this.handler.sendMessage(message);
                        } else {
                            Message message2 = new Message();
                            message2.what = 0;
                            updatemain.this.handler.sendMessage(message2);
                        }
                    }
                }
            } catch (Exception e) {
                Message message3 = new Message();
                message3.what = 3;
                updatemain.this.handler.sendMessage(message3);
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.thiscontext);
        builder.setTitle("Software Update");
        builder.setMessage("New EasyControl is available, Download now ?");
        builder.setPositiveButton("Download Now!", new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.assist.AutoUpdate.updatemain.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                String str = updatemain.this.mHashMap.get("url");
                File file = new File("/Android/data/com.dave.smartbushome/update");
                if (!file.exists()) {
                    file.mkdirs();
                }
                UpdateService.Builder.create(str).setStoreDir("/Android/data/com.dave.smartbushome/update").build(updatemain.this.thiscontext);
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() { // from class: com.shima.smartbushome.assist.AutoUpdate.updatemain.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
