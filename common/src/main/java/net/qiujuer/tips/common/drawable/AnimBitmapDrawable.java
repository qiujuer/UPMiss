package net.qiujuer.tips.common.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.view.animation.Interpolator;

/**
 * Created by JuQiu
 * on 16/6/28.
 */

public class AnimBitmapDrawable extends BitmapDrawable implements Animatable {
    private static final int FRAME_DURATION = 16;
    private static final int IN_ANIM_DURATION = 380;
    private boolean mRun = false;
    private Interpolator mInterpolator = AnimJagDrawable.DECELERATE_INTERPOLATOR;
    private long mStartTime;

    private boolean mFirst = true;

    public AnimBitmapDrawable(Bitmap bitmap) {
        super(bitmap);
        setAlpha(0);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mFirst) {
            mFirst = false;
            start();
        } else {
            super.draw(canvas);
            canvas.drawColor(0x50f0f0f0);
        }
    }

    private void onInAnimateUpdate(float factor) {
        setAlpha((int) (255 * factor));
    }

    private final Runnable mAnim = new Runnable() {
        @Override
        public void run() {
            long currentTime = SystemClock.uptimeMillis();
            long diff = currentTime - mStartTime;
            final int duration = IN_ANIM_DURATION;
            if (diff <= duration) {
                float interpolation = mInterpolator.getInterpolation((float) diff / (float) duration);
                // Notify
                onInAnimateUpdate(interpolation);

                // Next
                scheduleSelf(this, currentTime + FRAME_DURATION);
            } else {
                unscheduleSelf(this);

                // Notify
                onInAnimateUpdate(1f);

                mRun = false;
            }
        }
    };

    @Override
    public void start() {
        mRun = true;
        // Start animation
        mStartTime = SystemClock.uptimeMillis() + FRAME_DURATION;
        scheduleSelf(mAnim, mStartTime);
    }

    @Override
    public void stop() {
        if (mRun) {
            unscheduleSelf(mAnim);
            mRun = false;
        }
    }

    @Override
    public boolean isRunning() {
        return mRun;
    }
}
