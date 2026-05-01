package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class NaturalOrdering extends Ordering<Comparable> implements Serializable {
    static final NaturalOrdering INSTANCE = new NaturalOrdering();
    private static final long serialVersionUID = 0;

    public String toString() {
        return "Ordering.natural()";
    }

    @Override // java.util.Comparator
    public int compare(Comparable comparable, Comparable comparable2) {
        Preconditions.checkNotNull(comparable2);
        if (comparable == comparable2) {
            return 0;
        }
        return comparable.compareTo(comparable2);
    }

    @Override // com.google.common.collect.Ordering
    public <S extends Comparable> Ordering<S> reverse() {
        return ReverseNaturalOrdering.INSTANCE;
    }

    @Override // com.google.common.collect.Ordering
    public int binarySearch(List<? extends Comparable> list, Comparable comparable) {
        return Collections.binarySearch(list, comparable);
    }

    @Override // com.google.common.collect.Ordering
    public <E extends Comparable> List<E> sortedCopy(Iterable<E> iterable) {
        ArrayList arrayListNewArrayList = Lists.newArrayList(iterable);
        Collections.sort(arrayListNewArrayList);
        return arrayListNewArrayList;
    }

    private Object readResolve() {
        return INSTANCE;
    }

    private NaturalOrdering() {
    }
}
