package com.google.common.io;

import java.io.IOException;

/* loaded from: classes.dex */
public interface OutputSupplier<T> {
    T getOutput() throws IOException;
}
