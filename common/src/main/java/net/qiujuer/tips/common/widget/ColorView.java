package net.qiujuer.tips.common.widget;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import net.qiujuer.tips.common.R;

public class ColorView extends View {
    private final static int EX_CIRCLE_SIZE = 2;

    private int mColor;
    private float mExCircleSize;
    private float mExCircleRadius;
    private float mRadius;
    private boolean isCheck;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaintEx = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mCenterX, mCenterY;

    public ColorView(Context context) {
        super(context);
        init(null);
    }

    public ColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ColorView);

        int color = a.getColor(R.styleable.ColorView_iColor, Color.CYAN);

        a.recycle();

        final float density = getContext().getResources().getDisplayMetrics().density;

        mExCircleSize = (EX_CIRCLE_SIZE * density);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mPaintEx.setStyle(Paint.Style.STROKE);
        mPaintEx.setAntiAlias(true);
        mPaintEx.setDither(true);
        mPaintEx.setStrokeWidth(mExCircleSize);
        mPaintEx.setColor(getResources().getColor(R.color.black_alpha_128));

        setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int width = getWidth();
        int height = getHeight();

        int top = getPaddingTop();
        int left = getPaddingLeft();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        int contentW = width - left - right;
        int contentH = height - top - bottom;

        mCenterX = (contentW >> 1) + left;
        mCenterY = (contentH >> 1) + top;

        float halfExSize = mExCircleSize / 2;

        mExCircleRadius = Math.min(contentH / 2, contentW / 2) - halfExSize;
        mRadius = mExCircleRadius - mExCircleSize * 0.8f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isCheck) {
            canvas.drawCircle(mCenterX, mCenterY, mExCircleRadius, mPaintEx);
        }
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
        invalidate();
    }

    public void setColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }

    public int getColor() {
        return mColor;
    }

}
