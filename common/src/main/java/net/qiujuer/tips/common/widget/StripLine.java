package net.qiujuer.tips.common.widget;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import net.qiujuer.tips.common.R;


public class StripLine extends View {
    public static final String AndroidStyleNameSpace = "http://schemas.android.com/apk/res/android";
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANIMATION_DURATION = 5000;
    private static final int INTERVAL = 10;
    private static final int MIN_HEIGHT = 4;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] mLines = new float[12];
    private float[] mTargetLines = new float[12];
    private float mInterval;
    private float mMinHeight;
    private float mItemWidth;
    private int mHeight;

    private ObjectAnimator mAnimator;


    public StripLine(Context context) {
        this(context, null);
    }

    public StripLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StripLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StripLine(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PieChart, defStyle, 0);

        setEnabled(attrs.getAttributeBooleanValue(AndroidStyleNameSpace, "enabled", true));

        a.recycle();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);


        final float density = getContext().getResources().getDisplayMetrics().density;
        mInterval = density * INTERVAL;
        mMinHeight = density * MIN_HEIGHT;

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight();
        float itemW = getWidth();
        itemW -= mInterval * 11;
        mItemWidth = itemW / 12;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isEnabled()) {
            mPaint.setColor(getResources().getColor(R.color.grey_400));
        } else {
            mPaint.setColor(getResources().getColor(R.color.grey_300));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mLines.length; i++) {
            drawLine(i, mLines[i], canvas);
        }
    }

    private void drawLine(int i, float line, Canvas canvas) {
        final float startHeight = mHeight - (mHeight * line) - mMinHeight;
        final float startX = (mItemWidth + mInterval) * i;
        final float stopX = startX + mItemWidth;
        canvas.drawRect(startX, startHeight, stopX, mHeight, mPaint);
    }

    private void setAnimatorValue(float[] values) {
        mLines = values;
        invalidate();
    }

    public void setLines(float[] lines) {
        if (mTargetLines != lines) {
            mTargetLines = lines;
            animateCheckedState(mTargetLines);
        }
    }

    private void animateCheckedState(float[] values) {
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofObject(this, ANIM_VALUE, new AnimatorEvaluator(mLines), values);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                mAnimator.setAutoCancel(true);
            mAnimator.setDuration(ANIMATION_DURATION);
            mAnimator.setInterpolator(ANIMATION_INTERPOLATOR);
        } else {
            mAnimator.cancel();
            mAnimator.setObjectValues(values);
        }
        mAnimator.start();
    }


    /**
     * =============================================================================================
     * The custom properties
     * =============================================================================================
     */

    private final static class AnimatorEvaluator implements TypeEvaluator<float[]> {
        private final float[] mProperty;

        public AnimatorEvaluator(float[] property) {
            mProperty = property;
        }

        @Override
        public float[] evaluate(float fraction, float[] startValue, float[] endValue) {

            for (int i = 0; i < mProperty.length; i++) {
                mProperty[i] = startValue[i] + fraction * (endValue[i] - startValue[i]);
            }

            return mProperty;
        }
    }

    private final static Property<StripLine, float[]> ANIM_VALUE = new Property<StripLine, float[]>(float[].class, "animValue") {
        @Override
        public float[] get(StripLine object) {
            return object.mLines;
        }

        @Override
        public void set(StripLine object, float[] value) {
            object.setAnimatorValue(value);
        }
    };


}