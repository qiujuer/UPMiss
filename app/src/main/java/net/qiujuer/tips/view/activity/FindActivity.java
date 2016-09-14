package net.qiujuer.tips.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.qiujuer.tips.R;

public class FindActivity extends BlurActivity implements View.OnClickListener {
    public static final String APP_ID = "100029045";
    public static final String SECRET_KEY = "6ed3b63c764ff55ff2613df18a91efb8";
    public static final String BANNER = "30d7fd51a4c3ede0c01c8b1c7c84fea7";
    public static final String INTERSTITIAL = "2d1ccfaaab9a09d4be8eec7d86ccca77";
    public static final String APP_WALL = "57cbaaefcf9154351ca08635620660d3";

    @Override
    protected int getContentView() {
        return R.layout.activity_find;
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        findViewById(R.id.btn_find_richScan).setOnClickListener(this);
        findViewById(R.id.btn_find_user).setOnClickListener(this);
        findViewById(R.id.btn_find_search_kit).setOnClickListener(this);
        findViewById(R.id.btn_find_ads).setOnClickListener(this);

        findViewById(R.id.btn_find_gif).setOnClickListener(this);
        findViewById(R.id.btn_find_wish).setOnClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            setBlur(this);
            return true;
        }

        return super.onMenuItemClick(item);
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
    protected void onInflateMenu(Toolbar toolbar) {
        super.onInflateMenu(toolbar);
        toolbar.inflateMenu(R.menu.menu_find);
    }


    @Override
    public void onClick(View v) {
        BaseActivity context = this;
        switch (v.getId()) {
            case R.id.btn_find_richScan: {
//                Intent intent = new Intent();
//                intent.setClass(context, MipCaptureActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent, MipCaptureActivity.SCANNIN_GREQUEST_CODE);
            }
            break;
            case R.id.btn_find_user: {
                Intent intent = new Intent(context, UserActivity.class);
                startActivity(intent);
                context.setBlur(context);
            }
            break;
            case R.id.btn_find_search_kit: {
                Intent intent = new Intent(context, SearchKitActivity.class);
                startActivity(intent);
                context.setBlur(context);
            }
            break;
            case R.id.btn_find_ads: {
                if (ADS_INIT_STATUS > 0) {
                    //Ads.showAppWall(FindActivity.this, APP_WALL);
                } else if (ADS_INIT_STATUS == 0) {
                    Toast.makeText(FindActivity.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FindActivity.this, "Oh~ init failed.", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                Toast.makeText(FindActivity.this, R.string.btn_add_float_frame_way_toast, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int ADS_INIT_STATUS = 0;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case MipCaptureActivity.SCANNIN_GREQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    // bundle.getParcelableExtra("bitmap"));
//
//                    Object object = new QRcodePresenter().decode(bundle.getString("result"));
//                    if (object != null) {
//                        if (SimpleRecordModel.class.isInstance(object)) {
//                            SimpleRecordModel model = (SimpleRecordModel) object;
//                            RecordAddActivity.actionStart(FindActivity.this, model.getType(), model.getBrief(), model.getColor(), model.getDate());
//                        } else {
//                            Toast.makeText(FindActivity.this, R.string.btn_add_float_frame_way_toast, Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(FindActivity.this, "Oh~ I can't decode this.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
    }
}
