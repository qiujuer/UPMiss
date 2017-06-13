package net.qiujuer.tips.view.activity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.genius.ui.widget.ImageView;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.model.api.ProductVersionModel;
import net.qiujuer.tips.factory.presenter.ProductPresenter;
import net.qiujuer.tips.factory.presenter.SyncPresenter;
import net.qiujuer.tips.factory.view.ProductView;
import net.qiujuer.tips.factory.view.SyncView;
import net.qiujuer.tips.view.fragment.ContactsFragment;
import net.qiujuer.tips.view.fragment.QuickFragment;
import net.qiujuer.tips.view.fragment.RecordsFragment;
import net.qiujuer.tips.view.fragment.ZoomOutPageTransformer;

import java.text.SimpleDateFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class MainActivity extends BaseActivity implements ProductView, Toolbar.OnMenuItemClickListener, SyncView, View.OnClickListener {
    private Loading mLoading;
    private ViewPager mPager;
    private Toolbar mToolbar;
    private SyncPresenter mPresenter;
    private SectionsPagerAdapter mAdapter;
    private View mCreate;
    private int mCreateTranslationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_nav_main);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.menu_main);

        initSyncItem();
        initTitle();

        mCreate = findViewById(R.id.main_img_create);
        mCreate.setOnClickListener(this);

        mAdapter = new SectionsPagerAdapter(getFragmentManager());
        mPager = (ViewPager) findViewById(R.id.container);
        mPager.setAdapter(mAdapter);
        mPager.setOnPageChangeListener(mAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mPresenter = new SyncPresenter(this);

        // Get the Create button Translation 56+16+8 dp
        mCreateTranslationY = (int) (getResources().getDisplayMetrics().density * 80);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initSyncItem() {
        MenuItem sync = mToolbar.getMenu().findItem(R.id.action_sync);

        View view = sync.getActionView();
        if (view != null) {
            ImageView shareImage = (ImageView) view.findViewById(R.id.iv_action_share);
            Loading loading = (Loading) view.findViewById(R.id.loading_action_share);
            if (shareImage != null && loading != null) {
                mLoading = loading;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.sync();
                    }
                });
            }
        }
    }

    private void initTitle() {
        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(getTitle()); // Bold this
        // Create the Typeface you want to apply to certain text
        try {
            CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(getAssets(), "fonts/Lobster.otf"));
            // Apply typeface to the Spannable 0 - 6 "Hello!" This can of course by dynamic.
            sBuilder.setSpan(typefaceSpan, 0, sBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mToolbar.setTitle(sBuilder);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            return true;
        } else if (id == R.id.action_search) {
            SearchKitActivity.show(this);
            return true;
        } else if (id == R.id.action_update) {
            Toast.makeText(this, R.string.toast_check_updating, Toast.LENGTH_SHORT).show();
            new ProductPresenter().update(this);
            return true;
        } else if (id == R.id.action_help) {
            HelpActivity.show(this);
            return true;
        } else if (id == R.id.action_about) {
            AboutActivity.show(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void syncStart() {
        mLoading.start();
    }

    @Override
    public void syncStop(@StringRes int statusRes) {
        mLoading.stop();

        Toast.makeText(this, statusRes, Toast.LENGTH_SHORT).show();
        switch (statusRes) {
            case R.string.status_account_login_need:
            case R.string.status_account_login_expire:
            case R.string.status_account_phone_unbind:
                AccountActivity.actionStart(this);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onClick(View v) {
        if (mPager.getCurrentItem() == 2)
            ContactAddActivity.actionStart(MainActivity.this);
        else
            RecordAddActivity.actionStart(MainActivity.this);
    }

    @Override
    public void showIsNew() {
        Toast.makeText(MainActivity.this, R.string.toast_check_update_new, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void showNewProduct(final ProductVersionModel model) {
        // 视图绑定
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams")
        View viewAddEmployee = layoutInflater.inflate(
                R.layout.item_alert_update, null);
        // 控件定义绑定
        final TextView update_time = (TextView) viewAddEmployee
                .findViewById(R.id.update_time);
        final TextView update_info = (TextView) viewAddEmployee
                .findViewById(R.id.update_info);
        // 控件数据初始化
        String text_reported = this.getResources().getString(R.string.txt_update_title);
        update_time.setText(String.format(text_reported, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(model.getPublished())));
        // Html
        update_info.setMovementMethod(LinkMovementMethod.getInstance());
        update_info.setText(Html.fromHtml(model.getContent()));

        showDialog(this, model.getVerName(), viewAddEmployee, "暂不更新", "立刻更新", null, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri appUri = Uri.parse(model.getAddress());
                Intent intent = new Intent(Intent.ACTION_VIEW, appUri);
                startActivity(intent);
                dialog.dismiss();
            }
        }).show();
    }


    class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        private RecordsFragment recordsFragment;
        private QuickFragment quickFragment;
        private ContactsFragment contactsFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            if (position == 1)
                return recordsFragment == null ? recordsFragment = new RecordsFragment() : recordsFragment;
            else if (position == 0)
                return quickFragment == null ? quickFragment = new QuickFragment() : quickFragment;
            else if (position == 2)
                return contactsFragment == null ? contactsFragment = new ContactsFragment() : contactsFragment;
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == 1)
                recordsFragment = null;
            else if (position == 0)
                quickFragment = null;
            else if (position == 2)
                contactsFragment = null;
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tY = 0;
            if (position == 0) {
                tY = (int) (mCreateTranslationY - mCreateTranslationY * positionOffset);
            }
            mCreate.setTranslationY(tY);
        }

        @Override
        public void onPageSelected(int position) {
            /*
            if (position == 0) {
                mCreate.animate()
                        .translationYBy(mCreate.getTranslationY())
                        .translationY(mCreateTranslationY)
                        .setDuration(320)
                        .start();
            } else {
                mCreate.animate()
                        .translationYBy(mCreate.getTranslationY())
                        .translationY(0)
                        .setDuration(320)
                        .start();
            }
            */
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
