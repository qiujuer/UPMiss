package net.qiujuer.tips.factory.view;

import java.util.UUID;

public interface ContactDetailView {
    void setNameStr(String name);

    void setPhoneNumber(String phoneNumber);

    void setQQ(String qq);

    void setSex(int isMan);

    void setRelation(int relation);

    UUID getId();

    void setDetailStatus(boolean status);

    void setColor(int color);
}
