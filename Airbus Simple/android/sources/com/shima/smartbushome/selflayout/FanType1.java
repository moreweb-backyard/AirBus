package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.Adapter.DeviceListAdapter;
import com.shima.smartbushome.assist.SwipeLayout;
import com.shima.smartbushome.database.Savefan;
import com.shima.smartbushome.founction_command.fancontrol;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FanType1 extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
    EditText cha;
    private String[] colorarray;
    LinearLayout delete;
    boolean deletemode;
    CheckBox deleteview;
    EditText dev;
    Savefan fancontent;
    LinearLayout fanshowpopup;
    fancontrol fc;
    private List<String> iconarray;
    String iconstring;
    private ImageView im_image;
    LayoutInflater inflater;
    public OnItemClickListener itemclick;
    EditText name;
    AlertView pairalter;
    boolean receiveChange;
    Context rootcontext;
    private SeekBar sb_process;
    private SeekBar.OnSeekBarChangeListener seekbarchange;
    AlertView settingalter;
    EditText sub;
    private TextView tv_remark;
    LinearLayout type1linear;
    View view;

    public FanType1(Context context) {
        super(context);
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.FanType1.1
            {
                add("fan_icon1");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.deletemode = false;
        this.receiveChange = false;
        this.fc = new fancontrol();
        this.iconstring = "fan_icon1";
        this.seekbarchange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.FanType1.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                FanType1.this.fc.FanChannelControl((byte) FanType1.this.fancontent.subnetID, (byte) FanType1.this.fancontent.deviceID, FanType1.this.fancontent.channel, FanType1.this.sb_process.getProgress(), MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (FanType1.this.receiveChange) {
                    FanType1.this.receiveChange = false;
                } else {
                    FanType1.this.fc.FanChannelControl((byte) FanType1.this.fancontent.subnetID, (byte) FanType1.this.fancontent.deviceID, FanType1.this.fancontent.channel, i, MainActivity.mydupsocket);
                }
            }
        };
        this.itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.FanType1.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj != FanType1.this.settingalter) {
                    AlertView alertView = FanType1.this.pairalter;
                    return;
                }
                if (i == 0) {
                    Savefan savefan = new Savefan();
                    savefan.room_id = FanType1.this.fancontent.room_id;
                    savefan.fan_id = FanType1.this.fancontent.fan_id;
                    savefan.subnetID = Integer.parseInt(FanType1.this.sub.getText().toString().trim());
                    savefan.deviceID = Integer.parseInt(FanType1.this.dev.getText().toString().trim());
                    savefan.channel = Integer.parseInt(FanType1.this.cha.getText().toString().trim());
                    savefan.fan_statement = FanType1.this.name.getText().toString().trim();
                    MainActivity.mgr.updatefan(savefan);
                    FanType1.this.fancontent.subnetID = Integer.parseInt(FanType1.this.sub.getText().toString().trim());
                    FanType1.this.fancontent.deviceID = Integer.parseInt(FanType1.this.dev.getText().toString().trim());
                    FanType1.this.fancontent.channel = Integer.parseInt(FanType1.this.cha.getText().toString().trim());
                    FanType1.this.fancontent.fan_statement = FanType1.this.name.getText().toString().trim();
                    FanType1.this.setremark(FanType1.this.fancontent.fan_statement);
                }
            }
        };
        initview(context);
    }

    public FanType1(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.selflayout.FanType1.1
            {
                add("fan_icon1");
            }
        };
        this.colorarray = new String[]{"FF2176BC", "FF8E4E87", "FFB62F32", "FF7BC176", "FFEB6A68", "FFF08519", "FFFAC65A"};
        this.deletemode = false;
        this.receiveChange = false;
        this.fc = new fancontrol();
        this.iconstring = "fan_icon1";
        this.seekbarchange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.FanType1.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                FanType1.this.fc.FanChannelControl((byte) FanType1.this.fancontent.subnetID, (byte) FanType1.this.fancontent.deviceID, FanType1.this.fancontent.channel, FanType1.this.sb_process.getProgress(), MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (FanType1.this.receiveChange) {
                    FanType1.this.receiveChange = false;
                } else {
                    FanType1.this.fc.FanChannelControl((byte) FanType1.this.fancontent.subnetID, (byte) FanType1.this.fancontent.deviceID, FanType1.this.fancontent.channel, i, MainActivity.mydupsocket);
                }
            }
        };
        this.itemclick = new OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.FanType1.4
            @Override // com.bigkoo.alertview.OnItemClickListener
            public void onItemClick(Object obj, int i) {
                if (obj != FanType1.this.settingalter) {
                    AlertView alertView = FanType1.this.pairalter;
                    return;
                }
                if (i == 0) {
                    Savefan savefan = new Savefan();
                    savefan.room_id = FanType1.this.fancontent.room_id;
                    savefan.fan_id = FanType1.this.fancontent.fan_id;
                    savefan.subnetID = Integer.parseInt(FanType1.this.sub.getText().toString().trim());
                    savefan.deviceID = Integer.parseInt(FanType1.this.dev.getText().toString().trim());
                    savefan.channel = Integer.parseInt(FanType1.this.cha.getText().toString().trim());
                    savefan.fan_statement = FanType1.this.name.getText().toString().trim();
                    MainActivity.mgr.updatefan(savefan);
                    FanType1.this.fancontent.subnetID = Integer.parseInt(FanType1.this.sub.getText().toString().trim());
                    FanType1.this.fancontent.deviceID = Integer.parseInt(FanType1.this.dev.getText().toString().trim());
                    FanType1.this.fancontent.channel = Integer.parseInt(FanType1.this.cha.getText().toString().trim());
                    FanType1.this.fancontent.fan_statement = FanType1.this.name.getText().toString().trim();
                    FanType1.this.setremark(FanType1.this.fancontent.fan_statement);
                }
            }
        };
        initview(context);
    }

    public void initview(Context context) {
        this.view = View.inflate(context, R.layout.fan_type1, this);
        this.rootcontext = context;
        this.sb_process = (SeekBar) this.view.findViewById(R.id.fantype1seekbar);
        this.sb_process.setOnSeekBarChangeListener(this.seekbarchange);
        this.deleteview = (CheckBox) this.view.findViewById(R.id.fantype1_cb);
        this.delete = (LinearLayout) this.view.findViewById(R.id.fantype1_delete);
        SwipeLayout.addSwipeView((SwipeLayout) findViewById(R.id.fantype1swipe));
        this.im_image = (ImageView) this.view.findViewById(R.id.fantype1_img);
        this.tv_remark = (TextView) this.view.findViewById(R.id.fantype1_name);
        this.type1linear = (LinearLayout) this.view.findViewById(R.id.fantype1linear);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.fanshowpopup = (LinearLayout) this.view.findViewById(R.id.fanshowpopup);
        this.fanshowpopup.setOnLongClickListener(this);
        this.delete.setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.fantype1_delete) {
            return;
        }
        MainActivity.mgr.deletefan("fan", this.fancontent.fan_id, this.fancontent.room_id);
        broadcastUpdate(FounctionActivity.ACTION_DELETEFAN);
        Toast.makeText(this.rootcontext, "delete succeed", 0).show();
    }

    private void broadcastUpdate(String str) {
        FounctionActivity.fcontext.sendBroadcast(new Intent(str));
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        if (MainActivity.islockchangeid) {
            return true;
        }
        showPopupMenu(this.fanshowpopup);
        return true;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this.rootcontext, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.shima.smartbushome.selflayout.FanType1.3
            @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_open /* 2131230749 */:
                        FanType1.this.settingalter = new AlertView("Settings", null, "CANCEL", new String[]{"SAVE"}, null, FanType1.this.rootcontext, AlertView.Style.Alert, FanType1.this.itemclick);
                        View viewInflate = FanType1.this.inflater.inflate(R.layout.setting_faninfo, (ViewGroup) null);
                        FanType1.this.sub = (EditText) viewInflate.findViewById(R.id.setfaninfo_sub);
                        FanType1.this.dev = (EditText) viewInflate.findViewById(R.id.setfaninfo_dev);
                        FanType1.this.cha = (EditText) viewInflate.findViewById(R.id.setfaninfo_chan);
                        FanType1.this.name = (EditText) viewInflate.findViewById(R.id.setfaninfo_name);
                        FanType1.this.sub.setText(String.valueOf(FanType1.this.fancontent.subnetID));
                        FanType1.this.dev.setText(String.valueOf(FanType1.this.fancontent.deviceID));
                        FanType1.this.cha.setText(String.valueOf(FanType1.this.fancontent.channel));
                        FanType1.this.name.setText(FanType1.this.fancontent.fan_statement);
                        FanType1.this.settingalter.addExtView(viewInflate);
                        FanType1.this.settingalter.show();
                        break;
                    case R.id.action_pair /* 2131230750 */:
                        FanType1.this.pairalter = new AlertView("Select Device", null, "CANCEL", null, null, FanType1.this.rootcontext, AlertView.Style.Alert, FanType1.this.itemclick);
                        View viewInflate2 = FanType1.this.inflater.inflate(R.layout.auto_pair_dialog, (ViewGroup) null);
                        ListView listView = (ListView) viewInflate2.findViewById(R.id.listView4);
                        listView.setAdapter((ListAdapter) new DeviceListAdapter(FanType1.this.rootcontext, MainActivity.netdeviceList));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.selflayout.FanType1.3.1
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                Savefan savefan = new Savefan();
                                savefan.room_id = FanType1.this.fancontent.room_id;
                                savefan.fan_id = FanType1.this.fancontent.fan_id;
                                savefan.channel = FanType1.this.fancontent.channel;
                                savefan.fan_statement = FanType1.this.fancontent.fan_statement;
                                savefan.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                                savefan.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                                MainActivity.mgr.updatefan(savefan);
                                Toast.makeText(FanType1.this.rootcontext, "apir " + MainActivity.netdeviceList.get(i).get("devicename") + " succeed", 0).show();
                                FanType1.this.fancontent.subnetID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("subnetID"));
                                FanType1.this.fancontent.deviceID = Integer.parseInt(MainActivity.netdeviceList.get(i).get("deviceID"));
                                FanType1.this.pairalter.dismiss();
                            }
                        });
                        FanType1.this.pairalter.addExtView(viewInflate2);
                        FanType1.this.pairalter.show();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
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

    public void setimage(int i) {
        this.im_image.setImageResource(i);
    }

    public void setremark(String str) {
        this.tv_remark.setText(str);
    }

    public void setReceiveChange(byte b) {
        this.receiveChange = true;
        int i = b & 255;
        if (i == this.sb_process.getProgress()) {
            this.receiveChange = false;
        } else {
            setprocess(i);
        }
    }

    public int getprocess() {
        return this.sb_process.getProgress();
    }

    public int getType1fanid() {
        return this.fancontent.fan_id;
    }

    public boolean getIfneedtoDelete() {
        return this.deleteview.isChecked();
    }

    public void setprocess(int i) {
        this.sb_process.setProgress(i);
    }

    public int getsubid() {
        return this.fancontent.subnetID;
    }

    public int getdevid() {
        return this.fancontent.deviceID;
    }

    public int getchannel() {
        return this.fancontent.channel;
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

    public void setcontant(Savefan savefan) {
        this.fancontent = savefan;
        setremark(savefan.fan_statement);
        this.type1linear.setBackground(getResources().getDrawable(R.drawable.control_back_10));
        setimage(getResourdIdByResourdName(this.rootcontext, this.fancontent.fan_icon));
    }
}
