package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: classes.dex */
abstract class AbstractMultimap<K, V> implements Multimap<K, V>, Serializable {
    private static final long serialVersionUID = 2447537837011683357L;
    private transient Map<K, Collection<V>> asMap;
    private transient Collection<Map.Entry<K, V>> entries;
    private transient Set<K> keySet;
    private transient Map<K, Collection<V>> map;
    private transient Multiset<K> multiset;
    private transient int totalSize;
    private transient Collection<V> valuesCollection;

    abstract Collection<V> createCollection();

    static /* synthetic */ int access$208(AbstractMultimap abstractMultimap) {
        int i = abstractMultimap.totalSize;
        abstractMultimap.totalSize = i + 1;
        return i;
    }

    static /* synthetic */ int access$210(AbstractMultimap abstractMultimap) {
        int i = abstractMultimap.totalSize;
        abstractMultimap.totalSize = i - 1;
        return i;
    }

    static /* synthetic */ int access$212(AbstractMultimap abstractMultimap, int i) {
        int i2 = abstractMultimap.totalSize + i;
        abstractMultimap.totalSize = i2;
        return i2;
    }

    static /* synthetic */ int access$220(AbstractMultimap abstractMultimap, int i) {
        int i2 = abstractMultimap.totalSize - i;
        abstractMultimap.totalSize = i2;
        return i2;
    }

    protected AbstractMultimap(Map<K, Collection<V>> map) {
        Preconditions.checkArgument(map.isEmpty());
        this.map = map;
    }

    final void setMap(Map<K, Collection<V>> map) {
        this.map = map;
        this.totalSize = 0;
        for (Collection<V> collection : map.values()) {
            Preconditions.checkArgument(!collection.isEmpty());
            this.totalSize += collection.size();
        }
    }

    Collection<V> createCollection(@Nullable K k) {
        return createCollection();
    }

    Map<K, Collection<V>> backingMap() {
        return this.map;
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return this.totalSize;
    }

