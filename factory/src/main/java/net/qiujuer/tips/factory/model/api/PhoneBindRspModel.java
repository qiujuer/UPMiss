package net.qiujuer.tips.factory.model.api;


import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PhoneBindRspModel {
    @SerializedName("AccountEmail")
    public String accountEmail;
    @SerializedName("PhoneToken")
    public String phoneToken;

    public String getPhoneToken() {
        return phoneToken;
    }

    public void setPhoneToken(String phoneToken) {
        this.phoneToken = phoneToken;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public static Type getRspType() {
        return new TypeToken<RspModel<PhoneBindRspModel>>() {
        }.getType();
    }
}
