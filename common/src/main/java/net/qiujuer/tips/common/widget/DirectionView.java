package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import net.qiujuer.tips.common.R;
import net.qiujuer.tips.common.drawable.shape.DirectionShape;

/**
 * Created by qiujuer on 15/8/5.
 * DirectionView
 */
public class DirectionView extends View {
    public DirectionView(Context context) {
        super(context);
    }

    public DirectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(null, 0, 0);
    }

    public DirectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DirectionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        ShapeDrawable drawable = new ShapeDrawable(new DirectionShape(3));
        Paint paint = drawable.getPaint();

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DirectionView);
        int color = a.getColor(R.styleable.DirectionView_iLineColor, Color.GRAY);
        int size = a.getDimensionPixelOffset(R.styleable.DirectionView_iLineSize, (int) (getResources().getDisplayMetrics().density * 2));
        a.recycle();

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(size);
        paint.setColor(color);

        setBackground(drawable);
    }
}
