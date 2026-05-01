package com.google.zxing.aztec;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;

/* loaded from: classes.dex */
public final class AztecReader implements Reader {
    @Override // com.google.zxing.Reader
    public void reset() {
    }

    @Override // com.google.zxing.Reader
    public Result decode(BinaryBitmap binaryBitmap) throws NotFoundException, FormatException {
        return decode(binaryBitmap, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x005b A[LOOP:0: B:30:0x0059->B:31:0x005b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0083  */
    @Override // com.google.zxing.Reader
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.google.zxing.Result decode(com.google.zxing.BinaryBitmap r6, java.util.Map<com.google.zxing.DecodeHintType, ?> r7) throws com.google.zxing.NotFoundException, com.google.zxing.FormatException {
        /*
            r5 = this;
            com.google.zxing.aztec.detector.Detector r0 = new com.google.zxing.aztec.detector.Detector
            com.google.zxing.common.BitMatrix r6 = r6.getBlackMatrix()
            r0.<init>(r6)
            r6 = 0
            r1 = 0
            com.google.zxing.aztec.AztecDetectorResult r2 = r0.detect(r6)     // Catch: com.google.zxing.FormatException -> L25 com.google.zxing.NotFoundException -> L2b
            com.google.zxing.ResultPoint[] r3 = r2.getPoints()     // Catch: com.google.zxing.FormatException -> L25 com.google.zxing.NotFoundException -> L2b
            com.google.zxing.aztec.decoder.Decoder r4 = new com.google.zxing.aztec.decoder.Decoder     // Catch: com.google.zxing.FormatException -> L21 com.google.zxing.NotFoundException -> L23
            r4.<init>()     // Catch: com.google.zxing.FormatException -> L21 com.google.zxing.NotFoundException -> L23
            com.google.zxing.common.DecoderResult r2 = r4.decode(r2)     // Catch: com.google.zxing.FormatException -> L21 com.google.zxing.NotFoundException -> L23
            r4 = r3
            r3 = r1
            r1 = r2
            r2 = r3
            goto L2f
        L21:
            r2 = move-exception
            goto L27
        L23:
            r2 = move-exception
            goto L2d
        L25:
            r2 = move-exception
            r3 = r1
        L27:
            r4 = r3
            r3 = r2
            r2 = r1
            goto L2f
        L2b:
            r2 = move-exception
            r3 = r1
        L2d:
            r4 = r3
            r3 = r1
        L2f:
            if (r1 != 0) goto L4c
            r1 = 1
            com.google.zxing.aztec.AztecDetectorResult r0 = r0.detect(r1)     // Catch: java.lang.Throwable -> L44
            com.google.zxing.ResultPoint[] r4 = r0.getPoints()     // Catch: java.lang.Throwable -> L44
            com.google.zxing.aztec.decoder.Decoder r1 = new com.google.zxing.aztec.decoder.Decoder     // Catch: java.lang.Throwable -> L44
            r1.<init>()     // Catch: java.lang.Throwable -> L44
            com.google.zxing.common.DecoderResult r1 = r1.decode(r0)     // Catch: java.lang.Throwable -> L44
            goto L4c
        L44:
            r6 = move-exception
            if (r2 != 0) goto L4b
            if (r3 == 0) goto L4a
            throw r3
        L4a:
            throw r6
        L4b:
            throw r2
        L4c:
            if (r7 == 0) goto L63
            com.google.zxing.DecodeHintType r0 = com.google.zxing.DecodeHintType.NEED_RESULT_POINT_CALLBACK
            java.lang.Object r7 = r7.get(r0)
            com.google.zxing.ResultPointCallback r7 = (com.google.zxing.ResultPointCallback) r7
            if (r7 == 0) goto L63
            int r0 = r4.length
        L59:
            if (r6 >= r0) goto L63
            r2 = r4[r6]
            r7.foundPossibleResultPoint(r2)
            int r6 = r6 + 1
            goto L59
        L63:
            com.google.zxing.Result r6 = new com.google.zxing.Result
            java.lang.String r7 = r1.getText()
            byte[] r0 = r1.getRawBytes()
            com.google.zxing.BarcodeFormat r2 = com.google.zxing.BarcodeFormat.AZTEC
            r6.<init>(r7, r0, r4, r2)
            java.util.List r7 = r1.getByteSegments()
            if (r7 == 0) goto L7d
            com.google.zxing.ResultMetadataType r0 = com.google.zxing.ResultMetadataType.BYTE_SEGMENTS
            r6.putMetadata(r0, r7)
        L7d:
            java.lang.String r7 = r1.getECLevel()
            if (r7 == 0) goto L88
            com.google.zxing.ResultMetadataType r0 = com.google.zxing.ResultMetadataType.ERROR_CORRECTION_LEVEL
            r6.putMetadata(r0, r7)
        L88:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.aztec.AztecReader.decode(com.google.zxing.BinaryBitmap, java.util.Map):com.google.zxing.Result");
    }
}
