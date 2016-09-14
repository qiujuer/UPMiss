package net.qiujuer.tips.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.qiujuer.tips.R;
import net.qiujuer.tips.view.activity.MainActivity;
import net.qiujuer.tips.view.activity.RecordDetailActivity;
import net.qiujuer.tips.view.adapter.AdapterSelectCallback;
import net.qiujuer.tips.view.adapter.RecordsAdapter;

import java.util.UUID;


public class RecordsFragment extends Fragment implements AdapterSelectCallback {
    private RecordsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_records, container, false);

        RecyclerView mRecycler = (RecyclerView) view.findViewById(R.id.time_line_recycler);
        TextView mStatus = (TextView) view.findViewById(R.id.text_status);

        mAdapter = new RecordsAdapter(mRecycler, mStatus, this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.refresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter.destroy();
    }

    @Override
    public void onItemSelected(UUID id) {
        MainActivity activity = (MainActivity) getActivity();

        RecordDetailActivity.actionStart(activity, id);

        activity.setBlur(activity);
    }

    @Override
    public void setLoading(boolean isLoad) {

    }
}
