package voice.encoder;

import com.litesuits.http.data.Charsets;
import java.io.UnsupportedEncodingException;
import voice.Util;
import voice.WiFiInfo;

/* loaded from: classes.dex */
public class DataEncoder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static int MAX_APART_LEN = 32;

    static byte bitGet(byte b, int i, int i2) {
        int i3 = b;
        if (i2 < 8) {
            i3 = b >> (8 - i2);
        }
        return (byte) (i3 & (255 >> (8 - (i2 - i))));
    }

    static byte bitSet(byte b, int i, int i2, byte b2) {
        byte b3 = (byte) ((i > 0 ? 255 >> i : 255) & (i2 < 8 ? 255 << (8 - i2) : 255));
        return (byte) ((b & (b3 ^ (-1))) | (b3 & (b2 << (8 - i2))));
    }

    static int digit2HexCount(byte[] bArr, int i, int i2) {
        return i2;
    }

    private static int hexIdx(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c < 'a' || c > 'f') {
            return 0;
        }
        return c - 'a';
    }

    static int type2HexCount(int i, int i2) {
        return (i == 1 || i == 2 || i == 4) ? 2 : 1;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0082 A[LOOP:2: B:43:0x0080->B:44:0x0082, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String encodeWIFI(byte[] r11, int r12, java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 240
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: voice.encoder.DataEncoder.encodeWIFI(byte[], int, java.lang.String):java.lang.String");
    }

    public static WiFiInfo decodeWiFi(String str) {
        int i;
        int i2;
        int i3;
        char c;
        int i4;
        int i5;
        int i6;
        byte bHexChar2Int;
        int length = str.length();
        byte[] bArr = new byte[8];
        byte[] bArr2 = new byte[50];
        byte bHexChar2Int2 = (byte) Util.hexChar2Int((byte) str.charAt(0));
        byte b = (byte) (bHexChar2Int2 & 8);
        int i7 = ((bHexChar2Int2 & 7) + 1) * 2;
        if (b != 0) {
            i2 = (length - i7) - 1;
            i3 = 0;
            c = 0;
            i4 = -1;
            i = i7 + 1;
            i5 = 1;
        } else {
            i = i7 + 2;
            i2 = (length - i7) - 2;
            byte bHexChar2Int3 = (byte) Util.hexChar2Int((byte) str.charAt(1));
            byte b2 = (byte) (bHexChar2Int3 & 8);
            i3 = (bHexChar2Int3 & 7) + 1;
            if (b2 == 0) {
                c = 3;
                i4 = (i2 - i3) + i;
                i5 = 2;
            } else if (i3 == 8) {
                i4 = i;
                i3 = i2;
                i5 = 2;
                c = 1;
            } else {
                i4 = i;
                i5 = 2;
                c = 2;
            }
        }
        if (i7 % 2 == 0 && (i2 - i3) % 2 == 0) {
            if ((c > 0 ? 1 : 0) + 1 + i7 + i2 == length) {
                int i8 = 0;
                byte bHexChar2Int4 = 0;
                for (int i9 = 0; i9 < i7; i9++) {
                    if (i9 % 2 == 1) {
                        bHexChar2Int4 = (byte) ((bHexChar2Int4 << 4) | (((byte) Util.hexChar2Int((byte) str.charAt(i5 + i9))) & 15));
                        bArr[i8] = bHexChar2Int4;
                        i8++;
                    } else {
                        bHexChar2Int4 = (byte) (((byte) Util.hexChar2Int((byte) str.charAt(i5 + i9))) & 15);
                    }
                }
                if (i8 * 2 != i7) {
                    return null;
                }
                int i10 = 0;
                int i11 = 0;
                int i12 = 0;
                while (true) {
                    if (i10 >= i2) {
                        i6 = i12;
                        break;
                    }
                    int i13 = i + i10;
                    if (i13 >= i4 && i13 < i4 + i3) {
                        i6 = i12 + 1;
                        bArr2[i12] = (byte) (Util.hexChar2Int((byte) str.charAt(i13)) + 48);
                        if (i6 >= 50) {
                            break;
                        }
                        i12 = i6;
                    } else {
                        if (i11 % 2 == 1) {
                            bHexChar2Int = (byte) ((bHexChar2Int4 << 4) + (((byte) Util.hexChar2Int((byte) str.charAt(i13))) & 15));
                            bArr2[i12] = bHexChar2Int;
                            i12++;
                        } else {
                            bHexChar2Int = (byte) (((byte) Util.hexChar2Int((byte) str.charAt(i13))) & 15);
                        }
                        i11++;
                        bHexChar2Int4 = bHexChar2Int;
                    }
                    i10++;
                }
                bArr2[i6] = 0;
                if (i6 * 2 != i2 + i3) {
                    return null;
                }
                WiFiInfo wiFiInfo = new WiFiInfo();
                wiFiInfo.mac = new byte[i8];
                for (int i14 = 0; i14 < i8; i14++) {
                    wiFiInfo.mac[i14] = bArr[i14];
                }
                wiFiInfo.pwd = new String(bArr2, 0, i6);
                return wiFiInfo;
            }
        }
        return null;
    }

    public static String decode(String str) throws NumberFormatException {
        long j = Long.parseLong(str.substring(0, str.length() - 1), 16);
        String strValueOf = String.valueOf(j);
        if (j > 1000000000 && j < 10000000000L) {
            long j2 = j / 1000000000;
            if (j2 == 3 || j2 == 4 || j2 == 5 || j2 == 8) {
                return "1" + strValueOf;
            }
        }
        return strValueOf;
    }

    public static String encode(long j) {
        String hexString = Long.toHexString(j);
        while (hexString.length() < 9) {
            hexString = "0" + hexString;
        }
        return hexString + checkSum(hexString);
    }

    static void bitsSet(byte[] bArr, int i, int i2, byte b) {
        int i3 = i / 8;
        int i4 = i2 / 8;
        if (i3 == i4) {
            bArr[i3] = bitSet(bArr[i3], i % 8, i2 % 8, b);
            return;
        }
        int i5 = i2 % 8;
        byte b2 = (byte) (b >> i5);
        bArr[i3] = bitSet(bArr[i3], i % 8, 8, b2);
        bArr[i4] = bitSet(bArr[i4], 0, i5, (byte) (b & (255 >> (8 - i5))));
    }

    static byte bitsGet(byte[] bArr, int i, int i2) {
        int i3 = i / 8;
        int i4 = i2 / 8;
        if (i3 == i4) {
            return bitGet(bArr[i3], i % 8, i2 % 8);
        }
        int i5 = i2 % 8;
        return (byte) (bitGet(bArr[i4], 0, i5) | (bitGet(bArr[i3], i % 8, 8) << i5));
    }

    static int digit2Hex(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        for (int i4 = 0; i4 < i2; i4++) {
            bArr2[i4 + i3] = bArr[i4 + i];
        }
        return i2;
    }

    static int lower2HexCount(byte[] bArr, int i, int i2) {
        int i3 = i2 * 5;
        return i3 % 4 > 0 ? (i3 / 4) + 1 : i3 / 4;
    }

    static void charsToHex(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        for (int i4 = 0; i4 < i2; i4++) {
            if (i4 % 2 == 0) {
                bArr2[i4 + i3] = (byte) hexChars[(bArr[(i4 / 2) + i] >> 4) & 15];
            } else {
                bArr2[i4 + i3] = (byte) hexChars[bArr[(i4 / 2) + i] & 15];
            }
        }
    }

    static int lower2Hex(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int iLower2HexCount = lower2HexCount(bArr, i, i2);
        byte[] bArr3 = new byte[i2];
        int i4 = 0;
        while (i4 < i2) {
            int i5 = i4 + 1;
            bitsSet(bArr3, i4 * 5, i5 * 5, (byte) (bArr[i4 + i] - 97));
            i4 = i5;
        }
        charsToHex(bArr3, 0, iLower2HexCount, bArr2, i3);
        return iLower2HexCount;
    }

    static int upper2HexCount(byte[] bArr, int i, int i2) {
        return lower2HexCount(bArr, i, i2);
    }

    static int upper2Hex(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int iUpper2HexCount = upper2HexCount(bArr, i, i2);
        byte[] bArr3 = new byte[i2];
        int i4 = 0;
        while (i4 < i2) {
            int i5 = i4 + 1;
            bitsSet(bArr3, i4 * 5, i5 * 5, (byte) (bArr[i4 + i] - 65));
            i4 = i5;
        }
        charsToHex(bArr3, 0, iUpper2HexCount, bArr2, i3);
        return iUpper2HexCount;
    }

    static int char64ToHexCount(byte[] bArr, int i, int i2) {
        int i3 = i2 * 6;
        return i3 % 4 > 0 ? (i3 / 4) + 1 : i3 / 4;
    }

    static int char64ToHex(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int iChar64ToHexCount = char64ToHexCount(bArr, i, i2);
        byte[] bArr3 = new byte[i2];
        int i4 = 0;
        while (i4 < i2) {
            int i5 = i4 + 1;
            bitsSet(bArr3, i4 * 6, i5 * 6, (byte) Util.char64ToInt(bArr[i4 + i]));
            i4 = i5;
        }
        charsToHex(bArr3, 0, iChar64ToHexCount, bArr2, i3);
        return iChar64ToHexCount;
    }

    static int char256ToHexCount(byte[] bArr, int i, int i2) {
        return (i2 * 8) / 4;
    }

    static int char256ToHex(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int iChar256ToHexCount = char256ToHexCount(bArr, i, i2);
        charsToHex(bArr, i, iChar256ToHexCount, bArr2, i3);
        return iChar256ToHexCount;
    }

    static int type2Hex(int i, int i2, byte[] bArr, int i3) {
        int i4 = i3 + 0;
        bArr[i4] = (byte) (i << 1);
        int iType2HexCount = type2HexCount(i, i2);
        if (iType2HexCount > 1) {
            int i5 = i2 - 1;
            bArr[i4] = (byte) (bArr[i4] | ((i5 & 16) >> 4));
            bArr[i3 + 1] = (byte) hexChars[i5 & 15];
        }
        bArr[i4] = (byte) hexChars[bArr[i4]];
        return iType2HexCount;
    }

    static int encodeType(byte[] bArr, int i, int[] iArr) {
        boolean z = true;
        boolean z2 = true;
        boolean z3 = true;
        boolean z4 = false;
        boolean z5 = false;
        int i2 = 0;
        boolean z6 = false;
        int i3 = 0;
        boolean z7 = true;
        int i4 = 0;
        for (int i5 = 0; i5 < i; i5++) {
            boolean zIsDigit = Util.isDigit(bArr[i5]);
            boolean zIsLowerChar = Util.isLowerChar(bArr[i5]);
            boolean zIsUpperChar = Util.isUpperChar(bArr[i5]);
            boolean zIs64Char = Util.is64Char(bArr[i5]);
            if (z && !zIsDigit) {
                if (i5 > 0) {
                    i4 = i5;
                    z4 = zIs64Char;
                }
                z = false;
            }
            if (z4 && !zIs64Char) {
                z4 = false;
            }
            if (z2 && !zIsLowerChar) {
                if (i5 > 0) {
                    i2 = i5;
                    z5 = zIsDigit;
                }
                z2 = false;
            }
            if (z5 && !zIsDigit) {
                z5 = false;
            }
            if (z3 && !zIsUpperChar) {
                z3 = false;
            }
            if (z7 && !zIs64Char) {
                z7 = false;
            }
            if (!z7) {
                z6 = false;
            } else if (i5 > 0) {
                if (!z6 && zIsDigit) {
                    i3 = i5;
                }
                z6 = zIsDigit;
            }
        }
        if (z) {
            return 0;
        }
        if (z2) {
            return 5;
        }
        if (z3) {
            return 7;
        }
        if (z5 && i2 <= MAX_APART_LEN) {
            iArr[0] = i2;
            return 1;
        }
        if (z6 && i3 <= MAX_APART_LEN) {
            iArr[0] = i3;
            return 2;
        }
        if (!z4) {
            return z7 ? 6 : 3;
        }
        if (i4 >= MAX_APART_LEN) {
            i4 = MAX_APART_LEN;
        }
        iArr[0] = i4;
        return 4;
    }

    static int encodeData(byte[] bArr, byte[] bArr2, int i) {
        int[] iArr = {0};
        int iEncodeType = encodeType(bArr, bArr.length, iArr);
        int iType2Hex = type2Hex(iEncodeType, iArr[0], bArr2, i);
        if (iEncodeType == 1) {
            int iLower2Hex = iType2Hex + lower2Hex(bArr, 0, iArr[0], bArr2, iType2Hex + i);
            return iLower2Hex + digit2Hex(bArr, iArr[0], bArr.length - iArr[0], bArr2, i + iLower2Hex);
        }
        if (iEncodeType == 2) {
            int iChar64ToHex = iType2Hex + char64ToHex(bArr, 0, iArr[0], bArr2, iType2Hex + i);
            return iChar64ToHex + digit2Hex(bArr, iArr[0], bArr.length - iArr[0], bArr2, i + iChar64ToHex);
        }
        if (iEncodeType == 4) {
            int iDigit2Hex = iType2Hex + digit2Hex(bArr, 0, iArr[0], bArr2, iType2Hex + i);
            return iDigit2Hex + char64ToHex(bArr, iArr[0], bArr.length - iArr[0], bArr2, i + iDigit2Hex);
        }
        if (iEncodeType == 0) {
            return iType2Hex + digit2Hex(bArr, 0, bArr.length, bArr2, i + iType2Hex);
        }
        if (iEncodeType == 3) {
            return iType2Hex + char256ToHex(bArr, 0, bArr.length, bArr2, i + iType2Hex);
        }
        if (iEncodeType == 5) {
            return iType2Hex + lower2Hex(bArr, 0, bArr.length, bArr2, i + iType2Hex);
        }
        if (iEncodeType == 6) {
            return iType2Hex + char64ToHex(bArr, 0, bArr.length, bArr2, i + iType2Hex);
        }
        return iEncodeType == 7 ? iType2Hex + upper2Hex(bArr, 0, bArr.length, bArr2, i + iType2Hex) : iType2Hex;
    }

    static byte[] String2bytes(String str) throws UnsupportedEncodingException {
        byte[] bytes;
        try {
            bytes = str.getBytes(Charsets.GBK);
        } catch (UnsupportedEncodingException unused) {
            bytes = null;
        }
        return bytes == null ? str.getBytes() : bytes;
    }

    public static String encodeString(String str) throws UnsupportedEncodingException {
        byte[] bArrString2bytes = String2bytes(str);
        byte[] bArr = new byte[(bArrString2bytes.length * 2) + 7];
        bArr[0] = (byte) hexChars[6];
        return new String(bArr, 0, encodeData(bArrString2bytes, bArr, 1) + 1);
    }

    public static String encodeMacWiFi(byte[] bArr, String str) throws UnsupportedEncodingException {
        byte[] bArrString2bytes = String2bytes(str);
        byte[] bArr2 = new byte[((bArr.length + bArrString2bytes.length) * 2) + 7];
        bArr2[0] = (byte) hexChars[(bArr.length - 1) & 15];
        int iChar256ToHex = char256ToHex(bArr, 0, bArr.length, bArr2, 1) + 1;
        return new String(bArr2, 0, iChar256ToHex + encodeData(bArrString2bytes, bArr2, iChar256ToHex));
    }

    public static String encodeSSIDWiFi(String str, String str2) throws UnsupportedEncodingException {
        byte[] bArrString2bytes = String2bytes(str);
        byte[] bArrString2bytes2 = String2bytes(str2);
        byte[] bArr = new byte[((bArrString2bytes.length + bArrString2bytes2.length) * 2) + 7];
        int length = bArrString2bytes.length - 1;
        bArr[0] = (byte) hexChars[((length & 16) >> 4) | 2];
        bArr[1] = (byte) hexChars[length & 15];
        int iEncodeData = encodeData(bArrString2bytes, bArr, 2) + 2;
        return new String(bArr, 0, iEncodeData + encodeData(bArrString2bytes2, bArr, iEncodeData));
    }

    public static String encodePhone(String str, String str2) throws UnsupportedEncodingException {
        byte[] bArrString2bytes = String2bytes(str2);
        byte[] bArrString2bytes2 = String2bytes(str);
        byte[] bArr = new byte[((bArrString2bytes2.length + bArrString2bytes.length) * 2) + 7];
        int length = bArrString2bytes.length - 1;
        bArr[0] = (byte) hexChars[((length & 16) >> 4) | 4];
        bArr[1] = (byte) hexChars[length & 15];
        int iEncodeData = encodeData(bArrString2bytes2, bArr, 2) + 2;
        return new String(bArr, 0, iEncodeData + encodeData(bArrString2bytes, bArr, iEncodeData));
    }

    private static char checkSum(String str) {
        int length = str.length();
        int iHexIdx = 0;
        for (int i = 0; i < length; i++) {
            iHexIdx ^= hexIdx(str.charAt(i));
        }
        return hexChars[((-iHexIdx) & 15) % 16];
    }

    public static boolean isCheckSum(String str) {
        return str.charAt(str.length() - 1) == checkSum(str.substring(0, str.length() - 1));
    }

    public static void main(String[] strArr) throws UnsupportedEncodingException {
        System.out.println(encodeString("thisisatest"));
        System.out.println(encodeString("this is a test"));
        System.out.println(encodeString("1234567890中文"));
        System.out.println(encodeString("1234567890"));
    }
}
