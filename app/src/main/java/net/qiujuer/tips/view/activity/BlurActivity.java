package net.qiujuer.tips.view.activity;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.qiujuer.tips.R;

/**
 * Blur activity
 */
public abstract class BlurActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Background
        onInitBlurBackground();

        // Super
        super.onCreate(savedInstanceState);

        // SetContent
        setContentView(getContentView());

        // ToolBar
        onInitToolBar();

        // Notify
        onInit(savedInstanceState);
    }

    protected abstract int getContentView();

    protected abstract void onInit(Bundle savedInstanceState);

    protected void onInitBlurBackground() {
        Drawable drawable = getBlur();
        if (drawable == null)
            drawable = new ColorDrawable(0xc0ffffff);
        getWindow().getDecorView().setBackgroundDrawable(drawable);
    }

    protected void onInitToolBar() {
        // SetBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null)
            return;
        mToolbar.setOnMenuItemClickListener(this);
        onInflateMenu(mToolbar);
    }

    protected void onInflateMenu(Toolbar toolbar) {

    }

    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
