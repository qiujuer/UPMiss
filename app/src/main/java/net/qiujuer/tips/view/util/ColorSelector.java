package net.qiujuer.tips.view.util;

import android.view.LayoutInflater;
import android.view.View;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.widget.ColorPaperView;
import net.qiujuer.tips.common.widget.ColorView;


public class ColorSelector implements View.OnClickListener {
    private View mRoot;
    private int mColor;
    private ColorView mColorView;
    private ColorPaperView mColorPaperView;

    public ColorSelector(LayoutInflater inflater, int color) {
        mColor = color;

        mRoot = inflater.inflate(R.layout.dialog_color_select, null);
        mColorPaperView = (ColorPaperView) mRoot.findViewById(R.id.color_item_container);

        initColorView(R.id.color_item_red);
        initColorView(R.id.color_item_pink);
        initColorView(R.id.color_item_purple);
        initColorView(R.id.color_item_deep_purple);
        initColorView(R.id.color_item_indigo);

        initColorView(R.id.color_item_blue);
        initColorView(R.id.color_item_light_blue);
        initColorView(R.id.color_item_cyan);
        initColorView(R.id.color_item_teal);
        initColorView(R.id.color_item_green);

        initColorView(R.id.color_item_deep_light_green);
        initColorView(R.id.color_item_lime);
        initColorView(R.id.color_item_yellow);
        initColorView(R.id.color_item_amber);
        initColorView(R.id.color_item_orange);

        initColorView(R.id.color_item_deep_orange);
        initColorView(R.id.color_item_brown);
        initColorView(R.id.color_item_grey);
        initColorView(R.id.color_item_blue_grey);
        initColorView(R.id.color_item_black);
    }

    private void initColorView(int id) {
        ColorView view = (ColorView) mRoot.findViewById(id);
        view.setOnClickListener(this);
        if (view.getColor() == mColor) {
            onClick(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (mColorView != null) {
            mColorView.setCheck(false);
        }
        mColorView = (ColorView) v;
        mColorView.setCheck(true);

        final int color = mColorView.getColor();
        mColor = color;
        mColorPaperView.setColor(color);
    }

    public int getColor() {
        return mColor;
    }

    public View getView() {
        return mRoot;
    }
}
