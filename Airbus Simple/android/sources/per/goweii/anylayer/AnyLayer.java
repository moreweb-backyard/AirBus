package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import java.util.Objects;
import per.goweii.anylayer.LayerManager;
import per.goweii.burred.Blurred;

/* loaded from: classes.dex */
public class AnyLayer implements LayerManager.LifeListener {
    private final FrameLayout mActivityContentView;
    private Alignment.Direction mAlignmentDirection;
    private Alignment.Horizontal mAlignmentHorizontal;
    private boolean mAlignmentInside;
    private Alignment.Vertical mAlignmentVertical;

    @IdRes
    private int mAsStatusBarViewId;
    private IAnim mBackgroundAnim;
    private Bitmap mBackgroundBitmap;
    private float mBackgroundBlurPercent;
    private float mBackgroundBlurRadius;
    private float mBackgroundBlurScale;
    private int mBackgroundColor;
    private Drawable mBackgroundDrawable;
    private Animation mBackgroundInAnim;
    private long mBackgroundInAnimDuration;
    private Animation mBackgroundOutAnim;
    private long mBackgroundOutAnimDuration;
    private int mBackgroundResource;
    private boolean mCancelableOnTouchOutside;
    private IAnim mContentAnim;
    private Animation mContentInAnim;
    private long mContentInAnimDuration;
    private Animation mContentOutAnim;
    private long mContentOutAnimDuration;
    private final Context mContext;
    private IDataBinder mDataBinder;
    private int mGravity;
    private final LayoutInflater mInflater;
    private LayerManager mLayerManager;
    private OnLayerDismissListener mOnLayerDismissListener;
    private OnLayerShowListener mOnLayerShowListener;
    private OnVisibleChangeListener mOnVisibleChangeListener;
    private final ViewGroup mRootView;
    private SoftInputHelper mSoftInputHelper;
    private final View mTargetView;
    private ViewHolder mViewHolder;

    public static class Alignment {

        public enum Direction {
            HORIZONTAL,
            VERTICAL
        }

        public enum Horizontal {
            CENTER,
            TO_LEFT,
            TO_RIGHT,
            ALIGN_LEFT,
            ALIGN_RIGHT
        }

        public enum Vertical {
            CENTER,
            ABOVE,
            BELOW,
            ALIGN_TOP,
            ALIGN_BOTTOM
        }
    }

    public interface IAnim {
        long inAnim(View view);

        long outAnim(View view);
    }

    public interface IDataBinder {
        void bind(AnyLayer anyLayer);
    }

    public interface OnLayerClickListener {
        void onClick(AnyLayer anyLayer, View view);
    }

    public interface OnLayerDismissListener {
        void onDismissed(AnyLayer anyLayer);

        void onDismissing(AnyLayer anyLayer);
    }

    public interface OnLayerShowListener {
        void onShowing(AnyLayer anyLayer);

        void onShown(AnyLayer anyLayer);
    }

    public interface OnVisibleChangeListener {
        void onDismiss(AnyLayer anyLayer);

        void onShow(AnyLayer anyLayer);
    }

    public static AnyLayer with(@NonNull ViewGroup viewGroup) {
        return new AnyLayer(viewGroup);
    }

    public static AnyLayer with(@NonNull Context context) {
        return new AnyLayer(context);
    }

    public static AnyLayer target(@NonNull View view) {
        return new AnyLayer(view);
    }

    public AnyLayer contentView(@NonNull View view) {
        this.mViewHolder.setContent(view);
        return this;
    }

    public AnyLayer contentView(@LayoutRes int i) {
        return contentView(this.mInflater.inflate(i, (ViewGroup) this.mViewHolder.getContainer(), false));
    }

    public AnyLayer asStatusBar(@IdRes int i) {
        this.mAsStatusBarViewId = i;
        return this;
    }

    public AnyLayer bindData(IDataBinder iDataBinder) {
        this.mDataBinder = iDataBinder;
        return this;
    }

    public AnyLayer onVisibleChangeListener(OnVisibleChangeListener onVisibleChangeListener) {
        this.mOnVisibleChangeListener = onVisibleChangeListener;
        return this;
    }

    public AnyLayer onLayerShowListener(OnLayerShowListener onLayerShowListener) {
        this.mOnLayerShowListener = onLayerShowListener;
        return this;
    }

    public AnyLayer onLayerDismissListener(OnLayerDismissListener onLayerDismissListener) {
        this.mOnLayerDismissListener = onLayerDismissListener;
        return this;
    }

    public AnyLayer gravity(int i) {
        this.mGravity = i;
        return this;
    }

