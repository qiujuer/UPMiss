package net.qiujuer.tips.factory.view;


import net.qiujuer.tips.factory.util.TipsCalender;

import java.util.UUID;

public interface RecordEditView extends RecordAddView {
    UUID getId();

    void setType(int type);

    void setBrief(String title);

    void setDate(TipsCalender date);

    void setColor(int color);

    void setContactName(String name);
}
