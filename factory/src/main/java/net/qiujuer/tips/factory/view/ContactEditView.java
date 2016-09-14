package net.qiujuer.tips.factory.view;

import java.util.UUID;

/**
 * Created by Qiujuer on 2015/7/20.
 */
public interface ContactEditView extends ContactAddView {
    void setNameStr(String name);

    void setPhoneNumber(String phoneNumber);

    void setQQ(String qq);

    void setGender(int isMan);

    UUID getId();
}
