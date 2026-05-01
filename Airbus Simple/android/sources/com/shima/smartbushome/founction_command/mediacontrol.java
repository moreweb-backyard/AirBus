package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class mediacontrol {
    public boolean IRControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, (byte) i2};
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

    public boolean IRMarcoControl(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
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

    public boolean ReadRemarkOfIR(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            byte b3 = 1;
            boolean z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 55564, b, b2, false)) {
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

    public boolean ReadQTYofIR(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[0];
            short length = (short) bArr.length;
            byte b3 = 1;
            boolean z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 55572, b, b2, false)) {
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

    public boolean ReadIRMarcoRemark(byte b, byte b2, int i, udp_socket udp_socketVar) {
        boolean z;
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            byte b3 = 1;
            z = false;
            while (b3 < 2) {
                try {
                    if (!udp_socketVar.SendUDPBuffer(bArr, length, 56378, b, b2, false)) {
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
