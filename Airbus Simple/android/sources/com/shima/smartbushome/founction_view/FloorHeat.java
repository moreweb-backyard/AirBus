package com.shima.smartbushome.founction_view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.shima.smartbushome.assist.Adapter.FloorHeatAdapter;
import com.shima.smartbushome.database.Savefloorheat;
import com.shima.smartbushome.selflayout.FloorHeatLayout;
import com.shima.smartbushome.udp.udp_socket;
import com.shima.smartbushome.util.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FloorHeat extends Fragment {
    AlertView addAlter;
    Button bt_AddFloorHeat;
    AlertView deleteAlter;
    EditText et_ch;
    EditText et_dev;
    EditText et_remark;
    EditText et_sub;
    FloorHeatAdapter floorHeatAdapter;
    FloorHeatLayout floorHeatLayout;
    GridView gv_FloorHeatList;
    RelativeLayout rl_noFloorHeat;
    AlertView settingAlter;
    ScrollView sv_FloorHeatView;
    View view;
    List<Savefloorheat> floorHeatList = new ArrayList();
    int currPosition = 0;
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.FloorHeat.4
        /* JADX WARN: Removed duplicated region for block: B:24:0x0069 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:44:0x0123  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x0138 A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:66:0x019e A[ADDED_TO_REGION] */
        /* JADX WARN: Removed duplicated region for block: B:94:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
        @Override // com.bigkoo.alertview.OnItemClickListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onItemClick(java.lang.Object r11, int r12) throws java.lang.NumberFormatException {
            /*
                Method dump skipped, instructions count: 499
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.founction_view.FloorHeat.AnonymousClass4.onItemClick(java.lang.Object, int):void");
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.FloorHeat.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    FloorHeat.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                if (FloorHeat.this.sv_FloorHeatView.getVisibility() == 0) {
                    FloorHeat.this.rl_noFloorHeat.setVisibility(8);
                    FloorHeat.this.sv_FloorHeatView.setVisibility(8);
                    FloorHeat.this.gv_FloorHeatList.setVisibility(0);
                    FloorHeat.this.currPosition = -1;
                    return;
                }
                FloorHeat.this.getActivity().finish();
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getActivity().setTitle("Floor Heat");
        this.view = layoutInflater.inflate(R.layout.fragment_floorheat, viewGroup, false);
        setHasOptionsMenu(true);
        initView();
        refreshDataList();
        return this.view;
    }

    public void initView() {
        this.sv_FloorHeatView = (ScrollView) this.view.findViewById(R.id.floorheatview);
        this.rl_noFloorHeat = (RelativeLayout) this.view.findViewById(R.id.nofloorheatinfo);
        this.gv_FloorHeatList = (GridView) this.view.findViewById(R.id.floorheatgrid);
        this.bt_AddFloorHeat = (Button) this.view.findViewById(R.id.button46);
        this.floorHeatLayout = (FloorHeatLayout) this.view.findViewById(R.id.floorheatkongjian);
        this.gv_FloorHeatList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.FloorHeat.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                FloorHeat.this.rl_noFloorHeat.setVisibility(8);
                FloorHeat.this.sv_FloorHeatView.setVisibility(0);
                FloorHeat.this.gv_FloorHeatList.setVisibility(8);
                FloorHeat.this.floorHeatLayout.setFH(FloorHeat.this.floorHeatList.get(i));
                FloorHeat.this.currPosition = i;
            }
        });
        this.bt_AddFloorHeat.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.founction_view.FloorHeat.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MainActivity.islockchangeid) {
                    return;
                }
                FloorHeat.this.addAlter = new AlertView("Add FloorHeat", null, "CANCEL", new String[]{"SAVE"}, null, FloorHeat.this.getActivity(), AlertView.Style.Alert, FloorHeat.this.itemclick);
                View viewInflate = FloorHeat.this.getActivity().getLayoutInflater().inflate(R.layout.setting_fhinfo, (ViewGroup) null);
                FloorHeat.this.et_sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
                FloorHeat.this.et_dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
                FloorHeat.this.et_ch = (EditText) viewInflate.findViewById(R.id.et_channel);
                FloorHeat.this.et_remark = (EditText) viewInflate.findViewById(R.id.et_fhremark);
                FloorHeat.this.et_sub.setText(String.valueOf(0));
                FloorHeat.this.et_dev.setText(String.valueOf(0));
                FloorHeat.this.et_ch.setText(String.valueOf(0));
                FloorHeat.this.et_remark.setText("FloorHeat" + (FloorHeat.this.floorHeatList.size() + 1));
                FloorHeat.this.addAlter.addExtView(viewInflate);
                FloorHeat.this.addAlter.setCancelable(false);
                FloorHeat.this.addAlter.setShoulddismiss(false);
                FloorHeat.this.addAlter.show();
            }
        });
    }

    public void refreshDataList() {
        this.floorHeatList.clear();
        List<Savefloorheat> listQueryfloorheat = MainActivity.mgr.queryfloorheat();
        for (int i = 0; i < listQueryfloorheat.size(); i++) {
            if (listQueryfloorheat.get(i).room_id == FounctionActivity.roomidfc) {
                this.floorHeatList.add(listQueryfloorheat.get(i));
            }
        }
        this.floorHeatAdapter = new FloorHeatAdapter(getContext(), this.floorHeatList);
        this.gv_FloorHeatList.setAdapter((ListAdapter) this.floorHeatAdapter);
        if (this.floorHeatList.size() > 0) {
            this.rl_noFloorHeat.setVisibility(8);
            this.sv_FloorHeatView.setVisibility(8);
            this.gv_FloorHeatList.setVisibility(0);
        } else {
            this.rl_noFloorHeat.setVisibility(0);
            this.sv_FloorHeatView.setVisibility(8);
            this.gv_FloorHeatList.setVisibility(8);
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
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.floorheat_setting_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.floorheat_add /* 2131231143 */:
                if (!MainActivity.islockchangeid) {
                    this.addAlter = new AlertView("Add FloorHeat", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_fhinfo, (ViewGroup) null);
                    this.et_sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
                    this.et_dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
                    this.et_ch = (EditText) viewInflate.findViewById(R.id.et_channel);
                    this.et_remark = (EditText) viewInflate.findViewById(R.id.et_fhremark);
                    this.et_sub.setText(String.valueOf(0));
                    this.et_dev.setText(String.valueOf(0));
                    this.et_ch.setText(String.valueOf(0));
                    this.et_remark.setText("FloorHeat" + (this.floorHeatList.size() + 1));
                    this.addAlter.addExtView(viewInflate);
                    this.addAlter.setCancelable(false);
                    this.addAlter.setShoulddismiss(false);
                    this.addAlter.show();
                    break;
                }
                break;
            case R.id.floorheat_remove /* 2131231144 */:
                if (!MainActivity.islockchangeid) {
                    this.deleteAlter = new AlertView("Select FloorHeat to Delete", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.listdialog, (ViewGroup) null);
                    ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                    viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                    String[] strArr = new String[this.floorHeatList.size()];
                    for (int i = 0; i < this.floorHeatList.size(); i++) {
                        strArr[i] = this.floorHeatList.get(i).floorheat_remark;
                    }
                    listView.setAdapter((ListAdapter) new ArrayAdapter(getActivity(), R.layout.simplelistitem, strArr));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.FloorHeat.3
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                            MainActivity.mgr.deletefloorheat("floorheat", FloorHeat.this.floorHeatList.get(i2)._id, FounctionActivity.roomidfc);
                            FloorHeat.this.deleteSharedPreferences(FloorHeat.this.floorHeatList.get(i2));
                            Toast.makeText(FloorHeat.this.getActivity(), "Delete Succeed", 0).show();
                            FloorHeat.this.deleteAlter.dismiss();
                            FloorHeat.this.refreshDataList();
                        }
                    });
                    this.deleteAlter.addExtView(viewGroup);
                    this.deleteAlter.show();
                    break;
                }
                break;
            case R.id.floorheat_setting /* 2131231145 */:
                if (!MainActivity.islockchangeid) {
                    this.settingAlter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate2 = getActivity().getLayoutInflater().inflate(R.layout.setting_fhinfo, (ViewGroup) null);
                    this.et_sub = (EditText) viewInflate2.findViewById(R.id.acsubedit);
                    this.et_dev = (EditText) viewInflate2.findViewById(R.id.acdevedit);
                    this.et_ch = (EditText) viewInflate2.findViewById(R.id.et_channel);
                    this.et_remark = (EditText) viewInflate2.findViewById(R.id.et_fhremark);
                    if (this.floorHeatList.size() == 0) {
                        Toast.makeText(getContext(), "Please add a device first", 0).show();
                        break;
                    } else {
                        if (this.currPosition < 0) {
                            this.et_sub.setText("" + this.floorHeatList.get(0).subnetID);
                            this.et_dev.setText("" + this.floorHeatList.get(0).deviceID);
                            this.et_ch.setText("" + this.floorHeatList.get(0).channel);
                            this.et_remark.setText("" + this.floorHeatList.get(0).floorheat_remark);
                        } else {
                            this.et_sub.setText("" + this.floorHeatList.get(this.currPosition).subnetID);
                            this.et_dev.setText("" + this.floorHeatList.get(this.currPosition).deviceID);
                            this.et_ch.setText("" + this.floorHeatList.get(this.currPosition).channel);
                            this.et_remark.setText("" + this.floorHeatList.get(this.currPosition).floorheat_remark);
                        }
                        this.settingAlter.addExtView(viewInflate2);
                        this.settingAlter.setCancelable(false);
                        this.settingAlter.setShoulddismiss(false);
                        this.settingAlter.show();
                        break;
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void deleteSharedPreferences(Savefloorheat savefloorheat) {
        String str = "floorHeatCurTemperType" + savefloorheat._id;
        String str2 = "floorHeatZoneBeastSubID" + savefloorheat._id;
        String str3 = "floorHeatZoneBeastDevID" + savefloorheat._id;
        String str4 = "floorHeatSensorSubID" + savefloorheat._id;
        String str5 = "floorHeatSensorDevID" + savefloorheat._id;
        String str6 = "floorHeatFTSubID" + savefloorheat._id;
        String str7 = "floorHeatFTDevID" + savefloorheat._id;
        SharedPreferencesHelper.init(getContext());
        SharedPreferencesHelper.getInstance().deleteData(str);
        SharedPreferencesHelper.getInstance().deleteData(str2);
        SharedPreferencesHelper.getInstance().deleteData(str3);
        SharedPreferencesHelper.getInstance().deleteData(str4);
        SharedPreferencesHelper.getInstance().deleteData(str5);
        SharedPreferencesHelper.getInstance().deleteData(str6);
        SharedPreferencesHelper.getInstance().deleteData(str7);
    }

    public void RunReceiveData(byte[] bArr) {
        this.floorHeatLayout.setFeedBackData(bArr);
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
