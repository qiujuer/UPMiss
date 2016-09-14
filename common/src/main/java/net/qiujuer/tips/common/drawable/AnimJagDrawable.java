package net.qiujuer.tips.common.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Have a circle animation
 */
public class AnimJagDrawable extends JagDrawable {
    protected static final int FRAME_DURATION = 16;

    // Base Values
    protected static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator(1.2f);

    // Time
    protected static final int IN_ANIM_DURATION = 560;

    private Point mPoint = new Point();
    private float mRadius;
    private float mMaxRadius;

    private Interpolator mInterpolator = DECELERATE_INTERPOLATOR;
    private long mStartTime;
    private boolean isRun = false;
    private boolean isFirst = true;

    @Override
    protected void draw(Canvas canvas, Path path, Paint paint) {
        if (isFirst && isRun) {
            isFirst = false;
            startAnim();
        }
        if (mRadius > 0) {
            int sc = canvas.save();
            canvas.clipPath(path);
            canvas.drawCircle(mPoint.x, mPoint.y, mRadius, paint);
            canvas.restoreToCount(sc);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mPoint.set(bounds.left, bounds.top);

        int x = bounds.right - bounds.left;
        int y = bounds.bottom - bounds.top;

        mMaxRadius = (float) Math.sqrt(x * x + y * y);
    }

    public void startAnim() {
        if (isRun) {
            unscheduleSelf(mAnim);
        }
        isRun = true;
        // Start animation
        // delay 3*FRAME_DURATION time
        mStartTime = SystemClock.uptimeMillis() + 3 * FRAME_DURATION;
        scheduleSelf(mAnim, mStartTime);
    }

    protected void onInAnimateUpdate(float factor) {
        mRadius = mMaxRadius * factor;
        invalidateSelf();
    }


    private final Runnable mAnim = new Runnable() {
        @Override
        public void run() {
            isFirst = false;
            long currentTime = SystemClock.uptimeMillis();
            long diff = currentTime - mStartTime;
            int duration = IN_ANIM_DURATION;
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

                isRun = false;
            }
        }
    };
}
