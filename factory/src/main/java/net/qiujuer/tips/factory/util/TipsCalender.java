package net.qiujuer.tips.factory.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Is a Calender Model
 */
public class TipsCalender implements Parcelable {
    private final static String[] NUM_ZH = {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
    private final static String FORMAT_Y_M_D = "%d年%d月%d日";
    private final static String FORMAT_Y_M = "%d年%d月";
    private final static long EMPTY_DATE = 201501010;

    private long date = EMPTY_DATE;

    private transient int year;
    private transient int month;
    private transient int day;
    private transient boolean isLunar;

    public boolean getIsLunar() {
        return isLunar;
    }

    private transient Calendar mCalendar, mNextCalendar;

    public TipsCalender() {
        this(EMPTY_DATE);
    }

    public TipsCalender(long date) {
        set(date);
    }

    public TipsCalender(String date) {
        set(date);
    }

    public void set(long date) {
        if (date < 100001010 || date > 999912311)
            return;

        this.date = date;

        makeOther();

        // Calender
        initCalender();
    }

    public void set(String date) {
        if (date == null || date.length() != 9)
            return;

        this.date = Long.parseLong(date);

        makeOther();

        // Calender
        initCalender();
    }

    private void makeOther() {
        long d = this.date;

        this.year = (int) (d / 100000);
        d = d % 100000;

        this.month = (int) (d / 1000);
        d = d % 1000;

        this.day = (int) (d / 10);
        d = d % 10;

        this.isLunar = d != 0;
    }

    private void makeOther(String str) {
        this.year = Integer.parseInt(str.substring(0, 4));
        this.month = Integer.parseInt(str.substring(4, 6));
        this.day = Integer.parseInt(str.substring(6, 8));
        this.isLunar = str.substring(8).equals("1");
    }

    private void makeDate() {
        long date = this.year * 100000;
        date += this.month * 1000;
        date += this.day * 10;
        if (this.isLunar)
            date += 1;
        this.date = date;
    }

    public void setYear(int year) {
        if (this.year == year)
            return;

        this.year = year;

        // Make
        makeDate();

        // Calender
        initCalender();
    }

    public void setMonth(int month) {
        if (this.month == month)
            return;

        this.month = month;

        // Make
        makeDate();

        // Calender
        initCalender();
    }

    public void setDay(int day) {
        if (this.day == day)
            return;

        this.day = day;

        // Make
        makeDate();

        // Calender
        initCalender();
    }

    public void setLunar(boolean isLunar) {
        if (this.isLunar == isLunar)
            return;

        this.isLunar = isLunar;

        // Make
        makeDate();

        // Calender
        initCalender();
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    private void initCalender() {
        // Calender
        setCalendar(mCalendar);
        setNextCalendar(mNextCalendar);
    }


    private synchronized Calendar setCalendar(Calendar calendar) {
        if (calendar == null)
            return null;
        int y;
        int m;
        int d;
        if (isLunar) {
            int[] date = LunarCalendar.lunarToSolar(year, month, day, false);
            y = date[0];
            m = date[1] - 1;
            d = date[2];
        } else {
            y = year;
            m = month - 1;
            d = day;
        }
        // 设置
        calendar.set(y, m, d);
        return calendar;
    }


    private synchronized Calendar setNextCalendar(Calendar calendar) {
        if (calendar == null)
            return null;
        long formDate, toDate;

        Calendar formCalendar = getNowClearCalender();
        formDate = formCalendar.getTimeInMillis();
        int nowYear = formCalendar.get(Calendar.YEAR);
        int d = coercionDay(nowYear, month, day, isLunar);
        if (isLunar) {
            int[] sun = LunarCalendar.lunarToSolar(nowYear, month, d, false);
            calendar.set(sun[0], sun[1] - 1, sun[2], 0, 0, 0);
            toDate = calendar.getTimeInMillis();
            if (toDate < formDate) {
                d = coercionDay(nowYear + 1, month, day, isLunar);
                sun = LunarCalendar.lunarToSolar(nowYear + 1, month, d, false);
                calendar.set(sun[0], sun[1] - 1, sun[2], 0, 0, 0);
            }
        } else {
            calendar.set(nowYear, month - 1, d, 0, 0, 0);
            toDate = calendar.getTimeInMillis();
            if (toDate < formDate) {
                d = coercionDay(nowYear + 1, month, day, isLunar);
                calendar.set(nowYear + 1, month - 1, d, 0, 0, 0);
            }
        }
        return calendar;
    }

    /**
     * 转换成阳历，并设置为Calendar类型
     *
     * @return Calendar
     */
    private synchronized Calendar getCalenderPrivate() {
        if (mCalendar == null) {
            Calendar calendar = getNowClearCalender();
            mCalendar = setCalendar(calendar);
        }
        return mCalendar;
    }

    public Calendar getCalender() {
        return (Calendar) getCalenderPrivate().clone();
    }

    /**
     * 获取离当前日期最近的一个Calender
     * 阳历
     *
     * @return Calendar
     */
    public synchronized Calendar getNextCalenderPrivate() {
        if (mNextCalendar == null) {
            Calendar calendar = getNowClearCalender();
            mNextCalendar = setNextCalendar(calendar);
        }
        return mNextCalendar;
    }

    public Calendar getNextCalender() {
        return (Calendar) getNextCalenderPrivate().clone();
    }


    public long toLong() {
        return date;
    }

    @Override
    public String toString() {
        return String.valueOf(date);
    }

    public String toDate() {
        return String.format("%04d", year) +
                "-" +
                String.format("%02d", month) +
                "-" +
                String.format("%02d", day);
    }


    public String toSunString() {
        if (isLunar) {
            int[] sunDate = LunarCalendar.lunarToSolar(year, month, day, false);
            return String.format(FORMAT_Y_M_D, sunDate[0], sunDate[1], sunDate[2]);
        } else {
            return String.format(FORMAT_Y_M_D, year, month, day);
        }
    }

    public String toSunNowString() {
        Calendar calendar = getNextCalenderPrivate();
        return String.format(FORMAT_Y_M_D, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String toLunarString() {
        if (isLunar) {
            return numToZHString(year) + LunarCalendar.numToChineseMonth(month) +
                    LunarCalendar.numToChineseDay(day);
        } else {
            int[] lunar = LunarCalendar.solarToLunar(year, month, day);
            return numToZHString(lunar[0]) + LunarCalendar.numToChineseMonth(lunar[1]) +
                    LunarCalendar.numToChineseDay(lunar[2]);
        }
    }

    /**
     * 记录的x月x日，在距离 最近的的阴历
     *
     * @return String
     */
    public String toNextLunarString() {
        Calendar calendar = getNextCalenderPrivate();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int[] lunar = LunarCalendar.solarToLunar(y, m, d);
        return numToZHString(lunar[0]) + LunarCalendar.numToChineseMonth(lunar[1]) + LunarCalendar.numToChineseDay(lunar[2]);
    }

    /**
     * 记录的年月日，在距离最近的下一个的年月的阳历
     *
     * @return String
     */
    public String toNextSunYMString() {
        Calendar calendar = getNextCalenderPrivate();
        return String.format(FORMAT_Y_M, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 记录的年月
     */
    public String toSunYMString() {
        if (isLunar) {
            int[] sunDate = LunarCalendar.lunarToSolar(year, month, day, false);
            return String.format(FORMAT_Y_M, sunDate[0], sunDate[1]);
        } else {
            return String.format(FORMAT_Y_M, year, month);
        }
    }


    /**
     * 下一个月日隔的天数
     *
     * @return int
     */
    public int distanceNowSpecial() {
        long formDate, toDate;
        Calendar toCalendar = getNextCalenderPrivate();
        Calendar formCalendar = getNowClearCalender();
        formDate = formCalendar.getTimeInMillis();
        toDate = toCalendar.getTimeInMillis();
        return Integer.parseInt(Long.toString((toDate - formDate) / 1000 / 60 / 60 / 24));
    }

    /**
     * 设定时间距离现在的时间差
     * ”+“标识还未发生
     * ”-“标识发生过了
     *
     * @return Int
     */
    public int distanceNow() {
        int distanceDays;
        long formDate, toDate;
        Calendar toCalendar = getCalenderPrivate();
        Calendar formCalendar = getNowClearCalender();
        toDate = toCalendar.getTimeInMillis();
        formDate = formCalendar.getTimeInMillis();
        distanceDays = Integer.parseInt(Long.toString((toDate - formDate) / 1000 / 60 / 60 / 24));
        return distanceDays;
    }

    /**
     * 获取当前日期
     *
     * @return Calender
     */
    private static Calendar getNowClearCalender() {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.clear();
        calendar.set(y, m, d);
        return calendar;
    }

    /**
     * 转换阴阳
     */
    public static int[] transform(int y, int m, int d, boolean isLunar) {
        int[] date;
        if (isLunar)//sun->lunar
        {
            date = LunarCalendar.solarToLunar(y, m, d);
        } else {
            date = LunarCalendar.lunarToSolar(y, m, d, false);
        }
        date[3] = isLunar ? 1 : 0;
        return date;
    }

    /**
     * 转换阴历输出年
     */
    public int transformLunaryear() {
        int[] date;
        if (isLunar) //is lunar
        {
            return year;
        } else {
            date = LunarCalendar.solarToLunar(year, month, day);
            return date[0];
        }
    }

    /**
     * 查看某年某月天数是否大于了该月的最大天数
     *
     * @return int
     */
    public static int coercionDay(int y, int m, int d, boolean isLunar) {
        int days;
        // sun->lunar
        if (isLunar) {
            days = LunarCalendar.daysInMonth(y, m);
        } else {
            Calendar sunCalendar = getNowClearCalender();
            sunCalendar.set(y, m - 1, 1);
            sunCalendar.roll(Calendar.DATE, -1);
            days = sunCalendar.get(Calendar.DATE);
        }
        if (d > days) {
            return days;
        }
        return d;
    }

    public static String numToZHString(int year) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1000; i > 0; i = i / 10) {
            sb.append(NUM_ZH[year / i]);
            year = year % i;
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date);
    }

    protected TipsCalender(Parcel in) {
        long date = in.readLong();
        set(date);
    }

    public static final Creator<TipsCalender> CREATOR = new Creator<TipsCalender>() {
        @Override
        public TipsCalender createFromParcel(Parcel in) {
            return new TipsCalender(in);
        }

        @Override
        public TipsCalender[] newArray(int size) {
            return new TipsCalender[size];
        }
    };

    public static TipsCalender getNow() {
        TipsCalender calender = new TipsCalender();
        Calendar calendar = Calendar.getInstance();
        calender.year = calendar.get(Calendar.YEAR);
        calender.month = calendar.get(Calendar.MONTH);
        calender.day = calendar.get(Calendar.DAY_OF_MONTH);
        // Make
        calender.makeDate();
        // Calender
        calender.initCalender();
        return calender;
    }
}
