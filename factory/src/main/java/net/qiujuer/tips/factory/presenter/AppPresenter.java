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
    private static final Object SERVICE_LOCK = new Object();
    private static IMissServiceInterface SERVICE = null;

    private static ServiceConnection CONNECT = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SERVICE = IMissServiceInterface.Stub.asInterface(iBinder);
            if (SERVICE != null) {
                synchronized (SERVICE_LOCK) {
                    SERVICE_LOCK.notifyAll();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            SERVICE = null;
        }
    };

    public static void setApplication(Application application) {
        Model.setApplication(application);

        bindService();
    }

    public static void dispose() {
        // Service
        unBindService();

        // Model
        Model.dispose();
    }

    private static void bindService() {
        Application application = Model.getApplication();
        if (application == null)
            return;
        // UnBind
        unBindService();

        Intent intent = new Intent(application, MissService.class);
        application.bindService(intent, CONNECT, Context.BIND_AUTO_CREATE);
        if (SERVICE == null) {
            synchronized (SERVICE_LOCK) {
                try {
                    SERVICE_LOCK.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        if (SERVICE == null)
            bindService();
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
