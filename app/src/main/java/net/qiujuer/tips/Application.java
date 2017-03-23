package net.qiujuer.tips;


import android.app.Activity;

import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.presenter.AppPresenter;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Application extends android.app.Application {
    private List<Activity> mActivities = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/FZLanTingHeiS-L-GB-Regular.TTF")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Model.setApplication(this);
    }

    public void init() {
        AppPresenter.setApplication(this);
        Cache.init();
    }

    public void dispose() {
        AppPresenter.dispose();

        // Model
        Model.dispose();
    }

    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        mActivities.remove(activity);
    }

    public void exit() {
        dispose();
        Cache.destroy();
        for (Activity activity : mActivities) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }
}
