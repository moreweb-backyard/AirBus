package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements SortedSet<E> {
    final transient Comparator<? super E> comparator;
    private static final Comparator NATURAL_ORDER = Ordering.natural();
    private static final ImmutableSortedSet<Object> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet(NATURAL_ORDER);

    abstract boolean hasPartialArray();

    abstract ImmutableSortedSet<E> headSetImpl(E e);

    abstract int indexOf(Object obj);

    abstract ImmutableSortedSet<E> subSetImpl(E e, E e2);

    abstract ImmutableSortedSet<E> tailSetImpl(E e);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
        return headSet((ImmutableSortedSet<E>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedSet
    public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
        return tailSet((ImmutableSortedSet<E>) obj);
    }

    private static <E> ImmutableSortedSet<E> emptySet() {
        return (ImmutableSortedSet<E>) NATURAL_EMPTY_SET;
    }

    static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
        if (NATURAL_ORDER.equals(comparator)) {
            return emptySet();
        }
        return new EmptyImmutableSortedSet(comparator);
    }

    public static <E> ImmutableSortedSet<E> of() {
        return emptySet();
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    public static ImmutableSortedSet of(Comparable comparable) {
        return new RegularImmutableSortedSet(new Object[]{Preconditions.checkNotNull(comparable)}, Ordering.natural());
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2) {
        return ofInternal(Ordering.natural(), comparable, comparable2);
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2, Comparable comparable3) {
        return ofInternal(Ordering.natural(), comparable, comparable2, comparable3);
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4) {
        return ofInternal(Ordering.natural(), comparable, comparable2, comparable3, comparable4);
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>(TE;TE;TE;TE;TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    public static ImmutableSortedSet of(Comparable comparable, Comparable comparable2, Comparable comparable3, Comparable comparable4, Comparable comparable5) {
        return ofInternal(Ordering.natural(), comparable, comparable2, comparable3, comparable4, comparable5);
    }

    /* JADX WARN: Incorrect types in method signature: <E::Ljava/lang/Comparable<-TE;>;>([TE;)Lcom/google/common/collect/ImmutableSortedSet<TE;>; */
    public static ImmutableSortedSet of(Comparable... comparableArr) {
        return ofInternal(Ordering.natural(), comparableArr);
    }

    private static <E> ImmutableSortedSet<E> ofInternal(Comparator<? super E> comparator, E... eArr) {
        Preconditions.checkNotNull(eArr);
        if (eArr.length == 0) {
            return emptySet(comparator);
        }
        Object[] objArr = new Object[eArr.length];
        for (int i = 0; i < eArr.length; i++) {
            objArr[i] = Preconditions.checkNotNull(eArr[i]);
        }
        sort(objArr, comparator);
        return new RegularImmutableSortedSet(removeDupes(objArr, comparator), comparator);
    }

    private static <E> void sort(Object[] objArr, Comparator<? super E> comparator) {
        Arrays.sort(objArr, comparator);
    }

    private static <E> Object[] removeDupes(Object[] objArr, Comparator<? super E> comparator) {
        int i = 1;
        for (int i2 = 1; i2 < objArr.length; i2++) {
            Object obj = objArr[i2];
            if (unsafeCompare(comparator, objArr[i - 1], obj) != 0) {
                objArr[i] = obj;
                i++;
            }
        }
        if (i == objArr.length) {
            return objArr;
        }
        Object[] objArr2 = new Object[i];
        Platform.unsafeArrayCopy(objArr, 0, objArr2, 0, i);
        return objArr2;
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> iterable) {
        return copyOfInternal(Ordering.natural(), iterable, false);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> it) {
        return copyOfInternal(Ordering.natural(), it);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(comparator);
        return copyOfInternal(comparator, iterable, false);
    }

    public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> it) {
        Preconditions.checkNotNull(comparator);
        return copyOfInternal(comparator, it);
    }

    public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
        Comparator<? super E> comparator = sortedSet.comparator();
        if (comparator == null) {
            comparator = NATURAL_ORDER;
        }
        return copyOfInternal(comparator, sortedSet, true);
    }

    private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> comparator, Iterable<? extends E> iterable, boolean z) {
        boolean z2 = z || hasSameComparator(iterable, comparator);
        if (z2 && (iterable instanceof ImmutableSortedSet)) {
            ImmutableSortedSet<E> immutableSortedSet = (ImmutableSortedSet) iterable;
            if (!immutableSortedSet.hasPartialArray()) {
                return immutableSortedSet;
            }
        }
        Object[] objArrNewObjectArray = newObjectArray(iterable);
        if (objArrNewObjectArray.length == 0) {
            return emptySet(comparator);
        }
        for (Object obj : objArrNewObjectArray) {
            Preconditions.checkNotNull(obj);
        }
        if (!z2) {
            sort(objArrNewObjectArray, comparator);
            objArrNewObjectArray = removeDupes(objArrNewObjectArray, comparator);
        }
        return new RegularImmutableSortedSet(objArrNewObjectArray, comparator);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> Object[] newObjectArray(Iterable<T> iterable) {
        Collection collectionNewArrayList = iterable instanceof Collection ? (Collection) iterable : Lists.newArrayList(iterable);
        return collectionNewArrayList.toArray(new Object[collectionNewArrayList.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> comparator, Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return emptySet(comparator);
        }
        ArrayList arrayListNewArrayList = Lists.newArrayList();
        while (it.hasNext()) {
            arrayListNewArrayList.add(Preconditions.checkNotNull(it.next()));
        }
        Object[] array = arrayListNewArrayList.toArray();
        sort(array, comparator);
        return new RegularImmutableSortedSet(removeDupes(array, comparator), comparator);
    }

    static boolean hasSameComparator(Iterable<?> iterable, Comparator<?> comparator) {
        if (!(iterable instanceof SortedSet)) {
            return false;
        }
        Comparator<? super E> comparator2 = ((SortedSet) iterable).comparator();
        return comparator2 == null ? comparator == Ordering.natural() : comparator.equals(comparator2);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder<>(comparator);
    }

    public static <E extends Comparable<E>> Builder<E> reverseOrder() {
        return new Builder<>(Ordering.natural().reverse());
    }

    public static <E extends Comparable<E>> Builder<E> naturalOrder() {
        return new Builder<>(Ordering.natural());
    }

    public static final class Builder<E> extends ImmutableSet.Builder<E> {
        private final Comparator<? super E> comparator;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableCollection.Builder add(Object obj) {
            return add((Builder<E>) obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public /* bridge */ /* synthetic */ ImmutableSet.Builder add(Object obj) {
            return add((Builder<E>) obj);
        }

        public Builder(Comparator<? super E> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E e) {
            super.add((Builder<E>) e);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> add(E... eArr) {
            super.add((Object[]) eArr);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            super.addAll((Iterable) iterable);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.Builder
        public ImmutableSortedSet<E> build() {
            return ImmutableSortedSet.copyOfInternal(this.comparator, this.contents.iterator());
        }
    }

    int unsafeCompare(Object obj, Object obj2) {
        return unsafeCompare(this.comparator, obj, obj2);
    }

    static int unsafeCompare(Comparator<?> comparator, Object obj, Object obj2) {
        return comparator.compare(obj, obj2);
    }

    ImmutableSortedSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    @Override // java.util.SortedSet
    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedSet
    public ImmutableSortedSet<E> headSet(E e) {
        return headSetImpl(Preconditions.checkNotNull(e));
    }

    @Override // java.util.SortedSet
    public ImmutableSortedSet<E> subSet(E e, E e2) {
        Preconditions.checkNotNull(e);
        Preconditions.checkNotNull(e2);
        Preconditions.checkArgument(this.comparator.compare(e, e2) <= 0);
        return subSetImpl(e, e2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedSet
    public ImmutableSortedSet<E> tailSet(E e) {
        return tailSetImpl(Preconditions.checkNotNull(e));
    }

    private static class SerializedForm<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Comparator<? super E> comparator;
        final Object[] elements;

        public SerializedForm(Comparator<? super E> comparator, Object[] objArr) {
            this.comparator = comparator;
            this.elements = objArr;
        }

        /* JADX WARN: Multi-variable type inference failed */
        Object readResolve() {
            return new Builder(this.comparator).add(this.elements).build();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }
}
