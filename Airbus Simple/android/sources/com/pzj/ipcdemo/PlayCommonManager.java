package com.pzj.ipcdemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.pzj.ipcdemo.utils.AudioPlayer;
import com.pzj.ipcdemo.utils.CustomBuffer;
import com.pzj.ipcdemo.utils.CustomBufferData;
import com.pzj.ipcdemo.utils.CustomBufferHead;
import com.shima.smartbushome.assist.CrashHandle.DateUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class PlayCommonManager implements MediaScannerConnection.MediaScannerConnectionClient {
    private static final int AUDIO_BUFFER_START_CODE = 16711935;
    private static final int DRAG = 1;
    private static final int NONE = 0;
    public static final int PLAY_MODE_LIVE = 0;
    public static final int PLAY_MODE_RECORDED_SDCARD = 1;
    private static final int ZOOM = 2;
    private CustomBuffer AudioBuffer;
    private Activity activity;
    private AudioPlayer audioPlayer;
    float baseValue;
    private RelativeLayout bottomView;
    private Animation dismissAnim;
    private Animation dismissTopAnim;
    private MediaScannerConnection mediaScannerConnection;
    private float oldDist;
    float originalScale;
    private int playMode;
    private Animation showAnim;
    private Animation showTopAnim;
    private File snapshotFile;
    private String snapshotSaveFileName;
    private String snapshotSaveFolderName;
    private String strDID;
    private RelativeLayout topbg;
    private File videoFile;
    private ImageView videoView;
    private boolean isPictSave = false;
    private boolean isBackPressed = false;
    private BitmapDrawable drawable = null;
    private boolean isUpDownPressed = false;
    private boolean isShowtoping = false;
    private boolean isDown = false;
    private boolean isSecondDown = false;
    private float x1 = 0.0f;
    private float x2 = 0.0f;
    private float y1 = 0.0f;
    private float y2 = 0.0f;
    private int mode = 0;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float mMaxZoom = 2.0f;
    float mMinZoom = 0.3125f;
    protected Matrix mBaseMatrix = new Matrix();
    protected Matrix mSuppMatrix = new Matrix();
    private Matrix mDisplayMatrix = new Matrix();
    private final float[] mMatrixValues = new float[9];

    public String getTimeZone(int i) {
        switch (i) {
            case -43200:
                return "GMT+12:00";
            case -39600:
                return "GMT+11:00";
            case -36000:
                return "GMT+10:00";
            case -34200:
                return "GMT+09:30";
            case -32400:
                return "GMT+09:00";
            case -28800:
                return "GMT+08:00";
            case -25200:
                return "GMT+07:00";
            case -21600:
                return "GMT+06:00";
            case -19800:
                return "GMT+05:30";
            case -18000:
                return "GMT+05:00";
            case -16200:
                return "GMT+04:30";
            case -14400:
                return "GMT+04:00";
            case -12600:
                return "GMT+03:30";
            case -10800:
                return "GMT+03:00";
            case -7200:
                return "GMT+02:00";
            case -3600:
                return "GMT+01:00";
            case 0:
                return "GMT";
            case 3600:
                return "GMT-01:00";
            case 7200:
                return "GMT-02:00";
            case 10800:
                return "GMT-03:00";
            case 12600:
                return "GMT-03:30";
            case 14400:
                return "GMT-04:00";
            case 18000:
                return "GMT-05:00";
            case 21600:
                return "GMT-06:00";
            case 25200:
                return "GMT-07:00";
            case 28800:
                return "GMT-08:00";
            case 32400:
                return "GMT-09:00";
            case 36000:
                return "GMT-10:00";
            case 39600:
                return "GMT-11:00";
            default:
                return null;
        }
    }

    protected void onResume() {
    }

    public PlayCommonManager(Activity activity, ImageView imageView, int i, String str) {
        this.videoView = null;
        this.AudioBuffer = null;
        this.audioPlayer = null;
        this.snapshotSaveFolderName = "";
        this.snapshotSaveFileName = "";
        this.activity = activity;
        this.playMode = i;
        this.strDID = str;
        this.videoView = imageView;
        this.snapshotSaveFolderName = "wer";
        this.snapshotSaveFileName = "wer";
        this.AudioBuffer = new CustomBuffer();
        this.audioPlayer = new AudioPlayer(this.AudioBuffer);
        initViews();
        this.dismissTopAnim = AnimationUtils.loadAnimation(activity, activity.getResources().getIdentifier("ptz_top_anim_dismiss", "anim", activity.getPackageName()));
        this.showTopAnim = AnimationUtils.loadAnimation(activity, activity.getResources().getIdentifier("ptz_top_anim_show", "anim", activity.getPackageName()));
        this.showAnim = AnimationUtils.loadAnimation(activity, activity.getResources().getIdentifier("ptz_otherset_anim_show", "anim", activity.getPackageName()));
        this.dismissAnim = AnimationUtils.loadAnimation(activity, activity.getResources().getIdentifier("ptz_otherset_anim_dismiss", "anim", activity.getPackageName()));
    }

    public void initViews() {
        this.bottomView = (RelativeLayout) this.activity.findViewById(R.id.bottom_view);
        this.topbg = (RelativeLayout) this.activity.findViewById(R.id.top_bg);
        this.drawable = new BitmapDrawable(BitmapFactory.decodeResource(this.activity.getResources(), this.activity.getResources().getIdentifier("top_bg", "drawable", this.activity.getPackageName())));
        this.drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        this.drawable.setDither(true);
        this.topbg.setBackgroundDrawable(this.drawable);
        this.bottomView.setBackgroundDrawable(this.drawable);
    }

    private void showTop() {
        if (this.isShowtoping) {
            this.isShowtoping = false;
            this.topbg.setVisibility(8);
            this.topbg.startAnimation(this.dismissTopAnim);
        } else {
            this.isShowtoping = true;
            this.topbg.setVisibility(0);
            this.topbg.startAnimation(this.showTopAnim);
        }
    }

    private void showBottom() {
        if (this.isUpDownPressed) {
            this.isUpDownPressed = false;
            this.bottomView.startAnimation(this.dismissAnim);
            this.bottomView.setVisibility(8);
        } else {
            this.isUpDownPressed = true;
            this.bottomView.startAnimation(this.showAnim);
            this.bottomView.setVisibility(0);
        }
    }

    public void onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            this.isBackPressed = true;
        }
    }

    public void onTouch(View view, MotionEvent motionEvent) {
        if (!this.isDown) {
            this.x1 = motionEvent.getX();
            this.y1 = motionEvent.getY();
            this.isDown = true;
        }
        switch (motionEvent.getAction() & 255) {
            case 0:
                this.savedMatrix.set(this.matrix);
                this.start.set(motionEvent.getX(), motionEvent.getY());
                this.mode = 1;
                this.originalScale = getScale();
                break;
            case 1:
                if (Math.abs(this.x1 - this.x2) < 25.0f && Math.abs(this.y1 - this.y2) < 25.0f) {
                    int i = this.playMode;
                    if (!this.isSecondDown) {
                        showTop();
                        showBottom();
                    }
                    this.isSecondDown = false;
                }
                this.x1 = 0.0f;
                this.x2 = 0.0f;
                this.y1 = 0.0f;
                this.y2 = 0.0f;
                this.isDown = false;
                break;
            case 5:
                this.isSecondDown = true;
                break;
        }
    }

    protected void zoomTo(float f, float f2, float f3) {
        Log.d("zoomTo", "zoomTo scale:" + f);
        if (f > this.mMaxZoom) {
            f = this.mMaxZoom;
        } else if (f < this.mMinZoom) {
            f = this.mMinZoom;
        }
        float scale = f / getScale();
        Log.d("deltaScale", "deltaScale:" + scale);
        this.mSuppMatrix.postScale(scale, scale, f2, f3);
        this.videoView.setScaleType(ImageView.ScaleType.MATRIX);
        this.videoView.setImageMatrix(getImageViewMatrix());
    }

    protected Matrix getImageViewMatrix() {
        this.mDisplayMatrix.set(this.mBaseMatrix);
        this.mDisplayMatrix.postConcat(this.mSuppMatrix);
        return this.mDisplayMatrix;
    }

    protected float getScale(Matrix matrix) {
        return getValue(matrix, 0);
    }

    protected float getScale() {
        return getScale(this.mSuppMatrix);
    }

    protected float getValue(Matrix matrix, int i) {
        matrix.getValues(this.mMatrixValues);
        return this.mMatrixValues[i];
    }

    private void midPoint(PointF pointF, MotionEvent motionEvent) {
        pointF.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
    }

    protected void onPause() {
        this.activity.finish();
    }

    protected void onDestroy() {
        if (this.playMode == 0) {
            NativeCaller.StopPPPPLivestream(this.strDID);
        } else if (this.playMode == 1) {
            NativeCaller.StopPlayBack(this.strDID);
        }
        StopAudio();
        boolean z = this.isBackPressed;
        this.isBackPressed = false;
    }

    public void downloadVideo() {
        if (this.mediaScannerConnection == null) {
            this.mediaScannerConnection = new MediaScannerConnection(this.activity, this);
        }
        String str = Environment.getExternalStorageDirectory() + "/ipcam/" + this.strDID + "_" + getStrDate() + ".mp4";
        File file = new File(Environment.getExternalStorageDirectory(), "ipcam");
        if (!file.exists()) {
            file.mkdirs();
        }
        NativeCaller.StrarRecordPlayBack(this.strDID, str);
    }

    public void onDownloadVideoFinished() {
        if (!this.mediaScannerConnection.isConnected()) {
            this.mediaScannerConnection.connect();
        } else if (this.videoFile != null) {
            this.mediaScannerConnection.scanFile(this.videoFile.getAbsolutePath(), null);
            this.videoFile = null;
        }
    }

    public boolean canTakePhoto() {
        return existSdcard();
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.pzj.ipcdemo.PlayCommonManager$1] */
    public void takePicture(final Bitmap bitmap) {
        if (this.mediaScannerConnection == null) {
            this.mediaScannerConnection = new MediaScannerConnection(this.activity, this);
        }
        if (this.isPictSave) {
            return;
        }
        this.isPictSave = true;
        new Thread() { // from class: com.pzj.ipcdemo.PlayCommonManager.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                PlayCommonManager.this.savePicToSDcard(bitmap);
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ad A[Catch: all -> 0x00d4, TryCatch #2 {, blocks: (B:3:0x0001, B:12:0x0062, B:13:0x0064, B:31:0x00a5, B:33:0x00ad, B:34:0x00b3, B:36:0x00b7, B:16:0x0069, B:25:0x009b, B:27:0x009f, B:40:0x00c7, B:42:0x00cb, B:46:0x00d3, B:45:0x00d0), top: B:54:0x0001, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00b3 A[Catch: all -> 0x00d4, TryCatch #2 {, blocks: (B:3:0x0001, B:12:0x0062, B:13:0x0064, B:31:0x00a5, B:33:0x00ad, B:34:0x00b3, B:36:0x00b7, B:16:0x0069, B:25:0x009b, B:27:0x009f, B:40:0x00c7, B:42:0x00cb, B:46:0x00d3, B:45:0x00d0), top: B:54:0x0001, inners: #5 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00cb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void savePicToSDcard(android.graphics.Bitmap r8) {
        /*
            r7 = this;
            monitor-enter(r7)
            java.lang.String r0 = r7.getStrDate()     // Catch: java.lang.Throwable -> Ld4
            r1 = 0
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.io.File r4 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.lang.String r5 = r7.snapshotSaveFolderName     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            r3.<init>(r4, r5)     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            boolean r4 = r3.exists()     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            if (r4 != 0) goto L1b
            r3.mkdirs()     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
        L1b:
            java.io.File r4 = new java.io.File     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            r5.<init>()     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.lang.String r6 = r7.snapshotSaveFileName     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            r5.append(r6)     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.lang.String r6 = "_"
            r5.append(r6)     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            r5.append(r0)     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.lang.String r0 = ".jpg"
            r5.append(r0)     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.lang.String r0 = r5.toString()     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            r4.<init>(r3, r0)     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            r7.snapshotFile = r4     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            java.io.File r3 = r7.snapshotFile     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            r0.<init>(r3)     // Catch: java.lang.Throwable -> L6f java.lang.Exception -> L72
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
            r4 = 100
            boolean r8 = r8.compress(r3, r4, r0)     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
            if (r8 == 0) goto L62
            r0.flush()     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
            java.lang.String r8 = "tag"
            java.lang.String r3 = "takepicture success"
            android.util.Log.d(r8, r3)     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
            android.app.Activity r8 = r7.activity     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
            com.pzj.ipcdemo.PlayCommonManager$2 r3 = new com.pzj.ipcdemo.PlayCommonManager$2     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
            r3.<init>()     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
            r8.runOnUiThread(r3)     // Catch: java.lang.Exception -> L6d java.lang.Throwable -> Lc6
        L62:
            r7.isPictSave = r1     // Catch: java.lang.Throwable -> Ld4
            r0.close()     // Catch: java.io.IOException -> L68 java.lang.Throwable -> Ld4
            goto La5
        L68:
            r8 = move-exception
        L69:
            r8.printStackTrace()     // Catch: java.lang.Throwable -> Ld4
            goto La5
        L6d:
            r8 = move-exception
            goto L74
        L6f:
            r8 = move-exception
            r0 = r2
            goto Lc7
        L72:
            r8 = move-exception
            r0 = r2
        L74:
            android.app.Activity r3 = r7.activity     // Catch: java.lang.Throwable -> Lc6
            com.pzj.ipcdemo.PlayCommonManager$3 r4 = new com.pzj.ipcdemo.PlayCommonManager$3     // Catch: java.lang.Throwable -> Lc6
            r4.<init>()     // Catch: java.lang.Throwable -> Lc6
            r3.runOnUiThread(r4)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r3 = "tag"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc6
            r4.<init>()     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r5 = "exception:"
            r4.append(r5)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r5 = r8.getMessage()     // Catch: java.lang.Throwable -> Lc6
            r4.append(r5)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> Lc6
            android.util.Log.d(r3, r4)     // Catch: java.lang.Throwable -> Lc6
            r8.printStackTrace()     // Catch: java.lang.Throwable -> Lc6
            r7.isPictSave = r1     // Catch: java.lang.Throwable -> Ld4
            if (r0 == 0) goto La5
            r0.close()     // Catch: java.io.IOException -> La3 java.lang.Throwable -> Ld4
            goto La5
        La3:
            r8 = move-exception
            goto L69
        La5:
            android.media.MediaScannerConnection r8 = r7.mediaScannerConnection     // Catch: java.lang.Throwable -> Ld4
            boolean r8 = r8.isConnected()     // Catch: java.lang.Throwable -> Ld4
            if (r8 != 0) goto Lb3
            android.media.MediaScannerConnection r8 = r7.mediaScannerConnection     // Catch: java.lang.Throwable -> Ld4
            r8.connect()     // Catch: java.lang.Throwable -> Ld4
            goto Lc4
        Lb3:
            java.io.File r8 = r7.snapshotFile     // Catch: java.lang.Throwable -> Ld4
            if (r8 == 0) goto Lc4
            android.media.MediaScannerConnection r8 = r7.mediaScannerConnection     // Catch: java.lang.Throwable -> Ld4
            java.io.File r0 = r7.snapshotFile     // Catch: java.lang.Throwable -> Ld4
            java.lang.String r0 = r0.getAbsolutePath()     // Catch: java.lang.Throwable -> Ld4
            r8.scanFile(r0, r2)     // Catch: java.lang.Throwable -> Ld4
            r7.snapshotFile = r2     // Catch: java.lang.Throwable -> Ld4
        Lc4:
            monitor-exit(r7)
            return
        Lc6:
            r8 = move-exception
        Lc7:
            r7.isPictSave = r1     // Catch: java.lang.Throwable -> Ld4
            if (r0 == 0) goto Ld3
            r0.close()     // Catch: java.io.IOException -> Lcf java.lang.Throwable -> Ld4
            goto Ld3
        Lcf:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Ld4
        Ld3:
            throw r8     // Catch: java.lang.Throwable -> Ld4
        Ld4:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.pzj.ipcdemo.PlayCommonManager.savePicToSDcard(android.graphics.Bitmap):void");
    }

    private String getStrDate() {
        return new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
    }

    public String getDeviceTime(long j, String str) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(str));
        calendar.setTimeInMillis(j);
        calendar.get(13);
        return DateFormat.getDateFormat(this.activity).format(calendar.getTime()) + "   " + new SimpleDateFormat(DateFormat.is24HourFormat(this.activity) ? DateUtil.DEFAULT_FORMAT_TIME : "hh:mm:ss a", Locale.getDefault()).format(calendar.getTime()).replace("am", "AM").replace("pm", "PM");
    }

    @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
    public void onMediaScannerConnected() {
        if (this.snapshotFile != null) {
            this.mediaScannerConnection.scanFile(this.snapshotFile.getAbsolutePath(), null);
        }
        if (this.videoFile != null) {
            this.mediaScannerConnection.scanFile(this.videoFile.getAbsolutePath(), null);
        }
    }

    @Override // android.media.MediaScannerConnection.OnScanCompletedListener
    public void onScanCompleted(String str, Uri uri) {
        this.mediaScannerConnection.disconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToast(String str) {
        Toast.makeText(this.activity, str, 1).show();
    }

    public void addAudioData(byte[] bArr, int i) {
        CustomBufferHead customBufferHead = new CustomBufferHead();
        CustomBufferData customBufferData = new CustomBufferData();
        customBufferHead.length = i;
        customBufferHead.startcode = AUDIO_BUFFER_START_CODE;
        customBufferData.head = customBufferHead;
        customBufferData.data = bArr;
        this.AudioBuffer.addData(customBufferData);
    }

    public boolean isAudioPlaying() {
        return this.audioPlayer.isAudioPlaying();
    }

    public void StartAudio() {
        synchronized (this) {
            this.AudioBuffer.ClearAll();
            this.audioPlayer.AudioPlayStart();
            if (this.playMode == 0) {
                NativeCaller.PPPPStartAudio(this.strDID);
            }
        }
    }

    public void StopAudio() {
        synchronized (this) {
            this.audioPlayer.AudioPlayStop();
            this.AudioBuffer.ClearAll();
            if (this.playMode == 0) {
                NativeCaller.PPPPStopAudio(this.strDID);
            }
        }
    }

    public String getSysteTotalMemorySize(Context context) {
        ActivityManager activityManager = (ActivityManager) this.activity.getSystemService("activity");
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return Formatter.formatFileSize(context, memoryInfo.totalMem);
    }

    public static String getAvailableInternalMemorySize(Context context) {
        File dataDirectory = Environment.getDataDirectory();
        Log.i("zzz", dataDirectory.getAbsolutePath());
        StatFs statFs = new StatFs(dataDirectory.getPath());
        return Formatter.formatFileSize(context, statFs.getAvailableBlocks() * statFs.getBlockSize());
    }

    public static String getTotalInternalMemorySize(Context context) {
        File dataDirectory = Environment.getDataDirectory();
        Log.i("zzz", dataDirectory.getAbsolutePath());
        StatFs statFs = new StatFs(dataDirectory.getPath());
        return Formatter.formatFileSize(context, statFs.getBlockCount() * statFs.getBlockSize());
    }

    public static String getDiskCacheDir(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            path = context.getExternalCacheDir().getPath();
        } else {
            path = context.getCacheDir().getPath();
        }
        createFile(path);
        return path;
    }

    public static File createFile(String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean existSdcard() {
        return Environment.getExternalStorageState().equals("mounted");
    }
}
