package com.pzj.ipcdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.pzj.ipcdemo.service.BridgeService;
import com.pzj.ipcdemo.utils.CircularProgressBar;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.pzj.ipcdemo.utils.MyRender;
import com.pzj.ipcdemo.utils.Tools;
import java.nio.ByteBuffer;
import java.util.Date;
import vstc2.nativecaller.NativeCaller;

/* loaded from: classes.dex */
public class PlayBackActivity extends Activity implements BridgeService.PlayBackInterface, BridgeService.DateTimeInterface, View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {
    public static PlayBackActivity self;
    private float cachePos;
    private CircularProgressBar circularProgress;
    private long currentTimeStamp;
    private boolean isDownloaded;
    private boolean isDownloading;
    private boolean isPlaying;
    private boolean isSliding;
    private LinearLayout layoutConnPrompt;
    private Bitmap mBmp;
    private GLSurfaceView myGlSurfaceView;
    private MyRender myRender;
    private PlayCommonManager playCommonManager;
    private ImageView playImg;
    private SeekBar playSeekBar;
    private LinearLayout playSeekBarLayout;
    private float pos;
    private ImageButton ptzDownloadVideo;
    private ImageButton ptzPlay_pause;
    private ImageButton ptzTake_photos;
    int ptz_play_pause;
    int ptz_take_photos;
    long slidingTimeMillis;
    private String strDID;
    private String strFilePath;
    private int strFilesize;
    private TextView textTimeStamp;
    private long time;
    private String strName = null;
    private byte[] videodata = null;
    private int videoDataLen = 0;
    private int nVideoWidth = 0;
    private int nVideoHeight = 0;
    private TextView textback = null;
    private TextView textDownloadPercent = null;
    private String tzStr = "GMT+08:00";
    private String timeShow = " ";
    private boolean isOneShow = true;
    private boolean isTakepic = false;
    private Handler mHandler = new Handler() { // from class: com.pzj.ipcdemo.PlayBackActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if ((message.what == 1 || message.what == 2) && PlayBackActivity.this.isOneShow) {
                PlayBackActivity.this.layoutConnPrompt.setVisibility(8);
                PlayBackActivity.this.isOneShow = false;
            }
            switch (message.what) {
                case 0:
                    PlayBackActivity.this.isSliding = false;
                    break;
                case 1:
                    PlayBackActivity.this.textTimeStamp.setText(PlayBackActivity.this.timeShow);
                    if (!PlayBackActivity.this.isSliding) {
                        PlayBackActivity.this.playSeekBar.setProgress((int) (PlayBackActivity.this.pos * 10000.0f));
                        PlayBackActivity.this.playSeekBar.setSecondaryProgress((int) (PlayBackActivity.this.cachePos * 10000.0f));
                        Log.d("PlayBackActivity", "#pos = " + PlayBackActivity.this.pos + " cachePos = " + PlayBackActivity.this.cachePos);
                    }
                    if (PlayBackActivity.this.isDownloading) {
                        PlayBackActivity.this.circularProgress.setVisibility(0);
                        PlayBackActivity.this.circularProgress.setProgress(((int) (PlayBackActivity.this.cachePos * 10000.0f)) / 100);
                    }
                    if (PlayBackActivity.this.pos == 1.0f) {
                        PlayBackActivity.this.togglePlayPauseButton(true);
                    }
                    if (PlayBackActivity.this.cachePos == 1.0f && PlayBackActivity.this.isDownloading && !PlayBackActivity.this.isDownloaded) {
                        PlayBackActivity.this.isDownloaded = true;
                        PlayBackActivity.this.showToast("messageDownloadVideoSuccess");
                        PlayBackActivity.this.playCommonManager.onDownloadVideoFinished();
                    }
                    PlayBackActivity.this.myRender.writeSample(PlayBackActivity.this.videodata, PlayBackActivity.this.nVideoWidth, PlayBackActivity.this.nVideoHeight);
                    PlayBackActivity.this.playImg.setVisibility(8);
                    int width = PlayBackActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, (width * 3) / 4);
                    layoutParams.gravity = 17;
                    PlayBackActivity.this.myGlSurfaceView.setLayoutParams(layoutParams);
                    break;
                case 2:
                    PlayBackActivity.this.textTimeStamp.setText(PlayBackActivity.this.timeShow);
                    Bitmap bitmapDecodeByteArray = BitmapFactory.decodeByteArray(PlayBackActivity.this.videodata, 0, PlayBackActivity.this.videoDataLen);
                    if (bitmapDecodeByteArray != null) {
                        Bitmap bitmapCreateScaledBitmap = null;
                        int width2 = PlayBackActivity.this.getWindowManager().getDefaultDisplay().getWidth();
                        int height = PlayBackActivity.this.getWindowManager().getDefaultDisplay().getHeight();
                        int i = (width2 * 3) / 4;
                        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(width2, i);
                        layoutParams2.gravity = 17;
                        PlayBackActivity.this.playImg.setLayoutParams(layoutParams2);
                        if (PlayBackActivity.this.getResources().getConfiguration().orientation == 1) {
                            bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapDecodeByteArray, width2, i, true);
                        } else if (PlayBackActivity.this.getResources().getConfiguration().orientation == 2) {
                            bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapDecodeByteArray, width2, height, true);
                        }
                        PlayBackActivity.this.playImg.setVisibility(0);
                        PlayBackActivity.this.playImg.setImageBitmap(bitmapCreateScaledBitmap);
                        break;
                    }
                    break;
            }
        }
    };

    @Override // com.pzj.ipcdemo.service.BridgeService.DateTimeInterface
    public void callBackSetSystemParamsResult(String str, int i, int i2) {
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToast(String str) {
        Toast.makeText(this, str, 1).show();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        self = this;
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.playback);
        getWindow().addFlags(128);
        Intent intent = getIntent();
        this.strDID = intent.getStringExtra(DatabaseUtil.KEY_DID);
        this.strName = "Name";
        this.strFilePath = intent.getStringExtra(DatabaseUtil.KEY_FILEPATH);
        this.strFilesize = intent.getIntExtra("filesize", 0);
        Log.d("getDataFromOther", "strDID:" + this.strDID);
        Log.d("getDataFromOther", "strFilePath:" + this.strFilePath);
        findView();
        this.playCommonManager = new PlayCommonManager(this, this.playImg, 1, this.strDID);
        BridgeService.setPlayBackInterface(this);
        Log.e("videodate", this.strFilePath + "strFilesize" + this.strFilesize + "Tools.getPhoneMemoryForPlayBack()" + Tools.getPhoneMemoryForPlayBack());
        StringBuilder sb = new StringBuilder();
        sb.append(this.playCommonManager.getSysteTotalMemorySize(this));
        sb.append("");
        Log.e("videodate", sb.toString());
        Log.e("videodate", PlayCommonManager.getAvailableInternalMemorySize(this) + "");
        Log.e("videodate", PlayCommonManager.getTotalInternalMemorySize(this) + "");
        startPlayBack();
        this.isPlaying = true;
        this.isSliding = false;
        this.isDownloading = false;
        this.isDownloaded = false;
        BridgeService.setDateTimeInterface(this);
        NativeCaller.PPPPGetSystemParams(this.strDID, 4);
        this.playCommonManager.StartAudio();
    }

    private void startPlayBack() {
        this.isPlaying = true;
        String str = this.strDID;
        String str2 = this.strFilePath;
        long j = this.strFilesize;
        PlayCommonManager playCommonManager = this.playCommonManager;
        NativeCaller.StartPlayBack(str, str2, 0, j, PlayCommonManager.getDiskCacheDir(this), Tools.getPhoneSDKIntForPlayBack(), Tools.getPhoneMemoryForPlayBack());
    }

    public static void stopVideoAndHide() {
        try {
            if (self != null) {
                self.finish();
            }
        } catch (Exception unused) {
            System.out.print("");
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.playCommonManager.onTouch(view, motionEvent);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void togglePlayPauseButton(boolean z) {
        if (z) {
            this.ptzPlay_pause.setImageResource(R.drawable.ptz_play);
        } else {
            this.ptzPlay_pause.setImageResource(R.drawable.ptz_pause);
        }
        this.isPlaying = !z;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == this.ptz_take_photos) {
            this.isTakepic = this.playCommonManager.canTakePhoto();
        }
        if (view.getId() == this.ptz_play_pause) {
            if (this.pos < 1.0f) {
                NativeCaller.PausePlayBack(this.strDID, !this.isPlaying ? 1 : 0);
            } else if (this.pos == 1.0f) {
                startPlayBack();
            }
            togglePlayPauseButton(this.isPlaying);
            return;
        }
        if (view.getId() == R.id.ptz_download) {
            if (this.isDownloading) {
                return;
            }
            this.playCommonManager.downloadVideo();
            this.isDownloading = true;
            return;
        }
        if (view.getId() == R.id.back) {
            finish();
        }
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStartTrackingTouch(SeekBar seekBar) {
        this.isSliding = true;
        Log.d("PlayBackActivity", "isSliding = " + this.isSliding);
    }

    @Override // android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("PlayBackActivity", "isSliding = " + this.isSliding);
        changePlayBackTime(seekBar.getProgress());
        this.mHandler.postDelayed(new Runnable() { // from class: com.pzj.ipcdemo.PlayBackActivity.2
            @Override // java.lang.Runnable
            public void run() {
                PlayBackActivity.this.isSliding = false;
            }
        }, 2000L);
    }

    private void changePlayBackTime(int i) {
        float f = (i / 10000.0f) / this.cachePos;
        Log.d("PlayBackActivity", "##pos = " + f);
        long jPlayBackMovePos = NativeCaller.PlayBackMovePos(this.strDID, f);
        Log.d("PlayBackActivity", "##time = " + jPlayBackMovePos);
        NativeCaller.SetPlayBackPos(this.strDID, jPlayBackMovePos);
        this.slidingTimeMillis = System.currentTimeMillis();
    }

    private void findView() {
        this.playImg = (ImageView) findViewById(R.id.vedioview);
        this.layoutConnPrompt = (LinearLayout) findViewById(R.id.progressLayout);
        this.playSeekBar = (SeekBar) findViewById(R.id.playback_seekbar);
        this.playSeekBarLayout = (LinearLayout) findViewById(R.id.playback_seekbar_layout);
        this.playSeekBarLayout.setVisibility(0);
        this.circularProgress = (CircularProgressBar) findViewById(R.id.circularProgress);
        this.circularProgress.setProgressWidth(10);
        this.circularProgress.showProgressText(true);
        this.circularProgress.setTextColor(-1);
        this.circularProgress.useRoundedCorners(true);
        this.circularProgress.setProgressColor(SupportMenu.CATEGORY_MASK);
        this.playSeekBar.setOnSeekBarChangeListener(this);
        this.playSeekBar.setMax(10000);
        this.textTimeStamp = (TextView) findViewById(R.id.textTimeStamp);
        this.myGlSurfaceView = (GLSurfaceView) findViewById(R.id.mysurfaceview);
        this.myGlSurfaceView.setOnTouchListener(this);
        this.myRender = new MyRender(this.myGlSurfaceView);
        this.myGlSurfaceView.setRenderer(this.myRender);
        this.ptzTake_photos = (ImageButton) findViewById(R.id.ptz_take_photos);
        this.ptzTake_photos.setOnClickListener(this);
        this.ptzTake_photos.setVisibility(8);
        this.ptz_play_pause = getResources().getIdentifier("ptz_play_pause", DatabaseUtil.KEY_ID, getPackageName());
        this.ptzPlay_pause = (ImageButton) findViewById(this.ptz_play_pause);
        this.ptzPlay_pause.setOnClickListener(this);
        this.ptzPlay_pause.setVisibility(0);
        this.ptzDownloadVideo = (ImageButton) findViewById(R.id.ptz_download);
        this.ptzDownloadVideo.setOnClickListener(this);
        this.ptzDownloadVideo.setVisibility(0);
        this.textback = (TextView) findViewById(R.id.back);
        this.textback.setText("back");
        this.textback.setVisibility(0);
        this.textback.setOnClickListener(this);
        this.textDownloadPercent = (TextView) findViewById(R.id.text_download_percent);
        this.textDownloadPercent.setVisibility(0);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        this.playCommonManager.onKeyDown(i, keyEvent);
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity
    protected void onResume() {
        this.playCommonManager.onResume();
        super.onResume();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.playCommonManager.onPause();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.playCommonManager.onDestroy();
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.DateTimeInterface
    public void callBackDatetimeParams(String str, int i, int i2, int i3, String str2) {
        String timeZone = this.playCommonManager.getTimeZone(i2);
        if (timeZone != null) {
            this.tzStr = timeZone;
        }
    }

    @Override // com.pzj.ipcdemo.service.BridgeService.PlayBackInterface
    public void callBackPlaybackVideoData(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, int i7, float f, float f2) {
        if (i == 1) {
            if (i6 == 1) {
                this.time = i5;
                this.pos = f;
                this.cachePos = f2;
                this.currentTimeStamp = this.time * 1000;
                Log.d("PlayBackActivity", "temp = " + new Date(this.currentTimeStamp).toString());
                this.videodata = bArr;
                this.videoDataLen = i2;
                this.nVideoWidth = i3;
                this.nVideoHeight = i4;
                this.timeShow = this.playCommonManager.getDeviceTime(this.currentTimeStamp, this.tzStr);
                if (i == 1) {
                    this.mHandler.sendEmptyMessage(1);
                } else {
                    this.mHandler.sendEmptyMessage(2);
                }
                if (this.isTakepic) {
                    this.isTakepic = false;
                    byte[] bArr2 = new byte[this.nVideoWidth * this.nVideoHeight * 2];
                    try {
                        NativeCaller.YUV4202RGB565(bArr, bArr2, this.nVideoWidth, this.nVideoHeight);
                        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr2);
                        this.mBmp = Bitmap.createBitmap(this.nVideoWidth, this.nVideoHeight, Bitmap.Config.RGB_565);
                        this.mBmp.copyPixelsFromBuffer(byteBufferWrap);
                        this.playCommonManager.takePicture(this.mBmp);
                        return;
                    } catch (Exception unused) {
                        System.out.print("");
                        return;
                    }
                }
                return;
            }
            if (i6 == 6 && this.playCommonManager.isAudioPlaying()) {
                this.playCommonManager.addAudioData(bArr, i2);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("oneFramesize", i7);
                message.setData(bundle);
            }
        }
    }
}
