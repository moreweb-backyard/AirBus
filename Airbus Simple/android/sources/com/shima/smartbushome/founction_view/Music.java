package com.shima.smartbushome.founction_view;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActivityChooserView;
import android.view.GestureDetector;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.MusicAdapter;
import com.shima.smartbushome.database.Savemusic;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.selflayout.AudioInLayout;
import com.shima.smartbushome.selflayout.MusicLayout;
import com.shima.smartbushome.selflayout.RadioLayout;
import com.shima.smartbushome.udp.udp_socket;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Music extends Fragment implements View.OnClickListener {
    public static Activity getact;
    public static int musicchoosestate;
    EditText add_dev;
    EditText add_remark;
    EditText add_sub;
    AlertView addmusicalter;
    Button addmusicbutton;
    AudioInLayout ail;
    Dialog aupairdialog;
    AlertView deleteAlter;
    private GestureDetector detector;
    EditText dev;
    GridView gv_Musics;
    RelativeLayout havemusic;
    MusicLayout ml;
    MusicAdapter musicAdapter;
    RelativeLayout nomusic;
    AlertView pairalter;
    RadioButton rb_audioin;
    RadioButton rb_fm;
    RadioButton rb_music;
    EditText remark;
    RadioLayout rl;
    RadioGroup rp;
    AlertView settingalter;
    EditText sub;
    View view;
    private ViewFlipper viewFlipper;
    RelativeLayout zlayout;
    Savemusic thismusic = new Savemusic();
    Handler getdata = new Handler();
    musiccontrol zaudiocontrol = new musiccontrol();
    Runnable getmusicidrun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Music.1
        @Override // java.lang.Runnable
        public void run() {
            List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
            final ArrayList arrayList = new ArrayList();
            for (int i = 0; i < listQuerymusic.size(); i++) {
                if (listQuerymusic.get(i).room_id == FounctionActivity.roomidfc) {
                    arrayList.add(listQuerymusic.get(i));
                }
            }
            if (arrayList.size() == 1) {
                Music.this.havemusic.setVisibility(0);
                Music.this.nomusic.setVisibility(8);
                Music.this.gv_Musics.setVisibility(8);
                Music.this.thismusic = (Savemusic) arrayList.get(0);
                Music.this.ml.setcontent(Music.this.thismusic);
                Music.this.rl.setcontent(Music.this.thismusic);
                Music.this.ail.setcontent(Music.this.thismusic);
                if (Music.this.rb_music.isChecked()) {
                    Music.this.ml.setVisibility(0);
                    Music.this.ail.setVisibility(8);
                    Music.this.rl.setVisibility(8);
                    Music.this.ml.SwitchtoMusicSD();
                    return;
                }
                return;
            }
            if (arrayList.size() > 1) {
                Music.this.rp.setVisibility(8);
                Music.this.havemusic.setVisibility(0);
                Music.this.nomusic.setVisibility(8);
                Music.this.ml.setVisibility(8);
                Music.this.ail.setVisibility(8);
                Music.this.rl.setVisibility(8);
                Music.this.gv_Musics.setVisibility(0);
                Music.this.musicAdapter = new MusicAdapter(Music.this.getContext(), arrayList);
                Music.this.gv_Musics.setAdapter((ListAdapter) Music.this.musicAdapter);
                Music.this.gv_Musics.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Music.1.1
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                        Music.this.rp.setVisibility(0);
                        Music.this.havemusic.setVisibility(0);
                        Music.this.nomusic.setVisibility(8);
                        Music.this.gv_Musics.setVisibility(8);
                        Music.this.thismusic = (Savemusic) arrayList.get(i2);
                        Music.this.ml.setcontent(Music.this.thismusic);
                        Music.this.rl.setcontent(Music.this.thismusic);
                        Music.this.ail.setcontent(Music.this.thismusic);
                        if (Music.this.rb_music.isChecked()) {
                            Music.this.ml.setVisibility(0);
                            Music.this.ail.setVisibility(8);
                            Music.this.rl.setVisibility(8);
                            Music.this.ml.SwitchtoMusicSD();
                        }
                    }
                });
                return;
            }
            Music.this.havemusic.setVisibility(8);
            Music.this.nomusic.setVisibility(0);
            Music.this.gv_Musics.setVisibility(8);
        }
    };
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Music.4
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == Music.this.settingalter) {
                if (i == 0) {
                    Savemusic savemusic = new Savemusic();
                    savemusic.room_id = FounctionActivity.roomidfc;
                    savemusic.music_id = Music.this.thismusic.music_id;
                    savemusic.subnetID = Integer.parseInt(Music.this.sub.getText().toString().trim());
                    savemusic.deviceID = Integer.parseInt(Music.this.dev.getText().toString().trim());
                    savemusic.music_remark = Music.this.remark.getText().toString();
                    MainActivity.mgr.updatemusic(savemusic);
                    Music.this.getdata.postDelayed(Music.this.getmusicidrun, 20L);
                    return;
                }
                return;
            }
            if (obj == Music.this.addmusicalter && i == 0) {
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < listQuerymusic.size(); i2++) {
                    if (listQuerymusic.get(i2).room_id == FounctionActivity.roomidfc) {
                        arrayList.add(listQuerymusic.get(i2));
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                Savemusic savemusic2 = new Savemusic();
                savemusic2.room_id = FounctionActivity.roomidfc;
                savemusic2.music_id = arrayList.size() + 1;
                savemusic2.subnetID = Integer.parseInt(Music.this.add_sub.getText().toString().trim());
                savemusic2.deviceID = Integer.parseInt(Music.this.add_dev.getText().toString().trim());
                savemusic2.music_remark = Music.this.add_remark.getText().toString();
                arrayList2.add(savemusic2);
                MainActivity.mgr.addmusic(arrayList2);
                Music.this.getdata.postDelayed(Music.this.getmusicidrun, 20L);
            }
        }
    };
    public RadioGroup.OnCheckedChangeListener selectchange = new RadioGroup.OnCheckedChangeListener() { // from class: com.shima.smartbushome.founction_view.Music.5
        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.audio) {
                Music.musicchoosestate = 2;
                Music.this.ml.setVisibility(8);
                Music.this.ail.setVisibility(0);
                Music.this.rl.setVisibility(8);
                if (!Music.this.ail.ifinitstarted()) {
                    Music.this.ail.initview(Music.this.getActivity());
                }
                Music.this.ail.SwitchtoAudioIn();
                return;
            }
            if (checkedRadioButtonId == R.id.music) {
                Music.musicchoosestate = 0;
                Music.this.ml.setVisibility(0);
                Music.this.ail.setVisibility(8);
                Music.this.rl.setVisibility(8);
                Music.this.ml.SwitchtoMusicSD();
                return;
            }
            if (checkedRadioButtonId != R.id.radio) {
                return;
            }
            Music.musicchoosestate = 1;
            Music.this.ml.setVisibility(8);
            Music.this.ail.setVisibility(8);
            Music.this.rl.setVisibility(0);
            if (!Music.this.rl.ifinitstarted()) {
                Music.this.rl.initview(Music.this.getActivity());
            }
            Music.this.rl.SwitchtoRadio();
        }
    };
    int playorpause = 0;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Music.6
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    Music.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                Music.this.onbackpress();
            } else {
                if (!FounctionActivity.ACTION_SHAKE.equals(action) || MainActivity.islockshake) {
                    return;
                }
                Music.this.shakeperform(intent.getIntExtra("shake_type", 0));
            }
        }
    };

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_music, viewGroup, false);
        this.zlayout = (RelativeLayout) this.view.findViewById(R.id.zaudiolayout);
        this.havemusic = (RelativeLayout) this.view.findViewById(R.id.musiclayoutcontent);
        this.nomusic = (RelativeLayout) this.view.findViewById(R.id.nomusic);
        this.addmusicbutton = (Button) this.view.findViewById(R.id.button3);
        this.addmusicbutton.setOnClickListener(this);
        this.ml = (MusicLayout) this.view.findViewById(R.id.view4);
        this.ail = (AudioInLayout) this.view.findViewById(R.id.view6);
        this.rl = (RadioLayout) this.view.findViewById(R.id.view8);
        this.rp = (RadioGroup) this.view.findViewById(R.id.radioGroup);
        this.rp.setOnCheckedChangeListener(this.selectchange);
        this.rb_music = (RadioButton) this.view.findViewById(R.id.music);
        this.rb_fm = (RadioButton) this.view.findViewById(R.id.radio);
        this.rb_audioin = (RadioButton) this.view.findViewById(R.id.audio);
        this.gv_Musics = (GridView) this.view.findViewById(R.id.gv_Musics);
        this.aupairdialog = new Dialog(getActivity());
        getact = getActivity();
        getActivity().setTitle("Music");
        setHasOptionsMenu(true);
        this.getdata.postDelayed(this.getmusicidrun, 20L);
        return this.view;
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
        this.ml.removetimer();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.button3) {
            return;
        }
        this.addmusicalter = new AlertView("Add", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
        View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_musicinfo, (ViewGroup) null);
        this.add_sub = (EditText) viewInflate.findViewById(R.id.music_subedit);
        this.add_dev = (EditText) viewInflate.findViewById(R.id.music_devedit);
        this.add_remark = (EditText) viewInflate.findViewById(R.id.et_music_remark);
        this.add_sub.setText("0");
        this.add_dev.setText("0");
        this.add_remark.setText("");
        this.addmusicalter.addExtView(viewInflate);
        this.addmusicalter.show();
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.music_setting, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.music_add /* 2131231456 */:
                if (!MainActivity.islockchangeid) {
                    this.addmusicalter = new AlertView("Add", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.setting_musicinfo, (ViewGroup) null);
                    this.add_sub = (EditText) viewInflate.findViewById(R.id.music_subedit);
                    this.add_dev = (EditText) viewInflate.findViewById(R.id.music_devedit);
                    this.add_remark = (EditText) viewInflate.findViewById(R.id.et_music_remark);
                    this.add_sub.setText("0");
                    this.add_dev.setText("0");
                    this.add_remark.setText("");
                    this.addmusicalter.addExtView(viewInflate);
                    this.addmusicalter.show();
                    break;
                }
                break;
            case R.id.music_delete /* 2131231457 */:
                if (!MainActivity.islockchangeid) {
                    List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < listQuerymusic.size(); i++) {
                        if (listQuerymusic.get(i).room_id == FounctionActivity.roomidfc) {
                            arrayList.add(listQuerymusic.get(i));
                        }
                    }
                    this.deleteAlter = new AlertView("Select Music to Delete", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.listdialog, (ViewGroup) null);
                    ListView listView = (ListView) viewGroup.findViewById(R.id.listView6);
                    viewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, 400));
                    String[] strArr = new String[arrayList.size()];
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        if (((Savemusic) arrayList.get(i2)).music_remark.trim().equals("")) {
                            strArr[i2] = ((Savemusic) arrayList.get(i2)).subnetID + "_" + ((Savemusic) arrayList.get(i2)).deviceID;
                        } else {
                            strArr[i2] = ((Savemusic) arrayList.get(i2)).music_remark;
                        }
                    }
                    listView.setAdapter((ListAdapter) new ArrayAdapter(getActivity(), R.layout.simplelistitem, strArr));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Music.3
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j) {
                            MainActivity.mgr.deletemusic("music", ((Savemusic) arrayList.get(i3)).music_id, FounctionActivity.roomidfc);
                            MainActivity.mgr.deletesong("song", FounctionActivity.roomidfc, ((Savemusic) arrayList.get(i3)).music_id);
                            MainActivity.mgr.deleteradio("radio", FounctionActivity.roomidfc);
                            Toast.makeText(Music.this.getActivity(), "Delete Succeed", 0).show();
                            Music.this.deleteAlter.dismiss();
                            List<Savemusic> listQuerymusic2 = MainActivity.mgr.querymusic();
                            final ArrayList arrayList2 = new ArrayList();
                            for (int i4 = 0; i4 < listQuerymusic2.size(); i4++) {
                                if (listQuerymusic2.get(i4).room_id == FounctionActivity.roomidfc) {
                                    arrayList2.add(listQuerymusic2.get(i4));
                                }
                            }
                            Music.this.rp.setVisibility(8);
                            Music.this.havemusic.setVisibility(0);
                            Music.this.nomusic.setVisibility(8);
                            Music.this.ml.setVisibility(8);
                            Music.this.ail.setVisibility(8);
                            Music.this.rl.setVisibility(8);
                            Music.this.gv_Musics.setVisibility(0);
                            Music.this.musicAdapter = new MusicAdapter(Music.this.getContext(), arrayList2);
                            Music.this.gv_Musics.setAdapter((ListAdapter) Music.this.musicAdapter);
                            Music.this.gv_Musics.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Music.3.1
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView2, View view2, int i5, long j2) {
                                    Music.this.rp.setVisibility(0);
                                    Music.this.havemusic.setVisibility(0);
                                    Music.this.nomusic.setVisibility(8);
                                    Music.this.gv_Musics.setVisibility(8);
                                    Music.this.thismusic = (Savemusic) arrayList2.get(i5);
                                    Music.this.ml.setcontent(Music.this.thismusic);
                                    Music.this.rl.setcontent(Music.this.thismusic);
                                    Music.this.ail.setcontent(Music.this.thismusic);
                                    if (Music.this.rb_music.isChecked()) {
                                        Music.this.ml.setVisibility(0);
                                        Music.this.ail.setVisibility(8);
                                        Music.this.rl.setVisibility(8);
                                        Music.this.ml.SwitchtoMusicSD();
                                    }
                                }
                            });
                        }
                    });
                    this.deleteAlter.addExtView(viewGroup);
                    this.deleteAlter.show();
                    break;
                }
                break;
            case R.id.music_pair /* 2131231465 */:
                if (!MainActivity.islockchangeid) {
                    this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate2 = getActivity().getLayoutInflater().inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView2 = (ListView) viewInflate2.findViewById(R.id.listView4);
                    listView2.setAdapter((ListAdapter) new DeviceListAdapter(getActivity(), MainActivity.netdeviceList));
                    listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Music.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i3, long j) {
                            Savemusic savemusic = new Savemusic();
                            savemusic.room_id = FounctionActivity.roomidfc;
                            savemusic.music_id = Music.this.thismusic.music_id;
                            savemusic.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i3).get("subnetID"));
                            savemusic.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i3).get("deviceID"));
                            MainActivity.mgr.updatemusic(savemusic);
                            Music.this.getdata.postDelayed(Music.this.getmusicidrun, 20L);
                            Toast.makeText(Music.this.getActivity(), "apir " + MainActivity.netdeviceList.get(i3).get("devicename") + " succeed", 0).show();
                            Music.this.pairalter.dismiss();
                        }
                    });
                    this.pairalter.addExtView(viewInflate2);
                    this.pairalter.show();
                    break;
                }
                break;
            case R.id.music_setting /* 2131231466 */:
                if (!MainActivity.islockchangeid) {
                    this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate3 = getActivity().getLayoutInflater().inflate(R.layout.setting_musicinfo, (ViewGroup) null);
                    this.sub = (EditText) viewInflate3.findViewById(R.id.music_subedit);
                    this.dev = (EditText) viewInflate3.findViewById(R.id.music_devedit);
                    this.remark = (EditText) viewInflate3.findViewById(R.id.et_music_remark);
                    this.sub.setText(String.valueOf(this.thismusic.subnetID));
                    this.dev.setText(String.valueOf(this.thismusic.deviceID));
                    this.remark.setText(String.valueOf(this.thismusic.music_remark));
                    this.settingalter.addExtView(viewInflate3);
                    this.settingalter.show();
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public static void finishmusic() {
        getact.finish();
    }

    public void shakeperform(int i) {
        switch (i) {
            case 1:
                this.zaudiocontrol.MusicControl((byte) 4, (byte) 1, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                break;
            case 2:
                this.zaudiocontrol.MusicControl((byte) 4, (byte) 2, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                break;
            case 5:
                if (this.playorpause == 0) {
                    this.zaudiocontrol.MusicControl((byte) 4, (byte) 4, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                    this.playorpause = 1;
                    break;
                } else {
                    this.zaudiocontrol.MusicControl((byte) 4, (byte) 3, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                    this.playorpause = 0;
                    break;
                }
            case 6:
                if (this.playorpause == 0) {
                    this.playorpause = 1;
                    this.ml.setplaybuttonstate("pause");
                    break;
                } else {
                    this.playorpause = 0;
                    this.ml.setplaybuttonstate("play");
                    break;
                }
        }
    }

    public void RunReceiveData(byte[] bArr) {
        if (this.rb_music.isChecked()) {
            this.ml.receiveddata(bArr);
        } else if (this.rb_audioin.isChecked()) {
            this.ail.receiveddata(bArr);
        } else if (this.rb_fm.isChecked()) {
            this.rl.receiveddata(bArr);
        }
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_SHAKE);
        return intentFilter;
    }

    public void onbackpress() {
        if (this.rb_music.isChecked()) {
            this.ml.musicbackpress();
        } else if (this.rb_fm.isChecked()) {
            this.rl.radiobackpress();
        } else if (this.rb_audioin.isChecked()) {
            this.ail.radiobackpress();
        }
    }

    public int getResourdIdByResourdName(String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }
}
