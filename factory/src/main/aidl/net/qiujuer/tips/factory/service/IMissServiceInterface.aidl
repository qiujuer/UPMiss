package net.qiujuer.tips.factory.service;
import net.qiujuer.tips.factory.service.bean.MissServiceBean;


interface IMissServiceInterface {
    void order();
    void orderAsync();
    void destroy();

    void add(long id);
    void edit(long id);
    void delete(String mark, int type);

    void addContact(long id);
    void editContact(long id);
    void deleteContact(String mark);

    void sync();

    void refreshDesktop(int size);

    MissServiceBean getMissBean();
}
