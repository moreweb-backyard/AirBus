package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.FinalizableReferenceQueue;
import com.google.common.base.FinalizableSoftReference;
import com.google.common.base.FinalizableWeakReference;
import com.google.common.base.Function;
import com.google.common.collect.CustomConcurrentHashMap;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import voice.encoder.BuildConfig;

@GwtCompatible(emulated = BuildConfig.DEBUG)
/* loaded from: classes.dex */
public final class MapMaker {
    private static final ValueReference<Object, Object> COMPUTING = new ValueReference<Object, Object>() { // from class: com.google.common.collect.MapMaker.1
        @Override // com.google.common.collect.MapMaker.ValueReference
        public Object get() {
            return null;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<Object, Object> copyFor(ReferenceEntry<Object, Object> referenceEntry) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public Object waitForValue() {
            throw new AssertionError();
        }
    };
    private boolean useCustomMap;
    private Strength keyStrength = Strength.STRONG;
    private Strength valueStrength = Strength.STRONG;
    private long expirationNanos = 0;
    private final CustomConcurrentHashMap.Builder builder = new CustomConcurrentHashMap.Builder();

    private interface ReferenceEntry<K, V> {
        int getHash();

        K getKey();

        ReferenceEntry<K, V> getNext();

        ValueReference<K, V> getValueReference();

        void setValueReference(ValueReference<K, V> valueReference);

        void valueReclaimed();
    }

    private enum Strength {
        WEAK { // from class: com.google.common.collect.MapMaker.Strength.1
            @Override // com.google.common.collect.MapMaker.Strength
            boolean equal(Object obj, Object obj2) {
                return obj == obj2;
            }

            @Override // com.google.common.collect.MapMaker.Strength
            int hash(Object obj) {
                return System.identityHashCode(obj);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> referenceEntry, V v) {
                return new WeakValueReference(v, referenceEntry);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return referenceEntry == null ? new WeakEntry(internals, k, i) : new LinkedWeakEntry(internals, k, i, referenceEntry);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> copyEntry(K k, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                WeakEntry weakEntry = (WeakEntry) referenceEntry;
                return referenceEntry2 == null ? new WeakEntry(weakEntry.internals, k, weakEntry.hash) : new LinkedWeakEntry(weakEntry.internals, k, weakEntry.hash, referenceEntry2);
            }
        },
        SOFT { // from class: com.google.common.collect.MapMaker.Strength.2
            @Override // com.google.common.collect.MapMaker.Strength
            boolean equal(Object obj, Object obj2) {
                return obj == obj2;
            }

            @Override // com.google.common.collect.MapMaker.Strength
            int hash(Object obj) {
                return System.identityHashCode(obj);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> referenceEntry, V v) {
                return new SoftValueReference(v, referenceEntry);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return referenceEntry == null ? new SoftEntry(internals, k, i) : new LinkedSoftEntry(internals, k, i, referenceEntry);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> copyEntry(K k, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                SoftEntry softEntry = (SoftEntry) referenceEntry;
                return referenceEntry2 == null ? new SoftEntry(softEntry.internals, k, softEntry.hash) : new LinkedSoftEntry(softEntry.internals, k, softEntry.hash, referenceEntry2);
            }
        },
        STRONG { // from class: com.google.common.collect.MapMaker.Strength.3
            @Override // com.google.common.collect.MapMaker.Strength
            boolean equal(Object obj, Object obj2) {
                return obj.equals(obj2);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            int hash(Object obj) {
                return obj.hashCode();
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> referenceEntry, V v) {
                return new StrongValueReference(v);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry) {
                return referenceEntry == null ? new StrongEntry(internals, k, i) : new LinkedStrongEntry(internals, k, i, referenceEntry);
            }

            @Override // com.google.common.collect.MapMaker.Strength
            <K, V> ReferenceEntry<K, V> copyEntry(K k, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                StrongEntry strongEntry = (StrongEntry) referenceEntry;
                return referenceEntry2 == null ? new StrongEntry(strongEntry.internals, k, strongEntry.hash) : new LinkedStrongEntry(strongEntry.internals, k, strongEntry.hash, referenceEntry2);
            }
        };

        abstract <K, V> ReferenceEntry<K, V> copyEntry(K k, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2);

        abstract boolean equal(Object obj, Object obj2);

        abstract int hash(Object obj);

        abstract <K, V> ReferenceEntry<K, V> newEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry);

        abstract <K, V> ValueReference<K, V> referenceValue(ReferenceEntry<K, V> referenceEntry, V v);
    }

    private interface ValueReference<K, V> {
        ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry);

        V get();

        V waitForValue() throws InterruptedException;
    }

    public MapMaker initialCapacity(int i) {
        this.builder.initialCapacity(i);
        return this;
    }

    @GwtIncompatible("java.util.concurrent.ConcurrentHashMap concurrencyLevel")
    public MapMaker concurrencyLevel(int i) {
        this.builder.concurrencyLevel(i);
        return this;
    }

    @GwtIncompatible("java.lang.ref.WeakReference")
    public MapMaker weakKeys() {
        return setKeyStrength(Strength.WEAK);
    }

    @GwtIncompatible("java.lang.ref.SoftReference")
    public MapMaker softKeys() {
        return setKeyStrength(Strength.SOFT);
    }

    private MapMaker setKeyStrength(Strength strength) {
        if (this.keyStrength != Strength.STRONG) {
            throw new IllegalStateException("Key strength was already set to " + this.keyStrength + FileUtil.FILE_EXTENSION_SEPARATOR);
        }
        this.keyStrength = strength;
        this.useCustomMap = true;
        return this;
    }

    @GwtIncompatible("java.lang.ref.WeakReference")
    public MapMaker weakValues() {
        return setValueStrength(Strength.WEAK);
    }

    @GwtIncompatible("java.lang.ref.SoftReference")
    public MapMaker softValues() {
        return setValueStrength(Strength.SOFT);
    }

    private MapMaker setValueStrength(Strength strength) {
        if (this.valueStrength != Strength.STRONG) {
            throw new IllegalStateException("Value strength was already set to " + this.valueStrength + FileUtil.FILE_EXTENSION_SEPARATOR);
        }
        this.valueStrength = strength;
        this.useCustomMap = true;
        return this;
    }

    public MapMaker expiration(long j, TimeUnit timeUnit) {
        if (this.expirationNanos != 0) {
            throw new IllegalStateException("expiration time of " + this.expirationNanos + " ns was already set");
        }
        if (j <= 0) {
            throw new IllegalArgumentException("invalid duration: " + j);
        }
        this.expirationNanos = timeUnit.toNanos(j);
        this.useCustomMap = true;
        return this;
    }

    public <K, V> ConcurrentMap<K, V> makeMap() {
        return this.useCustomMap ? new StrategyImpl(this).map : new ConcurrentHashMap(this.builder.getInitialCapacity(), 0.75f, this.builder.getConcurrencyLevel());
    }

    public <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> function) {
        return new StrategyImpl(this, function).map;
    }

    private static class StrategyImpl<K, V> implements Serializable, CustomConcurrentHashMap.ComputingStrategy<K, V, ReferenceEntry<K, V>> {
        private static final long serialVersionUID = 0;
        final long expirationNanos;
        CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        final Strength keyStrength;
        final ConcurrentMap<K, V> map;
        final Strength valueStrength;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.ComputingStrategy
        public /* bridge */ /* synthetic */ Object compute(Object obj, Object obj2, Function function) {
            return compute((StrategyImpl<K, V>) obj, (ReferenceEntry<StrategyImpl<K, V>, V>) obj2, (Function<? super StrategyImpl<K, V>, ? extends V>) function);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public /* bridge */ /* synthetic */ Object copyEntry(Object obj, Object obj2, Object obj3) {
            return copyEntry((StrategyImpl<K, V>) obj, (ReferenceEntry<StrategyImpl<K, V>, V>) obj2, (ReferenceEntry<StrategyImpl<K, V>, V>) obj3);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public /* bridge */ /* synthetic */ Object newEntry(Object obj, int i, Object obj2) {
            return newEntry((StrategyImpl<K, V>) obj, i, (ReferenceEntry<StrategyImpl<K, V>, V>) obj2);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public /* bridge */ /* synthetic */ void setValue(Object obj, Object obj2) {
            setValue((ReferenceEntry<K, ReferenceEntry<K, V>>) obj, (ReferenceEntry<K, V>) obj2);
        }

        StrategyImpl(MapMaker mapMaker) {
            this.keyStrength = mapMaker.keyStrength;
            this.valueStrength = mapMaker.valueStrength;
            this.expirationNanos = mapMaker.expirationNanos;
            this.map = mapMaker.builder.buildMap(this);
        }

        StrategyImpl(MapMaker mapMaker, Function<? super K, ? extends V> function) {
            this.keyStrength = mapMaker.keyStrength;
            this.valueStrength = mapMaker.valueStrength;
            this.expirationNanos = mapMaker.expirationNanos;
            this.map = mapMaker.builder.buildComputingMap(this, function);
        }

        public void setValue(ReferenceEntry<K, V> referenceEntry, V v) {
            setValueReference(referenceEntry, this.valueStrength.referenceValue(referenceEntry, v));
            if (this.expirationNanos > 0) {
                scheduleRemoval(referenceEntry.getKey(), v);
            }
        }

        void scheduleRemoval(K k, V v) {
            final WeakReference weakReference = new WeakReference(k);
            final WeakReference weakReference2 = new WeakReference(v);
            ExpirationTimer.instance.schedule(new TimerTask() { // from class: com.google.common.collect.MapMaker.StrategyImpl.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    Object obj = weakReference.get();
                    if (obj != null) {
                        StrategyImpl.this.map.remove(obj, weakReference2.get());
                    }
                }
            }, TimeUnit.NANOSECONDS.toMillis(this.expirationNanos));
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public boolean equalKeys(K k, Object obj) {
            return this.keyStrength.equal(k, obj);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public boolean equalValues(V v, Object obj) {
            return this.valueStrength.equal(v, obj);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public int hashKey(Object obj) {
            return this.keyStrength.hash(obj);
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public K getKey(ReferenceEntry<K, V> referenceEntry) {
            return referenceEntry.getKey();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public int getHash(ReferenceEntry<K, V> referenceEntry) {
            return referenceEntry.getHash();
        }

        public ReferenceEntry<K, V> newEntry(K k, int i, ReferenceEntry<K, V> referenceEntry) {
            return this.keyStrength.newEntry(this.internals, k, i, referenceEntry);
        }

        public ReferenceEntry<K, V> copyEntry(K k, ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
            ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            if (valueReference == MapMaker.COMPUTING) {
                ReferenceEntry<K, V> referenceEntryNewEntry = newEntry((StrategyImpl<K, V>) k, referenceEntry.getHash(), (ReferenceEntry<StrategyImpl<K, V>, V>) referenceEntry2);
                referenceEntryNewEntry.setValueReference(new FutureValueReference(referenceEntry, referenceEntryNewEntry));
                return referenceEntryNewEntry;
            }
            ReferenceEntry<K, V> referenceEntryNewEntry2 = newEntry((StrategyImpl<K, V>) k, referenceEntry.getHash(), (ReferenceEntry<StrategyImpl<K, V>, V>) referenceEntry2);
            referenceEntryNewEntry2.setValueReference(valueReference.copyFor(referenceEntryNewEntry2));
            return referenceEntryNewEntry2;
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.ComputingStrategy
        public V waitForValue(ReferenceEntry<K, V> referenceEntry) throws InterruptedException {
            ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            if (valueReference == MapMaker.COMPUTING) {
                synchronized (referenceEntry) {
                    while (true) {
                        valueReference = referenceEntry.getValueReference();
                        if (valueReference != MapMaker.COMPUTING) {
                            break;
                        }
                        referenceEntry.wait();
                    }
                }
            }
            return valueReference.waitForValue();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public V getValue(ReferenceEntry<K, V> referenceEntry) {
            return referenceEntry.getValueReference().get();
        }

        public V compute(K k, ReferenceEntry<K, V> referenceEntry, Function<? super K, ? extends V> function) {
            try {
                V vApply = function.apply(k);
                if (vApply == null) {
                    String str = function + " returned null for key " + k + FileUtil.FILE_EXTENSION_SEPARATOR;
                    setValueReference(referenceEntry, new NullOutputExceptionReference(str));
                    throw new NullOutputException(str);
                }
                setValue((ReferenceEntry<K, ReferenceEntry<K, V>>) referenceEntry, (ReferenceEntry<K, V>) vApply);
                return vApply;
            } catch (ComputationException e) {
                setValueReference(referenceEntry, new ComputationExceptionReference(e.getCause()));
                throw e;
            } catch (Throwable th) {
                setValueReference(referenceEntry, new ComputationExceptionReference(th));
                throw new ComputationException(th);
            }
        }

        void setValueReference(ReferenceEntry<K, V> referenceEntry, ValueReference<K, V> valueReference) {
            boolean z = referenceEntry.getValueReference() == MapMaker.COMPUTING;
            referenceEntry.setValueReference(valueReference);
            if (z) {
                synchronized (referenceEntry) {
                    referenceEntry.notifyAll();
                }
            }
        }

        private class FutureValueReference implements ValueReference<K, V> {
            final ReferenceEntry<K, V> newEntry;
            final ReferenceEntry<K, V> original;

            FutureValueReference(ReferenceEntry<K, V> referenceEntry, ReferenceEntry<K, V> referenceEntry2) {
                this.original = referenceEntry;
                this.newEntry = referenceEntry2;
            }

            @Override // com.google.common.collect.MapMaker.ValueReference
            public V get() {
                try {
                    return this.original.getValueReference().get();
                } catch (Throwable th) {
                    removeEntry();
                    throw th;
                }
            }

            @Override // com.google.common.collect.MapMaker.ValueReference
            public ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry) {
                return new FutureValueReference(this.original, referenceEntry);
            }

            @Override // com.google.common.collect.MapMaker.ValueReference
            public V waitForValue() throws InterruptedException {
                try {
                    return (V) StrategyImpl.this.waitForValue((ReferenceEntry) this.original);
                } catch (Throwable th) {
                    removeEntry();
                    throw th;
                }
            }

            void removeEntry() {
                StrategyImpl.this.internals.removeEntry(this.newEntry);
            }
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public ReferenceEntry<K, V> getNext(ReferenceEntry<K, V> referenceEntry) {
            return referenceEntry.getNext();
        }

        @Override // com.google.common.collect.CustomConcurrentHashMap.Strategy
        public void setInternals(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals) {
            this.internals = internals;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.writeObject(this.keyStrength);
            objectOutputStream.writeObject(this.valueStrength);
            objectOutputStream.writeLong(this.expirationNanos);
            objectOutputStream.writeObject(this.internals);
            objectOutputStream.writeObject(this.map);
        }

        private static class Fields {
            static final Field keyStrength = findField("keyStrength");
            static final Field valueStrength = findField("valueStrength");
            static final Field expirationNanos = findField("expirationNanos");
            static final Field internals = findField("internals");
            static final Field map = findField("map");

            private Fields() {
            }

            static Field findField(String str) throws NoSuchFieldException {
                try {
                    Field declaredField = StrategyImpl.class.getDeclaredField(str);
                    declaredField.setAccessible(true);
                    return declaredField;
                } catch (NoSuchFieldException e) {
                    throw new AssertionError(e);
                }
            }
        }

        private void readObject(ObjectInputStream objectInputStream) throws IllegalAccessException, IOException, ClassNotFoundException, IllegalArgumentException {
            try {
                Fields.keyStrength.set(this, objectInputStream.readObject());
                Fields.valueStrength.set(this, objectInputStream.readObject());
                Fields.expirationNanos.set(this, Long.valueOf(objectInputStream.readLong()));
                Fields.internals.set(this, objectInputStream.readObject());
                Fields.map.set(this, objectInputStream.readObject());
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> ValueReference<K, V> computing() {
        return (ValueReference<K, V>) COMPUTING;
    }

    private static class NullOutputExceptionReference<K, V> implements ValueReference<K, V> {
        final String message;

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V get() {
            return null;
        }

        NullOutputExceptionReference(String str) {
            this.message = str;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            throw new NullOutputException(this.message);
        }
    }

    private static class ComputationExceptionReference<K, V> implements ValueReference<K, V> {
        final Throwable t;

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V get() {
            return null;
        }

        ComputationExceptionReference(Throwable th) {
            this.t = th;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            throw new AsynchronousComputationException(this.t);
        }
    }

    private static class QueueHolder {
        static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();

        private QueueHolder() {
        }
    }

    private static class StrongEntry<K, V> implements ReferenceEntry<K, V> {
        final int hash;
        final CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        final K key;
        volatile ValueReference<K, V> valueReference = MapMaker.computing();

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return null;
        }

        StrongEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i) {
            this.internals = internals;
            this.key = k;
            this.hash = i;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public K getKey() {
            return this.key;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public int getHash() {
            return this.hash;
        }
    }

    private static class LinkedStrongEntry<K, V> extends StrongEntry<K, V> {
        final ReferenceEntry<K, V> next;

        LinkedStrongEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(internals, k, i);
            this.next = referenceEntry;
        }

        @Override // com.google.common.collect.MapMaker.StrongEntry, com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    private static class SoftEntry<K, V> extends FinalizableSoftReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        volatile ValueReference<K, V> valueReference;

        public ReferenceEntry<K, V> getNext() {
            return null;
        }

        SoftEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i) {
            super(k, QueueHolder.queue);
            this.valueReference = MapMaker.computing();
            this.internals = internals;
            this.hash = i;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public K getKey() {
            return (K) get();
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.internals.removeEntry(this);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public int getHash() {
            return this.hash;
        }
    }

    private static class LinkedSoftEntry<K, V> extends SoftEntry<K, V> {
        final ReferenceEntry<K, V> next;

        LinkedSoftEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(internals, k, i);
            this.next = referenceEntry;
        }

        @Override // com.google.common.collect.MapMaker.SoftEntry, com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    private static class WeakEntry<K, V> extends FinalizableWeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        volatile ValueReference<K, V> valueReference;

        public ReferenceEntry<K, V> getNext() {
            return null;
        }

        WeakEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i) {
            super(k, QueueHolder.queue);
            this.valueReference = MapMaker.computing();
            this.internals = internals;
            this.hash = i;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public K getKey() {
            return (K) get();
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.internals.removeEntry(this);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void setValueReference(ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }

        @Override // com.google.common.collect.MapMaker.ReferenceEntry
        public int getHash() {
            return this.hash;
        }
    }

    private static class LinkedWeakEntry<K, V> extends WeakEntry<K, V> {
        final ReferenceEntry<K, V> next;

        LinkedWeakEntry(CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, K k, int i, ReferenceEntry<K, V> referenceEntry) {
            super(internals, k, i);
            this.next = referenceEntry;
        }

        @Override // com.google.common.collect.MapMaker.WeakEntry, com.google.common.collect.MapMaker.ReferenceEntry
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }

    private static class WeakValueReference<K, V> extends FinalizableWeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        WeakValueReference(V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, QueueHolder.queue);
            this.entry = referenceEntry;
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.entry.valueReclaimed();
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference(get(), referenceEntry);
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    private static class SoftValueReference<K, V> extends FinalizableSoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;

        SoftValueReference(V v, ReferenceEntry<K, V> referenceEntry) {
            super(v, QueueHolder.queue);
            this.entry = referenceEntry;
        }

        @Override // com.google.common.base.FinalizableReference
        public void finalizeReferent() {
            this.entry.valueReclaimed();
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry) {
            return new SoftValueReference(get(), referenceEntry);
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            return get();
        }
    }

    private static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;

        @Override // com.google.common.collect.MapMaker.ValueReference
        public ValueReference<K, V> copyFor(ReferenceEntry<K, V> referenceEntry) {
            return this;
        }

        StrongValueReference(V v) {
            this.referent = v;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V get() {
            return this.referent;
        }

        @Override // com.google.common.collect.MapMaker.ValueReference
        public V waitForValue() {
            return get();
        }
    }
}
