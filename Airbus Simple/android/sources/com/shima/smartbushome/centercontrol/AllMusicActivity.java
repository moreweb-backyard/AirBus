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
import com.shima.smartbushome.database.Savemusic;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.founction_command.musiccontrol;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AllMusicActivity extends AppCompatActivity {
    TextView allmusicselectroom;
    WheelView wva;
    musiccontrol mc = new musiccontrol();
    List<Saveroom> roomlist = new ArrayList();
    List<String> roomnamelist = new ArrayList();
    List<Savemusic> allmusiclist = new ArrayList();
    Handler getdatahandler = new Handler();
    Handler senthandler = new Handler();
    String selectroom = "";
    int value1 = 0;
    int value2 = 0;
    int value3 = 0;
    int value4 = 0;
    boolean senting = false;
    boolean allmusic = false;
    int roomid = 0;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllMusicActivity.2
        @Override // java.lang.Runnable
        public void run() {
            AllMusicActivity.this.roomlist = MainActivity.mgr.queryroom();
            for (int i = 0; i < AllMusicActivity.this.roomlist.size(); i++) {
                AllMusicActivity.this.roomnamelist.add(AllMusicActivity.this.roomlist.get(i).room_name);
            }
            AllMusicActivity.this.roomnamelist.add("All Music");
            AllMusicActivity.this.wva.setItems(AllMusicActivity.this.roomnamelist);
            AllMusicActivity.this.selectroom = AllMusicActivity.this.roomnamelist.get(0);
            AllMusicActivity.this.allmusicselectroom.setText("Select Room: " + AllMusicActivity.this.selectroom);
            AllMusicActivity.this.allmusiclist = MainActivity.mgr.querymusic();
        }
    };
    int count = 0;
    Runnable senrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllMusicActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (AllMusicActivity.this.count >= AllMusicActivity.this.allmusiclist.size()) {
                AllMusicActivity.this.count = 0;
                AllMusicActivity allMusicActivity = AllMusicActivity.this;
                AllMusicActivity allMusicActivity2 = AllMusicActivity.this;
                AllMusicActivity allMusicActivity3 = AllMusicActivity.this;
                AllMusicActivity.this.value4 = 0;
                allMusicActivity3.value3 = 0;
                allMusicActivity2.value2 = 0;
                allMusicActivity.value1 = 0;
                AllMusicActivity.this.senting = false;
                AllMusicActivity.this.allmusic = false;
                Toast.makeText(AllMusicActivity.this, "finished", 0).show();
                AllMusicActivity.this.senthandler.removeCallbacks(AllMusicActivity.this.senrun);
                return;
            }
            if (AllMusicActivity.this.allmusic || AllMusicActivity.this.allmusiclist.get(AllMusicActivity.this.count).room_id == AllMusicActivity.this.roomid) {
                AllMusicActivity.this.mc.MusicControl((byte) AllMusicActivity.this.value1, (byte) AllMusicActivity.this.value2, (byte) AllMusicActivity.this.value3, (byte) AllMusicActivity.this.value4, (byte) AllMusicActivity.this.allmusiclist.get(AllMusicActivity.this.count).subnetID, (byte) AllMusicActivity.this.allmusiclist.get(AllMusicActivity.this.count).deviceID, MainActivity.mydupsocket);
            }
            AllMusicActivity.this.count++;
            AllMusicActivity.this.senthandler.postDelayed(AllMusicActivity.this.senrun, 70L);
        }
    };

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.allmusic_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("All Music");
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
        ((RelativeLayout) findViewById(R.id.allmusicout)).setBackgroundColor(getSharedPreferences("xxx", 0).getInt("xxx", ViewCompat.MEASURED_STATE_MASK));
        this.wva = (WheelView) findViewById(R.id.musicroomchoose);
        this.allmusicselectroom = (TextView) findViewById(R.id.allmusicselectroom);
        this.wva.setOffset(1);
        this.wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.AllMusicActivity.1
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                AllMusicActivity.this.allmusicselectroom.setText("Select Room: " + str);
                AllMusicActivity.this.selectroom = str;
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

    public void playall(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 4;
        this.value2 = 3;
        this.value3 = 0;
        this.value4 = 0;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void pauseall(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 4;
        this.value2 = 4;
        this.value3 = 0;
        this.value4 = 0;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void backall(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 4;
        this.value2 = 1;
        this.value3 = 0;
        this.value4 = 0;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void nextall(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 4;
        this.value2 = 2;
        this.value3 = 0;
        this.value4 = 0;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void all0(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 5;
        this.value2 = 1;
        this.value3 = 3;
        this.value4 = 79;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void all25(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 5;
        this.value2 = 1;
        this.value3 = 3;
        this.value4 = 60;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void all50(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 5;
        this.value2 = 1;
        this.value3 = 3;
        this.value4 = 40;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void all100(View view) {
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
        if (this.selectroom.equals("All Music")) {
            this.allmusic = true;
        }
        this.count = 0;
        this.senting = true;
        this.value1 = 5;
        this.value2 = 1;
        this.value3 = 3;
        this.value4 = 0;
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
