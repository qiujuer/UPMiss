package net.qiujuer.tips.factory.view;


import android.graphics.Bitmap;

import net.qiujuer.tips.factory.util.TipsCalender;

public interface UserView {
    int STATUS_OK = 0;
    int STATUS_ERROR_SYNC = -1;
    int STATUS_ERROR_NAME = -2;
    int STATUS_ERROR_ACCOUNT = -3;

    String getName();

    TipsCalender getBirthday();

    int getSex();

    int getColor();

    void setAccount(String account);

    void setName(String name);

    void setBirthday(TipsCalender date);

    void setSex(int sex);

    void setColor(int color);

    void setQRCode(Bitmap bitmap);

    void setStatus(long status);
}
