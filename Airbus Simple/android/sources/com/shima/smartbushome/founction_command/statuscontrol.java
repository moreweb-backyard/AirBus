package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class statuscontrol {
    public void getlightstate(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (byte b3 = 0; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 51, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadTemp(byte b, byte b2, byte b3, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[1];
            switch (b3) {
                case 1:
                    bArr[0] = 1;
                    break;
                case 2:
                    bArr[0] = 0;
                    break;
            }
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 58343, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void ACReadCFFlag(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 57632, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void ACReadTempRange(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6400, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void ACReadCountFanAndMode(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 57636, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void ACReadCState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 57580, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
