package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.litesuits.http.data.Consts;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public final class LinkedListMultimap<K, V> implements ListMultimap<K, V>, Serializable {
    private static final long serialVersionUID = 0;
    private transient Collection<Map.Entry<K, V>> entries;
    private transient Node<K, V> head;
    private transient Multiset<K> keyCount;
    private transient Set<K> keySet;
    private transient Map<K, Node<K, V>> keyToKeyHead;
    private transient Map<K, Node<K, V>> keyToKeyTail;
    private transient Multiset<K> keys;
    private transient Map<K, Collection<V>> map;
    private transient Node<K, V> tail;
    private transient Collection<V> valuesCollection;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection get(Object obj) {
        return get((LinkedListMultimap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection replaceValues(Object obj, Iterable iterable) {
        return replaceValues((LinkedListMultimap<K, V>) obj, iterable);
    }

    private static final class Node<K, V> {
        final K key;
        Node<K, V> next;
        Node<K, V> nextSibling;
        Node<K, V> previous;
        Node<K, V> previousSibling;
        V value;

        Node(@Nullable K k, @Nullable V v) {
            this.key = k;
            this.value = v;
        }

        public String toString() {
            return this.key + Consts.EQUALS + this.value;
        }
    }

    public static <K, V> LinkedListMultimap<K, V> create() {
        return new LinkedListMultimap<>();
    }

    public static <K, V> LinkedListMultimap<K, V> create(int i) {
        return new LinkedListMultimap<>(i);
    }

    public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new LinkedListMultimap<>(multimap);
    }

    private LinkedListMultimap() {
        this.keyCount = LinkedHashMultiset.create();
        this.keyToKeyHead = Maps.newHashMap();
        this.keyToKeyTail = Maps.newHashMap();
    }

    private LinkedListMultimap(int i) {
        this.keyCount = LinkedHashMultiset.create(i);
        this.keyToKeyHead = Maps.newHashMapWithExpectedSize(i);
        this.keyToKeyTail = Maps.newHashMapWithExpectedSize(i);
    }

    private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size());
        putAll(multimap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Node<K, V> addNode(@Nullable K k, @Nullable V v, @Nullable Node<K, V> node) {
        Node<K, V> node2 = new Node<>(k, v);
        if (this.head == null) {
            this.tail = node2;
            this.head = node2;
            this.keyToKeyHead.put(k, node2);
            this.keyToKeyTail.put(k, node2);
        } else if (node == null) {
            this.tail.next = node2;
            node2.previous = this.tail;
            Node<K, V> node3 = this.keyToKeyTail.get(k);
            if (node3 == null) {
                this.keyToKeyHead.put(k, node2);
            } else {
                node3.nextSibling = node2;
                node2.previousSibling = node3;
            }
            this.keyToKeyTail.put(k, node2);
            this.tail = node2;
        } else {
            node2.previous = node.previous;
            node2.previousSibling = node.previousSibling;
            node2.next = node;
            node2.nextSibling = node;
            if (node.previousSibling == null) {
                this.keyToKeyHead.put(k, node2);
            } else {
                node.previousSibling.nextSibling = node2;
            }
            if (node.previous == null) {
                this.head = node2;
            } else {
                node.previous.next = node2;
            }
            node.previous = node2;
            node.previousSibling = node2;
        }
        this.keyCount.add(k);
        return node2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeNode(Node<K, V> node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            this.head = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        } else {
            this.tail = node.previous;
        }
        if (node.previousSibling != null) {
            node.previousSibling.nextSibling = node.nextSibling;
        } else if (node.nextSibling != null) {
            this.keyToKeyHead.put(node.key, node.nextSibling);
        } else {
            this.keyToKeyHead.remove(node.key);
        }
        if (node.nextSibling != null) {
            node.nextSibling.previousSibling = node.previousSibling;
        } else if (node.previousSibling != null) {
            this.keyToKeyTail.put(node.key, node.previousSibling);
        } else {
            this.keyToKeyTail.remove(node.key);
        }
        this.keyCount.remove(node.key);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeAllNodes(@Nullable Object obj) {
        ValueForKeyIterator valueForKeyIterator = new ValueForKeyIterator(obj);
        while (valueForKeyIterator.hasNext()) {
            valueForKeyIterator.next();
            valueForKeyIterator.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkElement(@Nullable Object obj) {
        if (obj == null) {
            throw new NoSuchElementException();
        }
    }

    private class NodeIterator implements Iterator<Node<K, V>> {
        Node<K, V> current;
        Node<K, V> next;

        private NodeIterator() {
            this.next = LinkedListMultimap.this.head;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.Iterator
        public Node<K, V> next() {
            LinkedListMultimap.checkElement(this.next);
            this.current = this.next;
            this.next = this.next.next;
            return this.current;
        }

        @Override // java.util.Iterator
        public void remove() {
            Preconditions.checkState(this.current != null);
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }
    }

    private class DistinctKeyIterator implements Iterator<K> {
        Node<K, V> current;
        Node<K, V> next;
        final Set<K> seenKeys;

        private DistinctKeyIterator() {
            this.seenKeys = new HashSet(Maps.capacity(LinkedListMultimap.this.keySet().size()));
            this.next = LinkedListMultimap.this.head;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.Iterator
        public K next() {
            LinkedListMultimap.checkElement(this.next);
            this.current = this.next;
            this.seenKeys.add(this.current.key);
            do {
                this.next = this.next.next;
                if (this.next == null) {
                    break;
                }
            } while (!this.seenKeys.add(this.next.key));
            return this.current.key;
        }

        @Override // java.util.Iterator
        public void remove() {
            Preconditions.checkState(this.current != null);
            LinkedListMultimap.this.removeAllNodes(this.current.key);
            this.current = null;
        }
    }

    private class ValueForKeyIterator implements ListIterator<V> {
        Node<K, V> current;
        final Object key;
        Node<K, V> next;
        int nextIndex;
        Node<K, V> previous;

        ValueForKeyIterator(@Nullable Object obj) {
            this.key = obj;
            this.next = (Node) LinkedListMultimap.this.keyToKeyHead.get(obj);
        }

        public ValueForKeyIterator(@Nullable Object obj, int i) {
            int iCount = LinkedListMultimap.this.keyCount.count(obj);
            Preconditions.checkPositionIndex(i, iCount);
            if (i >= iCount / 2) {
                this.previous = (Node) LinkedListMultimap.this.keyToKeyTail.get(obj);
                this.nextIndex = iCount;
                while (true) {
                    int i2 = i + 1;
                    if (i >= iCount) {
                        break;
                    }
                    previous();
                    i = i2;
                }
            } else {
                this.next = (Node) LinkedListMultimap.this.keyToKeyHead.get(obj);
                while (true) {
                    int i3 = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    next();
                    i = i3;
                }
            }
            this.key = obj;
            this.current = null;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public V next() {
            LinkedListMultimap.checkElement(this.next);
            Node<K, V> node = this.next;
            this.current = node;
            this.previous = node;
            this.next = this.next.nextSibling;
            this.nextIndex++;
            return this.current.value;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.previous != null;
        }

        @Override // java.util.ListIterator
        public V previous() {
            LinkedListMultimap.checkElement(this.previous);
            Node<K, V> node = this.previous;
            this.current = node;
            this.next = node;
            this.previous = this.previous.previousSibling;
            this.nextIndex--;
            return this.current.value;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.nextIndex;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.nextIndex - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            Preconditions.checkState(this.current != null);
            if (this.current != this.next) {
                this.previous = this.current.previousSibling;
                this.nextIndex--;
            } else {
                this.next = this.current.nextSibling;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }

        @Override // java.util.ListIterator
        public void set(V v) {
            Preconditions.checkState(this.current != null);
            this.current.value = v;
        }

        @Override // java.util.ListIterator
        public void add(V v) {
            this.previous = LinkedListMultimap.this.addNode(this.key, v, this.next);
            this.nextIndex++;
            this.current = null;
        }
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return this.keyCount.size();
    }

    @Override // com.google.common.collect.Multimap
    public boolean isEmpty() {
        return this.head == null;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@Nullable Object obj) {
        return this.keyToKeyHead.containsKey(obj);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsValue(@Nullable Object obj) {
        NodeIterator nodeIterator = new NodeIterator();
        while (nodeIterator.hasNext()) {
            if (Objects.equal(nodeIterator.next().value, obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsEntry(@Nullable Object obj, @Nullable Object obj2) {
        ValueForKeyIterator valueForKeyIterator = new ValueForKeyIterator(obj);
        while (valueForKeyIterator.hasNext()) {
            if (Objects.equal(valueForKeyIterator.next(), obj2)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public boolean put(@Nullable K k, @Nullable V v) {
        addNode(k, v, null);
        return true;
    }

    @Override // com.google.common.collect.Multimap
    public boolean remove(@Nullable Object obj, @Nullable Object obj2) {
        ValueForKeyIterator valueForKeyIterator = new ValueForKeyIterator(obj);
        while (valueForKeyIterator.hasNext()) {
            if (Objects.equal(valueForKeyIterator.next(), obj2)) {
                valueForKeyIterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(@Nullable K k, Iterable<? extends V> iterable) {
        Iterator<? extends V> it = iterable.iterator();
        boolean zPut = false;
        while (it.hasNext()) {
            zPut |= put(k, it.next());
        }
        return zPut;
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean zPut = false;
        for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
            zPut |= put(entry.getKey(), entry.getValue());
        }
        return zPut;
    }

    @Override // com.google.common.collect.ListMultimap, com.google.common.collect.Multimap
    public List<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        List<V> copy = getCopy(k);
        ValueForKeyIterator valueForKeyIterator = new ValueForKeyIterator(k);
        Iterator<? extends V> it = iterable.iterator();
        while (valueForKeyIterator.hasNext() && it.hasNext()) {
            valueForKeyIterator.next();
            valueForKeyIterator.set(it.next());
        }
        while (valueForKeyIterator.hasNext()) {
            valueForKeyIterator.next();
            valueForKeyIterator.remove();
        }
        while (it.hasNext()) {
            valueForKeyIterator.add(it.next());
        }
        return copy;
    }

    private List<V> getCopy(@Nullable Object obj) {
        return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(obj)));
    }

    @Override // com.google.common.collect.Multimap
    public List<V> removeAll(@Nullable Object obj) {
        List<V> copy = getCopy(obj);
        removeAllNodes(obj);
        return copy;
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        this.head = null;
        this.tail = null;
        this.keyCount.clear();
        this.keyToKeyHead.clear();
        this.keyToKeyTail.clear();
    }

    @Override // com.google.common.collect.ListMultimap, com.google.common.collect.Multimap
    public List<V> get(@Nullable final K k) {
        return new AbstractSequentialList<V>() { // from class: com.google.common.collect.LinkedListMultimap.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return LinkedListMultimap.this.keyCount.count(k);
            }

            @Override // java.util.AbstractSequentialList, java.util.AbstractList, java.util.List
            public ListIterator<V> listIterator(int i) {
                return new ValueForKeyIterator(k, i);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public boolean removeAll(Collection<?> collection) {
                return Iterators.removeAll(iterator(), collection);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public boolean retainAll(Collection<?> collection) {
                return Iterators.retainAll(iterator(), collection);
            }
        };
    }

    @Override // com.google.common.collect.Multimap
    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set != null) {
            return set;
        }
        AbstractSet<K> abstractSet = new AbstractSet<K>() { // from class: com.google.common.collect.LinkedListMultimap.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return LinkedListMultimap.this.keyCount.elementSet().size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<K> iterator() {
                return new DistinctKeyIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                return LinkedListMultimap.this.keyCount.contains(obj);
            }
        };
        this.keySet = abstractSet;
        return abstractSet;
    }

    @Override // com.google.common.collect.Multimap
    public Multiset<K> keys() {
        Multiset<K> multiset = this.keys;
        if (multiset != null) {
            return multiset;
        }
        MultisetView multisetView = new MultisetView();
        this.keys = multisetView;
        return multisetView;
    }

    private class MultisetView extends AbstractCollection<K> implements Multiset<K> {
        private MultisetView() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return LinkedListMultimap.this.keyCount.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, com.google.common.collect.Multiset
        public Iterator<K> iterator() {
            final NodeIterator nodeIterator = new NodeIterator();
            return new Iterator<K>() { // from class: com.google.common.collect.LinkedListMultimap.MultisetView.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return nodeIterator.hasNext();
                }

                @Override // java.util.Iterator
                public K next() {
                    return ((Node) nodeIterator.next()).key;
                }

                @Override // java.util.Iterator
                public void remove() {
                    nodeIterator.remove();
                }
            };
        }

        @Override // com.google.common.collect.Multiset
        public int count(@Nullable Object obj) {
            return LinkedListMultimap.this.keyCount.count(obj);
        }

        @Override // com.google.common.collect.Multiset
        public int add(@Nullable K k, int i) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.common.collect.Multiset
        public int remove(@Nullable Object obj, int i) {
            Preconditions.checkArgument(i >= 0);
            int iCount = count(obj);
            ValueForKeyIterator valueForKeyIterator = new ValueForKeyIterator(obj);
            while (true) {
                int i2 = i - 1;
                if (i <= 0 || !valueForKeyIterator.hasNext()) {
                    break;
                }
                valueForKeyIterator.next();
                valueForKeyIterator.remove();
                i = i2;
            }
            return iCount;
        }

        @Override // com.google.common.collect.Multiset
        public int setCount(K k, int i) {
            return Multisets.setCountImpl(this, k, i);
        }

        @Override // com.google.common.collect.Multiset
        public boolean setCount(K k, int i, int i2) {
            return Multisets.setCountImpl(this, k, i, i2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public boolean removeAll(Collection<?> collection) {
            return Iterators.removeAll(iterator(), collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.Multiset
        public boolean retainAll(Collection<?> collection) {
            return Iterators.retainAll(iterator(), collection);
        }

        @Override // com.google.common.collect.Multiset
        public Set<K> elementSet() {
            return LinkedListMultimap.this.keySet();
        }

        /* renamed from: com.google.common.collect.LinkedListMultimap$MultisetView$2, reason: invalid class name */
        class AnonymousClass2 extends AbstractSet<Multiset.Entry<K>> {
            AnonymousClass2() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return LinkedListMultimap.this.keyCount.elementSet().size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Multiset.Entry<K>> iterator() {
                final DistinctKeyIterator distinctKeyIterator = new DistinctKeyIterator();
                return new Iterator<Multiset.Entry<K>>() { // from class: com.google.common.collect.LinkedListMultimap.MultisetView.2.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return distinctKeyIterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public Multiset.Entry<K> next() {
                        final Object next = distinctKeyIterator.next();
                        return new Multisets.AbstractEntry<K>() { // from class: com.google.common.collect.LinkedListMultimap.MultisetView.2.1.1
                            @Override // com.google.common.collect.Multiset.Entry
                            public K getElement() {
                                return (K) next;
                            }

                            @Override // com.google.common.collect.Multiset.Entry
                            public int getCount() {
                                return LinkedListMultimap.this.keyCount.count(next);
                            }
                        };
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        distinctKeyIterator.remove();
                    }
                };
            }
        }

        @Override // com.google.common.collect.Multiset
        public Set<Multiset.Entry<K>> entrySet() {
            return new AnonymousClass2();
        }

        @Override // java.util.Collection, com.google.common.collect.Multiset
        public boolean equals(@Nullable Object obj) {
            return LinkedListMultimap.this.keyCount.equals(obj);
        }

        @Override // java.util.Collection, com.google.common.collect.Multiset
        public int hashCode() {
            return LinkedListMultimap.this.keyCount.hashCode();
        }

        @Override // java.util.AbstractCollection, com.google.common.collect.Multiset
        public String toString() {
            return LinkedListMultimap.this.keyCount.toString();
        }
    }

    @Override // com.google.common.collect.Multimap
    public Collection<V> values() {
        Collection<V> collection = this.valuesCollection;
        if (collection != null) {
            return collection;
        }
        AbstractCollection<V> abstractCollection = new AbstractCollection<V>() { // from class: com.google.common.collect.LinkedListMultimap.3
            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return LinkedListMultimap.this.keyCount.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<V> iterator() {
                final NodeIterator nodeIterator = new NodeIterator();
                return new Iterator<V>() { // from class: com.google.common.collect.LinkedListMultimap.3.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return nodeIterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public V next() {
                        return ((Node) nodeIterator.next()).value;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        nodeIterator.remove();
                    }
                };
            }
        };
        this.valuesCollection = abstractCollection;
        return abstractCollection;
    }

    @Override // com.google.common.collect.Multimap
    public Collection<Map.Entry<K, V>> entries() {
        Collection<Map.Entry<K, V>> collection = this.entries;
        if (collection != null) {
            return collection;
        }
        AbstractCollection<Map.Entry<K, V>> abstractCollection = new AbstractCollection<Map.Entry<K, V>>() { // from class: com.google.common.collect.LinkedListMultimap.4
            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return LinkedListMultimap.this.keyCount.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<Map.Entry<K, V>> iterator() {
                final NodeIterator nodeIterator = new NodeIterator();
                return new Iterator<Map.Entry<K, V>>() { // from class: com.google.common.collect.LinkedListMultimap.4.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return nodeIterator.hasNext();
                    }

                    @Override // java.util.Iterator
                    public Map.Entry<K, V> next() {
                        final Node node = (Node) nodeIterator.next();
                        return new AbstractMapEntry<K, V>() { // from class: com.google.common.collect.LinkedListMultimap.4.1.1
                            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                            public K getKey() {
                                return node.key;
                            }

                            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                            public V getValue() {
                                return node.value;
                            }

                            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                            public V setValue(V v) {
                                V v2 = node.value;
                                node.value = v;
                                return v2;
                            }
                        };
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        nodeIterator.remove();
                    }
                };
            }
        };
        this.entries = abstractCollection;
        return abstractCollection;
    }

    private class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>> {
        private AsMapEntries() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return LinkedListMultimap.this.keyCount.elementSet().size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final DistinctKeyIterator distinctKeyIterator = new DistinctKeyIterator();
            return new Iterator<Map.Entry<K, Collection<V>>>() { // from class: com.google.common.collect.LinkedListMultimap.AsMapEntries.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return distinctKeyIterator.hasNext();
                }

                @Override // java.util.Iterator
                public Map.Entry<K, Collection<V>> next() {
                    final Object next = distinctKeyIterator.next();
                    return new AbstractMapEntry<K, Collection<V>>() { // from class: com.google.common.collect.LinkedListMultimap.AsMapEntries.1.1
                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public K getKey() {
                            return (K) next;
                        }

                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public Collection<V> getValue() {
                            return LinkedListMultimap.this.get((LinkedListMultimap) next);
                        }
                    };
                }

                @Override // java.util.Iterator
                public void remove() {
                    distinctKeyIterator.remove();
                }
            };
        }
    }

    @Override // com.google.common.collect.ListMultimap, com.google.common.collect.Multimap
    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> map = this.map;
        if (map != null) {
            return map;
        }
        AbstractMap<K, Collection<V>> abstractMap = new AbstractMap<K, Collection<V>>() { // from class: com.google.common.collect.LinkedListMultimap.5
            Set<Map.Entry<K, Collection<V>>> entrySet;

            @Override // java.util.AbstractMap, java.util.Map
            public Set<Map.Entry<K, Collection<V>>> entrySet() {
                Set<Map.Entry<K, Collection<V>>> set = this.entrySet;
                if (set != null) {
                    return set;
                }
                AsMapEntries asMapEntries = new AsMapEntries();
                this.entrySet = asMapEntries;
                return asMapEntries;
            }

            @Override // java.util.AbstractMap, java.util.Map
            public boolean containsKey(@Nullable Object obj) {
                return LinkedListMultimap.this.containsKey(obj);
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Collection<V> get(@Nullable Object obj) {
                List<V> list = LinkedListMultimap.this.get((LinkedListMultimap) obj);
                if (list.isEmpty()) {
                    return null;
                }
                return list;
            }

            @Override // java.util.AbstractMap, java.util.Map
            public Collection<V> remove(@Nullable Object obj) {
                List<V> listRemoveAll = LinkedListMultimap.this.removeAll(obj);
                if (listRemoveAll.isEmpty()) {
                    return null;
                }
                return listRemoveAll;
            }
        };
        this.map = abstractMap;
        return abstractMap;
    }

    @Override // com.google.common.collect.ListMultimap
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Multimap) {
            return asMap().equals(((Multimap) obj).asMap());
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public int hashCode() {
        return asMap().hashCode();
    }

    public String toString() {
        return asMap().toString();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        for (Map.Entry<K, V> entry : entries()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.keyCount = LinkedHashMultiset.create();
        this.keyToKeyHead = Maps.newHashMap();
        this.keyToKeyTail = Maps.newHashMap();
        int i = objectInputStream.readInt();
        for (int i2 = 0; i2 < i; i2++) {
            put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }
}
