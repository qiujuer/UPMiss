package net.qiujuer.tips.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import net.qiujuer.tips.common.R;


public class YearMarker extends TextView {
    private int mMarkerSize = 24;
    private int mLineSize = 12;
    private Drawable mBeginLine;
    private Drawable mEndLine;
    private Drawable mMarkerDrawable;

    public YearMarker(Context context) {
        this(context, null);
    }

    public YearMarker(Context context, AttributeSet attrs) {
        this(context, attrs, R.style.IDefaultYearStyle);
    }

    public YearMarker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.YearMarker, R.attr.YearStyle, defStyle);

        mMarkerSize = a.getDimensionPixelSize(
                R.styleable.YearMarker_markerSize,
                mMarkerSize);

        mLineSize = a.getDimensionPixelSize(
                R.styleable.YearMarker_lineSize,
                mLineSize);

        mBeginLine = a.getDrawable(
                R.styleable.YearMarker_beginLine);

        mEndLine = a.getDrawable(
                R.styleable.YearMarker_endLine);

        mMarkerDrawable = a.getDrawable(
                R.styleable.YearMarker_marker);

        if (mBeginLine != null)
            mBeginLine.setCallback(this);

        if (mEndLine != null)
            mEndLine.setCallback(this);

        if (mMarkerDrawable != null)
            mMarkerDrawable.setCallback(this);

        a.recycle();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = getPaddingLeft() + getPaddingRight();
        int h = getPaddingTop() + getPaddingBottom();

        if (mMarkerDrawable != null) {
            w += mMarkerSize;
            h += mMarkerSize;
        }

        w = Math.max(w, getMeasuredWidth());
        h = Math.max(h, getMeasuredHeight());

        int widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        int heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);


        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initDrawable();
    }

    private void initDrawable() {
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        int pTop = getPaddingTop();
        int pBottom = getPaddingBottom();

        int width = getWidth();
        int height = getHeight();

        int cWidth = width - pLeft - pRight;
        int cHeight = height - pTop - pBottom;

        Rect bounds;

        if (mMarkerDrawable != null) {
            // Size
            int markerSize = Math.min(mMarkerSize, Math.min(cWidth, cHeight));
            mMarkerDrawable.setBounds(0, pTop,
                    width, pTop + markerSize);

            bounds = mMarkerDrawable.getBounds();
        } else {
            bounds = new Rect(pLeft, pTop, pLeft + cWidth, pTop + cHeight);
        }


        int halfLineSize = mLineSize >> 1;
        int lineLeft = bounds.centerX() - halfLineSize;

        if (mBeginLine != null) {
            mBeginLine.setBounds(lineLeft, 0, lineLeft + mLineSize, bounds.top);
        }

        if (mEndLine != null) {
            mEndLine.setBounds(lineLeft, bounds.bottom, lineLeft + mLineSize, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBeginLine != null) {
            mBeginLine.draw(canvas);
        }

        if (mEndLine != null) {
            mEndLine.draw(canvas);
        }

        if (mMarkerDrawable != null) {
            mMarkerDrawable.draw(canvas);
        }

        super.onDraw(canvas);
    }

    public int getLineSize() {
        return mLineSize;
    }

    public void setLineSize(int lineSize) {
        this.mLineSize = lineSize;
        initDrawable();
    }

    public float getMarkerSize() {
        return mMarkerSize;
    }

    public void setMarkerSize(int markerSize) {
        mMarkerSize = markerSize;
        initDrawable();
    }

    public Drawable getmBeginLine() {
        return mBeginLine;
    }

    public void setBeginLine(Drawable beginLine) {
        this.mBeginLine = beginLine;
        initDrawable();
        invalidate();
    }

    public Drawable getEndLine() {
        return mEndLine;
    }

    public void setEndLine(Drawable endLine) {
        this.mEndLine = endLine;
        initDrawable();
        invalidate();
    }

    public Drawable getMarkerDrawable() {
        return mMarkerDrawable;
    }

    public void setMarkerDrawable(Drawable markerDrawable) {
        mMarkerDrawable = markerDrawable;
        initDrawable();
        invalidate();
    }

    public void setMarkerDrawable(int color, int borderColor, int borderSize) {
        GradientDrawable drawable;

        if (mMarkerDrawable != null && mMarkerDrawable instanceof GradientDrawable)
            drawable = (GradientDrawable) mMarkerDrawable;
        else {
            drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
        }

        drawable.setColor(color);
        drawable.setStroke(borderSize, borderColor);

        setMarkerDrawable(drawable);
    }
}
