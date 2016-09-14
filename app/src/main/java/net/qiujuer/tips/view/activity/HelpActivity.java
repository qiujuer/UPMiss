package net.qiujuer.tips.view.activity;

import android.content.Intent;
import android.os.Bundle;

import net.qiujuer.tips.R;

public class HelpActivity extends BaseActivity {

    public static void show(BaseActivity activity) {
        Intent intent = new Intent(activity, HelpActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
}
