package com.shima.smartbushome.founction_command;

import android.os.Handler;
import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class othercontrol {
    int channel2;
    byte dev;
    Handler doublesenthandler = new Handler();
    Runnable doublesentrun = new Runnable() { // from class: com.shima.smartbushome.founction_command.othercontrol.1
        @Override // java.lang.Runnable
        public void run() {
            othercontrol.this.SingleChannelControl(othercontrol.this.sub, othercontrol.this.dev, othercontrol.this.channel2, 0, othercontrol.this.udpsocket);
            othercontrol.this.doublesenthandler.removeCallbacks(othercontrol.this.doublesentrun);
        }
    };
    byte sub;
    udp_socket udpsocket;

    public void CurtainControl(byte b, byte b2, int i, int i2, String str, udp_socket udp_socketVar) {
        this.udpsocket = udp_socketVar;
        if (str.equals("open")) {
            SingleChannelControl(b, b2, i, 100, udp_socketVar);
            return;
        }
        if (str.equals("close")) {
            SingleChannelControl(b, b2, i2, 100, udp_socketVar);
            return;
        }
        if (str.equals("stop")) {
            this.sub = b;
            this.dev = b2;
            this.channel2 = i2;
            SingleChannelControl(b, b2, i, 0, udp_socketVar);
            this.doublesenthandler.postDelayed(this.doublesentrun, 90L);
        }
    }

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

    public boolean logicSingleControl(byte b, byte b2, int i, udp_socket udp_socketVar) {
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

    public void getonoffstate(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (byte b3 = 0; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 51, b, b2, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDryContectState(byte b, byte b2, udp_socket udp_socketVar) {
        for (byte b3 = 0; b3 < 2; b3 = (byte) (b3 + 1)) {
            try {
                udp_socketVar.SendUDPBuffer(null, (short) 0, 300, b, b2, false);
            } catch (Exception unused) {
                return;
            }
        }
    }

    public boolean SceneControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        boolean z;
        try {
            byte[] bArr = {(byte) i, (byte) i2};
            short length = (short) bArr.length;
            byte b3 = 1;
            z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 2, b, b2, false)) {
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

    public boolean SequenceControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        boolean z;
        try {
            byte[] bArr = {(byte) i, (byte) i2};
            short length = (short) bArr.length;
            byte b3 = 1;
            z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 26, b, b2, false)) {
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
