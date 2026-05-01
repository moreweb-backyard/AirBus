package com.shima.smartbushome.founction_view;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActivityChooserView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.litesuits.http.data.Charsets;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MoodIconAdapter;
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.database.Savemood;
import com.shima.smartbushome.database.Savemoodbutton;
import com.shima.smartbushome.database.Savemusic;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.founction_command.audio_incontrol;
import com.shima.smartbushome.founction_command.curtaincontrol;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.founction_command.moodcontrol;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.founction_command.radiocontrol;
import com.shima.smartbushome.selflayout.MoodLayout;
import com.shima.smartbushome.udp.udp_socket;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Mood extends Fragment {
    private static final byte const_mode_auto = 3;
    private static final byte const_mode_cool = 0;
    private static final byte const_mode_fan = 2;
    private static final byte const_mode_heat = 1;
    MenuItem add;
    AlertView addingalter;
    AlertView curtainalter;
    MenuItem delete;
    byte[] fanArray;
    AlertView iconalter;
    ACcontrol macc;
    audio_incontrol maic;
    curtaincontrol mctc;
    lightcontrol mlc;
    moodcontrol mmc;
    musiccontrol mmuc;
    byte[] modeArray;
    LinearLayout moodcontainer;
    EditText moodename;
    ProgressDialog moodsaving;
    radiocontrol mrc;
    AlertView musicalter;
    ListView test;
    TextView tv_cur_statue;
    TextView tv_music_status;
    View view;
    List<Savemoodbutton> roommoodbutton = new ArrayList();
    List<MoodLayout> moodlist = new ArrayList();
    Handler moodhandler = new Handler();
    Handler moodselecthandler = new Handler();
    Handler lighthandler = new Handler();
    Handler hvachandler = new Handler();
    Handler zaudiohandler = new Handler();
    Handler curtainhandler = new Handler();
    List<Savelight> lightdata = new ArrayList();
    List<Savehvac> hvacdata = new ArrayList();
    List<Savemusic> musicdata = new ArrayList();
    List<Savecurtain> curtaindata = new ArrayList();
    boolean deletemood = false;
    String iconrout = "mood_icon1";
    String savecurtainstate = "";
    String savezaudiostate = "";
    private List<String> iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.founction_view.Mood.1
        {
            add("mood_icon1");
            add("mood_icon2");
            add("mood_icon3");
            add("mood_icon4");
            add("mood_icon5");
            add("mood_icon6");
            add("mood_icon7");
            add("mood_icon8");
            add("mood_icon9");
            add("mood_icon10");
        }
    };
    boolean getallstate = false;
    public OnItemClickListener itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Mood.5
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == Mood.this.addingalter && i == 0) {
                SparseBooleanArray checkedItemPositions = Mood.this.test.getCheckedItemPositions();
                for (int i2 = 0; i2 < checkedItemPositions.size(); i2++) {
                    int iKeyAt = checkedItemPositions.keyAt(i2);
                    if (checkedItemPositions.valueAt(i2)) {
                        Mood.this.selectedItems[iKeyAt] = 1;
                    }
                }
                if (Mood.this.moodename.getText().toString().trim().length() < 1) {
                    Toast.makeText(Mood.this.getActivity(), "please enter the mood name", 0).show();
                    return;
                }
                Savemoodbutton savemoodbutton = new Savemoodbutton();
                savemoodbutton.room_id = FounctionActivity.roomidfc;
                if (Mood.this.roommoodbutton.size() == 0) {
                    savemoodbutton.mood_id = 1;
                } else {
                    savemoodbutton.mood_id = Mood.this.roommoodbutton.get(Mood.this.roommoodbutton.size() - 1).mood_id + 1;
                }
                savemoodbutton.mood_name = Mood.this.moodename.getText().toString().trim();
                savemoodbutton.mood_icon = Mood.this.iconrout;
                MainActivity.mgr.addmoodbutton(savemoodbutton);
                Mood.this.moodhandler.postDelayed(Mood.this.getmoodbuttonlist, 30L);
                Mood.this.getallstate = true;
                Mood.this.addingalter.dismiss();
            }
        }
    };
    Runnable getmoodbuttonlist = new Runnable() { // from class: com.shima.smartbushome.founction_view.Mood.6
        @Override // java.lang.Runnable
        public void run() {
            Mood.this.moodcontainer.removeAllViews();
            Mood.this.roommoodbutton.clear();
            List<Savemoodbutton> listQuerymoodbutton = MainActivity.mgr.querymoodbutton();
            for (int i = 0; i < listQuerymoodbutton.size(); i++) {
                if (listQuerymoodbutton.get(i).room_id == FounctionActivity.roomidfc) {
                    Mood.this.roommoodbutton.add(listQuerymoodbutton.get(i));
                }
            }
            for (int i2 = 0; i2 < Mood.this.roommoodbutton.size(); i2++) {
                Mood.this.addmood(Mood.this.roommoodbutton.get(i2));
            }
            if (Mood.this.getallstate) {
                Mood.this.getallstate = false;
                List<Savelight> listQuerylight = MainActivity.mgr.querylight();
                for (int i3 = 0; i3 < listQuerylight.size(); i3++) {
                    if (listQuerylight.get(i3).room_id == FounctionActivity.roomidfc) {
                        Mood.this.lightdata.add(listQuerylight.get(i3));
                    }
                }
                List<Savehvac> listQueryhvac = MainActivity.mgr.queryhvac();
                for (int i4 = 0; i4 < listQueryhvac.size(); i4++) {
                    if (listQueryhvac.get(i4).room_id == FounctionActivity.roomidfc) {
                        Mood.this.hvacdata.add(listQueryhvac.get(i4));
                    }
                }
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                for (int i5 = 0; i5 < listQuerymusic.size(); i5++) {
                    if (listQuerymusic.get(i5).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i5).music_id == 1) {
                        Mood.this.musicdata.add(listQuerymusic.get(i5));
                    }
                }
                List<Savecurtain> listQuerycurtain = MainActivity.mgr.querycurtain();
                for (int i6 = 0; i6 < listQuerycurtain.size(); i6++) {
                    if (listQuerycurtain.get(i6).room_id == FounctionActivity.roomidfc) {
                        Mood.this.curtaindata.add(listQuerycurtain.get(i6));
                    }
                }
                Mood.this.moodsaving.show();
                Mood.this.getmoodsavestaterun();
            }
        }
    };
    byte lightsub = 0;
    byte lightdev = 0;
    int lightcount = 0;
    int state1 = 0;
    boolean lightsent = false;
    int lightovertime = 0;
    Runnable getlightstaterun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Mood.7
        @Override // java.lang.Runnable
        public void run() {
            if (Mood.this.lightsent) {
                Mood.this.lightovertime++;
                if (Mood.this.lightovertime > 20) {
                    Mood.this.lightsent = false;
                    Mood.this.lightcount++;
                } else {
                    Mood.this.mlc.getlightstate(Mood.this.lightsub, Mood.this.lightdev, MainActivity.mydupsocket);
                }
                Mood.this.lighthandler.postDelayed(Mood.this.getlightstaterun, 300L);
                return;
            }
            if (Mood.this.lightcount < Mood.this.lightdata.size()) {
                Mood.this.lightovertime = 0;
                if (Mood.this.lightdata.get(Mood.this.lightcount).subnetID != (Mood.this.lightsub & 255)) {
                    Mood.this.lightsub = (byte) Mood.this.lightdata.get(Mood.this.lightcount).subnetID;
                    Mood.this.lightdev = (byte) Mood.this.lightdata.get(Mood.this.lightcount).deviceID;
                    Mood.this.lightsent = true;
                    Mood.this.state1 = Mood.this.lightcount;
                } else if (Mood.this.lightdata.get(Mood.this.lightcount).deviceID != (Mood.this.lightdev & 255)) {
                    Mood.this.lightsub = (byte) Mood.this.lightdata.get(Mood.this.lightcount).subnetID;
                    Mood.this.lightdev = (byte) Mood.this.lightdata.get(Mood.this.lightcount).deviceID;
                    Mood.this.lightsent = true;
                    Mood.this.state1 = Mood.this.lightcount;
                } else {
                    Mood.this.lightcount++;
                }
                Mood.this.lighthandler.postDelayed(Mood.this.getlightstaterun, 300L);
                return;
            }
            Mood.this.lighthandler.removeCallbacks(Mood.this.getlightstaterun);
            Mood.this.selectedcount++;
            Mood.this.getmoodsavestaterun();
            Mood.this.lightsub = (byte) 0;
            Mood.this.lightdev = (byte) 0;
            Mood.this.lightcount = 0;
            Mood.this.lightovertime = 0;
        }
    };
    int acStateCount = 0;
    int acdeviceCount = 0;
    boolean readCountfanAndMode = false;
    boolean readcstate = false;
    Runnable gethvacstaterun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Mood.8
        @Override // java.lang.Runnable
        public void run() {
            switch (Mood.this.acStateCount) {
                case 0:
                    Mood.this.macc.ACReadCountFanAndMode((byte) Mood.this.hvacdata.get(Mood.this.acdeviceCount).subnetID, (byte) Mood.this.hvacdata.get(Mood.this.acdeviceCount).deviceID, MainActivity.mydupsocket);
                    Mood.this.hvachandler.postDelayed(Mood.this.gethvacstaterun, 300L);
                    break;
                case 1:
                    Mood.this.macc.ACReadCState((byte) Mood.this.hvacdata.get(Mood.this.acdeviceCount).subnetID, (byte) Mood.this.hvacdata.get(Mood.this.acdeviceCount).deviceID, MainActivity.mydupsocket);
                    Mood.this.hvachandler.postDelayed(Mood.this.gethvacstaterun, 300L);
                    break;
                case 2:
                    Mood.this.acdeviceCount++;
                    if (Mood.this.acdeviceCount >= Mood.this.hvacdata.size()) {
                        Mood.this.hvachandler.removeCallbacks(Mood.this.gethvacstaterun);
                        Mood.this.selectedcount++;
                        Mood.this.getmoodsavestaterun();
                        Mood.this.acdeviceCount = 0;
                        Mood.this.acStateCount = 0;
                        Mood.this.readCountfanAndMode = false;
                        Mood.this.readcstate = false;
                        break;
                    } else {
                        Mood.this.acStateCount = 0;
                        Mood.this.readCountfanAndMode = false;
                        Mood.this.readcstate = false;
                        Mood.this.hvachandler.postDelayed(Mood.this.gethvacstaterun, 300L);
                        break;
                    }
            }
        }
    };
    boolean musicstate = false;
    int musicovertime = 0;
    Runnable getzaudiostaterun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Mood.9
        @Override // java.lang.Runnable
        public void run() {
            if (!Mood.this.musicstate) {
                Mood.this.musicovertime++;
                if (Mood.this.musicovertime > 30) {
                    Mood.this.musicstate = true;
                } else {
                    Mood.this.mmuc.GetMusicState((byte) Mood.this.musicdata.get(0).subnetID, (byte) Mood.this.musicdata.get(0).deviceID, MainActivity.mydupsocket);
                }
                Mood.this.zaudiohandler.postDelayed(Mood.this.getzaudiostaterun, 300L);
                return;
            }
            Mood.this.musicovertime = 0;
            Mood.this.musicstate = false;
            Mood.this.voicestate = false;
            Mood.this.albumstate = false;
            Mood.this.songOrchannelstate = false;
            Mood.this.currentfounction = 0;
            Mood.this.ablumno = 0;
            Mood.this.songno = 0;
            Mood.this.channelno = 0;
            Mood.this.selectedcount++;
            Mood.this.getmoodsavestaterun();
            Mood.this.zaudiohandler.removeCallbacks(Mood.this.getzaudiostaterun);
        }
    };
    byte curtainsub = 0;
    byte curtaindev = 0;
    int curtaincount = 0;
    int state2 = 0;
    boolean curtainsent = false;
    Runnable getcurtainstaterun = new Runnable() { // from class: com.shima.smartbushome.founction_view.Mood.10
        @Override // java.lang.Runnable
        public void run() {
            for (int i = 0; i < Mood.this.curtaindata.size(); i++) {
                Savemood savemood = new Savemood();
                savemood.room_id = FounctionActivity.roomidfc;
                savemood.subnetID = Mood.this.curtaindata.get(i).subnetID;
                savemood.deviceID = Mood.this.curtaindata.get(i).deviceID;
                savemood.mood_id = Mood.this.roommoodbutton.get(Mood.this.roommoodbutton.size() - 1).mood_id;
                savemood.control_type = 4;
                if (Mood.this.savecurtainstate.equals("open")) {
                    savemood.value_1 = Mood.this.curtaindata.get(i).channel_1;
                    savemood.value_2 = 100;
                } else if (Mood.this.savecurtainstate.equals("close")) {
                    savemood.value_1 = Mood.this.curtaindata.get(i).channel_2;
                    savemood.value_2 = 100;
                }
                savemood.value_3 = 0;
                savemood.value_4 = 0;
                savemood.value_5 = 0;
                savemood.value_6 = 0;
                MainActivity.mgr.addmood(savemood);
            }
            Mood.this.selectedcount++;
            Mood.this.getmoodsavestaterun();
        }
    };
    int[] selectedItems = new int[4];
    int selectedcount = 0;
    boolean doing = false;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.founction_view.Mood.11
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) throws NumberFormatException {
            String action = intent.getAction();
            if (udp_socket.ACTION_DATA_IN.equals(action)) {
                byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
                if (byteArrayExtra.length <= 27 || Mood.this.doing) {
                    return;
                }
                Mood.this.doing = true;
                switch (Mood.this.selectedcount) {
                    case 0:
                        Mood.this.runrevlight(byteArrayExtra);
                        break;
                    case 1:
                        Mood.this.runrevhvac(byteArrayExtra);
                        break;
                    case 2:
                        Mood.this.runrevzaudio(byteArrayExtra);
                        break;
                }
                Mood.this.doing = false;
                return;
            }
            if (FounctionActivity.ACTION_BACKPRESS.equals(action)) {
                Mood.this.getActivity().finish();
            } else if (FounctionActivity.ACTION_DELETEMOOD.equals(action)) {
                Mood.this.moodhandler.postDelayed(Mood.this.getmoodbuttonlist, 30L);
            }
        }
    };
    int currentfounction = 0;
    int ablumno = 0;
    int songno = 0;
    int channelno = 0;
    boolean voicestate = false;
    boolean albumstate = false;
    boolean songOrchannelstate = false;

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_mood, viewGroup, false);
        getActivity().setTitle("Mood");
        setHasOptionsMenu(true);
        initView();
        this.moodhandler.postDelayed(this.getmoodbuttonlist, 30L);
        setMargins((ImageView) this.view.findViewById(R.id.iv_layout), 0, -FounctionActivity.topHeight, 0, 0);
        return this.view;
    }

    public static void setMargins(View view, int i, int i2, int i3, int i4) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i, i2, i3, i4);
            view.requestLayout();
        }
    }

    public void initView() {
        this.moodcontainer = (LinearLayout) this.view.findViewById(R.id.moodview);
        this.moodsaving = new ProgressDialog(getActivity());
        this.moodsaving.setCancelable(true);
        this.moodsaving.setCanceledOnTouchOutside(false);
        this.moodsaving.setMessage("Saving Mood info...");
        this.moodsaving.setProgressStyle(1);
        this.mmc = new moodcontrol();
        this.mlc = new lightcontrol();
        this.macc = new ACcontrol();
        this.mmuc = new musiccontrol();
        this.mrc = new radiocontrol();
        this.maic = new audio_incontrol();
        this.mctc = new curtaincontrol();
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
        this.lighthandler.removeCallbacks(this.getlightstaterun);
        this.hvachandler.removeCallbacks(this.gethvacstaterun);
        this.zaudiohandler.removeCallbacks(this.getzaudiostaterun);
        this.curtainhandler.removeCallbacks(this.getcurtainstaterun);
        this.moodhandler.removeCallbacks(this.getmoodbuttonlist);
    }

    @Override // android.support.v4.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.mood_setting_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
        this.add = menu.findItem(R.id.mood_add);
        this.delete = menu.findItem(R.id.mood_remove);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != R.id.mood_add) {
            if (itemId == R.id.mood_remove) {
                this.deletemood = !this.deletemood;
                if (this.deletemood) {
                    if (this.moodlist.size() > 0) {
                        this.moodlist.clear();
                    }
                    for (int i = 0; i < this.roommoodbutton.size(); i++) {
                        MoodLayout moodLayout = (MoodLayout) this.moodcontainer.findViewById(this.roommoodbutton.get(i).mood_id);
                        this.moodlist.add(moodLayout);
                        moodLayout.setdeleteable(true);
                        moodLayout.setDeletemode(true);
                        moodLayout.setcheck(false);
                    }
                    this.add.setTitle("CANCEL DELETE");
                    this.delete.setTitle("DELETE");
                } else {
                    for (int i2 = 0; i2 < this.moodlist.size(); i2++) {
                        if (this.moodlist.get(i2).getchoosestate()) {
                            MainActivity.mgr.deletemood("mood", this.moodlist.get(i2).getMoodID(), this.moodlist.get(i2).getRoomid());
                            MainActivity.mgr.deletemoodbutton("moodbutton", this.moodlist.get(i2).getMoodID(), this.moodlist.get(i2).getRoomid());
                        }
                    }
                    this.moodhandler.postDelayed(this.getmoodbuttonlist, 30L);
                    this.add.setTitle("ADD MOOD");
                    this.delete.setTitle("DELETE MOOD");
                    if (this.moodlist.size() > 0) {
                        this.moodlist.clear();
                    }
                }
            }
        } else if (this.deletemood) {
            this.deletemood = false;
            for (int i3 = 0; i3 < this.moodlist.size(); i3++) {
                this.moodlist.get(i3).setdeleteable(false);
                this.moodlist.get(i3).setDeletemode(false);
            }
            this.add.setTitle("ADD MOOD");
            this.delete.setTitle("DELETE MOOD");
            if (this.moodlist.size() > 0) {
                this.moodlist.clear();
            }
        } else {
            this.addingalter = new AlertView("Add Mood", null, "CANCEL", new String[]{"SAVE"}, null, getActivity(), AlertView.Style.Alert, this.itemclick);
            View viewInflate = getActivity().getLayoutInflater().inflate(R.layout.mood_add_layout, (ViewGroup) null);
            this.tv_cur_statue = (TextView) viewInflate.findViewById(R.id.tv_cur_status);
            this.tv_music_status = (TextView) viewInflate.findViewById(R.id.tv_music_status);
            this.test = (ListView) viewInflate.findViewById(R.id.listView5);
            this.moodename = (EditText) viewInflate.findViewById(R.id.moodnamedeit);
            final ImageView imageView = (ImageView) viewInflate.findViewById(R.id.imageView5);
            this.test.setAdapter((ListAdapter) new ArrayAdapter(getActivity(), R.layout.simple_list_item_choice, new ArrayList<String>() { // from class: com.shima.smartbushome.founction_view.Mood.2
                {
                    add("Light");
                    add("HVAC");
                    add("Z-Audio");
                    add("Curtain");
                }
            }));
            this.test.setChoiceMode(2);
            this.test.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Mood.3
                @Override // android.widget.AdapterView.OnItemClickListener
                public void onItemClick(AdapterView<?> adapterView, View view, int i4, long j) {
                    if (i4 == 2) {
                        if (Mood.this.test.isItemChecked(2)) {
                            Mood.this.musicalter = new AlertView(null, null, "CANCEL", null, null, Mood.this.getActivity(), AlertView.Style.ActionSheet, Mood.this.itemclick);
                            ListView listView = new ListView(Mood.this.getActivity());
                            listView.setAdapter((ListAdapter) new ArrayAdapter(Mood.this.getActivity(), R.layout.simplelistitem, new String[]{"ZAudio On", "ZAudio Off"}));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Mood.3.1
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView2, View view2, int i5, long j2) {
                                    if (i5 == 0) {
                                        Mood.this.savezaudiostate = "open";
                                        Mood.this.tv_music_status.setText("ZAudio On");
                                    } else {
                                        Mood.this.savezaudiostate = "close";
                                        Mood.this.tv_music_status.setText("ZAudio Off");
                                    }
                                    Mood.this.musicalter.dismiss();
                                }
                            });
                            Mood.this.musicalter.addExtView(listView);
                            Mood.this.musicalter.show();
                        } else {
                            Mood.this.tv_music_status.setText("");
                        }
                    }
                    if (i4 == 3) {
                        if (Mood.this.test.isItemChecked(3)) {
                            Mood.this.curtainalter = new AlertView(null, null, "CANCEL", null, null, Mood.this.getActivity(), AlertView.Style.ActionSheet, Mood.this.itemclick);
                            ListView listView2 = new ListView(Mood.this.getActivity());
                            listView2.setAdapter((ListAdapter) new ArrayAdapter(Mood.this.getActivity(), R.layout.simplelistitem, new String[]{"Curtain Open", "Curtain Close"}));
                            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Mood.3.2
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView2, View view2, int i5, long j2) {
                                    if (i5 == 0) {
                                        Mood.this.savecurtainstate = "open";
                                        Mood.this.tv_cur_statue.setText("Curtain Open");
                                    } else {
                                        Mood.this.savecurtainstate = "close";
                                        Mood.this.tv_cur_statue.setText("Curtain Close");
                                    }
                                    Mood.this.curtainalter.dismiss();
                                }
                            });
                            Mood.this.curtainalter.addExtView(listView2);
                            Mood.this.curtainalter.show();
                            return;
                        }
                        Mood.this.tv_cur_statue.setText("");
                    }
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.founction_view.Mood.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Mood.this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, Mood.this.getActivity(), AlertView.Style.Alert, Mood.this.itemclick);
                    View viewInflate2 = Mood.this.getActivity().getLayoutInflater().inflate(R.layout.mood_icon_select, (ViewGroup) null);
                    GridView gridView = (GridView) viewInflate2.findViewById(R.id.gridView2);
                    gridView.setAdapter((ListAdapter) new MoodIconAdapter(Mood.this.getActivity()));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.founction_view.Mood.4.1
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view2, int i4, long j) {
                            Mood.this.iconrout = (String) Mood.this.iconarray.get(i4);
                            imageView.setImageResource(Mood.getResourdIdByResourdName(Mood.this.getActivity(), (String) Mood.this.iconarray.get(i4)));
                            Mood.this.iconalter.dismiss();
                        }
                    });
                    Mood.this.iconalter.addExtView(viewInflate2);
                    Mood.this.iconalter.show();
                }
            });
            this.addingalter.addExtView(viewInflate);
            this.addingalter.setShoulddismiss(false);
            this.addingalter.show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void addmood(Savemoodbutton savemoodbutton) {
        MoodLayout moodLayout = new MoodLayout(getActivity());
        moodLayout.seticon(getResources().getDrawable(getResourdIdByResourdName(getActivity(), savemoodbutton.mood_icon)));
        moodLayout.setitile(savemoodbutton.mood_name);
        moodLayout.setId(savemoodbutton.mood_id);
        moodLayout.setcontent(savemoodbutton);
        this.moodcontainer.addView(moodLayout);
    }

    public void getmoodsavestaterun() {
        if (this.selectedcount == 4) {
            for (int i = 0; i < 4; i++) {
                this.selectedItems[i] = 0;
            }
            this.selectedcount = 0;
            this.lighthandler.removeCallbacks(this.getlightstaterun);
            this.hvachandler.removeCallbacks(this.gethvacstaterun);
            this.zaudiohandler.removeCallbacks(this.getzaudiostaterun);
            this.curtainhandler.removeCallbacks(this.getcurtainstaterun);
            this.moodsaving.dismiss();
            Toast.makeText(getActivity(), "succeed", 0).show();
            return;
        }
        if (this.selectedItems[this.selectedcount] == 1) {
            this.moodsaving.setProgress((this.selectedcount + 1) * 25);
            switch (this.selectedcount) {
                case 0:
                    this.lighthandler.postDelayed(this.getlightstaterun, 10L);
                    break;
                case 1:
                    this.hvachandler.postDelayed(this.gethvacstaterun, 10L);
                    break;
                case 2:
                    this.zaudiohandler.postDelayed(this.getzaudiostaterun, 10L);
                    break;
                case 3:
                    this.curtainhandler.postDelayed(this.getcurtainstaterun, 10L);
                    break;
            }
        }
        if (this.selectedItems[this.selectedcount] == 0) {
            this.moodsaving.setProgress((this.selectedcount + 1) * 25);
            this.selectedcount++;
            getmoodsavestaterun();
        }
    }

    public void runrevlight(byte[] bArr) {
        if (((bArr[21] & 255) << 8) + (bArr[22] & 255) == 52 && this.lightsub == bArr[17] && this.lightdev == bArr[18] && this.lightcount == this.state1) {
            for (int i = 0; i < this.lightdata.size(); i++) {
                if (this.lightdata.get(i).subnetID == (bArr[17] & 255) && this.lightdata.get(i).deviceID == (bArr[18] & 255)) {
                    if (this.lightdata.get(i).lightType != 3) {
                        try {
                            Savemood savemood = new Savemood();
                            savemood.room_id = FounctionActivity.roomidfc;
                            savemood.subnetID = bArr[17] & 255;
                            savemood.deviceID = bArr[18] & 255;
                            savemood.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                            savemood.control_type = 1;
                            savemood.value_1 = this.lightdata.get(i).channel;
                            savemood.value_2 = bArr[this.lightdata.get(i).channel + 25];
                            savemood.value_3 = 0;
                            savemood.value_4 = 0;
                            savemood.value_5 = 0;
                            savemood.value_6 = 0;
                            MainActivity.mgr.addmood(savemood);
                        } catch (Exception unused) {
                            Toast.makeText(getActivity(), "Record light status fail", 0).show();
                        }
                    } else {
                        Savemood savemood2 = new Savemood();
                        savemood2.room_id = FounctionActivity.roomidfc;
                        savemood2.subnetID = bArr[17] & 255;
                        savemood2.deviceID = bArr[18] & 255;
                        savemood2.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                        savemood2.control_type = 1;
                        savemood2.value_1 = bArr[26];
                        savemood2.value_2 = bArr[27];
                        savemood2.value_3 = bArr[28];
                        savemood2.value_4 = bArr[29];
                        savemood2.value_5 = 0;
                        savemood2.value_6 = 1;
                        MainActivity.mgr.addmood(savemood2);
                    }
                }
            }
            this.lightsent = false;
            this.lightcount++;
        }
    }

    public void runrevhvac(byte[] bArr) {
        byte b;
        byte b2;
        byte b3;
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if ((bArr[17] & 255) == this.hvacdata.get(this.acdeviceCount).subnetID && (bArr[18] & 255) == this.hvacdata.get(this.acdeviceCount).deviceID) {
            if (i != 57581) {
                if (i == 57637 && !this.readCountfanAndMode) {
                    this.fanArray = new byte[bArr[25]];
                    for (int i2 = 0; i2 < this.fanArray.length; i2++) {
                        this.fanArray[i2] = bArr[i2 + 26];
                    }
                    int length = this.fanArray.length + 26;
                    for (int i3 = 0; i3 < 10; i3++) {
                        if (bArr[length] <= 32 && bArr[length] != 0) {
                            if (bArr[length] < 6) {
                                break;
                            }
                        } else {
                            length++;
                        }
                    }
                    this.modeArray = new byte[bArr[length]];
                    for (int i4 = 0; i4 < this.modeArray.length; i4++) {
                        this.modeArray[i4] = bArr[length + 1 + i4];
                    }
                    this.readCountfanAndMode = true;
                    this.acStateCount = 1;
                    return;
                }
                return;
            }
            if (this.readcstate) {
                return;
            }
            if (bArr[25] == 0) {
                Savemood savemood = new Savemood();
                savemood.room_id = FounctionActivity.roomidfc;
                savemood.subnetID = bArr[17] & 255;
                savemood.deviceID = bArr[18] & 255;
                savemood.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                savemood.control_type = 2;
                savemood.value_1 = 1;
                savemood.value_2 = 0;
                savemood.value_3 = 0;
                savemood.value_4 = 0;
                savemood.value_5 = 0;
                savemood.value_6 = 0;
                MainActivity.mgr.addmood(savemood);
            } else {
                Savemood savemood2 = new Savemood();
                savemood2.room_id = FounctionActivity.roomidfc;
                savemood2.subnetID = bArr[17] & 255;
                savemood2.deviceID = bArr[18] & 255;
                savemood2.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                savemood2.control_type = 2;
                savemood2.value_1 = 1;
                savemood2.value_2 = 1;
                savemood2.value_3 = 0;
                savemood2.value_4 = 0;
                savemood2.value_5 = 0;
                savemood2.value_6 = 0;
                MainActivity.mgr.addmood(savemood2);
            }
            if ((bArr[27] >> 4) == this.modeArray.length) {
                b = this.modeArray[(bArr[27] >> 4) - 1];
            } else {
                b = this.modeArray[bArr[27] >> 4];
            }
            Savemood savemood3 = new Savemood();
            savemood3.room_id = FounctionActivity.roomidfc;
            savemood3.subnetID = bArr[17] & 255;
            savemood3.deviceID = bArr[18] & 255;
            savemood3.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
            savemood3.control_type = 2;
            savemood3.value_1 = 2;
            savemood3.value_2 = b;
            savemood3.value_3 = 0;
            savemood3.value_4 = 0;
            savemood3.value_5 = 0;
            savemood3.value_6 = 0;
            MainActivity.mgr.addmood(savemood3);
            if (b == 3) {
                b2 = bArr[32];
            } else {
                switch (b) {
                    case 0:
                        b2 = bArr[26];
                        break;
                    case 1:
                        b2 = bArr[30];
                        break;
                    default:
                        b2 = 0;
                        break;
                }
            }
            Savemood savemood4 = new Savemood();
            savemood4.room_id = FounctionActivity.roomidfc;
            savemood4.subnetID = bArr[17] & 255;
            savemood4.deviceID = bArr[18] & 255;
            savemood4.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
            savemood4.control_type = 2;
            savemood4.value_1 = 3;
            savemood4.value_2 = b;
            savemood4.value_3 = b2;
            savemood4.value_4 = 0;
            savemood4.value_5 = 0;
            savemood4.value_6 = 0;
            MainActivity.mgr.addmood(savemood4);
            if ((bArr[27] & 15) == this.fanArray.length) {
                b3 = this.fanArray[(bArr[27] & 15) - 1];
            } else {
                b3 = this.fanArray[bArr[27] & 15];
            }
            Savemood savemood5 = new Savemood();
            savemood5.room_id = FounctionActivity.roomidfc;
            savemood5.subnetID = bArr[17] & 255;
            savemood5.deviceID = bArr[18] & 255;
            savemood5.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
            savemood5.control_type = 2;
            savemood5.value_1 = 4;
            savemood5.value_2 = b3;
            savemood5.value_3 = 0;
            savemood5.value_4 = 0;
            savemood5.value_5 = 0;
            savemood5.value_6 = 0;
            MainActivity.mgr.addmood(savemood5);
            this.fanArray = null;
            this.modeArray = null;
            this.readcstate = true;
            this.acStateCount = 2;
        }
    }

    public void runrevzaudio(byte[] bArr) throws NumberFormatException {
        if (this.musicstate) {
            return;
        }
        int i = (bArr[25] << 8) + (bArr[26] & 255);
        int i2 = bArr[36] & 255;
        if (i == 9050) {
            if (this.voicestate) {
                return;
            }
            this.voicestate = true;
            if (bArr[32] == 83 && bArr[33] == 82 && bArr[34] == 67) {
                this.currentfounction = Integer.parseInt(bytetostring(new byte[]{bArr[35]}, "ascii"));
                Savemood savemood = new Savemood();
                savemood.room_id = FounctionActivity.roomidfc;
                savemood.subnetID = bArr[17] & 255;
                savemood.deviceID = bArr[18] & 255;
                savemood.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                savemood.control_type = 3;
                savemood.value_1 = this.currentfounction;
                savemood.value_2 = 0;
                savemood.value_3 = 0;
                savemood.value_4 = 0;
                savemood.value_5 = 0;
                savemood.value_6 = 1;
                MainActivity.mgr.addmood(savemood);
            }
            byte[] bArr2 = new byte[2];
            if (bArr[37] == 86 && bArr[38] == 79 && bArr[39] == 76) {
                if (bArr[41] == 13) {
                    bArr2[0] = 48;
                    bArr2[1] = bArr[40];
                } else if (bArr[42] == 13) {
                    bArr2[0] = bArr[40];
                    bArr2[1] = bArr[41];
                }
            }
            int i3 = Integer.parseInt(bytetostring(bArr2, "ascii"));
            Savemood savemood2 = new Savemood();
            savemood2.room_id = FounctionActivity.roomidfc;
            savemood2.subnetID = bArr[17] & 255;
            savemood2.deviceID = bArr[18] & 255;
            savemood2.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
            savemood2.control_type = 3;
            savemood2.value_1 = this.currentfounction;
            savemood2.value_2 = 0;
            savemood2.value_3 = 0;
            savemood2.value_4 = 0;
            savemood2.value_5 = i3;
            savemood2.value_6 = 2;
            MainActivity.mgr.addmood(savemood2);
            if (this.currentfounction == 6) {
                if (this.savezaudiostate.equals("open")) {
                    Savemood savemood3 = new Savemood();
                    savemood3.room_id = FounctionActivity.roomidfc;
                    savemood3.subnetID = bArr[17] & 255;
                    savemood3.deviceID = bArr[18] & 255;
                    savemood3.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                    savemood3.control_type = 3;
                    savemood3.value_1 = this.currentfounction;
                    savemood3.value_2 = 0;
                    savemood3.value_3 = 0;
                    savemood3.value_4 = 0;
                    savemood3.value_5 = 0;
                    savemood3.value_6 = 3;
                    MainActivity.mgr.addmood(savemood3);
                } else if (this.savezaudiostate.equals("close")) {
                    Savemood savemood4 = new Savemood();
                    savemood4.room_id = FounctionActivity.roomidfc;
                    savemood4.subnetID = bArr[17] & 255;
                    savemood4.deviceID = bArr[18] & 255;
                    savemood4.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                    savemood4.control_type = 3;
                    savemood4.value_1 = this.currentfounction;
                    savemood4.value_2 = 0;
                    savemood4.value_3 = 0;
                    savemood4.value_4 = 0;
                    savemood4.value_5 = 0;
                    savemood4.value_6 = 99;
                    MainActivity.mgr.addmood(savemood4);
                }
                this.albumstate = true;
                this.songOrchannelstate = true;
                this.musicstate = true;
                return;
            }
            return;
        }
        if (i == 9043) {
            if (i2 == 49) {
                if (this.albumstate) {
                    return;
                }
                this.albumstate = true;
                if (this.currentfounction == 1) {
                    this.ablumno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 0), Charsets.Unicode));
                    return;
                }
                return;
            }
            if (i2 == 51 && ((!this.songOrchannelstate) && this.albumstate)) {
                this.songOrchannelstate = true;
                if (this.currentfounction == 1) {
                    this.songno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 1), Charsets.Unicode));
                    Savemood savemood5 = new Savemood();
                    savemood5.room_id = FounctionActivity.roomidfc;
                    savemood5.subnetID = bArr[17] & 255;
                    savemood5.deviceID = bArr[18] & 255;
                    savemood5.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                    savemood5.control_type = 3;
                    savemood5.value_1 = 1;
                    savemood5.value_2 = this.ablumno;
                    savemood5.value_3 = this.songno;
                    savemood5.value_4 = 0;
                    savemood5.value_5 = 0;
                    savemood5.value_6 = 3;
                    MainActivity.mgr.addmood(savemood5);
                    if (this.savezaudiostate.equals("close")) {
                        Savemood savemood6 = new Savemood();
                        savemood6.room_id = FounctionActivity.roomidfc;
                        savemood6.subnetID = bArr[17] & 255;
                        savemood6.deviceID = bArr[18] & 255;
                        savemood6.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                        savemood6.control_type = 3;
                        savemood6.value_1 = 1;
                        savemood6.value_2 = 0;
                        savemood6.value_3 = 0;
                        savemood6.value_4 = 0;
                        savemood6.value_5 = 0;
                        savemood6.value_6 = 99;
                        MainActivity.mgr.addmood(savemood6);
                    }
                } else if (this.currentfounction == 5) {
                    this.channelno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 2), Charsets.Unicode));
                    Savemood savemood7 = new Savemood();
                    savemood7.room_id = FounctionActivity.roomidfc;
                    savemood7.subnetID = bArr[17] & 255;
                    savemood7.deviceID = bArr[18] & 255;
                    savemood7.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                    savemood7.control_type = 3;
                    savemood7.value_1 = this.currentfounction;
                    savemood7.value_2 = 0;
                    savemood7.value_3 = 0;
                    savemood7.value_4 = this.channelno;
                    savemood7.value_5 = 0;
                    savemood7.value_6 = 3;
                    MainActivity.mgr.addmood(savemood7);
                    if (this.savezaudiostate.equals("open")) {
                        Savemood savemood8 = new Savemood();
                        savemood8.room_id = FounctionActivity.roomidfc;
                        savemood8.subnetID = bArr[17] & 255;
                        savemood8.deviceID = bArr[18] & 255;
                        savemood8.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                        savemood8.control_type = 3;
                        savemood8.value_1 = this.currentfounction;
                        savemood8.value_2 = 0;
                        savemood8.value_3 = 0;
                        savemood8.value_4 = 0;
                        savemood8.value_5 = 0;
                        savemood8.value_6 = 4;
                        MainActivity.mgr.addmood(savemood8);
                    } else if (this.savezaudiostate.equals("close")) {
                        Savemood savemood9 = new Savemood();
                        savemood9.room_id = FounctionActivity.roomidfc;
                        savemood9.subnetID = bArr[17] & 255;
                        savemood9.deviceID = bArr[18] & 255;
                        savemood9.mood_id = this.roommoodbutton.get(this.roommoodbutton.size() - 1).mood_id;
                        savemood9.control_type = 3;
                        savemood9.value_1 = this.currentfounction;
                        savemood9.value_2 = 0;
                        savemood9.value_3 = 0;
                        savemood9.value_4 = 0;
                        savemood9.value_5 = 0;
                        savemood9.value_6 = 99;
                        MainActivity.mgr.addmood(savemood9);
                    }
                }
                this.musicstate = true;
            }
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

    public static int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }

    public void setDialogdismiss(boolean z, DialogInterface dialogInterface) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            Field declaredField = dialogInterface.getClass().getSuperclass().getDeclaredField("mShowing");
            declaredField.setAccessible(true);
            declaredField.set(dialogInterface, Boolean.valueOf(z));
            dialogInterface.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(udp_socket.ACTION_DATA_IN);
        intentFilter.addAction(FounctionActivity.ACTION_BACKPRESS);
        intentFilter.addAction(FounctionActivity.ACTION_DELETEMOOD);
        return intentFilter;
    }

    public byte[] getpieceofbyte(byte[] bArr, int i) {
        int i2;
        int i3 = i == 0 ? 49 : i == 1 ? 51 : i == 2 ? 55 : 0;
        if (i != 2) {
            i2 = i3;
            while (i2 < bArr.length) {
                if (bArr[i2] == 0 && bArr[i2 + 1] == 47) {
                    break;
                }
                i2++;
            }
            i2 = 0;
        } else {
            i2 = i3;
            while (i2 < bArr.length) {
                if (bArr[i2] == 0 && bArr[i2 + 1] == 3 && bArr[i2 + 2] == 13) {
                    break;
                }
                i2++;
            }
            i2 = 0;
        }
        int i4 = i2 - i3;
        byte[] bArr2 = new byte[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            bArr2[i5] = bArr[i3 + i5];
        }
        return bArr2;
    }

    public String bytetostring(byte[] bArr, String str) {
        try {
            return new String(bArr, str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
