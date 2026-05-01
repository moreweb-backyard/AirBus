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
import com.shima.smartbushome.assist.Adapter.MediaAdapter;
import com.shima.smartbushome.database.Savemedia;
import com.shima.smartbushome.database.Savemediabutton;
import com.shima.smartbushome.selflayout.MediaType1;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Media extends Fragment implements View.OnClickListener {
    MediaAdapter adapter;
    AlertView addalter;
    Button button45;
    AlertView deletealter;
    EditText dev;
    MediaType1 mediacontrolview;
    GridView mediagrid;
    RelativeLayout nomediainfo;
    AlertView pairalter;
    EditText remark;
    AlertView settingalter;
    EditText sub;
    View view;
    Handler getdatahandler = new Handler();
    int step = 0;
    List<Savemedia> roommedia = new ArrayList();
    Savemedia roommediadata = new Savemedia();
    String defaultmediaicon = "media_icon1";
    Runnable getdatarun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Media.3
        @Override // java.lang.Runnable
        public void run() {
            Media.this.renewdata();
            if (Media.this.roommedia.size() == 1) {
                Media.this.roommediadata = Media.this.roommedia.get(0);
                Media.this.mediacontrolview.setcontent(Media.this.roommediadata);
                Media.this.mediagrid.setVisibility(8);
                Media.this.mediacontrolview.setVisibility(0);
                Media.this.nomediainfo.setVisibility(8);
                return;
            }
            if (Media.this.roommedia.size() > 1) {
                Media.this.nomediainfo.setVisibility(8);
                Media.this.mediagrid.setVisibility(0);
                Media.this.mediacontrolview.setVisibility(8);
            } else {
                Media.this.nomediainfo.setVisibility(0);
                Media.this.mediagrid.setVisibility(8);
                Media.this.mediacontrolview.setVisibility(8);
            }
        }
    };
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Media.4
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == Media.this.settingalter) {
                if (i == 0) {
                    Savemedia savemedia = new Savemedia();
                    savemedia.room_id = FounctionActivity.roomidfc;
                    savemedia.media_id = Media.this.roommediadata.media_id;
                    savemedia.subnetID = Integer.parseInt(Media.this.sub.getText().toString().trim());
                    savemedia.deviceID = Integer.parseInt(Media.this.dev.getText().toString().trim());
                    savemedia.media_statement = Media.this.remark.getText().toString().trim();
                    savemedia.media_icon = Media.this.defaultmediaicon;
                    MainActivity.mgr.updatemedia(savemedia);
                    Media.this.renewdata();
                    Media.this.roommediadata.subnetID = Integer.parseInt(Media.this.sub.getText().toString().trim());
                    Media.this.roommediadata.deviceID = Integer.parseInt(Media.this.dev.getText().toString().trim());
                    Media.this.roommediadata.media_statement = Media.this.remark.getText().toString().trim();
                    Media.this.mediacontrolview.setcontent(Media.this.roommediadata);
                    return;
                }
                return;
            }
            if (obj == Media.this.addalter && i == 0) {
                if (Media.this.remark.getText().toString().trim().length() < 1) {
                    Toast.makeText(Media.this.getActivity(), "please enter a name", 0).show();
                    return;
                }
                int i2 = Media.this.roommedia.size() != 0 ? 1 + Media.this.roommedia.get(Media.this.roommedia.size() - 1).media_id : 1;
                MainActivity.mgr.addmedia(new Savemedia(FounctionActivity.roomidfc, Integer.parseInt(Media.this.sub.getText().toString().trim()), Integer.parseInt(Media.this.dev.getText().toString().trim()), i2, Media.this.remark.getText().toString().trim(), Media.this.defaultmediaicon));
                ArrayList arrayList = new ArrayList();
                for (int i3 = 0; i3 < 30; i3++) {
                    arrayList.add(new Savemediabutton(FounctionActivity.roomidfc, i2, i3, 0, 255, 1, 0));
                }
                MainActivity.mgr.addmediabutton(arrayList);
                Toast.makeText(Media.this.getActivity(), "Add Succeed", 0).show();
                Media.this.getdatahandler.postDelayed(Media.this.getdatarun, 30L);
                Media.this.addalter.dismiss();
            }
        }
    };
    public AdapterView.OnItemClickListener mediaitemclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Media.5
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            Media.this.roommediadata = Media.this.roommedia.get(i);
            Media.this.mediacontrolview.setcontent(Media.this.roommediadata);
            Media.this.nomediainfo.setVisibility(8);
            Media.this.mediagrid.setVisibility(8);
            Media.this.mediacontrolview.setVisibility(0);
            Media.this.step = 1;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Media.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                if (Media.this.step == 0) {
                    Media.this.getActivity().finish();
                    return;
                } else {
                    if (Media.this.step == 1) {
                        Media.this.mediagrid.setVisibility(0);
                        Media.this.mediacontrolview.setVisibility(8);
                        Media.this.nomediainfo.setVisibility(8);
                        Media.this.step = 0;
                        return;
                    }
                    return;
                }
            }
            if (FounctionActivity.ACTION_SHAKE.equals(action)) {
                intent.getIntExtra("shake_type", 0);
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_media, viewGroup, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Media");
        this.nomediainfo = (RelativeLayout) this.view.findViewById(R.id.nomediainfo);
        this.mediagrid = (GridView) this.view.findViewById(R.id.mediagrid);
        this.mediacontrolview = (MediaType1) this.view.findViewById(R.id.mediacontrolview);
        this.button45 = (Button) this.view.findViewById(R.id.button45);
        this.button45.setOnClickListener(this);
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
        menuInflater.inflate(R.menu.media_setting_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.button45) {
            return;
        }
        this.addalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
        View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
        this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
        this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
        this.remark = (EditText) viewInflate.findViewById(R.id.acremark);
        this.sub.setText(String.valueOf(0));
        this.dev.setText(String.valueOf(0));
        if (this.roommedia.size() == 0) {
            this.remark.setText("Media1");
        } else {
            this.remark.setText("Media" + (this.roommedia.get(this.roommedia.size() - 1).media_id + 1));
        }
        this.addalter.addExtView(viewInflate);
        this.addalter.setCancelable(false);
        this.addalter.setShoulddismiss(false);
        this.addalter.show();
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.media_add /* 2131231380 */:
                if (!MainActivity.islockchangeid) {
                    this.addalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
                    this.sub = (EditText) viewInflate.findViewById(R.id.acsubedit);
                    this.dev = (EditText) viewInflate.findViewById(R.id.acdevedit);
                    this.remark = (EditText) viewInflate.findViewById(R.id.acremark);
                    this.sub.setText(String.valueOf(0));
                    this.dev.setText(String.valueOf(0));
                    if (this.roommedia.size() == 0) {
                        this.remark.setText("Media1");
                    } else {
                        this.remark.setText("Media" + (this.roommedia.get(this.roommedia.size() - 1).media_id + 1));
                    }
                    this.addalter.addExtView(viewInflate);
                    this.addalter.setCancelable(false);
                    this.addalter.setShoulddismiss(false);
                    this.addalter.show();
                    break;
                }
                break;
            case R.id.media_delete /* 2131231381 */:
                if (!MainActivity.islockchangeid) {
                    String[] strArr = new String[this.roommedia.size()];
                    for (int i = 0; i < this.roommedia.size(); i++) {
                        strArr[i] = this.roommedia.get(i).media_statement;
                    }
                    this.deletealter = new AlertView("Select Media to Delete", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.listdialog, (ViewGroup) null);
                    ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                    viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                    listView.setAdapter((ListAdapter) new ArrayAdapter(getActivity(), R.layout.simplelistitem, strArr));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Media.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                            MainActivity.mgr.deletemedia("media", Media.this.roommedia.get(i2).media_id, FounctionActivity.roomidfc);
                            MainActivity.mgr.deletemediabutton("mediabutton", Media.this.roommedia.get(i2).media_id, FounctionActivity.roomidfc);
                            Toast.makeText(Media.this.getActivity(), "Delete Succeed", 0).show();
                            Media.this.getdatahandler.postDelayed(Media.this.getdatarun, 30L);
                            Media.this.deletealter.dismiss();
                        }
                    });
                    this.deletealter.addExtView(viewGroup);
                    this.deletealter.show();
                    break;
                }
                break;
            case R.id.mediapair /* 2131231399 */:
                if (!MainActivity.islockchangeid) {
                    this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate2 = getActivity().getLayoutInflater().inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView2 = (ListView) viewInflate2.findViewById(R.id.listView4);
                    listView2.setAdapter((ListAdapter) new DeviceListAdapter(getActivity(), MainActivity.netdeviceList));
                    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Media.1
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                            Savemedia savemedia = new Savemedia();
                            savemedia.room_id = FounctionActivity.roomidfc;
                            savemedia.media_id = Media.this.roommediadata.media_id;
                            savemedia.media_statement = Media.this.roommediadata.media_statement;
                            savemedia.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("subnetID"));
                            savemedia.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("deviceID"));
                            MainActivity.mgr.updatemedia(savemedia);
                            Media.this.renewdata();
                            Media.this.roommediadata.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("subnetID"));
                            Media.this.roommediadata.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i2).get("deviceID"));
                            Media.this.mediacontrolview.setcontent(Media.this.roommediadata);
                            Toast.makeText(Media.this.getActivity(), "apir " + MainActivity.netdeviceList.get(i2).get("devicename") + " succeed", 0).show();
                            Media.this.pairalter.dismiss();
                        }
                    });
                    this.pairalter.addExtView(viewInflate2);
                    this.pairalter.show();
                    break;
                }
                break;
            case R.id.mediasetting /* 2131231400 */:
                if (!MainActivity.islockchangeid) {
                    this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate3 = getActivity().getLayoutInflater().inflate(R.layout.setting_acinfo, (ViewGroup) null);
                    this.sub = (EditText) viewInflate3.findViewById(R.id.acsubedit);
                    this.dev = (EditText) viewInflate3.findViewById(R.id.acdevedit);
                    this.remark = (EditText) viewInflate3.findViewById(R.id.acremark);
                    this.sub.setText(String.valueOf(this.roommediadata.subnetID));
                    this.dev.setText(String.valueOf(this.roommediadata.deviceID));
                    this.remark.setText(this.roommediadata.media_statement);
                    this.settingalter.addExtView(viewInflate3);
                    this.settingalter.show();
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void renewdata() {
        if (this.roommedia.size() != 0) {
            this.roommedia.clear();
        }
        List<Savemedia> listQuerymedia = MainActivity.mgr.querymedia();
        for (int i = 0; i < listQuerymedia.size(); i++) {
            if (listQuerymedia.get(i).room_id == FounctionActivity.roomidfc) {
                this.roommedia.add(listQuerymedia.get(i));
            }
        }
        this.adapter = new MediaAdapter(getActivity(), this.roommedia);
        this.mediagrid.setAdapter((ListAdapter) this.adapter);
        this.mediagrid.setOnItemClickListener(this.mediaitemclick);
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_SHAKE);
        return intentFilter;
    }
}
