package com.shima.smartbushome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import com.shima.smartbushome.assist.MusicNotifyReceiver;
import com.shima.smartbushome.assist.ShakeEventManager;
import com.shima.smartbushome.founction_view.Curtain;
import com.shima.smartbushome.founction_view.Fan;
import com.shima.smartbushome.founction_view.FloorHeat;
import com.shima.smartbushome.founction_view.HAVC;
import com.shima.smartbushome.founction_view.Light;
import com.shima.smartbushome.founction_view.Media;
import com.shima.smartbushome.founction_view.Mood;
import com.shima.smartbushome.founction_view.Music;
import com.shima.smartbushome.founction_view.NineInOne;
import com.shima.smartbushome.founction_view.Other;
import com.shima.smartbushome.util.SystemUIUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FounctionActivity extends AppCompatActivity implements ShakeEventManager.ShakeListener {
    public static final String ACTION_BACKPRESS = "com.example.founction.BACKPRESS";
    public static final String ACTION_DELETECURTAIN = "com.example.founction.DELETECURTAIN";
    public static final String ACTION_DELETEFAN = "com.example.founction.DELETEFAN";
    public static final String ACTION_DELETELIGHT = "com.example.founction.DELETELIGHT";
    public static final String ACTION_DELETEMOOD = "com.example.founction.DELETEMOOD";
    public static final String ACTION_DELETEOTHER = "com.example.founction.DELETEOTHER";
    public static final String ACTION_SHAKE = "com.example.founction.SHAKE";
    public static Context fcontext;
    public static int roomidfc;
    public static int topHeight;
    ActionBar actionBar;
    ArrayAdapter<String> adapter;
    private Fragment contentFragment;
    private FragmentManager fragmentManager;
    MusicNotifyReceiver mReceiver;
    private ShakeEventManager sd;
    String x;
    boolean initfinish = false;
    String[] fc = null;
    List<String> spinnerlist = new ArrayList();
    Handler startwork = new Handler();

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_founction);
        SystemUIUtil.setSystemUIVisible(this, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.founction_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        topHeight = dip2px(this, 56.0f);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
            topHeight = dip2px(this, 56.0f) + getStatusBarHeight(this);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        Intent intent = getIntent();
        roomidfc = intent.getIntExtra("roomid", 0);
        this.x = intent.getStringExtra("founction_type");
        this.fc = getIntent().getExtras().getStringArray("founction_count");
        this.adapter = getadapter(this.fc);
        this.actionBar = getSupportActionBar();
        this.actionBar.setNavigationMode(1);
        this.actionBar.setListNavigationCallbacks(this.adapter, new DropDownListenser());
        this.fragmentManager = getSupportFragmentManager();
        fcontext = this;
        this.sd = new ShakeEventManager();
        this.sd.setListener(this);
        this.sd.init(this);
        FragmentTransaction fragmentTransactionBeginTransaction = this.fragmentManager.beginTransaction();
        if (this.x.equals("Light")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Light"));
            this.contentFragment = new Light();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
            return;
        }
        if (this.x.equals("HVAC")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("HVAC"));
            this.contentFragment = new HAVC();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
            return;
        }
        if (this.x.equals("Mood")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Mood"));
            this.contentFragment = new Mood();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
            return;
        }
        if (this.x.equals("Fan")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Fan"));
            this.contentFragment = new Fan();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
            return;
        }
        if (this.x.equals("Curtain")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Curtain"));
            this.contentFragment = new Curtain();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
            return;
        }
        if (this.x.equals("Music")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Music"));
            this.contentFragment = new Music();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
            return;
        }
        if (this.x.equals("Other")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Other"));
            this.contentFragment = new Other();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
            return;
        }
        if (this.x.equals("Media")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Media"));
            this.contentFragment = new Media();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
        } else if (this.x.equals("9 in 1")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("9 in 1"));
            this.contentFragment = new NineInOne();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
        } else if (this.x.equals("Floor Heat")) {
            this.actionBar.setSelectedNavigationItem(getspinnerindex("Floor Heat"));
            this.contentFragment = new FloorHeat();
            fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, this.contentFragment).commit();
        }
    }

    class DropDownListenser implements ActionBar.OnNavigationListener {
        DropDownListenser() {
        }

        @Override // android.support.v7.app.ActionBar.OnNavigationListener
        public boolean onNavigationItemSelected(int i, long j) {
            if (FounctionActivity.this.initfinish) {
                FragmentTransaction fragmentTransactionBeginTransaction = FounctionActivity.this.fragmentManager.beginTransaction();
                if (FounctionActivity.this.spinnerlist.get(i).equals("Light")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new Light()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("HVAC")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new HAVC()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("Mood")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new Mood()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("Curtain")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new Curtain()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("Music")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new Music()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("Other")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new Other()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("Fan")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new Fan()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("Media")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new Media()).commit();
                    return false;
                }
                if (FounctionActivity.this.spinnerlist.get(i).equals("9 in 1")) {
                    fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new NineInOne()).commit();
                    return false;
                }
                if (!FounctionActivity.this.spinnerlist.get(i).equals("Floor Heat")) {
                    return false;
                }
                fragmentTransactionBeginTransaction.replace(R.id.founctionlayout, new FloorHeat()).commit();
                return false;
            }
            FounctionActivity.this.initfinish = true;
            return false;
        }
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int getTitleHeight(Activity activity) {
        Rect rect = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        return window.findViewById(android.R.id.content).getTop() - rect.top;
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // com.shima.smartbushome.assist.ShakeEventManager.ShakeListener
    public void onShake(int i) {
        Intent intent = new Intent(ACTION_SHAKE);
        intent.putExtra("shake_type", i);
        fcontext.sendBroadcast(intent);
    }

    @Override // android.app.Activity
    public void onRestart() {
        super.onRestart();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.sd.register();
        SystemUIUtil.setSystemUIVisible(this, false);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.sd.deregister();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.initfinish = false;
        this.spinnerlist.clear();
    }

    private void broadcastUpdate(String str) {
        fcontext.sendBroadcast(new Intent(str));
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.founction_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            broadcastUpdate(ACTION_BACKPRESS);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        broadcastUpdate(ACTION_BACKPRESS);
    }

    public ArrayAdapter getadapter(String[] strArr) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_dropdown_item);
        if (strArr[0].equals("1")) {
            this.spinnerlist.add("Light");
            arrayAdapter.add("Light");
        }
        if (strArr[1].equals("1")) {
            this.spinnerlist.add("HVAC");
            arrayAdapter.add("HVAC");
        }
        if (strArr[2].equals("1")) {
            this.spinnerlist.add("Fan");
            arrayAdapter.add("Fan");
        }
        if (strArr[3].equals("1")) {
            this.spinnerlist.add("Curtain");
            arrayAdapter.add("Curtain");
        }
        if (strArr[4].equals("1")) {
            this.spinnerlist.add("Music");
            arrayAdapter.add("Music");
        }
        if (strArr[5].equals("1")) {
            this.spinnerlist.add("Mood");
            arrayAdapter.add("Mood");
        }
        if (strArr[6].equals("1")) {
            this.spinnerlist.add("Other");
            arrayAdapter.add("Other");
        }
        if (strArr[7].equals("1")) {
            this.spinnerlist.add("Media");
            arrayAdapter.add("Media");
        }
        if (strArr[8].equals("1")) {
            this.spinnerlist.add("9 in 1");
            arrayAdapter.add("9 in 1");
        }
        if (strArr[9].equals("1")) {
            this.spinnerlist.add("Floor Heat");
            arrayAdapter.add("Floor Heat");
        }
        return arrayAdapter;
    }

    public int getspinnerindex(String str) {
        int i = 0;
        for (int i2 = 0; i2 < this.spinnerlist.size(); i2++) {
            if (this.spinnerlist.get(i2).equals(str)) {
                i = i2;
            }
        }
        return i;
    }
}
