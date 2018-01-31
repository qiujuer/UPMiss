package net.qiujuer.tips.factory.service;

import net.qiujuer.tips.factory.service.bean.MissServiceBean;

/**
 * Create at 31/01/2018
 *
 * @author qiujuer Email: qiujuer@live.cn
 * @version 1.0.0
 */

public interface MissBinderInterface {
    void order();

    void orderAsync();

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
