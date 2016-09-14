package net.qiujuer.tips.factory.model.xml;

import android.content.Context;
import android.content.SharedPreferences;

import net.qiujuer.tips.factory.model.Model;

public abstract class XmlPreference {

    public XmlPreference() {
        refresh(getSharedPreferences());
    }

    protected SharedPreferences getSharedPreferences() {
        Context context = Model.getApplication();
        return context.getSharedPreferences(getPreferenceName(), Context.MODE_PRIVATE);
    }

    protected abstract void refresh(SharedPreferences sp);

    protected abstract String getPreferenceName();

    public abstract void save();
}
