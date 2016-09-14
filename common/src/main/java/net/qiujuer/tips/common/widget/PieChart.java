package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;

import net.qiujuer.tips.common.R;
import net.qiujuer.tips.common.drawable.ShadowDrawable;


public class PieChart extends ImageView implements Checkable {
    private static final int START_ANGLE = 225;

    private boolean isChecked = true;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF mOval = new RectF();
    private int mRingSize = 16;

    private float mShadowRadius;
    private int mMaxShadowOffset;

    private int mBackgroundColor = 0x1D000000;
    private int mForegroundColor = 0xFFFFFFFF;

    private float mPercent;
    private int mPercentSweepAngle = 0;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PieChart, defStyle, 0);

        mBackgroundColor = a.getColor(R.styleable.PieChart_iRingBackColor, mBackgroundColor);
        mForegroundColor = a.getColor(R.styleable.PieChart_iRingForeColor, mForegroundColor);
        mRingSize = a.getDimensionPixelSize(R.styleable.PieChart_iRingSize, mRingSize);
        isChecked = a.getBoolean(R.styleable.PieChart_iChecked, isChecked);

        a.recycle();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mRingSize);

        mShadowPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setDither(true);
        mShadowPaint.setStrokeWidth(mRingSize);
        mShadowPaint.setColor(mForegroundColor);
        mShadowPaint.setStrokeJoin(Paint.Join.ROUND);
        mShadowPaint.setStrokeCap(Paint.Cap.ROUND);


        final float density = getContext().getResources().getDisplayMetrics().density;
        int mShadowXOffset = 0; //(int) (density * ShadowDrawable.X_OFFSET);
        int mShadowYOffset = 0; //(int) (density * ShadowDrawable.Y_OFFSET);
        mMaxShadowOffset = 0;// Math.max(mShadowXOffset, mShadowYOffset);
        mShadowRadius = density * ShadowDrawable.SHADOW_RADIUS;

        setLayerType(View.LAYER_TYPE_SOFTWARE, mShadowPaint);
        mShadowPaint.setShadowLayer(mShadowRadius, mShadowXOffset, mShadowYOffset,
                ShadowDrawable.KEY_SHADOW_COLOR);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Init this Layout size

        int centerX = getWidth() >> 1;
        int centerY = getHeight() >> 1;

        int center = Math.min(centerX, centerY);
        int shadowPadding = (int) (mShadowRadius + mMaxShadowOffset + 0.5);
        int areRadius = center - ((mRingSize + 1) >> 1) - shadowPadding;

        mOval.set(centerX - areRadius, centerY - areRadius, centerX + areRadius, centerY + areRadius);
    }

    private void drawBackground(Canvas canvas) {
        if (mPercentSweepAngle == 360)
            return;
        mPaint.setColor(mBackgroundColor);
        canvas.drawArc(mOval, 0, 360, false, mPaint);
    }

    private void drawForeground(Canvas canvas) {
        Paint paint;
        if (isChecked) {
            paint = mShadowPaint;
        } else {
            paint = mPaint;
            paint.setColor(mForegroundColor);
        }
        canvas.drawArc(mOval, START_ANGLE, mPercentSweepAngle, false, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawForeground(canvas);
    }

    private boolean mBroadcasting;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
        this.mOnCheckedChangeListener = l;
    }

    @Override
    public void setChecked(boolean checked) {
        if (isChecked != checked) {
            isChecked = checked;
            invalidate();

            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }
            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, checked);
            }
            mBroadcasting = false;
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }


    public void setPercent(float p) {
        if (this.mPercent != p) {
            this.mPercent = p;
            setPercentSweepAngle((int) (360 * mPercent));
        }
    }

    public float getPercent() {
        return mPercent;
    }

    private void setPercentSweepAngle(int angle) {
        this.mPercentSweepAngle = angle;
        invalidate();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(PieChart pieChart, boolean isChecked);
    }
}
