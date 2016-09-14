package net.qiujuer.tips.factory.model.xml;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import net.qiujuer.tips.factory.model.Model;

public class SettingModel extends XmlPreference {
    private int startColor;
    private int endColor;
    private int leadTime;
    private int verCode;
    private boolean isBuy;

    public SettingModel() {
        super();
    }

    @Override
    protected String getPreferenceName() {
        return SettingModel.class.getName();
    }

    @Override
    protected void refresh(SharedPreferences sp) {
        startColor = sp.getInt("startColor", 0xffff9800);
        endColor = sp.getInt("endColor", 0xffe51c23);
        leadTime = sp.getInt("leadTime", 2);
        verCode = sp.getInt("verCode", -1);
        isBuy = sp.getBoolean("isBuy", false);
    }

    public int getStartColor() {
        return startColor;
    }

    public int getEndColor() {
        return endColor;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setIsBuy(boolean isBuy) {
        this.isBuy = isBuy;
    }

    @Override
    public void save() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt("startColor", startColor);
        editor.putInt("endColor", endColor);
        editor.putInt("leadTime", leadTime);
        editor.putInt("verCode", verCode);
        editor.putBoolean("isBuy", isBuy);

        editor.apply();
        editor.commit();
    }

    public boolean isFirstUse() {
        int verCode = getVerCode();
        if (this.verCode < verCode) {
            this.verCode = verCode;
            save();
            return true;
        } else
            return false;
    }


    public static int getVerCode() {
        PackageInfo info = getPackageInfo();
        if (info != null)
            return info.versionCode;
        return -1;
    }


    public static String getVerName() {
        PackageInfo info = getPackageInfo();
        if (info != null)
            return info.versionName;
        return null;
    }

    public static PackageInfo getPackageInfo() {
        try {
            PackageManager manager = Model.getApplication().getPackageManager();
            return manager.getPackageInfo(Model.getApplication().getPackageName(), 0);
        } catch (Exception e) {
            return null;
        }
    }
}
