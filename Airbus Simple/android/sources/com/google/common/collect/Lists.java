package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible
/* loaded from: classes.dex */
public final class Lists {
    private Lists() {
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> ArrayList<E> newArrayList(E... eArr) {
        Preconditions.checkNotNull(eArr);
        ArrayList<E> arrayList = new ArrayList<>(computeArrayListCapacity(eArr.length));
        Collections.addAll(arrayList, eArr);
        return arrayList;
    }

    @VisibleForTesting
    static int computeArrayListCapacity(int i) {
        Preconditions.checkArgument(i >= 0);
        return (int) Math.min(i + 5 + (i / 10), 2147483647L);
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof Collection) {
            return new ArrayList<>((Collection) iterable);
        }
        return newArrayList(iterable.iterator());
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> it) {
        Preconditions.checkNotNull(it);
        ArrayList<E> arrayListNewArrayList = newArrayList();
        while (it.hasNext()) {
            arrayListNewArrayList.add(it.next());
        }
        return arrayListNewArrayList;
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> ArrayList<E> newArrayListWithCapacity(int i) {
        return new ArrayList<>(i);
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> ArrayList<E> newArrayListWithExpectedSize(int i) {
        return new ArrayList<>(computeArrayListCapacity(i));
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    @GwtCompatible(serializable = BuildConfig.DEBUG)
    public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> iterable) {
        LinkedList<E> linkedListNewLinkedList = newLinkedList();
        Iterator<? extends E> it = iterable.iterator();
        while (it.hasNext()) {
            linkedListNewLinkedList.add(it.next());
        }
        return linkedListNewLinkedList;
    }

    public static <E> List<E> asList(@Nullable E e, E[] eArr) {
        return new OnePlusArrayList(e, eArr);
    }

    private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID = 0;
        final E first;
        final E[] rest;

        OnePlusArrayList(@Nullable E e, E[] eArr) {
            this.first = e;
            this.rest = (E[]) ((Object[]) Preconditions.checkNotNull(eArr));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.rest.length + 1;
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i) {
            Preconditions.checkElementIndex(i, size());
            return i == 0 ? this.first : this.rest[i - 1];
        }
    }

    public static <E> List<E> asList(@Nullable E e, @Nullable E e2, E[] eArr) {
        return new TwoPlusArrayList(e, e2, eArr);
    }

    private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {
        private static final long serialVersionUID = 0;
        final E first;
        final E[] rest;
        final E second;

        TwoPlusArrayList(@Nullable E e, @Nullable E e2, E[] eArr) {
            this.first = e;
            this.second = e2;
            this.rest = (E[]) ((Object[]) Preconditions.checkNotNull(eArr));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.rest.length + 2;
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i) {
            switch (i) {
                case 0:
                    return this.first;
                case 1:
                    return this.second;
                default:
                    Preconditions.checkElementIndex(i, size());
                    return this.rest[i - 2];
            }
        }
    }

    public static <F, T> List<T> transform(List<F> list, Function<? super F, ? extends T> function) {
        return list instanceof RandomAccess ? new TransformingRandomAccessList(list, function) : new TransformingSequentialList(list, function);
    }

    private static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {
        private static final long serialVersionUID = 0;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingSequentialList(List<F> list, Function<? super F, ? extends T> function) {
            this.fromList = (List) Preconditions.checkNotNull(list);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.fromList.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fromList.size();
        }

        @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
        public ListIterator<T> listIterator(int i) {
            final ListIterator<F> listIterator = this.fromList.listIterator(i);
            return new ListIterator<T>() { // from class: com.google.common.collect.Lists.TransformingSequentialList.1
                @Override // java.util.ListIterator
                public void add(T t) {
                    throw new UnsupportedOperationException();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return listIterator.hasNext();
                }

                @Override // java.util.ListIterator
                public boolean hasPrevious() {
                    return listIterator.hasPrevious();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public T next() {
                    return TransformingSequentialList.this.function.apply((Object) listIterator.next());
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return listIterator.nextIndex();
                }

                @Override // java.util.ListIterator
                public T previous() {
                    return TransformingSequentialList.this.function.apply((Object) listIterator.previous());
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return listIterator.previousIndex();
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public void remove() {
                    listIterator.remove();
                }

                @Override // java.util.ListIterator
                public void set(T t) {
                    throw new UnsupportedOperationException("not supported");
                }
            };
        }
    }

    private static class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess, Serializable {
        private static final long serialVersionUID = 0;
        final List<F> fromList;
        final Function<? super F, ? extends T> function;

        TransformingRandomAccessList(List<F> list, Function<? super F, ? extends T> function) {
            this.fromList = (List) Preconditions.checkNotNull(list);
            this.function = (Function) Preconditions.checkNotNull(function);
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.fromList.clear();
        }

        @Override // java.util.AbstractList, java.util.List
        public T get(int i) {
            return this.function.apply(this.fromList.get(i));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return this.fromList.isEmpty();
        }

        @Override // java.util.AbstractList, java.util.List
        public T remove(int i) {
            return this.function.apply(this.fromList.remove(i));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.fromList.size();
        }
    }

    public static <T> List<List<T>> partition(List<T> list, int i) {
        Preconditions.checkNotNull(list);
        Preconditions.checkArgument(i > 0);
        return list instanceof RandomAccess ? new RandomAccessPartition(list, i) : new Partition(list, i);
    }

    private static class Partition<T> extends AbstractList<List<T>> {
        final List<T> list;
        final int size;

        Partition(List<T> list, int i) {
            this.list = list;
            this.size = i;
        }

        @Override // java.util.AbstractList, java.util.List
        public List<T> get(int i) {
            Preconditions.checkElementIndex(i, size());
            int i2 = i * this.size;
            return Platform.subList(this.list, i2, Math.min(this.size + i2, this.list.size()));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return ((this.list.size() + this.size) - 1) / this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return this.list.isEmpty();
        }
    }

    private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
        RandomAccessPartition(List<T> list, int i) {
            super(list, i);
        }
    }
}
