package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;

import net.qiujuer.genius.ui.widget.FloatActionButton;
import net.qiujuer.tips.common.R;

public class FloatAction extends FloatActionButton {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mCenterX, mCenterY;
    private float mHalfLong;

    public FloatAction(Context context) {
        super(context);
        init();
    }

    public FloatAction(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatAction(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatAction(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        final float density = getContext().getResources().getDisplayMetrics().density;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources().getColor(R.color.white_alpha_192));
        mPaint.setStrokeWidth(2 * density);

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

        int minLong = Math.min(contentH, contentW);
        mHalfLong = minLong * 0.2f;
    }


    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAdd(canvas);
    }

    private void drawAdd(Canvas canvas) {
        float topY = mCenterY - mHalfLong;
        float bottomY = mCenterY + mHalfLong;

        canvas.drawLine(mCenterX, topY, mCenterX, bottomY, mPaint);


        float leftX = mCenterX - mHalfLong;
        float rightX = mCenterX + mHalfLong;

        canvas.drawLine(leftX, mCenterY, rightX, mCenterY, mPaint);

    }
}
