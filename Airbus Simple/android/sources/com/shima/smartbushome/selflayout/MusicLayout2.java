package com.shima.smartbushome.selflayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.litesuits.http.data.Charsets;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MusicAlbumAdapter;
import com.shima.smartbushome.assist.Adapter.MusicFolderAdapter;
import com.shima.smartbushome.assist.Adapter.MusicOptionAdapter;
import com.shima.smartbushome.assist.Adapter.MusicSongAdapter;
import com.shima.smartbushome.assist.AtoZlist.AtoZAdapter;
import com.shima.smartbushome.assist.AtoZlist.CharacterParser;
import com.shima.smartbushome.assist.AtoZlist.PinyinComparator;
import com.shima.smartbushome.assist.AtoZlist.SideBar;
import com.shima.smartbushome.assist.AtoZlist.SortModel;
import com.shima.smartbushome.assist.MusicNotification;
import com.shima.smartbushome.assist.MusicNotifyReceiver;
import com.shima.smartbushome.assist.rollingTextview;
import com.shima.smartbushome.database.Savemusic;
import com.shima.smartbushome.database.Savesong;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.founction_view.Music;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class MusicLayout2 extends RelativeLayout implements View.OnClickListener {
    public int AlbumorRadioControl;
    public int ControlSpecSong;
    public int Music_Source;
    public int PlayControl;
    public int PlayModeChange;
    private List<SortModel> SourceDateList;
    public int VoluneControl;
    int adapternum;
    List<String> albumlist;
    int albumno;
    AtoZAdapter allsongadapter;
    int bg_position;
    int bigpackagesmax;
    Button bt_back;
    Button bt_next;
    Button bt_play;
    Button bt_playmode;
    Button bt_voice;
    CheckBox cb_like;
    private CharacterParser characterParser;
    MusicSongAdapter currentSongadapter;
    int currentbigpackages;
    List<Savesong> data;
    TextView dialog;
    boolean doing;
    boolean feedback;
    MusicFolderAdapter filefolderadapter;
    private AdapterView.OnItemLongClickListener folderlongclick;
    MusicFolderAdapter foldersongadapter;
    int foroldvoice;
    Handler getdatahandler;
    Runnable getdatarun;
    boolean getreflashablum;
    boolean getreflashsong;
    boolean getreflashvoice;
    Runnable getselectmusic;
    AlertView iconalter;
    private CompoundButton.OnCheckedChangeListener likeclick;
    MusicSongAdapter likesongadapter;
    boolean listcontrol;
    musiccontrol mc;
    MusicOptionAdapter menuadapter;
    private AdapterView.OnItemClickListener menuclick;
    GridView menugridview;
    List<String> menulist;
    LayoutInflater musicinflater;
    private PinyinComparator pinyinComparator;
    int playmodecount;
    int playstate;
    private ProgressDialog progress;
    boolean readabd;
    boolean readsongbp;
    boolean readsongdata;
    int recalbumno;
    Handler receivehandler;
    int recsongno;
    int reflashcount;
    Handler reflashhandler;
    Runnable reflashrun;
    boolean reflashstep;
    boolean reflashuienable;
    private SideBar.OnTouchingLetterChangedListener righttouch;
    Context rootcontext;
    List<Savesong> savesongdata;
    SeekBar sb_setvoice;
    public OnItemClickListener selectbg;
    Savesong selectedsong;
    boolean setabp;
    boolean setlikestate;
    boolean setvoicestate;
    SideBar sideBar;
    List<MusicSongAdapter> songadapter;
    private AdapterView.OnItemClickListener songclick;
    List<HashMap<Integer, String>> songdetailhash;
    ListView songlistview;
    String songmaxtime;
    List<HashMap<String, Integer>> songnumhash;
    int songpackno;
    Savemusic thismusic;
    rollingTextview tv_songname;
    Runnable updatarun;
    public OnItemClickListener updateclick;
    int updatestep;
    View view;
    private SeekBar.OnSeekBarChangeListener voicechange;
    int waitfeedback;
    Runnable writedata;
    Handler writehandler;
    int writenum;
    boolean writesongdata;

    public void initrollingtext() {
    }

    public MusicLayout2(Context context) {
        super(context);
        this.foroldvoice = 0;
        this.recalbumno = 0;
        this.recsongno = 0;
        this.adapternum = 0;
        this.albumno = 1;
        this.songpackno = 1;
        this.updatestep = 0;
        this.bigpackagesmax = 0;
        this.currentbigpackages = 1;
        this.doing = true;
        this.setlikestate = false;
        this.setvoicestate = false;
        this.listcontrol = false;
        this.playmodecount = 0;
        this.playstate = 0;
        this.getdatahandler = new Handler();
        this.writehandler = new Handler();
        this.receivehandler = new Handler();
        this.reflashhandler = new Handler();
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.thismusic = new Savemusic();
        this.selectedsong = new Savesong();
        this.data = new ArrayList();
        this.menulist = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.1
            {
                add("Album");
                add("All Songs");
                add("FAVORITE");
                add("Update List");
                add("File Managerment");
            }
        };
        this.albumlist = new ArrayList();
        this.savesongdata = new ArrayList();
        this.songnumhash = new ArrayList();
        this.songdetailhash = new ArrayList();
        this.songadapter = new ArrayList();
        this.reflashuienable = false;
        this.setabp = false;
        this.readabd = false;
        this.readsongbp = false;
        this.readsongdata = false;
        this.writesongdata = false;
        this.righttouch = new SideBar.OnTouchingLetterChangedListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.2
            @Override // com.shima.smartbushome.assist.AtoZlist.SideBar.OnTouchingLetterChangedListener
            public void onTouchingLetterChanged(String str) {
                int positionForSection = MusicLayout2.this.allsongadapter.getPositionForSection(str.charAt(0));
                if (positionForSection != -1) {
                    MusicLayout2.this.songlistview.setSelection(positionForSection);
                }
            }
        };
        this.likeclick = new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (MusicLayout2.this.tv_songname.getText().toString().equals("song name")) {
                    Toast.makeText(MusicLayout2.this.getContext(), "you need to select one song", 0).show();
                    return;
                }
                if (MusicLayout2.this.setlikestate) {
                    return;
                }
                if (z) {
                    MusicLayout2.this.selectedsong.like = 1;
                    MainActivity.mgr.updatesong(MusicLayout2.this.selectedsong);
                    Toast.makeText(MusicLayout2.this.getContext(), "ADDED", 0).show();
                } else {
                    MusicLayout2.this.selectedsong.like = 0;
                    MainActivity.mgr.updatesong(MusicLayout2.this.selectedsong);
                    Toast.makeText(MusicLayout2.this.getContext(), "DELETED", 0).show();
                }
            }
        };
        this.bg_position = 0;
        this.menuclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (MusicLayout2.this.adapternum == 0) {
                    switch (i) {
                        case 0:
                            MusicLayout2.this.menugridview.setAdapter((ListAdapter) new MusicAlbumAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.albumlist));
                            MusicLayout2.this.adapternum = 1;
                            MusicLayout2.this.listcontrol = false;
                            break;
                        case 1:
                            MusicLayout2.this.adapternum = 3;
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.sideBar.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.allsongadapter);
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 2:
                            MusicLayout2.this.adapternum = 4;
                            ArrayList arrayList = new ArrayList();
                            for (int i2 = 0; i2 < MusicLayout2.this.data.size(); i2++) {
                                if (MusicLayout2.this.data.get(i2).like == 1) {
                                    arrayList.add(MusicLayout2.this.data.get(i2).song_name);
                                }
                            }
                            MusicLayout2.this.likesongadapter = new MusicSongAdapter(MusicLayout2.this.getContext(), arrayList);
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.likesongadapter);
                            MusicLayout2.this.currentSongadapter = MusicLayout2.this.likesongadapter;
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 3:
                            MusicLayout2.this.showPopupMenu(view);
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 4:
                            MusicLayout2.this.filefolderadapter = new MusicFolderAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.albumlist, 1);
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.filefolderadapter);
                            MusicLayout2.this.adapternum = 5;
                            MusicLayout2.this.listcontrol = false;
                            break;
                    }
                }
                if (MusicLayout2.this.adapternum == 1) {
                    MusicLayout2.this.menugridview.setVisibility(8);
                    MusicLayout2.this.songlistview.setVisibility(0);
                    MusicLayout2.this.adapternum = 2;
                    MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.songadapter.get(i));
                    MusicLayout2.this.currentSongadapter = MusicLayout2.this.songadapter.get(i);
                    MusicLayout2.this.listcontrol = true;
                }
            }
        };
        this.selectbg = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.5
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i != 0) {
                    return;
                }
                MusicLayout2.this.savecolorInfo(MusicLayout2.this.getContext(), "music_bg" + MusicLayout2.this.bg_position);
            }
        };
        this.songclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.6
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                switch (MusicLayout2.this.adapternum) {
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                        MusicLayout2.this.currentSongadapter.setSelectItem(i);
                        MusicLayout2.this.currentSongadapter.notifyDataSetInvalidated();
                        for (int i2 = 0; i2 < MusicLayout2.this.data.size(); i2++) {
                            if (MusicLayout2.this.currentSongadapter.getselectSongname().equals(MusicLayout2.this.data.get(i2).song_name)) {
                                MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i2);
                            }
                        }
                        byte[] bArr = {(byte) ((MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (MusicLayout2.this.selectedsong.song_num - (MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK))};
                        MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.ControlSpecSong, (byte) MusicLayout2.this.selectedsong.album_num, bArr[0], bArr[1], (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                        MusicLayout2.this.setplaybuttonstate("play");
                        break;
                    case 3:
                        MusicLayout2.this.allsongadapter.setSelectItem(i);
                        MusicLayout2.this.allsongadapter.notifyDataSetInvalidated();
                        for (int i3 = 0; i3 < MusicLayout2.this.data.size(); i3++) {
                            if (MusicLayout2.this.allsongadapter.getselectSongname().equals(MusicLayout2.this.data.get(i3).song_name)) {
                                MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i3);
                            }
                        }
                        byte[] bArr2 = {(byte) ((MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (MusicLayout2.this.selectedsong.song_num - (MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK))};
                        MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.ControlSpecSong, (byte) MusicLayout2.this.selectedsong.album_num, bArr2[0], bArr2[1], (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                        MusicLayout2.this.setplaybuttonstate("play");
                        break;
                    case 5:
                        MusicLayout2.this.foldersongadapter = new MusicFolderAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.songadapter.get(i).getsonglist(), 2);
                        MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.foldersongadapter);
                        MusicLayout2.this.listcontrol = true;
                        MusicLayout2.this.adapternum = 6;
                        break;
                }
            }
        };
        this.folderlongclick = new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.7
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                switch (MusicLayout2.this.adapternum) {
                    case 5:
                        MusicLayout2.this.showPopupfolderMenu(view);
                        break;
                    case 6:
                        MusicLayout2.this.showPopupmp3Menu(view);
                        break;
                }
                return true;
            }
        };
        this.voicechange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.8
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicLayout2.this.listcontrol = true;
                MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((seekBar.getProgress() * 79) / 100)), (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i == 0) {
                    MusicLayout2.this.bt_voice.setBackgroundResource(R.drawable.btn_voice_none);
                } else {
                    MusicLayout2.this.bt_voice.setBackgroundResource(R.drawable.btn_voice_normal);
                }
                boolean z2 = MusicLayout2.this.setvoicestate;
            }
        };
        this.updateclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.9
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i != -1 && i == 0) {
                    if (MusicLayout2.this.albumlist.size() != 0) {
                        MusicLayout2.this.albumlist.clear();
                    }
                    if (MusicLayout2.this.songnumhash.size() != 0) {
                        MusicLayout2.this.songnumhash.clear();
                    }
                    if (MusicLayout2.this.songdetailhash.size() != 0) {
                        MusicLayout2.this.songdetailhash.clear();
                    }
                    MusicLayout2.this.albumno = 1;
                    MusicLayout2.this.songpackno = 1;
                    MusicLayout2.this.updatestep = 0;
                    MusicLayout2.this.bigpackagesmax = 0;
                    MusicLayout2.this.currentbigpackages = 1;
                    MusicLayout2.this.setabp = false;
                    MusicLayout2.this.readabd = false;
                    MusicLayout2.this.readsongbp = false;
                    MusicLayout2.this.readsongdata = false;
                    MusicLayout2.this.writesongdata = false;
                    MusicLayout2.this.progress = new ProgressDialog(MusicLayout2.this.getContext());
                    MusicLayout2.this.progress.setCancelable(true);
                    MusicLayout2.this.progress.setCanceledOnTouchOutside(false);
                    MusicLayout2.this.progress.setMessage("Getting Music...");
                    MusicLayout2.this.progress.setProgressStyle(1);
                    MusicLayout2.this.progress.show();
                    if (MusicLayout2.this.data.size() != 0) {
                        MainActivity.mgr.deletesong("song", FounctionActivity.roomidfc);
                    }
                    MusicLayout2.this.updatestep = 1;
                    MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 10L);
                }
            }
        };
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.13
            @Override // java.lang.Runnable
            public void run() {
                List<Savesong> listQuerysong = MainActivity.mgr.querysong();
                if (MusicLayout2.this.data.size() > 0) {
                    MusicLayout2.this.data.clear();
                }
                for (int i = 0; i < listQuerysong.size(); i++) {
                    if (listQuerysong.get(i).room_id == FounctionActivity.roomidfc) {
                        MusicLayout2.this.data.add(listQuerysong.get(i));
                    }
                }
                if (MusicLayout2.this.data.size() > 0) {
                    if (MusicLayout2.this.songadapter.size() > 0) {
                        MusicLayout2.this.songadapter.clear();
                    }
                    if (MusicLayout2.this.albumlist.size() > 0) {
                        MusicLayout2.this.albumlist.clear();
                    }
                    int i2 = MusicLayout2.this.data.get(MusicLayout2.this.data.size() - 1).album_num;
                    int i3 = 0;
                    int i4 = 1;
                    while (i3 < i2) {
                        ArrayList arrayList = new ArrayList();
                        int i5 = i4;
                        for (int i6 = 0; i6 < MusicLayout2.this.data.size(); i6++) {
                            if (MusicLayout2.this.data.get(i6).album_num == i3 + 1) {
                                arrayList.add(MusicLayout2.this.data.get(i6).song_name);
                            }
                            if (MusicLayout2.this.data.get(i6).album_num == i5) {
                                MusicLayout2.this.albumlist.add(MusicLayout2.this.data.get(i6).album_name);
                                i5++;
                            }
                        }
                        MusicLayout2.this.songadapter.add(new MusicSongAdapter(MusicLayout2.this.getContext(), arrayList));
                        i3++;
                        i4 = i5;
                    }
                }
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList2 = new ArrayList();
                for (int i7 = 0; i7 < listQuerymusic.size(); i7++) {
                    if (listQuerymusic.get(i7).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i7).music_id == 1) {
                        arrayList2.add(listQuerymusic.get(i7));
                    }
                }
                if (arrayList2.size() > 0) {
                    MusicLayout2.this.thismusic = (Savemusic) arrayList2.get(0);
                    String[] strArr = new String[MusicLayout2.this.data.size()];
                    for (int i8 = 0; i8 < MusicLayout2.this.data.size(); i8++) {
                        strArr[i8] = MusicLayout2.this.data.get(i8).song_name;
                    }
                    MusicLayout2.this.SourceDateList = MusicLayout2.this.filledData(strArr);
                    Collections.sort(MusicLayout2.this.SourceDateList, MusicLayout2.this.pinyinComparator);
                    MusicLayout2.this.allsongadapter = new AtoZAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.SourceDateList);
                    MusicLayout2.this.reflashui();
                }
                MusicLayout2.this.initrollingtext();
            }
        };
        this.getselectmusic = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.14
            @Override // java.lang.Runnable
            public void run() {
                int i = 0;
                while (true) {
                    if (i < MusicLayout2.this.data.size()) {
                        if (MusicLayout2.this.data.get(i).album_num == MusicLayout2.this.recalbumno && MusicLayout2.this.data.get(i).song_num == MusicLayout2.this.recsongno) {
                            MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i);
                            MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                            MusicNotifyReceiver.subnetID = MusicLayout2.this.thismusic.subnetID;
                            MusicNotifyReceiver.deviceID = MusicLayout2.this.thismusic.deviceID;
                            MusicNotification.sendResidentNoticeType0(MainActivity.maincontext, MusicLayout2.this.selectedsong.song_name);
                            MusicLayout2.this.reflashstep = true;
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                MusicLayout2.this.receivehandler.removeCallbacks(MusicLayout2.this.getselectmusic);
            }
        };
        this.reflashstep = false;
        this.getreflashablum = false;
        this.getreflashsong = false;
        this.getreflashvoice = false;
        this.reflashcount = 0;
        this.reflashrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.15
            @Override // java.lang.Runnable
            public void run() {
                if (MusicLayout2.this.reflashstep) {
                    MusicLayout2.this.reflashuienable = false;
                    MusicLayout2.this.reflashcount = 0;
                    MusicLayout2.this.reflashhandler.removeCallbacks(MusicLayout2.this.reflashrun);
                    return;
                }
                MusicLayout2.this.mc.GetMusicState((byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                MusicLayout2.this.reflashhandler.postDelayed(MusicLayout2.this.reflashrun, 300L);
                MusicLayout2.this.reflashcount++;
                if (MusicLayout2.this.reflashcount >= 1) {
                    MusicLayout2.this.reflashstep = true;
                }
            }
        };
        this.waitfeedback = 0;
        this.feedback = false;
        this.updatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.16
            @Override // java.lang.Runnable
            public void run() {
                MusicLayout2.this.waitfeedback++;
                if (MusicLayout2.this.waitfeedback > 10 && !MusicLayout2.this.feedback) {
                    MusicLayout2.this.albumlist.clear();
                    MusicLayout2.this.songnumhash.clear();
                    MusicLayout2.this.songdetailhash.clear();
                    MusicLayout2.this.savesongdata.clear();
                    MusicLayout2.this.albumno = 1;
                    MusicLayout2.this.songpackno = 1;
                    MusicLayout2.this.updatestep = 0;
                    MusicLayout2.this.bigpackagesmax = 0;
                    MusicLayout2.this.currentbigpackages = 1;
                    MusicLayout2.this.feedback = false;
                    MusicLayout2.this.waitfeedback = 0;
                    MusicLayout2.this.progress.dismiss();
                    Toast.makeText(MusicLayout2.this.getContext(), "update fail", 0).show();
                    MusicLayout2.this.getdatahandler.removeCallbacks(MusicLayout2.this.updatarun);
                }
                switch (MusicLayout2.this.updatestep) {
                    case 1:
                        MusicLayout2.this.mc.MusicReadAlbumQTY((byte) 1, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 2:
                        MusicLayout2.this.feedback = true;
                        MusicLayout2.this.mc.MusicReadAlbum((byte) 1, (byte) MusicLayout2.this.currentbigpackages, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 3:
                        MusicLayout2.this.mc.MusicReadSongQTY((byte) 1, (byte) MusicLayout2.this.albumno, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 4:
                        MusicLayout2.this.mc.MusicReadSong((byte) 1, (byte) MusicLayout2.this.albumno, (byte) MusicLayout2.this.songpackno, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 5:
                        if (!MusicLayout2.this.writesongdata) {
                            MusicLayout2.this.writesongdata = true;
                            MusicLayout2.this.progress.setProgress(80);
                            for (int i = 0; i < MusicLayout2.this.songdetailhash.size(); i++) {
                                Savesong savesong = new Savesong();
                                savesong.room_id = FounctionActivity.roomidfc;
                                savesong.album_num = Integer.parseInt(MusicLayout2.this.songdetailhash.get(i).get(1));
                                savesong.album_name = MusicLayout2.this.albumlist.get(Integer.parseInt(MusicLayout2.this.songdetailhash.get(i).get(1)) - 1);
                                savesong.song_num = Integer.parseInt(MusicLayout2.this.songdetailhash.get(i).get(2));
                                savesong.song_name = MusicLayout2.this.songdetailhash.get(i).get(3);
                                savesong.like = 0;
                                savesong.music_id = MusicLayout2.this.thismusic.music_id;
                                MusicLayout2.this.savesongdata.add(savesong);
                            }
                            MusicLayout2.this.writenum = 0;
                            MusicLayout2.this.writehandler.postDelayed(MusicLayout2.this.writedata, 50L);
                            MusicLayout2.this.getdatahandler.removeCallbacks(MusicLayout2.this.updatarun);
                            break;
                        }
                        break;
                }
            }
        };
        this.writenum = 0;
        this.writedata = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.17
            @Override // java.lang.Runnable
            public void run() {
                if (MusicLayout2.this.writenum < MusicLayout2.this.savesongdata.size()) {
                    MainActivity.mgr.addsong(MusicLayout2.this.savesongdata.get(MusicLayout2.this.writenum));
                    MusicLayout2.this.writenum++;
                    MusicLayout2.this.progress.setProgress(((MusicLayout2.this.writenum * 20) / MusicLayout2.this.savesongdata.size()) + 80);
                    MusicLayout2.this.writehandler.postDelayed(MusicLayout2.this.writedata, 100L);
                    return;
                }
                MusicLayout2.this.albumlist.clear();
                MusicLayout2.this.songnumhash.clear();
                MusicLayout2.this.songdetailhash.clear();
                MusicLayout2.this.savesongdata.clear();
                MusicLayout2.this.albumno = 1;
                MusicLayout2.this.songpackno = 1;
                MusicLayout2.this.updatestep = 0;
                MusicLayout2.this.bigpackagesmax = 0;
                MusicLayout2.this.currentbigpackages = 1;
                MusicLayout2.this.feedback = false;
                MusicLayout2.this.waitfeedback = 0;
                MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.getdatarun, 100L);
                MusicLayout2.this.progress.dismiss();
                Toast.makeText(MusicLayout2.this.getContext(), "update succeed", 0).show();
                MusicLayout2.this.writehandler.removeCallbacks(MusicLayout2.this.writedata);
            }
        };
        init(context);
    }

    public MusicLayout2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.foroldvoice = 0;
        this.recalbumno = 0;
        this.recsongno = 0;
        this.adapternum = 0;
        this.albumno = 1;
        this.songpackno = 1;
        this.updatestep = 0;
        this.bigpackagesmax = 0;
        this.currentbigpackages = 1;
        this.doing = true;
        this.setlikestate = false;
        this.setvoicestate = false;
        this.listcontrol = false;
        this.playmodecount = 0;
        this.playstate = 0;
        this.getdatahandler = new Handler();
        this.writehandler = new Handler();
        this.receivehandler = new Handler();
        this.reflashhandler = new Handler();
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.thismusic = new Savemusic();
        this.selectedsong = new Savesong();
        this.data = new ArrayList();
        this.menulist = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.1
            {
                add("Album");
                add("All Songs");
                add("FAVORITE");
                add("Update List");
                add("File Managerment");
            }
        };
        this.albumlist = new ArrayList();
        this.savesongdata = new ArrayList();
        this.songnumhash = new ArrayList();
        this.songdetailhash = new ArrayList();
        this.songadapter = new ArrayList();
        this.reflashuienable = false;
        this.setabp = false;
        this.readabd = false;
        this.readsongbp = false;
        this.readsongdata = false;
        this.writesongdata = false;
        this.righttouch = new SideBar.OnTouchingLetterChangedListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.2
            @Override // com.shima.smartbushome.assist.AtoZlist.SideBar.OnTouchingLetterChangedListener
            public void onTouchingLetterChanged(String str) {
                int positionForSection = MusicLayout2.this.allsongadapter.getPositionForSection(str.charAt(0));
                if (positionForSection != -1) {
                    MusicLayout2.this.songlistview.setSelection(positionForSection);
                }
            }
        };
        this.likeclick = new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (MusicLayout2.this.tv_songname.getText().toString().equals("song name")) {
                    Toast.makeText(MusicLayout2.this.getContext(), "you need to select one song", 0).show();
                    return;
                }
                if (MusicLayout2.this.setlikestate) {
                    return;
                }
                if (z) {
                    MusicLayout2.this.selectedsong.like = 1;
                    MainActivity.mgr.updatesong(MusicLayout2.this.selectedsong);
                    Toast.makeText(MusicLayout2.this.getContext(), "ADDED", 0).show();
                } else {
                    MusicLayout2.this.selectedsong.like = 0;
                    MainActivity.mgr.updatesong(MusicLayout2.this.selectedsong);
                    Toast.makeText(MusicLayout2.this.getContext(), "DELETED", 0).show();
                }
            }
        };
        this.bg_position = 0;
        this.menuclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (MusicLayout2.this.adapternum == 0) {
                    switch (i) {
                        case 0:
                            MusicLayout2.this.menugridview.setAdapter((ListAdapter) new MusicAlbumAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.albumlist));
                            MusicLayout2.this.adapternum = 1;
                            MusicLayout2.this.listcontrol = false;
                            break;
                        case 1:
                            MusicLayout2.this.adapternum = 3;
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.sideBar.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.allsongadapter);
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 2:
                            MusicLayout2.this.adapternum = 4;
                            ArrayList arrayList = new ArrayList();
                            for (int i2 = 0; i2 < MusicLayout2.this.data.size(); i2++) {
                                if (MusicLayout2.this.data.get(i2).like == 1) {
                                    arrayList.add(MusicLayout2.this.data.get(i2).song_name);
                                }
                            }
                            MusicLayout2.this.likesongadapter = new MusicSongAdapter(MusicLayout2.this.getContext(), arrayList);
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.likesongadapter);
                            MusicLayout2.this.currentSongadapter = MusicLayout2.this.likesongadapter;
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 3:
                            MusicLayout2.this.showPopupMenu(view);
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 4:
                            MusicLayout2.this.filefolderadapter = new MusicFolderAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.albumlist, 1);
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.filefolderadapter);
                            MusicLayout2.this.adapternum = 5;
                            MusicLayout2.this.listcontrol = false;
                            break;
                    }
                }
                if (MusicLayout2.this.adapternum == 1) {
                    MusicLayout2.this.menugridview.setVisibility(8);
                    MusicLayout2.this.songlistview.setVisibility(0);
                    MusicLayout2.this.adapternum = 2;
                    MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.songadapter.get(i));
                    MusicLayout2.this.currentSongadapter = MusicLayout2.this.songadapter.get(i);
                    MusicLayout2.this.listcontrol = true;
                }
            }
        };
        this.selectbg = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.5
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i != 0) {
                    return;
                }
                MusicLayout2.this.savecolorInfo(MusicLayout2.this.getContext(), "music_bg" + MusicLayout2.this.bg_position);
            }
        };
        this.songclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.6
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                switch (MusicLayout2.this.adapternum) {
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                        MusicLayout2.this.currentSongadapter.setSelectItem(i);
                        MusicLayout2.this.currentSongadapter.notifyDataSetInvalidated();
                        for (int i2 = 0; i2 < MusicLayout2.this.data.size(); i2++) {
                            if (MusicLayout2.this.currentSongadapter.getselectSongname().equals(MusicLayout2.this.data.get(i2).song_name)) {
                                MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i2);
                            }
                        }
                        byte[] bArr = {(byte) ((MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (MusicLayout2.this.selectedsong.song_num - (MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK))};
                        MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.ControlSpecSong, (byte) MusicLayout2.this.selectedsong.album_num, bArr[0], bArr[1], (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                        MusicLayout2.this.setplaybuttonstate("play");
                        break;
                    case 3:
                        MusicLayout2.this.allsongadapter.setSelectItem(i);
                        MusicLayout2.this.allsongadapter.notifyDataSetInvalidated();
                        for (int i3 = 0; i3 < MusicLayout2.this.data.size(); i3++) {
                            if (MusicLayout2.this.allsongadapter.getselectSongname().equals(MusicLayout2.this.data.get(i3).song_name)) {
                                MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i3);
                            }
                        }
                        byte[] bArr2 = {(byte) ((MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (MusicLayout2.this.selectedsong.song_num - (MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK))};
                        MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.ControlSpecSong, (byte) MusicLayout2.this.selectedsong.album_num, bArr2[0], bArr2[1], (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                        MusicLayout2.this.setplaybuttonstate("play");
                        break;
                    case 5:
                        MusicLayout2.this.foldersongadapter = new MusicFolderAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.songadapter.get(i).getsonglist(), 2);
                        MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.foldersongadapter);
                        MusicLayout2.this.listcontrol = true;
                        MusicLayout2.this.adapternum = 6;
                        break;
                }
            }
        };
        this.folderlongclick = new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.7
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                switch (MusicLayout2.this.adapternum) {
                    case 5:
                        MusicLayout2.this.showPopupfolderMenu(view);
                        break;
                    case 6:
                        MusicLayout2.this.showPopupmp3Menu(view);
                        break;
                }
                return true;
            }
        };
        this.voicechange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.8
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicLayout2.this.listcontrol = true;
                MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((seekBar.getProgress() * 79) / 100)), (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i == 0) {
                    MusicLayout2.this.bt_voice.setBackgroundResource(R.drawable.btn_voice_none);
                } else {
                    MusicLayout2.this.bt_voice.setBackgroundResource(R.drawable.btn_voice_normal);
                }
                boolean z2 = MusicLayout2.this.setvoicestate;
            }
        };
        this.updateclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.9
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (i != -1 && i == 0) {
                    if (MusicLayout2.this.albumlist.size() != 0) {
                        MusicLayout2.this.albumlist.clear();
                    }
                    if (MusicLayout2.this.songnumhash.size() != 0) {
                        MusicLayout2.this.songnumhash.clear();
                    }
                    if (MusicLayout2.this.songdetailhash.size() != 0) {
                        MusicLayout2.this.songdetailhash.clear();
                    }
                    MusicLayout2.this.albumno = 1;
                    MusicLayout2.this.songpackno = 1;
                    MusicLayout2.this.updatestep = 0;
                    MusicLayout2.this.bigpackagesmax = 0;
                    MusicLayout2.this.currentbigpackages = 1;
                    MusicLayout2.this.setabp = false;
                    MusicLayout2.this.readabd = false;
                    MusicLayout2.this.readsongbp = false;
                    MusicLayout2.this.readsongdata = false;
                    MusicLayout2.this.writesongdata = false;
                    MusicLayout2.this.progress = new ProgressDialog(MusicLayout2.this.getContext());
                    MusicLayout2.this.progress.setCancelable(true);
                    MusicLayout2.this.progress.setCanceledOnTouchOutside(false);
                    MusicLayout2.this.progress.setMessage("Getting Music...");
                    MusicLayout2.this.progress.setProgressStyle(1);
                    MusicLayout2.this.progress.show();
                    if (MusicLayout2.this.data.size() != 0) {
                        MainActivity.mgr.deletesong("song", FounctionActivity.roomidfc);
                    }
                    MusicLayout2.this.updatestep = 1;
                    MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 10L);
                }
            }
        };
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.13
            @Override // java.lang.Runnable
            public void run() {
                List<Savesong> listQuerysong = MainActivity.mgr.querysong();
                if (MusicLayout2.this.data.size() > 0) {
                    MusicLayout2.this.data.clear();
                }
                for (int i = 0; i < listQuerysong.size(); i++) {
                    if (listQuerysong.get(i).room_id == FounctionActivity.roomidfc) {
                        MusicLayout2.this.data.add(listQuerysong.get(i));
                    }
                }
                if (MusicLayout2.this.data.size() > 0) {
                    if (MusicLayout2.this.songadapter.size() > 0) {
                        MusicLayout2.this.songadapter.clear();
                    }
                    if (MusicLayout2.this.albumlist.size() > 0) {
                        MusicLayout2.this.albumlist.clear();
                    }
                    int i2 = MusicLayout2.this.data.get(MusicLayout2.this.data.size() - 1).album_num;
                    int i3 = 0;
                    int i4 = 1;
                    while (i3 < i2) {
                        ArrayList arrayList = new ArrayList();
                        int i5 = i4;
                        for (int i6 = 0; i6 < MusicLayout2.this.data.size(); i6++) {
                            if (MusicLayout2.this.data.get(i6).album_num == i3 + 1) {
                                arrayList.add(MusicLayout2.this.data.get(i6).song_name);
                            }
                            if (MusicLayout2.this.data.get(i6).album_num == i5) {
                                MusicLayout2.this.albumlist.add(MusicLayout2.this.data.get(i6).album_name);
                                i5++;
                            }
                        }
                        MusicLayout2.this.songadapter.add(new MusicSongAdapter(MusicLayout2.this.getContext(), arrayList));
                        i3++;
                        i4 = i5;
                    }
                }
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList2 = new ArrayList();
                for (int i7 = 0; i7 < listQuerymusic.size(); i7++) {
                    if (listQuerymusic.get(i7).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i7).music_id == 1) {
                        arrayList2.add(listQuerymusic.get(i7));
                    }
                }
                if (arrayList2.size() > 0) {
                    MusicLayout2.this.thismusic = (Savemusic) arrayList2.get(0);
                    String[] strArr = new String[MusicLayout2.this.data.size()];
                    for (int i8 = 0; i8 < MusicLayout2.this.data.size(); i8++) {
                        strArr[i8] = MusicLayout2.this.data.get(i8).song_name;
                    }
                    MusicLayout2.this.SourceDateList = MusicLayout2.this.filledData(strArr);
                    Collections.sort(MusicLayout2.this.SourceDateList, MusicLayout2.this.pinyinComparator);
                    MusicLayout2.this.allsongadapter = new AtoZAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.SourceDateList);
                    MusicLayout2.this.reflashui();
                }
                MusicLayout2.this.initrollingtext();
            }
        };
        this.getselectmusic = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.14
            @Override // java.lang.Runnable
            public void run() {
                int i = 0;
                while (true) {
                    if (i < MusicLayout2.this.data.size()) {
                        if (MusicLayout2.this.data.get(i).album_num == MusicLayout2.this.recalbumno && MusicLayout2.this.data.get(i).song_num == MusicLayout2.this.recsongno) {
                            MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i);
                            MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                            MusicNotifyReceiver.subnetID = MusicLayout2.this.thismusic.subnetID;
                            MusicNotifyReceiver.deviceID = MusicLayout2.this.thismusic.deviceID;
                            MusicNotification.sendResidentNoticeType0(MainActivity.maincontext, MusicLayout2.this.selectedsong.song_name);
                            MusicLayout2.this.reflashstep = true;
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                MusicLayout2.this.receivehandler.removeCallbacks(MusicLayout2.this.getselectmusic);
            }
        };
        this.reflashstep = false;
        this.getreflashablum = false;
        this.getreflashsong = false;
        this.getreflashvoice = false;
        this.reflashcount = 0;
        this.reflashrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.15
            @Override // java.lang.Runnable
            public void run() {
                if (MusicLayout2.this.reflashstep) {
                    MusicLayout2.this.reflashuienable = false;
                    MusicLayout2.this.reflashcount = 0;
                    MusicLayout2.this.reflashhandler.removeCallbacks(MusicLayout2.this.reflashrun);
                    return;
                }
                MusicLayout2.this.mc.GetMusicState((byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                MusicLayout2.this.reflashhandler.postDelayed(MusicLayout2.this.reflashrun, 300L);
                MusicLayout2.this.reflashcount++;
                if (MusicLayout2.this.reflashcount >= 1) {
                    MusicLayout2.this.reflashstep = true;
                }
            }
        };
        this.waitfeedback = 0;
        this.feedback = false;
        this.updatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.16
            @Override // java.lang.Runnable
            public void run() {
                MusicLayout2.this.waitfeedback++;
                if (MusicLayout2.this.waitfeedback > 10 && !MusicLayout2.this.feedback) {
                    MusicLayout2.this.albumlist.clear();
                    MusicLayout2.this.songnumhash.clear();
                    MusicLayout2.this.songdetailhash.clear();
                    MusicLayout2.this.savesongdata.clear();
                    MusicLayout2.this.albumno = 1;
                    MusicLayout2.this.songpackno = 1;
                    MusicLayout2.this.updatestep = 0;
                    MusicLayout2.this.bigpackagesmax = 0;
                    MusicLayout2.this.currentbigpackages = 1;
                    MusicLayout2.this.feedback = false;
                    MusicLayout2.this.waitfeedback = 0;
                    MusicLayout2.this.progress.dismiss();
                    Toast.makeText(MusicLayout2.this.getContext(), "update fail", 0).show();
                    MusicLayout2.this.getdatahandler.removeCallbacks(MusicLayout2.this.updatarun);
                }
                switch (MusicLayout2.this.updatestep) {
                    case 1:
                        MusicLayout2.this.mc.MusicReadAlbumQTY((byte) 1, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 2:
                        MusicLayout2.this.feedback = true;
                        MusicLayout2.this.mc.MusicReadAlbum((byte) 1, (byte) MusicLayout2.this.currentbigpackages, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 3:
                        MusicLayout2.this.mc.MusicReadSongQTY((byte) 1, (byte) MusicLayout2.this.albumno, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 4:
                        MusicLayout2.this.mc.MusicReadSong((byte) 1, (byte) MusicLayout2.this.albumno, (byte) MusicLayout2.this.songpackno, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 5:
                        if (!MusicLayout2.this.writesongdata) {
                            MusicLayout2.this.writesongdata = true;
                            MusicLayout2.this.progress.setProgress(80);
                            for (int i = 0; i < MusicLayout2.this.songdetailhash.size(); i++) {
                                Savesong savesong = new Savesong();
                                savesong.room_id = FounctionActivity.roomidfc;
                                savesong.album_num = Integer.parseInt(MusicLayout2.this.songdetailhash.get(i).get(1));
                                savesong.album_name = MusicLayout2.this.albumlist.get(Integer.parseInt(MusicLayout2.this.songdetailhash.get(i).get(1)) - 1);
                                savesong.song_num = Integer.parseInt(MusicLayout2.this.songdetailhash.get(i).get(2));
                                savesong.song_name = MusicLayout2.this.songdetailhash.get(i).get(3);
                                savesong.like = 0;
                                savesong.music_id = MusicLayout2.this.thismusic.music_id;
                                MusicLayout2.this.savesongdata.add(savesong);
                            }
                            MusicLayout2.this.writenum = 0;
                            MusicLayout2.this.writehandler.postDelayed(MusicLayout2.this.writedata, 50L);
                            MusicLayout2.this.getdatahandler.removeCallbacks(MusicLayout2.this.updatarun);
                            break;
                        }
                        break;
                }
            }
        };
        this.writenum = 0;
        this.writedata = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.17
            @Override // java.lang.Runnable
            public void run() {
                if (MusicLayout2.this.writenum < MusicLayout2.this.savesongdata.size()) {
                    MainActivity.mgr.addsong(MusicLayout2.this.savesongdata.get(MusicLayout2.this.writenum));
                    MusicLayout2.this.writenum++;
                    MusicLayout2.this.progress.setProgress(((MusicLayout2.this.writenum * 20) / MusicLayout2.this.savesongdata.size()) + 80);
                    MusicLayout2.this.writehandler.postDelayed(MusicLayout2.this.writedata, 100L);
                    return;
                }
                MusicLayout2.this.albumlist.clear();
                MusicLayout2.this.songnumhash.clear();
                MusicLayout2.this.songdetailhash.clear();
                MusicLayout2.this.savesongdata.clear();
                MusicLayout2.this.albumno = 1;
                MusicLayout2.this.songpackno = 1;
                MusicLayout2.this.updatestep = 0;
                MusicLayout2.this.bigpackagesmax = 0;
                MusicLayout2.this.currentbigpackages = 1;
                MusicLayout2.this.feedback = false;
                MusicLayout2.this.waitfeedback = 0;
                MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.getdatarun, 100L);
                MusicLayout2.this.progress.dismiss();
                Toast.makeText(MusicLayout2.this.getContext(), "update succeed", 0).show();
                MusicLayout2.this.writehandler.removeCallbacks(MusicLayout2.this.writedata);
            }
        };
        init(context);
    }

    public MusicLayout2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.foroldvoice = 0;
        this.recalbumno = 0;
        this.recsongno = 0;
        this.adapternum = 0;
        this.albumno = 1;
        this.songpackno = 1;
        this.updatestep = 0;
        this.bigpackagesmax = 0;
        this.currentbigpackages = 1;
        this.doing = true;
        this.setlikestate = false;
        this.setvoicestate = false;
        this.listcontrol = false;
        this.playmodecount = 0;
        this.playstate = 0;
        this.getdatahandler = new Handler();
        this.writehandler = new Handler();
        this.receivehandler = new Handler();
        this.reflashhandler = new Handler();
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.thismusic = new Savemusic();
        this.selectedsong = new Savesong();
        this.data = new ArrayList();
        this.menulist = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.1
            {
                add("Album");
                add("All Songs");
                add("FAVORITE");
                add("Update List");
                add("File Managerment");
            }
        };
        this.albumlist = new ArrayList();
        this.savesongdata = new ArrayList();
        this.songnumhash = new ArrayList();
        this.songdetailhash = new ArrayList();
        this.songadapter = new ArrayList();
        this.reflashuienable = false;
        this.setabp = false;
        this.readabd = false;
        this.readsongbp = false;
        this.readsongdata = false;
        this.writesongdata = false;
        this.righttouch = new SideBar.OnTouchingLetterChangedListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.2
            @Override // com.shima.smartbushome.assist.AtoZlist.SideBar.OnTouchingLetterChangedListener
            public void onTouchingLetterChanged(String str) {
                int positionForSection = MusicLayout2.this.allsongadapter.getPositionForSection(str.charAt(0));
                if (positionForSection != -1) {
                    MusicLayout2.this.songlistview.setSelection(positionForSection);
                }
            }
        };
        this.likeclick = new CompoundButton.OnCheckedChangeListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (MusicLayout2.this.tv_songname.getText().toString().equals("song name")) {
                    Toast.makeText(MusicLayout2.this.getContext(), "you need to select one song", 0).show();
                    return;
                }
                if (MusicLayout2.this.setlikestate) {
                    return;
                }
                if (z) {
                    MusicLayout2.this.selectedsong.like = 1;
                    MainActivity.mgr.updatesong(MusicLayout2.this.selectedsong);
                    Toast.makeText(MusicLayout2.this.getContext(), "ADDED", 0).show();
                } else {
                    MusicLayout2.this.selectedsong.like = 0;
                    MainActivity.mgr.updatesong(MusicLayout2.this.selectedsong);
                    Toast.makeText(MusicLayout2.this.getContext(), "DELETED", 0).show();
                }
            }
        };
        this.bg_position = 0;
        this.menuclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                if (MusicLayout2.this.adapternum == 0) {
                    switch (i2) {
                        case 0:
                            MusicLayout2.this.menugridview.setAdapter((ListAdapter) new MusicAlbumAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.albumlist));
                            MusicLayout2.this.adapternum = 1;
                            MusicLayout2.this.listcontrol = false;
                            break;
                        case 1:
                            MusicLayout2.this.adapternum = 3;
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.sideBar.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.allsongadapter);
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 2:
                            MusicLayout2.this.adapternum = 4;
                            ArrayList arrayList = new ArrayList();
                            for (int i22 = 0; i22 < MusicLayout2.this.data.size(); i22++) {
                                if (MusicLayout2.this.data.get(i22).like == 1) {
                                    arrayList.add(MusicLayout2.this.data.get(i22).song_name);
                                }
                            }
                            MusicLayout2.this.likesongadapter = new MusicSongAdapter(MusicLayout2.this.getContext(), arrayList);
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.likesongadapter);
                            MusicLayout2.this.currentSongadapter = MusicLayout2.this.likesongadapter;
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 3:
                            MusicLayout2.this.showPopupMenu(view);
                            MusicLayout2.this.listcontrol = true;
                            break;
                        case 4:
                            MusicLayout2.this.filefolderadapter = new MusicFolderAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.albumlist, 1);
                            MusicLayout2.this.menugridview.setVisibility(8);
                            MusicLayout2.this.songlistview.setVisibility(0);
                            MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.filefolderadapter);
                            MusicLayout2.this.adapternum = 5;
                            MusicLayout2.this.listcontrol = false;
                            break;
                    }
                }
                if (MusicLayout2.this.adapternum == 1) {
                    MusicLayout2.this.menugridview.setVisibility(8);
                    MusicLayout2.this.songlistview.setVisibility(0);
                    MusicLayout2.this.adapternum = 2;
                    MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.songadapter.get(i2));
                    MusicLayout2.this.currentSongadapter = MusicLayout2.this.songadapter.get(i2);
                    MusicLayout2.this.listcontrol = true;
                }
            }
        };
        this.selectbg = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.5
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i2) {
                if (i2 != 0) {
                    return;
                }
                MusicLayout2.this.savecolorInfo(MusicLayout2.this.getContext(), "music_bg" + MusicLayout2.this.bg_position);
            }
        };
        this.songclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.6
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                switch (MusicLayout2.this.adapternum) {
                    case 0:
                    case 1:
                    case 2:
                    case 4:
                        MusicLayout2.this.currentSongadapter.setSelectItem(i2);
                        MusicLayout2.this.currentSongadapter.notifyDataSetInvalidated();
                        for (int i22 = 0; i22 < MusicLayout2.this.data.size(); i22++) {
                            if (MusicLayout2.this.currentSongadapter.getselectSongname().equals(MusicLayout2.this.data.get(i22).song_name)) {
                                MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i22);
                            }
                        }
                        byte[] bArr = {(byte) ((MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (MusicLayout2.this.selectedsong.song_num - (MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK))};
                        MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.ControlSpecSong, (byte) MusicLayout2.this.selectedsong.album_num, bArr[0], bArr[1], (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                        MusicLayout2.this.setplaybuttonstate("play");
                        break;
                    case 3:
                        MusicLayout2.this.allsongadapter.setSelectItem(i2);
                        MusicLayout2.this.allsongadapter.notifyDataSetInvalidated();
                        for (int i3 = 0; i3 < MusicLayout2.this.data.size(); i3++) {
                            if (MusicLayout2.this.allsongadapter.getselectSongname().equals(MusicLayout2.this.data.get(i3).song_name)) {
                                MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i3);
                            }
                        }
                        byte[] bArr2 = {(byte) ((MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (MusicLayout2.this.selectedsong.song_num - (MusicLayout2.this.selectedsong.song_num & MotionEventCompat.ACTION_POINTER_INDEX_MASK))};
                        MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.ControlSpecSong, (byte) MusicLayout2.this.selectedsong.album_num, bArr2[0], bArr2[1], (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                        MusicLayout2.this.setplaybuttonstate("play");
                        break;
                    case 5:
                        MusicLayout2.this.foldersongadapter = new MusicFolderAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.songadapter.get(i2).getsonglist(), 2);
                        MusicLayout2.this.songlistview.setAdapter((ListAdapter) MusicLayout2.this.foldersongadapter);
                        MusicLayout2.this.listcontrol = true;
                        MusicLayout2.this.adapternum = 6;
                        break;
                }
            }
        };
        this.folderlongclick = new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.7
            @Override // android.widget.AdapterView.OnItemLongClickListener
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i2, long j) {
                switch (MusicLayout2.this.adapternum) {
                    case 5:
                        MusicLayout2.this.showPopupfolderMenu(view);
                        break;
                    case 6:
                        MusicLayout2.this.showPopupmp3Menu(view);
                        break;
                }
                return true;
            }
        };
        this.voicechange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.8
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicLayout2.this.listcontrol = true;
                MusicLayout2.this.mc.MusicControl((byte) MusicLayout2.this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((seekBar.getProgress() * 79) / 100)), (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (i2 == 0) {
                    MusicLayout2.this.bt_voice.setBackgroundResource(R.drawable.btn_voice_none);
                } else {
                    MusicLayout2.this.bt_voice.setBackgroundResource(R.drawable.btn_voice_normal);
                }
                boolean z2 = MusicLayout2.this.setvoicestate;
            }
        };
        this.updateclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.9
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i2) {
                if (i2 != -1 && i2 == 0) {
                    if (MusicLayout2.this.albumlist.size() != 0) {
                        MusicLayout2.this.albumlist.clear();
                    }
                    if (MusicLayout2.this.songnumhash.size() != 0) {
                        MusicLayout2.this.songnumhash.clear();
                    }
                    if (MusicLayout2.this.songdetailhash.size() != 0) {
                        MusicLayout2.this.songdetailhash.clear();
                    }
                    MusicLayout2.this.albumno = 1;
                    MusicLayout2.this.songpackno = 1;
                    MusicLayout2.this.updatestep = 0;
                    MusicLayout2.this.bigpackagesmax = 0;
                    MusicLayout2.this.currentbigpackages = 1;
                    MusicLayout2.this.setabp = false;
                    MusicLayout2.this.readabd = false;
                    MusicLayout2.this.readsongbp = false;
                    MusicLayout2.this.readsongdata = false;
                    MusicLayout2.this.writesongdata = false;
                    MusicLayout2.this.progress = new ProgressDialog(MusicLayout2.this.getContext());
                    MusicLayout2.this.progress.setCancelable(true);
                    MusicLayout2.this.progress.setCanceledOnTouchOutside(false);
                    MusicLayout2.this.progress.setMessage("Getting Music...");
                    MusicLayout2.this.progress.setProgressStyle(1);
                    MusicLayout2.this.progress.show();
                    if (MusicLayout2.this.data.size() != 0) {
                        MainActivity.mgr.deletesong("song", FounctionActivity.roomidfc);
                    }
                    MusicLayout2.this.updatestep = 1;
                    MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 10L);
                }
            }
        };
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.13
            @Override // java.lang.Runnable
            public void run() {
                List<Savesong> listQuerysong = MainActivity.mgr.querysong();
                if (MusicLayout2.this.data.size() > 0) {
                    MusicLayout2.this.data.clear();
                }
                for (int i2 = 0; i2 < listQuerysong.size(); i2++) {
                    if (listQuerysong.get(i2).room_id == FounctionActivity.roomidfc) {
                        MusicLayout2.this.data.add(listQuerysong.get(i2));
                    }
                }
                if (MusicLayout2.this.data.size() > 0) {
                    if (MusicLayout2.this.songadapter.size() > 0) {
                        MusicLayout2.this.songadapter.clear();
                    }
                    if (MusicLayout2.this.albumlist.size() > 0) {
                        MusicLayout2.this.albumlist.clear();
                    }
                    int i22 = MusicLayout2.this.data.get(MusicLayout2.this.data.size() - 1).album_num;
                    int i3 = 0;
                    int i4 = 1;
                    while (i3 < i22) {
                        ArrayList arrayList = new ArrayList();
                        int i5 = i4;
                        for (int i6 = 0; i6 < MusicLayout2.this.data.size(); i6++) {
                            if (MusicLayout2.this.data.get(i6).album_num == i3 + 1) {
                                arrayList.add(MusicLayout2.this.data.get(i6).song_name);
                            }
                            if (MusicLayout2.this.data.get(i6).album_num == i5) {
                                MusicLayout2.this.albumlist.add(MusicLayout2.this.data.get(i6).album_name);
                                i5++;
                            }
                        }
                        MusicLayout2.this.songadapter.add(new MusicSongAdapter(MusicLayout2.this.getContext(), arrayList));
                        i3++;
                        i4 = i5;
                    }
                }
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList2 = new ArrayList();
                for (int i7 = 0; i7 < listQuerymusic.size(); i7++) {
                    if (listQuerymusic.get(i7).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i7).music_id == 1) {
                        arrayList2.add(listQuerymusic.get(i7));
                    }
                }
                if (arrayList2.size() > 0) {
                    MusicLayout2.this.thismusic = (Savemusic) arrayList2.get(0);
                    String[] strArr = new String[MusicLayout2.this.data.size()];
                    for (int i8 = 0; i8 < MusicLayout2.this.data.size(); i8++) {
                        strArr[i8] = MusicLayout2.this.data.get(i8).song_name;
                    }
                    MusicLayout2.this.SourceDateList = MusicLayout2.this.filledData(strArr);
                    Collections.sort(MusicLayout2.this.SourceDateList, MusicLayout2.this.pinyinComparator);
                    MusicLayout2.this.allsongadapter = new AtoZAdapter(MusicLayout2.this.getContext(), MusicLayout2.this.SourceDateList);
                    MusicLayout2.this.reflashui();
                }
                MusicLayout2.this.initrollingtext();
            }
        };
        this.getselectmusic = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.14
            @Override // java.lang.Runnable
            public void run() {
                int i2 = 0;
                while (true) {
                    if (i2 < MusicLayout2.this.data.size()) {
                        if (MusicLayout2.this.data.get(i2).album_num == MusicLayout2.this.recalbumno && MusicLayout2.this.data.get(i2).song_num == MusicLayout2.this.recsongno) {
                            MusicLayout2.this.selectedsong = MusicLayout2.this.data.get(i2);
                            MusicLayout2.this.setsong(MusicLayout2.this.selectedsong);
                            MusicNotifyReceiver.subnetID = MusicLayout2.this.thismusic.subnetID;
                            MusicNotifyReceiver.deviceID = MusicLayout2.this.thismusic.deviceID;
                            MusicNotification.sendResidentNoticeType0(MainActivity.maincontext, MusicLayout2.this.selectedsong.song_name);
                            MusicLayout2.this.reflashstep = true;
                            break;
                        }
                        i2++;
                    } else {
                        break;
                    }
                }
                MusicLayout2.this.receivehandler.removeCallbacks(MusicLayout2.this.getselectmusic);
            }
        };
        this.reflashstep = false;
        this.getreflashablum = false;
        this.getreflashsong = false;
        this.getreflashvoice = false;
        this.reflashcount = 0;
        this.reflashrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.15
            @Override // java.lang.Runnable
            public void run() {
                if (MusicLayout2.this.reflashstep) {
                    MusicLayout2.this.reflashuienable = false;
                    MusicLayout2.this.reflashcount = 0;
                    MusicLayout2.this.reflashhandler.removeCallbacks(MusicLayout2.this.reflashrun);
                    return;
                }
                MusicLayout2.this.mc.GetMusicState((byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                MusicLayout2.this.reflashhandler.postDelayed(MusicLayout2.this.reflashrun, 300L);
                MusicLayout2.this.reflashcount++;
                if (MusicLayout2.this.reflashcount >= 1) {
                    MusicLayout2.this.reflashstep = true;
                }
            }
        };
        this.waitfeedback = 0;
        this.feedback = false;
        this.updatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.16
            @Override // java.lang.Runnable
            public void run() {
                MusicLayout2.this.waitfeedback++;
                if (MusicLayout2.this.waitfeedback > 10 && !MusicLayout2.this.feedback) {
                    MusicLayout2.this.albumlist.clear();
                    MusicLayout2.this.songnumhash.clear();
                    MusicLayout2.this.songdetailhash.clear();
                    MusicLayout2.this.savesongdata.clear();
                    MusicLayout2.this.albumno = 1;
                    MusicLayout2.this.songpackno = 1;
                    MusicLayout2.this.updatestep = 0;
                    MusicLayout2.this.bigpackagesmax = 0;
                    MusicLayout2.this.currentbigpackages = 1;
                    MusicLayout2.this.feedback = false;
                    MusicLayout2.this.waitfeedback = 0;
                    MusicLayout2.this.progress.dismiss();
                    Toast.makeText(MusicLayout2.this.getContext(), "update fail", 0).show();
                    MusicLayout2.this.getdatahandler.removeCallbacks(MusicLayout2.this.updatarun);
                }
                switch (MusicLayout2.this.updatestep) {
                    case 1:
                        MusicLayout2.this.mc.MusicReadAlbumQTY((byte) 1, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 2:
                        MusicLayout2.this.feedback = true;
                        MusicLayout2.this.mc.MusicReadAlbum((byte) 1, (byte) MusicLayout2.this.currentbigpackages, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 3:
                        MusicLayout2.this.mc.MusicReadSongQTY((byte) 1, (byte) MusicLayout2.this.albumno, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 4:
                        MusicLayout2.this.mc.MusicReadSong((byte) 1, (byte) MusicLayout2.this.albumno, (byte) MusicLayout2.this.songpackno, (byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.updatarun, 500L);
                        break;
                    case 5:
                        if (!MusicLayout2.this.writesongdata) {
                            MusicLayout2.this.writesongdata = true;
                            MusicLayout2.this.progress.setProgress(80);
                            for (int i2 = 0; i2 < MusicLayout2.this.songdetailhash.size(); i2++) {
                                Savesong savesong = new Savesong();
                                savesong.room_id = FounctionActivity.roomidfc;
                                savesong.album_num = Integer.parseInt(MusicLayout2.this.songdetailhash.get(i2).get(1));
                                savesong.album_name = MusicLayout2.this.albumlist.get(Integer.parseInt(MusicLayout2.this.songdetailhash.get(i2).get(1)) - 1);
                                savesong.song_num = Integer.parseInt(MusicLayout2.this.songdetailhash.get(i2).get(2));
                                savesong.song_name = MusicLayout2.this.songdetailhash.get(i2).get(3);
                                savesong.like = 0;
                                savesong.music_id = MusicLayout2.this.thismusic.music_id;
                                MusicLayout2.this.savesongdata.add(savesong);
                            }
                            MusicLayout2.this.writenum = 0;
                            MusicLayout2.this.writehandler.postDelayed(MusicLayout2.this.writedata, 50L);
                            MusicLayout2.this.getdatahandler.removeCallbacks(MusicLayout2.this.updatarun);
                            break;
                        }
                        break;
                }
            }
        };
        this.writenum = 0;
        this.writedata = new Runnable() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.17
            @Override // java.lang.Runnable
            public void run() {
                if (MusicLayout2.this.writenum < MusicLayout2.this.savesongdata.size()) {
                    MainActivity.mgr.addsong(MusicLayout2.this.savesongdata.get(MusicLayout2.this.writenum));
                    MusicLayout2.this.writenum++;
                    MusicLayout2.this.progress.setProgress(((MusicLayout2.this.writenum * 20) / MusicLayout2.this.savesongdata.size()) + 80);
                    MusicLayout2.this.writehandler.postDelayed(MusicLayout2.this.writedata, 100L);
                    return;
                }
                MusicLayout2.this.albumlist.clear();
                MusicLayout2.this.songnumhash.clear();
                MusicLayout2.this.songdetailhash.clear();
                MusicLayout2.this.savesongdata.clear();
                MusicLayout2.this.albumno = 1;
                MusicLayout2.this.songpackno = 1;
                MusicLayout2.this.updatestep = 0;
                MusicLayout2.this.bigpackagesmax = 0;
                MusicLayout2.this.currentbigpackages = 1;
                MusicLayout2.this.feedback = false;
                MusicLayout2.this.waitfeedback = 0;
                MusicLayout2.this.getdatahandler.postDelayed(MusicLayout2.this.getdatarun, 100L);
                MusicLayout2.this.progress.dismiss();
                Toast.makeText(MusicLayout2.this.getContext(), "update succeed", 0).show();
                MusicLayout2.this.writehandler.removeCallbacks(MusicLayout2.this.writedata);
            }
        };
        init(context);
    }

    public void init(Context context) {
        this.view = View.inflate(context, R.layout.music_layout, this);
        this.musicinflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.rootcontext = context;
        this.mc = new musiccontrol();
        this.characterParser = CharacterParser.getInstance();
        this.pinyinComparator = new PinyinComparator();
        this.sb_setvoice = (SeekBar) this.view.findViewById(R.id.seekBar2);
        this.dialog = (TextView) this.view.findViewById(R.id.dialog);
        this.sideBar = (SideBar) this.view.findViewById(R.id.sidrbar);
        this.tv_songname = (rollingTextview) this.view.findViewById(R.id.name);
        this.sideBar.setTextView(this.dialog);
        this.bt_back = (Button) this.view.findViewById(R.id.back);
        this.bt_next = (Button) this.view.findViewById(R.id.next);
        this.bt_voice = (Button) this.view.findViewById(R.id.f1voice);
        this.bt_playmode = (Button) this.view.findViewById(R.id.playmode);
        this.bt_play = (Button) this.view.findViewById(R.id.play);
        this.cb_like = (CheckBox) this.view.findViewById(R.id.like);
        this.menugridview = (GridView) this.view.findViewById(R.id.gridView);
        this.songlistview = (ListView) this.view.findViewById(R.id.listView);
        this.bt_back.setOnClickListener(this);
        this.bt_next.setOnClickListener(this);
        this.bt_voice.setOnClickListener(this);
        this.bt_playmode.setOnClickListener(this);
        this.bt_play.setOnClickListener(this);
        this.cb_like.setOnCheckedChangeListener(this.likeclick);
        this.sb_setvoice.setOnSeekBarChangeListener(this.voicechange);
        this.menugridview.setOnItemClickListener(this.menuclick);
        this.songlistview.setOnItemClickListener(this.songclick);
        this.songlistview.setOnItemLongClickListener(this.folderlongclick);
        this.sideBar.setOnTouchingLetterChangedListener(this.righttouch);
        this.menuadapter = new MusicOptionAdapter(context, this.menulist);
        this.menugridview.setAdapter((ListAdapter) this.menuadapter);
        this.getdatahandler.postDelayed(this.getdatarun, 70L);
    }

    public void setcontent(Savemusic savemusic) {
        this.thismusic = savemusic;
    }

    public void setsong(Savesong savesong) {
        this.tv_songname.setText(savesong.song_name);
        if (savesong.like == 1) {
            this.setlikestate = true;
            this.cb_like.setChecked(true);
            this.setlikestate = false;
        } else {
            this.setlikestate = true;
            this.cb_like.setChecked(false);
            this.setlikestate = false;
        }
    }

    public void setVoiceValue(int i) {
        this.setvoicestate = true;
        this.sb_setvoice.setProgress(i);
        this.setvoicestate = false;
    }

    public void SwitchtoMusicSD() {
        this.mc.SwitchtoMusicSD((byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
    }

    public void setAlbumBigPackages(int i) {
        if (this.setabp) {
            return;
        }
        if (i > 0) {
            this.bigpackagesmax = i;
            if (this.progress != null) {
                this.progress.setProgress(10);
            }
            this.currentbigpackages = 1;
            this.updatestep = 2;
            this.setabp = true;
            return;
        }
        this.updatestep = 0;
        this.progress.setProgress(100);
        this.progress.dismiss();
    }

    public void readalbumdata(byte[] bArr) {
        if (this.readabd || this.currentbigpackages != (bArr[28] & 255) || this.currentbigpackages > this.bigpackagesmax) {
            return;
        }
        int i = bArr[29] & 255;
        int length = 0;
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = length + 31;
            byte[] bArr2 = new byte[bArr[i3]];
            for (int i4 = 0; i4 < bArr[i3]; i4++) {
                bArr2[i4] = bArr[length + 32 + i4];
            }
            this.albumlist.add(bytetostring(bArr2, Charsets.Unicode).replace(".PLS", ""));
            length += bArr2.length + 2;
        }
        if (this.currentbigpackages == this.bigpackagesmax) {
            for (int i5 = 0; i5 < this.albumlist.size(); i5++) {
                if (this.albumlist.get(i5).equals("SPECIAL")) {
                    this.albumlist.remove(i5);
                }
            }
            this.currentbigpackages++;
            if (this.progress != null) {
                this.progress.setProgress(30);
            }
            this.updatestep = 3;
            this.albumno = 1;
            this.readabd = true;
            return;
        }
        this.updatestep = 2;
        this.currentbigpackages++;
    }

    public void readSongBigPackages(byte[] bArr) {
        if (this.readsongbp || (bArr[26] & 255) != this.albumno) {
            return;
        }
        HashMap<String, Integer> map = new HashMap<>();
        map.put("albumno", Integer.valueOf(bArr[26] & 255));
        map.put("songbigpackage", Integer.valueOf(bArr[27] & 255));
        this.songnumhash.add(map);
        if (this.albumno < this.albumlist.size()) {
            this.albumno++;
            return;
        }
        this.progress.setProgress(50);
        int i = 0;
        while (true) {
            if (i >= this.albumlist.size()) {
                break;
            }
            if (this.songnumhash.get(i).get("songbigpackage").intValue() != 0) {
                this.albumno = i + 1;
                break;
            }
            i++;
        }
        this.updatestep = 4;
        this.songpackno = 1;
        this.readsongbp = true;
    }

    public void readSongdata(byte[] bArr) {
        if (!this.readsongdata && this.albumno == (bArr[28] & 255) && this.songpackno == (bArr[29] & 255)) {
            int i = bArr[30] & 255;
            int length = 0;
            for (int i2 = 0; i2 < i; i2++) {
                byte[] bArr2 = new byte[bArr[length + 33]];
                HashMap<Integer, String> map = new HashMap<>();
                for (int i3 = 0; i3 < bArr2.length; i3++) {
                    bArr2[i3] = bArr[length + 34 + i3];
                }
                int i4 = (bArr[length + 31] << 8) + (bArr[length + 32] & 255);
                map.put(1, String.valueOf(this.albumno));
                map.put(2, String.valueOf(i4));
                map.put(3, bytetostring(bArr2, Charsets.Unicode));
                this.songdetailhash.add(map);
                length += bArr2.length + 3;
            }
            if (this.albumno - 1 >= this.songnumhash.size()) {
                return;
            }
            if (this.songpackno == this.songnumhash.get(this.albumno - 1).get("songbigpackage").intValue()) {
                if (this.albumno == this.songnumhash.size()) {
                    this.updatestep = 5;
                    this.progress.setProgress(70);
                    this.readsongdata = true;
                    return;
                }
                this.albumno++;
                for (int i5 = 0; i5 < this.albumlist.size(); i5++) {
                    if (this.songnumhash.get(this.albumno - 1).get("songbigpackage").intValue() != 0) {
                        this.songpackno = 1;
                        this.updatestep = 4;
                        this.progress.setProgress(((this.albumno * 20) / this.albumlist.size()) + 50);
                        return;
                    } else {
                        this.albumno++;
                        if (this.albumno >= this.albumlist.size()) {
                            this.updatestep = 5;
                            this.progress.setProgress(70);
                            this.readsongdata = true;
                            return;
                        }
                    }
                }
                return;
            }
            this.songpackno++;
            this.updatestep = 4;
        }
    }

    public void getmusicstate(byte[] bArr) {
        int i = (bArr[25] << 8) + (bArr[26] & 255);
        int i2 = bArr[36] & 255;
        if (i == 9050) {
            if (this.getreflashvoice) {
                return;
            }
            this.getreflashvoice = true;
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
            setVoiceValue(100 - ((Integer.parseInt(bytetostring(bArr2, "ascii")) * 100) / 79));
            return;
        }
        if (i != 9043 || this.reflashstep) {
            return;
        }
        if (i2 == 49) {
            if (this.getreflashablum) {
                return;
            }
            this.getreflashablum = true;
            this.recalbumno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 0), Charsets.Unicode));
            return;
        }
        if (i2 == 51 && this.getreflashvoice && this.getreflashablum && !this.getreflashsong) {
            this.getreflashsong = true;
            try {
                this.recsongno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 1), Charsets.Unicode));
                this.receivehandler.postDelayed(this.getselectmusic, 20L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getpieceofbyte(byte[] bArr, int i) {
        int i2 = i == 0 ? 49 : i == 1 ? 51 : 0;
        int i3 = i2;
        while (true) {
            if (i3 >= bArr.length) {
                i3 = 0;
                break;
            }
            if (bArr[i3] == 0 && bArr[i3 + 1] == 47) {
                break;
            }
            i3++;
        }
        int i4 = i3 - i2;
        byte[] bArr2 = new byte[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            try {
                bArr2[i5] = bArr[i2 + i5];
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            android.widget.Button r13 = r12.bt_play
            r0 = 2131165562(0x7f07017a, float:1.7945345E38)
            r13.setBackgroundResource(r0)
            r12.playstate = r2
            com.shima.smartbushome.founction_command.musiccontrol r3 = r12.mc
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
            android.widget.Button r13 = r12.bt_play
            r0 = 2131165558(0x7f070176, float:1.7945337E38)
            r13.setBackgroundResource(r0)
            r12.playstate = r3
            com.shima.smartbushome.founction_command.musiccontrol r4 = r12.mc
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
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.MusicLayout2.setplaybuttonstate(java.lang.String):void");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int i = 0;
        switch (view.getId()) {
            case R.id.back /* 2131230829 */:
                switch (this.adapternum) {
                    case 0:
                    case 1:
                        this.mc.MusicControl((byte) this.PlayControl, (byte) 1, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        reflashui();
                        break;
                    case 2:
                        this.currentSongadapter.setNextorBack("back");
                        this.songlistview.setSelection(this.currentSongadapter.getSelectItem());
                        this.currentSongadapter.notifyDataSetInvalidated();
                        String str = this.currentSongadapter.getselectSongname();
                        while (true) {
                            if (i < this.data.size()) {
                                if (this.data.get(i).song_name.equals(str)) {
                                    this.selectedsong = this.data.get(i);
                                    setsong(this.selectedsong);
                                } else {
                                    i++;
                                }
                            }
                        }
                        this.mc.MusicControl((byte) this.PlayControl, (byte) 1, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        break;
                    case 3:
                        this.allsongadapter.setNextorBack("back");
                        this.songlistview.setSelection(this.allsongadapter.getSelectItem());
                        this.allsongadapter.notifyDataSetInvalidated();
                        String str2 = this.allsongadapter.getselectSongname();
                        while (true) {
                            if (i < this.data.size()) {
                                if (this.data.get(i).song_name.equals(str2)) {
                                    this.selectedsong = this.data.get(i);
                                    setsong(this.selectedsong);
                                } else {
                                    i++;
                                }
                            }
                        }
                        palyselectsong();
                        break;
                    case 4:
                        this.currentSongadapter.setNextorBack("back");
                        this.songlistview.setSelection(this.currentSongadapter.getSelectItem());
                        this.currentSongadapter.notifyDataSetInvalidated();
                        String str3 = this.currentSongadapter.getselectSongname();
                        while (true) {
                            if (i < this.data.size()) {
                                if (this.data.get(i).song_name.equals(str3)) {
                                    this.selectedsong = this.data.get(i);
                                    setsong(this.selectedsong);
                                } else {
                                    i++;
                                }
                            }
                        }
                        palyselectsong();
                        break;
                }
            case R.id.next /* 2131231481 */:
                switch (this.adapternum) {
                    case 0:
                    case 1:
                        this.mc.MusicControl((byte) this.PlayControl, (byte) 2, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        reflashui();
                        break;
                    case 2:
                        this.currentSongadapter.setNextorBack("next");
                        this.songlistview.setSelection(this.currentSongadapter.getSelectItem());
                        this.currentSongadapter.notifyDataSetInvalidated();
                        String str4 = this.currentSongadapter.getselectSongname();
                        while (true) {
                            if (i < this.data.size()) {
                                if (this.data.get(i).song_name.equals(str4)) {
                                    this.selectedsong = this.data.get(i);
                                    setsong(this.selectedsong);
                                } else {
                                    i++;
                                }
                            }
                        }
                        this.mc.MusicControl((byte) this.PlayControl, (byte) 2, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        break;
                    case 3:
                        this.allsongadapter.setNextorBack("next");
                        this.songlistview.setSelection(this.allsongadapter.getSelectItem());
                        this.allsongadapter.notifyDataSetInvalidated();
                        String str5 = this.allsongadapter.getselectSongname();
                        while (true) {
                            if (i < this.data.size()) {
                                if (this.data.get(i).song_name.equals(str5)) {
                                    this.selectedsong = this.data.get(i);
                                    setsong(this.selectedsong);
                                } else {
                                    i++;
                                }
                            }
                        }
                        palyselectsong();
                        break;
                    case 4:
                        this.currentSongadapter.setNextorBack("next");
                        this.songlistview.setSelection(this.currentSongadapter.getSelectItem());
                        this.currentSongadapter.notifyDataSetInvalidated();
                        String str6 = this.currentSongadapter.getselectSongname();
                        while (true) {
                            if (i < this.data.size()) {
                                if (this.data.get(i).song_name.equals(str6)) {
                                    this.selectedsong = this.data.get(i);
                                    setsong(this.selectedsong);
                                } else {
                                    i++;
                                }
                            }
                        }
                        palyselectsong();
                        break;
                }
            case R.id.play /* 2131231585 */:
                switch (this.playstate) {
                    case 0:
                        setplaybuttonstate("play");
                        break;
                    case 1:
                        setplaybuttonstate("pause");
                        break;
                }
            case R.id.playmode /* 2131231589 */:
                switch (this.playmodecount) {
                    case 0:
                        this.bt_playmode.setBackgroundResource(R.drawable.songplaymode2);
                        this.mc.MusicControl((byte) this.PlayModeChange, (byte) 2, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        this.playmodecount = 1;
                        Toast.makeText(getContext(), "repeat 1 song", 0).show();
                        break;
                    case 1:
                        this.bt_playmode.setBackgroundResource(R.drawable.songplaymode3);
                        this.mc.MusicControl((byte) this.PlayModeChange, (byte) 3, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        this.playmodecount = 2;
                        Toast.makeText(getContext(), "repeat 1 album", 0).show();
                        break;
                    case 2:
                        this.bt_playmode.setBackgroundResource(R.drawable.songplaymode4);
                        this.mc.MusicControl((byte) this.PlayModeChange, (byte) 4, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        this.playmodecount = 3;
                        Toast.makeText(getContext(), "repeat all album", 0).show();
                        break;
                    case 3:
                        this.bt_playmode.setBackgroundResource(R.drawable.songplaymode1);
                        this.mc.MusicControl((byte) this.PlayModeChange, (byte) 1, (byte) 0, (byte) 0, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                        this.playmodecount = 0;
                        Toast.makeText(getContext(), "not repeat", 0).show();
                        break;
                }
            case R.id.f1voice /* 2131232218 */:
                if (this.sb_setvoice.getProgress() > 0 && this.sb_setvoice.getProgress() <= 100) {
                    this.foroldvoice = this.sb_setvoice.getProgress();
                    this.sb_setvoice.setProgress(0);
                    this.mc.MusicControl((byte) this.VoluneControl, (byte) 1, (byte) 3, (byte) 79, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                    this.bt_voice.setBackgroundResource(R.drawable.btn_voice_none);
                    break;
                } else if (this.sb_setvoice.getProgress() == 0) {
                    if (this.foroldvoice == 0) {
                        this.sb_setvoice.setProgress(50);
                        this.mc.MusicControl((byte) this.VoluneControl, (byte) 1, (byte) 3, (byte) 40, (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                    } else {
                        this.sb_setvoice.setProgress(this.foroldvoice);
                        this.mc.MusicControl((byte) this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((this.foroldvoice * 79) / 100)), (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
                    }
                    this.bt_voice.setBackgroundResource(R.drawable.btn_voice_normal);
                    break;
                }
                break;
        }
    }

    public void savecolorInfo(Context context, String str) {
        SharedPreferences.Editor editorEdit = context.getSharedPreferences("pagesbgcolor", 0).edit();
        editorEdit.putString("musicbg", str);
        editorEdit.commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<SortModel> filledData(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < strArr.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(strArr[i]);
            String upperCase = this.characterParser.getSelling(strArr[i]).substring(0, 1).toUpperCase();
            if (upperCase.matches("[A-Z]")) {
                sortModel.setSortLetters(upperCase.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
            arrayList.add(sortModel);
        }
        return arrayList;
    }

    public void musicbackpress() {
        if (this.adapternum == 0) {
            Music.finishmusic();
            this.listcontrol = false;
            return;
        }
        if (this.adapternum == 1) {
            this.adapternum = 0;
            this.menugridview.setAdapter((ListAdapter) this.menuadapter);
            this.listcontrol = false;
            return;
        }
        if (this.adapternum == 2) {
            this.adapternum = 1;
            this.menugridview.setVisibility(0);
            this.songlistview.setVisibility(8);
            this.menugridview.setAdapter((ListAdapter) new MusicAlbumAdapter(getContext(), this.albumlist));
            this.listcontrol = false;
            return;
        }
        if (this.adapternum == 3) {
            this.adapternum = 0;
            this.menugridview.setVisibility(0);
            this.songlistview.setVisibility(8);
            this.sideBar.setVisibility(8);
            this.menugridview.setAdapter((ListAdapter) this.menuadapter);
            this.listcontrol = false;
            return;
        }
        if (this.adapternum == 4) {
            this.adapternum = 0;
            this.menugridview.setVisibility(0);
            this.songlistview.setVisibility(8);
            this.menugridview.setAdapter((ListAdapter) this.menuadapter);
            this.listcontrol = false;
            return;
        }
        if (this.adapternum == 5) {
            this.adapternum = 0;
            this.menugridview.setVisibility(0);
            this.songlistview.setVisibility(8);
            this.menugridview.setAdapter((ListAdapter) this.menuadapter);
            this.listcontrol = false;
            return;
        }
        if (this.adapternum == 6) {
            this.songlistview.setAdapter((ListAdapter) new MusicFolderAdapter(getContext(), this.albumlist, 1));
            this.adapternum = 5;
            this.listcontrol = false;
        }
    }

    public void music_setting() {
        AlertView alertView = new AlertView("Update music list", "Are you sure to update? It will take a few minutes to finish", "CANCEL", new String[]{"YES"}, null, this.rootcontext, AlertView.Style.Alert, this.updateclick);
        alertView.setCancelable(false);
        alertView.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.music_update_setting, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.10
            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.music_updatelist /* 2131231468 */:
                        MusicLayout2.this.music_setting();
                        return false;
                    case R.id.music_updatezaudiolist /* 2131231469 */:
                        MusicLayout2.this.mc.updateZaudiolist((byte) MusicLayout2.this.thismusic.subnetID, (byte) MusicLayout2.this.thismusic.deviceID, MainActivity.mydupsocket);
                        Toast.makeText(MusicLayout2.this.rootcontext, "updating,please wait for a few sec", 0).show();
                        return false;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupfolderMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.music_folder_setting, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.11
            @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItem.getItemId();
                return false;
            }
        });
        popupMenu.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupmp3Menu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.music_mp3file_setting, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.shima.smartbushome.selflayout.MusicLayout2.12
            @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItem.getItemId();
                return false;
            }
        });
        popupMenu.show();
    }

    public void palyselectsong() {
        byte[] bArr = {(byte) (this.selectedsong.song_num >> 8), (byte) ((this.selectedsong.song_num & 255) - (this.selectedsong.song_num >> 8))};
        this.mc.MusicControl((byte) this.ControlSpecSong, (byte) this.selectedsong.album_num, bArr[0], bArr[1], (byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
    }

    public void reflashui() {
        this.reflashuienable = true;
        this.reflashstep = false;
        this.getreflashablum = false;
        this.getreflashvoice = false;
        this.getreflashsong = false;
        this.reflashhandler.postDelayed(this.reflashrun, 80L);
    }

    public void removetimer() {
        this.reflashhandler.removeCallbacks(this.reflashrun);
    }

    public void receiveddata(byte[] bArr) {
        if (this.doing) {
            this.doing = false;
            int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
            if (bArr[17] == ((byte) this.thismusic.subnetID) && bArr[18] == ((byte) this.thismusic.deviceID)) {
                if (i == 737) {
                    setAlbumBigPackages(bArr[26] & 255);
                } else if (i == 739) {
                    readalbumdata(bArr);
                } else if (i == 741) {
                    readSongBigPackages(bArr);
                } else if (i == 743) {
                    readSongdata(bArr);
                } else if (i == 6447) {
                    if (this.listcontrol) {
                        this.listcontrol = false;
                    } else if (this.reflashuienable) {
                        try {
                            getmusicstate(bArr);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        setCurrSongName(bArr);
                    }
                }
            }
            this.doing = true;
        }
    }

    public void setCurrSongName(byte[] bArr) {
        int i = (bArr[25] << 8) + (bArr[26] & 255);
        int i2 = bArr[36] & 255;
        if (i == 9043) {
            if (i2 == 49) {
                this.recalbumno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 0), Charsets.Unicode));
                return;
            }
            if (i2 != 51) {
                return;
            }
            try {
                this.recsongno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 1), Charsets.Unicode));
                this.receivehandler.postDelayed(this.getselectmusic, 20L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
