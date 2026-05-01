package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.MoodIconAdapter;
import com.shima.smartbushome.assist.SwipeLayout;
import com.shima.smartbushome.database.Savemood;
import com.shima.smartbushome.database.Savemoodbutton;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.founction_command.audio_incontrol;
import com.shima.smartbushome.founction_command.curtaincontrol;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.founction_command.moodcontrol;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.founction_command.radiocontrol;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MoodLayout extends LinearLayout implements View.OnLongClickListener, View.OnClickListener {
    private static final byte const_ac_cmd_type_onoff = 3;
    private static final byte const_ac_cmd_type_set_auto_tmp = 8;
    private static final byte const_ac_cmd_type_set_cold_tmp = 4;
    private static final byte const_ac_cmd_type_set_fan = 5;
    private static final byte const_ac_cmd_type_set_heat_tmp = 7;
    private static final byte const_ac_cmd_type_set_mode = 6;
    private static final byte const_fan_speed_anto = 0;
    private static final byte const_fan_speed_high = 1;
    private static final byte const_fan_speed_low = 3;
    private static final byte const_fan_speed_medium = 2;
    private static final byte const_mode_auto = 3;
    private static final byte const_mode_cool = 0;
    private static final byte const_mode_fan = 2;
    private static final byte const_mode_heat = 1;
    public int AlbumorRadioControl;
    public int ControlSpecSong;
    public int Music_Source;
    public int PlayControl;
    public int PlayModeChange;
    public int VoluneControl;
    ACcontrol acc;
    audio_incontrol aic;
    CheckBox cb_mood;
    private String[] colorarray;
    curtaincontrol ctc;
    LinearLayout delete;
    boolean deletemode;
    Handler getcommandhandler;
    Runnable getmoodcommandrun;
    AlertView iconalter;
    private List<String> iconarray;
    String iconrount;
    LayoutInflater inflater;
    LinearLayout item;
    ImageView iv_moodicon;
    lightcontrol lc;
    moodcontrol mc;
    LinearLayout moodlinear;
    String moodremark;
    musiccontrol muc;
    EditText name;
    radiocontrol rc;
    Context rootcontext;
    View selfview;
    int sentcount;
    Handler senthandler;
    boolean senting;
    Runnable sentingcommandrun;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    List<Savemood> thismood;
    Savemoodbutton thismoodbutton;
    public View.OnTouchListener touchcolor;
    TextView tv_moodremark;
    View view;

    public MoodLayout(Context context) {
        super(context);
        this.thismood = new ArrayList();
        this.getcommandhandler = new Handler();
        this.senthandler = new Handler();
        this.deletemode = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.MoodLayout.1
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
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.touchcolor = new View.OnTouchListener() { // from class: com.shima.smartbushome.selflayout.MoodLayout.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    return false;
                }
                motionEvent.getAction();
                return false;
            }
        };
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MoodLayout.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == MoodLayout.this.settingalter && i == 0) {
                    Savemoodbutton savemoodbutton = new Savemoodbutton();
                    savemoodbutton.room_id = MoodLayout.this.thismoodbutton.room_id;
                    savemoodbutton.mood_id = MoodLayout.this.thismoodbutton.mood_id;
                    savemoodbutton.mood_name = MoodLayout.this.name.getText().toString().trim();
                    savemoodbutton.mood_icon = MoodLayout.this.iconrount;
                    MainActivity.mgr.updatemoodbutton(savemoodbutton);
                    MoodLayout.this.thismoodbutton.mood_name = MoodLayout.this.name.getText().toString().trim();
                    MoodLayout.this.thismoodbutton.mood_icon = MoodLayout.this.iconrount;
                    MoodLayout.this.setcontent(MoodLayout.this.thismoodbutton);
                }
            }
        };
        this.getmoodcommandrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MoodLayout.5
            @Override // java.lang.Runnable
            public void run() {
                List<Savemood> listQuerymood = MainActivity.mgr.querymood();
                for (int i = 0; i < listQuerymood.size(); i++) {
                    if (listQuerymood.get(i).room_id == MoodLayout.this.thismoodbutton.room_id && listQuerymood.get(i).mood_id == MoodLayout.this.thismoodbutton.mood_id) {
                        MoodLayout.this.thismood.add(listQuerymood.get(i));
                    }
                }
            }
        };
        this.sentingcommandrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MoodLayout.6
            @Override // java.lang.Runnable
            public void run() {
                if (MoodLayout.this.senting) {
                    MoodLayout.this.sentcount++;
                    MoodLayout.this.senting = false;
                    if (MoodLayout.this.sentcount >= MoodLayout.this.thismood.size()) {
                        MoodLayout.this.senthandler.removeCallbacks(MoodLayout.this.sentingcommandrun);
                        MoodLayout.this.sentcount = 0;
                        return;
                    } else {
                        MoodLayout.this.senthandler.postDelayed(MoodLayout.this.sentingcommandrun, 75L);
                        return;
                    }
                }
                if (MoodLayout.this.thismood.size() > 0) {
                    MoodLayout.this.sentcommand();
                }
                MoodLayout.this.senthandler.postDelayed(MoodLayout.this.sentingcommandrun, 450L);
                MoodLayout.this.senting = true;
            }
        };
        this.sentcount = 0;
        this.senting = false;
        this.rootcontext = context;
        initview(context);
    }

    public MoodLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.thismood = new ArrayList();
        this.getcommandhandler = new Handler();
        this.senthandler = new Handler();
        this.deletemode = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.MoodLayout.1
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
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.touchcolor = new View.OnTouchListener() { // from class: com.shima.smartbushome.selflayout.MoodLayout.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    return false;
                }
                motionEvent.getAction();
                return false;
            }
        };
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MoodLayout.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == MoodLayout.this.settingalter && i == 0) {
                    Savemoodbutton savemoodbutton = new Savemoodbutton();
                    savemoodbutton.room_id = MoodLayout.this.thismoodbutton.room_id;
                    savemoodbutton.mood_id = MoodLayout.this.thismoodbutton.mood_id;
                    savemoodbutton.mood_name = MoodLayout.this.name.getText().toString().trim();
                    savemoodbutton.mood_icon = MoodLayout.this.iconrount;
                    MainActivity.mgr.updatemoodbutton(savemoodbutton);
                    MoodLayout.this.thismoodbutton.mood_name = MoodLayout.this.name.getText().toString().trim();
                    MoodLayout.this.thismoodbutton.mood_icon = MoodLayout.this.iconrount;
                    MoodLayout.this.setcontent(MoodLayout.this.thismoodbutton);
                }
            }
        };
        this.getmoodcommandrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MoodLayout.5
            @Override // java.lang.Runnable
            public void run() {
                List<Savemood> listQuerymood = MainActivity.mgr.querymood();
                for (int i = 0; i < listQuerymood.size(); i++) {
                    if (listQuerymood.get(i).room_id == MoodLayout.this.thismoodbutton.room_id && listQuerymood.get(i).mood_id == MoodLayout.this.thismoodbutton.mood_id) {
                        MoodLayout.this.thismood.add(listQuerymood.get(i));
                    }
                }
            }
        };
        this.sentingcommandrun = new Runnable() { // from class: com.shima.smartbushome.selflayout.MoodLayout.6
            @Override // java.lang.Runnable
            public void run() {
                if (MoodLayout.this.senting) {
                    MoodLayout.this.sentcount++;
                    MoodLayout.this.senting = false;
                    if (MoodLayout.this.sentcount >= MoodLayout.this.thismood.size()) {
                        MoodLayout.this.senthandler.removeCallbacks(MoodLayout.this.sentingcommandrun);
                        MoodLayout.this.sentcount = 0;
                        return;
                    } else {
                        MoodLayout.this.senthandler.postDelayed(MoodLayout.this.sentingcommandrun, 75L);
                        return;
                    }
                }
                if (MoodLayout.this.thismood.size() > 0) {
                    MoodLayout.this.sentcommand();
                }
                MoodLayout.this.senthandler.postDelayed(MoodLayout.this.sentingcommandrun, 450L);
                MoodLayout.this.senting = true;
            }
        };
        this.sentcount = 0;
        this.senting = false;
        this.rootcontext = context;
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.mood_layout, this);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.tv_moodremark = (TextView) this.view.findViewById(R.id.mood_text);
        this.iv_moodicon = (ImageView) this.view.findViewById(R.id.mood_image);
        this.cb_mood = (CheckBox) this.view.findViewById(R.id.mooddeletecb);
        this.delete = (LinearLayout) this.view.findViewById(R.id.mood_delete);
        SwipeLayout.addSwipeView((SwipeLayout) findViewById(R.id.moodswipe));
        this.moodlinear = (LinearLayout) this.view.findViewById(R.id.moodlinear);
        this.moodlinear.setOnClickListener(this);
        this.moodlinear.setOnLongClickListener(this);
        this.moodlinear.setOnTouchListener(this.touchcolor);
        this.delete.setOnClickListener(this);
        this.mc = new moodcontrol();
        this.lc = new lightcontrol();
        this.acc = new ACcontrol();
        this.muc = new musiccontrol();
        this.rc = new radiocontrol();
        this.aic = new audio_incontrol();
        this.ctc = new curtaincontrol();
        this.getcommandhandler.postDelayed(this.getmoodcommandrun, 30L);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.mood_delete) {
            MainActivity.mgr.deletemoodbutton("moodbutton", this.thismoodbutton.mood_id, this.thismoodbutton.room_id);
            MainActivity.mgr.deletemood("mood", this.thismoodbutton.mood_id, this.thismoodbutton.room_id);
            broadcastUpdate(FounctionActivity.ACTION_DELETEMOOD);
            Toast.makeText(this.rootcontext, "delete succeed", 0).show();
            return;
        }
        if (id != R.id.moodlinear) {
            return;
        }
        if (this.deletemode) {
            this.cb_mood.setChecked(!this.cb_mood.isChecked());
            return;
        }
        if (this.thismood.size() == 0) {
            this.getcommandhandler.postDelayed(this.getmoodcommandrun, 30L);
        }
        this.senthandler.postDelayed(this.sentingcommandrun, 50L);
    }

    private void broadcastUpdate(String str) {
        FounctionActivity.fcontext.sendBroadcast(new Intent(str));
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (view.getId() != R.id.moodlinear || this.deletemode) {
            return true;
        }
        showPopupMenu(this.moodlinear);
        return true;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.mood_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new AnonymousClass3());
        popupMenu.show();
    }

    /* renamed from: com.shima.smartbushome.selflayout.MoodLayout$3, reason: invalid class name */
    class AnonymousClass3 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass3() {
        }

        @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            MoodLayout.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, MoodLayout.this.rootcontext, AlertView.Style.Alert, MoodLayout.this.settingclick);
            MoodLayout.this.selfview = MoodLayout.this.inflater.inflate(R.layout.setting_moodinfo, (ViewGroup) null);
            final ImageView imageView = (ImageView) MoodLayout.this.selfview.findViewById(R.id.imageView7);
            MoodLayout.this.name = (EditText) MoodLayout.this.selfview.findViewById(R.id.changemoodname);
            MoodLayout.this.name.setText(MoodLayout.this.thismoodbutton.mood_name);
            MoodLayout.this.iconrount = MoodLayout.this.thismoodbutton.mood_icon;
            imageView.setImageDrawable(MoodLayout.this.getResources().getDrawable(MoodLayout.getResourdIdByResourdName(MoodLayout.this.rootcontext, MoodLayout.this.thismoodbutton.mood_icon)));
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.MoodLayout.3.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    MoodLayout.this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, MoodLayout.this.rootcontext, AlertView.Style.Alert, MoodLayout.this.settingclick);
                    View viewInflate = MoodLayout.this.inflater.inflate(R.layout.mood_icon_select, (ViewGroup) null);
                    GridView gridView = (GridView) viewInflate.findViewById(R.id.gridView2);
                    gridView.setAdapter((ListAdapter) new MoodIconAdapter(MoodLayout.this.rootcontext));
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.MoodLayout.3.1.1
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                            MoodLayout.this.iconrount = (String) MoodLayout.this.iconarray.get(i);
                            imageView.setImageDrawable(MoodLayout.this.getResources().getDrawable(MoodLayout.getResourdIdByResourdName(MoodLayout.this.rootcontext, (String) MoodLayout.this.iconarray.get(i))));
                            MoodLayout.this.iconalter.dismiss();
                        }
                    });
                    MoodLayout.this.iconalter.addExtView(viewInflate);
                    MoodLayout.this.iconalter.show();
                }
            });
            MoodLayout.this.settingalter.addExtView(MoodLayout.this.selfview);
            MoodLayout.this.settingalter.show();
            return false;
        }
    }

    public void setDeletemode(boolean z) {
        this.deletemode = z;
    }

    public void seticon(Drawable drawable) {
        this.iv_moodicon.setImageDrawable(drawable);
    }

    public void setitile(String str) {
        this.tv_moodremark.setText(str);
    }

    public void setcontent(Savemoodbutton savemoodbutton) {
        this.thismoodbutton = savemoodbutton;
        setitile(this.thismoodbutton.mood_name);
        this.moodlinear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        seticon(getResources().getDrawable(getResourdIdByResourdName(this.rootcontext, this.thismoodbutton.mood_icon)));
    }

    public void setcheck(boolean z) {
        this.cb_mood.setChecked(z);
    }

    public int getMoodID() {
        return this.thismoodbutton.mood_id;
    }

    public int getRoomid() {
        return this.thismoodbutton.room_id;
    }

    public void setdeleteable(boolean z) {
        if (z) {
            this.cb_mood.setVisibility(0);
        } else {
            this.cb_mood.setVisibility(4);
        }
    }

    public boolean getchoosestate() {
        return this.cb_mood.isChecked();
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

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    public void sentcommand() {
        Savemood savemood = this.thismood.get(this.sentcount);
        switch (savemood.control_type) {
            case 1:
                if (savemood.value_6 == 0) {
                    this.lc.SingleChannelControl((byte) savemood.subnetID, (byte) savemood.deviceID, savemood.value_1, savemood.value_2, MainActivity.mydupsocket);
                    break;
                } else {
                    this.lc.ARGBlightcontrol((byte) savemood.subnetID, (byte) savemood.deviceID, ToColor(new byte[]{(byte) savemood.value_1, (byte) savemood.value_2, (byte) savemood.value_3, (byte) savemood.value_4}), MainActivity.mydupsocket);
                    break;
                }
            case 2:
                switch (savemood.value_1) {
                    case 1:
                        this.acc.ACControl((byte) savemood.subnetID, (byte) savemood.deviceID, 3, savemood.value_2, MainActivity.mydupsocket);
                        break;
                    case 2:
                        this.acc.ACControl((byte) savemood.subnetID, (byte) savemood.deviceID, 6, savemood.value_2, MainActivity.mydupsocket);
                        break;
                    case 3:
                        int i = savemood.value_2;
                        if (i != 3) {
                            switch (i) {
                                case 0:
                                    this.acc.ACControl((byte) savemood.subnetID, (byte) savemood.deviceID, 4, savemood.value_3, MainActivity.mydupsocket);
                                    break;
                                case 1:
                                    this.acc.ACControl((byte) savemood.subnetID, (byte) savemood.deviceID, 7, savemood.value_3, MainActivity.mydupsocket);
                                    break;
                            }
                        } else {
                            this.acc.ACControl((byte) savemood.subnetID, (byte) savemood.deviceID, 8, savemood.value_3, MainActivity.mydupsocket);
                            break;
                        }
                        break;
                    case 4:
                        this.acc.ACControl((byte) savemood.subnetID, (byte) savemood.deviceID, 5, savemood.value_2, MainActivity.mydupsocket);
                        break;
                }
            case 3:
                int i2 = savemood.value_1;
                if (i2 == 1) {
                    if (savemood.value_6 == 1) {
                        this.muc.SwitchtoMusicSD((byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                        break;
                    } else if (savemood.value_6 == 2) {
                        this.muc.MusicControl((byte) this.VoluneControl, (byte) 1, (byte) 3, (byte) savemood.value_5, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                        break;
                    } else if (savemood.value_6 == 3) {
                        byte[] bArr = {(byte) ((savemood.value_3 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (savemood.value_3 - (65280 & savemood.value_3))};
                        this.muc.MusicControl((byte) this.ControlSpecSong, (byte) savemood.value_2, bArr[0], bArr[1], (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                        break;
                    } else if (savemood.value_6 == 99) {
                        this.muc.MusicControl((byte) this.PlayControl, (byte) 4, (byte) 0, (byte) 0, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                        break;
                    }
                } else {
                    switch (i2) {
                        case 5:
                            if (savemood.value_6 == 1) {
                                this.rc.SwitchtoRadio((byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            } else if (savemood.value_6 == 2) {
                                this.rc.MusicControl((byte) this.VoluneControl, (byte) 1, (byte) 3, (byte) savemood.value_5, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            } else if (savemood.value_6 == 3) {
                                this.rc.MusicControl((byte) this.AlbumorRadioControl, (byte) 6, (byte) savemood.value_4, (byte) 0, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            } else if (savemood.value_6 == 4) {
                                this.rc.MusicControl((byte) this.PlayControl, (byte) 3, (byte) 0, (byte) 0, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            } else if (savemood.value_6 == 99) {
                                this.rc.MusicControl((byte) this.PlayControl, (byte) 4, (byte) 0, (byte) 0, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            }
                            break;
                        case 6:
                            if (savemood.value_6 == 1) {
                                this.aic.SwitchtoAudioIn((byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            } else if (savemood.value_6 == 2) {
                                this.aic.MusicControl((byte) this.VoluneControl, (byte) 1, (byte) 3, (byte) savemood.value_5, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            } else if (savemood.value_6 == 3) {
                                this.aic.MusicControl((byte) this.PlayControl, (byte) 3, (byte) 0, (byte) 0, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            } else if (savemood.value_6 == 99) {
                                this.aic.MusicControl((byte) this.PlayControl, (byte) 4, (byte) 0, (byte) 0, (byte) savemood.subnetID, (byte) savemood.deviceID, MainActivity.mydupsocket);
                                break;
                            }
                            break;
                    }
                }
                break;
            case 4:
                this.lc.SingleChannelControl((byte) savemood.subnetID, (byte) savemood.deviceID, savemood.value_1, savemood.value_2, MainActivity.mydupsocket);
                break;
        }
    }

    public int ToColor(byte[] bArr) {
        int i = ((bArr[0] & 255) * 255) / 100;
        int i2 = ((bArr[1] & 255) * 255) / 100;
        int i3 = ((bArr[2] & 255) * 255) / 100;
        String hexString = Integer.toHexString(i);
        switch (hexString.length()) {
            case 0:
                hexString = "00";
                break;
            case 1:
                hexString = "0" + hexString;
                break;
        }
        String hexString2 = Integer.toHexString(i2);
        switch (hexString2.length()) {
            case 0:
                hexString2 = "00";
                break;
            case 1:
                hexString2 = "0" + hexString2;
                break;
        }
        String hexString3 = Integer.toHexString(i3);
        switch (hexString3.length()) {
            case 0:
                hexString3 = "00";
                break;
            case 1:
                hexString3 = "0" + hexString3;
                break;
        }
        return Color.argb(255, Integer.parseInt(hexString, 16), Integer.parseInt(hexString2, 16), Integer.parseInt(hexString3, 16));
    }
}
