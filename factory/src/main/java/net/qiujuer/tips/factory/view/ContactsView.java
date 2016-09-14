package net.qiujuer.tips.factory.view;

import net.qiujuer.tips.factory.adapter.BaseAdapter;
import net.qiujuer.tips.factory.model.adapter.ContactViewModel;


public interface ContactsView extends BaseAdapter<ContactViewModel> {
    void setLoading(boolean isLoad);
}
