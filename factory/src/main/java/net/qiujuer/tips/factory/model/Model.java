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

    public static boolean DEBUG = BuildConfig.DEBUG;
    private static Application APPLICATION;

    private static volatile RequestQueue REQUEST_QUEUE;
    private static volatile ExecutorService EXECUTORSERVICE;


    public static Application getApplication() {
        return APPLICATION;
    }

    public static void setApplication(Application application) {
        APPLICATION = application;
        // DB Flow init
        FlowManager.init(new FlowConfig.Builder(application)
                .openDatabasesOnInit(true)
                .build());

        Model.log(TAG, "setApplication");
    }

    public static RequestQueue getRequestQueue() {
        if (REQUEST_QUEUE == null) {
            synchronized (Model.class) {
                if (REQUEST_QUEUE == null) {
                    REQUEST_QUEUE = Volley.newRequestQueue(APPLICATION);
                }
            }
        }
        return REQUEST_QUEUE;
    }

    public static Executor getThreadPool() {
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
            Log.w(tag, msg);
    }
}
