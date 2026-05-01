package com.shima.smartbushome;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.assist.Adapter.FounctionAdapter;
import com.shima.smartbushome.assist.DraggableGridViewType2;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.util.SystemUIUtil;

/* loaded from: classes.dex */
public class RoomActivity extends AppCompatActivity {
    private FounctionAdapter mAdapter;
    private DraggableGridViewType2 mDraggableGridViewPager;
    private int mGridCount;
    int roomid;
    public String[] founctionlist = {""};
    public boolean deletefc = false;
    Handler handlefc = new Handler();
    String[] founction_count = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
    Runnable getfc = new Runnable() { // from class: com.shima.smartbushome.RoomActivity.3
        @Override // java.lang.Runnable
        public void run() {
            RoomActivity.this.founctionlist[0] = "0";
            RoomActivity.this.founctionlist[1] = "0";
            RoomActivity.this.founctionlist[2] = "0";
            RoomActivity.this.founctionlist[3] = "0";
            RoomActivity.this.founctionlist[4] = "0";
            RoomActivity.this.founctionlist[5] = "0";
            RoomActivity.this.founctionlist[6] = "0";
            RoomActivity.this.founctionlist[7] = "0";
            RoomActivity.this.founctionlist[8] = "0";
            RoomActivity.this.founctionlist[9] = "0";
            RoomActivity.this.mAdapter.clear();
            RoomActivity.this.founctionlist = MainActivity.mgr.queryroomfounction(RoomActivity.this.roomid);
            for (int i = 0; i < RoomActivity.this.founctionlist.length; i++) {
                if (RoomActivity.this.founctionlist[i].equals("1")) {
                    switch (i) {
                        case 0:
                            RoomActivity.this.mAdapter.add("Light");
                            break;
                        case 1:
                            RoomActivity.this.mAdapter.add("HVAC");
                            break;
                        case 2:
                            RoomActivity.this.mAdapter.add("Fan");
                            break;
                        case 3:
                            RoomActivity.this.mAdapter.add("Curtain");
                            break;
                        case 4:
                            RoomActivity.this.mAdapter.add("Music");
                            break;
                        case 5:
                            RoomActivity.this.mAdapter.add("Mood");
                            break;
                        case 6:
                            RoomActivity.this.mAdapter.add("Other");
                            break;
                        case 7:
                            RoomActivity.this.mAdapter.add("Media");
                            break;
                        case 8:
                            RoomActivity.this.mAdapter.add("9 in 1");
                            break;
                        case 9:
                            RoomActivity.this.mAdapter.add("Floor Heat");
                            break;
                    }
                }
            }
            RoomActivity.this.mDraggableGridViewPager.setAdapter(RoomActivity.this.mAdapter);
            RoomActivity.this.handlefc.removeCallbacks(RoomActivity.this.getfc);
        }
    };
    final String[] mItems = {"Light", "HVAC", "Fan", "Curtain", "Music", "Mood", "Other", "Media", "9 in 1", "Floor Heat"};
    public OnItemClickListener itemlick = new OnItemClickListener() { // from class: com.shima.smartbushome.RoomActivity.4
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i == -1) {
                return;
            }
            boolean z = false;
            for (int i2 = 0; i2 < RoomActivity.this.mAdapter.getCount(); i2++) {
                if (RoomActivity.this.mAdapter.getItem(i2).equals(RoomActivity.this.mItems[i])) {
                    z = true;
                }
            }
            if (!z) {
                Saveroom saveroom = new Saveroom();
                saveroom.room_id = RoomActivity.this.roomid;
                if (RoomActivity.this.mItems[i].equals("Light")) {
                    saveroom.light = 1;
                    RoomActivity.this.founction_count[0] = "1";
                } else if (RoomActivity.this.mItems[i].equals("HVAC")) {
                    saveroom.hvac = 1;
                    RoomActivity.this.founction_count[1] = "1";
                } else if (RoomActivity.this.mItems[i].equals("Curtain")) {
                    saveroom.curtain = 1;
                    RoomActivity.this.founction_count[3] = "1";
                } else if (RoomActivity.this.mItems[i].equals("Music")) {
                    saveroom.music = 1;
                    RoomActivity.this.founction_count[4] = "1";
                } else if (RoomActivity.this.mItems[i].equals("Mood")) {
                    saveroom.mood = 1;
                    RoomActivity.this.founction_count[5] = "1";
                } else if (RoomActivity.this.mItems[i].equals("Fan")) {
                    saveroom.fan = 1;
                    RoomActivity.this.founction_count[2] = "1";
                } else if (RoomActivity.this.mItems[i].equals("Other")) {
                    saveroom.other = 1;
                    RoomActivity.this.founction_count[6] = "1";
                } else if (RoomActivity.this.mItems[i].equals("Media")) {
                    saveroom.media = 1;
                    RoomActivity.this.founction_count[7] = "1";
                } else if (RoomActivity.this.mItems[i].equals("9 in 1")) {
                    saveroom.nio = 1;
                    RoomActivity.this.founction_count[8] = "1";
                } else if (RoomActivity.this.mItems[i].equals("Floor Heat")) {
                    saveroom.fh = 1;
                    RoomActivity.this.founction_count[9] = "1";
                }
                MainActivity.mgr.updateroom(saveroom, RoomActivity.this.mItems[i].toLowerCase());
                RoomActivity.this.handlefc.postDelayed(RoomActivity.this.getfc, 20L);
                return;
            }
            Toast.makeText(RoomActivity.this, RoomActivity.this.mItems[i] + " already exist", 0).show();
        }
    };

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(R.layout.activity_room);
        SystemUIUtil.setSystemUIVisible(this, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.room_toolbar);
        Intent intent = getIntent();
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Room:" + intent.getStringExtra("roomname"));
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
        ((RelativeLayout) findViewById(R.id.roomacbg)).setBackgroundColor(getSharedPreferences("pagesbgcolor", 0).getInt("roombgcolor", ViewCompat.MEASURED_STATE_MASK));
        this.mDraggableGridViewPager = (DraggableGridViewType2) findViewById(R.id.draggable_grid_view_pager);
        this.roomid = intent.getIntExtra("roomid", 0);
        this.mAdapter = new FounctionAdapter(this);
        try {
            this.founctionlist = MainActivity.mgr.queryroomfounction(this.roomid);
        } catch (Exception unused) {
            finish();
        }
        for (int i = 0; i < this.founctionlist.length; i++) {
            if (this.founctionlist[i].equals("1")) {
                switch (i) {
                    case 0:
                        this.mAdapter.add("Light");
                        this.founction_count[0] = "1";
                        break;
                    case 1:
                        this.mAdapter.add("HVAC");
                        this.founction_count[1] = "1";
                        break;
                    case 2:
                        this.mAdapter.add("Fan");
                        this.founction_count[2] = "1";
                        break;
                    case 3:
                        this.mAdapter.add("Curtain");
                        this.founction_count[3] = "1";
                        break;
                    case 4:
                        this.mAdapter.add("Music");
                        this.founction_count[4] = "1";
                        break;
                    case 5:
                        this.mAdapter.add("Mood");
                        this.founction_count[5] = "1";
                        break;
                    case 6:
                        this.mAdapter.add("Other");
                        this.founction_count[6] = "1";
                        break;
                    case 7:
                        this.mAdapter.add("Media");
                        this.founction_count[7] = "1";
                        break;
                    case 8:
                        this.mAdapter.add("9 in 1");
                        this.founction_count[8] = "1";
                        break;
                    case 9:
                        this.mAdapter.add("Floor Heat");
                        this.founction_count[9] = "1";
                        break;
                }
            }
        }
        this.mDraggableGridViewPager.setAdapter(this.mAdapter);
        this.mDraggableGridViewPager.setOnPageChangeListener(new DraggableGridViewType2.OnPageChangeListener() { // from class: com.shima.smartbushome.RoomActivity.1
            @Override // com.shima.smartbushome.assist.DraggableGridViewType2.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // com.shima.smartbushome.assist.DraggableGridViewType2.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
            }

            @Override // com.shima.smartbushome.assist.DraggableGridViewType2.OnPageChangeListener
            public void onPageSelected(int i2) {
            }
        });
        this.mDraggableGridViewPager.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.RoomActivity.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                if (RoomActivity.this.deletefc) {
                    String strTrim = ((TextView) view.findViewById(R.id.dragtype2tv)).getText().toString().trim();
                    Saveroom saveroom = new Saveroom();
                    saveroom.room_id = RoomActivity.this.roomid;
                    if (strTrim.equals("Light")) {
                        saveroom.light = 0;
                        RoomActivity.this.founctionlist[0] = "0";
                        MainActivity.mgr.deletefc("light", RoomActivity.this.roomid);
                    } else if (strTrim.equals("HVAC")) {
                        saveroom.hvac = 0;
                        RoomActivity.this.founctionlist[1] = "0";
                        MainActivity.mgr.deletefc("hvac", RoomActivity.this.roomid);
                    } else if (strTrim.equals("Mood")) {
                        saveroom.mood = 0;
                        RoomActivity.this.founctionlist[5] = "0";
                        MainActivity.mgr.deletefc("mood", RoomActivity.this.roomid);
                        MainActivity.mgr.deletefc("moodbutton", RoomActivity.this.roomid);
                    } else if (strTrim.equals("Fan")) {
                        saveroom.fan = 0;
                        RoomActivity.this.founctionlist[2] = "0";
                        MainActivity.mgr.deletefc("fan", RoomActivity.this.roomid);
                    } else if (strTrim.equals("Curtain")) {
                        saveroom.curtain = 0;
                        RoomActivity.this.founctionlist[3] = "0";
                        MainActivity.mgr.deletefc("curtain", RoomActivity.this.roomid);
                    } else if (strTrim.equals("Music")) {
                        saveroom.music = 0;
                        RoomActivity.this.founctionlist[4] = "0";
                        MainActivity.mgr.deletefc("music", RoomActivity.this.roomid);
                        MainActivity.mgr.deletefc("song", RoomActivity.this.roomid);
                        MainActivity.mgr.deletefc("radio", RoomActivity.this.roomid);
                    } else if (strTrim.equals("Other")) {
                        saveroom.other = 0;
                        RoomActivity.this.founctionlist[6] = "0";
                        MainActivity.mgr.deletefc("other", RoomActivity.this.roomid);
                    } else if (strTrim.equals("Media")) {
                        saveroom.media = 0;
                        RoomActivity.this.founctionlist[7] = "0";
                        MainActivity.mgr.deletefc("media", RoomActivity.this.roomid);
                        MainActivity.mgr.deletefc("mediabutton", RoomActivity.this.roomid);
                    } else if (strTrim.equals("9 in 1")) {
                        saveroom.nio = 0;
                        RoomActivity.this.founctionlist[8] = "0";
                        MainActivity.mgr.deletefc("nio", RoomActivity.this.roomid);
                    } else if (strTrim.equals("Floor Heat")) {
                        saveroom.fh = 0;
                        RoomActivity.this.founctionlist[9] = "0";
                        MainActivity.mgr.deletefc("floorheat", RoomActivity.this.roomid);
                    }
                    MainActivity.mgr.updateroom(saveroom, strTrim.toLowerCase());
                    RoomActivity.this.handlefc.postDelayed(RoomActivity.this.getfc, 20L);
                    RoomActivity.this.deletefc = false;
                    return;
                }
                Intent intent2 = new Intent();
                intent2.putExtra("roomid", RoomActivity.this.roomid);
                intent2.putExtra("founction_type", ((TextView) view.findViewById(R.id.dragtype2tv)).getText().toString().trim());
                intent2.setClass(RoomActivity.this, FounctionActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putStringArray("founction_count", RoomActivity.this.founction_count);
                intent2.putExtras(bundle2);
                RoomActivity.this.startActivity(intent2);
            }
        });
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    private void showToast(String str) {
        Toast.makeText(this, str, 0).show();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != 16908332) {
            switch (itemId) {
                case R.id.founction_add /* 2131231150 */:
                    new AlertView(null, null, "CANCEL", null, this.mItems, this, AlertView.Style.Alert, this.itemlick).show();
                    break;
                case R.id.founction_delete /* 2131231151 */:
                    if (this.mAdapter.getCount() > 0) {
                        this.deletefc = true;
                        showToast("pls click one item to delete");
                        break;
                    }
                    break;
            }
        } else {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        SystemUIUtil.setSystemUIVisible(this, false);
    }
}