    public AnyLayer alignment(@NonNull Alignment.Direction direction, @NonNull Alignment.Horizontal horizontal, @NonNull Alignment.Vertical vertical, boolean z) {
        this.mAlignmentDirection = direction;
        this.mAlignmentHorizontal = horizontal;
        this.mAlignmentVertical = vertical;
        this.mAlignmentInside = z;
        return this;
    }

    public AnyLayer contentAnim(IAnim iAnim) {
        this.mContentAnim = iAnim;
        return this;
    }

    public AnyLayer contentInAnim(@AnimRes int i) {
        contentInAnim(AnimationUtils.loadAnimation(this.mContext, i));
        return this;
    }

    public AnyLayer contentInAnim(@NonNull Animation animation) {
        this.mContentInAnim = animation;
        this.mContentInAnimDuration = this.mContentInAnim.getDuration();
        return this;
    }

    public AnyLayer contentOutAnim(@AnimRes int i) {
        contentOutAnim(AnimationUtils.loadAnimation(this.mContext, i));
        return this;
    }

    public AnyLayer contentOutAnim(@NonNull Animation animation) {
        this.mContentOutAnim = animation;
        this.mContentOutAnimDuration = this.mContentOutAnim.getDuration();
        return this;
    }

    public AnyLayer backgroundAnim(IAnim iAnim) {
        this.mBackgroundAnim = iAnim;
        return this;
    }

    public AnyLayer backgroundInAnim(@AnimRes int i) {
        backgroundInAnim(AnimationUtils.loadAnimation(this.mContext, i));
        return this;
    }

    public AnyLayer backgroundInAnim(@NonNull Animation animation) {
        this.mBackgroundInAnim = animation;
        this.mBackgroundInAnimDuration = this.mBackgroundInAnim.getDuration();
        return this;
    }

    public AnyLayer backgroundOutAnim(@AnimRes int i) {
        backgroundOutAnim(AnimationUtils.loadAnimation(this.mContext, i));
        return this;
    }

    public AnyLayer backgroundOutAnim(@NonNull Animation animation) {
        this.mBackgroundOutAnim = animation;
        this.mBackgroundOutAnimDuration = this.mBackgroundOutAnim.getDuration();
        return this;
    }

    public AnyLayer defaultContentAnimDuration(long j) {
        this.mContentInAnimDuration = j;
        this.mContentOutAnimDuration = j;
        return this;
    }

    public AnyLayer defaultContentInAnimDuration(long j) {
        this.mContentInAnimDuration = j;
        return this;
    }

    public AnyLayer defaultContentOutAnimDuration(long j) {
        this.mContentOutAnimDuration = j;
        return this;
    }

    public AnyLayer defaultBackgroundAnimDuration(long j) {
        this.mBackgroundInAnimDuration = j;
        this.mBackgroundOutAnimDuration = j;
        return this;
    }

    public AnyLayer defaultBackgroundInAnimDuration(long j) {
        this.mBackgroundInAnimDuration = j;
        return this;
    }

    public AnyLayer defaultBackgroundOutAnimDuration(long j) {
        this.mBackgroundOutAnimDuration = j;
        return this;
    }

    public AnyLayer backgroundBlurRadius(@FloatRange(from = 0.0d, fromInclusive = false, to = 25.0d) float f) {
        this.mBackgroundBlurRadius = f;
        return this;
    }

    public AnyLayer backgroundBlurPercent(@FloatRange(from = 0.0d, fromInclusive = false) float f) {
        this.mBackgroundBlurPercent = f;
        return this;
    }

    public AnyLayer backgroundBlurScale(@FloatRange(from = 1.0d) float f) {
        this.mBackgroundBlurScale = f;
        return this;
    }

    public AnyLayer backgroundBitmap(@NonNull Bitmap bitmap) {
        this.mBackgroundBitmap = bitmap;
        return this;
    }

    public AnyLayer backgroundResource(@DrawableRes int i) {
        this.mBackgroundResource = i;
        return this;
    }

    public AnyLayer backgroundDrawable(@NonNull Drawable drawable) {
        this.mBackgroundDrawable = drawable;
        return this;
    }

    public AnyLayer backgroundColorInt(@ColorInt int i) {
        this.mBackgroundColor = i;
        return this;
    }

    public AnyLayer backgroundColorRes(@ColorRes int i) {
        this.mBackgroundColor = ContextCompat.getColor(this.mContext, i);
        return this;
    }

    public AnyLayer cancelableOnTouchOutside(boolean z) {
        this.mCancelableOnTouchOutside = z;
        return this;
    }

