package com.google.zxing.pdf417.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import cz.msebera.android.httpclient.message.TokenParser;
import java.math.BigInteger;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
final class DecodedBitStreamParser {
    private static final int AL = 28;
    private static final int AS = 27;
    private static final int BEGIN_MACRO_PDF417_CONTROL_BLOCK = 928;
    private static final int BEGIN_MACRO_PDF417_OPTIONAL_FIELD = 923;
    private static final int BYTE_COMPACTION_MODE_LATCH = 901;
    private static final int BYTE_COMPACTION_MODE_LATCH_6 = 924;
    private static final int LL = 27;
    private static final int MACRO_PDF417_TERMINATOR = 922;
    private static final int MAX_NUMERIC_CODEWORDS = 15;
    private static final int ML = 28;
    private static final int MODE_SHIFT_TO_BYTE_COMPACTION_MODE = 913;
    private static final int NUMBER_OF_SEQUENCE_CODEWORDS = 2;
    private static final int NUMERIC_COMPACTION_MODE_LATCH = 902;
    private static final int PAL = 29;
    private static final int PL = 25;
    private static final int PS = 29;
    private static final int TEXT_COMPACTION_MODE_LATCH = 900;
    private static final char[] PUNCT_CHARS = {';', '<', '>', '@', '[', '\\', '}', '_', '`', '~', '!', TokenParser.CR, '\t', ',', ':', '\n', '-', FilenameUtils.EXTENSION_SEPARATOR, '$', IOUtils.DIR_SEPARATOR_UNIX, TokenParser.DQUOTE, '|', '*', '(', ')', '?', '{', '}', '\''};
    private static final char[] MIXED_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '&', TokenParser.CR, '\t', ',', ':', '#', '-', FilenameUtils.EXTENSION_SEPARATOR, '$', IOUtils.DIR_SEPARATOR_UNIX, '+', '%', '*', '=', '^'};
    private static final BigInteger[] EXP900 = new BigInteger[16];

    private enum Mode {
        ALPHA,
        LOWER,
        MIXED,
        PUNCT,
        ALPHA_SHIFT,
        PUNCT_SHIFT
    }

    static {
        EXP900[0] = BigInteger.ONE;
        BigInteger bigIntegerValueOf = BigInteger.valueOf(900L);
        EXP900[1] = bigIntegerValueOf;
        for (int i = 2; i < EXP900.length; i++) {
            EXP900[i] = EXP900[i - 1].multiply(bigIntegerValueOf);
        }
    }

    private DecodedBitStreamParser() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static com.google.zxing.common.DecoderResult decode(int[] r5, java.lang.String r6) throws com.google.zxing.FormatException {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            int r1 = r5.length
            r2 = 2
            int r1 = r1 * 2
            r0.<init>(r1)
            r1 = 1
            r1 = r5[r1]
            com.google.zxing.pdf417.PDF417ResultMetadata r3 = new com.google.zxing.pdf417.PDF417ResultMetadata
            r3.<init>()
        L11:
            r4 = 0
            r4 = r5[r4]
            if (r2 >= r4) goto L50
            r4 = 913(0x391, float:1.28E-42)
            if (r1 == r4) goto L3f
            r4 = 928(0x3a0, float:1.3E-42)
            if (r1 == r4) goto L3a
            switch(r1) {
                case 900: goto L35;
                case 901: goto L3f;
                case 902: goto L30;
                default: goto L21;
            }
        L21:
            switch(r1) {
                case 922: goto L2b;
                case 923: goto L2b;
                case 924: goto L3f;
                default: goto L24;
            }
        L24:
            int r2 = r2 + (-1)
            int r1 = textCompaction(r5, r2, r0)
            goto L43
        L2b:
            com.google.zxing.FormatException r5 = com.google.zxing.FormatException.getFormatInstance()
            throw r5
        L30:
            int r1 = numericCompaction(r5, r2, r0)
            goto L43
        L35:
            int r1 = textCompaction(r5, r2, r0)
            goto L43
        L3a:
            int r1 = decodeMacroBlock(r5, r2, r3)
            goto L43
        L3f:
            int r1 = byteCompaction(r1, r5, r2, r0)
        L43:
            int r2 = r5.length
            if (r1 >= r2) goto L4b
            int r2 = r1 + 1
            r1 = r5[r1]
            goto L11
        L4b:
            com.google.zxing.FormatException r5 = com.google.zxing.FormatException.getFormatInstance()
            throw r5
        L50:
            int r5 = r0.length()
            if (r5 == 0) goto L64
            com.google.zxing.common.DecoderResult r5 = new com.google.zxing.common.DecoderResult
            java.lang.String r0 = r0.toString()
            r1 = 0
            r5.<init>(r1, r0, r1, r6)
            r5.setOther(r3)
            return r5
        L64:
            com.google.zxing.FormatException r5 = com.google.zxing.FormatException.getFormatInstance()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DecodedBitStreamParser.decode(int[], java.lang.String):com.google.zxing.common.DecoderResult");
    }

    private static int decodeMacroBlock(int[] iArr, int i, PDF417ResultMetadata pDF417ResultMetadata) throws FormatException {
        if (i + 2 > iArr[0]) {
            throw FormatException.getFormatInstance();
        }
        int[] iArr2 = new int[2];
        int i2 = i;
        int i3 = 0;
        while (i3 < 2) {
            iArr2[i3] = iArr[i2];
            i3++;
            i2++;
        }
        pDF417ResultMetadata.setSegmentIndex(Integer.parseInt(decodeBase900toBase10(iArr2, 2)));
        StringBuilder sb = new StringBuilder();
        int iTextCompaction = textCompaction(iArr, i2, sb);
        pDF417ResultMetadata.setFileId(sb.toString());
        if (iArr[iTextCompaction] == BEGIN_MACRO_PDF417_OPTIONAL_FIELD) {
            int i4 = iTextCompaction + 1;
            int[] iArr3 = new int[iArr[0] - i4];
            boolean z = false;
            int i5 = 0;
            while (i4 < iArr[0] && !z) {
                int i6 = i4 + 1;
                int i7 = iArr[i4];
                if (i7 < TEXT_COMPACTION_MODE_LATCH) {
                    iArr3[i5] = i7;
                    i4 = i6;
                    i5++;
                } else if (i7 == MACRO_PDF417_TERMINATOR) {
                    pDF417ResultMetadata.setLastSegment(true);
                    i4 = i6 + 1;
                    z = true;
                } else {
                    throw FormatException.getFormatInstance();
                }
            }
            pDF417ResultMetadata.setOptionalData(Arrays.copyOf(iArr3, i5));
            return i4;
        }
        if (iArr[iTextCompaction] != MACRO_PDF417_TERMINATOR) {
            return iTextCompaction;
        }
        pDF417ResultMetadata.setLastSegment(true);
        return iTextCompaction + 1;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:14:0x0034. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x0037. Please report as an issue. */
    private static int textCompaction(int[] iArr, int i, StringBuilder sb) {
        int[] iArr2 = new int[(iArr[0] - i) << 1];
        int[] iArr3 = new int[(iArr[0] - i) << 1];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i4 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i2] = i4 / 30;
                iArr2[i2 + 1] = i4 % 30;
                i2 += 2;
            } else if (i4 != MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                if (i4 != 928) {
                    switch (i4) {
                        case TEXT_COMPACTION_MODE_LATCH /* 900 */:
                            iArr2[i2] = TEXT_COMPACTION_MODE_LATCH;
                            i2++;
                            break;
                        case BYTE_COMPACTION_MODE_LATCH /* 901 */:
                        case NUMERIC_COMPACTION_MODE_LATCH /* 902 */:
                            break;
                        default:
                            switch (i4) {
                            }
                    }
                }
                i = i3 - 1;
                z = true;
            } else {
                iArr2[i2] = MODE_SHIFT_TO_BYTE_COMPACTION_MODE;
                i = i3 + 1;
                iArr3[i2] = iArr[i3];
                i2++;
            }
            i = i3;
        }
        decodeTextCompaction(iArr2, iArr3, i2, sb);
        return i;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static void decodeTextCompaction(int[] iArr, int[] iArr2, int i, StringBuilder sb) {
        char c;
        Mode mode;
        Mode mode2 = Mode.ALPHA;
        Mode mode3 = Mode.ALPHA;
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = iArr[i2];
            switch (mode2) {
                case ALPHA:
                    if (i3 < 26) {
                        c = (char) (i3 + 65);
                        break;
                    } else {
                        if (i3 != 26) {
                            if (i3 == 27) {
                                mode = Mode.LOWER;
                            } else if (i3 == 28) {
                                mode = Mode.MIXED;
                            } else if (i3 != 29) {
                                if (i3 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                    sb.append((char) iArr2[i2]);
                                } else if (i3 == TEXT_COMPACTION_MODE_LATCH) {
                                    mode = Mode.ALPHA;
                                }
                                c = 0;
                                break;
                            } else {
                                mode = Mode.PUNCT_SHIFT;
                                mode3 = mode2;
                            }
                            mode2 = mode;
                            c = 0;
                        }
                        c = TokenParser.SP;
                        break;
                    }
                case LOWER:
                    if (i3 < 26) {
                        c = (char) (i3 + 97);
                        break;
                    } else {
                        if (i3 != 26) {
                            if (i3 == 27) {
                                mode = Mode.ALPHA_SHIFT;
                            } else {
                                if (i3 == 28) {
                                    mode = Mode.MIXED;
                                } else if (i3 != 29) {
                                    if (i3 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                        sb.append((char) iArr2[i2]);
                                    } else if (i3 == TEXT_COMPACTION_MODE_LATCH) {
                                        mode = Mode.ALPHA;
                                    }
                                    c = 0;
                                    break;
                                } else {
                                    mode = Mode.PUNCT_SHIFT;
                                }
                                mode2 = mode;
                                c = 0;
                            }
                            mode3 = mode2;
                            mode2 = mode;
                            c = 0;
                        }
                        c = TokenParser.SP;
                        break;
                    }
                case MIXED:
                    if (i3 < 25) {
                        c = MIXED_CHARS[i3];
                        break;
                    } else {
                        if (i3 != 25) {
                            if (i3 != 26) {
                                if (i3 == 27) {
                                    mode = Mode.LOWER;
                                } else if (i3 == 28) {
                                    mode = Mode.ALPHA;
                                } else if (i3 != 29) {
                                    if (i3 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                        sb.append((char) iArr2[i2]);
                                    } else if (i3 == TEXT_COMPACTION_MODE_LATCH) {
                                        mode = Mode.ALPHA;
                                    }
                                    c = 0;
                                    break;
                                } else {
                                    mode = Mode.PUNCT_SHIFT;
                                    mode3 = mode2;
                                }
                            }
                            c = TokenParser.SP;
                            break;
                        } else {
                            mode = Mode.PUNCT;
                        }
                        mode2 = mode;
                        c = 0;
                    }
                case PUNCT:
                    if (i3 < 29) {
                        c = PUNCT_CHARS[i3];
                        break;
                    } else {
                        if (i3 != 29) {
                            if (i3 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                sb.append((char) iArr2[i2]);
                            } else if (i3 == TEXT_COMPACTION_MODE_LATCH) {
                                mode = Mode.ALPHA;
                            }
                            c = 0;
                            break;
                        } else {
                            mode = Mode.ALPHA;
                        }
                        mode2 = mode;
                        c = 0;
                    }
                case ALPHA_SHIFT:
                    if (i3 < 26) {
                        c = (char) (i3 + 65);
                        mode2 = mode3;
                        break;
                    } else if (i3 == 26) {
                        mode2 = mode3;
                        c = TokenParser.SP;
                        break;
                    } else {
                        if (i3 == TEXT_COMPACTION_MODE_LATCH) {
                            mode = Mode.ALPHA;
                            mode2 = mode;
                            c = 0;
                            break;
                        }
                        mode2 = mode3;
                        c = 0;
                    }
                case PUNCT_SHIFT:
                    if (i3 < 29) {
                        c = PUNCT_CHARS[i3];
                        mode2 = mode3;
                        break;
                    } else {
                        if (i3 != 29) {
                            if (i3 == MODE_SHIFT_TO_BYTE_COMPACTION_MODE) {
                                sb.append((char) iArr2[i2]);
                            } else if (i3 == TEXT_COMPACTION_MODE_LATCH) {
                                mode = Mode.ALPHA;
                            }
                            mode2 = mode3;
                            c = 0;
                            break;
                        } else {
                            mode = Mode.ALPHA;
                        }
                        mode2 = mode;
                        c = 0;
                    }
                default:
                    c = 0;
                    break;
            }
            if (c != 0) {
                sb.append(c);
            }
        }
    }

    private static int byteCompaction(int i, int[] iArr, int i2, StringBuilder sb) {
        int i3;
        int i4 = MACRO_PDF417_TERMINATOR;
        int i5 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
        long j = 900;
        if (i != BYTE_COMPACTION_MODE_LATCH) {
            if (i != BYTE_COMPACTION_MODE_LATCH_6) {
                return i2;
            }
            int i6 = i2;
            int i7 = 0;
            boolean z = false;
            long j2 = 0;
            while (i6 < iArr[0] && !z) {
                int i8 = i6 + 1;
                int i9 = iArr[i6];
                if (i9 < TEXT_COMPACTION_MODE_LATCH) {
                    i7++;
                    j2 = (j2 * 900) + i9;
                    i6 = i8;
                } else {
                    if (i9 != TEXT_COMPACTION_MODE_LATCH && i9 != BYTE_COMPACTION_MODE_LATCH && i9 != NUMERIC_COMPACTION_MODE_LATCH && i9 != BYTE_COMPACTION_MODE_LATCH_6 && i9 != 928) {
                        if (i9 != BEGIN_MACRO_PDF417_OPTIONAL_FIELD) {
                            if (i9 != MACRO_PDF417_TERMINATOR) {
                                i6 = i8;
                            }
                        }
                        i6 = i8 - 1;
                        z = true;
                    }
                    i6 = i8 - 1;
                    z = true;
                }
                if (i7 % 5 == 0 && i7 > 0) {
                    char[] cArr = new char[6];
                    int i10 = 0;
                    for (int i11 = 6; i10 < i11; i11 = 6) {
                        cArr[5 - i10] = (char) (j2 & 255);
                        j2 >>= 8;
                        i10++;
                    }
                    sb.append(cArr);
                    i7 = 0;
                }
            }
            return i6;
        }
        char[] cArr2 = new char[6];
        int[] iArr2 = new int[6];
        int i12 = iArr[i2];
        int i13 = i2 + 1;
        boolean z2 = false;
        int i14 = 0;
        long j3 = 0;
        while (i13 < iArr[0] && !z2) {
            int i15 = i14 + 1;
            iArr2[i14] = i12;
            j3 = (j3 * j) + i12;
            int i16 = i13 + 1;
            int i17 = iArr[i13];
            if (i17 == TEXT_COMPACTION_MODE_LATCH || i17 == BYTE_COMPACTION_MODE_LATCH || i17 == NUMERIC_COMPACTION_MODE_LATCH || i17 == BYTE_COMPACTION_MODE_LATCH_6 || i17 == 928 || i17 == i5 || i17 == i4) {
                i16--;
                i14 = i15;
                i4 = MACRO_PDF417_TERMINATOR;
                i5 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
                j = 900;
                z2 = true;
            } else if (i15 % 5 != 0 || i15 <= 0) {
                i14 = i15;
                i4 = MACRO_PDF417_TERMINATOR;
                i5 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
                j = 900;
            } else {
                for (int i18 = 0; i18 < 6; i18++) {
                    cArr2[5 - i18] = (char) (j3 % 256);
                    j3 >>= 8;
                }
                sb.append(cArr2);
                i4 = MACRO_PDF417_TERMINATOR;
                i5 = BEGIN_MACRO_PDF417_OPTIONAL_FIELD;
                j = 900;
                i14 = 0;
            }
            int i19 = i16;
            i12 = i17;
            i13 = i19;
        }
        if (i13 != iArr[0] || i12 >= TEXT_COMPACTION_MODE_LATCH) {
            i3 = i14;
        } else {
            i3 = i14 + 1;
            iArr2[i14] = i12;
        }
        for (int i20 = 0; i20 < i3; i20++) {
            sb.append((char) iArr2[i20]);
        }
        return i13;
    }

    private static int numericCompaction(int[] iArr, int i, StringBuilder sb) throws FormatException {
        int[] iArr2 = new int[15];
        boolean z = false;
        int i2 = 0;
        while (i < iArr[0] && !z) {
            int i3 = i + 1;
            int i4 = iArr[i];
            if (i3 == iArr[0]) {
                z = true;
            }
            if (i4 < TEXT_COMPACTION_MODE_LATCH) {
                iArr2[i2] = i4;
                i2++;
            } else if (i4 == TEXT_COMPACTION_MODE_LATCH || i4 == BYTE_COMPACTION_MODE_LATCH || i4 == BYTE_COMPACTION_MODE_LATCH_6 || i4 == 928 || i4 == BEGIN_MACRO_PDF417_OPTIONAL_FIELD || i4 == MACRO_PDF417_TERMINATOR) {
                i3--;
                z = true;
            }
            if (i2 % 15 == 0 || i4 == NUMERIC_COMPACTION_MODE_LATCH || z) {
                sb.append(decodeBase900toBase10(iArr2, i2));
                i2 = 0;
            }
            i = i3;
        }
        return i;
    }

    private static String decodeBase900toBase10(int[] iArr, int i) throws FormatException {
        BigInteger bigIntegerAdd = BigInteger.ZERO;
        for (int i2 = 0; i2 < i; i2++) {
            bigIntegerAdd = bigIntegerAdd.add(EXP900[(i - i2) - 1].multiply(BigInteger.valueOf(iArr[i2])));
        }
        String string = bigIntegerAdd.toString();
        if (string.charAt(0) != '1') {
            throw FormatException.getFormatInstance();
        }
        return string.substring(1);
    }
}
