package net.qiujuer.tips.factory.model.xml;


import android.content.SharedPreferences;

import net.qiujuer.tips.factory.util.TipsCalender;

public class UserModel extends XmlPreference {
    private long birthdayDate;
    private String name;
    private int sex;
    private int color;

    @Override
    protected void refresh(SharedPreferences sp) {
        birthdayDate = sp.getLong("birthdayDate", 201501010);
        name = sp.getString("name", "");
        sex = sp.getInt("sex", 1);
        color = sp.getInt("color", 0xffe51c23);
    }

    @Override
    protected String getPreferenceName() {
        return UserModel.class.getName();
    }

    @Override
    public void save() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();

        editor.putLong("birthdayDate", birthdayDate);
        editor.putString("name", name);
        editor.putInt("sex", sex);
        editor.putInt("color", color);

        editor.apply();
        editor.commit();
    }

    public TipsCalender getBirthday() {
        return new TipsCalender(birthdayDate);
    }

    public void setBirthday(TipsCalender birthday) {
        this.birthdayDate = birthday.toLong();
    }

    public String getName() {
        return name;
    }

    public int getSex() {
        return sex;
    }

    public int getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