    public AnyLayer cancelableOnClickKeyBack(boolean z) {
        this.mLayerManager.setCancelableOnClickKeyBack(z);
        return this;
    }

    public AnyLayer onClick(@IdRes int i, OnLayerClickListener onLayerClickListener) {
        this.mViewHolder.addOnClickListener(onLayerClickListener, i, null);
        return this;
    }

    public AnyLayer onClick(OnLayerClickListener onLayerClickListener, @IdRes int i, @IdRes int... iArr) {
        this.mViewHolder.addOnClickListener(onLayerClickListener, i, iArr);
        return this;
    }

    public AnyLayer onClickToDismiss(@IdRes int i, @IdRes int... iArr) {
        this.mViewHolder.addOnClickListener(new OnLayerClickListener() { // from class: per.goweii.anylayer.AnyLayer.1
            @Override // per.goweii.anylayer.AnyLayer.OnLayerClickListener
            public void onClick(AnyLayer anyLayer, View view) {
                AnyLayer.this.dismiss();
            }
        }, i, iArr);
        return this;
    }

    public void show() {
        this.mLayerManager.add();
    }

    public void dismiss() {
        this.mLayerManager.remove();
    }

    public <V extends View> V getView(@IdRes int i) {
        return (V) this.mViewHolder.getView(i);
    }

    public ViewHolder getViewHolder() {
        return this.mViewHolder;
    }

    public View getContentView() {
        return this.mViewHolder.getContent();
    }

    public ImageView getBackground() {
        return this.mViewHolder.getBackground();
    }

    public boolean isShow() {
        return this.mViewHolder.getContainer().getParent() != null;
    }

    public void compatSoftInput(EditText... editTextArr) {
        Activity activity = getActivity();
        if (activity != null) {
            SoftInputHelper.attach(activity).init(this.mViewHolder.getContentWrapper(), this.mViewHolder.getContent(), editTextArr).moveWithTranslation().duration(300L);
        }
    }

    public void removeSoftInput() {
        if (this.mSoftInputHelper != null) {
            this.mSoftInputHelper.detach();
        }
    }

    private AnyLayer(@NonNull ViewGroup viewGroup) {
        this.mAsStatusBarViewId = 0;
        this.mGravity = 17;
        this.mBackgroundBlurPercent = 0.0f;
        this.mBackgroundBlurRadius = 0.0f;
        this.mBackgroundBlurScale = 2.0f;
        this.mBackgroundBitmap = null;
        this.mBackgroundResource = -1;
        this.mBackgroundDrawable = null;
        this.mBackgroundColor = 0;
        this.mCancelableOnTouchOutside = true;
        this.mBackgroundAnim = null;
        this.mBackgroundInAnim = null;
        this.mBackgroundOutAnim = null;
        this.mContentAnim = null;
        this.mContentInAnim = null;
        this.mContentOutAnim = null;
        this.mBackgroundInAnimDuration = 300L;
        this.mBackgroundOutAnimDuration = 300L;
        this.mContentInAnimDuration = 300L;
        this.mContentOutAnimDuration = 300L;
        this.mDataBinder = null;
        this.mOnVisibleChangeListener = null;
        this.mOnLayerShowListener = null;
        this.mOnLayerDismissListener = null;
        this.mAlignmentInside = false;
        this.mAlignmentDirection = Alignment.Direction.VERTICAL;
        this.mAlignmentHorizontal = Alignment.Horizontal.CENTER;
        this.mAlignmentVertical = Alignment.Vertical.BELOW;
        this.mSoftInputHelper = null;
        this.mContext = viewGroup.getContext();
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mTargetView = null;
        this.mRootView = viewGroup;
        this.mActivityContentView = null;
        initView();
    }

    private AnyLayer(@NonNull Context context) {
        this.mAsStatusBarViewId = 0;
        this.mGravity = 17;
        this.mBackgroundBlurPercent = 0.0f;
        this.mBackgroundBlurRadius = 0.0f;
        this.mBackgroundBlurScale = 2.0f;
        this.mBackgroundBitmap = null;
        this.mBackgroundResource = -1;
        this.mBackgroundDrawable = null;
        this.mBackgroundColor = 0;
        this.mCancelableOnTouchOutside = true;
        this.mBackgroundAnim = null;
        this.mBackgroundInAnim = null;
        this.mBackgroundOutAnim = null;
        this.mContentAnim = null;
        this.mContentInAnim = null;
        this.mContentOutAnim = null;
        this.mBackgroundInAnimDuration = 300L;
        this.mBackgroundOutAnimDuration = 300L;
        this.mContentInAnimDuration = 300L;
        this.mContentOutAnimDuration = 300L;
        this.mDataBinder = null;
        this.mOnVisibleChangeListener = null;
        this.mOnLayerShowListener = null;
        this.mOnLayerDismissListener = null;
        this.mAlignmentInside = false;
        this.mAlignmentDirection = Alignment.Direction.VERTICAL;
        this.mAlignmentHorizontal = Alignment.Horizontal.CENTER;
        this.mAlignmentVertical = Alignment.Vertical.BELOW;
        this.mSoftInputHelper = null;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mTargetView = null;
        this.mRootView = (FrameLayout) ((Activity) Objects.requireNonNull(Utils.getActivity(this.mContext))).getWindow().getDecorView();
        this.mActivityContentView = (FrameLayout) this.mRootView.findViewById(android.R.id.content);
        initView();
    }

