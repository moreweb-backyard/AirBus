package com.shima.smartbushome.assist.AutoUpdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.shima.smartbushome.R;
import java.io.File;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class UpdateService extends Service {
    public static final String ACTION = "me.shenfan.UPDATE_APP";
    public static boolean DEBUG = false;
    public static final int DEFAULT_RES_ID = -1;
    private static final String DOWNLOAD_ERROR_NOTIFICATION_FLAG = "downloadErrorNotificationFlag";
    private static final String DOWNLOAD_NOTIFICATION_FLAG = "downloadNotificationFlag";
    private static final String DOWNLOAD_SUCCESS_NOTIFICATION_FLAG = "downloadSuccessNotificationFlag";
    private static final String ICO_RES_ID = "icoResId";
    private static final String ICO_SMALL_RES_ID = "icoSmallResId";
    private static final String IS_SEND_BROADCAST = "isSendBroadcast";
    public static final String PROGRESS = "progress";
    public static final String STATUS = "status";
    private static final String STORE_DIR = "storeDir";
    public static final String TAG = "UpdateService";
    public static final int UPDATE_ERROR_STATUS = -1;
    public static final int UPDATE_NUMBER_SIZE = 1;
    private static final String UPDATE_PROGRESS = "updateProgress";
    public static final int UPDATE_PROGRESS_STATUS = 0;
    public static final int UPDATE_SUCCESS_STATUS = 1;
    private static final String URL = "downloadUrl";
    private String appName;
    private NotificationCompat.Builder builder;
    private DownloadApk downloadApkTask;
    private int downloadErrorNotificationFlag;
    private int downloadNotificationFlag;
    private int downloadSuccessNotificationFlag;
    private String downloadUrl;
    private int icoResId;
    private int icoSmallResId;
    private boolean isSendBroadcast;
    private int lastProgressNumber;
    private LocalBinder localBinder = new LocalBinder();
    private LocalBroadcastManager localBroadcastManager;
    private Intent localIntent;
    private NotificationManager manager;
    private int notifyId;
    private boolean startDownload;
    private String storeDir;
    private int updateProgress;
    private UpdateProgressListener updateProgressListener;

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public void setUpdateProgressListener(UpdateProgressListener updateProgressListener) {
            UpdateService.this.setUpdateProgressListener(updateProgressListener);
        }
    }

    public static void debug() {
        DEBUG = true;
    }

    private static Intent installIntent(String str) {
        Uri uriFromFile = Uri.fromFile(new File(str));
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setDataAndType(uriFromFile, "application/vnd.android.package-archive");
        return intent;
    }

    private static Intent webLauncher(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setFlags(268435456);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSaveFileName(String str) {
        return (str == null || TextUtils.isEmpty(str)) ? "noName.apk" : str.substring(str.lastIndexOf("/"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File getDownloadDir(UpdateService updateService) {
        File file;
        if (Environment.getExternalStorageState().equals("mounted")) {
            if (updateService.storeDir != null) {
                file = new File(Environment.getExternalStorageDirectory(), updateService.storeDir);
            } else {
                file = new File(updateService.getExternalCacheDir(), "update");
            }
        } else {
            file = new File(updateService.getCacheDir(), "update");
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.appName = getApplicationName();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (!this.startDownload && intent != null) {
            this.startDownload = true;
            this.downloadUrl = intent.getStringExtra(URL);
            this.icoResId = intent.getIntExtra(ICO_RES_ID, -1);
            this.icoSmallResId = intent.getIntExtra(ICO_SMALL_RES_ID, -1);
            this.storeDir = intent.getStringExtra(STORE_DIR);
            this.updateProgress = intent.getIntExtra(UPDATE_PROGRESS, 1);
            this.downloadNotificationFlag = intent.getIntExtra(DOWNLOAD_NOTIFICATION_FLAG, 0);
            this.downloadErrorNotificationFlag = intent.getIntExtra(DOWNLOAD_ERROR_NOTIFICATION_FLAG, 0);
            this.downloadSuccessNotificationFlag = intent.getIntExtra(DOWNLOAD_SUCCESS_NOTIFICATION_FLAG, 0);
            this.isSendBroadcast = intent.getBooleanExtra(IS_SEND_BROADCAST, false);
            if (DEBUG) {
                Log.d(TAG, "downloadUrl: " + this.downloadUrl);
                Log.d(TAG, "icoResId: " + this.icoResId);
                Log.d(TAG, "icoSmallResId: " + this.icoSmallResId);
                Log.d(TAG, "storeDir: " + this.storeDir);
                Log.d(TAG, "updateProgress: " + this.updateProgress);
                Log.d(TAG, "downloadNotificationFlag: " + this.downloadNotificationFlag);
                Log.d(TAG, "downloadErrorNotificationFlag: " + this.downloadErrorNotificationFlag);
                Log.d(TAG, "downloadSuccessNotificationFlag: " + this.downloadSuccessNotificationFlag);
                Log.d(TAG, "isSendBroadcast: " + this.isSendBroadcast);
            }
            this.notifyId = i2;
            buildNotification();
            buildBroadcast();
            this.downloadApkTask = new DownloadApk(this);
            this.downloadApkTask.execute(this.downloadUrl);
        }
        return super.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return this.localBinder;
    }

    public void setUpdateProgressListener(UpdateProgressListener updateProgressListener) {
        this.updateProgressListener = updateProgressListener;
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (this.downloadApkTask != null) {
            this.downloadApkTask.cancel(true);
        }
        if (this.updateProgressListener != null) {
            this.updateProgressListener = null;
        }
        this.localIntent = null;
        this.builder = null;
        super.onDestroy();
    }

    public String getApplicationName() throws PackageManager.NameNotFoundException {
        PackageManager packageManager;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            try {
                applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException unused) {
            }
        } catch (PackageManager.NameNotFoundException unused2) {
            packageManager = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    private void buildBroadcast() {
        if (this.isSendBroadcast) {
            this.localBroadcastManager = LocalBroadcastManager.getInstance(this);
            this.localIntent = new Intent(ACTION);
        }
    }

    private void sendLocalBroadcast(int i, int i2) {
        if (!this.isSendBroadcast || this.localIntent == null) {
            return;
        }
        this.localIntent.putExtra("status", i);
        this.localIntent.putExtra("progress", i2);
        this.localBroadcastManager.sendBroadcast(this.localIntent);
    }

    private void buildNotification() {
        this.manager = (NotificationManager) getSystemService("notification");
        this.builder = new NotificationCompat.Builder(this);
        this.builder.setContentTitle(getString(R.string.update_app_model_prepare, new Object[]{this.appName})).setWhen(System.currentTimeMillis()).setProgress(100, 1, false).setSmallIcon(this.icoSmallResId).setLargeIcon(BitmapFactory.decodeResource(getResources(), this.icoResId)).setDefaults(this.downloadNotificationFlag);
        this.manager.notify(this.notifyId, this.builder.build());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void start() {
        this.builder.setContentTitle(this.appName);
        this.builder.setContentText(getString(R.string.update_app_model_prepare, new Object[]{1}));
        this.manager.notify(this.notifyId, this.builder.build());
        sendLocalBroadcast(0, 1);
        if (this.updateProgressListener != null) {
            this.updateProgressListener.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update(int i) {
        if (i - this.lastProgressNumber > this.updateProgress) {
            this.lastProgressNumber = i;
            this.builder.setProgress(100, i, false);
            this.builder.setContentText(getString(R.string.update_app_model_progress, new Object[]{Integer.valueOf(i), "%"}));
            this.manager.notify(this.notifyId, this.builder.build());
            sendLocalBroadcast(0, i);
            if (this.updateProgressListener != null) {
                this.updateProgressListener.update(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void success(String str) {
        this.builder.setProgress(0, 0, false);
        this.builder.setContentText(getString(R.string.update_app_model_success));
        Intent intentInstallIntent = installIntent(str);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intentInstallIntent, 134217728);
        this.builder.setContentIntent(activity);
        this.builder.setDefaults(this.downloadSuccessNotificationFlag);
        Notification notificationBuild = this.builder.build();
        notificationBuild.contentIntent = activity;
        this.manager.notify(this.notifyId, notificationBuild);
        sendLocalBroadcast(1, 100);
        if (this.updateProgressListener != null) {
            this.updateProgressListener.success();
        }
        startActivity(intentInstallIntent);
        stopSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void error() {
        PendingIntent activity = PendingIntent.getActivity(this, 0, webLauncher(this.downloadUrl), 134217728);
        this.builder.setContentText(getString(R.string.update_app_model_error));
        this.builder.setContentIntent(activity);
        this.builder.setProgress(0, 0, false);
        this.builder.setDefaults(this.downloadErrorNotificationFlag);
        Notification notificationBuild = this.builder.build();
        notificationBuild.contentIntent = activity;
        this.manager.notify(this.notifyId, notificationBuild);
        sendLocalBroadcast(-1, -1);
        if (this.updateProgressListener != null) {
            this.updateProgressListener.error();
        }
        stopSelf();
    }

    private static class DownloadApk extends AsyncTask<String, Integer, String> {
        private WeakReference<UpdateService> updateServiceWeakReference;

        public DownloadApk(UpdateService updateService) {
            this.updateServiceWeakReference = new WeakReference<>(updateService);
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            UpdateService updateService = this.updateServiceWeakReference.get();
            if (updateService != null) {
                updateService.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:102:0x0146 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:105:0x0120 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:107:0x013c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:115:? A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:67:0x011b  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x0137  */
        /* JADX WARN: Removed duplicated region for block: B:98:0x012a A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r5v1 */
        /* JADX WARN: Type inference failed for: r5v3 */
        /* JADX WARN: Type inference failed for: r5v4 */
        /* JADX WARN: Type inference failed for: r5v5, types: [java.io.FileOutputStream] */
        /* JADX WARN: Type inference failed for: r5v7 */
        /* JADX WARN: Type inference failed for: r5v8 */
        /* JADX WARN: Type inference failed for: r5v9, types: [java.io.FileOutputStream] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.String doInBackground(java.lang.String... r11) throws java.lang.Throwable {
            /*
                Method dump skipped, instructions count: 335
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.assist.AutoUpdate.UpdateService.DownloadApk.doInBackground(java.lang.String[]):java.lang.String");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onProgressUpdate(Integer... numArr) {
            super.onProgressUpdate((Object[]) numArr);
            if (UpdateService.DEBUG) {
                Log.d(UpdateService.TAG, "current progress is " + numArr[0]);
            }
            UpdateService updateService = this.updateServiceWeakReference.get();
            if (updateService != null) {
                updateService.update(numArr[0].intValue());
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(String str) {
            super.onPostExecute((DownloadApk) str);
            UpdateService updateService = this.updateServiceWeakReference.get();
            if (updateService != null) {
                if (str != null) {
                    updateService.success(str);
                } else {
                    updateService.error();
                }
            }
        }
    }

    public static class Builder {
        private int downloadErrorNotificationFlag;
        private int downloadNotificationFlag;
        private int downloadSuccessNotificationFlag;
        private String downloadUrl;
        private boolean isSendBroadcast;
        private String storeDir;
        private int icoResId = -1;
        private int icoSmallResId = -1;
        private int updateProgress = 1;

        protected Builder(String str) {
            this.downloadUrl = str;
        }

        public static Builder create(String str) {
            if (str == null) {
                throw new NullPointerException("downloadUrl == null");
            }
            return new Builder(str);
        }

        public String getDownloadUrl() {
            return this.downloadUrl;
        }

        public int getIcoResId() {
            return this.icoResId;
        }

        public Builder setIcoResId(int i) {
            this.icoResId = i;
            return this;
        }

        public int getIcoSmallResId() {
            return this.icoSmallResId;
        }

        public Builder setIcoSmallResId(int i) {
            this.icoSmallResId = i;
            return this;
        }

        public int getUpdateProgress() {
            return this.updateProgress;
        }

        public Builder setUpdateProgress(int i) {
            if (i < 1) {
                throw new IllegalArgumentException("updateProgress < 1");
            }
            this.updateProgress = i;
            return this;
        }

        public String getStoreDir() {
            return this.storeDir;
        }

        public Builder setStoreDir(String str) {
            this.storeDir = str;
            return this;
        }

        public int getDownloadNotificationFlag() {
            return this.downloadNotificationFlag;
        }

        public Builder setDownloadNotificationFlag(int i) {
            this.downloadNotificationFlag = i;
            return this;
        }

        public int getDownloadSuccessNotificationFlag() {
            return this.downloadSuccessNotificationFlag;
        }

        public Builder setDownloadSuccessNotificationFlag(int i) {
            this.downloadSuccessNotificationFlag = i;
            return this;
        }

        public int getDownloadErrorNotificationFlag() {
            return this.downloadErrorNotificationFlag;
        }

        public Builder setDownloadErrorNotificationFlag(int i) {
            this.downloadErrorNotificationFlag = i;
            return this;
        }

        public boolean isSendBroadcast() {
            return this.isSendBroadcast;
        }

        public Builder setIsSendBroadcast(boolean z) {
            this.isSendBroadcast = z;
            return this;
        }

        public Builder build(Context context) {
            if (context == null) {
                throw new NullPointerException("context == null");
            }
            Intent intent = new Intent();
            intent.setClass(context, UpdateService.class);
            intent.putExtra(UpdateService.URL, this.downloadUrl);
            if (this.icoResId == -1) {
                this.icoResId = getIcon(context);
            }
            if (this.icoSmallResId == -1) {
                this.icoSmallResId = this.icoResId;
            }
            intent.putExtra(UpdateService.ICO_RES_ID, this.icoResId);
            intent.putExtra(UpdateService.STORE_DIR, this.storeDir);
            intent.putExtra(UpdateService.ICO_SMALL_RES_ID, this.icoSmallResId);
            intent.putExtra(UpdateService.UPDATE_PROGRESS, this.updateProgress);
            intent.putExtra(UpdateService.DOWNLOAD_NOTIFICATION_FLAG, this.downloadNotificationFlag);
            intent.putExtra(UpdateService.DOWNLOAD_SUCCESS_NOTIFICATION_FLAG, this.downloadSuccessNotificationFlag);
            intent.putExtra(UpdateService.DOWNLOAD_ERROR_NOTIFICATION_FLAG, this.downloadErrorNotificationFlag);
            intent.putExtra(UpdateService.IS_SEND_BROADCAST, this.isSendBroadcast);
            context.startService(intent);
            return this;
        }

        private int getIcon(Context context) throws PackageManager.NameNotFoundException {
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                applicationInfo = null;
            }
            if (applicationInfo != null) {
                return applicationInfo.icon;
            }
            return 0;
        }
    }
}
