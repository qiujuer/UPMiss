package net.qiujuer.tips.factory.presenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.view.RecordsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class ContactRecordsPresenter {
    private UUID mContactId;
    private RecordsView mView;

    public ContactRecordsPresenter(UUID contactId, RecordsView view) {
        mContactId = contactId;
        mView = view;
    }

    public void refresh() {
        if (mContactId == null)
            return;

        final RecordsView view = mView;
        if (view == null)
            return;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.setLoading(true);
            }
        });

        Model.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });
    }

    public void destroy() {
        mView = null;
    }

    private void update() {
        RecordsView view = mView;
        if (view == null)
            return;
        List<RecordViewModel> dataList = new ArrayList<RecordViewModel>();
        ContactModel contactModel = ContactModel.get(mContactId);
        List<RecordModel> models = contactModel.records();
        if (models.size() > 512)
            models = models.subList(0, 512);
        for (RecordModel model : models) {
            RecordViewModel data = new RecordViewModel();
            data.set(model);
            dataList.add(data);
        }
        Collections.sort(dataList);
        update(dataList);
    }

    private void update(final List<RecordViewModel> caches) {
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
