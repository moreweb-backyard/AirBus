package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.lang.reflect.Array;
import java.util.List;
import voice.encoder.BuildConfig;

@GwtCompatible(emulated = BuildConfig.DEBUG)
/* loaded from: classes.dex */
class Platform {
    @GwtIncompatible("List.subList")
    static <T> List<T> subList(List<T> list, int i, int i2) {
        return list.subList(i, i2);
    }

    @GwtIncompatible("Class.isInstance")
    static boolean isInstance(Class<?> cls, Object obj) {
        return cls.isInstance(obj);
    }

    static <T> T[] clone(T[] tArr) {
        return (T[]) ((Object[]) tArr.clone());
    }

    static void unsafeArrayCopy(Object[] objArr, int i, Object[] objArr2, int i2, int i3) {
        System.arraycopy(objArr, i, objArr2, i2, i3);
    }

    @GwtIncompatible("Array.newInstance(Class, int)")
    static <T> T[] newArray(Class<T> cls, int i) {
        return (T[]) ((Object[]) Array.newInstance((Class<?>) cls, i));
    }

    static <T> T[] newArray(T[] tArr, int i) {
        return (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i));
    }

    static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }

    private Platform() {
    }
}
