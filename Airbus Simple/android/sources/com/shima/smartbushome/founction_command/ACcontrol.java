package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class ACcontrol {
    public boolean ACControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, (byte) i2};
            return udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 58328, b, b2, false);
        } catch (Exception unused) {
            return false;
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
        } catch (Exception unused) {
        }
    }

    public void ACReadTemperatureValue(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 58343, b, b2, false);
        } catch (Exception unused) {
        }
    }
}
