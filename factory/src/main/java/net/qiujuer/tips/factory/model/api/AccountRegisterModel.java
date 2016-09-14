package net.qiujuer.tips.factory.model.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountRegisterModel extends AccountLoginModel {
    @SerializedName("ConfirmPassword")
    private String confirmPassword;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public JSONObject toJson() {
        try {
            return new JSONObject(new Gson().toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
