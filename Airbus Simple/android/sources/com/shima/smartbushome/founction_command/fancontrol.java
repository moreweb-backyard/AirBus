package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class fancontrol {
    public boolean FanChannelControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        boolean z;
        int i3 = i2;
        if (i3 == 4) {
            i3 = 100;
        }
        try {
            byte[] bArr = {(byte) i, (byte) i3, 0, 0};
            short length = (short) bArr.length;
            byte b3 = 1;
            z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 49, b, b2, false)) {
                        break;
                    }
                    b3 = (byte) (b3 + 1);
                    z = true;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    return z;
                }
            }
        } catch (Exception e2) {
            e = e2;
            z = false;
        }
        return z;
    }
}
