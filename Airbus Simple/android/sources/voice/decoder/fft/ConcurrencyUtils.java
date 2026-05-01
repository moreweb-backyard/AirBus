package voice.decoder.fft;

import java.lang.Thread;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/* loaded from: classes.dex */
public class ConcurrencyUtils {
    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool(new CustomThreadFactory(new CustomExceptionHandler()));
    private static int THREADS_BEGIN_N_1D_FFT_2THREADS = 8192;
    private static int THREADS_BEGIN_N_1D_FFT_4THREADS = 65536;
    private static int THREADS_BEGIN_N_2D = 65536;
    private static int THREADS_BEGIN_N_3D = 65536;
    private static int NTHREADS = prevPow2(getNumberOfProcessors());

    public static boolean isPowerOf2(int i) {
        return i > 0 && (i & (i + (-1))) == 0;
    }

    private ConcurrencyUtils() {
    }

    private static class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
        private CustomExceptionHandler() {
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            th.printStackTrace();
        }
    }

    private static class CustomThreadFactory implements ThreadFactory {
        private static final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        private final Thread.UncaughtExceptionHandler handler;

        CustomThreadFactory(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.handler = uncaughtExceptionHandler;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread threadNewThread = defaultFactory.newThread(runnable);
            threadNewThread.setUncaughtExceptionHandler(this.handler);
            return threadNewThread;
        }
    }

    public static int getNumberOfProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static int getNumberOfThreads() {
        return NTHREADS;
    }

    public static void setNumberOfThreads(int i) {
        NTHREADS = prevPow2(i);
    }

    public static int getThreadsBeginN_1D_FFT_2Threads() {
        return THREADS_BEGIN_N_1D_FFT_2THREADS;
    }

    public static int getThreadsBeginN_1D_FFT_4Threads() {
        return THREADS_BEGIN_N_1D_FFT_4THREADS;
    }

    public static int getThreadsBeginN_2D() {
        return THREADS_BEGIN_N_2D;
    }

    public static int getThreadsBeginN_3D() {
        return THREADS_BEGIN_N_3D;
    }

    public static void setThreadsBeginN_1D_FFT_2Threads(int i) {
        if (i < 512) {
            THREADS_BEGIN_N_1D_FFT_2THREADS = 512;
        } else {
            THREADS_BEGIN_N_1D_FFT_2THREADS = i;
        }
    }

    public static void setThreadsBeginN_1D_FFT_4Threads(int i) {
        if (i < 512) {
            THREADS_BEGIN_N_1D_FFT_4THREADS = 512;
        } else {
            THREADS_BEGIN_N_1D_FFT_4THREADS = i;
        }
    }

    public static void setThreadsBeginN_2D(int i) {
        THREADS_BEGIN_N_2D = i;
    }

    public static void setThreadsBeginN_3D(int i) {
        THREADS_BEGIN_N_3D = i;
    }

    public static void resetThreadsBeginN_FFT() {
        THREADS_BEGIN_N_1D_FFT_2THREADS = 8192;
        THREADS_BEGIN_N_1D_FFT_4THREADS = 65536;
    }

    public static void resetThreadsBeginN() {
        THREADS_BEGIN_N_2D = 65536;
        THREADS_BEGIN_N_3D = 65536;
    }

    public static int nextPow2(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("x must be greater or equal 1");
        }
        if (((i - 1) & i) == 0) {
            return i;
        }
        int i2 = i | (i >>> 1);
        int i3 = i2 | (i2 >>> 2);
        int i4 = i3 | (i3 >>> 4);
        int i5 = i4 | (i4 >>> 8);
        int i6 = i5 | (i5 >>> 16);
        return (i6 | (i6 >>> 32)) + 1;
    }

    public static int prevPow2(int i) {
        if (i < 1) {
            throw new IllegalArgumentException("x must be greater or equal 1");
        }
        return (int) Math.pow(2.0d, Math.floor(Math.log(i) / Math.log(2.0d)));
    }

    public static void sleep(long j) throws InterruptedException {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Future<?> submit(Runnable runnable) {
        return THREAD_POOL.submit(runnable);
    }

    public static void waitForCompletion(Future<?>[] futureArr) throws ExecutionException, InterruptedException {
        for (Future<?> future : futureArr) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            } catch (ExecutionException e2) {
                e2.printStackTrace();
                return;
            }
        }
    }
}
