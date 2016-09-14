package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import net.qiujuer.tips.common.R;


public class TackinessView extends View {
    public static float MAX_COUNT = 10;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Circle[] mCircles;
    private int mSectionLen;
    private int mCenterY;


    public TackinessView(Context context) {
        this(context, null);
    }

    public TackinessView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TackinessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TackinessView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PieChart, defStyle, 0);

        a.recycle();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(196);

        mCircles = new Circle[12];
        for (int i = 0; i < 12; i++) {
            mCircles[i] = new Circle();
            mCircles[i].month = i + 1;
            mCircles[i].count = mCircles[i].month * 2;
            if (mCircles[i].count > 10)
                mCircles[i].count -= 10;
            mCircles[i].color = 0x30000000;
        }

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterY = getHeight() >> 1;
        mSectionLen = getWidth() / 13;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Circle circle : mCircles) {
            drawCircle(circle, canvas);
        }

    }

    private void drawCircle(Circle circle, Canvas canvas) {
        int circleX = mSectionLen * circle.month;
        float radius;
        if (circle.count > MAX_COUNT)
            radius = mSectionLen * 0.8f;
        else {
            radius = mSectionLen * 0.8f * (circle.count / MAX_COUNT);
        }
        mPaint.setColor(circle.color);
        canvas.drawCircle(circleX, mCenterY, radius, mPaint);
    }

    public class Circle {
        public int month;
        public int color;
        public int count;
    }
}
