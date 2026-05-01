package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class lightcontrol {
    public boolean SingleChannelControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, (byte) i2, 0, 0};
            short length = (short) bArr.length;
            byte b3 = 1;
            boolean z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 49, b, b2, false)) {
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

    public void ARGBlightcontrol(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {getcolor("R", i), getcolor("G", i), getcolor("B", i), (byte) (100 - getcolor("A", i)), 0, 0};
            short length = (short) bArr.length;
            System.out.println("sentvalue:   a:" + ((int) bArr[3]) + "    r:" + ((int) bArr[0]) + "    g:" + ((int) bArr[1]) + "      b:" + ((int) bArr[2]));
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 61568, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public byte getcolor(String str, int i) {
        if (str.equals("A")) {
            return (byte) ((Integer.parseInt(Integer.toHexString(i).substring(0, 2), 16) * 3922) / 10000);
        }
        if (str.equals("R")) {
            return (byte) ((Integer.parseInt(Integer.toHexString(i).substring(2, 4), 16) * 3922) / 10000);
        }
        if (str.equals("G")) {
            return (byte) ((Integer.parseInt(Integer.toHexString(i).substring(4, 6), 16) * 3922) / 10000);
        }
        if (str.equals("B")) {
            return (byte) ((Integer.parseInt(Integer.toHexString(i).substring(6, 8), 16) * 3922) / 10000);
        }
        return (byte) 0;
    }

    public void getlightstate(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 51, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
