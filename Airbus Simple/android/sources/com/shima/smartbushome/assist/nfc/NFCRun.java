package com.shima.smartbushome.assist.nfc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.widget.Toast;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.assist.marcoCompare;
import com.shima.smartbushome.database.Savecurtain;
import com.shima.smartbushome.database.Savefan;
import com.shima.smartbushome.database.Savehvac;
import com.shima.smartbushome.database.Savelight;
import com.shima.smartbushome.database.Savemarco;
import com.shima.smartbushome.database.Savemedia;
import com.shima.smartbushome.database.Savemediabutton;
import com.shima.smartbushome.database.Savenfc;
import com.shima.smartbushome.database.Saveother;
import com.shima.smartbushome.founction_command.ACcontrol;
import com.shima.smartbushome.founction_command.curtaincontrol;
import com.shima.smartbushome.founction_command.fancontrol;
import com.shima.smartbushome.founction_command.lightcontrol;
import com.shima.smartbushome.founction_command.mediacontrol;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.founction_command.othercontrol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class NFCRun {
    private static final byte const_ac_cmd_type_onoff = 3;
    private static final byte const_ac_cmd_type_set_auto_tmp = 8;
    private static final byte const_ac_cmd_type_set_cold_tmp = 4;
    private static final byte const_ac_cmd_type_set_fan = 5;
    private static final byte const_ac_cmd_type_set_heat_tmp = 7;
    private static final byte const_ac_cmd_type_set_mode = 6;
    private Notification notification;
    private NotificationManager notificationManager;
    private Context rootcontext;
    private String nofitycontent = "";
    List<Savemarco> clickmarco = new ArrayList();
    Handler senthandler = new Handler();
    int sentcount = 0;
    boolean senting = false;
    Runnable sent = new Runnable() { // from class: com.shima.smartbushome.assist.nfc.NFCRun.1
        @Override // java.lang.Runnable
        public void run() {
            if (NFCRun.this.senting) {
                NFCRun.this.sentcount++;
                NFCRun.this.senting = false;
                if (NFCRun.this.sentcount >= NFCRun.this.clickmarco.size()) {
                    NFCRun.this.senthandler.removeCallbacks(NFCRun.this.sent);
                    NFCRun.this.sentcount = 0;
                    Toast.makeText(NFCRun.this.rootcontext, "finished", 0).show();
                    return;
                }
                NFCRun.this.senthandler.postDelayed(NFCRun.this.sent, 75L);
                return;
            }
            if (NFCRun.this.clickmarco.size() > 0) {
                NFCRun.this.sentmarco(NFCRun.this.clickmarco.get(NFCRun.this.sentcount));
            }
            NFCRun.this.senthandler.postDelayed(NFCRun.this.sent, 250L);
            NFCRun.this.senting = true;
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

    public NFCRun(Context context) {
        this.rootcontext = context;
    }

    public void run(List<Savenfc> list) {
        for (int i = 0; i < list.size(); i++) {
            RunnfcSystem(list.get(i));
        }
    }

    public void RunnfcSystem(Savenfc savenfc) {
        switch (savenfc.action_type) {
            case 1:
                this.nofitycontent = "Marco:" + savenfc.marco_name;
                break;
            case 2:
                this.nofitycontent = "Call:" + savenfc.call_num;
                break;
        }
        Notification.Builder builder = new Notification.Builder(this.rootcontext);
        builder.setContentIntent(PendingIntent.getActivity(this.rootcontext, 0, new Intent("android.intent.action.VIEW", Uri.parse("http://blog.csdn.net/itachi85/")), 0));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(this.rootcontext.getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle(this.nofitycontent);
        Context context = this.rootcontext;
        Context context2 = this.rootcontext;
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 16) {
            this.notification = builder.build();
        } else {
            this.notification = new Notification(R.mipmap.ic_launcher, this.nofitycontent, System.currentTimeMillis());
        }
        if (this.notificationManager != null && this.notification != null) {
            this.notificationManager.notify(0, this.notification);
        }
        switch (savenfc.action_type) {
            case 1:
                if (savenfc.state == 0 && savenfc.nfc_id != 99999999) {
                    if (this.clickmarco.size() > 0) {
                        this.clickmarco.clear();
                    }
                    this.clickmarco = listorder(savenfc.marco_ID, MainActivity.mgr.querymarco());
                    this.senthandler.postDelayed(this.sent, 20L);
                    break;
                }
                break;
            case 2:
                if (savenfc.state == 0) {
                    Intent intent = new Intent("android.intent.action.CALL");
                    intent.setData(Uri.parse("tel:" + savenfc.call_num));
                    this.rootcontext.startActivity(intent);
                    break;
                }
                break;
        }
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
                i = NFCRun.this.senttemp.value3;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
            if (i != 3) {
                switch (i) {
                    case 0:
                        NFCRun.this.ac.ACControl((byte) NFCRun.this.senttemp.subnetID, (byte) NFCRun.this.senttemp.deviceID, 4, NFCRun.this.senttemp.value2, MainActivity.mydupsocket);
                        break;
                    case 1:
                        NFCRun.this.ac.ACControl((byte) NFCRun.this.senttemp.subnetID, (byte) NFCRun.this.senttemp.deviceID, 7, NFCRun.this.senttemp.value2, MainActivity.mydupsocket);
                        break;
                }
                return;
            }
            NFCRun.this.ac.ACControl((byte) NFCRun.this.senttemp.subnetID, (byte) NFCRun.this.senttemp.deviceID, 8, NFCRun.this.senttemp.value2, MainActivity.mydupsocket);
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
