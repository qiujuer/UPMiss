package net.qiujuer.tips.view.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.drawable.AnimJagDrawable;
import net.qiujuer.tips.common.widget.CalendarView;
import net.qiujuer.tips.factory.util.TipsCalender;

import java.util.Calendar;

public class SearchKitActivity extends BlurActivity implements View.OnClickListener {
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean mIsLunar;
    private TipsCalender mDate;

    private Button mButton;
    private TextView mLunarDate;
    private TextView mSunDate;
    private TextView mDistanceNow;
    private TextView mWeek;
    private TextView mConstellation;
    private TextView mZodiac;
    private View mSearchLayShow;
    private CalendarView mCalendarView;
    private String[] mWeeks;
    private String[] mZodiacs;
    private String[] mConstellations;
    private final int[] mConstellationInterval = new int[]{
            20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22
    };
    private AnimJagDrawable mTopBg;


    public static void show(BaseActivity activity) {
        Intent intent = new Intent(activity, SearchKitActivity.class);
        activity.startActivity(intent);
        activity.setBlur(activity);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search_kit;
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
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.menu_search_kit);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change) {
            mCalendarView.setLunar(!mCalendarView.isLunar());
            if (mCalendarView.isLunar()) {
                item.setIcon(R.drawable.ic_brightness_medium_black_24dp);
            } else {
                item.setIcon(R.drawable.ic_brightness_high_black_24dp);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * mWeeks 7
     * mConstellations 12star
     * mZodiacs 12
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onInit(Bundle savedInstanceState) {
        mWeeks = getResources().getStringArray(R.array.array_search_week);
        mConstellations = getResources().getStringArray(R.array.array_search_constellation);
        mZodiacs = getResources().getStringArray(R.array.array_search_zodiac);

        mCalendarView = (CalendarView) findViewById(R.id.search_kit_calendar_container);
        mLunarDate = (TextView) findViewById(R.id.search_txt_show_lunar);
        mSunDate = (TextView) findViewById(R.id.search_txt_show_sun);
        mDistanceNow = (TextView) findViewById(R.id.search_txt_show_distance_now);
        mWeek = (TextView) findViewById(R.id.search_txt_show_week);
        mConstellation = (TextView) findViewById(R.id.search_txt_show_constellation);
        mZodiac = (TextView) findViewById(R.id.search_txt_show_zodiac);

        mSearchLayShow = findViewById(R.id.search_lay_show);
        mButton = (Button) findViewById(R.id.search_btn_commit);

        View view = findViewById(R.id.lay_top);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        AnimJagDrawable drawable = new AnimJagDrawable();
        drawable.setFluCount(new Rect(0, 0, 0, 36));
        drawable.setColor(getResources().getColor(R.color.purple_500));
        drawable.setAlpha(164);
        view.setBackgroundDrawable(drawable);
        mTopBg = drawable;

        // onInitValues
        onInitValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTopBg != null) {
            mTopBg.startAnim();
            mTopBg = null;
        }
    }

    private void onInitValues() {
        mButton.setOnClickListener(this);
        mDate = TipsCalender.getNow();
        initMydDate(mDate.getYear(), mDate.getMonth(),
                mDate.getDay(), mDate.getIsLunar());
        mCalendarView.setDate(mYear, mMonth, mDay, mIsLunar);
        mCalendarView.setOnNewValueListener(new CalendarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int newYear, int newMonth, int newDay, boolean isLunar) {
                initMydDate(newYear, newMonth, newDay, isLunar);
            }
        });

    }

    private void initMydDate(int y, int m, int d, boolean isLunar) {
        mYear = y;
        mMonth = m;
        mDay = d;
        mIsLunar = isLunar;
    }

    private void setmDate(int y, int m, int d, boolean isLunar) {
        mDate.setYear(y);
        mDate.setMonth(m);
        mDate.setDay(d);
        mDate.setLunar(isLunar);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn_commit) {
            mSearchLayShow.setVisibility(View.VISIBLE);
            setmDate(mYear, mMonth, mDay, mIsLunar);
            refreshShow(mDate);
        }
    }

    private void refreshShow(TipsCalender date) {
        Calendar calendar = date.getCalender();

        String lunar = getResources().getString(R.string.txt_search_kit_str_lunar);
        lunar = String.format(lunar, date.toLunarString());
        mLunarDate.setText(lunar);

        String sun = getResources().getString(R.string.txt_search_kit_str_sun);
        sun = String.format(sun, date.toSunString());
        mSunDate.setText(sun);

        String distance = getResources().getString(R.string.txt_search_kit_str_distance);
        distance = String.format(distance, String.valueOf(date.distanceNow()));
        mDistanceNow.setText(distance);

        String week = getResources().getString(R.string.txt_search_kit_str_week);
        week = String.format(week, getWeek(calendar));
        mWeek.setText(week);

        String constellation = getResources().getString(R.string.txt_search_kit_str_constellation);
        constellation = String.format(constellation, getConstellation(calendar));
        mConstellation.setText(constellation);

        String zodiac = getResources().getString(R.string.txt_search_kit_str_zodiac);
        zodiac = String.format(zodiac, getZodiac(date));
        mZodiac.setText(zodiac);

    }

    private String getZodiac(TipsCalender date) {
        int year = date.transformLunaryear();
        return mZodiacs[year % 12];
    }

    private String getWeek(Calendar calendar) {
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return mWeeks[weekDay - 1];
    }


    private String getConstellation(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int index = month;

        if (day < mConstellationInterval[month]) {
            if (index == 0) {
                index = 11;
            } else {
                index = index - 1;
            }
        }
        return mConstellations[index];
    }
}
