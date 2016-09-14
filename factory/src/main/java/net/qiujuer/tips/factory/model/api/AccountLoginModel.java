package net.qiujuer.tips.factory.model.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountLoginModel extends AppInfoModel {
    @SerializedName("Email")
    private String email;
    @SerializedName("Password")
    private String password;
    @SerializedName("IsExplicit")
    private boolean isExplicit;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExplicit() {
        return isExplicit;
    }

    public void setExplicit(boolean explicit) {
        isExplicit = explicit;
    }

    public JSONObject toJson() {
        try {
            return new JSONObject(new Gson().toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
