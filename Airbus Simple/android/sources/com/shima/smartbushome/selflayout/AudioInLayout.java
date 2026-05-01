package com.shima.smartbushome.selflayout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.shima.smartbushome.FounctionActivity;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.database.Savemusic;
import com.shima.smartbushome.founction_command.audio_incontrol;
import com.shima.smartbushome.founction_view.Music;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AudioInLayout extends RelativeLayout {
    public int AlbumorRadioControl;
    public int ControlSpecSong;
    public int Music_Source;
    public int PlayControl;
    public int PlayModeChange;
    public int VoluneControl;
    audio_incontrol ai;
    ImageView audioin;
    Handler getdatahandler;
    Runnable getdatarun;
    boolean initstart;
    Button play;
    int playstate;
    Context rootcontext;
    SeekBar sb_voice;
    boolean setvolume;
    Savemusic thismusic;
    TextView tv_voicevalue;
    View view;
    private SeekBar.OnSeekBarChangeListener voicechange;

    public AudioInLayout(Context context) {
        super(context);
        this.initstart = false;
        this.getdatahandler = new Handler();
        this.thismusic = new Savemusic();
        this.playstate = 0;
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.voicechange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.AudioInLayout.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                AudioInLayout.this.tv_voicevalue.setText(seekBar.getProgress() + "%");
                AudioInLayout.this.ai.MusicControl((byte) AudioInLayout.this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((seekBar.getProgress() * 79) / 100)), (byte) AudioInLayout.this.thismusic.subnetID, (byte) AudioInLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (AudioInLayout.this.setvolume) {
                    AudioInLayout.this.tv_voicevalue.setText(i + "%");
                    return;
                }
                AudioInLayout.this.tv_voicevalue.setText(i + "%");
            }
        };
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.AudioInLayout.3
            @Override // java.lang.Runnable
            public void run() {
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < listQuerymusic.size(); i++) {
                    if (listQuerymusic.get(i).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i).music_id == 1) {
                        arrayList.add(listQuerymusic.get(i));
                    }
                }
                if (arrayList.size() > 0) {
                    AudioInLayout.this.thismusic = (Savemusic) arrayList.get(0);
                    AudioInLayout.this.getvoicevalue();
                }
            }
        };
        this.setvolume = false;
    }

    public AudioInLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.initstart = false;
        this.getdatahandler = new Handler();
        this.thismusic = new Savemusic();
        this.playstate = 0;
        this.Music_Source = 1;
        this.PlayModeChange = 2;
        this.AlbumorRadioControl = 3;
        this.PlayControl = 4;
        this.VoluneControl = 5;
        this.ControlSpecSong = 6;
        this.voicechange = new SeekBar.OnSeekBarChangeListener() { // from class: com.shima.smartbushome.selflayout.AudioInLayout.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                AudioInLayout.this.tv_voicevalue.setText(seekBar.getProgress() + "%");
                AudioInLayout.this.ai.MusicControl((byte) AudioInLayout.this.VoluneControl, (byte) 1, (byte) 3, (byte) (79 - ((seekBar.getProgress() * 79) / 100)), (byte) AudioInLayout.this.thismusic.subnetID, (byte) AudioInLayout.this.thismusic.deviceID, MainActivity.mydupsocket);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (AudioInLayout.this.setvolume) {
                    AudioInLayout.this.tv_voicevalue.setText(i + "%");
                    return;
                }
                AudioInLayout.this.tv_voicevalue.setText(i + "%");
            }
        };
        this.getdatarun = new Runnable() { // from class: com.shima.smartbushome.selflayout.AudioInLayout.3
            @Override // java.lang.Runnable
            public void run() {
                List<Savemusic> listQuerymusic = MainActivity.mgr.querymusic();
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < listQuerymusic.size(); i++) {
                    if (listQuerymusic.get(i).room_id == FounctionActivity.roomidfc && listQuerymusic.get(i).music_id == 1) {
                        arrayList.add(listQuerymusic.get(i));
                    }
                }
                if (arrayList.size() > 0) {
                    AudioInLayout.this.thismusic = (Savemusic) arrayList.get(0);
                    AudioInLayout.this.getvoicevalue();
                }
            }
        };
        this.setvolume = false;
    }

    public boolean ifinitstarted() {
        return this.initstart;
    }

    public void initview(Context context) {
        this.rootcontext = context;
        this.view = View.inflate(context, R.layout.music_audioin_layout, this);
        this.initstart = true;
        this.audioin = (ImageView) this.view.findViewById(R.id.imageView4);
        this.tv_voicevalue = (TextView) this.view.findViewById(R.id.textView4);
        this.sb_voice = (SeekBar) this.view.findViewById(R.id.seekBar3);
        this.play = (Button) this.view.findViewById(R.id.checkBox4);
        this.play.setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.selflayout.AudioInLayout.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                switch (AudioInLayout.this.playstate) {
                    case 0:
                        AudioInLayout.this.setplaybuttonstate("play");
                        break;
                    case 1:
                        AudioInLayout.this.setplaybuttonstate("pause");
                        break;
                }
            }
        });
        this.sb_voice.setOnSeekBarChangeListener(this.voicechange);
        this.ai = new audio_incontrol();
        if (Build.VERSION.SDK_INT >= 16) {
            this.audioin.setBackground(getResources().getDrawable(R.drawable.music_audioin));
        }
        this.getdatahandler.postDelayed(this.getdatarun, 50L);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setplaybuttonstate(java.lang.String r13) {
        /*
            r12 = this;
            int r0 = r13.hashCode()
            r1 = 3443508(0x348b34, float:4.825382E-39)
            r2 = 0
            r3 = 1
            if (r0 == r1) goto L1b
            r1 = 106440182(0x65825f6, float:4.0652974E-35)
            if (r0 == r1) goto L11
            goto L25
        L11:
            java.lang.String r0 = "pause"
            boolean r13 = r13.equals(r0)
            if (r13 == 0) goto L25
            r13 = 1
            goto L26
        L1b:
            java.lang.String r0 = "play"
            boolean r13 = r13.equals(r0)
            if (r13 == 0) goto L25
            r13 = 0
            goto L26
        L25:
            r13 = -1
        L26:
            switch(r13) {
                case 0: goto L4c;
                case 1: goto L2a;
                default: goto L29;
            }
        L29:
            goto L6d
        L2a:
            android.widget.Button r13 = r12.play
            r0 = 2131165562(0x7f07017a, float:1.7945345E38)
            r13.setBackgroundResource(r0)
            r12.playstate = r2
            com.shima.smartbushome.founction_command.audio_incontrol r3 = r12.ai
            int r13 = r12.PlayControl
            byte r4 = (byte) r13
            r5 = 4
            r6 = 0
            r7 = 0
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.subnetID
            byte r8 = (byte) r13
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.deviceID
            byte r9 = (byte) r13
            com.shima.smartbushome.udp.udp_socket r10 = com.shima.smartbushome.MainActivity.mydupsocket
            r3.MusicControl(r4, r5, r6, r7, r8, r9, r10)
            goto L6d
        L4c:
            android.widget.Button r13 = r12.play
            r0 = 2131165558(0x7f070176, float:1.7945337E38)
            r13.setBackgroundResource(r0)
            r12.playstate = r3
            com.shima.smartbushome.founction_command.audio_incontrol r4 = r12.ai
            int r13 = r12.PlayControl
            byte r5 = (byte) r13
            r6 = 3
            r7 = 0
            r8 = 0
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.subnetID
            byte r9 = (byte) r13
            com.shima.smartbushome.database.Savemusic r13 = r12.thismusic
            int r13 = r13.deviceID
            byte r10 = (byte) r13
            com.shima.smartbushome.udp.udp_socket r11 = com.shima.smartbushome.MainActivity.mydupsocket
            r4.MusicControl(r5, r6, r7, r8, r9, r10, r11)
        L6d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.selflayout.AudioInLayout.setplaybuttonstate(java.lang.String):void");
    }

    public void setcontent(Savemusic savemusic) {
        this.thismusic = savemusic;
    }

    public void setvoicevalue(byte[] bArr) {
        int i = (bArr[25] << 8) + (bArr[26] & 255);
        byte b = bArr[36];
        if (i == 9050) {
            byte[] bArr2 = new byte[2];
            if (bArr[37] == 86 && bArr[38] == 79 && bArr[39] == 76) {
                if (bArr[41] == 13) {
                    bArr2[0] = 48;
                    bArr2[1] = bArr[40];
                } else if (bArr[42] == 13) {
                    bArr2[0] = bArr[40];
                    bArr2[1] = bArr[41];
                }
            }
            int i2 = 100 - ((Integer.parseInt(bytetostring(bArr2, "ascii")) * 100) / 79);
            this.setvolume = true;
            this.sb_voice.setProgress(i2);
            this.setvolume = false;
        }
    }

    public void getvoicevalue() {
        this.ai.GetMusicState((byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
    }

    public String bytetostring(byte[] bArr, String str) {
        try {
            return new String(bArr, str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void SwitchtoAudioIn() {
        this.ai.SwitchtoAudioIn((byte) this.thismusic.subnetID, (byte) this.thismusic.deviceID, MainActivity.mydupsocket);
    }

    public void receiveddata(byte[] bArr) {
        int i = ((bArr[21] & 255) << 8) + (bArr[22] & 255);
        if (bArr[17] == ((byte) this.thismusic.subnetID) && bArr[18] == ((byte) this.thismusic.deviceID) && i == 6447) {
            setvoicevalue(bArr);
        }
    }

    public void radiobackpress() {
        Music.finishmusic();
    }

    public void setdrawable(View view) throws Resources.NotFoundException, NoSuchMethodException, SecurityException {
        GradientDrawable gradientDrawable;
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.music_audioin);
        Drawable.ConstantState constantState = ((StateListDrawable) drawable).getConstantState();
        try {
            Method method = constantState.getClass().getMethod("getChildren", new Class[0]);
            method.setAccessible(true);
            Drawable[] drawableArr = (Drawable[]) method.invoke(constantState, new Object[0]);
            for (int i = 0; i < drawableArr.length && (gradientDrawable = (GradientDrawable) drawableArr[i]) != null; i++) {
                if (i == 0) {
                    gradientDrawable.setColor(ToColor("40edbaff"));
                } else {
                    gradientDrawable.setColor(ToColor("40edbaff"));
                }
            }
            view.setBackground(drawable);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
    }

    public int ToColor(String str) {
        return Color.argb(Integer.parseInt(str.substring(0, 2), 16), Integer.parseInt(str.substring(2, 4), 16), Integer.parseInt(str.substring(4, 6), 16), Integer.parseInt(str.substring(6, 8), 16));
    }
}
