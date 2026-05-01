package com.shima.smartbushome.andserver;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.shima.smartbushome.database.DatabaseContext;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.AssetsWebsite;

/* loaded from: classes.dex */
public class CoreService extends Service {
    private AssetManager mAssetManager;
    private Server.Listener mListener = new Server.Listener() { // from class: com.shima.smartbushome.andserver.CoreService.1
        @Override // com.yanzhenjie.andserver.Server.Listener
        public void onError(Exception exc) {
        }

        @Override // com.yanzhenjie.andserver.Server.Listener
        public void onStarted() {
            ServerStatusReceiver.serverStart(CoreService.this);
        }

        @Override // com.yanzhenjie.andserver.Server.Listener
        public void onStopped() {
            ServerStatusReceiver.serverStop(CoreService.this);
        }
    };
    private Server mServer;

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        this.mAssetManager = getAssets();
        this.mServer = new AndServer.Build().port(8899).timeout(10000).registerHandler("smartbus_dbbackup_g4airbus", new RequestFileHandler(DatabaseContext.dbPath)).registerHandler("upload", new RequestUploadHandler()).website(new AssetsWebsite(this.mAssetManager, "")).listener(this.mListener).build().createServer();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        startServer();
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        stopServer();
    }

    private void startServer() {
        if (this.mServer != null) {
            if (this.mServer.isRunning()) {
                ServerStatusReceiver.serverHasStarted(this);
            } else {
                this.mServer.start();
            }
        }
    }

    private void stopServer() {
        if (this.mServer != null) {
            this.mServer.stop();
        }
    }
}
