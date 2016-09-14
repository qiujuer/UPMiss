package net.qiujuer.tips.factory.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MissService extends Service {
    private final static String TAG = MissService.class.getName();
    public final static String ACTION_MISS_MAIN = "net.qiujuer.tips.factory.service.MissService.MISS_MAIN";
    public final static String ACTION_MISS_WIDGET = "net.qiujuer.tips.factory.service.MissService.MISS_WIDGET";
    public final static String ACTION_MISS_WIDGET_VALUES = "VALUES";
    public final static String ACTION_MISS_WIDGET_REFRESH = "net.qiujuer.tips.factory.service.MissService.MISS_WIDGET_REFRESH";
    public final static String ACTION_MISS_SYNC = "net.qiujuer.tips.factory.service.MissService.MISS_SYNC";
    public final static String ACTION_MISS_SYNC_VALUE_STATUS = "STATUS";
    //public final static String ACTION_MISS_ALARM_RECEIVER = "android.intent.action.ALARM_RECEIVER";
    //public final static String ACTION_MISS_ALARM_RECEIVER_REFRESH = "android.intent.action.ALARM_RECEIVER_REFRESH";

    private static final int INTERVAL = 1000 * 60 * 60 * 24;
    private static final int DAY_COUNT = 5;

    private MissServiceImpl mBinder;
    private MissServiceBroadcastReceiver mReceiver;

    public static void start(Context context) {
        try {
            Intent serviceIntent = new Intent(context, MissService.class);
            context.startService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Model.log(TAG, "onCreate");

        Application application = getApplication();
        Model.setApplication(application);

        mBinder = new MissServiceImpl(application);
        mReceiver = new MissServiceBroadcastReceiver();

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_MISS_WIDGET_REFRESH);
            //filter.addAction(ACTION_MISS_ALARM_RECEIVER_REFRESH);
            application.registerReceiver(mReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Register Alarm
        addAlarm(getApplication());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        MissServiceImpl binder = mBinder;
        mBinder = null;
        if (binder != null) {
            binder.destroy();
        }

        MissServiceBroadcastReceiver receiver = mReceiver;
        mReceiver = null;
        Application application = Model.getApplication();
        if (receiver != null && application != null) {
            try {
                application.unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Model.dispose();

        super.onDestroy();

        if (application != null)
            MissService.start(application);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void addAlarm(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);

        final long startTime = calendar.getTimeInMillis();

        // Service intent
        Intent intent = new Intent(context, MissAlarmService.class);
        PendingIntent sender = PendingIntent.getService(context, 0, intent, 0);

        // Schedule the alarm
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
        am.setRepeating(AlarmManager.RTC, startTime, INTERVAL, sender);
    }

    public class MissServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_MISS_WIDGET_REFRESH)) {
                mBinder.refreshWidget();
            }
        }
    }

    class MissServiceImpl extends IMissServiceInterface.Stub implements SyncCallback {
        private final Context mContext;
        private final MissServiceBean mBean;
        private int mLeadDay;

        public MissServiceImpl(Context context) {
            mContext = context;
            mBean = new MissServiceBean();
            mLeadDay = new SettingModel().getLeadTime();
            orderAsync();
        }

        @Override
        public void order() {
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

        @Override
        public void orderAsync() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    order();
                    sendUpdateMainBroadcast();
                }
            };
            Model.getThreadPool().execute(runnable);
        }

        @Override
        public void destroy() {
            stopSelf();
        }

        @Override
        public MissServiceBean getMissBean() {
            Model.log("MissService getMissBean", mBean.toString());
            return mBean;
        }

        @Override
        public void add(long id) {
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
        public void edit(long id) {
            RecordModel model = RecordModel.get(id);
            if (model == null)
                return;

            updateSTTTCache();
            updateNewestCache();
            editTimeLine(model);

            sendUpdateMainBroadcast();
        }

        @Override
        public void delete(String mark, int type) {
            mBean.delete(type);
            deleteTimeLine(UUID.fromString(mark));
            updateNewestCache();

            sendUpdateMainBroadcast();
        }

        @Override
        public void addContact(long id) throws RemoteException {
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
        public void editContact(long id) throws RemoteException {
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
        public void deleteContact(String mark) throws RemoteException {
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
        public void sync() {
            try {
                Sync.sync(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void refreshDesktop(int size) {
            mLeadDay = size;
            sendUpdateWidget();
        }

        @Override
        public void syncStop(@StringRes int status) {
            // Update
            updateSTTTCache();
            updateNewestCache();

            // Broadcast
            Intent intent = new Intent(ACTION_MISS_SYNC);
            intent.putExtra(ACTION_MISS_SYNC_VALUE_STATUS, status);
            mContext.sendBroadcast(intent);

            sendUpdateMainBroadcast();
        }

        @Override
        public void syncUpdate(SyncChangeModel syncModel) {
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

        private void clear() {
            mBean.getNewest().clear();
            mBean.getTimeLine().clear();
            mBean.setMemorialCount(0);
            mBean.setFutureCount(0);
            mBean.setBirthdayCount(0);
        }

        private RecordViewModel getListAdapterDataFromRecordCache(UUID id, List<RecordViewModel> models) {
            for (RecordViewModel d : models) {
                if (d.getId().equals(id))
                    return d;
            }
            return null;
        }

        private ContactViewModel getListAdapterDataFromContactCache(UUID id, List<ContactViewModel> models) {
            for (ContactViewModel d : models) {
                if (d.getId().equals(id))
                    return d;
            }
            return null;
        }

        private void updateSTTTCache() {
            mBean.setBirthdayCount(RecordModel.getCount(RecordModel.TYPE_BIRTHDAY));
            mBean.setFutureCount(RecordModel.getCount(RecordModel.TYPE_FUTURE));
            mBean.setMemorialCount(RecordModel.getCount(RecordModel.TYPE_MEMORIAL));

            Model.log("MissService updateSTTTCache", mBean.toString());
        }


        private void updateNewestCache() {
            List<NewestModel> models = mBean.getNewest();
            models.clear();
            List<RecordModel> recordModels = RecordModel.getNewest(5);
            for (RecordModel model : recordModels) {
                NewestModel cache = new NewestModel(model);
                models.add(cache);
            }
        }

        private void updateTimeLine() {
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

        private void updateContacts() {
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

        private void editTimeLine(RecordModel model) {
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

        private void deleteTimeLine(UUID id) {
            List<RecordViewModel> models = mBean.getTimeLine();
            for (RecordViewModel d : models) {
                if (id.equals(d.getId())) {
                    models.remove(d);
                    break;
                }
            }
        }

        private String mWidgetValues = null;

        private String getBroadcastValue() {
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

        private void sendUpdateMainBroadcast() {
            Intent intent = new Intent(ACTION_MISS_MAIN);
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
            bundle.putString(ACTION_MISS_WIDGET_VALUES, values);

            Intent intent = new Intent(ACTION_MISS_WIDGET);
            intent.putExtras(bundle);

            mContext.sendBroadcast(intent);
        }

        public void refreshWidget() {
            String values = mWidgetValues;
            sendUpdateWidgetBroadcast(values);
        }
    }
}
