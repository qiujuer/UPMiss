package net.qiujuer.tips.factory.model.api;


import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

public class AccountRspModel {
    @SerializedName("Email")
    private String email;
    @SerializedName("AccessToken")
    private String accessToken;
    @SerializedName("ExpiresIn")
    private Date expiresIn;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }

    public static Type getRspType() {
        return new TypeToken<RspModel<AccountRspModel>>() {
        }.getType();
    }
}
