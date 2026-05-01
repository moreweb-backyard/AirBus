package com.shima.smartbushome.centercontrol;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.WheelView;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.founction_command.niocontrol;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AllHVACActivity extends AppCompatActivity {
    private static final byte const_ac_cmd_type_onoff = 3;
    private static final byte const_ac_cmd_type_set_auto_tmp = 8;
    private static final byte const_ac_cmd_type_set_cold_tmp = 4;
    private static final byte const_ac_cmd_type_set_fan = 5;
    private static final byte const_ac_cmd_type_set_heat_tmp = 7;
    private static final byte const_ac_cmd_type_set_mode = 6;
    private static final byte const_mode_auto = 3;
    private static final byte const_mode_cool = 0;
    private static final byte const_mode_fan = 2;
    private static final byte const_mode_heat = 1;
    TextView allhvacselectroom;
    WheelView wva;
    ACcontrol ac = new ACcontrol();
    niocontrol nio = new niocontrol();
    List<Saveroom> roomlist = new ArrayList();
    List<String> roomnamelist = new ArrayList();
    List<Savehvac> allhvaclist = new ArrayList();
    Handler getdatahandler = new Handler();
    Handler senthandler = new Handler();
    String selectroom = "";
    int controltype = 0;
    boolean senting = false;
    boolean allac = false;
    int roomid = 0;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllHVACActivity.2
        @Override // java.lang.Runnable
        public void run() {
            AllHVACActivity.this.roomlist = MainActivity.mgr.queryroom();
            for (int i = 0; i < AllHVACActivity.this.roomlist.size(); i++) {
                AllHVACActivity.this.roomnamelist.add(AllHVACActivity.this.roomlist.get(i).room_name);
            }
            AllHVACActivity.this.roomnamelist.add("All hvac");
            AllHVACActivity.this.wva.setItems(AllHVACActivity.this.roomnamelist);
            AllHVACActivity.this.selectroom = AllHVACActivity.this.roomnamelist.get(0);
            AllHVACActivity.this.allhvacselectroom.setText("Select Room: " + AllHVACActivity.this.selectroom);
            AllHVACActivity.this.allhvaclist = MainActivity.mgr.queryhvac();
        }
    };
    int count = 0;
    Runnable senrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllHVACActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (AllHVACActivity.this.count >= AllHVACActivity.this.allhvaclist.size()) {
                AllHVACActivity.this.count = 0;
                AllHVACActivity.this.senting = false;
                AllHVACActivity.this.allac = false;
                AllHVACActivity.this.controltype = 0;
                Toast.makeText(AllHVACActivity.this, "finished", 0).show();
                AllHVACActivity.this.senthandler.removeCallbacks(AllHVACActivity.this.senrun);
                return;
            }
            if (AllHVACActivity.this.allac) {
                switch (AllHVACActivity.this.controltype) {
                    case 1:
                        AllHVACActivity.this.nio.IRControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 1, MainActivity.mydupsocket);
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 3, 1, MainActivity.mydupsocket);
                        break;
                    case 2:
                        AllHVACActivity.this.nio.IRControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 2, MainActivity.mydupsocket);
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 3, 0, MainActivity.mydupsocket);
                        break;
                    case 3:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 0, MainActivity.mydupsocket);
                        break;
                    case 4:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 1, MainActivity.mydupsocket);
                        break;
                    case 5:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 2, MainActivity.mydupsocket);
                        break;
                    case 6:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 3, MainActivity.mydupsocket);
                        break;
                }
            } else if (AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).room_id == AllHVACActivity.this.roomid) {
                switch (AllHVACActivity.this.controltype) {
                    case 1:
                        AllHVACActivity.this.nio.IRControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 1, MainActivity.mydupsocket);
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 3, 1, MainActivity.mydupsocket);
                        break;
                    case 2:
                        AllHVACActivity.this.nio.IRControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 2, MainActivity.mydupsocket);
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 3, 0, MainActivity.mydupsocket);
                        break;
                    case 3:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 0, MainActivity.mydupsocket);
                        break;
                    case 4:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 1, MainActivity.mydupsocket);
                        break;
                    case 5:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 2, MainActivity.mydupsocket);
                        break;
                    case 6:
                        AllHVACActivity.this.ac.ACControl((byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).subnetID, (byte) AllHVACActivity.this.allhvaclist.get(AllHVACActivity.this.count).deviceID, 6, 3, MainActivity.mydupsocket);
                        break;
                }
            }
            AllHVACActivity.this.count++;
            AllHVACActivity.this.senthandler.postDelayed(AllHVACActivity.this.senrun, 70L);
        }
    };

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_hvac);
        Toolbar toolbar = (Toolbar) findViewById(R.id.allhvac_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("All HVAC");
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
        ((RelativeLayout) findViewById(R.id.allhvacout)).setBackgroundColor(getSharedPreferences("xxx", 0).getInt("xxx", ViewCompat.MEASURED_STATE_MASK));
        this.wva = (WheelView) findViewById(R.id.hvacroomchoose);
        this.allhvacselectroom = (TextView) findViewById(R.id.allhvacselectroom);
        this.wva.setOffset(1);
        this.wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.AllHVACActivity.1
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                AllHVACActivity.this.allhvacselectroom.setText("Select Room: " + str);
                AllHVACActivity.this.selectroom = str;
            }
        });
        if (MainActivity.mydupsocket != null) {
            MainActivity.mydupsocket.initprocess();
        } else {
            finish();
        }
        this.getdatahandler.postDelayed(this.getdatarun, 20L);
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        MainActivity.mydupsocket.StopAllThread();
    }

    public void allacon(View view) {
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
        if (this.selectroom.equals("All hvac")) {
            this.allac = true;
        }
        this.count = 0;
        this.senting = true;
        this.controltype = 1;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allacoff(View view) {
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
        if (this.selectroom.equals("All hvac")) {
            this.allac = true;
        }
        this.count = 0;
        this.senting = true;
        this.controltype = 2;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allaccool(View view) {
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
        if (this.selectroom.equals("All hvac")) {
            this.allac = true;
        }
        this.count = 0;
        this.senting = true;
        this.controltype = 3;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allacheat(View view) {
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
        if (this.selectroom.equals("All hvac")) {
            this.allac = true;
        }
        this.count = 0;
        this.senting = true;
        this.controltype = 4;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allacfan(View view) {
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
        if (this.selectroom.equals("All hvac")) {
            this.allac = true;
        }
        this.count = 0;
        this.senting = true;
        this.controltype = 5;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allacauto(View view) {
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
        if (this.selectroom.equals("All hvac")) {
            this.allac = true;
        }
        this.count = 0;
        this.senting = true;
        this.controltype = 6;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
