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
import com.shima.smartbushome.database.Saveother;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.founction_command.othercontrol;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AllOtherActivity extends AppCompatActivity {
    TextView allotherselectroom;
    WheelView wva;
    othercontrol oc = new othercontrol();
    List<Saveroom> roomlist = new ArrayList();
    List<String> roomnamelist = new ArrayList();
    List<Saveother> allotherlist = new ArrayList();
    Handler getdatahandler = new Handler();
    Handler senthandler = new Handler();
    String selectroom = "";
    int value = 0;
    boolean senting = false;
    boolean allother = false;
    int roomid = 0;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllOtherActivity.2
        @Override // java.lang.Runnable
        public void run() {
            AllOtherActivity.this.roomlist = MainActivity.mgr.queryroom();
            for (int i = 0; i < AllOtherActivity.this.roomlist.size(); i++) {
                AllOtherActivity.this.roomnamelist.add(AllOtherActivity.this.roomlist.get(i).room_name);
            }
            AllOtherActivity.this.roomnamelist.add("All Other");
            AllOtherActivity.this.wva.setItems(AllOtherActivity.this.roomnamelist);
            AllOtherActivity.this.selectroom = AllOtherActivity.this.roomnamelist.get(0);
            AllOtherActivity.this.allotherselectroom.setText("Select Room: " + AllOtherActivity.this.selectroom);
            AllOtherActivity.this.allotherlist = MainActivity.mgr.queryother();
        }
    };
    int count = 0;
    Runnable senrun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.AllOtherActivity.3
        @Override // java.lang.Runnable
        public void run() {
            if (AllOtherActivity.this.count >= AllOtherActivity.this.allotherlist.size()) {
                AllOtherActivity.this.count = 0;
                AllOtherActivity.this.senting = false;
                AllOtherActivity.this.allother = false;
                Toast.makeText(AllOtherActivity.this, "finished", 0).show();
                AllOtherActivity.this.senthandler.removeCallbacks(AllOtherActivity.this.senrun);
                return;
            }
            if (AllOtherActivity.this.allother) {
                if (AllOtherActivity.this.value == 2) {
                    switch (AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).other_type) {
                        case 1:
                            AllOtherActivity.this.oc.SingleChannelControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, 100, MainActivity.mydupsocket);
                            break;
                        case 2:
                            AllOtherActivity.this.oc.CurtainControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_2, "open", MainActivity.mydupsocket);
                            break;
                    }
                } else if (AllOtherActivity.this.value == 3) {
                    switch (AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).other_type) {
                        case 1:
                            AllOtherActivity.this.oc.SingleChannelControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, 0, MainActivity.mydupsocket);
                            break;
                        case 2:
                            AllOtherActivity.this.oc.CurtainControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_2, "close", MainActivity.mydupsocket);
                            break;
                    }
                }
            } else if (AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).room_id == AllOtherActivity.this.roomid) {
                if (AllOtherActivity.this.value == 2) {
                    switch (AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).other_type) {
                        case 1:
                            AllOtherActivity.this.oc.SingleChannelControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, 100, MainActivity.mydupsocket);
                            break;
                        case 2:
                            AllOtherActivity.this.oc.CurtainControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_2, "open", MainActivity.mydupsocket);
                            break;
                    }
                } else if (AllOtherActivity.this.value == 3) {
                    switch (AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).other_type) {
                        case 1:
                            AllOtherActivity.this.oc.SingleChannelControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, 0, MainActivity.mydupsocket);
                            break;
                        case 2:
                            AllOtherActivity.this.oc.CurtainControl((byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).subnetID, (byte) AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).deviceID, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_1, AllOtherActivity.this.allotherlist.get(AllOtherActivity.this.count).channel_2, "close", MainActivity.mydupsocket);
                            break;
                    }
                }
            }
            AllOtherActivity.this.count++;
            AllOtherActivity.this.senthandler.postDelayed(AllOtherActivity.this.senrun, 70L);
        }
    };

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_all_other);
        Toolbar toolbar = (Toolbar) findViewById(R.id.allother_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("All Other");
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
        ((RelativeLayout) findViewById(R.id.allotherout)).setBackgroundColor(getSharedPreferences("xxx", 0).getInt("xxx", ViewCompat.MEASURED_STATE_MASK));
        this.wva = (WheelView) findViewById(R.id.otherroomchoose);
        this.allotherselectroom = (TextView) findViewById(R.id.allotherselectroom);
        this.wva.setOffset(1);
        this.wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() { // from class: com.shima.smartbushome.centercontrol.AllOtherActivity.1
            @Override // com.shima.smartbushome.assist.WheelView.OnWheelViewListener
            public void onSelected(int i, String str) {
                AllOtherActivity.this.allotherselectroom.setText("Select Room: " + str);
                AllOtherActivity.this.selectroom = str;
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

    public void allotheron(View view) {
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
        if (this.selectroom.equals("All Other")) {
            this.allother = true;
        }
        this.count = 0;
        this.senting = true;
        this.value = 2;
        this.senthandler.postDelayed(this.senrun, 30L);
    }

    public void allotheroff(View view) {
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
        if (this.selectroom.equals("All Other")) {
            this.allother = true;
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
