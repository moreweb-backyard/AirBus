package vstc2.nativecaller;

import android.content.Context;

/* loaded from: classes.dex */
public class NativeCaller {
    public static native int DecodeAudio(byte[] bArr, int i, int i2, int i3, int i4);

    public static native int DecodeH264Frame(byte[] bArr, int i, byte[] bArr2, int i2, int[] iArr);

    public static native int DeleSensor(String str, int i);

    public static native int EditSensor(String str, int i, String str2);

    public static native int FindProcessByName(String str);

    public static native void FisheyeYUVdataSplit(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i, int i2, int i3);

    public static native void FormatSD(String str);

    public static native void Free();

    public static native int FreeDecodeAudio();

    public static native int GetConnectStatus();

    public static native int GetP2PVersion(int i);

    public static native int GetVersion();

    public static native void Init();

    public static native int IsPTPInitial();

    public static native int IsQXPTPInitial();

    public static native int MagLowpowerAwakenDevice(String str);

    public static native int MagLowpowerDeviceConnect(String str);

    public static native void MagLowpowerDeviceDisconnect();

    public static native int MagLowpowerGetDeviceStatus(String str);

    public static native int MagLowpowerInitDevice(String str);

    public static native int PPPPAlarmSetting(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27, int i28, int i29, int i30, int i31, int i32, int i33, int i34, int i35, int i36, int i37, int i38, int i39, int i40, int i41, int i42, int i43, int i44, int i45, int i46, int i47, int i48, int i49, int i50, int i51, int i52, int i53, int i54, int i55, int i56);

    public static native int PPPPCameraControl(String str, int i, int i2);

    public static native int PPPPCameraStatus(String str, int i);

    public static native int PPPPDDNSSetting(String str, int i, String str2, String str3, String str4, String str5, int i2, int i3);

    public static native int PPPPDatetimeSetting(String str, int i, int i2, int i3, String str2);

    public static native int PPPPEverydaySetting(String str, int i, int i2, int i3, int i4, int i5, int i6);

    public static native int PPPPFtpSetting(String str, String str2, String str3, String str4, String str5, int i, int i2, int i3);

    public static native int PPPPGetCGI(String str, int i);

    public static native int PPPPGetSDCardRecordFileList(String str, int i, int i2);

    public static native int PPPPGetSystemParams(String str, int i);

    public static native void PPPPInitial(String str);

    public static native void PPPPInitialOther(String str);

    public static native int PPPPMailSetting(String str, String str2, int i, String str3, String str4, int i2, String str5, String str6, String str7, String str8, String str9);

    public static native int PPPPNetworkDetect();

    public static native int PPPPNetworkSetting(String str, String str2, String str3, String str4, String str5, String str6, int i, int i2, int i3);

    public static native int PPPPPTZControl(String str, int i);

    public static native int PPPPPTZSetting(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    public static native int PPPPRebootDevice(String str);

    public static native int PPPPRestorFactory(String str);

    public static native int PPPPSDRecordSetting(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18, int i19, int i20, int i21, int i22, int i23, int i24, int i25, int i26, int i27);

    public static native int PPPPSetCallbackContext(Context context);

    public static native int PPPPStartAudio(String str);

    public static native int PPPPStartTalk(String str);

    public static native int PPPPStartTalk2(String str, int i);

    public static native int PPPPStopAudio(String str);

    public static native int PPPPStopTalk(String str);

    public static native int PPPPTalkAudioData(String str, byte[] bArr, int i);

    public static native int PPPPUserSetting(String str, String str2, String str3, String str4, String str5, String str6, String str7);

    public static native int PPPPWifiSetting(String str, int i, String str2, int i2, int i3, int i4, int i5, int i6, int i7, String str3, String str4, String str5, String str6, int i8, int i9, int i10, int i11, String str7);

    public static native void PTPInitial(String str);

    public static native int PausePlayBack(String str, int i);

    public static native long PlayBackMovePos(String str, float f);

    public static native void PrintJNILog(int i);

    public static native void QXPTPInitial(String str);

    public static native int RecordLocal(String str, String str2, int i);

    public static native int ResetDecodeH264();

    public static native void SetAPPDataPath(String str);

    public static native int SetHardCodeing(String str, int i);

    public static native int SetPlayBackPos(String str, long j);

    public static native int SetSensorPrest(String str, int i, int i2);

    public static native int SetSensorStatus(String str, int i);

    public static native int StartPPPP(String str, String str2, String str3, int i, String str4, int i2);

    public static native int StartPPPPExt(String str, String str2, String str3, int i, String str4, String str5, int i2);

    public static native int StartPPPPLivestream(String str, int i, int i2);

    public static native int StartPlayBack(String str, String str2, int i, long j, String str3, int i2, int i3);

    public static native void StartSearch();

    public static native int StopPPPP(String str);

    public static native int StopPPPPLivestream(String str);

    public static native int StopPlayBack(String str);

    public static native void StopSearch();

    public static native int StrarRecordPlayBack(String str, String str2);

    public static native int TransferMessage(String str, String str2, int i);

    public static native void UpgradeFirmware(String str, String str2, String str3, int i);

    public static native int YUV4202RGB565(byte[] bArr, byte[] bArr2, int i, int i2);

    public static native void YUV420OFFSET(byte[] bArr, byte[] bArr2, int i, int i2);

    public static native void YUV420SPTOYUV420P(byte[] bArr, byte[] bArr2, int i);

    public static native void YUV420SPTOYUV420POFFSET(byte[] bArr, byte[] bArr2, int i, int i2);

    static {
        System.loadLibrary("vstc2_jni");
    }
}
