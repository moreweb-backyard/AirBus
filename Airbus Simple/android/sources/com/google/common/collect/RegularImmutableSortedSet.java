package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    private final Object[] elements;
    private final int fromIndex;
    private final int toIndex;

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean isEmpty() {
        return false;
    }

    RegularImmutableSortedSet(Object[] objArr, Comparator<? super E> comparator) {
        super(comparator);
        this.elements = objArr;
        this.fromIndex = 0;
        this.toIndex = objArr.length;
    }

    RegularImmutableSortedSet(Object[] objArr, Comparator<? super E> comparator, int i, int i2) {
        super(comparator);
        this.elements = objArr;
        this.fromIndex = i;
        this.toIndex = i2;
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public UnmodifiableIterator<E> iterator() {
        return Iterators.forArray(this.elements, this.fromIndex, size());
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        return this.toIndex - this.fromIndex;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return binarySearch(obj) >= 0;
        } catch (ClassCastException unused) {
            return false;
        }
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public boolean containsAll(Collection<?> collection) {
        if (!hasSameComparator(collection, comparator()) || collection.size() <= 1) {
            return super.containsAll(collection);
        }
        int i = this.fromIndex;
        Iterator<?> it = collection.iterator();
        Object next = it.next();
        while (i < this.toIndex) {
            int iUnsafeCompare = unsafeCompare(this.elements[i], next);
            if (iUnsafeCompare < 0) {
                i++;
            } else if (iUnsafeCompare == 0) {
                if (!it.hasNext()) {
                    return true;
                }
                next = it.next();
                i++;
            } else if (iUnsafeCompare > 0) {
                return false;
            }
        }
        return false;
    }

    private int binarySearch(Object obj) {
        int i = this.fromIndex;
        int i2 = this.toIndex - 1;
        while (i <= i2) {
            int i3 = ((i2 - i) / 2) + i;
            int iUnsafeCompare = unsafeCompare(obj, this.elements[i3]);
            if (iUnsafeCompare < 0) {
                i2 = i3 - 1;
            } else {
                if (iUnsafeCompare <= 0) {
                    return i3;
                }
                i = i3 + 1;
            }
        }
        return (-i) - 1;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] objArr = new Object[size()];
        Platform.unsafeArrayCopy(this.elements, this.fromIndex, objArr, 0, size());
        return objArr;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
    public <T> T[] toArray(T[] tArr) {
        int size = size();
        if (tArr.length < size) {
            tArr = (T[]) ObjectArrays.newArray(tArr, size);
        } else if (tArr.length > size) {
            tArr[size] = null;
        }
        Platform.unsafeArrayCopy(this.elements, this.fromIndex, tArr, 0, size);
        return tArr;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set = (Set) obj;
        if (size() != set.size()) {
            return false;
        }
        if (hasSameComparator(set, this.comparator)) {
            Iterator<E> it = set.iterator();
            try {
                for (int i = this.fromIndex; i < this.toIndex; i++) {
                    E next = it.next();
                    if (next == null || unsafeCompare(this.elements[i], next) != 0) {
                        return false;
                    }
                }
                return true;
            } catch (ClassCastException unused) {
                return false;
            } catch (NoSuchElementException unused2) {
                return false;
            }
        }
        return containsAll(set);
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        int iHashCode = 0;
        for (int i = this.fromIndex; i < this.toIndex; i++) {
            iHashCode += this.elements[i].hashCode();
        }
        return iHashCode;
    }

    @Override // java.util.SortedSet
    public E first() {
        return (E) this.elements[this.fromIndex];
    }

    @Override // java.util.SortedSet
    public E last() {
        return (E) this.elements[this.toIndex - 1];
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> headSetImpl(E e) {
        return createSubset(this.fromIndex, findSubsetIndex(e));
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> subSetImpl(E e, E e2) {
        return createSubset(findSubsetIndex(e), findSubsetIndex(e2));
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> tailSetImpl(E e) {
        return createSubset(findSubsetIndex(e), this.toIndex);
    }

    private int findSubsetIndex(E e) {
        int iBinarySearch = binarySearch(e);
        return iBinarySearch >= 0 ? iBinarySearch : (-iBinarySearch) - 1;
    }

    private ImmutableSortedSet<E> createSubset(int i, int i2) {
        if (i < i2) {
            return new RegularImmutableSortedSet(this.elements, this.comparator, i, i2);
        }
        return emptySet(this.comparator);
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    boolean hasPartialArray() {
        return (this.fromIndex == 0 && this.toIndex == this.elements.length) ? false : true;
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        try {
            int iBinarySearch = binarySearch(obj);
            if (iBinarySearch < 0 || !this.elements[iBinarySearch].equals(obj)) {
                return -1;
            }
            return iBinarySearch - this.fromIndex;
        } catch (ClassCastException unused) {
            return -1;
        }
    }

    @Override // com.google.common.collect.ImmutableCollection
    ImmutableList<E> createAsList() {
        return new ImmutableSortedAsList(this.elements, this.fromIndex, size(), this);
    }
}
