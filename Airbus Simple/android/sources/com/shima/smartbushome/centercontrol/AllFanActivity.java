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
import com.shima.smartbushome.database.Savefan;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.founction_command.fancontrol;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AllFanActivity extends AppCompatActivity {
    TextView allfanselectroom;
    WheelView wva;
    fancontrol fc = new fancontrol();
    List<Saveroom> roomlist = new ArrayList();
    List<String> roomnamelist = new ArrayList();
    List<Savefan> allfanlist = new ArrayList();
    Handler getdatahandler = new Handler();
    Handler senthandler = new Handler();
    String selectroom = "";
    int value = 0;
    boolean senting = false;
    boolean allfan = false;
    int roomid = 0;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllFanActivity.2
        @Override // java.lang.Runnable
        public void run() {
            AllFanActivity.this.roomlist = MainActivity.mgr.queryroom();
            for (int i = 0; i < AllFanActivity.this.roomlist.size(); i++) {
                AllFanActivity.this.roomnamelist.add(AllFanActivity.this.roomlist.get(i).room_name);
            }
            AllFanActivity.this.roomnamelist.add("All Fan");
            AllFanActivity.this.wva.setItems(AllFanActivity.this.roomnamelist);
            AllFanActivity.this.selectroom = AllFanActivity.this.roomnamelist.get(0);
            AllFanActivity.this.allfanselectroom.setText("Select Room: " + AllFanActivity.this.selectroom);
            AllFanActivity.this.allfanlist = MainActivity.mgr.queryfan();
        }
    };
    int count = 0;
    Runnable senrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllFanActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (AllFanActivity.this.count >= AllFanActivity.this.allfanlist.size()) {
                AllFanActivity.this.count = 0;
                AllFanActivity.this.senting = false;
                AllFanActivity.this.allfan = false;
                Toast.makeText(AllFanActivity.this, "finished", 0).show();
                AllFanActivity.this.senthandler.removeCallbacks(AllFanActivity.this.senrun);
                return;
            }
            if (AllFanActivity.this.allfan || AllFanActivity.this.allfanlist.get(AllFanActivity.this.count).room_id == AllFanActivity.this.roomid) {
                AllFanActivity.this.fc.FanChannelControl((byte) AllFanActivity.this.allfanlist.get(AllFanActivity.this.count).subnetID, (byte) AllFanActivity.this.allfanlist.get(AllFanActivity.this.count).deviceID, (byte) AllFanActivity.this.allfanlist.get(AllFanActivity.this.count).channel, AllFanActivity.this.value, MainActivity.mydupsocket);
            }
            AllFanActivity.this.count++;
            AllFanActivity.this.senthandler.postDelayed(AllFanActivity.this.senrun, 70L);
        }
    };

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_fan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.allfan_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("All Fan");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        ((RelativeLayout) findViewById(R.id.allfanout)).setBackgroundColor(getSharedPreferences("xxx", 0).getInt("xxx", ViewCompat.MEASURED_STATE_MASK));
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        this.wva = (WheelView) findViewById(R.id.fanroomchoose);
        this.allfanselectroom = (TextView) findViewById(R.id.allfanselectroom);
        this.wva.setOffset(1);
        this.wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.AllFanActivity.1
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                AllFanActivity.this.allfanselectroom.setText("Select Room: " + str);
                AllFanActivity.this.selectroom = str;
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

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    public void allfanoff(View view) {
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
        if (this.selectroom.equals("All Fan")) {
            this.allfan = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 0;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allfanfull(View view) {
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
        if (this.selectroom.equals("All Fan")) {
            this.allfan = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 4;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allfanlow(View view) {
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
        if (this.selectroom.equals("All Fan")) {
            this.allfan = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 1;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allfanmid(View view) {
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
        if (this.selectroom.equals("All Fan")) {
            this.allfan = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 2;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allfanhigh(View view) {
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
        if (this.selectroom.equals("All Fan")) {
            this.allfan = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 3;
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
