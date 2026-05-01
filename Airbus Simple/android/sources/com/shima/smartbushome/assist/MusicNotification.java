package com.shima.smartbushome.assist;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;

/* loaded from: classes.dex */
public class MusicNotification {
    public static final String ACTION_Button = "com.dave.smartbushome.button";
    public static final String ACTION_notify_close = "com.dave.smartbushome.close";
    public static final String NOTICE_ID_KEY = "NOTICE_ID";
    public static final int NOTICE_ID_TYPE_0 = 2131624039;
    public static NotificationManager manager = null;
    public static Notification notification = null;
    public static boolean playcheck = false;
    public static RemoteViews remoteViews;

    @TargetApi(16)
    public static void sendResidentNoticeType0(Context context, String str) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setOngoing(true);
        builder.setPriority(2);
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.music_notify);
        remoteViews.setTextViewText(R.id.music_notify_songname, str);
        if (playcheck) {
            remoteViews.setImageViewResource(R.id.music_notify_play, R.drawable.notify_play);
        } else {
            remoteViews.setImageViewResource(R.id.music_notify_play, R.drawable.notify_pause);
        }
        remoteViews.setInt(R.id.close_iv, "setColorFilter", getIconColor());
        Intent intent = new Intent(context, (Class<?>) MainActivity.class);
        intent.putExtra(NOTICE_ID_KEY, R.string.app_name);
        intent.setFlags(268468224);
        remoteViews.setOnClickPendingIntent(R.id.notice_view_type_0, PendingIntent.getActivity(context, (int) SystemClock.uptimeMillis(), intent, 134217728));
        int iUptimeMillis = (int) SystemClock.uptimeMillis();
        Intent intent2 = new Intent(ACTION_notify_close);
        intent2.putExtra(NOTICE_ID_KEY, R.string.app_name);
        remoteViews.setOnClickPendingIntent(R.id.close_iv, PendingIntent.getBroadcast(context, iUptimeMillis, intent2, 134217728));
        Intent intent3 = new Intent(ACTION_Button);
        intent3.putExtra("button_type", 0);
        remoteViews.setOnClickPendingIntent(R.id.music_notify_back, PendingIntent.getBroadcast(context, 111, intent3, 268435456));
        intent3.putExtra("button_type", 1);
        remoteViews.setOnClickPendingIntent(R.id.music_notify_next, PendingIntent.getBroadcast(context, 222, intent3, 268435456));
        intent3.putExtra("button_type", 2);
        remoteViews.setOnClickPendingIntent(R.id.music_notify_play, PendingIntent.getBroadcast(context, 333, intent3, 268435456));
        builder.setSmallIcon(R.mipmap.ic_launcher);
    }

    public static void sendDefaultNotice(Context context, String str, String str2, @DrawableRes int i) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setOngoing(true);
        builder.setPriority(2);
        ((NotificationManager) context.getSystemService("notification")).notify(R.string.app_name, builder.setContentTitle("Campus").setContentText("It's a default notification").setSmallIcon(R.mipmap.ic_launcher).build());
    }

    public static int getIconColor() {
        return Color.parseColor("#999999");
    }

    public static void clearNotification(Context context, int i) {
        ((NotificationManager) context.getSystemService("notification")).cancel(i);
    }
}
