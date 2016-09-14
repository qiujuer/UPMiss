package net.qiujuer.tips.factory.view;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * This is a Share view.
 * Use to SharePresenter
 */
public interface ShareView {
    Activity getActivity();

    Bitmap getBitmap();

    String getShareTitle();

    String getShareSummary();
}
