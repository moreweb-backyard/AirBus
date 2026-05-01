package com.pzj.ipcdemo.utils;

import android.media.AudioTrack;
import voice.encoder.SignalGenerator;

/* loaded from: classes.dex */
public class AudioPlayer {
    CustomBuffer audioBuffer;
    private boolean bAudioPlaying = false;
    private Thread audioThread = null;
    private AudioTrack m_AudioTrack = null;

    public AudioPlayer(CustomBuffer customBuffer) {
        this.audioBuffer = null;
        this.audioBuffer = customBuffer;
    }

    public boolean isAudioPlaying() {
        return this.bAudioPlaying;
    }

    public boolean AudioPlayStart() {
        synchronized (this) {
            if (this.bAudioPlaying) {
                return true;
            }
            this.bAudioPlaying = true;
            this.audioThread = new Thread(new AudioPlayThread());
            this.audioThread.start();
            return true;
        }
    }

    public void AudioPlayStop() {
        synchronized (this) {
            if (this.bAudioPlaying && this.audioThread != null) {
                this.bAudioPlaying = false;
                try {
                    this.audioThread.join();
                } catch (Exception unused) {
                }
                this.audioThread = null;
            }
        }
    }

    public boolean initAudioDev() throws IllegalStateException {
        int minBufferSize = AudioTrack.getMinBufferSize(SignalGenerator.SAMPLE_RATE_8, 4, 2);
        System.out.println("--audio, mMinBufSize=" + minBufferSize);
        if (minBufferSize == -2 || minBufferSize == -1) {
            System.out.println("初始化失败");
            return false;
        }
        try {
            this.m_AudioTrack = new AudioTrack(3, SignalGenerator.SAMPLE_RATE_8, 4, 2, minBufferSize * 2, 1);
            this.m_AudioTrack.play();
            return true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    class AudioPlayThread implements Runnable {
        AudioPlayThread() {
        }

        @Override // java.lang.Runnable
        public void run() throws IllegalStateException, InterruptedException {
            if (AudioPlayer.this.initAudioDev()) {
                while (AudioPlayer.this.bAudioPlaying) {
                    CustomBufferData customBufferDataRemoveData = AudioPlayer.this.audioBuffer.RemoveData();
                    if (customBufferDataRemoveData != null) {
                        AudioPlayer.this.m_AudioTrack.write(customBufferDataRemoveData.data, 0, customBufferDataRemoveData.head.length);
                    } else {
                        try {
                            Thread.sleep(10L);
                        } catch (Exception unused) {
                            AudioPlayer.this.m_AudioTrack.stop();
                            return;
                        }
                    }
                }
                AudioPlayer.this.m_AudioTrack.stop();
                AudioPlayer.this.m_AudioTrack.release();
                AudioPlayer.this.m_AudioTrack = null;
            }
        }
    }
}
