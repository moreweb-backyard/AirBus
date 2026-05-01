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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MarcoIconAdapter;
import com.shima.smartbushome.assist.Adapter.MarcoItemAdapter;
import com.shima.smartbushome.assist.DragListView.DragView;
import com.shima.smartbushome.assist.marcoCompare;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savemarco;
import com.shima.smartbushome.database.Savemarcobutton;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class MarcoAddActivity extends AppCompatActivity {
    public static String MISSION_REFLASH = "reflash marco mission";
    public static int SAVEMISSION = 11;
    DragView actionlist;
    ImageView icon;
    AlertView iconalter;
    MarcoItemAdapter listadapter;
    AlertView mAlertView;
    public DBManager mgr;
    EditText name;
    Handler getdata = new Handler();
    int marcoid = 0;
    int editmarco = 0;
    List<Savemarco> thismarcolist = new ArrayList();
    String iconstring = "marco_icon1";
    private List<String> iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.centercontrol.MarcoAddActivity.1
        {
            add("marco_icon1");
            add("marco_icon2");
            add("marco_icon3");
            add("marco_icon4");
            add("marco_icon5");
            add("marco_icon6");
            add("marco_icon7");
            add("marco_icon8");
            add("marco_icon9");
        }
    };
    Runnable run = new Runnable() { // from class: com.shima.smartbushome.centercontrol.MarcoAddActivity.3
        @Override // java.lang.Runnable
        public void run() {
            new ArrayList();
            List<Savemarco> listQuerymarco = MarcoAddActivity.this.mgr.querymarco();
            if (MarcoAddActivity.this.thismarcolist.size() > 0) {
                MarcoAddActivity.this.thismarcolist.clear();
            }
            for (int i = 0; i < listQuerymarco.size(); i++) {
                if (MarcoAddActivity.this.marcoid == listQuerymarco.get(i).marco_id) {
                    MarcoAddActivity.this.thismarcolist.add(listQuerymarco.get(i));
                }
            }
            Collections.sort(MarcoAddActivity.this.thismarcolist, new marcoCompare());
            MarcoAddActivity.this.listadapter = new MarcoItemAdapter(MarcoAddActivity.this, MarcoAddActivity.this.thismarcolist);
            MarcoAddActivity.this.actionlist.setAdapter((ListAdapter) MarcoAddActivity.this.listadapter);
            List<Savemarcobutton> listQuerymarcobutton = MarcoAddActivity.this.mgr.querymarcobutton();
            for (int i2 = 0; i2 < listQuerymarcobutton.size(); i2++) {
                if (MarcoAddActivity.this.marcoid == listQuerymarcobutton.get(i2).marco_id) {
                    MarcoAddActivity.this.name.setText(listQuerymarcobutton.get(i2).marco_remark);
                    return;
                }
            }
        }
    };
    public OnItemClickListener exitclick = new OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddActivity.4
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i == -1) {
                MarcoAddActivity.this.exitdialogshow = false;
            } else if (i == 0) {
                MarcoAddActivity.this.finish();
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.centercontrol.MarcoAddActivity.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MarcoAddActivity.MISSION_REFLASH.equals(intent.getAction())) {
                MarcoAddActivity.this.getdata.postDelayed(MarcoAddActivity.this.run, 30L);
            }
        }
    };
    boolean exitdialogshow = false;

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_marco_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.marco_add_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mgr = new DBManager(this);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        Drawable drawable = getResources().getDrawable(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        drawable.setColorFilter(getResources().getColor(R.color.tab_bgcolor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        Intent intent = getIntent();
        this.marcoid = intent.getIntExtra("marcoID", 0);
        this.editmarco = intent.getIntExtra("editmarco", 0);
        this.actionlist = (DragView) findViewById(R.id.listView7);
        this.icon = (ImageView) findViewById(R.id.marco_add_icon);
        this.name = (EditText) findViewById(R.id.addmarco_name);
        this.getdata.postDelayed(this.run, 30L);
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

    public void changeicon(View view) {
        this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, this, AlertView.Style.Alert, null);
        View viewInflate = getLayoutInflater().inflate(R.layout.mood_icon_select, (ViewGroup) null);
        GridView gridView = (GridView) viewInflate.findViewById(R.id.gridView2);
        gridView.setAdapter((ListAdapter) new MarcoIconAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoAddActivity.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                MarcoAddActivity.this.iconstring = (String) MarcoAddActivity.this.iconarray.get(i);
                MarcoAddActivity.this.icon.setImageResource(MarcoAddActivity.getResourdIdByResourdName(MarcoAddActivity.this, (String) MarcoAddActivity.this.iconarray.get(i)));
                MarcoAddActivity.this.iconalter.dismiss();
            }
        });
        this.iconalter.addExtView(viewInflate);
        this.iconalter.show();
    }

    public void addmission(View view) {
        Intent intent = new Intent(this, (Class<?>) MarcoAddDetailActivity.class);
        intent.putExtra("marcoID", this.marcoid);
        intent.putExtra("marcoOrder", this.thismarcolist.size() > 0 ? this.thismarcolist.get(this.thismarcolist.size() - 1).sentorder + 1 : 0);
        startActivityForResult(intent, SAVEMISSION);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != 11) {
            return;
        }
        this.getdata.postDelayed(this.run, 30L);
        Toast.makeText(this, "add succeed", 0).show();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marco_add_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            if (itemId == R.id.marco_add_save) {
                if (this.name.getText().toString().length() > 0) {
                    if (this.editmarco == 1) {
                        Savemarcobutton savemarcobutton = new Savemarcobutton();
                        savemarcobutton.marco_id = this.marcoid;
                        savemarcobutton.marco_remark = this.name.getText().toString().trim();
                        savemarcobutton.marco_icon = this.iconstring;
                        this.mgr.updatemarcobutton(savemarcobutton);
                        Toast.makeText(this, "saved", 0).show();
                        setResult(MarcoActivity.SAVEMARCO);
                        finish();
                    } else {
                        Savemarcobutton savemarcobutton2 = new Savemarcobutton();
                        savemarcobutton2.marco_id = this.marcoid;
                        savemarcobutton2.marco_remark = this.name.getText().toString().trim();
                        savemarcobutton2.marco_icon = this.iconstring;
                        this.mgr.addmarcobutton(savemarcobutton2);
                        Toast.makeText(this, "saved", 0).show();
                        setResult(MarcoActivity.SAVEMARCO);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Please enter a name", 0).show();
                }
            }
        } else if (this.exitdialogshow) {
            this.mAlertView.dismiss();
            this.exitdialogshow = false;
        } else {
            this.mAlertView = new AlertView("Warning", "You still not save this setting, Are you sure to quit?", "CANCEL", new String[]{"YES"}, null, this, AlertView.Style.Alert, this.exitclick);
            this.mAlertView.setCancelable(false);
            this.mAlertView.show();
            this.exitdialogshow = true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public static int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.exitdialogshow) {
            this.mAlertView.dismiss();
            this.exitdialogshow = false;
        } else {
            this.mAlertView = new AlertView("Warning", "You still not save this setting, Are you sure to quit?", "CANCEL", new String[]{"YES"}, null, this, AlertView.Style.Alert, this.exitclick);
            this.mAlertView.setCancelable(false);
            this.mAlertView.show();
            this.exitdialogshow = true;
        }
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(MISSION_REFLASH);
        return intentFilter;
    }
}
