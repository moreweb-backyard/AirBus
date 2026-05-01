package com.shima.smartbushome.assist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/* loaded from: classes.dex */
public class CircleView extends View {
    private int backgroundColor;
    int center;
    int centerx;
    int centery;
    private int circleColor;
    private int circleWidth;
    public Dir dir;
    private int innerCircleColor;
    private float innerCircleRadius;
    int innerRadius;
    private int innerpressCircleColor;
    private OnPressListener listener;
    boolean longpress;
    public Canvas maincanvas;
    Context mcontext;
    View.OnTouchListener onTouchListener;
    private Paint paint;
    int press;
    int returnresult;
    private float smallCircle;

    public enum Dir {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        CENTER,
        UNDEFINE
    }

    public interface OnPressListener {
        void onPress(String str, String str2);
    }

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.circleWidth = 5;
        this.circleColor = -11028030;
        this.innerCircleColor = -11698802;
        this.innerpressCircleColor = 1078820238;
        this.backgroundColor = 0;
        this.paint = new Paint();
        this.center = 0;
        this.centerx = 0;
        this.centery = 0;
        this.innerRadius = 0;
        this.innerCircleRadius = 0.0f;
        this.smallCircle = 10.0f;
        this.dir = Dir.UP;
        this.press = 0;
        this.returnresult = 0;
        this.longpress = false;
        this.onTouchListener = new View.OnTouchListener() { // from class: com.shima.smartbushome.assist.CircleView.1
            long downTime;

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Dir dir = Dir.UNDEFINE;
                switch (motionEvent.getAction()) {
                    case 0:
                        this.downTime = motionEvent.getDownTime();
                        CircleView.this.touchdown(motionEvent.getX(), motionEvent.getY());
                        return true;
                    case 1:
                        motionEvent.getEventTime();
                        CircleView.this.press = 0;
                        if (CircleView.this.returnresult != 1) {
                            CircleView.this.touchup(motionEvent.getX(), motionEvent.getY());
                        }
                        CircleView.this.returnresult = 0;
                        CircleView.this.longpress = false;
                        return true;
                    case 2:
                        long eventTime = motionEvent.getEventTime();
                        if (!CircleView.this.longpress && eventTime - this.downTime > 700) {
                            CircleView.this.longpress = true;
                            CircleView.this.touchuplongpress(motionEvent.getX(), motionEvent.getY());
                            if (CircleView.this.returnresult == 1) {
                                CircleView.this.press = 0;
                                CircleView.this.invalidate();
                            }
                        }
                        return true;
                    default:
                        return true;
                }
            }
        };
        this.mcontext = context;
    }

    public CircleView(Context context) {
        super(context);
        this.circleWidth = 5;
        this.circleColor = -11028030;
        this.innerCircleColor = -11698802;
        this.innerpressCircleColor = 1078820238;
        this.backgroundColor = 0;
        this.paint = new Paint();
        this.center = 0;
        this.centerx = 0;
        this.centery = 0;
        this.innerRadius = 0;
        this.innerCircleRadius = 0.0f;
        this.smallCircle = 10.0f;
        this.dir = Dir.UP;
        this.press = 0;
        this.returnresult = 0;
        this.longpress = false;
        this.onTouchListener = new View.OnTouchListener() { // from class: com.shima.smartbushome.assist.CircleView.1
            long downTime;

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Dir dir = Dir.UNDEFINE;
                switch (motionEvent.getAction()) {
                    case 0:
                        this.downTime = motionEvent.getDownTime();
                        CircleView.this.touchdown(motionEvent.getX(), motionEvent.getY());
                        return true;
                    case 1:
                        motionEvent.getEventTime();
                        CircleView.this.press = 0;
                        if (CircleView.this.returnresult != 1) {
                            CircleView.this.touchup(motionEvent.getX(), motionEvent.getY());
                        }
                        CircleView.this.returnresult = 0;
                        CircleView.this.longpress = false;
                        return true;
                    case 2:
                        long eventTime = motionEvent.getEventTime();
                        if (!CircleView.this.longpress && eventTime - this.downTime > 700) {
                            CircleView.this.longpress = true;
                            CircleView.this.touchuplongpress(motionEvent.getX(), motionEvent.getY());
                            if (CircleView.this.returnresult == 1) {
                                CircleView.this.press = 0;
                                CircleView.this.invalidate();
                            }
                        }
                        return true;
                    default:
                        return true;
                }
            }
        };
        this.mcontext = context;
    }

    public CircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.circleWidth = 5;
        this.circleColor = -11028030;
        this.innerCircleColor = -11698802;
        this.innerpressCircleColor = 1078820238;
        this.backgroundColor = 0;
        this.paint = new Paint();
        this.center = 0;
        this.centerx = 0;
        this.centery = 0;
        this.innerRadius = 0;
        this.innerCircleRadius = 0.0f;
        this.smallCircle = 10.0f;
        this.dir = Dir.UP;
        this.press = 0;
        this.returnresult = 0;
        this.longpress = false;
        this.onTouchListener = new View.OnTouchListener() { // from class: com.shima.smartbushome.assist.CircleView.1
            long downTime;

            /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Dir dir = Dir.UNDEFINE;
                switch (motionEvent.getAction()) {
                    case 0:
                        this.downTime = motionEvent.getDownTime();
                        CircleView.this.touchdown(motionEvent.getX(), motionEvent.getY());
                        return true;
                    case 1:
                        motionEvent.getEventTime();
                        CircleView.this.press = 0;
                        if (CircleView.this.returnresult != 1) {
                            CircleView.this.touchup(motionEvent.getX(), motionEvent.getY());
                        }
                        CircleView.this.returnresult = 0;
                        CircleView.this.longpress = false;
                        return true;
                    case 2:
                        long eventTime = motionEvent.getEventTime();
                        if (!CircleView.this.longpress && eventTime - this.downTime > 700) {
                            CircleView.this.longpress = true;
                            CircleView.this.touchuplongpress(motionEvent.getX(), motionEvent.getY());
                            if (CircleView.this.returnresult == 1) {
                                CircleView.this.press = 0;
                                CircleView.this.invalidate();
                            }
                        }
                        return true;
                    default:
                        return true;
                }
            }
        };
        this.mcontext = context;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int iMeasureHeight = measureHeight(i2);
        int iMeasureWidth = measureWidth(i);
        setMeasuredDimension(iMeasureWidth, iMeasureHeight);
        this.center = Math.min(iMeasureWidth, iMeasureHeight) / 2;
        this.centerx = iMeasureWidth / 2;
        this.centery = iMeasureHeight / 2;
        this.innerCircleRadius = this.center / 2;
        this.innerRadius = this.center - 2;
        setOnTouchListener(this.onTouchListener);
    }

    private int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == Integer.MIN_VALUE) {
            return getWidth();
        }
        if (mode == 1073741824) {
            return size;
        }
        return 0;
    }

    private int measureHeight(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == Integer.MIN_VALUE || mode == 1073741824) {
            return size;
        }
        return 0;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.maincanvas = canvas;
        initBackGround(canvas);
        drawDirTriangle(canvas, this.dir);
    }

    private void drawDirTriangle(Canvas canvas, Dir dir) {
        this.paint.setColor(this.innerCircleColor);
        this.paint.setStrokeWidth(1.0f);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(this.backgroundColor);
    }

    private void drawOnclikColor(Canvas canvas, Dir dir) {
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(100.0f);
        switch (dir) {
            case UP:
                canvas.drawArc(new RectF(this.centerx - this.innerRadius, this.centery - this.innerRadius, this.centerx + this.innerRadius, this.centery + this.innerRadius), 225.0f, 90.0f, false, this.paint);
                break;
            case DOWN:
                canvas.drawArc(new RectF(this.centerx - this.innerRadius, this.centery - this.innerRadius, this.centerx + this.innerRadius, this.centery + this.innerRadius), 45.0f, 90.0f, false, this.paint);
                break;
            case LEFT:
                canvas.drawArc(new RectF(this.centerx - this.innerRadius, this.centery - this.innerRadius, this.centerx + this.innerRadius, this.centery + this.innerRadius), 135.0f, 90.0f, false, this.paint);
                break;
            case RIGHT:
                canvas.drawArc(new RectF(this.centerx - this.innerRadius, this.centery - this.innerRadius, this.centerx + this.innerRadius, this.centery + this.innerRadius), -45.0f, 90.0f, false, this.paint);
                break;
        }
        this.paint.setStyle(Paint.Style.FILL);
    }

    private void drawUpTriangle(Canvas canvas) {
        Path path = new Path();
        path.moveTo(this.center, this.center);
        double d = this.innerCircleRadius;
        double dSqrt = Math.sqrt(2.0d);
        Double.isNaN(d);
        double d2 = d / dSqrt;
        double d3 = this.innerCircleRadius;
        double dSqrt2 = Math.sqrt(2.0d);
        Double.isNaN(d3);
        double d4 = d3 * dSqrt2;
        double d5 = this.center;
        Double.isNaN(d5);
        double d6 = this.center;
        Double.isNaN(d6);
        path.lineTo((float) (d5 - d2), (float) (d6 - d2));
        float f = this.center;
        double d7 = this.center;
        Double.isNaN(d7);
        path.lineTo(f, (float) (d7 - d4));
        double d8 = this.center;
        Double.isNaN(d8);
        double d9 = this.center;
        Double.isNaN(d9);
        path.lineTo((float) (d8 + d2), (float) (d9 - d2));
        canvas.drawPath(path, this.paint);
        this.paint.setColor(this.backgroundColor);
        canvas.drawLine(this.center, this.center, this.center, this.center - this.innerCircleRadius, this.paint);
        drawOnclikColor(canvas, Dir.UP);
    }

    private void initBackGround(Canvas canvas) {
        clearCanvas(canvas);
        drawBackCircle(canvas);
        drawInnerCircle(canvas);
    }

    private void drawInnerCircle(Canvas canvas) {
        if (this.press == 1) {
            this.paint.setColor(this.innerCircleColor);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeWidth(2.0f);
            canvas.drawCircle(this.centerx, this.centery, this.innerCircleRadius, this.paint);
            this.paint.setColor(-1);
            this.paint.setTextSize(50.0f);
            this.paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("OK", this.centerx, this.centery + 20, this.paint);
            return;
        }
        this.paint.setColor(this.innerCircleColor);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(2.0f);
        canvas.drawCircle(this.centerx, this.centery, this.innerCircleRadius, this.paint);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setTextSize(50.0f);
        this.paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("OK", this.centerx, this.centery + 20, this.paint);
    }

    private void drawBackCircle(Canvas canvas) {
        this.paint.setColor(this.circleColor);
        this.paint.setStrokeWidth(this.circleWidth);
        switch (this.press) {
            case 0:
            case 1:
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(this.centerx, this.centery, this.innerRadius, this.paint);
                break;
            case 2:
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(this.centerx, this.centery, this.innerRadius, this.paint);
                float f = this.innerCircleRadius + ((this.innerRadius - this.innerCircleRadius) / 2.0f);
                this.paint.setStrokeWidth((f - this.innerCircleRadius) * 2.0f);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setColor(-16776961);
                canvas.drawArc(new RectF(this.centerx - f, this.centery - f, this.centerx + f, this.centery + f), 225.0f, 90.0f, false, this.paint);
                break;
            case 3:
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(this.centerx, this.centery, this.innerRadius, this.paint);
                float f2 = this.innerCircleRadius + ((this.innerRadius - this.innerCircleRadius) / 2.0f);
                this.paint.setStrokeWidth((f2 - this.innerCircleRadius) * 2.0f);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setColor(-16776961);
                canvas.drawArc(new RectF(this.centerx - f2, this.centery - f2, this.centerx + f2, this.centery + f2), 315.0f, 90.0f, false, this.paint);
                break;
            case 4:
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(this.centerx, this.centery, this.innerRadius, this.paint);
                float f3 = this.innerCircleRadius + ((this.innerRadius - this.innerCircleRadius) / 2.0f);
                this.paint.setStrokeWidth((f3 - this.innerCircleRadius) * 2.0f);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setColor(-16776961);
                canvas.drawArc(new RectF(this.centerx - f3, this.centery - f3, this.centerx + f3, this.centery + f3), 135.0f, 90.0f, false, this.paint);
                break;
            case 5:
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(this.centerx, this.centery, this.innerRadius, this.paint);
                float f4 = this.innerCircleRadius + ((this.innerRadius - this.innerCircleRadius) / 2.0f);
                this.paint.setStrokeWidth((f4 - this.innerCircleRadius) * 2.0f);
                this.paint.setStyle(Paint.Style.STROKE);
                this.paint.setColor(-16776961);
                canvas.drawArc(new RectF(this.centerx - f4, this.centery - f4, this.centerx + f4, this.centery + f4), 45.0f, 90.0f, false, this.paint);
                break;
        }
        this.paint.setColor(-1);
        this.paint.setStrokeWidth(1.0f);
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.paint.setTextSize(100.0f);
        this.paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("^", this.centerx, (this.centery - ((this.innerRadius - this.innerCircleRadius) / 5.0f)) - this.innerCircleRadius, this.paint);
        canvas.drawText("v", this.centerx, this.centery + ((this.innerRadius - this.innerCircleRadius) / 2.0f) + this.innerCircleRadius, this.paint);
        canvas.drawText("<", (this.centerx - ((this.innerRadius - this.innerCircleRadius) / 2.0f)) - this.innerCircleRadius, this.centery + 25, this.paint);
        canvas.drawText(">", this.centerx + ((this.innerRadius - this.innerCircleRadius) / 2.0f) + this.innerCircleRadius, this.centery + 25, this.paint);
        this.paint.setColor(this.circleColor);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setStrokeWidth(4.0f);
        double d = this.innerCircleRadius;
        double dSqrt = Math.sqrt(2.0d);
        Double.isNaN(d);
        float f5 = (float) (d / dSqrt);
        double d2 = this.innerRadius;
        double dSqrt2 = Math.sqrt(2.0d);
        Double.isNaN(d2);
        float f6 = (float) (d2 / dSqrt2);
        canvas.drawLine(this.centerx - f5, this.centery - f5, this.centerx - f6, this.centery - f6, this.paint);
        canvas.drawLine(this.centerx + f5, this.centery - f5, this.centerx + f6, this.centery - f6, this.paint);
        canvas.drawLine(this.centerx - f5, this.centery + f5, this.centerx - f6, this.centery + f6, this.paint);
        canvas.drawLine(this.centerx + f5, this.centery + f5, this.centerx + f6, this.centery + f6, this.paint);
    }

    private void clearCanvas(Canvas canvas) {
        canvas.drawColor(this.backgroundColor);
    }

    public void setOnPressListener(OnPressListener onPressListener) {
        this.listener = onPressListener;
    }

    public void touchuplongpress(float f, float f2) {
        if (Math.sqrt(Math.pow(f2 - this.center, 2.0d) + Math.pow(f - this.center, 2.0d)) < this.innerCircleRadius) {
            this.dir = Dir.CENTER;
            if (this.listener == null || this.press != 1) {
                return;
            }
            this.listener.onPress("center", "longpress");
            this.returnresult = 1;
            return;
        }
        if (getangle(f, f2) > 45.0d && getangle(f, f2) < 135.0d) {
            this.dir = Dir.UP;
            if (this.listener == null || this.press != 2) {
                return;
            }
            this.listener.onPress("up", "longpress");
            this.returnresult = 1;
            return;
        }
        if ((getangle(f, f2) >= 0.0d && getangle(f, f2) < 45.0d) || (getangle(f, f2) > 315.0d && getangle(f, f2) <= 360.0d)) {
            this.dir = Dir.RIGHT;
            if (this.listener == null || this.press != 3) {
                return;
            }
            this.listener.onPress("right", "longpress");
            this.returnresult = 1;
            return;
        }
        if (getangle(f, f2) > 135.0d && getangle(f, f2) < 225.0d) {
            this.dir = Dir.LEFT;
            if (this.listener == null || this.press != 4) {
                return;
            }
            this.listener.onPress("left", "longpress");
            this.returnresult = 1;
            return;
        }
        if (getangle(f, f2) <= 225.0d || getangle(f, f2) >= 315.0d) {
            return;
        }
        this.dir = Dir.DOWN;
        if (this.listener == null || this.press != 5) {
            return;
        }
        this.listener.onPress("down", "longpress");
        this.returnresult = 1;
    }

    public void touchup(float f, float f2) {
        if (Math.sqrt(Math.pow(f2 - this.center, 2.0d) + Math.pow(f - this.center, 2.0d)) < this.innerCircleRadius) {
            this.dir = Dir.CENTER;
            if (this.listener != null) {
                this.listener.onPress("center", "press");
            }
        } else if (getangle(f, f2) > 45.0d && getangle(f, f2) < 135.0d) {
            this.dir = Dir.UP;
            if (this.listener != null) {
                this.listener.onPress("up", "press");
            }
        } else if ((getangle(f, f2) >= 0.0d && getangle(f, f2) < 45.0d) || (getangle(f, f2) > 315.0d && getangle(f, f2) <= 360.0d)) {
            this.dir = Dir.RIGHT;
            if (this.listener != null) {
                this.listener.onPress("right", "press");
            }
        } else if (getangle(f, f2) > 135.0d && getangle(f, f2) < 225.0d) {
            this.dir = Dir.LEFT;
            if (this.listener != null) {
                this.listener.onPress("left", "press");
            }
        } else if (getangle(f, f2) > 225.0d && getangle(f, f2) < 315.0d) {
            this.dir = Dir.DOWN;
            if (this.listener != null) {
                this.listener.onPress("down", "press");
            }
        }
        invalidate();
    }

    public void touchdown(float f, float f2) {
        if (Math.sqrt(Math.pow(f2 - this.centery, 2.0d) + Math.pow(f - this.centerx, 2.0d)) < this.innerCircleRadius) {
            this.dir = Dir.CENTER;
            this.press = 1;
        } else if (getangle(f, f2) > 45.0d && getangle(f, f2) < 135.0d) {
            this.dir = Dir.UP;
            this.press = 2;
        } else if ((getangle(f, f2) >= 0.0d && getangle(f, f2) < 45.0d) || (getangle(f, f2) > 315.0d && getangle(f, f2) <= 360.0d)) {
            this.dir = Dir.RIGHT;
            this.press = 3;
        } else if (getangle(f, f2) > 135.0d && getangle(f, f2) < 225.0d) {
            this.dir = Dir.LEFT;
            this.press = 4;
        } else if (getangle(f, f2) > 225.0d && getangle(f, f2) < 315.0d) {
            this.dir = Dir.DOWN;
            this.press = 5;
        }
        invalidate();
    }

    public double getangle(float f, float f2) {
        int i = ((int) f) - this.centerx;
        int i2 = ((int) f2) - this.centery;
        int i3 = i2 * i2;
        int iSqrt = (int) Math.sqrt(r1 + i3);
        double d = ((i * i) + (iSqrt * iSqrt)) - i3;
        double d2 = i;
        Double.isNaN(d2);
        double d3 = iSqrt;
        Double.isNaN(d3);
        Double.isNaN(d);
        double degrees = Math.toDegrees(Math.acos(d / ((d2 * 2.0d) * d3)));
        return f2 > ((float) this.centery) ? 360.0d - degrees : degrees;
    }
}
