package net.qiujuer.tips.common.drawable.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;

/**
 * This DirectionShape.
 */
public class DirectionShape extends Shape {
    int mDirection = 3;

    public DirectionShape(int direction) {
        mDirection = direction;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        final float size = paint.getStrokeWidth();

        if (mDirection == 3) {
            canvas.drawLine(size, size, getWidth() - size, getHeight() / 2, paint);
            canvas.drawLine(size, getHeight() - size, getWidth() - size, getHeight() / 2, paint);
        }
    }
}
