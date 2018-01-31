package net.qiujuer.tips.factory.cache;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.adapter.ContactViewModel;
import net.qiujuer.tips.factory.model.adapter.NewestModel;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;
import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.presenter.AppPresenter;
import net.qiujuer.tips.factory.service.MissBinderInterface;
import net.qiujuer.tips.factory.service.MissService;
import net.qiujuer.tips.factory.service.bean.MissServiceBean;

import java.util.List;

public class Cache extends BroadcastReceiver {
    private static boolean isDispose = true;
    private final static Cache instance;

    static {
        instance = new Cache();
    }

    public static void destroy() {
        isDispose = true;
        try {
            Model.getApplication().unregisterReceiver(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance.cacheNotify.clear();
    }

    public static void init() {
        isDispose = false;

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(MissService.ACTION_MISS_MAIN);
            Model.getApplication().registerReceiver(instance, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        instance.initData();
    }

    public static Cache getInstance() {
        return instance;
    }

    public final CacheNotify cacheNotify = new CacheNotify();
    private final CacheStaCount cacheStaCount = new CacheStaCount();

    public CacheStaCount getCacheStaCount() {
        return cacheStaCount;
    }

    public List<RecordViewModel> getRecords() {
        try {
            MissBinderInterface service = AppPresenter.getService();
            if (service == null)
                return null;
            MissServiceBean bean = service.getMissBean();
            if (bean == null)
                return null;
            return bean.getTimeLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ContactViewModel> getContacts() {
        try {
            MissBinderInterface service = AppPresenter.getService();
            if (service == null)
                return null;
            MissServiceBean bean = service.getMissBean();
            if (bean == null)
                return null;
            return bean.getContacts();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<NewestModel> getNewest() {
        try {
            MissBinderInterface service = AppPresenter.getService();
            if (service == null)
                return null;
            MissServiceBean bean = service.getMissBean();
            if (bean == null)
                return null;
            return bean.getNewest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (MissService.ACTION_MISS_MAIN.equals(action)) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        MissBinderInterface service = AppPresenter.getService();
                        if (service == null)
                            return;

                        MissServiceBean bean = service.getMissBean();
                        if (bean == null)
                            return;


                        cacheNotify.notifyRecordsList(bean.getTimeLine());
                        cacheStaCount.set(bean);
                        cacheNotify.notifySTTT(cacheStaCount);
                        cacheNotify.notifyNewest(bean.getNewest());
                        cacheNotify.notifyContacts(bean.getContacts());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Model.getThreadPool().execute(runnable);
        }
    }

    private void initData() {
        try {
            MissBinderInterface service = AppPresenter.getService();
            if (service == null)
                return;


            service.order();
            MissServiceBean bean = service.getMissBean();
            if (bean == null)
                return;
            cacheStaCount.set(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(RecordModel model) {
        if (isDispose)
            return;

        try {
            AppPresenter.getService().add(model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(RecordModel model) {
        if (isDispose)
            return;

        try {
            AppPresenter.getService().delete(model.getMark().toString(), model.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(RecordModel model) {
        if (isDispose)
            return;

        try {
            AppPresenter.getService().edit(model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(ContactModel model) {
        if (isDispose)
            return;

        try {
            AppPresenter.getService().addContact(model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(ContactModel model) {
        if (isDispose)
            return;

        try {
            AppPresenter.getService().deleteContact(model.getMark().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(ContactModel model) {
        if (isDispose)
            return;

        try {
            AppPresenter.getService().editContact(model.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshDesktop(int day) {
        if (isDispose)
            return;

        try {
            AppPresenter.getService().refreshDesktop(day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
