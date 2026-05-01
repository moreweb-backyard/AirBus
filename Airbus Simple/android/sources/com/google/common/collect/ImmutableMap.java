package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    @Override // java.util.Map
    public abstract boolean containsValue(@Nullable Object obj);

    @Override // java.util.Map
    public abstract ImmutableSet<Map.Entry<K, V>> entrySet();

    @Override // java.util.Map
    public abstract V get(@Nullable Object obj);

    @Override // java.util.Map
    public abstract ImmutableSet<K> keySet();

    @Override // java.util.Map
    public abstract ImmutableCollection<V> values();

    public static <K, V> ImmutableMap<K, V> of() {
        return EmptyImmutableMap.INSTANCE;
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v) {
        return new SingletonImmutableMap(Preconditions.checkNotNull(k), Preconditions.checkNotNull(v));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }

    public static <K, V> ImmutableMap<K, V> of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return new RegularImmutableMap(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    static <K, V> Map.Entry<K, V> entryOf(K k, V v) {
        return Maps.immutableEntry(Preconditions.checkNotNull(k), Preconditions.checkNotNull(v));
    }

    public static class Builder<K, V> {
        final List<Map.Entry<K, V>> entries = Lists.newArrayList();

        public Builder<K, V> put(K k, V v) {
            this.entries.add(ImmutableMap.entryOf(k, v));
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableMap<K, V> build() {
            return fromEntryList(this.entries);
        }

        private static <K, V> ImmutableMap<K, V> fromEntryList(List<Map.Entry<K, V>> list) {
            switch (list.size()) {
                case 0:
                    return ImmutableMap.of();
                case 1:
                    return new SingletonImmutableMap((Map.Entry) Iterables.getOnlyElement(list));
                default:
                    return new RegularImmutableMap((Map.Entry[]) list.toArray(new Map.Entry[list.size()]));
            }
        }
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
            return (ImmutableMap) map;
        }
        Map.Entry[] entryArr = (Map.Entry[]) map.entrySet().toArray(new Map.Entry[0]);
        switch (entryArr.length) {
            case 0:
                return of();
            case 1:
                return new SingletonImmutableMap(entryOf(entryArr[0].getKey(), entryArr[0].getValue()));
        }
        for (int i = 0; i < entryArr.length; i++) {
            entryArr[i] = entryOf(entryArr[i].getKey(), entryArr[i].getValue());
        }
        return new RegularImmutableMap(entryArr);
    }

    ImmutableMap() {
    }

    @Override // java.util.Map
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public final V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public boolean containsKey(@Nullable Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.Map
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map) {
            return entrySet().equals(((Map) obj).entrySet());
        }
        return false;
    }

    @Override // java.util.Map
    public int hashCode() {
        return entrySet().hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(size() * 16);
        sb.append('{');
        Maps.standardJoiner.appendTo(sb, (Map<?, ?>) this);
        sb.append('}');
        return sb.toString();
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final Object[] keys;
        private final Object[] values;

        SerializedForm(ImmutableMap<?, ?> immutableMap) {
            this.keys = new Object[immutableMap.size()];
            this.values = new Object[immutableMap.size()];
            Iterator it = immutableMap.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                i++;
            }
        }

        Object readResolve() {
            return createMap(new Builder<>());
        }

        Object createMap(Builder<Object, Object> builder) {
            for (int i = 0; i < this.keys.length; i++) {
                builder.put(this.keys[i], this.values[i]);
            }
            return builder.build();
        }
    }

    Object writeReplace() {
        return new SerializedForm(this);
    }
}
