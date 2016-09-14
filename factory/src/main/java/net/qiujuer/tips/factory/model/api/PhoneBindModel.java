package net.qiujuer.tips.factory.model.api;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import net.qiujuer.tips.factory.model.xml.PhonePreference;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneBindModel extends AccessModel {
    @SerializedName("Device")
    private String device;
    @SerializedName("Hardware")
    private String hardware;
    @SerializedName("Brand")
    private String brand;
    @SerializedName("Sdk")
    private int sdk;

    public PhoneBindModel() {
        super();

        PhonePreference phonePreference = new PhonePreference();
        this.device = phonePreference.getDevice();
        this.hardware = phonePreference.getHardware();
        this.brand = phonePreference.getBrand();
        this.sdk = phonePreference.getSdk();
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSdk() {
        return sdk;
    }

    public void setSdk(int sdk) {
        this.sdk = sdk;
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
