package net.qiujuer.tips.factory.util.http;


import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.qiujuer.tips.factory.util.SpecificClassExclusionStrategy;

public class HttpKit {
    public static final int SOCKET_TIMEOUT_MS = 15000;

    private final static String SITE_URL = "http://upmiss.qiujuer.net/";
    private final static String SITE_URL_LOCAL = "http://192.168.0.114:14373/";

    public static String getAccountRegisterUrl() {
        return SITE_URL + "api/Account/Register";
    }

    public static String getAccountLoginUrl() {
        return SITE_URL + "api/Account/Login";
    }

    public static String getPhoneBindUrl() {
        return SITE_URL + "api/Phone/Bind";
    }

    public static String getRecordPushUrl() {
        return SITE_URL + "api/Sync/Push";
    }

    public static String getRecordSyncUrl() {
        return SITE_URL + "api/Sync/Pull";
    }

    public static GsonBuilder getGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new SpecificClassExclusionStrategy(null, BaseModel.class));
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return gsonBuilder;
    }

    public static GsonBuilder getRspGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new SpecificClassExclusionStrategy(null, BaseModel.class));
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return gsonBuilder;
    }
}
