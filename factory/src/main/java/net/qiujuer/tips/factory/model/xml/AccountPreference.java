package net.qiujuer.tips.factory.model.xml;


import android.content.SharedPreferences;
import android.text.TextUtils;

import net.qiujuer.tips.factory.model.api.AccountRspModel;

import java.util.Date;

public class AccountPreference extends XmlPreference {
    private static AccountPreference INSTANCE;
    private String email;
    private String accessToken;
    private Date expiresIn;
    private String phoneToken;
    private Date syncLast;

    public static AccountPreference getInstance() {
        if (INSTANCE == null) {
            synchronized (AccountPreference.class) {
                if (INSTANCE == null)
                    INSTANCE = new AccountPreference();
            }
        }
        return INSTANCE;
    }

    private AccountPreference() {
        super();
    }

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

    public String getPhoneToken() {
        return phoneToken;
    }

    public void setPhoneToken(String phoneToken) {
        this.phoneToken = phoneToken;
    }

    public Date getSyncLast() {
        return syncLast;
    }

    public void setSyncLast(Date syncLast) {
        this.syncLast = syncLast;
    }

    @Override
    protected void refresh(SharedPreferences sp) {
        email = sp.getString("Email", "");
        accessToken = sp.getString("AccessToken", "");
        expiresIn = new Date(sp.getLong("ExpiresIn", 0));
        phoneToken = sp.getString("PhoneToken", "");
        syncLast = new Date(sp.getLong("SyncLast", 0));
    }

    @Override
    protected String getPreferenceName() {
        return AccountPreference.class.getName();
    }

    @Override
    public void save() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("Email", email);
        editor.putString("AccessToken", accessToken);
        editor.putLong("ExpiresIn", expiresIn.getTime());
        editor.putString("PhoneToken", phoneToken);
        editor.putLong("SyncLast", syncLast.getTime());

        editor.apply();
    }

    public static AccountPreference save(AccountRspModel model) {
        AccountPreference accountPreference = getInstance();
        accountPreference.email = model.getEmail();
        accountPreference.accessToken = model.getAccessToken();
        accountPreference.expiresIn = model.getExpiresIn();
        accountPreference.save();
        return accountPreference;
    }

    public static boolean isLogin() {
        AccountPreference accountPreference = getInstance();
        return !(accountPreference.email == null
                || accountPreference.email.length() == 0
                || accountPreference.accessToken == null
                || accountPreference.accessToken.length() == 0
                || accountPreference.expiresIn.getTime() == 0
                || accountPreference.phoneToken == null
                || accountPreference.phoneToken.length() == 0);
    }

    public static void updateLastDate(Date date) {
        if (date == null || date.getTime() == 0)
            return;
        AccountPreference accountPreference = getInstance();
        accountPreference.setSyncLast(date);
        accountPreference.save();
    }

    public static void updatePhoneToken(String token) {
        if (TextUtils.isEmpty(token))
            return;
        AccountPreference accountPreference = getInstance();
        accountPreference.setPhoneToken(token);
        accountPreference.save();
    }
}
