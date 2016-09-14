package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import net.qiujuer.genius.ui.drawable.TouchEffectDrawable;
import net.qiujuer.genius.ui.drawable.effect.RippleEffect;
import net.qiujuer.tips.common.R;


public class TouchEffectLayout extends LinearLayout implements TouchEffectDrawable.PerformClicker {
    private TouchEffectDrawable mTouch;

    public TouchEffectLayout(Context context) {
        super(context);
        init();
    }

    public TouchEffectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchEffectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TouchEffectLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mTouch = new TouchEffectDrawable();
        mTouch.setEffect(new RippleEffect());
        mTouch.setColor(getResources().getColor(R.color.black_alpha_16));
        setBackgroundDrawable(mTouch);
    }

    @Override
    public boolean performClick() {
        final TouchEffectDrawable d = mTouch;

        if (d != null) {
            return d.performClick(this) && super.performClick();
        } else
            return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        final boolean ret = super.onTouchEvent(event);

        // send to touch drawable
        final TouchEffectDrawable d = mTouch;
        if (ret && d != null && isEnabled()) {
            d.onTouch(event);
        }

        return ret;
    }

    @Override
    public void postPerformClick() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                performClick();
            }
        };

        if (!this.post(runnable)) {
            performClick();
        }
    }
}
