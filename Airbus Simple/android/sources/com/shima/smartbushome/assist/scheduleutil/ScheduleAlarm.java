package com.shima.smartbushome.assist.scheduleutil;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import com.litesuits.http.data.Consts;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.assist.marcoCompare;
import com.shima.smartbushome.database.DBManager;
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.database.Savefan;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.database.Savemarco;
import com.shima.smartbushome.database.Savemedia;
import com.shima.smartbushome.database.Savemediabutton;
import com.shima.smartbushome.database.Saveother;
import com.shima.smartbushome.database.Saveschedule;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.founction_command.curtaincontrol;
import com.shima.smartbushome.founction_command.fancontrol;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.founction_command.mediacontrol;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.founction_command.othercontrol;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/* loaded from: classes.dex */
public class ScheduleAlarm {
    private static final byte const_ac_cmd_type_onoff = 3;
    private static final byte const_ac_cmd_type_set_auto_tmp = 8;
    private static final byte const_ac_cmd_type_set_cold_tmp = 4;
    private static final byte const_ac_cmd_type_set_fan = 5;
    private static final byte const_ac_cmd_type_set_heat_tmp = 7;
    private static final byte const_ac_cmd_type_set_mode = 6;
    private OnStatusChangeListener listener;
    private Context rootcontext;
    private DBManager thismgr;
    private Saveschedule thisschedule;
    private int marcoID = 0;
    private String tv_repeat = "";
    private List<Savemarco> clickmarco = new ArrayList();
    Handler senthandler = new Handler();
    int sentcount = 0;
    boolean senting = false;
    Runnable sent = new Runnable() { // from class: com.shima.smartbushome.assist.scheduleutil.ScheduleAlarm.1
        @Override // java.lang.Runnable
        public void run() {
            if (!ScheduleAlarm.this.senting) {
                if (ScheduleAlarm.this.clickmarco.size() > 0) {
                    ScheduleAlarm.this.sentmarco((Savemarco) ScheduleAlarm.this.clickmarco.get(ScheduleAlarm.this.sentcount));
                }
                ScheduleAlarm.this.senthandler.postDelayed(ScheduleAlarm.this.sent, 250L);
                ScheduleAlarm.this.senting = true;
                return;
            }
            ScheduleAlarm.this.sentcount++;
            ScheduleAlarm.this.senting = false;
            if (ScheduleAlarm.this.sentcount >= ScheduleAlarm.this.clickmarco.size()) {
                ScheduleAlarm.this.senthandler.removeCallbacks(ScheduleAlarm.this.sent);
                ScheduleAlarm.this.sentcount = 0;
                ScheduleAlarm.this.updateAlarmtime(ScheduleAlarm.this.thisschedule);
                ScheduleAlarm.this.listener.onStatusChange("done");
                return;
            }
            ScheduleAlarm.this.senthandler.postDelayed(ScheduleAlarm.this.sent, 75L);
        }
    };
    Savemarco senttemp = new Savemarco();
    lightcontrol lc = new lightcontrol();
    ACcontrol ac = new ACcontrol();
    curtaincontrol cc = new curtaincontrol();
    musiccontrol mc = new musiccontrol();
    othercontrol oc = new othercontrol();
    fancontrol fc = new fancontrol();
    mediacontrol mec = new mediacontrol();

    public interface OnStatusChangeListener {
        void onStatusChange(String str);
    }

    public void start(Saveschedule saveschedule, Context context, DBManager dBManager) {
        this.rootcontext = context;
        this.thisschedule = saveschedule;
        this.thismgr = dBManager;
        this.clickmarco = listorder(saveschedule.marco_ID, this.thismgr.querymarco());
        this.senthandler.postDelayed(this.sent, 20L);
    }

    public void setOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        this.listener = onStatusChangeListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAlarmtime(Saveschedule saveschedule) {
        Saveschedule saveschedule2 = new Saveschedule();
        saveschedule2.schedule_id = saveschedule.schedule_id;
        saveschedule2.schedule_icon = "";
        saveschedule2.schedule_name = saveschedule.schedule_name;
        this.tv_repeat = "";
        setrepeat(saveschedule.repeat_option1, saveschedule.repeat_option2);
        saveschedule2.alarm_time = getTargettime(saveschedule.repeat_option1, saveschedule.repeat_option2, saveschedule.alarm_time.split(":")[3]);
        if (saveschedule.repeat_option1 == 1) {
            saveschedule2.last_status = 1;
        } else {
            saveschedule2.last_status = 0;
        }
        saveschedule2.marco_ID = saveschedule.marco_ID;
        saveschedule2.repeat_option1 = saveschedule.repeat_option1;
        saveschedule2.repeat_option2 = saveschedule.repeat_option2;
        this.thismgr.updateschedule(saveschedule2);
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
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.assist.scheduleutil.ScheduleAlarm.getweekint(java.lang.String):int");
    }

