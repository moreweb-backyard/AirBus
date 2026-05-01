package voice.decoder.fft;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* loaded from: classes.dex */
public class FloatFFT {
    private static final float PI = 3.1415927f;
    private static final float TWO_PI = 6.2831855f;
    private static final int[] factors = {4, 2, 3, 5};
    private float[] bk1;
    private float[] bk2;
    private int[] ip;
    private int n;
    private int nBluestein;
    private int nc;
    private int nw;
    private Plans plan;
    private float[] w;
    private float[] wtable;
    private float[] wtable_r;

    private strict enum Plans {
        SPLIT_RADIX,
        MIXED_RADIX,
        BLUESTEIN
    }

    public FloatFFT(int i) throws ExecutionException, InterruptedException {
        if (i < 1) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        this.n = i;
        if (!ConcurrencyUtils.isPowerOf2(i)) {
            if (getReminder(i, factors) >= 211) {
                this.plan = Plans.BLUESTEIN;
                this.nBluestein = ConcurrencyUtils.nextPow2((i * 2) - 1);
                this.bk1 = new float[this.nBluestein * 2];
                this.bk2 = new float[this.nBluestein * 2];
                Double.isNaN(this.nBluestein);
                this.ip = new int[((int) Math.ceil((1 << (((int) (Math.log(r7 + 0.5d) / Math.log(2.0d))) / 2)) + 2)) + 2];
                this.w = new float[this.nBluestein];
                int i2 = this.nBluestein * 2;
                this.nw = this.ip[0];
                if (i2 > (this.nw << 2)) {
                    this.nw = i2 >> 2;
                    makewt(this.nw);
                }
                this.nc = this.ip[1];
                if (this.nBluestein > (this.nc << 2)) {
                    this.nc = this.nBluestein >> 2;
                    makect(this.nc, this.w, this.nw);
                }
                bluesteini();
                return;
            }
            this.plan = Plans.MIXED_RADIX;
            this.wtable = new float[(i * 4) + 15];
            this.wtable_r = new float[(i * 2) + 15];
            cffti();
            rffti();
            return;
        }
        this.plan = Plans.SPLIT_RADIX;
        Double.isNaN(i);
        this.ip = new int[((int) Math.ceil((1 << (((int) (Math.log(r7 + 0.5d) / Math.log(2.0d))) / 2)) + 2)) + 2];
        this.w = new float[i];
        int i3 = i * 2;
        this.nw = this.ip[0];
        if (i3 > (this.nw << 2)) {
            this.nw = i3 >> 2;
            makewt(this.nw);
        }
        this.nc = this.ip[1];
        if (i > (this.nc << 2)) {
            this.nc = i >> 2;
            makect(this.nc, this.w, this.nw);
        }
    }

    public void complexForward(float[] fArr) throws ExecutionException, InterruptedException {
        complexForward(fArr, 0);
    }

    public void complexForward(float[] fArr, int i) throws ExecutionException, InterruptedException {
        if (this.n == 1) {
        }
        switch (this.plan) {
            case SPLIT_RADIX:
                cftbsub(this.n * 2, fArr, i, this.ip, this.nw, this.w);
                break;
            case MIXED_RADIX:
                cfftf(fArr, i, -1);
                break;
            case BLUESTEIN:
                bluestein_complex(fArr, i, -1);
                break;
        }
    }

    public void complexInverse(float[] fArr, boolean z) throws ExecutionException, InterruptedException {
        complexInverse(fArr, 0, z);
    }

    public void complexInverse(float[] fArr, int i, boolean z) throws ExecutionException, InterruptedException {
        if (this.n == 1) {
            return;
        }
        switch (this.plan) {
            case SPLIT_RADIX:
                cftfsub(this.n * 2, fArr, i, this.ip, this.nw, this.w);
                break;
            case MIXED_RADIX:
                cfftf(fArr, i, 1);
                break;
            case BLUESTEIN:
                bluestein_complex(fArr, i, 1);
                break;
        }
        if (z) {
            scale(this.n, fArr, i, true);
        }
    }

    public void realForward(float[] fArr) throws ExecutionException, InterruptedException {
        realForward(fArr, 0);
    }

    public void realForward(float[] fArr, int i) throws ExecutionException, InterruptedException {
        if (this.n == 1) {
        }
        switch (this.plan) {
            case SPLIT_RADIX:
                if (this.n > 4) {
                    cftfsub(this.n, fArr, i, this.ip, this.nw, this.w);
                    rftfsub(this.n, fArr, i, this.nc, this.w, this.nw);
                } else if (this.n == 4) {
                    cftx020(fArr, i);
                }
                int i2 = i + 1;
                float f = fArr[i] - fArr[i2];
                fArr[i] = fArr[i] + fArr[i2];
                fArr[i2] = f;
                break;
            case MIXED_RADIX:
                rfftf(fArr, i);
                for (int i3 = this.n - 1; i3 >= 2; i3--) {
                    int i4 = i + i3;
                    float f2 = fArr[i4];
                    int i5 = i4 - 1;
                    fArr[i4] = fArr[i5];
                    fArr[i5] = f2;
                }
                break;
            case BLUESTEIN:
                bluestein_real_forward(fArr, i);
                break;
        }
    }

    public void realForwardFull(float[] fArr) throws ExecutionException, InterruptedException {
        realForwardFull(fArr, 0);
    }