    @Override // com.google.common.collect.Multimap
    public boolean isEmpty() {
        return this.totalSize == 0;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsValue(@Nullable Object obj) {
        Iterator<Collection<V>> it = this.map.values().iterator();
        while (it.hasNext()) {
            if (it.next().contains(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsEntry(@Nullable Object obj, @Nullable Object obj2) {
        Collection<V> collection = this.map.get(obj);
        return collection != null && collection.contains(obj2);
    }

    @Override // com.google.common.collect.Multimap
    public boolean put(@Nullable K k, @Nullable V v) {
        if (!getOrCreateCollection(k).add(v)) {
            return false;
        }
        this.totalSize++;
        return true;
    }

    private Collection<V> getOrCreateCollection(@Nullable K k) {
        Collection<V> collection = this.map.get(k);
        if (collection != null) {
            return collection;
        }
        Collection<V> collectionCreateCollection = createCollection(k);
        this.map.put(k, collectionCreateCollection);
        return collectionCreateCollection;
    }

    @Override // com.google.common.collect.Multimap
    public boolean remove(@Nullable Object obj, @Nullable Object obj2) {
        Collection<V> collection = this.map.get(obj);
        if (collection == null) {
            return false;
        }
        boolean zRemove = collection.remove(obj2);
        if (zRemove) {
            this.totalSize--;
            if (collection.isEmpty()) {
                this.map.remove(obj);
            }
        }
        return zRemove;
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(@Nullable K k, Iterable<? extends V> iterable) {
        boolean zAddAll;
        boolean zAdd = false;
        if (!iterable.iterator().hasNext()) {
            return false;
        }
        Collection<V> orCreateCollection = getOrCreateCollection(k);
        int size = orCreateCollection.size();
        if (iterable instanceof Collection) {
            zAddAll = orCreateCollection.addAll((Collection) iterable);
        } else {
            Iterator<? extends V> it = iterable.iterator();
            while (it.hasNext()) {
                zAdd |= orCreateCollection.add(it.next());
            }
            zAddAll = zAdd;
        }
        this.totalSize += orCreateCollection.size() - size;
        return zAddAll;
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean zPut = false;
        for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
            zPut |= put(entry.getKey(), entry.getValue());
        }
        return zPut;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        Iterator<? extends V> it = iterable.iterator();
        if (!it.hasNext()) {
            return removeAll(k);
        }
        Collection<V> orCreateCollection = getOrCreateCollection(k);
        Collection<V> collectionCreateCollection = createCollection();
        collectionCreateCollection.addAll(orCreateCollection);
        this.totalSize -= orCreateCollection.size();
        orCreateCollection.clear();
        while (it.hasNext()) {
            if (orCreateCollection.add(it.next())) {
                this.totalSize++;
            }
        }
        return unmodifiableCollectionSubclass(collectionCreateCollection);
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> removeAll(@Nullable Object obj) {
        Collection<V> collectionRemove = this.map.remove(obj);
        Collection<V> collectionCreateCollection = createCollection();
        if (collectionRemove != null) {
            collectionCreateCollection.addAll(collectionRemove);
            this.totalSize -= collectionRemove.size();
            collectionRemove.clear();
        }
        return unmodifiableCollectionSubclass(collectionCreateCollection);
    }

    private Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return Collections.unmodifiableSortedSet((SortedSet) collection);
        }
        if (collection instanceof Set) {
            return Collections.unmodifiableSet((Set) collection);
        }
        if (collection instanceof List) {
            return Collections.unmodifiableList((List) collection);
        }
        return Collections.unmodifiableCollection(collection);
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        Iterator<Collection<V>> it = this.map.values().iterator();
        while (it.hasNext()) {
            it.next().clear();
        }
        this.map.clear();
        this.totalSize = 0;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> get(@Nullable K k) {
        Collection<V> collectionCreateCollection = this.map.get(k);
        if (collectionCreateCollection == null) {
            collectionCreateCollection = createCollection(k);
        }
        return wrapCollection(k, collectionCreateCollection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Collection<V> wrapCollection(@Nullable K k, Collection<V> collection) {
        if (collection instanceof SortedSet) {
            return new WrappedSortedSet(k, (SortedSet) collection, null);
        }
        if (collection instanceof Set) {
            return new WrappedSet(k, (Set) collection);
        }
        if (collection instanceof List) {
            return wrapList(k, (List) collection, null);
        }
        return new WrappedCollection(k, collection, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<V> wrapList(K k, List<V> list, @Nullable AbstractMultimap<K, V>.WrappedCollection wrappedCollection) {
        return list instanceof RandomAccess ? new RandomAccessWrappedList(k, list, wrappedCollection) : new WrappedList(k, list, wrappedCollection);
    }

    private class WrappedCollection extends AbstractCollection<V> {
        final AbstractMultimap<K, V>.WrappedCollection ancestor;
        final Collection<V> ancestorDelegate;
        Collection<V> delegate;
        final K key;

        WrappedCollection(@Nullable K k, Collection<V> collection, @Nullable AbstractMultimap<K, V>.WrappedCollection wrappedCollection) {
            this.key = k;
            this.delegate = collection;
            this.ancestor = wrappedCollection;
            this.ancestorDelegate = wrappedCollection == null ? null : wrappedCollection.getDelegate();
        }

        void refreshIfEmpty() {
            Collection<V> collection;
            if (this.ancestor != null) {
                this.ancestor.refreshIfEmpty();
                if (this.ancestor.getDelegate() != this.ancestorDelegate) {
                    throw new ConcurrentModificationException();
                }
            } else {
                if (!this.delegate.isEmpty() || (collection = (Collection) AbstractMultimap.this.map.get(this.key)) == null) {
                    return;
                }
                this.delegate = collection;
            }
        }

        void removeIfEmpty() {
            if (this.ancestor != null) {
                this.ancestor.removeIfEmpty();
            } else if (this.delegate.isEmpty()) {
                AbstractMultimap.this.map.remove(this.key);
            }
        }

        K getKey() {
            return this.key;
        }

        void addToMap() {
            if (this.ancestor == null) {
                AbstractMultimap.this.map.put(this.key, this.delegate);
            } else {
                this.ancestor.addToMap();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            refreshIfEmpty();
            return this.delegate.size();
        }

        @Override // java.util.Collection
        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            refreshIfEmpty();
            return this.delegate.equals(obj);
        }

        @Override // java.util.Collection
        public int hashCode() {
            refreshIfEmpty();
            return this.delegate.hashCode();
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            refreshIfEmpty();
            return this.delegate.toString();
        }

        Collection<V> getDelegate() {
            return this.delegate;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            refreshIfEmpty();
            return new WrappedIterator();
        }

        class WrappedIterator implements Iterator<V> {
            final Iterator<V> delegateIterator;
            final Collection<V> originalDelegate;

            WrappedIterator() {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = AbstractMultimap.this.iteratorOrListIterator(WrappedCollection.this.delegate);
            }

            WrappedIterator(Iterator<V> it) {
                this.originalDelegate = WrappedCollection.this.delegate;
                this.delegateIterator = it;
            }

            void validateIterator() {
                WrappedCollection.this.refreshIfEmpty();
                if (WrappedCollection.this.delegate != this.originalDelegate) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                validateIterator();
                return this.delegateIterator.hasNext();
            }

            @Override // java.util.Iterator
            public V next() {
                validateIterator();
                return this.delegateIterator.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.delegateIterator.remove();
                AbstractMultimap.access$210(AbstractMultimap.this);
                WrappedCollection.this.removeIfEmpty();
            }

            Iterator<V> getDelegateIterator() {
                validateIterator();
                return this.delegateIterator;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean add(V v) {
            refreshIfEmpty();
            boolean zIsEmpty = this.delegate.isEmpty();
            boolean zAdd = this.delegate.add(v);
            if (zAdd) {
                AbstractMultimap.access$208(AbstractMultimap.this);
                if (zIsEmpty) {
                    addToMap();
                }
            }
            return zAdd;
        }

        AbstractMultimap<K, V>.WrappedCollection getAncestor() {
            return this.ancestor;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean zAddAll = this.delegate.addAll(collection);
            if (zAddAll) {
                AbstractMultimap.access$212(AbstractMultimap.this, this.delegate.size() - size);
                if (size == 0) {
                    addToMap();
                }
            }
            return zAddAll;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            refreshIfEmpty();
            return this.delegate.contains(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            refreshIfEmpty();
            return this.delegate.containsAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            int size = size();
            if (size == 0) {
                return;
            }
            this.delegate.clear();
            AbstractMultimap.access$220(AbstractMultimap.this, size);
            removeIfEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            refreshIfEmpty();
            boolean zRemove = this.delegate.remove(obj);
            if (zRemove) {
                AbstractMultimap.access$210(AbstractMultimap.this);
                removeIfEmpty();
            }
            return zRemove;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean zRemoveAll = this.delegate.removeAll(collection);
            if (zRemoveAll) {
                AbstractMultimap.access$212(AbstractMultimap.this, this.delegate.size() - size);
                removeIfEmpty();
            }
            return zRemoveAll;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            Preconditions.checkNotNull(collection);
            int size = size();
            boolean zRetainAll = this.delegate.retainAll(collection);
            if (zRetainAll) {
                AbstractMultimap.access$212(AbstractMultimap.this, this.delegate.size() - size);
                removeIfEmpty();
            }
            return zRetainAll;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Iterator<V> iteratorOrListIterator(Collection<V> collection) {
        return collection instanceof List ? ((List) collection).listIterator() : collection.iterator();
    }

    private class WrappedSet extends AbstractMultimap<K, V>.WrappedCollection implements Set<V> {
        WrappedSet(K k, Set<V> set) {
            super(k, set, null);
        }
    }

    private class WrappedSortedSet extends AbstractMultimap<K, V>.WrappedCollection implements SortedSet<V> {
        WrappedSortedSet(@Nullable K k, SortedSet<V> sortedSet, @Nullable AbstractMultimap<K, V>.WrappedCollection wrappedCollection) {
            super(k, sortedSet, wrappedCollection);
        }

        SortedSet<V> getSortedSetDelegate() {
            return (SortedSet) getDelegate();
        }

        @Override // java.util.SortedSet
        public Comparator<? super V> comparator() {
            return getSortedSetDelegate().comparator();
        }

        @Override // java.util.SortedSet
        public V first() {
            refreshIfEmpty();
            return getSortedSetDelegate().first();
        }

        @Override // java.util.SortedSet
        public V last() {
            refreshIfEmpty();
            return getSortedSetDelegate().last();
        }

        @Override // java.util.SortedSet
        public SortedSet<V> headSet(V v) {
            refreshIfEmpty();
            return new WrappedSortedSet(getKey(), getSortedSetDelegate().headSet(v), getAncestor() == null ? this : getAncestor());
        }

        @Override // java.util.SortedSet
        public SortedSet<V> subSet(V v, V v2) {
            refreshIfEmpty();
            return new WrappedSortedSet(getKey(), getSortedSetDelegate().subSet(v, v2), getAncestor() == null ? this : getAncestor());
        }

        @Override // java.util.SortedSet
        public SortedSet<V> tailSet(V v) {
            refreshIfEmpty();
            return new WrappedSortedSet(getKey(), getSortedSetDelegate().tailSet(v), getAncestor() == null ? this : getAncestor());
        }
    }

    private class WrappedList extends AbstractMultimap<K, V>.WrappedCollection implements List<V> {
        WrappedList(K k, List<V> list, @Nullable AbstractMultimap<K, V>.WrappedCollection wrappedCollection) {
            super(k, list, wrappedCollection);
        }

        List<V> getListDelegate() {
            return (List) getDelegate();
        }

        @Override // java.util.List
        public boolean addAll(int i, Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean zAddAll = getListDelegate().addAll(i, collection);
            if (zAddAll) {
                AbstractMultimap.access$212(AbstractMultimap.this, getDelegate().size() - size);
                if (size == 0) {
                    addToMap();
                }
            }
            return zAddAll;
        }

        @Override // java.util.List
        public V get(int i) {
            refreshIfEmpty();
            return getListDelegate().get(i);
        }

        @Override // java.util.List
        public V set(int i, V v) {
            refreshIfEmpty();
            return getListDelegate().set(i, v);
        }

        @Override // java.util.List
        public void add(int i, V v) {
            refreshIfEmpty();
            boolean zIsEmpty = getDelegate().isEmpty();
            getListDelegate().add(i, v);
            AbstractMultimap.access$208(AbstractMultimap.this);
            if (zIsEmpty) {
                addToMap();
            }
        }

        @Override // java.util.List
        public V remove(int i) {
            refreshIfEmpty();
            V vRemove = getListDelegate().remove(i);
            AbstractMultimap.access$210(AbstractMultimap.this);
            removeIfEmpty();
            return vRemove;
        }

        @Override // java.util.List
        public int indexOf(Object obj) {
            refreshIfEmpty();
            return getListDelegate().indexOf(obj);
        }

        @Override // java.util.List
        public int lastIndexOf(Object obj) {
            refreshIfEmpty();
            return getListDelegate().lastIndexOf(obj);
        }

        @Override // java.util.List
        public ListIterator<V> listIterator() {
            refreshIfEmpty();
            return new WrappedListIterator();
        }

        @Override // java.util.List
        public ListIterator<V> listIterator(int i) {
            refreshIfEmpty();
            return new WrappedListIterator(i);
        }

        @Override // java.util.List
        @GwtIncompatible("List.subList")
        public List<V> subList(int i, int i2) {
            refreshIfEmpty();
            return AbstractMultimap.this.wrapList(getKey(), Platform.subList(getListDelegate(), i, i2), getAncestor() == null ? this : getAncestor());
        }

        private class WrappedListIterator extends AbstractMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
            WrappedListIterator() {
                super();
            }

            public WrappedListIterator(int i) {
                super(WrappedList.this.getListDelegate().listIterator(i));
            }

            private ListIterator<V> getDelegateListIterator() {
                return (ListIterator) getDelegateIterator();
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return getDelegateListIterator().hasPrevious();
            }

            @Override // java.util.ListIterator
            public V previous() {
                return getDelegateListIterator().previous();
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return getDelegateListIterator().nextIndex();
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return getDelegateListIterator().previousIndex();
            }

            @Override // java.util.ListIterator
            public void set(V v) {
                getDelegateListIterator().set(v);
            }

            @Override // java.util.ListIterator
            public void add(V v) {
                boolean zIsEmpty = WrappedList.this.isEmpty();
                getDelegateListIterator().add(v);
                AbstractMultimap.access$208(AbstractMultimap.this);
                if (zIsEmpty) {
                    WrappedList.this.addToMap();
                }
            }
        }
    }

    private class RandomAccessWrappedList extends WrappedList implements RandomAccess {
        RandomAccessWrappedList(K k, List<V> list, @Nullable AbstractMultimap<K, V>.WrappedCollection wrappedCollection) {
            super(k, list, wrappedCollection);
        }
    }

    @Override // com.google.common.collect.Multimap
    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        Set<K> setCreateKeySet = createKeySet();
        this.keySet = setCreateKeySet;
        return setCreateKeySet;
    }

    private Set<K> createKeySet() {
        return this.map instanceof SortedMap ? new SortedKeySet((SortedMap) this.map) : new KeySet(this.map);
    }

    private class KeySet extends AbstractSet<K> {
        final Map<K, Collection<V>> subMap;

        KeySet(Map<K, Collection<V>> map) {
            this.subMap = map;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.subMap.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new Iterator<K>() { // from class: com.google.common.collect.AbstractMultimap.KeySet.1
                Map.Entry<K, Collection<V>> entry;
                final Iterator<Map.Entry<K, Collection<V>>> entryIterator;

                {
                    this.entryIterator = KeySet.this.subMap.entrySet().iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.entryIterator.hasNext();
                }

                @Override // java.util.Iterator
                public K next() {
                    this.entry = this.entryIterator.next();
                    return this.entry.getKey();
                }

                @Override // java.util.Iterator
                public void remove() {
                    Preconditions.checkState(this.entry != null);
                    Collection<V> value = this.entry.getValue();
                    this.entryIterator.remove();
                    AbstractMultimap.access$220(AbstractMultimap.this, value.size());
                    value.clear();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.subMap.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int size;
            Collection<V> collectionRemove = this.subMap.remove(obj);
            if (collectionRemove != null) {
                size = collectionRemove.size();
                collectionRemove.clear();
                AbstractMultimap.access$220(AbstractMultimap.this, size);
            } else {
                size = 0;
            }
            return size > 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> collection) {
            return this.subMap.keySet().containsAll(collection);
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public boolean equals(@Nullable Object obj) {
            return this == obj || this.subMap.keySet().equals(obj);
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return this.subMap.keySet().hashCode();
        }
    }

    private class SortedKeySet extends AbstractMultimap<K, V>.KeySet implements SortedSet<K> {
        SortedKeySet(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) this.subMap;
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        @Override // java.util.SortedSet
        public K first() {
            return sortedMap().firstKey();
        }

        @Override // java.util.SortedSet
        public SortedSet<K> headSet(K k) {
            return new SortedKeySet(sortedMap().headMap(k));
        }

        @Override // java.util.SortedSet
        public K last() {
            return sortedMap().lastKey();
        }

        @Override // java.util.SortedSet
        public SortedSet<K> subSet(K k, K k2) {
            return new SortedKeySet(sortedMap().subMap(k, k2));
        }

        @Override // java.util.SortedSet
        public SortedSet<K> tailSet(K k) {
            return new SortedKeySet(sortedMap().tailMap(k));
        }
    }

    @Override // com.google.common.collect.Multimap
    public Multiset<K> keys() {
        Multiset<K> multiset = this.multiset;
        if (multiset != null) {
            return multiset;
        }
        MultisetView multisetView = new MultisetView();
        this.multiset = multisetView;
        return multisetView;
    }

    private class MultisetView extends AbstractMultiset<K> {
        transient Set<Multiset.Entry<K>> entrySet;

        private MultisetView() {
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int remove(Object obj, int i) {
            if (i == 0) {
                return count(obj);
            }
            Preconditions.checkArgument(i > 0);
            try {
                Collection collection = (Collection) AbstractMultimap.this.map.get(obj);
                if (collection == null) {
                    return 0;
                }
                int size = collection.size();
                if (i >= size) {
                    return AbstractMultimap.this.removeValuesForKey(obj);
                }
                Iterator it = collection.iterator();
                for (int i2 = 0; i2 < i; i2++) {
                    it.next();
                    it.remove();
                }
                AbstractMultimap.access$220(AbstractMultimap.this, i);
                return size;
            } catch (ClassCastException unused) {
                return 0;
            } catch (NullPointerException unused2) {
                return 0;
            }
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public Set<K> elementSet() {
            return AbstractMultimap.this.keySet();
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public Set<Multiset.Entry<K>> entrySet() {
            Set<Multiset.Entry<K>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            EntrySet entrySet = new EntrySet();
            this.entrySet = entrySet;
            return entrySet;
        }

        private class EntrySet extends AbstractSet<Multiset.Entry<K>> {
            private EntrySet() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Multiset.Entry<K>> iterator() {
                return new MultisetEntryIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return AbstractMultimap.this.map.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                if (!(obj instanceof Multiset.Entry)) {
                    return false;
                }
                Multiset.Entry entry = (Multiset.Entry) obj;
                Collection collection = (Collection) AbstractMultimap.this.map.get(entry.getElement());
                return collection != null && collection.size() == entry.getCount();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                AbstractMultimap.this.clear();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                return contains(obj) && AbstractMultimap.this.removeValuesForKey(((Multiset.Entry) obj).getElement()) > 0;
            }
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
        public Iterator<K> iterator() {
            return new MultisetKeyIterator();
        }

        @Override // com.google.common.collect.AbstractMultiset, com.google.common.collect.Multiset
        public int count(Object obj) {
            try {
                Collection collection = (Collection) AbstractMultimap.this.map.get(obj);
                if (collection == null) {
                    return 0;
                }
                return collection.size();
            } catch (ClassCastException unused) {
                return 0;
            } catch (NullPointerException unused2) {
                return 0;
            }
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractMultimap.this.totalSize;
        }

        @Override // com.google.common.collect.AbstractMultiset, java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractMultimap.this.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int removeValuesForKey(Object obj) {
        try {
            Collection<V> collectionRemove = this.map.remove(obj);
            if (collectionRemove == null) {
                return 0;
            }
            int size = collectionRemove.size();
            collectionRemove.clear();
            this.totalSize -= size;
            return size;
        } catch (ClassCastException unused) {
            return 0;
        } catch (NullPointerException unused2) {
            return 0;
        }
    }

    private class MultisetEntryIterator implements Iterator<Multiset.Entry<K>> {
        final Iterator<Map.Entry<K, Collection<V>>> asMapIterator;

        private MultisetEntryIterator() {
            this.asMapIterator = AbstractMultimap.this.asMap().entrySet().iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.asMapIterator.hasNext();
        }

        @Override // java.util.Iterator
        public Multiset.Entry<K> next() {
            return new MultisetEntry(this.asMapIterator.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            this.asMapIterator.remove();
        }
    }

    private class MultisetEntry extends Multisets.AbstractEntry<K> {
        final Map.Entry<K, Collection<V>> entry;

        public MultisetEntry(Map.Entry<K, Collection<V>> entry) {
            this.entry = entry;
        }

        @Override // com.google.common.collect.Multiset.Entry
        public K getElement() {
            return this.entry.getKey();
        }

        @Override // com.google.common.collect.Multiset.Entry
        public int getCount() {
            return this.entry.getValue().size();
        }
    }

    private class MultisetKeyIterator implements Iterator<K> {
        final Iterator<Map.Entry<K, V>> entryIterator;

        private MultisetKeyIterator() {
            this.entryIterator = AbstractMultimap.this.entries().iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.entryIterator.hasNext();
        }

        @Override // java.util.Iterator
        public K next() {
            return this.entryIterator.next().getKey();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.entryIterator.remove();
        }
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> values() {
        Collection<V> collection = this.valuesCollection;
        if (collection != null) {
            return collection;
        }
        Values values = new Values();
        this.valuesCollection = values;
        return values;
    }

    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractMultimap.this.totalSize;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractMultimap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return AbstractMultimap.this.containsValue(obj);
        }
    }

    private class ValueIterator implements Iterator<V> {
        final Iterator<Map.Entry<K, V>> entryIterator;

        private ValueIterator() {
            this.entryIterator = AbstractMultimap.this.createEntryIterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.entryIterator.hasNext();
        }

        @Override // java.util.Iterator
        public V next() {
            return this.entryIterator.next().getValue();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.entryIterator.remove();
        }
    }

    @Override // com.google.common.collect.Multimap
    public Collection<Map.Entry<K, V>> entries() {
        Collection<Map.Entry<K, V>> collection = this.entries;
        if (this.entries != null) {
            return collection;
        }
        Collection<Map.Entry<K, V>> collectionCreateEntries = createEntries();
        this.entries = collectionCreateEntries;
        return collectionCreateEntries;
    }

    private Collection<Map.Entry<K, V>> createEntries() {
        return this instanceof SetMultimap ? new EntrySet() : new Entries();
    }

    private class Entries extends AbstractCollection<Map.Entry<K, V>> {
        private Entries() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return AbstractMultimap.this.createEntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractMultimap.this.totalSize;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return AbstractMultimap.this.containsEntry(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            AbstractMultimap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return AbstractMultimap.this.remove(entry.getKey(), entry.getValue());
        }
    }

    Iterator<Map.Entry<K, V>> createEntryIterator() {
        return new EntryIterator();
    }

    private class EntryIterator implements Iterator<Map.Entry<K, V>> {
        Collection<V> collection;
        K key;
        final Iterator<Map.Entry<K, Collection<V>>> keyIterator;
        Iterator<V> valueIterator;

        EntryIterator() {
            this.keyIterator = AbstractMultimap.this.map.entrySet().iterator();
            if (this.keyIterator.hasNext()) {
                findValueIteratorAndKey();
            } else {
                this.valueIterator = Iterators.emptyModifiableIterator();
            }
        }

        void findValueIteratorAndKey() {
            Map.Entry<K, Collection<V>> next = this.keyIterator.next();
            this.key = next.getKey();
            this.collection = next.getValue();
            this.valueIterator = this.collection.iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.keyIterator.hasNext() || this.valueIterator.hasNext();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            if (!this.valueIterator.hasNext()) {
                findValueIteratorAndKey();
            }
            return Maps.immutableEntry(this.key, this.valueIterator.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            this.valueIterator.remove();
            if (this.collection.isEmpty()) {
                this.keyIterator.remove();
            }
            AbstractMultimap.access$210(AbstractMultimap.this);
        }
    }

    private class EntrySet extends AbstractMultimap<K, V>.Entries implements Set<Map.Entry<K, V>> {
        private EntrySet() {
            super();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(@Nullable Object obj) {
            return Collections2.setEquals(this, obj);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    @Override // com.google.common.collect.Multimap
    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> map = this.asMap;
        if (map != null) {
            return map;
        }
        Map<K, Collection<V>> mapCreateAsMap = createAsMap();
        this.asMap = mapCreateAsMap;
        return mapCreateAsMap;
    }

    private Map<K, Collection<V>> createAsMap() {
        return this.map instanceof SortedMap ? new SortedAsMap((SortedMap) this.map) : new AsMap(this.map);
    }

    private class AsMap extends AbstractMap<K, Collection<V>> {
        transient Set<Map.Entry<K, Collection<V>>> entrySet;
        final transient Map<K, Collection<V>> submap;

        AsMap(Map<K, Collection<V>> map) {
            this.submap = map;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, Collection<V>>> entrySet() {
            Set<Map.Entry<K, Collection<V>>> set = this.entrySet;
            if (this.entrySet != null) {
                return set;
            }
            AsMapEntries asMapEntries = new AsMapEntries();
            this.entrySet = asMapEntries;
            return asMapEntries;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return Maps.safeContainsKey(this.submap, obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> get(Object obj) {
            Collection collection = (Collection) Maps.safeGet(this.submap, obj);
            if (collection == null) {
                return null;
            }
            return AbstractMultimap.this.wrapCollection(obj, collection);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return AbstractMultimap.this.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> remove(Object obj) {
            Collection<V> collectionRemove = this.submap.remove(obj);
            if (collectionRemove == null) {
                return null;
            }
            Collection<V> collectionCreateCollection = AbstractMultimap.this.createCollection();
            collectionCreateCollection.addAll(collectionRemove);
            AbstractMultimap.access$220(AbstractMultimap.this, collectionRemove.size());
            collectionRemove.clear();
            return collectionCreateCollection;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean equals(@Nullable Object obj) {
            return this == obj || this.submap.equals(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int hashCode() {
            return this.submap.hashCode();
        }

        @Override // java.util.AbstractMap
        public String toString() {
            return this.submap.toString();
        }

        class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>> {
            AsMapEntries() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return AsMap.this.new AsMapIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return AsMap.this.submap.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                return Collections2.safeContains(AsMap.this.submap.entrySet(), obj);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                if (!contains(obj)) {
                    return false;
                }
                AbstractMultimap.this.removeValuesForKey(((Map.Entry) obj).getKey());
                return true;
            }
        }

        class AsMapIterator implements Iterator<Map.Entry<K, Collection<V>>> {
            Collection<V> collection;
            final Iterator<Map.Entry<K, Collection<V>>> delegateIterator;

            AsMapIterator() {
                this.delegateIterator = AsMap.this.submap.entrySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.delegateIterator.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, Collection<V>> next() {
                Map.Entry<K, Collection<V>> next = this.delegateIterator.next();
                K key = next.getKey();
                this.collection = next.getValue();
                return Maps.immutableEntry(key, AbstractMultimap.this.wrapCollection(key, this.collection));
            }

            @Override // java.util.Iterator
            public void remove() {
                this.delegateIterator.remove();
                AbstractMultimap.access$220(AbstractMultimap.this, this.collection.size());
                this.collection.clear();
            }
        }
    }

    private class SortedAsMap extends AbstractMultimap<K, V>.AsMap implements SortedMap<K, Collection<V>> {
        SortedSet<K> sortedKeySet;

        SortedAsMap(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        SortedMap<K, Collection<V>> sortedMap() {
            return (SortedMap) this.submap;
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            return sortedMap().comparator();
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            return sortedMap().firstKey();
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            return sortedMap().lastKey();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, Collection<V>> headMap(K k) {
            return new SortedAsMap(sortedMap().headMap(k));
        }

        @Override // java.util.SortedMap
        public SortedMap<K, Collection<V>> subMap(K k, K k2) {
            return new SortedAsMap(sortedMap().subMap(k, k2));
        }

        @Override // java.util.SortedMap
        public SortedMap<K, Collection<V>> tailMap(K k) {
            return new SortedAsMap(sortedMap().tailMap(k));
        }

        @Override // com.google.common.collect.AbstractMultimap.AsMap, java.util.AbstractMap, java.util.Map
        public SortedSet<K> keySet() {
            SortedSet<K> sortedSet = this.sortedKeySet;
            if (sortedSet != null) {
                return sortedSet;
            }
            SortedKeySet sortedKeySet = new SortedKeySet(sortedMap());
            this.sortedKeySet = sortedKeySet;
            return sortedKeySet;
        }
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Multimap) {
            return this.map.equals(((Multimap) obj).asMap());
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        return this.map.toString();
    }
}
