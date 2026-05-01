package com.shima.smartbushome.assist.AtoZlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.shima.smartbushome.R;

/* loaded from: classes.dex */
public class SideBar extends View {
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private int choose;
    private TextView mTextDialog;
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private Paint paint;

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String str);
    }

    public void setTextView(TextView textView) {
        this.mTextDialog = textView;
    }

    public SideBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.choose = -1;
        this.paint = new Paint();
    }

    public SideBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.choose = -1;
        this.paint = new Paint();
    }

    public SideBar(Context context) {
        super(context);
        this.choose = -1;
        this.paint = new Paint();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int length = height / b.length;
        for (int i = 0; i < b.length; i++) {
            this.paint.setColor(Color.parseColor("#ffffff"));
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
            this.paint.setAntiAlias(true);
            this.paint.setTextSize(35.0f);
            if (i == this.choose) {
                this.paint.setColor(Color.parseColor("#02A3E9"));
                this.paint.setFakeBoldText(true);
            }
            canvas.drawText(b[i], (width / 2) - (this.paint.measureText(b[i]) / 2.0f), (length * i) + length, this.paint);
            this.paint.reset();
        }
    }

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float y = motionEvent.getY();
        int i = this.choose;
        OnTouchingLetterChangedListener onTouchingLetterChangedListener = this.onTouchingLetterChangedListener;
        int height = (int) ((y / getHeight()) * b.length);
        if (action == 1) {
            setBackgroundDrawable(new ColorDrawable(0));
            this.choose = -1;
            invalidate();
            if (this.mTextDialog != null) {
                this.mTextDialog.setVisibility(4);
            }
        } else {
            setBackgroundResource(R.drawable.sidebar_background);
            if (i != height && height >= 0 && height < b.length) {
                if (onTouchingLetterChangedListener != null) {
                    onTouchingLetterChangedListener.onTouchingLetterChanged(b[height]);
                }
                if (this.mTextDialog != null) {
                    this.mTextDialog.setText(b[height]);
                    this.mTextDialog.setVisibility(0);
                }
                this.choose = height;
                invalidate();
            }
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }
}
