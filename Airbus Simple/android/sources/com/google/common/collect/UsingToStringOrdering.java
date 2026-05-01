package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class UsingToStringOrdering extends Ordering<Object> implements Serializable {
    static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();
    private static final long serialVersionUID = 0;

    public String toString() {
        return "Ordering.usingToString()";
    }

    @Override // java.util.Comparator
    public int compare(Object obj, Object obj2) {
        return obj.toString().compareTo(obj2.toString());
    }

    private Object readResolve() {
        return INSTANCE;
    }

    private UsingToStringOrdering() {
    }
}
