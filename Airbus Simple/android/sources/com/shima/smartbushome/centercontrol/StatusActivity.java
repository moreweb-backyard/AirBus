package com.shima.smartbushome.centercontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import com.bigkoo.alertview.AlertView;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.statusTypeAdapter;
import com.shima.smartbushome.database.Savestatus;
import com.shima.smartbushome.founction_command.statuscontrol;
import com.shima.smartbushome.selflayout.StatusLayout;
import com.shima.smartbushome.udp.CONST;
import com.shima.smartbushome.udp.udp_socket;
import com.shima.smartbushome.util.SystemUIUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StatusActivity extends AppCompatActivity {
    public static final String ACTION_REFLASH = "com.example.status.REFLASH";
    MenuItem add;
    MenuItem delete;
    private byte[] fanArray;
    private byte[] modeArray;
    RelativeLayout nodeviceinfo;
    int[] readinglist;
    statuscontrol sc;
    ScrollView scrollView9;
    Savestatus sent;
    LinearLayout statuscontent;
    Handler getstatushandler = new Handler();
    Handler getdatahandler = new Handler();
    List<Savestatus> statuslist = new ArrayList();
    List<StatusLayout> viewchildlist = new ArrayList();
    boolean intodeletemode = false;
    Runnable getstatusrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.StatusActivity.1
        @Override // java.lang.Runnable
        public void run() {
            if (StatusActivity.this.statuslist.size() > 0) {
                StatusActivity.this.statuslist.clear();
            }
            if (StatusActivity.this.viewchildlist.size() > 0) {
                StatusActivity.this.viewchildlist.clear();
            }
            StatusActivity.this.statuscontent.removeAllViews();
            StatusActivity.this.statuslist = MainActivity.mgr.querystatus();
            if (StatusActivity.this.statuslist.size() <= 0) {
                StatusActivity.this.nodeviceinfo.setVisibility(0);
                StatusActivity.this.statuscontent.setVisibility(8);
                StatusActivity.this.scrollView9.setVisibility(8);
                return;
            }
            StatusActivity.this.nodeviceinfo.setVisibility(8);
            StatusActivity.this.scrollView9.setVisibility(0);
            StatusActivity.this.statuscontent.setVisibility(0);
            for (int i = 0; i < StatusActivity.this.statuslist.size(); i++) {
                StatusActivity.this.addspecView(StatusActivity.this.statuslist.get(i));
            }
            StatusActivity.this.getstatus();
        }
    };
    int sentcount = 0;
    int oldsentcount = 0;
    int samecommandcount = 0;
    boolean readingfinish = true;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.StatusActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (StatusActivity.this.sentcount >= StatusActivity.this.statuslist.size()) {
                StatusActivity.this.sentcount = 0;
                StatusActivity.this.oldsentcount = 0;
                StatusActivity.this.samecommandcount = 0;
                StatusActivity.this.getdatahandler.removeCallbacks(StatusActivity.this.getdatarun);
                StatusActivity.this.readingfinish = true;
                return;
            }
            StatusActivity.this.sent = StatusActivity.this.statuslist.get(StatusActivity.this.sentcount);
            if (StatusActivity.this.sentcount == StatusActivity.this.oldsentcount) {
                StatusActivity.this.samecommandcount++;
                if (StatusActivity.this.samecommandcount > 10) {
                    StatusActivity.this.sentcount++;
                    StatusActivity.this.samecommandcount = 0;
                }
            } else {
                StatusActivity.this.samecommandcount = 0;
            }
            switch (StatusActivity.this.sent.type) {
                case 1:
                    StatusActivity.this.sc.getlightstate((byte) StatusActivity.this.sent.subnetID, (byte) StatusActivity.this.sent.deviceID, MainActivity.mydupsocket);
                    break;
                case 2:
                    StatusActivity.this.sc.getlightstate((byte) StatusActivity.this.sent.subnetID, (byte) StatusActivity.this.sent.deviceID, MainActivity.mydupsocket);
                    break;
                case 3:
                    StatusActivity.this.sc.ReadTemp((byte) StatusActivity.this.sent.subnetID, (byte) StatusActivity.this.sent.deviceID, (byte) StatusActivity.this.sent.unit, MainActivity.mydupsocket);
                    break;
                case 4:
                    StatusActivity.this.sc.ACReadCFFlag((byte) StatusActivity.this.sent.subnetID, (byte) StatusActivity.this.sent.deviceID, MainActivity.mydupsocket);
                    break;
                case 5:
                    StatusActivity.this.sc.getlightstate((byte) StatusActivity.this.sent.subnetID, (byte) StatusActivity.this.sent.deviceID, MainActivity.mydupsocket);
                    break;
                case 6:
                    StatusActivity.this.sc.ReadTemp((byte) StatusActivity.this.sent.subnetID, (byte) StatusActivity.this.sent.deviceID, (byte) StatusActivity.this.sent.unit, MainActivity.mydupsocket);
                    break;
            }
            StatusActivity.this.oldsentcount = StatusActivity.this.sentcount;
            StatusActivity.this.getdatahandler.postDelayed(StatusActivity.this.getdatarun, 250L);
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.StatusActivity.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    StatusActivity.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                StatusActivity.this.finish();
            } else if (StatusActivity.ACTION_REFLASH.equals(action)) {
                StatusActivity.this.getstatushandler.postDelayed(StatusActivity.this.getstatusrun, 20L);
            }
        }
    };
    boolean doing = false;
    String acunit = "";
    String fanstate = "";
    String modestate = "";
    String actemp = "";
    boolean readCF = false;
    boolean readCountfanAndMode = false;
    boolean readcstate = false;
    public byte CurrentMode = 3;
    public byte CoolTemp = 20;
    public byte HeatTemp = 20;
    public byte AutoTemp = 20;
    public byte CurrentFanMode = 2;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_status);
        SystemUIUtil.setSystemUIVisible(this, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.status_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Status");
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
        initUI();
        this.sc = new statuscontrol();
        this.getstatushandler.postDelayed(this.getstatusrun, 20L);
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
    }

    public void initUI() {
        this.statuscontent = (LinearLayout) findViewById(R.id.statuslinear);
        this.nodeviceinfo = (RelativeLayout) findViewById(R.id.relativeLayout18);
        this.scrollView9 = (ScrollView) findViewById(R.id.scrollView9);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addspecView(Savestatus savestatus) {
        StatusLayout statusLayout = new StatusLayout(this.statuscontent.getContext());
        statusLayout.setcontan(savestatus);
        statusLayout.setId(savestatus.status_id);
        this.statuscontent.addView(statusLayout);
        this.viewchildlist.add(statusLayout);
    }

    public void getstatus() {
        this.readingfinish = false;
        this.readinglist = new int[this.viewchildlist.size()];
        for (int i = 0; i < this.viewchildlist.size(); i++) {
            this.readinglist[i] = 0;
        }
        this.getdatahandler.postDelayed(this.getdatarun, 20L);
    }

    public void addstatusbutton(View view) {
        addstatus();
    }

    public void addstatus() {
        if (this.intodeletemode) {
            this.add.setTitle("ADD");
            this.delete.setTitle("DELETE");
            this.intodeletemode = false;
            for (int i = 0; i < this.viewchildlist.size(); i++) {
                this.viewchildlist.get(i).setdeletevisable(false);
            }
            this.getstatushandler.postDelayed(this.getstatusrun, 20L);
            return;
        }
        final AlertView alertView = new AlertView(null, null, "CANCEL", null, null, this, AlertView.Style.Alert, null);
        ListView listView = new ListView(this);
        listView.setAdapter((ListAdapter) new statusTypeAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.StatusActivity.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                int i3 = StatusActivity.this.statuslist.size() == 0 ? 1 : 1 + StatusActivity.this.statuslist.get(StatusActivity.this.statuslist.size() - 1).status_id;
                switch (i2) {
                    case 0:
                        MainActivity.mgr.addstatus(new Savestatus(i3, "device" + i3, 0, 0, 1, 0, "light", 0));
                        break;
                    case 1:
                        MainActivity.mgr.addstatus(new Savestatus(i3, "device" + i3, 0, 0, 2, 0, "light_type3_on", 0));
                        break;
                    case 2:
                        MainActivity.mgr.addstatus(new Savestatus(i3, "device" + i3, 0, 0, 3, 0, "nio", 0));
                        break;
                    case 3:
                        MainActivity.mgr.addstatus(new Savestatus(i3, "device" + i3, 0, 0, 4, 0, "hvacitem_icon", 0));
                        break;
                    case 4:
                        MainActivity.mgr.addstatus(new Savestatus(i3, "device" + i3, 0, 0, 5, 0, "other", 0));
                        break;
                    case 5:
                        MainActivity.mgr.addstatus(new Savestatus(i3, "device" + i3, 0, 0, 6, 0, "hvac", 0));
                        break;
                }
                StatusActivity.this.getstatushandler.postDelayed(StatusActivity.this.getstatusrun, 20L);
                alertView.dismiss();
            }
        });
        alertView.addExtView(listView);
        alertView.show();
    }

    public void RunReceiveData(byte[] bArr) {
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (this.doing) {
            return;
        }
        this.doing = true;
        if (!this.readingfinish) {
            if (this.sentcount < this.viewchildlist.size() && this.readinglist[this.sentcount] != 1 && (bArr[17] & 255) == this.statuslist.get(this.sentcount).subnetID && (bArr[18] & 255) == this.statuslist.get(this.sentcount).deviceID) {
                if (i == 52) {
                    int i2 = this.statuslist.get(this.sentcount).type;
                    if (i2 != 5) {
                        switch (i2) {
                            case 1:
                                this.viewchildlist.get(this.sentcount).setstatus(((int) bArr[this.viewchildlist.get(this.sentcount).getchannel() + 25]) + "%");
                                break;
                            case 2:
                                this.viewchildlist.get(this.sentcount).setstatuscolor(ToColor(new byte[]{bArr[26], bArr[27], bArr[28], bArr[29]}));
                                break;
                        }
                    } else {
                        String str = "";
                        int i3 = bArr[this.viewchildlist.get(this.sentcount).getchannel() + 25] & 255;
                        if (i3 == 0) {
                            str = CONST.CONST_STR_OFF;
                        } else if (i3 == 100) {
                            str = CONST.CONST_STR_ON;
                        }
                        this.viewchildlist.get(this.sentcount).setstatus(str);
                    }
                    this.readinglist[this.sentcount] = 1;
                    this.sentcount++;
                } else if (i == 57581) {
                    if (!this.readcstate) {
                        this.readcstate = true;
                        byte b = bArr[25];
                        this.CoolTemp = bArr[26];
                        this.HeatTemp = bArr[30];
                        this.AutoTemp = bArr[32];
                        if ((bArr[27] & 15) == this.fanArray.length) {
                            this.CurrentFanMode = this.fanArray[(bArr[27] & 15) - 1];
                        } else {
                            this.CurrentFanMode = this.fanArray[bArr[27] & 15];
                        }
                        switch (this.CurrentFanMode) {
                            case 0:
                                this.fanstate = "Auto speed";
                                break;
                            case 1:
                                this.fanstate = "High speed";
                                break;
                            case 2:
                                this.fanstate = "Medium speed";
                                break;
                            case 3:
                                this.fanstate = "Low speed";
                                break;
                        }
                        if ((bArr[27] >> 4) == this.modeArray.length) {
                            this.CurrentMode = this.modeArray[(bArr[27] >> 4) - 1];
                        } else {
                            this.CurrentMode = this.modeArray[bArr[27] >> 4];
                        }
                        switch (this.CurrentMode) {
                            case 0:
                                this.actemp = String.valueOf(this.CoolTemp & 255);
                                this.modestate = "COOL";
                                break;
                            case 1:
                                this.actemp = String.valueOf(this.HeatTemp & 255);
                                this.modestate = "HEAT";
                                break;
                            case 2:
                                this.modestate = "FAN";
                                break;
                            case 3:
                                this.actemp = String.valueOf(this.AutoTemp & 255);
                                this.modestate = "AUTO";
                                break;
                        }
                    }
                    this.viewchildlist.get(this.sentcount).setstatus((bArr[25] & 255) == 0 ? CONST.CONST_STR_OFF : "ON:/" + this.fanstate + "/" + this.modestate + "/" + this.actemp + this.acunit);
                    this.readinglist[this.sentcount] = 1;
                    this.sentcount++;
                    this.acunit = "";
                    this.fanstate = "";
                    this.modestate = "";
                    this.actemp = "";
                    this.readCF = false;
                    this.readCountfanAndMode = false;
                    this.readcstate = false;
                    this.CurrentMode = (byte) 3;
                    this.CoolTemp = (byte) 20;
                    this.HeatTemp = (byte) 20;
                    this.AutoTemp = (byte) 20;
                    this.CurrentFanMode = (byte) 2;
                } else if (i == 57633) {
                    if (!this.readCF) {
                        switch (bArr[25] % 255) {
                            case 0:
                                this.acunit = " ℃";
                                break;
                            case 1:
                                this.acunit = " ℉";
                                break;
                        }
                        this.readCF = true;
                    }
                    this.sc.ACReadCountFanAndMode((byte) this.sent.subnetID, (byte) this.sent.deviceID, MainActivity.mydupsocket);
                } else if (i == 57637) {
                    if (!this.readCountfanAndMode) {
                        this.readCountfanAndMode = true;
                        this.fanArray = new byte[bArr[25]];
                        for (int i4 = 0; i4 < this.fanArray.length; i4++) {
                            this.fanArray[i4] = bArr[i4 + 26];
                        }
                        int length = this.fanArray.length + 26;
                        for (int i5 = 0; i5 < 10; i5++) {
                            if (bArr[length] <= 32 && bArr[length] != 0) {
                                if (bArr[length] < 6) {
                                    break;
                                }
                            } else {
                                length++;
                            }
                        }
                        this.modeArray = new byte[bArr[length]];
                        for (int i6 = 0; i6 < this.modeArray.length; i6++) {
                            this.modeArray[i6] = bArr[length + 1 + i6];
                        }
                    }
                    this.sc.ACReadCState((byte) this.sent.subnetID, (byte) this.sent.deviceID, MainActivity.mydupsocket);
                } else if (i == 58344) {
                    int i7 = this.statuslist.get(this.sentcount).type;
                    if (i7 == 3) {
                        String str2 = "";
                        String str3 = "";
                        switch (bArr[25] % 255) {
                            case 0:
                                str3 = " ℉";
                                break;
                            case 1:
                                str3 = " ℃";
                                break;
                        }
                        if (bArr.length > 35) {
                            switch (bArr[34] & 255) {
                                case 0:
                                    str2 = "" + (bArr[26] & 255) + str3;
                                    break;
                                case 1:
                                    str2 = "-" + (bArr[26] & 255) + str3;
                                    break;
                            }
                        } else {
                            str2 = "" + (bArr[26] & 255) + str3;
                        }
                        this.viewchildlist.get(this.sentcount).setstatus(str2);
                    } else if (i7 == 6) {
                        String str4 = "";
                        String str5 = "";
                        int i8 = this.viewchildlist.get(this.sentcount).getchannel();
                        switch (bArr[25] % 255) {
                            case 0:
                                str5 = " ℉";
                                break;
                            case 1:
                                str5 = " ℃";
                                break;
                        }
                        switch (bArr[i8 + 33] & 255) {
                            case 0:
                                str4 = "" + (bArr[i8 + 25] & 255) + str5;
                                break;
                            case 1:
                                str4 = "-" + (bArr[i8 + 25] & 255) + str5;
                                break;
                        }
                        this.viewchildlist.get(this.sentcount).setstatus(str4);
                    }
                    this.readinglist[this.sentcount] = 1;
                    this.sentcount++;
                }
            }
        } else {
            boolean z = false;
            for (int i9 = 0; i9 < this.statuslist.size(); i9++) {
                if ((bArr[17] & 255) == this.statuslist.get(i9).subnetID && (bArr[18] & 255) == this.statuslist.get(i9).deviceID) {
                    switch (this.statuslist.get(i9).type) {
                        case 1:
                            if ((bArr[25] & 255) == this.viewchildlist.get(i9).getchannel() && i == 50) {
                                this.viewchildlist.get(i9).setstatus((bArr[27] & 255) + "%");
                                z = true;
                                break;
                            }
                            break;
                        case 3:
                            if (i == 58344) {
                                String str6 = "";
                                String str7 = "";
                                switch (bArr[25] % 255) {
                                    case 0:
                                        str7 = " ℉";
                                        break;
                                    case 1:
                                        str7 = " ℃";
                                        break;
                                }
                                if (bArr.length > 35) {
                                    switch (bArr[34] & 255) {
                                        case 0:
                                            str6 = "" + (bArr[26] & 255) + str7;
                                            break;
                                        case 1:
                                            str6 = "-" + (bArr[26] & 255) + str7;
                                            break;
                                    }
                                } else {
                                    str6 = "" + (bArr[26] & 255) + str7;
                                }
                                this.viewchildlist.get(i9).setstatus(str6);
                                z = true;
                                break;
                            }
                            break;
                        case 5:
                            if ((bArr[25] & 255) == this.viewchildlist.get(i9).getchannel() && i == 50) {
                                String str8 = "";
                                int i10 = bArr[27] & 255;
                                if (i10 == 0) {
                                    str8 = CONST.CONST_STR_OFF;
                                } else if (i10 == 100) {
                                    str8 = CONST.CONST_STR_ON;
                                }
                                this.viewchildlist.get(i9).setstatus(str8);
                                z = true;
                                break;
                            }
                            break;
                        case 6:
                            if (i == 58344) {
                                String str9 = "";
                                String str10 = "";
                                int i11 = this.viewchildlist.get(i9).getchannel();
                                switch (bArr[25] % 255) {
                                    case 0:
                                        str10 = " ℉";
                                        break;
                                    case 1:
                                        str10 = " ℃";
                                        break;
                                }
                                switch (bArr[i11 + 33] & 255) {
                                    case 0:
                                        str9 = "" + (bArr[i11 + 25] & 255) + str10;
                                        break;
                                    case 1:
                                        str9 = "-" + (bArr[i11 + 25] & 255) + str10;
                                        break;
                                }
                                this.viewchildlist.get(i9).setstatus(str9);
                                z = true;
                                break;
                            }
                            break;
                    }
                    if (z) {
                    }
                }
            }
        }
        this.doing = false;
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(ACTION_REFLASH);
        return intentFilter;
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.light_setting_menu, menu);
        this.add = menu.findItem(R.id.light_add);
        this.delete = menu.findItem(R.id.light_remove);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
        } else {
            switch (itemId) {
                case R.id.light_add /* 2131231274 */:
                    addstatus();
                    break;
                case R.id.light_remove /* 2131231275 */:
                    this.intodeletemode = !this.intodeletemode;
                    if (this.intodeletemode) {
                        this.add.setTitle("CANCLE DELETE");
                        this.delete.setTitle("DELETE");
                        if (this.viewchildlist.size() > 0) {
                            this.viewchildlist.clear();
                        }
                        for (int i = 0; i < this.statuslist.size(); i++) {
                            StatusLayout statusLayout = (StatusLayout) this.statuscontent.findViewById(this.statuslist.get(i).status_id);
                            this.viewchildlist.add(statusLayout);
                            statusLayout.setdeletevisable(true);
                        }
                        break;
                    } else {
                        for (int i2 = 0; i2 < this.viewchildlist.size(); i2++) {
                            if (this.viewchildlist.get(i2).getIfneedtoDelete()) {
                                MainActivity.mgr.deletestatus("status", this.viewchildlist.get(i2).getstatusid());
                            }
                        }
                        this.getstatushandler.postDelayed(this.getstatusrun, 20L);
                        this.add.setTitle("ADD");
                        this.delete.setTitle("DELETE");
                        this.intodeletemode = false;
                        break;
                    }
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public int ToColor(byte[] bArr) {
        int i = ((bArr[0] & 255) * 255) / 100;
        int i2 = ((bArr[1] & 255) * 255) / 100;
        int i3 = ((bArr[2] & 255) * 255) / 100;
        String hexString = Integer.toHexString(i);
        switch (hexString.length()) {
            case 0:
                hexString = "00";
                break;
            case 1:
                hexString = "0" + hexString;
                break;
        }
        String hexString2 = Integer.toHexString(i2);
        switch (hexString2.length()) {
            case 0:
                hexString2 = "00";
                break;
            case 1:
                hexString2 = "0" + hexString2;
                break;
        }
        String hexString3 = Integer.toHexString(i3);
        switch (hexString3.length()) {
            case 0:
                hexString3 = "00";
                break;
            case 1:
                hexString3 = "0" + hexString3;
                break;
        }
        return Color.argb(255, Integer.parseInt(hexString, 16), Integer.parseInt(hexString2, 16), Integer.parseInt(hexString3, 16));
    }
}
