package net.qiujuer.tips.factory.view;

import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.util.TipsCalender;

public interface RecordAddView {
    int getType();

    String getBrief();

    TipsCalender getDate();

    int getColor();

    void setStatus(long status);

    ContactModel getContact();
}
