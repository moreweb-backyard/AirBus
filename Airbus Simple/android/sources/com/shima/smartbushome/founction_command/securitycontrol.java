package com.shima.smartbushome.founction_command;

import android.support.v4.view.MotionEventCompat;
import com.shima.smartbushome.udp.udp_socket;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class securitycontrol {
    public void setAlarm(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte b3 = (byte) i2;
            byte[] bArr = {(byte) i, b3};
            short length = (short) bArr.length;
            for (byte b4 = 1; b4 < 2; b4 = (byte) (b4 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 260, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadAreaName(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 584, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadLogSum(byte b, byte b2, String str, String str2, udp_socket udp_socketVar) {
        try {
            String[] strArrSplit = str.split("-");
            String[] strArrSplit2 = str2.split("-");
            byte[] bArr = {(byte) Integer.parseInt(strArrSplit[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit[1]), (byte) Integer.parseInt(strArrSplit[2]), (byte) Integer.parseInt(strArrSplit2[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit2[1]), (byte) Integer.parseInt(strArrSplit2[2])};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 312, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadLogvalue(byte b, byte b2, String str, String str2, byte[] bArr, int i, udp_socket udp_socketVar) {
        try {
            String[] strArrSplit = str.split("-");
            String[] strArrSplit2 = str2.split("-");
            int i2 = i & MotionEventCompat.ACTION_POINTER_INDEX_MASK;
            byte[] bArr2 = {(byte) Integer.parseInt(strArrSplit[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit[1]), (byte) Integer.parseInt(strArrSplit[2]), (byte) Integer.parseInt(strArrSplit2[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit2[1]), (byte) Integer.parseInt(strArrSplit2[2]), bArr[0], bArr[1], (byte) (i2 >> 8), (byte) (i - i2)};
            short length = (short) bArr2.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr2, length, 314, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ClearLog(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {-8, 56, 56, 56, 56, 56, 56};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 330, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void WriteAreaName(byte b, byte b2, int i, String str, udp_socket udp_socketVar) throws UnsupportedEncodingException {
        try {
            byte[] bytes = str.getBytes("UnicodeLittleUnmarked");
            byte[] bArr = new byte[bytes.length];
            for (int i2 = 0; i2 < bytes.length; i2++) {
                bArr[i2] = bytes[(bytes.length - 1) - i2];
            }
            byte[] bArr2 = new byte[20];
            if (bArr.length < 20) {
                for (int i3 = 0; i3 < 20; i3++) {
                    if (i3 < bArr.length) {
                        bArr2[i3] = bArr[i3];
                    } else {
                        bArr2[i3] = 32;
                    }
                }
            }
            byte[] bArr3 = new byte[22];
            bArr3[0] = (byte) i;
            bArr3[1] = 1;
            for (int i4 = 0; i4 < 20; i4++) {
                bArr3[i4 + 2] = bArr2[i4];
            }
            short length = (short) bArr3.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr3, length, 586, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void setBypass(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, 0, (byte) i2};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 304, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadBypass(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, 0};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 302, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadWatchDog(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 306, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void WriteWatchDog(byte b, byte b2, int i, int i2, byte[] bArr, udp_socket udp_socketVar) {
        try {
            byte[] bArr2 = {(byte) i, 0, bArr[0], (byte) i2, bArr[1], bArr[2], bArr[3], bArr[4], bArr[5], bArr[6], bArr[7], bArr[8], bArr[9]};
            short length = (short) bArr2.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr2, length, 308, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }
}
