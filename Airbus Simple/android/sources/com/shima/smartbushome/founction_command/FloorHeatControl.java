package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class FloorHeatControl {
    public boolean ConfigureModeAndSensorAddressControl(byte b, byte b2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, udp_socket udp_socketVar) {
        boolean z = false;
        try {
            int i13 = 1;
            byte[] bArr = {(byte) i, (byte) i2, (byte) i3, (byte) i4, (byte) i5, (byte) i6, (byte) i7, (byte) i8, (byte) i9, (byte) i10, (byte) i11, (byte) i12};
            short length = (short) bArr.length;
            while (i13 < 2) {
                boolean zSendUDPBuffer = udp_socketVar.SendUDPBuffer(bArr, length, 965, b, b2, false);
                if (zSendUDPBuffer) {
                    return zSendUDPBuffer;
                }
                i13++;
                z = zSendUDPBuffer;
            }
            return z;
        } catch (Exception e) {
            e.printStackTrace();
            return z;
        }
    }

    public boolean ReadodeModeStateAndSensorAddressControl(byte b, byte b2, int i, udp_socket udp_socketVar) {
        boolean z = false;
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            int i2 = 1;
            boolean z2 = false;
            while (i2 < 2) {
                try {
                    boolean zSendUDPBuffer = udp_socketVar.SendUDPBuffer(bArr, length, 967, b, b2, false);
                    if (zSendUDPBuffer) {
                        return zSendUDPBuffer;
                    }
                    i2++;
                    z2 = zSendUDPBuffer;
                } catch (Exception e) {
                    e = e;
                    z = z2;
                    e.printStackTrace();
                    return z;
                }
            }
            return z2;
        } catch (Exception e2) {
            e = e2;
        }
    }

    public boolean TypeValueControl(byte b, byte b2, byte b3, int i, int i2, udp_socket udp_socketVar) {
        boolean z;
        try {
            byte[] bArr = {b3, (byte) i, (byte) i2};
            short length = (short) bArr.length;
            int i3 = 1;
            z = false;
            while (i3 < 2) {
                try {
                    boolean zSendUDPBuffer = udp_socketVar.SendUDPBuffer(bArr, length, 58328, b, b2, false);
                    if (zSendUDPBuffer) {
                        return zSendUDPBuffer;
                    }
                    i3++;
                    z = zSendUDPBuffer;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    return z;
                }
            }
            return z;
        } catch (Exception e2) {
            e = e2;
            z = false;
        }
    }

    public boolean ReadOnOrOFFAndModeControl(byte b, byte b2, byte b3, int i, udp_socket udp_socketVar) {
        boolean z;
        try {
            byte[] bArr = {b3, (byte) i};
            short length = (short) bArr.length;
            int i2 = 1;
            z = false;
            while (i2 < 2) {
                try {
                    boolean zSendUDPBuffer = udp_socketVar.SendUDPBuffer(bArr, length, 58330, b, b2, false);
                    if (zSendUDPBuffer) {
                        return zSendUDPBuffer;
                    }
                    i2++;
                    z = zSendUDPBuffer;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    return z;
                }
            }
            return z;
        } catch (Exception e2) {
            e = e2;
            z = false;
        }
    }

    public boolean ConfigureTimeControl(byte b, byte b2, int i, int i2, int i3, int i4, int i5, udp_socket udp_socketVar) {
        boolean z;
        try {
            byte[] bArr = {(byte) i, (byte) i2, (byte) i3, (byte) i4, (byte) i5};
            short length = (short) bArr.length;
            int i6 = 1;
            z = false;
            while (i6 < 2) {
                try {
                    boolean zSendUDPBuffer = udp_socketVar.SendUDPBuffer(bArr, length, 969, b, b2, false);
                    if (zSendUDPBuffer) {
                        return zSendUDPBuffer;
                    }
                    i6++;
                    z = zSendUDPBuffer;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    return z;
                }
            }
            return z;
        } catch (Exception e2) {
            e = e2;
            z = false;
        }
    }

    public boolean ReadTimeControl(byte b, byte b2, int i, udp_socket udp_socketVar) {
        boolean z = false;
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            int i2 = 1;
            boolean z2 = false;
            while (i2 < 2) {
                try {
                    boolean zSendUDPBuffer = udp_socketVar.SendUDPBuffer(bArr, length, 971, b, b2, false);
                    if (zSendUDPBuffer) {
                        return zSendUDPBuffer;
                    }
                    i2++;
                    z2 = zSendUDPBuffer;
                } catch (Exception e) {
                    e = e;
                    z = z2;
                    e.printStackTrace();
                    return z;
                }
            }
            return z2;
        } catch (Exception e2) {
            e = e2;
        }
    }

    public void ReadTemperatureValue(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 58343, b, b2, false);
        } catch (Exception unused) {
        }
    }
}
