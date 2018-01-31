package net.qiujuer.tips.factory.service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.qiujuer.tips.factory.model.Model;

import java.util.Calendar;

public class MissService extends Service {
    private final static String TAG = MissService.class.getName();
    public final static String ACTION_MISS_MAIN = "net.qiujuer.tips.factory.service.MissService.MISS_MAIN";
    public final static String ACTION_MISS_WIDGET = "net.qiujuer.tips.factory.service.MissService.MISS_WIDGET";
    public final static String ACTION_MISS_WIDGET_VALUES = "VALUES";
    public final static String ACTION_MISS_WIDGET_REFRESH = "net.qiujuer.tips.factory.service.MissService.MISS_WIDGET_REFRESH";
    public final static String ACTION_MISS_SYNC = "net.qiujuer.tips.factory.service.MissService.MISS_SYNC";
    public final static String ACTION_MISS_SYNC_VALUE_STATUS = "STATUS";

    private static final int INTERVAL = 1000 * 60 * 60 * 24;

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
        mReceiver = new MissServiceBroadcastReceiver();

        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_MISS_WIDGET_REFRESH);
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

        Model.log(TAG, "onStartCommand");

        // Refresh at once
        MissBinder.getInstance(this).orderAsync();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
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

        super.onDestroy();

        if (application != null)
            MissService.start(application);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        if (am == null) {
            return;
        }
        am.cancel(sender);
        am.setRepeating(AlarmManager.RTC, startTime, INTERVAL, sender);
    }

    public class MissServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_MISS_WIDGET_REFRESH.equals(action)) {
                MissBinder.getInstance(context).refreshWidget();
            }
        }
    }
}
