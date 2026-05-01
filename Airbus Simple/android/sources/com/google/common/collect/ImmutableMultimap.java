package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Serialization;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: classes.dex */
public abstract class ImmutableMultimap<K, V> implements Multimap<K, V>, Serializable {
    private static final long serialVersionUID = 0;
    private transient ImmutableCollection<Map.Entry<K, V>> entries;
    private transient ImmutableMultiset<K> keys;
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
    final transient int size;
    private transient ImmutableCollection<V> values;

    @Override // com.google.common.collect.Multimap
    public abstract ImmutableCollection<V> get(K k);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection get(Object obj) {
        return get((ImmutableMultimap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ Collection replaceValues(Object obj, Iterable iterable) {
        return replaceValues((ImmutableMultimap<K, V>) obj, iterable);
    }

    public static <K, V> ImmutableMultimap<K, V> of() {
        return ImmutableListMultimap.of();
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v) {
        return ImmutableListMultimap.of((Object) k, (Object) v);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2) {
        return ImmutableListMultimap.of((Object) k, (Object) v, (Object) k2, (Object) v2);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return ImmutableListMultimap.of((Object) k, (Object) v, (Object) k2, (Object) v2, (Object) k3, (Object) v3);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ImmutableListMultimap.of((Object) k, (Object) v, (Object) k2, (Object) v2, (Object) k3, (Object) v3, (Object) k4, (Object) v4);
    }

    public static <K, V> ImmutableMultimap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return ImmutableListMultimap.of((Object) k, (Object) v, (Object) k2, (Object) v2, (Object) k3, (Object) v3, (Object) k4, (Object) v4, (Object) k5, (Object) v5);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    private static class BuilderMultimap<K, V> extends AbstractMultimap<K, V> {
        private static final long serialVersionUID = 0;

        BuilderMultimap() {
            super(new LinkedHashMap());
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection<V> createCollection() {
            return Lists.newArrayList();
        }
    }

    public static class Builder<K, V> {
        private final Multimap<K, V> builderMultimap = new BuilderMultimap();

        /* JADX WARN: Multi-variable type inference failed */
        public Builder<K, V> put(K k, V v) {
            this.builderMultimap.put(Preconditions.checkNotNull(k), Preconditions.checkNotNull(v));
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder<K, V> putAll(K k, Iterable<? extends V> iterable) {
            Collection collection = this.builderMultimap.get(Preconditions.checkNotNull(k));
            Iterator<? extends V> it = iterable.iterator();
            while (it.hasNext()) {
                collection.add(Preconditions.checkNotNull(it.next()));
            }
            return this;
        }

        public Builder<K, V> putAll(K k, V... vArr) {
            return putAll((Builder<K, V>) k, Arrays.asList(vArr));
        }

        public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
            for (Map.Entry<? extends K, Collection<? extends V>> entry : multimap.asMap().entrySet()) {
                putAll((Builder<K, V>) entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableMultimap<K, V> build() {
            return ImmutableMultimap.copyOf(this.builderMultimap);
        }
    }

    public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
        if (multimap instanceof ImmutableMultimap) {
            return (ImmutableMultimap) multimap;
        }
        return ImmutableListMultimap.copyOf((Multimap) multimap);
    }

    static class FieldSettersHolder {
        static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
        static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");

        FieldSettersHolder() {
        }
    }

    ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> immutableMap, int i) {
        this.map = immutableMap;
        this.size = i;
    }

    @Override // com.google.common.collect.Multimap
    public ImmutableCollection<V> removeAll(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public ImmutableCollection<V> replaceValues(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public boolean put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(K k, Iterable<? extends V> iterable) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public boolean remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsEntry(@Nullable Object obj, @Nullable Object obj2) {
        ImmutableCollection<V> immutableCollection = this.map.get(obj);
        return immutableCollection != null && immutableCollection.contains(obj2);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsKey(@Nullable Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // com.google.common.collect.Multimap
    public boolean containsValue(@Nullable Object obj) {
        Iterator it = this.map.values().iterator();
        while (it.hasNext()) {
            if (((ImmutableCollection) it.next()).contains(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.Multimap
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.Multimap, com.google.common.collect.ListMultimap
    public boolean equals(@Nullable Object obj) {
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

    @Override // com.google.common.collect.Multimap
    public ImmutableSet<K> keySet() {
        return this.map.keySet();
    }

    @Override // com.google.common.collect.Multimap
    public ImmutableMap<K, Collection<V>> asMap() {
        return this.map;
    }

    @Override // com.google.common.collect.Multimap
    public ImmutableCollection<Map.Entry<K, V>> entries() {
        ImmutableCollection<Map.Entry<K, V>> immutableCollection = this.entries;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        EntryCollection entryCollection = new EntryCollection(this);
        this.entries = entryCollection;
        return entryCollection;
    }

    private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap<K, V> multimap;

        EntryCollection(ImmutableMultimap<K, V> immutableMultimap) {
            this.multimap = immutableMultimap;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            final UnmodifiableIterator<Map.Entry<K, ? extends ImmutableCollection<V>>> it = this.multimap.map.entrySet().iterator();
            return new UnmodifiableIterator<Map.Entry<K, V>>() { // from class: com.google.common.collect.ImmutableMultimap.EntryCollection.1
                K key;
                Iterator<V> valueIterator;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return (this.key != null && this.valueIterator.hasNext()) || it.hasNext();
                }

                @Override // java.util.Iterator
                public Map.Entry<K, V> next() {
                    if (this.key == null || !this.valueIterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        this.key = (K) entry.getKey();
                        this.valueIterator = ((ImmutableCollection) entry.getValue()).iterator();
                    }
                    return Maps.immutableEntry(this.key, this.valueIterator.next());
                }
            };
        }

        @Override // java.util.Collection
        public int size() {
            return this.multimap.size();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
        }
    }

    @Override // com.google.common.collect.Multimap
    public ImmutableMultiset<K> keys() {
        ImmutableMultiset<K> immutableMultiset = this.keys;
        if (immutableMultiset != null) {
            return immutableMultiset;
        }
        ImmutableMultiset<K> immutableMultisetCreateKeys = createKeys();
        this.keys = immutableMultisetCreateKeys;
        return immutableMultisetCreateKeys;
    }

    private ImmutableMultiset<K> createKeys() {
        ImmutableMultiset.Builder builder = ImmutableMultiset.builder();
        Iterator it = this.map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            builder.addCopies(entry.getKey(), ((ImmutableCollection) entry.getValue()).size());
        }
        return builder.build();
    }

    @Override // com.google.common.collect.Multimap
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        Values values = new Values(this);
        this.values = values;
        return values;
    }

    private static class Values<V> extends ImmutableCollection<V> {
        private static final long serialVersionUID = 0;
        final Multimap<?, V> multimap;

        Values(Multimap<?, V> multimap) {
            this.multimap = multimap;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<V> iterator() {
            final Iterator<Map.Entry<?, V>> it = this.multimap.entries().iterator();
            return new UnmodifiableIterator<V>() { // from class: com.google.common.collect.ImmutableMultimap.Values.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override // java.util.Iterator
                public V next() {
                    return (V) ((Map.Entry) it.next()).getValue();
                }
            };
        }

        @Override // java.util.Collection
        public int size() {
            return this.multimap.size();
        }
    }
}
