package voice.decoder.fft;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class AudioFormat {
    public static final int NOT_SPECIFIED = -1;
    protected boolean bigEndian;
    protected int channels;
    protected Encoding encoding;
    protected float frameRate;
    protected int frameSize;
    private HashMap<String, Object> properties;
    protected float sampleRate;
    protected int sampleSizeInBits;

    public AudioFormat(Encoding encoding, float f, int i, int i2, int i3, float f2, boolean z) {
        this.encoding = encoding;
        this.sampleRate = f;
        this.sampleSizeInBits = i;
        this.channels = i2;
        this.frameSize = i3;
        this.frameRate = f2;
        this.bigEndian = z;
        this.properties = null;
    }

    public AudioFormat(Encoding encoding, float f, int i, int i2, int i3, float f2, boolean z, Map<String, Object> map) {
        this(encoding, f, i, i2, i3, f2, z);
        this.properties = new HashMap<>(map);
    }

    public AudioFormat(float f, int i, int i2, boolean z, boolean z2) {
        this(z ? Encoding.PCM_SIGNED : Encoding.PCM_UNSIGNED, f, i, i2, (i2 == -1 || i == -1) ? -1 : ((i + 7) / 8) * i2, f, z2);
    }

    public Encoding getEncoding() {
        return this.encoding;
    }

    public float getSampleRate() {
        return this.sampleRate;
    }

    public int getSampleSizeInBits() {
        return this.sampleSizeInBits;
    }

    public int getChannels() {
        return this.channels;
    }

    public int getFrameSize() {
        return this.frameSize;
    }

    public float getFrameRate() {
        return this.frameRate;
    }

    public boolean isBigEndian() {
        return this.bigEndian;
    }

    public Map<String, Object> properties() {
        Map map;
        if (this.properties == null) {
            map = new HashMap(0);
        } else {
            map = (Map) this.properties.clone();
        }
        return Collections.unmodifiableMap(map);
    }

    public Object getProperty(String str) {
        if (this.properties == null) {
            return null;
        }
        return this.properties.get(str);
    }

    public boolean matches(AudioFormat audioFormat) {
        if (!audioFormat.getEncoding().equals(getEncoding())) {
            return false;
        }
        if (audioFormat.getChannels() != -1 && audioFormat.getChannels() != getChannels()) {
            return false;
        }
        if (audioFormat.getSampleRate() != -1.0f && audioFormat.getSampleRate() != getSampleRate()) {
            return false;
        }
        if (audioFormat.getSampleSizeInBits() != -1 && audioFormat.getSampleSizeInBits() != getSampleSizeInBits()) {
            return false;
        }
        if (audioFormat.getFrameRate() != -1.0f && audioFormat.getFrameRate() != getFrameRate()) {
            return false;
        }
        if (audioFormat.getFrameSize() == -1 || audioFormat.getFrameSize() == getFrameSize()) {
            return getSampleSizeInBits() <= 8 || audioFormat.isBigEndian() == isBigEndian();
        }
        return false;
    }

    public String toString() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5 = "";
        if (getEncoding() != null) {
            str5 = getEncoding().toString() + " ";
        }
        if (getSampleRate() == -1.0f) {
            str = "unknown sample rate, ";
        } else {
            str = "" + getSampleRate() + " Hz, ";
        }
        if (getSampleSizeInBits() == -1.0f) {
            str2 = "unknown bits per sample, ";
        } else {
            str2 = "" + getSampleSizeInBits() + " bit, ";
        }
        if (getChannels() == 1) {
            str3 = "mono, ";
        } else if (getChannels() == 2) {
            str3 = "stereo, ";
        } else if (getChannels() == -1) {
            str3 = " unknown number of channels, ";
        } else {
            str3 = "" + getChannels() + " channels, ";
        }
        if (getFrameSize() == -1.0f) {
            str4 = "unknown frame size, ";
        } else {
            str4 = "" + getFrameSize() + " bytes/frame, ";
        }
        String str6 = "";
        if (Math.abs(getSampleRate() - getFrameRate()) > 1.0E-5d) {
            if (getFrameRate() == -1.0f) {
                str6 = "unknown frame rate, ";
            } else {
                str6 = getFrameRate() + " frames/second, ";
            }
        }
        String str7 = "";
        if ((getEncoding().equals(Encoding.PCM_SIGNED) || getEncoding().equals(Encoding.PCM_UNSIGNED)) && (getSampleSizeInBits() > 8 || getSampleSizeInBits() == -1)) {
            str7 = isBigEndian() ? "big-endian" : "little-endian";
        }
        return str5 + str + str2 + str3 + str4 + str6 + str7;
    }

    public static class Encoding {
        private String name;
        public static final Encoding PCM_SIGNED = new Encoding("PCM_SIGNED");
        public static final Encoding PCM_UNSIGNED = new Encoding("PCM_UNSIGNED");
        public static final Encoding PCM_FLOAT = new Encoding("PCM_FLOAT");
        public static final Encoding ULAW = new Encoding("ULAW");
        public static final Encoding ALAW = new Encoding("ALAW");

        public Encoding(String str) {
            this.name = str;
        }

        public final boolean equals(Object obj) {
            if (toString() == null) {
                return obj != null && obj.toString() == null;
            }
            if (obj instanceof Encoding) {
                return toString().equals(obj.toString());
            }
            return false;
        }

        public final int hashCode() {
            if (toString() == null) {
                return 0;
            }
            return toString().hashCode();
        }

        public final String toString() {
            return this.name;
        }
    }
}
