`ACcontrol.java`
```
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
```

`FloorHeatControl.java`
```
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
```

`audio_incontrol.java`
```
package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class audio_incontrol {
    public void SwitchtoAudioIn(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1, 2, 0, 0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3, b4};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
        } catch (Exception unused) {
        }
    }

    public void GetMusicState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 90, 49, 83, 84, 65, 84, 85, 83, 63, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

`curtaincontrol.java`
```
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
```

`energycontrol.java`
```
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
```

`fancontrol.java`
```
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
```

`lightcontrol.java`
```
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
```

`mediacontrol.java`
```
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
```

`moodcontrol.java`
```
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
```

`musiccontrol.java`
```
package com.shima.smartbushome.founction_command;

import android.support.v4.view.MotionEventCompat;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import com.shima.smartbushome.udp.udp_socket;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class musiccontrol {
    public void deletesong(int i, int i2, String str, String str2, udp_socket udp_socketVar) {
    }

    public void sentsongcontent(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, udp_socket udp_socketVar) {
        byte[] bArr4 = new byte[bArr2.length + 11];
        bArr4[0] = 4;
        bArr4[1] = 11;
        bArr4[2] = 1;
        bArr4[3] = bArr[0];
        bArr4[4] = bArr[1];
        for (int i3 = 0; i3 < 4; i3++) {
            bArr4[i3 + 5] = bArr3[i3];
        }
        bArr4[9] = (byte) (((bArr2.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        bArr4[10] = (byte) ((bArr2.length - 2) - (65280 & (bArr2.length - 2)));
        for (int i4 = 0; i4 < bArr2.length; i4++) {
            bArr4[i4 + 11] = bArr2[i4];
        }
        udp_socketVar.SendUDPBuffer(bArr4, (short) bArr4.length, 512, (byte) i, (byte) i2, true);
    }

    public byte[] UploadSongStart(int i, int i2, String str, String str2, int i3, udp_socket udp_socketVar) throws UnsupportedEncodingException {
        byte[] bArr = new byte[2];
        int i4 = 0;
        int length = str2.substring(0, str2.indexOf(FileUtil.FILE_EXTENSION_SEPARATOR)).length();
        try {
            int iNextInt = new Random().nextInt(65535);
            byte[] bytes = str.getBytes("ascii");
            byte[] bytes2 = str2.getBytes("UTF-16BE");
            byte[] bytes3 = str2.getBytes("ascii");
            byte[] bytes4 = str2.substring(0, str2.indexOf(FileUtil.FILE_EXTENSION_SEPARATOR)).getBytes("ascii");
            if (length > 8) {
                byte[] bArr2 = new byte[bytes.length + 29 + bytes2.length];
                bArr2[0] = (byte) (((bArr2.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
                bArr2[1] = (byte) ((bArr2.length - 2) - ((bArr2.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK));
                bArr2[2] = 0;
                bArr2[3] = (byte) (iNextInt / 256);
                bArr2[4] = (byte) (iNextInt % 256);
                bArr[0] = bArr2[3];
                bArr[1] = bArr2[4];
                bArr2[6] = 92;
                bArr2[7] = 0;
                for (int i5 = 0; i5 < bytes.length; i5++) {
                    bArr2[i5 + 8] = bytes[i5];
                }
                bArr2[bytes.length + 8] = 0;
                bArr2[bytes.length + 9] = 92;
                for (int i6 = 0; i6 < bytes2.length; i6++) {
                    bArr2[bytes.length + 10 + i6] = bytes2[i6];
                }
                bArr2[5] = (byte) (bytes.length + bytes2.length + 18);
                bArr2[bytes.length + 10 + bytes2.length] = 0;
                bArr2[bytes.length + 11 + bytes2.length] = 0;
                bArr2[bytes.length + 12 + bytes2.length] = 0;
                while (i4 < 6) {
                    bArr2[bytes.length + 13 + bytes2.length + i4] = bytes3[i4];
                    i4++;
                }
                bArr2[bytes.length + 19 + bytes2.length] = 126;
                bArr2[bytes.length + 20 + bytes2.length] = 49;
                bArr2[bytes.length + 21 + bytes2.length] = 77;
                bArr2[bytes.length + 22 + bytes2.length] = 80;
                bArr2[bytes.length + 23 + bytes2.length] = 51;
                bArr2[bytes.length + 24 + bytes2.length] = (byte) (((i3 / 256) / 256) / 256);
                bArr2[bytes.length + 25 + bytes2.length] = (byte) (((i3 / 256) / 256) % 256);
                bArr2[bytes.length + 26 + bytes2.length] = (byte) (i3 / 256);
                bArr2[bytes.length + 27 + bytes2.length] = (byte) (i3 % 256);
                bArr2[bytes.length + 28 + bytes2.length] = 32;
                udp_socketVar.SendUDPBuffer(bArr2, (short) bArr2.length, 512, (byte) i, (byte) i2, true);
            } else {
                byte[] bArr3 = new byte[bytes.length + 29];
                bArr3[0] = (byte) (((bArr3.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
                bArr3[1] = (byte) ((bArr3.length - 2) - ((bArr3.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK));
                bArr3[2] = 0;
                bArr3[3] = (byte) (iNextInt / 256);
                bArr3[4] = (byte) (iNextInt % 256);
                bArr[0] = bArr3[3];
                bArr[1] = bArr3[4];
                bArr3[6] = 92;
                bArr3[7] = 0;
                for (int i7 = 0; i7 < bytes.length; i7++) {
                    bArr3[i7 + 8] = bytes[i7];
                }
                bArr3[bytes.length + 8] = 0;
                bArr3[bytes.length + 9] = 92;
                bArr3[bytes.length + 10] = 0;
                bArr3[bytes.length + 11] = 0;
                bArr3[bytes.length + 12] = 0;
                while (i4 < 8) {
                    if (i4 < bytes4.length) {
                        bArr3[bytes.length + 13 + i4] = bytes4[i4];
                    } else {
                        bArr3[bytes.length + 13 + i4] = 32;
                    }
                    i4++;
                }
                bArr3[bytes.length + 21] = 77;
                bArr3[bytes.length + 22] = 80;
                bArr3[bytes.length + 23] = 51;
                bArr3[bytes.length + 24] = (byte) (((i3 / 256) / 256) / 256);
                bArr3[bytes.length + 25] = (byte) (((i3 / 256) / 256) % 256);
                bArr3[bytes.length + 26] = (byte) (i3 / 256);
                bArr3[bytes.length + 27] = (byte) (i3 % 256);
                bArr3[bytes.length + 28] = 32;
                bArr3[5] = (byte) (bytes.length + 18);
                udp_socketVar.SendUDPBuffer(bArr3, (short) bArr3.length, 512, (byte) i, (byte) i2, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bArr;
    }

    public void GetMusicState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 90, 49, 83, 84, 65, 84, 85, 83, 63, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateZaudiolist(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 83, 49, 85, 80, 68, 65, 84, 69, 76, 73, 83, 84, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SwitchtoMusicSD(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1, 1, 0, 0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl2(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3, b4};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            switch (b & 255) {
                case 1:
                case 2:
                    byte[] bArr = {b, b2, b3, b4};
                    udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
                    break;
                case 4:
                    byte[] bArr2 = {42, 83, 49, 0, 0, 0, 0, MultipartStream.CR, 85, -86, 1, 2, 83, 4, 0, 0, 0, 0, 14, 53};
                    switch (b2 & 255) {
                        case 1:
                            bArr2[3] = 80;
                            bArr2[4] = 82;
                            bArr2[5] = 69;
                            bArr2[6] = 86;
                            break;
                        case 2:
                            bArr2[3] = 78;
                            bArr2[4] = 69;
                            bArr2[5] = 88;
                            bArr2[6] = 84;
                            break;
                        case 3:
                            bArr2[3] = 80;
                            bArr2[4] = 76;
                            bArr2[5] = 65;
                            bArr2[6] = 89;
                            break;
                        case 4:
                            bArr2[3] = 83;
                            bArr2[4] = 84;
                            bArr2[5] = 79;
                            bArr2[6] = 80;
                            break;
                    }
                    udp_socketVar.SendUDPBuffer(bArr2, (short) bArr2.length, 6446, b5, b6, false);
                    break;
                case 5:
                    byte[] bArr3 = {42, 90, 49, 86, 79, 76, 0, 0, MultipartStream.CR, 85, -86, 1};
                    int i = b4 & 255;
                    bArr3[6] = String.valueOf((i / 10) % 10).getBytes("ascii")[0];
                    bArr3[7] = String.valueOf(i % 10).getBytes("ascii")[0];
                    udp_socketVar.SendUDPBuffer(bArr3, (short) bArr3.length, 6446, b5, b6, false);
                    break;
                case 6:
                    byte[] bArr4 = {42, 83, 49, 76, 73, 83, 84, 0, 0, 0, 44, 83, 79, 78, 71, 0, 0, 0, MultipartStream.CR, 85, -86, 1, 2, 83, 4, 0, 0, 0, 0, 14, 53};
                    int i2 = b2 & 255;
                    bArr4[7] = String.valueOf((i2 / 100) % 10).getBytes("ascii")[0];
                    bArr4[8] = String.valueOf((i2 / 10) % 10).getBytes("ascii")[0];
                    bArr4[9] = String.valueOf(i2 % 10).getBytes("ascii")[0];
                    int i3 = (b3 << 8) + (b4 & 255);
                    bArr4[15] = String.valueOf((i3 / 100) % 10).getBytes("ascii")[0];
                    bArr4[16] = String.valueOf((i3 / 10) % 10).getBytes("ascii")[0];
                    bArr4[17] = String.valueOf(i3 % 10).getBytes("ascii")[0];
                    udp_socketVar.SendUDPBuffer(bArr4, (short) bArr4.length, 6446, b5, b6, false);
                    break;
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadAlbumQTY(byte b, byte b2, byte b3, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b};
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 736, b2, b3, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadAlbum(byte b, byte b2, byte b3, byte b4, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 738, b3, b4, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadSongQTY(byte b, byte b2, byte b3, byte b4, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 740, b3, b4, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadSong(byte b, byte b2, byte b3, byte b4, byte b5, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 742, b4, b5, false);
            }
        } catch (Exception unused) {
        }
    }
}
```

`musiccontrol_2.java`
```
package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class musiccontrol_2 {
    public void GetMusicState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 90, 49, 83, 84, 65, 84, 85, 83, 63, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SwitchtoMusicSD(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1, 1, 0, 0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3, b4};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
        } catch (Exception unused) {
        }
    }

    public void MusicReadAlbumQTY(byte b, byte b2, byte b3, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b};
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 736, b2, b3, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadAlbum(byte b, byte b2, byte b3, byte b4, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 738, b3, b4, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadSongQTY(byte b, byte b2, byte b3, byte b4, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 740, b3, b4, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadSong(byte b, byte b2, byte b3, byte b4, byte b5, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 742, b4, b5, false);
            }
        } catch (Exception unused) {
        }
    }
}
```

`niocontrol.java`
```
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
```

`othercontrol.java`
```
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
```

`radiocontrol.java`
```
package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class radiocontrol {
    public void SwitchtoRadio(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1, 4, 0, 0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3, b4};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
        } catch (Exception unused) {
        }
    }

    public void GetChannelValue(int i, byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 57664, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void WriteChannelValue(int i, int i2, byte[] bArr, byte b, byte b2, udp_socket udp_socketVar) {
        try {
            int i3 = 65280 & i2;
            byte[] bArr2 = {(byte) (i3 >> 8), (byte) (i2 - i3)};
            byte[] bArr3 = new byte[43];
            bArr3[0] = (byte) i;
            bArr3[1] = bArr2[0];
            bArr3[2] = bArr2[1];
            for (int i4 = 0; i4 < 40; i4++) {
                bArr3[i4 + 3] = bArr[i4];
            }
            udp_socketVar.SendUDPBuffer(bArr3, (short) bArr3.length, 57666, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void GetMusicState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 90, 49, 83, 84, 65, 84, 85, 83, 63, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

`securitycontrol.java`
```
package com.shima.smartbushome.founction_command;

import android.support.v4.view.MotionEventCompat;
import com.shima.smartbushome.udp.udp_socket;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class securitycontrol {
    public void setAlarm(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte b3 = (byte) i2;
            byte[] bArr = {(byte) i, b3};
            short length = (short) bArr.length;
            for (byte b4 = 1; b4 < 2; b4 = (byte) (b4 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 260, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadAreaName(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 584, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadLogSum(byte b, byte b2, String str, String str2, udp_socket udp_socketVar) {
        try {
            String[] strArrSplit = str.split("-");
            String[] strArrSplit2 = str2.split("-");
            byte[] bArr = {(byte) Integer.parseInt(strArrSplit[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit[1]), (byte) Integer.parseInt(strArrSplit[2]), (byte) Integer.parseInt(strArrSplit2[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit2[1]), (byte) Integer.parseInt(strArrSplit2[2])};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 312, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadLogvalue(byte b, byte b2, String str, String str2, byte[] bArr, int i, udp_socket udp_socketVar) {
        try {
            String[] strArrSplit = str.split("-");
            String[] strArrSplit2 = str2.split("-");
            int i2 = i & MotionEventCompat.ACTION_POINTER_INDEX_MASK;
            byte[] bArr2 = {(byte) Integer.parseInt(strArrSplit[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit[1]), (byte) Integer.parseInt(strArrSplit[2]), (byte) Integer.parseInt(strArrSplit2[0].substring(2, 4)), (byte) Integer.parseInt(strArrSplit2[1]), (byte) Integer.parseInt(strArrSplit2[2]), bArr[0], bArr[1], (byte) (i2 >> 8), (byte) (i - i2)};
            short length = (short) bArr2.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr2, length, 314, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ClearLog(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {-8, 56, 56, 56, 56, 56, 56};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 330, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void WriteAreaName(byte b, byte b2, int i, String str, udp_socket udp_socketVar) throws UnsupportedEncodingException {
        try {
            byte[] bytes = str.getBytes("UnicodeLittleUnmarked");
            byte[] bArr = new byte[bytes.length];
            for (int i2 = 0; i2 < bytes.length; i2++) {
                bArr[i2] = bytes[(bytes.length - 1) - i2];
            }
            byte[] bArr2 = new byte[20];
            if (bArr.length < 20) {
                for (int i3 = 0; i3 < 20; i3++) {
                    if (i3 < bArr.length) {
                        bArr2[i3] = bArr[i3];
                    } else {
                        bArr2[i3] = 32;
                    }
                }
            }
            byte[] bArr3 = new byte[22];
            bArr3[0] = (byte) i;
            bArr3[1] = 1;
            for (int i4 = 0; i4 < 20; i4++) {
                bArr3[i4 + 2] = bArr2[i4];
            }
            short length = (short) bArr3.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr3, length, 586, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void setBypass(byte b, byte b2, int i, int i2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, 0, (byte) i2};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 304, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadBypass(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i, 0};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 302, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadWatchDog(byte b, byte b2, int i, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {(byte) i};
            short length = (short) bArr.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 306, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void WriteWatchDog(byte b, byte b2, int i, int i2, byte[] bArr, udp_socket udp_socketVar) {
        try {
            byte[] bArr2 = {(byte) i, 0, bArr[0], (byte) i2, bArr[1], bArr[2], bArr[3], bArr[4], bArr[5], bArr[6], bArr[7], bArr[8], bArr[9]};
            short length = (short) bArr2.length;
            for (byte b3 = 1; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr2, length, 308, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }
}
```

`statuscontrol.java`
```
package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;

/* loaded from: classes.dex */
public class statuscontrol {
    public void getlightstate(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (byte b3 = 0; b3 < 2; b3 = (byte) (b3 + 1)) {
                udp_socketVar.SendUDPBuffer(bArr, length, 51, b, b2, false);
            }
        } catch (Exception unused) {
        }
    }

    public void ReadTemp(byte b, byte b2, byte b3, udp_socket udp_socketVar) {
        try {
            byte[] bArr = new byte[1];
            switch (b3) {
                case 1:
                    bArr[0] = 1;
                    break;
                case 2:
                    bArr[0] = 0;
                    break;
            }
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 58343, b, b2, false);
        } catch (Exception unused) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

