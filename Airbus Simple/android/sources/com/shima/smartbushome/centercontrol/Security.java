package com.shima.smartbushome.centercontrol;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.litesuits.http.data.Charsets;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.WheelView;
import com.shima.smartbushome.database.Savesecurity;
import com.shima.smartbushome.database.SavesecurityArea;
import com.shima.smartbushome.founction_command.securitycontrol;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Security extends AppCompatActivity {
    EditText dev;
    EditText password;
    ProgressDialog renewdialog;
    AlertView setIDdialog;
    EditText sub;
    WheelView wva;
    List<SavesecurityArea> securityarealist = new ArrayList();
    Savesecurity thisecurity = new Savesecurity();
    int selectArea = 1;
    Handler getareahandler = new Handler();
    securitycontrol secc = new securitycontrol();
    List<String> areanamelist = new ArrayList();
    boolean updateareaname = false;
    int alarmid = 0;
    int areanum = 1;
    int count = 0;
    Runnable getarearun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.Security.2
        @Override // java.lang.Runnable
        public void run() {
            if (Security.this.areanum > 8) {
                Security.this.renewdialog.dismiss();
                Security.this.renewdialog.setProgress(0);
                Security.this.areanum = 1;
                Security.this.getareahandler.removeCallbacks(Security.this.getarearun);
                Security.this.securityarealist = MainActivity.mgr.querysecurityarea();
                if (Security.this.areanamelist.size() > 0) {
                    Security.this.areanamelist.clear();
                }
                int i = 0;
                while (i < Security.this.securityarealist.size()) {
                    List<String> list = Security.this.areanamelist;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Area");
                    int i2 = i + 1;
                    sb.append(i2);
                    sb.append(": ");
                    sb.append(Security.this.securityarealist.get(i).areaName);
                    list.add(sb.toString());
                    i = i2;
                }
                Security.this.wva.setItems(Security.this.areanamelist);
                Security.this.updateareaname = false;
                return;
            }
            Security.this.count++;
            Security.this.secc.ReadAreaName((byte) Security.this.thisecurity.subnetID, (byte) Security.this.thisecurity.deviceID, Security.this.areanum, MainActivity.mydupsocket);
            if (Security.this.count >= 50) {
                Security.this.count = 0;
                Security.this.areanum = 1;
                Security.this.getareahandler.removeCallbacks(Security.this.getarearun);
                return;
            }
            Security.this.getareahandler.postDelayed(Security.this.getarearun, 500L);
        }
    };
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.Security.3
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == Security.this.setIDdialog) {
                switch (i) {
                    case -1:
                        Security.this.finish();
                        break;
                    case 0:
                        Savesecurity savesecurity = new Savesecurity();
                        savesecurity.room_id = 0;
                        savesecurity.security_id = 0;
                        savesecurity.subnetID = Integer.parseInt(Security.this.sub.getText().toString().trim());
                        savesecurity.deviceID = Integer.parseInt(Security.this.dev.getText().toString().trim());
                        savesecurity.password = 888888;
                        MainActivity.mgr.addsecurity(savesecurity);
                        Security.this.thisecurity = MainActivity.mgr.querysecurity().get(0);
                        Security.this.securityarealist = MainActivity.mgr.querysecurityarea();
                        if (Security.this.securityarealist.size() <= 0) {
                            Security.this.renewdialog.show();
                            Security.this.getareahandler.postDelayed(Security.this.getarearun, 20L);
                            break;
                        }
                        break;
                }
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.Security.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (udp_socket.ACTION_DATA_IN.equals(intent.getAction())) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    Security.this.RunReceiveData(byteArrayExtra);
                }
            }
        }
    };
    boolean doing = false;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_secerity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.security_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Security");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int i = 0;
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        this.password = (EditText) findViewById(R.id.security_password);
        this.renewdialog = new ProgressDialog(this);
        this.renewdialog.setCancelable(true);
        this.renewdialog.setCanceledOnTouchOutside(false);
        this.renewdialog.setMessage("Getting Area Name...");
        this.renewdialog.setProgressStyle(1);
        this.wva = (WheelView) findViewById(R.id.security_area);
        this.wva.setOffset(1);
        this.wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.Security.1
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i2, String str) {
                Security.this.selectArea = i2;
            }
        });
        if (MainActivity.mgr.querysecurity().size() <= 0) {
            this.setIDdialog = new AlertView("Setting", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
            View viewInflate = getLayoutInflater().inflate(R.layout.setting_musicinfo, (ViewGroup) null);
            this.sub = (EditText) viewInflate.findViewById(R.id.music_subedit);
            this.dev = (EditText) viewInflate.findViewById(R.id.music_devedit);
            this.sub.setText(String.valueOf(this.thisecurity.subnetID));
            this.dev.setText(String.valueOf(this.thisecurity.deviceID));
            this.setIDdialog.addExtView(viewInflate);
            this.setIDdialog.setCancelable(false);
            this.setIDdialog.show();
            return;
        }
        this.thisecurity = MainActivity.mgr.querysecurity().get(0);
        this.securityarealist = MainActivity.mgr.querysecurityarea();
        if (this.securityarealist.size() != 8) {
            MainActivity.mgr.deletesecurityarea("securityarea");
            this.renewdialog.show();
            this.getareahandler.postDelayed(this.getarearun, 20L);
            return;
        }
        while (i < this.securityarealist.size()) {
            List<String> list = this.areanamelist;
            StringBuilder sb = new StringBuilder();
            sb.append("Area");
            int i2 = i + 1;
            sb.append(i2);
            sb.append(": ");
            sb.append(this.securityarealist.get(i).areaName);
            list.add(sb.toString());
            i = i2;
        }
        this.wva.setItems(this.areanamelist);
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        registerReceiver(this.mGattUpdateReceiver, makeUpdateIntentFilter());
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        unregisterReceiver(this.mGattUpdateReceiver);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.getareahandler.removeCallbacks(this.getarearun);
    }

    public void away(View view) {
        try {
            if (Integer.parseInt(this.password.getText().toString().trim()) == this.thisecurity.password) {
                this.alarmid = 2;
                this.secc.setAlarm((byte) this.thisecurity.subnetID, (byte) this.thisecurity.deviceID, this.selectArea, 2, MainActivity.mydupsocket);
            } else {
                Toast.makeText(this, "wrong password", 0).show();
            }
        } catch (Exception unused) {
            Toast.makeText(this, "wrong password", 0).show();
        }
    }

    public void night(View view) {
        try {
            if (Integer.parseInt(this.password.getText().toString().trim()) == this.thisecurity.password) {
                this.alarmid = 3;
                this.secc.setAlarm((byte) this.thisecurity.subnetID, (byte) this.thisecurity.deviceID, this.selectArea, 3, MainActivity.mydupsocket);
            } else {
                Toast.makeText(this, "wrong password", 0).show();
            }
        } catch (Exception unused) {
            Toast.makeText(this, "wrong password", 0).show();
        }
    }

    public void nightwithguest(View view) {
        try {
            if (Integer.parseInt(this.password.getText().toString().trim()) == this.thisecurity.password) {
                this.alarmid = 4;
                this.secc.setAlarm((byte) this.thisecurity.subnetID, (byte) this.thisecurity.deviceID, this.selectArea, 4, MainActivity.mydupsocket);
            } else {
                Toast.makeText(this, "wrong password", 0).show();
            }
        } catch (Exception unused) {
            Toast.makeText(this, "wrong password", 0).show();
        }
    }

    public void day(View view) {
        try {
            if (Integer.parseInt(this.password.getText().toString().trim()) == this.thisecurity.password) {
                this.alarmid = 5;
                this.secc.setAlarm((byte) this.thisecurity.subnetID, (byte) this.thisecurity.deviceID, this.selectArea, 5, MainActivity.mydupsocket);
            } else {
                Toast.makeText(this, "wrong password", 0).show();
            }
        } catch (Exception unused) {
            Toast.makeText(this, "wrong password", 0).show();
        }
    }

    public void vacation(View view) {
        try {
            if (Integer.parseInt(this.password.getText().toString().trim()) == this.thisecurity.password) {
                this.alarmid = 1;
                this.secc.setAlarm((byte) this.thisecurity.subnetID, (byte) this.thisecurity.deviceID, this.selectArea, 1, MainActivity.mydupsocket);
            } else {
                Toast.makeText(this, "wrong password", 0).show();
            }
        } catch (Exception unused) {
            Toast.makeText(this, "wrong password", 0).show();
        }
    }

    public void disarm(View view) {
        try {
            if (Integer.parseInt(this.password.getText().toString().trim()) == this.thisecurity.password) {
                this.alarmid = 6;
                this.secc.setAlarm((byte) this.thisecurity.subnetID, (byte) this.thisecurity.deviceID, this.selectArea, 6, MainActivity.mydupsocket);
            } else {
                Toast.makeText(this, "wrong password", 0).show();
            }
        } catch (Exception unused) {
            Toast.makeText(this, "wrong password", 0).show();
        }
    }

    public void RunReceiveData(byte[] bArr) {
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (this.doing) {
            return;
        }
        this.doing = true;
        if ((bArr[17] & 255) == this.thisecurity.subnetID && (bArr[18] & 255) == this.thisecurity.deviceID) {
            if (i != 261) {
                if (i == 585 && bArr[25] == ((byte) this.areanum)) {
                    if (this.updateareaname) {
                        byte[] bArr2 = new byte[20];
                        for (int i2 = 0; i2 < 20; i2++) {
                            int i3 = i2 + 27;
                            if (bArr[i3] == -1) {
                                bArr2[i2] = 32;
                            } else {
                                bArr2[i2] = bArr[i3];
                            }
                        }
                        String string = new StringBuffer(bytetostring(bArr2, Charsets.Unicode).replaceAll("†", "")).reverse().toString();
                        SavesecurityArea savesecurityArea = new SavesecurityArea();
                        savesecurityArea.areaNO = this.areanum;
                        savesecurityArea.security_id = 0;
                        savesecurityArea.areaName = string;
                        MainActivity.mgr.updatesecurityarea(savesecurityArea);
                        this.renewdialog.setProgress((this.areanum * 100) / 8);
                        this.areanum++;
                    } else {
                        byte[] bArr3 = new byte[20];
                        for (int i4 = 0; i4 < 20; i4++) {
                            int i5 = i4 + 27;
                            if (bArr[i5] == -1) {
                                bArr3[i4] = 32;
                            } else {
                                bArr3[i4] = bArr[i5];
                            }
                        }
                        String strReplaceAll = bytetostring(bArr3, "ascii").replaceAll(" ", "");
                        SavesecurityArea savesecurityArea2 = new SavesecurityArea();
                        savesecurityArea2.areaNO = this.areanum;
                        savesecurityArea2.security_id = 0;
                        savesecurityArea2.areaName = strReplaceAll;
                        MainActivity.mgr.addsecurityarea(savesecurityArea2);
                        this.renewdialog.setProgress((this.areanum * 100) / 8);
                        this.areanum++;
                    }
                }
            } else if ((bArr[26] & 255) == this.alarmid) {
                Toast.makeText(this, "suecceed", 0).show();
            }
        }
        this.doing = false;
    }

    public String bytetostring(byte[] bArr, String str) {
        try {
            return new String(bArr, str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String orderDesc(String str) {
        if (str == null || str.length() < 0) {
            return "ERROR";
        }
        byte[] bytes = str.getBytes();
        for (int i = 0; i < bytes.length / 2; i++) {
            Byte bValueOf = Byte.valueOf(bytes[i]);
            bytes[i] = bytes[(bytes.length - 1) - i];
            bytes[(bytes.length - 1) - i] = bValueOf.byteValue();
        }
        return new String(bytes);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.security_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
        } else {
            switch (itemId) {
                case R.id.security_reflashname /* 2131231764 */:
                    this.renewdialog.show();
                    this.updateareaname = true;
                    this.getareahandler.postDelayed(this.getarearun, 20L);
                    break;
                case R.id.security_setting /* 2131231765 */:
                    String string = this.password.getText().toString();
                    if (string == null) {
                        Toast.makeText(this, "please put password", 0).show();
                        break;
                    } else {
                        try {
                            if (Integer.parseInt(string) == this.thisecurity.password) {
                                startActivity(new Intent(this, (Class<?>) SecuritySettingActivity.class));
                            } else {
                                Toast.makeText(this, "wrong password", 0).show();
                            }
                            break;
                        } catch (Exception unused) {
                            Toast.makeText(this, "wrong password", 0).show();
                            break;
                        }
                    }
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        return intentFilter;
    }
}
