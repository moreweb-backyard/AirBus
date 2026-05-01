package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
    boolean isHashCodeFast() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public abstract UnmodifiableIterator<E> iterator();

    public static <E> ImmutableSet<E> of() {
        return EmptyImmutableSet.INSTANCE;
    }

    public static <E> ImmutableSet<E> of(E e) {
        return new SingletonImmutableSet(e);
    }

    public static <E> ImmutableSet<E> of(E e, E e2) {
        return create(e, e2);
    }

    public static <E> ImmutableSet<E> of(E e, E e2, E e3) {
        return create(e, e2, e3);
    }

    public static <E> ImmutableSet<E> of(E e, E e2, E e3, E e4) {
        return create(e, e2, e3, e4);
    }

    public static <E> ImmutableSet<E> of(E e, E e2, E e3, E e4, E e5) {
        return create(e, e2, e3, e4, e5);
    }

    public static <E> ImmutableSet<E> of(E... eArr) {
        Preconditions.checkNotNull(eArr);
        switch (eArr.length) {
            case 0:
                return of();
            case 1:
                return of((Object) eArr[0]);
            default:
                return create(eArr);
        }
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> iterable) {
        if ((iterable instanceof ImmutableSet) && !(iterable instanceof ImmutableSortedSet)) {
            return (ImmutableSet) iterable;
        }
        return copyOfInternal(Collections2.toCollection(iterable));
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> it) {
        return copyOfInternal(Lists.newArrayList(it));
    }

    private static <E> ImmutableSet<E> copyOfInternal(Collection<? extends E> collection) {
        switch (collection.size()) {
            case 0:
                return of();
            case 1:
                return of((Object) collection.iterator().next());
            default:
                return create(collection, collection.size());
        }
    }

    ImmutableSet() {
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj instanceof ImmutableSet) && isHashCodeFast() && ((ImmutableSet) obj).isHashCodeFast() && hashCode() != obj.hashCode()) {
            return false;
        }
        return Collections2.setEquals(this, obj);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        Iterator it = iterator();
        int iHashCode = 0;
        while (it.hasNext()) {
            iHashCode += it.next().hashCode();
        }
        return iHashCode;
    }

    private static <E> ImmutableSet<E> create(E... eArr) {
        return create(Arrays.asList(eArr), eArr.length);
    }

    private static <E> ImmutableSet<E> create(Iterable<? extends E> iterable, int i) {
        int iChooseTableSize = Hashing.chooseTableSize(i);
        Object[] objArr = new Object[iChooseTableSize];
        int i2 = iChooseTableSize - 1;
        ArrayList arrayList = new ArrayList(i);
        int i3 = 0;
        for (E e : iterable) {
            Preconditions.checkNotNull(e);
            int iHashCode = e.hashCode();
            int iSmear = Hashing.smear(iHashCode);
            while (true) {
                int i4 = iSmear & i2;
                Object obj = objArr[i4];
                if (obj == null) {
                    objArr[i4] = e;
                    arrayList.add(e);
                    i3 += iHashCode;
                    break;
                }
                if (obj.equals(e)) {
                    break;
                }
                iSmear++;
            }
        }
        if (arrayList.size() == 1) {
            return new SingletonImmutableSet(arrayList.get(0), i3);
        }
        if (iChooseTableSize > Hashing.chooseTableSize(arrayList.size())) {
            return create(arrayList, arrayList.size());
        }
        return new RegularImmutableSet(arrayList.toArray(), i3, objArr, i2);
    }

    static abstract class ArrayImmutableSet<E> extends ImmutableSet<E> {
        final transient Object[] elements;

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return false;
        }

        ArrayImmutableSet(Object[] objArr) {
            this.elements = objArr;
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            return this.elements.length;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<E> iterator() {
            return Iterators.forArray(this.elements);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            Object[] objArr = new Object[size()];
            Platform.unsafeArrayCopy(this.elements, 0, objArr, 0, size());
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
            Platform.unsafeArrayCopy(this.elements, 0, tArr, 0, size);
            return tArr;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean containsAll(Collection<?> collection) {
            if (collection == this) {
                return true;
            }
            if (!(collection instanceof ArrayImmutableSet)) {
                return super.containsAll(collection);
            }
            if (collection.size() > size()) {
                return false;
            }
            for (Object obj : ((ArrayImmutableSet) collection).elements) {
                if (!contains(obj)) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.collect.ImmutableCollection
        ImmutableList<E> createAsList() {
            return new ImmutableAsList(this.elements, this);
        }
    }

    static abstract class TransformedImmutableSet<D, E> extends ImmutableSet<E> {
        final int hashCode;
        final D[] source;

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return false;
        }

        @Override // com.google.common.collect.ImmutableSet
        boolean isHashCodeFast() {
            return true;
        }

        abstract E transform(D d);

        TransformedImmutableSet(D[] dArr, int i) {
            this.source = dArr;
            this.hashCode = i;
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            return this.source.length;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<E> iterator() {
            return new AbstractIterator<E>() { // from class: com.google.common.collect.ImmutableSet.TransformedImmutableSet.1
                int index = 0;

                @Override // com.google.common.collect.AbstractIterator
                protected E computeNext() {
                    if (this.index >= TransformedImmutableSet.this.source.length) {
                        return endOfData();
                    }
                    TransformedImmutableSet transformedImmutableSet = TransformedImmutableSet.this;
                    D[] dArr = TransformedImmutableSet.this.source;
                    int i = this.index;
                    this.index = i + 1;
                    return (E) transformedImmutableSet.transform(dArr[i]);
                }
            };
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return toArray(new Object[size()]);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public <T> T[] toArray(T[] tArr) {
            int size = size();
            if (tArr.length < size) {
                tArr = (T[]) ObjectArrays.newArray(tArr, size);
            } else if (tArr.length > size) {
                tArr[size] = null;
            }
            for (int i = 0; i < this.source.length; i++) {
                tArr[i] = transform(this.source[i]);
            }
            return tArr;
        }

        @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
        public final int hashCode() {
            return this.hashCode;
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] objArr) {
            this.elements = objArr;
        }

        Object readResolve() {
            return ImmutableSet.of(this.elements);
        }
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E> extends ImmutableCollection.Builder<E> {
        final ArrayList<E> contents = Lists.newArrayList();

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object obj) {
            return add((Builder<E>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E e) {
            this.contents.add(Preconditions.checkNotNull(e));
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... eArr) {
            Preconditions.checkNotNull(eArr);
            this.contents.ensureCapacity(this.contents.size() + eArr.length);
            super.add((Object[]) eArr);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                this.contents.ensureCapacity(this.contents.size() + ((Collection) iterable).size());
            }
            super.addAll((Iterable) iterable);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSet<E> build() {
            return ImmutableSet.copyOf((Iterable) this.contents);
        }
    }
}
