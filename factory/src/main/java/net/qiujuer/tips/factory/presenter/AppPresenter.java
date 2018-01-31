package net.qiujuer.tips.factory.presenter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.RequestQueue;

import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.xml.AccountPreference;
import net.qiujuer.tips.factory.model.xml.SettingModel;
import net.qiujuer.tips.factory.service.MissBinder;
import net.qiujuer.tips.factory.service.MissBinderInterface;
import net.qiujuer.tips.factory.service.MissService;


public class AppPresenter {

    public static void setApplication(Application application) {
        Model.setApplication(application);

        Intent serviceIntent = new Intent(application, MissService.class);
        application.startService(serviceIntent);

        MissService.start(application);
    }

    public static synchronized MissBinderInterface getService() {
        return MissBinder.getInstance(Model.getApplication());
    }

    public static RequestQueue getRequestQueue() {
        return Model.getRequestQueue();
    }

    public static boolean isLogin() {
        return AccountPreference.isLogin();
    }

    public static boolean isFirstUse() {
        SettingModel model = new SettingModel();
        return model.isFirstUse();
    }

    public static boolean isHaveNetwork() {
        final Context context = Model.getApplication();
        if (context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo aNetworkInfo : networkInfo) {
                    if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
