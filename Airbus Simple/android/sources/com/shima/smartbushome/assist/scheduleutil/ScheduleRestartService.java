package com.shima.smartbushome.assist.scheduleutil;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import com.shima.smartbushome.StrongService;

/* loaded from: classes.dex */
public class ScheduleRestartService extends Service {
    private Handler handler = new Handler() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleRestartService.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            ScheduleRestartService.this.startService1();
        }
    };
    private StrongService startS1 = new StrongService.Stub() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleRestartService.2
        @Override // com.shima.smartbushome.StrongService
        public void stopService() throws RemoteException {
            ScheduleRestartService.this.getBaseContext().stopService(new Intent(ScheduleRestartService.this.getBaseContext(), (Class<?>) ScheduleServer.class));
        }

        @Override // com.shima.smartbushome.StrongService
        public void startService() throws RemoteException {
            ScheduleRestartService.this.getBaseContext().startService(new Intent(ScheduleRestartService.this.getBaseContext(), (Class<?>) ScheduleServer.class));
        }
    };

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    @Override // android.app.Service, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        startService1();
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.shima.smartbushome.assist.scheduleutil.ScheduleRestartService$3] */
    @Override // android.app.Service
    @SuppressLint({"NewApi"})
    public void onCreate() {
        startService1();
        new Thread() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleRestartService.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws InterruptedException {
                while (true) {
                    if (!scheduleUtils.isServiceWork(ScheduleRestartService.this, "com.dave.smartbushome.assist.scheduleutil.ScheduleServer")) {
                        Message messageObtain = Message.obtain();
                        messageObtain.what = 1;
                        ScheduleRestartService.this.handler.sendMessage(messageObtain);
                    }
                    try {
                        Thread.sleep(1L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startService1() {
        if (scheduleUtils.isServiceWork(this, "com.dave.smartbushome.assist.scheduleutil.ScheduleServer")) {
            return;
        }
        try {
            this.startS1.startService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return (IBinder) this.startS1;
    }
}
