package net.qiujuer.tips.factory.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JuQiu
 * on 16/9/5.
 */
public class AppInfoModel {
    @SerializedName("AppKey")
    private String appKey;
    @SerializedName("AppSecret")
    private String appSecret;

    public AppInfoModel() {
        appKey = "dc390f15-b970-4f32-bf34-04e4234b2d13";
        appSecret = "OUE1MTAzOTg0RTgzRTUyNEY5RUIxQ0YzQzEwRUI0MTA";
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        appSecret = appSecret;
    }
}