    private AnyLayer(@NonNull View view) {
        this.mAsStatusBarViewId = 0;
        this.mGravity = 17;
        this.mBackgroundBlurPercent = 0.0f;
        this.mBackgroundBlurRadius = 0.0f;
        this.mBackgroundBlurScale = 2.0f;
        this.mBackgroundBitmap = null;
        this.mBackgroundResource = -1;
        this.mBackgroundDrawable = null;
        this.mBackgroundColor = 0;
        this.mCancelableOnTouchOutside = true;
        this.mBackgroundAnim = null;
        this.mBackgroundInAnim = null;
        this.mBackgroundOutAnim = null;
        this.mContentAnim = null;
        this.mContentInAnim = null;
        this.mContentOutAnim = null;
        this.mBackgroundInAnimDuration = 300L;
        this.mBackgroundOutAnimDuration = 300L;
        this.mContentInAnimDuration = 300L;
        this.mContentOutAnimDuration = 300L;
        this.mDataBinder = null;
        this.mOnVisibleChangeListener = null;
        this.mOnLayerShowListener = null;
        this.mOnLayerDismissListener = null;
        this.mAlignmentInside = false;
        this.mAlignmentDirection = Alignment.Direction.VERTICAL;
        this.mAlignmentHorizontal = Alignment.Horizontal.CENTER;
        this.mAlignmentVertical = Alignment.Vertical.BELOW;
        this.mSoftInputHelper = null;
        this.mContext = view.getContext();
        this.mInflater = LayoutInflater.from(this.mContext);
        this.mTargetView = view;
        this.mRootView = (FrameLayout) ((Activity) Objects.requireNonNull(Utils.getActivity(this.mContext))).getWindow().getDecorView();
        this.mActivityContentView = (FrameLayout) this.mRootView.findViewById(android.R.id.content);
        initView();
    }

    private void initView() {
        Blurred.init(this.mContext);
        FrameLayout frameLayout = (FrameLayout) this.mInflater.inflate(R.layout.layout_any_layer, this.mRootView, false);
        this.mViewHolder = new ViewHolder(this, frameLayout);
        this.mLayerManager = new LayerManager(this.mRootView, frameLayout);
        this.mLayerManager.setLifeListener(this);
    }

    @Nullable
    private Activity getActivity() {
        if (this.mContext instanceof Activity) {
            return (Activity) this.mContext;
        }
        if (!(this.mContext instanceof ContextWrapper)) {
            return null;
        }
        Context baseContext = ((ContextWrapper) this.mContext).getBaseContext();
        if (baseContext instanceof Activity) {
            return (Activity) baseContext;
        }
        return null;
    }

    private void startContentInAnim() {
        if (this.mContentAnim != null) {
            this.mContentInAnimDuration = this.mContentAnim.inAnim(this.mViewHolder.getContent());
        } else if (this.mContentInAnim != null) {
            this.mViewHolder.getContent().startAnimation(this.mContentInAnim);
        } else {
            AnimHelper.startZoomInAnim(this.mViewHolder.getContent(), this.mContentInAnimDuration);
        }
    }

    private void startContentOutAnim() {
        if (this.mContentAnim != null) {
            this.mContentOutAnimDuration = this.mContentAnim.outAnim(this.mViewHolder.getContent());
        } else if (this.mContentOutAnim != null) {
            this.mViewHolder.getContent().startAnimation(this.mContentOutAnim);
        } else {
            AnimHelper.startZoomOutAnim(this.mViewHolder.getContent(), this.mContentOutAnimDuration);
        }
    }

