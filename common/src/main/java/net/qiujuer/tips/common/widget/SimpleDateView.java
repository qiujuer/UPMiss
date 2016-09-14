package net.qiujuer.tips.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import net.qiujuer.tips.common.R;


public class SimpleDateView extends View {
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private StaticLayout[] mTexts = new StaticLayout[7];

    private float mStrokeWidth;

    private float mItemWidth = 0;
    private float mSelectRadius = 0;
    private float mSelectHPoint = 0;

    private int[] mDates;
    private int mSelectIndex;

    public SimpleDateView(Context context) {
        this(context, null);
    }

    public SimpleDateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleDateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final Resources resources = getResources();
        final float density = getContext().getResources().getDisplayMetrics().density;

        mStrokeWidth = 1 * density;

        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16 * density);

        if (!isInEditMode())
            mTextPaint.setTypeface(Typeface.createFromAsset(resources.getAssets(), "fonts/FZLanTingHeiS-L-GB-Regular.TTF"));

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mStrokeWidth);

        setColor(resources.getColor(R.color.white_alpha_240));
        setDate(new int[]{1, 2, 3, 4, 5, 6, 7}, 0);
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

        mItemWidth = contentW / 7.0f;
        mSelectRadius = Math.min(contentH, mItemWidth) / 2.0f - mStrokeWidth / 2;
        mSelectHPoint = top + contentH / 2.0f;

        initText();
    }

    private void initText() {
        if (mDates == null)
            return;
        for (int i = 0; i < 7; i++) {
            String str = String.format("%02d", mDates[i]);

            StaticLayout layout = new StaticLayout(str, mTextPaint, (int) mItemWidth,
                    Layout.Alignment.ALIGN_CENTER, 1, 0, false);

            mTexts[i] = layout;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDates == null)
            return;
        drawText(canvas);
        drawSelect(canvas);
    }

    private void drawText(Canvas canvas) {
        int left = getPaddingLeft();

        int count = canvas.save();
        canvas.translate(left, mSelectHPoint - mTexts[0].getHeight() / 2);
        mTexts[0].draw(canvas);

        for (int i = 1; i < 7; i++) {
            StaticLayout layout = mTexts[i];
            canvas.translate(mItemWidth, 0);
            layout.draw(canvas);
        }

        canvas.restoreToCount(count);
    }

    private void drawSelect(Canvas canvas) {
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int height = getHeight();
        int bottom = getPaddingBottom();

        int contentH = height - top - bottom;

        int count = canvas.save();
        canvas.translate(mSelectIndex * mItemWidth + left, 0);

        canvas.drawCircle(mItemWidth / 2.0f, mSelectHPoint, mSelectRadius, mPaint);

        canvas.restoreToCount(count);
    }

    public void setDate(int[] dates, int index) {
        mDates = dates;
        mSelectIndex = index;
        initText();
    }

    public void setColor(int color) {
        mTextPaint.setColor(color);
        mPaint.setColor(color);
        invalidate();
    }

    public int getSelectDay() {
        return mDates[mSelectIndex];
    }

}
