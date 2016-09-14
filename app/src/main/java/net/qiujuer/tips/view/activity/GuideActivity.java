package net.qiujuer.tips.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.qiujuer.tips.R;
import net.qiujuer.tips.view.fragment.GuideFragment;

public class GuideActivity extends BaseActivity {
    private ViewPager mPager;
    private View[] mViews;
    private View mLastView;
    private Button mButtonSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getFragmentManager());
        mPager = (ViewPager) findViewById(R.id.guide_container);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(adapter);

        ViewGroup guides = (ViewGroup) findViewById(R.id.guide_lay_circle_point_line);
        mViews = new View[guides.getChildCount()];
        for (int i = 0; i < mViews.length; i++) {
            mViews[i] = guides.getChildAt(i);
        }
        mLastView = mViews[0];

        mButtonSkip = (Button) findViewById(R.id.guide_btn_skip);
        mButtonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        GuideFragment[] mGuides;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mGuides = new GuideFragment[6];
            mGuides[0] = new GuideFragment(R.string.txt_guide_app_title,
                    R.mipmap.ic_guide_main, R.string.txt_guide_app_details);
            mGuides[1] = new GuideFragment(R.string.txt_guide_synchronization_title,
                    R.mipmap.ic_guide_synchronization, R.string.txt_guide_synchronization_details);
            mGuides[2] = new GuideFragment(R.string.txt_guide_timer_shaft_title,
                    R.mipmap.ic_guide_time_line, R.string.txt_guide_timer_shaft_details);
            mGuides[3] = new GuideFragment(R.string.txt_guide_gift_Shop_title,
                    R.mipmap.ic_guide_gift, R.string.txt_guide_gift_Shop_details);
            mGuides[4] = new GuideFragment(R.string.txt_guide_square_benediction_title,
                    R.mipmap.ic_guide_wish, R.string.txt_guide_square_benediction_details);
            mGuides[5] = new GuideFragment(R.string.txt_guide_alert_title,
                    R.mipmap.ic_guide_remind, R.string.txt_guide_alert_details);
        }

        @Override
        public Fragment getItem(int position) {
            return mGuides[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mLastView.setBackgroundResource(R.drawable.ic_guide_view_bg_fill);
            mViews[position].setBackgroundResource(R.drawable.ic_guide_view_bg_fill_all);
            mLastView = mViews[position];
            if (position == 5) {
                mButtonSkip.setVisibility(View.VISIBLE);
            } else {
                mButtonSkip.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
