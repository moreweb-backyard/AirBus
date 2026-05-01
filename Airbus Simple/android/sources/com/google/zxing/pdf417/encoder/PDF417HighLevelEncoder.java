package com.google.zxing.pdf417.encoder;

import com.google.zxing.WriterException;
import com.google.zxing.common.CharacterSetECI;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
final class PDF417HighLevelEncoder {
    private static final int BYTE_COMPACTION = 1;
    private static final int ECI_CHARSET = 927;
    private static final int ECI_GENERAL_PURPOSE = 926;
    private static final int ECI_USER_DEFINED = 925;
    private static final int LATCH_TO_BYTE = 924;
    private static final int LATCH_TO_BYTE_PADDED = 901;
    private static final int LATCH_TO_NUMERIC = 902;
    private static final int LATCH_TO_TEXT = 900;
    private static final int NUMERIC_COMPACTION = 2;
    private static final int SHIFT_TO_BYTE = 913;
    private static final int SUBMODE_ALPHA = 0;
    private static final int SUBMODE_LOWER = 1;
    private static final int SUBMODE_MIXED = 2;
    private static final int SUBMODE_PUNCTUATION = 3;
    private static final int TEXT_COMPACTION = 0;
    private static final byte[] TEXT_MIXED_RAW = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 38, MultipartStream.CR, 9, 44, 58, 35, MultipartStream.DASH, 46, 36, 47, 43, 37, 42, 61, 94, 0, 32, 0, 0, 0};
    private static final byte[] TEXT_PUNCTUATION_RAW = {59, 60, 62, 64, 91, 92, 93, 95, 96, 126, 33, MultipartStream.CR, 9, 44, 58, 10, MultipartStream.DASH, 46, 36, 47, 34, 124, 42, 40, 41, 63, 123, 125, 39, 0};
    private static final byte[] MIXED = new byte[128];
    private static final byte[] PUNCTUATION = new byte[128];
    static final Charset DEFAULT_ENCODING = Charset.forName("Cp437");

    private static boolean isAlphaLower(char c) {
        return c == ' ' || (c >= 'a' && c <= 'z');
    }

    private static boolean isAlphaUpper(char c) {
        return c == ' ' || (c >= 'A' && c <= 'Z');
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isText(char c) {
        return c == '\t' || c == '\n' || c == '\r' || (c >= ' ' && c <= '~');
    }

    static {
        Arrays.fill(MIXED, (byte) -1);
        for (byte b = 0; b < TEXT_MIXED_RAW.length; b = (byte) (b + 1)) {
            byte b2 = TEXT_MIXED_RAW[b];
            if (b2 > 0) {
                MIXED[b2] = b;
            }
        }
        Arrays.fill(PUNCTUATION, (byte) -1);
        for (byte b3 = 0; b3 < TEXT_PUNCTUATION_RAW.length; b3 = (byte) (b3 + 1)) {
            byte b4 = TEXT_PUNCTUATION_RAW[b3];
            if (b4 > 0) {
                PUNCTUATION[b4] = b3;
            }
        }
    }

    private PDF417HighLevelEncoder() {
    }

    static String encodeHighLevel(String str, Compaction compaction, Charset charset) throws WriterException {
        int iDetermineConsecutiveDigitCount;
        CharacterSetECI characterSetECIByName;
        StringBuilder sb = new StringBuilder(str.length());
        if (!DEFAULT_ENCODING.equals(charset) && (characterSetECIByName = CharacterSetECI.getCharacterSetECIByName(charset.name())) != null) {
            encodingECI(characterSetECIByName.getValue(), sb);
        }
        int length = str.length();
        if (compaction == Compaction.TEXT) {
            encodeText(str, 0, length, sb, 0);
        } else if (compaction == Compaction.BYTE) {
            byte[] bytes = str.getBytes(charset);
            encodeBinary(bytes, 0, bytes.length, 1, sb);
        } else if (compaction == Compaction.NUMERIC) {
            sb.append((char) 902);
            encodeNumeric(str, 0, length, sb);
        } else {
            byte[] bytes2 = null;
            int i = 0;
            int i2 = 0;
            loop0: while (true) {
                int iEncodeText = 0;
                while (i < length) {
                    iDetermineConsecutiveDigitCount = determineConsecutiveDigitCount(str, i);
                    if (iDetermineConsecutiveDigitCount >= 13) {
                        break;
                    }
                    int iDetermineConsecutiveTextCount = determineConsecutiveTextCount(str, i);
                    if (iDetermineConsecutiveTextCount >= 5 || iDetermineConsecutiveDigitCount == length) {
                        if (i2 != 0) {
                            sb.append((char) 900);
                            i2 = 0;
                            iEncodeText = 0;
                        }
                        iEncodeText = encodeText(str, i, iDetermineConsecutiveTextCount, sb, iEncodeText);
                        i += iDetermineConsecutiveTextCount;
                    } else {
                        if (bytes2 == null) {
                            bytes2 = str.getBytes(charset);
                        }
                        int iDetermineConsecutiveBinaryCount = determineConsecutiveBinaryCount(str, bytes2, i);
                        if (iDetermineConsecutiveBinaryCount == 0) {
                            iDetermineConsecutiveBinaryCount = 1;
                        }
                        if (iDetermineConsecutiveBinaryCount == 1 && i2 == 0) {
                            encodeBinary(bytes2, i, 1, 0, sb);
                        } else {
                            encodeBinary(bytes2, i, iDetermineConsecutiveBinaryCount, i2, sb);
                            i2 = 1;
                            iEncodeText = 0;
                        }
                        i += iDetermineConsecutiveBinaryCount;
                    }
                }
                sb.append((char) 902);
                i2 = 2;
                encodeNumeric(str, i, iDetermineConsecutiveDigitCount, sb);
                i += iDetermineConsecutiveDigitCount;
            }
        }
        return sb.toString();
    }

    private static int encodeText(CharSequence charSequence, int i, int i2, StringBuilder sb, int i3) {
        StringBuilder sb2 = new StringBuilder(i2);
        int i4 = i3;
        int i5 = 0;
        while (true) {
            int i6 = i + i5;
            char cCharAt = charSequence.charAt(i6);
            switch (i4) {
                case 0:
                    if (isAlphaUpper(cCharAt)) {
                        if (cCharAt == ' ') {
                            sb2.append((char) 26);
                            break;
                        } else {
                            sb2.append((char) (cCharAt - 'A'));
                            break;
                        }
                    } else if (isAlphaLower(cCharAt)) {
                        sb2.append((char) 27);
                        i4 = 1;
                    } else if (isMixed(cCharAt)) {
                        sb2.append((char) 28);
                        i4 = 2;
                    } else {
                        sb2.append((char) 29);
                        sb2.append((char) PUNCTUATION[cCharAt]);
                        break;
                    }
                case 1:
                    if (isAlphaLower(cCharAt)) {
                        if (cCharAt == ' ') {
                            sb2.append((char) 26);
                            break;
                        } else {
                            sb2.append((char) (cCharAt - 'a'));
                            break;
                        }
                    } else if (isAlphaUpper(cCharAt)) {
                        sb2.append((char) 27);
                        sb2.append((char) (cCharAt - 'A'));
                        break;
                    } else if (isMixed(cCharAt)) {
                        sb2.append((char) 28);
                        i4 = 2;
                    } else {
                        sb2.append((char) 29);
                        sb2.append((char) PUNCTUATION[cCharAt]);
                        break;
                    }
                case 2:
                    if (isMixed(cCharAt)) {
                        sb2.append((char) MIXED[cCharAt]);
                        break;
                    } else if (isAlphaUpper(cCharAt)) {
                        sb2.append((char) 28);
                        i4 = 0;
                    } else if (isAlphaLower(cCharAt)) {
                        sb2.append((char) 27);
                        i4 = 1;
                    } else {
                        int i7 = i6 + 1;
                        if (i7 < i2 && isPunctuation(charSequence.charAt(i7))) {
                            i4 = 3;
                            sb2.append((char) 25);
                        } else {
                            sb2.append((char) 29);
                            sb2.append((char) PUNCTUATION[cCharAt]);
                            break;
                        }
                    }
                    break;
                default:
                    if (isPunctuation(cCharAt)) {
                        sb2.append((char) PUNCTUATION[cCharAt]);
                        break;
                    } else {
                        sb2.append((char) 29);
                        i4 = 0;
                    }
            }
            i5++;
            if (i5 >= i2) {
                int length = sb2.length();
                char cCharAt2 = 0;
                for (int i8 = 0; i8 < length; i8++) {
                    if (i8 % 2 != 0) {
                        cCharAt2 = (char) ((cCharAt2 * 30) + sb2.charAt(i8));
                        sb.append(cCharAt2);
                    } else {
                        cCharAt2 = sb2.charAt(i8);
                    }
                }
                if (length % 2 != 0) {
                    sb.append((char) ((cCharAt2 * 30) + 29));
                }
                return i4;
            }
        }
    }

    private static void encodeBinary(byte[] bArr, int i, int i2, int i3, StringBuilder sb) {
        int i4;
        if (i2 == 1 && i3 == 0) {
            sb.append((char) 913);
        } else if (i2 % 6 == 0) {
            sb.append((char) 924);
        } else {
            sb.append((char) 901);
        }
        int i5 = 6;
        if (i2 >= 6) {
            int i6 = 5;
            char[] cArr = new char[5];
            i4 = i;
            while ((i + i2) - i4 >= i5) {
                long j = 0;
                for (int i7 = 0; i7 < i5; i7++) {
                    j = (j << 8) + (bArr[i4 + i7] & 255);
                }
                int i8 = 0;
                while (i8 < i6) {
                    cArr[i8] = (char) (j % 900);
                    j /= 900;
                    i8++;
                    i6 = 5;
                }
                for (int length = cArr.length - 1; length >= 0; length--) {
                    sb.append(cArr[length]);
                }
                i4 += 6;
                i5 = 6;
                i6 = 5;
            }
        } else {
            i4 = i;
        }
        while (i4 < i + i2) {
            sb.append((char) (bArr[i4] & 255));
            i4++;
        }
    }

    private static void encodeNumeric(String str, int i, int i2, StringBuilder sb) {
        StringBuilder sb2 = new StringBuilder((i2 / 3) + 1);
        BigInteger bigIntegerValueOf = BigInteger.valueOf(900L);
        BigInteger bigIntegerValueOf2 = BigInteger.valueOf(0L);
        int i3 = 0;
        while (i3 < i2 - 1) {
            sb2.setLength(0);
            int iMin = Math.min(44, i2 - i3);
            StringBuilder sb3 = new StringBuilder();
            sb3.append('1');
            int i4 = i + i3;
            sb3.append(str.substring(i4, i4 + iMin));
            BigInteger bigInteger = new BigInteger(sb3.toString());
            do {
                sb2.append((char) bigInteger.mod(bigIntegerValueOf).intValue());
                bigInteger = bigInteger.divide(bigIntegerValueOf);
            } while (!bigInteger.equals(bigIntegerValueOf2));
            for (int length = sb2.length() - 1; length >= 0; length--) {
                sb.append(sb2.charAt(length));
            }
            i3 += iMin;
        }
    }

    private static boolean isMixed(char c) {
        return MIXED[c] != -1;
    }

    private static boolean isPunctuation(char c) {
        return PUNCTUATION[c] != -1;
    }

    private static int determineConsecutiveDigitCount(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        if (i < length) {
            char cCharAt = charSequence.charAt(i);
            while (isDigit(cCharAt) && i < length) {
                i2++;
                i++;
                if (i < length) {
                    cCharAt = charSequence.charAt(i);
                }
            }
        }
        return i2;
    }

    private static int determineConsecutiveTextCount(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = i;
        while (i2 < length) {
            char cCharAt = charSequence.charAt(i2);
            int i3 = 0;
            while (i3 < 13 && isDigit(cCharAt) && i2 < length) {
                i3++;
                i2++;
                if (i2 < length) {
                    cCharAt = charSequence.charAt(i2);
                }
            }
            if (i3 >= 13) {
                return (i2 - i) - i3;
            }
            if (i3 <= 0) {
                if (!isText(charSequence.charAt(i2))) {
                    break;
                }
                i2++;
            }
        }
        return i2 - i;
    }

    private static int determineConsecutiveBinaryCount(CharSequence charSequence, byte[] bArr, int i) throws WriterException {
        int i2;
        int i3;
        int length = charSequence.length();
        int i4 = i;
        while (i4 < length) {
            int i5 = 0;
            char cCharAt = charSequence.charAt(i4);
            int i6 = 0;
            while (i6 < 13 && isDigit(cCharAt) && (i3 = i4 + (i6 = i6 + 1)) < length) {
                cCharAt = charSequence.charAt(i3);
            }
            if (i6 >= 13) {
                return i4 - i;
            }
            while (i5 < 5 && isText(cCharAt) && (i2 = i4 + (i5 = i5 + 1)) < length) {
                cCharAt = charSequence.charAt(i2);
            }
            if (i5 >= 5) {
                return i4 - i;
            }
            char cCharAt2 = charSequence.charAt(i4);
            if (bArr[i4] == 63 && cCharAt2 != '?') {
                throw new WriterException("Non-encodable character detected: " + cCharAt2 + " (Unicode: " + ((int) cCharAt2) + ')');
            }
            i4++;
        }
        return i4 - i;
    }

    private static void encodingECI(int i, StringBuilder sb) throws WriterException {
        if (i >= 0 && i < LATCH_TO_TEXT) {
            sb.append((char) 927);
            sb.append((char) i);
            return;
        }
        if (i < 810900) {
            sb.append((char) 926);
            sb.append((char) ((i / LATCH_TO_TEXT) - 1));
            sb.append((char) (i % LATCH_TO_TEXT));
        } else if (i < 811800) {
            sb.append((char) 925);
            sb.append((char) (810900 - i));
        } else {
            throw new WriterException("ECI number not in valid range from 0..811799, but was " + i);
        }
    }
}
