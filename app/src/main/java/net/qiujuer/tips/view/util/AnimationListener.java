package net.qiujuer.tips.view.util;

import android.view.animation.Animation;


public class AnimationListener implements Animation.AnimationListener {

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animation.setAnimationListener(null);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}