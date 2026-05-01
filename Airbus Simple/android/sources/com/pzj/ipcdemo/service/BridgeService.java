package com.pzj.ipcdemo.service;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.R;
import com.pzj.ipcdemo.utils.ContentCommon;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.utils.MySharedPreferenceUtil;
import com.pzj.ipcdemo.utils.MyStringUtils;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class BridgeService extends Service {
    private static AddCameraInterface addCameraInterface;
    private static AlarmInterface alarmInterface;
    private static CallBack_AlarmParamsInterface alarmParamsInterface;
    private static DateTimeInterface dateTimeInterface;
    private static FtpInterface ftpInterface;
    private static IpcamClientInterface ipcamClientInterface;
    public static CameraLightInterfaceInterface mCameraLightInterface;
    public static LowPwerInterface mLowPwerInterface;
    public static LowPwerInterface2109 mLowPwerInterfaceForIndexCgiHelper;
    public static LowPwerInterface mLowPwerInterfaceForLightLevel;
    public static PushTimingInterface mPushTimingInterface;
    public static TimingInterface mTimingInterface;
    public static VideoTimingInterface mVideoTimingInterface;
    private static MailInterface mailInterface;
    private static CallBackMessageInterface messageInterface;
    private static PictureInterface pictureInterface;
    private static PlayBackInterface playBackInterface;
    private static PlayBackTFInterface playBackTFInterface;
    private static PlayInterface playInterface;
    private static SDCardInterface sCardInterface;
    private static SensorListActivityAllDataInterface sensorListInterfece;
    private static SensorSetCodeInterface setCodeInterface;
    private static EditSensorListActivityInterface setEditSensor;
    private static Firmware updatefirmware;
    private static UserInterface userInterface;
    private static VideoInterface videoInterface;
    private static WifiInterface wifiInterface;
    private String TAG = BridgeService.class.getSimpleName();
    private Notification mNotify2;
    private NotificationManager notifyManager;

    public interface AddCameraInterface {
        void callBackSearchResultData(int i, String str, String str2, String str3, String str4, int i2);
    }

    public interface AlarmInterface {
        void callBackAlarmParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, int i31, int i32, int i33, int i34);

        void callBackSetSystemParamsResult(String str, int i, int i2);
    }

    public interface CallBackMessageInterface {
        void CallBackGetStatus(String str, String str2, int i);
    }

    public interface CallBack_AlarmParamsInterface {
        void CallBack_AlarmParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, int i31, int i32, int i33, int i34, int i35, int i36, int i37, int i38, int i39, int i40, int i41, int i42, int i43, int i44, int i45, int i46, int i47, int i48, int i49, int i50, int i51, int i52, int i53, int i54, int i55);
    }

    public interface CameraLightInterfaceInterface {
        void LightSireCallBack(String str, String str2, String str3, String str4, String str5);
    }

    public interface DateTimeInterface {
        void callBackDatetimeParams(String str, int i, int i2, int i3, String str2);

        void callBackSetSystemParamsResult(String str, int i, int i2);
    }

    public interface EditSensorListActivityInterface {
        void CallBackMessages(String str, String str2, int i);
    }

    public interface Firmware {
        void CallBack_UpdateFirmware(String str, String str2, String str3, String str4);
    }

    public interface FtpInterface {
        void callBackFtpParams(String str, String str2, String str3, String str4, String str5, int i, int i2, int i3);

        void callBackSetSystemParamsResult(String str, int i, int i2);
    }

    public interface IpcamClientInterface {
        void BSMsgNotifyData(String str, int i, int i2);

        void BSSnapshotNotify(String str, byte[] bArr, int i);

        void CameraStatus(String str, int i);

        void callBackUserParams(String str, String str2, String str3, String str4, String str5, String str6, String str7);
    }

    public interface LowPwerInterface {
        void LowPwerCallBack(String str, String str2, String str3, String str4);
    }

    public interface LowPwerInterface2109 {
        void LowPwerCallBack(String str, String str2, String str3, String str4, String str5);
    }

    public interface MailInterface {
        void callBackMailParams(String str, String str2, int i, String str3, String str4, int i2, String str5, String str6, String str7, String str8, String str9);

        void callBackSetSystemParamsResult(String str, int i, int i2);
    }

    public interface PictureInterface {
        void BSMsgNotifyData(String str, int i, int i2);
    }

    public interface PlayBackInterface {
        void callBackPlaybackVideoData(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, float f, float f2);
    }

    public interface PlayBackTFInterface {
        void callBackRecordFileSearchResult(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6);
    }

    public interface PlayInterface {
        void callBackAudioData(byte[] bArr, int i);

        void callBackCameraParamNotify(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7);

        void callBackH264Data(byte[] bArr, int i, int i2);

        void callBackMessageNotify(String str, int i, int i2);

        void callBackVideoData(byte[] bArr, int i, int i2, int i3, int i4);
    }

    public interface PushTimingInterface {
        void PushTimingCallback(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, String str22, String str23, String str24, String str25);
    }

    public interface SDCardInterface {
        void callBackRecordSchParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29);

        void callBackSetSystemParamsResult(String str, int i, int i2);
    }

    public interface SensorListActivityAllDataInterface {
        void CallBackMessage(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);
    }

    public interface SensorSetCodeInterface {
        void CallBackReCodeMessage(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);
    }

    public interface TimingInterface {
        void TimingCallback(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, String str22, String str23, String str24, String str25);
    }

    public interface UserInterface {
        void callBackPPPPMsgNotifyData(String str, int i, int i2);

        void callBackSetSystemParamsResult(String str, int i, int i2);

        void callBackUserParams(String str, String str2, String str3, String str4, String str5, String str6, String str7);
    }

    public interface VideoInterface {
        void BSMsgNotifyData(String str, int i, int i2);
    }

    public interface VideoTimingInterface {
        void VideoTimingCallback(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18, String str19, String str20, String str21, String str22, String str23, String str24, String str25);
    }

    public interface WifiInterface {
        void callBackPPPPMsgNotifyData(String str, int i, int i2);

        void callBackSetSystemParamsResult(String str, int i, int i2);

        void callBackWifiParams(String str, int i, String str2, int i2, int i3, int i4, int i5, int i6, int i7, String str3, String str4, String str5, String str6, int i8, int i9, int i10, int i11, String str7);

        void callBackWifiScanResult(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6);
    }

    public void CallBackAlermLogList(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    public void CallBackOriFramLen(String str, int i) {
    }

    public void CallBackTransJson(String str) {
    }

    public void CallBackTransJson(String str, String str2) {
    }

    public void CallBackTransferCamList(String str, String str2, int i, int i2, String str3, String str4, String str5) {
    }

    public void CallBack_LowpowerDevMag(String str, int i) {
    }

    public void CallBack_TFCardRecord(String str, float f, int i) {
    }

    public void P2PRawDataNotify(String str, byte[] bArr, int i, int i2) {
    }

    public void P2PRawDataSendStatusNotify(String str, int i, int i2) {
    }

    public void onTimeOut(Dialog dialog) {
    }

    public void onTransCMDString(String str, byte[] bArr, int i) {
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Log.d("tag", "BridgeService onBind()");
        return new ControllerBinder();
    }

    class ControllerBinder extends Binder {
        ControllerBinder() {
        }

        public BridgeService getBridgeService() {
            return BridgeService.this;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.d("tag", "BridgeService onCreate()");
        this.notifyManager = (NotificationManager) getSystemService("notification");
        NativeCaller.PPPPSetCallbackContext(this);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.notifyManager.cancel(R.drawable.app);
    }

    public void VideoData(String str, byte[] bArr, int i, int i2, int i3, int i4, int i5, short s, int i6, int i7, int i8) {
        Log.d(this.TAG, "BridgeService----Call VideoData 视频数据返回...h264Data: " + i + " len: " + i2 + " videobuf len: " + i2 + "width: " + i3 + "height: " + i4 + ",did:" + str + ",sessid:" + i6 + ",version:" + i7);
        if (playInterface != null) {
            playInterface.callBackVideoData(bArr, i, i2, i3, i4);
        }
    }

    public void CallBack_H264Data(String str, byte[] bArr, int i, int i2, int i3, short s, int i4, int i5) {
        Log.w(this.TAG, "H264 数据返回:" + str + Consts.SECOND_LEVEL_SPLIT + bArr.length + ",type:" + i + ",size:" + i2 + ",time:" + i3 + ",did:" + str + ",sessid:" + i4 + ",version:" + i5);
        if (playInterface != null) {
            playInterface.callBackH264Data(bArr, i, i2);
        }
    }

    private void MessageNotify(String str, int i, int i2) {
        if (ipcamClientInterface != null) {
            ipcamClientInterface.BSMsgNotifyData(str, i, i2);
        }
        if (wifiInterface != null) {
            wifiInterface.callBackPPPPMsgNotifyData(str, i, i2);
        }
        if (userInterface != null) {
            userInterface.callBackPPPPMsgNotifyData(str, i, i2);
        }
    }

    public void AudioData(byte[] bArr, int i) {
        Log.d(this.TAG, "AudioData: len :+ " + i);
        if (playInterface != null) {
            playInterface.callBackAudioData(bArr, i);
        }
    }

    public void PPPPMsgNotify(String str, int i, int i2) {
        Log.d(this.TAG, "PPPPMsgNotify  did:" + str + " type:" + i + " param:" + i2);
        if (ipcamClientInterface != null) {
            ipcamClientInterface.BSMsgNotifyData(str, i, i2);
        }
        if (wifiInterface != null) {
            wifiInterface.callBackPPPPMsgNotifyData(str, i, i2);
        }
        if (userInterface != null) {
            userInterface.callBackPPPPMsgNotifyData(str, i, i2);
        }
    }

    public void SearchResult(String str, String str2, String str3, String str4, String str5, String str6, int i) {
        if (str5.length() == 0 || addCameraInterface == null) {
            return;
        }
        addCameraInterface.callBackSearchResultData(0, str3, str4, str5, str6, i);
    }

    public void CallBack_SetSystemParamsResult(String str, int i, int i2) {
        if (i != 28) {
            switch (i) {
                case 10:
                    if (userInterface != null) {
                        userInterface.callBackSetSystemParamsResult(str, i, i2);
                        break;
                    }
                    break;
                case 11:
                    if (wifiInterface != null) {
                        wifiInterface.callBackSetSystemParamsResult(str, i, i2);
                        break;
                    }
                    break;
                case 12:
                    if (dateTimeInterface != null) {
                        Log.d(this.TAG, "user result:" + i2 + " paramType:" + i);
                        dateTimeInterface.callBackSetSystemParamsResult(str, i, i2);
                        break;
                    }
                    break;
                default:
                    switch (i) {
                        case 16:
                            if (mailInterface != null) {
                                mailInterface.callBackSetSystemParamsResult(str, i, i2);
                                break;
                            }
                            break;
                        case 17:
                            if (ftpInterface != null) {
                                ftpInterface.callBackSetSystemParamsResult(str, i, i2);
                                break;
                            }
                            break;
                        case 18:
                            if (alarmInterface != null) {
                                alarmInterface.callBackSetSystemParamsResult(str, i, i2);
                                break;
                            }
                            break;
                    }
            }
        }
        if (sCardInterface != null) {
            sCardInterface.callBackSetSystemParamsResult(str, i, i2);
        }
    }

    public void CallBackTransCMDString(String str, String str2) {
        Log.e("vstcallback_CGI++++++++", str2);
        String strSpitValue = MyStringUtils.spitValue(str2, "cmd=");
        String strSpitValue2 = MyStringUtils.spitValue(str2, "type=");
        Log.e("callback_CGI", str2);
        if (strSpitValue.equals("2017") && strSpitValue2.equals("3")) {
            try {
                mTimingInterface.TimingCallback(str, MyStringUtils.spitValue(str2, "command="), MyStringUtils.spitValue(str2, "mask="), MyStringUtils.spitValue(str2, "record_plan1="), MyStringUtils.spitValue(str2, "record_plan2="), MyStringUtils.spitValue(str2, "record_plan3="), MyStringUtils.spitValue(str2, "record_plan4="), MyStringUtils.spitValue(str2, "record_plan5="), MyStringUtils.spitValue(str2, "record_plan6="), MyStringUtils.spitValue(str2, "record_plan7="), MyStringUtils.spitValue(str2, "record_plan8="), MyStringUtils.spitValue(str2, "record_plan9="), MyStringUtils.spitValue(str2, "record_plan10="), MyStringUtils.spitValue(str2, "record_plan11="), MyStringUtils.spitValue(str2, "record_plan12="), MyStringUtils.spitValue(str2, "record_plan13="), MyStringUtils.spitValue(str2, "record_plan14="), MyStringUtils.spitValue(str2, "record_plan15="), MyStringUtils.spitValue(str2, "record_plan16="), MyStringUtils.spitValue(str2, "record_plan17="), MyStringUtils.spitValue(str2, "record_plan18="), MyStringUtils.spitValue(str2, "record_plan19="), MyStringUtils.spitValue(str2, "record_plan20="), MyStringUtils.spitValue(str2, "record_plan21="), MyStringUtils.spitValue(str2, "record_plan_enable="));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (strSpitValue.equals("2017") && strSpitValue2.equals("1")) {
            try {
                mVideoTimingInterface.VideoTimingCallback(str, MyStringUtils.spitValue(str2, "command="), MyStringUtils.spitValue(str2, "mask="), MyStringUtils.spitValue(str2, "motion_record_plan1="), MyStringUtils.spitValue(str2, "motion_record_plan2="), MyStringUtils.spitValue(str2, "motion_record_plan3="), MyStringUtils.spitValue(str2, "motion_record_plan4="), MyStringUtils.spitValue(str2, "motion_record_plan5="), MyStringUtils.spitValue(str2, "motion_record_plan6="), MyStringUtils.spitValue(str2, "motion_record_plan7="), MyStringUtils.spitValue(str2, "motion_record_plan8="), MyStringUtils.spitValue(str2, "motion_record_plan9="), MyStringUtils.spitValue(str2, "motion_record_plan10="), MyStringUtils.spitValue(str2, "motion_record_plan11="), MyStringUtils.spitValue(str2, "motion_record_plan12="), MyStringUtils.spitValue(str2, "motion_record_plan13="), MyStringUtils.spitValue(str2, "motion_record_plan14="), MyStringUtils.spitValue(str2, "motion_record_plan15="), MyStringUtils.spitValue(str2, "motion_record_plan16="), MyStringUtils.spitValue(str2, "motion_record_plan17="), MyStringUtils.spitValue(str2, "motion_record_plan18="), MyStringUtils.spitValue(str2, "motion_record_plan19="), MyStringUtils.spitValue(str2, "motion_record_plan20="), MyStringUtils.spitValue(str2, "motion_record_plan21="), MyStringUtils.spitValue(str2, "motion_record_enable="));
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        if (strSpitValue.equals("2017") && strSpitValue2.equals("2")) {
            try {
                mPushTimingInterface.PushTimingCallback(str, MyStringUtils.spitValue(str2, "command="), MyStringUtils.spitValue(str2, "mask="), MyStringUtils.spitValue(str2, "motion_push_plan1="), MyStringUtils.spitValue(str2, "motion_push_plan2="), MyStringUtils.spitValue(str2, "motion_push_plan3="), MyStringUtils.spitValue(str2, "motion_push_plan4="), MyStringUtils.spitValue(str2, "motion_push_plan5="), MyStringUtils.spitValue(str2, "motion_push_plan6="), MyStringUtils.spitValue(str2, "motion_push_plan7="), MyStringUtils.spitValue(str2, "motion_push_plan8="), MyStringUtils.spitValue(str2, "motion_push_plan9="), MyStringUtils.spitValue(str2, "motion_push_plan10="), MyStringUtils.spitValue(str2, "motion_push_plan11="), MyStringUtils.spitValue(str2, "motion_push_plan12="), MyStringUtils.spitValue(str2, "motion_push_plan13="), MyStringUtils.spitValue(str2, "motion_push_plan14="), MyStringUtils.spitValue(str2, "motion_push_plan15="), MyStringUtils.spitValue(str2, "motion_push_plan16="), MyStringUtils.spitValue(str2, "motion_push_plan17="), MyStringUtils.spitValue(str2, "motion_push_plan18="), MyStringUtils.spitValue(str2, "motion_push_plan19="), MyStringUtils.spitValue(str2, "motion_push_plan20="), MyStringUtils.spitValue(str2, "motion_push_plan21="), MyStringUtils.spitValue(str2, "motion_push_enable="));
                return;
            } catch (Exception e3) {
                e3.printStackTrace();
                return;
            }
        }
        if (strSpitValue.equals("2108")) {
            try {
                String strSpitValue3 = MyStringUtils.spitValue(str2, "command=");
                if (strSpitValue3.equals("-1")) {
                    return;
                }
                if (strSpitValue3.equals("0") || strSpitValue3.equals("1")) {
                    String strSpitValue4 = MyStringUtils.spitValue(str2, "sirenMode=");
                    String strSpitValue5 = MyStringUtils.spitValue(str2, "lightMode=");
                    if (mLowPwerInterface != null) {
                        mLowPwerInterface.LowPwerCallBack(str, strSpitValue, strSpitValue3, strSpitValue4 + strSpitValue5);
                    }
                    if (mLowPwerInterfaceForIndexCgiHelper != null) {
                        mLowPwerInterfaceForIndexCgiHelper.LowPwerCallBack(str, strSpitValue, strSpitValue3, strSpitValue4, strSpitValue5);
                        return;
                    }
                    return;
                }
                return;
            } catch (Exception e4) {
                e4.printStackTrace();
                return;
            }
        }
        if (strSpitValue.equals("2109")) {
            try {
                if (mCameraLightInterface != null) {
                    String strSpitValue6 = MyStringUtils.spitValue(str2, "command=");
                    if (strSpitValue6.equals("2")) {
                        mCameraLightInterface.LightSireCallBack(str, strSpitValue, strSpitValue6, MyStringUtils.spitValue(str2, "sirenStatus="), MyStringUtils.spitValue(str2, "lightStatus="));
                    } else if (strSpitValue6.equals("0")) {
                        String strSpitValue7 = MyStringUtils.spitValue(str2, "siren=");
                        String strSpitValue8 = MyStringUtils.spitValue(str2, "light=");
                        if (strSpitValue6.equals("-1")) {
                        } else {
                            mLowPwerInterfaceForIndexCgiHelper.LowPwerCallBack(str, strSpitValue, strSpitValue6, strSpitValue7, strSpitValue8);
                        }
                    }
                }
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
    }

    public void CallBack_CameraParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Log.d("ddd", "CallBack_CameraParams");
        if (playInterface != null) {
            playInterface.callBackCameraParamNotify(str, i, i2, i3, i4, i5, i6, i8);
        }
    }

    public void CallBack_WifiParams(String str, int i, String str2, int i2, int i3, int i4, int i5, int i6, int i7, String str3, String str4, String str5, String str6, int i8, int i9, int i10, int i11, String str7) {
        Log.d("ddd", "CallBack_WifiParams");
        if (wifiInterface != null) {
            wifiInterface.callBackWifiParams(str, i, str2, i2, i3, i4, i5, i6, i7, str3, str4, str5, str6, i8, i9, i10, i11, str7);
        }
    }

    public void CallBack_UserParams(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Log.d("ddd", "CallBack_UserParams");
        if (userInterface != null) {
            userInterface.callBackUserParams(str, str2, str3, str4, str5, str6, str7);
        }
        if (ipcamClientInterface != null) {
            ipcamClientInterface.callBackUserParams(str, str2, str3, str4, str5, str6, str7);
        }
    }

    public void CallBack_FtpParams(String str, String str2, String str3, String str4, String str5, int i, int i2, int i3) {
        if (ftpInterface != null) {
            ftpInterface.callBackFtpParams(str, str2, str3, str4, str5, i, i2, i3);
        }
    }

    public void CallBack_DDNSParams(String str, int i, String str2, String str3, String str4, String str5, int i2, int i3) {
        Log.d("ddd", "CallBack_DDNSParams");
    }

    public void CallBack_MailParams(String str, String str2, int i, String str3, String str4, int i2, String str5, String str6, String str7, String str8, String str9) {
        if (mailInterface != null) {
            mailInterface.callBackMailParams(str, str2, i, str3, str4, i2, str5, str6, str7, str8, str9);
        }
    }

    public void CallBack_DatetimeParams(String str, int i, int i2, int i3, String str2) {
        if (dateTimeInterface != null) {
            dateTimeInterface.callBackDatetimeParams(str, i, i2, i3, str2);
        }
    }

    public void PPPPSnapshotNotify(String str, byte[] bArr, int i) {
        Log.d(this.TAG, "PPPPSnapshotNotify  did:" + str + " len:" + i);
        if (ipcamClientInterface != null) {
            ipcamClientInterface.BSSnapshotNotify(str, bArr, i);
        }
    }

    public void CallBack_Snapshot(String str, byte[] bArr, int i) {
        if (ipcamClientInterface != null) {
            ipcamClientInterface.BSSnapshotNotify(str, bArr, i);
        }
    }

    public void CallBack_NetworkParams(String str, String str2, String str3, String str4, String str5, String str6, int i, int i2, int i3) {
        Log.d("ddd", "CallBack_NetworkParams");
    }

    public void CallBack_CameraStatusParams(String str, String str2, String str3, String str4, String str5, String str6, int i, int i2, int i3, int i4) {
        if (ipcamClientInterface != null) {
            ipcamClientInterface.CameraStatus(str, i);
        }
        if (updatefirmware != null) {
            Log.i("info", "othersSettingActivity");
            updatefirmware.CallBack_UpdateFirmware(str, str2, str5, str6);
        }
    }

    public void CallBack_PTZParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        Log.d("ddd", "CallBack_PTZParams");
    }

    public void CallBack_WifiScanResult(String str, String str2, String str3, int i, int i2, int i3, int i4, int i5, int i6) {
        Log.d("tag", "CallBack_WifiScanResult");
        if (wifiInterface != null) {
            wifiInterface.callBackWifiScanResult(str, str2, str3, i, i2, i3, i4, i5, i6);
        }
    }

    public void CallBack_AlarmParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, int i31, int i32, int i33, int i34, int i35, int i36, int i37, int i38, int i39, int i40, int i41, int i42, int i43, int i44, int i45, int i46, int i47, int i48, int i49, int i50, int i51, int i52, int i53, int i54, int i55, int i56) {
        if (alarmInterface != null) {
            alarmInterface.callBackAlarmParams(str, i, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20, i21, i22, i23, i24, i25, i26, i27, i28, i29, i30, i31, i32, i33, i34);
        }
        if (alarmParamsInterface != null) {
            alarmParamsInterface.CallBack_AlarmParams(str, i, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20, i21, i22, i23, i24, i25, i26, i27, i28, i29, i30, i31, i32, i33, i34, i35, i36, i37, i38, i39, i40, i41, i42, i43, i44, i45, i46, i47, i48, i49, i50, i51, i52, i53, i54, i55);
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0021. Please report as an issue. */
    public void CallBack_AlarmNotify(String str, int i) throws Resources.NotFoundException {
        Log.d("tag", "callBack_AlarmNotify did:" + str + " alarmtype:" + i);
        switch (i) {
            case 1:
                getResources().getString(R.string.alerm_motion_alarm);
                break;
            case 2:
                getResources().getString(R.string.alerm_gpio_alarm);
                break;
            default:
                switch (i) {
                }
        }
    }

    public void CallBack_RecordFileSearchResult(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6) {
        Log.d("info", "CallBack_RecordFileSearchResult did: " + str + " filename: " + str2 + " size: " + i);
        if (playBackTFInterface != null) {
            playBackTFInterface.callBackRecordFileSearchResult(str, str2, i, i2, i3, i4, i5, i6);
        }
    }

    public void CallBack_PlaybackVideoData(String str, byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Log.d(this.TAG, "CallBack_PlaybackVideoData  len:" + i2 + " width:" + i3 + " height:" + i4);
        if (playBackInterface != null) {
            playBackInterface.callBackPlaybackVideoData(bArr, i, i2, i3, i4, i5, i7, i8, 0.0f, 0.0f);
        }
    }

    public void CallBack_RecordSchParams(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30) {
        if (sCardInterface != null) {
            sCardInterface.callBackRecordSchParams(str, i, i2, i3, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15, i16, i17, i18, i19, i20, i21, i22, i23, i24, i25, i26, i27, i28, i29, i30);
        }
        Log.e(this.TAG, "录像计划:record_schedule_sun_0=" + i6 + ",record_schedule_sun_1=" + i7 + ",record_schedule_sun_2=" + i8 + ",record_schedule_mon_0=" + i9 + ",record_schedule_mon_1=" + i10 + ",record_schedule_mon_2=" + i11);
    }

    public static void setFirmware(Firmware firmware) {
        updatefirmware = firmware;
    }

    public static void setLowPwerInterface(LowPwerInterface2109 lowPwerInterface2109) {
        mLowPwerInterfaceForIndexCgiHelper = lowPwerInterface2109;
    }

    public static void setLowPwerInterfaceForLightLevel(LowPwerInterface lowPwerInterface) {
        mLowPwerInterfaceForLightLevel = lowPwerInterface;
    }

    public static void setCameraLightInterfaceInterface(CameraLightInterfaceInterface cameraLightInterfaceInterface) {
        mCameraLightInterface = cameraLightInterfaceInterface;
    }

    public static void setIpcamClientInterface(IpcamClientInterface ipcamClientInterface2) {
        ipcamClientInterface = ipcamClientInterface2;
    }

    public static void setPictureInterface(PictureInterface pictureInterface2) {
        pictureInterface = pictureInterface2;
    }

    public static void setVideoInterface(VideoInterface videoInterface2) {
        videoInterface = videoInterface2;
    }

    public static void setWifiInterface(WifiInterface wifiInterface2) {
        wifiInterface = wifiInterface2;
    }

    public static void setTimingInterface(TimingInterface timingInterface) {
        mTimingInterface = timingInterface;
    }

    public static void setVideoTimingInterface(VideoTimingInterface videoTimingInterface) {
        mVideoTimingInterface = videoTimingInterface;
    }

    public static void setPushTimingInterface(PushTimingInterface pushTimingInterface) {
        mPushTimingInterface = pushTimingInterface;
    }

    public static void setUserInterface(UserInterface userInterface2) {
        userInterface = userInterface2;
    }

    public static void setAlarmInterface(AlarmInterface alarmInterface2) {
        alarmInterface = alarmInterface2;
    }

    public static void setCallBack_AlarmParamsInterface(CallBack_AlarmParamsInterface callBack_AlarmParamsInterface) {
        alarmParamsInterface = callBack_AlarmParamsInterface;
    }

    public static void setCallBack_AlarmParamsInterfaceToNull() {
        alarmParamsInterface = null;
    }

    public static void setDateTimeInterface(DateTimeInterface dateTimeInterface2) {
        dateTimeInterface = dateTimeInterface2;
    }

    public static void setMailInterface(MailInterface mailInterface2) {
        mailInterface = mailInterface2;
    }

    public static void setFtpInterface(FtpInterface ftpInterface2) {
        ftpInterface = ftpInterface2;
    }

    public static void setSDCardInterface(SDCardInterface sDCardInterface) {
        sCardInterface = sDCardInterface;
    }

    public static void setPlayInterface(PlayInterface playInterface2) {
        playInterface = playInterface2;
    }

    public static void getPlayBackVideo(PlayBackInterface playBackInterface2) {
        playBackInterface = playBackInterface2;
    }

    public static void setPlayBackTFInterface(PlayBackTFInterface playBackTFInterface2) {
        playBackTFInterface = playBackTFInterface2;
    }

    public static void setPlayBackInterface(PlayBackInterface playBackInterface2) {
        playBackInterface = playBackInterface2;
    }

    public static void setAddCameraInterface(AddCameraInterface addCameraInterface2) {
        addCameraInterface = addCameraInterface2;
    }

    public static void setSensorListInterface(SensorListActivityAllDataInterface sensorListActivityAllDataInterface) {
        sensorListInterfece = sensorListActivityAllDataInterface;
    }

    public static void setSensornameInterface(EditSensorListActivityInterface editSensorListActivityInterface) {
        setEditSensor = editSensorListActivityInterface;
    }

    public static void setCallBackMessage(CallBackMessageInterface callBackMessageInterface) {
        messageInterface = callBackMessageInterface;
    }

    public void CallBackTransferMessage(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        Log.e("info", "Service CallBackTransferMessage---resultPbuf:" + str2 + "--did:" + str + "---cmd:" + i + ",id1=" + i2 + ",id2=" + i3 + ",id3=" + i4 + ",sensortype=" + i5 + ",sensortatus=" + i6 + ",presetid=" + i7 + ",index:" + i8);
        if (i == 24756 && setEditSensor != null) {
            setEditSensor.CallBackMessages(str, str2, i);
        }
        if (i == 24755 && setEditSensor != null) {
            setEditSensor.CallBackMessages(str, str2, i);
        }
        if (i == 24759) {
            setEditSensor.CallBackMessages(str, str2, i);
        }
        if (i == 24577 && messageInterface != null) {
            messageInterface.CallBackGetStatus(str, str2, i);
        }
        if (sensorListInterfece != null && i != 24759 && i != 24755 && i != 24759 && i != 24753 && i != 24577 && i != 24761 && i != 24756 && i != 24754) {
            sensorListInterfece.CallBackMessage(str, str2, i, i2, i3, i4, i5, i6, i7, i8);
        }
        if (i == 24585 && str2.contains("correctModel=")) {
            int iIndexOf = str2.indexOf("correctModel=") + "correctModel=".length();
            MySharedPreferenceUtil.saveDeviceInformation(this, str, ContentCommon.DEVICE_MODEL_TYPE, str2.substring(iIndexOf, iIndexOf + 1));
        }
    }

    public void CallBackAlermMessage(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        Log.e("info", "CallBackAlermMessage=====shix name:" + str2 + "  headcmd:" + i + "  selfcmd:" + i2 + "  linkpreset:" + i3 + "  sensortype:" + i4 + "   sensoraction:" + i5 + "   channel:" + i6 + "  sensorid1:" + i7 + "  sensorid2" + i8 + "  sensorid3:" + i9);
        if (i2 == 1 && i5 == 8) {
            setCodeInterface.CallBackReCodeMessage(str, str2, i, i2, i3, i4, i5, i6, i7, i8, i9);
        }
    }

    public static void setCodeInterface(SensorSetCodeInterface sensorSetCodeInterface) {
        setCodeInterface = sensorSetCodeInterface;
    }

    public void CallBack_TFCardPlayback(String str, byte[] bArr, int i, int i2, int i3, int i4, int i5, float f, float f2) {
        Log.e("videodate brig", DatabaseUtil.KEY_DID + str + "yuv" + bArr.length + "width" + i3 + "height" + i4);
        if (playBackInterface != null) {
            playBackInterface.callBackPlaybackVideoData(bArr, 1, i2, i3, i4, i5, i, 0, f, f2);
        }
    }
}
