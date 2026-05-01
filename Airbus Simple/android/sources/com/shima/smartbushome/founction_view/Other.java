package com.shima.smartbushome.founction_view;

import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.OnoffTypeAdapter;
import com.shima.smartbushome.database.Saveother;
import com.shima.smartbushome.founction_command.othercontrol;
import com.shima.smartbushome.selflayout.OtherType1;
import com.shima.smartbushome.selflayout.OtherType2;
import com.shima.smartbushome.selflayout.OtherType3;
import com.shima.smartbushome.selflayout.OtherType4;
import com.shima.smartbushome.selflayout.OtherType5;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class Other extends Fragment {
    public static Context lightcontext;
    MenuItem add;
    AlertView addingalter;
    Dialog addonoffdialog;
    MenuItem delete;
    LinearLayout othercontrollayout;
    othercontrol reflash;
    View view;
    Handler onoffhandle = new Handler();
    Handler reflashroomonoff = new Handler();
    public List<OtherType1> type1list = new ArrayList();
    public List<OtherType2> type2list = new ArrayList();
    public List<OtherType3> type3list = new ArrayList();
    public List<OtherType4> type4list = new ArrayList();
    public List<OtherType5> type5list = new ArrayList();
    List<Saveother> roomonoff = new ArrayList();
    boolean intodeletemode = false;
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Other.2
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            AlertView alertView = Other.this.addingalter;
        }
    };
    Runnable getonofflist = new Runnable() { // from class: com.shima.smartbushome.founction_view.Other.3
        @Override // java.lang.Runnable
        public void run() {
            if (Other.this.roomonoff.size() > 0) {
                Other.this.roomonoff.clear();
            }
            Other.this.othercontrollayout.removeAllViews();
            List<Saveother> listQueryother = MainActivity.mgr.queryother();
            for (int i = 0; i < listQueryother.size(); i++) {
                if (listQueryother.get(i).room_id == FounctionActivity.roomidfc) {
                    Other.this.roomonoff.add(listQueryother.get(i));
                }
            }
            for (int i2 = 0; i2 < Other.this.roomonoff.size(); i2++) {
                Other.this.addspecView(Other.this.roomonoff.get(i2));
            }
            for (int i3 = 0; i3 < Other.this.roomonoff.size(); i3++) {
                switch (Other.this.roomonoff.get(i3).other_type) {
                    case 1:
                        Other.this.type1list.add((OtherType1) Other.this.othercontrollayout.findViewById(Other.this.roomonoff.get(i3).other_id));
                        break;
                    case 2:
                        Other.this.type2list.add((OtherType2) Other.this.othercontrollayout.findViewById(Other.this.roomonoff.get(i3).other_id));
                        break;
                    case 3:
                        Other.this.type3list.add((OtherType3) Other.this.othercontrollayout.findViewById(Other.this.roomonoff.get(i3).other_id));
                        break;
                    case 4:
                        Other.this.type4list.add((OtherType4) Other.this.othercontrollayout.findViewById(Other.this.roomonoff.get(i3).other_id));
                        break;
                    case 5:
                        Other.this.type5list.add((OtherType5) Other.this.othercontrollayout.findViewById(Other.this.roomonoff.get(i3).other_id));
                        break;
                }
            }
            Other.this.reflashroomlight();
            Other.this.onoffhandle.removeCallbacks(Other.this.getonofflist);
        }
    };
    Runnable reflashuiRun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Other.4
        @Override // java.lang.Runnable
        public void run() {
            Other.this.reflashroomlight();
        }
    };
    byte sub = 0;
    byte dev = 0;
    byte count = 0;
    Runnable reflashroomonoffrun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Other.5
        @Override // java.lang.Runnable
        public void run() {
            if (Other.this.count < Other.this.roomonoff.size()) {
                if (Other.this.roomonoff.get(Other.this.count).subnetID != (Other.this.sub & 255)) {
                    Other.this.sub = (byte) Other.this.roomonoff.get(Other.this.count).subnetID;
                    Other.this.dev = (byte) Other.this.roomonoff.get(Other.this.count).deviceID;
                    if (Other.this.roomonoff.get(Other.this.count).other_type == 1) {
                        Other.this.reflash.getDryContectState(Other.this.sub, Other.this.dev, MainActivity.mydupsocket);
                    } else {
                        Other.this.reflash.getonoffstate(Other.this.sub, Other.this.dev, MainActivity.mydupsocket);
                    }
                } else if (Other.this.roomonoff.get(Other.this.count).deviceID != (Other.this.dev & 255)) {
                    Other.this.sub = (byte) Other.this.roomonoff.get(Other.this.count).subnetID;
                    Other.this.dev = (byte) Other.this.roomonoff.get(Other.this.count).deviceID;
                    if (Other.this.roomonoff.get(Other.this.count).other_type == 1) {
                        Other.this.reflash.getDryContectState(Other.this.sub, Other.this.dev, MainActivity.mydupsocket);
                    } else {
                        Other.this.reflash.getonoffstate(Other.this.sub, Other.this.dev, MainActivity.mydupsocket);
                    }
                }
                Other other = Other.this;
                other.count = (byte) (other.count + 1);
                Other.this.reflashroomonoff.postDelayed(Other.this.reflashroomonoffrun, 70L);
                return;
            }
            Other.this.reflashroomonoff.removeCallbacks(Other.this.reflashroomonoffrun);
            Other.this.sub = (byte) 0;
            Other.this.dev = (byte) 0;
            Other.this.count = (byte) 0;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Other.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    Other.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                Other.this.getActivity().finish();
            } else if (FounctionActivity.ACTION_DELETEOTHER.equals(action)) {
                Other.this.onoffhandle.postDelayed(Other.this.getonofflist, 30L);
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_other, viewGroup, false);
        this.othercontrollayout = (LinearLayout) this.view.findViewById(R.id.otherlinearLayout);
        getActivity().setTitle("Other");
        setHasOptionsMenu(true);
        this.reflash = new othercontrol();
        this.onoffhandle.postDelayed(this.getonofflist, 30L);
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
        this.reflashroomonoff.removeCallbacks(this.reflashroomonoffrun);
        this.onoffhandle.removeCallbacks(this.getonofflist);
        this.type1list.clear();
        this.roomonoff.clear();
        this.intodeletemode = false;
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.light_setting_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
        this.add = menu.findItem(R.id.light_add);
        this.delete = menu.findItem(R.id.light_remove);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.light_add /* 2131231274 */:
                if (!MainActivity.islockchangeid) {
                    if (this.intodeletemode) {
                        this.add.setTitle("ADD");
                        this.delete.setTitle("DELETE");
                        this.intodeletemode = false;
                        for (int i = 0; i < this.type1list.size(); i++) {
                            this.type1list.get(i).setdeletevisable(false);
                        }
                        for (int i2 = 0; i2 < this.type2list.size(); i2++) {
                            this.type2list.get(i2).setdeletevisable(false);
                        }
                        for (int i3 = 0; i3 < this.type3list.size(); i3++) {
                            this.type3list.get(i3).setdeletevisable(false);
                        }
                        for (int i4 = 0; i4 < this.type4list.size(); i4++) {
                            this.type4list.get(i4).setdeletevisable(false);
                        }
                        for (int i5 = 0; i5 < this.type5list.size(); i5++) {
                            this.type5list.get(i5).setdeletevisable(false);
                        }
                        break;
                    } else {
                        ListView listView = new ListView(getActivity());
                        this.addingalter = new AlertView(null, null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                        listView.setAdapter((ListAdapter) new OnoffTypeAdapter(getActivity()));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Other.1
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view, int i6, long j) {
                                int i7 = Other.this.roomonoff.size() == 0 ? 1 : 1 + Other.this.roomonoff.get(Other.this.roomonoff.size() - 1).other_id;
                                ArrayList arrayList = new ArrayList();
                                switch (i6) {
                                    case 0:
                                        arrayList.add(new Saveother(FounctionActivity.roomidfc, 0, 0, i7, 0, 0, "single" + i7, "other_icon1", 1));
                                        MainActivity.mgr.addother(arrayList);
                                        break;
                                    case 1:
                                        arrayList.add(new Saveother(FounctionActivity.roomidfc, 0, 0, i7, 0, 0, "interlock" + i7, "other_icon8", 2));
                                        MainActivity.mgr.addother(arrayList);
                                        break;
                                    case 2:
                                        arrayList.add(new Saveother(FounctionActivity.roomidfc, 0, 0, i7, 0, 0, "logic" + i7, "other_icon3", 3));
                                        MainActivity.mgr.addother(arrayList);
                                        break;
                                    case 3:
                                        arrayList.add(new Saveother(FounctionActivity.roomidfc, 0, 0, i7, 0, 0, "scene" + i7, "other_icon2", 4));
                                        MainActivity.mgr.addother(arrayList);
                                        break;
                                    case 4:
                                        arrayList.add(new Saveother(FounctionActivity.roomidfc, 0, 0, i7, 0, 0, "sequence" + i7, "other_icon7", 5));
                                        MainActivity.mgr.addother(arrayList);
                                        break;
                                }
                                Other.this.onoffhandle.postDelayed(Other.this.getonofflist, 30L);
                                Other.this.addingalter.dismiss();
                            }
                        });
                        this.addingalter.addExtView(listView);
                        this.addingalter.show();
                        break;
                    }
                }
                break;
            case R.id.light_remove /* 2131231275 */:
                if (!MainActivity.islockchangeid) {
                    this.intodeletemode = !this.intodeletemode;
                    if (this.intodeletemode) {
                        this.add.setTitle("CANCLE DELETE");
                        this.delete.setTitle("DELETE");
                        if (this.type1list.size() > 0) {
                            this.type1list.clear();
                        }
                        if (this.type2list.size() > 0) {
                            this.type2list.clear();
                        }
                        if (this.type3list.size() > 0) {
                            this.type3list.clear();
                        }
                        if (this.type4list.size() > 0) {
                            this.type4list.clear();
                        }
                        if (this.type5list.size() > 0) {
                            this.type5list.clear();
                        }
                        for (int i6 = 0; i6 < this.roomonoff.size(); i6++) {
                            switch (this.roomonoff.get(i6).other_type) {
                                case 1:
                                    OtherType1 otherType1 = (OtherType1) this.othercontrollayout.findViewById(this.roomonoff.get(i6).other_id);
                                    this.type1list.add(otherType1);
                                    otherType1.setdeletevisable(true);
                                    break;
                                case 2:
                                    OtherType2 otherType2 = (OtherType2) this.othercontrollayout.findViewById(this.roomonoff.get(i6).other_id);
                                    this.type2list.add(otherType2);
                                    otherType2.setdeletevisable(true);
                                    break;
                                case 3:
                                    OtherType3 otherType3 = (OtherType3) this.othercontrollayout.findViewById(this.roomonoff.get(i6).other_id);
                                    this.type3list.add(otherType3);
                                    otherType3.setdeletevisable(true);
                                    break;
                                case 4:
                                    OtherType4 otherType4 = (OtherType4) this.othercontrollayout.findViewById(this.roomonoff.get(i6).other_id);
                                    this.type4list.add(otherType4);
                                    otherType4.setdeletevisable(true);
                                    break;
                                case 5:
                                    OtherType5 otherType5 = (OtherType5) this.othercontrollayout.findViewById(this.roomonoff.get(i6).other_id);
                                    this.type5list.add(otherType5);
                                    otherType5.setdeletevisable(true);
                                    break;
                            }
                        }
                        break;
                    } else {
                        for (int i7 = 0; i7 < this.type1list.size(); i7++) {
                            if (this.type1list.get(i7).getIfneedtoDelete()) {
                                MainActivity.mgr.deleteother("other", this.type1list.get(i7).getType1otherid(), FounctionActivity.roomidfc);
                            }
                        }
                        for (int i8 = 0; i8 < this.type2list.size(); i8++) {
                            if (this.type2list.get(i8).getIfneedtoDelete()) {
                                MainActivity.mgr.deleteother("other", this.type2list.get(i8).getType2otherid(), FounctionActivity.roomidfc);
                            }
                        }
                        for (int i9 = 0; i9 < this.type3list.size(); i9++) {
                            if (this.type3list.get(i9).getIfneedtoDelete()) {
                                MainActivity.mgr.deleteother("other", this.type3list.get(i9).getType3otherid(), FounctionActivity.roomidfc);
                            }
                        }
                        for (int i10 = 0; i10 < this.type4list.size(); i10++) {
                            if (this.type4list.get(i10).getIfneedtoDelete()) {
                                MainActivity.mgr.deleteother("other", this.type4list.get(i10).getType4otherid(), FounctionActivity.roomidfc);
                            }
                        }
                        for (int i11 = 0; i11 < this.type5list.size(); i11++) {
                            if (this.type5list.get(i11).getIfneedtoDelete()) {
                                MainActivity.mgr.deleteother("other", this.type5list.get(i11).getType4otherid(), FounctionActivity.roomidfc);
                            }
                        }
                        this.onoffhandle.postDelayed(this.getonofflist, 30L);
                        this.add.setTitle("ADD");
                        this.delete.setTitle("DELETE");
                        this.intodeletemode = false;
                        break;
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addspecView(Saveother saveother) {
        switch (saveother.other_type) {
            case 1:
                OtherType1 otherType1 = new OtherType1(this.othercontrollayout.getContext());
                otherType1.setcontan(saveother);
                otherType1.setId(saveother.other_id);
                this.othercontrollayout.addView(otherType1);
                break;
            case 2:
                OtherType2 otherType2 = new OtherType2(this.othercontrollayout.getContext());
                otherType2.setcontan(saveother);
                otherType2.setId(saveother.other_id);
                this.othercontrollayout.addView(otherType2);
                break;
            case 3:
                OtherType3 otherType3 = new OtherType3(this.othercontrollayout.getContext());
                otherType3.setcontan(saveother);
                otherType3.setId(saveother.other_id);
                this.othercontrollayout.addView(otherType3);
                break;
            case 4:
                OtherType4 otherType4 = new OtherType4(this.othercontrollayout.getContext());
                otherType4.setcontan(saveother);
                otherType4.setId(saveother.other_id);
                this.othercontrollayout.addView(otherType4);
                break;
            case 5:
                OtherType5 otherType5 = new OtherType5(this.othercontrollayout.getContext());
                otherType5.setcontan(saveother);
                otherType5.setId(saveother.other_id);
                this.othercontrollayout.addView(otherType5);
                break;
        }
    }

    public void reflashroomlight() {
        this.reflashroomonoff.postDelayed(this.reflashroomonoffrun, 0L);
    }

    private static String byte2hex(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            str = str + " " + hexString;
        }
        return str;
    }

    public void RunReceiveData(byte[] bArr) {
        int i;
        int i2;
        int i3 = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        String str = Integer.toHexString(i3) + IOUtils.LINE_SEPARATOR_UNIX;
        int i4 = bArr[17] & 255;
        int i5 = bArr[18] & 255;
        int i6 = 0;
        if (i3 == 50) {
            if (bArr[26] == -8) {
                int i7 = bArr[25] & 255;
                OtherType1 otherType1 = null;
                while (i6 < this.type1list.size()) {
                    if (this.type1list.get(i6).getsubid() == i4 && this.type1list.get(i6).getdevid() == i5 && this.type1list.get(i6).getchannel() == i7) {
                        otherType1 = this.type1list.get(i6);
                    }
                    i6++;
                }
                if (otherType1 != null) {
                    otherType1.setReceiveChange(bArr[27]);
                    return;
                }
                return;
            }
            return;
        }
        if (i3 == 52) {
            while (i6 < this.type1list.size()) {
                if (this.type1list.get(i6).getsubid() == i4 && this.type1list.get(i6).getdevid() == i5) {
                    this.type1list.get(i6).setReceiveChange(bArr[this.type1list.get(i6).getchannel() + 25]);
                }
                i6++;
            }
            return;
        }
        if (i3 == 301) {
            for (int i8 = 0; i8 < this.type1list.size(); i8++) {
                if (this.type1list.get(i8).getsubid() == i4 && this.type1list.get(i8).getdevid() == i5 && (i = bArr[26] & 255) >= this.type1list.get(i8).getchannel()) {
                    if ((bArr[i + 26 + this.type1list.get(i8).getchannel()] & 255) == 0) {
                        this.type1list.get(i8).setReceiveChange((byte) 0);
                    } else {
                        this.type1list.get(i8).setReceiveChange((byte) 100);
                    }
                }
            }
            return;
        }
        if (i3 == 56354 || i3 == 57367) {
            for (int i9 = 0; i9 < this.type1list.size(); i9++) {
                if (this.type1list.get(i9).getsubid() == i4 && this.type1list.get(i9).getdevid() == i5 && (i2 = bArr[25] & 255) >= this.type1list.get(i9).getchannel()) {
                    if ((bArr[i2 + 25 + this.type1list.get(i9).getchannel()] & 255) == 0) {
                        this.type1list.get(i9).setReceiveChange((byte) 0);
                    } else {
                        this.type1list.get(i9).setReceiveChange((byte) 100);
                    }
                }
            }
        }
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_DELETEOTHER);
        return intentFilter;
    }
}
