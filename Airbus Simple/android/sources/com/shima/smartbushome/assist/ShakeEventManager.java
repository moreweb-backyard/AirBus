package com.shima.smartbushome.assist;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

/* loaded from: classes.dex */
public class ShakeEventManager implements SensorEventListener {
    private static final float ALPHA = 0.8f;
    public static final int DOWM = 2;
    public static final int LEFT = 3;
    private static final int MOV_COUNTS = 2;
    private static final int MOV_COUNTSDATA = 3;
    private static final int MOV_THRESHOLD = 4;
    public static final int RIGHT = 4;
    public static final int SHAKE_DOWM = 6;
    public static final int SHAKE_LEFT = 1;
    public static final int SHAKE_RIGHT = 2;
    public static final int SHAKE_UP = 5;
    private static final int SHAKE_WINDOW_TIME_INTERVAL = 350;
    public static final int UP = 1;
    private int counter;
    private long firstMovTime;
    private ShakeListener listener;
    private Sensor s;
    private SensorManager sManager;
    private float[] gravity = new float[3];
    int delayremark = 0;
    int getingdata = 0;
    float[] olddirection = new float[3];
    Handler delay = new Handler();
    Runnable delayrun = new Runnable() { // from class: com.shima.smartbushome.assist.ShakeEventManager.1
        @Override // java.lang.Runnable
        public void run() {
            ShakeEventManager.this.delayremark = 0;
            ShakeEventManager.this.delay.removeCallbacks(ShakeEventManager.this.delayrun);
        }
    };
    float[] maxnum = new float[3];
    float[] maxnumdir = new float[3];

    public interface ShakeListener {
        void onShake(int i);
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void setListener(ShakeListener shakeListener) {
        this.listener = shakeListener;
    }

    public void init(Context context) {
        this.sManager = (SensorManager) context.getSystemService("sensor");
        this.s = this.sManager.getDefaultSensor(1);
        register();
    }

    public void register() {
        this.sManager.registerListener(this, this.s, 3);
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArrCalcMaxAcceleration = calcMaxAcceleration(sensorEvent);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (((int) fArrCalcMaxAcceleration[1]) != 0 && this.getingdata == 0) {
            this.delayremark = 1;
            this.getingdata = 1;
        }
        if (this.delayremark == 1) {
            if (this.counter < 3) {
                this.maxnum[this.counter] = fArrCalcMaxAcceleration[0];
                this.maxnumdir[this.counter] = fArrCalcMaxAcceleration[1];
            }
            if (this.counter == 0) {
                this.counter++;
                this.firstMovTime = System.currentTimeMillis();
                return;
            }
            this.counter++;
            if (this.counter < 2 || this.listener == null || jCurrentTimeMillis - this.firstMovTime <= 350) {
                return;
            }
            this.listener.onShake(getmaxdir());
            resetAllData();
        }
    }

