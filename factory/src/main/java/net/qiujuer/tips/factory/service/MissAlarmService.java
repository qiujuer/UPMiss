package net.qiujuer.tips.factory.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MissAlarmService extends Service {
    private IMissServiceInterface mService = null;


    @Override
    public void onCreate() {
        super.onCreate();
        Thread thr = new Thread(null, mTask, "MissAlarmService");
        thr.start();
    }

    @Override
    public void onDestroy() {
        unBindService(getApplication());
        super.onDestroy();
    }

    private void task() {
        bindService(getApplication());

        // Do order
        if (mService != null) {
            try {
                mService.orderAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Stop
        stopSelf();
    }

    private void bindService(Context context) {
        if (context == null)
            return;

        Intent intent = new Intent(context, MissService.class);
        context.bindService(intent, mConnect, Context.BIND_AUTO_CREATE);

        long endTime = System.currentTimeMillis() + 30 * 1000;
        while (System.currentTimeMillis() < endTime) {
            if (mService == null) {
                synchronized (mBinder) {
                    if (mService == null) {
                        try {
                            mBinder.wait(endTime - System.currentTimeMillis());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void unBindService(Context context) {
        // Service
        IMissServiceInterface service = mService;
        mService = null;
        if (service != null && context != null) {
            context.unbindService(mConnect);
        }
    }

    /**
     * The function that runs in our worker thread
     */
    Runnable mTask = new Runnable() {
        public void run() {
            task();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    /**
     * This is service connector
     */
    private final ServiceConnection mConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IMissServiceInterface.Stub.asInterface(iBinder);
            if (mService != null) {
                synchronized (mBinder) {
                    mBinder.notifyAll();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    /**
     * This is the object that receives interactions from clients.
     */
    private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                                     int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
}

