package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class niocontrol {
    public boolean IRControl(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, -1};
            short length = (short) bArr.length;
            byte b3 = 1;
            boolean z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 57372, b, b2, false)) {
                        return z;
                    }
                    b3 = (byte) (b3 + 1);
                    z = true;
                } catch (Exception unused) {
                    return z;
                }
            }
            return z;
        } catch (Exception unused2) {
            return false;
        }
    }

    public void ReadTempSetting(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 396, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void Readlux(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 55698, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void ReadTemp(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 58343, b, b2, false);
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
