package net.qiujuer.tips.factory.presenter;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.cache.notify.UpdateListNotify;
import net.qiujuer.tips.factory.model.adapter.ContactViewModel;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.view.ContactsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ContactsPresenter implements UpdateListNotify<ContactViewModel> {
    private ContactsView mView;

    public ContactsPresenter(ContactsView view) {
        mView = view;

        Cache cache = Cache.getInstance();
        cache.cacheNotify.addUpdateContactsNotify(this);

    }

    public void refresh() {
        final ContactsView view = mView;
        if (view == null)
            return;

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.setLoading(true);
            }
        });

        Cache cache = Cache.getInstance();
        update(cache.getContacts());
    }

    public void destroy() {
        mView = null;
        Cache cache = Cache.getInstance();
        cache.cacheNotify.removeUpdateContactsNotify(this);
    }

    private void update() {
        ContactsView view = mView;
        if (view == null)
            return;

        List<ContactViewModel> dataList = new ArrayList<ContactViewModel>();
        List<ContactModel> models = ContactModel.getAll();
        for (ContactModel model : models) {
            ContactViewModel data = new ContactViewModel();
            data.set(model);
            dataList.add(data);
        }

        Collections.sort(dataList);
        update(dataList);
    }

    @Override
    public void update(final List<ContactViewModel> caches) {
        final ContactsView view = mView;
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
