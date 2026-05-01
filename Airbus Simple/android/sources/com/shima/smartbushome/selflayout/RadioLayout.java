package com.shima.smartbushome.selflayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MusicRadioChannelAdapter;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import com.shima.smartbushome.database.Savemusic;
import com.shima.smartbushome.database.Saveradio;
import com.shima.smartbushome.founction_command.radiocontrol;
import com.shima.smartbushome.founction_view.Music;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class RadioLayout extends RelativeLayout {
    public int AlbumorRadioControl;
    public int ControlSpecSong;
    public int Music_Source;
    public int PlayControl;
    public int PlayModeChange;
    public int VoluneControl;
    List<Saveradio> alldata;
    int channel;
    private AdapterView.OnItemClickListener channelclick;
    List<HashMap<String, String>> channellist;
    private AdapterView.OnItemLongClickListener channellongclick;
    EditText channelname;
    EditText channelvalue;
    Handler getchannel;
    Runnable getchannelrun;
    Handler getdatahandler;
    Runnable getdatarun;
    LayoutInflater inflater;
    boolean initstart;
    public OnItemClickListener itemlick;
    final String[] mItems;
    Button play;
    int playstate;
    ProgressDialog progress;
    MusicRadioChannelAdapter radioadapter;
    ListView radiochannellist;
    radiocontrol rc;
    Context rootcontext;
    SeekBar sb_voice;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    boolean setvolume;
    Runnable showsettingalertrun;
    Handler showsettinghandler;
    Savemusic thismusic;
    List<Saveradio> thisroomradio;
    TextView tv_voicevalue;
    View view;
    private SeekBar.OnSeekBarChangeListener voicechange;

    public RadioLayout(Context context) {
        super(context);
        this.channellist = new ArrayList();
        this.getdatahandler = new Handler();
        this.getchannel = new Handler();
        this.thismusic = new Savemusic();
        this.alldata = new ArrayList();
        this.thisroomradio = new ArrayList();
        this.playstate = 0;
        this.initstart = false;
        this.setvolume = false;
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.voicechange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                RadioLayout.this.tv_voicevalue.setText(seekBar.getProgress() + "%");
                RadioLayout.this.rc.MusicControl((byte) RadioLayout.this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((seekBar.getProgress() * 79) / 100)), (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (RadioLayout.this.setvolume) {
                    RadioLayout.this.tv_voicevalue.setText(i + "%");
                    return;
                }
                RadioLayout.this.tv_voicevalue.setText(i + "%");
            }
        };
        this.channelclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                RadioLayout.this.radioadapter.setSelectItem(i);
                RadioLayout.this.radioadapter.notifyDataSetInvalidated();
                RadioLayout.this.rc.MusicControl((byte) RadioLayout.this.AlbumorRadioControl, (byte) 6, (byte) (i + 1), (byte) 0, (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
            }
        };
        this.mItems = new String[]{"setting", "update all channel"};
        this.channellongclick = new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.4
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                new AlertView("Settings", null, "CANCEL", null, RadioLayout.this.mItems, RadioLayout.this.getContext(), AlertView.Style.Alert, RadioLayout.this.itemlick).show();
                return false;
            }
        };
        this.showsettinghandler = new Handler();
        this.itemlick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.5
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i == -1) {
                    return;
                }
                if (RadioLayout.this.mItems[i].equals("setting")) {
                    RadioLayout.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, RadioLayout.this.rootcontext, AlertView.Style.Alert, RadioLayout.this.settingclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(RadioLayout.this.rootcontext).inflate(R.layout.setting_radioinfo, (ViewGroup) null);
                    RadioLayout.this.channelname = (EditText) viewGroup.findViewById(R.id.etradioname);
                    RadioLayout.this.channelvalue = (EditText) viewGroup.findViewById(R.id.etradiovalue);
                    RadioLayout.this.channelvalue.setHint("87.5MHZ to 108.0MHZ");
                    RadioLayout.this.channelname.setText(RadioLayout.this.alldata.get(RadioLayout.this.radioadapter.getSelectItem()).channel_name.trim());
                    RadioLayout.this.channelvalue.setText(RadioLayout.this.channelvaluetoedit(RadioLayout.this.alldata.get(RadioLayout.this.radioadapter.getSelectItem()).channel_value));
                    RadioLayout.this.settingalter.addExtView(viewGroup);
                    RadioLayout.this.showsettinghandler.postDelayed(RadioLayout.this.showsettingalertrun, 500L);
                    return;
                }
                if (RadioLayout.this.mItems[i].equals("update all channel")) {
                    MainActivity.mgr.deleteradio("radio", FounctionActivity.roomidfc);
                    RadioLayout.this.channellist.clear();
                    RadioLayout.this.getChannelValue();
                }
            }
        };
        this.showsettingalertrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.RadioLayout.6
            @Override // java.lang.Runnable
            public void run() {
                RadioLayout.this.settingalter.show();
                RadioLayout.this.showsettinghandler.removeCallbacks(RadioLayout.this.showsettingalertrun);
            }
        };
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.7
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) throws NumberFormatException, UnsupportedEncodingException {
                if (i == 0) {
                    if (RadioLayout.this.channelvalue.getText().toString().trim().length() > 3) {
                        int i2 = Integer.parseInt(RadioLayout.this.edittochannelvalue(RadioLayout.this.channelvalue.getText().toString().trim()));
                        if (i2 >= 875 && i2 <= 1080) {
                            Saveradio saveradio = new Saveradio();
                            saveradio.room_id = FounctionActivity.roomidfc;
                            saveradio.channel_num = i + 1;
                            saveradio.channel_name = RadioLayout.this.channelname.getText().toString().trim();
                            saveradio.channel_value = RadioLayout.this.edittochannelvalue(RadioLayout.this.channelvalue.getText().toString().trim());
                            MainActivity.mgr.updateradio(saveradio);
                            byte[] bArr = new byte[40];
                            try {
                                byte[] bytes = saveradio.channel_name.trim().getBytes("UnicodeLittleUnmarked");
                                for (int i3 = 0; i3 < bytes.length; i3++) {
                                    bArr[i3] = bytes[i3];
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            RadioLayout.this.rc.WriteChannelValue(saveradio.channel_num, Integer.parseInt(saveradio.channel_value), bArr, (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
                            RadioLayout.this.alldata.clear();
                            RadioLayout.this.channellist.clear();
                            RadioLayout.this.getdatahandler.postDelayed(RadioLayout.this.getdatarun, 50L);
                            return;
                        }
                        Toast.makeText(RadioLayout.this.rootcontext, "channel value out of range", 0).show();
                        return;
                    }
                    Toast.makeText(RadioLayout.this.rootcontext, "you need to add ‘.’ in the edittext", 0).show();
                }
            }
        };
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.RadioLayout.8
            @Override // java.lang.Runnable
            public void run() {
                if (RadioLayout.this.thisroomradio.size() > 0) {
                    RadioLayout.this.thisroomradio.clear();
                }
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < listQuerymusic.size(); i++) {
                    if (listQuerymusic.get(i).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i).music_id == 1) {
                        arrayList.add(listQuerymusic.get(i));
                    }
                }
                if (arrayList.size() > 0) {
                    RadioLayout.this.thismusic = (Savemusic) arrayList.get(0);
                    RadioLayout.this.alldata = MainActivity.mgr.queryradio();
                    for (int i2 = 0; i2 < RadioLayout.this.alldata.size(); i2++) {
                        if (FounctionActivity.roomidfc == RadioLayout.this.alldata.get(i2).room_id) {
                            RadioLayout.this.thisroomradio.add(RadioLayout.this.alldata.get(i2));
                        }
                    }
                    if (RadioLayout.this.thisroomradio.size() > 0) {
                        for (int i3 = 0; i3 < RadioLayout.this.thisroomradio.size(); i3++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("channel", String.valueOf(RadioLayout.this.alldata.get(i3).channel_num));
                            map.put("channelvalue", RadioLayout.this.alldata.get(i3).channel_value);
                            map.put("channelname", "Channel " + RadioLayout.this.alldata.get(i3).channel_num + ": " + RadioLayout.this.alldata.get(i3).channel_name);
                            RadioLayout.this.channellist.add(map);
                        }
                        RadioLayout.this.radioadapter = new MusicRadioChannelAdapter(RadioLayout.this.rootcontext, RadioLayout.this.channellist);
                        RadioLayout.this.radiochannellist.setAdapter((ListAdapter) RadioLayout.this.radioadapter);
                    } else {
                        RadioLayout.this.radioadapter = new MusicRadioChannelAdapter(RadioLayout.this.rootcontext, RadioLayout.this.channellist);
                        RadioLayout.this.radiochannellist.setAdapter((ListAdapter) RadioLayout.this.radioadapter);
                        RadioLayout.this.getChannelValue();
                    }
                    RadioLayout.this.getVoiceValue();
                }
            }
        };
        this.channel = 1;
        this.getchannelrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.RadioLayout.9
            @Override // java.lang.Runnable
            public void run() {
                RadioLayout.this.rc.GetChannelValue(RadioLayout.this.channel, (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
                if (RadioLayout.this.channel == 26) {
                    RadioLayout.this.channel = 0;
                    RadioLayout.this.getchannel.removeCallbacks(RadioLayout.this.getchannelrun);
                    RadioLayout.this.progress.dismiss();
                    RadioLayout.this.getdatahandler.postDelayed(RadioLayout.this.getdatarun, 50L);
                    Toast.makeText(RadioLayout.this.rootcontext, "update succeed", 0).show();
                    return;
                }
                RadioLayout.this.getchannel.postDelayed(RadioLayout.this.getchannelrun, 500L);
            }
        };
    }

    public RadioLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.channellist = new ArrayList();
        this.getdatahandler = new Handler();
        this.getchannel = new Handler();
        this.thismusic = new Savemusic();
        this.alldata = new ArrayList();
        this.thisroomradio = new ArrayList();
        this.playstate = 0;
        this.initstart = false;
        this.setvolume = false;
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.voicechange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                RadioLayout.this.tv_voicevalue.setText(seekBar.getProgress() + "%");
                RadioLayout.this.rc.MusicControl((byte) RadioLayout.this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((seekBar.getProgress() * 79) / 100)), (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (RadioLayout.this.setvolume) {
                    RadioLayout.this.tv_voicevalue.setText(i + "%");
                    return;
                }
                RadioLayout.this.tv_voicevalue.setText(i + "%");
            }
        };
        this.channelclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                RadioLayout.this.radioadapter.setSelectItem(i);
                RadioLayout.this.radioadapter.notifyDataSetInvalidated();
                RadioLayout.this.rc.MusicControl((byte) RadioLayout.this.AlbumorRadioControl, (byte) 6, (byte) (i + 1), (byte) 0, (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
            }
        };
        this.mItems = new String[]{"setting", "update all channel"};
        this.channellongclick = new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.4
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                new AlertView("Settings", null, "CANCEL", null, RadioLayout.this.mItems, RadioLayout.this.getContext(), AlertView.Style.Alert, RadioLayout.this.itemlick).show();
                return false;
            }
        };
        this.showsettinghandler = new Handler();
        this.itemlick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.5
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i == -1) {
                    return;
                }
                if (RadioLayout.this.mItems[i].equals("setting")) {
                    RadioLayout.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, RadioLayout.this.rootcontext, AlertView.Style.Alert, RadioLayout.this.settingclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(RadioLayout.this.rootcontext).inflate(R.layout.setting_radioinfo, (ViewGroup) null);
                    RadioLayout.this.channelname = (EditText) viewGroup.findViewById(R.id.etradioname);
                    RadioLayout.this.channelvalue = (EditText) viewGroup.findViewById(R.id.etradiovalue);
                    RadioLayout.this.channelvalue.setHint("87.5MHZ to 108.0MHZ");
                    RadioLayout.this.channelname.setText(RadioLayout.this.alldata.get(RadioLayout.this.radioadapter.getSelectItem()).channel_name.trim());
                    RadioLayout.this.channelvalue.setText(RadioLayout.this.channelvaluetoedit(RadioLayout.this.alldata.get(RadioLayout.this.radioadapter.getSelectItem()).channel_value));
                    RadioLayout.this.settingalter.addExtView(viewGroup);
                    RadioLayout.this.showsettinghandler.postDelayed(RadioLayout.this.showsettingalertrun, 500L);
                    return;
                }
                if (RadioLayout.this.mItems[i].equals("update all channel")) {
                    MainActivity.mgr.deleteradio("radio", FounctionActivity.roomidfc);
                    RadioLayout.this.channellist.clear();
                    RadioLayout.this.getChannelValue();
                }
            }
        };
        this.showsettingalertrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.RadioLayout.6
            @Override // java.lang.Runnable
            public void run() {
                RadioLayout.this.settingalter.show();
                RadioLayout.this.showsettinghandler.removeCallbacks(RadioLayout.this.showsettingalertrun);
            }
        };
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.7
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) throws NumberFormatException, UnsupportedEncodingException {
                if (i == 0) {
                    if (RadioLayout.this.channelvalue.getText().toString().trim().length() > 3) {
                        int i2 = Integer.parseInt(RadioLayout.this.edittochannelvalue(RadioLayout.this.channelvalue.getText().toString().trim()));
                        if (i2 >= 875 && i2 <= 1080) {
                            Saveradio saveradio = new Saveradio();
                            saveradio.room_id = FounctionActivity.roomidfc;
                            saveradio.channel_num = i + 1;
                            saveradio.channel_name = RadioLayout.this.channelname.getText().toString().trim();
                            saveradio.channel_value = RadioLayout.this.edittochannelvalue(RadioLayout.this.channelvalue.getText().toString().trim());
                            MainActivity.mgr.updateradio(saveradio);
                            byte[] bArr = new byte[40];
                            try {
                                byte[] bytes = saveradio.channel_name.trim().getBytes("UnicodeLittleUnmarked");
                                for (int i3 = 0; i3 < bytes.length; i3++) {
                                    bArr[i3] = bytes[i3];
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            RadioLayout.this.rc.WriteChannelValue(saveradio.channel_num, Integer.parseInt(saveradio.channel_value), bArr, (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
                            RadioLayout.this.alldata.clear();
                            RadioLayout.this.channellist.clear();
                            RadioLayout.this.getdatahandler.postDelayed(RadioLayout.this.getdatarun, 50L);
                            return;
                        }
                        Toast.makeText(RadioLayout.this.rootcontext, "channel value out of range", 0).show();
                        return;
                    }
                    Toast.makeText(RadioLayout.this.rootcontext, "you need to add ‘.’ in the edittext", 0).show();
                }
            }
        };
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.RadioLayout.8
            @Override // java.lang.Runnable
            public void run() {
                if (RadioLayout.this.thisroomradio.size() > 0) {
                    RadioLayout.this.thisroomradio.clear();
                }
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < listQuerymusic.size(); i++) {
                    if (listQuerymusic.get(i).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i).music_id == 1) {
                        arrayList.add(listQuerymusic.get(i));
                    }
                }
                if (arrayList.size() > 0) {
                    RadioLayout.this.thismusic = (Savemusic) arrayList.get(0);
                    RadioLayout.this.alldata = MainActivity.mgr.queryradio();
                    for (int i2 = 0; i2 < RadioLayout.this.alldata.size(); i2++) {
                        if (FounctionActivity.roomidfc == RadioLayout.this.alldata.get(i2).room_id) {
                            RadioLayout.this.thisroomradio.add(RadioLayout.this.alldata.get(i2));
                        }
                    }
                    if (RadioLayout.this.thisroomradio.size() > 0) {
                        for (int i3 = 0; i3 < RadioLayout.this.thisroomradio.size(); i3++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("channel", String.valueOf(RadioLayout.this.alldata.get(i3).channel_num));
                            map.put("channelvalue", RadioLayout.this.alldata.get(i3).channel_value);
                            map.put("channelname", "Channel " + RadioLayout.this.alldata.get(i3).channel_num + ": " + RadioLayout.this.alldata.get(i3).channel_name);
                            RadioLayout.this.channellist.add(map);
                        }
                        RadioLayout.this.radioadapter = new MusicRadioChannelAdapter(RadioLayout.this.rootcontext, RadioLayout.this.channellist);
                        RadioLayout.this.radiochannellist.setAdapter((ListAdapter) RadioLayout.this.radioadapter);
                    } else {
                        RadioLayout.this.radioadapter = new MusicRadioChannelAdapter(RadioLayout.this.rootcontext, RadioLayout.this.channellist);
                        RadioLayout.this.radiochannellist.setAdapter((ListAdapter) RadioLayout.this.radioadapter);
                        RadioLayout.this.getChannelValue();
                    }
                    RadioLayout.this.getVoiceValue();
                }
            }
        };
        this.channel = 1;
        this.getchannelrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.RadioLayout.9
            @Override // java.lang.Runnable
            public void run() {
                RadioLayout.this.rc.GetChannelValue(RadioLayout.this.channel, (byte) RadioLayout.this.thismusic.subnetID, (byte) RadioLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
                if (RadioLayout.this.channel == 26) {
                    RadioLayout.this.channel = 0;
                    RadioLayout.this.getchannel.removeCallbacks(RadioLayout.this.getchannelrun);
                    RadioLayout.this.progress.dismiss();
                    RadioLayout.this.getdatahandler.postDelayed(RadioLayout.this.getdatarun, 50L);
                    Toast.makeText(RadioLayout.this.rootcontext, "update succeed", 0).show();
                    return;
                }
                RadioLayout.this.getchannel.postDelayed(RadioLayout.this.getchannelrun, 500L);
            }
        };
    }

    public boolean ifinitstarted() {
        return this.initstart;
    }

    public void initview(Context context) {
        this.rootcontext = context;
        this.initstart = true;
        this.view = View.inflate(context, R.layout.music_radio_layout, this);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.radiochannellist = (ListView) this.view.findViewById(R.id.listView2);
        this.tv_voicevalue = (TextView) this.view.findViewById(R.id.textView5);
        this.radiochannellist.setOnItemClickListener(this.channelclick);
        this.radiochannellist.setOnItemLongClickListener(this.channellongclick);
        this.play = (Button) this.view.findViewById(R.id.checkBox3);
        this.sb_voice = (SeekBar) this.view.findViewById(R.id.seekBar4);
        this.sb_voice.setOnSeekBarChangeListener(this.voicechange);
        this.play.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.RadioLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                switch (RadioLayout.this.playstate) {
                    case 0:
                        RadioLayout.this.setplaybuttonstate("play");
                        break;
                    case 1:
                        RadioLayout.this.setplaybuttonstate("pause");
                        break;
                }
            }
        });
        this.rc = new radiocontrol();
        this.progress = new ProgressDialog(this.rootcontext);
        this.progress.setCancelable(true);
        this.progress.setCanceledOnTouchOutside(false);
        this.progress.setMessage("Getting Radio Channel info...");
        this.progress.setProgressStyle(1);
        this.getdatahandler.postDelayed(this.getdatarun, 50L);
    }

    public void setcontent(Savemusic savemusic) {
        this.thismusic = savemusic;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setplaybuttonstate(java.lang.String r13) {
        /*
            r12 = this;
            int r0 = r13.hashCode()
            r1 = 3443508(0x348b34, float:4.825382E-39)
            r2 = 0
            r3 = 1
            if (r0 == r1) goto L1b
            r1 = 106440182(0x65825f6, float:4.0652974E-35)
            if (r0 == r1) goto L11
            goto L25
        L11:
            java.lang.String r0 = "pause"
            boolean r13 = r13.equals(r0)
            if (r13 == 0) goto L25
            r13 = 1
            goto L26
        L1b:
            java.lang.String r0 = "play"
            boolean r13 = r13.equals(r0)
            if (r13 == 0) goto L25
            r13 = 0
            goto L26
        L25:
            r13 = -1
        L26:
            switch(r13) {
                case 0: goto L4c;
                case 1: goto L2a;
                default: goto L29;
            }
        L29:
            goto L6d
        L2a:
            android.widget.Button r13 = r12.play
            r0 = 2131165562(0x7f07017a, float:1.7945345E38)
            r13.setBackgroundResource(r0)
            r12.playstate = r2
            com.shima.smartbushome.founction_command.radiocontrol r3 = r12.rc
            int r13 = r12.PlayControl
            byte r4 = (byte) r13
            r5 = 4
            r6 = 0
            r7 = 0
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.subnetID
            byte r8 = (byte) r13
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.deviceID
            byte r9 = (byte) r13
            com.shima.smartbushome.udp.udp_socket r10 = com.shima.smartbushome.MainActivity.mydupsocket
            r3.MusicControl(r4, r5, r6, r7, r8, r9, r10)
            goto L6d
        L4c:
            android.widget.Button r13 = r12.play
            r0 = 2131165558(0x7f070176, float:1.7945337E38)
            r13.setBackgroundResource(r0)
            r12.playstate = r3
            com.shima.smartbushome.founction_command.radiocontrol r4 = r12.rc
            int r13 = r12.PlayControl
            byte r5 = (byte) r13
            r6 = 3
            r7 = 0
            r8 = 0
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.subnetID
            byte r9 = (byte) r13
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.deviceID
            byte r10 = (byte) r13
            com.shima.smartbushome.udp.udp_socket r11 = com.shima.smartbushome.MainActivity.mydupsocket
            r4.MusicControl(r5, r6, r7, r8, r9, r10, r11)
        L6d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.RadioLayout.setplaybuttonstate(java.lang.String):void");
    }

    public void getVoiceValue() {
        this.rc.GetMusicState((byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
    }

    public void getChannelValue() {
        this.progress.show();
        this.getchannel.postDelayed(this.getchannelrun, 100L);
    }

    public void SwitchtoRadio() {
        this.rc.SwitchtoRadio((byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
    }

    public void setvoicevalue(byte[] bArr) {
        int i = (bArr[25] << 8) + (bArr[26] & 255);
        byte b = bArr[36];
        if (i == 9050) {
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
            int i2 = 100 - ((Integer.parseInt(bytetostring(bArr2, "ascii")) * 100) / 79);
            this.setvolume = true;
            this.sb_voice.setProgress(i2);
            this.setvolume = false;
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

    public String channelvalue(byte b, byte b2) {
        return String.valueOf(((b & 255) << 8) + (b2 & 255));
    }

    public String edittochannelvalue(String str) {
        return str.substring(0, str.lastIndexOf(FileUtil.FILE_EXTENSION_SEPARATOR)) + str.substring(str.lastIndexOf(FileUtil.FILE_EXTENSION_SEPARATOR) + 1, str.length());
    }

    public String channelvaluetoedit(String str) {
        return str.substring(0, str.length() - 1) + FileUtil.FILE_EXTENSION_SEPARATOR + str.substring(str.length() - 1, str.length());
    }

    public String bytetostring(byte[] bArr, String str) {
        try {
            return new String(bArr, str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void receiveddata(byte[] bArr) {
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (bArr[17] == ((byte) this.thismusic.subnetID) && bArr[18] == ((byte) this.thismusic.deviceID)) {
            if (i != 6447) {
                if (i == 57665 && (bArr[26] & 255) == this.channel) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("channel", String.valueOf(bArr[26] & 255));
                    map.put("channelvalue", channelvalue(bArr[27], bArr[28]));
                    byte[] bArr2 = new byte[40];
                    for (int i2 = 0; i2 < 40; i2++) {
                        bArr2[i2] = bArr[i2 + 29];
                    }
                    map.put("channelname", "Channel " + (bArr[26] & 255) + ": " + bytetostring(bArr2, "UnicodeLittleUnmarked"));
                    if (this.channellist.contains(map)) {
                        return;
                    }
                    this.channellist.add(map);
                    this.radioadapter.addDevice(map);
                    this.radioadapter.notifyDataSetChanged();
                    Saveradio saveradio = new Saveradio();
                    saveradio.room_id = FounctionActivity.roomidfc;
                    saveradio.channel_num = bArr[26] & 255;
                    saveradio.channel_value = channelvalue(bArr[27], bArr[28]);
                    saveradio.channel_name = bytetostring(bArr2, "UnicodeLittleUnmarked");
                    MainActivity.mgr.addradio(saveradio);
                    this.channel++;
                    this.progress.setProgress(this.channel * 4);
                    return;
                }
                return;
            }
            setvoicevalue(bArr);
        }
    }

    public void radiobackpress() {
        Music.finishmusic();
    }
}
