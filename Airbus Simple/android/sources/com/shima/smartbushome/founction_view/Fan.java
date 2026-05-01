package com.shima.smartbushome.founction_view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActivityChooserView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savefan;
import com.shima.smartbushome.founction_command.fancontrol;
import com.shima.smartbushome.selflayout.FanType1;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Fan extends Fragment {
    MenuItem add;
    MenuItem delete;
    LinearLayout fancontrollayout;
    View view;
    List<FanType1> type1list = new ArrayList();
    List<Savefan> roomfan = new ArrayList();
    boolean intodeletemode = false;
    Handler fanhandler = new Handler();
    Runnable getfanlist = new Runnable() { // from class: com.shima.smartbushome.founction_view.Fan.1
        @Override // java.lang.Runnable
        public void run() {
            if (Fan.this.roomfan.size() > 0) {
                Fan.this.roomfan.clear();
            }
            Fan.this.fancontrollayout.removeAllViews();
            List<Savefan> listQueryfan = MainActivity.mgr.queryfan();
            for (int i = 0; i < listQueryfan.size(); i++) {
                if (listQueryfan.get(i).room_id == FounctionActivity.roomidfc) {
                    Fan.this.roomfan.add(listQueryfan.get(i));
                }
            }
            for (int i2 = 0; i2 < Fan.this.roomfan.size(); i2++) {
                Fan.this.addspecView(Fan.this.roomfan.get(i2));
            }
            for (int i3 = 0; i3 < Fan.this.roomfan.size(); i3++) {
                if (Fan.this.roomfan.get(i3).fan_Type == 1) {
                    Fan.this.type1list.add((FanType1) Fan.this.fancontrollayout.findViewById(Fan.this.roomfan.get(i3).fan_id));
                }
            }
            Fan.this.fanhandler.removeCallbacks(Fan.this.getfanlist);
        }
    };
    fancontrol fcommand = new fancontrol();
    int shakecount = 0;
    Handler shakehandler = new Handler();
    int shakefanvalue = 0;
    Runnable shakerun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Fan.2
        @Override // java.lang.Runnable
        public void run() {
            if (Fan.this.shakecount >= Fan.this.roomfan.size()) {
                Fan.this.shakecount = 0;
                Fan.this.shakehandler.removeCallbacks(Fan.this.shakerun);
            } else {
                Fan.this.fcommand.FanChannelControl((byte) Fan.this.roomfan.get(Fan.this.shakecount).subnetID, (byte) Fan.this.roomfan.get(Fan.this.shakecount).deviceID, Fan.this.roomfan.get(Fan.this.shakecount).channel, Fan.this.shakefanvalue, MainActivity.mydupsocket);
                Fan.this.shakecount++;
                Fan.this.shakehandler.postDelayed(Fan.this.shakerun, 80L);
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Fan.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                int length = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN).length;
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                Fan.this.getActivity().finish();
                return;
            }
            if (FounctionActivity.ACTION_DELETEFAN.equals(action)) {
                Fan.this.fanhandler.postDelayed(Fan.this.getfanlist, 30L);
            } else {
                if (!FounctionActivity.ACTION_SHAKE.equals(action) || MainActivity.islockshake) {
                    return;
                }
                Fan.this.shakeperform(intent.getIntExtra("shake_type", 0));
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getActivity().setTitle("Fan");
        this.view = layoutInflater.inflate(R.layout.fragment_fan, viewGroup, false);
        this.fancontrollayout = (LinearLayout) this.view.findViewById(R.id.fanlayout);
        setHasOptionsMenu(true);
        this.fanhandler.postDelayed(this.getfanlist, 30L);
        setMargins((ImageView) this.view.findViewById(R.id.iv_layout), 0, -FounctionActivity.topHeight, 0, 0);
        return this.view;
    }

    public static void setMargins(View view, int i, int i2, int i3, int i4) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i, i2, i3, i4);
            view.requestLayout();
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(this.mGattUpdateReceiver, makeUpdateIntentFilter());
    }

    @Override // android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(this.mGattUpdateReceiver);
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.type1list.clear();
        this.intodeletemode = false;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.curtain_setting_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
        this.add = menu.findItem(R.id.add_curtain);
        this.delete = menu.findItem(R.id.delete_curtain);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.add_curtain) {
            if (!MainActivity.islockchangeid) {
                if (this.intodeletemode) {
                    this.add.setTitle("ADD");
                    this.delete.setTitle("DELETE");
                    this.intodeletemode = false;
                    for (int i = 0; i < this.type1list.size(); i++) {
                        this.type1list.get(i).setdeletevisable(false);
                    }
                } else {
                    int i2 = this.roomfan.size() == 0 ? 1 : 1 + this.roomfan.get(this.roomfan.size() - 1).fan_id;
                    MainActivity.mgr.addfan(new Savefan(FounctionActivity.roomidfc, 0, 0, i2, "fan" + i2, 0, 1, "fan_icon1"));
                    Toast.makeText(getActivity(), "add succeed", 0).show();
                    this.fanhandler.postDelayed(this.getfanlist, 30L);
                }
            }
        } else if (itemId == R.id.delete_curtain && !MainActivity.islockchangeid) {
            this.intodeletemode = !this.intodeletemode;
            if (this.intodeletemode) {
                this.add.setTitle("CANCLE DELETE");
                this.delete.setTitle("DELETE");
                if (this.type1list.size() > 0) {
                    this.type1list.clear();
                }
                for (int i3 = 0; i3 < this.roomfan.size(); i3++) {
                    if (this.roomfan.get(i3).fan_Type == 1) {
                        FanType1 fanType1 = (FanType1) this.fancontrollayout.findViewById(this.roomfan.get(i3).fan_id);
                        this.type1list.add(fanType1);
                        fanType1.setdeletevisable(true);
                    }
                }
            } else {
                for (int i4 = 0; i4 < this.type1list.size(); i4++) {
                    if (this.type1list.get(i4).getIfneedtoDelete()) {
                        MainActivity.mgr.deletefan("fan", this.type1list.get(i4).getType1fanid(), FounctionActivity.roomidfc);
                    }
                }
                this.fanhandler.postDelayed(this.getfanlist, 30L);
                this.add.setTitle("ADD");
                this.delete.setTitle("DELETE");
                this.intodeletemode = false;
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addspecView(Savefan savefan) {
        if (savefan.fan_Type != 1) {
            return;
        }
        FanType1 fanType1 = new FanType1(this.fancontrollayout.getContext());
        fanType1.setcontant(savefan);
        fanType1.setId(savefan.fan_id);
        this.fancontrollayout.addView(fanType1);
    }

    public void shakeperform(int i) {
        switch (i) {
            case 1:
                this.shakefanvalue--;
                if (this.shakefanvalue <= 0) {
                    this.shakefanvalue = 0;
                }
                this.shakehandler.postDelayed(this.shakerun, 30L);
                break;
            case 2:
                this.shakefanvalue++;
                if (this.shakefanvalue >= 4) {
                    this.shakefanvalue = 4;
                }
                this.shakehandler.postDelayed(this.shakerun, 30L);
                break;
        }
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_DELETEFAN);
        intentFilter.addAction(FounctionActivity.ACTION_SHAKE);
        return intentFilter;
    }
}
