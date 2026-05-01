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
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.founction_command.curtaincontrol;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AllCurtainActivity extends AppCompatActivity {
    TextView allcurtainselectroom;
    WheelView wva;
    curtaincontrol cc = new curtaincontrol();
    List<Saveroom> roomlist = new ArrayList();
    List<String> roomnamelist = new ArrayList();
    List<Savecurtain> allcurtainlist = new ArrayList();
    Handler getdatahandler = new Handler();
    Handler senthandler = new Handler();
    String selectroom = "";
    String value = "";
    boolean senting = false;
    boolean allcurtain = false;
    int roomid = 0;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllCurtainActivity.2
        @Override // java.lang.Runnable
        public void run() {
            AllCurtainActivity.this.roomlist = MainActivity.mgr.queryroom();
            for (int i = 0; i < AllCurtainActivity.this.roomlist.size(); i++) {
                AllCurtainActivity.this.roomnamelist.add(AllCurtainActivity.this.roomlist.get(i).room_name);
            }
            AllCurtainActivity.this.roomnamelist.add("All Curtain");
            AllCurtainActivity.this.wva.setItems(AllCurtainActivity.this.roomnamelist);
            AllCurtainActivity.this.selectroom = AllCurtainActivity.this.roomnamelist.get(0);
            AllCurtainActivity.this.allcurtainselectroom.setText("Select Room: " + AllCurtainActivity.this.selectroom);
            AllCurtainActivity.this.allcurtainlist = MainActivity.mgr.querycurtain();
        }
    };
    int count = 0;
    Runnable senrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllCurtainActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (AllCurtainActivity.this.count >= AllCurtainActivity.this.allcurtainlist.size()) {
                AllCurtainActivity.this.count = 0;
                AllCurtainActivity.this.senting = false;
                AllCurtainActivity.this.allcurtain = false;
                Toast.makeText(AllCurtainActivity.this, "finished", 0).show();
                AllCurtainActivity.this.senthandler.removeCallbacks(AllCurtainActivity.this.senrun);
                return;
            }
            if (AllCurtainActivity.this.allcurtain || AllCurtainActivity.this.allcurtainlist.get(AllCurtainActivity.this.count).room_id == AllCurtainActivity.this.roomid) {
                AllCurtainActivity.this.cc.CurtainControl((byte) AllCurtainActivity.this.allcurtainlist.get(AllCurtainActivity.this.count).subnetID, (byte) AllCurtainActivity.this.allcurtainlist.get(AllCurtainActivity.this.count).deviceID, AllCurtainActivity.this.allcurtainlist.get(AllCurtainActivity.this.count).channel_1, AllCurtainActivity.this.allcurtainlist.get(AllCurtainActivity.this.count).channel_2, AllCurtainActivity.this.value, MainActivity.mydupsocket);
            }
            AllCurtainActivity.this.count++;
            AllCurtainActivity.this.senthandler.postDelayed(AllCurtainActivity.this.senrun, 70L);
        }
    };

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_curtain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.allcurtain_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("All Curtain");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        ((RelativeLayout) findViewById(R.id.allcurtainout)).setBackgroundColor(getSharedPreferences("xxx", 0).getInt("xxx", ViewCompat.MEASURED_STATE_MASK));
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        this.wva = (WheelView) findViewById(R.id.curtainroomchoose);
        this.allcurtainselectroom = (TextView) findViewById(R.id.allcurtainselectroom);
        this.wva.setOffset(1);
        this.wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.AllCurtainActivity.1
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                AllCurtainActivity.this.allcurtainselectroom.setText("Select Room: " + str);
                AllCurtainActivity.this.selectroom = str;
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

    public void allopen(View view) {
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
        if (this.selectroom.equals("All Curtain")) {
            this.allcurtain = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = "open";
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allclose(View view) {
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
        if (this.selectroom.equals("All Curtain")) {
            this.allcurtain = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = "close";
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allstop(View view) {
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
        if (this.selectroom.equals("All Curtain")) {
            this.allcurtain = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = "stop";
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
