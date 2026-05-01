package com.shima.smartbushome.centercontrol;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.SecurityLogAdapter;
import com.shima.smartbushome.assist.WheelView;
import com.shima.smartbushome.database.Savesecurity;
import com.shima.smartbushome.database.SavesecurityArea;
import com.shima.smartbushome.database.seclogdata;
import com.shima.smartbushome.founction_command.securitycontrol;
import com.shima.smartbushome.udp.udp_socket;
import com.shima.smartbushome.util.SystemUIUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* loaded from: classes.dex */
public class SecuritySettingActivity extends AppCompatActivity {
    AlertView areanameDialog;
    Switch bypass;
    private Calendar calendar;
    AlertView changepasswordDialog;
    private int day;
    EditText dev;
    AlertView logDialog;
    ListView loglist;
    SecurityLogAdapter madapter;
    private int month;
    EditText newAreaname;
    EditText newp1;
    EditText newp2;
    EditText oldp;
    ProgressDialog readinglog;
    AlertView setIDdialog;
    AlertView settingDialog;
    Button setting_seclog_clean;
    Button setting_seclog_enddate;
    Button setting_seclog_exit;
    Button setting_seclog_reflash;
    Button setting_seclog_startdate;
    EditText sub;
    TextView tv_enddate;
    TextView tv_startdate;
    Switch watchdog;
    private int year;
    Savesecurity thissecurity = new Savesecurity();
    List<SavesecurityArea> arealist = new ArrayList();
    List<seclogdata> logdatalist = new ArrayList();
    securitycontrol secc = new securitycontrol();
    int logsumcount = 0;
    byte[] logvalue = new byte[2];
    List<SavesecurityArea> securityarealist = new ArrayList();
    List<String> areanamelist = new ArrayList();
    int selectArea = 1;
    byte[] indata = new byte[10];
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.7
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) throws UnsupportedEncodingException {
            if (obj == SecuritySettingActivity.this.setIDdialog) {
                switch (i) {
                    case 0:
                        Savesecurity savesecurity = new Savesecurity();
                        savesecurity.subnetID = Integer.parseInt(SecuritySettingActivity.this.sub.getText().toString().trim());
                        savesecurity.deviceID = Integer.parseInt(SecuritySettingActivity.this.dev.getText().toString().trim());
                        savesecurity.password = SecuritySettingActivity.this.thissecurity.password;
                        MainActivity.mgr.updatesecurity(savesecurity);
                        SecuritySettingActivity.this.thissecurity.subnetID = savesecurity.subnetID;
                        SecuritySettingActivity.this.thissecurity.deviceID = savesecurity.deviceID;
                        break;
                }
            }
            if (obj == SecuritySettingActivity.this.changepasswordDialog) {
                switch (i) {
                    case 0:
                        String strTrim = SecuritySettingActivity.this.oldp.getText().toString().trim();
                        String strTrim2 = SecuritySettingActivity.this.newp1.getText().toString().trim();
                        String strTrim3 = SecuritySettingActivity.this.newp2.getText().toString().trim();
                        if (strTrim.length() <= 0 || strTrim2.length() <= 0 || strTrim3.length() <= 0) {
                            Toast.makeText(SecuritySettingActivity.this, "should not have blank value", 0).show();
                            break;
                        } else if (strTrim2.equals(strTrim3)) {
                            if (Integer.parseInt(strTrim) == SecuritySettingActivity.this.thissecurity.password) {
                                if (strTrim2.length() != 6) {
                                    Toast.makeText(SecuritySettingActivity.this, "new password should be 6 num", 0).show();
                                    break;
                                } else {
                                    Savesecurity savesecurity2 = new Savesecurity();
                                    savesecurity2.subnetID = SecuritySettingActivity.this.thissecurity.subnetID;
                                    savesecurity2.deviceID = SecuritySettingActivity.this.thissecurity.deviceID;
                                    savesecurity2.password = Integer.parseInt(strTrim2);
                                    MainActivity.mgr.updatesecurity(savesecurity2);
                                    SecuritySettingActivity.this.thissecurity.password = savesecurity2.password;
                                    Toast.makeText(SecuritySettingActivity.this, "save", 0).show();
                                    break;
                                }
                            } else {
                                Toast.makeText(SecuritySettingActivity.this, "password fail", 0).show();
                                break;
                            }
                        } else {
                            Toast.makeText(SecuritySettingActivity.this, "2 new password is diferent", 0).show();
                            break;
                        }
                        break;
                }
            }
            if (obj == SecuritySettingActivity.this.areanameDialog) {
                switch (i) {
                    case 0:
                        if (SecuritySettingActivity.this.newAreaname.getText().toString().trim().length() > 0) {
                            SecuritySettingActivity.this.secc.WriteAreaName((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.selectArea, SecuritySettingActivity.this.newAreaname.getText().toString().trim(), MainActivity.mydupsocket);
                            SavesecurityArea savesecurityArea = new SavesecurityArea();
                            savesecurityArea.security_id = 0;
                            savesecurityArea.areaNO = SecuritySettingActivity.this.selectArea;
                            savesecurityArea.areaName = SecuritySettingActivity.this.newAreaname.getText().toString().trim();
                            MainActivity.mgr.updatesecurityarea(savesecurityArea);
                        }
                        new Thread(SecuritySettingActivity.this.new bypasssave()).start();
                        break;
                }
            }
        }
    };
    Handler getloghandler = new Handler();
    int sentcount = 1;
    Runnable getlogrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.8
        @Override // java.lang.Runnable
        public void run() {
            if (SecuritySettingActivity.this.sentcount > SecuritySettingActivity.this.logsumcount) {
                SecuritySettingActivity.this.madapter = new SecurityLogAdapter(SecuritySettingActivity.this, SecuritySettingActivity.this.logdatalist);
                SecuritySettingActivity.this.loglist.setAdapter((ListAdapter) SecuritySettingActivity.this.madapter);
                SecuritySettingActivity.this.sentcount = 1;
                SecuritySettingActivity.this.logsumcount = 0;
                SecuritySettingActivity.this.readinglog.dismiss();
                SecuritySettingActivity.this.readinglog.setProgress(0);
                SecuritySettingActivity.this.getloghandler.removeCallbacks(SecuritySettingActivity.this.getlogrun);
                return;
            }
            SecuritySettingActivity.this.secc.ReadLogvalue((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.tv_startdate.getText().toString().trim(), SecuritySettingActivity.this.tv_enddate.getText().toString().trim(), SecuritySettingActivity.this.logvalue, SecuritySettingActivity.this.sentcount, MainActivity.mydupsocket);
            SecuritySettingActivity.this.getloghandler.postDelayed(SecuritySettingActivity.this.getlogrun, 500L);
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.9
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (udp_socket.ACTION_DATA_IN.equals(intent.getAction())) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    SecuritySettingActivity.this.RunReceiveData(byteArrayExtra);
                }
            }
        }
    };
    boolean doing = false;

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_security_setting);
        SystemUIUtil.setSystemUIVisible(this, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.securitysetting_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Setting");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        this.readinglog = new ProgressDialog(this);
        this.readinglog.setCancelable(true);
        this.readinglog.setCanceledOnTouchOutside(false);
        this.readinglog.setMessage("Getting Log...");
        this.readinglog.setProgressStyle(1);
        this.thissecurity = MainActivity.mgr.querysecurity().get(0);
        this.arealist = MainActivity.mgr.querysecurityarea();
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
        this.getloghandler.removeCallbacks(this.getlogrun);
    }

    public void setID(View view) {
        this.setIDdialog = new AlertView("Setting", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        View viewInflate = getLayoutInflater().inflate(R.layout.setting_musicinfo, (ViewGroup) null);
        this.sub = (EditText) viewInflate.findViewById(R.id.music_subedit);
        this.dev = (EditText) viewInflate.findViewById(R.id.music_devedit);
        this.sub.setText(String.valueOf(this.thissecurity.subnetID));
        this.dev.setText(String.valueOf(this.thissecurity.deviceID));
        this.setIDdialog.addExtView(viewInflate);
        this.setIDdialog.setCancelable(false);
        this.setIDdialog.show();
    }

    public void changepassword(View view) {
        this.changepasswordDialog = new AlertView("Change Password", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        View viewInflate = getLayoutInflater().inflate(R.layout.setting_changepassword, (ViewGroup) null);
        this.oldp = (EditText) viewInflate.findViewById(R.id.setting_cpw_oldp);
        this.newp1 = (EditText) viewInflate.findViewById(R.id.setting_cpw_newp1);
        this.newp2 = (EditText) viewInflate.findViewById(R.id.setting_cpw_newp2);
        this.changepasswordDialog.addExtView(viewInflate);
        this.changepasswordDialog.setCancelable(false);
        this.changepasswordDialog.setShoulddismiss(false);
        this.changepasswordDialog.show();
    }

    public void history(View view) {
        this.logDialog = new AlertView(null, null, null, new String[]{"Clear History"}, null, this, AlertView.Style.Alert, this.itemclick);
        View viewInflate = getLayoutInflater().inflate(R.layout.setting_securitylog, (ViewGroup) null);
        this.calendar = Calendar.getInstance();
        this.year = this.calendar.get(1);
        this.month = this.calendar.get(2);
        this.day = this.calendar.get(5);
        this.setting_seclog_startdate = (Button) viewInflate.findViewById(R.id.setting_seclog_startdate);
        this.setting_seclog_startdate.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                new DatePickerDialog(SecuritySettingActivity.this, new DatePickerDialog.OnDateSetListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.1.1
                    @Override // android.app.DatePickerDialog.OnDateSetListener
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        SecuritySettingActivity.this.tv_startdate.setText(i + "-" + (i2 + 1) + "-" + i3);
                    }
                }, SecuritySettingActivity.this.year, SecuritySettingActivity.this.month, SecuritySettingActivity.this.day).show();
            }
        });
        this.setting_seclog_enddate = (Button) viewInflate.findViewById(R.id.setting_seclog_enddate);
        this.setting_seclog_enddate.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                new DatePickerDialog(SecuritySettingActivity.this, new DatePickerDialog.OnDateSetListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.2.1
                    @Override // android.app.DatePickerDialog.OnDateSetListener
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        SecuritySettingActivity.this.tv_enddate.setText(i + "-" + (i2 + 1) + "-" + i3);
                    }
                }, SecuritySettingActivity.this.year, SecuritySettingActivity.this.month, SecuritySettingActivity.this.day).show();
            }
        });
        this.setting_seclog_reflash = (Button) viewInflate.findViewById(R.id.setting_seclog_reflash);
        this.setting_seclog_reflash.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (SecuritySettingActivity.this.logdatalist.size() > 0) {
                    SecuritySettingActivity.this.logdatalist.clear();
                }
                SecuritySettingActivity.this.readinglog.setProgress(0);
                SecuritySettingActivity.this.readinglog.show();
                SecuritySettingActivity.this.secc.ReadLogSum((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.tv_startdate.getText().toString().trim(), SecuritySettingActivity.this.tv_enddate.getText().toString().trim(), MainActivity.mydupsocket);
            }
        });
        this.setting_seclog_clean = (Button) viewInflate.findViewById(R.id.setting_seclog_clearlog);
        this.setting_seclog_clean.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SecuritySettingActivity.this.secc.ClearLog((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, MainActivity.mydupsocket);
            }
        });
        this.setting_seclog_exit = (Button) viewInflate.findViewById(R.id.setting_seclog_exit);
        this.setting_seclog_exit.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SecuritySettingActivity.this.logDialog.dismiss();
            }
        });
        this.tv_startdate = (TextView) viewInflate.findViewById(R.id.setting_seclog_tvstartdate);
        this.tv_enddate = (TextView) viewInflate.findViewById(R.id.setting_seclog_tvenddate);
        this.loglist = (ListView) viewInflate.findViewById(R.id.setting_seclog_content);
        this.tv_startdate.setText(this.year + "-" + (this.month + 1) + "-" + this.day);
        this.tv_enddate.setText(this.year + "-" + (this.month + 1) + "-" + this.day);
        if (this.logdatalist.size() > 0) {
            this.madapter = new SecurityLogAdapter(this, this.logdatalist);
            this.loglist.setAdapter((ListAdapter) this.madapter);
        }
        this.logDialog.addExtView(viewInflate);
        this.logDialog.show();
    }

    public void setareaname(View view) {
        if (this.areanamelist.size() > 0) {
            this.areanamelist.clear();
        }
        if (this.securityarealist.size() > 0) {
            this.securityarealist.clear();
        }
        this.areanameDialog = new AlertView("Set Area Name", null, "CANCEL", new String[]{"SAVE"}, null, this, AlertView.Style.Alert, this.itemclick);
        View viewInflate = getLayoutInflater().inflate(R.layout.alertview_setareaname, (ViewGroup) null);
        this.bypass = (Switch) viewInflate.findViewById(R.id.setareaname_bypass);
        this.watchdog = (Switch) viewInflate.findViewById(R.id.setareaname_watchdog);
        WheelView wheelView = (WheelView) viewInflate.findViewById(R.id.setareaname_arealist);
        wheelView.setOffset(1);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.SecuritySettingActivity.6
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                SecuritySettingActivity.this.selectArea = i;
                SecuritySettingActivity.this.secc.ReadBypass((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.selectArea, MainActivity.mydupsocket);
                new Thread(SecuritySettingActivity.this.new Udpsent()).start();
            }
        });
        this.securityarealist = MainActivity.mgr.querysecurityarea();
        int i = 0;
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
        wheelView.setItems(this.areanamelist);
        this.newAreaname = (EditText) viewInflate.findViewById(R.id.setareaname_edname);
        this.areanameDialog.addExtView(viewInflate);
        this.areanameDialog.show();
    }

    public void RunReceiveData(byte[] bArr) {
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (this.doing) {
            return;
        }
        this.doing = true;
        if ((bArr[17] & 255) == this.thissecurity.subnetID && (bArr[18] & 255) == this.thissecurity.deviceID) {
            if (i == 303) {
                if ((bArr[26] & 255) == this.selectArea) {
                    switch (bArr[28] & 255) {
                        case 0:
                            this.bypass.setChecked(true);
                            break;
                        case 1:
                            this.bypass.setChecked(false);
                            break;
                    }
                }
            } else if (i == 307) {
                if ((bArr[26] & 255) == this.selectArea) {
                    switch (bArr[28] & 255) {
                        case 0:
                            this.watchdog.setChecked(true);
                            break;
                        case 1:
                            this.watchdog.setChecked(false);
                            break;
                    }
                }
                this.indata[0] = bArr[27];
                int i2 = 0;
                while (i2 < 9) {
                    int i3 = i2 + 1;
                    this.indata[i3] = bArr[i2 + 29];
                    i2 = i3;
                }
            } else if (i == 313) {
                this.logsumcount = (bArr[25] << 8) + (bArr[26] & 255);
                this.logvalue[0] = bArr[27];
                this.logvalue[1] = bArr[28];
                if (this.logsumcount > 0) {
                    this.getloghandler.postDelayed(this.getlogrun, 50L);
                } else {
                    Toast.makeText(this, "Nothing in the log", 0).show();
                }
            } else if (i == 315 && (bArr[26] << 8) + (bArr[27] & 255) == this.sentcount) {
                seclogdata seclogdataVar = new seclogdata();
                seclogdataVar.lognum = this.sentcount;
                seclogdataVar.Date = String.valueOf(bArr[29] & 255) + "-" + String.valueOf(bArr[30] & 255) + "-" + String.valueOf(bArr[31] & 255);
                seclogdataVar.Time = String.valueOf(bArr[32] & 255) + ":" + String.valueOf(bArr[33] & 255) + ":" + String.valueOf(bArr[34] & 255);
                StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(bArr[35] & 255));
                sb.append("-");
                sb.append(String.valueOf(bArr[36] & 255));
                seclogdataVar.Address = sb.toString();
                seclogdataVar.Channel = String.valueOf(bArr[38] & 255);
                switch (bArr[37]) {
                    case 1:
                        seclogdataVar.Type = "vacation";
                        break;
                    case 2:
                        seclogdataVar.Type = "Away";
                        break;
                    case 3:
                        seclogdataVar.Type = "Night";
                        break;
                    case 4:
                        seclogdataVar.Type = "Night with visitor";
                        break;
                    case 5:
                        seclogdataVar.Type = "Day";
                        break;
                    case 6:
                        seclogdataVar.Type = "Disarm";
                        break;
                }
                this.logdatalist.add(seclogdataVar);
                this.readinglog.setProgress((this.sentcount * 100) / this.logsumcount);
                this.sentcount++;
            }
        }
        this.doing = false;
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        return intentFilter;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class Udpsent implements Runnable {
        public Udpsent() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                SecuritySettingActivity.this.secc.ReadWatchDog((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.selectArea, MainActivity.mydupsocket);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public class bypasssave implements Runnable {
        public bypasssave() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (SecuritySettingActivity.this.bypass.isChecked()) {
                    SecuritySettingActivity.this.secc.setBypass((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.selectArea, 0, MainActivity.mydupsocket);
                } else {
                    SecuritySettingActivity.this.secc.setBypass((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.selectArea, 1, MainActivity.mydupsocket);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            if (SecuritySettingActivity.this.watchdog.isChecked()) {
                SecuritySettingActivity.this.secc.WriteWatchDog((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.selectArea, 0, SecuritySettingActivity.this.indata, MainActivity.mydupsocket);
            } else {
                SecuritySettingActivity.this.secc.WriteWatchDog((byte) SecuritySettingActivity.this.thissecurity.subnetID, (byte) SecuritySettingActivity.this.thissecurity.deviceID, SecuritySettingActivity.this.selectArea, 1, SecuritySettingActivity.this.indata, MainActivity.mydupsocket);
            }
        }
    }
}
