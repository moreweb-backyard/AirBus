package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class moodcontrol {
    public boolean PanelControl(byte b, byte b2, int i, int i2, boolean z, boolean z2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, (byte) i2};
            short length = (short) bArr.length;
            byte b3 = 1;
            boolean z3 = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 58328, b, b2, false)) {
                        return z3;
                    }
                    b3 = (byte) (b3 + 1);
                    z3 = true;
                } catch (Exception unused) {
                    return z3;
                }
            }
            return z3;
        } catch (Exception unused2) {
            return false;
        }
    }
}
