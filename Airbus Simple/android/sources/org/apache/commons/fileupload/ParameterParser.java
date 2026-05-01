package org.apache.commons.fileupload;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.fileupload.util.mime.MimeUtility;

/* loaded from: classes.dex */
public class ParameterParser {
    private char[] chars = null;
    private int pos = 0;
    private int len = 0;
    private int i1 = 0;
    private int i2 = 0;
    private boolean lowerCaseNames = false;

    private boolean hasChar() {
        return this.pos < this.len;
    }

    private String getToken(boolean z) {
        while (this.i1 < this.i2 && Character.isWhitespace(this.chars[this.i1])) {
            this.i1++;
        }
        while (this.i2 > this.i1 && Character.isWhitespace(this.chars[this.i2 - 1])) {
            this.i2--;
        }
        if (z && this.i2 - this.i1 >= 2 && this.chars[this.i1] == '\"' && this.chars[this.i2 - 1] == '\"') {
            this.i1++;
            this.i2--;
        }
        if (this.i2 > this.i1) {
            return new String(this.chars, this.i1, this.i2 - this.i1);
        }
        return null;
    }

    private boolean isOneOf(char c, char[] cArr) {
        for (char c2 : cArr) {
            if (c == c2) {
                return true;
            }
        }
        return false;
    }

    private String parseToken(char[] cArr) {
        this.i1 = this.pos;
        this.i2 = this.pos;
        while (hasChar() && !isOneOf(this.chars[this.pos], cArr)) {
            this.i2++;
            this.pos++;
        }
        return getToken(false);
    }

    private String parseQuotedToken(char[] cArr) {
        this.i1 = this.pos;
        this.i2 = this.pos;
        boolean z = false;
        boolean z2 = false;
        while (hasChar()) {
            char c = this.chars[this.pos];
            if (!z && isOneOf(c, cArr)) {
                break;
            }
            if (!z2 && c == '\"') {
                z = !z;
            }
            z2 = !z2 && c == '\\';
            this.i2++;
            this.pos++;
        }
        return getToken(true);
    }

    public boolean isLowerCaseNames() {
        return this.lowerCaseNames;
    }

    public void setLowerCaseNames(boolean z) {
        this.lowerCaseNames = z;
    }

    public Map<String, String> parse(String str, char[] cArr) {
        if (cArr == null || cArr.length == 0) {
            return new HashMap();
        }
        char c = cArr[0];
        if (str != null) {
            int length = str.length();
            for (char c2 : cArr) {
                int iIndexOf = str.indexOf(c2);
                if (iIndexOf != -1 && iIndexOf < length) {
                    c = c2;
                    length = iIndexOf;
                }
            }
        }
        return parse(str, c);
    }

    public Map<String, String> parse(String str, char c) {
        if (str == null) {
            return new HashMap();
        }
        return parse(str.toCharArray(), c);
    }

    public Map<String, String> parse(char[] cArr, char c) {
        if (cArr == null) {
            return new HashMap();
        }
        return parse(cArr, 0, cArr.length, c);
    }

    public Map<String, String> parse(char[] cArr, int i, int i2, char c) throws UnsupportedEncodingException {
        if (cArr == null) {
            return new HashMap();
        }
        HashMap map = new HashMap();
        this.chars = cArr;
        this.pos = i;
        this.len = i2;
        while (hasChar()) {
            String token = parseToken(new char[]{'=', c});
            String quotedToken = null;
            if (hasChar() && cArr[this.pos] == '=') {
                this.pos++;
                quotedToken = parseQuotedToken(new char[]{c});
                if (quotedToken != null) {
                    try {
                        quotedToken = MimeUtility.decodeText(quotedToken);
                    } catch (UnsupportedEncodingException unused) {
                    }
                }
            }
            if (hasChar() && cArr[this.pos] == c) {
                this.pos++;
            }
            if (token != null && token.length() > 0) {
                if (this.lowerCaseNames) {
                    token = token.toLowerCase(Locale.ENGLISH);
                }
                map.put(token, quotedToken);
            }
        }
        return map;
    }
}
