package net.qiujuer.tips.factory.model;


import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import net.qiujuer.tips.common.BuildConfig;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model {
    private final static String TAG = Model.class.getName();
    public final static UUID EMPTY_ID = new UUID(0, 0);

    public static boolean DEBUG = true;
    private static Application APPLICATION;

    private static RequestQueue REQUEST_QUEUE;
    private static ExecutorService EXECUTORSERVICE;


    public static Application getApplication() {
        return APPLICATION;
    }

    public static void setApplication(Application application) {
        if (application != null && application != APPLICATION) {
            Model.log(TAG, "setApplication");

            APPLICATION = application;
            // DB Flow init
            FlowManager.init(new FlowConfig.Builder(APPLICATION).build());

            stopRequestQueue();
            REQUEST_QUEUE = Volley.newRequestQueue(APPLICATION);
        }
    }

    public static void dispose() {
        // ThreadPool
        stopThreadPool();

        // Network
        stopRequestQueue();

        // DB Flow destroy
        FlowManager.destroy();
        APPLICATION = null;
    }

    private static void stopRequestQueue() {
        RequestQueue queue = REQUEST_QUEUE;
        REQUEST_QUEUE = null;
        if (queue != null) {
            queue.stop();
        }
    }

    private static void stopThreadPool() {
        ExecutorService service = EXECUTORSERVICE;
        EXECUTORSERVICE = null;
        if (service != null && !service.isShutdown()) {
            try {
                service.shutdownNow();
                service.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static RequestQueue getRequestQueue() {
        return REQUEST_QUEUE;
    }

    public static Executor getThreadPool() {
        // Check and init executor
        if (EXECUTORSERVICE == null) {
            synchronized (Model.class) {
                if (EXECUTORSERVICE == null) {
                    // Init threads executor
                    int size = Runtime.getRuntime().availableProcessors();
                    EXECUTORSERVICE = Executors.newFixedThreadPool(size > 0 ? size : 2);
                }
            }
        }
        return EXECUTORSERVICE;
    }

    public static void log(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg);
    }
}
