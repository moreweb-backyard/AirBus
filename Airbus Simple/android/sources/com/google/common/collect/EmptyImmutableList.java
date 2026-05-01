package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class EmptyImmutableList extends ImmutableList<Object> {
    private static final long serialVersionUID = 0;
    static final EmptyImmutableList INSTANCE = new EmptyImmutableList();
    private static final Object[] EMPTY_ARRAY = new Object[0];

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        return false;
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        return 1;
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public int indexOf(Object obj) {
        return -1;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return true;
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public int lastIndexOf(Object obj) {
        return -1;
    }

    @Override // java.util.Collection, java.util.List
    public int size() {
        return 0;
    }

    @Override // com.google.common.collect.ImmutableCollection
    public String toString() {
        return "[]";
    }

    private EmptyImmutableList() {
    }

    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public UnmodifiableIterator<Object> iterator() {
        return Iterators.emptyIterator();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return EMPTY_ARRAY;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
    }

    @Override // java.util.List
    public Object get(int i) {
        Preconditions.checkElementIndex(i, 0);
        throw new AssertionError("unreachable");
    }

    @Override // com.google.common.collect.ImmutableList, java.util.List
    public ImmutableList<Object> subList(int i, int i2) {
        Preconditions.checkPositionIndexes(i, i2, 0);
        return this;
    }

    @Override // java.util.List
    public ListIterator<Object> listIterator() {
        return Collections.emptyList().listIterator();
    }

    @Override // java.util.List
    public ListIterator<Object> listIterator(int i) {
        Preconditions.checkPositionIndex(i, 0);
        return Collections.emptyList().listIterator();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof List) {
            return ((List) obj).isEmpty();
        }
        return false;
    }

    Object readResolve() {
        return INSTANCE;
    }
}
