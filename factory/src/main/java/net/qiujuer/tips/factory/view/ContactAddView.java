package net.qiujuer.tips.factory.view;

public interface ContactAddView {
    String getNameStr();

    String getPhoneNumber();

    String getQQ();

    void setRelation(int relation);

    int getGender();

    int getRelation();

    void setStatus(long status);
}
