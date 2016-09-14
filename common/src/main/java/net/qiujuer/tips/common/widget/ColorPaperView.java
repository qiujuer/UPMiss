package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import net.qiujuer.tips.common.R;
import net.qiujuer.tips.common.drawable.JagDrawable;


public class ColorPaperView extends TextView {
    private JagDrawable mBackground;

    public ColorPaperView(Context context) {
        super(context);
        init(null);
    }

    public ColorPaperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ColorPaperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorPaperView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ColorPaperView);
        int color = a.getColor(R.styleable.ColorPaperView_iPaperColor, Color.CYAN);
        a.recycle();

        mBackground = new JagDrawable();
        mBackground.setFluCount(new Rect(12, 0, 12, 0));
        mBackground.setDeepness(8, 20);
        setBackground(mBackground);

        setColor(color);
    }

    public void setColor(int color) {
        mBackground.setColor(color);
    }
}
