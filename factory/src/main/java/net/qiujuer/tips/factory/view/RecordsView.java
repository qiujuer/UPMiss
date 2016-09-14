package net.qiujuer.tips.factory.view;

import net.qiujuer.tips.factory.adapter.BaseAdapter;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;


public interface RecordsView extends BaseAdapter<RecordViewModel> {
    void setLoading(boolean isLoad);
}
