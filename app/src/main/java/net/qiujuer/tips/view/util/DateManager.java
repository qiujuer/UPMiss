package net.qiujuer.tips.view.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.widget.CalendarView;
import net.qiujuer.tips.factory.util.TipsCalender;

public class DateManager {
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean mIsLunar;
    private TipsCalender mDate;

    private TextView mTxtDate;
    private View mRoot;
    private CalendarView mCalendarView;

    public DateManager(LayoutInflater inflater, TipsCalender date) {
        mRoot = inflater.inflate(R.layout.dialog_date_select, null);
        mTxtDate = (TextView) mRoot.findViewById(R.id.dialog_date_select_txt_date);
        mCalendarView = (CalendarView) mRoot.findViewById(R.id.calendar_container);
        initDate(date);
        refreshTxtDate(date);
        mCalendarView.setDate(mYear, mMonth, mDay, mIsLunar);
        mCalendarView.setOnNewValueListener(new CalendarView.OnValueChangeListener() {
            @Override
            public void onValueChanged(int newYear, int newMonth, int newDay, boolean isLunar) {
                mYear = newYear;
                mMonth = newMonth;
                mDay = newDay;
                mIsLunar = isLunar;
                setDate();
                refreshTxtDate(mDate);
            }
        });
    }

    private void refreshTxtDate(TipsCalender date) {
        mTxtDate.setText(date.toDate());
    }

    private void initDate(TipsCalender date) {
        mYear = date.getYear();
        mMonth = date.getMonth();
        mDay = date.getDay();
        mIsLunar = date.getIsLunar();
        mDate = date;
    }

    public View getView() {
        return mRoot;
    }

    public TipsCalender getDate() {
        return mDate;
    }

    private void setDate() {
        mDate.setYear(mYear);
        mDate.setMonth(mMonth);
        mDate.setDay(mDay);
        mDate.setLunar(mIsLunar);
    }
}