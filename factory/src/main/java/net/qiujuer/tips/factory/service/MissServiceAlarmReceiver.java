package net.qiujuer.tips.factory.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MissServiceAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MissService.start(context);
    }
}
