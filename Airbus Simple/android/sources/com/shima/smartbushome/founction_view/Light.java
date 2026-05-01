package com.shima.smartbushome.founction_view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActivityChooserView;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.LightTypeAdapter;
import com.shima.smartbushome.assist.BLE.smartBeaconManager;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.selflayout.LightType1;
import com.shima.smartbushome.selflayout.LightType2;
import com.shima.smartbushome.selflayout.LightType3;
import com.shima.smartbushome.selflayout.LightType4;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class Light extends Fragment {
    public static Context lightcontext;
    MenuItem add;
    smartBeaconManager beaconManager;
    MenuItem delete;
    LinearLayout lightcontrollayout;
    TextView log;
    lightcontrol reflash;
    View view;
    Handler lighthandle = new Handler();
    Handler reflashroomlight = new Handler();
    public List<LightType1> type1list = new ArrayList();
    public List<LightType2> type2list = new ArrayList();
    public List<LightType3> type3list = new ArrayList();
    public List<LightType4> type4list = new ArrayList();
    List<Savelight> roomlight = new ArrayList();
    boolean intodeletemode = false;
    boolean onetime = false;
    Runnable getlightlist = new Runnable() { // from class: com.shima.smartbushome.founction_view.Light.2
        @Override // java.lang.Runnable
        public void run() {
            if (Light.this.roomlight.size() > 0) {
                Light.this.roomlight.clear();
            }
            Light.this.lightcontrollayout.removeAllViews();
            List<Savelight> listQuerylight = MainActivity.mgr.querylight();
            for (int i = 0; i < listQuerylight.size(); i++) {
                if (listQuerylight.get(i).room_id == FounctionActivity.roomidfc) {
                    Light.this.roomlight.add(listQuerylight.get(i));
                }
            }
            for (int i2 = 0; i2 < Light.this.roomlight.size(); i2++) {
                Light.this.addspecView(Light.this.roomlight.get(i2));
            }
            for (int i3 = 0; i3 < Light.this.roomlight.size(); i3++) {
                switch (Light.this.roomlight.get(i3).lightType) {
                    case 1:
                        Light.this.type1list.add((LightType1) Light.this.lightcontrollayout.findViewById(Light.this.roomlight.get(i3).light_id));
                        break;
                    case 2:
                        Light.this.type2list.add((LightType2) Light.this.lightcontrollayout.findViewById(Light.this.roomlight.get(i3).light_id));
                        break;
                    case 3:
                        Light.this.type3list.add((LightType3) Light.this.lightcontrollayout.findViewById(Light.this.roomlight.get(i3).light_id));
                        break;
                    case 4:
                        Light.this.type4list.add((LightType4) Light.this.lightcontrollayout.findViewById(Light.this.roomlight.get(i3).light_id));
                        break;
                }
            }
            Light.this.reflashroomlight();
            Light.this.lighthandle.removeCallbacks(Light.this.getlightlist);
        }
    };
    Runnable reflashuiRun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Light.3
        @Override // java.lang.Runnable
        public void run() {
            Light.this.reflashroomlight();
        }
    };
    byte sub = 0;
    byte dev = 0;
    byte count = 0;
    boolean getstatefinish = false;
    int getcount = 0;
    int getcount2 = 0;
    int timeout = 0;
    Runnable reflashroomlightrun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Light.4
        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < Light.this.roomlight.size(); i++) {
                if (Light.this.sub != ((byte) Light.this.roomlight.get(i).subnetID) || Light.this.dev != ((byte) Light.this.roomlight.get(i).deviceID)) {
                    Light.this.sub = (byte) Light.this.roomlight.get(i).subnetID;
                    Light.this.dev = (byte) Light.this.roomlight.get(i).deviceID;
                    Light.this.reflash.getlightstate(Light.this.sub, Light.this.dev, MainActivity.mydupsocket);
                }
            }
            Light.this.reflash.getlightstate(Light.this.sub, Light.this.dev, MainActivity.mydupsocket);
        }
    };
    int shakecount = 0;
    Handler shakehandler = new Handler();
    int shakelightvalue = 999;
    int shakeledvalue = 999;
    Runnable shakerun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Light.5
        @Override // java.lang.Runnable
        public void run() {
            if (Light.this.shakecount >= Light.this.roomlight.size()) {
                Light.this.shakelightvalue = 999;
                Light.this.shakeledvalue = 999;
                Light.this.shakecount = 0;
                Light.this.shakehandler.removeCallbacks(Light.this.shakerun);
                return;
            }
            if (Light.this.roomlight.get(Light.this.shakecount).lightType != 3) {
                Light.this.reflash.SingleChannelControl((byte) Light.this.roomlight.get(Light.this.shakecount).subnetID, (byte) Light.this.roomlight.get(Light.this.shakecount).deviceID, Light.this.roomlight.get(Light.this.shakecount).channel, Light.this.shakelightvalue, MainActivity.mydupsocket);
            } else {
                Light.this.reflash.ARGBlightcontrol((byte) Light.this.roomlight.get(Light.this.shakecount).subnetID, (byte) Light.this.roomlight.get(Light.this.shakecount).deviceID, Light.this.shakeledvalue, MainActivity.mydupsocket);
            }
            Light.this.shakecount++;
            Light.this.shakehandler.postDelayed(Light.this.shakerun, 70L);
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Light.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    Light.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                Light.this.getActivity().finish();
                Light.this.reflashroomlight.removeCallbacks(Light.this.reflashroomlightrun);
            } else if (FounctionActivity.ACTION_DELETELIGHT.equals(action)) {
                Light.this.lighthandle.postDelayed(Light.this.getlightlist, 30L);
            } else {
                if (!FounctionActivity.ACTION_SHAKE.equals(action) || MainActivity.islockshake) {
                    return;
                }
                Light.this.shakeperform(intent.getIntExtra("shake_type", 0));
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_light, viewGroup, false);
        this.log = (TextView) this.view.findViewById(R.id.textView);
        this.log.setMovementMethod(new ScrollingMovementMethod());
        this.lightcontrollayout = (LinearLayout) this.view.findViewById(R.id.linearLayout2);
        getActivity().setTitle("Light");
        setHasOptionsMenu(true);
        this.reflash = new lightcontrol();
        this.lighthandle.postDelayed(this.getlightlist, 30L);
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
        this.reflashroomlight.removeCallbacks(this.reflashroomlightrun);
        this.lighthandle.removeCallbacks(this.getlightlist);
        this.type1list.clear();
        this.type2list.clear();
        this.type3list.clear();
        this.type4list.clear();
        this.roomlight.clear();
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
                        break;
                    } else {
                        final AlertView alertView = new AlertView(null, null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, null);
                        ListView listView = new ListView(getActivity());
                        listView.setAdapter((ListAdapter) new LightTypeAdapter(getActivity()));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Light.1
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view, int i5, long j) {
                                int i6 = Light.this.roomlight.size() == 0 ? 1 : 1 + Light.this.roomlight.get(Light.this.roomlight.size() - 1).light_id;
                                ArrayList arrayList = new ArrayList();
                                switch (i5) {
                                    case 0:
                                        arrayList.add(new Savelight(FounctionActivity.roomidfc, 0, 0, i6, 0, 100, 1, "light" + i6, "light_icon1"));
                                        MainActivity.mgr.addlight(arrayList);
                                        break;
                                    case 1:
                                        arrayList.add(new Savelight(FounctionActivity.roomidfc, 0, 0, i6, 0, 100, 2, "light" + i6, "light_icon1"));
                                        MainActivity.mgr.addlight(arrayList);
                                        break;
                                    case 2:
                                        arrayList.add(new Savelight(FounctionActivity.roomidfc, 0, 0, i6, 0, 100, 3, "light" + i6, "light_icon1"));
                                        MainActivity.mgr.addlight(arrayList);
                                        break;
                                    case 3:
                                        arrayList.add(new Savelight(FounctionActivity.roomidfc, 0, 0, i6, 0, 100, 4, "light" + i6, "light_icon1"));
                                        MainActivity.mgr.addlight(arrayList);
                                        break;
                                }
                                Light.this.lighthandle.postDelayed(Light.this.getlightlist, 30L);
                                alertView.dismiss();
                            }
                        });
                        alertView.addExtView(listView);
                        alertView.show();
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
                        for (int i5 = 0; i5 < this.roomlight.size(); i5++) {
                            switch (this.roomlight.get(i5).lightType) {
                                case 1:
                                    LightType1 lightType1 = (LightType1) this.lightcontrollayout.findViewById(this.roomlight.get(i5).light_id);
                                    this.type1list.add(lightType1);
                                    lightType1.setdeletevisable(true);
                                    break;
                                case 2:
                                    LightType2 lightType2 = (LightType2) this.lightcontrollayout.findViewById(this.roomlight.get(i5).light_id);
                                    this.type2list.add(lightType2);
                                    lightType2.setdeletevisable(true);
                                    break;
                                case 3:
                                    LightType3 lightType3 = (LightType3) this.lightcontrollayout.findViewById(this.roomlight.get(i5).light_id);
                                    this.type3list.add(lightType3);
                                    lightType3.setdeletevisable(true);
                                    break;
                                case 4:
                                    LightType4 lightType4 = (LightType4) this.lightcontrollayout.findViewById(this.roomlight.get(i5).light_id);
                                    this.type4list.add(lightType4);
                                    lightType4.setdeletevisable(true);
                                    break;
                            }
                        }
                        break;
                    } else {
                        for (int i6 = 0; i6 < this.type1list.size(); i6++) {
                            if (this.type1list.get(i6).getIfneedtoDelete()) {
                                MainActivity.mgr.deletelight("light", this.type1list.get(i6).getType1lightid(), FounctionActivity.roomidfc);
                            }
                        }
                        for (int i7 = 0; i7 < this.type2list.size(); i7++) {
                            if (this.type2list.get(i7).getIfneedtoDelete()) {
                                MainActivity.mgr.deletelight("light", this.type2list.get(i7).getType2lightid(), FounctionActivity.roomidfc);
                            }
                        }
                        for (int i8 = 0; i8 < this.type3list.size(); i8++) {
                            if (this.type3list.get(i8).getIfneedtoDelete()) {
                                MainActivity.mgr.deletelight("light", this.type3list.get(i8).getType3lightid(), FounctionActivity.roomidfc);
                            }
                        }
                        for (int i9 = 0; i9 < this.type4list.size(); i9++) {
                            if (this.type4list.get(i9).getIfneedtoDelete()) {
                                MainActivity.mgr.deletelight("light", this.type4list.get(i9).getType4lightid(), FounctionActivity.roomidfc);
                            }
                        }
                        this.lighthandle.postDelayed(this.getlightlist, 30L);
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
    public void addspecView(Savelight savelight) {
        switch (savelight.lightType) {
            case 1:
                LightType1 lightType1 = new LightType1(this.lightcontrollayout.getContext());
                lightType1.setcontant(savelight);
                lightType1.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType1);
                break;
            case 2:
                LightType2 lightType2 = new LightType2(this.lightcontrollayout.getContext());
                lightType2.setcontant(savelight);
                lightType2.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType2);
                break;
            case 3:
                LightType3 lightType3 = new LightType3(this.lightcontrollayout.getContext());
                lightType3.setcontant(savelight);
                lightType3.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType3);
                break;
            case 4:
                LightType4 lightType4 = new LightType4(this.lightcontrollayout.getContext());
                lightType4.setcontant(savelight);
                lightType4.setId(savelight.light_id);
                this.lightcontrollayout.addView(lightType4);
                break;
        }
    }

    public void reflashroomlight() {
        this.reflashroomlight.postDelayed(this.reflashroomlightrun, 0L);
    }

    public void shakeperform(int i) {
        switch (i) {
            case 1:
                this.shakelightvalue = 0;
                this.shakeledvalue = Color.argb(255, 0, 0, 0);
                this.shakehandler.postDelayed(this.shakerun, 30L);
                break;
            case 2:
                this.shakelightvalue = 100;
                this.shakeledvalue = Color.argb(255, 255, 255, 255);
                this.shakehandler.postDelayed(this.shakerun, 30L);
                break;
        }
    }

    public static byte[] getByteToBits(byte b) {
        byte[] bArr = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bArr[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return bArr;
    }

    public byte[] getChannelStates(byte[] bArr) {
        int i = (bArr[25] & 255) + 25 + 1;
        int i2 = bArr[i] & 255;
        int i3 = (i2 / 8) + 1;
        byte[] bArr2 = new byte[i2];
        int i4 = 0;
        while (i4 < i3) {
            byte[] byteToBits = getByteToBits(bArr[i + i4 + 1]);
            int i5 = 8;
            while (true) {
                if (i5 <= 0) {
                    break;
                }
                int i6 = (i4 * 8) + (8 - i5);
                if (i6 > i2 - 1) {
                    i4 = i3;
                    break;
                }
                if ((byteToBits[i5 - 1] & 255) == 0) {
                    bArr2[i6] = 0;
                } else {
                    bArr2[i6] = 100;
                }
                i5--;
            }
            i4++;
        }
        return bArr2;
    }

    public void RunReceiveData(byte[] bArr) {
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        String str = Integer.toHexString(i) + IOUtils.LINE_SEPARATOR_UNIX;
        int i2 = bArr[17] & 255;
        int i3 = bArr[18] & 255;
        this.log.append(str);
        int i4 = 0;
        if (i == 50) {
            if (bArr[26] == -8) {
                int i5 = bArr[25] & 255;
                LightType3 lightType3 = null;
                LightType1 lightType1 = null;
                for (int i6 = 0; i6 < this.type1list.size(); i6++) {
                    if (this.type1list.get(i6).getsubid() == i2 && this.type1list.get(i6).getdevid() == i3 && this.type1list.get(i6).getchannel() == i5) {
                        lightType1 = this.type1list.get(i6);
                    }
                }
                LightType2 lightType2 = null;
                for (int i7 = 0; i7 < this.type2list.size(); i7++) {
                    if (this.type2list.get(i7).getsubid() == i2 && this.type2list.get(i7).getdevid() == i3 && this.type2list.get(i7).getchannel() == i5) {
                        lightType2 = this.type2list.get(i7);
                    }
                }
                while (i4 < this.type3list.size()) {
                    if (this.type3list.get(i4).getsubid() == i2 && this.type3list.get(i4).getdevid() == i3) {
                        lightType3 = this.type3list.get(i4);
                    }
                    i4++;
                }
                if (lightType1 != null) {
                    lightType1.setReceiveChange(bArr[27]);
                    return;
                } else if (lightType2 != null) {
                    lightType2.setReceiveChange(bArr[27]);
                    return;
                } else {
                    if (lightType3 != null) {
                        this.reflash.getlightstate((byte) i2, (byte) i3, MainActivity.mydupsocket);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        if (i != 52) {
            if (i != 61439) {
                return;
            }
            int i8 = bArr[(bArr[25] & 255) + 25 + 1] & 255;
            byte[] channelStates = getChannelStates(bArr);
            for (int i9 = 0; i9 < this.type1list.size(); i9++) {
                if (this.type1list.get(i9).getsubid() == i2 && this.type1list.get(i9).getdevid() == i3) {
                    LightType1 lightType12 = this.type1list.get(i9);
                    if (lightType12.getchannel() <= i8 && lightType12.getchannel() > 0) {
                        lightType12.setReceiveChange(channelStates[lightType12.getchannel() - 1]);
                    }
                }
            }
            while (i4 < this.type2list.size()) {
                if (this.type2list.get(i4).getsubid() == i2 && this.type2list.get(i4).getdevid() == i3) {
                    LightType2 lightType22 = this.type2list.get(i4);
                    if (lightType22.getchannel() <= i8 && lightType22.getchannel() > 0) {
                        lightType22.setReceiveChange2(channelStates[lightType22.getchannel() - 1]);
                    }
                }
                i4++;
            }
            return;
        }
        this.getcount2++;
        for (int i10 = 0; i10 < this.type1list.size(); i10++) {
            try {
                if (this.type1list.get(i10).getsubid() == i2 && this.type1list.get(i10).getdevid() == i3 && this.type1list.get(i10).getchannel() != 255) {
                    try {
                        this.type1list.get(i10).setReceiveChange(bArr[this.type1list.get(i10).getchannel() + 25]);
                    } catch (Exception unused) {
                        switch (this.type1list.get(i10).getchannel()) {
                            case 49:
                                this.type1list.get(i10).setReceiveChange(bArr[26]);
                                break;
                            case 50:
                                this.type1list.get(i10).setReceiveChange(bArr[27]);
                                break;
                            case 51:
                                this.type1list.get(i10).setReceiveChange(bArr[28]);
                                break;
                            case 52:
                                this.type1list.get(i10).setReceiveChange(bArr[29]);
                                break;
                            default:
                                Toast.makeText(getActivity(), "you had set wrong channel", 0).show();
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        for (int i11 = 0; i11 < this.type2list.size(); i11++) {
            if (this.type2list.get(i11).getsubid() == i2 && this.type2list.get(i11).getdevid() == i3 && this.type2list.get(i11).getchannel() != 255) {
                try {
                    this.type2list.get(i11).setReceiveChange(bArr[this.type2list.get(i11).getchannel() + 25]);
                } catch (Exception unused2) {
                    switch (this.type2list.get(i11).getchannel()) {
                        case 49:
                            this.type2list.get(i11).setReceiveChange(bArr[26]);
                            break;
                        case 50:
                            this.type2list.get(i11).setReceiveChange(bArr[27]);
                            break;
                        case 51:
                            this.type2list.get(i11).setReceiveChange(bArr[28]);
                            break;
                        case 52:
                            this.type2list.get(i11).setReceiveChange(bArr[29]);
                            break;
                        default:
                            Toast.makeText(getActivity(), "you had set wrong channel", 0).show();
                            break;
                    }
                }
            }
        }
        for (int i12 = 0; i12 < this.type3list.size(); i12++) {
            if (this.type3list.get(i12).getsubid() == i2 && this.type3list.get(i12).getdevid() == i3) {
                this.type3list.get(i12).setReceiveChange(new byte[]{bArr[26], bArr[27], bArr[28], bArr[29]});
            }
        }
        for (int i13 = 0; i13 < this.type4list.size(); i13++) {
            if (this.type4list.get(i13).getsubid() == i2 && this.type4list.get(i13).getdevid() == i3 && this.type4list.get(i13).getchannel() != 255) {
                try {
                    this.type4list.get(i13).setReceiveChange(bArr[this.type4list.get(i13).getchannel() + 25]);
                } catch (Exception unused3) {
                    switch (this.type4list.get(i13).getchannel()) {
                        case 49:
                            this.type4list.get(i13).setReceiveChange(bArr[26]);
                            break;
                        case 50:
                            this.type4list.get(i13).setReceiveChange(bArr[27]);
                            break;
                        case 51:
                            this.type4list.get(i13).setReceiveChange(bArr[28]);
                            break;
                        case 52:
                            this.type4list.get(i13).setReceiveChange(bArr[29]);
                            break;
                        default:
                            Toast.makeText(getActivity(), "you had set wrong channel", 0).show();
                            break;
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
        intentFilter.addAction(FounctionActivity.ACTION_DELETELIGHT);
        intentFilter.addAction(FounctionActivity.ACTION_SHAKE);
        return intentFilter;
    }
}
