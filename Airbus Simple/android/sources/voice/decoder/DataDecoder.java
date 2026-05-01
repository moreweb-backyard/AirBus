package voice.decoder;

import com.litesuits.http.data.Charsets;
import java.io.UnsupportedEncodingException;
import voice.PhoneInfo;
import voice.SSIDWiFiInfo;
import voice.Util;
import voice.WiFiInfo;

/* loaded from: classes.dex */
public class DataDecoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    static byte bitGet(byte b, int i, int i2) {
        int i3 = b;
        if (i2 < 8) {
            i3 = b >> (8 - i2);
        }
        return (byte) (i3 & (255 >> (8 - (i2 - i))));
    }

    static byte bitsGet(byte[] bArr, int i, int i2, int i3) {
        int i4 = i2 / 8;
        int i5 = i3 / 8;
        if (i4 == i5) {
            return bitGet(bArr[i + i4], i2 % 8, i3 % 8);
        }
        int i6 = i3 % 8;
        return (byte) (bitGet(bArr[i + i5], 0, i6) | (bitGet(bArr[i4 + i], i2 % 8, 8) << i6));
    }

    public static int hex2Digit(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int i3, int i4) {
        int i5 = 0;
        while (i5 < i2 && (i4 < 0 || i5 < i4)) {
            bArr2[i3 + i5] = bArr[i + i5];
            i5++;
        }
        iArr[0] = i5;
        return i5;
    }

    public static int hex2Chars(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int i3, int i4) {
        int i5 = i4 < 0 ? i2 : (i4 + 3) / 4;
        if (i5 <= i2) {
            i2 = i5;
        }
        int i6 = 0;
        while (i6 < i2 / 2) {
            int i7 = (i6 * 2) + i;
            bArr2[i3 + i6] = (byte) (Util.hexChar2Int(bArr[i7 + 1]) | (Util.hexChar2Int(bArr[i7]) << 4));
            i6++;
        }
        int i8 = i6 * 2;
        iArr[0] = i8;
        if (i2 % 2 <= 0) {
            return i6;
        }
        iArr[0] = i8 + 1;
        bArr2[i3 + i6] = (byte) (Util.hexChar2Int(bArr[i + i8]) << 4);
        return i6 + 1;
    }

    public static int hex2Lower(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int i3, int i4) {
        byte[] bArr3 = new byte[(i2 / 2) + 1];
        hex2Chars(bArr, i, i2, iArr, bArr3, 0, i4 > 0 ? i4 * 5 : i4);
        int i5 = 0;
        while (true) {
            if (i4 >= 0 && i5 >= i4) {
                break;
            }
            int i6 = i5 + 1;
            int i7 = i6 * 5;
            if (i7 > i2 * 4) {
                break;
            }
            bArr2[i3 + i5] = (byte) (bitsGet(bArr3, 0, i5 * 5, i7) + 97);
            iArr[0] = (i7 + 3) / 4;
            i5 = i6;
        }
        return i5;
    }

    public static int hex2Upper(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int i3, int i4) {
        byte[] bArr3 = new byte[(i2 / 2) + 1];
        hex2Chars(bArr, i, i2, iArr, bArr3, 0, i4 > 0 ? i4 * 5 : i4);
        int i5 = 0;
        while (true) {
            if (i4 >= 0 && i5 >= i4) {
                break;
            }
            int i6 = i5 + 1;
            int i7 = i6 * 5;
            if (i7 > i2 * 4) {
                break;
            }
            bArr2[i3 + i5] = (byte) (bitsGet(bArr3, 0, i5 * 5, i7) + 65);
            iArr[0] = (i7 + 3) / 4;
            i5 = i6;
        }
        return i5;
    }

    static int hex2Char64(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int i3, int i4) {
        byte[] bArr3 = new byte[(i2 / 2) + 1];
        hex2Chars(bArr, i, i2, iArr, bArr3, 0, i4 > 0 ? i4 * 6 : i4);
        int i5 = 0;
        while (true) {
            if (i4 >= 0 && i5 >= i4) {
                break;
            }
            int i6 = i5 + 1;
            int i7 = i6 * 6;
            if (i7 > i2 * 4) {
                break;
            }
            bArr2[i3 + i5] = Util.int2Char64(bitsGet(bArr3, 0, i5 * 6, i7));
            iArr[0] = (i7 + 3) / 4;
            i5 = i6;
        }
        return i5;
    }

    static int hex2Char256(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int i3, int i4) {
        if (i4 > 0) {
            i4 *= 8;
        }
        return hex2Chars(bArr, i, i2, iArr, bArr2, i3, i4);
    }

    public static int hex2Type(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int[] iArr2) {
        int i3 = i + 0;
        bArr2[0] = (byte) (Util.hexChar2Int(bArr[i3]) >> 1);
        iArr[0] = 1;
        if (bArr2[0] != 1 && bArr2[0] != 2 && bArr2[0] != 4) {
            return 1;
        }
        iArr2[0] = Util.hexChar2Int(bArr[i + 1]) | ((Util.hexChar2Int(bArr[i3]) & 1) << 4);
        iArr2[0] = iArr2[0] + 1;
        iArr[0] = 2;
        return 2;
    }

    public static int decodeData(byte[] bArr, int i, int i2, int[] iArr, byte[] bArr2, int i3, int i4) {
        int[] iArr2 = {i4};
        int[] iArr3 = {0};
        byte[] bArr3 = {0};
        iArr[0] = 0;
        hex2Type(bArr, i, i2, iArr, bArr3, iArr2);
        if (bArr3[0] == 1) {
            int iHex2Lower = hex2Lower(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, iArr2[0]) + 0;
            iArr[0] = iArr[0] + iArr3[0];
            int iHex2Digit = hex2Digit(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3 + iHex2Lower, i4 < 0 ? i4 : i4 - iHex2Lower) + iHex2Lower;
            iArr[0] = iArr[0] + iArr3[0];
            return iHex2Digit;
        }
        if (bArr3[0] == 2) {
            int iHex2Char64 = hex2Char64(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, iArr2[0]) + 0;
            iArr[0] = iArr[0] + iArr3[0];
            int iHex2Digit2 = hex2Digit(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3 + iHex2Char64, i4 < 0 ? i4 : i4 - iHex2Char64) + iHex2Char64;
            iArr[0] = iArr[0] + iArr3[0];
            return iHex2Digit2;
        }
        if (bArr3[0] == 4) {
            int iHex2Digit3 = hex2Digit(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, iArr2[0]) + 0;
            iArr[0] = iArr[0] + iArr3[0];
            int iHex2Char642 = hex2Char64(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3 + iHex2Digit3, i4 < 0 ? i4 : i4 - iHex2Digit3) + iHex2Digit3;
            iArr[0] = iArr[0] + iArr3[0];
            return iHex2Char642;
        }
        if (bArr3[0] == 0) {
            int iHex2Digit4 = hex2Digit(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, i4) + 0;
            iArr[0] = iArr[0] + iArr3[0];
            return iHex2Digit4;
        }
        if (bArr3[0] == 3) {
            int iHex2Char256 = hex2Char256(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, i4) + 0;
            iArr[0] = iArr[0] + iArr3[0];
            return iHex2Char256;
        }
        if (bArr3[0] == 5) {
            int iHex2Lower2 = hex2Lower(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, i4) + 0;
            iArr[0] = iArr[0] + iArr3[0];
            return iHex2Lower2;
        }
        if (bArr3[0] == 6) {
            int iHex2Char643 = hex2Char64(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, i4) + 0;
            iArr[0] = iArr[0] + iArr3[0];
            return iHex2Char643;
        }
        if (bArr3[0] != 7) {
            return 0;
        }
        int iHex2Upper = hex2Upper(bArr, i + iArr[0], i2 - iArr[0], iArr3, bArr2, i3, i4) + 0;
        iArr[0] = iArr[0] + iArr3[0];
        return iHex2Upper;
    }

    public static int decodeInfoType(byte[] bArr, int i) {
        return Util.hexChar2Int(bArr[0]) >> 1;
    }

    static String bytes2String(byte[] bArr, int i, int i2) {
        String str;
        try {
            str = new String(bArr, i, i2, Charsets.GBK);
        } catch (UnsupportedEncodingException unused) {
            str = null;
        }
        return str == null ? new String(bArr, i, i2) : str;
    }

    public static String decodeString(int i, byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        return bytes2String(bArr2, 0, decodeData(bArr, 0, bArr.length, new int[]{0}, bArr2, 0, -1));
    }

    public static WiFiInfo decodeWiFi(int i, byte[] bArr) {
        int iHexChar2Int = (((Util.hexChar2Int(bArr[0]) & 1) << 4) | Util.hexChar2Int(bArr[1])) + 1;
        int[] iArr = {2};
        int[] iArr2 = {0};
        WiFiInfo wiFiInfo = new WiFiInfo();
        wiFiInfo.mac = new byte[iHexChar2Int];
        byte[] bArr2 = new byte[80];
        decodeData(bArr, iArr[0], bArr.length - iArr[0], iArr2, wiFiInfo.mac, 0, iHexChar2Int);
        iArr[0] = iArr[0] + iArr2[0];
        wiFiInfo.pwd = bytes2String(bArr2, 0, decodeData(bArr, iArr[0], bArr.length - iArr[0], iArr2, bArr2, 0, -1));
        return wiFiInfo;
    }

    public static SSIDWiFiInfo decodeSSIDWiFi(int i, byte[] bArr) {
        int[] iArr = {2};
        int[] iArr2 = {0};
        byte[] bArr2 = new byte[80];
        byte[] bArr3 = new byte[80];
        int iDecodeData = decodeData(bArr, iArr[0], bArr.length - iArr[0], iArr2, bArr2, 0, (((Util.hexChar2Int(bArr[0]) & 1) << 4) | Util.hexChar2Int(bArr[1])) + 1);
        SSIDWiFiInfo sSIDWiFiInfo = new SSIDWiFiInfo();
        sSIDWiFiInfo.ssid = bytes2String(bArr2, 0, iDecodeData);
        iArr[0] = iArr[0] + iArr2[0];
        sSIDWiFiInfo.pwd = bytes2String(bArr3, 0, decodeData(bArr, iArr[0], bArr.length - iArr[0], iArr2, bArr3, 0, -1));
        return sSIDWiFiInfo;
    }

    public static PhoneInfo decodePhone(int i, byte[] bArr) {
        int[] iArr = {2};
        int[] iArr2 = {0};
        byte[] bArr2 = new byte[80];
        byte[] bArr3 = new byte[80];
        int iDecodeData = decodeData(bArr, iArr[0], bArr.length - iArr[0], iArr2, bArr2, 0, (((Util.hexChar2Int(bArr[0]) & 1) << 4) | Util.hexChar2Int(bArr[1])) + 1);
        PhoneInfo phoneInfo = new PhoneInfo();
        phoneInfo.imei = bytes2String(bArr2, 0, iDecodeData);
        iArr[0] = iArr[0] + iArr2[0];
        phoneInfo.phoneName = bytes2String(bArr3, 0, decodeData(bArr, iArr[0], bArr.length - iArr[0], iArr2, bArr3, 0, -1));
        return phoneInfo;
    }

    public static void main(String[] strArr) {
        System.out.println(decodeString(0, "a99d124481324a6".getBytes()));
        System.out.println(decodeString(0, "67468697320697320612074657374".getBytes()));
        System.out.println(decodeString(0, "631323334353637383930d6d0cec4".getBytes()));
        System.out.println(decodeString(0, "89012345678982082081f7dd771c7182081f7df7df7df7df7f2820820820820820820bc".getBytes()));
    }
}