    private void startBackgroundInAnim() {
        if (this.mBackgroundAnim != null) {
            this.mBackgroundInAnimDuration = this.mBackgroundAnim.inAnim(this.mViewHolder.getBackground());
        } else if (this.mBackgroundInAnim != null) {
            this.mViewHolder.getBackground().startAnimation(this.mBackgroundInAnim);
        } else {
            AnimHelper.startAlphaInAnim(this.mViewHolder.getBackground(), this.mBackgroundInAnimDuration);
        }
    }

    private void startBackgroundOutAnim() {
        if (this.mBackgroundAnim != null) {
            this.mBackgroundOutAnimDuration = this.mBackgroundAnim.outAnim(this.mViewHolder.getBackground());
        } else if (this.mBackgroundOutAnim != null) {
            this.mViewHolder.getBackground().startAnimation(this.mBackgroundOutAnim);
        } else {
            AnimHelper.startAlphaOutAnim(this.mViewHolder.getBackground(), this.mBackgroundOutAnimDuration);
        }
    }

    @Override // per.goweii.anylayer.LayerManager.LifeListener
    public void onAttach() {
        initContainer();
        initBackground();
        initContent();
        this.mViewHolder.bindListener();
        if (this.mOnVisibleChangeListener != null) {
            this.mOnVisibleChangeListener.onShow(this);
        }
        if (this.mDataBinder != null) {
            this.mDataBinder.bind(this);
        }
        if (this.mOnLayerShowListener != null) {
            this.mOnLayerShowListener.onShowing(this);
        }
    }

    @Override // per.goweii.anylayer.LayerManager.LifeListener
    public long onAnimIn(View view) {
        startContentInAnim();
        startBackgroundInAnim();
        return Math.max(this.mBackgroundInAnimDuration, this.mContentInAnimDuration);
    }

    @Override // per.goweii.anylayer.LayerManager.LifeListener
    public long onAnimOut(View view) {
        startContentOutAnim();
        startBackgroundOutAnim();
        return Math.max(this.mBackgroundOutAnimDuration, this.mContentOutAnimDuration);
    }

    @Override // per.goweii.anylayer.LayerManager.LifeListener
    public void onShow() {
        if (this.mOnLayerShowListener != null) {
            this.mOnLayerShowListener.onShown(this);
        }
    }

    @Override // per.goweii.anylayer.LayerManager.LifeListener
    public void onRemove() {
        if (this.mOnLayerDismissListener != null) {
            this.mOnLayerDismissListener.onDismissing(this);
        }
    }

    @Override // per.goweii.anylayer.LayerManager.LifeListener
    public void onDetach() {
        if (this.mOnVisibleChangeListener != null) {
            this.mOnVisibleChangeListener.onDismiss(this);
        }
        if (this.mOnLayerDismissListener != null) {
            this.mOnLayerDismissListener.onDismissed(this);
        }
        this.mViewHolder.recycle();
    }

