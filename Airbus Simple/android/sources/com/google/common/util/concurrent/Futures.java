package com.google.common.util.concurrent;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class Futures {
    private Futures() {
    }

    public static <V> UninterruptibleFuture<V> makeUninterruptible(final Future<V> future) {
        Preconditions.checkNotNull(future);
        if (future instanceof UninterruptibleFuture) {
            return (UninterruptibleFuture) future;
        }
        return new UninterruptibleFuture<V>() { // from class: com.google.common.util.concurrent.Futures.1
            @Override // java.util.concurrent.Future
            public boolean cancel(boolean z) {
                return future.cancel(z);
            }

            @Override // java.util.concurrent.Future
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override // java.util.concurrent.Future
            public boolean isDone() {
                return future.isDone();
            }

            @Override // com.google.common.util.concurrent.UninterruptibleFuture, java.util.concurrent.Future
            public V get(long j, TimeUnit timeUnit) throws ExecutionException, TimeoutException {
                boolean z = false;
                try {
                    long nanos = timeUnit.toNanos(j);
                    long jNanoTime = System.nanoTime() + nanos;
                    while (nanos > 0) {
                        try {
                            return (V) future.get(nanos, TimeUnit.NANOSECONDS);
                        } catch (InterruptedException unused) {
                            z = true;
                            nanos = jNanoTime - System.nanoTime();
                        }
                    }
                    throw new TimeoutException();
                } finally {
                    if (z) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            @Override // com.google.common.util.concurrent.UninterruptibleFuture, java.util.concurrent.Future
            public V get() throws ExecutionException {
                V v;
                boolean z = false;
                while (true) {
                    try {
                        v = (V) future.get();
                        break;
                    } catch (InterruptedException unused) {
                        z = true;
                    } catch (Throwable th) {
                        if (z) {
                            Thread.currentThread().interrupt();
                        }
                        throw th;
                    }
                }
                if (z) {
                    Thread.currentThread().interrupt();
                }
                return v;
            }
        };
    }

    public static <T> ListenableFuture<T> makeListenable(Future<T> future) {
        if (future instanceof ListenableFuture) {
            return (ListenableFuture) future;
        }
        return new ListenableFutureAdapter(future);
    }

    public static <T, E extends Exception> CheckedFuture<T, E> makeChecked(Future<T> future, Function<Exception, E> function) {
        return new MappingCheckedFuture(makeListenable(future), function);
    }

    public static <T> ListenableFuture<T> immediateFuture(@Nullable T t) {
        ValueFuture valueFutureCreate = ValueFuture.create();
        valueFutureCreate.set(t);
        return valueFutureCreate;
    }

    public static <T, E extends Exception> CheckedFuture<T, E> immediateCheckedFuture(@Nullable T t) {
        ValueFuture valueFutureCreate = ValueFuture.create();
        valueFutureCreate.set(t);
        return makeChecked(valueFutureCreate, new Function<Exception, E>() { // from class: com.google.common.util.concurrent.Futures.2
            /* JADX WARN: Incorrect return type in method signature: (Ljava/lang/Exception;)TE; */
            @Override // com.google.common.base.Function
            public Exception apply(Exception exc) {
                throw new AssertionError("impossible");
            }
        });
    }

    public static <T> ListenableFuture<T> immediateFailedFuture(Throwable th) {
        Preconditions.checkNotNull(th);
        ValueFuture valueFutureCreate = ValueFuture.create();
        valueFutureCreate.setException(th);
        return valueFutureCreate;
    }

    public static <T, E extends Exception> CheckedFuture<T, E> immediateFailedCheckedFuture(final E e) {
        Preconditions.checkNotNull(e);
        return makeChecked(immediateFailedFuture(e), new Function<Exception, E>() { // from class: com.google.common.util.concurrent.Futures.3
            /* JADX WARN: Incorrect return type in method signature: (Ljava/lang/Exception;)TE; */
            @Override // com.google.common.base.Function
            public Exception apply(Exception exc) {
                return e;
            }
        });
    }

    public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> listenableFuture, Function<? super I, ? extends ListenableFuture<? extends O>> function) {
        return chain(listenableFuture, function, Executors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> chain(ListenableFuture<I> listenableFuture, Function<? super I, ? extends ListenableFuture<? extends O>> function, Executor executor) {
        ChainingListenableFuture chainingListenableFuture = new ChainingListenableFuture(function, listenableFuture);
        listenableFuture.addListener(chainingListenableFuture, executor);
        return chainingListenableFuture;
    }

    public static <I, O> ListenableFuture<O> compose(ListenableFuture<I> listenableFuture, Function<? super I, ? extends O> function) {
        return compose(listenableFuture, function, Executors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> compose(ListenableFuture<I> listenableFuture, final Function<? super I, ? extends O> function, Executor executor) {
        return chain(listenableFuture, new Function<I, ListenableFuture<O>>() { // from class: com.google.common.util.concurrent.Futures.4
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.base.Function
            public /* bridge */ /* synthetic */ Object apply(Object obj) {
                return apply((AnonymousClass4<I, O>) obj);
            }

            @Override // com.google.common.base.Function
            public ListenableFuture<O> apply(I i) {
                return Futures.immediateFuture(function.apply(i));
            }
        }, executor);
    }

    public static <I, O> Future<O> compose(final Future<I> future, final Function<? super I, ? extends O> function) {
        return new Future<O>() { // from class: com.google.common.util.concurrent.Futures.5
            private final Object lock = new Object();
            private boolean set = false;
            private O value = null;

            @Override // java.util.concurrent.Future
            public O get() throws ExecutionException, InterruptedException {
                return apply(future.get());
            }

            @Override // java.util.concurrent.Future
            public O get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return apply(future.get(j, timeUnit));
            }

            private O apply(I i) {
                O o;
                synchronized (this.lock) {
                    if (!this.set) {
                        this.value = (O) function.apply(i);
                        this.set = true;
                    }
                    o = this.value;
                }
                return o;
            }

            @Override // java.util.concurrent.Future
            public boolean cancel(boolean z) {
                return future.cancel(z);
            }

            @Override // java.util.concurrent.Future
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override // java.util.concurrent.Future
            public boolean isDone() {
                return future.isDone();
            }
        };
    }

    private static class ChainingListenableFuture<I, O> extends AbstractListenableFuture<O> implements Runnable {
        private final Function<? super I, ? extends ListenableFuture<? extends O>> function;
        private final UninterruptibleFuture<? extends I> inputFuture;

        private ChainingListenableFuture(Function<? super I, ? extends ListenableFuture<? extends O>> function, ListenableFuture<? extends I> listenableFuture) {
            this.function = function;
            this.inputFuture = Futures.makeUninterruptible(listenableFuture);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    final ListenableFuture<? extends O> listenableFutureApply = this.function.apply(this.inputFuture.get());
                    listenableFutureApply.addListener(new Runnable() { // from class: com.google.common.util.concurrent.Futures.ChainingListenableFuture.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                ChainingListenableFuture.this.set(Futures.makeUninterruptible(listenableFutureApply).get());
                            } catch (ExecutionException e) {
                                ChainingListenableFuture.this.setException(e.getCause());
                            }
                        }
                    }, Executors.sameThreadExecutor());
                } catch (CancellationException unused) {
                    cancel();
                } catch (ExecutionException e) {
                    setException(e.getCause());
                }
            } catch (Error e2) {
                setException(e2);
                throw e2;
            } catch (UndeclaredThrowableException e3) {
                setException(e3.getCause());
            } catch (RuntimeException e4) {
                setException(e4);
            }
        }
    }

    private static class MappingCheckedFuture<T, E extends Exception> extends AbstractCheckedFuture<T, E> {
        final Function<Exception, E> mapper;

        MappingCheckedFuture(ListenableFuture<T> listenableFuture, Function<Exception, E> function) {
            super(listenableFuture);
            this.mapper = function;
        }

        @Override // com.google.common.util.concurrent.AbstractCheckedFuture
        protected E mapException(Exception exc) {
            return this.mapper.apply(exc);
        }
    }

    private static class ListenableFutureAdapter<T> extends ForwardingFuture<T> implements ListenableFuture<T> {
        private static final Executor adapterExecutor = java.util.concurrent.Executors.newCachedThreadPool();
        private final Future<T> delegate;
        private final ExecutionList executionList = new ExecutionList();
        private final AtomicBoolean hasListeners = new AtomicBoolean(false);

        ListenableFutureAdapter(Future<T> future) {
            this.delegate = future;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.util.concurrent.ForwardingFuture, com.google.common.collect.ForwardingObject
        public Future<T> delegate() {
            return this.delegate;
        }

        @Override // com.google.common.util.concurrent.ListenableFuture
        public void addListener(Runnable runnable, Executor executor) {
            if (!this.hasListeners.get() && this.hasListeners.compareAndSet(false, true)) {
                adapterExecutor.execute(new Runnable() { // from class: com.google.common.util.concurrent.Futures.ListenableFutureAdapter.1
                    @Override // java.lang.Runnable
                    public void run() throws ExecutionException, InterruptedException {
                        try {
                            ListenableFutureAdapter.this.delegate.get();
                        } catch (InterruptedException e) {
                            throw new IllegalStateException("Adapter thread interrupted!", e);
                        } catch (CancellationException | ExecutionException unused) {
                        }
                        ListenableFutureAdapter.this.executionList.run();
                    }
                });
            }
            this.executionList.add(runnable, executor);
        }
    }
}
