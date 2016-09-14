package net.qiujuer.tips.factory.view;

import android.content.Context;
import android.support.annotation.StringRes;

public interface SyncView {
    void syncStart();

    void syncStop(@StringRes int statusRes);

    Context getContext();
}
