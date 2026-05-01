package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class energycontrol {
    public void ReadChannelValue(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 336, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }
}
