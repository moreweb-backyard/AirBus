package com.pzj.ipcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.pzj.ipcdemo.adapter.PushVideoTimingAdapter;
import com.pzj.ipcdemo.bean.AlermBean;
import com.pzj.ipcdemo.bean.SwitchBean;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.ContentCommon;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.view.MyListView;
import java.util.HashMap;
import java.util.Map;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class MoveNotificationActivity extends BaseActivity implements View.OnClickListener, BridgeService.PushTimingInterface, BridgeService.CallBack_AlarmParamsInterface {
    private static AlermBean alermBean = null;
    private static String pushmark = "147258369";
    private static Map<Integer, Integer> pushplan;
    private static String strDID;
    private static String strPWD;
    private static SwitchBean switchBean;
    private static Handler waitHandler = new Handler() { // from class: com.pzj.ipcdemo.MoveNotificationActivity.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    NativeCaller.TransferMessage(MoveNotificationActivity.strDID, "trans_cmd_string.cgi?cmd=2017&command=11&mark=" + MoveNotificationActivity.pushmark + "&type=2&loginuse=admin&loginpas=" + MoveNotificationActivity.strPWD, 1);
                    break;
                case 2:
                    NativeCaller.PPPPGetSystemParams(MoveNotificationActivity.strDID, 4);
                    break;
            }
        }
    };
    private Button btn_infoback;
    private MyListView lv_info_plan;
    private RelativeLayout rl_add_infoplan;
    private PushVideoTimingAdapter pushAdapter = null;
    private Handler upHandler = new Handler() { // from class: com.pzj.ipcdemo.MoveNotificationActivity.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            int i2 = message.arg1;
            switch (i) {
                case 1:
                    for (int i3 = 1; i3 < 22; i3++) {
                        int iIntValue = ((Integer) MoveNotificationActivity.pushplan.get(Integer.valueOf(i3))).intValue();
                        if (iIntValue == -1 || iIntValue == 0) {
                            MoveNotificationActivity.pushplan.put(Integer.valueOf(i3), Integer.valueOf(i2));
                            MoveNotificationActivity.this.pushAdapter.addPlan(i3, i2);
                            MoveNotificationActivity.this.pushAdapter.notifyDataSetChanged();
                            MoveNotificationActivity.this.setAlarmHandler.sendEmptyMessage(1);
                            break;
                        }
                    }
                    MoveNotificationActivity.this.pushAdapter.notifyDataSetChanged();
                    MoveNotificationActivity.this.setAlarmHandler.sendEmptyMessage(1);
                    break;
                case 2:
                    int i4 = message.arg2;
                    MoveNotificationActivity.pushplan.put(Integer.valueOf(i4), Integer.valueOf(i2));
                    MoveNotificationActivity.this.pushAdapter.notify(i4, i2);
                    MoveNotificationActivity.this.pushAdapter.notifyDataSetChanged();
                    MoveNotificationActivity.this.setAlarmHandler.sendEmptyMessage(1);
                    break;
            }
        }
    };
    private Handler deleteHandler = new Handler() { // from class: com.pzj.ipcdemo.MoveNotificationActivity.4
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            int i = message.arg1;
            MoveNotificationActivity.this.pushAdapter.removePlan(i);
            MoveNotificationActivity.pushplan.put(Integer.valueOf(i), -1);
            MoveNotificationActivity.this.pushAdapter.notifyDataSetChanged();
            MoveNotificationActivity.this.setAlarmHandler.sendEmptyMessage(1);
        }
    };
    private Handler setAlarmHandler = new Handler() { // from class: com.pzj.ipcdemo.MoveNotificationActivity.5
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            MoveNotificationActivity.setPushTiming(MoveNotificationActivity.strDID, MoveNotificationActivity.strPWD);
        }
    };
    private Handler callbackHandler = new Handler() { // from class: com.pzj.ipcdemo.MoveNotificationActivity.6
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            for (int i = 1; i < 22; i++) {
                int iIntValue = ((Integer) MoveNotificationActivity.pushplan.get(Integer.valueOf(i))).intValue();
                if (iIntValue != 0 && iIntValue != -1) {
                    MoveNotificationActivity.this.pushAdapter.addPlan(i, iIntValue);
                    MoveNotificationActivity.this.pushAdapter.notifyDataSetChanged();
                }
            }
        }
    };
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.MoveNotificationActivity.7
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 4) {
                switch (i) {
                    case 0:
                        Toast.makeText(MoveNotificationActivity.this, R.string.alerm_set_failed, 1).show();
                        break;
                    case 2:
                        Log.e("1111alermBean.getMotion_armed()", "***" + MoveNotificationActivity.alermBean.getMotion_armed());
                        if (MoveNotificationActivity.alermBean.getMotion_armed() == 0) {
                            Log.e("1111111", "11111");
                            MoveNotificationActivity.setSDLing();
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
        setContentView(R.layout.move_message);
        switchBean = new SwitchBean();
        alermBean = new AlermBean();
        findView();
        setLister();
        BridgeService.setPushTimingInterface(this);
        BridgeService.setCallBack_AlarmParamsInterface(this);
        waitHandler.sendEmptyMessageDelayed(1, 100L);
        waitHandler.sendEmptyMessageDelayed(2, 100L);
    }

    private void getDataFromOther() {
        Intent intent = getIntent();
        strDID = intent.getStringExtra(ContentCommon.STR_CAMERA_ID);
        strPWD = intent.getStringExtra(ContentCommon.STR_CAMERA_PWD);
    }

    public void setLister() {
        this.btn_infoback.setOnClickListener(this);
        this.rl_add_infoplan.setOnClickListener(this);
    }

    public void findView() {
        this.btn_infoback = (Button) findViewById(R.id.btn_infoback);
        this.lv_info_plan = (MyListView) findViewById(R.id.lv_info_plan);
        this.rl_add_infoplan = (RelativeLayout) findViewById(R.id.rl_add_infoplan);
        pushplan = new HashMap();
        this.pushAdapter = new PushVideoTimingAdapter(this);
        this.lv_info_plan.setAdapter((ListAdapter) this.pushAdapter);
        this.lv_info_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.pzj.ipcdemo.MoveNotificationActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Map<Integer, Integer> map = MoveNotificationActivity.this.pushAdapter.movetiming.get(i);
                int iIntValue = map.entrySet().iterator().next().getValue().intValue();
                int iIntValue2 = map.entrySet().iterator().next().getKey().intValue();
                Intent intent = new Intent(MoveNotificationActivity.this, (Class<?>) SCameraSetPushVideoTiming.class);
                intent.putExtra(DatabaseUtil.KEY_TYPE, 1);
                intent.putExtra("value", iIntValue);
                intent.putExtra("key", iIntValue2);
                MoveNotificationActivity.this.startActivityForResult(intent, 1);
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_infoback) {
            finish();
        } else if (id == R.id.rl_add_infoplan) {
            Intent intent = new Intent(this, (Class<?>) SCameraSetPushVideoTiming.class);
            intent.putExtra(DatabaseUtil.KEY_TYPE, 0);
            startActivityForResult(intent, 0);
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        int intExtra;
        if (i2 == 2011) {
            int intExtra2 = intent.getIntExtra("jnitime", 1);
            Message message = new Message();
            message.what = 1;
            message.arg1 = intExtra2;
            this.upHandler.sendMessage(message);
        }
        if (i2 == 2012) {
            int intExtra3 = intent.getIntExtra("jnitime", 1);
            int intExtra4 = intent.getIntExtra("key", -1);
            if (intExtra4 == -1) {
                return;
            }
            Message message2 = new Message();
            message2.what = 2;
            message2.arg1 = intExtra3;
            message2.arg2 = intExtra4;
            this.upHandler.sendMessage(message2);
        }
        if (i2 != 2013 || (intExtra = intent.getIntExtra("key", -1)) == -1) {
            return;
        }
        Message message3 = new Message();
        message3.what = 1;
        message3.arg1 = intExtra;
        this.deleteHandler.sendMessage(message3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setPushTiming(String str, String str2) {
        NativeCaller.TransferMessage(str, "trans_cmd_string.cgi?cmd=2017&command=2&mark=" + pushmark + "&motion_push_plan1=" + pushplan.get(1) + "&motion_push_plan2=" + pushplan.get(2) + "&motion_push_plan3=" + pushplan.get(3) + "&motion_push_plan4=" + pushplan.get(4) + "&motion_push_plan5=" + pushplan.get(5) + "&motion_push_plan6=" + pushplan.get(6) + "&motion_push_plan7=" + pushplan.get(7) + "&motion_push_plan8=" + pushplan.get(8) + "&motion_push_plan9=" + pushplan.get(9) + "&motion_push_plan10=" + pushplan.get(10) + "&motion_push_plan11=" + pushplan.get(11) + "&motion_push_plan12=" + pushplan.get(12) + "&motion_push_plan13=" + pushplan.get(13) + "&motion_push_plan14=" + pushplan.get(14) + "&motion_push_plan15=" + pushplan.get(15) + "&motion_push_plan16=" + pushplan.get(16) + "&motion_push_plan17=" + pushplan.get(17) + "&motion_push_plan18=" + pushplan.get(18) + "&motion_push_plan19=" + pushplan.get(19) + "&motion_push_plan20=" + pushplan.get(20) + "&motion_push_plan21=" + pushplan.get(21) + "&motion_push_plan_enable=" + switchBean.getMotion_push_plan_enable() + "&loginuse=admin&loginpas=" + str2, -1);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.PushTimingInterface
    public void PushTimingCallback(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, String str22, String str23, String str24, String str25) {
        if (str.contains(str) && str3.contains(pushmark)) {
            pushplan.put(1, Integer.valueOf(str4));
            pushplan.put(2, Integer.valueOf(str5));
            pushplan.put(3, Integer.valueOf(str6));
            pushplan.put(4, Integer.valueOf(str7));
            pushplan.put(5, Integer.valueOf(str8));
            pushplan.put(6, Integer.valueOf(str9));
            pushplan.put(7, Integer.valueOf(str10));
            pushplan.put(8, Integer.valueOf(str11));
            pushplan.put(9, Integer.valueOf(str12));
            pushplan.put(10, Integer.valueOf(str13));
            pushplan.put(11, Integer.valueOf(str14));
            pushplan.put(12, Integer.valueOf(str15));
            pushplan.put(13, Integer.valueOf(str16));
            pushplan.put(14, Integer.valueOf(str17));
            pushplan.put(15, Integer.valueOf(str18));
            pushplan.put(16, Integer.valueOf(str19));
            pushplan.put(17, Integer.valueOf(str20));
            pushplan.put(18, Integer.valueOf(str21));
            pushplan.put(19, Integer.valueOf(str22));
            pushplan.put(20, Integer.valueOf(str23));
            pushplan.put(21, Integer.valueOf(str24));
            switchBean.setMotion_push_plan_enable(str25);
            this.callbackHandler.sendEmptyMessage(1);
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
