package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Map;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    final transient ImmutableMap<K, V> delegate;
    final transient ImmutableBiMap<V, K> inverse;

    /* JADX WARN: Multi-variable type inference failed */
    RegularImmutableBiMap(ImmutableMap<K, V> immutableMap) {
        this.delegate = immutableMap;
        ImmutableMap.Builder builder = ImmutableMap.builder();
        Iterator it = immutableMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            builder.put(entry.getValue(), entry.getKey());
        }
        this.inverse = new RegularImmutableBiMap(builder.build(), this);
    }

    RegularImmutableBiMap(ImmutableMap<K, V> immutableMap, ImmutableBiMap<V, K> immutableBiMap) {
        this.delegate = immutableMap;
        this.inverse = immutableBiMap;
    }

    @Override // com.google.common.collect.ImmutableBiMap
    ImmutableMap<K, V> delegate() {
        return this.delegate;
    }

    @Override // com.google.common.collect.ImmutableBiMap, com.google.common.collect.BiMap
    public ImmutableBiMap<V, K> inverse() {
        return this.inverse;
    }
}
