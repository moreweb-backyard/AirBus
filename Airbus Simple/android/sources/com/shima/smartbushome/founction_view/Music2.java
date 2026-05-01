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
import android.widget.Button;
import android.widget.EditText;
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
public class Music2 extends Fragment implements View.OnClickListener {
    public static Activity getact;
    public static int musicchoosestate;
    EditText add_dev;
    EditText add_sub;
    AlertView addmusicalter;
    Button addmusicbutton;
    AudioInLayout ail;
    Dialog aupairdialog;
    private GestureDetector detector;
    EditText dev;
    RelativeLayout havemusic;
    MusicLayout ml;
    RelativeLayout nomusic;
    AlertView pairalter;
    RadioButton rb_audioin;
    RadioButton rb_fm;
    RadioButton rb_music;
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
    Runnable getmusicidrun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Music2.1
        @Override // java.lang.Runnable
        public void run() {
            List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < listQuerymusic.size(); i++) {
                if (listQuerymusic.get(i).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i).music_id == 1) {
                    arrayList.add(listQuerymusic.get(i));
                }
            }
            if (arrayList.size() > 0) {
                Music2.this.havemusic.setVisibility(0);
                Music2.this.nomusic.setVisibility(8);
                Music2.this.thismusic = (Savemusic) arrayList.get(0);
                Music2.this.ml.setcontent(Music2.this.thismusic);
                Music2.this.rl.setcontent(Music2.this.thismusic);
                Music2.this.ail.setcontent(Music2.this.thismusic);
                if (Music2.this.rb_music.isChecked()) {
                    Music2.this.ml.setVisibility(0);
                    Music2.this.ail.setVisibility(8);
                    Music2.this.rl.setVisibility(8);
                    Music2.this.ml.SwitchtoMusicSD();
                    return;
                }
                return;
            }
            Music2.this.havemusic.setVisibility(8);
            Music2.this.nomusic.setVisibility(0);
        }
    };
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Music2.3
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == Music2.this.settingalter) {
                if (i == 0) {
                    Savemusic savemusic = new Savemusic();
                    savemusic.room_id = FounctionActivity.roomidfc;
                    savemusic.music_id = 1;
                    savemusic.subnetID = Integer.parseInt(Music2.this.sub.getText().toString().trim());
                    savemusic.deviceID = Integer.parseInt(Music2.this.dev.getText().toString().trim());
                    MainActivity.mgr.updatemusic(savemusic);
                    Music2.this.getdata.postDelayed(Music2.this.getmusicidrun, 20L);
                    return;
                }
                return;
            }
            if (obj == Music2.this.addmusicalter && i == 0) {
                ArrayList arrayList = new ArrayList();
                Savemusic savemusic2 = new Savemusic();
                savemusic2.room_id = FounctionActivity.roomidfc;
                savemusic2.music_id = 1;
                savemusic2.subnetID = Integer.parseInt(Music2.this.add_sub.getText().toString().trim());
                savemusic2.deviceID = Integer.parseInt(Music2.this.add_dev.getText().toString().trim());
                arrayList.add(savemusic2);
                MainActivity.mgr.addmusic(arrayList);
                Music2.this.getdata.postDelayed(Music2.this.getmusicidrun, 20L);
            }
        }
    };
    public RadioGroup.OnCheckedChangeListener selectchange = new RadioGroup.OnCheckedChangeListener() { // from class: com.shima.smartbushome.founction_view.Music2.4
        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.audio) {
                Music2.musicchoosestate = 2;
                Music2.this.ml.setVisibility(8);
                Music2.this.ail.setVisibility(0);
                Music2.this.rl.setVisibility(8);
                if (!Music2.this.ail.ifinitstarted()) {
                    Music2.this.ail.initview(Music2.this.getActivity());
                }
                Music2.this.ail.SwitchtoAudioIn();
                return;
            }
            if (checkedRadioButtonId == R.id.music) {
                Music2.musicchoosestate = 0;
                Music2.this.ml.setVisibility(0);
                Music2.this.ail.setVisibility(8);
                Music2.this.rl.setVisibility(8);
                Music2.this.ml.SwitchtoMusicSD();
                return;
            }
            if (checkedRadioButtonId != R.id.radio) {
                return;
            }
            Music2.musicchoosestate = 1;
            Music2.this.ml.setVisibility(8);
            Music2.this.ail.setVisibility(8);
            Music2.this.rl.setVisibility(0);
            if (!Music2.this.rl.ifinitstarted()) {
                Music2.this.rl.initview(Music2.this.getActivity());
            }
            Music2.this.rl.SwitchtoRadio();
        }
    };
    int playorpause = 0;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Music2.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length > 25) {
                    Music2.this.RunReceiveData(byteArrayExtra);
                    return;
                }
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                Music2.this.onbackpress();
            } else {
                if (!FounctionActivity.ACTION_SHAKE.equals(action) || MainActivity.islockshake) {
                    return;
                }
                Music2.this.shakeperform(intent.getIntExtra("shake_type", 0));
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
        this.add_sub.setText("0");
        this.add_dev.setText("0");
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
            case R.id.music_pair /* 2131231465 */:
                if (!MainActivity.islockchangeid) {
                    this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView = (ListView) viewInflate.findViewById(R.id.listView4);
                    listView.setAdapter((ListAdapter) new DeviceListAdapter(getActivity(), MainActivity.netdeviceList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Music2.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                            Savemusic savemusic = new Savemusic();
                            savemusic.room_id = FounctionActivity.roomidfc;
                            savemusic.music_id = 1;
                            savemusic.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            savemusic.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            MainActivity.mgr.updatemusic(savemusic);
                            Music2.this.getdata.postDelayed(Music2.this.getmusicidrun, 20L);
                            Toast.makeText(Music2.this.getActivity(), "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                            Music2.this.pairalter.dismiss();
                        }
                    });
                    this.pairalter.addExtView(viewInflate);
                    this.pairalter.show();
                    break;
                }
                break;
            case R.id.music_setting /* 2131231466 */:
                if (!MainActivity.islockchangeid) {
                    this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
                    View viewInflate2 = getActivity().getLayoutInflater().inflate(R.layout.setting_musicinfo, (ViewGroup) null);
                    this.sub = (EditText) viewInflate2.findViewById(R.id.music_subedit);
                    this.dev = (EditText) viewInflate2.findViewById(R.id.music_devedit);
                    this.sub.setText(String.valueOf(this.thismusic.subnetID));
                    this.dev.setText(String.valueOf(this.thismusic.deviceID));
                    this.settingalter.addExtView(viewInflate2);
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