    private int getStatusBarHeight() {
        int identifier = this.mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return this.mContext.getResources().getDimensionPixelSize(identifier);
        }
        return 0;
    }

    private void initContainer() {
        int i;
        final int i2;
        final int i3;
        int i4;
        if (this.mCancelableOnTouchOutside) {
            this.mViewHolder.getContainer().setOnClickListener(new View.OnClickListener() { // from class: per.goweii.anylayer.AnyLayer.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AnyLayer.this.dismiss();
                }
            });
        }
        int i5 = 0;
        if (this.mActivityContentView != null) {
            int[] iArr = new int[2];
            this.mRootView.getLocationOnScreen(iArr);
            int[] iArr2 = new int[2];
            this.mActivityContentView.getLocationOnScreen(iArr2);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mViewHolder.getContainer().getLayoutParams();
            layoutParams.leftMargin = iArr2[0] - iArr[0];
            layoutParams.topMargin = 0;
            layoutParams.width = this.mActivityContentView.getWidth();
            layoutParams.height = this.mActivityContentView.getHeight() + (iArr2[1] - iArr[1]);
            this.mViewHolder.getContainer().setLayoutParams(layoutParams);
        }
        if (this.mTargetView == null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mViewHolder.getContentWrapper().getLayoutParams();
            layoutParams2.width = -1;
            layoutParams2.height = -1;
            this.mViewHolder.getContentWrapper().setLayoutParams(layoutParams2);
            return;
        }
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mViewHolder.getContentWrapper().getLayoutParams();
        layoutParams3.width = -2;
        layoutParams3.height = -2;
        this.mViewHolder.getContentWrapper().setLayoutParams(layoutParams3);
        int[] iArr3 = new int[2];
        this.mTargetView.getLocationOnScreen(iArr3);
        int[] iArr4 = new int[2];
        this.mRootView.getLocationOnScreen(iArr4);
        final int i6 = iArr3[0] - iArr4[0];
        final int i7 = iArr3[1] - iArr4[1];
        final int width = this.mTargetView.getWidth();
        final int height = this.mTargetView.getHeight();
        FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) this.mViewHolder.getContainer().getLayoutParams();
        if (this.mAlignmentDirection == Alignment.Direction.HORIZONTAL) {
            if (this.mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                i4 = layoutParams4.width - i6;
            } else {
                if (this.mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                    i3 = i6 + width;
                } else if (this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                    i3 = i6;
                } else if (this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                    i4 = (layoutParams4.width - i6) - width;
                }
                i = 0;
                i2 = 0;
                this.mViewHolder.getContainer().setPadding(i3, i2, i5, i);
                this.mViewHolder.getContainer().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: per.goweii.anylayer.AnyLayer.3
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public boolean onPreDraw() {
                        int i8;
                        int i9;
                        if (AnyLayer.this.mViewHolder.getContainer().getViewTreeObserver().isAlive()) {
                            AnyLayer.this.mViewHolder.getContainer().getViewTreeObserver().removeOnPreDrawListener(this);
                        }
                        int width2 = AnyLayer.this.mViewHolder.getContentWrapper().getWidth();
                        int height2 = AnyLayer.this.mViewHolder.getContentWrapper().getHeight();
                        if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.CENTER) {
                            if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.TO_LEFT) {
                                if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.TO_RIGHT) {
                                    if (AnyLayer.this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                                        i8 = i6;
                                    } else {
                                        i8 = AnyLayer.this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT ? i6 - (width2 - width) : 0;
                                    }
                                } else {
                                    i8 = i6 + width;
                                }
                            } else {
                                i8 = i6 - width2;
                            }
                        } else {
                            i8 = i6 - ((width2 - width) / 2);
                        }
                        if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.CENTER) {
                            if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.ABOVE) {
                                if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.BELOW) {
                                    if (AnyLayer.this.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                                        i9 = i7;
                                    } else {
                                        i9 = AnyLayer.this.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM ? i7 - (height2 - height) : 0;
                                    }
                                } else {
                                    i9 = i7 + height;
                                }
                            } else {
                                i9 = i7 - height2;
                            }
                        } else {
                            i9 = i7 - ((height2 - height) / 2);
                        }
                        int i10 = i8 - i3;
                        int i11 = i9 - i2;
                        if (AnyLayer.this.mAlignmentInside) {
                            int width3 = ((AnyLayer.this.mViewHolder.getContainer().getWidth() - AnyLayer.this.mViewHolder.getContainer().getPaddingLeft()) - AnyLayer.this.mViewHolder.getContainer().getPaddingRight()) - width2;
                            int height3 = ((AnyLayer.this.mViewHolder.getContainer().getHeight() - AnyLayer.this.mViewHolder.getContainer().getPaddingTop()) - AnyLayer.this.mViewHolder.getContainer().getPaddingBottom()) - height2;
                            if (i10 < 0) {
                                i10 = 0;
                            } else if (i10 > width3) {
                                i10 = width3;
                            }
                            if (i11 < 0) {
                                i11 = 0;
                            } else if (i11 > height3) {
                                i11 = height3;
                            }
                        }
                        FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) AnyLayer.this.mViewHolder.getContentWrapper().getLayoutParams();
                        layoutParams5.leftMargin = i10;
                        layoutParams5.topMargin = i11;
                        AnyLayer.this.mViewHolder.getContentWrapper().setLayoutParams(layoutParams5);
                        return false;
                    }
                });
            }
            i5 = i4;
        } else {
            if (this.mAlignmentDirection == Alignment.Direction.VERTICAL) {
                if (this.mAlignmentVertical == Alignment.Vertical.ABOVE) {
                    i = layoutParams4.height - i7;
                } else {
                    if (this.mAlignmentVertical == Alignment.Vertical.BELOW) {
                        i2 = i7 + height;
                    } else if (this.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                        i2 = i7;
                    } else if (this.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                        i = (layoutParams4.height - i7) - height;
                    }
                    i = 0;
                    i3 = 0;
                }
                i3 = 0;
                i2 = 0;
            }
            this.mViewHolder.getContainer().setPadding(i3, i2, i5, i);
            this.mViewHolder.getContainer().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: per.goweii.anylayer.AnyLayer.3
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    int i8;
                    int i9;
                    if (AnyLayer.this.mViewHolder.getContainer().getViewTreeObserver().isAlive()) {
                        AnyLayer.this.mViewHolder.getContainer().getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    int width2 = AnyLayer.this.mViewHolder.getContentWrapper().getWidth();
                    int height2 = AnyLayer.this.mViewHolder.getContentWrapper().getHeight();
                    if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.CENTER) {
                        if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.TO_LEFT) {
                            if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.TO_RIGHT) {
                                if (AnyLayer.this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                                    i8 = i6;
                                } else {
                                    i8 = AnyLayer.this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT ? i6 - (width2 - width) : 0;
                                }
                            } else {
                                i8 = i6 + width;
                            }
                        } else {
                            i8 = i6 - width2;
                        }
                    } else {
                        i8 = i6 - ((width2 - width) / 2);
                    }
                    if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.CENTER) {
                        if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.ABOVE) {
                            if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.BELOW) {
                                if (AnyLayer.this.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                                    i9 = i7;
                                } else {
                                    i9 = AnyLayer.this.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM ? i7 - (height2 - height) : 0;
                                }
                            } else {
                                i9 = i7 + height;
                            }
                        } else {
                            i9 = i7 - height2;
                        }
                    } else {
                        i9 = i7 - ((height2 - height) / 2);
                    }
                    int i10 = i8 - i3;
                    int i11 = i9 - i2;
                    if (AnyLayer.this.mAlignmentInside) {
                        int width3 = ((AnyLayer.this.mViewHolder.getContainer().getWidth() - AnyLayer.this.mViewHolder.getContainer().getPaddingLeft()) - AnyLayer.this.mViewHolder.getContainer().getPaddingRight()) - width2;
                        int height3 = ((AnyLayer.this.mViewHolder.getContainer().getHeight() - AnyLayer.this.mViewHolder.getContainer().getPaddingTop()) - AnyLayer.this.mViewHolder.getContainer().getPaddingBottom()) - height2;
                        if (i10 < 0) {
                            i10 = 0;
                        } else if (i10 > width3) {
                            i10 = width3;
                        }
                        if (i11 < 0) {
                            i11 = 0;
                        } else if (i11 > height3) {
                            i11 = height3;
                        }
                    }
                    FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) AnyLayer.this.mViewHolder.getContentWrapper().getLayoutParams();
                    layoutParams5.leftMargin = i10;
                    layoutParams5.topMargin = i11;
                    AnyLayer.this.mViewHolder.getContentWrapper().setLayoutParams(layoutParams5);
                    return false;
                }
            });
        }
        i = 0;
        i3 = 0;
        i2 = 0;
        this.mViewHolder.getContainer().setPadding(i3, i2, i5, i);
        this.mViewHolder.getContainer().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: per.goweii.anylayer.AnyLayer.3
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                int i8;
                int i9;
                if (AnyLayer.this.mViewHolder.getContainer().getViewTreeObserver().isAlive()) {
                    AnyLayer.this.mViewHolder.getContainer().getViewTreeObserver().removeOnPreDrawListener(this);
                }
                int width2 = AnyLayer.this.mViewHolder.getContentWrapper().getWidth();
                int height2 = AnyLayer.this.mViewHolder.getContentWrapper().getHeight();
                if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.CENTER) {
                    if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.TO_LEFT) {
                        if (AnyLayer.this.mAlignmentHorizontal != Alignment.Horizontal.TO_RIGHT) {
                            if (AnyLayer.this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                                i8 = i6;
                            } else {
                                i8 = AnyLayer.this.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT ? i6 - (width2 - width) : 0;
                            }
                        } else {
                            i8 = i6 + width;
                        }
                    } else {
                        i8 = i6 - width2;
                    }
                } else {
                    i8 = i6 - ((width2 - width) / 2);
                }
                if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.CENTER) {
                    if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.ABOVE) {
                        if (AnyLayer.this.mAlignmentVertical != Alignment.Vertical.BELOW) {
                            if (AnyLayer.this.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                                i9 = i7;
                            } else {
                                i9 = AnyLayer.this.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM ? i7 - (height2 - height) : 0;
                            }
                        } else {
                            i9 = i7 + height;
                        }
                    } else {
                        i9 = i7 - height2;
                    }
                } else {
                    i9 = i7 - ((height2 - height) / 2);
                }
                int i10 = i8 - i3;
                int i11 = i9 - i2;
                if (AnyLayer.this.mAlignmentInside) {
                    int width3 = ((AnyLayer.this.mViewHolder.getContainer().getWidth() - AnyLayer.this.mViewHolder.getContainer().getPaddingLeft()) - AnyLayer.this.mViewHolder.getContainer().getPaddingRight()) - width2;
                    int height3 = ((AnyLayer.this.mViewHolder.getContainer().getHeight() - AnyLayer.this.mViewHolder.getContainer().getPaddingTop()) - AnyLayer.this.mViewHolder.getContainer().getPaddingBottom()) - height2;
                    if (i10 < 0) {
                        i10 = 0;
                    } else if (i10 > width3) {
                        i10 = width3;
                    }
                    if (i11 < 0) {
                        i11 = 0;
                    } else if (i11 > height3) {
                        i11 = height3;
                    }
                }
                FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) AnyLayer.this.mViewHolder.getContentWrapper().getLayoutParams();
                layoutParams5.leftMargin = i10;
                layoutParams5.topMargin = i11;
                AnyLayer.this.mViewHolder.getContentWrapper().setLayoutParams(layoutParams5);
                return false;
            }
        });
    }

    private void initBackground() {
        if (this.mBackgroundBlurPercent > 0.0f || this.mBackgroundBlurRadius > 0.0f) {
            this.mViewHolder.getBackground().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: per.goweii.anylayer.AnyLayer.4
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    AnyLayer.this.mViewHolder.getBackground().getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap bitmapSnapshot = Utils.snapshot(AnyLayer.this.mRootView);
                    int[] iArr = new int[2];
                    AnyLayer.this.mRootView.getLocationOnScreen(iArr);
                    int[] iArr2 = new int[2];
                    AnyLayer.this.mViewHolder.getBackground().getLocationOnScreen(iArr2);
                    Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmapSnapshot, iArr2[0] - iArr[0], iArr2[1] - iArr[1], AnyLayer.this.mViewHolder.getBackground().getWidth(), AnyLayer.this.mViewHolder.getBackground().getHeight());
                    bitmapSnapshot.recycle();
                    Blurred blurredScale = Blurred.with(bitmapCreateBitmap).recycleOriginal(true).keepSize(false).scale(AnyLayer.this.mBackgroundBlurScale);
                    if (AnyLayer.this.mBackgroundBlurPercent > 0.0f) {
                        blurredScale.percent(AnyLayer.this.mBackgroundBlurPercent);
                    } else if (AnyLayer.this.mBackgroundBlurRadius > 0.0f) {
                        blurredScale.radius(AnyLayer.this.mBackgroundBlurRadius);
                    }
                    Bitmap bitmapBlur = blurredScale.blur();
                    AnyLayer.this.mViewHolder.getBackground().setScaleType(ImageView.ScaleType.CENTER_CROP);
                    AnyLayer.this.mViewHolder.getBackground().setImageBitmap(bitmapBlur);
                    AnyLayer.this.mViewHolder.getBackground().setColorFilter(AnyLayer.this.mBackgroundColor);
                    return true;
                }
            });
            return;
        }
        if (this.mBackgroundBitmap != null) {
            this.mViewHolder.getBackground().setImageBitmap(this.mBackgroundBitmap);
            this.mViewHolder.getBackground().setColorFilter(this.mBackgroundColor);
        } else if (this.mBackgroundResource != -1) {
            this.mViewHolder.getBackground().setImageResource(this.mBackgroundResource);
            this.mViewHolder.getBackground().setColorFilter(this.mBackgroundColor);
        } else if (this.mBackgroundDrawable != null) {
            this.mViewHolder.getBackground().setImageDrawable(this.mBackgroundDrawable);
            this.mViewHolder.getBackground().setColorFilter(this.mBackgroundColor);
        } else {
            this.mViewHolder.getBackground().setImageDrawable(new ColorDrawable(this.mBackgroundColor));
        }
    }

    private void initContent() {
        View viewFindViewById;
        if (this.mViewHolder.getContent() != null) {
            ViewGroup viewGroup = (ViewGroup) this.mViewHolder.getContent().getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.mViewHolder.getContent());
            }
            this.mViewHolder.getContent().setClickable(true);
            if (this.mTargetView == null && this.mGravity != -1) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mViewHolder.getContent().getLayoutParams();
                layoutParams.gravity = this.mGravity;
                this.mViewHolder.getContent().setLayoutParams(layoutParams);
            }
            if (this.mAsStatusBarViewId > 0 && (viewFindViewById = this.mViewHolder.getContent().findViewById(this.mAsStatusBarViewId)) != null) {
                ViewGroup.LayoutParams layoutParams2 = viewFindViewById.getLayoutParams();
                layoutParams2.height = getStatusBarHeight();
                viewFindViewById.setLayoutParams(layoutParams2);
                viewFindViewById.setVisibility(0);
            }
            this.mViewHolder.getContentWrapper().addView(this.mViewHolder.getContent());
        }
    }
}
