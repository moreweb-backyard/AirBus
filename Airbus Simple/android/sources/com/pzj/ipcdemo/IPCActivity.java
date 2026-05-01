package com.pzj.ipcdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mediatek.demo.smartconnection.MainActivity;
import com.pzj.ipcdemo.adapter.IPCListAdapter;
import com.pzj.ipcdemo.entity.VStarCamera;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.utils.JSONUtil;
import com.pzj.ipcdemo.utils.SPUtil;
import com.pzj.ipcdemo.utils.SystemUIUtil;
import com.pzj.ipcdemo.view.SearchAnyLayerView;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class IPCActivity extends AppCompatActivity implements BridgeService.IpcamClientInterface, View.OnClickListener, SearchAnyLayerView.OnSelectSearchListener {
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final String DECODED_CONTENT_KEY = "codedContent";
    public static final int PPPP_STATUS_CONNECTING = 0;
    public static final int PPPP_STATUS_CONNECT_FAILED = 3;
    public static final int PPPP_STATUS_CONNECT_TIMEOUT = 7;
    public static final int PPPP_STATUS_DEVICE_NOT_ON_LINE = 6;
    public static final int PPPP_STATUS_DISCONNECT = 4;
    public static final int PPPP_STATUS_INITIALING = 1;
    public static final int PPPP_STATUS_INVALID_ID = 5;
    public static final int PPPP_STATUS_ON_LINE = 2;
    public static final int PPPP_STATUS_WRONGPWD_RIGHTUSER = 9;
    public static final int PPPP_STATUS_WRONGPWD_WRONGUSER = 10;
    public static final int PPPP_STATUS_WRONGUSER_RIGHTPWD = 8;
    private static final int REQUEST_CODE_SCAN = 0;
    public static final String SP_FILE_NAME = "share_ipc";
    public static final String SP_IPC_KEY = "vstarcam_devices";
    public static String TAG = "IPCActivity";
    public static String deviceId = "VSTA899484MXNCJ";
    public static String deviceName = "admin";
    public static String devicePass = "12345678";
    private AnyLayer editDialog;
    private FloatingActionButton fb_addCamera;
    private IPCListAdapter ipcListAdapter;
    private RecyclerView rv_ipcList;
    private SearchAnyLayerView searchAnyLayerView;
    private List<VStarCamera> vStarCameraList = new ArrayList();
    Handler ipcStatusHandler = new Handler() { // from class: com.pzj.ipcdemo.IPCActivity.3
        @Override // android.os.Handler
        public void handleMessage(Message message) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            super.handleMessage(message);
            Bundle data = message.getData();
            String string = data.getString(DatabaseUtil.KEY_DID);
            int i = 0;
            switch (message.what) {
                case 1:
                    data.getInt(DatabaseUtil.KEY_TYPE);
                    int i2 = data.getInt("param");
                    while (i < IPCActivity.this.vStarCameraList.size()) {
                        if (string.equals(((VStarCamera) IPCActivity.this.vStarCameraList.get(i)).getId())) {
                            IPCActivity.this.ipcListAdapter.setIpcStatesIndex(i2, i);
                        }
                        i++;
                    }
                    break;
                case 2:
                    byte[] byteArray = data.getByteArray("bImage");
                    data.getInt("len");
                    while (i < IPCActivity.this.vStarCameraList.size()) {
                        if (string.equals(((VStarCamera) IPCActivity.this.vStarCameraList.get(i)).getId())) {
                            ((VStarCamera) IPCActivity.this.vStarCameraList.get(i)).setbImage(byteArray);
                            IPCActivity.this.ipcListAdapter.notifyDataSetChanged();
                        }
                        i++;
                    }
                    SPUtil.put(IPCActivity.this, IPCActivity.SP_FILE_NAME, IPCActivity.SP_IPC_KEY, JSONUtil.listToJson(IPCActivity.this.vStarCameraList));
                    break;
            }
        }
    };

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void CameraStatus(String str, int i) {
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void callBackUserParams(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ipc);
        SystemUIUtil.setSystemUIVisible(this, false);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_ipc);
        toolbar.setTitleTextColor(getResources().getColor(R.color.tab_bgcolor));
        toolbar.setTitle("Camera");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            toolbar.setPadding(0, getStatusBarHeight(this), 0, 0);
        }
        Window window = getWindow();
        window.addFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        initView();
        String string = SPUtil.getString(this, SP_FILE_NAME, SP_IPC_KEY, "");
        this.vStarCameraList = JSONUtil.jsonToList(string, VStarCamera.class);
        if (this.vStarCameraList == null) {
            this.vStarCameraList = new ArrayList();
        }
        Log.d(TAG, "获取设备数据:" + this.vStarCameraList.size() + "   JsonStr:" + string);
        this.ipcListAdapter = new IPCListAdapter(this, this.vStarCameraList);
        this.rv_ipcList.setAdapter(this.ipcListAdapter);
        this.ipcListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.1
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                if (view.getId() == R.id.iv_ipcSetting && IPCActivity.this.ipcListAdapter.getIpcStates().get(i).intValue() == 2) {
                    VStarCamera vStarCamera = (VStarCamera) IPCActivity.this.vStarCameraList.get(i);
                    Intent intent = new Intent("com.shima.smartbushome.vstarcam.settings");
                    intent.putExtra(ContentCommon.STR_CAMERA_ID, vStarCamera.getId());
                    intent.putExtra(ContentCommon.STR_CAMERA_NAME, vStarCamera.getUsername());
                    intent.putExtra(ContentCommon.STR_CAMERA_PWD, vStarCamera.getPassword());
                    IPCActivity.this.startActivity(intent);
                }
                if (view.getId() == R.id.iv_ipcUpdate) {
                    IPCActivity.this.showUpdateCameraEditDialog((VStarCamera) IPCActivity.this.vStarCameraList.get(i));
                }
                if (view.getId() == R.id.iv_ipcDelete) {
                    IPCActivity.this.vStarCameraList.remove(i);
                    IPCActivity.this.ipcListAdapter.getIpcStates().remove(i);
                    SPUtil.put(IPCActivity.this, IPCActivity.SP_FILE_NAME, IPCActivity.SP_IPC_KEY, JSONUtil.listToJson(IPCActivity.this.vStarCameraList));
                    IPCActivity.this.ipcListRefreshData();
                }
                if (view.getId() == R.id.iv_item && IPCActivity.this.ipcListAdapter.getIpcStates().get(i).intValue() == 2) {
                    VStarCamera vStarCamera2 = (VStarCamera) IPCActivity.this.vStarCameraList.get(i);
                    Intent intent2 = new Intent("com.pzj.play");
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(DatabaseUtil.KEY_NAME, vStarCamera2.getUsername());
                    bundle2.putString("pass", vStarCamera2.getPassword());
                    bundle2.putString(DatabaseUtil.KEY_ID, vStarCamera2.getId());
                    intent2.putExtras(bundle2);
                    IPCActivity.this.startActivity(intent2);
                }
            }
        });
        this.ipcListAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.2
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildLongClickListener
            public boolean onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                LinearLayout linearLayout = (LinearLayout) IPCActivity.this.ipcListAdapter.getViewByPosition(IPCActivity.this.rv_ipcList, i, R.id.layout_settings);
                if (view.getId() == R.id.iv_item) {
                    int visibility = linearLayout.getVisibility();
                    if (visibility != 0 && visibility == 4) {
                        linearLayout.setVisibility(0);
                    } else {
                        linearLayout.setVisibility(4);
                    }
                }
                if ((view.getId() == R.id.iv_ipcSetting) | (view.getId() == R.id.iv_ipcDelete) | (view.getId() == R.id.iv_ipcUpdate)) {
                    linearLayout.setVisibility(4);
                }
                return true;
            }
        });
        Intent intent = new Intent();
        intent.setClass(this, BridgeService.class);
        startService(intent);
        Log.d(TAG, "启动服务....");
        new IpcInitThread().start();
        BridgeService.setIpcamClientInterface(this);
        this.fb_addCamera.setOnClickListener(this);
    }

    @Override // com.pzj.ipcdemo.view.SearchAnyLayerView.OnSelectSearchListener
    public void onSelectSearchListener(VStarCamera vStarCamera) {
        Iterator<VStarCamera> it = this.vStarCameraList.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(vStarCamera.getId())) {
                Toast.makeText(this, "Can't add devices repeatedly", 0).show();
                this.searchAnyLayerView.dismiss();
                return;
            }
        }
        this.searchAnyLayerView.dismiss();
        showAddCameraEditDialog(vStarCamera);
    }

    class IpcInitThread extends Thread {
        IpcInitThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws InterruptedException {
            super.run();
            try {
                for (VStarCamera vStarCamera : IPCActivity.this.vStarCameraList) {
                    NativeCaller.PPPPInitialOther("ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL");
                    Log.d(IPCActivity.TAG, "初始化服务器...");
                    Thread.sleep(3000L);
                    NativeCaller.Init();
                    Log.d(IPCActivity.TAG, "初始化回调...");
                    Thread.sleep(200L);
                    IPCActivity.this.startCameraPPPP(vStarCamera.getUsername(), vStarCamera.getPassword(), vStarCamera.getId());
                    Log.d(IPCActivity.TAG, "开启 p2p 连接 (账号:" + vStarCamera.getUsername() + "，密码:" + vStarCamera.getPassword() + "，设备ID:" + vStarCamera.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initView() {
        this.rv_ipcList = (RecyclerView) findViewById(R.id.rv_ipcList);
        this.fb_addCamera = (FloatingActionButton) findViewById(R.id.fb_addCamera);
        this.rv_ipcList.setLayoutManager(new GridLayoutManager(this, 1));
        this.rv_ipcList.setHasFixedSize(true);
    }

    public void ipcListRefreshData() {
        this.vStarCameraList.clear();
        this.vStarCameraList.addAll(JSONUtil.jsonToList(SPUtil.getString(this, SP_FILE_NAME, SP_IPC_KEY, ""), VStarCamera.class));
        this.ipcListAdapter.notifyDataSetChanged();
    }

    public int getStatusBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return context.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startCameraPPPP(String str, String str2, String str3) {
        if (str3.toLowerCase().startsWith("vsta")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("vstd")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "HZLXSXIALKHYEIEJHUASLMHWEESUEKAUIHPHSWAOSTEMENSQPDLRLNPAPEPGEPERIBLQLKHXELEHHULOEGIAEEHYEIEK-$$", 1);
            return;
        }
        if (str3.toLowerCase().startsWith("vstf")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "HZLXEJIALKHYATPCHULNSVLMEELSHWIHPFIBAOHXIDICSQEHENEKPAARSTELERPDLNEPLKEILPHUHXHZEJEEEHEGEM-$$", 1);
            return;
        }
        if (str3.toLowerCase().startsWith("vste")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EEGDFHBAKKIOGNJHEGHMFEEDGLNOHJMPHAFPBEDLADILKEKPDLBDDNPOHKKCIFKJBNNNKLCPPPNDBFDL", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("pisr")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EFGFFBBOKAIEGHJAEDHJFEEOHMNGDCNJCDFKAKHLEBJHKEKMCAFCDLLLHAOCJPPMBHMNOMCJKGJEBGGHJHIOMFBDNPKNFEGCEGCBGCALMFOHBCGMFK", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("vstg")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EEGDFHBOKCIGGFJPECHIFNEBGJNLHOMIHEFJBADPAGJELNKJDKANCBPJGHLAIALAADMDKPDGOENEBECCIK:vstarcam2018", 0);
            return;
        }
        if (str3.toLowerCase().startsWith("vsth")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "EEGDFHBLKGJIGEJLEKGOFMEDHAMHHJNAGGFABMCOBGJOLHLJDFAFCPPHGILKIKLMANNHKEDKOINIBNCPJOMK:vstarcam2018", 0);
        } else if (str3.toLowerCase().startsWith("vstb") || str3.toLowerCase().startsWith("vstc")) {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "ADCBBFAOPPJAHGJGBBGLFLAGDBJJHNJGGMBFBKHIBBNKOKLDHOBHCBOEHOKJJJKJBPMFLGCPPJMJAPDOIPNL", 0);
        } else {
            NativeCaller.StartPPPPExt(str3, str, str2, 1, "", "", 0);
        }
    }

    private void stopCameraPPPP(String str) {
        NativeCaller.StopPPPP(str);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void BSMsgNotifyData(String str, int i, int i2) {
        Log.d(TAG, "设备状态信息及视频连接流程相关参数回调");
        Log.d(TAG, "did:" + str);
        Log.d(TAG, "type:" + i);
        Log.d(TAG, "param:" + i2);
        Message message = new Message();
        message.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString(DatabaseUtil.KEY_DID, str);
        bundle.putInt(DatabaseUtil.KEY_TYPE, i);
        bundle.putInt("param", i2);
        message.setData(bundle);
        this.ipcStatusHandler.sendMessage(message);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.IpcamClientInterface
    public void BSSnapshotNotify(String str, byte[] bArr, int i) {
        Log.i(TAG, "BSSnapshotNotify---len" + i);
        Message message = new Message();
        message.what = 2;
        Bundle bundle = new Bundle();
        bundle.putString(DatabaseUtil.KEY_DID, str);
        bundle.putByteArray("bImage", bArr);
        bundle.putInt("len", i);
        message.setData(bundle);
        this.ipcStatusHandler.sendMessage(message);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        SystemUIUtil.setSystemUIVisible(this, false);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        try {
            for (VStarCamera vStarCamera : this.vStarCameraList) {
                NativeCaller.StopPPPPLivestream(vStarCamera.getId());
                NativeCaller.StopPPPP(vStarCamera.getId());
            }
            NativeCaller.Free();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.fb_addCamera) {
            showAddCameraTypeDialog();
        }
    }

    public void showAddCameraTypeDialog() {
        AnyLayer.with(this).contentView(R.layout.dialog_add_camera_type).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).onClick(R.id.iv_link, new AnyLayer.OnLayerClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.8
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                IPCActivity.this.startActivity(new Intent(IPCActivity.this, (Class<?>) MainActivity.class));
            }
        }).onClick(R.id.iv_scan, new AnyLayer.OnLayerClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.7
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                if (anyLayer.isShow()) {
                    anyLayer.dismiss();
                }
                if (ContextCompat.checkSelfPermission(IPCActivity.this, "android.permission.CAMERA") == 0) {
                    IPCActivity.this.goScan();
                } else {
                    ActivityCompat.requestPermissions(IPCActivity.this, new String[]{"android.permission.CAMERA"}, 1);
                }
            }
        }).onClick(R.id.iv_search, new AnyLayer.OnLayerClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.6
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                if (anyLayer.isShow()) {
                    anyLayer.dismiss();
                }
                IPCActivity.this.searchAnyLayerView = new SearchAnyLayerView(IPCActivity.this);
                IPCActivity.this.searchAnyLayerView.setOnSelectSearchListener(IPCActivity.this);
                IPCActivity.this.searchAnyLayerView.show();
            }
        }).onClick(R.id.iv_edit, new AnyLayer.OnLayerClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.5
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                if (anyLayer.isShow()) {
                    anyLayer.dismiss();
                }
                IPCActivity.this.showAddCameraEditDialog();
            }
        }).contentAnim(new AnyLayer.IAnim() { // from class: com.pzj.ipcdemo.IPCActivity.4
            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long inAnim(View view) {
                AnimHelper.startTopAlphaInAnim(view, 350L);
                return 350L;
            }

            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long outAnim(View view) {
                AnimHelper.startTopAlphaOutAnim(view, 350L);
                return 0L;
            }
        }).show();
    }

    public void showAddCameraEditDialog() {
        this.editDialog = AnyLayer.with(this).contentView(R.layout.dialog_add_camera_edit).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.10
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                if (anyLayer.isShow()) {
                    anyLayer.dismiss();
                }
            }
        }).contentAnim(new AnyLayer.IAnim() { // from class: com.pzj.ipcdemo.IPCActivity.9
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
        final EditText editText = (EditText) this.editDialog.getView(R.id.et_name);
        final EditText editText2 = (EditText) this.editDialog.getView(R.id.et_id);
        final EditText editText3 = (EditText) this.editDialog.getView(R.id.et_username);
        final EditText editText4 = (EditText) this.editDialog.getView(R.id.et_password);
        ((FrameLayout) this.editDialog.getView(R.id.fl_dialog_yes)).setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                String string = editText.getText().toString();
                String strTrim = editText2.getText().toString().trim();
                String strTrim2 = editText3.getText().toString().trim();
                String strTrim3 = editText4.getText().toString().trim();
                if (string.equals("") | strTrim.equals("") | strTrim2.equals("") | strTrim3.equals("")) {
                    Toast.makeText(IPCActivity.this, "Input cannot be empty", 1).show();
                    return;
                }
                IPCActivity.this.vStarCameraList.add(new VStarCamera(string, strTrim, strTrim2, strTrim3));
                IPCActivity.this.ipcListAdapter.getIpcStates().add(-1);
                SPUtil.put(IPCActivity.this, IPCActivity.SP_FILE_NAME, IPCActivity.SP_IPC_KEY, JSONUtil.listToJson(IPCActivity.this.vStarCameraList));
                IPCActivity.this.ipcListRefreshData();
                if (IPCActivity.this.editDialog.isShow()) {
                    IPCActivity.this.editDialog.dismiss();
                }
                try {
                    for (VStarCamera vStarCamera : IPCActivity.this.vStarCameraList) {
                        NativeCaller.StopPPPPLivestream(vStarCamera.getId());
                        NativeCaller.StopPPPP(vStarCamera.getId());
                    }
                    NativeCaller.Free();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                IPCActivity.this.new IpcInitThread().start();
            }
        });
        this.editDialog.show();
    }

    public void showAddCameraEditDialog(VStarCamera vStarCamera) {
        this.editDialog = AnyLayer.with(this).contentView(R.layout.dialog_add_camera_edit).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.13
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                if (anyLayer.isShow()) {
                    anyLayer.dismiss();
                }
            }
        }).contentAnim(new AnyLayer.IAnim() { // from class: com.pzj.ipcdemo.IPCActivity.12
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
        final EditText editText = (EditText) this.editDialog.getView(R.id.et_name);
        final EditText editText2 = (EditText) this.editDialog.getView(R.id.et_id);
        final EditText editText3 = (EditText) this.editDialog.getView(R.id.et_username);
        final EditText editText4 = (EditText) this.editDialog.getView(R.id.et_password);
        FrameLayout frameLayout = (FrameLayout) this.editDialog.getView(R.id.fl_dialog_yes);
        editText.setText("" + vStarCamera.getName());
        editText2.setText("" + vStarCamera.getId());
        editText3.setText("" + vStarCamera.getUsername());
        editText4.setText("" + vStarCamera.getPassword());
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                String string = editText.getText().toString();
                String strTrim = editText2.getText().toString().trim();
                String strTrim2 = editText3.getText().toString().trim();
                String strTrim3 = editText4.getText().toString().trim();
                if (string.equals("") | strTrim.equals("") | strTrim2.equals("") | strTrim3.equals("")) {
                    Toast.makeText(IPCActivity.this, "Input cannot be empty", 1).show();
                    return;
                }
                IPCActivity.this.vStarCameraList.add(new VStarCamera(string, strTrim, strTrim2, strTrim3));
                IPCActivity.this.ipcListAdapter.getIpcStates().add(-1);
                SPUtil.put(IPCActivity.this, IPCActivity.SP_FILE_NAME, IPCActivity.SP_IPC_KEY, JSONUtil.listToJson(IPCActivity.this.vStarCameraList));
                IPCActivity.this.ipcListRefreshData();
                if (IPCActivity.this.editDialog.isShow()) {
                    IPCActivity.this.editDialog.dismiss();
                }
                try {
                    for (VStarCamera vStarCamera2 : IPCActivity.this.vStarCameraList) {
                        NativeCaller.StopPPPPLivestream(vStarCamera2.getId());
                        NativeCaller.StopPPPP(vStarCamera2.getId());
                    }
                    NativeCaller.Free();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                IPCActivity.this.new IpcInitThread().start();
            }
        });
        this.editDialog.show();
    }

    public void showUpdateCameraEditDialog(final VStarCamera vStarCamera) {
        this.editDialog = AnyLayer.with(this).contentView(R.layout.dialog_add_camera_edit).backgroundBlurPercent(0.015f).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.16
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                if (anyLayer.isShow()) {
                    anyLayer.dismiss();
                }
            }
        }).contentAnim(new AnyLayer.IAnim() { // from class: com.pzj.ipcdemo.IPCActivity.15
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
        TextView textView = (TextView) this.editDialog.getView(R.id.tv_title);
        final EditText editText = (EditText) this.editDialog.getView(R.id.et_name);
        EditText editText2 = (EditText) this.editDialog.getView(R.id.et_id);
        final EditText editText3 = (EditText) this.editDialog.getView(R.id.et_username);
        final EditText editText4 = (EditText) this.editDialog.getView(R.id.et_password);
        FrameLayout frameLayout = (FrameLayout) this.editDialog.getView(R.id.fl_dialog_yes);
        textView.setText("Update Camera");
        editText.setText("" + vStarCamera.getName());
        editText2.setHint("" + vStarCamera.getId());
        editText2.setFocusableInTouchMode(false);
        editText3.setText("" + vStarCamera.getUsername());
        editText4.setText("" + vStarCamera.getPassword());
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.IPCActivity.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                String string = editText.getText().toString();
                String id = vStarCamera.getId();
                String strTrim = editText3.getText().toString().trim();
                String strTrim2 = editText4.getText().toString().trim();
                if (!(string.equals("") | strTrim.equals("") | strTrim2.equals(""))) {
                    for (int i = 0; i < IPCActivity.this.vStarCameraList.size(); i++) {
                        if (((VStarCamera) IPCActivity.this.vStarCameraList.get(i)).getId().equals(id)) {
                            ((VStarCamera) IPCActivity.this.vStarCameraList.get(i)).setName(string);
                            ((VStarCamera) IPCActivity.this.vStarCameraList.get(i)).setUsername(strTrim);
                            ((VStarCamera) IPCActivity.this.vStarCameraList.get(i)).setPassword(strTrim2);
                        }
                    }
                    SPUtil.put(IPCActivity.this, IPCActivity.SP_FILE_NAME, IPCActivity.SP_IPC_KEY, JSONUtil.listToJson(IPCActivity.this.vStarCameraList));
                    IPCActivity.this.ipcListRefreshData();
                    if (IPCActivity.this.editDialog.isShow()) {
                        IPCActivity.this.editDialog.dismiss();
                    }
                    try {
                        for (VStarCamera vStarCamera2 : IPCActivity.this.vStarCameraList) {
                            NativeCaller.StopPPPPLivestream(vStarCamera2.getId());
                            NativeCaller.StopPPPP(vStarCamera2.getId());
                        }
                        NativeCaller.Free();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    IPCActivity.this.new IpcInitThread().start();
                    return;
                }
                Toast.makeText(IPCActivity.this, "Input cannot be empty", 1).show();
            }
        });
        this.editDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goScan() {
        startActivityForResult(new Intent("com.shima.smartbushome.scan"), 0);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 1) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            goScan();
        } else {
            Toast.makeText(this, "You refused to apply for permission. You may not be able to open the camera scanner!", 0).show();
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 0 && i2 == -1 && intent != null) {
            String stringExtra = intent.getStringExtra(DECODED_CONTENT_KEY);
            Log.d(TAG, "扫码内容是：" + stringExtra);
            String cameraIDByJsonStr = getCameraIDByJsonStr(stringExtra);
            if (cameraIDByJsonStr.length() == 0) {
                Toast.makeText(this, "Error: Invalid content", 0).show();
                return;
            }
            Iterator<VStarCamera> it = this.vStarCameraList.iterator();
            while (it.hasNext()) {
                if (it.next().getId().equals(cameraIDByJsonStr)) {
                    Toast.makeText(this, "Can't add devices repeatedly", 0).show();
                    return;
                }
            }
            showAddCameraEditDialog(new VStarCamera("IPCAM", cameraIDByJsonStr, ContentCommon.DEFAULT_USER_NAME, ""));
        }
    }

    public String getCameraIDByJsonStr(String str) {
        Exception e;
        String strOptString;
        try {
            strOptString = new JSONObject(str).optString("ID", null);
            try {
                Log.d(TAG, "解析二维码内容获取摄像头ID：" + strOptString);
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                return strOptString;
            }
        } catch (Exception e3) {
            e = e3;
            strOptString = "";
        }
        return strOptString;
    }
}
