package com.google.common.collect;

import android.support.v7.widget.ActivityChooserView;
import com.google.common.base.Function;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class CustomConcurrentHashMap {

    public interface ComputingStrategy<K, V, E> extends Strategy<K, V, E> {
        V compute(K k, E e, Function<? super K, ? extends V> function);

        V waitForValue(E e) throws InterruptedException;
    }

    public interface Internals<K, V, E> {
        E getEntry(K k);

        boolean removeEntry(E e);

        boolean removeEntry(E e, @Nullable V v);
    }

    public interface Strategy<K, V, E> {
        E copyEntry(K k, E e, E e2);

        boolean equalKeys(K k, Object obj);

        boolean equalValues(V v, Object obj);

        int getHash(E e);

        K getKey(E e);

        E getNext(E e);

        V getValue(E e);

        int hashKey(Object obj);

        E newEntry(K k, int i, E e);

        void setInternals(Internals<K, V, E> internals);

        void setValue(E e, V v);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int rehash(int i) {
        int i2 = i + ((i << 15) ^ (-12931));
        int i3 = i2 ^ (i2 >>> 10);
        int i4 = i3 + (i3 << 3);
        int i5 = i4 ^ (i4 >>> 6);
        int i6 = i5 + (i5 << 2) + (i5 << 14);
        return i6 ^ (i6 >>> 16);
    }

    private CustomConcurrentHashMap() {
    }

    static final class Builder {
        private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
        private static final int DEFAULT_INITIAL_CAPACITY = 16;
        private static final int UNSET_CONCURRENCY_LEVEL = -1;
        private static final int UNSET_INITIAL_CAPACITY = -1;
        int initialCapacity = -1;
        int concurrencyLevel = -1;

        Builder() {
        }

        public Builder initialCapacity(int i) {
            if (this.initialCapacity == -1) {
                if (i < 0) {
                    throw new IllegalArgumentException();
                }
                this.initialCapacity = i;
                return this;
            }
            throw new IllegalStateException("initial capacity was already set to " + this.initialCapacity);
        }

        public Builder concurrencyLevel(int i) {
            if (this.concurrencyLevel == -1) {
                if (i <= 0) {
                    throw new IllegalArgumentException();
                }
                this.concurrencyLevel = i;
                return this;
            }
            throw new IllegalStateException("concurrency level was already set to " + this.concurrencyLevel);
        }

        public <K, V, E> ConcurrentMap<K, V> buildMap(Strategy<K, V, E> strategy) {
            if (strategy == null) {
                throw new NullPointerException("strategy");
            }
            return new Impl(strategy, this);
        }

        public <K, V, E> ConcurrentMap<K, V> buildComputingMap(ComputingStrategy<K, V, E> computingStrategy, Function<? super K, ? extends V> function) {
            if (computingStrategy == null) {
                throw new NullPointerException("strategy");
            }
            if (function == null) {
                throw new NullPointerException("computer");
            }
            return new ComputingImpl(computingStrategy, this, function);
        }

        int getInitialCapacity() {
            if (this.initialCapacity == -1) {
                return 16;
            }
            return this.initialCapacity;
        }

        int getConcurrencyLevel() {
            if (this.concurrencyLevel == -1) {
                return 16;
            }
            return this.concurrencyLevel;
        }
    }

    static class Impl<K, V, E> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, Serializable {
        static final int MAXIMUM_CAPACITY = 1073741824;
        static final int MAX_SEGMENTS = 65536;
        static final int RETRIES_BEFORE_LOCK = 2;
        private static final long serialVersionUID = 1;
        Set<Map.Entry<K, V>> entrySet;
        Set<K> keySet;
        final int segmentMask;
        final int segmentShift;
        final Impl<K, V, E>.Segment[] segments;
        final Strategy<K, V, E> strategy;
        Collection<V> values;

        Impl(Strategy<K, V, E> strategy, Builder builder) {
            int concurrencyLevel = builder.getConcurrencyLevel();
            int initialCapacity = builder.getInitialCapacity();
            int i = 1;
            int i2 = 1;
            int i3 = 0;
            while (i2 < (concurrencyLevel > 65536 ? 65536 : concurrencyLevel)) {
                i3++;
                i2 <<= 1;
            }
            this.segmentShift = 32 - i3;
            this.segmentMask = i2 - 1;
            this.segments = newSegmentArray(i2);
            initialCapacity = initialCapacity > MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : initialCapacity;
            int i4 = initialCapacity / i2;
            while (i < (i2 * i4 < initialCapacity ? i4 + 1 : i4)) {
                i <<= 1;
            }
            for (int i5 = 0; i5 < this.segments.length; i5++) {
                this.segments[i5] = new Segment(i);
            }
            this.strategy = strategy;
            strategy.setInternals(new InternalsImpl());
        }

        int hash(Object obj) {
            return CustomConcurrentHashMap.rehash(this.strategy.hashKey(obj));
        }

        class InternalsImpl implements Internals<K, V, E>, Serializable {
            static final long serialVersionUID = 0;

            InternalsImpl() {
            }

            @Override // com.google.common.collect.CustomConcurrentHashMap.Internals
            public E getEntry(K k) {
                if (k == null) {
                    throw new NullPointerException("key");
                }
                int iHash = Impl.this.hash(k);
                return Impl.this.segmentFor(iHash).getEntry(k, iHash);
            }

            @Override // com.google.common.collect.CustomConcurrentHashMap.Internals
            public boolean removeEntry(E e, V v) {
                if (e == null) {
                    throw new NullPointerException("entry");
                }
                int hash = Impl.this.strategy.getHash(e);
                return Impl.this.segmentFor(hash).removeEntry(e, hash, v);
            }

            @Override // com.google.common.collect.CustomConcurrentHashMap.Internals
            public boolean removeEntry(E e) {
                if (e == null) {
                    throw new NullPointerException("entry");
                }
                int hash = Impl.this.strategy.getHash(e);
                return Impl.this.segmentFor(hash).removeEntry(e, hash);
            }
        }

        Impl<K, V, E>.Segment[] newSegmentArray(int i) {
            return (Segment[]) Array.newInstance((Class<?>) Segment.class, i);
        }

        Impl<K, V, E>.Segment segmentFor(int i) {
            return this.segments[(i >>> this.segmentShift) & this.segmentMask];
        }

        final class Segment extends ReentrantLock {
            volatile int count;
            int modCount;
            volatile AtomicReferenceArray<E> table;
            int threshold;

            Segment(int i) {
                setTable(newEntryArray(i));
            }

            AtomicReferenceArray<E> newEntryArray(int i) {
                return new AtomicReferenceArray<>(i);
            }

            void setTable(AtomicReferenceArray<E> atomicReferenceArray) {
                this.threshold = (atomicReferenceArray.length() * 3) / 4;
                this.table = atomicReferenceArray;
            }

            E getFirst(int i) {
                return this.table.get(i & (r0.length() - 1));
            }

            public E getEntry(Object obj, int i) {
                K key;
                Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count == 0) {
                    return null;
                }
                for (E next = (E) getFirst(i); next != null; next = strategy.getNext(next)) {
                    if (strategy.getHash(next) == i && (key = strategy.getKey(next)) != null && strategy.equalKeys(key, obj)) {
                        return next;
                    }
                }
                return null;
            }

            /* JADX WARN: Multi-variable type inference failed */
            V get(Object obj, int i) {
                Object entry = getEntry(obj, i);
                if (entry == null) {
                    return null;
                }
                return (V) Impl.this.strategy.getValue(entry);
            }

            /* JADX WARN: Multi-variable type inference failed */
            boolean containsKey(Object obj, int i) {
                Object key;
                Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count != 0) {
                    for (Object first = getFirst(i); first != null; first = strategy.getNext(first)) {
                        if (strategy.getHash(first) == i && (key = strategy.getKey(first)) != null && strategy.equalKeys(key, obj)) {
                            return strategy.getValue(first) != null;
                        }
                    }
                }
                return false;
            }

            boolean containsValue(Object obj) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.count != 0) {
                    AtomicReferenceArray<E> atomicReferenceArray = this.table;
                    int length = atomicReferenceArray.length();
                    for (int i = 0; i < length; i++) {
                        for (E next = atomicReferenceArray.get(i); next; next = strategy.getNext(next)) {
                            V value = strategy.getValue(next);
                            if (value != null && strategy.equalValues(value, obj)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            /* JADX WARN: Multi-variable type inference failed */
            boolean replace(K k, int i, V v, V v2) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    for (Object first = getFirst(i); first != null; first = strategy.getNext(first)) {
                        Object key = strategy.getKey(first);
                        if (strategy.getHash(first) == i && key != null && strategy.equalKeys(k, key)) {
                            Object value = strategy.getValue(first);
                            if (value == null) {
                                return false;
                            }
                            if (strategy.equalValues(value, v)) {
                                strategy.setValue(first, v2);
                                return true;
                            }
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            V replace(K k, int i, V v) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    for (Object first = getFirst(i); first != null; first = strategy.getNext(first)) {
                        Object key = strategy.getKey(first);
                        if (strategy.getHash(first) == i && key != null && strategy.equalKeys(k, key)) {
                            V v2 = (V) strategy.getValue(first);
                            if (v2 == null) {
                                return null;
                            }
                            strategy.setValue(first, v);
                            return v2;
                        }
                    }
                    return null;
                } finally {
                    unlock();
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            V put(K k, int i, V v, boolean z) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count;
                    int i3 = i2 + 1;
                    if (i2 > this.threshold) {
                        expand();
                    }
                    AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) this.table;
                    int length = (atomicReferenceArray.length() - 1) & i;
                    Object obj = atomicReferenceArray.get(length);
                    for (Object next = obj; next != null; next = strategy.getNext(next)) {
                        Object key = strategy.getKey(next);
                        if (strategy.getHash(next) == i && key != null && strategy.equalKeys(k, key)) {
                            V v2 = (V) strategy.getValue(next);
                            if (z && v2 != null) {
                                return v2;
                            }
                            strategy.setValue(next, v);
                            return v2;
                        }
                    }
                    this.modCount++;
                    E eNewEntry = strategy.newEntry(k, i, obj);
                    strategy.setValue(eNewEntry, v);
                    atomicReferenceArray.set(length, eNewEntry);
                    this.count = i3;
                    return null;
                } finally {
                    unlock();
                }
            }

            void expand() {
                AtomicReferenceArray<E> atomicReferenceArray = this.table;
                int length = atomicReferenceArray.length();
                if (length >= Impl.MAXIMUM_CAPACITY) {
                    return;
                }
                Strategy<K, V, E> strategy = Impl.this.strategy;
                AtomicReferenceArray<E> atomicReferenceArrayNewEntryArray = newEntryArray(length << 1);
                this.threshold = (atomicReferenceArrayNewEntryArray.length() * 3) / 4;
                int length2 = atomicReferenceArrayNewEntryArray.length() - 1;
                for (int i = 0; i < length; i++) {
                    E next = atomicReferenceArray.get(i);
                    if (next != null) {
                        E next2 = strategy.getNext(next);
                        int hash = strategy.getHash(next) & length2;
                        if (next2 == null) {
                            atomicReferenceArrayNewEntryArray.set(hash, next);
                        } else {
                            E e = next;
                            while (next2 != null) {
                                int hash2 = strategy.getHash(next2) & length2;
                                if (hash2 != hash) {
                                    e = next2;
                                    hash = hash2;
                                }
                                next2 = strategy.getNext(next2);
                            }
                            atomicReferenceArrayNewEntryArray.set(hash, e);
                            while (next != e) {
                                K key = strategy.getKey(next);
                                if (key != null) {
                                    int hash3 = strategy.getHash(next) & length2;
                                    atomicReferenceArrayNewEntryArray.set(hash3, strategy.copyEntry(key, next, atomicReferenceArrayNewEntryArray.get(hash3)));
                                }
                                next = strategy.getNext(next);
                            }
                        }
                    }
                }
                this.table = atomicReferenceArrayNewEntryArray;
            }

            /* JADX WARN: Multi-variable type inference failed */
            V remove(Object obj, int i) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) this.table;
                    int length = (atomicReferenceArray.length() - 1) & i;
                    Object next = atomicReferenceArray.get(length);
                    for (Object next2 = next; next2 != null; next2 = strategy.getNext(next2)) {
                        Object key = strategy.getKey(next2);
                        if (strategy.getHash(next2) == i && key != null && strategy.equalKeys(key, obj)) {
                            V v = (V) Impl.this.strategy.getValue(next2);
                            this.modCount++;
                            E next3 = strategy.getNext(next2);
                            while (next != next2) {
                                Object key2 = strategy.getKey(next);
                                if (key2 != null) {
                                    next3 = strategy.copyEntry(key2, next, next3);
                                }
                                next = strategy.getNext(next);
                            }
                            atomicReferenceArray.set(length, next3);
                            this.count = i2;
                            return v;
                        }
                    }
                    return null;
                } finally {
                    unlock();
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            boolean remove(Object obj, int i, Object obj2) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) this.table;
                    int length = (atomicReferenceArray.length() - 1) & i;
                    Object next = atomicReferenceArray.get(length);
                    for (Object next2 = next; next2 != null; next2 = strategy.getNext(next2)) {
                        Object key = strategy.getKey(next2);
                        if (strategy.getHash(next2) == i && key != null && strategy.equalKeys(key, obj)) {
                            Object value = Impl.this.strategy.getValue(next2);
                            if (obj2 != value && (obj2 == null || value == null || !strategy.equalValues(value, obj2))) {
                                return false;
                            }
                            this.modCount++;
                            E next3 = strategy.getNext(next2);
                            while (next != next2) {
                                Object key2 = strategy.getKey(next);
                                if (key2 != null) {
                                    next3 = strategy.copyEntry(key2, next, next3);
                                }
                                next = strategy.getNext(next);
                            }
                            atomicReferenceArray.set(length, next3);
                            this.count = i2;
                            return true;
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            public boolean removeEntry(E e, int i, V v) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) this.table;
                    int length = (atomicReferenceArray.length() - 1) & i;
                    Object next = atomicReferenceArray.get(length);
                    for (Object next2 = next; next2 != null; next2 = strategy.getNext(next2)) {
                        if (strategy.getHash(next2) == i && e.equals(next2)) {
                            Object value = strategy.getValue(next2);
                            if (value != v && (v == null || !strategy.equalValues(value, v))) {
                                return false;
                            }
                            this.modCount++;
                            E next3 = strategy.getNext(next2);
                            while (next != next2) {
                                Object key = strategy.getKey(next);
                                if (key != null) {
                                    next3 = strategy.copyEntry(key, next, next3);
                                }
                                next = strategy.getNext(next);
                            }
                            atomicReferenceArray.set(length, next3);
                            this.count = i2;
                            return true;
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            public boolean removeEntry(E e, int i) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                lock();
                try {
                    int i2 = this.count - 1;
                    AtomicReferenceArray atomicReferenceArray = (AtomicReferenceArray<E>) this.table;
                    int length = (atomicReferenceArray.length() - 1) & i;
                    Object next = atomicReferenceArray.get(length);
                    for (Object next2 = next; next2 != null; next2 = strategy.getNext(next2)) {
                        if (strategy.getHash(next2) == i && e.equals(next2)) {
                            this.modCount++;
                            E next3 = strategy.getNext(next2);
                            while (next != next2) {
                                Object key = strategy.getKey(next);
                                if (key != null) {
                                    next3 = strategy.copyEntry(key, next, next3);
                                }
                                next = strategy.getNext(next);
                            }
                            atomicReferenceArray.set(length, next3);
                            this.count = i2;
                            return true;
                        }
                    }
                    return false;
                } finally {
                    unlock();
                }
            }

            void clear() {
                if (this.count != 0) {
                    lock();
                    try {
                        AtomicReferenceArray<E> atomicReferenceArray = this.table;
                        for (int i = 0; i < atomicReferenceArray.length(); i++) {
                            atomicReferenceArray.set(i, null);
                        }
                        this.modCount++;
                        this.count = 0;
                    } finally {
                        unlock();
                    }
                }
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            Impl<K, V, E>.Segment[] segmentArr = this.segments;
            int[] iArr = new int[segmentArr.length];
            int i = 0;
            for (int i2 = 0; i2 < segmentArr.length; i2++) {
                if (segmentArr[i2].count != 0) {
                    return false;
                }
                int i3 = segmentArr[i2].modCount;
                iArr[i2] = i3;
                i += i3;
            }
            if (i == 0) {
                return true;
            }
            for (int i4 = 0; i4 < segmentArr.length; i4++) {
                if (segmentArr[i4].count != 0 || iArr[i4] != segmentArr[i4].modCount) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            long j;
            Impl<K, V, E>.Segment[] segmentArr = this.segments;
            int[] iArr = new int[segmentArr.length];
            long j2 = 0;
            long j3 = 0;
            int i = 0;
            while (true) {
                if (i >= 2) {
                    break;
                }
                long j4 = 0;
                int i2 = 0;
                for (int i3 = 0; i3 < segmentArr.length; i3++) {
                    j4 += segmentArr[i3].count;
                    int i4 = segmentArr[i3].modCount;
                    iArr[i3] = i4;
                    i2 += i4;
                }
                if (i2 != 0) {
                    j = 0;
                    int i5 = 0;
                    while (true) {
                        if (i5 >= segmentArr.length) {
                            break;
                        }
                        j += segmentArr[i5].count;
                        if (iArr[i5] != segmentArr[i5].modCount) {
                            j = -1;
                            break;
                        }
                        i5++;
                    }
                } else {
                    j = 0;
                }
                if (j == j4) {
                    j2 = j4;
                    j3 = j;
                    break;
                }
                i++;
                j2 = j4;
                j3 = j;
            }
            if (j3 != j2) {
                for (Impl<K, V, E>.Segment segment : segmentArr) {
                    segment.lock();
                }
                j2 = 0;
                for (Impl<K, V, E>.Segment segment2 : segmentArr) {
                    j2 += segment2.count;
                }
                for (Impl<K, V, E>.Segment segment3 : segmentArr) {
                    segment3.unlock();
                }
            }
            return j2 > 2147483647L ? ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED : (int) j2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            if (obj == null) {
                throw new NullPointerException("key");
            }
            int iHash = hash(obj);
            return segmentFor(iHash).get(obj, iHash);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            if (obj == null) {
                throw new NullPointerException("key");
            }
            int iHash = hash(obj);
            return segmentFor(iHash).containsKey(obj, iHash);
        }

        /* JADX WARN: Finally extract failed */
        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object obj) {
            if (obj == null) {
                throw new NullPointerException("value");
            }
            Impl<K, V, E>.Segment[] segmentArr = this.segments;
            int[] iArr = new int[segmentArr.length];
            int i = 0;
            int i2 = 0;
            while (true) {
                boolean z = true;
                if (i2 < 2) {
                    int i3 = 0;
                    for (int i4 = 0; i4 < segmentArr.length; i4++) {
                        int i5 = segmentArr[i4].count;
                        int i6 = segmentArr[i4].modCount;
                        iArr[i4] = i6;
                        i3 += i6;
                        if (segmentArr[i4].containsValue(obj)) {
                            return true;
                        }
                    }
                    if (i3 != 0) {
                        int i7 = 0;
                        while (true) {
                            if (i7 >= segmentArr.length) {
                                break;
                            }
                            int i8 = segmentArr[i7].count;
                            if (iArr[i7] != segmentArr[i7].modCount) {
                                z = false;
                                break;
                            }
                            i7++;
                        }
                    }
                    if (z) {
                        return false;
                    }
                    i2++;
                } else {
                    for (Impl<K, V, E>.Segment segment : segmentArr) {
                        segment.lock();
                    }
                    try {
                        int length = segmentArr.length;
                        int i9 = 0;
                        while (true) {
                            if (i9 >= length) {
                                z = false;
                                break;
                            }
                            if (segmentArr[i9].containsValue(obj)) {
                                break;
                            }
                            i9++;
                        }
                        int length2 = segmentArr.length;
                        while (i < length2) {
                            segmentArr[i].unlock();
                            i++;
                        }
                        return z;
                    } catch (Throwable th) {
                        int length3 = segmentArr.length;
                        while (i < length3) {
                            segmentArr[i].unlock();
                            i++;
                        }
                        throw th;
                    }
                }
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(K k, V v) {
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("value");
            }
            int iHash = hash(k);
            return segmentFor(iHash).put(k, iHash, v, false);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public V putIfAbsent(K k, V v) {
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("value");
            }
            int iHash = hash(k);
            return segmentFor(iHash).put(k, iHash, v, true);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void putAll(Map<? extends K, ? extends V> map) {
            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(Object obj) {
            if (obj == null) {
                throw new NullPointerException("key");
            }
            int iHash = hash(obj);
            return segmentFor(iHash).remove(obj, iHash);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public boolean remove(Object obj, Object obj2) {
            if (obj == null) {
                throw new NullPointerException("key");
            }
            int iHash = hash(obj);
            return segmentFor(iHash).remove(obj, iHash, obj2);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public boolean replace(K k, V v, V v2) {
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("oldValue");
            }
            if (v2 == null) {
                throw new NullPointerException("newValue");
            }
            int iHash = hash(k);
            return segmentFor(iHash).replace(k, iHash, v, v2);
        }

        @Override // java.util.Map, java.util.concurrent.ConcurrentMap
        public V replace(K k, V v) {
            if (k == null) {
                throw new NullPointerException("key");
            }
            if (v == null) {
                throw new NullPointerException("value");
            }
            int iHash = hash(k);
            return segmentFor(iHash).replace(k, iHash, v);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            for (Impl<K, V, E>.Segment segment : this.segments) {
                segment.clear();
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            Set<K> set = this.keySet;
            if (set != null) {
                return set;
            }
            KeySet keySet = new KeySet();
            this.keySet = keySet;
            return keySet;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            Collection<V> collection = this.values;
            if (collection != null) {
                return collection;
            }
            Values values = new Values();
            this.values = values;
            return values;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            EntrySet entrySet = new EntrySet();
            this.entrySet = entrySet;
            return entrySet;
        }

        abstract class HashIterator {
            AtomicReferenceArray<E> currentTable;
            Impl<K, V, E>.WriteThroughEntry lastReturned;
            E nextEntry;
            Impl<K, V, E>.WriteThroughEntry nextExternal;
            int nextSegmentIndex;
            int nextTableIndex = -1;

            HashIterator() {
                this.nextSegmentIndex = Impl.this.segments.length - 1;
                advance();
            }

            public boolean hasMoreElements() {
                return hasNext();
            }

            final void advance() {
                this.nextExternal = null;
                if (nextInChain() || nextInTable()) {
                    return;
                }
                while (this.nextSegmentIndex >= 0) {
                    Impl<K, V, E>.Segment[] segmentArr = Impl.this.segments;
                    int i = this.nextSegmentIndex;
                    this.nextSegmentIndex = i - 1;
                    Impl<K, V, E>.Segment segment = segmentArr[i];
                    if (segment.count != 0) {
                        this.currentTable = segment.table;
                        this.nextTableIndex = this.currentTable.length() - 1;
                        if (nextInTable()) {
                            return;
                        }
                    }
                }
            }

            boolean nextInChain() {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                if (this.nextEntry == null) {
                    return false;
                }
                this.nextEntry = strategy.getNext(this.nextEntry);
                while (this.nextEntry != null) {
                    if (advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = strategy.getNext(this.nextEntry);
                }
                return false;
            }

            boolean nextInTable() {
                while (this.nextTableIndex >= 0) {
                    AtomicReferenceArray<E> atomicReferenceArray = this.currentTable;
                    int i = this.nextTableIndex;
                    this.nextTableIndex = i - 1;
                    E e = atomicReferenceArray.get(i);
                    this.nextEntry = e;
                    if (e != null && (advanceTo(this.nextEntry) || nextInChain())) {
                        return true;
                    }
                }
                return false;
            }

            boolean advanceTo(E e) {
                Strategy<K, V, E> strategy = Impl.this.strategy;
                K key = strategy.getKey(e);
                V value = strategy.getValue(e);
                if (key == null || value == null) {
                    return false;
                }
                this.nextExternal = new WriteThroughEntry(key, value);
                return true;
            }

            public boolean hasNext() {
                return this.nextExternal != null;
            }

            Impl<K, V, E>.WriteThroughEntry nextEntry() {
                if (this.nextExternal == null) {
                    throw new NoSuchElementException();
                }
                this.lastReturned = this.nextExternal;
                advance();
                return this.lastReturned;
            }

            public void remove() {
                if (this.lastReturned == null) {
                    throw new IllegalStateException();
                }
                Impl.this.remove(this.lastReturned.getKey());
                this.lastReturned = null;
            }
        }

        final class KeyIterator extends Impl<K, V, E>.HashIterator implements Iterator<K> {
            KeyIterator() {
                super();
            }

            @Override // java.util.Iterator
            public K next() {
                return super.nextEntry().getKey();
            }
        }

        final class ValueIterator extends Impl<K, V, E>.HashIterator implements Iterator<V> {
            ValueIterator() {
                super();
            }

            @Override // java.util.Iterator
            public V next() {
                return super.nextEntry().getValue();
            }
        }

        final class WriteThroughEntry extends AbstractMapEntry<K, V> {
            final K key;
            V value;

            WriteThroughEntry(K k, V v) {
                this.key = k;
                this.value = v;
            }

            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
            public K getKey() {
                return this.key;
            }

            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
            public V getValue() {
                return this.value;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
            public V setValue(V v) {
                if (v == null) {
                    throw new NullPointerException();
                }
                V v2 = (V) Impl.this.put(getKey(), v);
                this.value = v;
                return v2;
            }
        }

        final class EntryIterator extends Impl<K, V, E>.HashIterator implements Iterator<Map.Entry<K, V>> {
            EntryIterator() {
                super();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                return nextEntry();
            }
        }

        final class KeySet extends AbstractSet<K> {
            KeySet() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<K> iterator() {
                return new KeyIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return Impl.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                return Impl.this.containsKey(obj);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                return Impl.this.remove(obj) != null;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                Impl.this.clear();
            }
        }

        final class Values extends AbstractCollection<V> {
            Values() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<V> iterator() {
                return new ValueIterator();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return Impl.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean contains(Object obj) {
                return Impl.this.containsValue(obj);
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public void clear() {
                Impl.this.clear();
            }
        }

        final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
            EntrySet() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, V>> iterator() {
                return new EntryIterator();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                Map.Entry entry;
                Object key;
                Object obj2;
                return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && (obj2 = Impl.this.get(key)) != null && Impl.this.strategy.equalValues(obj2, entry.getValue());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                Map.Entry entry;
                Object key;
                return (obj instanceof Map.Entry) && (key = (entry = (Map.Entry) obj).getKey()) != null && Impl.this.remove(key, entry.getValue());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return Impl.this.size();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean isEmpty() {
                return Impl.this.isEmpty();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public void clear() {
                Impl.this.clear();
            }
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeInt(size());
            objectOutputStream.writeInt(this.segments.length);
            objectOutputStream.writeObject(this.strategy);
            for (Map.Entry<K, V> entry : entrySet()) {
                objectOutputStream.writeObject(entry.getKey());
                objectOutputStream.writeObject(entry.getValue());
            }
            objectOutputStream.writeObject(null);
        }

        static class Fields {
            static final Field segmentShift = findField("segmentShift");
            static final Field segmentMask = findField("segmentMask");
            static final Field segments = findField("segments");
            static final Field strategy = findField("strategy");

            Fields() {
            }

            static Field findField(String str) throws NoSuchFieldException {
                try {
                    Field declaredField = Impl.class.getDeclaredField(str);
                    declaredField.setAccessible(true);
                    return declaredField;
                } catch (NoSuchFieldException e) {
                    throw new AssertionError(e);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void readObject(ObjectInputStream objectInputStream) throws IllegalAccessException, IOException, ClassNotFoundException, IllegalArgumentException {
            try {
                int i = objectInputStream.readInt();
                int i2 = objectInputStream.readInt();
                Strategy strategy = (Strategy) objectInputStream.readObject();
                if (i2 > 65536) {
                    i2 = 65536;
                }
                int i3 = 1;
                int i4 = 1;
                int i5 = 0;
                while (i4 < i2) {
                    i5++;
                    i4 <<= 1;
                }
                Fields.segmentShift.set(this, Integer.valueOf(32 - i5));
                Fields.segmentMask.set(this, Integer.valueOf(i4 - 1));
                Fields.segments.set(this, newSegmentArray(i4));
                if (i > MAXIMUM_CAPACITY) {
                    i = MAXIMUM_CAPACITY;
                }
                int i6 = i / i4;
                if (i4 * i6 < i) {
                    i6++;
                }
                while (i3 < i6) {
                    i3 <<= 1;
                }
                for (int i7 = 0; i7 < this.segments.length; i7++) {
                    this.segments[i7] = new Segment(i3);
                }
                Fields.strategy.set(this, strategy);
                while (true) {
                    Object object = objectInputStream.readObject();
                    if (object == null) {
                        return;
                    } else {
                        put(object, objectInputStream.readObject());
                    }
                }
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }

    static class ComputingImpl<K, V, E> extends Impl<K, V, E> {
        static final long serialVersionUID = 0;
        final Function<? super K, ? extends V> computer;
        final ComputingStrategy<K, V, E> computingStrategy;

        ComputingImpl(ComputingStrategy<K, V, E> computingStrategy, Builder builder, Function<? super K, ? extends V> function) {
            super(computingStrategy, builder);
            this.computingStrategy = computingStrategy;
            this.computer = function;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Impl, java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            V v;
            Object objNewEntry;
            boolean z;
            if (obj == null) {
                throw new NullPointerException("key");
            }
            int iHash = hash(obj);
            Impl<K, V, E>.Segment segmentSegmentFor = segmentFor(iHash);
            while (true) {
                Object entry = segmentSegmentFor.getEntry(obj, iHash);
                boolean z2 = false;
                if (entry == null) {
                    segmentSegmentFor.lock();
                    try {
                        Object entry2 = segmentSegmentFor.getEntry(obj, iHash);
                        if (entry2 == null) {
                            int i = segmentSegmentFor.count;
                            int i2 = i + 1;
                            if (i > segmentSegmentFor.threshold) {
                                segmentSegmentFor.expand();
                            }
                            AtomicReferenceArray<E> atomicReferenceArray = segmentSegmentFor.table;
                            int length = (atomicReferenceArray.length() - 1) & iHash;
                            Object obj2 = atomicReferenceArray.get(length);
                            segmentSegmentFor.modCount++;
                            objNewEntry = this.computingStrategy.newEntry(obj, iHash, obj2);
                            atomicReferenceArray.set(length, objNewEntry);
                            segmentSegmentFor.count = i2;
                            z = true;
                        } else {
                            objNewEntry = entry2;
                            z = false;
                        }
                        if (z) {
                            try {
                                V v2 = (V) this.computingStrategy.compute(obj, objNewEntry, this.computer);
                                if (v2 != null) {
                                    return v2;
                                }
                                throw new NullPointerException("compute() returned null unexpectedly");
                            } finally {
                                segmentSegmentFor.removeEntry(objNewEntry, iHash);
                            }
                        }
                        entry = objNewEntry;
                    } finally {
                        segmentSegmentFor.unlock();
                    }
                }
                while (true) {
                    try {
                        v = (V) this.computingStrategy.waitForValue(entry);
                        break;
                    } catch (InterruptedException unused) {
                        z2 = true;
                    } catch (Throwable th) {
                        if (z2) {
                            Thread.currentThread().interrupt();
                        }
                        throw th;
                    }
                }
                if (v != null) {
                    if (z2) {
                        Thread.currentThread().interrupt();
                    }
                    return v;
                }
                if (z2) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class SimpleStrategy<K, V> implements Strategy<K, V, SimpleInternalEntry<K, V>> {
        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public void setInternals(Internals<K, V, SimpleInternalEntry<K, V>> internals) {
        }

        SimpleStrategy() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public /* bridge */ /* synthetic */ Object copyEntry(Object obj, Object obj2, Object obj3) {
            return copyEntry((SimpleStrategy<K, V>) obj, (SimpleInternalEntry<SimpleStrategy<K, V>, V>) obj2, (SimpleInternalEntry<SimpleStrategy<K, V>, V>) obj3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public /* bridge */ /* synthetic */ Object newEntry(Object obj, int i, Object obj2) {
            return newEntry((SimpleStrategy<K, V>) obj, i, (SimpleInternalEntry<SimpleStrategy<K, V>, V>) obj2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public /* bridge */ /* synthetic */ void setValue(Object obj, Object obj2) {
            setValue((SimpleInternalEntry<K, SimpleInternalEntry<K, V>>) obj, (SimpleInternalEntry<K, V>) obj2);
        }

        public SimpleInternalEntry<K, V> newEntry(K k, int i, SimpleInternalEntry<K, V> simpleInternalEntry) {
            return new SimpleInternalEntry<>(k, i, null, simpleInternalEntry);
        }

        public SimpleInternalEntry<K, V> copyEntry(K k, SimpleInternalEntry<K, V> simpleInternalEntry, SimpleInternalEntry<K, V> simpleInternalEntry2) {
            return new SimpleInternalEntry<>(k, simpleInternalEntry.hash, simpleInternalEntry.value, simpleInternalEntry2);
        }

        public void setValue(SimpleInternalEntry<K, V> simpleInternalEntry, V v) {
            simpleInternalEntry.value = v;
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public V getValue(SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.value;
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public boolean equalKeys(K k, Object obj) {
            return k.equals(obj);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public boolean equalValues(V v, Object obj) {
            return v.equals(obj);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public int hashKey(Object obj) {
            return obj.hashCode();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public K getKey(SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.key;
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public SimpleInternalEntry<K, V> getNext(SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.next;
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public int getHash(SimpleInternalEntry<K, V> simpleInternalEntry) {
            return simpleInternalEntry.hash;
        }
    }

    static class SimpleInternalEntry<K, V> {
        final int hash;
        final K key;
        final SimpleInternalEntry<K, V> next;
        volatile V value;

        SimpleInternalEntry(K k, int i, @Nullable V v, SimpleInternalEntry<K, V> simpleInternalEntry) {
            this.key = k;
            this.hash = i;
            this.value = v;
            this.next = simpleInternalEntry;
        }
    }
}
