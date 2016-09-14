package net.qiujuer.tips.factory.model.api;


import com.google.gson.annotations.SerializedName;

import net.qiujuer.tips.factory.model.xml.AccountPreference;
import net.qiujuer.tips.factory.util.http.HttpKit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class SyncPullModel extends AccessModel {
    @SerializedName("Last")
    private Date last;

    public SyncPullModel() {
        super();

        AccountPreference accountPreference = AccountPreference.getInstance();
        if (accountPreference.getSyncLast().getTime() != 0)
            last = accountPreference.getSyncLast();

    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    public JSONObject toJson() {
        try {
            return new JSONObject(HttpKit.getGsonBuilder().create().toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
