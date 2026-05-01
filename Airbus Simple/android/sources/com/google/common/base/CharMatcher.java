package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import cz.msebera.android.httpclient.message.TokenParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;

@GwtCompatible
/* loaded from: classes.dex */
public abstract class CharMatcher implements Predicate<Character> {
    public static final CharMatcher ANY;
    public static final CharMatcher DIGIT;
    public static final CharMatcher INVISIBLE;
    public static final CharMatcher JAVA_DIGIT;
    public static final CharMatcher JAVA_ISO_CONTROL;
    public static final CharMatcher JAVA_LETTER;
    public static final CharMatcher JAVA_LETTER_OR_DIGIT;
    public static final CharMatcher JAVA_LOWER_CASE;
    public static final CharMatcher JAVA_UPPER_CASE;
    public static final CharMatcher JAVA_WHITESPACE;
    public static final CharMatcher NONE;
    private static final String NON_BREAKING_WHITESPACE_CHARS = " \u180e ";
    public static final CharMatcher SINGLE_WIDTH;
    public static final CharMatcher WHITESPACE = anyOf("\t\n\u000b\f\r \u0085\u1680\u2028\u2029\u205f\u3000 \u180e ").or(inRange(8192, 8202));
    private static final String BREAKING_WHITESPACE_CHARS = "\t\n\u000b\f\r \u0085\u1680\u2028\u2029\u205f\u3000";
    public static final CharMatcher BREAKING_WHITESPACE = anyOf(BREAKING_WHITESPACE_CHARS).or(inRange(8192, 8198)).or(inRange(8200, 8202));
    public static final CharMatcher ASCII = inRange(0, 127);

    public abstract boolean matches(char c);

