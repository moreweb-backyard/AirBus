package com.shima.smartbushome;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.shima.smartbushome.R;
import com.shima.smartbushome.about.AboutActivity;
import com.shima.smartbushome.assist.Adapter.AreaAdapter;
import com.shima.smartbushome.assist.Adapter.RoomAdapter;
import com.shima.smartbushome.assist.Adapter.RoomIconAdapter;
import com.shima.smartbushome.assist.MusicNotification;
import com.shima.smartbushome.assist.MusicNotifyReceiver;
import com.shima.smartbushome.assist.holocolorpicker.ColorPicker;
import com.shima.smartbushome.database.SaveArea;
import com.shima.smartbushome.database.Saveroom;
import com.shima.smartbushome.mainsetting.MainSettingActivity;
import com.shima.smartbushome.util.FileUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;

/* loaded from: classes.dex */
public class MainFragment extends Fragment {
    public static final int PHOTO_REQUEST_CAREMA = 1;
    public static final int PHOTO_REQUEST_GALLERY = 2;
    public static final int PHOTO_REQUEST_GALLERY_AREA = 3;
    public static Context maincontext;
    public static List<HashMap<String, String>> netdeviceList = new ArrayList();
    AreaAdapter areaAdapter;
    EditText et_areaName;
    GridView gv_areaList;
    private GridView gv_rooms;
    ImageView iv_backgroud;
    ImageView iv_backgroudArea;
    ImageView iv_gallery;
    ImageView iv_galleryArea;
    private RoomAdapter mAdapter;
    AlertView mAlertView;
    MusicNotifyReceiver mReceiver;
    private AlertView maddingAlertViewExt;
    private AlertView miconAlertViewExt;
    private AlertView mpcolorAlertViewExt;
    private AlertView msettingAlertViewExt;
    EditText name;
    ColorPicker picker;
    TextView setmainbgcolor;
    Spinner sp_areaList;
    View view;
    private SaveArea currPageArea = null;
    Handler handle = new Handler();
    public List<Saveroom> roomdata = new ArrayList();
    private boolean deletechoose = false;
    String roomicon = "room_type1";
    String settingicon = "";
    String roombgcolor = "";
    int settingid = 0;
    private List<String> iconarray = new ArrayList<String>() { // from class: com.shima.smartbushome.MainFragment.1
        {
            add("room_type1");
            add("room_type2");
            add("room_type3");
            add("room_type4");
            add("room_type5");
            add("room_type6");
            add("room_type7");
            add("room_type8");
            add("room_type9");
            add("room_type10");
            add("room_type11");
            add("room_type12");
            add("room_type13");
            add("room_type14");
            add("room_type15");
        }
    };
    boolean exitdialogshow = false;
    AnyLayer areaListDialog = null;
    AnyLayer addAreaDialog = null;
    List<SaveArea> areaList = null;
    String areaBgPath = "";
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { // from class: com.shima.smartbushome.MainFragment.18
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (MainActivity.ACTION_BACKPRESS.equals(intent.getAction())) {
                if (MainFragment.this.exitdialogshow) {
                    if (MusicNotification.manager != null) {
                        MusicNotification.manager.cancel(R.string.app_name);
                    }
                    if (Build.VERSION.SDK_INT > 7) {
                        Intent intent2 = new Intent("android.intent.action.MAIN");
                        intent2.addCategory("android.intent.category.HOME");
                        intent2.setFlags(268435456);
                        MainFragment.this.startActivity(intent2);
                        System.exit(0);
                        return;
                    }
                    FragmentActivity activity = MainFragment.this.getActivity();
                    MainFragment.this.getActivity();
                    ((ActivityManager) activity.getSystemService("activity")).restartPackage(MainFragment.this.getActivity().getPackageName());
                    return;
                }
                MainFragment.this.mAlertView = new AlertView("Warning", "Are you sure to exit?", "CANCEL", new String[]{"YES"}, null, MainFragment.this.getActivity(), AlertView.Style.Alert, MainFragment.this.exitclick);
                MainFragment.this.mAlertView.setCancelable(false);
                MainFragment.this.mAlertView.show();
                MainFragment.this.exitdialogshow = true;
            }
        }
    };
    public OnItemClickListener exitclick = new OnItemClickListener() { // from class: com.shima.smartbushome.MainFragment.19
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i == -1) {
                MainFragment.this.mAlertView.dismiss();
                MainFragment.this.exitdialogshow = false;
                return;
            }
            if (i == 0) {
                if (MusicNotification.manager != null) {
                    MusicNotification.manager.cancel(R.string.app_name);
                }
                if (Build.VERSION.SDK_INT > 7) {
                    Intent intent = new Intent("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.HOME");
                    intent.setFlags(268435456);
                    MainFragment.this.startActivity(intent);
                    System.exit(0);
                    return;
                }
                FragmentActivity activity = MainFragment.this.getActivity();
                MainFragment.this.getActivity();
                ((ActivityManager) activity.getSystemService("activity")).restartPackage(MainFragment.this.getActivity().getPackageName());
            }
        }
    };
    Runnable getroomrun = new Runnable() { // from class: com.shima.smartbushome.MainFragment.20
        /* JADX WARN: Removed duplicated region for block: B:14:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:15:0x00b3  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x0083 -> B:17:0x0079). Please report as a decompilation issue!!! */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                r5 = this;
                com.shima.smartbushome.MainFragment r0 = com.shima.smartbushome.MainFragment.this
                java.util.List<com.shima.smartbushome.database.Saveroom> r0 = r0.roomdata
                int r0 = r0.size()
                if (r0 <= 0) goto L11
                com.shima.smartbushome.MainFragment r0 = com.shima.smartbushome.MainFragment.this
                java.util.List<com.shima.smartbushome.database.Saveroom> r0 = r0.roomdata
                r0.clear()
            L11:
                com.shima.smartbushome.MainFragment r0 = com.shima.smartbushome.MainFragment.this
                com.shima.smartbushome.assist.Adapter.RoomAdapter r0 = com.shima.smartbushome.MainFragment.access$400(r0)
                r0.clear()
                r0 = 0
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.database.SaveArea r1 = com.shima.smartbushome.MainFragment.access$100(r1)     // Catch: java.lang.Exception -> L6a
                if (r1 == 0) goto L58
                android.widget.TextView r1 = com.shima.smartbushome.MainActivity.locationid     // Catch: java.lang.Exception -> L6a
                java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L6a
                r2.<init>()     // Catch: java.lang.Exception -> L6a
                java.lang.String r3 = ""
                r2.append(r3)     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.MainFragment r3 = com.shima.smartbushome.MainFragment.this     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.database.SaveArea r3 = com.shima.smartbushome.MainFragment.access$100(r3)     // Catch: java.lang.Exception -> L6a
                java.lang.String r3 = r3.getArea_name()     // Catch: java.lang.Exception -> L6a
                r2.append(r3)     // Catch: java.lang.Exception -> L6a
                java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L6a
                r1.setText(r2)     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.database.DBManager r2 = com.shima.smartbushome.MainActivity.mgr     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.MainFragment r3 = com.shima.smartbushome.MainFragment.this     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.database.SaveArea r3 = com.shima.smartbushome.MainFragment.access$100(r3)     // Catch: java.lang.Exception -> L6a
                int r3 = r3.getId()     // Catch: java.lang.Exception -> L6a
                java.util.List r2 = r2.queryRoomByAreaId(r3)     // Catch: java.lang.Exception -> L6a
                r1.roomdata = r2     // Catch: java.lang.Exception -> L6a
                goto L79
            L58:
                android.widget.TextView r1 = com.shima.smartbushome.MainActivity.locationid     // Catch: java.lang.Exception -> L6a
                java.lang.String r2 = ""
                r1.setText(r2)     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this     // Catch: java.lang.Exception -> L6a
                com.shima.smartbushome.database.DBManager r2 = com.shima.smartbushome.MainActivity.mgr     // Catch: java.lang.Exception -> L6a
                java.util.List r2 = r2.queryroom()     // Catch: java.lang.Exception -> L6a
                r1.roomdata = r2     // Catch: java.lang.Exception -> L6a
                goto L79
            L6a:
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this
                android.support.v4.app.FragmentActivity r1 = r1.getActivity()
                java.lang.String r2 = "your database is boarken"
                android.widget.Toast r1 = android.widget.Toast.makeText(r1, r2, r0)
                r1.show()
            L79:
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this
                java.util.List<com.shima.smartbushome.database.Saveroom> r1 = r1.roomdata
                int r1 = r1.size()
                if (r0 >= r1) goto Lb3
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this
                com.shima.smartbushome.assist.Adapter.RoomAdapter r1 = com.shima.smartbushome.MainFragment.access$400(r1)
                com.shima.smartbushome.MainFragment r2 = com.shima.smartbushome.MainFragment.this
                java.util.List<com.shima.smartbushome.database.Saveroom> r2 = r2.roomdata
                java.lang.Object r2 = r2.get(r0)
                com.shima.smartbushome.database.Saveroom r2 = (com.shima.smartbushome.database.Saveroom) r2
                java.lang.String r2 = r2.room_name
                com.shima.smartbushome.MainFragment r3 = com.shima.smartbushome.MainFragment.this
                java.util.List<com.shima.smartbushome.database.Saveroom> r3 = r3.roomdata
                java.lang.Object r3 = r3.get(r0)
                com.shima.smartbushome.database.Saveroom r3 = (com.shima.smartbushome.database.Saveroom) r3
                java.lang.String r3 = r3.room_icon
                com.shima.smartbushome.MainFragment r4 = com.shima.smartbushome.MainFragment.this
                java.util.List<com.shima.smartbushome.database.Saveroom> r4 = r4.roomdata
                java.lang.Object r4 = r4.get(r0)
                com.shima.smartbushome.database.Saveroom r4 = (com.shima.smartbushome.database.Saveroom) r4
                java.lang.String r4 = r4.room_icon_bg
                r1.add(r2, r3, r4)
                int r0 = r0 + 1
                goto L79
            Lb3:
                com.shima.smartbushome.MainFragment r0 = com.shima.smartbushome.MainFragment.this
                com.shima.smartbushome.assist.Adapter.RoomAdapter r0 = com.shima.smartbushome.MainFragment.access$400(r0)
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this
                java.util.List<com.shima.smartbushome.database.Saveroom> r1 = r1.roomdata
                r0.setRoomList(r1)
                com.shima.smartbushome.MainFragment r0 = com.shima.smartbushome.MainFragment.this
                android.widget.GridView r0 = com.shima.smartbushome.MainFragment.access$500(r0)
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this
                com.shima.smartbushome.assist.Adapter.RoomAdapter r1 = com.shima.smartbushome.MainFragment.access$400(r1)
                r0.setAdapter(r1)
                com.shima.smartbushome.MainFragment r0 = com.shima.smartbushome.MainFragment.this
                android.os.Handler r0 = r0.handle
                com.shima.smartbushome.MainFragment r1 = com.shima.smartbushome.MainFragment.this
                java.lang.Runnable r1 = r1.getroomrun
                r0.removeCallbacks(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.MainFragment.AnonymousClass20.run():void");
        }
    };
    public AdapterView.OnItemLongClickListener itemlongclick = new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.MainFragment.21
        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
            MainFragment.this.showPopupMenu(view);
            return true;
        }
    };
    int spAreaSelectposition = 0;
    public OnItemClickListener alterclick = new OnItemClickListener() { // from class: com.shima.smartbushome.MainFragment.23
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (obj == MainFragment.this.mpcolorAlertViewExt) {
                if (i == -1) {
                    MainFragment.this.mpcolorAlertViewExt.dismiss();
                    return;
                }
                return;
            }
            if (obj == MainFragment.this.miconAlertViewExt) {
                if (i == -1) {
                    MainFragment.this.miconAlertViewExt.dismiss();
                    return;
                }
                return;
            }
            if (obj != MainFragment.this.msettingAlertViewExt) {
                if (obj == MainFragment.this.maddingAlertViewExt) {
                    if (i == -1) {
                        MainFragment.this.maddingAlertViewExt.dismiss();
                        return;
                    }
                    if (i == 0) {
                        String strTrim = MainFragment.this.name.getText().toString().trim();
                        ArrayList arrayList = new ArrayList();
                        List<Saveroom> listQueryroom = MainActivity.mgr.queryroom();
                        arrayList.add(new Saveroom(listQueryroom.size() == 0 ? 1 : listQueryroom.get(listQueryroom.size() - 1).room_id + 1, strTrim, 0, 0, 0, 0, 0, 0, MainFragment.this.settingicon, MainFragment.this.backgroudPath, 0, 0, 0, 0, MainFragment.this.currPageArea != null ? MainFragment.this.currPageArea.getId() : 0));
                        MainActivity.mgr.addroom(arrayList);
                        MainFragment.this.handle.postDelayed(MainFragment.this.getroomrun, 20L);
                        return;
                    }
                    return;
                }
                return;
            }
            if (i == -1) {
                MainFragment.this.msettingAlertViewExt.dismiss();
                return;
            }
            if (i == 0) {
                Saveroom saveroom = new Saveroom();
                saveroom.room_id = MainFragment.this.roomdata.get(MainFragment.this.settingid).room_id;
                saveroom.room_name = MainFragment.this.name.getText().toString().trim();
                saveroom.room_icon = MainFragment.this.settingicon;
                saveroom.room_icon_bg = MainFragment.this.backgroudPath;
                List<SaveArea> listQueryArea = MainActivity.mgr.queryArea();
                saveroom.area_id = 0;
                try {
                    if (listQueryArea.size() >= MainFragment.this.spAreaSelectposition) {
                        saveroom.area_id = listQueryArea.get(MainFragment.this.spAreaSelectposition).getId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainActivity.mgr.updataroominfo(saveroom);
                MainFragment.this.handle.postDelayed(MainFragment.this.getroomrun, 20L);
            }
        }
    };
    public AdapterView.OnItemClickListener itemclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.MainFragment.24
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            if (MainFragment.this.deletechoose) {
                int i2 = 0;
                while (true) {
                    if (i2 >= MainFragment.this.roomdata.size()) {
                        i2 = 0;
                        break;
                    } else if (((TextView) view.findViewById(R.id.textView12)).getText().toString().trim().equals(MainFragment.this.roomdata.get(i2).room_name)) {
                        break;
                    } else {
                        i2++;
                    }
                }
                MainActivity.mgr.deletefounction("room", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("light", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("hvac", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("mood", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("moodbutton", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("fan", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("curtain", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("music", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("song", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("radio", MainFragment.this.roomdata.get(i2).room_id);
                MainActivity.mgr.deletefc("floorheat", MainFragment.this.roomdata.get(i2).room_id);
                MainFragment.this.deletechoose = false;
                MainFragment.this.handle.postDelayed(MainFragment.this.getroomrun, 20L);
                return;
            }
            int i3 = 0;
            while (true) {
                if (i3 >= MainFragment.this.roomdata.size()) {
                    i3 = 0;
                    break;
                } else if (((TextView) view.findViewById(R.id.textView12)).getText().toString().trim().equals(MainFragment.this.roomdata.get(i3).room_name)) {
                    break;
                } else {
                    i3++;
                }
            }
            Intent intent = new Intent();
            intent.putExtra("roomname", MainFragment.this.roomdata.get(i3).room_name);
            intent.putExtra("roomid", MainFragment.this.roomdata.get(i3).room_id);
            intent.setClass(MainFragment.this.getActivity(), RoomActivity.class);
            MainFragment.this.startActivity(intent);
        }
    };
    private String backgroudPath = "";

    public static MainFragment newInstance(String str) {
        return new MainFragment();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_main, viewGroup, false);
        setHasOptionsMenu(true);
        this.gv_rooms = (GridView) this.view.findViewById(R.id.gv_rooms);
        this.mAdapter = new RoomAdapter(getActivity());
        maincontext = getActivity();
        this.handle.postDelayed(this.getroomrun, 20L);
        this.gv_rooms.setOnItemClickListener(this.itemclick);
        this.gv_rooms.setOnItemLongClickListener(this.itemlongclick);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public void showAddAreaDialog() {
        this.addAreaDialog = AnyLayer.with(getActivity()).contentView(R.layout.view_addarea_dialog).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() { // from class: com.shima.smartbushome.MainFragment.4
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                anyLayer.dismiss();
            }
        }).onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() { // from class: com.shima.smartbushome.MainFragment.3
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                String string;
                SaveArea saveArea = new SaveArea();
                String str = "";
                String str2 = "";
                try {
                    string = MainFragment.this.et_areaName.getText().toString();
                } catch (Exception e) {
                    e = e;
                }
                try {
                    str2 = MainFragment.this.areaBgPath;
                    str = string;
                } catch (Exception e2) {
                    e = e2;
                    str = string;
                    e.printStackTrace();
                    saveArea.setArea_name(str);
                    saveArea.setArea_bg(str2);
                    MainActivity.mgr.addArea(saveArea);
                    MainFragment.this.areaList.clear();
                    MainFragment.this.areaList.addAll(MainActivity.mgr.queryArea());
                    MainFragment.this.areaAdapter.notifyDataSetChanged();
                    anyLayer.dismiss();
                }
                saveArea.setArea_name(str);
                saveArea.setArea_bg(str2);
                MainActivity.mgr.addArea(saveArea);
                MainFragment.this.areaList.clear();
                MainFragment.this.areaList.addAll(MainActivity.mgr.queryArea());
                MainFragment.this.areaAdapter.notifyDataSetChanged();
                anyLayer.dismiss();
            }
        }).contentAnim(new AnyLayer.IAnim() { // from class: com.shima.smartbushome.MainFragment.2
            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long inAnim(View view) {
                AnimHelper.startTopAlphaInAnim(view, 350L);
                return 350L;
            }

            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long outAnim(View view) {
                AnimHelper.startTopAlphaOutAnim(view, 350L);
                return 350L;
            }
        });
        this.et_areaName = (EditText) this.addAreaDialog.getContentView().findViewById(R.id.et_areaName);
        this.iv_backgroudArea = (ImageView) this.addAreaDialog.getContentView().findViewById(R.id.iv_backgroud);
        this.iv_galleryArea = (ImageView) this.addAreaDialog.getContentView().findViewById(R.id.iv_gallery);
        this.iv_galleryArea.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.MainFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainFragment.this.galleryArea();
            }
        });
        this.addAreaDialog.show();
    }

    public void showUpdateAreaDialog(final SaveArea saveArea) {
        this.addAreaDialog = AnyLayer.with(getActivity()).contentView(R.layout.view_addarea_dialog).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).onClick(R.id.fl_dialog_delete, new AnyLayer.OnLayerClickListener() { // from class: com.shima.smartbushome.MainFragment.9
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                MainActivity.mgr.deleteArea(saveArea.getId());
                List<Saveroom> listQueryRoomByAreaId = MainActivity.mgr.queryRoomByAreaId(saveArea.getId());
                for (int i = 0; i < listQueryRoomByAreaId.size(); i++) {
                    MainFragment.this.deleteRoomByRoomId(listQueryRoomByAreaId.get(i).room_id);
                }
                MainFragment.this.areaList.clear();
                MainFragment.this.areaList.addAll(MainActivity.mgr.queryArea());
                MainFragment.this.areaAdapter.notifyDataSetChanged();
                anyLayer.dismiss();
            }
        }).onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() { // from class: com.shima.smartbushome.MainFragment.8
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                anyLayer.dismiss();
            }
        }).onClick(R.id.fl_dialog_yes, new AnyLayer.OnLayerClickListener() { // from class: com.shima.smartbushome.MainFragment.7
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                String str = "";
                String str2 = "";
                try {
                    String string = MainFragment.this.et_areaName.getText().toString();
                    try {
                        str2 = MainFragment.this.areaBgPath;
                        str = string;
                    } catch (Exception e) {
                        e = e;
                        str = string;
                        e.printStackTrace();
                        saveArea.setArea_name(str);
                        saveArea.setArea_bg(str2);
                        MainActivity.mgr.updateArea(saveArea);
                        MainFragment.this.areaList.clear();
                        MainFragment.this.areaList.addAll(MainActivity.mgr.queryArea());
                        MainFragment.this.areaAdapter.notifyDataSetChanged();
                        anyLayer.dismiss();
                    }
                } catch (Exception e2) {
                    e = e2;
                }
                saveArea.setArea_name(str);
                saveArea.setArea_bg(str2);
                MainActivity.mgr.updateArea(saveArea);
                MainFragment.this.areaList.clear();
                MainFragment.this.areaList.addAll(MainActivity.mgr.queryArea());
                MainFragment.this.areaAdapter.notifyDataSetChanged();
                anyLayer.dismiss();
            }
        }).contentAnim(new AnyLayer.IAnim() { // from class: com.shima.smartbushome.MainFragment.6
            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long inAnim(View view) {
                AnimHelper.startTopAlphaInAnim(view, 350L);
                return 350L;
            }

            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long outAnim(View view) {
                AnimHelper.startTopAlphaOutAnim(view, 350L);
                return 350L;
            }
        });
        ((FrameLayout) this.addAreaDialog.getContentView().findViewById(R.id.fl_dialog_delete)).setVisibility(0);
        ((TextView) this.addAreaDialog.getContentView().findViewById(R.id.tv_title)).setText("Update Area");
        this.et_areaName = (EditText) this.addAreaDialog.getContentView().findViewById(R.id.et_areaName);
        this.et_areaName.setText(saveArea.getArea_name());
        this.iv_backgroudArea = (ImageView) this.addAreaDialog.getContentView().findViewById(R.id.iv_backgroud);
        this.iv_galleryArea = (ImageView) this.addAreaDialog.getContentView().findViewById(R.id.iv_gallery);
        this.iv_galleryArea.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.MainFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MainFragment.this.galleryArea();
            }
        });
        Glide.with(getContext()).load(saveArea.getArea_bg()).apply(RequestOptions.bitmapTransform(new RoundedCorners(10)).override(500, 500)).into(this.iv_backgroudArea);
        this.addAreaDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteRoomByRoomId(int i) {
        MainActivity.mgr.deletefounction("room", i);
        MainActivity.mgr.deletefc("light", i);
        MainActivity.mgr.deletefc("hvac", i);
        MainActivity.mgr.deletefc("mood", i);
        MainActivity.mgr.deletefc("moodbutton", i);
        MainActivity.mgr.deletefc("fan", i);
        MainActivity.mgr.deletefc("curtain", i);
        MainActivity.mgr.deletefc("music", i);
        MainActivity.mgr.deletefc("song", i);
        MainActivity.mgr.deletefc("radio", i);
        MainActivity.mgr.deletefc("floorheat", i);
    }

    @Override // android.support.v4.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.main_about /* 2131231349 */:
                startActivity(new Intent(getActivity(), (Class<?>) AboutActivity.class));
                break;
            case R.id.main_exit /* 2131231351 */:
                if (this.exitdialogshow) {
                    this.mAlertView.dismiss();
                    this.exitdialogshow = false;
                    break;
                } else {
                    this.mAlertView = new AlertView("Warning", "Are you sure to exit?", "CANCEL", new String[]{"YES"}, null, getActivity(), AlertView.Style.Alert, this.exitclick);
                    this.mAlertView.setCancelable(false);
                    this.mAlertView.show();
                    this.exitdialogshow = true;
                    break;
                }
            case R.id.main_setting /* 2131231352 */:
                startActivity(new Intent(getActivity(), (Class<?>) MainSettingActivity.class));
                break;
            case R.id.menu_area /* 2131231416 */:
                this.areaListDialog = AnyLayer.with(getActivity()).contentView(R.layout.view_arealist_dialog).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).onClick(R.id.fb_addArea, new AnyLayer.OnLayerClickListener() { // from class: com.shima.smartbushome.MainFragment.12
                    @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
                    public void onClick(AnyLayer anyLayer, View view) {
                        MainFragment.this.showAddAreaDialog();
                    }
                }).contentAnim(new AnyLayer.IAnim() { // from class: com.shima.smartbushome.MainFragment.11
                    @Override // per.goweii.anylayer.AnyLayer.IAnim
                    public long inAnim(View view) {
                        AnimHelper.startTopAlphaInAnim(view, 350L);
                        return 350L;
                    }

                    @Override // per.goweii.anylayer.AnyLayer.IAnim
                    public long outAnim(View view) {
                        AnimHelper.startTopAlphaOutAnim(view, 350L);
                        return 350L;
                    }
                });
                ((TextView) this.areaListDialog.getContentView().findViewById(R.id.tv_areaListTitle)).setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.MainFragment.13
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainFragment.this.currPageArea = null;
                        MainFragment.this.areaListDialog.dismiss();
                        MainFragment.this.handle.postDelayed(MainFragment.this.getroomrun, 20L);
                    }
                });
                this.gv_areaList = (GridView) this.areaListDialog.getContentView().findViewById(R.id.gv_areaList);
                this.areaList = MainActivity.mgr.queryArea();
                this.areaAdapter = new AreaAdapter(getContext(), this.areaList);
                this.gv_areaList.setAdapter((ListAdapter) this.areaAdapter);
                this.gv_areaList.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.MainFragment.14
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        MainFragment.this.areaListDialog.dismiss();
                        MainFragment.this.currPageArea = MainFragment.this.areaList.get(i);
                        MainFragment.this.handle.postDelayed(MainFragment.this.getroomrun, 20L);
                    }
                });
                this.gv_areaList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // from class: com.shima.smartbushome.MainFragment.15
                    @Override // android.widget.AdapterView.OnItemLongClickListener
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                        MainFragment.this.showUpdateAreaDialog(MainFragment.this.areaList.get(i));
                        return true;
                    }
                });
                this.areaListDialog.show();
                break;
            case R.id.room_add /* 2131231705 */:
                this.maddingAlertViewExt = new AlertView("Enter Room info", null, "CANCEL", null, new String[]{"SAVE"}, getActivity(), AlertView.Style.Alert, this.alterclick);
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.setting_maininfo2, (ViewGroup) null);
                final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.mainroomicon);
                this.iv_gallery = (ImageView) viewGroup.findViewById(R.id.iv_gallery);
                this.iv_backgroud = (ImageView) viewGroup.findViewById(R.id.iv_backgroud);
                this.name = (EditText) viewGroup.findViewById(R.id.mainnameeditText);
                this.iv_gallery.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.MainFragment.16
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainFragment.this.gallery();
                    }
                });
                List<Saveroom> listQueryroom = MainActivity.mgr.queryroom();
                if (listQueryroom.size() <= 0) {
                    this.name.setText("room1");
                } else {
                    this.name.setText("room" + (listQueryroom.get(listQueryroom.size() - 1).room_id + 1));
                }
                imageView.setImageDrawable(getResources().getDrawable(getResourdIdByResourdName(getActivity(), this.roomicon)));
                this.settingicon = this.roomicon;
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.MainFragment.17
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainFragment.this.miconAlertViewExt = new AlertView("Icon Selection", null, "CANCEL", null, null, MainFragment.this.getActivity(), AlertView.Style.Alert, MainFragment.this.alterclick);
                        MainFragment.this.miconAlertViewExt.setCancelable(false);
                        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(MainFragment.this.getActivity()).inflate(R.layout.mood_icon_select, (ViewGroup) null);
                        GridView gridView = (GridView) viewGroup2.findViewById(R.id.gridView2);
                        gridView.setAdapter((ListAdapter) new RoomIconAdapter(MainFragment.this.getActivity()));
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.MainFragment.17.1
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                                MainFragment.this.settingicon = (String) MainFragment.this.iconarray.get(i);
                                imageView.setImageDrawable(MainFragment.this.getResources().getDrawable(MainFragment.getResourdIdByResourdName(MainFragment.this.getActivity(), (String) MainFragment.this.iconarray.get(i))));
                                MainFragment.this.miconAlertViewExt.dismiss();
                            }
                        });
                        MainFragment.this.miconAlertViewExt.addExtView(viewGroup2);
                        MainFragment.this.miconAlertViewExt.show();
                    }
                });
                this.maddingAlertViewExt.addExtView(viewGroup);
                this.maddingAlertViewExt.show();
                break;
            case R.id.room_delete /* 2131231706 */:
                if (this.mAdapter.getCount() > 0) {
                    showToast("pls click one item to delete");
                    this.deletechoose = true;
                    break;
                }
                break;
            case R.id.search /* 2131231745 */:
                startActivity(new Intent(getActivity(), (Class<?>) NetDaviceListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void showToast(String str) {
        Toast.makeText(getActivity(), str, 0).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.main_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new AnonymousClass22(view));
        popupMenu.show();
    }

    /* renamed from: com.shima.smartbushome.MainFragment$22, reason: invalid class name */
    class AnonymousClass22 implements PopupMenu.OnMenuItemClickListener {
        final /* synthetic */ View val$popview;

        AnonymousClass22(View view) {
            this.val$popview = view;
        }

        @Override // android.support.v7.widget.PopupMenu.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.main_setting) {
                int i = 0;
                while (true) {
                    if (i >= MainFragment.this.roomdata.size()) {
                        break;
                    }
                    if (MainFragment.this.roomdata.get(i).room_name.equals(((TextView) this.val$popview.findViewById(R.id.textView12)).getText().toString().trim())) {
                        MainFragment.this.settingid = i;
                        MainFragment.this.settingicon = MainFragment.this.roomdata.get(i).room_icon;
                        break;
                    }
                    i++;
                }
                MainFragment.this.msettingAlertViewExt = new AlertView("Settings", null, "CANCEL", null, new String[]{"SAVE"}, MainFragment.this.getActivity(), AlertView.Style.Alert, MainFragment.this.alterclick);
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(MainFragment.this.getActivity()).inflate(R.layout.setting_maininfo2, (ViewGroup) null);
                final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.mainroomicon);
                ((LinearLayout) viewGroup.findViewById(R.id.layout_selectArea)).setVisibility(0);
                MainFragment.this.sp_areaList = (Spinner) viewGroup.findViewById(R.id.sp_areaList);
                MainFragment.this.spAreaSelectposition = 0;
                List<SaveArea> listQueryArea = MainActivity.mgr.queryArea();
                if (listQueryArea.size() > 0) {
                    String[] strArr = new String[listQueryArea.size()];
                    for (int i2 = 0; i2 < listQueryArea.size(); i2++) {
                        strArr[i2] = listQueryArea.get(i2).getArea_name();
                        if (MainFragment.this.roomdata.get(MainFragment.this.settingid).area_id == listQueryArea.get(i2).getId()) {
                            MainFragment.this.spAreaSelectposition = i2;
                        }
                    }
                    MainFragment.this.sp_areaList.setAdapter((SpinnerAdapter) new ArrayAdapter(MainFragment.this.getContext(), android.R.layout.simple_dropdown_item_1line, strArr));
                    try {
                        MainFragment.this.sp_areaList.setSelection(MainFragment.this.spAreaSelectposition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MainFragment.this.sp_areaList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.shima.smartbushome.MainFragment.22.1
                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }

                    @Override // android.widget.AdapterView.OnItemSelectedListener
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i3, long j) {
                        MainFragment.this.spAreaSelectposition = i3;
                    }
                });
                MainFragment.this.name = (EditText) viewGroup.findViewById(R.id.mainnameeditText);
                MainFragment.this.iv_gallery = (ImageView) viewGroup.findViewById(R.id.iv_gallery);
                MainFragment.this.iv_backgroud = (ImageView) viewGroup.findViewById(R.id.iv_backgroud);
                MainFragment.this.name.setText(MainFragment.this.roomdata.get(MainFragment.this.settingid).room_name);
                MainFragment.this.iv_gallery.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.MainFragment.22.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainFragment.this.gallery();
                    }
                });
                Glide.with(MainFragment.this.getContext()).load(MainFragment.this.roomdata.get(MainFragment.this.settingid).room_icon_bg).apply(RequestOptions.bitmapTransform(new RoundedCorners(10)).override(300, 300)).into(MainFragment.this.iv_backgroud);
                imageView.setImageDrawable(MainFragment.this.getResources().getDrawable(MainFragment.getResourdIdByResourdName(MainFragment.this.getActivity(), MainFragment.this.settingicon)));
                imageView.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.MainFragment.22.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        MainFragment.this.miconAlertViewExt = new AlertView("Icon Selection", null, "CANCEL", null, null, MainFragment.this.getActivity(), AlertView.Style.Alert, MainFragment.this.alterclick);
                        MainFragment.this.miconAlertViewExt.setCancelable(false);
                        ViewGroup viewGroup2 = (ViewGroup) LayoutInflater.from(MainFragment.this.getActivity()).inflate(R.layout.mood_icon_select, (ViewGroup) null);
                        GridView gridView = (GridView) viewGroup2.findViewById(R.id.gridView2);
                        gridView.setAdapter((ListAdapter) new RoomIconAdapter(MainFragment.this.getActivity()));
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.MainFragment.22.3.1
                            @Override // android.widget.AdapterView.OnItemClickListener
                            public void onItemClick(AdapterView<?> adapterView, View view2, int i3, long j) {
                                MainFragment.this.settingicon = (String) MainFragment.this.iconarray.get(i3);
                                imageView.setImageDrawable(MainFragment.this.getResources().getDrawable(MainFragment.getResourdIdByResourdName(MainFragment.this.getActivity(), (String) MainFragment.this.iconarray.get(i3))));
                                MainFragment.this.miconAlertViewExt.dismiss();
                            }
                        });
                        MainFragment.this.miconAlertViewExt.addExtView(viewGroup2);
                        MainFragment.this.miconAlertViewExt.show();
                    }
                });
                MainFragment.this.msettingAlertViewExt.addExtView(viewGroup);
                MainFragment.this.msettingAlertViewExt.show();
            }
            return false;
        }
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

    private static IntentFilter makeUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        intentFilter.addAction(MainActivity.ACTION_BACKPRESS);
        return intentFilter;
    }

    public void gallery() {
        if (isGrantExternalRW(getActivity())) {
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        }
    }

    public void galleryArea() {
        if (isGrantExternalRW(getActivity())) {
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");
            startActivityForResult(intent, 3);
        }
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT < 23 || activity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        activity.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        return false;
    }

    private boolean hasSdcard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2) {
            if (intent == null || intent.getData() == null) {
                return;
            }
            intent.getData();
            this.backgroudPath = FileUtil.UriToFilePath(getActivity(), FileUtil.getPictureUri(getContext(), intent));
            Glide.with(getContext()).load(this.backgroudPath).apply(RequestOptions.bitmapTransform(new RoundedCorners(10)).override(300, 300)).into(this.iv_backgroud);
            return;
        }
        if (i != 3 || intent == null || intent.getData() == null) {
            return;
        }
        intent.getData();
        this.areaBgPath = FileUtil.UriToFilePath(getActivity(), FileUtil.getPictureUri(getContext(), intent));
        Glide.with(getContext()).load(this.areaBgPath).apply(RequestOptions.bitmapTransform(new RoundedCorners(10)).override(500, 500)).into(this.iv_backgroudArea);
    }
}
