package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public final class MutableClassToInstanceMap<B> extends ConstrainedMap<Class<? extends B>, B> implements ClassToInstanceMap<B> {
    private static final long serialVersionUID = 0;
    private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>() { // from class: com.google.common.collect.MutableClassToInstanceMap.1
        @Override // com.google.common.collect.MapConstraint
        public void checkKeyValue(Class<?> cls, Object obj) {
            MutableClassToInstanceMap.cast(cls, obj);
        }
    };
    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS = new ImmutableMap.Builder().put(Boolean.TYPE, Boolean.class).put(Byte.TYPE, Byte.class).put(Character.TYPE, Character.class).put(Double.TYPE, Double.class).put(Float.TYPE, Float.class).put(Integer.TYPE, Integer.class).put(Long.TYPE, Long.class).put(Short.TYPE, Short.class).put(Void.TYPE, Void.class).build();

    @Override // com.google.common.collect.ConstrainedMap, com.google.common.collect.ForwardingMap, java.util.Map
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.ConstrainedMap, com.google.common.collect.ForwardingMap, java.util.Map, com.google.common.collect.BiMap
    public /* bridge */ /* synthetic */ void putAll(Map map) {
        super.putAll(map);
    }

    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap<>(new HashMap());
    }

    public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> map) {
        return new MutableClassToInstanceMap<>(map);
    }

    private MutableClassToInstanceMap(Map<Class<? extends B>, B> map) {
        super(map, VALUE_CAN_BE_CAST_TO_KEY);
    }

    @Override // com.google.common.collect.ClassToInstanceMap
    public <T extends B> T putInstance(Class<T> cls, T t) {
        return (T) cast(cls, put(cls, t));
    }

    @Override // com.google.common.collect.ClassToInstanceMap
    public <T extends B> T getInstance(Class<T> cls) {
        return (T) cast(cls, get(cls));
    }

    static <B, T extends B> T cast(Class<T> cls, B b) {
        return (T) wrap(cls).cast(b);
    }

    private static <T> Class<T> wrap(Class<T> cls) {
        return cls.isPrimitive() ? (Class) PRIMITIVES_TO_WRAPPERS.get(cls) : cls;
    }
}
