package com.shima.smartbushome.andserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.shima.smartbushome.DatabaseActivity;

/* loaded from: classes.dex */
public class ServerStatusReceiver extends BroadcastReceiver {
    private static final String ACTION = "com.shima.smartbushome.andserver.receiver";
    private static final String CMD_KEY = "CMD_KEY";
    private static final int CMD_VALUE_START = 1;
    private static final int CMD_VALUE_STARTED = 2;
    private static final int CMD_VALUE_STOP = 3;
    private DatabaseActivity mActivity;

    public static void serverStart(Context context) {
        sendBroadcast(context, 1);
    }

    public static void serverHasStarted(Context context) {
        sendBroadcast(context, 2);
    }

    public static void serverStop(Context context) {
        sendBroadcast(context, 3);
    }

    private static void sendBroadcast(Context context, int i) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(CMD_KEY, i);
        context.sendBroadcast(intent);
    }

    public ServerStatusReceiver(DatabaseActivity databaseActivity) {
        this.mActivity = databaseActivity;
    }

    public void register() {
        this.mActivity.registerReceiver(this, new IntentFilter(ACTION));
    }

    public void unRegister() {
        this.mActivity.unregisterReceiver(this);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            switch (intent.getIntExtra(CMD_KEY, 0)) {
                case 1:
                    this.mActivity.serverStart();
                    break;
                case 2:
                    this.mActivity.serverHasStarted();
                    break;
                case 3:
                    this.mActivity.serverStop();
                    break;
            }
        }
    }
}
