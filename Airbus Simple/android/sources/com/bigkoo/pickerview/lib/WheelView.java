package com.bigkoo.pickerview.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.adapter.WheelAdapter;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.bigkoo.pickerview.model.IPickerViewData;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class WheelView extends View {
    private static final float CENTERCONTENTOFFSET = 7.0f;
    private static final float SCALECONTENT = 0.8f;
    private static final int VELOCITYFLING = 5;
    WheelAdapter adapter;
    float centerY;
    int change;
    Context context;
    int dividerColor;
    private DividerType dividerType;
    private int drawCenterContentStart;
    private int drawOutContentStart;
    float firstLineY;
    private GestureDetector gestureDetector;
    int halfCircumference;
    Handler handler;
    int initPosition;
    boolean isLoop;
    private boolean isOptions;
    float itemHeight;
    int itemsVisible;
    private String label;
    float lineSpacingMultiplier;
    ScheduledExecutorService mExecutor;
    private ScheduledFuture<?> mFuture;
    private int mGravity;
    private int mOffset;
    int maxTextHeight;
    int maxTextWidth;
    int measuredHeight;
    int measuredWidth;
    OnItemSelectedListener onItemSelectedListener;
    Paint paintCenterText;
    Paint paintIndicator;
    Paint paintOuterText;
    int preCurrentIndex;
    private float previousY;
    int radius;
    float secondLineY;
    private int selectedItem;
    long startTime;
    int textColorCenter;
    int textColorOut;
    int textSize;
    int totalScrollY;
    int widthMeasureSpec;

    public enum ACTION {
        CLICK,
        FLING,
        DAGGLE
    }

    public enum DividerType {
        FILL,
        WRAP
    }

    public WheelView(Context context) {
        this(context, null);
    }

    public WheelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isOptions = false;
        this.mExecutor = Executors.newSingleThreadScheduledExecutor();
        this.textColorOut = -5723992;
        this.textColorCenter = -14013910;
        this.dividerColor = -2763307;
        this.lineSpacingMultiplier = 1.6f;
        this.itemsVisible = 11;
        this.mOffset = 0;
        this.previousY = 0.0f;
        this.startTime = 0L;
        this.mGravity = 17;
        this.drawCenterContentStart = 0;
        this.drawOutContentStart = 0;
        this.textSize = getResources().getDimensionPixelSize(R.dimen.pickerview_textsize);
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.pickerview, 0, 0);
            this.mGravity = typedArrayObtainStyledAttributes.getInt(R.styleable.pickerview_pickerview_gravity, 17);
            this.textColorOut = typedArrayObtainStyledAttributes.getColor(R.styleable.pickerview_pickerview_textColorOut, this.textColorOut);
            this.textColorCenter = typedArrayObtainStyledAttributes.getColor(R.styleable.pickerview_pickerview_textColorCenter, this.textColorCenter);
            this.dividerColor = typedArrayObtainStyledAttributes.getColor(R.styleable.pickerview_pickerview_dividerColor, this.dividerColor);
            this.textSize = typedArrayObtainStyledAttributes.getDimensionPixelOffset(R.styleable.pickerview_pickerview_textSize, this.textSize);
            this.lineSpacingMultiplier = typedArrayObtainStyledAttributes.getFloat(R.styleable.pickerview_pickerview_lineSpacingMultiplier, this.lineSpacingMultiplier);
            typedArrayObtainStyledAttributes.recycle();
        }
        judgeLineSpae();
        initLoopView(context);
    }

    private void judgeLineSpae() {
        if (this.lineSpacingMultiplier < 1.2f) {
            this.lineSpacingMultiplier = 1.2f;
        } else if (this.lineSpacingMultiplier > 2.0f) {
            this.lineSpacingMultiplier = 2.0f;
        }
    }

    private void initLoopView(Context context) {
        this.context = context;
        this.handler = new MessageHandler(this);
        this.gestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        this.gestureDetector.setIsLongpressEnabled(false);
        this.isLoop = true;
        this.totalScrollY = 0;
        this.initPosition = -1;
        initPaints();
    }

    private void initPaints() {
        this.paintOuterText = new Paint();
        this.paintOuterText.setColor(this.textColorOut);
        this.paintOuterText.setAntiAlias(true);
        this.paintOuterText.setTypeface(Typeface.MONOSPACE);
        this.paintOuterText.setTextSize(this.textSize);
        this.paintCenterText = new Paint();
        this.paintCenterText.setColor(this.textColorCenter);
        this.paintCenterText.setAntiAlias(true);
        this.paintCenterText.setTextScaleX(1.1f);
        this.paintCenterText.setTypeface(Typeface.MONOSPACE);
        this.paintCenterText.setTextSize(this.textSize);
        this.paintIndicator = new Paint();
        this.paintIndicator.setColor(this.dividerColor);
        this.paintIndicator.setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
    }

    private void remeasure() {
        if (this.adapter == null) {
            return;
        }
        measureTextWidthHeight();
        this.halfCircumference = (int) (this.itemHeight * (this.itemsVisible - 1));
        double d = this.halfCircumference * 2;
        Double.isNaN(d);
        this.measuredHeight = (int) (d / 3.141592653589793d);
        double d2 = this.halfCircumference;
        Double.isNaN(d2);
        this.radius = (int) (d2 / 3.141592653589793d);
        this.measuredWidth = View.MeasureSpec.getSize(this.widthMeasureSpec);
        this.firstLineY = (this.measuredHeight - this.itemHeight) / 2.0f;
        this.secondLineY = (this.measuredHeight + this.itemHeight) / 2.0f;
        this.centerY = (this.secondLineY - ((this.itemHeight - this.maxTextHeight) / 2.0f)) - CENTERCONTENTOFFSET;
        if (this.initPosition == -1) {
            if (this.isLoop) {
                this.initPosition = (this.adapter.getItemsCount() + 1) / 2;
            } else {
                this.initPosition = 0;
            }
        }
        this.preCurrentIndex = this.initPosition;
    }

    private void measureTextWidthHeight() {
        Rect rect = new Rect();
        for (int i = 0; i < this.adapter.getItemsCount(); i++) {
            String contentText = getContentText(this.adapter.getItem(i));
            this.paintCenterText.getTextBounds(contentText, 0, contentText.length(), rect);
            int iWidth = rect.width();
            if (iWidth > this.maxTextWidth) {
                this.maxTextWidth = iWidth;
            }
            this.paintCenterText.getTextBounds("星期", 0, 2, rect);
            int iHeight = rect.height();
            if (iHeight > this.maxTextHeight) {
                this.maxTextHeight = iHeight;
            }
        }
        this.itemHeight = this.lineSpacingMultiplier * this.maxTextHeight;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            this.mOffset = (int) (((this.totalScrollY % this.itemHeight) + this.itemHeight) % this.itemHeight);
            if (this.mOffset > this.itemHeight / 2.0f) {
                this.mOffset = (int) (this.itemHeight - this.mOffset);
            } else {
                this.mOffset = -this.mOffset;
            }
        }
        this.mFuture = this.mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, this.mOffset), 0L, 10L, TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float f) {
        cancelFuture();
        this.mFuture = this.mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, f), 0L, 5L, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (this.mFuture == null || this.mFuture.isCancelled()) {
            return;
        }
        this.mFuture.cancel(true);
        this.mFuture = null;
    }

    public final void setCyclic(boolean z) {
        this.isLoop = z;
    }

    public final void setTextSize(float f) {
        if (f > 0.0f) {
            this.textSize = (int) (this.context.getResources().getDisplayMetrics().density * f);
            this.paintOuterText.setTextSize(this.textSize);
            this.paintCenterText.setTextSize(this.textSize);
        }
    }

    public final void setCurrentItem(int i) {
        this.initPosition = i;
        this.totalScrollY = 0;
        invalidate();
    }

    public final void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public final void setAdapter(WheelAdapter wheelAdapter) {
        this.adapter = wheelAdapter;
        remeasure();
        invalidate();
    }

    public final WheelAdapter getAdapter() {
        return this.adapter;
    }

    public final int getCurrentItem() {
        return this.selectedItem;
    }

    protected final void onItemSelected() {
        if (this.onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        if (this.adapter == null) {
            return;
        }
        Object[] objArr = new Object[this.itemsVisible];
        this.change = (int) (this.totalScrollY / this.itemHeight);
        try {
            this.preCurrentIndex = this.initPosition + (this.change % this.adapter.getItemsCount());
        } catch (ArithmeticException unused) {
            Log.e("WheelView", "出错了！adapter.getItemsCount() == 0，联动数据不匹配");
        }
        if (!this.isLoop) {
            if (this.preCurrentIndex < 0) {
                this.preCurrentIndex = 0;
            }
            if (this.preCurrentIndex > this.adapter.getItemsCount() - 1) {
                this.preCurrentIndex = this.adapter.getItemsCount() - 1;
            }
        } else {
            if (this.preCurrentIndex < 0) {
                this.preCurrentIndex = this.adapter.getItemsCount() + this.preCurrentIndex;
            }
            if (this.preCurrentIndex > this.adapter.getItemsCount() - 1) {
                this.preCurrentIndex -= this.adapter.getItemsCount();
            }
        }
        int i2 = (int) (this.totalScrollY % this.itemHeight);
        for (int i3 = 0; i3 < this.itemsVisible; i3++) {
            int i4 = this.preCurrentIndex - ((this.itemsVisible / 2) - i3);
            if (this.isLoop) {
                objArr[i3] = this.adapter.getItem(getLoopMappingIndex(i4));
            } else if (i4 < 0) {
                objArr[i3] = "";
            } else if (i4 > this.adapter.getItemsCount() - 1) {
                objArr[i3] = "";
            } else {
                objArr[i3] = this.adapter.getItem(i4);
            }
        }
        if (this.dividerType == DividerType.WRAP) {
            float f = TextUtils.isEmpty(this.label) ? ((this.measuredWidth - this.maxTextWidth) / 2) - 12 : ((this.measuredWidth - this.maxTextWidth) / 4) - 12;
            float f2 = f <= 0.0f ? 10.0f : f;
            float f3 = this.measuredWidth - f2;
            float f4 = f2;
            canvas.drawLine(f4, this.firstLineY, f3, this.firstLineY, this.paintIndicator);
            canvas.drawLine(f4, this.secondLineY, f3, this.secondLineY, this.paintIndicator);
        } else {
            canvas.drawLine(0.0f, this.firstLineY, this.measuredWidth, this.firstLineY, this.paintIndicator);
            canvas.drawLine(0.0f, this.secondLineY, this.measuredWidth, this.secondLineY, this.paintIndicator);
        }
        if (!TextUtils.isEmpty(this.label)) {
            canvas.drawText(this.label, (this.measuredWidth - getTextWidth(this.paintCenterText, this.label)) - CENTERCONTENTOFFSET, this.centerY, this.paintCenterText);
        }
        int i5 = 0;
        while (i5 < this.itemsVisible) {
            canvas.save();
            double d = ((this.itemHeight * i5) - i2) / this.radius;
            Double.isNaN(d);
            float f5 = (float) (90.0d - ((d / 3.141592653589793d) * 180.0d));
            if (f5 >= 90.0f || f5 <= -90.0f) {
                i = i2;
                canvas.restore();
            } else {
                String contentText = getContentText(objArr[i5]);
                reMeasureTextSize(contentText);
                measuredCenterContentStart(contentText);
                measuredOutContentStart(contentText);
                double d2 = this.radius;
                double dCos = Math.cos(d);
                i = i2;
                double d3 = this.radius;
                Double.isNaN(d3);
                Double.isNaN(d2);
                double d4 = d2 - (dCos * d3);
                double dSin = Math.sin(d);
                double d5 = this.maxTextHeight;
                Double.isNaN(d5);
                float f6 = (float) (d4 - ((dSin * d5) / 2.0d));
                canvas.translate(0.0f, f6);
                canvas.scale(1.0f, (float) Math.sin(d));
                if (f6 <= this.firstLineY && this.maxTextHeight + f6 >= this.firstLineY) {
                    canvas.save();
                    canvas.clipRect(0.0f, 0.0f, this.measuredWidth, this.firstLineY - f6);
                    canvas.scale(1.0f, ((float) Math.sin(d)) * SCALECONTENT);
                    canvas.drawText(contentText, this.drawOutContentStart, this.maxTextHeight, this.paintOuterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0.0f, this.firstLineY - f6, this.measuredWidth, (int) this.itemHeight);
                    canvas.scale(1.0f, ((float) Math.sin(d)) * 1.0f);
                    canvas.drawText(contentText, this.drawCenterContentStart, this.maxTextHeight - CENTERCONTENTOFFSET, this.paintCenterText);
                    canvas.restore();
                } else if (f6 <= this.secondLineY && this.maxTextHeight + f6 >= this.secondLineY) {
                    canvas.save();
                    canvas.clipRect(0.0f, 0.0f, this.measuredWidth, this.secondLineY - f6);
                    canvas.scale(1.0f, ((float) Math.sin(d)) * 1.0f);
                    canvas.drawText(contentText, this.drawCenterContentStart, this.maxTextHeight - CENTERCONTENTOFFSET, this.paintCenterText);
                    canvas.restore();
                    canvas.save();
                    canvas.clipRect(0.0f, this.secondLineY - f6, this.measuredWidth, (int) this.itemHeight);
                    canvas.scale(1.0f, ((float) Math.sin(d)) * SCALECONTENT);
                    canvas.drawText(contentText, this.drawOutContentStart, this.maxTextHeight, this.paintOuterText);
                    canvas.restore();
                } else if (f6 >= this.firstLineY && this.maxTextHeight + f6 <= this.secondLineY) {
                    canvas.drawText(contentText, this.drawCenterContentStart, this.maxTextHeight - CENTERCONTENTOFFSET, this.paintCenterText);
                    this.selectedItem = this.adapter.indexOf(objArr[i5]);
                } else {
                    canvas.save();
                    canvas.clipRect(0, 0, this.measuredWidth, (int) this.itemHeight);
                    canvas.scale(1.0f, ((float) Math.sin(d)) * SCALECONTENT);
                    canvas.drawText(contentText, this.drawOutContentStart, this.maxTextHeight, this.paintOuterText);
                    canvas.restore();
                    canvas.restore();
                    this.paintCenterText.setTextSize(this.textSize);
                }
                canvas.restore();
                this.paintCenterText.setTextSize(this.textSize);
            }
            i5++;
            i2 = i;
        }
    }

    private void reMeasureTextSize(String str) {
        Rect rect = new Rect();
        this.paintCenterText.getTextBounds(str, 0, str.length(), rect);
        int i = this.textSize;
        for (int iWidth = rect.width(); iWidth > this.measuredWidth; iWidth = rect.width()) {
            i--;
            this.paintCenterText.setTextSize(i);
            this.paintCenterText.getTextBounds(str, 0, str.length(), rect);
        }
        this.paintOuterText.setTextSize(i);
    }

    private int getLoopMappingIndex(int i) {
        if (i < 0) {
            return getLoopMappingIndex(i + this.adapter.getItemsCount());
        }
        return i > this.adapter.getItemsCount() + (-1) ? getLoopMappingIndex(i - this.adapter.getItemsCount()) : i;
    }

    private String getContentText(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof IPickerViewData) {
            return ((IPickerViewData) obj).getPickerViewText();
        }
        return obj instanceof Integer ? String.format(Locale.getDefault(), "%02d", Integer.valueOf(((Integer) obj).intValue())) : obj.toString();
    }

    private void measuredCenterContentStart(String str) {
        Rect rect = new Rect();
        this.paintCenterText.getTextBounds(str, 0, str.length(), rect);
        int i = this.mGravity;
        if (i == 3) {
            this.drawCenterContentStart = 0;
            return;
        }
        if (i == 5) {
            this.drawCenterContentStart = (this.measuredWidth - rect.width()) - 7;
            return;
        }
        if (i != 17) {
            return;
        }
        if (this.isOptions || this.label == null || this.label.equals("")) {
            double dWidth = this.measuredWidth - rect.width();
            Double.isNaN(dWidth);
            this.drawCenterContentStart = (int) (dWidth * 0.5d);
        } else {
            double dWidth2 = this.measuredWidth - rect.width();
            Double.isNaN(dWidth2);
            this.drawCenterContentStart = (int) (dWidth2 * 0.25d);
        }
    }

    private void measuredOutContentStart(String str) {
        Rect rect = new Rect();
        this.paintOuterText.getTextBounds(str, 0, str.length(), rect);
        int i = this.mGravity;
        if (i == 3) {
            this.drawOutContentStart = 0;
            return;
        }
        if (i == 5) {
            this.drawOutContentStart = (this.measuredWidth - rect.width()) - 7;
            return;
        }
        if (i != 17) {
            return;
        }
        if (this.isOptions || this.label == null || this.label.equals("")) {
            double dWidth = this.measuredWidth - rect.width();
            Double.isNaN(dWidth);
            this.drawOutContentStart = (int) (dWidth * 0.5d);
        } else {
            double dWidth2 = this.measuredWidth - rect.width();
            Double.isNaN(dWidth2);
            this.drawOutContentStart = (int) (dWidth2 * 0.25d);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        this.widthMeasureSpec = i;
        remeasure();
        setMeasuredDimension(this.measuredWidth, this.measuredHeight);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean zOnTouchEvent = this.gestureDetector.onTouchEvent(motionEvent);
        int action = motionEvent.getAction();
        if (action == 0) {
            this.startTime = System.currentTimeMillis();
            cancelFuture();
            this.previousY = motionEvent.getRawY();
        } else if (action == 2) {
            float rawY = this.previousY - motionEvent.getRawY();
            this.previousY = motionEvent.getRawY();
            this.totalScrollY = (int) (this.totalScrollY + rawY);
            if (!this.isLoop) {
                float f = (-this.initPosition) * this.itemHeight;
                float itemsCount = ((this.adapter.getItemsCount() - 1) - this.initPosition) * this.itemHeight;
                double d = this.totalScrollY;
                double d2 = this.itemHeight;
                Double.isNaN(d2);
                Double.isNaN(d);
                if (d - (d2 * 0.3d) < f) {
                    f = this.totalScrollY - rawY;
                } else {
                    double d3 = this.totalScrollY;
                    double d4 = this.itemHeight;
                    Double.isNaN(d4);
                    Double.isNaN(d3);
                    if (d3 + (d4 * 0.3d) > itemsCount) {
                        itemsCount = this.totalScrollY - rawY;
                    }
                }
                if (this.totalScrollY < f) {
                    this.totalScrollY = (int) f;
                } else if (this.totalScrollY > itemsCount) {
                    this.totalScrollY = (int) itemsCount;
                }
            }
        } else if (!zOnTouchEvent) {
            double dAcos = Math.acos((this.radius - motionEvent.getY()) / this.radius);
            double d5 = this.radius;
            Double.isNaN(d5);
            double d6 = dAcos * d5;
            double d7 = this.itemHeight / 2.0f;
            Double.isNaN(d7);
            double d8 = d6 + d7;
            double d9 = this.itemHeight;
            Double.isNaN(d9);
            int i = (int) (d8 / d9);
            this.mOffset = (int) (((i - (this.itemsVisible / 2)) * this.itemHeight) - (((this.totalScrollY % this.itemHeight) + this.itemHeight) % this.itemHeight));
            if (System.currentTimeMillis() - this.startTime > 120) {
                smoothScroll(ACTION.DAGGLE);
            } else {
                smoothScroll(ACTION.CLICK);
            }
        }
        invalidate();
        return true;
    }

    public int getItemsCount() {
        if (this.adapter != null) {
            return this.adapter.getItemsCount();
        }
        return 0;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public void setGravity(int i) {
        this.mGravity = i;
    }

    public int getTextWidth(Paint paint, String str) {
        if (str == null || str.length() <= 0) {
            return 0;
        }
        int length = str.length();
        paint.getTextWidths(str, new float[length]);
        int iCeil = 0;
        for (int i = 0; i < length; i++) {
            iCeil += (int) Math.ceil(r2[i]);
        }
        return iCeil;
    }

    public void setIsOptions(boolean z) {
        this.isOptions = z;
    }

    public void setTextColorOut(int i) {
        if (i != 0) {
            this.textColorOut = i;
            this.paintOuterText.setColor(this.textColorOut);
        }
    }

    public void setTextColorCenter(int i) {
        if (i != 0) {
            this.textColorCenter = i;
            this.paintCenterText.setColor(this.textColorCenter);
        }
    }

    public void setDividerColor(int i) {
        if (i != 0) {
            this.dividerColor = i;
            this.paintIndicator.setColor(this.dividerColor);
        }
    }

    public void setDividerType(DividerType dividerType) {
        this.dividerType = dividerType;
    }

    public void setLineSpacingMultiplier(float f) {
        if (f != 0.0f) {
            this.lineSpacingMultiplier = f;
            judgeLineSpae();
        }
    }
}
