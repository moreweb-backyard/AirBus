package com.shima.smartbushome.assist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.litesuits.http.data.Charsets;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savesong;
import com.shima.smartbushome.founction_command.musiccontrol;
import com.shima.smartbushome.founction_view.Music;
import com.shima.smartbushome.udp.udp_socket;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MusicNotifyReceiver extends BroadcastReceiver {
    public static int deviceID;
    public static int subnetID;
    musiccontrol mc = new musiccontrol();
    public int Music_Source = 1;
    public int PlayModeChange = 2;
    public int AlbumorRadioControl = 3;
    public int PlayControl = 4;
    public int VoluneControl = 5;
    public int ControlSpecSong = 6;
    int playcount = 0;
    boolean count = false;
    Handler playhandler = new Handler();
    Handler receivehandler = new Handler();
    boolean doing = true;
    boolean reflashuienable = false;
    Handler reflashhandler = new Handler();
    boolean reflashstep = false;
    boolean getreflashablum = false;
    Runnable reflashrun = new Runnable() { // from class: com.shima.smartbushome.assist.MusicNotifyReceiver.1
        @Override // java.lang.Runnable
        public void run() {
            if (MusicNotifyReceiver.this.reflashstep) {
                MusicNotifyReceiver.this.reflashuienable = false;
                MusicNotifyReceiver.this.reflashhandler.removeCallbacks(MusicNotifyReceiver.this.reflashrun);
            } else {
                MusicNotifyReceiver.this.mc.GetMusicState((byte) MusicNotifyReceiver.subnetID, (byte) MusicNotifyReceiver.deviceID, MainActivity.mydupsocket);
                MusicNotifyReceiver.this.reflashhandler.postDelayed(MusicNotifyReceiver.this.reflashrun, 300L);
            }
        }
    };
    int recalbumno = 0;
    int recsongno = 0;
    Savesong selectedsong = new Savesong();
    List<Savesong> data = new ArrayList();
    Runnable getselectmusic = new Runnable() { // from class: com.shima.smartbushome.assist.MusicNotifyReceiver.2
        @Override // java.lang.Runnable
        public void run() {
            int i = 0;
            if (MusicNotifyReceiver.this.data.size() <= 0) {
                List<Savesong> listQuerysong = MainActivity.mgr.querysong();
                for (int i2 = 0; i2 < listQuerysong.size(); i2++) {
                    if (listQuerysong.get(i2).room_id == FounctionActivity.roomidfc) {
                        MusicNotifyReceiver.this.data.add(listQuerysong.get(i2));
                    }
                }
            }
            while (true) {
                if (i >= MusicNotifyReceiver.this.data.size()) {
                    break;
                }
                if (MusicNotifyReceiver.this.data.get(i).album_num == MusicNotifyReceiver.this.recalbumno && MusicNotifyReceiver.this.data.get(i).song_num == MusicNotifyReceiver.this.recsongno) {
                    MusicNotifyReceiver.this.selectedsong = MusicNotifyReceiver.this.data.get(i);
                    MusicNotification.sendResidentNoticeType0(MainActivity.maincontext, MusicNotifyReceiver.this.selectedsong.song_name);
                    MusicNotifyReceiver.this.reflashstep = true;
                    break;
                }
                i++;
            }
            MusicNotifyReceiver.this.receivehandler.removeCallbacks(MusicNotifyReceiver.this.getselectmusic);
        }
    };
    Runnable playrun = new Runnable() { // from class: com.shima.smartbushome.assist.MusicNotifyReceiver.3
        @Override // java.lang.Runnable
        public void run() {
            if (MusicNotifyReceiver.this.playcount == 0) {
                MusicNotifyReceiver.this.playcount++;
                MusicNotifyReceiver.this.playhandler.removeCallbacks(MusicNotifyReceiver.this.playrun);
                if (MusicNotification.playcheck) {
                    MusicNotification.playcheck = false;
                } else {
                    MusicNotification.playcheck = true;
                }
                MusicNotifyReceiver.this.playcount = 0;
            }
        }
    };

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra;
        String action = intent.getAction();
        if (MusicNotification.ACTION_Button.equals(action)) {
            switch (intent.getIntExtra("button_type", 0)) {
                case 0:
                    this.mc.MusicControl((byte) this.PlayControl, (byte) 1, (byte) 0, (byte) 0, (byte) subnetID, (byte) deviceID, MainActivity.mydupsocket);
                    break;
                case 1:
                    this.mc.MusicControl((byte) this.PlayControl, (byte) 2, (byte) 0, (byte) 0, (byte) subnetID, (byte) deviceID, MainActivity.mydupsocket);
                    break;
                case 2:
                    if (MusicNotification.playcheck) {
                        this.mc.MusicControl((byte) this.PlayControl, (byte) 3, (byte) 0, (byte) 0, (byte) subnetID, (byte) deviceID, MainActivity.mydupsocket);
                        MusicNotification.remoteViews.setImageViewResource(R.id.music_notify_play, R.drawable.notify_pause);
                        MusicNotification.manager.notify(R.string.app_name, MusicNotification.notification);
                    } else {
                        this.mc.MusicControl((byte) this.PlayControl, (byte) 4, (byte) 0, (byte) 0, (byte) subnetID, (byte) deviceID, MainActivity.mydupsocket);
                        MusicNotification.remoteViews.setImageViewResource(R.id.music_notify_play, R.drawable.notify_play);
                        MusicNotification.manager.notify(R.string.app_name, MusicNotification.notification);
                    }
                    this.playhandler.postDelayed(this.playrun, 300L);
                    break;
            }
        } else if (MusicNotification.ACTION_notify_close.equals(action) && (intExtra = intent.getIntExtra(MusicNotification.NOTICE_ID_KEY, -1)) != -1) {
            MusicNotification.clearNotification(context, intExtra);
        }
        if (udp_socket.ACTION_DATA_IN.equals(action) && Music.musicchoosestate == 0) {
            byte[] byteArrayExtra = intent.getByteArrayExtra(udp_socket.ACTION_DATA_IN);
            if (byteArrayExtra.length > 35) {
                try {
                    receiveddata(byteArrayExtra);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void receiveddata(byte[] bArr) {
        if (this.doing) {
            this.doing = false;
            int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
            if (bArr[17] == ((byte) subnetID) && bArr[18] == ((byte) deviceID) && i == 6447) {
                if (this.reflashuienable) {
                    getmusicstate(bArr);
                } else {
                    this.reflashstep = false;
                    this.getreflashablum = false;
                    reflashui();
                }
            }
            this.doing = true;
        }
    }

    public void reflashui() {
        this.reflashuienable = true;
        this.reflashhandler.postDelayed(this.reflashrun, 70L);
    }

    public void getmusicstate(byte[] bArr) {
        int i = (bArr[25] << 8) + (bArr[26] & 255);
        int i2 = bArr[36] & 255;
        if (i != 9043 || this.reflashstep) {
            return;
        }
        if (i2 == 49) {
            if (this.getreflashablum) {
                return;
            }
            this.getreflashablum = true;
            this.recalbumno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 0), Charsets.Unicode));
            return;
        }
        if (i2 != 51) {
            return;
        }
        this.recsongno = Integer.parseInt(bytetostring(getpieceofbyte(bArr, 1), Charsets.Unicode));
        this.reflashstep = true;
        this.receivehandler.postDelayed(this.getselectmusic, 10L);
    }

    public byte[] getpieceofbyte(byte[] bArr, int i) {
        int i2 = i == 0 ? 49 : i == 1 ? 51 : 0;
        int i3 = i2;
        while (true) {
            if (i3 >= bArr.length) {
                i3 = 0;
                break;
            }
            if (bArr[i3] == 0 && bArr[i3 + 1] == 47) {
                break;
            }
            i3++;
        }
        int i4 = i3 - i2;
        byte[] bArr2 = new byte[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            bArr2[i5] = bArr[i2 + i5];
        }
        return bArr2;
    }

    public String bytetostring(byte[] bArr, String str) {
        try {
            return new String(bArr, str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
