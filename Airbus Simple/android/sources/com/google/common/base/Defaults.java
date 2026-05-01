package com.google.common.base;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class Defaults {
    private static final Map<Class<?>, Object> DEFAULTS = new HashMap(16);

    private Defaults() {
    }

    static {
        put(Boolean.TYPE, false);
        put(Character.TYPE, (char) 0);
        put(Byte.TYPE, (byte) 0);
        put(Short.TYPE, (short) 0);
        put(Integer.TYPE, 0);
        put(Long.TYPE, 0L);
        put(Float.TYPE, Float.valueOf(0.0f));
        put(Double.TYPE, Double.valueOf(0.0d));
    }

    private static <T> void put(Class<T> cls, T t) {
        DEFAULTS.put(cls, t);
    }

    public static <T> T defaultValue(Class<T> cls) {
        return (T) DEFAULTS.get(cls);
    }
}
