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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.NioAdapter;
import com.shima.smartbushome.database.Savenio;
import com.shima.smartbushome.selflayout.NioLayout;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class NineInOne extends Fragment implements View.OnClickListener {
    NioAdapter adapter;
    AlertView addalter;
    Button button51;
    AlertView deletealter;
    EditText dev;
    NioLayout niocontrolview;
    GridView niogrid;
    RelativeLayout nonioinfo;
    AlertView pairalter;
    EditText remark;
    AlertView settingalter;
    EditText sub;
    View view;
    Handler getdatahandler = new Handler();
    List<Savenio> roomniolist = new ArrayList();
    Savenio thisniodevice = new Savenio();
    int step = 0;
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.founction_view.NineInOne.1
        @Override // java.lang.Runnable
        public void run() {
            NineInOne.this.renewdata();
            if (NineInOne.this.roomniolist.size() == 1) {
                NineInOne.this.thisniodevice = NineInOne.this.roomniolist.get(0);
                NineInOne.this.niocontrolview.setcontent(NineInOne.this.thisniodevice);
                NineInOne.this.niogrid.setVisibility(8);
                NineInOne.this.niocontrolview.setVisibility(0);
                NineInOne.this.nonioinfo.setVisibility(8);
                return;
            }
            if (NineInOne.this.roomniolist.size() > 1) {
                NineInOne.this.nonioinfo.setVisibility(8);
                NineInOne.this.niogrid.setVisibility(0);
                NineInOne.this.niocontrolview.setVisibility(8);
            } else {
                NineInOne.this.nonioinfo.setVisibility(0);
                NineInOne.this.niogrid.setVisibility(8);
                NineInOne.this.niocontrolview.setVisibility(8);
            }
        }
    };
    public AdapterView.OnItemClickListener nioitemclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.NineInOne.2
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            NineInOne.this.thisniodevice = NineInOne.this.roomniolist.get(i);
            NineInOne.this.niocontrolview.setcontent(NineInOne.this.thisniodevice);
            NineInOne.this.nonioinfo.setVisibility(8);
            NineInOne.this.niogrid.setVisibility(8);
            NineInOne.this.niocontrolview.setVisibility(0);
            NineInOne.this.step = 1;
        }
    };
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.NineInOne.5
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == NineInOne.this.addalter) {
                if (i == 0) {
                    if (NineInOne.this.remark.getText().toString().trim().length() < 1) {
                        Toast.makeText(NineInOne.this.getActivity(), "please enter a name", 0).show();
                        return;
                    }
                    MainActivity.mgr.addnio(new Savenio(FounctionActivity.roomidfc, Integer.parseInt(NineInOne.this.sub.getText().toString().trim()), Integer.parseInt(NineInOne.this.dev.getText().toString().trim()), NineInOne.this.roomniolist.size() == 0 ? 1 : 1 + NineInOne.this.roomniolist.get(NineInOne.this.roomniolist.size() - 1).nio_id, NineInOne.this.remark.getText().toString().trim(), "bt1", "bt2", "bt3", "bt4", "bt5", "bt6", "bt7", "bt8", "up", "down", "left", "right", "ok", "on", "off", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    Toast.makeText(NineInOne.this.getActivity(), "Add Succeed", 0).show();
                    NineInOne.this.getdatahandler.postDelayed(NineInOne.this.getdatarun, 30L);
                    NineInOne.this.addalter.dismiss();
                    return;
                }
                return;
            }
            if (obj == NineInOne.this.settingalter && i == 0) {
                Savenio savenio = new Savenio();
                savenio.room_id = FounctionActivity.roomidfc;
                savenio.nio_id = NineInOne.this.thisniodevice.nio_id;
                savenio.subnetID = Integer.parseInt(NineInOne.this.sub.getText().toString().trim());
                savenio.deviceID = Integer.parseInt(NineInOne.this.dev.getText().toString().trim());
                savenio.nio_remark = NineInOne.this.remark.getText().toString().trim();
                MainActivity.mgr.updateniosetting(savenio);
                NineInOne.this.renewdata();
                NineInOne.this.thisniodevice.subnetID = Integer.parseInt(NineInOne.this.sub.getText().toString().trim());
                NineInOne.this.thisniodevice.deviceID = Integer.parseInt(NineInOne.this.dev.getText().toString().trim());
                NineInOne.this.thisniodevice.nio_remark = NineInOne.this.remark.getText().toString().trim();
                NineInOne.this.niocontrolview.setcontent(NineInOne.this.thisniodevice);
            }
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.NineInOne.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    NineInOne.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                if (NineInOne.this.step == 0) {
                    NineInOne.this.getActivity().finish();
                } else if (NineInOne.this.step == 1) {
                    NineInOne.this.niogrid.setVisibility(0);
                    NineInOne.this.niocontrolview.setVisibility(8);
                    NineInOne.this.nonioinfo.setVisibility(8);
                    NineInOne.this.step = 0;
                }
            }
        }
    };
    boolean doing = false;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        getActivity().setTitle("9 in 1");
        setHasOptionsMenu(true);
        this.view = layoutInflater.inflate(R.layout.fragment_nineinone, viewGroup, false);
        this.nonioinfo = (RelativeLayout) this.view.findViewById(R.id.nonioinfo);
        this.niogrid = (GridView) this.view.findViewById(R.id.niogrid);
        this.niocontrolview = (NioLayout) this.view.findViewById(R.id.niocontrolview);
        this.button51 = (Button) this.view.findViewById(R.id.button51);
        this.button51.setOnClickListener(this);
        this.getdatahandler.postDelayed(this.getdatarun, 20L);
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
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.nio_setting_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void renewdata() {
        if (this.roomniolist.size() != 0) {
            this.roomniolist.clear();
        }
        List<Savenio> listQuerynio = MainActivity.mgr.querynio();
        for (int i = 0; i < listQuerynio.size(); i++) {
            if (listQuerynio.get(i).room_id == FounctionActivity.roomidfc) {
                this.roomniolist.add(listQuerynio.get(i));
            }
        }
        this.adapter = new NioAdapter(getActivity(), this.roomniolist);
        this.niogrid.setAdapter((ListAdapter) this.adapter);
        this.niogrid.setOnItemClickListener(this.nioitemclick);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.button51) {
            return;
        }
        this.addalter = new AlertView("Add 9in1", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
        View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
        this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
        this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
        this.remark = (EditText) viewInflate.findViewById(R.id.acremark);
        this.sub.setText(String.valueOf(0));
        this.dev.setText(String.valueOf(0));
        if (this.roomniolist.size() == 0) {
            this.remark.setText("9in1device1");
        } else {
            this.remark.setText("9in1device" + (this.roomniolist.get(this.roomniolist.size() - 1).nio_id + 1));
        }
        this.addalter.addExtView(viewInflate);
        this.addalter.setCancelable(false);
        this.addalter.setShoulddismiss(false);
        this.addalter.show();
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.add_nio) {
            if (itemId != R.id.autopair_nio) {
                if (itemId == R.id.delete_nio) {
                    if (!MainActivity.islockchangeid) {
                        String[] strArr = new String[this.roomniolist.size()];
                        for (int i = 0; i < this.roomniolist.size(); i++) {
                            strArr[i] = this.roomniolist.get(i).nio_remark;
                        }
                        this.deletealter = new AlertView("Select 9in1 to Delete", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.listdialog, (ViewGroup) null);
                        ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                        viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                        listView.setAdapter((ListAdapter) new ArrayAdapter(getActivity(), R.layout.simplelistitem, strArr));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.NineInOne.3
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                                MainActivity.mgr.deletenio("nio", NineInOne.this.roomniolist.get(i2).nio_id, FounctionActivity.roomidfc);
                                Toast.makeText(NineInOne.this.getActivity(), "Delete Succeed", 0).show();
                                NineInOne.this.getdatahandler.postDelayed(NineInOne.this.getdatarun, 30L);
                                NineInOne.this.deletealter.dismiss();
                            }
                        });
                        this.deletealter.addExtView(viewGroup);
                        this.deletealter.show();
                    }
                } else if (itemId == R.id.setting_nio && !MainActivity.islockchangeid) {
                    this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
                    this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
                    this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
                    this.remark = (EditText) viewInflate.findViewById(R.id.acremark);
                    this.sub.setText(String.valueOf(this.thisniodevice.subnetID));
                    this.dev.setText(String.valueOf(this.thisniodevice.deviceID));
                    this.remark.setText(this.thisniodevice.nio_remark);
                    this.settingalter.addExtView(viewInflate);
                    this.settingalter.show();
                }
            } else if (!MainActivity.islockchangeid) {
                this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                View viewInflate2 = getActivity().getLayoutInflater().inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                ListView listView2 = (ListView) viewInflate2.findViewById(R.id.listView4);
                listView2.setAdapter((ListAdapter) new DeviceListAdapter(getActivity(), MainActivity.netdeviceList));
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.NineInOne.4
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                        Savenio savenio = new Savenio();
                        savenio.room_id = FounctionActivity.roomidfc;
                        savenio.nio_id = NineInOne.this.thisniodevice.nio_id;
                        savenio.nio_remark = NineInOne.this.thisniodevice.nio_remark;
                        savenio.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("subnetID"));
                        savenio.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("deviceID"));
                        MainActivity.mgr.updateniosetting(savenio);
                        NineInOne.this.renewdata();
                        NineInOne.this.thisniodevice.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("subnetID"));
                        NineInOne.this.thisniodevice.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("deviceID"));
                        NineInOne.this.niocontrolview.setcontent(NineInOne.this.thisniodevice);
                        Toast.makeText(NineInOne.this.getActivity(), "apir " + MainActivity.netdeviceList.get(i2).get("devicename") + " succeed", 0).show();
                        NineInOne.this.pairalter.dismiss();
                    }
                });
                this.pairalter.addExtView(viewInflate2);
                this.pairalter.show();
            }
        } else if (!MainActivity.islockchangeid) {
            this.addalter = new AlertView("Add 9in1", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
            View viewInflate3 = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
            this.sub = (EditText) viewInflate3.findViewById(R.id.acsubedit);
            this.dev = (EditText) viewInflate3.findViewById(R.id.acdevedit);
            this.remark = (EditText) viewInflate3.findViewById(R.id.acremark);
            this.sub.setText(String.valueOf(0));
            this.dev.setText(String.valueOf(0));
            if (this.roomniolist.size() == 0) {
                this.remark.setText("9in1device1");
            } else {
                this.remark.setText("9in1device" + (this.roomniolist.get(this.roomniolist.size() - 1).nio_id + 1));
            }
            this.addalter.addExtView(viewInflate3);
            this.addalter.setCancelable(false);
            this.addalter.setShoulddismiss(false);
            this.addalter.show();
        }
        return super.onOptionsItemSelected(menuItem);
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
        if (this.doing) {
            return;
        }
        this.doing = true;
        this.niocontrolview.receivedata(bArr);
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
