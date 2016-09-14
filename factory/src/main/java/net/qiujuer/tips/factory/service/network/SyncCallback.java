package net.qiujuer.tips.factory.service.network;

import android.support.annotation.StringRes;

/**
 * Created by Qiujuer
 * on 2015/8/17.
 */
public interface SyncCallback {
    void syncUpdate(SyncChangeModel model);

    void syncStop(@StringRes int status);
}
