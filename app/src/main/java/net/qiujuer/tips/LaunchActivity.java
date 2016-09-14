package net.qiujuer.tips;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import net.qiujuer.tips.factory.presenter.AppPresenter;
import net.qiujuer.tips.view.activity.BaseActivity;
import net.qiujuer.tips.view.activity.GuideActivity;
import net.qiujuer.tips.view.activity.MainActivity;
import net.qiujuer.tips.view.util.AnimationListener;


public class LaunchActivity extends BaseActivity {

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        iconIn();

        Thread thread = new Thread("GraveTips-Launch-InitThread") {
            @Override
            public void run() {
                Application application = (Application) getApplication();
                application.init();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        skip();
                    }
                }, 3200);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private void iconIn() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.anim_launch_item_fade_in);
        anim.setStartOffset(560);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);

                Animation anim = AnimationUtils.loadAnimation(LaunchActivity.this,
                        R.anim.anim_launch_item_scale_in);
                View view = findViewById(R.id.launch_icon);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(anim);
            }
        });
        findViewById(R.id.content).startAnimation(anim);
    }

    private void skip() {
        Intent intent;
        if (AppPresenter.isFirstUse()) {
            Toast.makeText(this, R.string.app_welcome, Toast.LENGTH_LONG).show();
            intent = new Intent(this, GuideActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        /*intent = new Intent(this, GuideActivity.class);*/
        startActivity(intent);
        finish();
    }
}
