package net.qiujuer.tips.factory.presenter;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.android.volley.RequestQueue;

import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.xml.AccountPreference;
import net.qiujuer.tips.factory.model.xml.SettingModel;
import net.qiujuer.tips.factory.service.IMissServiceInterface;
import net.qiujuer.tips.factory.service.MissService;


public class AppPresenter {
    private static IMissServiceInterface SERVICE = null;

    private static ServiceConnection CONNECT = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SERVICE = IMissServiceInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            SERVICE = null;
        }
    };

    public static void setApplication(Application application) {
        Model.setApplication(application);

        Intent serviceIntent = new Intent(application, MissService.class);
        application.startService(serviceIntent);
        bindService();
    }

    private static void bindService() {
        Application application = Model.getApplication();
        Intent intent = new Intent(application, MissService.class);
        application.bindService(intent, CONNECT, Context.BIND_AUTO_CREATE);
    }

    private static void unBindService() {
        // Service
        IMissServiceInterface service = SERVICE;
        SERVICE = null;
        if (service != null) {
            try {
                service.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Application application = Model.getApplication();
            if (application != null)
                application.unbindService(CONNECT);
        }
    }

    public static synchronized IMissServiceInterface getService() {
        return SERVICE;
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
