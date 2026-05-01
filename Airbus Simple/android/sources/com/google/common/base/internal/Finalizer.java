package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public class Finalizer extends Thread {
    private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
    private final WeakReference<Class<?>> finalizableReferenceClassReference;
    private final PhantomReference<Object> frqReference;
    private final ReferenceQueue<Object> queue;
    private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
    private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();

    public static ReferenceQueue<Object> startFinalizer(Class<?> cls, Object obj) {
        if (!cls.getName().equals(FINALIZABLE_REFERENCE)) {
            throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
        }
        Finalizer finalizer = new Finalizer(cls, obj);
        finalizer.start();
        return finalizer.queue;
    }

    private Finalizer(Class<?> cls, Object obj) {
        super(Finalizer.class.getName());
        this.queue = new ReferenceQueue<>();
        this.finalizableReferenceClassReference = new WeakReference<>(cls);
        this.frqReference = new PhantomReference<>(obj, this.queue);
        setDaemon(true);
        try {
            if (inheritableThreadLocals != null) {
                inheritableThreadLocals.set(this, null);
            }
        } catch (Throwable th) {
            logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", th);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                cleanUp(this.queue.remove());
            } catch (ShutDown unused) {
                return;
            } catch (InterruptedException unused2) {
            }
        }
    }

    private void cleanUp(Reference<?> reference) throws ShutDown {
        Method finalizeReferentMethod = getFinalizeReferentMethod();
        do {
            reference.clear();
            if (reference == this.frqReference) {
                throw new ShutDown();
            }
            try {
                finalizeReferentMethod.invoke(reference, new Object[0]);
            } catch (Throwable th) {
                logger.log(Level.SEVERE, "Error cleaning up after reference.", th);
            }
            reference = this.queue.poll();
        } while (reference != null);
    }

    private Method getFinalizeReferentMethod() throws ShutDown {
        Class<?> cls = this.finalizableReferenceClassReference.get();
        if (cls == null) {
            throw new ShutDown();
        }
        try {
            return cls.getMethod("finalizeReferent", new Class[0]);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    public static Field getInheritableThreadLocalsField() {
        try {
            Field declaredField = Thread.class.getDeclaredField("inheritableThreadLocals");
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable unused) {
            logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
            return null;
        }
    }

    private static class ShutDown extends Exception {
        private ShutDown() {
        }
    }
}
