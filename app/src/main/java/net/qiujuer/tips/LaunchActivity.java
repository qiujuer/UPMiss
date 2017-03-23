package net.qiujuer.tips;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.tips.factory.presenter.AppPresenter;
import net.qiujuer.tips.view.activity.BaseActivity;
import net.qiujuer.tips.view.activity.GuideActivity;
import net.qiujuer.tips.view.activity.MainActivity;
import net.qiujuer.tips.view.util.AnimationListener;


public class LaunchActivity extends BaseActivity {
    private int mDoneCount = 0;
    private boolean mAlreadySkip = false;

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
                skipOnDone();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    private void iconIn() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.anim_launch_item_fade_in);
        anim.setStartOffset(480);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);

                Animation anim = AnimationUtils.loadAnimation(LaunchActivity.this,
                        R.anim.anim_launch_item_scale_in);
                anim.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        skipOnDone();
                    }
                });
                View view = findViewById(R.id.launch_icon);
                view.setVisibility(View.VISIBLE);
                view.startAnimation(anim);
            }
        });
        findViewById(R.id.content).startAnimation(anim);
    }

    private void skipOnDone() {
        if (mAlreadySkip || ((++mDoneCount) < 2))
            return;

        mAlreadySkip = true;

        Run.getUiHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skipByDelay();
            }
        }, 1000);

    }

    private void skipByDelay() {
        Intent intent;
        if (AppPresenter.isFirstUse()) {
            Toast.makeText(this, R.string.app_welcome, Toast.LENGTH_LONG).show();
            intent = new Intent(this, GuideActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
