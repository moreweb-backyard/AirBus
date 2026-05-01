package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public final class LinkedHashMultimap<K, V> extends AbstractSetMultimap<K, V> {
    private static final int DEFAULT_VALUES_PER_KEY = 8;
    private static final long serialVersionUID = 0;

    @VisibleForTesting
    transient int expectedValuesPerKey;
    transient Collection<Map.Entry<K, V>> linkedEntries;

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Map asMap() {
        return super.asMap();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsEntry(Object obj, Object obj2) {
        return super.containsEntry(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsKey(Object obj) {
        return super.containsKey(obj);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsValue(Object obj) {
        return super.containsValue(obj);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set get(Object obj) {
        return super.get((LinkedHashMultimap<K, V>) obj);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Multiset keys() {
        return super.keys();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean put(Object obj, Object obj2) {
        return super.put(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(Multimap multimap) {
        return super.putAll(multimap);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean putAll(Object obj, Iterable iterable) {
        return super.putAll(obj, iterable);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Set removeAll(Object obj) {
        return super.removeAll(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection replaceValues(Object obj, Iterable iterable) {
        return replaceValues((LinkedHashMultimap<K, V>) obj, iterable);
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public static <K, V> LinkedHashMultimap<K, V> create() {
        return new LinkedHashMultimap<>();
    }

    public static <K, V> LinkedHashMultimap<K, V> create(int i, int i2) {
        return new LinkedHashMultimap<>(i, i2);
    }

    public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new LinkedHashMultimap<>(multimap);
    }

    private LinkedHashMultimap() {
        super(new LinkedHashMap());
        this.expectedValuesPerKey = 8;
        this.linkedEntries = Sets.newLinkedHashSet();
    }

    private LinkedHashMultimap(int i, int i2) {
        super(new LinkedHashMap(i));
        this.expectedValuesPerKey = 8;
        Preconditions.checkArgument(i2 >= 0);
        this.expectedValuesPerKey = i2;
        this.linkedEntries = new LinkedHashSet(i * i2);
    }

    private LinkedHashMultimap(Multimap<? extends K, ? extends V> multimap) {
        super(new LinkedHashMap(Maps.capacity(multimap.keySet().size())));
        this.expectedValuesPerKey = 8;
        this.linkedEntries = new LinkedHashSet(Maps.capacity(multimap.size()));
        putAll(multimap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap
    public Set<V> createCollection() {
        return new LinkedHashSet(Maps.capacity(this.expectedValuesPerKey));
    }

    @Override // com.google.common.collect.AbstractMultimap
    Collection<V> createCollection(@Nullable K k) {
        return new SetDecorator(k, createCollection());
    }

    private class SetDecorator extends ForwardingSet<V> {
        final Set<V> delegate;
        final K key;

        SetDecorator(@Nullable K k, Set<V> set) {
            this.delegate = set;
            this.key = k;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.ForwardingSet, com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
        public Set<V> delegate() {
            return this.delegate;
        }

        <E> Map.Entry<K, E> createEntry(@Nullable E e) {
            return Maps.immutableEntry(this.key, e);
        }

        <E> Collection<Map.Entry<K, E>> createEntries(Collection<E> collection) {
            ArrayList arrayListNewArrayListWithExpectedSize = Lists.newArrayListWithExpectedSize(collection.size());
            Iterator<E> it = collection.iterator();
            while (it.hasNext()) {
                arrayListNewArrayListWithExpectedSize.add(createEntry(it.next()));
            }
            return arrayListNewArrayListWithExpectedSize;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean add(@Nullable V v) {
            boolean zAdd = this.delegate.add(v);
            if (zAdd) {
                LinkedHashMultimap.this.linkedEntries.add(createEntry(v));
            }
            return zAdd;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection
        public boolean addAll(Collection<? extends V> collection) {
            boolean zAddAll = this.delegate.addAll(collection);
            if (zAddAll) {
                LinkedHashMultimap.this.linkedEntries.addAll(createEntries(delegate()));
            }
            return zAddAll;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public void clear() {
            LinkedHashMultimap.this.linkedEntries.removeAll(createEntries(delegate()));
            this.delegate.clear();
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            final Iterator<V> it = this.delegate.iterator();
            return new Iterator<V>() { // from class: com.google.common.collect.LinkedHashMultimap.SetDecorator.1
                V value;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override // java.util.Iterator
                public V next() {
                    this.value = (V) it.next();
                    return this.value;
                }

                @Override // java.util.Iterator
                public void remove() {
                    it.remove();
                    LinkedHashMultimap.this.linkedEntries.remove(SetDecorator.this.createEntry(this.value));
                }
            };
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean remove(@Nullable Object obj) {
            boolean zRemove = this.delegate.remove(obj);
            if (zRemove) {
                LinkedHashMultimap.this.linkedEntries.remove(createEntry(obj));
            }
            return zRemove;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            boolean zRemoveAll = this.delegate.removeAll(collection);
            if (zRemoveAll) {
                LinkedHashMultimap.this.linkedEntries.removeAll(createEntries(collection));
            }
            return zRemoveAll;
        }

        @Override // com.google.common.collect.ForwardingCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            Iterator<V> it = this.delegate.iterator();
            boolean z = false;
            while (it.hasNext()) {
                V next = it.next();
                if (!collection.contains(next)) {
                    it.remove();
                    LinkedHashMultimap.this.linkedEntries.remove(Maps.immutableEntry(this.key, next));
                    z = true;
                }
            }
            return z;
        }
    }

    @Override // com.google.common.collect.AbstractMultimap
    Iterator<Map.Entry<K, V>> createEntryIterator() {
        final Iterator<Map.Entry<K, V>> it = this.linkedEntries.iterator();
        return new Iterator<Map.Entry<K, V>>() { // from class: com.google.common.collect.LinkedHashMultimap.1
            Map.Entry<K, V> entry;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                this.entry = (Map.Entry) it.next();
                return this.entry;
            }

            @Override // java.util.Iterator
            public void remove() {
                it.remove();
                LinkedHashMultimap.this.remove(this.entry.getKey(), this.entry.getValue());
            }
        };
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Set<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        return super.replaceValues((LinkedHashMultimap<K, V>) k, (Iterable) iterable);
    }

    @Override // com.google.common.collect.AbstractSetMultimap, com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Set<Map.Entry<K, V>> entries() {
        return super.entries();
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public Collection<V> values() {
        return super.values();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.expectedValuesPerKey);
        Serialization.writeMultimap(this, objectOutputStream);
        for (Map.Entry<K, V> entry : this.linkedEntries) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.expectedValuesPerKey = objectInputStream.readInt();
        int count = Serialization.readCount(objectInputStream);
        setMap(new LinkedHashMap(Maps.capacity(count)));
        this.linkedEntries = new LinkedHashSet(this.expectedValuesPerKey * count);
        Serialization.populateMultimap(this, objectInputStream, count);
        this.linkedEntries.clear();
        for (int i = 0; i < size(); i++) {
            this.linkedEntries.add(Maps.immutableEntry(objectInputStream.readObject(), objectInputStream.readObject()));
        }
    }
}
