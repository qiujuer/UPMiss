package net.qiujuer.tips.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.qiujuer.tips.R;

public class UserActivity extends BlurActivity {
    private Fragment mMain;
    private boolean mIsMain = true;

    @Override
    protected int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getFragmentManager();
        mMain = fragmentManager.findFragmentById(R.id.fragment_container);
    }

    @Override
    protected void onInitToolBar() {
        super.onInitToolBar();
        mToolbar.setTitle(getTitle());
        mToolbar.setNavigationIcon(R.mipmap.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mIsMain) {
            navToFragment(true);
        } else {
            super.onBackPressed();
        }
    }


    private void navToFragment(boolean isMain) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (isMain) {
            fragmentTransaction.show(mMain);
            mToolbar.setTitle(R.string.title_activity_user);
            mIsMain = true;
        } else {
            fragmentTransaction.hide(mMain);


            mToolbar.setTitle(R.string.title_activity_follow);
            mIsMain = false;
        }
        fragmentTransaction.commit();
    }
}
