package net.qiujuer.tips.factory.view;

import android.support.annotation.StringRes;

import net.qiujuer.tips.factory.util.TipsCalender;

import java.util.UUID;

/**
 * Detail view Interface
 */
public interface RecordDetailView {
    UUID getId();

    void setType(int type);

    void setBrief(String brief);

    void setTime(TipsCalender date);

    void setColor(int color);

    void setStatus(@StringRes int statusId);

    String saveToBitmapFile();
}
