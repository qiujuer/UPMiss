package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import net.qiujuer.tips.common.R;


public class StripTitle extends View {
    private TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private int mInterval = 10;
    private int mTextStartY;
    private float mItemWidth;
    private float mTextPaddingLeft;


    public StripTitle(Context context) {
        this(context, null);
    }

    public StripTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StripTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StripTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
    }


    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isEnabled()) {
            mPaint.setColor(getResources().getColor(R.color.grey_700));
        } else {
            mPaint.setColor(getResources().getColor(R.color.grey_300));
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float itemW = getWidth();
        itemW -= mInterval * 11;
        mItemWidth = itemW / 12;

        mPaint.setTextSize((mItemWidth) / 2);

        float textW = mPaint.measureText("00");
        mTextPaddingLeft = (mItemWidth - textW) / 2;

        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mTextStartY = (int) (getHeight() / 2 + Math.ceil(fm.descent - fm.ascent) / 2 - 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 1; i <= 12; i++) {
            drawText(i, canvas);
        }
    }

    private void drawText(int i, Canvas canvas) {
        final float startX = (mItemWidth + mInterval) * (i - 1) + mTextPaddingLeft;

        String str = format(i);
        canvas.drawText(str, 0, str.length(), startX, mTextStartY, mPaint);
    }

    public static String format(int d) {
        String format = "%0" + (2) + "d";
        return String.format(format, d);
    }

}
