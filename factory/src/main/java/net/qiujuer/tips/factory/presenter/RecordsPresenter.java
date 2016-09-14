package net.qiujuer.tips.factory.presenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.cache.notify.UpdateListNotify;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;
import net.qiujuer.tips.factory.view.RecordsView;

import java.util.List;


public class RecordsPresenter implements UpdateListNotify<RecordViewModel> {
    private RecordsView mView;

    public RecordsPresenter(RecordsView view) {
        mView = view;

        Cache cache = Cache.getInstance();
        cache.cacheNotify.addUpdateRecordsNotify(this);
    }

    public void refresh() {
        final RecordsView view = mView;
        if (view == null)
            return;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.setLoading(true);
            }
        });

        Cache cache = Cache.getInstance();
        update(cache.getRecords());
    }

    public void destroy() {
        mView = null;
        Cache cache = Cache.getInstance();
        cache.cacheNotify.removeUpdateRecordsNotify(this);
    }

    @Override
    public void update(final List<RecordViewModel> caches) {
        final RecordsView view = mView;
        if (view == null)
            return;

        view.setDataSet(caches);

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.setNull(caches == null || caches.isEmpty());
                view.notifyDataSetChanged();
                view.setLoading(false);
            }
        });
    }
}
