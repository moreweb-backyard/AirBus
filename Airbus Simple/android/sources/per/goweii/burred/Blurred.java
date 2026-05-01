package per.goweii.burred;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public final class Blurred {
    private static Blurred INSTANCE = null;
    private static final int MODE_NONE = 0;
    private static final int MODE_PERCENT = 1;
    private static final int MODE_RADIUS = 2;
    private final IBlur mBlur;
    private final ExecutorService mExecutor;
    private Bitmap mOriginalBitmap = null;
    private int mMode = 0;
    private float mPercent = 0.0f;
    private float mRadius = 0.0f;
    private float mScale = 0.0f;
    private boolean mKeepSize = false;
    private boolean mRecycleOriginal = false;
    private Callback mCallback = null;
    private Handler mCallbackHandler = null;

    public interface Callback {
        void down(Bitmap bitmap);
    }

    private Blurred(@Nullable Context context) {
        if (context != null && Build.VERSION.SDK_INT >= 17) {
            this.mBlur = GaussianBlur.get(context);
        } else {
            this.mBlur = FastBlur.get();
        }
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    public static void init(@NonNull Context context) {
        INSTANCE = new Blurred(context);
    }

    public static void recycle() {
        if (INSTANCE != null) {
            INSTANCE.mBlur.recycle();
            INSTANCE = null;
        }
    }

    public static Blurred with(@NonNull Bitmap bitmap) {
        if (INSTANCE == null) {
            INSTANCE = new Blurred(null);
        }
        INSTANCE.mOriginalBitmap = bitmap;
        return INSTANCE;
    }

    public static Blurred with(@NonNull View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(true);
        view.destroyDrawingCache();
        view.setDrawingCacheQuality(524288);
        return with(view.getDrawingCache());
    }

    public Blurred percent(float f) {
        this.mMode = 1;
        this.mPercent = f;
        return this;
    }

    public Blurred radius(float f) {
        this.mMode = 2;
        this.mRadius = f;
        return this;
    }

    public Blurred scale(float f) {
        this.mScale = f;
        return this;
    }

    public Blurred keepSize(boolean z) {
        this.mKeepSize = z;
        return this;
    }

    public Blurred recycleOriginal(boolean z) {
        this.mRecycleOriginal = z;
        return this;
    }

    public Bitmap blur() {
        float fMin;
        switch (this.mMode) {
            case 1:
                fMin = Math.min(this.mOriginalBitmap.getWidth(), this.mOriginalBitmap.getHeight()) * this.mPercent;
                break;
            case 2:
                fMin = this.mRadius;
                break;
            default:
                fMin = 0.0f;
                break;
        }
        float f = fMin < 0.0f ? 0.0f : fMin;
        if (this.mScale < 1.0f) {
            this.mScale = 1.0f;
        }
        return this.mBlur.process(this.mOriginalBitmap, f, this.mScale, this.mKeepSize, this.mRecycleOriginal);
    }

    @SuppressLint({"HandlerLeak"})
    public void blur(Callback callback) {
        this.mCallback = callback;
        this.mCallbackHandler = new Handler() { // from class: per.goweii.burred.Blurred.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                Blurred.this.mCallbackHandler = null;
                Bitmap bitmap = (Bitmap) message.obj;
                if (Blurred.this.mCallback != null) {
                    Blurred.this.mCallback.down(bitmap);
                }
            }
        };
        this.mExecutor.submit(new Runnable() { // from class: per.goweii.burred.Blurred.2
            @Override // java.lang.Runnable
            public void run() {
                Message messageObtainMessage = Blurred.this.mCallbackHandler.obtainMessage();
                messageObtainMessage.obj = Blurred.this.blur();
                Blurred.this.mCallbackHandler.sendMessage(messageObtainMessage);
            }
        });
    }
}