    public int getmaxdir() {
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < 3; i3++) {
            if (i <= ((int) this.maxnum[i3])) {
                i = (int) this.maxnum[i3];
                i2 = (int) this.maxnumdir[i3];
            }
        }
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < 3; i7++) {
            if (((int) this.maxnumdir[i7]) == 1) {
                i4++;
            }
            if (((int) this.maxnumdir[i7]) == 2) {
                i5++;
            }
            if (((int) this.maxnumdir[i7]) == 5 || ((int) this.maxnumdir[i7]) == 6) {
                i6++;
            }
        }
        int iMax = Math.max(Math.max(i4, i5), i6);
        int i8 = iMax == i4 ? 2 : 0;
        if (iMax == i5) {
            i8 = 1;
        }
        if (iMax == i6) {
            i8 = 5;
        }
        return (i2 == i8 || (i2 == 5 && i8 == 6) || ((i2 == 6 && i8 == 5) || i > 10 || iMax <= 5)) ? i2 : i8;
    }

    public void deregister() {
        this.sManager.unregisterListener(this);
    }

    private float[] calcMaxAcceleration(SensorEvent sensorEvent) {
        float[] fArr = new float[2];
        this.gravity[0] = calcGravityForce(sensorEvent.values[0], 0);
        this.gravity[1] = calcGravityForce(sensorEvent.values[1], 1);
        this.gravity[2] = calcGravityForce(sensorEvent.values[2], 2);
        float f = sensorEvent.values[0] - this.gravity[0];
        float f2 = sensorEvent.values[1] - this.gravity[1];
        float f3 = sensorEvent.values[2] - this.gravity[2];
        float fMax = Math.max(Math.max(Math.abs(f), Math.abs(f2)), Math.abs(f3));
        if (fMax != Math.abs(f) || fMax <= 4.0f) {
            if (fMax != Math.abs(f2) || fMax <= 4.0f) {
                if (fMax == Math.abs(f3) && fMax > 4.0f) {
                    if (f3 > 0.0f) {
                        fArr[0] = fMax;
                        fArr[1] = 6.0f;
                    } else {
                        fArr[0] = fMax;
                        fArr[1] = 5.0f;
                    }
                }
            } else if (f2 > 0.0f) {
                fArr[0] = fMax;
                fArr[1] = 3.0f;
            } else {
                fArr[0] = fMax;
                fArr[1] = 4.0f;
            }
        } else if (f > 0.0f) {
            fArr[0] = fMax;
            fArr[1] = 1.0f;
        } else {
            fArr[0] = fMax;
            fArr[1] = 2.0f;
        }
        return fArr;
    }

    private float calcGravityForce(float f, int i) {
        return (this.gravity[i] * ALPHA) + (f * 0.19999999f);
    }

    private void resetAllData() {
        Log.d("SwA", "Reset all data");
        this.counter = 0;
        this.delayremark = 0;
        this.getingdata = 0;
        this.firstMovTime = System.currentTimeMillis();
        for (int i = 0; i < 3; i++) {
            this.maxnum[i] = 0.0f;
            this.maxnumdir[i] = 0.0f;
        }
    }

    public void shakedirection(float[] fArr, float[] fArr2) {
        int i = (((int) fArr[0]) * 100) + (((int) fArr[1]) * 10) + (((int) fArr[2]) * 1);
        int i2 = (((int) fArr2[0]) * 100) + (((int) fArr2[1]) * 10) + (((int) fArr2[2]) * 1);
        char c = 4;
        switch (i) {
            case 1:
            case 11:
            case 21:
                c = 2;
                break;
            case 2:
            case 12:
            case 22:
                c = 1;
                break;
            case 10:
            case 20:
            default:
                c = 0;
                break;
            case 100:
            case 101:
            case 102:
            case 110:
            case 111:
            case 112:
            case 120:
            case 121:
            case 122:
                c = 3;
                break;
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 220:
            case 221:
            case 222:
                break;
        }
        switch (i2) {
            case 1:
                switch (c) {
                    case 1:
                        this.listener.onShake(5);
                        break;
                }
            case 2:
                switch (c) {
                    case 2:
                        this.listener.onShake(6);
                        break;
                }
            case 11:
                switch (c) {
                    case 1:
                        this.listener.onShake(5);
                        break;
                }
            case 12:
                switch (c) {
                    case 2:
                        this.listener.onShake(6);
                        break;
                }
            case 21:
                switch (c) {
                    case 1:
                        this.listener.onShake(5);
                        break;
                }
            case 22:
                switch (c) {
                    case 2:
                        this.listener.onShake(6);
                        break;
                }
            case 100:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 101:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 102:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 110:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 111:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 112:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 120:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 121:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 122:
                switch (c) {
                    case 4:
                        this.listener.onShake(2);
                        break;
                }
            case 200:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 201:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 202:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 210:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 211:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 212:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 220:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 221:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
            case 222:
                switch (c) {
                    case 3:
                        this.listener.onShake(1);
                        break;
                }
        }
        resetAllData();
        Log.d("SwA", "////old:" + i + "------new:" + i2 + "//////////////////////////////////");
    }
}
