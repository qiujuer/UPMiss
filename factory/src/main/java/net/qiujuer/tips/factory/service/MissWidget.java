package net.qiujuer.tips.factory.service;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import net.qiujuer.tips.factory.R;

public class MissWidget extends AppWidgetProvider {
    private Bundle mBundle;

    private static ComponentName getComponentName(Context context) {
        return new ComponentName(context, MissWidget.class);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Widget button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_miss_widget);
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.btn_start_activity, pending);
        appWidgetManager.updateAppWidget(appWidgetIds, views);
        if (mBundle != null) {
            performUpdate(context, appWidgetManager, appWidgetIds, mBundle);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MissService.ACTION_MISS_WIDGET.equals(intent.getAction())) {
            mBundle = intent.getExtras();
            performUpdate(context, AppWidgetManager.getInstance(context), AppWidgetManager.getInstance(context).getAppWidgetIds(getComponentName(context)), mBundle);
        } else {
            mBundle = null;
            super.onReceive(context, intent);

            // Start and send broadcast
            MissService.start(context);
            Intent i = new Intent(MissService.ACTION_MISS_WIDGET_REFRESH);
            context.sendBroadcast(i);
        }
    }

    private void performUpdate(Context context, AppWidgetManager awm, int[] appWidgetIds, Bundle bundle) {
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MissWidgetService.class);
            intent.putExtras(bundle);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_miss_widget);
            views.setRemoteAdapter(R.id.list_miss_widget, intent);

            //awm.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_miss_widget);
            awm.updateAppWidget(appWidgetId, views);
        }
    }
}
