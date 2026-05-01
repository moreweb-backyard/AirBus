package com.shima.smartbushome;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.shima.smartbushome.andserver.CoreService;
import com.shima.smartbushome.andserver.LoadingDialog;
import com.shima.smartbushome.andserver.ServerStatusReceiver;
import com.shima.smartbushome.database.DatabaseContext;
import com.shima.smartbushome.util.SystemUIUtil;
import java.io.File;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/* loaded from: classes.dex */
public class DatabaseActivity extends AppCompatActivity {
    public static File databaseFile;
    private LoadingDialog loadingDialog;
    private ServerStatusReceiver mReceiver;
    private Intent mService;
    private Switch sw_server;
    private TextView tv_url;
    Window window;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_setting_database);
        SystemUIUtil.setSystemUIVisible(this, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Upload and Download");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.DatabaseActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DatabaseActivity.this.finish();
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        this.window = getWindow();
        this.window.addFlags(67108864);
        this.window.addFlags(Integer.MIN_VALUE);
        initView();
        this.mService = new Intent(this, (Class<?>) CoreService.class);
        this.mReceiver = new ServerStatusReceiver(this);
        this.mReceiver.register();
        this.sw_server.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.DatabaseActivity.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (!z) {
                    DatabaseActivity.this.stopService(DatabaseActivity.this.mService);
                    DatabaseActivity.this.tv_url.setText("");
                } else {
                    DatabaseActivity.this.showDiolog();
                    DatabaseActivity.this.startService(DatabaseActivity.this.mService);
                }
            }
        });
        databaseFile = new File(DatabaseContext.dbPath);
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    public void initView() {
        this.sw_server = (Switch) findViewById(R.id.sw_server);
        this.tv_url = (TextView) findViewById(R.id.tv_url);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.mReceiver.unRegister();
    }

    public void serverStart() {
        String hostIP = getHostIP();
        if (TextUtils.isEmpty(hostIP)) {
            return;
        }
        closeDiolog();
        this.tv_url.setText("http://" + hostIP + ":8899");
    }

    public static String getHostIP() throws SocketException {
        String hostAddress = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (true) {
                    if (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddressNextElement = inetAddresses.nextElement();
                        if (!(inetAddressNextElement instanceof Inet6Address) && !"127.0.0.1".equals(inetAddressNextElement.getHostAddress())) {
                            hostAddress = inetAddressNextElement.getHostAddress();
                            break;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("DatabaseActivity", "SocketException");
            e.printStackTrace();
        }
        return hostAddress;
    }

    public void serverHasStarted() {
        closeDiolog();
        Toast.makeText(this, "server is runing", 0).show();
        String hostIP = getHostIP();
        if (TextUtils.isEmpty(hostIP)) {
            return;
        }
        this.tv_url.setText("http://" + hostIP + ":8899");
    }

    public void serverStop() {
        try {
            this.tv_url.setText("");
            Toast.makeText(this, "server stop", 0).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDiolog() {
        try {
            this.loadingDialog.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.loadingDialog = new LoadingDialog(this, "opening server");
            this.loadingDialog.show();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void closeDiolog() {
        try {
            this.loadingDialog.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        SystemUIUtil.setSystemUIVisible(this, false);
    }
}
