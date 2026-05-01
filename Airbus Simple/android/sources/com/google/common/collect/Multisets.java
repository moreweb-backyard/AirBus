package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: classes.dex */
public final class Multisets {
    private Multisets() {
    }

    public static <E> Multiset<E> unmodifiableMultiset(Multiset<? extends E> multiset) {
        return new UnmodifiableMultiset(multiset);
    }

    private static class UnmodifiableMultiset<E> extends ForwardingMultiset<E> implements Serializable {
        private static final long serialVersionUID = 0;
        final Multiset<? extends E> delegate;
        transient Set<E> elementSet;
        transient Set<Multiset.Entry<E>> entrySet;

        UnmodifiableMultiset(Multiset<? extends E> multiset) {
            this.delegate = multiset;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Multiset<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public Set<E> elementSet() {
            Set<E> set = this.elementSet;
            if (set != null) {
                return set;
            }
            Set<E> setUnmodifiableSet = Collections.unmodifiableSet(this.delegate.elementSet());
            this.elementSet = setUnmodifiableSet;
            return setUnmodifiableSet;
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public Set<Multiset.Entry<E>> entrySet() {
            Set<Multiset.Entry<E>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            Set<Multiset.Entry<E>> setUnmodifiableSet = Collections.unmodifiableSet(this.delegate.entrySet());
            this.entrySet = setUnmodifiableSet;
            return setUnmodifiableSet;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return Iterators.unmodifiableIterator(this.delegate.iterator());
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public int add(E e, int i) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public int remove(Object obj, int i) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public int setCount(E e, int i) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingMultiset, com.google.common.collect.Multiset
        public boolean setCount(E e, int i, int i2) {
            throw new UnsupportedOperationException();
        }
    }

    public static <E> Multiset.Entry<E> immutableEntry(@Nullable final E e, final int i) {
        Preconditions.checkArgument(i >= 0);
        return new AbstractEntry<E>() { // from class: com.google.common.collect.Multisets.1
            @Override // com.google.common.collect.Multiset.Entry
            public E getElement() {
                return (E) e;
            }

            @Override // com.google.common.collect.Multiset.Entry
            public int getCount() {
                return i;
            }
        };
    }

    static <E> Multiset<E> forSet(Set<E> set) {
        return new SetMultiset(set);
    }

    private static class SetMultiset<E> extends ForwardingCollection<E> implements Multiset<E>, Serializable {
        private static final long serialVersionUID = 0;
        final Set<E> delegate;
        transient Set<E> elementSet;
        transient Set<Multiset.Entry<E>> entrySet;

        SetMultiset(Set<E> set) {
            this.delegate = (Set) Preconditions.checkNotNull(set);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.Multiset
        public int count(Object obj) {
            return this.delegate.contains(obj) ? 1 : 0;
        }

        @Override // com.google.common.collect.Multiset
        public int add(E e, int i) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multiset
        public int remove(Object obj, int i) {
            if (i == 0) {
                return count(obj);
            }
            Preconditions.checkArgument(i > 0);
            return this.delegate.remove(obj) ? 1 : 0;
        }

        @Override // com.google.common.collect.Multiset
        public Set<E> elementSet() {
            Set<E> set = this.elementSet;
            if (set != null) {
                return set;
            }
            ElementSet elementSet = new ElementSet();
            this.elementSet = elementSet;
            return elementSet;
        }

        @Override // com.google.common.collect.Multiset
        public Set<Multiset.Entry<E>> entrySet() {
            Set<Multiset.Entry<E>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            EntrySet entrySet = new EntrySet();
            this.entrySet = entrySet;
            return entrySet;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multiset
        public int setCount(E e, int i) {
            Multisets.checkNonnegative(i, "count");
            if (i == count(e)) {
                return i;
            }
            if (i == 0) {
                remove(e);
                return 1;
            }
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multiset
        public boolean setCount(E e, int i, int i2) {
            return Multisets.setCountImpl(this, e, i, i2);
        }

        @Override // java.util.Collection, com.google.common.collect.Multiset
        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof Multiset)) {
                return false;
            }
            Multiset multiset = (Multiset) obj;
            return size() == multiset.size() && this.delegate.equals(multiset.elementSet());
        }

        @Override // java.util.Collection, com.google.common.collect.Multiset
        public int hashCode() {
            Iterator<E> it = iterator();
            int iHashCode = 0;
            while (it.hasNext()) {
                E next = it.next();
                iHashCode += (next == null ? 0 : next.hashCode()) ^ 1;
            }
            return iHashCode;
        }

        class ElementSet extends ForwardingSet<E> {
            ElementSet() {
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
            public Set<E> delegate() {
                return SetMultiset.this.delegate;
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
            public boolean add(E e) {
                throw new UnsupportedOperationException();
            }

            @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
            public boolean addAll(Collection<? extends E> collection) {
                throw new UnsupportedOperationException();
            }
        }

        class EntrySet extends AbstractSet<Multiset.Entry<E>> {
            EntrySet() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return SetMultiset.this.delegate.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Multiset.Entry<E>> iterator() {
                return new Iterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.Multisets.SetMultiset.EntrySet.1
                    final Iterator<E> elements;

                    {
                        this.elements = SetMultiset.this.delegate.iterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.elements.hasNext();
                    }

                    @Override // java.util.Iterator
                    public Multiset.Entry<E> next() {
                        return Multisets.immutableEntry(this.elements.next(), 1);
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        this.elements.remove();
                    }
                };
            }
        }
    }

    static int inferDistinctElements(Iterable<?> iterable) {
        if (iterable instanceof Multiset) {
            return ((Multiset) iterable).elementSet().size();
        }
        return 11;
    }

    public static <E> Multiset<E> intersection(Multiset<E> multiset, Multiset<?> multiset2) {
        Preconditions.checkNotNull(multiset);
        Preconditions.checkNotNull(multiset2);
        return new AnonymousClass2(multiset, multiset2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* renamed from: com.google.common.collect.Multisets$2, reason: invalid class name */
    static class AnonymousClass2<E> extends AbstractMultiset<E> {
        final Set<Multiset.Entry<E>> entrySet = new AbstractSet<Multiset.Entry<E>>() { // from class: com.google.common.collect.Multisets.2.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Multiset.Entry<E>> iterator() {
                final Iterator<Multiset.Entry<E>> it = AnonymousClass2.this.val$multiset1.entrySet().iterator();
                return new AbstractIterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.Multisets.2.1.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // com.google.common.collect.AbstractIterator
                    public Multiset.Entry<E> computeNext() {
                        while (it.hasNext()) {
                            Multiset.Entry entry = (Multiset.Entry) it.next();
                            Object element = entry.getElement();
                            int iMin = Math.min(entry.getCount(), AnonymousClass2.this.val$multiset2.count(element));
                            if (iMin > 0) {
                                return Multisets.immutableEntry(element, iMin);
                            }
                        }
                        return endOfData();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return AnonymousClass2.this.elementSet().size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                Multiset.Entry entry;
                int count;
                return (obj instanceof Multiset.Entry) && (count = (entry = (Multiset.Entry) obj).getCount()) > 0 && AnonymousClass2.this.count(entry.getElement()) == count;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean isEmpty() {
                return AnonymousClass2.this.elementSet().isEmpty();
            }
        };
        final /* synthetic */ Multiset val$multiset1;
        final /* synthetic */ Multiset val$multiset2;

        AnonymousClass2(Multiset multiset, Multiset multiset2) {
            this.val$multiset1 = multiset;
            this.val$multiset2 = multiset2;
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int count(Object obj) {
            int iCount = this.val$multiset1.count(obj);
            if (iCount == 0) {
                return 0;
            }
            return Math.min(iCount, this.val$multiset2.count(obj));
        }

        @Override // com.google.common.collect.AbstractMultiset
        Set<E> createElementSet() {
            return Sets.intersection(this.val$multiset1.elementSet(), this.val$multiset2.elementSet());
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public Set<Multiset.Entry<E>> entrySet() {
            return this.entrySet;
        }
    }

    static abstract class AbstractEntry<E> implements Multiset.Entry<E> {
        AbstractEntry() {
        }

        @Override // com.google.common.collect.Multiset.Entry
        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof Multiset.Entry)) {
                return false;
            }
            Multiset.Entry entry = (Multiset.Entry) obj;
            return getCount() == entry.getCount() && Objects.equal(getElement(), entry.getElement());
        }

        @Override // com.google.common.collect.Multiset.Entry
        public int hashCode() {
            E element = getElement();
            return (element == null ? 0 : element.hashCode()) ^ getCount();
        }

        @Override // com.google.common.collect.Multiset.Entry
        public String toString() {
            String strValueOf = String.valueOf(getElement());
            int count = getCount();
            if (count == 1) {
                return strValueOf;
            }
            return strValueOf + " x " + count;
        }
    }

    static <E> int setCountImpl(Multiset<E> multiset, E e, int i) {
        checkNonnegative(i, "count");
        int iCount = multiset.count(e);
        int i2 = i - iCount;
        if (i2 > 0) {
            multiset.add(e, i2);
        } else if (i2 < 0) {
            multiset.remove(e, -i2);
        }
        return iCount;
    }

    static <E> boolean setCountImpl(Multiset<E> multiset, E e, int i, int i2) {
        checkNonnegative(i, "oldCount");
        checkNonnegative(i2, "newCount");
        if (multiset.count(e) != i) {
            return false;
        }
        multiset.setCount(e, i2);
        return true;
    }

    static void checkNonnegative(int i, String str) {
        Preconditions.checkArgument(i >= 0, "%s cannot be negative: %s", str, Integer.valueOf(i));
    }
}
