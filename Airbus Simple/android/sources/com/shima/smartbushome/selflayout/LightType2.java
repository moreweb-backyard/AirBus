package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.Adapter.LightIconAdapter;
import com.shima.smartbushome.assist.SwipeLayout;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.founction_command.lightcontrol;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LightType2 extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    EditText cha;
    private String[] colorarray;
    LinearLayout delete;
    boolean deletemode;
    CheckBox deleteview;
    EditText dev;
    AlertView iconalter;
    private List<String> iconarray;
    String iconstring;
    private ImageView im_image;
    LayoutInflater inflater;
    lightcontrol lg;
    Savelight lightcontent;
    EditText name;
    boolean receiveChange;
    private String remark;
    Context rootcontext;
    private SeekBar sb_process;
    private SeekBar.OnSeekBarChangeListener seekbarchange;
    AlertView settingalter;
    public OnItemClickListener settingclick;
    private String state;
    EditText sub;
    private TextView tv_remark;
    private TextView tv_state;
    RadioButton type1;
    RadioButton type2;
    LinearLayout type2linear;
    RadioButton type3;
    RadioButton type4;
    View view;

    public LightType2(Context context) {
        super(context);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.LightType2.1
            {
                add("light_icon1");
                add("light_icon2");
                add("light_icon3");
                add("light_icon4");
                add("light_icon5");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "light_icon1";
        this.seekbarchange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.LightType2.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                LightType2.this.HandleLightDimming(seekBar.getProgress());
                LightType2.this.setstate(seekBar.getProgress() + "%");
                if (seekBar.getProgress() > 0) {
                    LightType2.this.setimage(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, LightType2.this.lightcontent.light_icon + "_on"));
                    return;
                }
                LightType2.this.setimage(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, LightType2.this.lightcontent.light_icon + "_off"));
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (LightType2.this.receiveChange) {
                    LightType2.this.receiveChange = false;
                    return;
                }
                LightType2.this.setstate(i + "%");
            }
        };
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType2.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == LightType2.this.settingalter && i == 0) {
                    Savelight savelight = new Savelight();
                    savelight.room_id = LightType2.this.lightcontent.room_id;
                    savelight.light_id = LightType2.this.lightcontent.light_id;
                    savelight.subnetID = Integer.parseInt(LightType2.this.sub.getText().toString().trim());
                    savelight.deviceID = Integer.parseInt(LightType2.this.dev.getText().toString().trim());
                    savelight.channel = Integer.parseInt(LightType2.this.cha.getText().toString().trim());
                    savelight.light_statement = LightType2.this.name.getText().toString().trim();
                    savelight.light_icon = LightType2.this.iconstring;
                    if (LightType2.this.type1.isChecked()) {
                        savelight.lightType = 1;
                    } else if (LightType2.this.type2.isChecked()) {
                        savelight.lightType = 2;
                    } else if (LightType2.this.type3.isChecked()) {
                        savelight.lightType = 3;
                    } else if (LightType2.this.type4.isChecked()) {
                        savelight.lightType = 4;
                    }
                    MainActivity.mgr.updatelight(savelight);
                    LightType2.this.lightcontent.subnetID = Integer.parseInt(LightType2.this.sub.getText().toString().trim());
                    LightType2.this.lightcontent.deviceID = Integer.parseInt(LightType2.this.dev.getText().toString().trim());
                    LightType2.this.lightcontent.channel = Integer.parseInt(LightType2.this.cha.getText().toString().trim());
                    LightType2.this.lightcontent.light_statement = LightType2.this.name.getText().toString().trim();
                    LightType2.this.lightcontent.light_icon = LightType2.this.iconstring;
                    LightType2.this.setremark(LightType2.this.lightcontent.light_statement);
                    LightType2.this.setimage(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, LightType2.this.iconstring + "_off"));
                    if (savelight.lightType != 2) {
                        LightType2.this.broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
                    }
                }
            }
        };
        this.rootcontext = context;
        initview(context);
    }

    public LightType2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.deletemode = false;
        this.receiveChange = false;
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.LightType2.1
            {
                add("light_icon1");
                add("light_icon2");
                add("light_icon3");
                add("light_icon4");
                add("light_icon5");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.iconstring = "light_icon1";
        this.seekbarchange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.LightType2.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                LightType2.this.HandleLightDimming(seekBar.getProgress());
                LightType2.this.setstate(seekBar.getProgress() + "%");
                if (seekBar.getProgress() > 0) {
                    LightType2.this.setimage(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, LightType2.this.lightcontent.light_icon + "_on"));
                    return;
                }
                LightType2.this.setimage(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, LightType2.this.lightcontent.light_icon + "_off"));
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (LightType2.this.receiveChange) {
                    LightType2.this.receiveChange = false;
                    return;
                }
                LightType2.this.setstate(i + "%");
            }
        };
        this.settingclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType2.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj == LightType2.this.settingalter && i == 0) {
                    Savelight savelight = new Savelight();
                    savelight.room_id = LightType2.this.lightcontent.room_id;
                    savelight.light_id = LightType2.this.lightcontent.light_id;
                    savelight.subnetID = Integer.parseInt(LightType2.this.sub.getText().toString().trim());
                    savelight.deviceID = Integer.parseInt(LightType2.this.dev.getText().toString().trim());
                    savelight.channel = Integer.parseInt(LightType2.this.cha.getText().toString().trim());
                    savelight.light_statement = LightType2.this.name.getText().toString().trim();
                    savelight.light_icon = LightType2.this.iconstring;
                    if (LightType2.this.type1.isChecked()) {
                        savelight.lightType = 1;
                    } else if (LightType2.this.type2.isChecked()) {
                        savelight.lightType = 2;
                    } else if (LightType2.this.type3.isChecked()) {
                        savelight.lightType = 3;
                    } else if (LightType2.this.type4.isChecked()) {
                        savelight.lightType = 4;
                    }
                    MainActivity.mgr.updatelight(savelight);
                    LightType2.this.lightcontent.subnetID = Integer.parseInt(LightType2.this.sub.getText().toString().trim());
                    LightType2.this.lightcontent.deviceID = Integer.parseInt(LightType2.this.dev.getText().toString().trim());
                    LightType2.this.lightcontent.channel = Integer.parseInt(LightType2.this.cha.getText().toString().trim());
                    LightType2.this.lightcontent.light_statement = LightType2.this.name.getText().toString().trim();
                    LightType2.this.lightcontent.light_icon = LightType2.this.iconstring;
                    LightType2.this.setremark(LightType2.this.lightcontent.light_statement);
                    LightType2.this.setimage(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, LightType2.this.iconstring + "_off"));
                    if (savelight.lightType != 2) {
                        LightType2.this.broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
                    }
                }
            }
        };
        this.rootcontext = context;
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.light_type2, this);
        this.lg = new lightcontrol();
        this.type2linear = (LinearLayout) this.view.findViewById(R.id.type2linear);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.tv_remark = (TextView) this.view.findViewById(R.id.remark2);
        this.tv_state = (TextView) this.view.findViewById(R.id.state2);
        this.im_image = (ImageView) this.view.findViewById(R.id.imageView2);
        this.sb_process = (SeekBar) this.view.findViewById(R.id.seekBar);
        this.delete = (LinearLayout) this.view.findViewById(R.id.lighttype2_delete);
        SwipeLayout.addSwipeView((SwipeLayout) findViewById(R.id.lighttype2swipe));
        this.deleteview = (CheckBox) this.view.findViewById(R.id.checkBox2);
        this.im_image.setOnClickListener(this);
        this.sb_process.setOnSeekBarChangeListener(this.seekbarchange);
        this.tv_remark.setOnLongClickListener(this);
        this.delete.setOnClickListener(this);
        this.tv_state.setText(EnvironmentCompat.MEDIA_UNKNOWN);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.imageView2) {
            if (id != R.id.lighttype2_delete) {
                return;
            }
            MainActivity.mgr.deletelight("light", this.lightcontent.light_id, this.lightcontent.room_id);
            broadcastUpdate(FounctionActivity.ACTION_DELETELIGHT);
            Toast.makeText(this.rootcontext, "delete succeed", 0).show();
            return;
        }
        if (getprocess() > 0) {
            setprocess(0);
            HandleLightDimming(0);
        } else {
            setprocess(100);
            HandleLightDimming(100);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str) {
        FounctionActivity.fcontext.sendBroadcast(new Intent(str));
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (MainActivity.islockchangeid) {
            return true;
        }
        showPopupMenu(this.tv_remark);
        return true;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new AnonymousClass3());
        popupMenu.show();
    }

    /* renamed from: com.shima.smartbushome.selflayout.LightType2$3, reason: invalid class name */
    class AnonymousClass3 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass3() {
        }

        @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_open /* 2131230749 */:
                    LightType2.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, LightType2.this.rootcontext, AlertView.Style.Alert, LightType2.this.settingclick);
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(LightType2.this.rootcontext).inflate(R.layout.setting_lightinfo, (ViewGroup) null);
                    LightType2.this.sub = (EditText) viewGroup.findViewById(R.id.subedit);
                    LightType2.this.dev = (EditText) viewGroup.findViewById(R.id.deviceedit);
                    LightType2.this.cha = (EditText) viewGroup.findViewById(R.id.chanedit);
                    LightType2.this.name = (EditText) viewGroup.findViewById(R.id.lightnameedit);
                    LightType2.this.type1 = (RadioButton) viewGroup.findViewById(R.id.lighttype1);
                    LightType2.this.type2 = (RadioButton) viewGroup.findViewById(R.id.lighttype2);
                    LightType2.this.type3 = (RadioButton) viewGroup.findViewById(R.id.lighttype3);
                    LightType2.this.type4 = (RadioButton) viewGroup.findViewById(R.id.lighttype4);
                    LightType2.this.type2.setChecked(true);
                    final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.imageView10);
                    LightType2.this.sub.setText(String.valueOf(LightType2.this.lightcontent.subnetID));
                    LightType2.this.dev.setText(String.valueOf(LightType2.this.lightcontent.deviceID));
                    LightType2.this.cha.setText(String.valueOf(LightType2.this.lightcontent.channel));
                    LightType2.this.name.setText(LightType2.this.lightcontent.light_statement);
                    imageView.setImageResource(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, LightType2.this.lightcontent.light_icon + "_on"));
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.LightType2.3.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            LightType2.this.iconalter = new AlertView("Icon Selection", null, "CANCEL", null, null, LightType2.this.rootcontext, AlertView.Style.Alert, LightType2.this.settingclick);
                            View viewInflate = LightType2.this.inflater.inflate(R.layout.mood_icon_select, (ViewGroup) null);
                            GridView gridView = (GridView) viewInflate.findViewById(R.id.gridView2);
                            gridView.setAdapter((ListAdapter) new LightIconAdapter(LightType2.this.rootcontext));
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType2.3.1.1
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                    LightType2.this.iconstring = (String) LightType2.this.iconarray.get(i);
                                    imageView.setImageResource(LightType2.getResourdIdByResourdName(LightType2.this.rootcontext, ((String) LightType2.this.iconarray.get(i)) + "_on"));
                                    LightType2.this.iconalter.dismiss();
                                }
                            });
                            LightType2.this.iconalter.addExtView(viewInflate);
                            LightType2.this.iconalter.show();
                        }
                    });
                    LightType2.this.settingalter.addExtView(viewGroup);
                    LightType2.this.settingalter.show();
                    break;
                case R.id.action_pair /* 2131230750 */:
                    final AlertView alertView = new AlertView("Select Device", null, "CANCEL", null, null, LightType2.this.rootcontext, AlertView.Style.Alert, LightType2.this.settingclick);
                    ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(LightType2.this.rootcontext).inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                    ListView listView = (ListView) viewGroup2.findViewById(R.id.listView4);
                    listView.setAdapter((ListAdapter) new DeviceListAdapter(LightType2.this.rootcontext, MainActivity.netdeviceList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.LightType2.3.2
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                            Savelight savelight = new Savelight();
                            savelight.room_id = LightType2.this.lightcontent.room_id;
                            savelight.light_id = LightType2.this.lightcontent.light_id;
                            savelight.light_statement = LightType2.this.lightcontent.light_statement;
                            savelight.channel = LightType2.this.lightcontent.channel;
                            savelight.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            savelight.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            savelight.light_icon = LightType2.this.lightcontent.light_icon;
                            savelight.lightType = LightType2.this.lightcontent.lightType;
                            MainActivity.mgr.updatelight(savelight);
                            Toast.makeText(LightType2.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                            LightType2.this.lightcontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                            LightType2.this.lightcontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                            alertView.dismiss();
                        }
                    });
                    alertView.addExtView(viewGroup2);
                    alertView.show();
                    break;
            }
            return false;
        }
    }

    public void setdeletevisable(boolean z) {
        if (z) {
            this.deleteview.setVisibility(0);
            this.deletemode = true;
            this.deleteview.setChecked(false);
        } else {
            this.deleteview.setVisibility(4);
            this.deletemode = false;
        }
    }

    public void setcontant(Savelight savelight) {
        this.lightcontent = savelight;
        setremark(savelight.light_statement);
        this.type2linear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_off"));
    }

    public void setimage(int i) {
        this.im_image.setImageResource(i);
    }

    public void setremark(String str) {
        this.tv_remark.setText(str);
    }

    public void setstate(String str) {
        this.tv_state.setText(str);
    }

    public void setReceiveChange(byte b) {
        this.receiveChange = true;
        int i = b & 255;
        if (i == this.sb_process.getProgress()) {
            setstate(String.valueOf(i) + "%");
            if (i > 0) {
                setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_on"));
            } else {
                setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_off"));
            }
            this.receiveChange = false;
            return;
        }
        setstate(String.valueOf(i) + "%");
        if (i > 0) {
            setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_on"));
        } else {
            setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_off"));
        }
        setprocess(i);
    }

    public void setReceiveChange2(byte b) {
        int i = b & 255;
        if (i == 100 && this.sb_process.getProgress() == 0) {
            setstate(String.valueOf(i) + "%");
            setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_on"));
            setprocess(i);
        }
        if (i == 0) {
            setstate(String.valueOf(i) + "%");
            setimage(getResourdIdByResourdName(this.rootcontext, this.lightcontent.light_icon + "_off"));
            setprocess(0);
        }
    }

    public int getprocess() {
        return this.sb_process.getProgress();
    }

    public int getType2lightid() {
        return this.lightcontent.light_id;
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public void setprocess(int i) {
        this.sb_process.setProgress(i);
    }

    public int getsubid() {
        return this.lightcontent.subnetID;
    }

    public int getdevid() {
        return this.lightcontent.deviceID;
    }

    public int getchannel() {
        return this.lightcontent.channel;
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

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }

    public void HandleLightDimming(int i) {
        try {
            this.lg.SingleChannelControl((byte) this.lightcontent.subnetID, (byte) this.lightcontent.deviceID, this.lightcontent.channel, i, MainActivity.mydupsocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
