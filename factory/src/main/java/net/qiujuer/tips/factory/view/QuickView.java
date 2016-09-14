package net.qiujuer.tips.factory.view;


import net.qiujuer.tips.factory.cache.CacheStaCount;
import net.qiujuer.tips.factory.model.adapter.NewestModel;

import java.util.List;

public interface QuickView {
    void setStatistics(CacheStaCount cache);

    void setNewest(List<NewestModel> caches);
}
