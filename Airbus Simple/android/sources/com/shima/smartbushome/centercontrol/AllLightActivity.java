package com.shima.smartbushome.centercontrol;

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
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.WheelView;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.selflayout.LightType1;
import com.shima.smartbushome.selflayout.LightType2;
import com.shima.smartbushome.selflayout.LightType3;
import com.shima.smartbushome.selflayout.LightType4;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AllLightActivity extends AppCompatActivity {
    TextView alllightselectroom;
    Button button8;
    LinearLayout lightcontrollayout;
    WheelView wva;
    lightcontrol lg = new lightcontrol();
    List<Saveroom> roomlist = new ArrayList();
    List<String> roomnamelist = new ArrayList();
    List<Savelight> alllightlist = new ArrayList();
    Handler getdatahandler = new Handler();
    Handler senthandler = new Handler();
    String selectroom = "";
    int value = 0;
    boolean senting = false;
    boolean alllight = false;
    int roomid = 0;
    public List<LightType1> type1list = new ArrayList();
    public List<LightType2> type2list = new ArrayList();
    public List<LightType3> type3list = new ArrayList();
    public List<LightType4> type4list = new ArrayList();
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllLightActivity.2
        @Override // java.lang.Runnable
        public void run() {
            AllLightActivity.this.roomlist = MainActivity.mgr.queryroom();
            for (int i = 0; i < AllLightActivity.this.roomlist.size(); i++) {
                AllLightActivity.this.roomnamelist.add(AllLightActivity.this.roomlist.get(i).room_name);
            }
            AllLightActivity.this.roomnamelist.add("All Light");
            AllLightActivity.this.wva.setItems(AllLightActivity.this.roomnamelist);
            AllLightActivity.this.selectroom = AllLightActivity.this.roomnamelist.get(0);
            AllLightActivity.this.alllightselectroom.setText("Select Room: " + AllLightActivity.this.selectroom);
            AllLightActivity.this.alllightlist = MainActivity.mgr.querylight();
            AllLightActivity.this.lightcontrollayout.removeAllViews();
            for (int i2 = 0; i2 < AllLightActivity.this.alllightlist.size(); i2++) {
                AllLightActivity.this.addspecView(AllLightActivity.this.alllightlist.get(i2));
            }
            AllLightActivity.this.reflashroomlight();
            AllLightActivity.this.getdatahandler.removeCallbacks(AllLightActivity.this.getdatarun);
        }
    };
    Handler reflashroomlight = new Handler();
    byte sub = 0;
    byte dev = 0;
    byte getlightcount = 0;
    boolean getstatefinish = false;
    int getcount = 0;
    int getcount2 = 0;
    int timeout = 0;
    Runnable reflashroomlightrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllLightActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (AllLightActivity.this.getlightcount < AllLightActivity.this.alllightlist.size()) {
                if (AllLightActivity.this.alllightlist.get(AllLightActivity.this.getlightcount).subnetID != (AllLightActivity.this.sub & 255)) {
                    AllLightActivity.this.sub = (byte) AllLightActivity.this.alllightlist.get(AllLightActivity.this.getlightcount).subnetID;
                    AllLightActivity.this.dev = (byte) AllLightActivity.this.alllightlist.get(AllLightActivity.this.getlightcount).deviceID;
                    AllLightActivity.this.lg.getlightstate(AllLightActivity.this.sub, AllLightActivity.this.dev, MainActivity.mydupsocket);
                    AllLightActivity.this.getcount++;
                } else if (AllLightActivity.this.alllightlist.get(AllLightActivity.this.getlightcount).deviceID != (AllLightActivity.this.dev & 255)) {
                    AllLightActivity.this.sub = (byte) AllLightActivity.this.alllightlist.get(AllLightActivity.this.getlightcount).subnetID;
                    AllLightActivity.this.dev = (byte) AllLightActivity.this.alllightlist.get(AllLightActivity.this.getlightcount).deviceID;
                    AllLightActivity.this.lg.getlightstate(AllLightActivity.this.sub, AllLightActivity.this.dev, MainActivity.mydupsocket);
                    AllLightActivity.this.getcount++;
                }
                AllLightActivity allLightActivity = AllLightActivity.this;
                allLightActivity.getlightcount = (byte) (allLightActivity.getlightcount + 1);
                AllLightActivity.this.reflashroomlight.postDelayed(AllLightActivity.this.reflashroomlightrun, 200L);
                return;
            }
            if (AllLightActivity.this.getcount2 != AllLightActivity.this.getcount) {
                AllLightActivity.this.sub = (byte) 0;
                AllLightActivity.this.dev = (byte) 0;
                AllLightActivity.this.getlightcount = (byte) 0;
                AllLightActivity.this.getcount = 0;
                AllLightActivity.this.getcount2 = 0;
                AllLightActivity.this.timeout++;
                if (AllLightActivity.this.timeout >= 10) {
                    AllLightActivity.this.reflashroomlight.removeCallbacks(AllLightActivity.this.reflashroomlightrun);
                    AllLightActivity.this.sub = (byte) 0;
                    AllLightActivity.this.dev = (byte) 0;
                    AllLightActivity.this.getlightcount = (byte) 0;
                    AllLightActivity.this.getcount = 0;
                    AllLightActivity.this.getcount2 = 0;
                    AllLightActivity.this.timeout = 0;
                    return;
                }
                AllLightActivity.this.reflashroomlight();
                return;
            }
            AllLightActivity.this.reflashroomlight.removeCallbacks(AllLightActivity.this.reflashroomlightrun);
            AllLightActivity.this.sub = (byte) 0;
            AllLightActivity.this.dev = (byte) 0;
            AllLightActivity.this.getlightcount = (byte) 0;
            AllLightActivity.this.getcount = 0;
            AllLightActivity.this.getcount2 = 0;
            AllLightActivity.this.timeout = 0;
        }
    };
    int count = 0;
    Runnable senrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllLightActivity.4
        @Override // java.lang.Runnable
        public void run() {
            if (AllLightActivity.this.count >= AllLightActivity.this.alllightlist.size()) {
                AllLightActivity.this.count = 0;
                AllLightActivity.this.senting = false;
                AllLightActivity.this.alllight = false;
                Toast.makeText(AllLightActivity.this, "finished", 0).show();
                AllLightActivity.this.senthandler.removeCallbacks(AllLightActivity.this.senrun);
                return;
            }
            if (AllLightActivity.this.alllight || AllLightActivity.this.alllightlist.get(AllLightActivity.this.count).room_id == AllLightActivity.this.roomid) {
                AllLightActivity.this.lg.SingleChannelControl((byte) AllLightActivity.this.alllightlist.get(AllLightActivity.this.count).subnetID, (byte) AllLightActivity.this.alllightlist.get(AllLightActivity.this.count).deviceID, AllLightActivity.this.alllightlist.get(AllLightActivity.this.count).channel, AllLightActivity.this.value, MainActivity.mydupsocket);
            }
            AllLightActivity.this.count++;
            AllLightActivity.this.senthandler.postDelayed(AllLightActivity.this.senrun, 200L);
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.AllLightActivity.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    AllLightActivity.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                AllLightActivity.this.finish();
                AllLightActivity.this.reflashroomlight.removeCallbacks(AllLightActivity.this.reflashroomlightrun);
            }
        }
    };

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_light);
        Toolbar toolbar = (Toolbar) findViewById(R.id.alllight_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("All Light");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        this.lightcontrollayout = (LinearLayout) findViewById(R.id.alllighcontent);
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        ((RelativeLayout) findViewById(R.id.alllightout)).setBackgroundColor(getSharedPreferences("xxx", 0).getInt("xxx", ViewCompat.MEASURED_STATE_MASK));
        this.wva = (WheelView) findViewById(R.id.lightroomchoose);
        this.alllightselectroom = (TextView) findViewById(R.id.alllightselectroom);
        this.wva.setOffset(1);
        this.wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.AllLightActivity.1
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                AllLightActivity.this.alllightselectroom.setText("Select Room: " + str);
                AllLightActivity.this.selectroom = str;
                AllLightActivity.this.lightcontrollayout.removeAllViews();
                for (int i2 = 0; i2 < AllLightActivity.this.alllightlist.size(); i2++) {
                    int i3 = i - 1;
                    if (i3 == AllLightActivity.this.roomlist.size()) {
                        AllLightActivity.this.addspecView(AllLightActivity.this.alllightlist.get(i2));
                    } else if (i3 < AllLightActivity.this.roomlist.size() && AllLightActivity.this.roomlist.get(i3).room_id == AllLightActivity.this.alllightlist.get(i2).room_id) {
                        AllLightActivity.this.addspecView(AllLightActivity.this.alllightlist.get(i2));
                    }
                }
                AllLightActivity.this.reflashroomlight();
            }
        });
        this.getdatahandler.postDelayed(this.getdatarun, 20L);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void addspecView(Savelight savelight) {
        switch (savelight.lightType) {
            case 1:
                LightType1 lightType1 = new LightType1(this.lightcontrollayout.getContext());
                lightType1.setcontant(savelight);
                lightType1.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType1);
                this.type1list.add(lightType1);
                break;
            case 2:
                LightType2 lightType2 = new LightType2(this.lightcontrollayout.getContext());
                lightType2.setcontant(savelight);
                lightType2.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType2);
                this.type2list.add(lightType2);
                break;
            case 3:
                LightType3 lightType3 = new LightType3(this.lightcontrollayout.getContext());
                lightType3.setcontant(savelight);
                lightType3.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType3);
                this.type3list.add(lightType3);
                break;
            case 4:
                LightType4 lightType4 = new LightType4(this.lightcontrollayout.getContext());
                lightType4.setcontant(savelight);
                lightType4.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType4);
                this.type4list.add(lightType4);
                break;
        }
    }

    public void reflashroomlight() {
        for (int i = 0; i < this.alllightlist.size(); i++) {
            this.sub = (byte) this.alllightlist.get(i).subnetID;
            this.dev = (byte) this.alllightlist.get(i).deviceID;
            this.lg.getlightstate(this.sub, this.dev, MainActivity.mydupsocket);
        }
    }

    public void alllighton(View view) {
        int i = 0;
        while (true) {
            if (i >= this.roomlist.size()) {
                break;
            }
            if (this.roomlist.get(i).room_name.equals(this.selectroom)) {
                this.roomid = this.roomlist.get(i).room_id;
                break;
            }
            i++;
        }
        if (this.selectroom.equals("All Light")) {
            this.alllight = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 100;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void alllightoff(View view) {
        if (this.senting) {
            Toast.makeText(this, "senting command,please wait", 0).show();
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.roomlist.size()) {
                break;
            }
            if (this.roomlist.get(i).room_name.equals(this.selectroom)) {
                this.roomid = this.roomlist.get(i).room_id;
                break;
            }
            i++;
        }
        if (this.selectroom.equals("All Light")) {
            this.alllight = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 0;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void alllight25(View view) {
        if (this.senting) {
            Toast.makeText(this, "senting command,please wait", 0).show();
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.roomlist.size()) {
                break;
            }
            if (this.roomlist.get(i).room_name.equals(this.selectroom)) {
                this.roomid = this.roomlist.get(i).room_id;
                break;
            }
            i++;
        }
        if (this.selectroom.equals("All Light")) {
            this.alllight = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 25;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void alllight50(View view) {
        if (this.senting) {
            Toast.makeText(this, "senting command,please wait", 0).show();
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.roomlist.size()) {
                break;
            }
            if (this.roomlist.get(i).room_name.equals(this.selectroom)) {
                this.roomid = this.roomlist.get(i).room_id;
                break;
            }
            i++;
        }
        if (this.selectroom.equals("All Light")) {
            this.alllight = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 50;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void alllight75(View view) {
        if (this.senting) {
            Toast.makeText(this, "senting command,please wait", 0).show();
            return;
        }
        int i = 0;
        while (true) {
            if (i >= this.roomlist.size()) {
                break;
            }
            if (this.roomlist.get(i).room_name.equals(this.selectroom)) {
                this.roomid = this.roomlist.get(i).room_id;
                break;
            }
            i++;
        }
        if (this.selectroom.equals("All Light")) {
            this.alllight = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 75;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void RunReceiveData(byte[] bArr) {
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (i == 50) {
            if (bArr[26] == -8) {
                int i2 = bArr[17] & 255;
                int i3 = bArr[18] & 255;
                int i4 = 255 & bArr[25];
                LightType3 lightType3 = null;
                LightType1 lightType1 = null;
                for (int i5 = 0; i5 < this.type1list.size(); i5++) {
                    if (this.type1list.get(i5).getsubid() == i2 && this.type1list.get(i5).getdevid() == i3 && this.type1list.get(i5).getchannel() == i4) {
                        lightType1 = this.type1list.get(i5);
                    }
                }
                LightType2 lightType2 = null;
                for (int i6 = 0; i6 < this.type2list.size(); i6++) {
                    if (this.type2list.get(i6).getsubid() == i2 && this.type2list.get(i6).getdevid() == i3 && this.type2list.get(i6).getchannel() == i4) {
                        lightType2 = this.type2list.get(i6);
                    }
                }
                for (int i7 = 0; i7 < this.type3list.size(); i7++) {
                    if (this.type3list.get(i7).getsubid() == i2 && this.type3list.get(i7).getdevid() == i3) {
                        lightType3 = this.type3list.get(i7);
                    }
                }
                if (lightType1 != null) {
                    lightType1.setReceiveChange(bArr[27]);
                    return;
                } else if (lightType2 != null) {
                    lightType2.setReceiveChange(bArr[27]);
                    return;
                } else {
                    if (lightType3 != null) {
                        this.lg.getlightstate((byte) i2, (byte) i3, MainActivity.mydupsocket);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (i != 52) {
            return;
        }
        this.getcount2++;
        int i8 = bArr[17] & 255;
        int i9 = bArr[18] & 255;
        for (int i10 = 0; i10 < this.type1list.size(); i10++) {
            try {
                if (this.type1list.get(i10).getsubid() == i8 && this.type1list.get(i10).getdevid() == i9 && this.type1list.get(i10).getchannel() != 255) {
                    try {
                        this.type1list.get(i10).setReceiveChange(bArr[this.type1list.get(i10).getchannel() + 25]);
                    } catch (Exception unused) {
                        switch (this.type1list.get(i10).getchannel()) {
                            case 49:
                                this.type1list.get(i10).setReceiveChange(bArr[26]);
                                break;
                            case 50:
                                this.type1list.get(i10).setReceiveChange(bArr[27]);
                                break;
                            case 51:
                                this.type1list.get(i10).setReceiveChange(bArr[28]);
                                break;
                            case 52:
                                this.type1list.get(i10).setReceiveChange(bArr[29]);
                                break;
                            default:
                                Toast.makeText(this, "you had set wrong channel", 0).show();
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        for (int i11 = 0; i11 < this.type2list.size(); i11++) {
            if (this.type2list.get(i11).getsubid() == i8 && this.type2list.get(i11).getdevid() == i9 && this.type2list.get(i11).getchannel() != 255) {
                try {
                    this.type2list.get(i11).setReceiveChange(bArr[this.type2list.get(i11).getchannel() + 25]);
                } catch (Exception unused2) {
                    switch (this.type2list.get(i11).getchannel()) {
                        case 49:
                            this.type2list.get(i11).setReceiveChange(bArr[26]);
                            break;
                        case 50:
                            this.type2list.get(i11).setReceiveChange(bArr[27]);
                            break;
                        case 51:
                            this.type2list.get(i11).setReceiveChange(bArr[28]);
                            break;
                        case 52:
                            this.type2list.get(i11).setReceiveChange(bArr[29]);
                            break;
                        default:
                            Toast.makeText(this, "you had set wrong channel", 0).show();
                            break;
                    }
                }
            }
        }
        for (int i12 = 0; i12 < this.type3list.size(); i12++) {
            if (this.type3list.get(i12).getsubid() == i8 && this.type3list.get(i12).getdevid() == i9) {
                this.type3list.get(i12).setReceiveChange(new byte[]{bArr[26], bArr[27], bArr[28], bArr[29]});
            }
        }
        for (int i13 = 0; i13 < this.type4list.size(); i13++) {
            if (this.type4list.get(i13).getsubid() == i8 && this.type4list.get(i13).getdevid() == i9 && this.type4list.get(i13).getchannel() != 255) {
                try {
                    this.type4list.get(i13).setReceiveChange(bArr[this.type4list.get(i13).getchannel() + 25]);
                } catch (Exception unused3) {
                    switch (this.type4list.get(i13).getchannel()) {
                        case 49:
                            this.type4list.get(i13).setReceiveChange(bArr[26]);
                            break;
                        case 50:
                            this.type4list.get(i13).setReceiveChange(bArr[27]);
                            break;
                        case 51:
                            this.type4list.get(i13).setReceiveChange(bArr[28]);
                            break;
                        case 52:
                            this.type4list.get(i13).setReceiveChange(bArr[29]);
                            break;
                        default:
                            Toast.makeText(this, "you had set wrong channel", 0).show();
                            break;
                    }
                }
            }
        }
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_DELETELIGHT);
        intentFilter.addAction(FounctionActivity.ACTION_SHAKE);
        return intentFilter;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
