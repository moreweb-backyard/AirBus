package com.example.xch.scanzxing.zxing.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.example.xch.scanzxing.R;
import com.example.xch.scanzxing.zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 80;
    private static final int CURRENT_POINT_OPACITY = 160;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;
    private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    private final int SCAN_VELOCITY;
    private CameraManager cameraManager;
    private final int laserColor;
    private List<ResultPoint> lastPossibleResultPoints;
    private final int maskColor;
    private final Paint paint;
    private List<ResultPoint> possibleResultPoints;
    private Bitmap resultBitmap;
    private final int resultColor;
    private final int resultPointColor;
    Bitmap scanLight;
    private int scanLineTop;
    private int scannerAlpha;
    private final int statusColor;

    public ViewfinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.SCAN_VELOCITY = 10;
        this.paint = new Paint(1);
        Resources resources = getResources();
        this.maskColor = resources.getColor(R.color.viewfinder_mask);
        this.resultColor = resources.getColor(R.color.result_view);
        this.laserColor = resources.getColor(R.color.viewfinder_laser);
        this.resultPointColor = resources.getColor(R.color.possible_result_points);
        this.statusColor = resources.getColor(R.color.status_text);
        this.scannerAlpha = 0;
        this.possibleResultPoints = new ArrayList(5);
        this.lastPossibleResultPoints = null;
        this.scanLight = BitmapFactory.decodeResource(resources, R.drawable.scan_light);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override // android.view.View
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) throws Resources.NotFoundException {
        if (this.cameraManager == null) {
            return;
        }
        Rect framingRect = this.cameraManager.getFramingRect();
        Rect framingRectInPreview = this.cameraManager.getFramingRectInPreview();
        if (framingRect == null || framingRectInPreview == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
        float f = width;
        canvas.drawRect(0.0f, 0.0f, f, framingRect.top, this.paint);
        canvas.drawRect(0.0f, framingRect.top, framingRect.left, framingRect.bottom + 1, this.paint);
        canvas.drawRect(framingRect.right + 1, framingRect.top, f, framingRect.bottom + 1, this.paint);
        canvas.drawRect(0.0f, framingRect.bottom + 1, f, height, this.paint);
        if (this.resultBitmap != null) {
            this.paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(this.resultBitmap, (Rect) null, framingRect, this.paint);
            return;
        }
        drawFrameBounds(canvas, framingRect);
        drawStatusText(canvas, framingRect, width);
        drawScanLight(canvas, framingRect);
        float fWidth = framingRect.width() / framingRectInPreview.width();
        float fHeight = framingRect.height() / framingRectInPreview.height();
        List<ResultPoint> list = this.possibleResultPoints;
        List<ResultPoint> list2 = this.lastPossibleResultPoints;
        int i = framingRect.left;
        int i2 = framingRect.top;
        if (list.isEmpty()) {
            this.lastPossibleResultPoints = null;
        } else {
            this.possibleResultPoints = new ArrayList(5);
            this.lastPossibleResultPoints = list;
            this.paint.setAlpha(CURRENT_POINT_OPACITY);
            this.paint.setColor(this.resultPointColor);
            synchronized (list) {
                for (ResultPoint resultPoint : list) {
                    canvas.drawCircle(((int) (resultPoint.getX() * fWidth)) + i, ((int) (resultPoint.getY() * fHeight)) + i2, 6.0f, this.paint);
                }
            }
        }
        if (list2 != null) {
            this.paint.setAlpha(80);
            this.paint.setColor(this.resultPointColor);
            synchronized (list2) {
                for (ResultPoint resultPoint2 : list2) {
                    canvas.drawCircle(((int) (resultPoint2.getX() * fWidth)) + i, ((int) (resultPoint2.getY() * fHeight)) + i2, 3.0f, this.paint);
                }
            }
        }
        postInvalidateDelayed(ANIMATION_DELAY, framingRect.left - 6, framingRect.top - 6, framingRect.right + 6, framingRect.bottom + 6);
    }

    private void drawFrameBounds(Canvas canvas, Rect rect) {
        this.paint.setColor(-1);
        this.paint.setStrokeWidth(2.0f);
        this.paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, this.paint);
        this.paint.setColor(-16776961);
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect.left - 15, rect.top, rect.left, rect.top + 45, this.paint);
        canvas.drawRect(rect.left - 15, rect.top - 15, rect.left + 45, rect.top, this.paint);
        canvas.drawRect(rect.right, rect.top, rect.right + 15, rect.top + 45, this.paint);
        canvas.drawRect(rect.right - 45, rect.top - 15, rect.right + 15, rect.top, this.paint);
        canvas.drawRect(rect.left - 15, rect.bottom - 45, rect.left, rect.bottom, this.paint);
        canvas.drawRect(rect.left - 15, rect.bottom, rect.left + 45, rect.bottom + 15, this.paint);
        canvas.drawRect(rect.right, rect.bottom - 45, rect.right + 15, rect.bottom, this.paint);
        canvas.drawRect(rect.right - 45, rect.bottom, rect.right + 15, rect.bottom + 15, this.paint);
    }

    private void drawStatusText(Canvas canvas, Rect rect, int i) throws Resources.NotFoundException {
        String string = getResources().getString(R.string.viewfinderview_status_text1);
        String string2 = getResources().getString(R.string.viewfinderview_status_text2);
        this.paint.setColor(this.statusColor);
        this.paint.setTextSize(45);
        canvas.drawText(string, (i - ((int) this.paint.measureText(string))) / 2, rect.top - 180, this.paint);
        canvas.drawText(string2, (i - ((int) this.paint.measureText(string2))) / 2, (rect.top - 180) + 60, this.paint);
    }

    private void drawScanLight(Canvas canvas, Rect rect) {
        if (this.scanLineTop == 0) {
            this.scanLineTop = rect.top;
        }
        if (this.scanLineTop >= rect.bottom) {
            this.scanLineTop = rect.top;
        } else {
            this.scanLineTop += 10;
        }
        canvas.drawBitmap(this.scanLight, (Rect) null, new Rect(rect.left, this.scanLineTop, rect.right, this.scanLineTop + 30), this.paint);
    }

    public void drawViewfinder() {
        Bitmap bitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (bitmap != null) {
            bitmap.recycle();
        }
        invalidate();
    }

    public void drawResultBitmap(Bitmap bitmap) {
        this.resultBitmap = bitmap;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint resultPoint) {
        List<ResultPoint> list = this.possibleResultPoints;
        synchronized (list) {
            list.add(resultPoint);
            int size = list.size();
            if (size > 20) {
                list.subList(0, size - 10).clear();
            }
        }
    }
}
