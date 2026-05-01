package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class CompoundOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final ImmutableList<Comparator<? super T>> comparators;

    CompoundOrdering(Comparator<? super T> comparator, Comparator<? super T> comparator2) {
        this.comparators = ImmutableList.of(comparator, comparator2);
    }

    CompoundOrdering(Iterable<? extends Comparator<? super T>> iterable) {
        this.comparators = ImmutableList.copyOf(iterable);
    }

    CompoundOrdering(List<? extends Comparator<? super T>> list, Comparator<? super T> comparator) {
        this.comparators = new ImmutableList.Builder().addAll((Iterable) list).add((ImmutableList.Builder) comparator).build();
    }

    @Override // java.util.Comparator
    public int compare(T t, T t2) {
        Iterator it = this.comparators.iterator();
        while (it.hasNext()) {
            int iCompare = ((Comparator) it.next()).compare(t, t2);
            if (iCompare != 0) {
                return iCompare;
            }
        }
        return 0;
    }

    @Override // java.util.Comparator
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CompoundOrdering) {
            return this.comparators.equals(((CompoundOrdering) obj).comparators);
        }
        return false;
    }

    public int hashCode() {
        return this.comparators.hashCode();
    }

    public String toString() {
        return "Ordering.compound(" + this.comparators + ")";
    }
}
