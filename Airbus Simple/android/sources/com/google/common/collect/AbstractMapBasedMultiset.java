package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: classes.dex */
abstract class AbstractMapBasedMultiset<E> extends AbstractMultiset<E> implements Serializable {
    private static final long serialVersionUID = -2250766705698539974L;
    private transient Map<E, AtomicInteger> backingMap;
    private transient AbstractMapBasedMultiset<E>.EntrySet entrySet;
    private transient long size = super.size();

    static /* synthetic */ long access$210(AbstractMapBasedMultiset abstractMapBasedMultiset) {
        long j = abstractMapBasedMultiset.size;
        abstractMapBasedMultiset.size = j - 1;
        return j;
    }

    static /* synthetic */ long access$222(AbstractMapBasedMultiset abstractMapBasedMultiset, long j) {
        long j2 = abstractMapBasedMultiset.size - j;
        abstractMapBasedMultiset.size = j2;
        return j2;
    }

    protected AbstractMapBasedMultiset(Map<E, AtomicInteger> map) {
        this.backingMap = (Map) Preconditions.checkNotNull(map);
    }

    Map<E, AtomicInteger> backingMap() {
        return this.backingMap;
    }

    void setBackingMap(Map<E, AtomicInteger> map) {
        this.backingMap = map;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public Set<Multiset.Entry<E>> entrySet() {
        AbstractMapBasedMultiset<E>.EntrySet entrySet = this.entrySet;
        if (entrySet != null) {
            return entrySet;
        }
        AbstractMapBasedMultiset<E>.EntrySet entrySet2 = new EntrySet();
        this.entrySet = entrySet2;
        return entrySet2;
    }

    private class EntrySet extends AbstractSet<Multiset.Entry<E>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Multiset.Entry<E>> iterator() {
            final Iterator<E> it = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
            return new Iterator<Multiset.Entry<E>>() { // from class: com.google.common.collect.AbstractMapBasedMultiset.EntrySet.1
                Map.Entry<E, AtomicInteger> toRemove;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override // java.util.Iterator
                public Multiset.Entry<E> next() {
                    final Map.Entry<E, AtomicInteger> entry = (Map.Entry) it.next();
                    this.toRemove = entry;
                    return new Multisets.AbstractEntry<E>() { // from class: com.google.common.collect.AbstractMapBasedMultiset.EntrySet.1.1
                        @Override // com.google.common.collect.Multiset.Entry
                        public E getElement() {
                            return (E) entry.getKey();
                        }

                        @Override // com.google.common.collect.Multiset.Entry
                        public int getCount() {
                            AtomicInteger atomicInteger;
                            int i = ((AtomicInteger) entry.getValue()).get();
                            return (i != 0 || (atomicInteger = (AtomicInteger) AbstractMapBasedMultiset.this.backingMap.get(getElement())) == null) ? i : atomicInteger.get();
                        }
                    };
                }

                @Override // java.util.Iterator
                public void remove() {
                    Preconditions.checkState(this.toRemove != null, "no calls to next() since the last call to remove()");
                    AbstractMapBasedMultiset.access$222(AbstractMapBasedMultiset.this, this.toRemove.getValue().getAndSet(0));
                    it.remove();
                    this.toRemove = null;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractMapBasedMultiset.this.backingMap.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Iterator<E> it = AbstractMapBasedMultiset.this.backingMap.values().iterator();
            while (it.hasNext()) {
                ((AtomicInteger) it.next()).set(0);
            }
            AbstractMapBasedMultiset.this.backingMap.clear();
            AbstractMapBasedMultiset.this.size = 0L;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof Multiset.Entry)) {
                return false;
            }
            Multiset.Entry entry = (Multiset.Entry) obj;
            int iCount = AbstractMapBasedMultiset.this.count(entry.getElement());
            return iCount == entry.getCount() && iCount > 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!contains(obj)) {
                return false;
            }
            AbstractMapBasedMultiset.access$222(AbstractMapBasedMultiset.this, ((AtomicInteger) AbstractMapBasedMultiset.this.backingMap.remove(((Multiset.Entry) obj).getElement())).getAndSet(0));
            return true;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
    public int size() {
        return (int) Math.min(this.size, 2147483647L);
    }

    @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
    public Iterator<E> iterator() {
        return new MapBasedMultisetIterator();
    }

    private class MapBasedMultisetIterator implements Iterator<E> {
        boolean canRemove;
        Map.Entry<E, AtomicInteger> currentEntry;
        final Iterator<Map.Entry<E, AtomicInteger>> entryIterator;
        int occurrencesLeft;

        MapBasedMultisetIterator() {
            this.entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.occurrencesLeft > 0 || this.entryIterator.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.occurrencesLeft == 0) {
                this.currentEntry = this.entryIterator.next();
                this.occurrencesLeft = this.currentEntry.getValue().get();
            }
            this.occurrencesLeft--;
            this.canRemove = true;
            return this.currentEntry.getKey();
        }

        @Override // java.util.Iterator
        public void remove() {
            Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");
            if (this.currentEntry.getValue().get() <= 0) {
                throw new ConcurrentModificationException();
            }
            if (this.currentEntry.getValue().addAndGet(-1) == 0) {
                this.entryIterator.remove();
            }
            AbstractMapBasedMultiset.access$210(AbstractMapBasedMultiset.this);
            this.canRemove = false;
        }
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int count(@Nullable Object obj) {
        AtomicInteger atomicInteger = this.backingMap.get(obj);
        if (atomicInteger == null) {
            return 0;
        }
        return atomicInteger.get();
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int add(@Nullable E e, int i) {
        int i2;
        if (i == 0) {
            return count(e);
        }
        Preconditions.checkArgument(i > 0, "occurrences cannot be negative: %s", Integer.valueOf(i));
        AtomicInteger atomicInteger = this.backingMap.get(e);
        if (atomicInteger == null) {
            this.backingMap.put(e, new AtomicInteger(i));
            i2 = 0;
        } else {
            i2 = atomicInteger.get();
            long j = i2 + i;
            Preconditions.checkArgument(j <= 2147483647L, "too many occurrences: %s", Long.valueOf(j));
            atomicInteger.getAndAdd(i);
        }
        this.size += i;
        return i2;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int remove(@Nullable Object obj, int i) {
        if (i == 0) {
            return count(obj);
        }
        Preconditions.checkArgument(i > 0, "occurrences cannot be negative: %s", Integer.valueOf(i));
        AtomicInteger atomicInteger = this.backingMap.get(obj);
        if (atomicInteger == null) {
            return 0;
        }
        int i2 = atomicInteger.get();
        if (i2 <= i) {
            this.backingMap.remove(obj);
            i = i2;
        }
        atomicInteger.addAndGet(-i);
        this.size -= i;
        return i2;
    }

    @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
    public int setCount(E e, int i) {
        int andSet;
        Multisets.checkNonnegative(i, "count");
        if (i == 0) {
            andSet = getAndSet(this.backingMap.remove(e), i);
        } else {
            AtomicInteger atomicInteger = this.backingMap.get(e);
            int andSet2 = getAndSet(atomicInteger, i);
            if (atomicInteger == null) {
                this.backingMap.put(e, new AtomicInteger(i));
            }
            andSet = andSet2;
        }
        this.size += i - andSet;
        return andSet;
    }

    private static int getAndSet(AtomicInteger atomicInteger, int i) {
        if (atomicInteger == null) {
            return 0;
        }
        return atomicInteger.getAndSet(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int removeAllOccurrences(@Nullable Object obj, Map<E, AtomicInteger> map) {
        AtomicInteger atomicIntegerRemove = map.remove(obj);
        if (atomicIntegerRemove == null) {
            return 0;
        }
        int andSet = atomicIntegerRemove.getAndSet(0);
        this.size -= andSet;
        return andSet;
    }

    @Override // com.google.common.collect.AbstractMultiset
    Set<E> createElementSet() {
        return new MapBasedElementSet(this.backingMap);
    }

    class MapBasedElementSet extends ForwardingSet<E> {
        private final Set<E> delegate;
        private final Map<E, AtomicInteger> map;

        MapBasedElementSet(Map<E, AtomicInteger> map) {
            this.map = map;
            this.delegate = map.keySet();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<E> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            final Iterator<Map.Entry<E, AtomicInteger>> it = this.map.entrySet().iterator();
            return new Iterator<E>() { // from class: com.google.common.collect.AbstractMapBasedMultiset.MapBasedElementSet.1
                Map.Entry<E, AtomicInteger> toRemove;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override // java.util.Iterator
                public E next() {
                    this.toRemove = (Map.Entry) it.next();
                    return this.toRemove.getKey();
                }

                @Override // java.util.Iterator
                public void remove() {
                    Preconditions.checkState(this.toRemove != null, "no calls to next() since the last call to remove()");
                    AbstractMapBasedMultiset.access$222(AbstractMapBasedMultiset.this, this.toRemove.getValue().getAndSet(0));
                    it.remove();
                    this.toRemove = null;
                }
            };
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return AbstractMapBasedMultiset.this.removeAllOccurrences(obj, this.map) != 0;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            return Iterators.removeAll(iterator(), collection);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            return Iterators.retainAll(iterator(), collection);
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            if (this.map == AbstractMapBasedMultiset.this.backingMap) {
                AbstractMapBasedMultiset.this.clear();
                return;
            }
            Iterator<E> it = iterator();
            while (it.hasNext()) {
                it.next();
                it.remove();
            }
        }

        public Map<E, AtomicInteger> getMap() {
            return this.map;
        }
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }
}
