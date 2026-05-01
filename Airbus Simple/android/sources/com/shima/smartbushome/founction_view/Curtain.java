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
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.founction_command.curtaincontrol;
import com.shima.smartbushome.selflayout.CurtainType1;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Curtain extends Fragment {
    MenuItem add;
    LinearLayout curtaincontrollayout;
    MenuItem delete;
    View view;
    List<CurtainType1> type1list = new ArrayList();
    List<Savecurtain> roomcurtain = new ArrayList();
    boolean intodeletemode = false;
    Handler curtainhandler = new Handler();
    Runnable getcurtainlist = new Runnable() { // from class: com.shima.smartbushome.founction_view.Curtain.1
        @Override // java.lang.Runnable
        public void run() {
            if (Curtain.this.roomcurtain.size() > 0) {
                Curtain.this.roomcurtain.clear();
            }
            Curtain.this.curtaincontrollayout.removeAllViews();
            List<Savecurtain> listQuerycurtain = MainActivity.mgr.querycurtain();
            for (int i = 0; i < listQuerycurtain.size(); i++) {
                if (listQuerycurtain.get(i).room_id == FounctionActivity.roomidfc) {
                    Curtain.this.roomcurtain.add(listQuerycurtain.get(i));
                }
            }
            for (int i2 = 0; i2 < Curtain.this.roomcurtain.size(); i2++) {
                Curtain.this.addspecView(Curtain.this.roomcurtain.get(i2));
            }
            for (int i3 = 0; i3 < Curtain.this.roomcurtain.size(); i3++) {
                if (Curtain.this.roomcurtain.get(i3).curtain_type == 1) {
                    Curtain.this.type1list.add((CurtainType1) Curtain.this.curtaincontrollayout.findViewById(Curtain.this.roomcurtain.get(i3).curtain_id));
                }
            }
            Curtain.this.curtainhandler.removeCallbacks(Curtain.this.getcurtainlist);
        }
    };
    curtaincontrol ccomand = new curtaincontrol();
    int shakecount = 0;
    Handler shakehandler = new Handler();
    String shakecurtainvalue = "";
    Runnable shakerun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Curtain.2
        @Override // java.lang.Runnable
        public void run() {
            if (Curtain.this.shakecount >= Curtain.this.roomcurtain.size()) {
                Curtain.this.shakecurtainvalue = "";
                Curtain.this.shakecount = 0;
                Curtain.this.shakehandler.removeCallbacks(Curtain.this.shakerun);
            } else {
                Curtain.this.ccomand.CurtainControl((byte) Curtain.this.roomcurtain.get(Curtain.this.shakecount).subnetID, (byte) Curtain.this.roomcurtain.get(Curtain.this.shakecount).deviceID, Curtain.this.roomcurtain.get(Curtain.this.shakecount).channel_1, Curtain.this.roomcurtain.get(Curtain.this.shakecount).channel_2, Curtain.this.shakecurtainvalue, MainActivity.mydupsocket);
                Curtain.this.shakecount++;
                Curtain.this.shakehandler.postDelayed(Curtain.this.shakerun, 80L);
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Curtain.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    Curtain.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                Curtain.this.getActivity().finish();
                return;
            }
            if (FounctionActivity.ACTION_DELETECURTAIN.equals(action)) {
                Curtain.this.curtainhandler.postDelayed(Curtain.this.getcurtainlist, 30L);
            } else {
                if (!FounctionActivity.ACTION_SHAKE.equals(action) || MainActivity.islockshake) {
                    return;
                }
                Curtain.this.shakeperform(intent.getIntExtra("shake_type", 0));
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getActivity().setTitle("Curtain");
        this.view = layoutInflater.inflate(R.layout.fragment_curtain, viewGroup, false);
        this.curtaincontrollayout = (LinearLayout) this.view.findViewById(R.id.curtainlayout);
        setHasOptionsMenu(true);
        this.curtainhandler.postDelayed(this.getcurtainlist, 30L);
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
                    int i2 = this.roomcurtain.size() == 0 ? 1 : 1 + this.roomcurtain.get(this.roomcurtain.size() - 1).curtain_id;
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new Savecurtain(FounctionActivity.roomidfc, 0, 0, i2, 1, 0, 1, "curtain" + i2, "close"));
                    MainActivity.mgr.addcurtain(arrayList);
                    Toast.makeText(getActivity(), "add succeed", 0).show();
                    this.curtainhandler.postDelayed(this.getcurtainlist, 30L);
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
                for (int i3 = 0; i3 < this.roomcurtain.size(); i3++) {
                    if (this.roomcurtain.get(i3).curtain_type == 1) {
                        CurtainType1 curtainType1 = (CurtainType1) this.curtaincontrollayout.findViewById(this.roomcurtain.get(i3).curtain_id);
                        this.type1list.add(curtainType1);
                        curtainType1.setdeletevisable(true);
                    }
                }
            } else {
                for (int i4 = 0; i4 < this.type1list.size(); i4++) {
                    if (this.type1list.get(i4).getIfneedtoDelete()) {
                        MainActivity.mgr.deletecurtain("curtain", this.type1list.get(i4).getType1curtainid(), FounctionActivity.roomidfc);
                    }
                }
                this.curtainhandler.postDelayed(this.getcurtainlist, 30L);
                this.add.setTitle("ADD");
                this.delete.setTitle("DELETE");
                this.intodeletemode = false;
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addspecView(Savecurtain savecurtain) {
        if (savecurtain.curtain_type != 1) {
            return;
        }
        CurtainType1 curtainType1 = new CurtainType1(this.curtaincontrollayout.getContext());
        curtainType1.setcontan(savecurtain);
        curtainType1.setId(savecurtain.curtain_id);
        this.curtaincontrollayout.addView(curtainType1);
    }

    public void shakeperform(int i) {
        if (i != 6) {
            switch (i) {
                case 1:
                    this.shakecurtainvalue = "close";
                    this.shakehandler.postDelayed(this.shakerun, 30L);
                    break;
                case 2:
                    this.shakecurtainvalue = "open";
                    this.shakehandler.postDelayed(this.shakerun, 30L);
                    break;
            }
        }
        this.shakecurtainvalue = "stop";
        this.shakehandler.postDelayed(this.shakerun, 30L);
    }

    public void RunReceiveData(byte[] bArr) {
        if (((bArr[21] & 255) << 8) + (bArr[22] & 255) == 50 && bArr[26] == -8) {
            int i = bArr[17] & 255;
            int i2 = bArr[18] & 255;
            int i3 = bArr[25] & 255;
            for (int i4 = 0; i4 < this.type1list.size(); i4++) {
                if (this.type1list.get(i4).getsubid() == i && this.type1list.get(i4).getdevid() == i2 && this.type1list.get(i4).getchannel()[0] == i3) {
                    if (bArr[27] == 100) {
                        this.type1list.get(i4).setReceiveChange(0);
                    }
                } else if (this.type1list.get(i4).getsubid() == i && this.type1list.get(i4).getdevid() == i2 && this.type1list.get(i4).getchannel()[1] == i3 && bArr[27] == 100) {
                    this.type1list.get(i4).setReceiveChange(1);
                }
            }
        }
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_DELETECURTAIN);
        intentFilter.addAction(FounctionActivity.ACTION_SHAKE);
        return intentFilter;
    }
}
