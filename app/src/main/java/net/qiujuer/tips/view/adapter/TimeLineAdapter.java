package net.qiujuer.tips.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeLineAdapter extends RecyclerView.Adapter<RecordsViewHolder> implements net.qiujuer.tips.factory.adapter.BaseAdapter<RecordViewModel> {
    private List<RecordViewModel> mDataSet;
    private AdapterSelectCallback mCallback;

    @Override
    public RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_records_recycler, parent, false);
        RecordsViewHolder holder = new RecordsViewHolder(v, viewType);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = v.getTag();
                if (object != null) {
                    mCallback.onItemSelected((UUID) object);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecordsViewHolder holder, int position) {
        holder.setData(mDataSet.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        final int size = mDataSet.size() - 1;
        if (size == 0)
            return ItemType.ATOM;
        else if (position == 0)
            return ItemType.START;
        else if (position == size)
            return ItemType.END;
        else return ItemType.NORMAL;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public List<RecordViewModel> getDataSet() {
        return mDataSet;
    }

    @Override
    public void setDataSet(List<RecordViewModel> dataSet) {
        if (dataSet == null)
            dataSet = new ArrayList<RecordViewModel>();
        this.mDataSet = dataSet;
    }

    @Override
    public void setNull(boolean isNull) {

    }

    public void setCallback(AdapterSelectCallback callback) {
        mCallback = callback;
    }
}
