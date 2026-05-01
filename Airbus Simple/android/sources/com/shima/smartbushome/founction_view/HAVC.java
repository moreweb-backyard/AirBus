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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.HvacAdapter;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.selflayout.HVACLayout;
import com.shima.smartbushome.udp.udp_socket;
import com.shima.smartbushome.util.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class HAVC extends Fragment implements View.OnClickListener {
    ACcontrol accommand;
    HvacAdapter adapter;
    AlertView addalter;
    Button addhvacbutton;
    Dialog aupairdialog;
    AlertView deletealter;
    EditText dev;
    HVACLayout hl;
    GridView hvacgrid;
    ScrollView hvacview;
    RelativeLayout nohvacinfo;
    AlertView pairalter;
    EditText remark;
    AlertView settingalter;
    EditText sub;
    AlertView switchalter;
    View view;
    Savehvac roomhvacdata = new Savehvac();
    Handler hvachandler = new Handler();
    Handler reflashhandler = new Handler();
    List<Savehvac> roomhvac = new ArrayList();
    public int POWER_ON = 0;
    public int POWER_OFF = 1;
    public int FANSPEED_AUTO = 2;
    public int FANSPEED_HIGH = 3;
    public int FANSPEED_MID = 4;
    public int FANSPEED_LOW = 5;
    public int MODE_AUTO = 6;
    public int MODE_COOL = 7;
    public int MODE_HEAT = 8;
    public int MODE_FAN = 9;
    public int TEMP = 10;
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.HAVC.3
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == HAVC.this.settingalter) {
                if (i == 0) {
                    Savehvac savehvac = new Savehvac();
                    savehvac.room_id = FounctionActivity.roomidfc;
                    savehvac.hvac_id = HAVC.this.roomhvacdata.hvac_id;
                    savehvac.subnetID = Integer.parseInt(HAVC.this.sub.getText().toString().trim());
                    savehvac.deviceID = Integer.parseInt(HAVC.this.dev.getText().toString().trim());
                    savehvac.hvac_remark = HAVC.this.remark.getText().toString().trim();
                    MainActivity.mgr.updatehvac(savehvac);
                    HAVC.this.renewdata();
                    HAVC.this.roomhvacdata.subnetID = Integer.parseInt(HAVC.this.sub.getText().toString().trim());
                    HAVC.this.roomhvacdata.deviceID = Integer.parseInt(HAVC.this.dev.getText().toString().trim());
                    HAVC.this.roomhvacdata.hvac_remark = HAVC.this.remark.getText().toString().trim();
                    HAVC.this.hl.setcontant(HAVC.this.roomhvacdata);
                    return;
                }
                return;
            }
            if (obj == HAVC.this.switchalter || obj == HAVC.this.deletealter || obj == HAVC.this.pairalter || obj != HAVC.this.addalter || i != 0) {
                return;
            }
            if (HAVC.this.remark.getText().toString().trim().length() < 1) {
                Toast.makeText(HAVC.this.getActivity(), "please enter a name", 0).show();
                return;
            }
            int i2 = HAVC.this.roomhvac.size() == 0 ? 1 : 1 + HAVC.this.roomhvac.get(HAVC.this.roomhvac.size() - 1).hvac_id;
            ArrayList arrayList = new ArrayList();
            arrayList.add(new Savehvac(FounctionActivity.roomidfc, i2, Integer.parseInt(HAVC.this.sub.getText().toString().trim()), Integer.parseInt(HAVC.this.dev.getText().toString().trim()), HAVC.this.remark.getText().toString().trim()));
            MainActivity.mgr.addhvac(arrayList);
            Toast.makeText(HAVC.this.getActivity(), "Add Succeed", 0).show();
            HAVC.this.hvachandler.postDelayed(HAVC.this.gethvactag, 30L);
            HAVC.this.addalter.dismiss();
        }
    };
    int step = 0;
    public AdapterView.OnItemClickListener hvacitemclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.HAVC.4
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            HAVC.this.roomhvacdata = HAVC.this.roomhvac.get(i);
            HAVC.this.hl.setcontant(HAVC.this.roomhvacdata);
            HAVC.this.hvacview.setVisibility(0);
            HAVC.this.hvacgrid.setVisibility(8);
            HAVC.this.nohvacinfo.setVisibility(8);
            HAVC.this.step = 1;
        }
    };
    Runnable gethvactag = new Runnable() { // from class: com.shima.smartbushome.founction_view.HAVC.5
        @Override // java.lang.Runnable
        public void run() {
            HAVC.this.renewdata();
            if (HAVC.this.roomhvac.size() == 1) {
                HAVC.this.roomhvacdata = HAVC.this.roomhvac.get(0);
                HAVC.this.hl.setcontant(HAVC.this.roomhvacdata);
                HAVC.this.hvacview.setVisibility(0);
                HAVC.this.hvacgrid.setVisibility(8);
                HAVC.this.nohvacinfo.setVisibility(8);
                return;
            }
            if (HAVC.this.roomhvac.size() > 1) {
                HAVC.this.hvacview.setVisibility(8);
                HAVC.this.hvacgrid.setVisibility(0);
                HAVC.this.nohvacinfo.setVisibility(8);
            } else {
                HAVC.this.hvacview.setVisibility(8);
                HAVC.this.hvacgrid.setVisibility(8);
                HAVC.this.nohvacinfo.setVisibility(0);
            }
        }
    };
    Runnable HVACReflashRun = new Runnable() { // from class: com.shima.smartbushome.founction_view.HAVC.6
        @Override // java.lang.Runnable
        public void run() {
            HAVC.this.hl.ReflashUI();
        }
    };
    int shakecount = 0;
    Handler shakehandler = new Handler();
    int shakehvacvalue = 999;
    Runnable shakerun = new Runnable() { // from class: com.shima.smartbushome.founction_view.HAVC.7
        @Override // java.lang.Runnable
        public void run() {
            if (HAVC.this.shakecount >= HAVC.this.roomhvac.size()) {
                HAVC.this.shakecount = 0;
                HAVC.this.shakehvacvalue = 999;
                HAVC.this.shakehandler.removeCallbacks(HAVC.this.shakerun);
            } else {
                HAVC.this.accommand.ACControl((byte) HAVC.this.roomhvac.get(HAVC.this.shakecount).subnetID, (byte) HAVC.this.roomhvac.get(HAVC.this.shakecount).deviceID, 3, HAVC.this.shakehvacvalue, MainActivity.mydupsocket);
                HAVC.this.shakecount++;
                HAVC.this.shakehandler.postDelayed(HAVC.this.shakerun, 80L);
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.HAVC.8
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    HAVC.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                if (HAVC.this.step == 0) {
                    HAVC.this.getActivity().finish();
                    return;
                } else {
                    if (HAVC.this.step == 1) {
                        HAVC.this.hvacview.setVisibility(8);
                        HAVC.this.hvacgrid.setVisibility(0);
                        HAVC.this.nohvacinfo.setVisibility(8);
                        HAVC.this.step = 0;
                        return;
                    }
                    return;
                }
            }
            if (!FounctionActivity.ACTION_SHAKE.equals(action) || MainActivity.islockshake) {
                return;
            }
            HAVC.this.shakeperform(intent.getIntExtra("shake_type", 0));
        }
    };
    boolean doing = false;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getActivity().setTitle("HVAC");
        this.view = layoutInflater.inflate(R.layout.fragment_havc, viewGroup, false);
        this.hl = (HVACLayout) this.view.findViewById(R.id.hvackongjian);
        this.hvacgrid = (GridView) this.view.findViewById(R.id.hvacgrid);
        this.nohvacinfo = (RelativeLayout) this.view.findViewById(R.id.nohvacinfo);
        this.hvacview = (ScrollView) this.view.findViewById(R.id.hvacview);
        this.addhvacbutton = (Button) this.view.findViewById(R.id.button46);
        this.addhvacbutton.setOnClickListener(this);
        setHasOptionsMenu(true);
        this.accommand = new ACcontrol();
        this.aupairdialog = new Dialog(getActivity());
        this.hvachandler.postDelayed(this.gethvactag, 30L);
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
        this.hl.removetimer();
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.hvac_setting_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.button46) {
            return;
        }
        this.addalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
        View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
        this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
        this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
        this.remark = (EditText) viewInflate.findViewById(R.id.acremark);
        this.sub.setText(String.valueOf(0));
        this.dev.setText(String.valueOf(0));
        if (this.roomhvac.size() == 0) {
            this.remark.setText("HV1");
        } else {
            this.remark.setText("HV" + (this.roomhvac.get(this.roomhvac.size() - 1).hvac_id + 1));
        }
        this.addalter.addExtView(viewInflate);
        this.addalter.setCancelable(false);
        this.addalter.setShoulddismiss(false);
        this.addalter.show();
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        String[] strArr = new String[this.roomhvac.size()];
        for (int i = 0; i < this.roomhvac.size(); i++) {
            strArr[i] = this.roomhvac.get(i).hvac_remark;
        }
        switch (menuItem.getItemId()) {
            case R.id.hvac_add /* 2131231174 */:
                if (!MainActivity.islockchangeid) {
                    this.addalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
                    this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
                    this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
                    this.remark = (EditText) viewInflate.findViewById(R.id.acremark);
                    this.sub.setText(String.valueOf(0));
                    this.dev.setText(String.valueOf(0));
                    if (this.roomhvac.size() == 0) {
                        this.remark.setText("HV1");
                    } else {
                        this.remark.setText("HV" + (this.roomhvac.get(this.roomhvac.size() - 1).hvac_id + 1));
                    }
                    this.addalter.addExtView(viewInflate);
                    this.addalter.setCancelable(false);
                    this.addalter.setShoulddismiss(false);
                    this.addalter.show();
                    break;
                }
                break;
            case R.id.hvac_delete /* 2131231175 */:
                if (!MainActivity.islockchangeid) {
                    this.deletealter = new AlertView("Select AC to Delete", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.listdialog, (ViewGroup) null);
                    ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                    viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                    listView.setAdapter((ListAdapter) new ArrayAdapter(getActivity(), R.layout.simplelistitem, strArr));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.HAVC.1
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                            MainActivity.mgr.deletehvac("hvac", HAVC.this.roomhvac.get(i2).hvac_id, FounctionActivity.roomidfc);
                            HAVC.this.deleteSharedPreferences(HAVC.this.roomhvac.get(i2));
                            Toast.makeText(HAVC.this.getActivity(), "Delete Succeed", 0).show();
                            HAVC.this.hvachandler.postDelayed(HAVC.this.gethvactag, 30L);
                            HAVC.this.deletealter.dismiss();
                        }
                    });
                    this.deletealter.addExtView(viewGroup);
                    this.deletealter.show();
                    break;
                }
                break;
            case R.id.hvacpair /* 2131231179 */:
                if (!MainActivity.islockchangeid) {
                    this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate2 = getActivity().getLayoutInflater().inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView2 = (ListView) viewInflate2.findViewById(R.id.listView4);
                    listView2.setAdapter((ListAdapter) new DeviceListAdapter(getActivity(), MainActivity.netdeviceList));
                    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.HAVC.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                            Savehvac savehvac = new Savehvac();
                            savehvac.room_id = FounctionActivity.roomidfc;
                            savehvac.hvac_id = HAVC.this.roomhvacdata.hvac_id;
                            savehvac.hvac_remark = HAVC.this.roomhvacdata.hvac_remark;
                            savehvac.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("subnetID"));
                            savehvac.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("deviceID"));
                            MainActivity.mgr.updatehvac(savehvac);
                            HAVC.this.renewdata();
                            HAVC.this.roomhvacdata.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("subnetID"));
                            HAVC.this.roomhvacdata.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("deviceID"));
                            HAVC.this.hl.setcontant(HAVC.this.roomhvacdata);
                            Toast.makeText(HAVC.this.getActivity(), "apir " + MainActivity.netdeviceList.get(i2).get("devicename") + " succeed", 0).show();
                            HAVC.this.pairalter.dismiss();
                        }
                    });
                    this.pairalter.addExtView(viewInflate2);
                    this.pairalter.show();
                    break;
                }
                break;
            case R.id.hvacsetting /* 2131231182 */:
                if (!MainActivity.islockchangeid) {
                    this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate3 = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
                    this.sub = (EditText) viewInflate3.findViewById(R.id.acsubedit);
                    this.dev = (EditText) viewInflate3.findViewById(R.id.acdevedit);
                    this.remark = (EditText) viewInflate3.findViewById(R.id.acremark);
                    this.sub.setText(String.valueOf(this.roomhvacdata.subnetID));
                    this.dev.setText(String.valueOf(this.roomhvacdata.deviceID));
                    this.remark.setText(this.roomhvacdata.hvac_remark);
                    this.settingalter.addExtView(viewInflate3);
                    this.settingalter.show();
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void deleteSharedPreferences(Savehvac savehvac) {
        String str = "CurTemperType" + savehvac._id;
        String str2 = "ZoneBeastSubID" + savehvac._id;
        String str3 = "ZoneBeastDevID" + savehvac._id;
        String str4 = "SensorSubID" + savehvac._id;
        String str5 = "SensorDevID" + savehvac._id;
        String str6 = "FTSubID" + savehvac._id;
        String str7 = "FTDevID" + savehvac._id;
        SharedPreferencesHelper.init(getContext());
        SharedPreferencesHelper.getInstance().deleteData(str);
        SharedPreferencesHelper.getInstance().deleteData(str2);
        SharedPreferencesHelper.getInstance().deleteData(str3);
        SharedPreferencesHelper.getInstance().deleteData(str4);
        SharedPreferencesHelper.getInstance().deleteData(str5);
        SharedPreferencesHelper.getInstance().deleteData(str6);
        SharedPreferencesHelper.getInstance().deleteData(str7);
    }

    public void renewdata() {
        if (this.roomhvac.size() != 0) {
            this.roomhvac.clear();
        }
        List<Savehvac> listQueryhvac = MainActivity.mgr.queryhvac();
        for (int i = 0; i < listQueryhvac.size(); i++) {
            if (listQueryhvac.get(i).room_id == FounctionActivity.roomidfc) {
                this.roomhvac.add(listQueryhvac.get(i));
            }
        }
        this.adapter = new HvacAdapter(getContext(), this.roomhvac);
        this.hvacgrid.setAdapter((ListAdapter) this.adapter);
        this.hvacgrid.setOnItemClickListener(this.hvacitemclick);
    }

    public void shakeperform(int i) {
        switch (i) {
            case 1:
                this.shakehvacvalue = i - 1;
                this.shakehandler.postDelayed(this.shakerun, 30L);
                break;
            case 2:
                this.shakehvacvalue = i - 1;
                this.shakehandler.postDelayed(this.shakerun, 30L);
                break;
        }
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
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (this.doing) {
            return;
        }
        this.doing = true;
        this.hl.setCurTemperByData(bArr);
        if ((bArr[17] & 255) == this.hl.getsubnetid() && (bArr[18] & 255) == this.hl.getdeviceid()) {
            if (i == 6401) {
                this.hl.setACTempRange(new byte[]{bArr[25], bArr[26], bArr[27], bArr[28], bArr[29], bArr[30]});
            } else if (i == 6459) {
                try {
                    if (bArr.length != 42 || this.hl.getdeviceid() == (bArr[39] & 255)) {
                        this.hl.setACCurrentStateBy193B(bArr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (i == 57581) {
                try {
                    this.hl.setACCurrentState(bArr);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if (i == 57633) {
                this.hl.setCandF(bArr[25]);
            } else if (i == 57637) {
                this.hl.setFanSpeedAndModeCount(bArr);
            } else if (i == 58329) {
                switch (bArr[25]) {
                    case 3:
                        if (bArr[26] == 0) {
                            this.hl.setReceiveChange(this.POWER_OFF, bArr[26]);
                        }
                        if (bArr[26] == 1) {
                            this.hl.setReceiveChange(this.POWER_ON, bArr[26]);
                            break;
                        }
                        break;
                    case 4:
                        this.hl.setReceiveChange(this.TEMP, bArr[26]);
                        break;
                    case 5:
                        switch (bArr[26]) {
                            case 0:
                                this.hl.setReceiveChange(this.FANSPEED_AUTO, bArr[26]);
                                break;
                            case 1:
                                this.hl.setReceiveChange(this.FANSPEED_HIGH, bArr[26]);
                                break;
                            case 2:
                                this.hl.setReceiveChange(this.FANSPEED_MID, bArr[26]);
                                break;
                            case 3:
                                this.hl.setReceiveChange(this.FANSPEED_LOW, bArr[26]);
                                break;
                        }
                    case 6:
                        switch (bArr[26]) {
                            case 0:
                                this.hl.setReceiveChange(this.MODE_COOL, bArr[26]);
                                break;
                            case 1:
                                this.hl.setReceiveChange(this.MODE_HEAT, bArr[26]);
                                break;
                            case 2:
                                this.hl.setReceiveChange(this.MODE_FAN, bArr[26]);
                                break;
                            case 3:
                                this.hl.setReceiveChange(this.MODE_AUTO, bArr[26]);
                                break;
                        }
                    case 7:
                        this.hl.setReceiveChange(this.TEMP, bArr[26]);
                        break;
                    case 8:
                        this.hl.setReceiveChange(this.TEMP, bArr[26]);
                        break;
                }
            }
        }
        this.doing = false;
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_SHAKE);
        return intentFilter;
    }
}
