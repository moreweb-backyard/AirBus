package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final long serialVersionUID = 0;
    private final transient Map.Entry<K, V>[] entries;
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    private transient ImmutableSet<K> keySet;
    private final transient int keySetHashCode;
    private final transient int mask;
    private final transient Object[] table;
    private transient ImmutableCollection<V> values;

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean isEmpty() {
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0041, code lost:
    
        r3 = com.google.common.base.Preconditions.checkNotNull(r3.getValue());
        r9.table[r7] = r4;
        r9.table[r7 + 1] = r3;
        r2 = r2 + r5;
        r1 = r1 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    RegularImmutableMap(java.util.Map.Entry<?, ?>... r10) {
        /*
            r9 = this;
            r9.<init>()
            java.lang.Object r0 = com.google.common.base.Preconditions.checkNotNull(r10)
            java.util.Map$Entry[] r0 = (java.util.Map.Entry[]) r0
            r9.entries = r0
            int r10 = r10.length
            int r10 = com.google.common.collect.Hashing.chooseTableSize(r10)
            int r0 = r10 * 2
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r9.table = r0
            int r10 = r10 + (-1)
            r9.mask = r10
            java.util.Map$Entry<K, V>[] r10 = r9.entries
            int r0 = r10.length
            r1 = 0
            r2 = 0
        L1f:
            if (r1 >= r0) goto L77
            r3 = r10[r1]
            com.google.common.base.Preconditions.checkNotNull(r3)
            java.lang.Object r4 = r3.getKey()
            java.lang.Object r4 = com.google.common.base.Preconditions.checkNotNull(r4)
            int r5 = r4.hashCode()
            int r6 = com.google.common.collect.Hashing.smear(r5)
        L36:
            int r7 = r9.mask
            r7 = r7 & r6
            int r7 = r7 * 2
            java.lang.Object[] r8 = r9.table
            r8 = r8[r7]
            if (r8 != 0) goto L57
            java.lang.Object r3 = r3.getValue()
            java.lang.Object r3 = com.google.common.base.Preconditions.checkNotNull(r3)
            java.lang.Object[] r6 = r9.table
            r6[r7] = r4
            java.lang.Object[] r4 = r9.table
            int r7 = r7 + 1
            r4[r7] = r3
            int r2 = r2 + r5
            int r1 = r1 + 1
            goto L1f
        L57:
            boolean r7 = r8.equals(r4)
            if (r7 != 0) goto L60
            int r6 = r6 + 1
            goto L36
        L60:
            java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "duplicate key: "
            r0.append(r1)
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            r10.<init>(r0)
            throw r10
        L77:
            r9.keySetHashCode = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.RegularImmutableMap.<init>(java.util.Map$Entry[]):void");
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(Object obj) {
        if (obj == null) {
            return null;
        }
        int iSmear = Hashing.smear(obj.hashCode());
        while (true) {
            int i = (this.mask & iSmear) * 2;
            Object obj2 = this.table[i];
            if (obj2 == null) {
                return null;
            }
            if (obj2.equals(obj)) {
                return (V) this.table[i + 1];
            }
            iSmear++;
        }
    }

    @Override // java.util.Map
    public int size() {
        return this.entries.length;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean containsValue(Object obj) {
        if (obj == null) {
            return false;
        }
        for (Map.Entry<K, V> entry : this.entries) {
            if (entry.getValue().equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        EntrySet entrySet = new EntrySet(this);
        this.entrySet = entrySet;
        return entrySet;
    }

    private static class EntrySet<K, V> extends ImmutableSet.ArrayImmutableSet<Map.Entry<K, V>> {
        final transient RegularImmutableMap<K, V> map;

        EntrySet(RegularImmutableMap<K, V> regularImmutableMap) {
            super(((RegularImmutableMap) regularImmutableMap).entries);
            this.map = regularImmutableMap;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            V v = this.map.get(entry.getKey());
            return v != null && v.equals(entry.getValue());
        }
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        KeySet keySet = new KeySet(this);
        this.keySet = keySet;
        return keySet;
    }

    private static class KeySet<K, V> extends ImmutableSet.TransformedImmutableSet<Map.Entry<K, V>, K> {
        final RegularImmutableMap<K, V> map;

        KeySet(RegularImmutableMap<K, V> regularImmutableMap) {
            super(((RegularImmutableMap) regularImmutableMap).entries, ((RegularImmutableMap) regularImmutableMap).keySetHashCode);
            this.map = regularImmutableMap;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableSet.TransformedImmutableSet
        public K transform(Map.Entry<K, V> entry) {
            return entry.getKey();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean contains(Object obj) {
            return this.map.containsKey(obj);
        }
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
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
        final RegularImmutableMap<?, V> map;

        Values(RegularImmutableMap<?, V> regularImmutableMap) {
            this.map = regularImmutableMap;
        }

        @Override // java.util.Collection
        public int size() {
            return ((RegularImmutableMap) this.map).entries.length;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<V> iterator() {
            return new AbstractIterator<V>() { // from class: com.google.common.collect.RegularImmutableMap.Values.1
                int index = 0;

                @Override // com.google.common.collect.AbstractIterator
                protected V computeNext() {
                    if (this.index >= ((RegularImmutableMap) Values.this.map).entries.length) {
                        return endOfData();
                    }
                    Map.Entry[] entryArr = ((RegularImmutableMap) Values.this.map).entries;
                    int i = this.index;
                    this.index = i + 1;
                    return (V) entryArr[i].getValue();
                }
            };
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.util.List
        public boolean contains(Object obj) {
            return this.map.containsValue(obj);
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    public String toString() {
        StringBuilder sb = new StringBuilder(size() * 16);
        sb.append('{');
        Collections2.standardJoiner.appendTo(sb, (Object[]) this.entries);
        sb.append('}');
        return sb.toString();
    }
}
