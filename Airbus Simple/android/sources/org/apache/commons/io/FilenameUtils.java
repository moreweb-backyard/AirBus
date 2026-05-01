package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;

/* loaded from: classes.dex */
public class FilenameUtils {
    private static final int NOT_FOUND = -1;
    private static final char OTHER_SEPARATOR;
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = Character.toString(EXTENSION_SEPARATOR);
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    private static boolean isSeparator(char c) {
        return c == '/' || c == '\\';
    }

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = '/';
        } else {
            OTHER_SEPARATOR = '\\';
        }
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == '\\';
    }

    public static String normalize(String str) {
        return doNormalize(str, SYSTEM_SEPARATOR, true);
    }

    public static String normalize(String str, boolean z) {
        return doNormalize(str, z ? '/' : '\\', true);
    }

    public static String normalizeNoEndSeparator(String str) {
        return doNormalize(str, SYSTEM_SEPARATOR, false);
    }

    public static String normalizeNoEndSeparator(String str, boolean z) {
        return doNormalize(str, z ? '/' : '\\', false);
    }

    private static String doNormalize(String str, char c, boolean z) {
        int i;
        boolean z2;
        int i2;
        if (str == null) {
            return null;
        }
        failIfNullBytePresent(str);
        int length = str.length();
        if (length == 0) {
            return str;
        }
        int prefixLength = getPrefixLength(str);
        if (prefixLength < 0) {
            return null;
        }
        char[] cArr = new char[length + 2];
        str.getChars(0, str.length(), cArr, 0);
        char c2 = c == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
        for (int i3 = 0; i3 < cArr.length; i3++) {
            if (cArr[i3] == c2) {
                cArr[i3] = c;
            }
        }
        if (cArr[length - 1] != c) {
            i = length + 1;
            cArr[length] = c;
            z2 = false;
        } else {
            i = length;
            z2 = true;
        }
        int i4 = prefixLength + 1;
        int i5 = i;
        int i6 = i4;
        while (i6 < i5) {
            if (cArr[i6] == c) {
                int i7 = i6 - 1;
                if (cArr[i7] == c) {
                    System.arraycopy(cArr, i6, cArr, i7, i5 - i6);
                    i5--;
                    i6--;
                }
            }
            i6++;
        }
        int i8 = i4;
        while (i8 < i5) {
            if (cArr[i8] == c) {
                int i9 = i8 - 1;
                if (cArr[i9] == '.' && (i8 == i4 || cArr[i8 - 2] == c)) {
                    if (i8 == i5 - 1) {
                        z2 = true;
                    }
                    System.arraycopy(cArr, i8 + 1, cArr, i9, i5 - i8);
                    i5 -= 2;
                    i8--;
                }
            }
            i8++;
        }
        int i10 = prefixLength + 2;
        boolean z3 = z2;
        int i11 = i10;
        while (i11 < i5) {
            if (cArr[i11] != c || cArr[i11 - 1] != '.' || cArr[i11 - 2] != '.' || (i11 != i10 && cArr[i11 - 3] != c)) {
                i2 = i11;
            } else {
                if (i11 == i10) {
                    return null;
                }
                if (i11 == i5 - 1) {
                    z3 = true;
                }
                int i12 = i11 - 4;
                while (true) {
                    if (i12 >= prefixLength) {
                        if (cArr[i12] == c) {
                            i2 = i12 + 1;
                            System.arraycopy(cArr, i11 + 1, cArr, i2, i5 - i11);
                            i5 -= i11 - i12;
                            break;
                        }
                        i12--;
                    } else {
                        int i13 = i11 + 1;
                        System.arraycopy(cArr, i13, cArr, prefixLength, i5 - i11);
                        i5 -= i13 - prefixLength;
                        i2 = i4;
                        break;
                    }
                }
            }
            i11 = i2 + 1;
        }
        if (i5 <= 0) {
            return "";
        }
        if (i5 <= prefixLength) {
            return new String(cArr, 0, i5);
        }
        if (z3 && z) {
            return new String(cArr, 0, i5);
        }
        return new String(cArr, 0, i5 - 1);
    }

    public static String concat(String str, String str2) {
        int prefixLength = getPrefixLength(str2);
        if (prefixLength < 0) {
            return null;
        }
        if (prefixLength > 0) {
            return normalize(str2);
        }
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length == 0) {
            return normalize(str2);
        }
        if (isSeparator(str.charAt(length - 1))) {
            return normalize(str + str2);
        }
        return normalize(str + '/' + str2);
    }

    public static boolean directoryContains(String str, String str2) throws IOException {
        if (str == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }
        if (str2 == null || IOCase.SYSTEM.checkEquals(str, str2)) {
            return false;
        }
        return IOCase.SYSTEM.checkStartsWith(str2, str);
    }

    public static String separatorsToUnix(String str) {
        return (str == null || str.indexOf(92) == -1) ? str : str.replace('\\', '/');
    }

    public static String separatorsToWindows(String str) {
        return (str == null || str.indexOf(47) == -1) ? str : str.replace('/', '\\');
    }

    public static String separatorsToSystem(String str) {
        if (str == null) {
            return null;
        }
        if (isSystemWindows()) {
            return separatorsToWindows(str);
        }
        return separatorsToUnix(str);
    }

    public static int getPrefixLength(String str) {
        if (str == null) {
            return -1;
        }
        int length = str.length();
        if (length == 0) {
            return 0;
        }
        char cCharAt = str.charAt(0);
        if (cCharAt == ':') {
            return -1;
        }
        if (length == 1) {
            if (cCharAt == '~') {
                return 2;
            }
            return isSeparator(cCharAt) ? 1 : 0;
        }
        if (cCharAt == '~') {
            int iIndexOf = str.indexOf(47, 1);
            int iIndexOf2 = str.indexOf(92, 1);
            if (iIndexOf == -1 && iIndexOf2 == -1) {
                return length + 1;
            }
            if (iIndexOf == -1) {
                iIndexOf = iIndexOf2;
            }
            if (iIndexOf2 == -1) {
                iIndexOf2 = iIndexOf;
            }
            return Math.min(iIndexOf, iIndexOf2) + 1;
        }
        char cCharAt2 = str.charAt(1);
        if (cCharAt2 == ':') {
            char upperCase = Character.toUpperCase(cCharAt);
            if (upperCase < 'A' || upperCase > 'Z') {
                return -1;
            }
            return (length == 2 || !isSeparator(str.charAt(2))) ? 2 : 3;
        }
        if (!isSeparator(cCharAt) || !isSeparator(cCharAt2)) {
            return isSeparator(cCharAt) ? 1 : 0;
        }
        int iIndexOf3 = str.indexOf(47, 2);
        int iIndexOf4 = str.indexOf(92, 2);
        if ((iIndexOf3 == -1 && iIndexOf4 == -1) || iIndexOf3 == 2 || iIndexOf4 == 2) {
            return -1;
        }
        if (iIndexOf3 == -1) {
            iIndexOf3 = iIndexOf4;
        }
        if (iIndexOf4 == -1) {
            iIndexOf4 = iIndexOf3;
        }
        return Math.min(iIndexOf3, iIndexOf4) + 1;
    }

    public static int indexOfLastSeparator(String str) {
        if (str == null) {
            return -1;
        }
        return Math.max(str.lastIndexOf(47), str.lastIndexOf(92));
    }

    public static int indexOfExtension(String str) {
        int iLastIndexOf;
        if (str != null && indexOfLastSeparator(str) <= (iLastIndexOf = str.lastIndexOf(46))) {
            return iLastIndexOf;
        }
        return -1;
    }

    public static String getPrefix(String str) {
        int prefixLength;
        if (str == null || (prefixLength = getPrefixLength(str)) < 0) {
            return null;
        }
        if (prefixLength > str.length()) {
            failIfNullBytePresent(str + '/');
            return str + '/';
        }
        String strSubstring = str.substring(0, prefixLength);
        failIfNullBytePresent(strSubstring);
        return strSubstring;
    }

    public static String getPath(String str) {
        return doGetPath(str, 1);
    }

    public static String getPathNoEndSeparator(String str) {
        return doGetPath(str, 0);
    }

    private static String doGetPath(String str, int i) {
        int prefixLength;
        if (str == null || (prefixLength = getPrefixLength(str)) < 0) {
            return null;
        }
        int iIndexOfLastSeparator = indexOfLastSeparator(str);
        int i2 = i + iIndexOfLastSeparator;
        if (prefixLength >= str.length() || iIndexOfLastSeparator < 0 || prefixLength >= i2) {
            return "";
        }
        String strSubstring = str.substring(prefixLength, i2);
        failIfNullBytePresent(strSubstring);
        return strSubstring;
    }

    public static String getFullPath(String str) {
        return doGetFullPath(str, true);
    }

    public static String getFullPathNoEndSeparator(String str) {
        return doGetFullPath(str, false);
    }

    private static String doGetFullPath(String str, boolean z) {
        int prefixLength;
        if (str == null || (prefixLength = getPrefixLength(str)) < 0) {
            return null;
        }
        if (prefixLength >= str.length()) {
            return z ? getPrefix(str) : str;
        }
        int iIndexOfLastSeparator = indexOfLastSeparator(str);
        if (iIndexOfLastSeparator < 0) {
            return str.substring(0, prefixLength);
        }
        int i = iIndexOfLastSeparator + (z ? 1 : 0);
        if (i == 0) {
            i++;
        }
        return str.substring(0, i);
    }

    public static String getName(String str) {
        if (str == null) {
            return null;
        }
        failIfNullBytePresent(str);
        return str.substring(indexOfLastSeparator(str) + 1);
    }

    private static void failIfNullBytePresent(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (str.charAt(i) == 0) {
                throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
            }
        }
    }

    public static String getBaseName(String str) {
        return removeExtension(getName(str));
    }

    public static String getExtension(String str) {
        if (str == null) {
            return null;
        }
        int iIndexOfExtension = indexOfExtension(str);
        return iIndexOfExtension == -1 ? "" : str.substring(iIndexOfExtension + 1);
    }

    public static String removeExtension(String str) {
        if (str == null) {
            return null;
        }
        failIfNullBytePresent(str);
        int iIndexOfExtension = indexOfExtension(str);
        return iIndexOfExtension == -1 ? str : str.substring(0, iIndexOfExtension);
    }

    public static boolean equals(String str, String str2) {
        return equals(str, str2, false, IOCase.SENSITIVE);
    }

    public static boolean equalsOnSystem(String str, String str2) {
        return equals(str, str2, false, IOCase.SYSTEM);
    }

    public static boolean equalsNormalized(String str, String str2) {
        return equals(str, str2, true, IOCase.SENSITIVE);
    }

    public static boolean equalsNormalizedOnSystem(String str, String str2) {
        return equals(str, str2, true, IOCase.SYSTEM);
    }

    public static boolean equals(String str, String str2, boolean z, IOCase iOCase) {
        if (str == null || str2 == null) {
            return str == null && str2 == null;
        }
        if (z) {
            str = normalize(str);
            str2 = normalize(str2);
            if (str == null || str2 == null) {
                throw new NullPointerException("Error normalizing one or both of the file names");
            }
        }
        if (iOCase == null) {
            iOCase = IOCase.SENSITIVE;
        }
        return iOCase.checkEquals(str, str2);
    }

    public static boolean isExtension(String str, String str2) {
        if (str == null) {
            return false;
        }
        failIfNullBytePresent(str);
        if (str2 == null || str2.isEmpty()) {
            return indexOfExtension(str) == -1;
        }
        return getExtension(str).equals(str2);
    }

    public static boolean isExtension(String str, String[] strArr) {
        if (str == null) {
            return false;
        }
        failIfNullBytePresent(str);
        if (strArr == null || strArr.length == 0) {
            return indexOfExtension(str) == -1;
        }
        String extension = getExtension(str);
        for (String str2 : strArr) {
            if (extension.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExtension(String str, Collection<String> collection) {
        if (str == null) {
            return false;
        }
        failIfNullBytePresent(str);
        if (collection == null || collection.isEmpty()) {
            return indexOfExtension(str) == -1;
        }
        String extension = getExtension(str);
        Iterator<String> it = collection.iterator();
        while (it.hasNext()) {
            if (extension.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    public static boolean wildcardMatch(String str, String str2) {
        return wildcardMatch(str, str2, IOCase.SENSITIVE);
    }

    public static boolean wildcardMatchOnSystem(String str, String str2) {
        return wildcardMatch(str, str2, IOCase.SYSTEM);
    }

    public static boolean wildcardMatch(String str, String str2, IOCase iOCase) {
        boolean z;
        int length;
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null || str2 == null) {
            return false;
        }
        if (iOCase == null) {
            iOCase = IOCase.SENSITIVE;
        }
        String[] strArrSplitOnTokens = splitOnTokens(str2);
        Stack stack = new Stack();
        boolean z2 = false;
        int i = 0;
        int i2 = 0;
        while (true) {
            if (stack.size() > 0) {
                int[] iArr = (int[]) stack.pop();
                int i3 = iArr[0];
                length = iArr[1];
                i2 = i3;
                z = true;
            } else {
                int i4 = i;
                z = z2;
                length = i4;
            }
            while (i2 < strArrSplitOnTokens.length) {
                if (strArrSplitOnTokens[i2].equals("?")) {
                    length++;
                    if (length > str.length()) {
                        break;
                    }
                    z = false;
                } else if (strArrSplitOnTokens[i2].equals("*")) {
                    if (i2 == strArrSplitOnTokens.length - 1) {
                        length = str.length();
                    }
                    z = true;
                } else {
                    if (z) {
                        length = iOCase.checkIndexOf(str, length, strArrSplitOnTokens[i2]);
                        if (length == -1) {
                            break;
                        }
                        int iCheckIndexOf = iOCase.checkIndexOf(str, length + 1, strArrSplitOnTokens[i2]);
                        if (iCheckIndexOf >= 0) {
                            stack.push(new int[]{i2, iCheckIndexOf});
                        }
                        length += strArrSplitOnTokens[i2].length();
                    } else {
                        if (!iOCase.checkRegionMatches(str, length, strArrSplitOnTokens[i2])) {
                            break;
                        }
                        length += strArrSplitOnTokens[i2].length();
                    }
                    z = false;
                }
                i2++;
            }
            if (i2 == strArrSplitOnTokens.length && length == str.length()) {
                return true;
            }
            if (stack.size() <= 0) {
                return false;
            }
            boolean z3 = z;
            i = length;
            z2 = z3;
        }
    }

    static String[] splitOnTokens(String str) {
        if (str.indexOf(63) == -1 && str.indexOf(42) == -1) {
            return new String[]{str};
        }
        char[] charArray = str.toCharArray();
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        int length = charArray.length;
        int i = 0;
        char c = 0;
        while (i < length) {
            char c2 = charArray[i];
            if (c2 == '?' || c2 == '*') {
                if (sb.length() != 0) {
                    arrayList.add(sb.toString());
                    sb.setLength(0);
                }
                if (c2 == '?') {
                    arrayList.add("?");
                } else if (c != '*') {
                    arrayList.add("*");
                }
            } else {
                sb.append(c2);
            }
            i++;
            c = c2;
        }
        if (sb.length() != 0) {
            arrayList.add(sb.toString());
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }
}
