package net.qiujuer.tips.factory.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MissServiceAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start service
        MissService.start(context);

        // Send broadcast
        // Intent i = new Intent(MissService.ACTION_MISS_ALARM_RECEIVER_REFRESH);
        // context.sendBroadcast(i);
    }
}
