package net.qiujuer.tips;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.qiujuer.tips.factory.service.MissService;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            MissService.start(context);
        }
        /*
        Intent newIntent = new Intent(context, LaunchActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
        */
    }
}