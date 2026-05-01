package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class EmptyImmutableMultiset extends ImmutableMultiset<Object> {
    static final EmptyImmutableMultiset INSTANCE = new EmptyImmutableMultiset();
    private static final long serialVersionUID = 0;

    private EmptyImmutableMultiset() {
        super(ImmutableMap.of(), 0);
    }

    Object readResolve() {
        return INSTANCE;
    }
}
