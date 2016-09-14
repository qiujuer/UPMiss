package net.qiujuer.tips.factory.cache.notify;


import net.qiujuer.tips.factory.model.adapter.NewestModel;

import java.util.List;

public interface UpdateNewestNotify {
    void update(List<NewestModel> caches);
}
