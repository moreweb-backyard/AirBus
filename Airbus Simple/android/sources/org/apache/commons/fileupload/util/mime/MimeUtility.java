package org.apache.commons.fileupload.util.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public final class MimeUtility {
    private static final String BASE64_ENCODING_MARKER = "B";
    private static final String ENCODED_TOKEN_FINISHER = "?=";
    private static final String ENCODED_TOKEN_MARKER = "=?";
    private static final String LINEAR_WHITESPACE = " \t\r\n";
    private static final Map<String, String> MIME2JAVA = new HashMap();
    private static final String QUOTEDPRINTABLE_ENCODING_MARKER = "Q";
    private static final String US_ASCII_CHARSET = "US-ASCII";

    static {
        MIME2JAVA.put("iso-2022-cn", "ISO2022CN");
        MIME2JAVA.put("iso-2022-kr", "ISO2022KR");
        MIME2JAVA.put("utf-8", "UTF8");
        MIME2JAVA.put("utf8", "UTF8");
        MIME2JAVA.put("ja_jp.iso2022-7", "ISO2022JP");
        MIME2JAVA.put("ja_jp.eucjp", "EUCJIS");
        MIME2JAVA.put("euc-kr", "KSC5601");
        MIME2JAVA.put("euckr", "KSC5601");
        MIME2JAVA.put("us-ascii", "ISO-8859-1");
        MIME2JAVA.put("x-us-ascii", "ISO-8859-1");
    }

    private MimeUtility() {
    }

    public static String decodeText(String str) throws UnsupportedEncodingException {
        if (str.indexOf(ENCODED_TOKEN_MARKER) < 0) {
            return str;
        }
        int length = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        int i = 0;
        boolean z = false;
        int i2 = -1;
        int i3 = -1;
        while (i < length) {
            if (LINEAR_WHITESPACE.indexOf(str.charAt(i)) != -1) {
                int i4 = i;
                while (true) {
                    if (i4 < length) {
                        if (LINEAR_WHITESPACE.indexOf(str.charAt(i4)) == -1) {
                            i3 = i4;
                            i2 = i;
                            i = i3;
                            break;
                        }
                        i4++;
                    } else {
                        int i5 = i4;
                        i2 = i;
                        i = i5;
                        break;
                    }
                }
            } else {
                int i6 = i;
                while (i6 < length && LINEAR_WHITESPACE.indexOf(str.charAt(i6)) == -1) {
                    i6++;
                }
                String strSubstring = str.substring(i, i6);
                if (strSubstring.startsWith(ENCODED_TOKEN_MARKER)) {
                    try {
                        String strDecodeWord = decodeWord(strSubstring);
                        if (!z && i2 != -1) {
                            sb.append(str.substring(i2, i3));
                            i2 = -1;
                        }
                        sb.append(strDecodeWord);
                        z = true;
                        i = i6;
                    } catch (ParseException unused) {
                    }
                }
                if (i2 != -1) {
                    sb.append(str.substring(i2, i3));
                    i2 = -1;
                }
                sb.append(strSubstring);
                i = i6;
                z = false;
            }
        }
        return sb.toString();
    }

    private static String decodeWord(String str) throws ParseException, UnsupportedEncodingException {
        if (!str.startsWith(ENCODED_TOKEN_MARKER)) {
            throw new ParseException("Invalid RFC 2047 encoded-word: " + str);
        }
        int iIndexOf = str.indexOf(63, 2);
        if (iIndexOf == -1) {
            throw new ParseException("Missing charset in RFC 2047 encoded-word: " + str);
        }
        String lowerCase = str.substring(2, iIndexOf).toLowerCase();
        int i = iIndexOf + 1;
        int iIndexOf2 = str.indexOf(63, i);
        if (iIndexOf2 == -1) {
            throw new ParseException("Missing encoding in RFC 2047 encoded-word: " + str);
        }
        String strSubstring = str.substring(i, iIndexOf2);
        int i2 = iIndexOf2 + 1;
        int iIndexOf3 = str.indexOf(ENCODED_TOKEN_FINISHER, i2);
        if (iIndexOf3 == -1) {
            throw new ParseException("Missing encoded text in RFC 2047 encoded-word: " + str);
        }
        String strSubstring2 = str.substring(i2, iIndexOf3);
        if (strSubstring2.length() == 0) {
            return "";
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(strSubstring2.length());
            byte[] bytes = strSubstring2.getBytes("US-ASCII");
            if (strSubstring.equals(BASE64_ENCODING_MARKER)) {
                Base64Decoder.decode(bytes, byteArrayOutputStream);
            } else if (strSubstring.equals(QUOTEDPRINTABLE_ENCODING_MARKER)) {
                QuotedPrintableDecoder.decode(bytes, byteArrayOutputStream);
            } else {
                throw new UnsupportedEncodingException("Unknown RFC 2047 encoding: " + strSubstring);
            }
            return new String(byteArrayOutputStream.toByteArray(), javaCharset(lowerCase));
        } catch (IOException unused) {
            throw new UnsupportedEncodingException("Invalid RFC 2047 encoding");
        }
    }

    private static String javaCharset(String str) {
        if (str == null) {
            return null;
        }
        String str2 = MIME2JAVA.get(str.toLowerCase(Locale.ENGLISH));
        return str2 == null ? str : str2;
    }
}
