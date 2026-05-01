package voice.decoder.fft;

import android.support.v7.widget.ActivityChooserView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import voice.decoder.fft.AudioFormat;

/* loaded from: classes.dex */
public abstract class AudioFloatConverter {
    public static final AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
    private AudioFormat format;

    public abstract byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3);

    public abstract float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3);

    private static class AudioFloatLSBFilter extends AudioFloatConverter {
        private AudioFloatConverter converter;
        private final byte mask;
        private byte[] mask_buffer;
        private final int offset;
        private final int stepsize;

        public AudioFloatLSBFilter(AudioFloatConverter audioFloatConverter, AudioFormat audioFormat) {
            int sampleSizeInBits = audioFormat.getSampleSizeInBits();
            boolean zIsBigEndian = audioFormat.isBigEndian();
            this.converter = audioFloatConverter;
            this.stepsize = (sampleSizeInBits + 7) / 8;
            this.offset = zIsBigEndian ? this.stepsize - 1 : 0;
            int i = sampleSizeInBits % 8;
            if (i == 0) {
                this.mask = (byte) 0;
                return;
            }
            if (i == 1) {
                this.mask = (byte) -128;
                return;
            }
            if (i == 2) {
                this.mask = (byte) -64;
                return;
            }
            if (i == 3) {
                this.mask = (byte) -32;
                return;
            }
            if (i == 4) {
                this.mask = (byte) -16;
                return;
            }
            if (i == 5) {
                this.mask = (byte) -8;
                return;
            }
            if (i == 6) {
                this.mask = (byte) -4;
            } else if (i == 7) {
                this.mask = (byte) -2;
            } else {
                this.mask = (byte) -1;
            }
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            byte[] byteArray = this.converter.toByteArray(fArr, i, i2, bArr, i3);
            int i4 = i2 * this.stepsize;
            int i5 = this.offset;
            while (true) {
                i3 += i5;
                if (i3 >= i4) {
                    return byteArray;
                }
                bArr[i3] = (byte) (bArr[i3] & this.mask);
                i5 = this.stepsize;
            }
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            if (this.mask_buffer == null || this.mask_buffer.length < bArr.length) {
                this.mask_buffer = new byte[bArr.length];
            }
            System.arraycopy(bArr, 0, this.mask_buffer, 0, bArr.length);
            int i4 = this.stepsize * i3;
            int i5 = this.offset + i;
            while (i5 < i4) {
                this.mask_buffer[i5] = (byte) (this.mask_buffer[i5] & this.mask);
                i5 += this.stepsize;
            }
            return this.converter.toFloatArray(this.mask_buffer, i, fArr, i2, i3);
        }
    }

    private static class AudioFloatConversion64L extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        double[] double_buff;
        DoubleBuffer floatbuffer;

        private AudioFloatConversion64L() {
            this.bytebuffer = null;
            this.floatbuffer = null;
            this.double_buff = null;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = i3 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i, i4);
            if (this.double_buff == null || this.double_buff.length < i3 + i2) {
                this.double_buff = new double[i3 + i2];
            }
            this.floatbuffer.get(this.double_buff, i2, i3);
            int i5 = i3 + i2;
            while (i2 < i5) {
                fArr[i2] = (float) this.double_buff[i2];
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i2 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            if (this.double_buff == null || this.double_buff.length < i + i2) {
                this.double_buff = new double[i + i2];
            }
            int i5 = i + i2;
            for (int i6 = i; i6 < i5; i6++) {
                this.double_buff[i6] = fArr[i6];
            }
            this.floatbuffer.put(this.double_buff, i, i2);
            this.bytebuffer.get(bArr, i3, i4);
            return bArr;
        }
    }

    private static class AudioFloatConversion64B extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        double[] double_buff;
        DoubleBuffer floatbuffer;

        private AudioFloatConversion64B() {
            this.bytebuffer = null;
            this.floatbuffer = null;
            this.double_buff = null;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = i3 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i, i4);
            if (this.double_buff == null || this.double_buff.length < i3 + i2) {
                this.double_buff = new double[i3 + i2];
            }
            this.floatbuffer.get(this.double_buff, i2, i3);
            int i5 = i3 + i2;
            while (i2 < i5) {
                fArr[i2] = (float) this.double_buff[i2];
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i2 * 8;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asDoubleBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            if (this.double_buff == null || this.double_buff.length < i + i2) {
                this.double_buff = new double[i + i2];
            }
            int i5 = i + i2;
            for (int i6 = i; i6 < i5; i6++) {
                this.double_buff[i6] = fArr[i6];
            }
            this.floatbuffer.put(this.double_buff, i, i2);
            this.bytebuffer.get(bArr, i3, i4);
            return bArr;
        }
    }

    private static class AudioFloatConversion32L extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        FloatBuffer floatbuffer;

        private AudioFloatConversion32L() {
            this.bytebuffer = null;
            this.floatbuffer = null;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = i3 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i, i4);
            this.floatbuffer.get(fArr, i2, i3);
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i2 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.LITTLE_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            this.floatbuffer.put(fArr, i, i2);
            this.bytebuffer.get(bArr, i3, i4);
            return bArr;
        }
    }

    private static class AudioFloatConversion32B extends AudioFloatConverter {
        ByteBuffer bytebuffer;
        FloatBuffer floatbuffer;

        private AudioFloatConversion32B() {
            this.bytebuffer = null;
            this.floatbuffer = null;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = i3 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.bytebuffer.position(0);
            this.floatbuffer.position(0);
            this.bytebuffer.put(bArr, i, i4);
            this.floatbuffer.get(fArr, i2, i3);
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i2 * 4;
            if (this.bytebuffer == null || this.bytebuffer.capacity() < i4) {
                this.bytebuffer = ByteBuffer.allocate(i4).order(ByteOrder.BIG_ENDIAN);
                this.floatbuffer = this.bytebuffer.asFloatBuffer();
            }
            this.floatbuffer.position(0);
            this.bytebuffer.position(0);
            this.floatbuffer.put(fArr, i, i2);
            this.bytebuffer.get(bArr, i3, i4);
            return bArr;
        }
    }

    private static class AudioFloatConversion8S extends AudioFloatConverter {
        private AudioFloatConversion8S() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                fArr[i2] = bArr[i] * 0.007874016f;
                i4++;
                i2++;
                i++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                bArr[i3] = (byte) (fArr[i] * 127.0f);
                i4++;
                i3++;
                i++;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion8U extends AudioFloatConverter {
        private AudioFloatConversion8U() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                fArr[i2] = ((bArr[i] & 255) - 127) * 0.007874016f;
                i4++;
                i2++;
                i++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                bArr[i3] = (byte) ((fArr[i] * 127.0f) + 127.0f);
                i4++;
                i3++;
                i++;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion16SL extends AudioFloatConverter {
        private AudioFloatConversion16SL() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = i3 + i2;
            while (i2 < i4) {
                fArr[i2] = ((short) ((bArr[i] & 255) | (bArr[r0] << 8))) * 3.051851E-5f;
                i2++;
                i = i + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i2 + i;
            while (i < i4) {
                double d = fArr[i];
                Double.isNaN(d);
                int i5 = (int) (d * 32767.0d);
                int i6 = i3 + 1;
                bArr[i3] = (byte) i5;
                i3 = i6 + 1;
                bArr[i6] = (byte) (i5 >>> 8);
                i++;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion16SB extends AudioFloatConverter {
        private AudioFloatConversion16SB() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                fArr[i2] = ((short) ((bArr[i] << 8) | (bArr[r2] & 255))) * 3.051851E-5f;
                i4++;
                i2++;
                i = i + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                double d = fArr[i];
                Double.isNaN(d);
                int i6 = (int) (d * 32767.0d);
                int i7 = i3 + 1;
                bArr[i3] = (byte) (i6 >>> 8);
                i3 = i7 + 1;
                bArr[i7] = (byte) i6;
                i4++;
                i = i5;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion16UL extends AudioFloatConverter {
        private AudioFloatConversion16UL() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                fArr[i2] = (((bArr[i] & 255) | ((bArr[r1] & 255) << 8)) - 32767) * 3.051851E-5f;
                i4++;
                i2++;
                i = i + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                double d = fArr[i];
                Double.isNaN(d);
                int i6 = ((int) (d * 32767.0d)) + 32767;
                int i7 = i3 + 1;
                bArr[i3] = (byte) i6;
                i3 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 8);
                i4++;
                i = i5;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion16UB extends AudioFloatConverter {
        private AudioFloatConversion16UB() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                fArr[i2] = ((((bArr[i] & 255) << 8) | (bArr[r1] & 255)) - 32767) * 3.051851E-5f;
                i4++;
                i2++;
                i = i + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                double d = fArr[i];
                Double.isNaN(d);
                int i6 = ((int) (d * 32767.0d)) + 32767;
                int i7 = i3 + 1;
                bArr[i3] = (byte) (i6 >>> 8);
                i3 = i7 + 1;
                bArr[i7] = (byte) i6;
                i4++;
                i = i5;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion24SL extends AudioFloatConverter {
        private AudioFloatConversion24SL() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = (bArr[i] & 255) | ((bArr[i5] & 255) << 8);
                int i8 = i6 + 1;
                int i9 = i7 | ((bArr[i6] & 255) << 16);
                if (i9 > 8388607) {
                    i9 -= 16777216;
                }
                fArr[i2] = i9 * 1.192093E-7f;
                i4++;
                i = i8;
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = (int) (fArr[i] * 8388607.0f);
                if (i6 < 0) {
                    i6 += 16777216;
                }
                int i7 = i3 + 1;
                bArr[i3] = (byte) i6;
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 8);
                bArr[i8] = (byte) (i6 >>> 16);
                i4++;
                i = i5;
                i3 = i8 + 1;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion24SB extends AudioFloatConverter {
        private AudioFloatConversion24SB() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = ((bArr[i] & 255) << 16) | ((bArr[i5] & 255) << 8);
                int i8 = i6 + 1;
                int i9 = i7 | (bArr[i6] & 255);
                if (i9 > 8388607) {
                    i9 -= 16777216;
                }
                fArr[i2] = i9 * 1.192093E-7f;
                i4++;
                i = i8;
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = (int) (fArr[i] * 8388607.0f);
                if (i6 < 0) {
                    i6 += 16777216;
                }
                int i7 = i3 + 1;
                bArr[i3] = (byte) (i6 >>> 16);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 8);
                bArr[i8] = (byte) i6;
                i4++;
                i = i5;
                i3 = i8 + 1;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion24UL extends AudioFloatConverter {
        private AudioFloatConversion24UL() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = (bArr[i] & 255) | ((bArr[i5] & 255) << 8);
                fArr[i2] = ((i6 | ((bArr[r2] & 255) << 16)) - 8388607) * 1.192093E-7f;
                i4++;
                i = i5 + 1 + 1;
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = ((int) (fArr[i] * 8388607.0f)) + 8388607;
                int i7 = i3 + 1;
                bArr[i3] = (byte) i6;
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 8);
                bArr[i8] = (byte) (i6 >>> 16);
                i4++;
                i = i5;
                i3 = i8 + 1;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion24UB extends AudioFloatConverter {
        private AudioFloatConversion24UB() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = ((bArr[i] & 255) << 16) | ((bArr[i5] & 255) << 8);
                fArr[i2] = ((i6 | (bArr[r2] & 255)) - 8388607) * 1.192093E-7f;
                i4++;
                i = i5 + 1 + 1;
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = ((int) (fArr[i] * 8388607.0f)) + 8388607;
                int i7 = i3 + 1;
                bArr[i3] = (byte) (i6 >>> 16);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 8);
                bArr[i8] = (byte) i6;
                i4++;
                i = i5;
                i3 = i8 + 1;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32SL extends AudioFloatConverter {
        private AudioFloatConversion32SL() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = (bArr[i] & 255) | ((bArr[i5] & 255) << 8);
                int i8 = i7 | ((bArr[i6] & 255) << 16);
                fArr[i2] = (i8 | ((bArr[r1] & 255) << 24)) * 4.656613E-10f;
                i4++;
                i2++;
                i = i6 + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = (int) (fArr[i] * 2.1474836E9f);
                int i7 = i3 + 1;
                bArr[i3] = (byte) i6;
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 8);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (i6 >>> 16);
                i3 = i9 + 1;
                bArr[i9] = (byte) (i6 >>> 24);
                i4++;
                i = i5;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32SB extends AudioFloatConverter {
        private AudioFloatConversion32SB() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = ((bArr[i] & 255) << 24) | ((bArr[i5] & 255) << 16);
                int i8 = i7 | ((bArr[i6] & 255) << 8);
                fArr[i2] = (i8 | (bArr[r1] & 255)) * 4.656613E-10f;
                i4++;
                i2++;
                i = i6 + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = (int) (fArr[i] * 2.1474836E9f);
                int i7 = i3 + 1;
                bArr[i3] = (byte) (i6 >>> 24);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 16);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (i6 >>> 8);
                i3 = i9 + 1;
                bArr[i9] = (byte) i6;
                i4++;
                i = i5;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32UL extends AudioFloatConverter {
        private AudioFloatConversion32UL() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = (bArr[i] & 255) | ((bArr[i5] & 255) << 8);
                int i8 = i7 | ((bArr[i6] & 255) << 16);
                fArr[i2] = ((i8 | ((bArr[r1] & 255) << 24)) - ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) * 4.656613E-10f;
                i4++;
                i2++;
                i = i6 + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = ((int) (fArr[i] * 2.1474836E9f)) + ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                int i7 = i3 + 1;
                bArr[i3] = (byte) i6;
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 8);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (i6 >>> 16);
                i3 = i9 + 1;
                bArr[i9] = (byte) (i6 >>> 24);
                i4++;
                i = i5;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32UB extends AudioFloatConverter {
        private AudioFloatConversion32UB() {
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = ((bArr[i] & 255) << 24) | ((bArr[i5] & 255) << 16);
                int i8 = i7 | ((bArr[i6] & 255) << 8);
                fArr[i2] = ((i8 | (bArr[r1] & 255)) - ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) * 4.656613E-10f;
                i4++;
                i2++;
                i = i6 + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = 0;
            while (i4 < i2) {
                int i5 = i + 1;
                int i6 = ((int) (fArr[i] * 2.1474836E9f)) + ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                int i7 = i3 + 1;
                bArr[i3] = (byte) (i6 >>> 24);
                int i8 = i7 + 1;
                bArr[i7] = (byte) (i6 >>> 16);
                int i9 = i8 + 1;
                bArr[i8] = (byte) (i6 >>> 8);
                i3 = i9 + 1;
                bArr[i9] = (byte) i6;
                i4++;
                i = i5;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32xSL extends AudioFloatConverter {
        final int xbytes;

        public AudioFloatConversion32xSL(int i) {
            this.xbytes = i;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + this.xbytes;
                int i6 = i5 + 1;
                int i7 = i6 + 1;
                int i8 = (bArr[i5] & 255) | ((bArr[i6] & 255) << 8);
                int i9 = i8 | ((bArr[i7] & 255) << 16);
                fArr[i2] = (i9 | ((bArr[r1] & 255) << 24)) * 4.656613E-10f;
                i4++;
                i2++;
                i = i7 + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i3;
            int i5 = i;
            int i6 = 0;
            while (i6 < i2) {
                int i7 = i5 + 1;
                int i8 = (int) (fArr[i5] * 2.1474836E9f);
                int i9 = i4;
                int i10 = 0;
                while (i10 < this.xbytes) {
                    bArr[i9] = 0;
                    i10++;
                    i9++;
                }
                int i11 = i9 + 1;
                bArr[i9] = (byte) i8;
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i8 >>> 8);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i8 >>> 16);
                bArr[i13] = (byte) (i8 >>> 24);
                i6++;
                i5 = i7;
                i4 = i13 + 1;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32xSB extends AudioFloatConverter {
        final int xbytes;

        public AudioFloatConversion32xSB(int i) {
            this.xbytes = i;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = ((bArr[i] & 255) << 24) | ((bArr[i5] & 255) << 16);
                int i8 = i6 + 1;
                int i9 = i7 | ((bArr[i6] & 255) << 8) | (bArr[i8] & 255);
                int i10 = this.xbytes + i8 + 1;
                fArr[i2] = i9 * 4.656613E-10f;
                i4++;
                i = i10;
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i3;
            int i5 = i;
            int i6 = 0;
            while (i6 < i2) {
                int i7 = i5 + 1;
                int i8 = (int) (fArr[i5] * 2.1474836E9f);
                int i9 = i4 + 1;
                bArr[i4] = (byte) (i8 >>> 24);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (i8 >>> 16);
                int i11 = i10 + 1;
                bArr[i10] = (byte) (i8 >>> 8);
                i4 = i11 + 1;
                bArr[i11] = (byte) i8;
                int i12 = 0;
                while (i12 < this.xbytes) {
                    bArr[i4] = 0;
                    i12++;
                    i4++;
                }
                i6++;
                i5 = i7;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32xUL extends AudioFloatConverter {
        final int xbytes;

        public AudioFloatConversion32xUL(int i) {
            this.xbytes = i;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + this.xbytes;
                int i6 = i5 + 1;
                int i7 = i6 + 1;
                int i8 = (bArr[i5] & 255) | ((bArr[i6] & 255) << 8);
                int i9 = i8 | ((bArr[i7] & 255) << 16);
                fArr[i2] = ((i9 | ((bArr[r1] & 255) << 24)) - ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) * 4.656613E-10f;
                i4++;
                i2++;
                i = i7 + 1 + 1;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i3;
            int i5 = i;
            int i6 = 0;
            while (i6 < i2) {
                int i7 = i5 + 1;
                int i8 = ((int) (fArr[i5] * 2.1474836E9f)) + ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                int i9 = i4;
                int i10 = 0;
                while (i10 < this.xbytes) {
                    bArr[i9] = 0;
                    i10++;
                    i9++;
                }
                int i11 = i9 + 1;
                bArr[i9] = (byte) i8;
                int i12 = i11 + 1;
                bArr[i11] = (byte) (i8 >>> 8);
                int i13 = i12 + 1;
                bArr[i12] = (byte) (i8 >>> 16);
                bArr[i13] = (byte) (i8 >>> 24);
                i6++;
                i5 = i7;
                i4 = i13 + 1;
            }
            return bArr;
        }
    }

    private static class AudioFloatConversion32xUB extends AudioFloatConverter {
        final int xbytes;

        public AudioFloatConversion32xUB(int i) {
            this.xbytes = i;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2, int i3) {
            int i4 = 0;
            while (i4 < i3) {
                int i5 = i + 1;
                int i6 = i5 + 1;
                int i7 = ((bArr[i] & 255) << 24) | ((bArr[i5] & 255) << 16);
                int i8 = i6 + 1;
                int i9 = i7 | ((bArr[i6] & 255) << 8) | (bArr[i8] & 255);
                int i10 = this.xbytes + i8 + 1;
                fArr[i2] = (i9 - ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) * 4.656613E-10f;
                i4++;
                i = i10;
                i2++;
            }
            return fArr;
        }

        @Override // voice.decoder.fft.AudioFloatConverter
        public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr, int i3) {
            int i4 = i3;
            int i5 = i;
            int i6 = 0;
            while (i6 < i2) {
                int i7 = i5 + 1;
                double d = fArr[i5];
                Double.isNaN(d);
                int i8 = ((int) (d * 2.147483647E9d)) + ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
                int i9 = i4 + 1;
                bArr[i4] = (byte) (i8 >>> 24);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (i8 >>> 16);
                int i11 = i10 + 1;
                bArr[i10] = (byte) (i8 >>> 8);
                i4 = i11 + 1;
                bArr[i11] = (byte) i8;
                int i12 = 0;
                while (i12 < this.xbytes) {
                    bArr[i4] = 0;
                    i12++;
                    i4++;
                }
                i6++;
                i5 = i7;
            }
            return bArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:114:0x01f3 A[PHI: r1
  0x01f3: PHI (r1v3 voice.decoder.fft.AudioFloatConverter$1) = 
  (r1v13 voice.decoder.fft.AudioFloatConverter$1)
  (r1v14 voice.decoder.fft.AudioFloatConverter$1)
  (r1v15 voice.decoder.fft.AudioFloatConverter$1)
  (r1v18 voice.decoder.fft.AudioFloatConverter$1)
  (r1v16 voice.decoder.fft.AudioFloatConverter$1)
  (r1v19 voice.decoder.fft.AudioFloatConverter$1)
  (r1v17 voice.decoder.fft.AudioFloatConverter$1)
  (r1v20 voice.decoder.fft.AudioFloatConverter$1)
  (r1v0 voice.decoder.fft.AudioFloatConverter$1)
  (r1v21 voice.decoder.fft.AudioFloatConverter$1)
 binds: [B:101:0x01bf, B:109:0x01df, B:98:0x01a4, B:99:0x01a6, B:77:0x014a, B:78:0x014c, B:52:0x00dc, B:53:0x00de, B:31:0x0080, B:32:0x0082] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Type inference failed for: r0v11, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion64L] */
    /* JADX WARN: Type inference failed for: r0v12, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion64B] */
    /* JADX WARN: Type inference failed for: r0v14, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32L] */
    /* JADX WARN: Type inference failed for: r0v15, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32B] */
    /* JADX WARN: Type inference failed for: r0v27, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32UL] */
    /* JADX WARN: Type inference failed for: r0v29, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion24UL] */
    /* JADX WARN: Type inference failed for: r0v31, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion16UL] */
    /* JADX WARN: Type inference failed for: r0v32, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion8U] */
    /* JADX WARN: Type inference failed for: r0v43, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32UB] */
    /* JADX WARN: Type inference failed for: r0v45, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion24UB] */
    /* JADX WARN: Type inference failed for: r0v47, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion16UB] */
    /* JADX WARN: Type inference failed for: r0v48, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion8U] */
    /* JADX WARN: Type inference failed for: r0v49 */
    /* JADX WARN: Type inference failed for: r0v50, types: [voice.decoder.fft.AudioFloatConverter] */
    /* JADX WARN: Type inference failed for: r0v51 */
    /* JADX WARN: Type inference failed for: r0v52, types: [voice.decoder.fft.AudioFloatConverter] */
    /* JADX WARN: Type inference failed for: r0v64, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32SL] */
    /* JADX WARN: Type inference failed for: r0v66, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion24SL] */
    /* JADX WARN: Type inference failed for: r0v68, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion16SL] */
    /* JADX WARN: Type inference failed for: r0v69, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion8S] */
    /* JADX WARN: Type inference failed for: r0v80, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32SB] */
    /* JADX WARN: Type inference failed for: r0v82, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion24SB] */
    /* JADX WARN: Type inference failed for: r0v84, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion16SB] */
    /* JADX WARN: Type inference failed for: r0v85, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion8S] */
    /* JADX WARN: Type inference failed for: r1v1, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32xUL] */
    /* JADX WARN: Type inference failed for: r1v11, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32xSL] */
    /* JADX WARN: Type inference failed for: r1v12, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32xSB] */
    /* JADX WARN: Type inference failed for: r1v2, types: [voice.decoder.fft.AudioFloatConverter$AudioFloatConversion32xUB] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static voice.decoder.fft.AudioFloatConverter getConverter(voice.decoder.fft.AudioFormat r7) {
        /*
            Method dump skipped, instructions count: 542
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: voice.decoder.fft.AudioFloatConverter.getConverter(voice.decoder.fft.AudioFormat):voice.decoder.fft.AudioFloatConverter");
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public float[] toFloatArray(byte[] bArr, float[] fArr, int i, int i2) {
        return toFloatArray(bArr, 0, fArr, i, i2);
    }

    public float[] toFloatArray(byte[] bArr, int i, float[] fArr, int i2) {
        return toFloatArray(bArr, i, fArr, 0, i2);
    }

    public float[] toFloatArray(byte[] bArr, float[] fArr, int i) {
        return toFloatArray(bArr, 0, fArr, 0, i);
    }

    public float[] toFloatArray(byte[] bArr, float[] fArr) {
        return toFloatArray(bArr, 0, fArr, 0, fArr.length);
    }

    public byte[] toByteArray(float[] fArr, int i, byte[] bArr, int i2) {
        return toByteArray(fArr, 0, i, bArr, i2);
    }

    public byte[] toByteArray(float[] fArr, int i, int i2, byte[] bArr) {
        return toByteArray(fArr, i, i2, bArr, 0);
    }

    public byte[] toByteArray(float[] fArr, int i, byte[] bArr) {
        return toByteArray(fArr, 0, i, bArr, 0);
    }

    public byte[] toByteArray(float[] fArr, byte[] bArr) {
        return toByteArray(fArr, 0, fArr.length, bArr, 0);
    }
}
