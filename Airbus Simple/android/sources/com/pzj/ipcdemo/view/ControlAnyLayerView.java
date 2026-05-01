package com.pzj.ipcdemo.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.pzj.ipcdemo.R;
import per.goweii.anylayer.AnimHelper;
import per.goweii.anylayer.AnyLayer;

/* loaded from: classes.dex */
public class ControlAnyLayerView {
    private Context context;
    private String deviceId;
    private ImageView iv_down;
    private ImageView iv_left;
    private ImageView iv_mic;
    private ImageView iv_right;
    private ImageView iv_up;
    private ImageView iv_voice;
    private RelativeLayout layout_dialog;
    private OnIpcControlListener onIpcControlListener;
    private AnyLayer anyLayer = null;
    private boolean isMute = false;

    public interface OnIpcControlListener {
        void onDownClickListener(String str, boolean z);

        void onLeftClickListener(String str, boolean z);

        void onMicClickListener(String str);

        void onRightClickListener(String str, boolean z);

        void onUpClickListener(String str, boolean z);

        void onVoiceClickListener(String str, boolean z);
    }

    public ControlAnyLayerView(Context context, String str) {
        this.deviceId = "";
        this.context = context;
        this.deviceId = str;
    }

    public void initView(AnyLayer anyLayer) {
        this.iv_up = (ImageView) anyLayer.getView(R.id.iv_up);
        this.iv_down = (ImageView) anyLayer.getView(R.id.iv_down);
        this.iv_left = (ImageView) anyLayer.getView(R.id.iv_left);
        this.iv_right = (ImageView) anyLayer.getView(R.id.iv_right);
        this.iv_voice = (ImageView) anyLayer.getView(R.id.iv_voice);
        this.iv_mic = (ImageView) anyLayer.getView(R.id.iv_mic);
        this.layout_dialog = (RelativeLayout) anyLayer.getView(R.id.layout_dialog);
    }

    public void show() {
        if (this.anyLayer != null) {
            if (this.anyLayer.isShow()) {
                return;
            }
            this.anyLayer.show();
            return;
        }
        this.anyLayer = AnyLayer.with(this.context).contentView(R.layout.dialog_control).gravity(49).cancelableOnTouchOutside(true).cancelableOnClickKeyBack(true).contentAnim(new AnyLayer.IAnim() { // from class: com.pzj.ipcdemo.view.ControlAnyLayerView.1
            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long inAnim(View view) {
                AnimHelper.startTopAlphaInAnim(view, 350L);
                return 350L;
            }

            @Override // per.goweii.anylayer.AnyLayer.IAnim
            public long outAnim(View view) {
                AnimHelper.startTopAlphaOutAnim(view, 350L);
                return 350L;
            }
        });
        initView(this.anyLayer);
        this.iv_up.setOnTouchListener(new View.OnTouchListener() { // from class: com.pzj.ipcdemo.view.ControlAnyLayerView.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    ControlAnyLayerView.this.onIpcControlListener.onUpClickListener(ControlAnyLayerView.this.deviceId, true);
                }
                if (action == 1) {
                    ControlAnyLayerView.this.onIpcControlListener.onUpClickListener(ControlAnyLayerView.this.deviceId, false);
                }
                return false;
            }
        });
        this.iv_down.setOnTouchListener(new View.OnTouchListener() { // from class: com.pzj.ipcdemo.view.ControlAnyLayerView.3
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    ControlAnyLayerView.this.onIpcControlListener.onDownClickListener(ControlAnyLayerView.this.deviceId, true);
                }
                if (action == 1) {
                    ControlAnyLayerView.this.onIpcControlListener.onDownClickListener(ControlAnyLayerView.this.deviceId, false);
                }
                return false;
            }
        });
        this.iv_left.setOnTouchListener(new View.OnTouchListener() { // from class: com.pzj.ipcdemo.view.ControlAnyLayerView.4
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    ControlAnyLayerView.this.onIpcControlListener.onLeftClickListener(ControlAnyLayerView.this.deviceId, true);
                }
                if (action == 1) {
                    ControlAnyLayerView.this.onIpcControlListener.onLeftClickListener(ControlAnyLayerView.this.deviceId, false);
                }
                return false;
            }
        });
        this.iv_right.setOnTouchListener(new View.OnTouchListener() { // from class: com.pzj.ipcdemo.view.ControlAnyLayerView.5
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == 0) {
                    ControlAnyLayerView.this.onIpcControlListener.onRightClickListener(ControlAnyLayerView.this.deviceId, true);
                }
                if (action == 1) {
                    ControlAnyLayerView.this.onIpcControlListener.onRightClickListener(ControlAnyLayerView.this.deviceId, false);
                }
                return false;
            }
        });
        this.iv_voice.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.view.ControlAnyLayerView.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ControlAnyLayerView.this.isMute) {
                    ControlAnyLayerView.this.iv_voice.setImageDrawable(ControlAnyLayerView.this.context.getResources().getDrawable(R.drawable.play_voice_bg_selector));
                    ControlAnyLayerView.this.onIpcControlListener.onVoiceClickListener(ControlAnyLayerView.this.deviceId, false);
                } else {
                    ControlAnyLayerView.this.iv_voice.setImageDrawable(ControlAnyLayerView.this.context.getResources().getDrawable(R.drawable.play_mute_bg_selector));
                    ControlAnyLayerView.this.onIpcControlListener.onVoiceClickListener(ControlAnyLayerView.this.deviceId, true);
                }
                ControlAnyLayerView.this.isMute = true ^ ControlAnyLayerView.this.isMute;
            }
        });
        this.layout_dialog.setOnClickListener(new View.OnClickListener() { // from class: com.pzj.ipcdemo.view.ControlAnyLayerView.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                try {
                    if (ControlAnyLayerView.this.anyLayer.isShow()) {
                        ControlAnyLayerView.this.anyLayer.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.anyLayer.show();
    }

    public void setOnControlListener(OnIpcControlListener onIpcControlListener) {
        this.onIpcControlListener = onIpcControlListener;
    }
}
