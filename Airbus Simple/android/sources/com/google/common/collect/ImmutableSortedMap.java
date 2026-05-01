package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import javax.annotation.Nullable;
import voice.encoder.BuildConfig;

@GwtCompatible(serializable = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements SortedMap<K, V> {
    private static final long serialVersionUID = 0;
    private final transient Comparator<? super K> comparator;
    private final transient Map.Entry<K, V>[] entries;
    private transient ImmutableSet<Map.Entry<K, V>> entrySet;
    private final transient int fromIndex;
    private transient ImmutableSortedSet<K> keySet;
    private final transient int toIndex;
    private transient ImmutableCollection<V> values;
    private static final Comparator NATURAL_ORDER = Ordering.natural();
    private static final Map.Entry<?, ?>[] EMPTY_ARRAY = new Map.Entry[0];
    private static final ImmutableMap<Object, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(EMPTY_ARRAY, NATURAL_ORDER);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap headMap(Object obj) {
        return headMap((ImmutableSortedMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedMap
    public /* bridge */ /* synthetic */ SortedMap tailMap(Object obj) {
        return tailMap((ImmutableSortedMap<K, V>) obj);
    }

    public static <K, V> ImmutableSortedMap<K, V> of() {
        return (ImmutableSortedMap) NATURAL_EMPTY_MAP;
    }

    private static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
        if (NATURAL_ORDER.equals(comparator)) {
            return of();
        }
        return new ImmutableSortedMap<>(EMPTY_ARRAY, comparator);
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj) {
        return new ImmutableSortedMap(new Map.Entry[]{entryOf(comparable, obj)}, Ordering.natural());
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2) {
        return new Builder(Ordering.natural()).put((Builder) comparable, (Comparable) obj).put((Builder<K, V>) comparable2, (Comparable) obj2).build();
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2, Comparable comparable3, Object obj3) {
        return new Builder(Ordering.natural()).put((Builder) comparable, (Comparable) obj).put((Builder<K, V>) comparable2, (Comparable) obj2).put((Builder<K, V>) comparable3, (Comparable) obj3).build();
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2, Comparable comparable3, Object obj3, Comparable comparable4, Object obj4) {
        return new Builder(Ordering.natural()).put((Builder) comparable, (Comparable) obj).put((Builder<K, V>) comparable2, (Comparable) obj2).put((Builder<K, V>) comparable3, (Comparable) obj3).put((Builder<K, V>) comparable4, (Comparable) obj4).build();
    }

    /* JADX WARN: Incorrect types in method signature: <K::Ljava/lang/Comparable<-TK;>;V:Ljava/lang/Object;>(TK;TV;TK;TV;TK;TV;TK;TV;TK;TV;)Lcom/google/common/collect/ImmutableSortedMap<TK;TV;>; */
    public static ImmutableSortedMap of(Comparable comparable, Object obj, Comparable comparable2, Object obj2, Comparable comparable3, Object obj3, Comparable comparable4, Object obj4, Comparable comparable5, Object obj5) {
        return new Builder(Ordering.natural()).put((Builder) comparable, (Comparable) obj).put((Builder<K, V>) comparable2, (Comparable) obj2).put((Builder<K, V>) comparable3, (Comparable) obj3).put((Builder<K, V>) comparable4, (Comparable) obj4).put((Builder<K, V>) comparable5, (Comparable) obj5).build();
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        return copyOfInternal(map, Ordering.natural());
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        return copyOfInternal(map, (Comparator) Preconditions.checkNotNull(comparator));
    }

    public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> sortedMap) {
        return copyOfInternal(sortedMap, sortedMap.comparator() == null ? NATURAL_ORDER : sortedMap.comparator());
    }

    private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
        boolean zEquals = false;
        if (map instanceof SortedMap) {
            Comparator<? super K> comparator2 = ((SortedMap) map).comparator();
            if (comparator2 != null) {
                zEquals = comparator.equals(comparator2);
            } else if (comparator == NATURAL_ORDER) {
                zEquals = true;
            }
        }
        if (zEquals && (map instanceof ImmutableSortedMap)) {
            return (ImmutableSortedMap) map;
        }
        ArrayList arrayListNewArrayListWithCapacity = Lists.newArrayListWithCapacity(map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            arrayListNewArrayListWithCapacity.add(entryOf(entry.getKey(), entry.getValue()));
        }
        Map.Entry[] entryArr = (Map.Entry[]) arrayListNewArrayListWithCapacity.toArray(new Map.Entry[arrayListNewArrayListWithCapacity.size()]);
        if (!zEquals) {
            sortEntries(entryArr, comparator);
            validateEntries(entryArr, comparator);
        }
        return new ImmutableSortedMap<>(entryArr, comparator);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sortEntries(Map.Entry<?, ?>[] entryArr, final Comparator<?> comparator) {
        Arrays.sort(entryArr, new Comparator<Map.Entry<?, ?>>() { // from class: com.google.common.collect.ImmutableSortedMap.1
            @Override // java.util.Comparator
            public int compare(Map.Entry<?, ?> entry, Map.Entry<?, ?> entry2) {
                return ImmutableSortedSet.unsafeCompare(comparator, entry.getKey(), entry2.getKey());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateEntries(Map.Entry<?, ?>[] entryArr, Comparator<?> comparator) {
        for (int i = 1; i < entryArr.length; i++) {
            int i2 = i - 1;
            if (ImmutableSortedSet.unsafeCompare(comparator, entryArr[i2].getKey(), entryArr[i].getKey()) == 0) {
                throw new IllegalArgumentException("Duplicate keys in mappings " + entryArr[i2] + " and " + entryArr[i]);
            }
        }
    }

    public static <K extends Comparable<K>, V> Builder<K, V> naturalOrder() {
        return new Builder<>(Ordering.natural());
    }

    public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
        return new Builder<>(comparator);
    }

    public static <K extends Comparable<K>, V> Builder<K, V> reverseOrder() {
        return new Builder<>(Ordering.natural().reverse());
    }

    public static final class Builder<K, V> extends ImmutableMap.Builder<K, V> {
        private final Comparator<? super K> comparator;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.ImmutableMap.Builder
        public /* bridge */ /* synthetic */ ImmutableMap.Builder put(Object obj, Object obj2) {
            return put((Builder<K, V>) obj, obj2);
        }

        public Builder(Comparator<? super K> comparator) {
            this.comparator = (Comparator) Preconditions.checkNotNull(comparator);
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> put(K k, V v) {
            this.entries.add(ImmutableMap.entryOf(k, v));
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put((Builder<K, V>) entry.getKey(), (K) entry.getValue());
            }
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public ImmutableSortedMap<K, V> build() {
            Map.Entry[] entryArr = (Map.Entry[]) this.entries.toArray(new Map.Entry[this.entries.size()]);
            ImmutableSortedMap.sortEntries(entryArr, this.comparator);
            ImmutableSortedMap.validateEntries(entryArr, this.comparator);
            return new ImmutableSortedMap<>(entryArr, this.comparator);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private ImmutableSortedMap(Map.Entry<?, ?>[] entryArr, Comparator<? super K> comparator, int i, int i2) {
        this.entries = entryArr;
        this.comparator = comparator;
        this.fromIndex = i;
        this.toIndex = i2;
    }

    ImmutableSortedMap(Map.Entry<?, ?>[] entryArr, Comparator<? super K> comparator) {
        this(entryArr, comparator, 0, entryArr.length);
    }

    @Override // java.util.Map
    public int size() {
        return this.toIndex - this.fromIndex;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            int iBinarySearch = binarySearch(obj);
            if (iBinarySearch >= 0) {
                return this.entries[iBinarySearch].getValue();
            }
            return null;
        } catch (ClassCastException unused) {
            return null;
        }
    }

    private int binarySearch(Object obj) {
        int i = this.fromIndex;
        int i2 = this.toIndex - 1;
        while (i <= i2) {
            int i3 = ((i2 - i) / 2) + i;
            int iUnsafeCompare = ImmutableSortedSet.unsafeCompare(this.comparator, obj, this.entries[i3].getKey());
            if (iUnsafeCompare < 0) {
                i2 = i3 - 1;
            } else {
                if (iUnsafeCompare <= 0) {
                    return i3;
                }
                i = i3 + 1;
            }
        }
        return (-i) - 1;
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public boolean containsValue(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        for (int i = this.fromIndex; i < this.toIndex; i++) {
            if (this.entries[i].getValue().equals(obj)) {
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
        ImmutableSet<Map.Entry<K, V>> immutableSetCreateEntrySet = createEntrySet();
        this.entrySet = immutableSetCreateEntrySet;
        return immutableSetCreateEntrySet;
    }

    private ImmutableSet<Map.Entry<K, V>> createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new EntrySet(this);
    }

    private static class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
        final transient ImmutableSortedMap<K, V> map;

        EntrySet(ImmutableSortedMap<K, V> immutableSortedMap) {
            this.map = immutableSortedMap;
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            return this.map.size();
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
            return Iterators.forArray(((ImmutableSortedMap) this.map).entries, ((ImmutableSortedMap) this.map).fromIndex, size());
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

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new EntrySetSerializedForm(this.map);
        }
    }

    private static class EntrySetSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableSortedMap<K, V> map;

        EntrySetSerializedForm(ImmutableSortedMap<K, V> immutableSortedMap) {
            this.map = immutableSortedMap;
        }

        Object readResolve() {
            return this.map.entrySet();
        }
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSortedSet<K> keySet() {
        ImmutableSortedSet<K> immutableSortedSet = this.keySet;
        if (immutableSortedSet != null) {
            return immutableSortedSet;
        }
        ImmutableSortedSet<K> immutableSortedSetCreateKeySet = createKeySet();
        this.keySet = immutableSortedSetCreateKeySet;
        return immutableSortedSetCreateKeySet;
    }

    private ImmutableSortedSet<K> createKeySet() {
        if (isEmpty()) {
            return ImmutableSortedSet.emptySet(this.comparator);
        }
        Object[] objArr = new Object[size()];
        for (int i = this.fromIndex; i < this.toIndex; i++) {
            objArr[i - this.fromIndex] = this.entries[i].getKey();
        }
        return new RegularImmutableSortedSet(objArr, this.comparator);
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
        private final ImmutableSortedMap<?, V> map;

        Values(ImmutableSortedMap<?, V> immutableSortedMap) {
            this.map = immutableSortedMap;
        }

        @Override // java.util.Collection
        public int size() {
            return this.map.size();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public UnmodifiableIterator<V> iterator() {
            return new AbstractIterator<V>() { // from class: com.google.common.collect.ImmutableSortedMap.Values.1
                int index;

                {
                    this.index = Values.this.map.fromIndex;
                }

                @Override // com.google.common.collect.AbstractIterator
                protected V computeNext() {
                    if (this.index >= Values.this.map.toIndex) {
                        return endOfData();
                    }
                    Map.Entry[] entryArr = Values.this.map.entries;
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

        @Override // com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return new ValuesSerializedForm(this.map);
        }
    }

    private static class ValuesSerializedForm<V> implements Serializable {
        private static final long serialVersionUID = 0;
        final ImmutableSortedMap<?, V> map;

        ValuesSerializedForm(ImmutableSortedMap<?, V> immutableSortedMap) {
            this.map = immutableSortedMap;
        }

        Object readResolve() {
            return this.map.values();
        }
    }

    @Override // java.util.SortedMap
    public Comparator<? super K> comparator() {
        return this.comparator;
    }

    @Override // java.util.SortedMap
    public K firstKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.entries[this.fromIndex].getKey();
    }

    @Override // java.util.SortedMap
    public K lastKey() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.entries[this.toIndex - 1].getKey();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedMap
    public ImmutableSortedMap<K, V> headMap(K k) {
        return createSubmap(this.fromIndex, findSubmapIndex(Preconditions.checkNotNull(k)));
    }

    @Override // java.util.SortedMap
    public ImmutableSortedMap<K, V> subMap(K k, K k2) {
        Preconditions.checkNotNull(k);
        Preconditions.checkNotNull(k2);
        Preconditions.checkArgument(this.comparator.compare(k, k2) <= 0);
        return createSubmap(findSubmapIndex(k), findSubmapIndex(k2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.SortedMap
    public ImmutableSortedMap<K, V> tailMap(K k) {
        return createSubmap(findSubmapIndex(Preconditions.checkNotNull(k)), this.toIndex);
    }

    private int findSubmapIndex(K k) {
        int iBinarySearch = binarySearch(k);
        return iBinarySearch >= 0 ? iBinarySearch : (-iBinarySearch) - 1;
    }

    private ImmutableSortedMap<K, V> createSubmap(int i, int i2) {
        if (i < i2) {
            return new ImmutableSortedMap<>(this.entries, this.comparator, i, i2);
        }
        return emptyMap(this.comparator);
    }

    private static class SerializedForm extends ImmutableMap.SerializedForm {
        private static final long serialVersionUID = 0;
        private final Comparator<Object> comparator;

        SerializedForm(ImmutableSortedMap<?, ?> immutableSortedMap) {
            super(immutableSortedMap);
            this.comparator = immutableSortedMap.comparator();
        }

        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        Object readResolve() {
            return createMap(new Builder(this.comparator));
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new SerializedForm(this);
    }
}
