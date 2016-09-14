package net.qiujuer.tips.factory.model.xml;


import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import net.qiujuer.tips.common.tools.HashTool;
import net.qiujuer.tips.common.tools.SysTool;
import net.qiujuer.tips.factory.model.Model;

import java.util.Date;

public class PhonePreference extends XmlPreference {
    private String device;
    private String hardware;
    private String brand;
    private int sdk;

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

    private String createDevice() {
        return HashTool.getMD5String(this.toString()
                + SysTool.getAndroidId(Model.getApplication())
                + SysTool.getSerialNumber()
                + SysTool.getDeviceId(Model.getApplication()));
    }

    @Override
    protected void refresh(SharedPreferences sp) {
        device = sp.getString("Device", createDevice());
        hardware = sp.getString("Hardware", Build.HARDWARE);
        brand = sp.getString("Brand", Build.BRAND);
        sdk = sp.getInt("Sdk", Build.VERSION.SDK_INT);
        save();
    }

    @Override
    protected String getPreferenceName() {
        return PhonePreference.class.getName();
    }

    @Override
    public void save() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("Device", device);
        editor.putString("Hardware", hardware);
        editor.putString("Brand", brand);
        editor.putInt("Sdk", sdk);

        editor.apply();
    }

    @Override
    public String toString() {
        return "Device:" + device +
                " Hardware:" + hardware +
                " Brand:" + brand +
                " Sdk:" + sdk;
    }
}
