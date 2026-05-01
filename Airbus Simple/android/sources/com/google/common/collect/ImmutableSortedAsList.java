package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableAsList;

/* loaded from: classes.dex */
final class ImmutableSortedAsList<E> extends RegularImmutableList<E> {
    private final transient ImmutableSortedSet<E> set;

    ImmutableSortedAsList(Object[] objArr, int i, int i2, ImmutableSortedSet<E> immutableSortedSet) {
        super(objArr, i, i2);
        this.set = immutableSortedSet;
    }

    @Override // com.google.common.collect.RegularImmutableList, com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        return this.set.indexOf(obj) >= 0;
    }

    @Override // com.google.common.collect.RegularImmutableList, com.google.common.collect.ImmutableList, java.util.List
    public int indexOf(Object obj) {
        return this.set.indexOf(obj);
    }

    @Override // com.google.common.collect.RegularImmutableList, com.google.common.collect.ImmutableList, java.util.List
    public int lastIndexOf(Object obj) {
        return this.set.indexOf(obj);
    }

    @Override // com.google.common.collect.RegularImmutableList, com.google.common.collect.ImmutableList, java.util.List
    public ImmutableList<E> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, size());
        return i == i2 ? ImmutableList.of() : new RegularImmutableSortedSet(array(), this.set.comparator(), offset() + i, offset() + i2).asList();
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new ImmutableAsList.SerializedForm(this.set);
    }
}
