package net.qiujuer.tips;


import android.app.Activity;
import android.os.Bundle;

import net.qiujuer.tips.factory.cache.Cache;
import net.qiujuer.tips.factory.presenter.AppPresenter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/FZLanTingHeiS-L-GB-Regular.TTF")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        registerActivityLifecycleCallbacks(mLifecycleCallbacks);

        AppPresenter.setApplication(this);
    }

    private void init() {

        Cache.init();
    }


    private ActivityLifecycleCallbacks mLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        private boolean mFirstCreatedView = true;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (mFirstCreatedView) {
                init();
                mFirstCreatedView = false;
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

}
