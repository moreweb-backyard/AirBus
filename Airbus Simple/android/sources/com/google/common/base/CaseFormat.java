package com.google.common.base;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public enum CaseFormat {
    LOWER_HYPHEN(Pattern.compile("[-]"), "-"),
    LOWER_UNDERSCORE(Pattern.compile("[_]"), "_"),
    LOWER_CAMEL(Pattern.compile("[A-Z]"), ""),
    UPPER_CAMEL(Pattern.compile("[A-Z]"), ""),
    UPPER_UNDERSCORE(Pattern.compile("[_]"), "_");

    private final Pattern wordBoundary;
    private final String wordSeparator;

    CaseFormat(Pattern pattern, String str) {
        this.wordBoundary = pattern;
        this.wordSeparator = str;
    }

    public String to(CaseFormat caseFormat, String str) {
        if (caseFormat == null) {
            throw new NullPointerException();
        }
        if (str == null) {
            throw new NullPointerException();
        }
        if (caseFormat == this) {
            return str;
        }
        switch (this) {
            case LOWER_UNDERSCORE:
                switch (caseFormat) {
                    case UPPER_UNDERSCORE:
                        return str.toUpperCase(Locale.US);
                    case LOWER_HYPHEN:
                        return str.replace("_", "-");
                }
            case UPPER_UNDERSCORE:
                int i = AnonymousClass1.$SwitchMap$com$google$common$base$CaseFormat[caseFormat.ordinal()];
                if (i == 1) {
                    return str.toLowerCase(Locale.US);
                }
                if (i == 3) {
                    return str.replace("_", "-").toLowerCase(Locale.US);
                }
                break;
            case LOWER_HYPHEN:
                switch (caseFormat) {
                    case LOWER_UNDERSCORE:
                        return str.replace("-", "_");
                    case UPPER_UNDERSCORE:
                        return str.replace("-", "_").toUpperCase(Locale.US);
                }
        }
        StringBuilder sb = null;
        int length = 0;
        Matcher matcher = this.wordBoundary.matcher(str);
        while (matcher.find()) {
            int iStart = matcher.start();
            if (length == 0) {
                sb = new StringBuilder(str.length() + (this.wordSeparator.length() * 4));
                sb.append(caseFormat.normalizeFirstWord(str.substring(length, iStart)));
            } else {
                sb.append(caseFormat.normalizeWord(str.substring(length, iStart)));
            }
            sb.append(caseFormat.wordSeparator);
            length = this.wordSeparator.length() + iStart;
        }
        if (length == 0) {
            return caseFormat.normalizeFirstWord(str);
        }
        sb.append(caseFormat.normalizeWord(str.substring(length)));
        return sb.toString();
    }

    private String normalizeFirstWord(String str) {
        if (AnonymousClass1.$SwitchMap$com$google$common$base$CaseFormat[ordinal()] == 4) {
            return str.toLowerCase(Locale.US);
        }
        return normalizeWord(str);
    }

    private String normalizeWord(String str) {
        switch (this) {
            case LOWER_UNDERSCORE:
                return str.toLowerCase(Locale.US);
            case UPPER_UNDERSCORE:
                return str.toUpperCase(Locale.US);
            case LOWER_HYPHEN:
                return str.toLowerCase(Locale.US);
            case LOWER_CAMEL:
                return toTitleCase(str);
            case UPPER_CAMEL:
                return toTitleCase(str);
            default:
                throw new RuntimeException("unknown case: " + this);
        }
    }

    private static String toTitleCase(String str) {
        if (str.length() < 2) {
            return str.toUpperCase(Locale.US);
        }
        return Character.toTitleCase(str.charAt(0)) + str.substring(1).toLowerCase(Locale.US);
    }
}
