package net.qiujuer.tips.view.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.widget.DragCircle;
import net.qiujuer.tips.common.widget.PieChart;
import net.qiujuer.tips.factory.cache.CacheStaCount;
import net.qiujuer.tips.factory.model.adapter.NewestModel;
import net.qiujuer.tips.factory.presenter.QuickPresenter;
import net.qiujuer.tips.factory.view.QuickView;
import net.qiujuer.tips.view.activity.MainActivity;
import net.qiujuer.tips.view.activity.RecordDetailActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickFragment extends Fragment implements View.OnClickListener, DragCircle.OnSelectListener, QuickView {
    private PieChart mBPieChart, mMPieChart, mFPieChart;
    private TextView mBText, mMText, mFText;
    private DragCircle mDragCircle;
    private QuickPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quick, container, false);

        initTop(view);

        mDragCircle = (DragCircle) view.findViewById(R.id.quick_drag);
        mDragCircle.setOnSelectListener(this);

        if (mPresenter != null)
            mPresenter.refresh();

        return view;
    }

    @SuppressLint("CutPasteId")
    private void initTop(View view) {
        // Find
        View layTop = view.findViewById(R.id.include_lay_birthday);
        mBPieChart = (PieChart) layTop.findViewById(R.id.item_sta_pie_chart);
        mBText = (TextView) layTop.findViewById(R.id.item_sta_txt);

        layTop = view.findViewById(R.id.include_lay_memorial);
        mMPieChart = (PieChart) layTop.findViewById(R.id.item_sta_pie_chart);
        mMText = (TextView) layTop.findViewById(R.id.item_sta_txt);

        layTop = view.findViewById(R.id.include_lay_future);
        mFPieChart = (PieChart) layTop.findViewById(R.id.item_sta_pie_chart);
        mFText = (TextView) layTop.findViewById(R.id.item_sta_txt);

        // Image
        mBPieChart.setImageResource(R.mipmap.ic_icon_birthday);
        mMPieChart.setImageResource(R.mipmap.ic_icon_memorial);
        mFPieChart.setImageResource(R.mipmap.ic_icon_future);

        //mBPieChart.setOnClickListener(this);
        //mMPieChart.setOnClickListener(this);
        //mFPieChart.setOnClickListener(this);
    }

    private CharSequence getTopText(long occupancy, final String suffix) {
        final String str = " " + String.valueOf(occupancy) + suffix;
        final int len = str.length();
        final int lenFx = len - suffix.length();
        final Resources resources = getResources();

        SpannableString span = new SpannableString(str);
        span.setSpan(new AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.font_20)),
                0, lenFx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(resources.getColor(R.color.white_alpha_224)),
                0, lenFx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mPresenter = new QuickPresenter(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.destroy();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSelect(int index) {
        if (mNewestCaches == null)
            return;
        NewestModel cache = mNewestCaches[index];
        if (cache != null) {
            MainActivity activity = (MainActivity) getActivity();
            RecordDetailActivity.actionStart(activity, cache.getId());
            activity.setBlur(activity);
        }
    }

    @Override
    public void setStatistics(CacheStaCount cache) {
        String[] types = getResources().getStringArray(R.array.array_quick_top_type);
        mBText.setText(getTopText(cache.birthdayCount, types[0]));
        mMText.setText(getTopText(cache.memorialCount, types[1]));
        mFText.setText(getTopText(cache.futureCount, types[2]));

        final float total = cache.birthdayCount + cache.memorialCount + cache.futureCount;
        if (total == 0) {
            mBPieChart.setPercent(0);
            mMPieChart.setPercent(0);
            mFPieChart.setPercent(0);
        } else {
            mBPieChart.setPercent(cache.birthdayCount / total);
            mMPieChart.setPercent(cache.memorialCount / total);
            mFPieChart.setPercent(cache.futureCount / total);
        }
    }

    @Override
    public void setNewest(List<NewestModel> caches) {
        DragCircle.CircleTag[] tags = new DragCircle.CircleTag[5];

        if (caches != null) {
            NewestModel[] newestCaches = new NewestModel[5];
            newestCaches = caches.toArray(newestCaches);

            mNewestCaches = newestCaches;

            final int defaultColor = getResources().getColor(R.color.black_alpha_16);

            for (int i = 0; i < mNewestCaches.length; i++) {
                NewestModel cache = mNewestCaches[i];
                int color = defaultColor;
                String str = "";
                if (cache != null) {
                    color = cache.getColor();
                    str = cache.getBrief();
                }
                tags[i] = new DragCircle.CircleTag(color, str);
            }

        }

        mDragCircle.setTag(tags);
    }

    private NewestModel[] mNewestCaches;
}
