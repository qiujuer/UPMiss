package net.qiujuer.tips.factory.model.api;

import com.google.gson.annotations.SerializedName;

import net.qiujuer.tips.factory.model.xml.AccountPreference;

/**
 * Created by JuQiu
 * on 16/9/6.
 */
public class AccessModel extends AppInfoModel {
    @SerializedName("AccessToken")
    private String accessToken;
    @SerializedName("PhoneToken")
    private String phoneToken;

    public AccessModel() {
        super();
        AccountPreference accountPreference = AccountPreference.getInstance();
        accessToken = accountPreference.getAccessToken();
        phoneToken = accountPreference.getPhoneToken();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPhoneToken() {
        return phoneToken;
    }

    public void setPhoneToken(String phoneToken) {
        this.phoneToken = phoneToken;
    }
}