    public void realForwardFull(final float[] fArr, final int i) throws ExecutionException, InterruptedException {
        int i2;
        final int i3 = this.n * 2;
        switch (this.plan) {
            case SPLIT_RADIX:
                realForward(fArr, i);
                int numberOfThreads = ConcurrencyUtils.getNumberOfThreads();
                if (numberOfThreads > 1 && this.n / 2 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
                    Future[] futureArr = new Future[numberOfThreads];
                    int i4 = (this.n / 2) / numberOfThreads;
                    int i5 = 0;
                    while (i5 < numberOfThreads) {
                        final int i6 = i5 * i4;
                        final int i7 = i5 == numberOfThreads + (-1) ? this.n / 2 : i6 + i4;
                        int i8 = i5;
                        futureArr[i8] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.1
                            @Override // java.lang.Runnable
                            public void run() {
                                for (int i9 = i6; i9 < i7; i9++) {
                                    int i10 = i9 * 2;
                                    int i11 = i + ((i3 - i10) % i3);
                                    fArr[i11] = fArr[i + i10];
                                    fArr[i11 + 1] = -fArr[i + i10 + 1];
                                }
                            }
                        });
                        i5 = i8 + 1;
                    }
                    ConcurrencyUtils.waitForCompletion(futureArr);
                } else {
                    for (int i9 = 0; i9 < this.n / 2; i9++) {
                        int i10 = i9 * 2;
                        int i11 = ((i3 - i10) % i3) + i;
                        int i12 = i10 + i;
                        fArr[i11] = fArr[i12];
                        fArr[i11 + 1] = -fArr[i12 + 1];
                    }
                }
                int i13 = i + 1;
                fArr[this.n + i] = -fArr[i13];
                fArr[i13] = 0.0f;
                break;
            case MIXED_RADIX:
                rfftf(fArr, i);
                if (this.n % 2 == 0) {
                    i2 = this.n / 2;
                } else {
                    i2 = (this.n + 1) / 2;
                }
                for (int i14 = 1; i14 < i2; i14++) {
                    int i15 = i14 * 2;
                    int i16 = (i + i3) - i15;
                    int i17 = i15 + i;
                    fArr[i16 + 1] = -fArr[i17];
                    fArr[i16] = fArr[i17 - 1];
                }
                for (int i18 = 1; i18 < this.n; i18++) {
                    int i19 = (this.n + i) - i18;
                    int i20 = i19 + 1;
                    float f = fArr[i20];
                    fArr[i20] = fArr[i19];
                    fArr[i19] = f;
                }
                fArr[i + 1] = 0.0f;
                break;
            case BLUESTEIN:
                bluestein_real_full(fArr, i, -1);
                break;
        }
    }

    public void realInverse(float[] fArr, boolean z) throws ExecutionException, InterruptedException {
        realInverse(fArr, 0, z);
    }

    public void realInverse(float[] fArr, int i, boolean z) throws ExecutionException, InterruptedException {
        if (this.n == 1) {
            return;
        }
        switch (this.plan) {
            case SPLIT_RADIX:
                int i2 = i + 1;
                double d = fArr[i] - fArr[i2];
                Double.isNaN(d);
                fArr[i2] = (float) (d * 0.5d);
                fArr[i] = fArr[i] - fArr[i2];
                if (this.n > 4) {
                    rftfsub(this.n, fArr, i, this.nc, this.w, this.nw);
                    cftbsub(this.n, fArr, i, this.ip, this.nw, this.w);
                } else if (this.n == 4) {
                    cftxc020(fArr, i);
                }
                if (z) {
                    scale(this.n / 2, fArr, i, false);
                    return;
                }
                return;
            case MIXED_RADIX:
                break;
            case BLUESTEIN:
                bluestein_real_inverse(fArr, i);
                if (z) {
                    scale(this.n, fArr, i, false);
                    return;
                }
                return;
            default:
                return;
        }
        for (int i3 = 2; i3 < this.n; i3++) {
            int i4 = i + i3;
            int i5 = i4 - 1;
            float f = fArr[i5];
            fArr[i5] = fArr[i4];
            fArr[i4] = f;
        }
        rfftb(fArr, i);
        if (z) {
            scale(this.n, fArr, i, false);
        }
    }

    public void realInverseFull(float[] fArr, boolean z) throws ExecutionException, InterruptedException {
        realInverseFull(fArr, 0, z);
    }

    public void realInverseFull(final float[] fArr, final int i, boolean z) throws ExecutionException, InterruptedException {
        int i2;
        final int i3 = this.n * 2;
        switch (this.plan) {
            case SPLIT_RADIX:
                realInverse2(fArr, i, z);
                int numberOfThreads = ConcurrencyUtils.getNumberOfThreads();
                if (numberOfThreads > 1 && this.n / 2 > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
                    Future[] futureArr = new Future[numberOfThreads];
                    int i4 = (this.n / 2) / numberOfThreads;
                    int i5 = 0;
                    while (i5 < numberOfThreads) {
                        final int i6 = i5 * i4;
                        final int i7 = i5 == numberOfThreads + (-1) ? this.n / 2 : i6 + i4;
                        int i8 = i5;
                        futureArr[i8] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.2
                            @Override // java.lang.Runnable
                            public void run() {
                                for (int i9 = i6; i9 < i7; i9++) {
                                    int i10 = i9 * 2;
                                    int i11 = i + ((i3 - i10) % i3);
                                    fArr[i11] = fArr[i + i10];
                                    fArr[i11 + 1] = -fArr[i + i10 + 1];
                                }
                            }
                        });
                        i5 = i8 + 1;
                    }
                    ConcurrencyUtils.waitForCompletion(futureArr);
                } else {
                    for (int i9 = 0; i9 < this.n / 2; i9++) {
                        int i10 = i9 * 2;
                        int i11 = ((i3 - i10) % i3) + i;
                        int i12 = i10 + i;
                        fArr[i11] = fArr[i12];
                        fArr[i11 + 1] = -fArr[i12 + 1];
                    }
                }
                int i13 = i + 1;
                fArr[this.n + i] = -fArr[i13];
                fArr[i13] = 0.0f;
                break;
            case MIXED_RADIX:
                rfftf(fArr, i);
                if (z) {
                    scale(this.n, fArr, i, false);
                }
                if (this.n % 2 == 0) {
                    i2 = this.n / 2;
                } else {
                    i2 = (this.n + 1) / 2;
                }
                for (int i14 = 1; i14 < i2; i14++) {
                    int i15 = i14 * 2;
                    int i16 = i + i15;
                    int i17 = (i + i3) - i15;
                    fArr[i16] = -fArr[i16];
                    fArr[i17 + 1] = -fArr[i16];
                    fArr[i17] = fArr[i16 - 1];
                }
                for (int i18 = 1; i18 < this.n; i18++) {
                    int i19 = (this.n + i) - i18;
                    int i20 = i19 + 1;
                    float f = fArr[i20];
                    fArr[i20] = fArr[i19];
                    fArr[i19] = f;
                }
                fArr[i + 1] = 0.0f;
                break;
            case BLUESTEIN:
                bluestein_real_full(fArr, i, 1);
                if (z) {
                    scale(this.n, fArr, i, true);
                    break;
                }
                break;
        }
    }

    protected void realInverse2(float[] fArr, int i, boolean z) throws ExecutionException, InterruptedException {
        if (this.n == 1) {
        }
        switch (this.plan) {
            case SPLIT_RADIX:
                if (this.n > 4) {
                    cftfsub(this.n, fArr, i, this.ip, this.nw, this.w);
                    rftbsub(this.n, fArr, i, this.nc, this.w, this.nw);
                } else if (this.n == 4) {
                    cftbsub(this.n, fArr, i, this.ip, this.nw, this.w);
                }
                int i2 = i + 1;
                float f = fArr[i] - fArr[i2];
                fArr[i] = fArr[i] + fArr[i2];
                fArr[i2] = f;
                if (z) {
                    scale(this.n, fArr, i, false);
                    break;
                }
                break;
            case MIXED_RADIX:
                rfftf(fArr, i);
                for (int i3 = this.n - 1; i3 >= 2; i3--) {
                    int i4 = i + i3;
                    float f2 = fArr[i4];
                    int i5 = i4 - 1;
                    fArr[i4] = fArr[i5];
                    fArr[i5] = f2;
                }
                if (z) {
                    scale(this.n, fArr, i, false);
                }
                if (this.n % 2 == 0) {
                    int i6 = this.n / 2;
                    for (int i7 = 1; i7 < i6; i7++) {
                        int i8 = (i7 * 2) + i + 1;
                        fArr[i8] = -fArr[i8];
                    }
                    break;
                } else {
                    int i9 = (this.n - 1) / 2;
                    for (int i10 = 0; i10 < i9; i10++) {
                        int i11 = (i10 * 2) + i + 1;
                        fArr[i11] = -fArr[i11];
                    }
                    break;
                }
            case BLUESTEIN:
                bluestein_real_inverse2(fArr, i);
                if (z) {
                    scale(this.n, fArr, i, false);
                    break;
                }
                break;
        }
    }

    private static int getReminder(int i, int[] iArr) {
        if (i <= 0) {
            throw new IllegalArgumentException("n must be positive integer");
        }
        for (int i2 = 0; i2 < iArr.length && i != 1; i2++) {
            int i3 = iArr[i2];
            while (i % i3 == 0) {
                i /= i3;
            }
        }
        return i;
    }

    void cffti(int i, int i2) {
        int i3;
        int i4 = i;
        int i5 = 1;
        if (i4 == 1) {
            return;
        }
        int i6 = i4 * 2;
        int i7 = i4 * 4;
        int i8 = i4;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        loop0: while (true) {
            i9++;
            i3 = 2;
            i10 = i9 <= 4 ? factors[i9 - 1] : i10 + 2;
            while (true) {
                int i12 = i8 / i10;
                if (i8 - (i10 * i12) != 0) {
                    break;
                }
                i11++;
                this.wtable[i2 + i11 + 1 + i7] = i10;
                if (i10 == 2 && i11 != 1) {
                    for (int i13 = 2; i13 <= i11; i13++) {
                        int i14 = i2 + (i11 - i13) + 2 + i7;
                        this.wtable[i14 + 1] = this.wtable[i14];
                    }
                    this.wtable[i2 + 2 + i7] = 2.0f;
                }
                if (i12 == 1) {
                    break loop0;
                }
                i8 = i12;
                i4 = i;
            }
        }
        float f = i4;
        this.wtable[i2 + i7] = f;
        this.wtable[i2 + 1 + i7] = i11;
        float f2 = TWO_PI / f;
        int i15 = 1;
        int i16 = 1;
        int i17 = 1;
        while (i15 <= i11) {
            int i18 = (int) this.wtable[i2 + i15 + i5 + i7];
            int i19 = i16 * i18;
            int i20 = i4 / i19;
            int i21 = i20 + i20 + i3;
            int i22 = i18 - 1;
            int i23 = i17;
            int i24 = 1;
            int i25 = 0;
            while (i24 <= i22) {
                int i26 = i2 + i23;
                this.wtable[(i26 - 1) + i6] = 1.0f;
                float f3 = 0.0f;
                this.wtable[i26 + i6] = 0.0f;
                int i27 = i25 + i16;
                float f4 = i27 * f2;
                int i28 = i23;
                int i29 = 4;
                while (i29 <= i21) {
                    i28 += 2;
                    f3 += 1.0f;
                    int i30 = i2 + i28 + i6;
                    int i31 = i16;
                    double d = f3 * f4;
                    this.wtable[i30 - 1] = (float) Math.cos(d);
                    this.wtable[i30] = (float) Math.sin(d);
                    i29 += 2;
                    i22 = i22;
                    i7 = i7;
                    i11 = i11;
                    i16 = i31;
                    i27 = i27;
                    f4 = f4;
                }
                int i32 = i22;
                int i33 = i7;
                int i34 = i16;
                int i35 = i11;
                int i36 = i27;
                if (i18 > 5) {
                    int i37 = i2 + i23 + i6;
                    int i38 = i2 + i28 + i6;
                    this.wtable[i37 - 1] = this.wtable[i38 - 1];
                    this.wtable[i37] = this.wtable[i38];
                }
                i24++;
                i23 = i28;
                i22 = i32;
                i7 = i33;
                i11 = i35;
                i16 = i34;
                i25 = i36;
            }
            i15++;
            i16 = i19;
            i17 = i23;
            i4 = i;
            i5 = 1;
            i3 = 2;
        }
    }

    void cffti() {
        if (this.n == 1) {
            return;
        }
        int i = 2;
        int i2 = this.n * 2;
        int i3 = 4;
        int i4 = this.n * 4;
        int i5 = this.n;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        loop0: while (true) {
            i6++;
            i7 = i6 <= 4 ? factors[i6 - 1] : i7 + 2;
            while (true) {
                int i9 = i5 / i7;
                if (i5 - (i7 * i9) != 0) {
                    break;
                }
                i8++;
                this.wtable[i8 + 1 + i4] = i7;
                if (i7 == 2 && i8 != 1) {
                    for (int i10 = 2; i10 <= i8; i10++) {
                        int i11 = (i8 - i10) + 2 + i4;
                        this.wtable[i11 + 1] = this.wtable[i11];
                    }
                    this.wtable[i4 + 2] = 2.0f;
                }
                if (i9 == 1) {
                    break loop0;
                } else {
                    i5 = i9;
                }
            }
        }
        this.wtable[i4] = this.n;
        this.wtable[i4 + 1] = i8;
        float f = TWO_PI / this.n;
        int i12 = 1;
        int i13 = 1;
        int i14 = 1;
        while (i12 <= i8) {
            i12++;
            int i15 = (int) this.wtable[i12 + i4];
            int i16 = i13 * i15;
            int i17 = this.n / i16;
            int i18 = i17 + i17 + i;
            int i19 = i15 - 1;
            int i20 = i14;
            int i21 = 1;
            int i22 = 0;
            while (i21 <= i19) {
                this.wtable[(i20 - 1) + i2] = 1.0f;
                int i23 = i20 + i2;
                float f2 = 0.0f;
                this.wtable[i23] = 0.0f;
                int i24 = i22 + i13;
                float f3 = i24 * f;
                while (i3 <= i18) {
                    i20 += 2;
                    f2 += 1.0f;
                    int i25 = i20 + i2;
                    float f4 = f3;
                    double d = f2 * f3;
                    this.wtable[i25 - 1] = (float) Math.cos(d);
                    this.wtable[i25] = (float) Math.sin(d);
                    i3 += 2;
                    i24 = i24;
                    i4 = i4;
                    f3 = f4;
                    f = f;
                }
                int i26 = i24;
                int i27 = i4;
                float f5 = f;
                if (i15 > 5) {
                    int i28 = i20 + i2;
                    this.wtable[i23 - 1] = this.wtable[i28 - 1];
                    this.wtable[i23] = this.wtable[i28];
                }
                i21++;
                i22 = i26;
                i4 = i27;
                f = f5;
                i = 2;
                i3 = 4;
            }
            i13 = i16;
            i14 = i20;
        }
    }

    void rffti() {
        int i = 1;
        if (this.n == 1) {
            return;
        }
        int i2 = this.n * 2;
        int i3 = this.n;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        loop0: while (true) {
            i4++;
            i5 = i4 <= 4 ? factors[i4 - 1] : i5 + 2;
            while (true) {
                int i7 = i3 / i5;
                if (i3 - (i5 * i7) != 0) {
                    break;
                }
                i6++;
                this.wtable_r[i6 + 1 + i2] = i5;
                if (i5 == 2 && i6 != 1) {
                    for (int i8 = 2; i8 <= i6; i8++) {
                        int i9 = (i6 - i8) + 2 + i2;
                        this.wtable_r[i9 + 1] = this.wtable_r[i9];
                    }
                    this.wtable_r[i2 + 2] = 2.0f;
                }
                if (i7 == 1) {
                    break loop0;
                } else {
                    i3 = i7;
                }
            }
        }
        this.wtable_r[i2] = this.n;
        this.wtable_r[i2 + 1] = i6;
        float f = TWO_PI / this.n;
        int i10 = i6 - 1;
        if (i10 == 0) {
            return;
        }
        int i11 = 1;
        int i12 = 1;
        int i13 = 0;
        while (i11 <= i10) {
            i11++;
            int i14 = (int) this.wtable_r[i11 + i2];
            int i15 = i12 * i14;
            int i16 = this.n / i15;
            int i17 = i14 - i;
            int i18 = i13;
            int i19 = 1;
            int i20 = 0;
            while (i19 <= i17) {
                i20 += i12;
                float f2 = i20 * f;
                int i21 = i18;
                int i22 = 3;
                float f3 = 0.0f;
                while (i22 <= i16) {
                    i21 += 2;
                    f3 += 1.0f;
                    int i23 = i21 + this.n;
                    double d = f3 * f2;
                    this.wtable_r[i23 - 2] = (float) Math.cos(d);
                    this.wtable_r[i23 - 1] = (float) Math.sin(d);
                    i22 += 2;
                    i2 = i2;
                    f = f;
                    i10 = i10;
                    i11 = i11;
                }
                i18 += i16;
                i19++;
                i = 1;
            }
            i12 = i15;
            i13 = i18;
        }
    }

    private void bluesteini() throws ExecutionException, InterruptedException {
        float f = PI / this.n;
        this.bk1[0] = 1.0f;
        this.bk1[1] = 0.0f;
        int i = 0;
        for (int i2 = 1; i2 < this.n; i2++) {
            int i3 = i2 * 2;
            i += i3 - 1;
            if (i >= this.n * 2) {
                i -= this.n * 2;
            }
            double d = i * f;
            this.bk1[i3] = (float) Math.cos(d);
            this.bk1[i3 + 1] = (float) Math.sin(d);
        }
        double d2 = this.nBluestein;
        Double.isNaN(d2);
        float f2 = (float) (1.0d / d2);
        this.bk2[0] = this.bk1[0] * f2;
        this.bk2[1] = this.bk1[1] * f2;
        for (int i4 = 2; i4 < this.n * 2; i4 += 2) {
            this.bk2[i4] = this.bk1[i4] * f2;
            int i5 = i4 + 1;
            this.bk2[i5] = this.bk1[i5] * f2;
            this.bk2[(this.nBluestein * 2) - i4] = this.bk2[i4];
            this.bk2[((this.nBluestein * 2) - i4) + 1] = this.bk2[i5];
        }
        cftbsub(this.nBluestein * 2, this.bk2, 0, this.ip, this.nw, this.w);
    }

    private void makewt(int i) {
        this.ip[0] = i;
        this.ip[1] = 1;
        if (i > 2) {
            int i2 = i >> 1;
            double d = i2;
            Double.isNaN(d);
            float f = (float) (0.7853981633974483d / d);
            float f2 = 2.0f * f;
            float fCos = (float) Math.cos(i2 * f);
            this.w[0] = 1.0f;
            this.w[1] = fCos;
            if (i2 == 4) {
                double d2 = f2;
                this.w[2] = (float) Math.cos(d2);
                this.w[3] = (float) Math.sin(d2);
            } else if (i2 > 4) {
                makeipt(i);
                this.w[2] = (float) (0.5d / Math.cos(f2));
                this.w[3] = (float) (0.5d / Math.cos(6.0f * f));
                for (int i3 = 4; i3 < i2; i3 += 4) {
                    float f3 = i3 * f;
                    double d3 = f3;
                    this.w[i3] = (float) Math.cos(d3);
                    this.w[i3 + 1] = (float) Math.sin(d3);
                    double d4 = 3.0f * f3;
                    this.w[i3 + 2] = (float) Math.cos(d4);
                    this.w[i3 + 3] = (float) (-Math.sin(d4));
                }
            }
            int i4 = 0;
            for (int i5 = 2; i2 > i5; i5 = 2) {
                int i6 = i4 + i2;
                i2 >>= 1;
                this.w[i6] = 1.0f;
                this.w[i6 + 1] = fCos;
                if (i2 == 4) {
                    float f4 = this.w[i4 + 4];
                    float f5 = this.w[i4 + 5];
                    this.w[i6 + 2] = f4;
                    this.w[i6 + 3] = f5;
                } else {
                    if (i2 > 4) {
                        float f6 = this.w[i4 + 4];
                        float f7 = this.w[i4 + 6];
                        double d5 = f6;
                        Double.isNaN(d5);
                        this.w[i6 + 2] = (float) (0.5d / d5);
                        double d6 = f7;
                        Double.isNaN(d6);
                        this.w[i6 + 3] = (float) (0.5d / d6);
                        for (int i7 = 4; i7 < i2; i7 += 4) {
                            int i8 = (i7 * 2) + i4;
                            int i9 = i6 + i7;
                            float f8 = this.w[i8];
                            float f9 = this.w[i8 + 1];
                            float f10 = this.w[i8 + 2];
                            float f11 = this.w[i8 + 3];
                            this.w[i9] = f8;
                            this.w[i9 + 1] = f9;
                            this.w[i9 + 2] = f10;
                            this.w[i9 + 3] = f11;
                        }
                    }
                    i4 = i6;
                }
                i4 = i6;
            }
        }
    }

    private void makeipt(int i) {
        this.ip[2] = 0;
        this.ip[3] = 16;
        int i2 = 2;
        while (i > 32) {
            int i3 = i2 << 1;
            int i4 = i3 << 3;
            for (int i5 = i2; i5 < i3; i5++) {
                int i6 = this.ip[i5] << 2;
                this.ip[i2 + i5] = i6;
                this.ip[i3 + i5] = i6 + i4;
            }
            i >>= 2;
            i2 = i3;
        }
    }

    private void makect(int i, float[] fArr, int i2) {
        this.ip[1] = i;
        if (i > 1) {
            int i3 = i >> 1;
            double d = i3;
            Double.isNaN(d);
            float f = (float) (0.7853981633974483d / d);
            fArr[i2] = (float) Math.cos(i3 * f);
            double d2 = fArr[i2];
            Double.isNaN(d2);
            fArr[i2 + i3] = (float) (d2 * 0.5d);
            for (int i4 = 1; i4 < i3; i4++) {
                double d3 = i4 * f;
                fArr[i2 + i4] = (float) (Math.cos(d3) * 0.5d);
                fArr[(i2 + i) - i4] = (float) (Math.sin(d3) * 0.5d);
            }
        }
    }

    private void bluestein_complex(final float[] fArr, final int i, final int i2) throws ExecutionException, InterruptedException {
        final float[] fArr2 = new float[this.nBluestein * 2];
        int i3 = 0;
        if (ConcurrencyUtils.getNumberOfThreads() > 1 && this.n > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            Future[] futureArr = new Future[2];
            int i4 = this.n / 2;
            int i5 = 0;
            while (i5 < 2) {
                final int i6 = i5 * i4;
                final int i7 = i5 == 1 ? this.n : i6 + i4;
                int i8 = i5;
                Future[] futureArr2 = futureArr;
                futureArr2[i8] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (i2 > 0) {
                            for (int i9 = i6; i9 < i7; i9++) {
                                int i10 = i9 * 2;
                                int i11 = i10 + 1;
                                int i12 = i + i10;
                                int i13 = i + i11;
                                fArr2[i10] = (fArr[i12] * FloatFFT.this.bk1[i10]) - (fArr[i13] * FloatFFT.this.bk1[i11]);
                                fArr2[i11] = (fArr[i12] * FloatFFT.this.bk1[i11]) + (fArr[i13] * FloatFFT.this.bk1[i10]);
                            }
                            return;
                        }
                        for (int i14 = i6; i14 < i7; i14++) {
                            int i15 = i14 * 2;
                            int i16 = i15 + 1;
                            int i17 = i + i15;
                            int i18 = i + i16;
                            fArr2[i15] = (fArr[i17] * FloatFFT.this.bk1[i15]) + (fArr[i18] * FloatFFT.this.bk1[i16]);
                            fArr2[i16] = ((-fArr[i17]) * FloatFFT.this.bk1[i16]) + (fArr[i18] * FloatFFT.this.bk1[i15]);
                        }
                    }
                });
                i5 = i8 + 1;
                futureArr = futureArr2;
            }
            Future[] futureArr3 = futureArr;
            ConcurrencyUtils.waitForCompletion(futureArr3);
            cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            int i9 = this.nBluestein / 2;
            int i10 = 0;
            while (i10 < 2) {
                final int i11 = i10 * i9;
                final int i12 = i10 == 1 ? this.nBluestein : i11 + i9;
                futureArr3[i10] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.4
                    @Override // java.lang.Runnable
                    public void run() {
                        if (i2 > 0) {
                            for (int i13 = i11; i13 < i12; i13++) {
                                int i14 = i13 * 2;
                                int i15 = i14 + 1;
                                float f = ((-fArr2[i14]) * FloatFFT.this.bk2[i15]) + (fArr2[i15] * FloatFFT.this.bk2[i14]);
                                fArr2[i14] = (fArr2[i14] * FloatFFT.this.bk2[i14]) + (fArr2[i15] * FloatFFT.this.bk2[i15]);
                                fArr2[i15] = f;
                            }
                            return;
                        }
                        for (int i16 = i11; i16 < i12; i16++) {
                            int i17 = i16 * 2;
                            int i18 = i17 + 1;
                            float f2 = (fArr2[i17] * FloatFFT.this.bk2[i18]) + (fArr2[i18] * FloatFFT.this.bk2[i17]);
                            fArr2[i17] = (fArr2[i17] * FloatFFT.this.bk2[i17]) - (fArr2[i18] * FloatFFT.this.bk2[i18]);
                            fArr2[i18] = f2;
                        }
                    }
                });
                i10++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr3);
            cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            int i13 = this.n / 2;
            while (i3 < 2) {
                final int i14 = i3 * i13;
                final int i15 = i3 == 1 ? this.n : i14 + i13;
                futureArr3[i3] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.5
                    @Override // java.lang.Runnable
                    public void run() {
                        if (i2 > 0) {
                            for (int i16 = i14; i16 < i15; i16++) {
                                int i17 = i16 * 2;
                                int i18 = i17 + 1;
                                int i19 = i + i17;
                                int i20 = i + i18;
                                fArr[i19] = (FloatFFT.this.bk1[i17] * fArr2[i17]) - (FloatFFT.this.bk1[i18] * fArr2[i18]);
                                fArr[i20] = (FloatFFT.this.bk1[i18] * fArr2[i17]) + (FloatFFT.this.bk1[i17] * fArr2[i18]);
                            }
                            return;
                        }
                        for (int i21 = i14; i21 < i15; i21++) {
                            int i22 = i21 * 2;
                            int i23 = i22 + 1;
                            int i24 = i + i22;
                            int i25 = i + i23;
                            fArr[i24] = (FloatFFT.this.bk1[i22] * fArr2[i22]) + (FloatFFT.this.bk1[i23] * fArr2[i23]);
                            fArr[i25] = ((-FloatFFT.this.bk1[i23]) * fArr2[i22]) + (FloatFFT.this.bk1[i22] * fArr2[i23]);
                        }
                    }
                });
                i3++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr3);
            return;
        }
        if (i2 > 0) {
            for (int i16 = 0; i16 < this.n; i16++) {
                int i17 = i16 * 2;
                int i18 = i17 + 1;
                int i19 = i + i17;
                int i20 = i + i18;
                fArr2[i17] = (fArr[i19] * this.bk1[i17]) - (fArr[i20] * this.bk1[i18]);
                fArr2[i18] = (fArr[i19] * this.bk1[i18]) + (fArr[i20] * this.bk1[i17]);
            }
        } else {
            for (int i21 = 0; i21 < this.n; i21++) {
                int i22 = i21 * 2;
                int i23 = i22 + 1;
                int i24 = i + i22;
                int i25 = i + i23;
                fArr2[i22] = (fArr[i24] * this.bk1[i22]) + (fArr[i25] * this.bk1[i23]);
                fArr2[i23] = ((-fArr[i24]) * this.bk1[i23]) + (fArr[i25] * this.bk1[i22]);
            }
        }
        cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        if (i2 > 0) {
            for (int i26 = 0; i26 < this.nBluestein; i26++) {
                int i27 = i26 * 2;
                int i28 = i27 + 1;
                float f = ((-fArr2[i27]) * this.bk2[i28]) + (fArr2[i28] * this.bk2[i27]);
                fArr2[i27] = (fArr2[i27] * this.bk2[i27]) + (fArr2[i28] * this.bk2[i28]);
                fArr2[i28] = f;
            }
        } else {
            for (int i29 = 0; i29 < this.nBluestein; i29++) {
                int i30 = i29 * 2;
                int i31 = i30 + 1;
                float f2 = (fArr2[i30] * this.bk2[i31]) + (fArr2[i31] * this.bk2[i30]);
                fArr2[i30] = (fArr2[i30] * this.bk2[i30]) - (fArr2[i31] * this.bk2[i31]);
                fArr2[i31] = f2;
            }
        }
        cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        if (i2 > 0) {
            while (i3 < this.n) {
                int i32 = i3 * 2;
                int i33 = i32 + 1;
                fArr[i + i32] = (this.bk1[i32] * fArr2[i32]) - (this.bk1[i33] * fArr2[i33]);
                fArr[i + i33] = (this.bk1[i33] * fArr2[i32]) + (this.bk1[i32] * fArr2[i33]);
                i3++;
            }
            return;
        }
        while (i3 < this.n) {
            int i34 = i3 * 2;
            int i35 = i34 + 1;
            fArr[i + i34] = (this.bk1[i34] * fArr2[i34]) + (this.bk1[i35] * fArr2[i35]);
            fArr[i + i35] = ((-this.bk1[i35]) * fArr2[i34]) + (this.bk1[i34] * fArr2[i35]);
            i3++;
        }
    }

    private void bluestein_real_full(final float[] fArr, final int i, final int i2) throws ExecutionException, InterruptedException {
        final float[] fArr2 = new float[this.nBluestein * 2];
        int i3 = 0;
        if (ConcurrencyUtils.getNumberOfThreads() > 1 && this.n > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            Future[] futureArr = new Future[2];
            int i4 = this.n / 2;
            int i5 = 0;
            while (i5 < 2) {
                final int i6 = i5 * i4;
                final int i7 = i5 == 1 ? this.n : i6 + i4;
                int i8 = i5;
                Future[] futureArr2 = futureArr;
                futureArr2[i8] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.6
                    @Override // java.lang.Runnable
                    public void run() {
                        if (i2 > 0) {
                            for (int i9 = i6; i9 < i7; i9++) {
                                int i10 = i9 * 2;
                                int i11 = i10 + 1;
                                int i12 = i + i9;
                                fArr2[i10] = fArr[i12] * FloatFFT.this.bk1[i10];
                                fArr2[i11] = fArr[i12] * FloatFFT.this.bk1[i11];
                            }
                            return;
                        }
                        for (int i13 = i6; i13 < i7; i13++) {
                            int i14 = i13 * 2;
                            int i15 = i14 + 1;
                            int i16 = i + i13;
                            fArr2[i14] = fArr[i16] * FloatFFT.this.bk1[i14];
                            fArr2[i15] = (-fArr[i16]) * FloatFFT.this.bk1[i15];
                        }
                    }
                });
                i5 = i8 + 1;
                futureArr = futureArr2;
            }
            Future[] futureArr3 = futureArr;
            ConcurrencyUtils.waitForCompletion(futureArr3);
            cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            int i9 = this.nBluestein / 2;
            int i10 = 0;
            while (i10 < 2) {
                final int i11 = i10 * i9;
                final int i12 = i10 == 1 ? this.nBluestein : i11 + i9;
                futureArr3[i10] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.7
                    @Override // java.lang.Runnable
                    public void run() {
                        if (i2 > 0) {
                            for (int i13 = i11; i13 < i12; i13++) {
                                int i14 = i13 * 2;
                                int i15 = i14 + 1;
                                float f = ((-fArr2[i14]) * FloatFFT.this.bk2[i15]) + (fArr2[i15] * FloatFFT.this.bk2[i14]);
                                fArr2[i14] = (fArr2[i14] * FloatFFT.this.bk2[i14]) + (fArr2[i15] * FloatFFT.this.bk2[i15]);
                                fArr2[i15] = f;
                            }
                            return;
                        }
                        for (int i16 = i11; i16 < i12; i16++) {
                            int i17 = i16 * 2;
                            int i18 = i17 + 1;
                            float f2 = (fArr2[i17] * FloatFFT.this.bk2[i18]) + (fArr2[i18] * FloatFFT.this.bk2[i17]);
                            fArr2[i17] = (fArr2[i17] * FloatFFT.this.bk2[i17]) - (fArr2[i18] * FloatFFT.this.bk2[i18]);
                            fArr2[i18] = f2;
                        }
                    }
                });
                i10++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr3);
            cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            int i13 = this.n / 2;
            while (i3 < 2) {
                final int i14 = i3 * i13;
                final int i15 = i3 == 1 ? this.n : i14 + i13;
                futureArr3[i3] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.8
                    @Override // java.lang.Runnable
                    public void run() {
                        if (i2 > 0) {
                            for (int i16 = i14; i16 < i15; i16++) {
                                int i17 = i16 * 2;
                                int i18 = i17 + 1;
                                fArr[i + i17] = (FloatFFT.this.bk1[i17] * fArr2[i17]) - (FloatFFT.this.bk1[i18] * fArr2[i18]);
                                fArr[i + i18] = (FloatFFT.this.bk1[i18] * fArr2[i17]) + (FloatFFT.this.bk1[i17] * fArr2[i18]);
                            }
                            return;
                        }
                        for (int i19 = i14; i19 < i15; i19++) {
                            int i20 = i19 * 2;
                            int i21 = i20 + 1;
                            fArr[i + i20] = (FloatFFT.this.bk1[i20] * fArr2[i20]) + (FloatFFT.this.bk1[i21] * fArr2[i21]);
                            fArr[i + i21] = ((-FloatFFT.this.bk1[i21]) * fArr2[i20]) + (FloatFFT.this.bk1[i20] * fArr2[i21]);
                        }
                    }
                });
                i3++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr3);
            return;
        }
        if (i2 > 0) {
            for (int i16 = 0; i16 < this.n; i16++) {
                int i17 = i16 * 2;
                int i18 = i17 + 1;
                int i19 = i + i16;
                fArr2[i17] = fArr[i19] * this.bk1[i17];
                fArr2[i18] = fArr[i19] * this.bk1[i18];
            }
        } else {
            for (int i20 = 0; i20 < this.n; i20++) {
                int i21 = i20 * 2;
                int i22 = i21 + 1;
                int i23 = i + i20;
                fArr2[i21] = fArr[i23] * this.bk1[i21];
                fArr2[i22] = (-fArr[i23]) * this.bk1[i22];
            }
        }
        cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        if (i2 > 0) {
            for (int i24 = 0; i24 < this.nBluestein; i24++) {
                int i25 = i24 * 2;
                int i26 = i25 + 1;
                float f = ((-fArr2[i25]) * this.bk2[i26]) + (fArr2[i26] * this.bk2[i25]);
                fArr2[i25] = (fArr2[i25] * this.bk2[i25]) + (fArr2[i26] * this.bk2[i26]);
                fArr2[i26] = f;
            }
        } else {
            for (int i27 = 0; i27 < this.nBluestein; i27++) {
                int i28 = i27 * 2;
                int i29 = i28 + 1;
                float f2 = (fArr2[i28] * this.bk2[i29]) + (fArr2[i29] * this.bk2[i28]);
                fArr2[i28] = (fArr2[i28] * this.bk2[i28]) - (fArr2[i29] * this.bk2[i29]);
                fArr2[i29] = f2;
            }
        }
        cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        if (i2 > 0) {
            while (i3 < this.n) {
                int i30 = i3 * 2;
                int i31 = i30 + 1;
                fArr[i + i30] = (this.bk1[i30] * fArr2[i30]) - (this.bk1[i31] * fArr2[i31]);
                fArr[i + i31] = (this.bk1[i31] * fArr2[i30]) + (this.bk1[i30] * fArr2[i31]);
                i3++;
            }
            return;
        }
        while (i3 < this.n) {
            int i32 = i3 * 2;
            int i33 = i32 + 1;
            fArr[i + i32] = (this.bk1[i32] * fArr2[i32]) + (this.bk1[i33] * fArr2[i33]);
            fArr[i + i33] = ((-this.bk1[i33]) * fArr2[i32]) + (this.bk1[i32] * fArr2[i33]);
            i3++;
        }
    }

    private void bluestein_real_forward(final float[] fArr, final int i) throws ExecutionException, InterruptedException {
        final float[] fArr2 = new float[this.nBluestein * 2];
        if (ConcurrencyUtils.getNumberOfThreads() > 1 && this.n > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            Future[] futureArr = new Future[2];
            int i2 = this.n / 2;
            int i3 = 0;
            while (i3 < 2) {
                final int i4 = i3 * i2;
                final int i5 = i3 == 1 ? this.n : i4 + i2;
                int i6 = i3;
                futureArr[i6] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.9
                    @Override // java.lang.Runnable
                    public void run() {
                        for (int i7 = i4; i7 < i5; i7++) {
                            int i8 = i7 * 2;
                            int i9 = i8 + 1;
                            int i10 = i + i7;
                            fArr2[i8] = fArr[i10] * FloatFFT.this.bk1[i8];
                            fArr2[i9] = (-fArr[i10]) * FloatFFT.this.bk1[i9];
                        }
                    }
                });
                i3 = i6 + 1;
            }
            ConcurrencyUtils.waitForCompletion(futureArr);
            cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            int i7 = this.nBluestein / 2;
            int i8 = 0;
            while (i8 < 2) {
                final int i9 = i8 * i7;
                final int i10 = i8 == 1 ? this.nBluestein : i9 + i7;
                futureArr[i8] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.10
                    @Override // java.lang.Runnable
                    public void run() {
                        for (int i11 = i9; i11 < i10; i11++) {
                            int i12 = i11 * 2;
                            int i13 = i12 + 1;
                            float f = (fArr2[i12] * FloatFFT.this.bk2[i13]) + (fArr2[i13] * FloatFFT.this.bk2[i12]);
                            fArr2[i12] = (fArr2[i12] * FloatFFT.this.bk2[i12]) - (fArr2[i13] * FloatFFT.this.bk2[i13]);
                            fArr2[i13] = f;
                        }
                    }
                });
                i8++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr);
        } else {
            for (int i11 = 0; i11 < this.n; i11++) {
                int i12 = i11 * 2;
                int i13 = i12 + 1;
                int i14 = i + i11;
                fArr2[i12] = fArr[i14] * this.bk1[i12];
                fArr2[i13] = (-fArr[i14]) * this.bk1[i13];
            }
            cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            for (int i15 = 0; i15 < this.nBluestein; i15++) {
                int i16 = i15 * 2;
                int i17 = i16 + 1;
                float f = (fArr2[i16] * this.bk2[i17]) + (fArr2[i17] * this.bk2[i16]);
                fArr2[i16] = (fArr2[i16] * this.bk2[i16]) - (fArr2[i17] * this.bk2[i17]);
                fArr2[i17] = f;
            }
        }
        cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        if (this.n % 2 == 0) {
            fArr[i] = (this.bk1[0] * fArr2[0]) + (this.bk1[1] * fArr2[1]);
            fArr[i + 1] = (this.bk1[this.n] * fArr2[this.n]) + (this.bk1[this.n + 1] * fArr2[this.n + 1]);
            for (int i18 = 1; i18 < this.n / 2; i18++) {
                int i19 = i18 * 2;
                int i20 = i19 + 1;
                fArr[i + i19] = (this.bk1[i19] * fArr2[i19]) + (this.bk1[i20] * fArr2[i20]);
                fArr[i + i20] = ((-this.bk1[i20]) * fArr2[i19]) + (this.bk1[i19] * fArr2[i20]);
            }
            return;
        }
        fArr[i] = (this.bk1[0] * fArr2[0]) + (this.bk1[1] * fArr2[1]);
        fArr[i + 1] = ((-this.bk1[this.n]) * fArr2[this.n - 1]) + (this.bk1[this.n - 1] * fArr2[this.n]);
        for (int i21 = 1; i21 < (this.n - 1) / 2; i21++) {
            int i22 = i21 * 2;
            int i23 = i22 + 1;
            fArr[i + i22] = (this.bk1[i22] * fArr2[i22]) + (this.bk1[i23] * fArr2[i23]);
            fArr[i + i23] = ((-this.bk1[i23]) * fArr2[i22]) + (this.bk1[i22] * fArr2[i23]);
        }
        fArr[(i + this.n) - 1] = (this.bk1[this.n - 1] * fArr2[this.n - 1]) + (this.bk1[this.n] * fArr2[this.n]);
    }

    private void bluestein_real_inverse(final float[] fArr, final int i) throws ExecutionException, InterruptedException {
        final float[] fArr2 = new float[this.nBluestein * 2];
        int i2 = 0;
        if (this.n % 2 == 0) {
            fArr2[0] = fArr[i] * this.bk1[0];
            fArr2[1] = fArr[i] * this.bk1[1];
            for (int i3 = 1; i3 < this.n / 2; i3++) {
                int i4 = i3 * 2;
                int i5 = i4 + 1;
                int i6 = i + i4;
                int i7 = i + i5;
                fArr2[i4] = (fArr[i6] * this.bk1[i4]) - (fArr[i7] * this.bk1[i5]);
                fArr2[i5] = (fArr[i6] * this.bk1[i5]) + (fArr[i7] * this.bk1[i4]);
            }
            int i8 = i + 1;
            fArr2[this.n] = fArr[i8] * this.bk1[this.n];
            fArr2[this.n + 1] = fArr[i8] * this.bk1[this.n + 1];
            for (int i9 = (this.n / 2) + 1; i9 < this.n; i9++) {
                int i10 = i9 * 2;
                int i11 = i10 + 1;
                int i12 = (i + (this.n * 2)) - i10;
                int i13 = i12 + 1;
                fArr2[i10] = (fArr[i12] * this.bk1[i10]) + (fArr[i13] * this.bk1[i11]);
                fArr2[i11] = (fArr[i12] * this.bk1[i11]) - (fArr[i13] * this.bk1[i10]);
            }
        } else {
            fArr2[0] = fArr[i] * this.bk1[0];
            fArr2[1] = fArr[i] * this.bk1[1];
            for (int i14 = 1; i14 < (this.n - 1) / 2; i14++) {
                int i15 = i14 * 2;
                int i16 = i15 + 1;
                int i17 = i + i15;
                int i18 = i + i16;
                fArr2[i15] = (fArr[i17] * this.bk1[i15]) - (fArr[i18] * this.bk1[i16]);
                fArr2[i16] = (fArr[i17] * this.bk1[i16]) + (fArr[i18] * this.bk1[i15]);
            }
            int i19 = i + 1;
            fArr2[this.n - 1] = (fArr[(i + this.n) - 1] * this.bk1[this.n - 1]) - (fArr[i19] * this.bk1[this.n]);
            fArr2[this.n] = (fArr[(i + this.n) - 1] * this.bk1[this.n]) + (fArr[i19] * this.bk1[this.n - 1]);
            fArr2[this.n + 1] = (fArr[(i + this.n) - 1] * this.bk1[this.n + 1]) + (fArr[i19] * this.bk1[this.n + 2]);
            fArr2[this.n + 2] = (fArr[(i + this.n) - 1] * this.bk1[this.n + 2]) - (fArr[i19] * this.bk1[this.n + 1]);
            for (int i20 = ((this.n - 1) / 2) + 2; i20 < this.n; i20++) {
                int i21 = i20 * 2;
                int i22 = i21 + 1;
                int i23 = (i + (this.n * 2)) - i21;
                int i24 = i23 + 1;
                fArr2[i21] = (fArr[i23] * this.bk1[i21]) + (fArr[i24] * this.bk1[i22]);
                fArr2[i22] = (fArr[i23] * this.bk1[i22]) - (fArr[i24] * this.bk1[i21]);
            }
        }
        cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        if (ConcurrencyUtils.getNumberOfThreads() > 1 && this.n > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            Future[] futureArr = new Future[2];
            int i25 = this.nBluestein / 2;
            int i26 = 0;
            while (i26 < 2) {
                final int i27 = i26 * i25;
                final int i28 = i26 == 1 ? this.nBluestein : i27 + i25;
                futureArr[i26] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.11
                    @Override // java.lang.Runnable
                    public void run() {
                        for (int i29 = i27; i29 < i28; i29++) {
                            int i30 = i29 * 2;
                            int i31 = i30 + 1;
                            float f = ((-fArr2[i30]) * FloatFFT.this.bk2[i31]) + (fArr2[i31] * FloatFFT.this.bk2[i30]);
                            fArr2[i30] = (fArr2[i30] * FloatFFT.this.bk2[i30]) + (fArr2[i31] * FloatFFT.this.bk2[i31]);
                            fArr2[i31] = f;
                        }
                    }
                });
                i26++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr);
            cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            int i29 = this.n / 2;
            while (i2 < 2) {
                final int i30 = i2 * i29;
                final int i31 = i2 == 1 ? this.n : i30 + i29;
                futureArr[i2] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.12
                    @Override // java.lang.Runnable
                    public void run() {
                        for (int i32 = i30; i32 < i31; i32++) {
                            int i33 = i32 * 2;
                            int i34 = i33 + 1;
                            fArr[i + i32] = (FloatFFT.this.bk1[i33] * fArr2[i33]) - (FloatFFT.this.bk1[i34] * fArr2[i34]);
                        }
                    }
                });
                i2++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr);
            return;
        }
        for (int i32 = 0; i32 < this.nBluestein; i32++) {
            int i33 = i32 * 2;
            int i34 = i33 + 1;
            float f = ((-fArr2[i33]) * this.bk2[i34]) + (fArr2[i34] * this.bk2[i33]);
            fArr2[i33] = (fArr2[i33] * this.bk2[i33]) + (fArr2[i34] * this.bk2[i34]);
            fArr2[i34] = f;
        }
        cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        while (i2 < this.n) {
            int i35 = i2 * 2;
            int i36 = i35 + 1;
            fArr[i + i2] = (this.bk1[i35] * fArr2[i35]) - (this.bk1[i36] * fArr2[i36]);
            i2++;
        }
    }

    private void bluestein_real_inverse2(final float[] fArr, final int i) throws ExecutionException, InterruptedException {
        final float[] fArr2 = new float[this.nBluestein * 2];
        if (ConcurrencyUtils.getNumberOfThreads() > 1 && this.n > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            Future[] futureArr = new Future[2];
            int i2 = this.n / 2;
            int i3 = 0;
            while (i3 < 2) {
                final int i4 = i3 * i2;
                final int i5 = i3 == 1 ? this.n : i4 + i2;
                int i6 = i3;
                futureArr[i6] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.13
                    @Override // java.lang.Runnable
                    public void run() {
                        for (int i7 = i4; i7 < i5; i7++) {
                            int i8 = i7 * 2;
                            int i9 = i8 + 1;
                            int i10 = i + i7;
                            fArr2[i8] = fArr[i10] * FloatFFT.this.bk1[i8];
                            fArr2[i9] = fArr[i10] * FloatFFT.this.bk1[i9];
                        }
                    }
                });
                i3 = i6 + 1;
            }
            ConcurrencyUtils.waitForCompletion(futureArr);
            cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            int i7 = this.nBluestein / 2;
            int i8 = 0;
            while (i8 < 2) {
                final int i9 = i8 * i7;
                final int i10 = i8 == 1 ? this.nBluestein : i9 + i7;
                futureArr[i8] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.14
                    @Override // java.lang.Runnable
                    public void run() {
                        for (int i11 = i9; i11 < i10; i11++) {
                            int i12 = i11 * 2;
                            int i13 = i12 + 1;
                            float f = ((-fArr2[i12]) * FloatFFT.this.bk2[i13]) + (fArr2[i13] * FloatFFT.this.bk2[i12]);
                            fArr2[i12] = (fArr2[i12] * FloatFFT.this.bk2[i12]) + (fArr2[i13] * FloatFFT.this.bk2[i13]);
                            fArr2[i13] = f;
                        }
                    }
                });
                i8++;
            }
            ConcurrencyUtils.waitForCompletion(futureArr);
        } else {
            for (int i11 = 0; i11 < this.n; i11++) {
                int i12 = i11 * 2;
                int i13 = i12 + 1;
                int i14 = i + i11;
                fArr2[i12] = fArr[i14] * this.bk1[i12];
                fArr2[i13] = fArr[i14] * this.bk1[i13];
            }
            cftbsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
            for (int i15 = 0; i15 < this.nBluestein; i15++) {
                int i16 = i15 * 2;
                int i17 = i16 + 1;
                float f = ((-fArr2[i16]) * this.bk2[i17]) + (fArr2[i17] * this.bk2[i16]);
                fArr2[i16] = (fArr2[i16] * this.bk2[i16]) + (fArr2[i17] * this.bk2[i17]);
                fArr2[i17] = f;
            }
        }
        cftfsub(this.nBluestein * 2, fArr2, 0, this.ip, this.nw, this.w);
        if (this.n % 2 == 0) {
            fArr[i] = (this.bk1[0] * fArr2[0]) - (this.bk1[1] * fArr2[1]);
            fArr[i + 1] = (this.bk1[this.n] * fArr2[this.n]) - (this.bk1[this.n + 1] * fArr2[this.n + 1]);
            for (int i18 = 1; i18 < this.n / 2; i18++) {
                int i19 = i18 * 2;
                int i20 = i19 + 1;
                fArr[i + i19] = (this.bk1[i19] * fArr2[i19]) - (this.bk1[i20] * fArr2[i20]);
                fArr[i + i20] = (this.bk1[i20] * fArr2[i19]) + (this.bk1[i19] * fArr2[i20]);
            }
            return;
        }
        fArr[i] = (this.bk1[0] * fArr2[0]) - (this.bk1[1] * fArr2[1]);
        fArr[i + 1] = (this.bk1[this.n] * fArr2[this.n - 1]) + (this.bk1[this.n - 1] * fArr2[this.n]);
        for (int i21 = 1; i21 < (this.n - 1) / 2; i21++) {
            int i22 = i21 * 2;
            int i23 = i22 + 1;
            fArr[i + i22] = (this.bk1[i22] * fArr2[i22]) - (this.bk1[i23] * fArr2[i23]);
            fArr[i + i23] = (this.bk1[i23] * fArr2[i22]) + (this.bk1[i22] * fArr2[i23]);
        }
        fArr[(i + this.n) - 1] = (this.bk1[this.n - 1] * fArr2[this.n - 1]) - (this.bk1[this.n] * fArr2[this.n]);
    }

    void rfftf(float[] fArr, int i) {
        if (this.n == 1) {
            return;
        }
        float[] fArr2 = new float[this.n];
        int i2 = this.n * 2;
        int i3 = (int) this.wtable_r[i2 + 1];
        int i4 = this.n;
        int i5 = i2 - 1;
        int i6 = 1;
        int i7 = 1;
        while (i7 <= i3) {
            int i8 = (int) this.wtable_r[(i3 - i7) + 2 + i2];
            int i9 = i4 / i8;
            int i10 = this.n / i4;
            int i11 = i10 * i9;
            int i12 = i5 - ((i8 - 1) * i10);
            int i13 = 1 - i6;
            switch (i8) {
                case 2:
                    if (i13 == 0) {
                        radf2(i10, i9, fArr, i, fArr2, 0, i12);
                        break;
                    } else {
                        radf2(i10, i9, fArr2, 0, fArr, i, i12);
                        break;
                    }
                case 3:
                    if (i13 == 0) {
                        radf3(i10, i9, fArr, i, fArr2, 0, i12);
                        break;
                    } else {
                        radf3(i10, i9, fArr2, 0, fArr, i, i12);
                        break;
                    }
                case 4:
                    if (i13 == 0) {
                        radf4(i10, i9, fArr, i, fArr2, 0, i12);
                        break;
                    } else {
                        radf4(i10, i9, fArr2, 0, fArr, i, i12);
                        break;
                    }
                case 5:
                    if (i13 == 0) {
                        radf5(i10, i9, fArr, i, fArr2, 0, i12);
                        break;
                    } else {
                        radf5(i10, i9, fArr2, 0, fArr, i, i12);
                        break;
                    }
                default:
                    if (i10 == 1) {
                        i13 = 1 - i13;
                    }
                    if (i13 == 0) {
                        radfg(i10, i8, i9, i11, fArr, i, fArr2, 0, i12);
                        i6 = 1;
                    } else {
                        radfg(i10, i8, i9, i11, fArr2, 0, fArr, i, i12);
                        i6 = 0;
                        continue;
                    }
                    i7++;
                    i4 = i9;
                    i5 = i12;
            }
            i6 = i13;
            i7++;
            i4 = i9;
            i5 = i12;
        }
        if (i6 == 1) {
            return;
        }
        System.arraycopy(fArr2, 0, fArr, i, this.n);
    }

    void rfftb(float[] fArr, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (this.n == 1) {
            return;
        }
        float[] fArr2 = new float[this.n];
        int i6 = this.n * 2;
        int i7 = (int) this.wtable_r[i6 + 1];
        int i8 = this.n;
        int i9 = 1;
        int i10 = 1;
        int i11 = 0;
        while (i9 <= i7) {
            int i12 = i9 + 1;
            int i13 = (int) this.wtable_r[i12 + i6];
            int i14 = i13 * i10;
            int i15 = this.n / i14;
            int i16 = i15 * i10;
            switch (i13) {
                case 2:
                    if (i11 == 0) {
                        radb2(i15, i10, fArr, i, fArr2, 0, i8);
                    } else {
                        radb2(i15, i10, fArr2, 0, fArr, i, i8);
                    }
                    i2 = 1 - i11;
                    break;
                case 3:
                    if (i11 == 0) {
                        radb3(i15, i10, fArr, i, fArr2, 0, i8);
                    } else {
                        radb3(i15, i10, fArr2, 0, fArr, i, i8);
                    }
                    i2 = 1 - i11;
                    break;
                case 4:
                    if (i11 == 0) {
                        radb4(i15, i10, fArr, i, fArr2, 0, i8);
                    } else {
                        radb4(i15, i10, fArr2, 0, fArr, i, i8);
                    }
                    i2 = 1 - i11;
                    break;
                case 5:
                    if (i11 == 0) {
                        radb5(i15, i10, fArr, i, fArr2, 0, i8);
                    } else {
                        radb5(i15, i10, fArr2, 0, fArr, i, i8);
                    }
                    i2 = 1 - i11;
                    break;
                default:
                    if (i11 == 0) {
                        i5 = i15;
                        i4 = i13;
                        radbg(i15, i13, i10, i16, fArr, i, fArr2, 0, i8);
                    } else {
                        i5 = i15;
                        i4 = i13;
                        radbg(i5, i4, i10, i16, fArr2, 0, fArr, i, i8);
                    }
                    i3 = i5;
                    if (i3 == 1) {
                        i11 = 1 - i11;
                        continue;
                    }
                    i8 += (i4 - 1) * i3;
                    i9 = i12;
                    i10 = i14;
            }
            i11 = i2;
            i3 = i15;
            i4 = i13;
            i8 += (i4 - 1) * i3;
            i9 = i12;
            i10 = i14;
        }
        if (i11 == 0) {
            return;
        }
        System.arraycopy(fArr2, 0, fArr, i, this.n);
    }

    void radf2(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6;
        int i7 = i2 * i;
        int i8 = i * 2;
        int i9 = 0;
        while (true) {
            i6 = 1;
            if (i9 >= i2) {
                break;
            }
            int i10 = i4 + (i9 * i8);
            int i11 = i3 + (i9 * i);
            int i12 = i11 + i7;
            float f = fArr[i11];
            float f2 = fArr[i12];
            fArr2[i10] = f + f2;
            fArr2[(i10 + i8) - 1] = f - f2;
            i9++;
        }
        if (i < 2) {
            return;
        }
        if (i != 2) {
            int i13 = i8;
            for (int i14 = 0; i14 < i2; i14++) {
                i13 = i14 * i;
                int i15 = i13 * 2;
                int i16 = i15 + i;
                int i17 = i13 + i7;
                for (int i18 = 2; i18 < i; i18 += 2) {
                    int i19 = (i18 - 1) + i5;
                    int i20 = i4 + i18 + i15;
                    int i21 = i4 + (i - i18) + i16;
                    int i22 = i3 + i18;
                    int i23 = i22 + i13;
                    int i24 = i22 + i17;
                    float f3 = fArr[i23 - 1];
                    float f4 = fArr[i23];
                    float f5 = fArr[i24 - 1];
                    float f6 = fArr[i24];
                    float f7 = this.wtable_r[i19 - 1];
                    float f8 = this.wtable_r[i19];
                    float f9 = (f7 * f5) + (f8 * f6);
                    float f10 = (f7 * f6) - (f8 * f5);
                    fArr2[i20] = f4 + f10;
                    i6 = 1;
                    fArr2[i20 - 1] = f3 + f9;
                    fArr2[i21] = f10 - f4;
                    fArr2[i21 - 1] = f3 - f9;
                }
            }
            if (i % 2 == i6) {
                return;
            } else {
                i8 = i13;
            }
        }
        int i25 = i8 * 2;
        for (int i26 = 0; i26 < i2; i26++) {
            int i27 = i4 + i25 + i;
            int i28 = ((i3 + i) - i6) + (i26 * i);
            fArr2[i27] = -fArr[i28 + i7];
            fArr2[i27 - i6] = fArr[i28];
        }
    }

    void radb2(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6;
        int i7 = i2 * i;
        for (int i8 = 0; i8 < i2; i8++) {
            int i9 = i8 * i;
            int i10 = i9 * 2;
            int i11 = i4 + i9;
            float f = fArr[i3 + i10];
            float f2 = fArr[((i3 + i) - 1) + i10 + i];
            fArr2[i11] = f + f2;
            fArr2[i11 + i7] = f - f2;
        }
        if (i < 2) {
            return;
        }
        if (i != 2) {
            for (int i12 = 0; i12 < i2; i12++) {
                int i13 = i12 * i;
                int i14 = i13 * 2;
                int i15 = i14 + i;
                int i16 = i13 + i7;
                for (int i17 = 2; i17 < i; i17 += 2) {
                    int i18 = (i17 - 1) + i5;
                    int i19 = i4 + i17;
                    float f3 = this.wtable_r[i18 - 1];
                    float f4 = this.wtable_r[i18];
                    int i20 = i3 + i17 + i14;
                    int i21 = i3 + (i - i17) + i15;
                    int i22 = i19 + i13;
                    int i23 = i19 + i16;
                    int i24 = i20 - 1;
                    int i25 = i21 - 1;
                    float f5 = fArr[i24] - fArr[i25];
                    float f6 = fArr[i20] + fArr[i21];
                    float f7 = fArr[i20];
                    float f8 = fArr[i24];
                    float f9 = fArr[i21];
                    fArr2[i22 - 1] = f8 + fArr[i25];
                    fArr2[i22] = f7 - f9;
                    fArr2[i23 - 1] = (f3 * f5) - (f4 * f6);
                    fArr2[i23] = (f3 * f6) + (f4 * f5);
                }
            }
            i6 = 1;
            if (i % 2 == 1) {
                return;
            }
        } else {
            i6 = 1;
        }
        for (int i26 = 0; i26 < i2; i26++) {
            int i27 = i26 * i;
            int i28 = ((i4 + i) - i6) + i27;
            int i29 = i3 + (i27 * 2) + i;
            fArr2[i28] = fArr[i29 - 1] * 2.0f;
            fArr2[i28 + i7] = fArr[i29] * (-2.0f);
        }
    }

    void radf3(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6 = i;
        int i7 = i2;
        int i8 = i5 + i6;
        int i9 = i7 * i6;
        int i10 = 0;
        for (int i11 = 0; i11 < i7; i11++) {
            int i12 = i11 * i6;
            int i13 = ((i11 * 3) + 1) * i6;
            int i14 = i3 + i12;
            int i15 = i14 + i9;
            int i16 = (i9 * 2) + i14;
            float f = fArr[i14];
            float f2 = fArr[i15];
            float f3 = fArr[i16];
            float f4 = f2 + f3;
            fArr2[i4 + (i12 * 3)] = f + f4;
            fArr2[i4 + i13 + i6] = (f3 - f2) * 0.8660254f;
            fArr2[((i4 + i6) - 1) + i13] = f + (f4 * (-0.5f));
        }
        if (i6 == 1) {
            return;
        }
        while (i10 < i7) {
            int i17 = i10 * i6;
            int i18 = i17 * 3;
            int i19 = i17 + i9;
            int i20 = i19 + i9;
            int i21 = i18 + i6;
            int i22 = i21 + i6;
            int i23 = 2;
            while (i23 < i6) {
                int i24 = i6 - i23;
                int i25 = i23 - 1;
                int i26 = i25 + i5;
                int i27 = i25 + i8;
                float f5 = this.wtable_r[i26 - 1];
                float f6 = this.wtable_r[i26];
                float f7 = this.wtable_r[i27 - 1];
                float f8 = this.wtable_r[i27];
                int i28 = i3 + i23;
                int i29 = i4 + i23;
                int i30 = i28 + i17;
                int i31 = i28 + i19;
                int i32 = i28 + i20;
                float f9 = fArr[i30 - 1];
                float f10 = fArr[i30];
                float f11 = fArr[i31 - 1];
                float f12 = fArr[i31];
                float f13 = fArr[i32 - 1];
                float f14 = fArr[i32];
                float f15 = (f5 * f11) + (f6 * f12);
                float f16 = (f5 * f12) - (f6 * f11);
                float f17 = (f7 * f13) + (f8 * f14);
                float f18 = (f7 * f14) - (f8 * f13);
                float f19 = f15 + f17;
                float f20 = f16 + f18;
                float f21 = f9 + (f19 * (-0.5f));
                float f22 = f10 + (f20 * (-0.5f));
                float f23 = (f16 - f18) * 0.8660254f;
                float f24 = (f17 - f15) * 0.8660254f;
                int i33 = i29 + i18;
                int i34 = i4 + i24 + i21;
                int i35 = i29 + i22;
                fArr2[i33 - 1] = f9 + f19;
                fArr2[i33] = f10 + f20;
                fArr2[i34 - 1] = f21 - f23;
                fArr2[i34] = f24 - f22;
                fArr2[i35 - 1] = f21 + f23;
                fArr2[i35] = f22 + f24;
                i23 += 2;
                i6 = i;
            }
            i10++;
            i6 = i;
            i7 = i2;
        }
    }

    void radb3(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6 = i;
        int i7 = i2;
        int i8 = i5 + i6;
        int i9 = 0;
        for (int i10 = 0; i10 < i7; i10++) {
            int i11 = i10 * i6;
            int i12 = i3 + (i11 * 3);
            int i13 = (i6 * 2) + i12;
            float f = fArr[i12];
            float f2 = fArr[i13 - 1] * 2.0f;
            float f3 = ((-0.5f) * f2) + f;
            float f4 = fArr[i13] * 1.7320508f;
            fArr2[i4 + i11] = f + f2;
            fArr2[i4 + ((i10 + i7) * i6)] = f3 - f4;
            fArr2[i4 + (((i7 * 2) + i10) * i6)] = f3 + f4;
        }
        if (i6 == 1) {
            return;
        }
        int i14 = i7 * i6;
        while (i9 < i7) {
            int i15 = i9 * i6;
            int i16 = i15 * 3;
            int i17 = i16 + i6;
            int i18 = i17 + i6;
            int i19 = i15 + i14;
            int i20 = i19 + i14;
            int i21 = 2;
            while (i21 < i6) {
                int i22 = i3 + i21;
                int i23 = i4 + i21;
                int i24 = i22 + i16;
                int i25 = i22 + i18;
                int i26 = i3 + (i6 - i21) + i17;
                float f5 = fArr[i24 - 1];
                float f6 = fArr[i24];
                float f7 = fArr[i25 - 1];
                float f8 = fArr[i25];
                float f9 = fArr[i26 - 1];
                float f10 = fArr[i26];
                float f11 = f7 + f9;
                float f12 = f5 + (f11 * (-0.5f));
                float f13 = f8 - f10;
                float f14 = f6 + (f13 * (-0.5f));
                float f15 = (f7 - f9) * 0.8660254f;
                float f16 = (f8 + f10) * 0.8660254f;
                float f17 = f12 - f16;
                float f18 = f12 + f16;
                float f19 = f14 + f15;
                float f20 = f14 - f15;
                int i27 = i21 - 1;
                int i28 = i27 + i5;
                int i29 = i27 + i8;
                float f21 = this.wtable_r[i28 - 1];
                float f22 = this.wtable_r[i28];
                float f23 = this.wtable_r[i29 - 1];
                float f24 = this.wtable_r[i29];
                int i30 = i23 + i15;
                int i31 = i23 + i19;
                int i32 = i23 + i20;
                fArr2[i30 - 1] = f5 + f11;
                fArr2[i30] = f6 + f13;
                fArr2[i31 - 1] = (f21 * f17) - (f22 * f19);
                fArr2[i31] = (f21 * f19) + (f22 * f17);
                fArr2[i32 - 1] = (f23 * f18) - (f24 * f20);
                fArr2[i32] = (f23 * f20) + (f24 * f18);
                i21 += 2;
                i6 = i;
            }
            i9++;
            i6 = i;
            i7 = i2;
        }
    }

    void radf4(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6;
        int i7 = i5 + i;
        int i8 = i7 + i;
        int i9 = i2 * i;
        for (int i10 = 0; i10 < i2; i10++) {
            int i11 = i10 * i;
            int i12 = i11 * 4;
            int i13 = i11 + i9;
            int i14 = i13 + i9;
            int i15 = i14 + i9;
            float f = fArr[i3 + i11];
            float f2 = fArr[i3 + i13];
            float f3 = fArr[i3 + i14];
            float f4 = fArr[i3 + i15];
            float f5 = f2 + f4;
            float f6 = f + f3;
            int i16 = i4 + i12 + i + i;
            fArr2[i4 + i12] = f5 + f6;
            int i17 = i16 - 1;
            fArr2[i17 + i + i] = f6 - f5;
            fArr2[i17] = f - f3;
            fArr2[i16] = f4 - f2;
        }
        if (i < 2) {
            return;
        }
        if (i != 2) {
            for (int i18 = 0; i18 < i2; i18++) {
                int i19 = i18 * i;
                int i20 = i19 + i9;
                int i21 = i20 + i9;
                int i22 = i21 + i9;
                int i23 = i19 * 4;
                int i24 = i23 + i;
                int i25 = i24 + i;
                int i26 = i25 + i;
                int i27 = 2;
                while (i27 < i) {
                    int i28 = i27 - 1;
                    int i29 = i28 + i5;
                    int i30 = i28 + i7;
                    int i31 = i28 + i8;
                    float f7 = this.wtable_r[i29 - 1];
                    float f8 = this.wtable_r[i29];
                    float f9 = this.wtable_r[i30 - 1];
                    int i32 = i7;
                    float f10 = this.wtable_r[i30];
                    int i33 = i8;
                    float f11 = this.wtable_r[i31 - 1];
                    int i34 = i9;
                    float f12 = this.wtable_r[i31];
                    int i35 = i3 + i27;
                    int i36 = i4 + i27;
                    int i37 = i4 + (i - i27);
                    int i38 = i35 + i19;
                    int i39 = i35 + i20;
                    int i40 = i35 + i21;
                    int i41 = i35 + i22;
                    float f13 = fArr[i38 - 1];
                    float f14 = fArr[i38];
                    float f15 = fArr[i39 - 1];
                    float f16 = fArr[i39];
                    float f17 = fArr[i40 - 1];
                    float f18 = fArr[i40];
                    float f19 = fArr[i41 - 1];
                    float f20 = fArr[i41];
                    float f21 = (f7 * f15) + (f8 * f16);
                    float f22 = (f7 * f16) - (f8 * f15);
                    float f23 = (f9 * f17) + (f10 * f18);
                    float f24 = (f9 * f18) - (f10 * f17);
                    float f25 = (f11 * f19) + (f12 * f20);
                    float f26 = (f11 * f20) - (f12 * f19);
                    float f27 = f21 + f25;
                    float f28 = f25 - f21;
                    float f29 = f22 + f26;
                    float f30 = f22 - f26;
                    float f31 = f14 + f24;
                    float f32 = f14 - f24;
                    float f33 = f13 + f23;
                    float f34 = f13 - f23;
                    int i42 = i36 + i23;
                    int i43 = i37 + i24;
                    int i44 = i36 + i25;
                    int i45 = i37 + i26;
                    fArr2[i42 - 1] = f27 + f33;
                    fArr2[i45 - 1] = f33 - f27;
                    fArr2[i42] = f29 + f31;
                    fArr2[i45] = f29 - f31;
                    fArr2[i44 - 1] = f30 + f34;
                    fArr2[i43 - 1] = f34 - f30;
                    fArr2[i44] = f28 + f32;
                    fArr2[i43] = f28 - f32;
                    i27 += 2;
                    i7 = i32;
                    i8 = i33;
                    i9 = i34;
                }
            }
            i6 = i9;
            if (i % 2 == 1) {
                return;
            }
        } else {
            i6 = i9;
        }
        for (int i46 = 0; i46 < i2; i46++) {
            int i47 = i46 * i;
            int i48 = i47 * 4;
            int i49 = i47 + i6;
            int i50 = i49 + i6;
            int i51 = i50 + i6;
            int i52 = i48 + i;
            int i53 = i52 + i;
            int i54 = (i3 + i) - 1;
            float f35 = fArr[i47 + i54];
            float f36 = fArr[i49 + i54];
            float f37 = fArr[i50 + i54];
            float f38 = fArr[i54 + i51];
            float f39 = (f36 + f38) * (-0.70710677f);
            float f40 = (f36 - f38) * 0.70710677f;
            int i55 = (i4 + i) - 1;
            fArr2[i48 + i55] = f40 + f35;
            fArr2[i55 + i53] = f35 - f40;
            fArr2[i4 + i52] = f39 - f37;
            fArr2[i4 + i53 + i] = f39 + f37;
        }
    }

    void radb4(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6;
        int i7 = i5 + i;
        int i8 = i7 + i;
        int i9 = i2 * i;
        for (int i10 = 0; i10 < i2; i10++) {
            int i11 = i10 * i;
            int i12 = i11 * 4;
            int i13 = i11 + i9;
            int i14 = i13 + i9;
            int i15 = i12 + i;
            int i16 = i15 + i;
            int i17 = i16 + i;
            float f = fArr[i3 + i12];
            float f2 = fArr[i3 + i16];
            int i18 = (i3 + i) - 1;
            float f3 = fArr[i18 + i17];
            float f4 = fArr[i18 + i15];
            float f5 = f - f3;
            float f6 = f + f3;
            float f7 = f4 + f4;
            float f8 = f2 + f2;
            fArr2[i4 + i11] = f6 + f7;
            fArr2[i4 + i13] = f5 - f8;
            fArr2[i4 + i14] = f6 - f7;
            fArr2[i4 + i14 + i9] = f5 + f8;
        }
        if (i < 2) {
            return;
        }
        if (i != 2) {
            for (int i19 = 0; i19 < i2; i19++) {
                int i20 = i19 * i;
                int i21 = i20 + i9;
                int i22 = i21 + i9;
                int i23 = i22 + i9;
                int i24 = i20 * 4;
                int i25 = i24 + i;
                int i26 = i25 + i;
                int i27 = i26 + i;
                int i28 = 2;
                while (i28 < i) {
                    int i29 = i28 - 1;
                    int i30 = i29 + i5;
                    int i31 = i29 + i7;
                    int i32 = i29 + i8;
                    float f9 = this.wtable_r[i30 - 1];
                    float f10 = this.wtable_r[i30];
                    float f11 = this.wtable_r[i31 - 1];
                    int i33 = i7;
                    float f12 = this.wtable_r[i31];
                    int i34 = i8;
                    float f13 = this.wtable_r[i32 - 1];
                    int i35 = i9;
                    float f14 = this.wtable_r[i32];
                    int i36 = i3 + i28;
                    int i37 = i3 + (i - i28);
                    int i38 = i4 + i28;
                    int i39 = i36 + i24;
                    int i40 = i37 + i25;
                    int i41 = i36 + i26;
                    int i42 = i37 + i27;
                    float f15 = fArr[i39 - 1];
                    float f16 = fArr[i39];
                    float f17 = fArr[i40 - 1];
                    float f18 = fArr[i40];
                    float f19 = fArr[i41 - 1];
                    float f20 = fArr[i41];
                    float f21 = fArr[i42 - 1];
                    float f22 = fArr[i42];
                    float f23 = f16 + f22;
                    float f24 = f16 - f22;
                    float f25 = f20 - f18;
                    float f26 = f20 + f18;
                    float f27 = f15 - f21;
                    float f28 = f15 + f21;
                    float f29 = f19 - f17;
                    float f30 = f19 + f17;
                    float f31 = f28 - f30;
                    float f32 = f24 - f25;
                    float f33 = f27 - f26;
                    float f34 = f27 + f26;
                    float f35 = f23 + f29;
                    float f36 = f23 - f29;
                    int i43 = i38 + i20;
                    int i44 = i38 + i21;
                    int i45 = i38 + i22;
                    int i46 = i38 + i23;
                    fArr2[i43 - 1] = f28 + f30;
                    fArr2[i43] = f24 + f25;
                    fArr2[i44 - 1] = (f9 * f33) - (f10 * f35);
                    fArr2[i44] = (f9 * f35) + (f10 * f33);
                    fArr2[i45 - 1] = (f11 * f31) - (f12 * f32);
                    fArr2[i45] = (f11 * f32) + (f12 * f31);
                    fArr2[i46 - 1] = (f13 * f34) - (f14 * f36);
                    fArr2[i46] = (f13 * f36) + (f14 * f34);
                    i28 += 2;
                    i7 = i33;
                    i8 = i34;
                    i9 = i35;
                }
            }
            i6 = i9;
            if (i % 2 == 1) {
                return;
            }
        } else {
            i6 = i9;
        }
        for (int i47 = 0; i47 < i2; i47++) {
            int i48 = i47 * i;
            int i49 = i48 * 4;
            int i50 = i48 + i6;
            int i51 = i50 + i6;
            int i52 = i49 + i;
            int i53 = i52 + i;
            int i54 = i53 + i;
            int i55 = (i3 + i) - 1;
            float f37 = fArr[i49 + i55];
            float f38 = fArr[i55 + i53];
            float f39 = fArr[i3 + i52];
            float f40 = fArr[i3 + i54];
            float f41 = f39 + f40;
            float f42 = f40 - f39;
            float f43 = f37 - f38;
            float f44 = f37 + f38;
            int i56 = (i4 + i) - 1;
            fArr2[i48 + i56] = f44 + f44;
            fArr2[i50 + i56] = (f43 - f41) * 1.4142135f;
            fArr2[i51 + i56] = f42 + f42;
            fArr2[i56 + i51 + i6] = (f43 + f41) * (-1.4142135f);
        }
    }

    void radf5(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6 = i2;
        int i7 = i5 + i;
        int i8 = i7 + i;
        int i9 = i8 + i;
        int i10 = i6 * i;
        for (int i11 = 0; i11 < i6; i11++) {
            int i12 = i11 * i;
            int i13 = i12 * 5;
            int i14 = i13 + i;
            int i15 = i14 + i;
            int i16 = i15 + i;
            int i17 = i12 + i10;
            int i18 = i17 + i10;
            int i19 = i18 + i10;
            int i20 = (i4 + i) - 1;
            float f = fArr[i3 + i12];
            float f2 = fArr[i3 + i17];
            float f3 = fArr[i3 + i18];
            float f4 = fArr[i3 + i19];
            float f5 = fArr[i3 + i19 + i10];
            float f6 = f5 + f2;
            float f7 = f5 - f2;
            float f8 = f4 + f3;
            float f9 = f4 - f3;
            fArr2[i4 + i13] = f + f6 + f8;
            fArr2[i20 + i14] = f + (f6 * 0.309017f) + (f8 * (-0.809017f));
            fArr2[i4 + i15] = (f7 * 0.95105654f) + (f9 * 0.58778524f);
            fArr2[i20 + i16] = f + (f6 * (-0.809017f)) + (f8 * 0.309017f);
            fArr2[i4 + i16 + i] = (f7 * 0.58778524f) - (f9 * 0.95105654f);
        }
        if (i == 1) {
            return;
        }
        int i21 = 0;
        while (i21 < i6) {
            int i22 = i21 * i;
            int i23 = i22 * 5;
            int i24 = i23 + i;
            int i25 = i24 + i;
            int i26 = i25 + i;
            int i27 = i26 + i;
            int i28 = i22 + i10;
            int i29 = i28 + i10;
            int i30 = i29 + i10;
            int i31 = i30 + i10;
            int i32 = 2;
            while (i32 < i) {
                int i33 = i32 - 1;
                int i34 = i33 + i5;
                int i35 = i33 + i7;
                int i36 = i33 + i8;
                int i37 = i33 + i9;
                float f10 = this.wtable_r[i34 - 1];
                float f11 = this.wtable_r[i34];
                float f12 = this.wtable_r[i35 - 1];
                int i38 = i7;
                float f13 = this.wtable_r[i35];
                int i39 = i8;
                float f14 = this.wtable_r[i36 - 1];
                int i40 = i9;
                float f15 = this.wtable_r[i36];
                int i41 = i10;
                float f16 = this.wtable_r[i37 - 1];
                int i42 = i21;
                float f17 = this.wtable_r[i37];
                int i43 = i3 + i32;
                int i44 = i4 + i32;
                int i45 = i4 + (i - i32);
                int i46 = i43 + i22;
                int i47 = i43 + i28;
                int i48 = i43 + i29;
                int i49 = i43 + i30;
                int i50 = i43 + i31;
                float f18 = fArr[i46 - 1];
                float f19 = fArr[i46];
                float f20 = fArr[i47 - 1];
                float f21 = fArr[i47];
                float f22 = fArr[i48 - 1];
                float f23 = fArr[i48];
                float f24 = fArr[i49 - 1];
                float f25 = fArr[i49];
                float f26 = fArr[i50 - 1];
                float f27 = fArr[i50];
                float f28 = (f10 * f20) + (f11 * f21);
                float f29 = (f10 * f21) - (f11 * f20);
                float f30 = (f12 * f22) + (f13 * f23);
                float f31 = (f12 * f23) - (f13 * f22);
                float f32 = (f14 * f24) + (f15 * f25);
                float f33 = (f14 * f25) - (f15 * f24);
                float f34 = (f16 * f26) + (f17 * f27);
                float f35 = (f16 * f27) - (f17 * f26);
                float f36 = f28 + f34;
                float f37 = f34 - f28;
                float f38 = f29 - f35;
                float f39 = f29 + f35;
                float f40 = f30 + f32;
                float f41 = f32 - f30;
                float f42 = f31 - f33;
                float f43 = f31 + f33;
                float f44 = f18 + (f36 * 0.309017f) + (f40 * (-0.809017f));
                float f45 = f19 + (f39 * 0.309017f) + (f43 * (-0.809017f));
                float f46 = f18 + (f36 * (-0.809017f)) + (f40 * 0.309017f);
                float f47 = f19 + (f39 * (-0.809017f)) + (f43 * 0.309017f);
                float f48 = (f38 * 0.95105654f) + (f42 * 0.58778524f);
                float f49 = (f37 * 0.95105654f) + (f41 * 0.58778524f);
                float f50 = (f38 * 0.58778524f) - (f42 * 0.95105654f);
                float f51 = (f37 * 0.58778524f) - (f41 * 0.95105654f);
                int i51 = i44 + i23;
                int i52 = i45 + i24;
                int i53 = i44 + i25;
                int i54 = i45 + i26;
                int i55 = i44 + i27;
                fArr2[i51 - 1] = f18 + f36 + f40;
                fArr2[i51] = f19 + f39 + f43;
                fArr2[i53 - 1] = f44 + f48;
                fArr2[i52 - 1] = f44 - f48;
                fArr2[i53] = f45 + f49;
                fArr2[i52] = f49 - f45;
                fArr2[i55 - 1] = f46 + f50;
                fArr2[i54 - 1] = f46 - f50;
                fArr2[i55] = f47 + f51;
                fArr2[i54] = f51 - f47;
                i32 += 2;
                i7 = i38;
                i8 = i39;
                i9 = i40;
                i10 = i41;
                i21 = i42;
            }
            i21++;
            i6 = i2;
        }
    }

    void radb5(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5) {
        int i6 = i;
        int i7 = i2;
        int i8 = i5 + i6;
        int i9 = i8 + i6;
        int i10 = i9 + i6;
        int i11 = i7 * i6;
        for (int i12 = 0; i12 < i7; i12++) {
            int i13 = i12 * i6;
            int i14 = i13 * 5;
            int i15 = i14 + i6;
            int i16 = i15 + i6;
            int i17 = i16 + i6;
            int i18 = i13 + i11;
            int i19 = i18 + i11;
            int i20 = i19 + i11;
            int i21 = (i3 + i6) - 1;
            float f = fArr[i3 + i14];
            float f2 = fArr[i3 + i16] * 2.0f;
            float f3 = fArr[i3 + i17 + i6] * 2.0f;
            float f4 = fArr[i21 + i15] * 2.0f;
            float f5 = fArr[i21 + i17] * 2.0f;
            float f6 = f + (f4 * 0.309017f) + (f5 * (-0.809017f));
            float f7 = ((-0.809017f) * f4) + f + (0.309017f * f5);
            float f8 = (f2 * 0.95105654f) + (f3 * 0.58778524f);
            float f9 = (f2 * 0.58778524f) - (f3 * 0.95105654f);
            fArr2[i4 + i13] = f + f4 + f5;
            fArr2[i4 + i18] = f6 - f8;
            fArr2[i4 + i19] = f7 - f9;
            fArr2[i4 + i20] = f7 + f9;
            fArr2[i4 + i20 + i11] = f6 + f8;
        }
        if (i6 == 1) {
            return;
        }
        int i22 = 0;
        while (i22 < i7) {
            int i23 = i22 * i6;
            int i24 = i23 * 5;
            int i25 = i24 + i6;
            int i26 = i25 + i6;
            int i27 = i26 + i6;
            int i28 = i27 + i6;
            int i29 = i23 + i11;
            int i30 = i29 + i11;
            int i31 = i30 + i11;
            int i32 = i31 + i11;
            int i33 = 2;
            while (i33 < i6) {
                int i34 = i6 - i33;
                int i35 = i33 - 1;
                int i36 = i35 + i5;
                int i37 = i35 + i8;
                int i38 = i35 + i9;
                int i39 = i35 + i10;
                float f10 = this.wtable_r[i36 - 1];
                float f11 = this.wtable_r[i36];
                float f12 = this.wtable_r[i37 - 1];
                float f13 = this.wtable_r[i37];
                int i40 = i8;
                float f14 = this.wtable_r[i38 - 1];
                int i41 = i9;
                float f15 = this.wtable_r[i38];
                int i42 = i10;
                float f16 = this.wtable_r[i39 - 1];
                int i43 = i11;
                float f17 = this.wtable_r[i39];
                int i44 = i3 + i33;
                int i45 = i3 + i34;
                int i46 = i4 + i33;
                int i47 = i44 + i24;
                int i48 = i45 + i25;
                int i49 = i44 + i26;
                int i50 = i45 + i27;
                int i51 = i44 + i28;
                float f18 = fArr[i47 - 1];
                float f19 = fArr[i47];
                float f20 = fArr[i48 - 1];
                float f21 = fArr[i48];
                float f22 = fArr[i49 - 1];
                float f23 = fArr[i49];
                float f24 = fArr[i50 - 1];
                float f25 = fArr[i50];
                float f26 = fArr[i51 - 1];
                float f27 = fArr[i51];
                float f28 = f23 + f21;
                float f29 = f23 - f21;
                float f30 = f27 + f25;
                float f31 = f27 - f25;
                float f32 = f22 - f20;
                float f33 = f22 + f20;
                float f34 = f26 - f24;
                float f35 = f26 + f24;
                float f36 = f18 + (f33 * 0.309017f) + (f35 * (-0.809017f));
                float f37 = f19 + (f29 * 0.309017f) + (f31 * (-0.809017f));
                float f38 = f18 + (f33 * (-0.809017f)) + (f35 * 0.309017f);
                float f39 = f19 + (f29 * (-0.809017f)) + (f31 * 0.309017f);
                float f40 = (f32 * 0.95105654f) + (f34 * 0.58778524f);
                float f41 = (f28 * 0.95105654f) + (f30 * 0.58778524f);
                float f42 = (f32 * 0.58778524f) - (f34 * 0.95105654f);
                float f43 = (f28 * 0.58778524f) - (f30 * 0.95105654f);
                float f44 = f38 - f43;
                float f45 = f38 + f43;
                float f46 = f39 + f42;
                float f47 = f39 - f42;
                float f48 = f36 + f41;
                float f49 = f36 - f41;
                float f50 = f37 - f40;
                float f51 = f37 + f40;
                int i52 = i46 + i23;
                int i53 = i46 + i29;
                int i54 = i46 + i30;
                int i55 = i46 + i31;
                int i56 = i46 + i32;
                fArr2[i52 - 1] = f18 + f33 + f35;
                fArr2[i52] = f19 + f29 + f31;
                fArr2[i53 - 1] = (f10 * f49) - (f11 * f51);
                fArr2[i53] = (f10 * f51) + (f11 * f49);
                fArr2[i54 - 1] = (f12 * f44) - (f13 * f46);
                fArr2[i54] = (f12 * f46) + (f13 * f44);
                fArr2[i55 - 1] = (f14 * f45) - (f15 * f47);
                fArr2[i55] = (f14 * f47) + (f15 * f45);
                fArr2[i56 - 1] = (f16 * f48) - (f17 * f50);
                fArr2[i56] = (f16 * f50) + (f17 * f48);
                i33 += 2;
                i8 = i40;
                i9 = i41;
                i10 = i42;
                i11 = i43;
                i6 = i;
            }
            i22++;
            i6 = i;
            i7 = i2;
        }
    }

    void radfg(int i, int i2, int i3, int i4, float[] fArr, int i5, float[] fArr2, int i6, int i7) {
        int i8;
        float f;
        float f2;
        int i9 = i2;
        double d = TWO_PI / i9;
        float fCos = (float) Math.cos(d);
        float fSin = (float) Math.sin(d);
        int i10 = (i9 + 1) / 2;
        int i11 = (i - 1) / 2;
        if (i != 1) {
            for (int i12 = 0; i12 < i4; i12++) {
                fArr2[i6 + i12] = fArr[i5 + i12];
            }
            for (int i13 = 1; i13 < i9; i13++) {
                int i14 = i13 * i3 * i;
                for (int i15 = 0; i15 < i3; i15++) {
                    int i16 = (i15 * i) + i14;
                    fArr2[i6 + i16] = fArr[i5 + i16];
                }
            }
            if (i11 <= i3) {
                int i17 = -i;
                int i18 = 1;
                while (i18 < i9) {
                    int i19 = i17 + i;
                    int i20 = i19 - 1;
                    int i21 = i18 * i3 * i;
                    int i22 = 2;
                    while (i22 < i) {
                        i20 += 2;
                        int i23 = i20 + i7;
                        int i24 = i5 + i22;
                        int i25 = i6 + i22;
                        float f3 = fSin;
                        float f4 = this.wtable_r[i23 - 1];
                        float f5 = fCos;
                        float f6 = this.wtable_r[i23];
                        for (int i26 = 0; i26 < i3; i26++) {
                            int i27 = (i26 * i) + i21;
                            int i28 = i25 + i27;
                            int i29 = i24 + i27;
                            float f7 = fArr[i29 - 1];
                            float f8 = fArr[i29];
                            fArr2[i28 - 1] = (f4 * f7) + (f6 * f8);
                            fArr2[i28] = (f8 * f4) - (f7 * f6);
                        }
                        i22 += 2;
                        fSin = f3;
                        fCos = f5;
                    }
                    i18++;
                    i17 = i19;
                }
                f = fSin;
                f2 = fCos;
            } else {
                f = fSin;
                f2 = fCos;
                int i30 = -i;
                for (int i31 = 1; i31 < i9; i31++) {
                    i30 += i;
                    int i32 = i31 * i3 * i;
                    for (int i33 = 0; i33 < i3; i33++) {
                        int i34 = (i33 * i) + i32;
                        int i35 = i30 - 1;
                        int i36 = 2;
                        while (i36 < i) {
                            i35 += 2;
                            int i37 = i35 + i7;
                            float f9 = this.wtable_r[i37 - 1];
                            int i38 = i30;
                            float f10 = this.wtable_r[i37];
                            int i39 = i6 + i36 + i34;
                            int i40 = i5 + i36 + i34;
                            float f11 = fArr[i40 - 1];
                            float f12 = fArr[i40];
                            fArr2[i39 - 1] = (f9 * f11) + (f10 * f12);
                            fArr2[i39] = (f9 * f12) - (f10 * f11);
                            i36 += 2;
                            i30 = i38;
                        }
                    }
                }
            }
            if (i11 >= i3) {
                for (int i41 = 1; i41 < i10; i41++) {
                    int i42 = i41 * i3 * i;
                    int i43 = (i9 - i41) * i3 * i;
                    for (int i44 = 0; i44 < i3; i44++) {
                        int i45 = i44 * i;
                        int i46 = i45 + i42;
                        int i47 = i45 + i43;
                        for (int i48 = 2; i48 < i; i48 += 2) {
                            int i49 = i5 + i48;
                            int i50 = i6 + i48;
                            int i51 = i49 + i46;
                            int i52 = i49 + i47;
                            int i53 = i50 + i46;
                            int i54 = i50 + i47;
                            float f13 = fArr2[i53 - 1];
                            float f14 = fArr2[i53];
                            float f15 = fArr2[i54 - 1];
                            float f16 = fArr2[i54];
                            fArr[i51 - 1] = f13 + f15;
                            fArr[i51] = f14 + f16;
                            fArr[i52 - 1] = f14 - f16;
                            fArr[i52] = f15 - f13;
                        }
                    }
                }
            } else {
                for (int i55 = 1; i55 < i10; i55++) {
                    int i56 = i55 * i3 * i;
                    int i57 = (i9 - i55) * i3 * i;
                    for (int i58 = 2; i58 < i; i58 += 2) {
                        int i59 = i5 + i58;
                        int i60 = i6 + i58;
                        for (int i61 = 0; i61 < i3; i61++) {
                            int i62 = i61 * i;
                            int i63 = i62 + i56;
                            int i64 = i62 + i57;
                            int i65 = i59 + i63;
                            int i66 = i59 + i64;
                            int i67 = i60 + i63;
                            int i68 = i60 + i64;
                            float f17 = fArr2[i67 - 1];
                            float f18 = fArr2[i67];
                            float f19 = fArr2[i68 - 1];
                            float f20 = fArr2[i68];
                            fArr[i65 - 1] = f17 + f19;
                            fArr[i65] = f18 + f20;
                            fArr[i66 - 1] = f18 - f20;
                            fArr[i66] = f19 - f17;
                        }
                    }
                }
            }
            i8 = i4;
        } else {
            i8 = i4;
            f = fSin;
            f2 = fCos;
            System.arraycopy(fArr2, i6, fArr, i5, i8);
        }
        for (int i69 = 1; i69 < i10; i69++) {
            int i70 = i69 * i3 * i;
            int i71 = (i9 - i69) * i3 * i;
            for (int i72 = 0; i72 < i3; i72++) {
                int i73 = i72 * i;
                int i74 = i73 + i70;
                int i75 = i73 + i71;
                float f21 = fArr2[i6 + i74];
                float f22 = fArr2[i6 + i75];
                fArr[i74 + i5] = f21 + f22;
                fArr[i75 + i5] = f22 - f21;
            }
        }
        int i76 = (i9 - 1) * i8;
        int i77 = 1;
        float f23 = 1.0f;
        float f24 = 0.0f;
        while (i77 < i10) {
            float f25 = (f2 * f23) - (f * f24);
            f24 = (f24 * f2) + (f23 * f);
            int i78 = i77 * i8;
            int i79 = (i9 - i77) * i8;
            int i80 = i11;
            for (int i81 = 0; i81 < i8; i81++) {
                int i82 = i6 + i81;
                int i83 = i5 + i81;
                fArr2[i82 + i78] = fArr[i83] + (fArr[i83 + i8] * f25);
                fArr2[i82 + i79] = fArr[i83 + i76] * f24;
            }
            float f26 = f24;
            float f27 = f25;
            int i84 = 2;
            while (i84 < i10) {
                float f28 = (f25 * f27) - (f24 * f26);
                f26 = (f26 * f25) + (f27 * f24);
                int i85 = i84 * i8;
                int i86 = (i9 - i84) * i8;
                int i87 = i76;
                for (int i88 = 0; i88 < i8; i88++) {
                    int i89 = i6 + i88;
                    int i90 = i5 + i88;
                    int i91 = i89 + i78;
                    fArr2[i91] = fArr2[i91] + (fArr[i90 + i85] * f28);
                    int i92 = i89 + i79;
                    fArr2[i92] = fArr2[i92] + (fArr[i90 + i86] * f26);
                }
                i84++;
                f27 = f28;
                i76 = i87;
            }
            i77++;
            f23 = f25;
            i11 = i80;
        }
        int i93 = i11;
        for (int i94 = 1; i94 < i10; i94++) {
            int i95 = i94 * i8;
            for (int i96 = 0; i96 < i8; i96++) {
                int i97 = i6 + i96;
                fArr2[i97] = fArr2[i97] + fArr[i5 + i96 + i95];
            }
        }
        if (i >= i3) {
            for (int i98 = 0; i98 < i3; i98++) {
                int i99 = i98 * i;
                int i100 = i99 * i9;
                for (int i101 = 0; i101 < i; i101++) {
                    fArr[i5 + i101 + i100] = fArr2[i6 + i101 + i99];
                }
            }
        } else {
            for (int i102 = 0; i102 < i; i102++) {
                for (int i103 = 0; i103 < i3; i103++) {
                    int i104 = i103 * i;
                    fArr[i5 + i102 + (i104 * i9)] = fArr2[i6 + i102 + i104];
                }
            }
        }
        int i105 = i9 * i;
        for (int i106 = 1; i106 < i10; i106++) {
            int i107 = i106 * i3 * i;
            int i108 = (i9 - i106) * i3 * i;
            int i109 = i106 * 2 * i;
            for (int i110 = 0; i110 < i3; i110++) {
                int i111 = i110 * i;
                int i112 = i110 * i105;
                fArr[((((i5 + i) - 1) + i109) - i) + i112] = fArr2[i111 + i107 + i6];
                fArr[i5 + i109 + i112] = fArr2[i111 + i108 + i6];
            }
        }
        int i113 = 1;
        if (i == 1) {
            return;
        }
        if (i93 >= i3) {
            while (i113 < i10) {
                int i114 = i113 * i3 * i;
                int i115 = (i9 - i113) * i3 * i;
                int i116 = i113 * 2 * i;
                int i117 = 0;
                while (i117 < i3) {
                    int i118 = i117 * i105;
                    int i119 = i117 * i;
                    int i120 = i105;
                    for (int i121 = 2; i121 < i; i121 += 2) {
                        int i122 = i5 + i121 + i116 + i118;
                        int i123 = (((i5 + (i - i121)) + i116) - i) + i118;
                        int i124 = i6 + i121 + i119;
                        int i125 = i124 + i114;
                        int i126 = i124 + i115;
                        float f29 = fArr2[i125 - 1];
                        float f30 = fArr2[i125];
                        float f31 = fArr2[i126 - 1];
                        float f32 = fArr2[i126];
                        fArr[i122 - 1] = f29 + f31;
                        fArr[i123 - 1] = f29 - f31;
                        fArr[i122] = f30 + f32;
                        fArr[i123] = f32 - f30;
                    }
                    i117++;
                    i105 = i120;
                }
                i113++;
            }
            return;
        }
        while (i113 < i10) {
            int i127 = i113 * i3 * i;
            int i128 = (i9 - i113) * i3 * i;
            int i129 = i113 * 2 * i;
            for (int i130 = 2; i130 < i; i130 += 2) {
                int i131 = i5 + i130;
                int i132 = (i - i130) + i5;
                int i133 = i6 + i130;
                for (int i134 = 0; i134 < i3; i134++) {
                    int i135 = i134 * i105;
                    int i136 = i131 + i129 + i135;
                    int i137 = ((i132 + i129) - i) + i135;
                    int i138 = i133 + (i134 * i);
                    int i139 = i138 + i127;
                    int i140 = i138 + i128;
                    float f33 = fArr2[i139 - 1];
                    float f34 = fArr2[i139];
                    float f35 = fArr2[i140 - 1];
                    float f36 = fArr2[i140];
                    fArr[i136 - 1] = f33 + f35;
                    fArr[i137 - 1] = f33 - f35;
                    fArr[i136] = f34 + f36;
                    fArr[i137] = f36 - f34;
                }
            }
            i113++;
            i9 = i2;
        }
    }

    void radbg(int i, int i2, int i3, int i4, float[] fArr, int i5, float[] fArr2, int i6, int i7) {
        int i8;
        int i9 = i;
        int i10 = i2;
        int i11 = i6;
        double d = TWO_PI / i10;
        float fCos = (float) Math.cos(d);
        float fSin = (float) Math.sin(d);
        int i12 = (i9 - 1) / 2;
        int i13 = (i10 + 1) / 2;
        int i14 = i10 * i9;
        if (i9 >= i3) {
            for (int i15 = 0; i15 < i3; i15++) {
                int i16 = i15 * i9;
                int i17 = i15 * i14;
                for (int i18 = 0; i18 < i9; i18++) {
                    fArr2[i11 + i18 + i16] = fArr[i5 + i18 + i17];
                }
            }
        } else {
            for (int i19 = 0; i19 < i9; i19++) {
                int i20 = i11 + i19;
                int i21 = i5 + i19;
                for (int i22 = 0; i22 < i3; i22++) {
                    fArr2[i20 + (i22 * i9)] = fArr[i21 + (i22 * i14)];
                }
            }
        }
        int i23 = 1;
        int i24 = (i5 + i9) - 1;
        int i25 = 1;
        while (i25 < i13) {
            int i26 = i25 * i3 * i9;
            int i27 = (i10 - i25) * i3 * i9;
            int i28 = i25 * 2 * i9;
            for (int i29 = 0; i29 < i3; i29++) {
                int i30 = i29 * i9;
                int i31 = i30 * i10;
                int i32 = i5 + i28 + i31;
                float f = fArr[((i24 + i28) + i31) - i9];
                float f2 = fArr[i32];
                int i33 = i11 + i30;
                fArr2[i33 + i26] = f + f;
                fArr2[i33 + i27] = f2 + f2;
            }
            i25++;
            i23 = 1;
        }
        if (i9 == i23) {
            i8 = i12;
        } else if (i12 >= i3) {
            int i34 = 1;
            while (i34 < i13) {
                int i35 = i34 * i3 * i9;
                int i36 = (i10 - i34) * i3 * i9;
                int i37 = i34 * 2 * i9;
                int i38 = i12;
                int i39 = 0;
                while (i39 < i3) {
                    int i40 = i39 * i9;
                    int i41 = i40 + i35;
                    int i42 = i40 + i36;
                    int i43 = (i39 * i10 * i9) + i37;
                    int i44 = i36;
                    for (int i45 = 2; i45 < i9; i45 += 2) {
                        int i46 = i11 + i45;
                        int i47 = i46 + i41;
                        int i48 = i46 + i42;
                        int i49 = i5 + i45 + i43;
                        int i50 = ((i5 + (i9 - i45)) + i43) - i9;
                        float f3 = fArr[i49 - 1];
                        float f4 = fArr[i49];
                        float f5 = fArr[i50 - 1];
                        float f6 = fArr[i50];
                        fArr2[i47 - 1] = f3 + f5;
                        fArr2[i48 - 1] = f3 - f5;
                        fArr2[i47] = f4 - f6;
                        fArr2[i48] = f4 + f6;
                    }
                    i39++;
                    i36 = i44;
                }
                i34++;
                i12 = i38;
            }
            i8 = i12;
        } else {
            i8 = i12;
            int i51 = 1;
            while (i51 < i13) {
                int i52 = i51 * i3 * i9;
                int i53 = (i10 - i51) * i3 * i9;
                int i54 = i51 * 2 * i9;
                int i55 = 2;
                while (i55 < i9) {
                    int i56 = i11 + i55;
                    int i57 = i5 + (i9 - i55);
                    int i58 = i5 + i55;
                    for (int i59 = 0; i59 < i3; i59++) {
                        int i60 = i59 * i9;
                        int i61 = (i59 * i10 * i9) + i54;
                        int i62 = i56 + i60 + i52;
                        int i63 = i56 + i60 + i53;
                        int i64 = i58 + i61;
                        int i65 = (i57 + i61) - i9;
                        float f7 = fArr[i64 - 1];
                        float f8 = fArr[i64];
                        float f9 = fArr[i65 - 1];
                        float f10 = fArr[i65];
                        fArr2[i62 - 1] = f7 + f9;
                        fArr2[i63 - 1] = f7 - f9;
                        fArr2[i62] = f8 - f10;
                        fArr2[i63] = f8 + f10;
                    }
                    i55 += 2;
                    i11 = i6;
                }
                i51++;
                i11 = i6;
            }
        }
        int i66 = (i10 - 1) * i4;
        int i67 = 1;
        float f11 = 1.0f;
        float f12 = 0.0f;
        while (i67 < i13) {
            float f13 = (fCos * f11) - (fSin * f12);
            f12 = (f12 * fCos) + (f11 * fSin);
            int i68 = i67 * i4;
            int i69 = (i10 - i67) * i4;
            float f14 = fSin;
            int i70 = 0;
            while (i70 < i4) {
                int i71 = i5 + i70;
                int i72 = i6 + i70;
                fArr[i71 + i68] = fArr2[i72] + (fArr2[i72 + i4] * f13);
                fArr[i71 + i69] = fArr2[i72 + i66] * f12;
                i70++;
                fCos = fCos;
            }
            float f15 = fCos;
            float f16 = f12;
            float f17 = f13;
            int i73 = 2;
            while (i73 < i13) {
                float f18 = (f13 * f17) - (f12 * f16);
                f16 = (f16 * f13) + (f17 * f12);
                int i74 = i73 * i4;
                int i75 = (i10 - i73) * i4;
                int i76 = i66;
                for (int i77 = 0; i77 < i4; i77++) {
                    int i78 = i5 + i77;
                    int i79 = i6 + i77;
                    int i80 = i78 + i68;
                    fArr[i80] = fArr[i80] + (fArr2[i79 + i74] * f18);
                    int i81 = i78 + i69;
                    fArr[i81] = fArr[i81] + (fArr2[i79 + i75] * f16);
                }
                i73++;
                f17 = f18;
                i66 = i76;
            }
            i67++;
            f11 = f13;
            fSin = f14;
            fCos = f15;
        }
        for (int i82 = 1; i82 < i13; i82++) {
            int i83 = i82 * i4;
            for (int i84 = 0; i84 < i4; i84++) {
                int i85 = i6 + i84;
                fArr2[i85] = fArr2[i85] + fArr2[i85 + i83];
            }
        }
        for (int i86 = 1; i86 < i13; i86++) {
            int i87 = i86 * i3 * i9;
            int i88 = (i10 - i86) * i3 * i9;
            for (int i89 = 0; i89 < i3; i89++) {
                int i90 = i89 * i9;
                int i91 = i6 + i90;
                int i92 = i90 + i5;
                float f19 = fArr[i92 + i87];
                float f20 = fArr[i92 + i88];
                fArr2[i91 + i87] = f19 - f20;
                fArr2[i91 + i88] = f19 + f20;
            }
        }
        if (i9 == 1) {
            return;
        }
        int i93 = i8;
        if (i93 >= i3) {
            for (int i94 = 1; i94 < i13; i94++) {
                int i95 = i94 * i3 * i9;
                int i96 = (i10 - i94) * i3 * i9;
                for (int i97 = 0; i97 < i3; i97++) {
                    int i98 = i97 * i9;
                    for (int i99 = 2; i99 < i9; i99 += 2) {
                        int i100 = i6 + i99 + i98;
                        int i101 = i100 + i95;
                        int i102 = i100 + i96;
                        int i103 = i5 + i99 + i98;
                        int i104 = i103 + i95;
                        int i105 = i103 + i96;
                        float f21 = fArr[i104 - 1];
                        float f22 = fArr[i104];
                        float f23 = fArr[i105 - 1];
                        float f24 = fArr[i105];
                        fArr2[i101 - 1] = f21 - f24;
                        fArr2[i102 - 1] = f21 + f24;
                        fArr2[i101] = f22 + f23;
                        fArr2[i102] = f22 - f23;
                    }
                }
            }
        } else {
            for (int i106 = 1; i106 < i13; i106++) {
                int i107 = i106 * i3 * i9;
                int i108 = (i10 - i106) * i3 * i9;
                int i109 = 2;
                while (i109 < i9) {
                    int i110 = i6 + i109;
                    int i111 = i5 + i109;
                    int i112 = i13;
                    for (int i113 = 0; i113 < i3; i113++) {
                        int i114 = i113 * i9;
                        int i115 = i110 + i114;
                        int i116 = i115 + i107;
                        int i117 = i115 + i108;
                        int i118 = i111 + i114;
                        int i119 = i118 + i107;
                        int i120 = i118 + i108;
                        float f25 = fArr[i119 - 1];
                        float f26 = fArr[i119];
                        float f27 = fArr[i120 - 1];
                        float f28 = fArr[i120];
                        fArr2[i116 - 1] = f25 - f28;
                        fArr2[i117 - 1] = f25 + f28;
                        fArr2[i116] = f26 + f27;
                        fArr2[i117] = f26 - f27;
                    }
                    i109 += 2;
                    i13 = i112;
                }
            }
        }
        System.arraycopy(fArr2, i6, fArr, i5, i4);
        for (int i121 = 1; i121 < i10; i121++) {
            int i122 = i121 * i3 * i9;
            for (int i123 = 0; i123 < i3; i123++) {
                int i124 = (i123 * i9) + i122;
                fArr[i5 + i124] = fArr2[i124 + i6];
            }
        }
        if (i93 > i3) {
            int i125 = -i9;
            int i126 = 1;
            while (i126 < i10) {
                i125 += i9;
                int i127 = i126 * i3 * i9;
                int i128 = 0;
                while (i128 < i3) {
                    int i129 = (i128 * i9) + i127;
                    int i130 = i125 - 1;
                    int i131 = 2;
                    while (i131 < i9) {
                        i130 += 2;
                        int i132 = i130 + i7;
                        float f29 = this.wtable_r[i132 - 1];
                        float f30 = this.wtable_r[i132];
                        int i133 = i5 + i131 + i129;
                        int i134 = i6 + i131 + i129;
                        float f31 = fArr2[i134 - 1];
                        float f32 = fArr2[i134];
                        fArr[i133 - 1] = (f29 * f31) - (f30 * f32);
                        fArr[i133] = (f29 * f32) + (f30 * f31);
                        i131 += 2;
                        i9 = i;
                    }
                    i128++;
                    i9 = i;
                }
                i126++;
                i9 = i;
                i10 = i2;
            }
            return;
        }
        int i135 = -i9;
        for (int i136 = 1; i136 < i10; i136++) {
            i135 += i9;
            int i137 = i136 * i3 * i9;
            int i138 = i135 - 1;
            int i139 = 2;
            while (i139 < i9) {
                int i140 = i138 + 2;
                int i141 = i140 + i7;
                float f33 = this.wtable_r[i141 - 1];
                int i142 = i135;
                float f34 = this.wtable_r[i141];
                int i143 = i5 + i139;
                int i144 = i6 + i139;
                for (int i145 = 0; i145 < i3; i145++) {
                    int i146 = (i145 * i9) + i137;
                    int i147 = i143 + i146;
                    int i148 = i144 + i146;
                    float f35 = fArr2[i148 - 1];
                    float f36 = fArr2[i148];
                    fArr[i147 - 1] = (f33 * f35) - (f34 * f36);
                    fArr[i147] = (f36 * f33) + (f35 * f34);
                }
                i139 += 2;
                i135 = i142;
                i138 = i140;
            }
        }
    }

    void cfftf(float[] fArr, int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        float[] fArr2;
        char c;
        int i7 = this.n * 2;
        float[] fArr3 = new float[i7];
        int i8 = this.n * 4;
        int[] iArr = {0};
        int i9 = (int) this.wtable[i8 + 1];
        int i10 = i7;
        int i11 = 1;
        int i12 = 2;
        int i13 = 0;
        while (i12 <= i9 + 1) {
            int i14 = (int) this.wtable[i12 + i8];
            int i15 = i14 * i11;
            int i16 = this.n / i15;
            int i17 = i16 + i16;
            int i18 = i17 * i11;
            switch (i14) {
                case 2:
                    i3 = i14;
                    i4 = i12;
                    if (i13 == 0) {
                        passf2(i17, i11, fArr, i, fArr3, 0, i10, i2);
                    } else {
                        passf2(i17, i11, fArr3, 0, fArr, i, i10, i2);
                    }
                    i5 = 1 - i13;
                    break;
                case 3:
                    i3 = i14;
                    i4 = i12;
                    if (i13 == 0) {
                        passf3(i17, i11, fArr, i, fArr3, 0, i10, i2);
                    } else {
                        passf3(i17, i11, fArr3, 0, fArr, i, i10, i2);
                    }
                    i5 = 1 - i13;
                    break;
                case 4:
                    i3 = i14;
                    i4 = i12;
                    if (i13 == 0) {
                        passf4(i17, i11, fArr, i, fArr3, 0, i10, i2);
                    } else {
                        passf4(i17, i11, fArr3, 0, fArr, i, i10, i2);
                    }
                    i5 = 1 - i13;
                    break;
                case 5:
                    if (i13 == 0) {
                        i3 = i14;
                        i4 = i12;
                        passf5(i17, i11, fArr, i, fArr3, 0, i10, i2);
                    } else {
                        i3 = i14;
                        i4 = i12;
                        passf5(i17, i11, fArr3, 0, fArr, i, i10, i2);
                    }
                    i5 = 1 - i13;
                    break;
                default:
                    i3 = i14;
                    i4 = i12;
                    if (i13 == 0) {
                        i6 = i9;
                        c = 0;
                        fArr2 = fArr3;
                        passfg(iArr, i17, i3, i11, i18, fArr, i, fArr3, 0, i10, i2);
                    } else {
                        i6 = i9;
                        fArr2 = fArr3;
                        c = 0;
                        passfg(iArr, i17, i3, i11, i18, fArr2, 0, fArr, i, i10, i2);
                    }
                    if (iArr[c] != 0) {
                        i13 = 1 - i13;
                    } else {
                        continue;
                    }
                    i10 += (i3 - 1) * i17;
                    i12 = i4 + 1;
                    i11 = i15;
                    i9 = i6;
                    fArr3 = fArr2;
            }
            i13 = i5;
            i6 = i9;
            fArr2 = fArr3;
            i10 += (i3 - 1) * i17;
            i12 = i4 + 1;
            i11 = i15;
            i9 = i6;
            fArr3 = fArr2;
        }
        float[] fArr4 = fArr3;
        if (i13 == 0) {
            return;
        }
        System.arraycopy(fArr4, 0, fArr, i, i7);
    }

    void passf2(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5, int i6) {
        int i7 = i;
        int i8 = i2;
        int i9 = i7 * i8;
        if (i7 <= 2) {
            for (int i10 = 0; i10 < i8; i10++) {
                int i11 = i10 * i7;
                int i12 = i3 + (i11 * 2);
                int i13 = i12 + i7;
                float f = fArr[i12];
                float f2 = fArr[i12 + 1];
                float f3 = fArr[i13];
                float f4 = fArr[i13 + 1];
                int i14 = i4 + i11;
                int i15 = i14 + i9;
                fArr2[i14] = f + f3;
                fArr2[i14 + 1] = f2 + f4;
                fArr2[i15] = f - f3;
                fArr2[i15 + 1] = f2 - f4;
            }
            return;
        }
        int i16 = 0;
        while (i16 < i8) {
            int i17 = 0;
            while (i17 < i7 - 1) {
                int i18 = i16 * i7;
                int i19 = i3 + i17 + (i18 * 2);
                int i20 = i19 + i7;
                float f5 = fArr[i19];
                float f6 = fArr[i19 + 1];
                float f7 = fArr[i20];
                float f8 = fArr[i20 + 1];
                int i21 = i17 + i5;
                float f9 = this.wtable[i21];
                float f10 = i6 * this.wtable[i21 + 1];
                float f11 = f5 - f7;
                float f12 = f6 - f8;
                int i22 = i4 + i17 + i18;
                int i23 = i22 + i9;
                fArr2[i22] = f5 + f7;
                fArr2[i22 + 1] = f6 + f8;
                fArr2[i23] = (f9 * f11) - (f10 * f12);
                fArr2[i23 + 1] = (f9 * f12) + (f10 * f11);
                i17 += 2;
                i7 = i;
            }
            i16++;
            i7 = i;
            i8 = i2;
        }
    }

    void passf3(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5, int i6) {
        int i7 = i;
        int i8 = i2;
        int i9 = i5 + i7;
        int i10 = i8 * i7;
        float f = -0.5f;
        int i11 = 2;
        int i12 = 1;
        if (i7 == 2) {
            int i13 = 1;
            while (i13 <= i8) {
                int i14 = i3 + (((i13 * 3) - 2) * i7);
                int i15 = i14 + i7;
                int i16 = i14 - i7;
                float f2 = fArr[i14];
                float f3 = fArr[i14 + 1];
                float f4 = fArr[i15];
                float f5 = fArr[i15 + 1];
                float f6 = fArr[i16];
                float f7 = fArr[i16 + 1];
                float f8 = f2 + f4;
                float f9 = f6 + (f8 * f);
                float f10 = f3 + f5;
                float f11 = f7 + (f10 * f);
                float f12 = i6 * 0.8660254f;
                float f13 = (f2 - f4) * f12;
                float f14 = f12 * (f3 - f5);
                int i17 = i4 + ((i13 - 1) * i7);
                int i18 = i17 + i10;
                int i19 = i18 + i10;
                fArr2[i17] = fArr[i16] + f8;
                fArr2[i17 + 1] = f7 + f10;
                fArr2[i18] = f9 - f14;
                fArr2[i18 + 1] = f11 + f13;
                fArr2[i19] = f9 + f14;
                fArr2[i19 + 1] = f11 - f13;
                i13++;
                f = -0.5f;
            }
            return;
        }
        int i20 = 1;
        while (i20 <= i8) {
            int i21 = i3 + (((i20 * 3) - i11) * i7);
            int i22 = i4 + ((i20 - 1) * i7);
            int i23 = 0;
            while (i23 < i7 - 1) {
                int i24 = i23 + i21;
                int i25 = i24 + i7;
                int i26 = i24 - i7;
                float f15 = fArr[i24];
                float f16 = fArr[i24 + i12];
                float f17 = fArr[i25];
                float f18 = fArr[i25 + i12];
                float f19 = fArr[i26];
                float f20 = fArr[i26 + 1];
                float f21 = f15 + f17;
                float f22 = f19 + (f21 * (-0.5f));
                float f23 = f16 + f18;
                float f24 = f20 + (f23 * (-0.5f));
                float f25 = i6;
                float f26 = f25 * 0.8660254f;
                float f27 = (f15 - f17) * f26;
                float f28 = f26 * (f16 - f18);
                float f29 = f22 - f28;
                float f30 = f22 + f28;
                float f31 = f24 + f27;
                float f32 = f24 - f27;
                int i27 = i23 + i5;
                int i28 = i23 + i9;
                float f33 = this.wtable[i27];
                float f34 = this.wtable[i27 + 1] * f25;
                float f35 = this.wtable[i28];
                float f36 = f25 * this.wtable[i28 + 1];
                int i29 = i23 + i22;
                int i30 = i29 + i10;
                int i31 = i30 + i10;
                fArr2[i29] = f19 + f21;
                fArr2[i29 + 1] = f20 + f23;
                fArr2[i30] = (f33 * f29) - (f34 * f31);
                fArr2[i30 + 1] = (f33 * f31) + (f34 * f29);
                fArr2[i31] = (f35 * f30) - (f36 * f32);
                fArr2[i31 + 1] = (f35 * f32) + (f36 * f30);
                i23 += 2;
                i7 = i;
                i12 = 1;
            }
            i20++;
            i7 = i;
            i8 = i2;
            i11 = 2;
            i12 = 1;
        }
    }

    void passf4(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5, int i6) {
        int i7 = i;
        int i8 = i2;
        int i9 = i6;
        int i10 = i5 + i7;
        int i11 = i10 + i7;
        int i12 = i8 * i7;
        if (i7 == 2) {
            for (int i13 = 0; i13 < i8; i13++) {
                int i14 = i13 * i7;
                int i15 = i3 + (i14 * 4) + 1;
                int i16 = i15 + i7;
                int i17 = i16 + i7;
                int i18 = i17 + i7;
                float f = fArr[i15 - 1];
                float f2 = fArr[i15];
                float f3 = fArr[i16 - 1];
                float f4 = fArr[i16];
                float f5 = fArr[i17 - 1];
                float f6 = fArr[i17];
                float f7 = fArr[i18 - 1];
                float f8 = fArr[i18];
                float f9 = f2 - f6;
                float f10 = f2 + f6;
                float f11 = f8 - f4;
                float f12 = f4 + f8;
                float f13 = f - f5;
                float f14 = f + f5;
                float f15 = f3 - f7;
                float f16 = f3 + f7;
                int i19 = i4 + i14;
                int i20 = i19 + i12;
                int i21 = i20 + i12;
                int i22 = i21 + i12;
                fArr2[i19] = f14 + f16;
                fArr2[i19 + 1] = f10 + f12;
                float f17 = i9;
                float f18 = f11 * f17;
                fArr2[i20] = f13 + f18;
                float f19 = f17 * f15;
                fArr2[i20 + 1] = f9 + f19;
                fArr2[i21] = f14 - f16;
                fArr2[i21 + 1] = f10 - f12;
                fArr2[i22] = f13 - f18;
                fArr2[i22 + 1] = f9 - f19;
            }
            return;
        }
        int i23 = 0;
        while (i23 < i8) {
            int i24 = i23 * i7;
            int i25 = i3 + 1 + (i24 * 4);
            int i26 = 0;
            while (i26 < i7 - 1) {
                int i27 = i26 + i25;
                int i28 = i27 + i7;
                int i29 = i28 + i7;
                int i30 = i29 + i7;
                float f20 = fArr[i27 - 1];
                float f21 = fArr[i27];
                float f22 = fArr[i28 - 1];
                float f23 = fArr[i28];
                float f24 = fArr[i29 - 1];
                float f25 = fArr[i29];
                float f26 = fArr[i30 - 1];
                float f27 = fArr[i30];
                float f28 = f21 - f25;
                float f29 = f21 + f25;
                float f30 = f23 + f27;
                float f31 = f27 - f23;
                float f32 = f20 - f24;
                float f33 = f20 + f24;
                float f34 = f22 - f26;
                float f35 = f22 + f26;
                float f36 = f33 - f35;
                float f37 = f29 - f30;
                float f38 = i9;
                float f39 = f31 * f38;
                float f40 = f32 + f39;
                float f41 = f32 - f39;
                float f42 = f34 * f38;
                float f43 = f28 + f42;
                float f44 = f28 - f42;
                int i31 = i26 + i5;
                int i32 = i26 + i10;
                int i33 = i26 + i11;
                float f45 = this.wtable[i31];
                float f46 = this.wtable[i31 + 1] * f38;
                float f47 = this.wtable[i32];
                float f48 = this.wtable[i32 + 1] * f38;
                int i34 = i10;
                float f49 = this.wtable[i33];
                int i35 = i11;
                float f50 = f38 * this.wtable[i33 + 1];
                int i36 = i4 + i26 + i24;
                int i37 = i36 + i12;
                int i38 = i37 + i12;
                int i39 = i38 + i12;
                fArr2[i36] = f33 + f35;
                fArr2[i36 + 1] = f29 + f30;
                fArr2[i37] = (f45 * f40) - (f46 * f43);
                fArr2[i37 + 1] = (f45 * f43) + (f46 * f40);
                fArr2[i38] = (f47 * f36) - (f48 * f37);
                fArr2[i38 + 1] = (f47 * f37) + (f48 * f36);
                fArr2[i39] = (f49 * f41) - (f50 * f44);
                fArr2[i39 + 1] = (f49 * f44) + (f50 * f41);
                i26 += 2;
                i10 = i34;
                i11 = i35;
                i7 = i;
                i9 = i6;
            }
            i23++;
            i7 = i;
            i8 = i2;
            i9 = i6;
        }
    }

    void passf5(int i, int i2, float[] fArr, int i3, float[] fArr2, int i4, int i5, int i6) {
        int i7 = i;
        int i8 = i2;
        int i9 = i6;
        int i10 = i5 + i7;
        int i11 = i10 + i7;
        int i12 = i11 + i7;
        int i13 = i8 * i7;
        if (i7 == 2) {
            for (int i14 = 1; i14 <= i8; i14++) {
                int i15 = i3 + (((i14 * 5) - 4) * i7) + 1;
                int i16 = i15 + i7;
                int i17 = i15 - i7;
                int i18 = i16 + i7;
                int i19 = i18 + i7;
                float f = fArr[i15 - 1];
                float f2 = fArr[i15];
                float f3 = fArr[i16 - 1];
                float f4 = fArr[i16];
                float f5 = fArr[i17 - 1];
                float f6 = fArr[i17];
                float f7 = fArr[i18 - 1];
                float f8 = fArr[i18];
                float f9 = fArr[i19 - 1];
                float f10 = fArr[i19];
                float f11 = f2 - f10;
                float f12 = f2 + f10;
                float f13 = f4 - f8;
                float f14 = f4 + f8;
                float f15 = f - f9;
                float f16 = f + f9;
                float f17 = f3 - f7;
                float f18 = f3 + f7;
                float f19 = f5 + (f16 * 0.309017f) + (f18 * (-0.809017f));
                float f20 = f6 + (f12 * 0.309017f) + (f14 * (-0.809017f));
                float f21 = f5 + (f16 * (-0.809017f)) + (f18 * 0.309017f);
                float f22 = f6 + (f12 * (-0.809017f)) + (f14 * 0.309017f);
                float f23 = i9;
                float f24 = ((f15 * 0.95105654f) + (f17 * 0.58778524f)) * f23;
                float f25 = ((f11 * 0.95105654f) + (f13 * 0.58778524f)) * f23;
                float f26 = ((f15 * 0.58778524f) - (f17 * 0.95105654f)) * f23;
                float f27 = f23 * ((f11 * 0.58778524f) - (f13 * 0.95105654f));
                int i20 = i4 + ((i14 - 1) * i7);
                int i21 = i20 + i13;
                int i22 = i21 + i13;
                int i23 = i22 + i13;
                int i24 = i23 + i13;
                fArr2[i20] = f5 + f16 + f18;
                fArr2[i20 + 1] = f6 + f12 + f14;
                fArr2[i21] = f19 - f25;
                fArr2[i21 + 1] = f20 + f24;
                fArr2[i22] = f21 - f27;
                fArr2[i22 + 1] = f22 + f26;
                fArr2[i23] = f21 + f27;
                fArr2[i23 + 1] = f22 - f26;
                fArr2[i24] = f19 + f25;
                fArr2[i24 + 1] = f20 - f24;
            }
        } else {
            int i25 = 1;
            while (i25 <= i8) {
                int i26 = i3 + 1 + (((i25 * 5) - 4) * i7);
                int i27 = i4 + ((i25 - 1) * i7);
                int i28 = 0;
                while (i28 < i7 - 1) {
                    int i29 = i28 + i26;
                    int i30 = i29 + i7;
                    int i31 = i29 - i7;
                    int i32 = i30 + i7;
                    int i33 = i32 + i7;
                    float f28 = fArr[i29 - 1];
                    float f29 = fArr[i29];
                    float f30 = fArr[i30 - 1];
                    float f31 = fArr[i30];
                    float f32 = fArr[i31 - 1];
                    float f33 = fArr[i31];
                    float f34 = fArr[i32 - 1];
                    float f35 = fArr[i32];
                    float f36 = fArr[i33 - 1];
                    float f37 = fArr[i33];
                    float f38 = f29 - f37;
                    float f39 = f29 + f37;
                    float f40 = f31 - f35;
                    float f41 = f31 + f35;
                    float f42 = f28 - f36;
                    float f43 = f28 + f36;
                    float f44 = f30 - f34;
                    float f45 = f30 + f34;
                    float f46 = f32 + (f43 * 0.309017f) + (f45 * (-0.809017f));
                    float f47 = f33 + (f39 * 0.309017f) + (f41 * (-0.809017f));
                    float f48 = f32 + (f43 * (-0.809017f)) + (f45 * 0.309017f);
                    float f49 = f33 + (f39 * (-0.809017f)) + (f41 * 0.309017f);
                    float f50 = i9;
                    float f51 = ((f42 * 0.95105654f) + (f44 * 0.58778524f)) * f50;
                    float f52 = ((f38 * 0.95105654f) + (f40 * 0.58778524f)) * f50;
                    float f53 = ((f42 * 0.58778524f) - (f44 * 0.95105654f)) * f50;
                    float f54 = ((f38 * 0.58778524f) - (f40 * 0.95105654f)) * f50;
                    float f55 = f48 - f54;
                    float f56 = f48 + f54;
                    float f57 = f49 + f53;
                    float f58 = f49 - f53;
                    float f59 = f46 + f52;
                    float f60 = f46 - f52;
                    float f61 = f47 - f51;
                    float f62 = f47 + f51;
                    int i34 = i28 + i5;
                    int i35 = i28 + i10;
                    int i36 = i28 + i11;
                    int i37 = i28 + i12;
                    float f63 = this.wtable[i34];
                    float f64 = this.wtable[i34 + 1] * f50;
                    float f65 = this.wtable[i35];
                    float f66 = this.wtable[i35 + 1] * f50;
                    float f67 = this.wtable[i36];
                    int i38 = i10;
                    float f68 = this.wtable[i36 + 1] * f50;
                    int i39 = i11;
                    float f69 = this.wtable[i37];
                    int i40 = i12;
                    float f70 = f50 * this.wtable[i37 + 1];
                    int i41 = i28 + i27;
                    int i42 = i41 + i13;
                    int i43 = i42 + i13;
                    int i44 = i43 + i13;
                    int i45 = i44 + i13;
                    fArr2[i41] = f32 + f43 + f45;
                    fArr2[i41 + 1] = f33 + f39 + f41;
                    fArr2[i42] = (f63 * f60) - (f64 * f62);
                    fArr2[i42 + 1] = (f63 * f62) + (f64 * f60);
                    fArr2[i43] = (f65 * f55) - (f66 * f57);
                    fArr2[i43 + 1] = (f65 * f57) + (f66 * f55);
                    fArr2[i44] = (f67 * f56) - (f68 * f58);
                    fArr2[i44 + 1] = (f67 * f58) + (f68 * f56);
                    fArr2[i45] = (f69 * f59) - (f70 * f61);
                    fArr2[i45 + 1] = (f69 * f61) + (f70 * f59);
                    i28 += 2;
                    i10 = i38;
                    i11 = i39;
                    i12 = i40;
                    i7 = i;
                    i9 = i6;
                }
                i25++;
                i7 = i;
                i8 = i2;
                i9 = i6;
            }
        }
    }

    void passfg(int[] iArr, int i, int i2, int i3, int i4, float[] fArr, int i5, float[] fArr2, int i6, int i7, int i8) {
        int i9;
        int i10;
        int i11 = i;
        int i12 = i6;
        int i13 = i11 / 2;
        int i14 = (i2 + 1) / 2;
        int i15 = i2 * i11;
        if (i11 >= i3) {
            int i16 = 1;
            while (i16 < i14) {
                int i17 = i16 * i11;
                int i18 = (i2 - i16) * i11;
                int i19 = i13;
                for (int i20 = 0; i20 < i3; i20++) {
                    int i21 = i20 * i11;
                    int i22 = i21 + (i17 * i3);
                    int i23 = i21 + (i18 * i3);
                    int i24 = i21 * i2;
                    for (int i25 = 0; i25 < i11; i25++) {
                        int i26 = i12 + i25;
                        int i27 = i5 + i25;
                        float f = fArr[i27 + i17 + i24];
                        float f2 = fArr[i27 + i18 + i24];
                        fArr2[i26 + i22] = f + f2;
                        fArr2[i26 + i23] = f - f2;
                    }
                }
                i16++;
                i13 = i19;
            }
            i9 = i13;
            int i28 = 0;
            while (i28 < i3) {
                int i29 = i28 * i11;
                int i30 = i29 * i2;
                int i31 = i15;
                for (int i32 = 0; i32 < i11; i32++) {
                    fArr2[i12 + i32 + i29] = fArr[i5 + i32 + i30];
                }
                i28++;
                i15 = i31;
            }
            i10 = i15;
        } else {
            i9 = i13;
            i10 = i15;
            for (int i33 = 1; i33 < i14; i33++) {
                int i34 = i2 - i33;
                int i35 = i33 * i3 * i11;
                int i36 = i34 * i3 * i11;
                int i37 = i33 * i11;
                int i38 = i34 * i11;
                for (int i39 = 0; i39 < i11; i39++) {
                    for (int i40 = 0; i40 < i3; i40++) {
                        int i41 = i40 * i11;
                        int i42 = i41 * i2;
                        int i43 = i5 + i39;
                        float f3 = fArr[i43 + i37 + i42];
                        float f4 = fArr[i43 + i38 + i42];
                        int i44 = i12 + i39 + i41;
                        fArr2[i44 + i35] = f3 + f4;
                        fArr2[i44 + i36] = f3 - f4;
                    }
                }
            }
            for (int i45 = 0; i45 < i11; i45++) {
                for (int i46 = 0; i46 < i3; i46++) {
                    int i47 = i46 * i11;
                    fArr2[i12 + i45 + i47] = fArr[i5 + i45 + (i47 * i2)];
                }
            }
        }
        int i48 = 2 - i11;
        int i49 = (i2 - 1) * i4;
        int i50 = i48;
        int i51 = 1;
        int i52 = 0;
        while (i51 < i14) {
            int i53 = i50 + i11;
            int i54 = i51 * i4;
            int i55 = (i2 - i51) * i4;
            int i56 = i53 + i7;
            int i57 = i48;
            float f5 = this.wtable[i56 - 2];
            int i58 = i51;
            float f6 = i8;
            float f7 = this.wtable[i56 - 1] * f6;
            for (int i59 = 0; i59 < i4; i59++) {
                int i60 = i5 + i59;
                int i61 = i12 + i59;
                fArr[i60 + i54] = fArr2[i61] + (fArr2[i61 + i4] * f5);
                fArr[i60 + i55] = fArr2[i61 + i49] * f7;
            }
            i52 += i11;
            int i62 = i53;
            int i63 = 2;
            while (i63 < i14) {
                int i64 = i2 - i63;
                int i65 = i62 + i52;
                int i66 = i49;
                int i67 = i10;
                if (i65 > i67) {
                    i65 -= i67;
                }
                int i68 = i65 + i7;
                float f8 = this.wtable[i68 - 2];
                int i69 = i65;
                float f9 = this.wtable[i68 - 1] * f6;
                int i70 = i63 * i4;
                int i71 = i64 * i4;
                int i72 = i52;
                for (int i73 = 0; i73 < i4; i73++) {
                    int i74 = i5 + i73;
                    int i75 = i12 + i73;
                    int i76 = i74 + i54;
                    fArr[i76] = fArr[i76] + (fArr2[i75 + i70] * f8);
                    int i77 = i74 + i55;
                    fArr[i77] = fArr[i77] + (fArr2[i75 + i71] * f9);
                }
                i63++;
                i49 = i66;
                i10 = i67;
                i62 = i69;
                i52 = i72;
            }
            i51 = i58 + 1;
            i50 = i53;
            i48 = i57;
        }
        int i78 = i48;
        for (int i79 = 1; i79 < i14; i79++) {
            int i80 = i79 * i4;
            for (int i81 = 0; i81 < i4; i81++) {
                int i82 = i12 + i81;
                fArr2[i82] = fArr2[i82] + fArr2[i82 + i80];
            }
        }
        for (int i83 = 1; i83 < i14; i83++) {
            int i84 = i83 * i4;
            int i85 = (i2 - i83) * i4;
            for (int i86 = 1; i86 < i4; i86 += 2) {
                int i87 = i12 + i86;
                int i88 = i5 + i86;
                int i89 = i88 + i84;
                int i90 = i88 + i85;
                float f10 = fArr[i89 - 1];
                float f11 = fArr[i89];
                float f12 = fArr[i90 - 1];
                float f13 = fArr[i90];
                int i91 = i87 + i84;
                int i92 = i87 + i85;
                fArr2[i91 - 1] = f10 - f13;
                fArr2[i92 - 1] = f10 + f13;
                fArr2[i91] = f11 + f12;
                fArr2[i92] = f11 - f12;
            }
        }
        iArr[0] = 1;
        if (i11 == 2) {
            return;
        }
        iArr[0] = 0;
        System.arraycopy(fArr2, i12, fArr, i5, i4);
        int i93 = i3;
        int i94 = i8;
        int i95 = i93 * i11;
        for (int i96 = 1; i96 < i2; i96++) {
            int i97 = i96 * i95;
            for (int i98 = 0; i98 < i93; i98++) {
                int i99 = i98 * i11;
                int i100 = i12 + i99 + i97;
                int i101 = i99 + i5 + i97;
                fArr[i101] = fArr2[i100];
                fArr[i101 + 1] = fArr2[i100 + 1];
            }
        }
        if (i9 > i93) {
            int i102 = 1;
            while (i102 < i2) {
                i78 += i11;
                int i103 = i102 * i93 * i11;
                int i104 = 0;
                while (i104 < i93) {
                    int i105 = (i104 * i11) + i103;
                    int i106 = i78;
                    int i107 = 3;
                    while (i107 < i11) {
                        i106 += 2;
                        int i108 = (i106 - 1) + i7;
                        float f14 = this.wtable[i108 - 1];
                        float f15 = i8 * this.wtable[i108];
                        int i109 = i5 + i107 + i105;
                        int i110 = i6 + i107 + i105;
                        float f16 = fArr2[i110 - 1];
                        float f17 = fArr2[i110];
                        fArr[i109 - 1] = (f14 * f16) - (f15 * f17);
                        fArr[i109] = (f14 * f17) + (f15 * f16);
                        i107 += 2;
                        i11 = i;
                    }
                    i104++;
                    i93 = i3;
                    i11 = i;
                }
                i102++;
                i93 = i3;
                i11 = i;
            }
            return;
        }
        int i111 = 1;
        int i112 = 0;
        while (i111 < i2) {
            int i113 = i111 * i93 * i11;
            int i114 = i112 + 2;
            int i115 = 3;
            while (i115 < i11) {
                int i116 = i114 + 2;
                int i117 = (i116 + i7) - 1;
                float f18 = this.wtable[i117 - 1];
                float f19 = i94 * this.wtable[i117];
                int i118 = i5 + i115;
                int i119 = i12 + i115;
                for (int i120 = 0; i120 < i93; i120++) {
                    int i121 = (i120 * i11) + i113;
                    int i122 = i118 + i121;
                    int i123 = i119 + i121;
                    float f20 = fArr2[i123 - 1];
                    float f21 = fArr2[i123];
                    fArr[i122 - 1] = (f18 * f20) - (f19 * f21);
                    fArr[i122] = (f21 * f18) + (f20 * f19);
                }
                i115 += 2;
                i114 = i116;
                i94 = i8;
                i12 = i6;
            }
            i111++;
            i112 = i114;
            i94 = i8;
            i12 = i6;
        }
    }

    private void cftfsub(int i, float[] fArr, int i2, int[] iArr, int i3, float[] fArr2) throws ExecutionException, InterruptedException {
        if (i <= 8) {
            if (i == 8) {
                cftf040(fArr, i2);
                return;
            } else {
                if (i == 4) {
                    cftxb020(fArr, i2);
                    return;
                }
                return;
            }
        }
        if (i <= 32) {
            if (i == 32) {
                cftf161(fArr, i2, fArr2, i3 - 8);
                bitrv216(fArr, i2);
                return;
            } else {
                cftf081(fArr, i2, fArr2, 0);
                bitrv208(fArr, i2);
                return;
            }
        }
        cftf1st(i, fArr, i2, fArr2, i3 - (i >> 2));
        if (ConcurrencyUtils.getNumberOfThreads() > 1 && i > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            cftrec4_th(i, fArr, i2, i3, fArr2);
        } else if (i > 512) {
            cftrec4(i, fArr, i2, i3, fArr2);
        } else if (i > 128) {
            cftleaf(i, 1, fArr, i2, i3, fArr2);
        } else {
            cftfx41(i, fArr, i2, i3, fArr2);
        }
        bitrv2(i, iArr, fArr, i2);
    }

    private void cftbsub(int i, float[] fArr, int i2, int[] iArr, int i3, float[] fArr2) throws ExecutionException, InterruptedException {
        if (i <= 8) {
            if (i == 8) {
                cftb040(fArr, i2);
                return;
            } else {
                if (i == 4) {
                    cftxb020(fArr, i2);
                    return;
                }
                return;
            }
        }
        if (i <= 32) {
            if (i == 32) {
                cftf161(fArr, i2, fArr2, i3 - 8);
                bitrv216neg(fArr, i2);
                return;
            } else {
                cftf081(fArr, i2, fArr2, 0);
                bitrv208neg(fArr, i2);
                return;
            }
        }
        cftb1st(i, fArr, i2, fArr2, i3 - (i >> 2));
        if (ConcurrencyUtils.getNumberOfThreads() > 1 && i > ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            cftrec4_th(i, fArr, i2, i3, fArr2);
        } else if (i > 512) {
            cftrec4(i, fArr, i2, i3, fArr2);
        } else if (i > 128) {
            cftleaf(i, 1, fArr, i2, i3, fArr2);
        } else {
            cftfx41(i, fArr, i2, i3, fArr2);
        }
        bitrv2conj(i, iArr, fArr, i2);
    }

    private void bitrv2(int i, int[] iArr, float[] fArr, int i2) {
        int i3 = i >> 2;
        int i4 = 1;
        while (i3 > 8) {
            i4 <<= 1;
            i3 >>= 2;
        }
        int i5 = i >> 1;
        int i6 = i4 * 4;
        if (i3 != 8) {
            for (int i7 = 0; i7 < i4; i7++) {
                int i8 = i7 * 4;
                for (int i9 = 0; i9 < i7; i9++) {
                    int i10 = (i9 * 4) + iArr[i4 + i7];
                    int i11 = iArr[i4 + i9] + i8;
                    int i12 = i2 + i10;
                    int i13 = i2 + i11;
                    float f = fArr[i12];
                    int i14 = i12 + 1;
                    float f2 = fArr[i14];
                    float f3 = fArr[i13];
                    int i15 = i13 + 1;
                    float f4 = fArr[i15];
                    fArr[i12] = f3;
                    fArr[i14] = f4;
                    fArr[i13] = f;
                    fArr[i15] = f2;
                    int i16 = i10 + i6;
                    int i17 = i11 + i6;
                    int i18 = i2 + i16;
                    int i19 = i2 + i17;
                    float f5 = fArr[i18];
                    int i20 = i18 + 1;
                    float f6 = fArr[i20];
                    float f7 = fArr[i19];
                    int i21 = i19 + 1;
                    float f8 = fArr[i21];
                    fArr[i18] = f7;
                    fArr[i20] = f8;
                    fArr[i19] = f5;
                    fArr[i21] = f6;
                    int i22 = i16 + i5;
                    int i23 = i17 + 2;
                    int i24 = i2 + i22;
                    int i25 = i2 + i23;
                    float f9 = fArr[i24];
                    int i26 = i24 + 1;
                    float f10 = fArr[i26];
                    float f11 = fArr[i25];
                    int i27 = i25 + 1;
                    float f12 = fArr[i27];
                    fArr[i24] = f11;
                    fArr[i26] = f12;
                    fArr[i25] = f9;
                    fArr[i27] = f10;
                    int i28 = i22 - i6;
                    int i29 = i23 - i6;
                    int i30 = i2 + i28;
                    int i31 = i2 + i29;
                    float f13 = fArr[i30];
                    int i32 = i30 + 1;
                    float f14 = fArr[i32];
                    float f15 = fArr[i31];
                    int i33 = i31 + 1;
                    float f16 = fArr[i33];
                    fArr[i30] = f15;
                    fArr[i32] = f16;
                    fArr[i31] = f13;
                    fArr[i33] = f14;
                    int i34 = i28 + 2;
                    int i35 = i29 + i5;
                    int i36 = i2 + i34;
                    int i37 = i2 + i35;
                    float f17 = fArr[i36];
                    int i38 = i36 + 1;
                    float f18 = fArr[i38];
                    float f19 = fArr[i37];
                    int i39 = i37 + 1;
                    float f20 = fArr[i39];
                    fArr[i36] = f19;
                    fArr[i38] = f20;
                    fArr[i37] = f17;
                    fArr[i39] = f18;
                    int i40 = i34 + i6;
                    int i41 = i35 + i6;
                    int i42 = i2 + i40;
                    int i43 = i2 + i41;
                    float f21 = fArr[i42];
                    int i44 = i42 + 1;
                    float f22 = fArr[i44];
                    float f23 = fArr[i43];
                    int i45 = i43 + 1;
                    float f24 = fArr[i45];
                    fArr[i42] = f23;
                    fArr[i44] = f24;
                    fArr[i43] = f21;
                    fArr[i45] = f22;
                    int i46 = i40 - i5;
                    int i47 = i41 - 2;
                    int i48 = i2 + i46;
                    int i49 = i2 + i47;
                    float f25 = fArr[i48];
                    int i50 = i48 + 1;
                    float f26 = fArr[i50];
                    float f27 = fArr[i49];
                    int i51 = i49 + 1;
                    float f28 = fArr[i51];
                    fArr[i48] = f27;
                    fArr[i50] = f28;
                    fArr[i49] = f25;
                    fArr[i51] = f26;
                    int i52 = i2 + (i46 - i6);
                    int i53 = i2 + (i47 - i6);
                    float f29 = fArr[i52];
                    int i54 = i52 + 1;
                    float f30 = fArr[i54];
                    float f31 = fArr[i53];
                    int i55 = i53 + 1;
                    float f32 = fArr[i55];
                    fArr[i52] = f31;
                    fArr[i54] = f32;
                    fArr[i53] = f29;
                    fArr[i55] = f30;
                }
                int i56 = i8 + iArr[i4 + i7];
                int i57 = i56 + 2;
                int i58 = i56 + i5;
                int i59 = i2 + i57;
                int i60 = i2 + i58;
                float f33 = fArr[i59];
                int i61 = i59 + 1;
                float f34 = fArr[i61];
                float f35 = fArr[i60];
                int i62 = i60 + 1;
                float f36 = fArr[i62];
                fArr[i59] = f35;
                fArr[i61] = f36;
                fArr[i60] = f33;
                fArr[i62] = f34;
                int i63 = i2 + i57 + i6;
                int i64 = i2 + i58 + i6;
                float f37 = fArr[i63];
                int i65 = i63 + 1;
                float f38 = fArr[i65];
                float f39 = fArr[i64];
                int i66 = i64 + 1;
                float f40 = fArr[i66];
                fArr[i63] = f39;
                fArr[i65] = f40;
                fArr[i64] = f37;
                fArr[i66] = f38;
            }
            return;
        }
        for (int i67 = 0; i67 < i4; i67++) {
            int i68 = i67 * 4;
            for (int i69 = 0; i69 < i67; i69++) {
                int i70 = (i69 * 4) + (iArr[i4 + i67] * 2);
                int i71 = (iArr[i4 + i69] * 2) + i68;
                int i72 = i2 + i70;
                int i73 = i2 + i71;
                float f41 = fArr[i72];
                int i74 = i72 + 1;
                float f42 = fArr[i74];
                float f43 = fArr[i73];
                int i75 = i73 + 1;
                float f44 = fArr[i75];
                fArr[i72] = f43;
                fArr[i74] = f44;
                fArr[i73] = f41;
                fArr[i75] = f42;
                int i76 = i70 + i6;
                int i77 = i6 * 2;
                int i78 = i71 + i77;
                int i79 = i2 + i76;
                int i80 = i2 + i78;
                float f45 = fArr[i79];
                int i81 = i79 + 1;
                float f46 = fArr[i81];
                float f47 = fArr[i80];
                int i82 = i80 + 1;
                float f48 = fArr[i82];
                fArr[i79] = f47;
                fArr[i81] = f48;
                fArr[i80] = f45;
                fArr[i82] = f46;
                int i83 = i76 + i6;
                int i84 = i78 - i6;
                int i85 = i2 + i83;
                int i86 = i2 + i84;
                float f49 = fArr[i85];
                int i87 = i85 + 1;
                float f50 = fArr[i87];
                float f51 = fArr[i86];
                int i88 = i86 + 1;
                float f52 = fArr[i88];
                fArr[i85] = f51;
                fArr[i87] = f52;
                fArr[i86] = f49;
                fArr[i88] = f50;
                int i89 = i83 + i6;
                int i90 = i84 + i77;
                int i91 = i2 + i89;
                int i92 = i2 + i90;
                float f53 = fArr[i91];
                int i93 = i91 + 1;
                float f54 = fArr[i93];
                float f55 = fArr[i92];
                int i94 = i92 + 1;
                float f56 = fArr[i94];
                fArr[i91] = f55;
                fArr[i93] = f56;
                fArr[i92] = f53;
                fArr[i94] = f54;
                int i95 = i89 + i5;
                int i96 = i90 + 2;
                int i97 = i2 + i95;
                int i98 = i2 + i96;
                float f57 = fArr[i97];
                int i99 = i97 + 1;
                float f58 = fArr[i99];
                float f59 = fArr[i98];
                int i100 = i98 + 1;
                float f60 = fArr[i100];
                fArr[i97] = f59;
                fArr[i99] = f60;
                fArr[i98] = f57;
                fArr[i100] = f58;
                int i101 = i95 - i6;
                int i102 = i96 - i77;
                int i103 = i2 + i101;
                int i104 = i2 + i102;
                float f61 = fArr[i103];
                int i105 = i103 + 1;
                float f62 = fArr[i105];
                float f63 = fArr[i104];
                int i106 = i104 + 1;
                float f64 = fArr[i106];
                fArr[i103] = f63;
                fArr[i105] = f64;
                fArr[i104] = f61;
                fArr[i106] = f62;
                int i107 = i101 - i6;
                int i108 = i102 + i6;
                int i109 = i2 + i107;
                int i110 = i2 + i108;
                float f65 = fArr[i109];
                int i111 = i109 + 1;
                float f66 = fArr[i111];
                float f67 = fArr[i110];
                int i112 = i110 + 1;
                float f68 = fArr[i112];
                fArr[i109] = f67;
                fArr[i111] = f68;
                fArr[i110] = f65;
                fArr[i112] = f66;
                int i113 = i107 - i6;
                int i114 = i108 - i77;
                int i115 = i2 + i113;
                int i116 = i2 + i114;
                float f69 = fArr[i115];
                int i117 = i115 + 1;
                float f70 = fArr[i117];
                float f71 = fArr[i116];
                int i118 = i116 + 1;
                float f72 = fArr[i118];
                fArr[i115] = f71;
                fArr[i117] = f72;
                fArr[i116] = f69;
                fArr[i118] = f70;
                int i119 = i113 + 2;
                int i120 = i114 + i5;
                int i121 = i2 + i119;
                int i122 = i2 + i120;
                float f73 = fArr[i121];
                int i123 = i121 + 1;
                float f74 = fArr[i123];
                float f75 = fArr[i122];
                int i124 = i122 + 1;
                float f76 = fArr[i124];
                fArr[i121] = f75;
                fArr[i123] = f76;
                fArr[i122] = f73;
                fArr[i124] = f74;
                int i125 = i119 + i6;
                int i126 = i120 + i77;
                int i127 = i2 + i125;
                int i128 = i2 + i126;
                float f77 = fArr[i127];
                int i129 = i127 + 1;
                float f78 = fArr[i129];
                float f79 = fArr[i128];
                int i130 = i128 + 1;
                float f80 = fArr[i130];
                fArr[i127] = f79;
                fArr[i129] = f80;
                fArr[i128] = f77;
                fArr[i130] = f78;
                int i131 = i125 + i6;
                int i132 = i126 - i6;
                int i133 = i2 + i131;
                int i134 = i2 + i132;
                float f81 = fArr[i133];
                int i135 = i133 + 1;
                float f82 = fArr[i135];
                float f83 = fArr[i134];
                int i136 = i134 + 1;
                float f84 = fArr[i136];
                fArr[i133] = f83;
                fArr[i135] = f84;
                fArr[i134] = f81;
                fArr[i136] = f82;
                int i137 = i131 + i6;
                int i138 = i132 + i77;
                int i139 = i2 + i137;
                int i140 = i2 + i138;
                float f85 = fArr[i139];
                int i141 = i139 + 1;
                float f86 = fArr[i141];
                float f87 = fArr[i140];
                int i142 = i140 + 1;
                float f88 = fArr[i142];
                fArr[i139] = f87;
                fArr[i141] = f88;
                fArr[i140] = f85;
                fArr[i142] = f86;
                int i143 = i137 - i5;
                int i144 = i138 - 2;
                int i145 = i2 + i143;
                int i146 = i2 + i144;
                float f89 = fArr[i145];
                int i147 = i145 + 1;
                float f90 = fArr[i147];
                float f91 = fArr[i146];
                int i148 = i146 + 1;
                float f92 = fArr[i148];
                fArr[i145] = f91;
                fArr[i147] = f92;
                fArr[i146] = f89;
                fArr[i148] = f90;
                int i149 = i143 - i6;
                int i150 = i144 - i77;
                int i151 = i2 + i149;
                int i152 = i2 + i150;
                float f93 = fArr[i151];
                int i153 = i151 + 1;
                float f94 = fArr[i153];
                float f95 = fArr[i152];
                int i154 = i152 + 1;
                float f96 = fArr[i154];
                fArr[i151] = f95;
                fArr[i153] = f96;
                fArr[i152] = f93;
                fArr[i154] = f94;
                int i155 = i149 - i6;
                int i156 = i150 + i6;
                int i157 = i2 + i155;
                int i158 = i2 + i156;
                float f97 = fArr[i157];
                int i159 = i157 + 1;
                float f98 = fArr[i159];
                float f99 = fArr[i158];
                int i160 = i158 + 1;
                float f100 = fArr[i160];
                fArr[i157] = f99;
                fArr[i159] = f100;
                fArr[i158] = f97;
                fArr[i160] = f98;
                int i161 = i2 + (i155 - i6);
                int i162 = i2 + (i156 - i77);
                float f101 = fArr[i161];
                int i163 = i161 + 1;
                float f102 = fArr[i163];
                float f103 = fArr[i162];
                int i164 = i162 + 1;
                float f104 = fArr[i164];
                fArr[i161] = f103;
                fArr[i163] = f104;
                fArr[i162] = f101;
                fArr[i164] = f102;
            }
            int i165 = i68 + (iArr[i4 + i67] * 2);
            int i166 = i165 + 2;
            int i167 = i165 + i5;
            int i168 = i2 + i166;
            int i169 = i2 + i167;
            float f105 = fArr[i168];
            int i170 = i168 + 1;
            float f106 = fArr[i170];
            float f107 = fArr[i169];
            int i171 = i169 + 1;
            float f108 = fArr[i171];
            fArr[i168] = f107;
            fArr[i170] = f108;
            fArr[i169] = f105;
            fArr[i171] = f106;
            int i172 = i166 + i6;
            int i173 = i6 * 2;
            int i174 = i167 + i173;
            int i175 = i2 + i172;
            int i176 = i2 + i174;
            float f109 = fArr[i175];
            int i177 = i175 + 1;
            float f110 = fArr[i177];
            float f111 = fArr[i176];
            int i178 = i176 + 1;
            float f112 = fArr[i178];
            fArr[i175] = f111;
            fArr[i177] = f112;
            fArr[i176] = f109;
            fArr[i178] = f110;
            int i179 = i172 + i6;
            int i180 = i174 - i6;
            int i181 = i2 + i179;
            int i182 = i2 + i180;
            float f113 = fArr[i181];
            int i183 = i181 + 1;
            float f114 = fArr[i183];
            float f115 = fArr[i182];
            int i184 = i182 + 1;
            float f116 = fArr[i184];
            fArr[i181] = f115;
            fArr[i183] = f116;
            fArr[i182] = f113;
            fArr[i184] = f114;
            int i185 = i179 - 2;
            int i186 = i180 - i5;
            int i187 = i2 + i185;
            int i188 = i2 + i186;
            float f117 = fArr[i187];
            int i189 = i187 + 1;
            float f118 = fArr[i189];
            float f119 = fArr[i188];
            int i190 = i188 + 1;
            float f120 = fArr[i190];
            fArr[i187] = f119;
            fArr[i189] = f120;
            fArr[i188] = f117;
            fArr[i190] = f118;
            int i191 = i5 + 2;
            int i192 = i185 + i191;
            int i193 = i186 + i191;
            int i194 = i2 + i192;
            int i195 = i2 + i193;
            float f121 = fArr[i194];
            int i196 = i194 + 1;
            float f122 = fArr[i196];
            float f123 = fArr[i195];
            int i197 = i195 + 1;
            float f124 = fArr[i197];
            fArr[i194] = f123;
            fArr[i196] = f124;
            fArr[i195] = f121;
            fArr[i197] = f122;
            int i198 = i2 + (i192 - (i5 - i6));
            int i199 = i2 + i193 + (i173 - 2);
            float f125 = fArr[i198];
            int i200 = i198 + 1;
            float f126 = fArr[i200];
            float f127 = fArr[i199];
            int i201 = i199 + 1;
            float f128 = fArr[i201];
            fArr[i198] = f127;
            fArr[i200] = f128;
            fArr[i199] = f125;
            fArr[i201] = f126;
        }
    }

    private void bitrv2conj(int i, int[] iArr, float[] fArr, int i2) {
        int i3 = i >> 2;
        int i4 = 1;
        while (i3 > 8) {
            i4 <<= 1;
            i3 >>= 2;
        }
        int i5 = i >> 1;
        int i6 = i4 * 4;
        if (i3 != 8) {
            int i7 = 0;
            while (i7 < i4) {
                int i8 = i7 * 4;
                int i9 = 0;
                while (i9 < i7) {
                    int i10 = (i9 * 4) + iArr[i4 + i7];
                    int i11 = iArr[i4 + i9] + i8;
                    int i12 = i2 + i10;
                    int i13 = i2 + i11;
                    float f = fArr[i12];
                    int i14 = i12 + 1;
                    float f2 = -fArr[i14];
                    float f3 = fArr[i13];
                    int i15 = i13 + 1;
                    int i16 = i8;
                    float f4 = -fArr[i15];
                    fArr[i12] = f3;
                    fArr[i14] = f4;
                    fArr[i13] = f;
                    fArr[i15] = f2;
                    int i17 = i10 + i6;
                    int i18 = i11 + i6;
                    int i19 = i2 + i17;
                    int i20 = i2 + i18;
                    float f5 = fArr[i19];
                    int i21 = i19 + 1;
                    float f6 = -fArr[i21];
                    float f7 = fArr[i20];
                    int i22 = i20 + 1;
                    float f8 = -fArr[i22];
                    fArr[i19] = f7;
                    fArr[i21] = f8;
                    fArr[i20] = f5;
                    fArr[i22] = f6;
                    int i23 = i17 + i5;
                    int i24 = i18 + 2;
                    int i25 = i2 + i23;
                    int i26 = i2 + i24;
                    float f9 = fArr[i25];
                    int i27 = i25 + 1;
                    float f10 = -fArr[i27];
                    float f11 = fArr[i26];
                    int i28 = i26 + 1;
                    int i29 = i7;
                    float f12 = -fArr[i28];
                    fArr[i25] = f11;
                    fArr[i27] = f12;
                    fArr[i26] = f9;
                    fArr[i28] = f10;
                    int i30 = i23 - i6;
                    int i31 = i24 - i6;
                    int i32 = i2 + i30;
                    int i33 = i2 + i31;
                    float f13 = fArr[i32];
                    int i34 = i32 + 1;
                    float f14 = -fArr[i34];
                    float f15 = fArr[i33];
                    int i35 = i33 + 1;
                    float f16 = -fArr[i35];
                    fArr[i32] = f15;
                    fArr[i34] = f16;
                    fArr[i33] = f13;
                    fArr[i35] = f14;
                    int i36 = i30 + 2;
                    int i37 = i31 + i5;
                    int i38 = i2 + i36;
                    int i39 = i2 + i37;
                    float f17 = fArr[i38];
                    int i40 = i38 + 1;
                    float f18 = -fArr[i40];
                    float f19 = fArr[i39];
                    int i41 = i39 + 1;
                    float f20 = -fArr[i41];
                    fArr[i38] = f19;
                    fArr[i40] = f20;
                    fArr[i39] = f17;
                    fArr[i41] = f18;
                    int i42 = i36 + i6;
                    int i43 = i37 + i6;
                    int i44 = i2 + i42;
                    int i45 = i2 + i43;
                    float f21 = fArr[i44];
                    int i46 = i44 + 1;
                    float f22 = -fArr[i46];
                    float f23 = fArr[i45];
                    int i47 = i45 + 1;
                    float f24 = -fArr[i47];
                    fArr[i44] = f23;
                    fArr[i46] = f24;
                    fArr[i45] = f21;
                    fArr[i47] = f22;
                    int i48 = i42 - i5;
                    int i49 = i43 - 2;
                    int i50 = i2 + i48;
                    int i51 = i2 + i49;
                    float f25 = fArr[i50];
                    int i52 = i50 + 1;
                    float f26 = -fArr[i52];
                    float f27 = fArr[i51];
                    int i53 = i51 + 1;
                    float f28 = -fArr[i53];
                    fArr[i50] = f27;
                    fArr[i52] = f28;
                    fArr[i51] = f25;
                    fArr[i53] = f26;
                    int i54 = i2 + (i48 - i6);
                    int i55 = i2 + (i49 - i6);
                    float f29 = fArr[i54];
                    int i56 = i54 + 1;
                    float f30 = -fArr[i56];
                    float f31 = fArr[i55];
                    int i57 = i55 + 1;
                    float f32 = -fArr[i57];
                    fArr[i54] = f31;
                    fArr[i56] = f32;
                    fArr[i55] = f29;
                    fArr[i57] = f30;
                    i9++;
                    i8 = i16;
                    i7 = i29;
                }
                int i58 = i7;
                int i59 = i8 + iArr[i4 + i58];
                int i60 = i59 + 2;
                int i61 = i59 + i5;
                int i62 = i2 + i60;
                int i63 = i2 + i61;
                int i64 = i62 - 1;
                fArr[i64] = -fArr[i64];
                float f33 = fArr[i62];
                int i65 = i62 + 1;
                float f34 = -fArr[i65];
                float f35 = fArr[i63];
                int i66 = i63 + 1;
                float f36 = -fArr[i66];
                fArr[i62] = f35;
                fArr[i65] = f36;
                fArr[i63] = f33;
                fArr[i66] = f34;
                int i67 = i63 + 3;
                fArr[i67] = -fArr[i67];
                int i68 = i2 + i60 + i6;
                int i69 = i2 + i61 + i6;
                int i70 = i68 - 1;
                fArr[i70] = -fArr[i70];
                float f37 = fArr[i68];
                int i71 = i68 + 1;
                float f38 = -fArr[i71];
                float f39 = fArr[i69];
                int i72 = i69 + 1;
                float f40 = -fArr[i72];
                fArr[i68] = f39;
                fArr[i71] = f40;
                fArr[i69] = f37;
                fArr[i72] = f38;
                int i73 = i69 + 3;
                fArr[i73] = -fArr[i73];
                i7 = i58 + 1;
            }
            return;
        }
        int i74 = 0;
        while (i74 < i4) {
            int i75 = i74 * 4;
            int i76 = 0;
            while (i76 < i74) {
                int i77 = (i76 * 4) + (iArr[i4 + i74] * 2);
                int i78 = (iArr[i4 + i76] * 2) + i75;
                int i79 = i2 + i77;
                int i80 = i2 + i78;
                float f41 = fArr[i79];
                int i81 = i79 + 1;
                float f42 = -fArr[i81];
                float f43 = fArr[i80];
                int i82 = i80 + 1;
                int i83 = i75;
                float f44 = -fArr[i82];
                fArr[i79] = f43;
                fArr[i81] = f44;
                fArr[i80] = f41;
                fArr[i82] = f42;
                int i84 = i77 + i6;
                int i85 = i6 * 2;
                int i86 = i78 + i85;
                int i87 = i2 + i84;
                int i88 = i2 + i86;
                float f45 = fArr[i87];
                int i89 = i87 + 1;
                float f46 = -fArr[i89];
                float f47 = fArr[i88];
                int i90 = i88 + 1;
                float f48 = -fArr[i90];
                fArr[i87] = f47;
                fArr[i89] = f48;
                fArr[i88] = f45;
                fArr[i90] = f46;
                int i91 = i84 + i6;
                int i92 = i86 - i6;
                int i93 = i2 + i91;
                int i94 = i2 + i92;
                float f49 = fArr[i93];
                int i95 = i93 + 1;
                float f50 = -fArr[i95];
                float f51 = fArr[i94];
                int i96 = i94 + 1;
                int i97 = i74;
                float f52 = -fArr[i96];
                fArr[i93] = f51;
                fArr[i95] = f52;
                fArr[i94] = f49;
                fArr[i96] = f50;
                int i98 = i91 + i6;
                int i99 = i92 + i85;
                int i100 = i2 + i98;
                int i101 = i2 + i99;
                float f53 = fArr[i100];
                int i102 = i100 + 1;
                float f54 = -fArr[i102];
                float f55 = fArr[i101];
                int i103 = i101 + 1;
                int i104 = i4;
                float f56 = -fArr[i103];
                fArr[i100] = f55;
                fArr[i102] = f56;
                fArr[i101] = f53;
                fArr[i103] = f54;
                int i105 = i98 + i5;
                int i106 = i99 + 2;
                int i107 = i2 + i105;
                int i108 = i2 + i106;
                float f57 = fArr[i107];
                int i109 = i107 + 1;
                float f58 = -fArr[i109];
                float f59 = fArr[i108];
                int i110 = i108 + 1;
                float f60 = -fArr[i110];
                fArr[i107] = f59;
                fArr[i109] = f60;
                fArr[i108] = f57;
                fArr[i110] = f58;
                int i111 = i105 - i6;
                int i112 = i106 - i85;
                int i113 = i2 + i111;
                int i114 = i2 + i112;
                float f61 = fArr[i113];
                int i115 = i113 + 1;
                float f62 = -fArr[i115];
                float f63 = fArr[i114];
                int i116 = i114 + 1;
                float f64 = -fArr[i116];
                fArr[i113] = f63;
                fArr[i115] = f64;
                fArr[i114] = f61;
                fArr[i116] = f62;
                int i117 = i111 - i6;
                int i118 = i112 + i6;
                int i119 = i2 + i117;
                int i120 = i2 + i118;
                float f65 = fArr[i119];
                int i121 = i119 + 1;
                float f66 = -fArr[i121];
                float f67 = fArr[i120];
                int i122 = i120 + 1;
                float f68 = -fArr[i122];
                fArr[i119] = f67;
                fArr[i121] = f68;
                fArr[i120] = f65;
                fArr[i122] = f66;
                int i123 = i117 - i6;
                int i124 = i118 - i85;
                int i125 = i2 + i123;
                int i126 = i2 + i124;
                float f69 = fArr[i125];
                int i127 = i125 + 1;
                float f70 = -fArr[i127];
                float f71 = fArr[i126];
                int i128 = i126 + 1;
                float f72 = -fArr[i128];
                fArr[i125] = f71;
                fArr[i127] = f72;
                fArr[i126] = f69;
                fArr[i128] = f70;
                int i129 = i123 + 2;
                int i130 = i124 + i5;
                int i131 = i2 + i129;
                int i132 = i2 + i130;
                float f73 = fArr[i131];
                int i133 = i131 + 1;
                float f74 = -fArr[i133];
                float f75 = fArr[i132];
                int i134 = i132 + 1;
                float f76 = -fArr[i134];
                fArr[i131] = f75;
                fArr[i133] = f76;
                fArr[i132] = f73;
                fArr[i134] = f74;
                int i135 = i129 + i6;
                int i136 = i130 + i85;
                int i137 = i2 + i135;
                int i138 = i2 + i136;
                float f77 = fArr[i137];
                int i139 = i137 + 1;
                float f78 = -fArr[i139];
                float f79 = fArr[i138];
                int i140 = i138 + 1;
                float f80 = -fArr[i140];
                fArr[i137] = f79;
                fArr[i139] = f80;
                fArr[i138] = f77;
                fArr[i140] = f78;
                int i141 = i135 + i6;
                int i142 = i136 - i6;
                int i143 = i2 + i141;
                int i144 = i2 + i142;
                float f81 = fArr[i143];
                int i145 = i143 + 1;
                float f82 = -fArr[i145];
                float f83 = fArr[i144];
                int i146 = i144 + 1;
                float f84 = -fArr[i146];
                fArr[i143] = f83;
                fArr[i145] = f84;
                fArr[i144] = f81;
                fArr[i146] = f82;
                int i147 = i141 + i6;
                int i148 = i142 + i85;
                int i149 = i2 + i147;
                int i150 = i2 + i148;
                float f85 = fArr[i149];
                int i151 = i149 + 1;
                float f86 = -fArr[i151];
                float f87 = fArr[i150];
                int i152 = i150 + 1;
                float f88 = -fArr[i152];
                fArr[i149] = f87;
                fArr[i151] = f88;
                fArr[i150] = f85;
                fArr[i152] = f86;
                int i153 = i147 - i5;
                int i154 = i148 - 2;
                int i155 = i2 + i153;
                int i156 = i2 + i154;
                float f89 = fArr[i155];
                int i157 = i155 + 1;
                float f90 = -fArr[i157];
                float f91 = fArr[i156];
                int i158 = i156 + 1;
                float f92 = -fArr[i158];
                fArr[i155] = f91;
                fArr[i157] = f92;
                fArr[i156] = f89;
                fArr[i158] = f90;
                int i159 = i153 - i6;
                int i160 = i154 - i85;
                int i161 = i2 + i159;
                int i162 = i2 + i160;
                float f93 = fArr[i161];
                int i163 = i161 + 1;
                float f94 = -fArr[i163];
                float f95 = fArr[i162];
                int i164 = i162 + 1;
                float f96 = -fArr[i164];
                fArr[i161] = f95;
                fArr[i163] = f96;
                fArr[i162] = f93;
                fArr[i164] = f94;
                int i165 = i159 - i6;
                int i166 = i160 + i6;
                int i167 = i2 + i165;
                int i168 = i2 + i166;
                float f97 = fArr[i167];
                int i169 = i167 + 1;
                float f98 = -fArr[i169];
                float f99 = fArr[i168];
                int i170 = i168 + 1;
                float f100 = -fArr[i170];
                fArr[i167] = f99;
                fArr[i169] = f100;
                fArr[i168] = f97;
                fArr[i170] = f98;
                int i171 = i2 + (i165 - i6);
                int i172 = i2 + (i166 - i85);
                float f101 = fArr[i171];
                int i173 = i171 + 1;
                float f102 = -fArr[i173];
                float f103 = fArr[i172];
                int i174 = i172 + 1;
                float f104 = -fArr[i174];
                fArr[i171] = f103;
                fArr[i173] = f104;
                fArr[i172] = f101;
                fArr[i174] = f102;
                i76++;
                i75 = i83;
                i74 = i97;
                i4 = i104;
            }
            int i175 = i74;
            int i176 = i4;
            int i177 = i75 + (iArr[i176 + i175] * 2);
            int i178 = i177 + 2;
            int i179 = i177 + i5;
            int i180 = i2 + i178;
            int i181 = i2 + i179;
            int i182 = i180 - 1;
            fArr[i182] = -fArr[i182];
            float f105 = fArr[i180];
            int i183 = i180 + 1;
            float f106 = -fArr[i183];
            float f107 = fArr[i181];
            int i184 = i181 + 1;
            float f108 = -fArr[i184];
            fArr[i180] = f107;
            fArr[i183] = f108;
            fArr[i181] = f105;
            fArr[i184] = f106;
            int i185 = i181 + 3;
            fArr[i185] = -fArr[i185];
            int i186 = i178 + i6;
            int i187 = i6 * 2;
            int i188 = i179 + i187;
            int i189 = i2 + i186;
            int i190 = i2 + i188;
            float f109 = fArr[i189];
            int i191 = i189 + 1;
            float f110 = -fArr[i191];
            float f111 = fArr[i190];
            int i192 = i190 + 1;
            float f112 = -fArr[i192];
            fArr[i189] = f111;
            fArr[i191] = f112;
            fArr[i190] = f109;
            fArr[i192] = f110;
            int i193 = i186 + i6;
            int i194 = i188 - i6;
            int i195 = i2 + i193;
            int i196 = i2 + i194;
            float f113 = fArr[i195];
            int i197 = i195 + 1;
            float f114 = -fArr[i197];
            float f115 = fArr[i196];
            int i198 = i196 + 1;
            float f116 = -fArr[i198];
            fArr[i195] = f115;
            fArr[i197] = f116;
            fArr[i196] = f113;
            fArr[i198] = f114;
            int i199 = i193 - 2;
            int i200 = i194 - i5;
            int i201 = i2 + i199;
            int i202 = i2 + i200;
            float f117 = fArr[i201];
            int i203 = i201 + 1;
            float f118 = -fArr[i203];
            float f119 = fArr[i202];
            int i204 = i202 + 1;
            float f120 = -fArr[i204];
            fArr[i201] = f119;
            fArr[i203] = f120;
            fArr[i202] = f117;
            fArr[i204] = f118;
            int i205 = i5 + 2;
            int i206 = i199 + i205;
            int i207 = i200 + i205;
            int i208 = i2 + i206;
            int i209 = i2 + i207;
            float f121 = fArr[i208];
            int i210 = i208 + 1;
            float f122 = -fArr[i210];
            float f123 = fArr[i209];
            int i211 = i209 + 1;
            float f124 = -fArr[i211];
            fArr[i208] = f123;
            fArr[i210] = f124;
            fArr[i209] = f121;
            fArr[i211] = f122;
            int i212 = i2 + (i206 - (i5 - i6));
            int i213 = i2 + i207 + (i187 - 2);
            int i214 = i212 - 1;
            fArr[i214] = -fArr[i214];
            float f125 = fArr[i212];
            int i215 = i212 + 1;
            float f126 = -fArr[i215];
            float f127 = fArr[i213];
            int i216 = i213 + 1;
            float f128 = -fArr[i216];
            fArr[i212] = f127;
            fArr[i215] = f128;
            fArr[i213] = f125;
            fArr[i216] = f126;
            int i217 = i213 + 3;
            fArr[i217] = -fArr[i217];
            i74 = i175 + 1;
            i4 = i176;
        }
    }

    private void bitrv216(float[] fArr, int i) {
        int i2 = i + 2;
        float f = fArr[i2];
        int i3 = i + 3;
        float f2 = fArr[i3];
        int i4 = i + 4;
        float f3 = fArr[i4];
        int i5 = i + 5;
        float f4 = fArr[i5];
        int i6 = i + 6;
        float f5 = fArr[i6];
        int i7 = i + 7;
        float f6 = fArr[i7];
        int i8 = i + 8;
        float f7 = fArr[i8];
        int i9 = i + 9;
        float f8 = fArr[i9];
        int i10 = i + 10;
        float f9 = fArr[i10];
        int i11 = i + 11;
        float f10 = fArr[i11];
        int i12 = i + 14;
        float f11 = fArr[i12];
        int i13 = i + 15;
        float f12 = fArr[i13];
        int i14 = i + 16;
        float f13 = fArr[i14];
        int i15 = i + 17;
        float f14 = fArr[i15];
        int i16 = i + 20;
        float f15 = fArr[i16];
        int i17 = i + 21;
        float f16 = fArr[i17];
        int i18 = i + 22;
        float f17 = fArr[i18];
        int i19 = i + 23;
        float f18 = fArr[i19];
        int i20 = i + 24;
        float f19 = fArr[i20];
        int i21 = i + 25;
        float f20 = fArr[i21];
        int i22 = i + 26;
        float f21 = fArr[i22];
        int i23 = i + 27;
        float f22 = fArr[i23];
        int i24 = i + 28;
        float f23 = fArr[i24];
        int i25 = i + 29;
        float f24 = fArr[i25];
        fArr[i2] = f13;
        fArr[i3] = f14;
        fArr[i4] = f7;
        fArr[i5] = f8;
        fArr[i6] = f19;
        fArr[i7] = f20;
        fArr[i8] = f3;
        fArr[i9] = f4;
        fArr[i10] = f15;
        fArr[i11] = f16;
        fArr[i12] = f23;
        fArr[i13] = f24;
        fArr[i14] = f;
        fArr[i15] = f2;
        fArr[i16] = f9;
        fArr[i17] = f10;
        fArr[i18] = f21;
        fArr[i19] = f22;
        fArr[i20] = f5;
        fArr[i21] = f6;
        fArr[i22] = f17;
        fArr[i23] = f18;
        fArr[i24] = f11;
        fArr[i25] = f12;
    }

    private void bitrv216neg(float[] fArr, int i) {
        int i2 = i + 2;
        float f = fArr[i2];
        int i3 = i + 3;
        float f2 = fArr[i3];
        int i4 = i + 4;
        float f3 = fArr[i4];
        int i5 = i + 5;
        float f4 = fArr[i5];
        int i6 = i + 6;
        float f5 = fArr[i6];
        int i7 = i + 7;
        float f6 = fArr[i7];
        int i8 = i + 8;
        float f7 = fArr[i8];
        int i9 = i + 9;
        float f8 = fArr[i9];
        int i10 = i + 10;
        float f9 = fArr[i10];
        int i11 = i + 11;
        float f10 = fArr[i11];
        int i12 = i + 12;
        float f11 = fArr[i12];
        int i13 = i + 13;
        float f12 = fArr[i13];
        int i14 = i + 14;
        float f13 = fArr[i14];
        int i15 = i + 15;
        float f14 = fArr[i15];
        int i16 = i + 16;
        float f15 = fArr[i16];
        int i17 = i + 17;
        float f16 = fArr[i17];
        int i18 = i + 18;
        float f17 = fArr[i18];
        int i19 = i + 19;
        float f18 = fArr[i19];
        int i20 = i + 20;
        float f19 = fArr[i20];
        int i21 = i + 21;
        float f20 = fArr[i21];
        int i22 = i + 22;
        float f21 = fArr[i22];
        int i23 = i + 23;
        float f22 = fArr[i23];
        int i24 = i + 24;
        float f23 = fArr[i24];
        int i25 = i + 25;
        float f24 = fArr[i25];
        int i26 = i + 26;
        float f25 = fArr[i26];
        int i27 = i + 27;
        float f26 = fArr[i27];
        int i28 = i + 28;
        float f27 = fArr[i28];
        int i29 = i + 29;
        float f28 = fArr[i29];
        int i30 = i + 30;
        float f29 = fArr[i30];
        int i31 = i + 31;
        float f30 = fArr[i31];
        fArr[i2] = f29;
        fArr[i3] = f30;
        fArr[i4] = f13;
        fArr[i5] = f14;
        fArr[i6] = f21;
        fArr[i7] = f22;
        fArr[i8] = f5;
        fArr[i9] = f6;
        fArr[i10] = f25;
        fArr[i11] = f26;
        fArr[i12] = f9;
        fArr[i13] = f10;
        fArr[i14] = f17;
        fArr[i15] = f18;
        fArr[i16] = f;
        fArr[i17] = f2;
        fArr[i18] = f27;
        fArr[i19] = f28;
        fArr[i20] = f11;
        fArr[i21] = f12;
        fArr[i22] = f19;
        fArr[i23] = f20;
        fArr[i24] = f3;
        fArr[i25] = f4;
        fArr[i26] = f23;
        fArr[i27] = f24;
        fArr[i28] = f7;
        fArr[i29] = f8;
        fArr[i30] = f15;
        fArr[i31] = f16;
    }

    private void bitrv208(float[] fArr, int i) {
        int i2 = i + 2;
        float f = fArr[i2];
        int i3 = i + 3;
        float f2 = fArr[i3];
        int i4 = i + 6;
        float f3 = fArr[i4];
        int i5 = i + 7;
        float f4 = fArr[i5];
        int i6 = i + 8;
        float f5 = fArr[i6];
        int i7 = i + 9;
        float f6 = fArr[i7];
        int i8 = i + 12;
        float f7 = fArr[i8];
        int i9 = i + 13;
        float f8 = fArr[i9];
        fArr[i2] = f5;
        fArr[i3] = f6;
        fArr[i4] = f7;
        fArr[i5] = f8;
        fArr[i6] = f;
        fArr[i7] = f2;
        fArr[i8] = f3;
        fArr[i9] = f4;
    }

    private void bitrv208neg(float[] fArr, int i) {
        int i2 = i + 2;
        float f = fArr[i2];
        int i3 = i + 3;
        float f2 = fArr[i3];
        int i4 = i + 4;
        float f3 = fArr[i4];
        int i5 = i + 5;
        float f4 = fArr[i5];
        int i6 = i + 6;
        float f5 = fArr[i6];
        int i7 = i + 7;
        float f6 = fArr[i7];
        int i8 = i + 8;
        float f7 = fArr[i8];
        int i9 = i + 9;
        float f8 = fArr[i9];
        int i10 = i + 10;
        float f9 = fArr[i10];
        int i11 = i + 11;
        float f10 = fArr[i11];
        int i12 = i + 12;
        float f11 = fArr[i12];
        int i13 = i + 13;
        float f12 = fArr[i13];
        int i14 = i + 14;
        float f13 = fArr[i14];
        int i15 = i + 15;
        float f14 = fArr[i15];
        fArr[i2] = f13;
        fArr[i3] = f14;
        fArr[i4] = f5;
        fArr[i5] = f6;
        fArr[i6] = f9;
        fArr[i7] = f10;
        fArr[i8] = f;
        fArr[i9] = f2;
        fArr[i10] = f11;
        fArr[i11] = f12;
        fArr[i12] = f3;
        fArr[i13] = f4;
        fArr[i14] = f7;
        fArr[i15] = f8;
    }

    private void cftf1st(int i, float[] fArr, int i2, float[] fArr2, int i3) {
        int i4 = i >> 3;
        int i5 = i4 * 2;
        int i6 = i5 + i5;
        int i7 = i6 + i5;
        int i8 = i2 + i5;
        int i9 = i2 + i6;
        int i10 = i2 + i7;
        float f = fArr[i2] + fArr[i9];
        int i11 = i2 + 1;
        int i12 = i9 + 1;
        float f2 = fArr[i11] + fArr[i12];
        float f3 = fArr[i2] - fArr[i9];
        float f4 = fArr[i11] - fArr[i12];
        float f5 = fArr[i8] + fArr[i10];
        int i13 = i8 + 1;
        int i14 = i10 + 1;
        float f6 = fArr[i13] + fArr[i14];
        float f7 = fArr[i8] - fArr[i10];
        float f8 = fArr[i13] - fArr[i14];
        fArr[i2] = f + f5;
        fArr[i11] = f2 + f6;
        fArr[i8] = f - f5;
        fArr[i13] = f2 - f6;
        fArr[i9] = f3 - f8;
        fArr[i12] = f4 + f7;
        fArr[i10] = f3 + f8;
        fArr[i14] = f4 - f7;
        float f9 = fArr2[i3 + 1];
        float f10 = fArr2[i3 + 2];
        float f11 = fArr2[i3 + 3];
        float f12 = 0.0f;
        float f13 = 1.0f;
        int i15 = 2;
        int i16 = 0;
        float f14 = 1.0f;
        float f15 = 0.0f;
        while (i15 < i4 - 2) {
            i16 += 4;
            int i17 = i3 + i16;
            float f16 = (f13 + fArr2[i17]) * f10;
            int i18 = i17 + 1;
            float f17 = (f12 + fArr2[i18]) * f10;
            int i19 = i17 + 2;
            float f18 = (f14 + fArr2[i19]) * f11;
            int i20 = i17 + 3;
            float f19 = (f15 + fArr2[i20]) * f11;
            float f20 = fArr2[i17];
            float f21 = fArr2[i18];
            float f22 = fArr2[i19];
            float f23 = fArr2[i20];
            int i21 = i15 + i5;
            int i22 = i21 + i5;
            int i23 = i22 + i5;
            int i24 = i2 + i21;
            int i25 = i2 + i22;
            int i26 = i2 + i23;
            int i27 = i2 + i15;
            float f24 = fArr[i27] + fArr[i25];
            int i28 = i27 + 1;
            int i29 = i25 + 1;
            float f25 = fArr[i28] + fArr[i29];
            float f26 = fArr[i27] - fArr[i25];
            float f27 = fArr[i28] - fArr[i29];
            int i30 = i27 + 2;
            int i31 = i25 + 2;
            float f28 = fArr[i30] + fArr[i31];
            int i32 = i27 + 3;
            int i33 = i25 + 3;
            float f29 = fArr[i32] + fArr[i33];
            float f30 = fArr[i30] - fArr[i31];
            float f31 = fArr[i32] - fArr[i33];
            float f32 = fArr[i24] + fArr[i26];
            int i34 = i24 + 1;
            int i35 = i26 + 1;
            float f33 = fArr[i34] + fArr[i35];
            float f34 = fArr[i24] - fArr[i26];
            float f35 = fArr[i34] - fArr[i35];
            int i36 = i24 + 2;
            int i37 = i26 + 2;
            float f36 = fArr[i36] + fArr[i37];
            int i38 = i24 + 3;
            int i39 = i26 + 3;
            float f37 = fArr[i38] + fArr[i39];
            float f38 = fArr[i36] - fArr[i37];
            float f39 = fArr[i38] - fArr[i39];
            fArr[i27] = f24 + f32;
            fArr[i28] = f25 + f33;
            fArr[i30] = f28 + f36;
            fArr[i32] = f29 + f37;
            fArr[i24] = f24 - f32;
            fArr[i34] = f25 - f33;
            fArr[i36] = f28 - f36;
            fArr[i38] = f29 - f37;
            float f40 = f26 - f35;
            float f41 = f27 + f34;
            fArr[i25] = (f16 * f40) - (f17 * f41);
            fArr[i29] = (f41 * f16) + (f40 * f17);
            float f42 = f30 - f39;
            float f43 = f31 + f38;
            fArr[i31] = (f20 * f42) - (f21 * f43);
            fArr[i33] = (f43 * f20) + (f42 * f21);
            float f44 = f26 + f35;
            float f45 = f27 - f34;
            fArr[i26] = (f18 * f44) + (f19 * f45);
            fArr[i35] = (f45 * f18) - (f44 * f19);
            float f46 = f30 + f39;
            float f47 = f31 - f38;
            fArr[i37] = (f22 * f46) + (f23 * f47);
            fArr[i39] = (f47 * f22) - (f46 * f23);
            int i40 = i5 - i15;
            int i41 = i40 + i5;
            int i42 = i41 + i5;
            int i43 = i42 + i5;
            int i44 = i2 + i40;
            int i45 = i2 + i41;
            int i46 = i2 + i42;
            int i47 = i2 + i43;
            float f48 = fArr[i44] + fArr[i46];
            int i48 = i44 + 1;
            int i49 = i46 + 1;
            float f49 = fArr[i48] + fArr[i49];
            float f50 = fArr[i44] - fArr[i46];
            float f51 = fArr[i48] - fArr[i49];
            int i50 = i44 - 2;
            int i51 = i46 - 2;
            float f52 = fArr[i50] + fArr[i51];
            int i52 = i44 - 1;
            int i53 = i46 - 1;
            float f53 = fArr[i52] + fArr[i53];
            float f54 = fArr[i50] - fArr[i51];
            float f55 = fArr[i52] - fArr[i53];
            float f56 = fArr[i45] + fArr[i47];
            int i54 = i45 + 1;
            int i55 = i47 + 1;
            float f57 = fArr[i54] + fArr[i55];
            float f58 = fArr[i45] - fArr[i47];
            float f59 = fArr[i54] - fArr[i55];
            int i56 = i45 - 2;
            int i57 = i47 - 2;
            float f60 = fArr[i56] + fArr[i57];
            int i58 = i45 - 1;
            int i59 = i47 - 1;
            float f61 = fArr[i58] + fArr[i59];
            float f62 = fArr[i56] - fArr[i57];
            float f63 = fArr[i58] - fArr[i59];
            fArr[i44] = f48 + f56;
            fArr[i48] = f49 + f57;
            fArr[i50] = f52 + f60;
            fArr[i52] = f53 + f61;
            fArr[i45] = f48 - f56;
            fArr[i54] = f49 - f57;
            fArr[i56] = f52 - f60;
            fArr[i58] = f53 - f61;
            float f64 = f50 - f59;
            float f65 = f51 + f58;
            fArr[i46] = (f17 * f64) - (f16 * f65);
            fArr[i49] = (f17 * f65) + (f16 * f64);
            float f66 = f54 - f63;
            float f67 = f55 + f62;
            fArr[i51] = (f21 * f66) - (f20 * f67);
            fArr[i53] = (f67 * f21) + (f66 * f20);
            float f68 = f50 + f59;
            float f69 = f51 - f58;
            fArr[i47] = (f19 * f68) + (f18 * f69);
            fArr[i55] = (f19 * f69) - (f18 * f68);
            float f70 = f54 + f63;
            float f71 = f55 - f62;
            fArr[i57] = (f23 * f70) + (f22 * f71);
            fArr[i59] = (f71 * f23) - (f70 * f22);
            i15 += 4;
            f13 = f20;
            f12 = f21;
            f14 = f22;
            f15 = f23;
        }
        float f72 = (f13 + f9) * f10;
        float f73 = f10 * (f12 + f9);
        float f74 = (f14 - f9) * f11;
        float f75 = f11 * (f15 - f9);
        int i60 = i4 + i5;
        int i61 = i60 + i5;
        int i62 = i5 + i61;
        int i63 = i2 + i4;
        int i64 = i2 + i60;
        int i65 = i2 + i61;
        int i66 = i2 + i62;
        int i67 = i63 - 2;
        int i68 = i65 - 2;
        float f76 = fArr[i67] + fArr[i68];
        int i69 = i63 - 1;
        int i70 = i65 - 1;
        float f77 = fArr[i69] + fArr[i70];
        float f78 = fArr[i67] - fArr[i68];
        float f79 = fArr[i69] - fArr[i70];
        int i71 = i64 - 2;
        int i72 = i66 - 2;
        float f80 = fArr[i71] + fArr[i72];
        int i73 = i64 - 1;
        int i74 = i66 - 1;
        float f81 = fArr[i73] + fArr[i74];
        float f82 = fArr[i71] - fArr[i72];
        float f83 = fArr[i73] - fArr[i74];
        fArr[i67] = f76 + f80;
        fArr[i69] = f77 + f81;
        fArr[i71] = f76 - f80;
        fArr[i73] = f77 - f81;
        float f84 = f78 - f83;
        float f85 = f79 + f82;
        fArr[i68] = (f72 * f84) - (f73 * f85);
        fArr[i70] = (f85 * f72) + (f84 * f73);
        float f86 = f78 + f83;
        float f87 = f79 - f82;
        fArr[i72] = (f74 * f86) + (f75 * f87);
        fArr[i74] = (f87 * f74) - (f86 * f75);
        float f88 = fArr[i63] + fArr[i65];
        int i75 = i63 + 1;
        int i76 = i65 + 1;
        float f89 = fArr[i75] + fArr[i76];
        float f90 = fArr[i63] - fArr[i65];
        float f91 = fArr[i75] - fArr[i76];
        float f92 = fArr[i64] + fArr[i66];
        int i77 = i64 + 1;
        int i78 = i66 + 1;
        float f93 = fArr[i77] + fArr[i78];
        float f94 = fArr[i64] - fArr[i66];
        float f95 = fArr[i77] - fArr[i78];
        fArr[i63] = f88 + f92;
        fArr[i75] = f89 + f93;
        fArr[i64] = f88 - f92;
        fArr[i77] = f89 - f93;
        float f96 = f90 - f95;
        float f97 = f91 + f94;
        fArr[i65] = (f96 - f97) * f9;
        fArr[i76] = (f97 + f96) * f9;
        float f98 = f90 + f95;
        float f99 = f91 - f94;
        float f100 = -f9;
        fArr[i66] = (f98 + f99) * f100;
        fArr[i78] = f100 * (f99 - f98);
        int i79 = i63 + 2;
        int i80 = i65 + 2;
        float f101 = fArr[i79] + fArr[i80];
        int i81 = i63 + 3;
        int i82 = i65 + 3;
        float f102 = fArr[i81] + fArr[i82];
        float f103 = fArr[i79] - fArr[i80];
        float f104 = fArr[i81] - fArr[i82];
        int i83 = i64 + 2;
        int i84 = i66 + 2;
        float f105 = fArr[i83] + fArr[i84];
        int i85 = i64 + 3;
        int i86 = i66 + 3;
        float f106 = fArr[i85] + fArr[i86];
        float f107 = fArr[i83] - fArr[i84];
        float f108 = fArr[i85] - fArr[i86];
        fArr[i79] = f101 + f105;
        fArr[i81] = f102 + f106;
        fArr[i83] = f101 - f105;
        fArr[i85] = f102 - f106;
        float f109 = f103 - f108;
        float f110 = f104 + f107;
        fArr[i80] = (f73 * f109) - (f72 * f110);
        fArr[i82] = (f73 * f110) + (f72 * f109);
        float f111 = f103 + f108;
        float f112 = f104 - f107;
        fArr[i84] = (f75 * f111) + (f74 * f112);
        fArr[i86] = (f75 * f112) - (f74 * f111);
    }

    private void cftb1st(int i, float[] fArr, int i2, float[] fArr2, int i3) {
        int i4 = i >> 3;
        int i5 = i4 * 2;
        int i6 = i5 + i5;
        int i7 = i6 + i5;
        int i8 = i2 + i5;
        int i9 = i2 + i6;
        int i10 = i2 + i7;
        float f = fArr[i2] + fArr[i9];
        int i11 = i2 + 1;
        int i12 = i9 + 1;
        float f2 = (-fArr[i11]) - fArr[i12];
        float f3 = fArr[i2] - fArr[i9];
        float f4 = (-fArr[i11]) + fArr[i12];
        float f5 = fArr[i8] + fArr[i10];
        int i13 = i8 + 1;
        int i14 = i10 + 1;
        float f6 = fArr[i13] + fArr[i14];
        float f7 = fArr[i8] - fArr[i10];
        float f8 = fArr[i13] - fArr[i14];
        fArr[i2] = f + f5;
        fArr[i11] = f2 - f6;
        fArr[i8] = f - f5;
        fArr[i13] = f2 + f6;
        fArr[i9] = f3 + f8;
        fArr[i12] = f4 + f7;
        fArr[i10] = f3 - f8;
        fArr[i14] = f4 - f7;
        float f9 = fArr2[i3 + 1];
        float f10 = fArr2[i3 + 2];
        float f11 = fArr2[i3 + 3];
        float f12 = 0.0f;
        float f13 = 1.0f;
        int i15 = 2;
        int i16 = 0;
        float f14 = 1.0f;
        float f15 = 0.0f;
        while (i15 < i4 - 2) {
            int i17 = i16 + 4;
            int i18 = i3 + i17;
            float f16 = (f13 + fArr2[i18]) * f10;
            int i19 = i18 + 1;
            float f17 = (f12 + fArr2[i19]) * f10;
            int i20 = i18 + 2;
            float f18 = (f14 + fArr2[i20]) * f11;
            int i21 = i18 + 3;
            float f19 = (f15 + fArr2[i21]) * f11;
            float f20 = fArr2[i18];
            float f21 = fArr2[i19];
            float f22 = fArr2[i20];
            float f23 = fArr2[i21];
            int i22 = i15 + i5;
            int i23 = i22 + i5;
            int i24 = i23 + i5;
            int i25 = i2 + i22;
            int i26 = i2 + i23;
            int i27 = i2 + i24;
            int i28 = i2 + i15;
            float f24 = fArr[i28] + fArr[i26];
            int i29 = i28 + 1;
            int i30 = i26 + 1;
            float f25 = (-fArr[i29]) - fArr[i30];
            float f26 = fArr[i28] - fArr[i26];
            float f27 = (-fArr[i29]) + fArr[i30];
            int i31 = i28 + 2;
            int i32 = i26 + 2;
            float f28 = fArr[i31] + fArr[i32];
            int i33 = i28 + 3;
            int i34 = i26 + 3;
            float f29 = (-fArr[i33]) - fArr[i34];
            float f30 = fArr[i31] - fArr[i32];
            int i35 = i4;
            float f31 = (-fArr[i33]) + fArr[i34];
            float f32 = fArr[i25] + fArr[i27];
            int i36 = i25 + 1;
            int i37 = i27 + 1;
            float f33 = fArr[i36] + fArr[i37];
            float f34 = fArr[i25] - fArr[i27];
            float f35 = fArr[i36] - fArr[i37];
            int i38 = i25 + 2;
            int i39 = i27 + 2;
            float f36 = fArr[i38] + fArr[i39];
            int i40 = i25 + 3;
            int i41 = i27 + 3;
            float f37 = fArr[i40] + fArr[i41];
            float f38 = fArr[i38] - fArr[i39];
            float f39 = fArr[i40] - fArr[i41];
            fArr[i28] = f24 + f32;
            fArr[i29] = f25 - f33;
            fArr[i31] = f28 + f36;
            fArr[i33] = f29 - f37;
            fArr[i25] = f24 - f32;
            fArr[i36] = f25 + f33;
            fArr[i38] = f28 - f36;
            fArr[i40] = f29 + f37;
            float f40 = f26 + f35;
            float f41 = f27 + f34;
            fArr[i26] = (f16 * f40) - (f17 * f41);
            fArr[i30] = (f41 * f16) + (f40 * f17);
            float f42 = f30 + f39;
            float f43 = f31 + f38;
            fArr[i32] = (f20 * f42) - (f21 * f43);
            fArr[i34] = (f43 * f20) + (f42 * f21);
            float f44 = f26 - f35;
            float f45 = f27 - f34;
            fArr[i27] = (f18 * f44) + (f19 * f45);
            fArr[i37] = (f45 * f18) - (f44 * f19);
            float f46 = f30 - f39;
            float f47 = f31 - f38;
            fArr[i39] = (f22 * f46) + (f23 * f47);
            fArr[i41] = (f47 * f22) - (f46 * f23);
            int i42 = i5 - i15;
            int i43 = i42 + i5;
            int i44 = i43 + i5;
            int i45 = i44 + i5;
            int i46 = i2 + i42;
            int i47 = i2 + i43;
            int i48 = i2 + i44;
            int i49 = i2 + i45;
            float f48 = fArr[i46] + fArr[i48];
            int i50 = i46 + 1;
            int i51 = i48 + 1;
            float f49 = (-fArr[i50]) - fArr[i51];
            float f50 = fArr[i46] - fArr[i48];
            int i52 = i5;
            float f51 = (-fArr[i50]) + fArr[i51];
            int i53 = i46 - 2;
            int i54 = i48 - 2;
            float f52 = fArr[i53] + fArr[i54];
            int i55 = i46 - 1;
            float f53 = f11;
            int i56 = i48 - 1;
            float f54 = (-fArr[i55]) - fArr[i56];
            float f55 = fArr[i53] - fArr[i54];
            float f56 = f10;
            float f57 = (-fArr[i55]) + fArr[i56];
            float f58 = fArr[i47] + fArr[i49];
            int i57 = i47 + 1;
            int i58 = i49 + 1;
            float f59 = fArr[i57] + fArr[i58];
            float f60 = fArr[i47] - fArr[i49];
            float f61 = fArr[i57] - fArr[i58];
            int i59 = i47 - 2;
            int i60 = i49 - 2;
            float f62 = fArr[i59] + fArr[i60];
            int i61 = i47 - 1;
            int i62 = i49 - 1;
            float f63 = fArr[i61] + fArr[i62];
            float f64 = fArr[i59] - fArr[i60];
            float f65 = fArr[i61] - fArr[i62];
            fArr[i46] = f48 + f58;
            fArr[i50] = f49 - f59;
            fArr[i53] = f52 + f62;
            fArr[i55] = f54 - f63;
            fArr[i47] = f48 - f58;
            fArr[i57] = f49 + f59;
            fArr[i59] = f52 - f62;
            fArr[i61] = f54 + f63;
            float f66 = f50 + f61;
            float f67 = f51 + f60;
            fArr[i48] = (f17 * f66) - (f16 * f67);
            fArr[i51] = (f17 * f67) + (f16 * f66);
            float f68 = f55 + f65;
            float f69 = f57 + f64;
            fArr[i54] = (f21 * f68) - (f20 * f69);
            fArr[i56] = (f69 * f21) + (f68 * f20);
            float f70 = f50 - f61;
            float f71 = f51 - f60;
            fArr[i49] = (f19 * f70) + (f18 * f71);
            fArr[i58] = (f19 * f71) - (f18 * f70);
            float f72 = f55 - f65;
            float f73 = f57 - f64;
            fArr[i60] = (f23 * f72) + (f22 * f73);
            fArr[i62] = (f73 * f23) - (f72 * f22);
            i15 += 4;
            f13 = f20;
            f12 = f21;
            f14 = f22;
            f15 = f23;
            i16 = i17;
            i4 = i35;
            i5 = i52;
            f11 = f53;
            f10 = f56;
        }
        int i63 = i4;
        int i64 = i5;
        float f74 = f10;
        float f75 = f11;
        float f76 = f74 * (f13 + f9);
        float f77 = f74 * (f12 + f9);
        float f78 = f75 * (f14 - f9);
        float f79 = f75 * (f15 - f9);
        int i65 = i63 + i64;
        int i66 = i65 + i64;
        int i67 = i66 + i64;
        int i68 = i2 + i63;
        int i69 = i2 + i65;
        int i70 = i2 + i66;
        int i71 = i2 + i67;
        int i72 = i68 - 2;
        int i73 = i70 - 2;
        float f80 = fArr[i72] + fArr[i73];
        int i74 = i68 - 1;
        int i75 = i70 - 1;
        float f81 = (-fArr[i74]) - fArr[i75];
        float f82 = fArr[i72] - fArr[i73];
        float f83 = (-fArr[i74]) + fArr[i75];
        int i76 = i69 - 2;
        int i77 = i71 - 2;
        float f84 = fArr[i76] + fArr[i77];
        int i78 = i69 - 1;
        int i79 = i71 - 1;
        float f85 = fArr[i78] + fArr[i79];
        float f86 = fArr[i76] - fArr[i77];
        float f87 = fArr[i78] - fArr[i79];
        fArr[i72] = f80 + f84;
        fArr[i74] = f81 - f85;
        fArr[i76] = f80 - f84;
        fArr[i78] = f81 + f85;
        float f88 = f82 + f87;
        float f89 = f83 + f86;
        fArr[i73] = (f76 * f88) - (f77 * f89);
        fArr[i75] = (f89 * f76) + (f88 * f77);
        float f90 = f82 - f87;
        float f91 = f83 - f86;
        fArr[i77] = (f78 * f90) + (f79 * f91);
        fArr[i79] = (f91 * f78) - (f90 * f79);
        float f92 = fArr[i68] + fArr[i70];
        int i80 = i68 + 1;
        int i81 = i70 + 1;
        float f93 = (-fArr[i80]) - fArr[i81];
        float f94 = fArr[i68] - fArr[i70];
        float f95 = (-fArr[i80]) + fArr[i81];
        float f96 = fArr[i69] + fArr[i71];
        int i82 = i69 + 1;
        int i83 = i71 + 1;
        float f97 = fArr[i82] + fArr[i83];
        float f98 = fArr[i69] - fArr[i71];
        float f99 = fArr[i82] - fArr[i83];
        fArr[i68] = f92 + f96;
        fArr[i80] = f93 - f97;
        fArr[i69] = f92 - f96;
        fArr[i82] = f93 + f97;
        float f100 = f94 + f99;
        float f101 = f95 + f98;
        fArr[i70] = (f100 - f101) * f9;
        fArr[i81] = f9 * (f101 + f100);
        float f102 = f94 - f99;
        float f103 = f95 - f98;
        float f104 = -f9;
        fArr[i71] = (f102 + f103) * f104;
        fArr[i83] = f104 * (f103 - f102);
        int i84 = i68 + 2;
        int i85 = i70 + 2;
        float f105 = fArr[i84] + fArr[i85];
        int i86 = i68 + 3;
        int i87 = i70 + 3;
        float f106 = (-fArr[i86]) - fArr[i87];
        float f107 = fArr[i84] - fArr[i85];
        float f108 = (-fArr[i86]) + fArr[i87];
        int i88 = i69 + 2;
        int i89 = i71 + 2;
        float f109 = fArr[i88] + fArr[i89];
        int i90 = i69 + 3;
        int i91 = i71 + 3;
        float f110 = fArr[i90] + fArr[i91];
        float f111 = fArr[i88] - fArr[i89];
        float f112 = fArr[i90] - fArr[i91];
        fArr[i84] = f105 + f109;
        fArr[i86] = f106 - f110;
        fArr[i88] = f105 - f109;
        fArr[i90] = f106 + f110;
        float f113 = f107 + f112;
        float f114 = f108 + f111;
        fArr[i85] = (f77 * f113) - (f76 * f114);
        fArr[i87] = (f77 * f114) + (f76 * f113);
        float f115 = f107 - f112;
        float f116 = f108 - f111;
        fArr[i89] = (f79 * f115) + (f78 * f116);
        fArr[i91] = (f79 * f116) - (f78 * f115);
    }

    private void cftrec4_th(final int i, final float[] fArr, int i2, final int i3, final float[] fArr2) throws ExecutionException, InterruptedException {
        int i4;
        int i5;
        int i6;
        int i7;
        int i8 = i >> 1;
        if (i > ConcurrencyUtils.getThreadsBeginN_1D_FFT_4Threads()) {
            i4 = i8 >> 1;
            i5 = 4;
            i6 = 1;
        } else {
            i4 = i8;
            i5 = 2;
            i6 = 0;
        }
        Future[] futureArr = new Future[i5];
        int i9 = 0;
        for (int i10 = 0; i10 < i5; i10++) {
            final int i11 = i2 + (i10 * i4);
            if (i10 != i6) {
                i7 = i9 + 1;
                final int i12 = i4;
                futureArr[i9] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.15
                    @Override // java.lang.Runnable
                    public void run() {
                        int i13 = i11 + i12;
                        int i14 = i;
                        while (i14 > 512) {
                            i14 >>= 2;
                            FloatFFT.this.cftmdl1(i14, fArr, i13 - i14, fArr2, i3 - (i14 >> 1));
                        }
                        FloatFFT.this.cftleaf(i14, 1, fArr, i13 - i14, i3, fArr2);
                        int i15 = i11 - i14;
                        int i16 = i12 - i14;
                        int i17 = 0;
                        while (i16 > 0) {
                            int i18 = i17 + 1;
                            int i19 = i14;
                            FloatFFT.this.cftleaf(i19, FloatFFT.this.cfttree(i19, i16, i18, fArr, i11, i3, fArr2), fArr, i15 + i16, i3, fArr2);
                            i16 -= i14;
                            i17 = i18;
                        }
                    }
                });
            } else {
                i7 = i9 + 1;
                final int i13 = i4;
                futureArr[i9] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.16
                    @Override // java.lang.Runnable
                    public void run() {
                        int i14 = i11 + i13;
                        int i15 = i;
                        int i16 = 1;
                        while (i15 > 512) {
                            i15 >>= 2;
                            i16 <<= 2;
                            FloatFFT.this.cftmdl2(i15, fArr, i14 - i15, fArr2, i3 - i15);
                        }
                        FloatFFT.this.cftleaf(i15, 0, fArr, i14 - i15, i3, fArr2);
                        int i17 = i11 - i15;
                        int i18 = i16 >> 1;
                        int i19 = i13 - i15;
                        while (i19 > 0) {
                            int i20 = i18 + 1;
                            int i21 = i15;
                            FloatFFT.this.cftleaf(i21, FloatFFT.this.cfttree(i21, i19, i20, fArr, i11, i3, fArr2), fArr, i17 + i19, i3, fArr2);
                            i19 -= i15;
                            i18 = i20;
                        }
                    }
                });
            }
            i9 = i7;
        }
        ConcurrencyUtils.waitForCompletion(futureArr);
    }

    private void cftrec4(int i, float[] fArr, int i2, int i3, float[] fArr2) {
        int i4 = i2 + i;
        int i5 = i;
        while (i5 > 512) {
            int i6 = i5 >> 2;
            cftmdl1(i6, fArr, i4 - i6, fArr2, i3 - (i6 >> 1));
            i5 = i6;
        }
        cftleaf(i5, 1, fArr, i4 - i5, i3, fArr2);
        int i7 = 0;
        int i8 = i2 - i5;
        int i9 = i - i5;
        while (i9 > 0) {
            int i10 = i7 + 1;
            int i11 = i5;
            cftleaf(i11, cfttree(i5, i9, i10, fArr, i2, i3, fArr2), fArr, i8 + i9, i3, fArr2);
            i9 -= i11;
            i7 = i10;
            i5 = i11;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int cfttree(int i, int i2, int i3, float[] fArr, int i4, int i5, float[] fArr2) {
        int i6;
        int i7 = i4 - i;
        if ((i3 & 3) != 0) {
            i6 = i3 & 1;
            if (i6 != 0) {
                cftmdl1(i, fArr, i7 + i2, fArr2, i5 - (i >> 1));
            } else {
                cftmdl2(i, fArr, i7 + i2, fArr2, i5 - i);
            }
        } else {
            int i8 = i;
            int i9 = i3;
            while ((i9 & 3) == 0) {
                i8 <<= 2;
                i9 >>= 2;
            }
            i6 = i9 & 1;
            int i10 = i4 + i2;
            if (i6 != 0) {
                while (i8 > 128) {
                    cftmdl1(i8, fArr, i10 - i8, fArr2, i5 - (i8 >> 1));
                    i8 >>= 2;
                }
            } else {
                while (i8 > 128) {
                    cftmdl2(i8, fArr, i10 - i8, fArr2, i5 - i8);
                    i8 >>= 2;
                }
            }
        }
        return i6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cftleaf(int i, int i2, float[] fArr, int i3, int i4, float[] fArr2) {
        if (i == 512) {
            int i5 = i4 - 64;
            cftmdl1(128, fArr, i3, fArr2, i5);
            int i6 = i4 - 8;
            cftf161(fArr, i3, fArr2, i6);
            int i7 = i4 - 32;
            cftf162(fArr, i3 + 32, fArr2, i7);
            cftf161(fArr, i3 + 64, fArr2, i6);
            cftf161(fArr, i3 + 96, fArr2, i6);
            int i8 = i3 + 128;
            int i9 = i4 - 128;
            cftmdl2(128, fArr, i8, fArr2, i9);
            cftf161(fArr, i8, fArr2, i6);
            cftf162(fArr, i3 + 160, fArr2, i7);
            cftf161(fArr, i3 + 192, fArr2, i6);
            cftf162(fArr, i3 + 224, fArr2, i7);
            int i10 = i3 + 256;
            cftmdl1(128, fArr, i10, fArr2, i5);
            cftf161(fArr, i10, fArr2, i6);
            cftf162(fArr, i3 + 288, fArr2, i7);
            cftf161(fArr, i3 + 320, fArr2, i6);
            cftf161(fArr, i3 + 352, fArr2, i6);
            if (i2 != 0) {
                cftmdl1(128, fArr, i3 + 384, fArr2, i5);
                cftf161(fArr, i3 + 480, fArr2, i6);
            } else {
                cftmdl2(128, fArr, i3 + 384, fArr2, i9);
                cftf162(fArr, i3 + 480, fArr2, i7);
            }
            cftf161(fArr, i3 + 384, fArr2, i6);
            cftf162(fArr, i3 + 416, fArr2, i7);
            cftf161(fArr, i3 + 448, fArr2, i6);
            return;
        }
        int i11 = i4 - 32;
        cftmdl1(64, fArr, i3, fArr2, i11);
        int i12 = i4 - 8;
        cftf081(fArr, i3, fArr2, i12);
        cftf082(fArr, i3 + 16, fArr2, i12);
        cftf081(fArr, i3 + 32, fArr2, i12);
        cftf081(fArr, i3 + 48, fArr2, i12);
        int i13 = i3 + 64;
        int i14 = i4 - 64;
        cftmdl2(64, fArr, i13, fArr2, i14);
        cftf081(fArr, i13, fArr2, i12);
        cftf082(fArr, i3 + 80, fArr2, i12);
        cftf081(fArr, i3 + 96, fArr2, i12);
        cftf082(fArr, i3 + 112, fArr2, i12);
        int i15 = i3 + 128;
        cftmdl1(64, fArr, i15, fArr2, i11);
        cftf081(fArr, i15, fArr2, i12);
        cftf082(fArr, i3 + 144, fArr2, i12);
        cftf081(fArr, i3 + 160, fArr2, i12);
        cftf081(fArr, i3 + 176, fArr2, i12);
        if (i2 != 0) {
            cftmdl1(64, fArr, i3 + 192, fArr2, i11);
            cftf081(fArr, i3 + 240, fArr2, i12);
        } else {
            cftmdl2(64, fArr, i3 + 192, fArr2, i14);
            cftf082(fArr, i3 + 240, fArr2, i12);
        }
        cftf081(fArr, i3 + 192, fArr2, i12);
        cftf082(fArr, i3 + 208, fArr2, i12);
        cftf081(fArr, i3 + 224, fArr2, i12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cftmdl1(int i, float[] fArr, int i2, float[] fArr2, int i3) {
        int i4 = i >> 3;
        int i5 = i4 * 2;
        int i6 = i5 + i5;
        int i7 = i6 + i5;
        int i8 = i2 + i5;
        int i9 = i2 + i6;
        int i10 = i2 + i7;
        float f = fArr[i2] + fArr[i9];
        int i11 = i2 + 1;
        int i12 = i9 + 1;
        float f2 = fArr[i11] + fArr[i12];
        float f3 = fArr[i2] - fArr[i9];
        float f4 = fArr[i11] - fArr[i12];
        float f5 = fArr[i8] + fArr[i10];
        int i13 = i8 + 1;
        int i14 = i10 + 1;
        float f6 = fArr[i13] + fArr[i14];
        float f7 = fArr[i8] - fArr[i10];
        float f8 = fArr[i13] - fArr[i14];
        fArr[i2] = f + f5;
        fArr[i11] = f2 + f6;
        fArr[i8] = f - f5;
        fArr[i13] = f2 - f6;
        fArr[i9] = f3 - f8;
        fArr[i12] = f4 + f7;
        fArr[i10] = f3 + f8;
        fArr[i14] = f4 - f7;
        float f9 = fArr2[i3 + 1];
        int i15 = 0;
        for (int i16 = 2; i16 < i4; i16 += 2) {
            i15 += 4;
            int i17 = i3 + i15;
            float f10 = fArr2[i17];
            float f11 = fArr2[i17 + 1];
            float f12 = fArr2[i17 + 2];
            float f13 = fArr2[i17 + 3];
            int i18 = i16 + i5;
            int i19 = i18 + i5;
            int i20 = i19 + i5;
            int i21 = i2 + i18;
            int i22 = i2 + i19;
            int i23 = i2 + i20;
            int i24 = i2 + i16;
            float f14 = fArr[i24] + fArr[i22];
            int i25 = i24 + 1;
            int i26 = i22 + 1;
            float f15 = fArr[i25] + fArr[i26];
            float f16 = fArr[i24] - fArr[i22];
            float f17 = fArr[i25] - fArr[i26];
            float f18 = fArr[i21] + fArr[i23];
            int i27 = i21 + 1;
            int i28 = i23 + 1;
            float f19 = fArr[i27] + fArr[i28];
            float f20 = fArr[i21] - fArr[i23];
            float f21 = fArr[i27] - fArr[i28];
            fArr[i24] = f14 + f18;
            fArr[i25] = f15 + f19;
            fArr[i21] = f14 - f18;
            fArr[i27] = f15 - f19;
            float f22 = f16 - f21;
            float f23 = f17 + f20;
            fArr[i22] = (f10 * f22) - (f11 * f23);
            fArr[i26] = (f23 * f10) + (f22 * f11);
            float f24 = f16 + f21;
            float f25 = f17 - f20;
            fArr[i23] = (f12 * f24) + (f13 * f25);
            fArr[i28] = (f25 * f12) - (f24 * f13);
            int i29 = i5 - i16;
            int i30 = i29 + i5;
            int i31 = i30 + i5;
            int i32 = i31 + i5;
            int i33 = i2 + i29;
            int i34 = i2 + i30;
            int i35 = i2 + i31;
            int i36 = i2 + i32;
            float f26 = fArr[i33] + fArr[i35];
            int i37 = i33 + 1;
            int i38 = i35 + 1;
            float f27 = fArr[i37] + fArr[i38];
            float f28 = fArr[i33] - fArr[i35];
            float f29 = fArr[i37] - fArr[i38];
            float f30 = fArr[i34] + fArr[i36];
            int i39 = i34 + 1;
            int i40 = i36 + 1;
            float f31 = fArr[i39] + fArr[i40];
            float f32 = fArr[i34] - fArr[i36];
            float f33 = fArr[i39] - fArr[i40];
            fArr[i33] = f26 + f30;
            fArr[i37] = f27 + f31;
            fArr[i34] = f26 - f30;
            fArr[i39] = f27 - f31;
            float f34 = f28 - f33;
            float f35 = f29 + f32;
            fArr[i35] = (f11 * f34) - (f10 * f35);
            fArr[i38] = (f11 * f35) + (f10 * f34);
            float f36 = f28 + f33;
            float f37 = f29 - f32;
            fArr[i36] = (f13 * f36) + (f12 * f37);
            fArr[i40] = (f13 * f37) - (f12 * f36);
        }
        int i41 = i4 + i5;
        int i42 = i41 + i5;
        int i43 = i5 + i42;
        int i44 = i2 + i4;
        int i45 = i2 + i41;
        int i46 = i2 + i42;
        int i47 = i2 + i43;
        float f38 = fArr[i44] + fArr[i46];
        int i48 = i44 + 1;
        int i49 = i46 + 1;
        float f39 = fArr[i48] + fArr[i49];
        float f40 = fArr[i44] - fArr[i46];
        float f41 = fArr[i48] - fArr[i49];
        float f42 = fArr[i45] + fArr[i47];
        int i50 = i45 + 1;
        int i51 = i47 + 1;
        float f43 = fArr[i50] + fArr[i51];
        float f44 = fArr[i45] - fArr[i47];
        float f45 = fArr[i50] - fArr[i51];
        fArr[i44] = f38 + f42;
        fArr[i48] = f39 + f43;
        fArr[i45] = f38 - f42;
        fArr[i50] = f39 - f43;
        float f46 = f40 - f45;
        float f47 = f41 + f44;
        fArr[i46] = (f46 - f47) * f9;
        fArr[i49] = (f47 + f46) * f9;
        float f48 = f40 + f45;
        float f49 = f41 - f44;
        float f50 = -f9;
        fArr[i47] = (f48 + f49) * f50;
        fArr[i51] = f50 * (f49 - f48);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cftmdl2(int i, float[] fArr, int i2, float[] fArr2, int i3) {
        int i4 = i >> 3;
        int i5 = i4 * 2;
        float f = fArr2[i3 + 1];
        int i6 = i5 + i5;
        int i7 = i6 + i5;
        int i8 = i2 + i5;
        int i9 = i2 + i6;
        int i10 = i2 + i7;
        int i11 = i9 + 1;
        float f2 = fArr[i2] - fArr[i11];
        int i12 = i2 + 1;
        float f3 = fArr[i12] + fArr[i9];
        float f4 = fArr[i2] + fArr[i11];
        float f5 = fArr[i12] - fArr[i9];
        int i13 = i10 + 1;
        float f6 = fArr[i8] - fArr[i13];
        int i14 = i8 + 1;
        float f7 = fArr[i14] + fArr[i10];
        float f8 = fArr[i8] + fArr[i13];
        float f9 = fArr[i14] - fArr[i10];
        float f10 = (f6 - f7) * f;
        float f11 = (f7 + f6) * f;
        fArr[i2] = f2 + f10;
        fArr[i12] = f3 + f11;
        fArr[i8] = f2 - f10;
        fArr[i14] = f3 - f11;
        float f12 = (f8 - f9) * f;
        float f13 = f * (f9 + f8);
        fArr[i9] = f4 - f13;
        fArr[i11] = f5 + f12;
        fArr[i10] = f4 + f13;
        fArr[i13] = f5 - f12;
        int i15 = i5 * 2;
        int i16 = 0;
        for (int i17 = 2; i17 < i4; i17 += 2) {
            i16 += 4;
            int i18 = i3 + i16;
            float f14 = fArr2[i18];
            float f15 = fArr2[i18 + 1];
            float f16 = fArr2[i18 + 2];
            float f17 = fArr2[i18 + 3];
            i15 -= 4;
            int i19 = i3 + i15;
            float f18 = fArr2[i19];
            float f19 = fArr2[i19 + 1];
            float f20 = fArr2[i19 + 2];
            float f21 = fArr2[i19 + 3];
            int i20 = i17 + i5;
            int i21 = i20 + i5;
            int i22 = i21 + i5;
            int i23 = i2 + i20;
            int i24 = i2 + i21;
            int i25 = i2 + i22;
            int i26 = i2 + i17;
            int i27 = i24 + 1;
            float f22 = fArr[i26] - fArr[i27];
            int i28 = i26 + 1;
            float f23 = fArr[i28] + fArr[i24];
            float f24 = fArr[i26] + fArr[i27];
            float f25 = fArr[i28] - fArr[i24];
            int i29 = i25 + 1;
            float f26 = fArr[i23] - fArr[i29];
            int i30 = i23 + 1;
            float f27 = fArr[i30] + fArr[i25];
            float f28 = fArr[i23] + fArr[i29];
            float f29 = fArr[i30] - fArr[i25];
            float f30 = (f14 * f22) - (f15 * f23);
            float f31 = (f23 * f14) + (f22 * f15);
            float f32 = (f19 * f26) - (f18 * f27);
            float f33 = (f27 * f19) + (f26 * f18);
            fArr[i26] = f30 + f32;
            fArr[i28] = f31 + f33;
            fArr[i23] = f30 - f32;
            fArr[i30] = f31 - f33;
            float f34 = (f16 * f24) + (f17 * f25);
            float f35 = (f25 * f16) - (f24 * f17);
            float f36 = (f21 * f28) + (f20 * f29);
            float f37 = (f29 * f21) - (f28 * f20);
            fArr[i24] = f34 + f36;
            fArr[i27] = f35 + f37;
            fArr[i25] = f34 - f36;
            fArr[i29] = f35 - f37;
            int i31 = i5 - i17;
            int i32 = i31 + i5;
            int i33 = i32 + i5;
            int i34 = i33 + i5;
            int i35 = i2 + i31;
            int i36 = i2 + i32;
            int i37 = i2 + i33;
            int i38 = i2 + i34;
            int i39 = i37 + 1;
            float f38 = fArr[i35] - fArr[i39];
            int i40 = i35 + 1;
            float f39 = fArr[i40] + fArr[i37];
            float f40 = fArr[i35] + fArr[i39];
            float f41 = fArr[i40] - fArr[i37];
            int i41 = i38 + 1;
            float f42 = fArr[i36] - fArr[i41];
            int i42 = i36 + 1;
            float f43 = fArr[i42] + fArr[i38];
            float f44 = fArr[i36] + fArr[i41];
            float f45 = fArr[i42] - fArr[i38];
            float f46 = (f18 * f38) - (f19 * f39);
            float f47 = (f18 * f39) + (f19 * f38);
            float f48 = (f15 * f42) - (f14 * f43);
            float f49 = (f15 * f43) + (f14 * f42);
            fArr[i35] = f46 + f48;
            fArr[i40] = f47 + f49;
            fArr[i36] = f46 - f48;
            fArr[i42] = f47 - f49;
            float f50 = (f20 * f40) + (f21 * f41);
            float f51 = (f20 * f41) - (f21 * f40);
            float f52 = (f17 * f44) + (f16 * f45);
            float f53 = (f17 * f45) - (f16 * f44);
            fArr[i37] = f50 + f52;
            fArr[i39] = f51 + f53;
            fArr[i38] = f50 - f52;
            fArr[i41] = f51 - f53;
        }
        int i43 = i3 + i5;
        float f54 = fArr2[i43];
        float f55 = fArr2[i43 + 1];
        int i44 = i4 + i5;
        int i45 = i44 + i5;
        int i46 = i5 + i45;
        int i47 = i2 + i4;
        int i48 = i2 + i44;
        int i49 = i2 + i45;
        int i50 = i2 + i46;
        int i51 = i49 + 1;
        float f56 = fArr[i47] - fArr[i51];
        int i52 = i47 + 1;
        float f57 = fArr[i52] + fArr[i49];
        float f58 = fArr[i47] + fArr[i51];
        float f59 = fArr[i52] - fArr[i49];
        int i53 = i50 + 1;
        float f60 = fArr[i48] - fArr[i53];
        int i54 = i48 + 1;
        float f61 = fArr[i54] + fArr[i50];
        float f62 = fArr[i48] + fArr[i53];
        float f63 = fArr[i54] - fArr[i50];
        float f64 = (f54 * f56) - (f55 * f57);
        float f65 = (f57 * f54) + (f56 * f55);
        float f66 = (f55 * f60) - (f54 * f61);
        float f67 = (f61 * f55) + (f60 * f54);
        fArr[i47] = f64 + f66;
        fArr[i52] = f65 + f67;
        fArr[i48] = f64 - f66;
        fArr[i54] = f65 - f67;
        float f68 = (f55 * f58) - (f54 * f59);
        float f69 = (f59 * f55) + (f58 * f54);
        float f70 = (f54 * f62) - (f55 * f63);
        float f71 = (f54 * f63) + (f55 * f62);
        fArr[i49] = f68 - f70;
        fArr[i51] = f69 - f71;
        fArr[i50] = f68 + f70;
        fArr[i53] = f69 + f71;
    }

    private void cftfx41(int i, float[] fArr, int i2, int i3, float[] fArr2) {
        if (i == 128) {
            int i4 = i3 - 8;
            cftf161(fArr, i2, fArr2, i4);
            cftf162(fArr, i2 + 32, fArr2, i3 - 32);
            cftf161(fArr, i2 + 64, fArr2, i4);
            cftf161(fArr, i2 + 96, fArr2, i4);
            return;
        }
        int i5 = i3 - 8;
        cftf081(fArr, i2, fArr2, i5);
        cftf082(fArr, i2 + 16, fArr2, i5);
        cftf081(fArr, i2 + 32, fArr2, i5);
        cftf081(fArr, i2 + 48, fArr2, i5);
    }

    private void cftf161(float[] fArr, int i, float[] fArr2, int i2) {
        float f = fArr2[i2 + 1];
        float f2 = fArr2[i2 + 2];
        float f3 = fArr2[i2 + 3];
        int i3 = i + 16;
        float f4 = fArr[i] + fArr[i3];
        int i4 = i + 1;
        int i5 = i + 17;
        float f5 = fArr[i4] + fArr[i5];
        float f6 = fArr[i] - fArr[i3];
        float f7 = fArr[i4] - fArr[i5];
        int i6 = i + 8;
        int i7 = i + 24;
        float f8 = fArr[i6] + fArr[i7];
        int i8 = i + 9;
        int i9 = i + 25;
        float f9 = fArr[i8] + fArr[i9];
        float f10 = fArr[i6] - fArr[i7];
        float f11 = fArr[i8] - fArr[i9];
        float f12 = f4 + f8;
        float f13 = f5 + f9;
        float f14 = f4 - f8;
        float f15 = f5 - f9;
        float f16 = f6 - f11;
        float f17 = f7 + f10;
        float f18 = f6 + f11;
        float f19 = f7 - f10;
        int i10 = i + 2;
        int i11 = i + 18;
        float f20 = fArr[i10] + fArr[i11];
        int i12 = i + 3;
        int i13 = i + 19;
        float f21 = fArr[i12] + fArr[i13];
        float f22 = fArr[i10] - fArr[i11];
        float f23 = fArr[i12] - fArr[i13];
        int i14 = i + 10;
        int i15 = i + 26;
        float f24 = fArr[i14] + fArr[i15];
        int i16 = i + 11;
        int i17 = i + 27;
        float f25 = fArr[i16] + fArr[i17];
        float f26 = fArr[i14] - fArr[i15];
        float f27 = fArr[i16] - fArr[i17];
        float f28 = f20 + f24;
        float f29 = f21 + f25;
        float f30 = f20 - f24;
        float f31 = f21 - f25;
        float f32 = f22 - f27;
        float f33 = f23 + f26;
        float f34 = (f2 * f32) - (f3 * f33);
        float f35 = (f33 * f2) + (f32 * f3);
        float f36 = f22 + f27;
        float f37 = f23 - f26;
        float f38 = (f3 * f36) - (f2 * f37);
        float f39 = (f37 * f3) + (f36 * f2);
        int i18 = i + 4;
        int i19 = i + 20;
        float f40 = fArr[i18] + fArr[i19];
        int i20 = i + 5;
        int i21 = i + 21;
        float f41 = fArr[i20] + fArr[i21];
        float f42 = fArr[i18] - fArr[i19];
        float f43 = fArr[i20] - fArr[i21];
        int i22 = i + 12;
        int i23 = i + 28;
        float f44 = fArr[i22] + fArr[i23];
        int i24 = i + 13;
        int i25 = i + 29;
        float f45 = fArr[i24] + fArr[i25];
        float f46 = fArr[i22] - fArr[i23];
        float f47 = fArr[i24] - fArr[i25];
        float f48 = f40 + f44;
        float f49 = f41 + f45;
        float f50 = f40 - f44;
        float f51 = f41 - f45;
        float f52 = f42 - f47;
        float f53 = f43 + f46;
        float f54 = (f52 - f53) * f;
        float f55 = (f53 + f52) * f;
        float f56 = f42 + f47;
        float f57 = f43 - f46;
        float f58 = (f56 + f57) * f;
        float f59 = (f57 - f56) * f;
        int i26 = i + 6;
        int i27 = i + 22;
        float f60 = fArr[i26] + fArr[i27];
        int i28 = i + 7;
        int i29 = i + 23;
        float f61 = fArr[i28] + fArr[i29];
        float f62 = fArr[i26] - fArr[i27];
        float f63 = fArr[i28] - fArr[i29];
        int i30 = i + 14;
        int i31 = i + 30;
        float f64 = fArr[i30] + fArr[i31];
        int i32 = i + 15;
        int i33 = i + 31;
        float f65 = fArr[i32] + fArr[i33];
        float f66 = fArr[i30] - fArr[i31];
        float f67 = fArr[i32] - fArr[i33];
        float f68 = f60 + f64;
        float f69 = f61 + f65;
        float f70 = f60 - f64;
        float f71 = f61 - f65;
        float f72 = f62 - f67;
        float f73 = f63 + f66;
        float f74 = (f3 * f72) - (f2 * f73);
        float f75 = (f73 * f3) + (f72 * f2);
        float f76 = f62 + f67;
        float f77 = f63 - f66;
        float f78 = (f2 * f76) - (f3 * f77);
        float f79 = (f2 * f77) + (f3 * f76);
        float f80 = f18 - f58;
        float f81 = f19 - f59;
        float f82 = f18 + f58;
        float f83 = f19 + f59;
        float f84 = f38 - f78;
        float f85 = f39 - f79;
        float f86 = f38 + f78;
        float f87 = f39 + f79;
        fArr[i7] = f80 + f84;
        fArr[i9] = f81 + f85;
        fArr[i15] = f80 - f84;
        fArr[i17] = f81 - f85;
        fArr[i23] = f82 - f87;
        fArr[i25] = f83 + f86;
        fArr[i31] = f82 + f87;
        fArr[i33] = f83 - f86;
        float f88 = f16 + f54;
        float f89 = f17 + f55;
        float f90 = f16 - f54;
        float f91 = f17 - f55;
        float f92 = f34 + f74;
        float f93 = f35 + f75;
        float f94 = f34 - f74;
        float f95 = f35 - f75;
        fArr[i3] = f88 + f92;
        fArr[i5] = f89 + f93;
        fArr[i11] = f88 - f92;
        fArr[i13] = f89 - f93;
        fArr[i19] = f90 - f95;
        fArr[i21] = f91 + f94;
        fArr[i27] = f90 + f95;
        fArr[i29] = f91 - f94;
        float f96 = f30 - f71;
        float f97 = f31 + f70;
        float f98 = (f96 - f97) * f;
        float f99 = (f97 + f96) * f;
        float f100 = f30 + f71;
        float f101 = f31 - f70;
        float f102 = (f100 - f101) * f;
        float f103 = f * (f101 + f100);
        float f104 = f14 - f51;
        float f105 = f15 + f50;
        float f106 = f14 + f51;
        float f107 = f15 - f50;
        fArr[i6] = f104 + f98;
        fArr[i8] = f105 + f99;
        fArr[i14] = f104 - f98;
        fArr[i16] = f105 - f99;
        fArr[i22] = f106 - f103;
        fArr[i24] = f107 + f102;
        fArr[i30] = f106 + f103;
        fArr[i32] = f107 - f102;
        float f108 = f12 + f48;
        float f109 = f13 + f49;
        float f110 = f12 - f48;
        float f111 = f13 - f49;
        float f112 = f28 + f68;
        float f113 = f29 + f69;
        float f114 = f28 - f68;
        float f115 = f29 - f69;
        fArr[i] = f108 + f112;
        fArr[i4] = f109 + f113;
        fArr[i10] = f108 - f112;
        fArr[i12] = f109 - f113;
        fArr[i18] = f110 - f115;
        fArr[i20] = f111 + f114;
        fArr[i26] = f110 + f115;
        fArr[i28] = f111 - f114;
    }

    private void cftf162(float[] fArr, int i, float[] fArr2, int i2) {
        float f = fArr2[i2 + 1];
        float f2 = fArr2[i2 + 4];
        float f3 = fArr2[i2 + 5];
        float f4 = fArr2[i2 + 6];
        float f5 = -fArr2[i2 + 7];
        float f6 = fArr2[i2 + 8];
        float f7 = fArr2[i2 + 9];
        int i3 = i + 17;
        float f8 = fArr[i] - fArr[i3];
        int i4 = i + 1;
        int i5 = i + 16;
        float f9 = fArr[i4] + fArr[i5];
        int i6 = i + 8;
        int i7 = i + 25;
        float f10 = fArr[i6] - fArr[i7];
        int i8 = i + 9;
        int i9 = i + 24;
        float f11 = fArr[i8] + fArr[i9];
        float f12 = (f10 - f11) * f;
        float f13 = (f11 + f10) * f;
        float f14 = f8 + f12;
        float f15 = f9 + f13;
        float f16 = f8 - f12;
        float f17 = f9 - f13;
        float f18 = fArr[i] + fArr[i3];
        float f19 = fArr[i4] - fArr[i5];
        float f20 = fArr[i6] + fArr[i7];
        float f21 = fArr[i8] - fArr[i9];
        float f22 = (f20 - f21) * f;
        float f23 = (f21 + f20) * f;
        float f24 = f18 - f23;
        float f25 = f19 + f22;
        float f26 = f18 + f23;
        float f27 = f19 - f22;
        int i10 = i + 2;
        int i11 = i + 19;
        float f28 = fArr[i10] - fArr[i11];
        int i12 = i + 3;
        int i13 = i + 18;
        float f29 = fArr[i12] + fArr[i13];
        float f30 = (f2 * f28) - (f3 * f29);
        float f31 = (f29 * f2) + (f28 * f3);
        int i14 = i + 10;
        int i15 = i + 27;
        float f32 = fArr[i14] - fArr[i15];
        int i16 = i + 11;
        int i17 = i + 26;
        float f33 = fArr[i16] + fArr[i17];
        float f34 = (f5 * f32) - (f4 * f33);
        float f35 = (f33 * f5) + (f32 * f4);
        float f36 = f30 + f34;
        float f37 = f31 + f35;
        float f38 = f30 - f34;
        float f39 = f31 - f35;
        float f40 = fArr[i10] + fArr[i11];
        float f41 = fArr[i12] - fArr[i13];
        float f42 = (f4 * f40) - (f5 * f41);
        float f43 = (f41 * f4) + (f40 * f5);
        float f44 = fArr[i14] + fArr[i15];
        float f45 = fArr[i16] - fArr[i17];
        float f46 = (f2 * f44) + (f3 * f45);
        float f47 = (f45 * f2) - (f44 * f3);
        float f48 = f42 - f46;
        float f49 = f43 - f47;
        float f50 = f42 + f46;
        float f51 = f43 + f47;
        int i18 = i + 4;
        int i19 = i + 21;
        float f52 = fArr[i18] - fArr[i19];
        int i20 = i + 5;
        int i21 = i + 20;
        float f53 = fArr[i20] + fArr[i21];
        float f54 = (f6 * f52) - (f7 * f53);
        float f55 = (f53 * f6) + (f52 * f7);
        int i22 = i + 12;
        int i23 = i + 29;
        float f56 = fArr[i22] - fArr[i23];
        int i24 = i + 13;
        int i25 = i + 28;
        float f57 = fArr[i24] + fArr[i25];
        float f58 = (f7 * f56) - (f6 * f57);
        float f59 = (f57 * f7) + (f56 * f6);
        float f60 = f54 + f58;
        float f61 = f55 + f59;
        float f62 = f54 - f58;
        float f63 = f55 - f59;
        float f64 = fArr[i18] + fArr[i19];
        float f65 = fArr[i20] - fArr[i21];
        float f66 = (f7 * f64) - (f6 * f65);
        float f67 = (f65 * f7) + (f64 * f6);
        float f68 = fArr[i22] + fArr[i23];
        float f69 = fArr[i24] - fArr[i25];
        float f70 = (f6 * f68) - (f7 * f69);
        float f71 = (f6 * f69) + (f7 * f68);
        float f72 = f66 - f70;
        float f73 = f67 - f71;
        float f74 = f66 + f70;
        float f75 = f67 + f71;
        int i26 = i + 6;
        int i27 = i + 23;
        float f76 = fArr[i26] - fArr[i27];
        int i28 = i + 7;
        int i29 = i + 22;
        float f77 = fArr[i28] + fArr[i29];
        float f78 = (f4 * f76) - (f5 * f77);
        float f79 = (f77 * f4) + (f76 * f5);
        int i30 = i + 14;
        int i31 = i + 31;
        float f80 = fArr[i30] - fArr[i31];
        int i32 = i + 15;
        int i33 = i + 30;
        float f81 = fArr[i32] + fArr[i33];
        float f82 = (f3 * f80) - (f2 * f81);
        float f83 = (f81 * f3) + (f80 * f2);
        float f84 = f78 + f82;
        float f85 = f79 + f83;
        float f86 = f78 - f82;
        float f87 = f79 - f83;
        float f88 = fArr[i26] + fArr[i27];
        float f89 = fArr[i28] - fArr[i29];
        float f90 = (f3 * f88) + (f2 * f89);
        float f91 = (f3 * f89) - (f2 * f88);
        float f92 = fArr[i30] + fArr[i31];
        float f93 = fArr[i32] - fArr[i33];
        float f94 = (f5 * f92) - (f4 * f93);
        float f95 = (f5 * f93) + (f4 * f92);
        float f96 = f90 + f94;
        float f97 = f91 + f95;
        float f98 = f90 - f94;
        float f99 = f91 - f95;
        float f100 = f14 + f60;
        float f101 = f15 + f61;
        float f102 = f36 + f84;
        float f103 = f37 + f85;
        fArr[i] = f100 + f102;
        fArr[i4] = f101 + f103;
        fArr[i10] = f100 - f102;
        fArr[i12] = f101 - f103;
        float f104 = f14 - f60;
        float f105 = f15 - f61;
        float f106 = f36 - f84;
        float f107 = f37 - f85;
        fArr[i18] = f104 - f107;
        fArr[i20] = f105 + f106;
        fArr[i26] = f104 + f107;
        fArr[i28] = f105 - f106;
        float f108 = f16 - f63;
        float f109 = f17 + f62;
        float f110 = f38 - f87;
        float f111 = f39 + f86;
        float f112 = (f110 - f111) * f;
        float f113 = (f111 + f110) * f;
        fArr[i6] = f108 + f112;
        fArr[i8] = f109 + f113;
        fArr[i14] = f108 - f112;
        fArr[i16] = f109 - f113;
        float f114 = f16 + f63;
        float f115 = f17 - f62;
        float f116 = f38 + f87;
        float f117 = f39 - f86;
        float f118 = (f116 - f117) * f;
        float f119 = (f117 + f116) * f;
        fArr[i22] = f114 - f119;
        fArr[i24] = f115 + f118;
        fArr[i30] = f114 + f119;
        fArr[i32] = f115 - f118;
        float f120 = f24 + f72;
        float f121 = f25 + f73;
        float f122 = f48 - f96;
        float f123 = f49 - f97;
        fArr[i5] = f120 + f122;
        fArr[i3] = f121 + f123;
        fArr[i13] = f120 - f122;
        fArr[i11] = f121 - f123;
        float f124 = f24 - f72;
        float f125 = f25 - f73;
        float f126 = f48 + f96;
        float f127 = f49 + f97;
        fArr[i21] = f124 - f127;
        fArr[i19] = f125 + f126;
        fArr[i29] = f124 + f127;
        fArr[i27] = f125 - f126;
        float f128 = f26 - f75;
        float f129 = f27 + f74;
        float f130 = f50 + f99;
        float f131 = f51 - f98;
        float f132 = (f130 - f131) * f;
        float f133 = (f131 + f130) * f;
        fArr[i9] = f128 + f132;
        fArr[i7] = f129 + f133;
        fArr[i17] = f128 - f132;
        fArr[i15] = f129 - f133;
        float f134 = f26 + f75;
        float f135 = f27 - f74;
        float f136 = f50 - f99;
        float f137 = f51 + f98;
        float f138 = (f136 - f137) * f;
        float f139 = f * (f137 + f136);
        fArr[i25] = f134 - f139;
        fArr[i23] = f135 + f138;
        fArr[i33] = f134 + f139;
        fArr[i31] = f135 - f138;
    }

    private void cftf081(float[] fArr, int i, float[] fArr2, int i2) {
        float f = fArr2[i2 + 1];
        int i3 = i + 8;
        float f2 = fArr[i] + fArr[i3];
        int i4 = i + 1;
        int i5 = i + 9;
        float f3 = fArr[i4] + fArr[i5];
        float f4 = fArr[i] - fArr[i3];
        float f5 = fArr[i4] - fArr[i5];
        int i6 = i + 4;
        int i7 = i + 12;
        float f6 = fArr[i6] + fArr[i7];
        int i8 = i + 5;
        int i9 = i + 13;
        float f7 = fArr[i8] + fArr[i9];
        float f8 = fArr[i6] - fArr[i7];
        float f9 = fArr[i8] - fArr[i9];
        float f10 = f2 + f6;
        float f11 = f3 + f7;
        float f12 = f2 - f6;
        float f13 = f3 - f7;
        float f14 = f4 - f9;
        float f15 = f5 + f8;
        float f16 = f4 + f9;
        float f17 = f5 - f8;
        int i10 = i + 2;
        int i11 = i + 10;
        float f18 = fArr[i10] + fArr[i11];
        int i12 = i + 3;
        int i13 = i + 11;
        float f19 = fArr[i12] + fArr[i13];
        float f20 = fArr[i10] - fArr[i11];
        float f21 = fArr[i12] - fArr[i13];
        int i14 = i + 6;
        int i15 = i + 14;
        float f22 = fArr[i14] + fArr[i15];
        int i16 = i + 7;
        int i17 = i + 15;
        float f23 = fArr[i16] + fArr[i17];
        float f24 = fArr[i14] - fArr[i15];
        float f25 = fArr[i16] - fArr[i17];
        float f26 = f18 + f22;
        float f27 = f19 + f23;
        float f28 = f18 - f22;
        float f29 = f19 - f23;
        float f30 = f20 - f25;
        float f31 = f21 + f24;
        float f32 = f20 + f25;
        float f33 = f21 - f24;
        float f34 = (f30 - f31) * f;
        float f35 = (f30 + f31) * f;
        float f36 = (f32 - f33) * f;
        float f37 = f * (f32 + f33);
        fArr[i3] = f14 + f34;
        fArr[i5] = f15 + f35;
        fArr[i11] = f14 - f34;
        fArr[i13] = f15 - f35;
        fArr[i7] = f16 - f37;
        fArr[i9] = f17 + f36;
        fArr[i15] = f16 + f37;
        fArr[i17] = f17 - f36;
        fArr[i] = f10 + f26;
        fArr[i4] = f11 + f27;
        fArr[i10] = f10 - f26;
        fArr[i12] = f11 - f27;
        fArr[i6] = f12 - f29;
        fArr[i8] = f13 + f28;
        fArr[i14] = f12 + f29;
        fArr[i16] = f13 - f28;
    }

    private void cftf082(float[] fArr, int i, float[] fArr2, int i2) {
        float f = fArr2[i2 + 1];
        float f2 = fArr2[i2 + 2];
        float f3 = fArr2[i2 + 3];
        int i3 = i + 9;
        float f4 = fArr[i] - fArr[i3];
        int i4 = i + 1;
        int i5 = i + 8;
        float f5 = fArr[i4] + fArr[i5];
        float f6 = fArr[i] + fArr[i3];
        float f7 = fArr[i4] - fArr[i5];
        int i6 = i + 4;
        int i7 = i + 13;
        float f8 = fArr[i6] - fArr[i7];
        int i8 = i + 5;
        int i9 = i + 12;
        float f9 = fArr[i8] + fArr[i9];
        float f10 = (f8 - f9) * f;
        float f11 = (f9 + f8) * f;
        float f12 = fArr[i6] + fArr[i7];
        float f13 = fArr[i8] - fArr[i9];
        float f14 = (f12 - f13) * f;
        float f15 = f * (f13 + f12);
        int i10 = i + 2;
        int i11 = i + 11;
        float f16 = fArr[i10] - fArr[i11];
        int i12 = i + 3;
        int i13 = i + 10;
        float f17 = fArr[i12] + fArr[i13];
        float f18 = (f2 * f16) - (f3 * f17);
        float f19 = (f17 * f2) + (f16 * f3);
        float f20 = fArr[i10] + fArr[i11];
        float f21 = fArr[i12] - fArr[i13];
        float f22 = (f3 * f20) - (f2 * f21);
        float f23 = (f21 * f3) + (f20 * f2);
        int i14 = i + 6;
        int i15 = i + 15;
        float f24 = fArr[i14] - fArr[i15];
        int i16 = i + 7;
        int i17 = i + 14;
        float f25 = fArr[i16] + fArr[i17];
        float f26 = (f3 * f24) - (f2 * f25);
        float f27 = (f25 * f3) + (f24 * f2);
        float f28 = fArr[i14] + fArr[i15];
        float f29 = fArr[i16] - fArr[i17];
        float f30 = (f2 * f28) - (f3 * f29);
        float f31 = (f2 * f29) + (f3 * f28);
        float f32 = f4 + f10;
        float f33 = f5 + f11;
        float f34 = f18 + f26;
        float f35 = f19 + f27;
        fArr[i] = f32 + f34;
        fArr[i4] = f33 + f35;
        fArr[i10] = f32 - f34;
        fArr[i12] = f33 - f35;
        float f36 = f4 - f10;
        float f37 = f5 - f11;
        float f38 = f18 - f26;
        float f39 = f19 - f27;
        fArr[i6] = f36 - f39;
        fArr[i8] = f37 + f38;
        fArr[i14] = f36 + f39;
        fArr[i16] = f37 - f38;
        float f40 = f6 - f15;
        float f41 = f7 + f14;
        float f42 = f22 - f30;
        float f43 = f23 - f31;
        fArr[i5] = f40 + f42;
        fArr[i3] = f41 + f43;
        fArr[i13] = f40 - f42;
        fArr[i11] = f41 - f43;
        float f44 = f6 + f15;
        float f45 = f7 - f14;
        float f46 = f22 + f30;
        float f47 = f23 + f31;
        fArr[i9] = f44 - f47;
        fArr[i7] = f45 + f46;
        fArr[i17] = f44 + f47;
        fArr[i15] = f45 - f46;
    }

    private void cftf040(float[] fArr, int i) {
        int i2 = i + 4;
        float f = fArr[i] + fArr[i2];
        int i3 = i + 1;
        int i4 = i + 5;
        float f2 = fArr[i3] + fArr[i4];
        float f3 = fArr[i] - fArr[i2];
        float f4 = fArr[i3] - fArr[i4];
        int i5 = i + 2;
        int i6 = i + 6;
        float f5 = fArr[i5] + fArr[i6];
        int i7 = i + 3;
        int i8 = i + 7;
        float f6 = fArr[i7] + fArr[i8];
        float f7 = fArr[i5] - fArr[i6];
        float f8 = fArr[i7] - fArr[i8];
        fArr[i] = f + f5;
        fArr[i3] = f2 + f6;
        fArr[i5] = f3 - f8;
        fArr[i7] = f4 + f7;
        fArr[i2] = f - f5;
        fArr[i4] = f2 - f6;
        fArr[i6] = f3 + f8;
        fArr[i8] = f4 - f7;
    }

    private void cftb040(float[] fArr, int i) {
        int i2 = i + 4;
        float f = fArr[i] + fArr[i2];
        int i3 = i + 1;
        int i4 = i + 5;
        float f2 = fArr[i3] + fArr[i4];
        float f3 = fArr[i] - fArr[i2];
        float f4 = fArr[i3] - fArr[i4];
        int i5 = i + 2;
        int i6 = i + 6;
        float f5 = fArr[i5] + fArr[i6];
        int i7 = i + 3;
        int i8 = i + 7;
        float f6 = fArr[i7] + fArr[i8];
        float f7 = fArr[i5] - fArr[i6];
        float f8 = fArr[i7] - fArr[i8];
        fArr[i] = f + f5;
        fArr[i3] = f2 + f6;
        fArr[i5] = f3 + f8;
        fArr[i7] = f4 - f7;
        fArr[i2] = f - f5;
        fArr[i4] = f2 - f6;
        fArr[i6] = f3 - f8;
        fArr[i8] = f4 + f7;
    }

    private void cftx020(float[] fArr, int i) {
        int i2 = i + 2;
        float f = fArr[i] - fArr[i2];
        int i3 = i + 1;
        int i4 = i + 3;
        float f2 = (-fArr[i3]) + fArr[i4];
        fArr[i] = fArr[i] + fArr[i2];
        fArr[i3] = fArr[i3] + fArr[i4];
        fArr[i2] = f;
        fArr[i4] = f2;
    }

    private void cftxb020(float[] fArr, int i) {
        int i2 = i + 2;
        float f = fArr[i] - fArr[i2];
        int i3 = i + 1;
        int i4 = i + 3;
        float f2 = fArr[i3] - fArr[i4];
        fArr[i] = fArr[i] + fArr[i2];
        fArr[i3] = fArr[i3] + fArr[i4];
        fArr[i2] = f;
        fArr[i4] = f2;
    }

    private void cftxc020(float[] fArr, int i) {
        int i2 = i + 2;
        float f = fArr[i] - fArr[i2];
        int i3 = i + 1;
        int i4 = i + 3;
        float f2 = fArr[i3] + fArr[i4];
        fArr[i] = fArr[i] + fArr[i2];
        fArr[i3] = fArr[i3] - fArr[i4];
        fArr[i2] = f;
        fArr[i4] = f2;
    }

    private void rftfsub(int i, float[] fArr, int i2, int i3, float[] fArr2, int i4) {
        int i5 = i >> 1;
        int i6 = (i3 * 2) / i5;
        int i7 = 0;
        for (int i8 = 2; i8 < i5; i8 += 2) {
            i7 += i6;
            double d = fArr2[(i4 + i3) - i7];
            Double.isNaN(d);
            float f = (float) (0.5d - d);
            float f2 = fArr2[i4 + i7];
            int i9 = i2 + i8;
            int i10 = i2 + (i - i8);
            float f3 = fArr[i9] - fArr[i10];
            int i11 = i9 + 1;
            int i12 = i10 + 1;
            float f4 = fArr[i11] + fArr[i12];
            float f5 = (f * f3) - (f2 * f4);
            float f6 = (f * f4) + (f2 * f3);
            fArr[i9] = fArr[i9] - f5;
            fArr[i11] = f6 - fArr[i11];
            fArr[i10] = fArr[i10] + f5;
            fArr[i12] = f6 - fArr[i12];
        }
        int i13 = i2 + i5 + 1;
        fArr[i13] = -fArr[i13];
    }

    private void rftbsub(int i, float[] fArr, int i2, int i3, float[] fArr2, int i4) {
        int i5 = i >> 1;
        int i6 = (i3 * 2) / i5;
        int i7 = 0;
        for (int i8 = 2; i8 < i5; i8 += 2) {
            i7 += i6;
            double d = fArr2[(i4 + i3) - i7];
            Double.isNaN(d);
            float f = (float) (0.5d - d);
            float f2 = fArr2[i4 + i7];
            int i9 = i2 + i8;
            int i10 = i2 + (i - i8);
            float f3 = fArr[i9] - fArr[i10];
            int i11 = i9 + 1;
            int i12 = i10 + 1;
            float f4 = fArr[i11] + fArr[i12];
            float f5 = (f * f3) - (f2 * f4);
            float f6 = (f * f4) + (f2 * f3);
            fArr[i9] = fArr[i9] - f5;
            fArr[i11] = fArr[i11] - f6;
            fArr[i10] = fArr[i10] + f5;
            fArr[i12] = fArr[i12] - f6;
        }
    }

    private void scale(float f, final float[] fArr, int i, boolean z) throws ExecutionException, InterruptedException {
        int i2;
        double d = f;
        Double.isNaN(d);
        final float f2 = (float) (1.0d / d);
        if (z) {
            i2 = this.n * 2;
        } else {
            i2 = this.n;
        }
        int numberOfThreads = ConcurrencyUtils.getNumberOfThreads();
        if (numberOfThreads <= 1 || i2 < ConcurrencyUtils.getThreadsBeginN_1D_FFT_2Threads()) {
            for (int i3 = i; i3 < i + i2; i3++) {
                fArr[i3] = fArr[i3] * f2;
            }
            return;
        }
        int i4 = i2 / numberOfThreads;
        Future[] futureArr = new Future[numberOfThreads];
        int i5 = 0;
        while (i5 < numberOfThreads) {
            final int i6 = i + (i5 * i4);
            final int i7 = i5 == numberOfThreads + (-1) ? i + i2 : i6 + i4;
            futureArr[i5] = ConcurrencyUtils.submit(new Runnable() { // from class: voice.decoder.fft.FloatFFT.17
                @Override // java.lang.Runnable
                public void run() {
                    for (int i8 = i6; i8 < i7; i8++) {
                        float[] fArr2 = fArr;
                        fArr2[i8] = fArr2[i8] * f2;
                    }
                }
            });
            i5++;
        }
        ConcurrencyUtils.waitForCompletion(futureArr);
    }
}
