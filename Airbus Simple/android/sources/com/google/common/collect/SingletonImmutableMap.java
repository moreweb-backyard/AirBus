package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class SingletonImmutableMap<K, V> extends ImmutableMap<K, V> {
    private transient Map.Entry<K, V> entry;
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    final transient K singleKey;
    final transient V singleValue;
    private transient ImmutableCollection<V> values;

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean isEmpty() {
        return false;
    }

    @Override // java.util.Map
    public int size() {
        return 1;
    }

    SingletonImmutableMap(K k, V v) {
        this.singleKey = k;
        this.singleValue = v;
    }

    SingletonImmutableMap(Map.Entry<K, V> entry) {
        this.entry = (Map.Entry) Preconditions.checkNotNull(entry);
        this.singleKey = (K) Preconditions.checkNotNull(entry.getKey());
        this.singleValue = (V) Preconditions.checkNotNull(entry.getValue());
    }

    private Map.Entry<K, V> entry() {
        Map.Entry<K, V> entry = this.entry;
        if (entry != null) {
            return entry;
        }
        Map.Entry<K, V> entryImmutableEntry = Maps.immutableEntry(this.singleKey, this.singleValue);
        this.entry = entryImmutableEntry;
        return entryImmutableEntry;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(Object obj) {
        if (this.singleKey.equals(obj)) {
            return this.singleValue;
        }
        return null;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean containsKey(Object obj) {
        return this.singleKey.equals(obj);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean containsValue(Object obj) {
        return this.singleValue.equals(obj);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<Map.Entry<K, V>> immutableSetOf = ImmutableSet.of(entry());
        this.entrySet = immutableSetOf;
        return immutableSetOf;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<K> immutableSetOf = ImmutableSet.of(this.singleKey);
        this.keySet = immutableSetOf;
        return immutableSetOf;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        Values values = new Values(this.singleValue);
        this.values = values;
        return values;
    }

    private static class Values<V> extends ImmutableCollection<V> {
        final V singleValue;

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean isEmpty() {
            return false;
        }

        @Override // java.util.Collection
        public int size() {
            return 1;
        }

        Values(V v) {
            this.singleValue = v;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean contains(Object obj) {
            return this.singleValue.equals(obj);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<V> iterator() {
            return Iterators.singletonIterator(this.singleValue);
        }
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        if (map.size() != 1) {
            return false;
        }
        Map.Entry<K, V> next = map.entrySet().iterator().next();
        return this.singleKey.equals(next.getKey()) && this.singleValue.equals(next.getValue());
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public int hashCode() {
        return this.singleKey.hashCode() ^ this.singleValue.hashCode();
    }

    @Override // com.google.common.collect.ImmutableMap
    public String toString() {
        return '{' + this.singleKey.toString() + '=' + this.singleValue.toString() + '}';
    }
}
