package com.pzj.ipcdemo;

import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.AudioPlayer;
import com.pzj.ipcdemo.utils.CustomBuffer;
import com.pzj.ipcdemo.utils.CustomBufferData;
import com.pzj.ipcdemo.utils.CustomBufferHead;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.utils.MyRender;
import com.pzj.ipcdemo.utils.SystemUIUtil;
import com.pzj.ipcdemo.utils.VideoFramePool;
import com.pzj.ipcdemo.view.ControlAnyLayerView;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class PlayActivity extends AppCompatActivity implements BridgeService.PlayInterface, ControlAnyLayerView.OnIpcControlListener {
    private static final int AUDIO_BUFFER_START_CODE = 16711935;
    private static String TAG = "PlayActivity";
    private ControlAnyLayerView controlAnyLayerView;
    private ControlThread controlThread;
    private VideoFramePool framePool;
    private MyRender myRender;
    private GLSurfaceView playSurface = null;
    private byte[] videodata = null;
    private int videoDataLen = 0;
    public int nVideoWidths = 0;
    public int nVideoHeights = 0;
    private CustomBuffer AudioBuffer = null;
    private AudioPlayer audioPlayer = null;
    public String deviceName = "";
    public String devicePass = "";
    public String deviceId = "";
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.PlayActivity.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 1) {
                return;
            }
            PlayActivity.this.framePool.pushBytes(PlayActivity.this.videodata, PlayActivity.this.videoDataLen, PlayActivity.this.nVideoWidths, PlayActivity.this.nVideoHeights);
        }
    };
    private Handler msgHandler = new Handler() { // from class: com.pzj.ipcdemo.PlayActivity.3
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Log.d(PlayActivity.TAG, "断线了");
                Toast.makeText(PlayActivity.this.getApplicationContext(), "disconnect", 0).show();
                PlayActivity.this.finish();
            }
        }
    };

    @Override // com.pzj.ipcdemo.service.BridgeService.PlayInterface
    public void callBackH264Data(byte[] bArr, int i, int i2) {
    }

    @Override // com.pzj.ipcdemo.view.ControlAnyLayerView.OnIpcControlListener
    public void onMicClickListener(String str) {
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityGingerbread, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        SystemUIUtil.setSystemUIVisible(this, false);
        setContentView(R.layout.activity_play);
        this.deviceName = getIntent().getExtras().getString(DatabaseUtil.KEY_NAME);
        this.devicePass = getIntent().getExtras().getString("pass");
        this.deviceId = getIntent().getExtras().getString(DatabaseUtil.KEY_ID);
        this.controlThread = new ControlThread();
        this.controlThread.start();
        this.controlAnyLayerView = new ControlAnyLayerView(this, this.deviceId);
        this.controlAnyLayerView.setOnControlListener(this);
        this.AudioBuffer = new CustomBuffer();
        this.audioPlayer = new AudioPlayer(this.AudioBuffer);
        this.playSurface = (GLSurfaceView) findViewById(R.id.mysurfaceview);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        if (getResources().getConfiguration().orientation == 1) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, (width * 720) / 1280);
            layoutParams.gravity = 17;
            this.playSurface.setLayoutParams(layoutParams);
        } else if (getResources().getConfiguration().orientation == 2) {
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width, height);
            layoutParams2.gravity = 17;
            this.playSurface.setLayoutParams(layoutParams2);
        }
        this.myRender = new MyRender(this.playSurface);
        this.framePool = new VideoFramePool(this.playSurface, this.myRender);
        this.framePool.setFrameRate(15);
        this.framePool.start();
        this.playSurface.setRenderer(this.myRender);
        BridgeService.setPlayInterface(this);
        NativeCaller.StartPPPPLivestream(this.deviceId, 10, 1);
        Log.d(TAG, "开始请求视屏流数据");
        StartAudio();
        this.playSurface.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.PlayActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PlayActivity.this.controlAnyLayerView.show();
            }
        });
    }

    private void StartAudio() {
        synchronized (this) {
            this.AudioBuffer.ClearAll();
            this.audioPlayer.AudioPlayStart();
            NativeCaller.PPPPStartAudio(this.deviceId);
            Log.d(TAG, "开始请求音频流数据");
        }
    }

    private void StopAudio() {
        synchronized (this) {
            this.audioPlayer.AudioPlayStop();
            this.AudioBuffer.ClearAll();
            NativeCaller.PPPPStopAudio(this.deviceId);
            Log.d(TAG, "停止音频流数据");
        }
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.PlayInterface
    public void callBackCameraParamNotify(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        Log.d("视频参数回调", i + Consts.SECOND_LEVEL_SPLIT + i2 + Consts.SECOND_LEVEL_SPLIT + i3 + Consts.SECOND_LEVEL_SPLIT + i4 + Consts.SECOND_LEVEL_SPLIT + i5 + Consts.SECOND_LEVEL_SPLIT + i6 + Consts.SECOND_LEVEL_SPLIT + i7);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.PlayInterface
    public void callBackVideoData(byte[] bArr, int i, int i2, int i3, int i4) {
        Log.d("视频数据流回调", "videobuf:" + bArr + "--h264Data" + i + "len" + i2 + "width" + i3 + "height" + i4);
        this.videodata = bArr;
        this.videoDataLen = i2;
        this.nVideoWidths = i3;
        this.nVideoHeights = i4;
        Message message = new Message();
        if (i == 1) {
            message.what = 1;
        } else {
            message.what = 2;
        }
        this.mHandler.sendMessage(message);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.PlayInterface
    public void callBackMessageNotify(String str, int i, int i2) {
        Message message = new Message();
        message.what = 1;
        this.msgHandler.sendMessage(message);
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.PlayInterface
    public void callBackAudioData(byte[] bArr, int i) {
        Log.d(TAG, "AudioData: len :+ " + i);
        if (this.audioPlayer.isAudioPlaying()) {
            CustomBufferHead customBufferHead = new CustomBufferHead();
            CustomBufferData customBufferData = new CustomBufferData();
            customBufferHead.length = i;
            customBufferHead.startcode = AUDIO_BUFFER_START_CODE;
            customBufferData.head = customBufferHead;
            customBufferData.data = bArr;
            this.AudioBuffer.addData(customBufferData);
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        SystemUIUtil.setSystemUIVisible(this, false);
        if (configuration.orientation == 2) {
            Log.d(TAG, "当前屏幕为横屏");
        } else {
            Log.d(TAG, "当前屏幕为竖屏");
        }
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();
        if (getResources().getConfiguration().orientation == 1) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, (width * 720) / 1280);
            layoutParams.gravity = 17;
            this.playSurface.setLayoutParams(layoutParams);
        } else if (getResources().getConfiguration().orientation == 2) {
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width, height);
            layoutParams2.gravity = 17;
            this.playSurface.setLayoutParams(layoutParams2);
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        try {
            NativeCaller.StopPPPPLivestream(this.deviceId);
            StopAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        try {
            NativeCaller.StopPPPPLivestream(this.deviceId);
            StopAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.pzj.ipcdemo.view.ControlAnyLayerView.OnIpcControlListener
    public void onUpClickListener(String str, boolean z) {
        if (z) {
            NativeCaller.PPPPPTZControl(str, 0);
        } else {
            NativeCaller.PPPPPTZControl(str, 1);
        }
    }

    @Override // com.pzj.ipcdemo.view.ControlAnyLayerView.OnIpcControlListener
    public void onDownClickListener(String str, boolean z) {
        if (z) {
            NativeCaller.PPPPPTZControl(str, 2);
        } else {
            NativeCaller.PPPPPTZControl(str, 3);
        }
    }

    @Override // com.pzj.ipcdemo.view.ControlAnyLayerView.OnIpcControlListener
    public void onLeftClickListener(String str, boolean z) {
        if (z) {
            NativeCaller.PPPPPTZControl(str, 4);
        } else {
            NativeCaller.PPPPPTZControl(str, 5);
        }
    }

    @Override // com.pzj.ipcdemo.view.ControlAnyLayerView.OnIpcControlListener
    public void onRightClickListener(String str, boolean z) {
        if (z) {
            NativeCaller.PPPPPTZControl(str, 6);
        } else {
            NativeCaller.PPPPPTZControl(str, 7);
        }
    }

    @Override // com.pzj.ipcdemo.view.ControlAnyLayerView.OnIpcControlListener
    public void onVoiceClickListener(String str, boolean z) {
        if (z) {
            StopAudio();
        } else {
            StartAudio();
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        SystemUIUtil.setSystemUIVisible(this, false);
    }

    public class ControlThread extends Thread {
        private int cmd_ptz;
        private String deviceId = "";
        private boolean isControl = false;

        public ControlThread() {
        }

        public void setControl(String str, int i, boolean z) {
            this.deviceId = str;
            this.cmd_ptz = i;
            this.isControl = z;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws InterruptedException {
            while (true) {
                if (this.isControl) {
                    for (int i = 0; i < 10; i++) {
                        NativeCaller.PPPPPTZControl(this.deviceId, this.cmd_ptz);
                        if (!this.isControl) {
                            break;
                        }
                        try {
                            Thread.sleep(1000L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private class ControlDeviceTask extends AsyncTask<Void, Void, Integer> {
        private String strDID;
        private int type;

        public ControlDeviceTask(String str, int i) {
            this.strDID = str;
            this.type = i;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) throws InterruptedException {
            if (this.type == 6) {
                NativeCaller.PPPPPTZControl(this.strDID, 6);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(this.strDID, 7);
            } else if (this.type == 4) {
                NativeCaller.PPPPPTZControl(this.strDID, 4);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(this.strDID, 5);
            } else if (this.type == 0) {
                NativeCaller.PPPPPTZControl(this.strDID, 0);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(this.strDID, 1);
            } else if (this.type == 2) {
                NativeCaller.PPPPPTZControl(this.strDID, 2);
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e4) {
                    e4.printStackTrace();
                }
                NativeCaller.PPPPPTZControl(this.strDID, 3);
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((ControlDeviceTask) num);
        }
    }
}
