package com.google.common.primitives;

import com.google.common.base.Preconditions;
import java.util.Comparator;

/* loaded from: classes.dex */
public final class UnsignedBytes {
    public static int compare(byte b, byte b2) {
        return (b & 255) - (b2 & 255);
    }

    public static byte saturatedCast(long j) {
        if (j > 255) {
            return (byte) -1;
        }
        if (j < 0) {
            return (byte) 0;
        }
        return (byte) j;
    }

    private UnsignedBytes() {
    }

    public static byte checkedCast(long j) {
        Preconditions.checkArgument((j >> 8) == 0, "out of range: %s", Long.valueOf(j));
        return (byte) j;
    }

    public static byte min(byte... bArr) {
        Preconditions.checkArgument(bArr.length > 0);
        int i = bArr[0] & 255;
        for (int i2 = 1; i2 < bArr.length; i2++) {
            int i3 = bArr[i2] & 255;
            if (i3 < i) {
                i = i3;
            }
        }
        return (byte) i;
    }

    public static byte max(byte... bArr) {
        Preconditions.checkArgument(bArr.length > 0);
        int i = bArr[0] & 255;
        for (int i2 = 1; i2 < bArr.length; i2++) {
            int i3 = bArr[i2] & 255;
            if (i3 > i) {
                i = i3;
            }
        }
        return (byte) i;
    }

    public static String join(String str, byte... bArr) {
        Preconditions.checkNotNull(str);
        if (bArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 5);
        sb.append(bArr[0] & 255);
        for (int i = 1; i < bArr.length; i++) {
            sb.append(str);
            sb.append(bArr[i] & 255);
        }
        return sb.toString();
    }

    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }

    private enum LexicographicalComparator implements Comparator<byte[]> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(byte[] bArr, byte[] bArr2) {
            int iMin = Math.min(bArr.length, bArr2.length);
            for (int i = 0; i < iMin; i++) {
                int iCompare = UnsignedBytes.compare(bArr[i], bArr2[i]);
                if (iCompare != 0) {
                    return iCompare;
                }
            }
            return bArr.length - bArr2.length;
        }
    }
}
