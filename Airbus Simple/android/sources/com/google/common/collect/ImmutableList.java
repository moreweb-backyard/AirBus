package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {
    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList<E> asList() {
        return this;
    }

    public abstract int indexOf(@Nullable Object obj);

    @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public abstract UnmodifiableIterator<E> iterator();

    public abstract int lastIndexOf(@Nullable Object obj);

    @Override // java.util.List
    public abstract ImmutableList<E> subList(int i, int i2);

    public static <E> ImmutableList<E> of() {
        return EmptyImmutableList.INSTANCE;
    }

    public static <E> ImmutableList<E> of(E e) {
        return new SingletonImmutableList(e);
    }

    public static <E> ImmutableList<E> of(E e, E e2) {
        return new RegularImmutableList(copyIntoArray(e, e2));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4, e5));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4, e5, e6));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4, e5, e6, e7));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4, e5, e6, e7, e8));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4, e5, e6, e7, e8, e9));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4, e5, e6, e7, e8, e9, e10));
    }

    public static <E> ImmutableList<E> of(E e, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
        return new RegularImmutableList(copyIntoArray(e, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11));
    }

    public static <E> ImmutableList<E> of(E... eArr) {
        Preconditions.checkNotNull(eArr);
        switch (eArr.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(eArr[0]);
            default:
                return new RegularImmutableList(copyIntoArray(eArr));
        }
    }

    public static <E> ImmutableList<E> copyOf(Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        return iterable instanceof Collection ? copyOf((Collection) iterable) : copyOf(iterable.iterator());
    }

    public static <E> ImmutableList<E> copyOf(Collection<? extends E> collection) {
        Preconditions.checkNotNull(collection);
        if (collection instanceof ImmutableList) {
            return (ImmutableList) collection;
        }
        return copyFromCollection(collection);
    }

    public static <E> ImmutableList<E> copyOf(Iterator<? extends E> it) {
        return copyFromCollection(Lists.newArrayList(it));
    }

    private static <E> ImmutableList<E> copyFromCollection(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        switch (array.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableList(array[0]);
            default:
                return new RegularImmutableList(copyIntoArray(array));
        }
    }

    ImmutableList() {
    }

    @Override // java.util.List
    public final boolean addAll(int i, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public final E set(int i, E e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public final void add(int i, E e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    public final E remove(int i) {
        throw new UnsupportedOperationException();
    }

    private static Object[] copyIntoArray(Object... objArr) {
        Object[] objArr2 = new Object[objArr.length];
        int length = objArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            Object obj = objArr[i];
            if (obj == null) {
                throw new NullPointerException("at index " + i2);
            }
            objArr2[i2] = obj;
            i++;
            i2++;
        }
        return objArr2;
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] objArr) {
            this.elements = objArr;
        }

        Object readResolve() {
            return ImmutableList.of(this.elements);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static final class Builder<E> extends ImmutableCollection.Builder<E> {
        private final ArrayList<E> contents = Lists.newArrayList();

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
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                this.contents.ensureCapacity(this.contents.size() + ((Collection) iterable).size());
            }
            super.addAll((Iterable) iterable);
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
        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        @Override // com.google.common.collect.ImmutableCollection.Builder
        public ImmutableList<E> build() {
            return ImmutableList.copyOf((Collection) this.contents);
        }
    }
}
