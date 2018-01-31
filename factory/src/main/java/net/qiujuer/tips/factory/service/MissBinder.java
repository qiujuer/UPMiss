package net.qiujuer.tips.factory.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;

import com.google.gson.Gson;

import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.adapter.ContactViewModel;
import net.qiujuer.tips.factory.model.adapter.NewestModel;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.model.xml.SettingModel;
import net.qiujuer.tips.factory.service.bean.MissServiceBean;
import net.qiujuer.tips.factory.service.bean.MissWidgetListData;
import net.qiujuer.tips.factory.service.network.Sync;
import net.qiujuer.tips.factory.service.network.SyncCallback;
import net.qiujuer.tips.factory.service.network.SyncChangeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Create at 31/01/2018
 *
 * @author qiujuer Email: qiujuer@live.cn
 * @version 1.0.0
 */

public class MissBinder implements MissBinderInterface, SyncCallback {
    private static final int DAY_COUNT = 5;
    private static volatile MissBinder INSTANCE;
    private final Context mContext;
    private final MissServiceBean mBean;
    private int mLeadDay;

    public static MissBinder getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MissBinder.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MissBinder(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    private MissBinder(Context context) {
        mContext = context;
        mBean = new MissServiceBean();
        mLeadDay = new SettingModel().getLeadTime();
        orderAsync();
    }

    @Override
    public synchronized void order() {
        try {
            clear();
            updateSTTTCache();
            updateNewestCache();
            updateTimeLine();
            updateContacts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Runnable mOrderAsyncRunnable;

    @Override
    public void orderAsync() {
        if (mOrderAsyncRunnable != null) {
            return;
        }

        mOrderAsyncRunnable = new Runnable() {
            @Override
            public void run() {
                order();
                sendUpdateMainBroadcast();
                mOrderAsyncRunnable = null;
            }
        };
        Model.getThreadPool().execute(mOrderAsyncRunnable);
    }


    @Override
    public MissServiceBean getMissBean() {
        Model.log("MissService getMissBean", mBean.toString());
        return mBean;
    }

    @Override
    public synchronized void add(long id) {
        RecordModel model = RecordModel.get(id);
        if (model == null) {
            Model.log("MissService add", "null");
            return;
        }
        Model.log("MissService add", model.toString());

        // Count
        mBean.add(model.getType());

        // Newest
        NewestModel cache = new NewestModel(model);
        mBean.getNewest().addFirst(cache);

        // TimeLine
        RecordViewModel data = new RecordViewModel();
        data.set(model);

        List<RecordViewModel> models = mBean.getTimeLine();
        models.add(data);
        Collections.sort(models);

        sendUpdateMainBroadcast();
    }

    @Override
    public synchronized void edit(long id) {
        RecordModel model = RecordModel.get(id);
        if (model == null)
            return;

        updateSTTTCache();
        updateNewestCache();
        editTimeLine(model);

        sendUpdateMainBroadcast();
    }

    @Override
    public synchronized void delete(String mark, int type) {
        mBean.delete(type);
        deleteTimeLine(UUID.fromString(mark));
        updateNewestCache();

        sendUpdateMainBroadcast();
    }

    @Override
    public synchronized void addContact(long id) {
        ContactModel model = ContactModel.get(id);
        if (model == null)
            return;

        // TimeLine
        ContactViewModel data = new ContactViewModel();
        data.set(model);

        List<ContactViewModel> models = mBean.getContacts();
        models.add(data);
        Collections.sort(models);

        sendUpdateMainBroadcast();
    }

    @Override
    public synchronized void editContact(long id) {
        ContactModel model = ContactModel.get(id);
        if (model == null)
            return;

        List<ContactViewModel> models = mBean.getContacts();
        UUID mark = model.getMark();
        for (ContactViewModel d : models) {
            if (mark.equals(d.getId())) {
                d.set(model);
                Collections.sort(models);
                // Send notify
                sendUpdateMainBroadcast();
                break;
            }
        }
    }

    @Override
    public synchronized void deleteContact(String mark) {
        UUID id = UUID.fromString(mark);
        List<ContactViewModel> models = mBean.getContacts();
        for (ContactViewModel d : models) {
            if (id.equals(d.getId())) {
                models.remove(d);
                // Send notify
                sendUpdateMainBroadcast();
                break;
            }
        }
    }

    @Override
    public synchronized void sync() {
        try {
            Sync.sync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void refreshDesktop(int size) {
        mLeadDay = size;
        sendUpdateWidget();
    }

    @Override
    public synchronized void syncStop(@StringRes int status) {
        // Update
        updateSTTTCache();
        updateNewestCache();

        // Broadcast
        Intent intent = new Intent(MissService.ACTION_MISS_SYNC);
        intent.putExtra(MissService.ACTION_MISS_SYNC_VALUE_STATUS, status);
        mContext.sendBroadcast(intent);

        sendUpdateMainBroadcast();
    }

    @Override
    public synchronized void syncUpdate(SyncChangeModel syncModel) {
        // Sync Contacts
        List<ContactViewModel> addContacts = new ArrayList<ContactViewModel>();
        List<ContactViewModel> contactViewModels = mBean.getContacts();

        List<UUID> ids = syncModel.getContactAdd();
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                ContactModel model = ContactModel.get(id);
                ContactViewModel data = getListAdapterDataFromContactCache(id, contactViewModels);
                if (data == null) {
                    if (model != null) {
                        data = new ContactViewModel();
                        data.set(model);
                        addContacts.add(data);
                    }
                } else {
                    if (model != null) {
                        data.set(model);
                    }
                }
            }
        }

        ids = syncModel.getContactEdit();
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                ContactModel model = ContactModel.get(id);
                ContactViewModel data = getListAdapterDataFromContactCache(id, contactViewModels);
                if (data == null) {
                    if (model != null) {
                        data = new ContactViewModel();
                        data.set(model);
                        addContacts.add(data);
                    }
                } else {
                    if (model != null) {
                        data.set(model);
                    }
                }
            }
        }

        contactViewModels.addAll(addContacts);
        Collections.sort(contactViewModels);

        // Sync Records
        List<RecordViewModel> addRecords = new ArrayList<RecordViewModel>();
        List<RecordViewModel> deleteRecords = new ArrayList<RecordViewModel>();

        List<RecordViewModel> recordViewModels = mBean.getTimeLine();

        ids = syncModel.getRecordAdd();
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                RecordModel model = RecordModel.get(id);
                RecordViewModel data = getListAdapterDataFromRecordCache(id, recordViewModels);
                if (data == null) {
                    if (model != null) {
                        data = new RecordViewModel();
                        data.set(model);
                        addRecords.add(data);
                    }
                } else {
                    if (model != null) {
                        data.set(model);
                    }
                }
            }
        }

