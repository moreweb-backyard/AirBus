package com.pzj.ipcdemo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.pzj.ipcdemo.adapter.MoveVideoTimingAdapter;
import com.pzj.ipcdemo.adapter.VideoTimingAdapter;
import com.pzj.ipcdemo.bean.AlermBean;
import com.pzj.ipcdemo.bean.SdcardBean;
import com.pzj.ipcdemo.bean.SwitchBean;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.view.MyListView;
import java.util.HashMap;
import java.util.Map;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class SettingSDCardActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, BridgeService.SDCardInterface, BridgeService.VideoTimingInterface, BridgeService.TimingInterface, BridgeService.CallBack_AlarmParamsInterface {
    private static AlermBean alermBean = null;
    private static String cmark = "147258369";
    private static String strDID;
    private static String strPWD;
    private static SwitchBean switchBean;
    private static Handler waitHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.5
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    NativeCaller.TransferMessage(SettingSDCardActivity.strDID, "trans_cmd_string.cgi?cmd=2017&command=11&mark=" + SettingSDCardActivity.cmark + "&type=3&loginuse=admin&loginpas=" + SettingSDCardActivity.strPWD, 1);
                    break;
                case 2:
                    NativeCaller.TransferMessage(SettingSDCardActivity.strDID, "trans_cmd_string.cgi?cmd=2017&command=11&mark=" + SettingSDCardActivity.cmark + "&type=1&loginuse=admin&loginpas=" + SettingSDCardActivity.strPWD, 1);
                    break;
                case 3:
                    NativeCaller.PPPPGetSystemParams(SettingSDCardActivity.strDID, 22);
                    break;
                case 4:
                    NativeCaller.PPPPGetSystemParams(SettingSDCardActivity.strDID, 4);
                    break;
            }
        }
    };
    private CheckBox iv_video_mode;
    private MyListView lv_move_plan;
    private MyListView lv_video_plan;
    private Map<Integer, Integer> moveplanmap;
    private Map<Integer, Integer> planmap;
    private RelativeLayout rl_add_move_plan;
    private RelativeLayout rl_add_plan;
    private RelativeLayout rl_plan_all;
    private SdcardBean sdcardBean;
    private TextView tvSdTotal = null;
    private TextView tvSdRemain = null;
    private TextView tvSdStatus = null;
    private Button btnFormat = null;
    private CheckBox cbxConverage = null;
    private EditText editRecordLength = null;
    private CheckBox cbxRecordTime = null;
    private Button btnBack = null;
    private Button btnOk = null;
    private final int TIMEOUT = 3000;
    private ProgressDialog progressDialog = null;
    private boolean successFlag = false;
    private final int FAILED = 0;
    private final int SUCCESS = 1;
    private final int PARAMS = 2;
    private VideoTimingAdapter adapter = null;
    private MoveVideoTimingAdapter moveAdapter = null;
    private Handler handler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    SettingSDCardActivity.this.showToast(R.string.sdcard_set_failed);
                    break;
                case 1:
                    SettingSDCardActivity.this.showToast(R.string.sdcard_set_success);
                    SettingSDCardActivity.this.finish();
                    break;
                case 2:
                    SettingSDCardActivity.this.successFlag = true;
                    SettingSDCardActivity.this.progressDialog.dismiss();
                    SettingSDCardActivity.this.tvSdTotal.setText(SettingSDCardActivity.this.sdcardBean.getSdtotal() + "MB");
                    SettingSDCardActivity.this.tvSdRemain.setText(SettingSDCardActivity.this.sdcardBean.getSdfree() + "MB");
                    if (SettingSDCardActivity.this.sdcardBean.getRecord_sd_status() != 1) {
                        if (SettingSDCardActivity.this.sdcardBean.getRecord_sd_status() == 2) {
                            SettingSDCardActivity.this.tvSdStatus.setText(SettingSDCardActivity.this.getString(R.string.sdcard_video));
                        } else if (SettingSDCardActivity.this.sdcardBean.getRecord_sd_status() == 3) {
                            SettingSDCardActivity.this.tvSdStatus.setText(SettingSDCardActivity.this.getString(R.string.sdcard_file_error));
                        } else if (SettingSDCardActivity.this.sdcardBean.getRecord_sd_status() == 4) {
                            SettingSDCardActivity.this.tvSdStatus.setText(SettingSDCardActivity.this.getString(R.string.sdcard_isformatting));
                        } else {
                            SettingSDCardActivity.this.tvSdStatus.setText(SettingSDCardActivity.this.getResources().getString(R.string.sdcard_status_info));
                        }
                    } else {
                        SettingSDCardActivity.this.tvSdStatus.setText(SettingSDCardActivity.this.getResources().getString(R.string.sdcard_inserted));
                    }
                    SettingSDCardActivity.this.cbxConverage.setChecked(true);
                    if (SettingSDCardActivity.this.sdcardBean.getRecord_time_enable() == 1) {
                        SettingSDCardActivity.this.cbxRecordTime.setChecked(true);
                    } else {
                        SettingSDCardActivity.this.cbxRecordTime.setChecked(false);
                    }
                    SettingSDCardActivity.this.editRecordLength.setText("15");
                    break;
            }
        }
    };
    private Runnable runnable = new Runnable() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.2
        @Override // java.lang.Runnable
        public void run() {
            if (SettingSDCardActivity.this.successFlag) {
                return;
            }
            SettingSDCardActivity.this.successFlag = false;
            SettingSDCardActivity.this.progressDialog.dismiss();
        }
    };
    private Handler upHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.8
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            int i2 = message.arg1;
            switch (i) {
                case 0:
                    for (int i3 = 1; i3 < 22; i3++) {
                        int iIntValue = ((Integer) SettingSDCardActivity.this.planmap.get(Integer.valueOf(i3))).intValue();
                        if (iIntValue == -1 || iIntValue == 0) {
                            SettingSDCardActivity.this.planmap.put(Integer.valueOf(i3), Integer.valueOf(i2));
                            SettingSDCardActivity.this.adapter.addPlan(i3, i2);
                            SettingSDCardActivity.this.adapter.notifyDataSetChanged();
                            SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(1);
                            break;
                        }
                    }
                    SettingSDCardActivity.this.adapter.notifyDataSetChanged();
                    SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(1);
                    break;
                case 1:
                    int i4 = message.arg2;
                    SettingSDCardActivity.this.planmap.put(Integer.valueOf(i4), Integer.valueOf(i2));
                    SettingSDCardActivity.this.adapter.notify(i4, i2);
                    SettingSDCardActivity.this.adapter.notifyDataSetChanged();
                    SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(1);
                    break;
            }
        }
    };
    private Handler moveHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.9
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            int i2 = message.arg1;
            switch (i) {
                case 0:
                    for (int i3 = 1; i3 < 22; i3++) {
                        int iIntValue = ((Integer) SettingSDCardActivity.this.moveplanmap.get(Integer.valueOf(i3))).intValue();
                        if (iIntValue == -1 || iIntValue == 0) {
                            SettingSDCardActivity.this.moveplanmap.put(Integer.valueOf(i3), Integer.valueOf(i2));
                            SettingSDCardActivity.this.moveAdapter.addPlan(i3, i2);
                            SettingSDCardActivity.this.moveAdapter.notifyDataSetChanged();
                            SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(2);
                            break;
                        }
                    }
                    SettingSDCardActivity.this.moveAdapter.notifyDataSetChanged();
                    SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(2);
                    break;
                case 1:
                    int i4 = message.arg2;
                    SettingSDCardActivity.this.moveplanmap.put(Integer.valueOf(i4), Integer.valueOf(i2));
                    SettingSDCardActivity.this.moveAdapter.notify(i4, i2);
                    SettingSDCardActivity.this.moveAdapter.notifyDataSetChanged();
                    SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(2);
                    break;
            }
        }
    };
    private Handler deleteHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.10
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    int i = message.arg1;
                    SettingSDCardActivity.this.adapter.removePlan(i);
                    SettingSDCardActivity.this.planmap.put(Integer.valueOf(i), -1);
                    SettingSDCardActivity.this.adapter.notifyDataSetChanged();
                    SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(1);
                    break;
                case 2:
                    int i2 = message.arg1;
                    SettingSDCardActivity.this.moveAdapter.removePlan(i2);
                    SettingSDCardActivity.this.moveplanmap.put(Integer.valueOf(i2), -1);
                    SettingSDCardActivity.this.moveAdapter.notifyDataSetChanged();
                    SettingSDCardActivity.this.setAlarmHandler.sendEmptyMessage(2);
                    break;
            }
        }
    };
    private Handler setAlarmHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.11
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    SettingSDCardActivity.this.setTiming(SettingSDCardActivity.strDID, SettingSDCardActivity.strPWD);
                    break;
                case 2:
                    SettingSDCardActivity.this.setMoveTiming(SettingSDCardActivity.strDID, SettingSDCardActivity.strPWD);
                    break;
            }
        }
    };
    private Handler callbackHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.12
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = 1;
            switch (message.what) {
                case 1:
                    while (i < 22) {
                        int iIntValue = ((Integer) SettingSDCardActivity.this.planmap.get(Integer.valueOf(i))).intValue();
                        if (iIntValue != 0 && iIntValue != -1) {
                            SettingSDCardActivity.this.adapter.addPlan(i, iIntValue);
                            SettingSDCardActivity.this.adapter.notifyDataSetChanged();
                        }
                        i++;
                    }
                    break;
                case 2:
                    while (i < 22) {
                        int iIntValue2 = ((Integer) SettingSDCardActivity.this.moveplanmap.get(Integer.valueOf(i))).intValue();
                        if (iIntValue2 != 0 && iIntValue2 != -1) {
                            SettingSDCardActivity.this.moveAdapter.addPlan(i, iIntValue2);
                            SettingSDCardActivity.this.moveAdapter.notifyDataSetChanged();
                        }
                        i++;
                    }
                    break;
            }
        }
    };
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.13
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 4) {
                switch (i) {
                    case 0:
                        Toast.makeText(SettingSDCardActivity.this, R.string.alerm_set_failed, 1).show();
                        break;
                    case 2:
                        Log.e("2222alermBean.getMotion_armed()", "************" + SettingSDCardActivity.alermBean.getMotion_armed());
                        if (SettingSDCardActivity.alermBean.getMotion_armed() == 0) {
                            SettingSDCardActivity.setSDLing();
                            break;
                        }
                        break;
                }
            }
        }
    };

    @Override // com.pzj.ipcdemo.BaseActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getDataFromOther();
        requestWindowFeature(1);
        setContentView(R.layout.settingsdcard);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setProgressStyle(0);
        this.progressDialog.setMessage(getString(R.string.sdcard_getparams));
        this.progressDialog.show();
        this.sdcardBean = new SdcardBean();
        switchBean = new SwitchBean();
        alermBean = new AlermBean();
        this.handler.postDelayed(this.runnable, 3000L);
        findView();
        setLister();
        BridgeService.setSDCardInterface(this);
        BridgeService.setVideoTimingInterface(this);
        BridgeService.setTimingInterface(this);
        BridgeService.setCallBack_AlarmParamsInterface(this);
        waitHandler.sendEmptyMessageDelayed(3, 100L);
        waitHandler.sendEmptyMessageDelayed(1, 100L);
        waitHandler.sendEmptyMessageDelayed(2, 100L);
        waitHandler.sendEmptyMessageDelayed(4, 100L);
    }

    private void getDataFromOther() {
        Intent intent = getIntent();
        strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        strPWD = intent.getStringExtra(ContentCommon.STR_CAMERA_PWD);
        Log.i("info", "did:" + strDID);
    }

    private void setLister() {
        this.btnBack.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        this.btnFormat.setOnClickListener(this);
        this.rl_add_plan.setOnClickListener(this);
        this.rl_add_move_plan.setOnClickListener(this);
        this.cbxConverage.setOnCheckedChangeListener(this);
        this.cbxRecordTime.setOnCheckedChangeListener(this);
        this.iv_video_mode.setOnCheckedChangeListener(this);
    }

    private void findView() {
        this.tvSdTotal = (TextView) findViewById(R.id.tv_sd_total);
        this.tvSdRemain = (TextView) findViewById(R.id.tv_sd_remain);
        this.tvSdStatus = (TextView) findViewById(R.id.tv_state);
        this.btnFormat = (Button) findViewById(R.id.btn_format);
        this.cbxConverage = (CheckBox) findViewById(R.id.cbx_coverage);
        this.editRecordLength = (EditText) findViewById(R.id.edit_record_length);
        this.cbxRecordTime = (CheckBox) findViewById(R.id.cbx_record_time);
        this.btnBack = (Button) findViewById(R.id.back);
        this.btnOk = (Button) findViewById(R.id.ok);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.top);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.top_bg));
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bitmapDrawable.setDither(true);
        relativeLayout.setBackgroundDrawable(bitmapDrawable);
        this.iv_video_mode = (CheckBox) findViewById(R.id.iv_video_mode);
        this.rl_add_plan = (RelativeLayout) findViewById(R.id.rl_add_plan);
        this.rl_add_move_plan = (RelativeLayout) findViewById(R.id.rl_add_move_plan);
        this.rl_plan_all = (RelativeLayout) findViewById(R.id.rl_plan_all);
        this.lv_video_plan = (MyListView) findViewById(R.id.lv_video_plan);
        this.lv_move_plan = (MyListView) findViewById(R.id.lv_move_plan);
        this.adapter = new VideoTimingAdapter(this);
        this.lv_video_plan.setAdapter((ListAdapter) this.adapter);
        this.planmap = new HashMap();
        this.lv_video_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Map<Integer, Integer> map = SettingSDCardActivity.this.adapter.sdtiming.get(i);
                int iIntValue = map.entrySet().iterator().next().getValue().intValue();
                int iIntValue2 = map.entrySet().iterator().next().getKey().intValue();
                Intent intent = new Intent(SettingSDCardActivity.this, (Class<?>) SCameraSetSDTiming.class);
                intent.putExtra(DatabaseUtil.KEY_TYPE, 1);
                intent.putExtra("value", iIntValue);
                intent.putExtra("key", iIntValue2);
                SettingSDCardActivity.this.startActivityForResult(intent, 1);
            }
        });
        this.moveAdapter = new MoveVideoTimingAdapter(this);
        this.lv_move_plan.setAdapter((ListAdapter) this.moveAdapter);
        this.moveplanmap = new HashMap();
        this.lv_move_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Map<Integer, Integer> map = SettingSDCardActivity.this.moveAdapter.movevideotiming.get(i);
                int iIntValue = map.entrySet().iterator().next().getValue().intValue();
                int iIntValue2 = map.entrySet().iterator().next().getKey().intValue();
                Intent intent = new Intent(SettingSDCardActivity.this, (Class<?>) SCameraSetPlanVideoTiming.class);
                intent.putExtra(DatabaseUtil.KEY_TYPE, 1);
                intent.putExtra("value", iIntValue);
                intent.putExtra("key", iIntValue2);
                SettingSDCardActivity.this.startActivityForResult(intent, 1);
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            finish();
            return;
        }
        if (id == R.id.ok) {
            setSDCardSchedule();
            return;
        }
        if (id == R.id.btn_format) {
            showFormatDialog();
            return;
        }
        if (id == R.id.rl_add_plan) {
            Intent intent = new Intent(this, (Class<?>) SCameraSetSDTiming.class);
            intent.putExtra(DatabaseUtil.KEY_TYPE, 0);
            startActivityForResult(intent, 0);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            return;
        }
        if (id == R.id.rl_add_move_plan) {
            Intent intent2 = new Intent(this, (Class<?>) SCameraSetPlanVideoTiming.class);
            intent2.putExtra(DatabaseUtil.KEY_TYPE, 0);
            startActivityForResult(intent2, 0);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
    }

    void showFormatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.sdcard_formatsd);
        builder.setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("info", "格式化");
                NativeCaller.FormatSD(SettingSDCardActivity.strDID);
                dialogInterface.dismiss();
            }
        }).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() { // from class: com.pzj.ipcdemo.SettingSDCardActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private void setSDCardSchedule() {
        if (this.sdcardBean.getRecord_time_enable() == 0) {
            this.sdcardBean.setSun_0(0);
            this.sdcardBean.setSun_1(0);
            this.sdcardBean.setSun_2(0);
            this.sdcardBean.setMon_0(0);
            this.sdcardBean.setMon_1(0);
            this.sdcardBean.setMon_2(0);
            this.sdcardBean.setTue_0(0);
            this.sdcardBean.setTue_1(0);
            this.sdcardBean.setTue_2(0);
            this.sdcardBean.setWed_0(0);
            this.sdcardBean.setWed_1(0);
            this.sdcardBean.setWed_2(0);
            this.sdcardBean.setThu_0(0);
            this.sdcardBean.setThu_1(0);
            this.sdcardBean.setThu_2(0);
            this.sdcardBean.setFri_0(0);
            this.sdcardBean.setFri_1(0);
            this.sdcardBean.setFri_2(0);
            this.sdcardBean.setSat_0(0);
            this.sdcardBean.setSat_1(0);
            this.sdcardBean.setSat_2(0);
        } else {
            this.sdcardBean.setSun_0(-1);
            this.sdcardBean.setSun_1(-1);
            this.sdcardBean.setSun_2(-1);
            this.sdcardBean.setMon_0(-1);
            this.sdcardBean.setMon_1(-1);
            this.sdcardBean.setMon_2(-1);
            this.sdcardBean.setTue_0(-1);
            this.sdcardBean.setTue_1(-1);
            this.sdcardBean.setTue_2(-1);
            this.sdcardBean.setWed_0(-1);
            this.sdcardBean.setWed_1(-1);
            this.sdcardBean.setWed_2(-1);
            this.sdcardBean.setThu_0(-1);
            this.sdcardBean.setThu_1(-1);
            this.sdcardBean.setThu_2(-1);
            this.sdcardBean.setFri_0(-1);
            this.sdcardBean.setFri_1(-1);
            this.sdcardBean.setFri_2(-1);
            this.sdcardBean.setSat_0(-1);
            this.sdcardBean.setSat_1(-1);
            this.sdcardBean.setSat_2(-1);
        }
        this.sdcardBean.setRecord_timer(15);
        NativeCaller.PPPPSDRecordSetting(strDID, this.sdcardBean.getRecord_conver_enable(), this.sdcardBean.getRecord_timer(), this.sdcardBean.getRecord_size(), this.sdcardBean.getRecord_chnl(), this.sdcardBean.getRecord_time_enable(), this.sdcardBean.getSun_0(), this.sdcardBean.getSun_1(), this.sdcardBean.getSun_2(), this.sdcardBean.getMon_0(), this.sdcardBean.getMon_1(), this.sdcardBean.getMon_2(), this.sdcardBean.getTue_0(), this.sdcardBean.getTue_1(), this.sdcardBean.getTue_2(), this.sdcardBean.getWed_0(), this.sdcardBean.getWed_1(), this.sdcardBean.getWed_2(), this.sdcardBean.getThu_0(), this.sdcardBean.getThu_1(), this.sdcardBean.getThu_2(), this.sdcardBean.getFri_0(), this.sdcardBean.getFri_1(), this.sdcardBean.getFri_2(), this.sdcardBean.getSat_0(), this.sdcardBean.getSat_1(), this.sdcardBean.getSat_2(), this.sdcardBean.getEnable_audio());
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        int id = compoundButton.getId();
        if (id == R.id.cbx_record_time) {
            if (z) {
                this.sdcardBean.setRecord_time_enable(1);
                return;
            } else {
                this.sdcardBean.setRecord_time_enable(0);
                return;
            }
        }
        if (id == R.id.iv_video_mode) {
            if (z) {
                this.rl_plan_all.setVisibility(0);
            } else {
                this.rl_plan_all.setVisibility(8);
            }
        }
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.SDCardInterface
    public void callBackRecordSchParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29) {
        Log.i("info", "---record_cover_enable" + i + "---record_time_enable" + i4 + "---record_timer" + i2);
        Log.i("info", "record_schedule_sun_0:" + i5 + ",record_schedule_sun_1:" + i6 + ",record_schedule_sun_2:" + i7 + ",record_schedule_mon_0:" + i8 + ",record_schedule_mon_1:" + i9 + ",record_schedule_mon_2:" + i10);
        this.sdcardBean.setDid(str);
        this.sdcardBean.setRecord_conver_enable(i);
        this.sdcardBean.setRecord_timer(i2);
        this.sdcardBean.setRecord_size(i3);
        this.sdcardBean.setRecord_time_enable(i4);
        this.sdcardBean.setRecord_sd_status(i26);
        this.sdcardBean.setSdtotal(i27);
        this.sdcardBean.setSdfree(i28);
        this.sdcardBean.setSun_0(i5);
        this.sdcardBean.setSun_1(i6);
        this.sdcardBean.setSun_2(i7);
        this.sdcardBean.setMon_0(i8);
        this.sdcardBean.setMon_1(i9);
        this.sdcardBean.setMon_2(i10);
        this.sdcardBean.setTue_0(i11);
        this.sdcardBean.setTue_1(i12);
        this.sdcardBean.setTue_2(i13);
        this.sdcardBean.setWed_0(i14);
        this.sdcardBean.setWed_1(i15);
        this.sdcardBean.setWed_2(i16);
        this.sdcardBean.setThu_0(i17);
        this.sdcardBean.setThu_1(i18);
        this.sdcardBean.setThu_2(i19);
        this.sdcardBean.setFri_0(i20);
        this.sdcardBean.setFri_1(i21);
        this.sdcardBean.setFri_2(i22);
        this.sdcardBean.setSat_0(i23);
        this.sdcardBean.setSat_1(i24);
        this.sdcardBean.setSat_2(i25);
        this.sdcardBean.setEnable_audio(i29);
        this.handler.sendEmptyMessage(2);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.SDCardInterface
    public void callBackSetSystemParamsResult(String str, int i, int i2) {
        Log.d("tag", "result:" + i2 + " paramType:" + i);
        if (strDID.equals(str)) {
            this.handler.sendEmptyMessage(i2);
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        int intExtra;
        if (i2 == 2015) {
            int intExtra2 = intent.getIntExtra("jnitime", 1);
            Message message = new Message();
            message.what = 0;
            message.arg1 = intExtra2;
            this.upHandler.sendMessage(message);
        }
        if (i2 == 2016) {
            int intExtra3 = intent.getIntExtra("jnitime", 1);
            int intExtra4 = intent.getIntExtra("key", -1);
            if (intExtra4 == -1) {
                return;
            }
            Message message2 = new Message();
            message2.what = 1;
            message2.arg1 = intExtra3;
            message2.arg2 = intExtra4;
            this.upHandler.sendMessage(message2);
        }
        if (i2 == 2017) {
            int intExtra5 = intent.getIntExtra("key", -1);
            if (intExtra5 == -1) {
                return;
            }
            Message message3 = new Message();
            message3.what = 1;
            message3.arg1 = intExtra5;
            this.deleteHandler.sendMessage(message3);
        }
        if (i2 == 2018) {
            int intExtra6 = intent.getIntExtra("jnitime", 1);
            Message message4 = new Message();
            message4.what = 0;
            message4.arg1 = intExtra6;
            this.moveHandler.sendMessage(message4);
        }
        if (i2 == 2019) {
            int intExtra7 = intent.getIntExtra("jnitime", 1);
            int intExtra8 = intent.getIntExtra("key", -1);
            if (intExtra8 == -1) {
                return;
            }
            Message message5 = new Message();
            message5.what = 1;
            message5.arg1 = intExtra7;
            message5.arg2 = intExtra8;
            this.moveHandler.sendMessage(message5);
        }
        if (i2 != 2020 || (intExtra = intent.getIntExtra("key", -1)) == -1) {
            return;
        }
        Message message6 = new Message();
        message6.what = 2;
        message6.arg1 = intExtra;
        this.deleteHandler.sendMessage(message6);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTiming(String str, String str2) {
        NativeCaller.TransferMessage(str, "trans_cmd_string.cgi?cmd=2017&command=3&mark=" + cmark + "&record_plan1=" + this.planmap.get(1) + "&record_plan2=" + this.planmap.get(2) + "&record_plan3=" + this.planmap.get(3) + "&record_plan4=" + this.planmap.get(4) + "&record_plan5=" + this.planmap.get(5) + "&record_plan6=" + this.planmap.get(6) + "&record_plan7=" + this.planmap.get(7) + "&record_plan8=" + this.planmap.get(8) + "&record_plan9=" + this.planmap.get(9) + "&record_plan10=" + this.planmap.get(10) + "&record_plan11=" + this.planmap.get(11) + "&record_plan12=" + this.planmap.get(12) + "&record_plan13=" + this.planmap.get(13) + "&record_plan14=" + this.planmap.get(14) + "&record_plan15=" + this.planmap.get(15) + "&record_plan16=" + this.planmap.get(16) + "&record_plan17=" + this.planmap.get(17) + "&record_plan18=" + this.planmap.get(18) + "&record_plan19=" + this.planmap.get(19) + "&record_plan20=" + this.planmap.get(20) + "&record_plan21=" + this.planmap.get(21) + "&record_plan_enable=" + switchBean.getRecord_plan_enable() + "&loginuse=admin&loginpas=" + str2, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMoveTiming(String str, String str2) {
        NativeCaller.TransferMessage(str, "trans_cmd_string.cgi?cmd=2017&command=1&mark=" + cmark + "&motion_record_plan1=" + this.moveplanmap.get(1) + "&motion_record_plan2=" + this.moveplanmap.get(2) + "&motion_record_plan3=" + this.moveplanmap.get(3) + "&motion_record_plan4=" + this.moveplanmap.get(4) + "&motion_record_plan5=" + this.moveplanmap.get(5) + "&motion_record_plan6=" + this.moveplanmap.get(6) + "&motion_record_plan7=" + this.moveplanmap.get(7) + "&motion_record_plan8=" + this.moveplanmap.get(8) + "&motion_record_plan9=" + this.moveplanmap.get(9) + "&motion_record_plan10=" + this.moveplanmap.get(10) + "&motion_record_plan11=" + this.moveplanmap.get(11) + "&motion_record_plan12=" + this.moveplanmap.get(12) + "&motion_record_plan13=" + this.moveplanmap.get(13) + "&motion_record_plan14=" + this.moveplanmap.get(14) + "&motion_record_plan15=" + this.moveplanmap.get(15) + "&motion_record_plan16=" + this.moveplanmap.get(16) + "&motion_record_plan17=" + this.moveplanmap.get(17) + "&motion_record_plan18=" + this.moveplanmap.get(18) + "&motion_record_plan19=" + this.moveplanmap.get(19) + "&motion_record_plan20=" + this.moveplanmap.get(20) + "&motion_record_plan21=" + this.moveplanmap.get(21) + "&motion_record_plan_enable=" + switchBean.getMotion_record_plan_enable() + "&loginuse=admin&loginpas=" + str2, -1);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.TimingInterface
    public void TimingCallback(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, String str22, String str23, String str24, String str25) {
        if (str.contains(strDID) && str3.contains(cmark)) {
            this.planmap.put(1, Integer.valueOf(str4));
            this.planmap.put(2, Integer.valueOf(str5));
            this.planmap.put(3, Integer.valueOf(str6));
            this.planmap.put(4, Integer.valueOf(str7));
            this.planmap.put(5, Integer.valueOf(str8));
            this.planmap.put(6, Integer.valueOf(str9));
            this.planmap.put(7, Integer.valueOf(str10));
            this.planmap.put(8, Integer.valueOf(str11));
            this.planmap.put(9, Integer.valueOf(str12));
            this.planmap.put(10, Integer.valueOf(str13));
            this.planmap.put(11, Integer.valueOf(str14));
            this.planmap.put(12, Integer.valueOf(str15));
            this.planmap.put(13, Integer.valueOf(str16));
            this.planmap.put(14, Integer.valueOf(str17));
            this.planmap.put(15, Integer.valueOf(str18));
            this.planmap.put(16, Integer.valueOf(str19));
            this.planmap.put(17, Integer.valueOf(str20));
            this.planmap.put(18, Integer.valueOf(str21));
            this.planmap.put(19, Integer.valueOf(str22));
            this.planmap.put(20, Integer.valueOf(str23));
            this.planmap.put(21, Integer.valueOf(str24));
            switchBean.setRecord_plan_enable(str25);
            this.callbackHandler.sendEmptyMessage(1);
        }
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.VideoTimingInterface
    public void VideoTimingCallback(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, String str22, String str23, String str24, String str25) {
        if (str.contains(strDID) && str3.contains(cmark)) {
            this.moveplanmap.put(1, Integer.valueOf(str4));
            this.moveplanmap.put(2, Integer.valueOf(str5));
            this.moveplanmap.put(3, Integer.valueOf(str6));
            this.moveplanmap.put(4, Integer.valueOf(str7));
            this.moveplanmap.put(5, Integer.valueOf(str8));
            this.moveplanmap.put(6, Integer.valueOf(str9));
            this.moveplanmap.put(7, Integer.valueOf(str10));
            this.moveplanmap.put(8, Integer.valueOf(str11));
            this.moveplanmap.put(9, Integer.valueOf(str12));
            this.moveplanmap.put(10, Integer.valueOf(str13));
            this.moveplanmap.put(11, Integer.valueOf(str14));
            this.moveplanmap.put(12, Integer.valueOf(str15));
            this.moveplanmap.put(13, Integer.valueOf(str16));
            this.moveplanmap.put(14, Integer.valueOf(str17));
            this.moveplanmap.put(15, Integer.valueOf(str18));
            this.moveplanmap.put(16, Integer.valueOf(str19));
            this.moveplanmap.put(17, Integer.valueOf(str20));
            this.moveplanmap.put(18, Integer.valueOf(str21));
            this.moveplanmap.put(19, Integer.valueOf(str22));
            this.moveplanmap.put(20, Integer.valueOf(str23));
            this.moveplanmap.put(21, Integer.valueOf(str24));
            switchBean.setMotion_record_plan_enable(str25);
            this.callbackHandler.sendEmptyMessage(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setSDLing() {
        NativeCaller.PPPPAlarmSetting(strDID, alermBean.getAlarm_audio(), 1, alermBean.getMotion_sensitivity(), alermBean.getInput_armed(), alermBean.getIoin_level(), alermBean.getIoout_level(), alermBean.getIolinkage(), alermBean.getAlermpresetsit(), alermBean.getMail(), alermBean.getSnapshot(), 1, alermBean.getUpload_interval(), alermBean.getSchedule_enable(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, alermBean.getDefense_plan1(), alermBean.getDefense_plan2(), alermBean.getDefense_plan3(), alermBean.getDefense_plan4(), alermBean.getDefense_plan5(), alermBean.getDefense_plan6(), alermBean.getDefense_plan7(), alermBean.getDefense_plan8(), alermBean.getDefense_plan9(), alermBean.getDefense_plan10(), alermBean.getDefense_plan11(), alermBean.getDefense_plan12(), alermBean.getDefense_plan13(), alermBean.getDefense_plan14(), alermBean.getDefense_plan15(), alermBean.getDefense_plan16(), alermBean.getDefense_plan17(), alermBean.getDefense_plan18(), alermBean.getDefense_plan19(), alermBean.getDefense_plan20(), alermBean.getDefense_plan21(), -1);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.CallBack_AlarmParamsInterface
    public void CallBack_AlarmParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, int i31, int i32, int i33, int i34, int i35, int i36, int i37, int i38, int i39, int i40, int i41, int i42, int i43, int i44, int i45, int i46, int i47, int i48, int i49, int i50, int i51, int i52, int i53, int i54, int i55) {
        if (strDID.equals(str)) {
            alermBean.setDid(str);
            alermBean.setAlarm_audio(i);
            alermBean.setMotion_armed(i2);
            alermBean.setMotion_sensitivity(i3);
            alermBean.setInput_armed(i4);
            alermBean.setIoin_level(i5);
            alermBean.setIolinkage(i6);
            alermBean.setIoout_level(i7);
            alermBean.setAlermpresetsit(i8);
            alermBean.setMail(i9);
            alermBean.setSnapshot(i10);
            alermBean.setRecord(i11);
            alermBean.setUpload_interval(i12);
            alermBean.setSchedule_enable(1);
            alermBean.setSchedule_sun_0(i14);
            alermBean.setSchedule_sun_1(i15);
            alermBean.setSchedule_sun_2(i16);
            alermBean.setSchedule_mon_0(i17);
            alermBean.setSchedule_mon_1(i18);
            alermBean.setSchedule_mon_2(i19);
            alermBean.setSchedule_tue_0(i20);
            alermBean.setSchedule_tue_1(i21);
            alermBean.setSchedule_tue_2(i22);
            alermBean.setSchedule_wed_0(i23);
            alermBean.setSchedule_wed_1(i24);
            alermBean.setSchedule_wed_2(i25);
            alermBean.setSchedule_thu_0(i26);
            alermBean.setSchedule_thu_1(i27);
            alermBean.setSchedule_thu_2(i28);
            alermBean.setSchedule_fri_0(i29);
            alermBean.setSchedule_fri_1(i30);
            alermBean.setSchedule_fri_2(i31);
            alermBean.setSchedule_sat_0(i32);
            alermBean.setSchedule_sat_1(i33);
            alermBean.setSchedule_sat_2(i34);
            alermBean.setDefense_plan1(i35);
            alermBean.setDefense_plan2(i36);
            alermBean.setDefense_plan3(i37);
            alermBean.setDefense_plan4(i38);
            alermBean.setDefense_plan5(i39);
            alermBean.setDefense_plan6(i40);
            alermBean.setDefense_plan7(i41);
            alermBean.setDefense_plan8(i42);
            alermBean.setDefense_plan9(i43);
            alermBean.setDefense_plan10(i44);
            alermBean.setDefense_plan11(i45);
            alermBean.setDefense_plan12(i46);
            alermBean.setDefense_plan13(i47);
            alermBean.setDefense_plan14(i48);
            alermBean.setDefense_plan15(i49);
            alermBean.setDefense_plan16(i50);
            alermBean.setDefense_plan17(i51);
            alermBean.setDefense_plan18(i52);
            alermBean.setDefense_plan19(i53);
            alermBean.setDefense_plan20(i54);
            alermBean.setDefense_plan21(i55);
            this.mHandler.sendEmptyMessage(2);
        }
    }
}
