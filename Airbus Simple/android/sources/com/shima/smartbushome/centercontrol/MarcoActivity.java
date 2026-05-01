package com.shima.smartbushome.centercontrol;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MarcoAdapter;
import com.shima.smartbushome.assist.marcoCompare;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.database.Savefan;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.database.Savemarco;
import com.shima.smartbushome.database.Savemarcobutton;
import com.shima.smartbushome.database.Savemedia;
import com.shima.smartbushome.database.Savemediabutton;
import com.shima.smartbushome.database.Saveother;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.founction_command.curtaincontrol;
import com.shima.smartbushome.founction_command.fancontrol;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.founction_command.mediacontrol;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.founction_command.othercontrol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class MarcoActivity extends AppCompatActivity {
    public static int SAVEMARCO = 22;
    private static final byte const_ac_cmd_type_onoff = 3;
    private static final byte const_ac_cmd_type_set_auto_tmp = 8;
    private static final byte const_ac_cmd_type_set_cold_tmp = 4;
    private static final byte const_ac_cmd_type_set_fan = 5;
    private static final byte const_ac_cmd_type_set_heat_tmp = 7;
    private static final byte const_ac_cmd_type_set_mode = 6;
    private static final byte const_fan_speed_anto = 0;
    private static final byte const_fan_speed_high = 1;
    private static final byte const_fan_speed_low = 3;
    private static final byte const_fan_speed_medium = 2;
    private static final byte const_mode_auto = 3;
    private static final byte const_mode_cool = 0;
    private static final byte const_mode_fan = 2;
    private static final byte const_mode_heat = 1;
    MarcoAdapter adapter;
    AlertView deleteview;
    GridView marcobuttonview;
    RelativeLayout marcon;
    public DBManager mgr;
    ScrollView nonemarcoview;
    Handler getdata = new Handler();
    List<Savemarcobutton> allmarcobutton = new ArrayList();
    List<Savemarco> clickmarco = new ArrayList();
    boolean deletemode = false;
    Runnable run = new Runnable() { // from class: com.shima.smartbushome.centercontrol.MarcoActivity.1
        @Override // java.lang.Runnable
        public void run() {
            if (MarcoActivity.this.allmarcobutton.size() > 0) {
                MarcoActivity.this.allmarcobutton.clear();
            }
            MarcoActivity.this.allmarcobutton = MarcoActivity.this.mgr.querymarcobutton();
            if (MarcoActivity.this.allmarcobutton.size() > 0) {
                MarcoActivity.this.marcon.setVisibility(0);
                MarcoActivity.this.nonemarcoview.setVisibility(8);
                MarcoActivity.this.adapter = new MarcoAdapter(MarcoActivity.this, MarcoActivity.this.allmarcobutton);
                MarcoActivity.this.marcobuttonview.setAdapter((ListAdapter) MarcoActivity.this.adapter);
                MarcoActivity.this.marcobuttonview.setOnItemClickListener(MarcoActivity.this.marcoitemclick);
                MarcoActivity.this.marcobuttonview.setOnItemLongClickListener(MarcoActivity.this.marcoitemlongclick);
                return;
            }
            MarcoActivity.this.nonemarcoview.setVisibility(0);
            MarcoActivity.this.marcon.setVisibility(8);
        }
    };
    public AdapterView.OnItemClickListener marcoitemclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoActivity.2
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long j) {
            if (MarcoActivity.this.deletemode) {
                String str = MarcoActivity.this.adapter.getselected(i).marco_remark;
                MarcoActivity.this.deleteview = new AlertView("Warning", "Are you sure to delete " + str + " ?", "CANCEL", new String[]{"YES"}, null, MarcoActivity.this, AlertView.Style.Alert, new OnItemClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoActivity.2.1
                    @Override // com.bigkoo.alertview.OnItemClickListener
                    public void onItemClick(Object obj, int i2) {
                        if (i2 == -1) {
                            MarcoActivity.this.deletemode = false;
                            return;
                        }
                        if (i2 == 0) {
                            int i3 = MarcoActivity.this.adapter.getselected(i).marco_id;
                            MarcoActivity.this.mgr.deletemarcobutton("marco", i3);
                            MarcoActivity.this.mgr.deletemarcobutton("marcobutton", i3);
                            MarcoActivity.this.deletemode = false;
                            MarcoActivity.this.getdata.postDelayed(MarcoActivity.this.run, 30L);
                        }
                    }
                });
                MarcoActivity.this.deleteview.setCancelable(false);
                MarcoActivity.this.deleteview.show();
                return;
            }
            if (MarcoActivity.this.clickmarco.size() > 0) {
                MarcoActivity.this.clickmarco.clear();
            }
            MarcoActivity.this.clickmarco = MarcoActivity.this.listorder(MarcoActivity.this.adapter.getselected(i).marco_id, MarcoActivity.this.mgr.querymarco());
            MarcoActivity.this.senthandler.postDelayed(MarcoActivity.this.sent, 20L);
        }
    };
    public AdapterView.OnItemLongClickListener marcoitemlongclick = new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.centercontrol.MarcoActivity.3
        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            Intent intent = new Intent(MarcoActivity.this, (Class<?>) MarcoAddActivity.class);
            intent.putExtra("marcoID", MarcoActivity.this.allmarcobutton.get(i).marco_id);
            intent.putExtra("editmarco", 1);
            MarcoActivity.this.startActivityForResult(intent, MarcoActivity.SAVEMARCO);
            return false;
        }
    };
    Handler senthandler = new Handler();
    int sentcount = 0;
    boolean senting = false;
    Runnable sent = new Runnable() { // from class: com.shima.smartbushome.centercontrol.MarcoActivity.4
        @Override // java.lang.Runnable
        public void run() {
            if (MarcoActivity.this.senting) {
                MarcoActivity.this.sentcount++;
                MarcoActivity.this.senting = false;
                if (MarcoActivity.this.sentcount >= MarcoActivity.this.clickmarco.size()) {
                    MarcoActivity.this.senthandler.removeCallbacks(MarcoActivity.this.sent);
                    MarcoActivity.this.sentcount = 0;
                    Toast.makeText(MarcoActivity.this, "finished", 0).show();
                    return;
                }
                MarcoActivity.this.senthandler.postDelayed(MarcoActivity.this.sent, 75L);
                return;
            }
            if (MarcoActivity.this.clickmarco.size() > 0) {
                MarcoActivity.this.sentmarco(MarcoActivity.this.clickmarco.get(MarcoActivity.this.sentcount));
            }
            MarcoActivity.this.senthandler.postDelayed(MarcoActivity.this.sent, 250L);
            MarcoActivity.this.senting = true;
        }
    };
    Savemarco senttemp = new Savemarco();
    lightcontrol lc = new lightcontrol();
    ACcontrol ac = new ACcontrol();
    curtaincontrol cc = new curtaincontrol();
    musiccontrol mc = new musiccontrol();
    othercontrol oc = new othercontrol();
    fancontrol fc = new fancontrol();
    mediacontrol mec = new mediacontrol();

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_marco);
        Toolbar toolbar = (Toolbar) findViewById(R.id.marco_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Macro");
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
        initView();
        this.getdata.postDelayed(this.run, 10L);
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

    public void initView() {
        this.nonemarcoview = (ScrollView) findViewById(R.id.marcoinsscroll);
        this.marcon = (RelativeLayout) findViewById(R.id.marcon);
        this.marcobuttonview = (GridView) findViewById(R.id.gridView3);
    }

    public void addmarco(View view) {
        Intent intent = new Intent(this, (Class<?>) MarcoAddActivity.class);
        intent.putExtra("marcoID", this.allmarcobutton.size() > 0 ? this.allmarcobutton.get(this.allmarcobutton.size() - 1).marco_id + 1 : 0);
        intent.putExtra("editmarco", 0);
        startActivityForResult(intent, SAVEMARCO);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != 22) {
            return;
        }
        this.getdata.postDelayed(this.run, 30L);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.marco_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            finish();
        } else if (itemId == R.id.marco_add) {
            Intent intent = new Intent(this, (Class<?>) MarcoAddActivity.class);
            intent.putExtra("marcoID", this.allmarcobutton.size() > 0 ? this.allmarcobutton.get(this.allmarcobutton.size() - 1).marco_id + 1 : 0);
            intent.putExtra("editmarco", 0);
            startActivityForResult(intent, SAVEMARCO);
        } else if (itemId == R.id.marco_delete) {
            this.deletemode = true;
            Toast.makeText(this, "Please click one button to delete", 0).show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class senttemp implements Runnable {
        public senttemp() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            int i;
            try {
                Thread.sleep(150L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                i = MarcoActivity.this.senttemp.value3;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
            if (i != 3) {
                switch (i) {
                    case 0:
                        MarcoActivity.this.ac.ACControl((byte) MarcoActivity.this.senttemp.subnetID, (byte) MarcoActivity.this.senttemp.deviceID, 4, MarcoActivity.this.senttemp.value2, MainActivity.mydupsocket);
                        break;
                    case 1:
                        MarcoActivity.this.ac.ACControl((byte) MarcoActivity.this.senttemp.subnetID, (byte) MarcoActivity.this.senttemp.deviceID, 7, MarcoActivity.this.senttemp.value2, MainActivity.mydupsocket);
                        break;
                }
                return;
            }
            MarcoActivity.this.ac.ACControl((byte) MarcoActivity.this.senttemp.subnetID, (byte) MarcoActivity.this.senttemp.deviceID, 8, MarcoActivity.this.senttemp.value2, MainActivity.mydupsocket);
        }
    }

    public void sentmarco(Savemarco savemarco) {
        int i = 0;
        switch (savemarco.control_type) {
            case 1:
                Savelight savelight = new Savelight();
                List<Savelight> listQuerylight = this.mgr.querylight();
                while (true) {
                    if (i < listQuerylight.size()) {
                        if (listQuerylight.get(i).room_id == savemarco.room_id && listQuerylight.get(i).light_statement.equals(savemarco.device)) {
                            savelight = listQuerylight.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value1) {
                    case 0:
                        switch (savemarco.value2) {
                            case 0:
                                this.lc.SingleChannelControl((byte) savelight.subnetID, (byte) savelight.deviceID, savelight.channel, 0, MainActivity.mydupsocket);
                                break;
                            case 1:
                                this.lc.SingleChannelControl((byte) savelight.subnetID, (byte) savelight.deviceID, savelight.channel, 100, MainActivity.mydupsocket);
                                break;
                        }
                    case 1:
                        this.lc.SingleChannelControl((byte) savelight.subnetID, (byte) savelight.deviceID, savelight.channel, savemarco.value2, MainActivity.mydupsocket);
                        break;
                    case 2:
                        this.lc.ARGBlightcontrol((byte) savelight.subnetID, (byte) savelight.deviceID, savemarco.value2, MainActivity.mydupsocket);
                        break;
                }
            case 2:
                Savehvac savehvac = new Savehvac();
                List<Savehvac> listQueryhvac = this.mgr.queryhvac();
                while (true) {
                    if (i < listQueryhvac.size()) {
                        if (listQueryhvac.get(i).room_id == savemarco.room_id && listQueryhvac.get(i).hvac_remark.equals(savemarco.device)) {
                            savehvac = listQueryhvac.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value1) {
                    case 0:
                        this.ac.ACControl((byte) savehvac.subnetID, (byte) savehvac.deviceID, 3, savemarco.value2, MainActivity.mydupsocket);
                        break;
                    case 1:
                        this.ac.ACControl((byte) savehvac.subnetID, (byte) savehvac.deviceID, 6, savemarco.value3, MainActivity.mydupsocket);
                        this.senttemp = savemarco;
                        this.senttemp.subnetID = savehvac.subnetID;
                        this.senttemp.deviceID = savehvac.deviceID;
                        new Thread(new senttemp()).start();
                        break;
                    case 2:
                        this.ac.ACControl((byte) savehvac.subnetID, (byte) savehvac.deviceID, 5, savemarco.value2, MainActivity.mydupsocket);
                        break;
                }
            case 3:
                Savecurtain savecurtain = new Savecurtain();
                List<Savecurtain> listQuerycurtain = this.mgr.querycurtain();
                while (true) {
                    if (i < listQuerycurtain.size()) {
                        if (listQuerycurtain.get(i).room_id == savemarco.room_id && listQuerycurtain.get(i).curtain_remark.equals(savemarco.device)) {
                            savecurtain = listQuerycurtain.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value2) {
                    case 0:
                        this.cc.CurtainControl((byte) savecurtain.subnetID, (byte) savecurtain.deviceID, savecurtain.channel_1, savecurtain.channel_2, "close", MainActivity.mydupsocket);
                        break;
                    case 1:
                        this.cc.CurtainControl((byte) savecurtain.subnetID, (byte) savecurtain.deviceID, savecurtain.channel_1, savecurtain.channel_2, "open", MainActivity.mydupsocket);
                        break;
                }
            case 4:
                int i2 = savemarco.value1;
                if (i2 == 1) {
                    this.mc.MusicControl((byte) 1, (byte) savemarco.value2, (byte) 0, (byte) 0, (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                    break;
                } else {
                    switch (i2) {
                        case 3:
                            this.mc.MusicControl((byte) 3, (byte) 6, (byte) savemarco.value3, (byte) 0, (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                        case 4:
                            this.mc.MusicControl((byte) 4, (byte) savemarco.value2, (byte) 0, (byte) 0, (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                        case 5:
                            this.mc.MusicControl((byte) 5, (byte) 1, (byte) 3, (byte) (79 - ((savemarco.value2 * 79) / 100)), (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                        case 6:
                            byte[] bArr = {(byte) ((savemarco.value3 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (savemarco.value3 - (65280 & savemarco.value3))};
                            this.mc.MusicControl((byte) 6, (byte) savemarco.value2, bArr[0], bArr[1], (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                    }
                }
            case 5:
                Saveother saveother = new Saveother();
                List<Saveother> listQueryother = this.mgr.queryother();
                while (true) {
                    if (i < listQueryother.size()) {
                        if (listQueryother.get(i).room_id == savemarco.room_id && listQueryother.get(i).other_statement.equals(savemarco.device)) {
                            saveother = listQueryother.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value1) {
                    case 0:
                        switch (savemarco.value2) {
                            case 0:
                                this.oc.SingleChannelControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, 0, MainActivity.mydupsocket);
                                break;
                            case 1:
                                this.oc.SingleChannelControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, 100, MainActivity.mydupsocket);
                                break;
                        }
                    case 1:
                        switch (savemarco.value2) {
                            case 0:
                                this.oc.CurtainControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, saveother.channel_2, "close", MainActivity.mydupsocket);
                                break;
                            case 1:
                                this.oc.CurtainControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, saveother.channel_2, "open", MainActivity.mydupsocket);
                                break;
                        }
                }
            case 6:
                Savefan savefan = new Savefan();
                List<Savefan> listQueryfan = this.mgr.queryfan();
                while (true) {
                    if (i < listQueryfan.size()) {
                        if (listQueryfan.get(i).room_id == savemarco.room_id && listQueryfan.get(i).fan_statement.equals(savemarco.device)) {
                            savefan = listQueryfan.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                this.fc.FanChannelControl((byte) savefan.subnetID, (byte) savefan.deviceID, savefan.channel, savemarco.value2, MainActivity.mydupsocket);
                break;
            case 7:
                Savemedia savemedia = new Savemedia();
                Savemediabutton savemediabutton = new Savemediabutton();
                List<Savemedia> listQuerymedia = this.mgr.querymedia();
                List<Savemediabutton> listQuerymediabutton = this.mgr.querymediabutton();
                int i3 = 0;
                while (true) {
                    if (i3 < listQuerymedia.size()) {
                        if (listQuerymedia.get(i3).room_id == savemarco.room_id && listQuerymedia.get(i3).media_statement.equals(savemarco.device)) {
                            savemedia = listQuerymedia.get(i3);
                        } else {
                            i3++;
                        }
                    }
                }
                while (true) {
                    if (i < listQuerymediabutton.size()) {
                        if (listQuerymediabutton.get(i).room_id == savemarco.room_id && listQuerymediabutton.get(i).media_id == savemedia.media_id && listQuerymediabutton.get(i).button_num == savemarco.value2) {
                            savemediabutton = listQuerymediabutton.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                if (savemediabutton.ifIRmarco == 1) {
                    this.mec.IRMarcoControl((byte) savemedia.subnetID, (byte) savemedia.deviceID, savemediabutton.media_swno, savemediabutton.media_controltype, MainActivity.mydupsocket);
                    break;
                } else {
                    this.mec.IRControl((byte) savemedia.subnetID, (byte) savemedia.deviceID, savemediabutton.media_swno, savemediabutton.media_controltype, MainActivity.mydupsocket);
                    break;
                }
                break;
        }
    }

    public List<Savemarco> listorder(int i, List<Savemarco> list) {
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (i == list.get(i2).marco_id) {
                arrayList.add(list.get(i2));
            }
        }
        Collections.sort(arrayList, new marcoCompare());
        return arrayList;
    }
}