        ids = syncModel.getRecordEdit();
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                RecordModel model = RecordModel.get(id);
                RecordViewModel data = getListAdapterDataFromRecordCache(id, recordViewModels);
                if (data == null) {
                    if (model != null) {
                        data = new RecordViewModel();
                        data.set(model);
                        addRecords.add(data);
                    }
                } else {
                    if (model != null) {
                        data.set(model);
                    }
                }
            }
        }

        ids = syncModel.getRecordDelete();
        if (ids != null && ids.size() > 0) {
            for (UUID id : ids) {
                RecordViewModel data = getListAdapterDataFromRecordCache(id, recordViewModels);
                if (data != null) {
                    deleteRecords.add(data);
                }
            }
        }

        recordViewModels.removeAll(deleteRecords);
        recordViewModels.addAll(addRecords);
        Collections.sort(recordViewModels);
    }

    private synchronized void clear() {
        mBean.getNewest().clear();
        mBean.getTimeLine().clear();
        mBean.setMemorialCount(0);
        mBean.setFutureCount(0);
        mBean.setBirthdayCount(0);
    }

    private synchronized RecordViewModel getListAdapterDataFromRecordCache(UUID id, List<RecordViewModel> models) {
        for (RecordViewModel d : models) {
            if (d.getId().equals(id))
                return d;
        }
        return null;
    }

    private synchronized ContactViewModel getListAdapterDataFromContactCache(UUID id, List<ContactViewModel> models) {
        for (ContactViewModel d : models) {
            if (d.getId().equals(id))
                return d;
        }
        return null;
    }

    private synchronized void updateSTTTCache() {
        mBean.setBirthdayCount(RecordModel.getCount(RecordModel.TYPE_BIRTHDAY));
        mBean.setFutureCount(RecordModel.getCount(RecordModel.TYPE_FUTURE));
        mBean.setMemorialCount(RecordModel.getCount(RecordModel.TYPE_MEMORIAL));

        Model.log("MissService updateSTTTCache", mBean.toString());
    }


    private synchronized void updateNewestCache() {
        List<NewestModel> models = mBean.getNewest();
        models.clear();
        List<RecordModel> recordModels = RecordModel.getNewest(5);
        for (RecordModel model : recordModels) {
            NewestModel cache = new NewestModel(model);
            models.add(cache);
        }
    }

    private synchronized void updateTimeLine() {
        List<RecordViewModel> dataList = new ArrayList<RecordViewModel>();
        List<RecordModel> models = RecordModel.getAll();
        if (models.size() > 512)
            models = models.subList(0, 512);
        for (RecordModel model : models) {
            RecordViewModel data = new RecordViewModel();
            data.set(model);
            dataList.add(data);
        }

        List<RecordViewModel> recordViewModels = mBean.getTimeLine();
        recordViewModels.clear();
        recordViewModels.addAll(dataList);
        Collections.sort(recordViewModels);
    }

    private synchronized void updateContacts() {
        List<ContactViewModel> dataList = new ArrayList<ContactViewModel>();
        List<ContactModel> models = ContactModel.getAll();
        for (ContactModel model : models) {
            ContactViewModel data = new ContactViewModel();
            data.set(model);
            dataList.add(data);
        }

        List<ContactViewModel> contactViewModels = mBean.getContacts();
        contactViewModels.clear();
        contactViewModels.addAll(dataList);
        Collections.sort(contactViewModels);
    }

    private synchronized void editTimeLine(RecordModel model) {
        List<RecordViewModel> models = mBean.getTimeLine();
        UUID id = model.getMark();
        for (RecordViewModel d : models) {
            if (id.equals(d.getId())) {
                d.set(model);
                Collections.sort(models);
                break;
            }
        }
    }

    private synchronized void deleteTimeLine(UUID id) {
        List<RecordViewModel> models = mBean.getTimeLine();
        for (RecordViewModel d : models) {
            if (id.equals(d.getId())) {
                models.remove(d);
                break;
            }
        }
    }

    private String mWidgetValues = null;

    private synchronized String getBroadcastValue() {
        List<MissWidgetListData> dataList = new ArrayList<MissWidgetListData>();

        List<RecordViewModel> models = mBean.getTimeLine();

        if (models.size() > 0) {
            for (RecordViewModel model : models) {
                int day = model.getDayNow();
                if ((day >= 0 && day <= mLeadDay) || dataList.size() < DAY_COUNT) {
                    MissWidgetListData widget = new MissWidgetListData(model);
                    dataList.add(widget);
                } else {
                    break;
                }
            }
        }
        if (dataList.size() > 0)
            mWidgetValues = new Gson().toJson(dataList);
        else
            mWidgetValues = null;

        return mWidgetValues;
    }

    private synchronized void sendUpdateMainBroadcast() {
        Intent intent = new Intent(MissService.ACTION_MISS_MAIN);
        mContext.sendBroadcast(intent);

        // Update widget
        sendUpdateWidget();
    }

    private void sendUpdateWidget() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String values = getBroadcastValue();
                sendUpdateWidgetBroadcast(values);
            }
        };
        Model.getThreadPool().execute(runnable);
    }

    private void sendUpdateWidgetBroadcast(String values) {
        if (values == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString(MissService.ACTION_MISS_WIDGET_VALUES, values);

        Intent intent = new Intent(MissService.ACTION_MISS_WIDGET);
        intent.putExtras(bundle);

        mContext.sendBroadcast(intent);
    }

    public void refreshWidget() {
        String values = mWidgetValues;
        sendUpdateWidgetBroadcast(values);
    }
}
