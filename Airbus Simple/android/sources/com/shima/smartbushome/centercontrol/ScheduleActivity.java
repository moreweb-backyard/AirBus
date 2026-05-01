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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Saveschedule;
import com.shima.smartbushome.selflayout.ScheduleLayout;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ScheduleActivity extends AppCompatActivity {
    public static String ACTION_ADD_Schedule = "com.smarthome.addschedule";
    public static String ACTION_UPDATE_Schedule = "com.smarthome.updateschedule";
    MenuItem add;
    MenuItem delete;
    RelativeLayout noschedule;
    LinearLayout schedulelayout;
    ScrollView scheduleview;
    boolean deletemode = false;
    Handler getdatahandler = new Handler();
    List<ScheduleLayout> layoutlist = new ArrayList();
    List<Saveschedule> allschedule = new ArrayList();
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.ScheduleActivity.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ScheduleActivity.ACTION_UPDATE_Schedule.equals(action)) {
                ScheduleActivity.this.getdatahandler.postDelayed(ScheduleActivity.this.getdatarun, 20L);
                Toast.makeText(ScheduleActivity.this, "Update Alarm Succeed", 0).show();
            } else if (ScheduleActivity.ACTION_ADD_Schedule.equals(action)) {
                ScheduleActivity.this.getdatahandler.postDelayed(ScheduleActivity.this.getdatarun, 20L);
                Toast.makeText(ScheduleActivity.this, "Set Alarm Succeed", 0).show();
            }
        }
    };
    int SCHEDULE_REQUEST = 1;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.centercontrol.ScheduleActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (ScheduleActivity.this.allschedule.size() > 0) {
                ScheduleActivity.this.allschedule.clear();
            }
            if (ScheduleActivity.this.layoutlist.size() > 0) {
                ScheduleActivity.this.layoutlist.clear();
            }
            ScheduleActivity.this.schedulelayout.removeAllViews();
            ScheduleActivity.this.allschedule = MainActivity.mgr.queryschedule();
            if (ScheduleActivity.this.allschedule.size() > 0) {
                ScheduleActivity.this.noschedule.setVisibility(8);
                ScheduleActivity.this.scheduleview.setVisibility(0);
                for (int i = 0; i < ScheduleActivity.this.allschedule.size(); i++) {
                    ScheduleActivity.this.addspecView(ScheduleActivity.this.allschedule.get(i));
                }
            } else {
                ScheduleActivity.this.noschedule.setVisibility(0);
                ScheduleActivity.this.scheduleview.setVisibility(8);
            }
            ScheduleActivity.this.getdatahandler.removeCallbacks(ScheduleActivity.this.getdatarun);
        }
    };

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.scheduletoolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Schedule");
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
        this.noschedule = (RelativeLayout) findViewById(R.id.noschedule);
        this.scheduleview = (ScrollView) findViewById(R.id.scheduleview);
        this.schedulelayout = (LinearLayout) findViewById(R.id.schedulelinearlayout);
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
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mGattUpdateReceiver);
    }

    public void addschedulebutton(View view) {
        startActivity(new Intent(this, (Class<?>) ScheduleAddActivity.class));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addspecView(Saveschedule saveschedule) {
        ScheduleLayout scheduleLayout = new ScheduleLayout(this);
        scheduleLayout.setcontan(saveschedule);
        this.schedulelayout.addView(scheduleLayout);
        this.layoutlist.add(scheduleLayout);
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
                    if (this.deletemode) {
                        this.add.setTitle("ADD");
                        this.delete.setTitle("DELETE");
                        this.deletemode = false;
                        for (int i = 0; i < this.layoutlist.size(); i++) {
                            this.layoutlist.get(i).setdeletevisable(false);
                        }
                        break;
                    } else {
                        startActivityForResult(new Intent(this, (Class<?>) ScheduleAddActivity.class), this.SCHEDULE_REQUEST);
                        break;
                    }
                case R.id.light_remove /* 2131231275 */:
                    this.deletemode = !this.deletemode;
                    if (this.deletemode) {
                        this.add.setTitle("CANCLE DELETE");
                        this.delete.setTitle("DELETE");
                        for (int i2 = 0; i2 < this.layoutlist.size(); i2++) {
                            this.layoutlist.get(i2).setdeletevisable(true);
                        }
                        break;
                    } else {
                        for (int i3 = 0; i3 < this.layoutlist.size(); i3++) {
                            if (this.layoutlist.get(i3).getIfneedtoDelete()) {
                                MainActivity.mgr.deleteschedule(this.layoutlist.get(i3).getscheduleid());
                            }
                        }
                        this.getdatahandler.postDelayed(this.getdatarun, 20L);
                        this.add.setTitle("ADD");
                        this.delete.setTitle("DELETE");
                        this.deletemode = false;
                        break;
                    }
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(ACTION_UPDATE_Schedule);
        intentFilter.addAction(ACTION_ADD_Schedule);
        return intentFilter;
    }
}
