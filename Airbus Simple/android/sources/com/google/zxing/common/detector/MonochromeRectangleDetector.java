package com.google.zxing.common.detector;

import com.google.zxing.NotFoundException;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitMatrix;

/* loaded from: classes.dex */
public final class MonochromeRectangleDetector {
    private static final int MAX_MODULES = 32;
    private final BitMatrix image;

    public MonochromeRectangleDetector(BitMatrix bitMatrix) {
        this.image = bitMatrix;
    }

    public ResultPoint[] detect() throws NotFoundException {
        int height = this.image.getHeight();
        int width = this.image.getWidth();
        int i = height >> 1;
        int i2 = width >> 1;
        int iMax = Math.max(1, height / 256);
        int iMax2 = Math.max(1, width / 256);
        int i3 = -iMax;
        int i4 = i2 >> 1;
        int y = ((int) findCornerFromCenter(i2, 0, 0, width, i, i3, 0, height, i4).getY()) - 1;
        int i5 = i >> 1;
        ResultPoint resultPointFindCornerFromCenter = findCornerFromCenter(i2, -iMax2, 0, width, i, 0, y, height, i5);
        int x = ((int) resultPointFindCornerFromCenter.getX()) - 1;
        ResultPoint resultPointFindCornerFromCenter2 = findCornerFromCenter(i2, iMax2, x, width, i, 0, y, height, i5);
        int x2 = ((int) resultPointFindCornerFromCenter2.getX()) + 1;
        ResultPoint resultPointFindCornerFromCenter3 = findCornerFromCenter(i2, 0, x, x2, i, iMax, y, height, i4);
        return new ResultPoint[]{findCornerFromCenter(i2, 0, x, x2, i, i3, y, ((int) resultPointFindCornerFromCenter3.getY()) + 1, i2 >> 2), resultPointFindCornerFromCenter, resultPointFindCornerFromCenter2, resultPointFindCornerFromCenter3};
    }

    private ResultPoint findCornerFromCenter(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) throws NotFoundException {
        int[] iArrBlackWhiteRange;
        int i10 = i;
        int i11 = i5;
        int[] iArr = null;
        while (i11 < i8 && i11 >= i7 && i10 < i4 && i10 >= i3) {
            if (i2 == 0) {
                iArrBlackWhiteRange = blackWhiteRange(i11, i9, i3, i4, true);
            } else {
                iArrBlackWhiteRange = blackWhiteRange(i10, i9, i7, i8, false);
            }
            if (iArrBlackWhiteRange == null) {
                if (iArr == null) {
                    throw NotFoundException.getNotFoundInstance();
                }
                if (i2 == 0) {
                    int i12 = i11 - i6;
                    if (iArr[0] < i) {
                        if (iArr[1] > i) {
                            return new ResultPoint(i6 > 0 ? iArr[0] : iArr[1], i12);
                        }
                        return new ResultPoint(iArr[0], i12);
                    }
                    return new ResultPoint(iArr[1], i12);
                }
                int i13 = i10 - i2;
                if (iArr[0] < i5) {
                    if (iArr[1] > i5) {
                        return new ResultPoint(i13, i2 < 0 ? iArr[0] : iArr[1]);
                    }
                    return new ResultPoint(i13, iArr[0]);
                }
                return new ResultPoint(i13, iArr[1]);
            }
            i11 += i6;
            i10 += i2;
            iArr = iArrBlackWhiteRange;
        }
        throw NotFoundException.getNotFoundInstance();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0035 A[EDGE_INSN: B:64:0x0035->B:20:0x0035 BREAK  A[LOOP:1: B:12:0x001e->B:68:0x001e], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x006e A[EDGE_INSN: B:81:0x006e->B:43:0x006e BREAK  A[LOOP:3: B:35:0x0058->B:86:0x0058], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int[] blackWhiteRange(int r6, int r7, int r8, int r9, boolean r10) {
        /*
            r5 = this;
            int r0 = r8 + r9
            r1 = 1
            int r0 = r0 >> r1
            r2 = r0
        L5:
            if (r2 < r8) goto L3e
            if (r10 == 0) goto L12
            com.google.zxing.common.BitMatrix r3 = r5.image
            boolean r3 = r3.get(r2, r6)
            if (r3 == 0) goto L1d
            goto L1a
        L12:
            com.google.zxing.common.BitMatrix r3 = r5.image
            boolean r3 = r3.get(r6, r2)
            if (r3 == 0) goto L1d
        L1a:
            int r2 = r2 + (-1)
            goto L5
        L1d:
            r3 = r2
        L1e:
            int r3 = r3 + (-1)
            if (r3 < r8) goto L35
            if (r10 == 0) goto L2d
            com.google.zxing.common.BitMatrix r4 = r5.image
            boolean r4 = r4.get(r3, r6)
            if (r4 == 0) goto L1e
            goto L35
        L2d:
            com.google.zxing.common.BitMatrix r4 = r5.image
            boolean r4 = r4.get(r6, r3)
            if (r4 == 0) goto L1e
        L35:
            int r4 = r2 - r3
            if (r3 < r8) goto L3e
            if (r4 <= r7) goto L3c
            goto L3e
        L3c:
            r2 = r3
            goto L5
        L3e:
            int r2 = r2 + r1
        L3f:
            if (r0 >= r9) goto L77
            if (r10 == 0) goto L4c
            com.google.zxing.common.BitMatrix r8 = r5.image
            boolean r8 = r8.get(r0, r6)
            if (r8 == 0) goto L57
            goto L54
        L4c:
            com.google.zxing.common.BitMatrix r8 = r5.image
            boolean r8 = r8.get(r6, r0)
            if (r8 == 0) goto L57
        L54:
            int r0 = r0 + 1
            goto L3f
        L57:
            r8 = r0
        L58:
            int r8 = r8 + r1
            if (r8 >= r9) goto L6e
            if (r10 == 0) goto L66
            com.google.zxing.common.BitMatrix r3 = r5.image
            boolean r3 = r3.get(r8, r6)
            if (r3 == 0) goto L58
            goto L6e
        L66:
            com.google.zxing.common.BitMatrix r3 = r5.image
            boolean r3 = r3.get(r6, r8)
            if (r3 == 0) goto L58
        L6e:
            int r3 = r8 - r0
            if (r8 >= r9) goto L77
            if (r3 <= r7) goto L75
            goto L77
        L75:
            r0 = r8
            goto L3f
        L77:
            int r0 = r0 + (-1)
            if (r0 <= r2) goto L84
            r6 = 2
            int[] r6 = new int[r6]
            r7 = 0
            r6[r7] = r2
            r6[r1] = r0
            goto L85
        L84:
            r6 = 0
        L85:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.common.detector.MonochromeRectangleDetector.blackWhiteRange(int, int, int, int, boolean):int[]");
    }
}
