package voice;

import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class Util {
    public static int char64ToInt(byte b) {
        if (b >= 65 && b <= 90) {
            return b - 65;
        }
        if (b >= 97 && b <= 122) {
            return (b - 97) + 26;
        }
        if (b >= 48 && b <= 57) {
            return (b - 48) + 52;
        }
        if (b == 95) {
            return 62;
        }
        return b == 45 ? 63 : -1;
    }

    public static int hexChar2Int(byte b) {
        if (b >= 48 && b <= 57) {
            return b - 48;
        }
        if (b >= 97 && b <= 122) {
            return (b - 97) + 10;
        }
        if (b < 65 || b > 90) {
            return -1;
        }
        return (b - 65) + 10;
    }

    public static byte int2Char64(int i) {
        if (i >= 0 && i < 26) {
            return (byte) (i + 65);
        }
        if (i >= 26 && i < 52) {
            return (byte) ((i + 97) - 26);
        }
        if (i >= 52 && i < 62) {
            return (byte) ((i + 48) - 52);
        }
        if (i == 62) {
            return (byte) 95;
        }
        if (i == 63) {
            return MultipartStream.DASH;
        }
        return (byte) -1;
    }

    public static boolean is64Char(byte b) {
        return (b >= 97 && b <= 122) || (b >= 65 && b <= 90) || ((b >= 48 && b <= 57) || b == 95 || b == 45);
    }

    public static boolean isDigit(byte b) {
        return b >= 48 && b <= 57;
    }

    public static boolean isLowerChar(byte b) {
        return b >= 97 && b <= 122;
    }

    public static boolean isUpperChar(byte b) {
        return b >= 65 && b <= 90;
    }
}
