package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import net.qiujuer.tips.common.R;
import net.qiujuer.tips.common.animation.TouchEffectAnimator;

public class IntervalButton extends Button {
    private TouchEffectAnimator mAnimator;

    public IntervalButton(Context context) {
        super(context);
        init();
    }

    public IntervalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IntervalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IntervalButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mAnimator = new TouchEffectAnimator(this);
        mAnimator.setTouchEffect(TouchEffectAnimator.PRESS);
        mAnimator.setClipRadius(0);
        mAnimator.setEffectColor(getResources().getColor(R.color.white_alpha_160));
        mAnimator.setAnimDurationFactor(1.2f);

        setBackground(null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize > heightSize)
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mAnimator.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return !mAnimator.interceptClick() && super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mAnimator.onDraw(canvas);
        super.onDraw(canvas);
    }
}
