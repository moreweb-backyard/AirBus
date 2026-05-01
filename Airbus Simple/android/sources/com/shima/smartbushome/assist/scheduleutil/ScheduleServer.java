package com.shima.smartbushome.assist.scheduleutil;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.litesuits.http.data.Consts;
import com.shima.smartbushome.StrongService;
import com.shima.smartbushome.assist.scheduleutil.ScheduleAlarm;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Saveschedule;
import com.shima.smartbushome.udp.udp_socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/* loaded from: classes.dex */
public class ScheduleServer extends Service {
    private String currenttime;
    private DBManager mgr;
    private udp_socket mudup;
    private Context rootcontext;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd:HHmm");
    private SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy:MM:dd");
    private Handler runschedulehandler = new Handler();
    private int schedule_count = 0;
    private List<Saveschedule> alldata = new ArrayList();
    private ScheduleAlarm alarm = new ScheduleAlarm();
    private Handler handler = new Handler() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleServer.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 1) {
                return;
            }
            ScheduleServer.this.startService2();
        }
    };
    private StrongService startS2 = new StrongService.Stub() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleServer.2
        @Override // com.shima.smartbushome.StrongService
        public void stopService() throws RemoteException {
            ScheduleServer.this.getBaseContext().stopService(new Intent(ScheduleServer.this.getBaseContext(), (Class<?>) ScheduleRestartService.class));
        }

        @Override // com.shima.smartbushome.StrongService
        public void startService() throws RemoteException {
            ScheduleServer.this.getBaseContext().startService(new Intent(ScheduleServer.this.getBaseContext(), (Class<?>) ScheduleRestartService.class));
        }
    };
    Runnable runschedule = new Runnable() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleServer.4
        @Override // java.lang.Runnable
        public void run() {
            if (ScheduleServer.this.schedule_count < ScheduleServer.this.alldata.size()) {
                if (ScheduleServer.this.currenttime.equals(((Saveschedule) ScheduleServer.this.alldata.get(ScheduleServer.this.schedule_count)).alarm_time)) {
                    if (((Saveschedule) ScheduleServer.this.alldata.get(ScheduleServer.this.schedule_count)).last_status == 0) {
                        ScheduleServer.this.alarm.start((Saveschedule) ScheduleServer.this.alldata.get(ScheduleServer.this.schedule_count), ScheduleServer.this.rootcontext, ScheduleServer.this.mgr);
                        ScheduleServer.this.alarm.setOnStatusChangeListener(new ScheduleAlarm.OnStatusChangeListener() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleServer.4.1
                            @Override // com.shima.smartbushome.assist.scheduleutil.ScheduleAlarm.OnStatusChangeListener
                            public void onStatusChange(String str) {
                                if (str.equals("done")) {
                                    ScheduleServer.access$708(ScheduleServer.this);
                                    ScheduleServer.this.runschedulehandler.postDelayed(ScheduleServer.this.runschedule, 20L);
                                }
                            }
                        });
                        return;
                    } else {
                        ScheduleServer.access$708(ScheduleServer.this);
                        ScheduleServer.this.runschedulehandler.postDelayed(ScheduleServer.this.runschedule, 20L);
                        return;
                    }
                }
                ScheduleServer.access$708(ScheduleServer.this);
                ScheduleServer.this.runschedulehandler.postDelayed(ScheduleServer.this.runschedule, 20L);
                return;
            }
            ScheduleServer.this.schedule_count = 0;
            ScheduleServer.this.runschedulehandler.removeCallbacks(ScheduleServer.this.runschedule);
        }
    };
    private String tv_repeat = "";

    static /* synthetic */ int access$708(ScheduleServer scheduleServer) {
        int i = scheduleServer.schedule_count;
        scheduleServer.schedule_count = i + 1;
        return i;
    }

    @Override // android.app.Service, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        startService2();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return (IBinder) this.startS2;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.shima.smartbushome.assist.scheduleutil.ScheduleServer$3] */
    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.rootcontext = this;
        this.mgr = new DBManager(this);
        this.mudup = new udp_socket(this);
        updateallScheduleAlarmTime();
        startService2();
        new Thread() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleServer.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() throws InterruptedException {
                while (true) {
                    if (!scheduleUtils.isServiceWork(ScheduleServer.this, "com.dave.smartbushome.assist.scheduleutil.ScheduleRestartService")) {
                        Message messageObtain = Message.obtain();
                        messageObtain.what = 1;
                        ScheduleServer.this.handler.sendMessage(messageObtain);
                    }
                    try {
                        Thread.sleep(58000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Date date = new Date(System.currentTimeMillis());
                    ScheduleServer.this.currenttime = ScheduleServer.this.formatter.format(date);
                    ScheduleServer.this.alldata.clear();
                    ScheduleServer.this.alldata = ScheduleServer.this.mgr.queryschedule();
                    ScheduleServer.this.runschedulehandler.postDelayed(ScheduleServer.this.runschedule, 20L);
                }
            }
        }.start();
        Log.i("liujun", "后台进程被创建。。。");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startService2() {
        if (scheduleUtils.isServiceWork(this, "com.dave.smartbushome.assist.scheduleutil.ScheduleRestartService")) {
            return;
        }
        try {
            this.startS2.startService();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.i("liujun", "后台进程。。。");
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.i("liujun", "后台进程被销毁了。。。");
        super.onDestroy();
    }

    private void updateallScheduleAlarmTime() {
        List<Saveschedule> listQueryschedule = this.mgr.queryschedule();
        for (int i = 0; i < listQueryschedule.size(); i++) {
            if (listQueryschedule.get(i).last_status == 0) {
                updatethisschedule(listQueryschedule.get(i));
            }
        }
    }

    private void updatethisschedule(Saveschedule saveschedule) {
        Saveschedule saveschedule2 = new Saveschedule();
        saveschedule2.schedule_id = saveschedule.schedule_id;
        saveschedule2.schedule_icon = "";
        saveschedule2.schedule_name = saveschedule.schedule_name;
        this.tv_repeat = "";
        setrepeat(saveschedule.repeat_option1, saveschedule.repeat_option2);
        saveschedule2.alarm_time = getTargettime(saveschedule.repeat_option1, saveschedule.repeat_option2, saveschedule.alarm_time.split(":")[3]);
        saveschedule2.last_status = 0;
        saveschedule2.marco_ID = saveschedule.marco_ID;
        saveschedule2.repeat_option1 = saveschedule.repeat_option1;
        saveschedule2.repeat_option2 = saveschedule.repeat_option2;
        this.mgr.updateschedule(saveschedule2);
    }

    public void setrepeat(int i, int i2) {
        switch (i) {
            case 1:
                this.tv_repeat = "Only One Time";
                break;
            case 2:
                this.tv_repeat = "EveryDay";
                break;
            case 3:
                if ((i2 & 128) == 128) {
                    this.tv_repeat += "Sun,";
                }
                if ((i2 & 64) == 64) {
                    this.tv_repeat += "Sat,";
                }
                if ((i2 & 32) == 32) {
                    this.tv_repeat += "Fri,";
                }
                if ((i2 & 16) == 16) {
                    this.tv_repeat += "Thu,";
                }
                if ((i2 & 8) == 8) {
                    this.tv_repeat += "Wed,";
                }
                if ((i2 & 4) == 4) {
                    this.tv_repeat += "Tue,";
                }
                if ((i2 & 2) == 2) {
                    this.tv_repeat += "Mon";
                    break;
                }
                break;
        }
    }

    private String getTargettime(int i, int i2, String str) {
        String str2;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:HHmm");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy:MM:dd");
        Date date = new Date(System.currentTimeMillis());
        String str3 = simpleDateFormat.format(date);
        switch (i) {
            case 1:
            case 2:
                if (Integer.parseInt(str3.split(":")[3]) < Integer.parseInt(str)) {
                    return str3.split(":")[0] + ":" + str3.split(":")[1] + ":" + str3.split(":")[2] + ":" + str;
                }
                Date date2 = new Date();
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date2);
                gregorianCalendar.add(5, 1);
                return simpleDateFormat2.format(gregorianCalendar.getTime()) + ":" + str;
            case 3:
                int week = DateUtils.getWeek(simpleDateFormat2.format(date));
                String[] strArrSplit = this.tv_repeat.split(Consts.SECOND_LEVEL_SPLIT);
                String str4 = "";
                switch (week) {
                    case 1:
                        str4 = "Sun";
                        break;
                    case 2:
                        str4 = "Mon";
                        break;
                    case 3:
                        str4 = "Tue";
                        break;
                    case 4:
                        str4 = "Wed";
                        break;
                    case 5:
                        str4 = "Thu";
                        break;
                    case 6:
                        str4 = "Fri";
                        break;
                    case 7:
                        str4 = "Sat";
                        break;
                }
                int i3 = 999;
                for (int i4 = 0; i4 < strArrSplit.length; i4++) {
                    if (str4.equals(strArrSplit[i4])) {
                        i3 = i4;
                    }
                }
                if (i3 != 999) {
                    if (Integer.parseInt(str3.split(":")[3]) < Integer.parseInt(str)) {
                        return str3.split(":")[0] + ":" + str3.split(":")[1] + ":" + str3.split(":")[2] + ":" + str;
                    }
                    if (strArrSplit.length == 1) {
                        Date date3 = new Date();
                        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
                        gregorianCalendar2.setTime(date3);
                        gregorianCalendar2.add(5, 7);
                        return simpleDateFormat2.format(gregorianCalendar2.getTime()) + ":" + str;
                    }
                    int i5 = i3 - 1;
                    if (i5 >= 0) {
                        str2 = strArrSplit[i5];
                    } else {
                        str2 = strArrSplit[strArrSplit.length - 1];
                    }
                    Date date4 = new Date();
                    GregorianCalendar gregorianCalendar3 = new GregorianCalendar();
                    gregorianCalendar3.setTime(date4);
                    gregorianCalendar3.add(5, getweektime(str4, str2));
                    return simpleDateFormat2.format(gregorianCalendar3.getTime()) + ":" + str;
                }
                int iMin = 9;
                for (String str5 : strArrSplit) {
                    iMin = Math.min(iMin, getweektime(str4, str5));
                }
                Date date5 = new Date();
                GregorianCalendar gregorianCalendar4 = new GregorianCalendar();
                gregorianCalendar4.setTime(date5);
                gregorianCalendar4.add(5, iMin);
                return simpleDateFormat2.format(gregorianCalendar4.getTime()) + ":" + str;
            default:
                return "";
        }
    }

    private int getweektime(String str, String str2) {
        int i = getweekint(str);
        int i2 = getweekint(str2);
        if (i > i2) {
            return 7 - (i - i2);
        }
        if (i < i2) {
            return i2 - i;
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0055  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getweekint(java.lang.String r9) {
        /*
            r8 = this;
            int r0 = r9.hashCode()
            r1 = 6
            r2 = 5
            r3 = 4
            r4 = 3
            r5 = 2
            r6 = 1
            r7 = 0
            switch(r0) {
                case 70909: goto L4b;
                case 77548: goto L41;
                case 82886: goto L37;
                case 83500: goto L2d;
                case 84065: goto L23;
                case 84452: goto L19;
                case 86838: goto Lf;
                default: goto Le;
            }
        Le:
            goto L55
        Lf:
            java.lang.String r0 = "Wed"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 3
            goto L56
        L19:
            java.lang.String r0 = "Tue"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 2
            goto L56
        L23:
            java.lang.String r0 = "Thu"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 4
            goto L56
        L2d:
            java.lang.String r0 = "Sun"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 0
            goto L56
        L37:
            java.lang.String r0 = "Sat"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 6
            goto L56
        L41:
            java.lang.String r0 = "Mon"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 1
            goto L56
        L4b:
            java.lang.String r0 = "Fri"
            boolean r9 = r9.equals(r0)
            if (r9 == 0) goto L55
            r9 = 5
            goto L56
        L55:
            r9 = -1
        L56:
            switch(r9) {
                case 0: goto L65;
                case 1: goto L63;
                case 2: goto L61;
                case 3: goto L5f;
                case 4: goto L5d;
                case 5: goto L5b;
                case 6: goto L66;
                default: goto L59;
            }
        L59:
            r1 = 0
            goto L66
        L5b:
            r1 = 5
            goto L66
        L5d:
            r1 = 4
            goto L66
        L5f:
            r1 = 3
            goto L66
        L61:
            r1 = 2
            goto L66
        L63:
            r1 = 1
            goto L66
        L65:
            r1 = 7
        L66:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.assist.scheduleutil.ScheduleServer.getweekint(java.lang.String):int");
    }
}
