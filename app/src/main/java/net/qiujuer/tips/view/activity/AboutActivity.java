package net.qiujuer.tips.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import net.qiujuer.tips.R;

public class AboutActivity extends BlurActivity implements View.OnClickListener {

    public static void show(BaseActivity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
        activity.setBlurSrcAsync();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    protected void onInitToolBar() {
        super.onInitToolBar();
        mToolbar.setNavigationIcon(R.mipmap.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        findViewById(R.id.btn_github).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_github) {
            Uri uri = Uri.parse("https://github.com/qiujuer/UPMiss");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
