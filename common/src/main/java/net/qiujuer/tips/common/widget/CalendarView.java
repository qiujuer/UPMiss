package net.qiujuer.tips.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.tips.common.R;

import java.lang.reflect.Field;


/**
 * mMinYear
 * mMaxYear
 * 设置年的范围 在1901~2098之间
 */
public class CalendarView extends LinearLayout implements NumberPicker.OnValueChangeListener {
    private int mMainColor = Resource.Color.CYAN;
    private int mMonthIndex, mDayIndex;
    private int mMinYear = 1901, mMaxYear = 2098, mYearIndex;
    private boolean isLunar;
    private DateSelectView mYearSelector;
    private DateSelectView mMonthSelector;
    private DateSelectView mDaySelector;
    private DateSelector mDateSelector;
    private OnValueChangeListener mOnValueChangeListener;
    private String[] mYearStringArray;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setOrientation(HORIZONTAL);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes);
        mMainColor = a.getColor(R.styleable.CalendarView_iMainColor, getResources().getColor(R.color.cyan_500));
        a.recycle();

        mDateSelector = new DateSunSelector();
        initYearStringArray();
        initChildView();
    }

    public void setOnNewValueListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public void setMinYear(int minYear) {
        if (mMinYear != minYear) {
            this.mMinYear = minYear;
            initSetValueYear();
        }
    }

    public void setMaxYear(int maxYear) {
        if (mMaxYear != maxYear) {
            this.mMaxYear = maxYear;
            initSetValueYear();
        }
    }

    private void initSetValueYear() {
        if (mMinYear == 0 || mMinYear < 1901)
            mMinYear = 1901;
        if (mMaxYear == 0 || mMaxYear > 2098)
            mMaxYear = 2098;
        if (mMinYear > mMaxYear) {
            mMinYear = 1901;
            mMaxYear = 2098;
        }
        initYearStringArray();
        initChildView();
    }

    private String[] initYearStringArray() {
        String yearStr = getResources().getString(R.string.calendar_year_str);
        int len = mMaxYear - mMinYear + 1;
        String[] strings = new String[len];
        for (int i = 0; i < len; i++) {
            strings[i] = ((i + mMinYear) + yearStr);
        }
        mYearStringArray = strings;
        return strings;
    }

    private void initChildView() {
        mYearSelector = getNumberPicker(mYearSelector,
                R.id.calendar_view_select_year_Picker,
                mYearStringArray,
                mYearIndex);

        mMonthSelector = getNumberPicker(mMonthSelector,
                R.id.calendar_view_select_month_Picker,
                mDateSelector.getMonthDisplayValue(mYearIndex),
                mMonthIndex);

        mDaySelector = getNumberPicker(mDaySelector,
                R.id.calendar_view_select_day_Picker,
                mDateSelector.getDayDisplayValue(),
                mDayIndex);

        upDateMonthSelector(mYearIndex);
        upDateDaySelector(mDateSelector.getDay(mYearIndex, mMonthIndex));
        notifyChange(mYearIndex, mMonthIndex, mDayIndex, isLunar);
    }

    private DateSelectView getNumberPicker(DateSelectView view, int id, String[] strings, int set) {
        // Create
        if (view == null) {
            view = new DateSelectView(getContext());
            view.setId(id);
            addView(view, new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        }
        // Set value
        if ((strings.length - 1) > view.getMaxValue()) {
            view.setDisplayedValues(strings);
            view.setMaxValue(strings.length - 1);
        } else {
            view.setMaxValue(strings.length - 1);
            view.setDisplayedValues(strings);
        }
        view.setValue(set);

        return view;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        int i = picker.getId();
        if (i == R.id.calendar_view_select_year_Picker) {
            int oldMonth = mDateSelector.getLeapMonth(oldVal);
            mYearIndex = newVal;
            int leap = mDateSelector.getLeapMonth(mYearIndex);
            if (leap > -1) {
                upDateMonthSelector(mYearIndex);
                if (oldMonth > 0 && leap == 0) {
                    if (mMonthSelector.getValue() > (oldMonth - 1)) {
                        mMonthSelector.setValue(mMonthIndex);
                    }
                }
            }
            upDateDaySelector(mDateSelector.getDay(mYearIndex, mMonthIndex));
        } else if (i == R.id.calendar_view_select_month_Picker) {
            int m = mDateSelector.getLeapMonth(mYearIndex);
            if (m > 0) {
                if (m > newVal) {
                    mMonthIndex = newVal;
                } else {
                    mMonthIndex = newVal - 1;
                }
            } else {
                mMonthIndex = newVal;
            }
            int dayLen = mDateSelector.getDay(mYearIndex, mMonthIndex);
            upDateDaySelector(dayLen);
        } else if (i == R.id.calendar_view_select_day_Picker) {
            mDayIndex = newVal;
        }
        notifyChange(mYearIndex, mMonthIndex, mDayIndex, isLunar);
    }

    private void notifyChange(int newYear, int newMonth, int newDay, boolean isLunar) {
        OnValueChangeListener listener = mOnValueChangeListener;
        if (listener != null) {
            listener.onValueChanged(newYear + mMinYear,
                    newMonth + 1, newDay + 1, isLunar);
        }
    }

    private void upDateDaySelector(int d) {
        if (mDaySelector.getValue() > (d - 1)) {
            mDayIndex = d - 1;
        }
        mDaySelector.setMaxValue(d - 1);
    }

    private void upDateMonthSelector(int y) {
        int m = mDateSelector.getLeapMonth(y);
        String[] strings = mDateSelector.getMonthDisplayValue(y);
        if (m > 0) {
            mMonthSelector.setDisplayedValues(strings);
            mMonthSelector.setMaxValue(12);
        } else {
            mMonthSelector.setMaxValue(11);
            mMonthSelector.setDisplayedValues(strings);
        }
        if ((m > 0) && (mMonthIndex > m - 2)) {
            mMonthSelector.setValue(mMonthIndex + 1);
        }
    }

    public boolean isLunar() {
        return isLunar;
    }

    public void setLunar(boolean isLunar) {
        if (this.isLunar != isLunar) {
            this.isLunar = isLunar;
            changeDateSelector();
            initChildView();
        }
    }

    private void changeDateSelector() {
        if (isLunar) {
            mDateSelector = new DateLunarSelector();
        } else {
            mDateSelector = new DateSunSelector();
        }
    }

    public void setDate(int year, int month, int day) {
        setDate(year, month, day, isLunar);
    }

    public void setDate(int year, int month, int day, boolean isLunar) {
        if (year < mMinYear)
            year = mMinYear;
        else if (year > mMaxYear)
            year = mMaxYear;
        mYearIndex = year - mMinYear;
        mMonthIndex = month - 1;
        mDayIndex = day - 1;

        if (this.isLunar != isLunar) {
            this.isLunar = isLunar;
            changeDateSelector();
        }
        initChildView();
    }

    public int getDay() {
        return mDayIndex + 1;
    }

    public int getMonth() {
        return mMonthIndex + 1;
    }

    public int getYear() {
        return mYearIndex + mMinYear;
    }

    public interface OnValueChangeListener {
        void onValueChanged(int newYear, int newMonth, int newDay, boolean isLunar);
    }

    private interface DateSelector {
        String[] getMonthDisplayValue(int y);

        String[] getDayDisplayValue();

        int getLeapMonth(int y);

        int getDay(int y, int m);
    }

    private class DateLunarSelector implements DateSelector {
        private String[] lunarDoubleMonthStr = new String[13];

        @Override
        public String[] getMonthDisplayValue(int y) {
            final int m = getLeapMonth(y);
            if (m == 0) {
                return LunarCalendar.getMonthStrs();
            } else {
                String[] src = LunarCalendar.getMonthStrs();
                System.arraycopy(src, 0, lunarDoubleMonthStr, 0, m);
                lunarDoubleMonthStr[m] = src[m - 1];
                if (m != 12)
                    System.arraycopy(src, m, lunarDoubleMonthStr, m + 1, src.length - m);
                return lunarDoubleMonthStr;
            }
        }

        @Override
        public String[] getDayDisplayValue() {
            return LunarCalendar.getDaysStrs();
        }

        @Override
        public int getLeapMonth(int y) {
            return LunarCalendar.leapMonth(y + mMinYear);
        }

        @Override
        public int getDay(int y, int m) {
            return LunarCalendar.daysInMonth(y + mMinYear, m + 1);
        }
    }

    private class DateSunSelector implements DateSelector {
        @Override
        public String[] getMonthDisplayValue(int y) {
            return getResources().getStringArray(R.array.calendar_Sun_month);
        }

        @Override
        public String[] getDayDisplayValue() {
            return getResources().getStringArray(R.array.calendar_sun_day);
        }

        @Override
        public int getLeapMonth(int y) {
            return -1;
        }

        @Override
        public int getDay(int y, int m) {
            int d;
            if (m == 1) {
                int realYear = y + mMinYear;
                if (realYear % 4 == 0 && realYear % 100 != 0 || realYear % 400 == 0) {
                    d = 29;
                } else {
                    d = 28;
                }
            } else if (m == 3 || m == 5 || m == 8 || m == 10) {
                d = 30;
            } else {
                d = 31;
            }
            return d;
        }
    }

    private class DateSelectView extends NumberPicker {

        public DateSelectView(Context context) {
            super(context);
            this.setMinValue(0);
            this.setOnValueChangedListener(CalendarView.this);
            this.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            this.setBackground(null);
            setDividerColor(this, mMainColor);
        }

        @Override
        public void addView(View child, int index, ViewGroup.LayoutParams params) {
            super.addView(child, index, params);
            updateView(child);
        }

        @Override
        public void addView(View child) {
            super.addView(child);
            updateView(child);
        }

        @Override
        public void addView(View child, ViewGroup.LayoutParams params) {
            super.addView(child, params);
            updateView(child);
        }

        private void updateView(View view) {
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(16);
                ((TextView) view).setTextColor(mMainColor);
            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static void setDividerColor(NumberPicker numberPicker, @ColorInt int colorRes) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值
                    pf.set(numberPicker, new ColorDrawable(colorRes));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}