package net.qiujuer.tips.factory.cache;

import net.qiujuer.tips.factory.service.bean.MissServiceBean;

public class CacheStaCount {
    public long birthdayCount = 0;
    public long memorialCount = 0;
    public long futureCount = 0;

    public void set(MissServiceBean bean) {
        birthdayCount = bean.getBirthdayCount();
        memorialCount = bean.getMemorialCount();
        futureCount = bean.getFutureCount();
    }
}
