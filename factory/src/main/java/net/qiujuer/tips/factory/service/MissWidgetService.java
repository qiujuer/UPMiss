package net.qiujuer.tips.factory.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.qiujuer.tips.factory.R;
import net.qiujuer.tips.factory.service.bean.MissWidgetListData;

import java.util.ArrayList;
import java.util.List;

public class MissWidgetService extends RemoteViewsService {
    private static String BUNDLE_STR;

    @Override
    public void onDestroy() {
        super.onDestroy();
        BUNDLE_STR = null;
    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            BUNDLE_STR = bundle.getString(MissService.ACTION_MISS_WIDGET_VALUES);
        else
            BUNDLE_STR = null;

        return super.onBind(intent);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MissWidgetFactory(getApplicationContext());
    }

    public class MissWidgetFactory implements RemoteViewsFactory {
        private Context mContext;
        private List<MissWidgetListData> mDataList = new ArrayList<MissWidgetListData>();

        public MissWidgetFactory(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position < 0 || position >= getCount()) {
                return null;
            }

            MissWidgetListData data = mDataList.get(position);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.layout_miss_widget_list_item);
            views.setInt(R.id.txt_color_circle, "setBackgroundColor", data.getColor());
            views.setTextViewText(R.id.txt_event_label, data.getTitle());
            views.setTextViewText(R.id.txt_event_distance_days, String.valueOf(data.getDay()));
            //views.setCharSequence(R.id.txt_event_distance_days, "setText", getDay(String.valueOf(data.getDay())));
            return views;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            mDataList.clear();

            if (BUNDLE_STR == null)
                return;

            java.lang.reflect.Type listType = new TypeToken<ArrayList<MissWidgetListData>>() {
            }.getType();
            List<MissWidgetListData> datas = new Gson().fromJson(BUNDLE_STR, listType);

            if (datas != null) {
                mDataList.addAll(datas);
            }
        }

        @Override
        public void onDestroy() {
            mDataList.clear();
            mDataList = null;
            mContext = null;
        }

        /*
        private CharSequence getDay(String day) {
            final Resources resources = getResources();
            final String suffix = resources.getString(R.string.miss_widget_list_item_day);
            final int dayLen = day.length();

            SpannableStringBuilder sBuilder = new SpannableStringBuilder();
            sBuilder.append(day)
                    .append('\n')
                    .append(suffix);

            // Font
            sBuilder.setSpan(new AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.font_20)),
                    0, dayLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Color
            sBuilder.setSpan(new ForegroundColorSpan(resources.getColor(R.color.cyan_700)),
                    0, dayLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return sBuilder;
        }
        */
    }
}