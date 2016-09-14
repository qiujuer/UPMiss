package net.qiujuer.tips.factory.cache;

import net.qiujuer.tips.factory.cache.notify.UpdateListNotify;
import net.qiujuer.tips.factory.cache.notify.UpdateNewestNotify;
import net.qiujuer.tips.factory.cache.notify.UpdateSTTTNotify;
import net.qiujuer.tips.factory.model.adapter.ContactViewModel;
import net.qiujuer.tips.factory.model.adapter.NewestModel;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;

import java.util.ArrayList;
import java.util.List;


public class CacheNotify {
    private UpdateNewestNotify updateNewestNotify;
    private UpdateSTTTNotify updateSTTTNotify;
    private UpdateListNotify<RecordViewModel> updateListNotify;
    private List<UpdateListNotify<RecordViewModel>> recordsNotifies;
    private List<UpdateListNotify<ContactViewModel>> contactsNotifies;

    protected void clear() {
        updateNewestNotify = null;
        updateSTTTNotify = null;
        updateListNotify = null;
    }

    public void setNewestNotify(UpdateNewestNotify notify) {
        updateNewestNotify = notify;
    }


    public void setSTTTNotify(UpdateSTTTNotify notify) {
        updateSTTTNotify = notify;
    }

    public void setUpdateListNotify(UpdateListNotify<RecordViewModel> updateListNotify) {
        this.updateListNotify = updateListNotify;
    }

    public void addUpdateContactsNotify(UpdateListNotify<ContactViewModel> notify) {
        if (contactsNotifies == null)
            contactsNotifies = new ArrayList<UpdateListNotify<ContactViewModel>>();
        contactsNotifies.add(notify);
    }

    public void removeUpdateContactsNotify(UpdateListNotify<ContactViewModel> notify) {
        if (contactsNotifies != null)
            contactsNotifies.remove(notify);
    }

    public void addUpdateRecordsNotify(UpdateListNotify<RecordViewModel> notify) {
        if (recordsNotifies == null)
            recordsNotifies = new ArrayList<UpdateListNotify<RecordViewModel>>();
        recordsNotifies.add(notify);
    }

    public void removeUpdateRecordsNotify(UpdateListNotify<RecordViewModel> notify) {
        if (recordsNotifies != null)
            recordsNotifies.remove(notify);
    }

    protected void notifyNewest(List<NewestModel> caches) {
        UpdateNewestNotify notify = updateNewestNotify;
        if (notify != null) {
            notify.update(caches);
        }
    }

    protected void notifySTTT(CacheStaCount cache) {
        UpdateSTTTNotify notify = updateSTTTNotify;
        if (notify != null) {
            notify.update(cache);
        }
    }

    protected void notifyRecordsList(List<RecordViewModel> caches) {
        UpdateListNotify<RecordViewModel> notify = updateListNotify;
        if (notify != null) {
            notify.update(caches);
        }


        List<UpdateListNotify<RecordViewModel>> notifies = recordsNotifies;
        if (notifies != null && (!notifies.isEmpty())) {
            for (UpdateListNotify<RecordViewModel> n : notifies) {
                n.update(caches);
            }
        }
    }

    protected void notifyContacts(List<ContactViewModel> caches) {
        List<UpdateListNotify<ContactViewModel>> notifies = contactsNotifies;
        if (notifies != null && (!notifies.isEmpty())) {
            for (UpdateListNotify<ContactViewModel> n : notifies) {
                n.update(caches);
            }
        }
    }
}