    public class senttemp implements Runnable {
        public senttemp() {
        }

        @Override // java.lang.Runnable
        public void run() throws InterruptedException {
            int i;
            try {
                Thread.sleep(150L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                i = ScheduleAlarm.this.senttemp.value3;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
            if (i != 3) {
                switch (i) {
                    case 0:
                        ScheduleAlarm.this.ac.ACControl((byte) ScheduleAlarm.this.senttemp.subnetID, (byte) ScheduleAlarm.this.senttemp.deviceID, 4, ScheduleAlarm.this.senttemp.value2, MainActivity.mydupsocket);
                        break;
                    case 1:
                        ScheduleAlarm.this.ac.ACControl((byte) ScheduleAlarm.this.senttemp.subnetID, (byte) ScheduleAlarm.this.senttemp.deviceID, 7, ScheduleAlarm.this.senttemp.value2, MainActivity.mydupsocket);
                        break;
                }
                return;
            }
            ScheduleAlarm.this.ac.ACControl((byte) ScheduleAlarm.this.senttemp.subnetID, (byte) ScheduleAlarm.this.senttemp.deviceID, 8, ScheduleAlarm.this.senttemp.value2, MainActivity.mydupsocket);
        }
    }

    public void sentmarco(Savemarco savemarco) {
        int i = 0;
        switch (savemarco.control_type) {
            case 1:
                Savelight savelight = new Savelight();
                List<Savelight> listQuerylight = MainActivity.mgr.querylight();
                while (true) {
                    if (i < listQuerylight.size()) {
                        if (listQuerylight.get(i).room_id == savemarco.room_id && listQuerylight.get(i).light_statement.equals(savemarco.device)) {
                            savelight = listQuerylight.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value1) {
                    case 0:
                        switch (savemarco.value2) {
                            case 0:
                                this.lc.SingleChannelControl((byte) savelight.subnetID, (byte) savelight.deviceID, savelight.channel, 0, MainActivity.mydupsocket);
                                break;
                            case 1:
                                this.lc.SingleChannelControl((byte) savelight.subnetID, (byte) savelight.deviceID, savelight.channel, 100, MainActivity.mydupsocket);
                                break;
                        }
                    case 1:
                        this.lc.SingleChannelControl((byte) savelight.subnetID, (byte) savelight.deviceID, savelight.channel, savemarco.value2, MainActivity.mydupsocket);
                        break;
                    case 2:
                        this.lc.ARGBlightcontrol((byte) savelight.subnetID, (byte) savelight.deviceID, savemarco.value2, MainActivity.mydupsocket);
                        break;
                }
            case 2:
                Savehvac savehvac = new Savehvac();
                List<Savehvac> listQueryhvac = MainActivity.mgr.queryhvac();
                while (true) {
                    if (i < listQueryhvac.size()) {
                        if (listQueryhvac.get(i).room_id == savemarco.room_id && listQueryhvac.get(i).hvac_remark.equals(savemarco.device)) {
                            savehvac = listQueryhvac.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value1) {
                    case 0:
                        this.ac.ACControl((byte) savehvac.subnetID, (byte) savehvac.deviceID, 3, savemarco.value2, MainActivity.mydupsocket);
                        break;
                    case 1:
                        this.ac.ACControl((byte) savehvac.subnetID, (byte) savehvac.deviceID, 6, savemarco.value3, MainActivity.mydupsocket);
                        this.senttemp = savemarco;
                        this.senttemp.subnetID = savehvac.subnetID;
                        this.senttemp.deviceID = savehvac.deviceID;
                        new Thread(new senttemp()).start();
                        break;
                    case 2:
                        this.ac.ACControl((byte) savehvac.subnetID, (byte) savehvac.deviceID, 5, savemarco.value2, MainActivity.mydupsocket);
                        break;
                }
            case 3:
                Savecurtain savecurtain = new Savecurtain();
                List<Savecurtain> listQuerycurtain = MainActivity.mgr.querycurtain();
                while (true) {
                    if (i < listQuerycurtain.size()) {
                        if (listQuerycurtain.get(i).room_id == savemarco.room_id && listQuerycurtain.get(i).curtain_remark.equals(savemarco.device)) {
                            savecurtain = listQuerycurtain.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value2) {
                    case 0:
                        this.cc.CurtainControl((byte) savecurtain.subnetID, (byte) savecurtain.deviceID, savecurtain.channel_1, savecurtain.channel_2, "close", MainActivity.mydupsocket);
                        break;
                    case 1:
                        this.cc.CurtainControl((byte) savecurtain.subnetID, (byte) savecurtain.deviceID, savecurtain.channel_1, savecurtain.channel_2, "open", MainActivity.mydupsocket);
                        break;
                }
            case 4:
                int i2 = savemarco.value1;
                if (i2 == 1) {
                    this.mc.MusicControl((byte) 1, (byte) savemarco.value2, (byte) 0, (byte) 0, (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                    break;
                } else {
                    switch (i2) {
                        case 3:
                            this.mc.MusicControl((byte) 3, (byte) 6, (byte) savemarco.value3, (byte) 0, (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                        case 4:
                            this.mc.MusicControl((byte) 4, (byte) savemarco.value2, (byte) 0, (byte) 0, (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                        case 5:
                            this.mc.MusicControl((byte) 5, (byte) 1, (byte) 3, (byte) (79 - ((savemarco.value2 * 79) / 100)), (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                        case 6:
                            byte[] bArr = {(byte) ((savemarco.value3 & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8), (byte) (savemarco.value3 - (65280 & savemarco.value3))};
                            this.mc.MusicControl((byte) 6, (byte) savemarco.value2, bArr[0], bArr[1], (byte) savemarco.subnetID, (byte) savemarco.deviceID, MainActivity.mydupsocket);
                            break;
                    }
                }
            case 5:
                Saveother saveother = new Saveother();
                List<Saveother> listQueryother = MainActivity.mgr.queryother();
                while (true) {
                    if (i < listQueryother.size()) {
                        if (listQueryother.get(i).room_id == savemarco.room_id && listQueryother.get(i).other_statement.equals(savemarco.device)) {
                            saveother = listQueryother.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                switch (savemarco.value1) {
                    case 0:
                        switch (savemarco.value2) {
                            case 0:
                                this.oc.SingleChannelControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, 0, MainActivity.mydupsocket);
                                break;
                            case 1:
                                this.oc.SingleChannelControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, 100, MainActivity.mydupsocket);
                                break;
                        }
                    case 1:
                        switch (savemarco.value2) {
                            case 0:
                                this.oc.CurtainControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, saveother.channel_2, "close", MainActivity.mydupsocket);
                                break;
                            case 1:
                                this.oc.CurtainControl((byte) saveother.subnetID, (byte) saveother.deviceID, saveother.channel_1, saveother.channel_2, "open", MainActivity.mydupsocket);
                                break;
                        }
                }
            case 6:
                Savefan savefan = new Savefan();
                List<Savefan> listQueryfan = MainActivity.mgr.queryfan();
                while (true) {
                    if (i < listQueryfan.size()) {
                        if (listQueryfan.get(i).room_id == savemarco.room_id && listQueryfan.get(i).fan_statement.equals(savemarco.device)) {
                            savefan = listQueryfan.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                this.fc.FanChannelControl((byte) savefan.subnetID, (byte) savefan.deviceID, savefan.channel, savemarco.value2, MainActivity.mydupsocket);
                break;
            case 7:
                Savemedia savemedia = new Savemedia();
                Savemediabutton savemediabutton = new Savemediabutton();
                List<Savemedia> listQuerymedia = MainActivity.mgr.querymedia();
                List<Savemediabutton> listQuerymediabutton = MainActivity.mgr.querymediabutton();
                int i3 = 0;
                while (true) {
                    if (i3 < listQuerymedia.size()) {
                        if (listQuerymedia.get(i3).room_id == savemarco.room_id && listQuerymedia.get(i3).media_statement.equals(savemarco.device)) {
                            savemedia = listQuerymedia.get(i3);
                        } else {
                            i3++;
                        }
                    }
                }
                while (true) {
                    if (i < listQuerymediabutton.size()) {
                        if (listQuerymediabutton.get(i).room_id == savemarco.room_id && listQuerymediabutton.get(i).media_id == savemedia.media_id && listQuerymediabutton.get(i).button_num == savemarco.value2) {
                            savemediabutton = listQuerymediabutton.get(i);
                        } else {
                            i++;
                        }
                    }
                }
                if (savemediabutton.ifIRmarco == 1) {
                    this.mec.IRMarcoControl((byte) savemedia.subnetID, (byte) savemedia.deviceID, savemediabutton.media_swno, savemediabutton.media_controltype, MainActivity.mydupsocket);
                    break;
                } else {
                    this.mec.IRControl((byte) savemedia.subnetID, (byte) savemedia.deviceID, savemediabutton.media_swno, savemediabutton.media_controltype, MainActivity.mydupsocket);
                    break;
                }
                break;
        }
    }

    public List<Savemarco> listorder(int i, List<Savemarco> list) {
        ArrayList arrayList = new ArrayList();
        new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (i == list.get(i2).marco_id) {
                arrayList.add(list.get(i2));
            }
        }
        Collections.sort(arrayList, new marcoCompare());
        return arrayList;
    }
}
