package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class radiocontrol {
    public void SwitchtoRadio(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1, 4, 0, 0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3, b4};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
        } catch (Exception unused) {
        }
    }

    public void GetChannelValue(int i, byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 57664, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void WriteChannelValue(int i, int i2, byte[] bArr, byte b, byte b2, udp_socket udp_socketVar) {
        try {
            int i3 = 65280 & i2;
            byte[] bArr2 = {(byte) (i3 >> 8), (byte) (i2 - i3)};
            byte[] bArr3 = new byte[43];
            bArr3[0] = (byte) i;
            bArr3[1] = bArr2[0];
            bArr3[2] = bArr2[1];
            for (int i4 = 0; i4 < 40; i4++) {
                bArr3[i4 + 3] = bArr[i4];
            }
            udp_socketVar.SendUDPBuffer(bArr3, (short) bArr3.length, 57666, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void GetMusicState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 90, 49, 83, 84, 65, 84, 85, 83, 63, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
