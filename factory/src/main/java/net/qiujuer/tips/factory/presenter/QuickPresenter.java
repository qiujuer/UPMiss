package net.qiujuer.tips.factory.presenter;


import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.cache.CacheNotify;
import net.qiujuer.tips.factory.cache.CacheStaCount;
import net.qiujuer.tips.factory.cache.notify.UpdateNewestNotify;
import net.qiujuer.tips.factory.cache.notify.UpdateSTTTNotify;
import net.qiujuer.tips.factory.model.adapter.NewestModel;
import net.qiujuer.tips.factory.view.QuickView;

import java.util.List;

public class QuickPresenter implements UpdateNewestNotify, UpdateSTTTNotify {
    private QuickView mView;

    public QuickPresenter(QuickView view) {
        mView = view;
        Cache cache = Cache.getInstance();
        CacheNotify notify = cache.cacheNotify;
        notify.setNewestNotify(this);
        notify.setSTTTNotify(this);
    }

    public void refresh() {
        Cache cache = Cache.getInstance();

        update(cache.getNewest());
        update(cache.getCacheStaCount());
    }

    public void destroy() {
        mView = null;
    }

    @Override
    public void update(final CacheStaCount cache) {
        if (mView == null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                if (mView == null)
                    return;
                mView.setStatistics(cache);
            }
        });
    }

    @Override
    public void update(final List<NewestModel> caches) {
        if (mView == null)
            return;
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                if (mView == null)
                    return;
                mView.setNewest(caches);
            }
        });
    }
}