    static {
        CharMatcher charMatcherInRange = inRange('0', '9');
        CharMatcher charMatcherOr = charMatcherInRange;
        for (char c : "٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray()) {
            charMatcherOr = charMatcherOr.or(inRange(c, (char) (c + '\t')));
        }
        DIGIT = charMatcherOr;
        JAVA_WHITESPACE = inRange('\t', TokenParser.CR).or(inRange((char) 28, TokenParser.SP)).or(is((char) 5760)).or(is((char) 6158)).or(inRange((char) 8192, (char) 8198)).or(inRange((char) 8200, (char) 8203)).or(inRange((char) 8232, (char) 8233)).or(is((char) 8287)).or(is((char) 12288));
        JAVA_DIGIT = new CharMatcher() { // from class: com.google.common.base.CharMatcher.1
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return Character.isDigit(c2);
            }
        };
        JAVA_LETTER = new CharMatcher() { // from class: com.google.common.base.CharMatcher.2
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return Character.isLetter(c2);
            }
        };
        JAVA_LETTER_OR_DIGIT = new CharMatcher() { // from class: com.google.common.base.CharMatcher.3
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return Character.isLetterOrDigit(c2);
            }
        };
        JAVA_UPPER_CASE = new CharMatcher() { // from class: com.google.common.base.CharMatcher.4
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return Character.isUpperCase(c2);
            }
        };
        JAVA_LOWER_CASE = new CharMatcher() { // from class: com.google.common.base.CharMatcher.5
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return Character.isLowerCase(c2);
            }
        };
        JAVA_ISO_CONTROL = inRange((char) 0, (char) 31).or(inRange((char) 127, (char) 159));
        INVISIBLE = inRange((char) 0, TokenParser.SP).or(inRange((char) 127, (char) 160)).or(is((char) 173)).or(inRange((char) 1536, (char) 1539)).or(anyOf("\u06dd\u070f\u1680឴឵\u180e")).or(inRange((char) 8192, (char) 8207)).or(inRange((char) 8232, (char) 8239)).or(inRange((char) 8287, (char) 8292)).or(inRange((char) 8298, (char) 8303)).or(is((char) 12288)).or(inRange((char) 55296, (char) 63743)).or(anyOf("\ufeff\ufff9\ufffa\ufffb"));
        SINGLE_WIDTH = inRange((char) 0, (char) 1273).or(is((char) 1470)).or(inRange((char) 1488, (char) 1514)).or(is((char) 1523)).or(is((char) 1524)).or(inRange((char) 1536, (char) 1791)).or(inRange((char) 1872, (char) 1919)).or(inRange((char) 3584, (char) 3711)).or(inRange((char) 7680, (char) 8367)).or(inRange((char) 8448, (char) 8506)).or(inRange((char) 64336, (char) 65023)).or(inRange((char) 65136, ByteOrderMark.UTF_BOM)).or(inRange((char) 65377, (char) 65500));
        ANY = new CharMatcher() { // from class: com.google.common.base.CharMatcher.6
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return true;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher precomputed() {
                return this;
            }

            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence charSequence) {
                return charSequence.length() == 0 ? -1 : 0;
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence charSequence, int i) {
                int length = charSequence.length();
                Preconditions.checkPositionIndex(i, length);
                if (i == length) {
                    return -1;
                }
                return i;
            }

            @Override // com.google.common.base.CharMatcher
            public int lastIndexIn(CharSequence charSequence) {
                return charSequence.length() - 1;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesAllOf(CharSequence charSequence) {
                Preconditions.checkNotNull(charSequence);
                return true;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesNoneOf(CharSequence charSequence) {
                return charSequence.length() == 0;
            }

            @Override // com.google.common.base.CharMatcher
            public String removeFrom(CharSequence charSequence) {
                Preconditions.checkNotNull(charSequence);
                return "";
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence charSequence, char c2) {
                char[] cArr = new char[charSequence.length()];
                Arrays.fill(cArr, c2);
                return new String(cArr);
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
                StringBuilder sb = new StringBuilder(charSequence.length() * charSequence2.length());
                for (int i = 0; i < charSequence.length(); i++) {
                    sb.append(charSequence2);
                }
                return sb.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String collapseFrom(CharSequence charSequence, char c2) {
                return charSequence.length() == 0 ? "" : String.valueOf(c2);
            }

            @Override // com.google.common.base.CharMatcher
            public String trimFrom(CharSequence charSequence) {
                Preconditions.checkNotNull(charSequence);
                return "";
            }

            @Override // com.google.common.base.CharMatcher
            public int countIn(CharSequence charSequence) {
                return charSequence.length();
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher charMatcher) {
                return (CharMatcher) Preconditions.checkNotNull(charMatcher);
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher charMatcher) {
                Preconditions.checkNotNull(charMatcher);
                return this;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return NONE;
            }
        };
        NONE = new CharMatcher() { // from class: com.google.common.base.CharMatcher.7
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return false;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher precomputed() {
                return this;
            }

            @Override // com.google.common.base.CharMatcher
            protected void setBits(LookupTable lookupTable) {
            }

            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence charSequence) {
                Preconditions.checkNotNull(charSequence);
                return -1;
            }

            @Override // com.google.common.base.CharMatcher
            public int indexIn(CharSequence charSequence, int i) {
                Preconditions.checkPositionIndex(i, charSequence.length());
                return -1;
            }

            @Override // com.google.common.base.CharMatcher
            public int lastIndexIn(CharSequence charSequence) {
                Preconditions.checkNotNull(charSequence);
                return -1;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesAllOf(CharSequence charSequence) {
                return charSequence.length() == 0;
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesNoneOf(CharSequence charSequence) {
                Preconditions.checkNotNull(charSequence);
                return true;
            }

            @Override // com.google.common.base.CharMatcher
            public String removeFrom(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence charSequence, char c2) {
                return charSequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
                Preconditions.checkNotNull(charSequence2);
                return charSequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String collapseFrom(CharSequence charSequence, char c2) {
                return charSequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public String trimFrom(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override // com.google.common.base.CharMatcher
            public int countIn(CharSequence charSequence) {
                Preconditions.checkNotNull(charSequence);
                return 0;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher charMatcher) {
                Preconditions.checkNotNull(charMatcher);
                return this;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher charMatcher) {
                return (CharMatcher) Preconditions.checkNotNull(charMatcher);
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return ANY;
            }
        };
    }

    public static CharMatcher is(final char c) {
        return new CharMatcher() { // from class: com.google.common.base.CharMatcher.8
            @Override // com.google.common.base.CharMatcher
            public CharMatcher precomputed() {
                return this;
            }

            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return c2 == c;
            }

            @Override // com.google.common.base.CharMatcher
            public String replaceFrom(CharSequence charSequence, char c2) {
                return charSequence.toString().replace(c, c2);
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? this : NONE;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? charMatcher : super.or(charMatcher);
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return isNot(c);
            }

            @Override // com.google.common.base.CharMatcher
            protected void setBits(LookupTable lookupTable) {
                lookupTable.set(c);
            }
        };
    }

    public static CharMatcher isNot(final char c) {
        return new CharMatcher() { // from class: com.google.common.base.CharMatcher.9
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c2) {
                return c2 != c;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher and(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? super.and(charMatcher) : charMatcher;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher or(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? ANY : this;
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return is(c);
            }
        };
    }

    public static CharMatcher anyOf(CharSequence charSequence) {
        switch (charSequence.length()) {
            case 0:
                return NONE;
            case 1:
                return is(charSequence.charAt(0));
            case 2:
                final char cCharAt = charSequence.charAt(0);
                final char cCharAt2 = charSequence.charAt(1);
                return new CharMatcher() { // from class: com.google.common.base.CharMatcher.10
                    @Override // com.google.common.base.CharMatcher
                    public CharMatcher precomputed() {
                        return this;
                    }

                    @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
                    public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                        return super.apply(ch);
                    }

                    @Override // com.google.common.base.CharMatcher
                    public boolean matches(char c) {
                        return c == cCharAt || c == cCharAt2;
                    }

                    @Override // com.google.common.base.CharMatcher
                    protected void setBits(LookupTable lookupTable) {
                        lookupTable.set(cCharAt);
                        lookupTable.set(cCharAt2);
                    }
                };
            default:
                final char[] charArray = charSequence.toString().toCharArray();
                Arrays.sort(charArray);
                return new CharMatcher() { // from class: com.google.common.base.CharMatcher.11
                    @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
                    public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                        return super.apply(ch);
                    }

                    @Override // com.google.common.base.CharMatcher
                    public boolean matches(char c) {
                        return Arrays.binarySearch(charArray, c) >= 0;
                    }

                    @Override // com.google.common.base.CharMatcher
                    protected void setBits(LookupTable lookupTable) {
                        for (char c : charArray) {
                            lookupTable.set(c);
                        }
                    }
                };
        }
    }

    public static CharMatcher noneOf(CharSequence charSequence) {
        return anyOf(charSequence).negate();
    }

    public static CharMatcher inRange(final char c, final char c2) {
        Preconditions.checkArgument(c2 >= c);
        return new CharMatcher() { // from class: com.google.common.base.CharMatcher.12
            @Override // com.google.common.base.CharMatcher
            public CharMatcher precomputed() {
                return this;
            }

            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c3) {
                return c <= c3 && c3 <= c2;
            }

            @Override // com.google.common.base.CharMatcher
            protected void setBits(LookupTable lookupTable) {
                char c3 = c;
                while (true) {
                    lookupTable.set(c3);
                    char c4 = (char) (c3 + 1);
                    if (c3 == c2) {
                        return;
                    } else {
                        c3 = c4;
                    }
                }
            }
        };
    }

    public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
        Preconditions.checkNotNull(predicate);
        if (predicate instanceof CharMatcher) {
            return (CharMatcher) predicate;
        }
        return new CharMatcher() { // from class: com.google.common.base.CharMatcher.13
            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return predicate.apply(Character.valueOf(c));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public boolean apply(Character ch) {
                return predicate.apply(Preconditions.checkNotNull(ch));
            }
        };
    }

    public CharMatcher negate() {
        return new CharMatcher() { // from class: com.google.common.base.CharMatcher.14
            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return !this.matches(c);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesAllOf(CharSequence charSequence) {
                return this.matchesNoneOf(charSequence);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matchesNoneOf(CharSequence charSequence) {
                return this.matchesAllOf(charSequence);
            }

            @Override // com.google.common.base.CharMatcher
            public int countIn(CharSequence charSequence) {
                return charSequence.length() - this.countIn(charSequence);
            }

            @Override // com.google.common.base.CharMatcher
            public CharMatcher negate() {
                return this;
            }
        };
    }

    public CharMatcher and(CharMatcher charMatcher) {
        return new And(Arrays.asList(this, (CharMatcher) Preconditions.checkNotNull(charMatcher)));
    }

    private static class And extends CharMatcher {
        List<CharMatcher> components;

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch) {
            return super.apply(ch);
        }

        And(List<CharMatcher> list) {
            this.components = list;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            Iterator<CharMatcher> it = this.components.iterator();
            while (it.hasNext()) {
                if (!it.next().matches(c)) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher and(CharMatcher charMatcher) {
            ArrayList arrayList = new ArrayList(this.components);
            arrayList.add(Preconditions.checkNotNull(charMatcher));
            return new And(arrayList);
        }
    }

    public CharMatcher or(CharMatcher charMatcher) {
        return new Or(Arrays.asList(this, (CharMatcher) Preconditions.checkNotNull(charMatcher)));
    }

    private static class Or extends CharMatcher {
        List<CharMatcher> components;

        @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
        public /* bridge */ /* synthetic */ boolean apply(Character ch) {
            return super.apply(ch);
        }

        Or(List<CharMatcher> list) {
            this.components = list;
        }

        @Override // com.google.common.base.CharMatcher
        public boolean matches(char c) {
            Iterator<CharMatcher> it = this.components.iterator();
            while (it.hasNext()) {
                if (it.next().matches(c)) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.google.common.base.CharMatcher
        public CharMatcher or(CharMatcher charMatcher) {
            ArrayList arrayList = new ArrayList(this.components);
            arrayList.add(Preconditions.checkNotNull(charMatcher));
            return new Or(arrayList);
        }

        @Override // com.google.common.base.CharMatcher
        protected void setBits(LookupTable lookupTable) {
            Iterator<CharMatcher> it = this.components.iterator();
            while (it.hasNext()) {
                it.next().setBits(lookupTable);
            }
        }
    }

    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }

    CharMatcher precomputedInternal() {
        final LookupTable lookupTable = new LookupTable();
        setBits(lookupTable);
        return new CharMatcher() { // from class: com.google.common.base.CharMatcher.15
            @Override // com.google.common.base.CharMatcher
            public CharMatcher precomputed() {
                return this;
            }

            @Override // com.google.common.base.CharMatcher, com.google.common.base.Predicate
            public /* bridge */ /* synthetic */ boolean apply(Character ch) {
                return super.apply(ch);
            }

            @Override // com.google.common.base.CharMatcher
            public boolean matches(char c) {
                return lookupTable.get(c);
            }
        };
    }

    protected void setBits(LookupTable lookupTable) {
        char c = 0;
        while (true) {
            if (matches(c)) {
                lookupTable.set(c);
            }
            char c2 = (char) (c + 1);
            if (c == 65535) {
                return;
            } else {
                c = c2;
            }
        }
    }

    protected static class LookupTable {
        int[] data = new int[2048];

        protected LookupTable() {
        }

        void set(char c) {
            int[] iArr = this.data;
            int i = c >> 5;
            iArr[i] = (1 << c) | iArr[i];
        }

        boolean get(char c) {
            return ((1 << c) & this.data[c >> 5]) != 0;
        }
    }

    public boolean matchesAllOf(CharSequence charSequence) {
        for (int length = charSequence.length() - 1; length >= 0; length--) {
            if (!matches(charSequence.charAt(length))) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesNoneOf(CharSequence charSequence) {
        return indexIn(charSequence) == -1;
    }

    public int indexIn(CharSequence charSequence) {
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (matches(charSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int indexIn(CharSequence charSequence, int i) {
        int length = charSequence.length();
        Preconditions.checkPositionIndex(i, length);
        while (i < length) {
            if (matches(charSequence.charAt(i))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int lastIndexIn(CharSequence charSequence) {
        for (int length = charSequence.length() - 1; length >= 0; length--) {
            if (matches(charSequence.charAt(length))) {
                return length;
            }
        }
        return -1;
    }

    public int countIn(CharSequence charSequence) {
        int i = 0;
        for (int i2 = 0; i2 < charSequence.length(); i2++) {
            if (matches(charSequence.charAt(i2))) {
                i++;
            }
        }
        return i;
    }

    public String removeFrom(CharSequence charSequence) {
        String string = charSequence.toString();
        int iIndexIn = indexIn(string);
        if (iIndexIn == -1) {
            return string;
        }
        char[] charArray = string.toCharArray();
        int i = 1;
        while (true) {
            iIndexIn++;
            while (iIndexIn != charArray.length) {
                if (matches(charArray[iIndexIn])) {
                    break;
                }
                charArray[iIndexIn - i] = charArray[iIndexIn];
                iIndexIn++;
            }
            return new String(charArray, 0, iIndexIn - i);
            i++;
        }
    }

    public String retainFrom(CharSequence charSequence) {
        return negate().removeFrom(charSequence);
    }

    public String replaceFrom(CharSequence charSequence, char c) {
        String string = charSequence.toString();
        int iIndexIn = indexIn(string);
        if (iIndexIn == -1) {
            return string;
        }
        char[] charArray = string.toCharArray();
        charArray[iIndexIn] = c;
        while (true) {
            iIndexIn++;
            if (iIndexIn < charArray.length) {
                if (matches(charArray[iIndexIn])) {
                    charArray[iIndexIn] = c;
                }
            } else {
                return new String(charArray);
            }
        }
    }

    public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
        int length = charSequence2.length();
        if (length == 0) {
            return removeFrom(charSequence);
        }
        int i = 0;
        if (length == 1) {
            return replaceFrom(charSequence, charSequence2.charAt(0));
        }
        String string = charSequence.toString();
        int iIndexIn = indexIn(string);
        if (iIndexIn == -1) {
            return string;
        }
        int length2 = string.length();
        double d = length2;
        Double.isNaN(d);
        StringBuilder sb = new StringBuilder(((int) (d * 1.5d)) + 16);
        do {
            sb.append((CharSequence) string, i, iIndexIn);
            sb.append(charSequence2);
            i = iIndexIn + 1;
            iIndexIn = indexIn(string, i);
        } while (iIndexIn != -1);
        sb.append((CharSequence) string, i, length2);
        return sb.toString();
    }

    public String trimFrom(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && matches(charSequence.charAt(i))) {
            i++;
        }
        int i2 = length - 1;
        while (i2 > i && matches(charSequence.charAt(i2))) {
            i2--;
        }
        return charSequence.subSequence(i, i2 + 1).toString();
    }

    public String trimLeadingFrom(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && matches(charSequence.charAt(i))) {
            i++;
        }
        return charSequence.subSequence(i, length).toString();
    }

    public String trimTrailingFrom(CharSequence charSequence) {
        int length = charSequence.length() - 1;
        while (length >= 0 && matches(charSequence.charAt(length))) {
            length--;
        }
        return charSequence.subSequence(0, length + 1).toString();
    }

    public String collapseFrom(CharSequence charSequence, char c) {
        int iIndexIn = indexIn(charSequence);
        if (iIndexIn == -1) {
            return charSequence.toString();
        }
        StringBuilder sb = new StringBuilder(charSequence.length());
        sb.append(charSequence.subSequence(0, iIndexIn));
        sb.append(c);
        boolean z = true;
        for (int i = iIndexIn + 1; i < charSequence.length(); i++) {
            char cCharAt = charSequence.charAt(i);
            if (!apply(Character.valueOf(cCharAt))) {
                sb.append(cCharAt);
                z = false;
            } else if (!z) {
                sb.append(c);
                z = true;
            }
        }
        return sb.toString();
    }

    public String trimAndCollapseFrom(CharSequence charSequence, char c) {
        int iIndexIn = negate().indexIn(charSequence);
        if (iIndexIn == -1) {
            return "";
        }
        StringBuilder sb = new StringBuilder(charSequence.length());
        boolean z = false;
        while (iIndexIn < charSequence.length()) {
            char cCharAt = charSequence.charAt(iIndexIn);
            if (apply(Character.valueOf(cCharAt))) {
                z = true;
            } else {
                if (z) {
                    sb.append(c);
                    z = false;
                }
                sb.append(cCharAt);
            }
            iIndexIn++;
        }
        return sb.toString();
    }

    @Override // com.google.common.base.Predicate
    public boolean apply(Character ch) {
        return matches(ch.charValue());
    }
}
