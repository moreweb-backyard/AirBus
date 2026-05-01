package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import voice.encoder.BuildConfig;

@GwtCompatible(emulated = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class Platform {
    private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal<char[]>() { // from class: com.google.common.base.Platform.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public char[] initialValue() {
            return new char[1024];
        }
    };

    private Platform() {
    }

    static boolean isInstance(Class<?> cls, Object obj) {
        return cls.isInstance(obj);
    }

    static char[] charBufferFromThreadLocal() {
        return DEST_TL.get();
    }

    static CharMatcher precomputeCharMatcher(CharMatcher charMatcher) {
        return charMatcher.precomputedInternal();
    }
}
