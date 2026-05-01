package com.google.common.util.concurrent;

import java.util.concurrent.Callable;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public final class Callables {
    private Callables() {
    }

    public static <T> Callable<T> returning(@Nullable final T t) {
        return new Callable<T>() { // from class: com.google.common.util.concurrent.Callables.1
            @Override // java.util.concurrent.Callable
            public T call() {
                return (T) t;
            }
        };
    }
}
