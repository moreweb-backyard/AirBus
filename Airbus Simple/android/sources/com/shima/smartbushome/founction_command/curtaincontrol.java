package com.shima.smartbushome.founction_command;

import android.os.Handler;
import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class curtaincontrol {
    int channel2;
    byte dev;
    Handler doublesenthandler = new Handler();
    Runnable doublesentrun = new Runnable() { // from class: com.shima.smartbushome.founction_command.curtaincontrol.1
        @Override // java.lang.Runnable
        public void run() {
            curtaincontrol.this.SingleChannelControl(curtaincontrol.this.sub, curtaincontrol.this.dev, curtaincontrol.this.channel2, 0, curtaincontrol.this.udpsocket);
            curtaincontrol.this.doublesenthandler.removeCallbacks(curtaincontrol.this.doublesentrun);
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

    public void SingleChannelControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, (byte) i2, 0, 0};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                if (!udp_socketVar.SendUDPBuffer(bArr, length, 49, b, b2, false)) {
                    return;
                }
            }
        } catch (Exception unused) {
        }
    }

    public void getcurtainstate(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (byte b3 = 0; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 51, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }
}
