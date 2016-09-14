package net.qiujuer.tips.view.adapter;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.qiujuer.tips.R;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;
import net.qiujuer.tips.factory.presenter.RecordsPresenter;
import net.qiujuer.tips.factory.view.RecordsView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsViewHolder> implements RecordsView {
    private List<RecordViewModel> mDataSet;
    private AdapterSelectCallback mCallback;
    private RecordsPresenter mPresenter;
    private RecyclerView mRecycler;
    private View mNullView;

    public RecordsAdapter(RecyclerView view, View nullView, AdapterSelectCallback callback) {
        mDataSet = new ArrayList<RecordViewModel>();
        mRecycler = view;
        mNullView = nullView;
        mCallback = callback;
        mPresenter = new RecordsPresenter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(this);
    }

    public void destroy() {
        if (mPresenter != null) {
            mPresenter.destroy();
            mRecycler = null;
            mNullView = null;
            mCallback = null;
            mPresenter = null;
            mDataSet.clear();
        }
    }

    public void refresh() {
        if (mPresenter != null)
            mPresenter.refresh();
    }

    @Override
    public void setLoading(boolean isLoad) {
        AdapterSelectCallback callback = mCallback;
        if (callback != null) {
            callback.setLoading(isLoad);
        }
    }

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
                if (object != null && mCallback != null) {
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
        if (this.mDataSet != dataSet) {
            this.mDataSet.clear();
            if (dataSet != null)
                this.mDataSet.addAll(dataSet);
        }
    }

    @Override
    public void setNull(boolean isNull) {
        if (mRecycler != null && mNullView != null) {
            if (isNull) {
                mRecycler.setVisibility(View.INVISIBLE);
                mNullView.setVisibility(View.VISIBLE);
            } else {
                mNullView.setVisibility(View.INVISIBLE);
                mRecycler.setVisibility(View.VISIBLE);
            }
        }
    }
}
