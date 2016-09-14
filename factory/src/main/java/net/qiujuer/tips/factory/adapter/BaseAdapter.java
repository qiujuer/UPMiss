package net.qiujuer.tips.factory.adapter;


import java.util.List;

public interface BaseAdapter<T> {
    List<T> getDataSet();

    void setDataSet(List<T> dataSet);

    void notifyDataSetChanged();

    void setNull(boolean isNull);
}
