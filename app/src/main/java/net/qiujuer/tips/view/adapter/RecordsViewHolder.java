package net.qiujuer.tips.view.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.widget.TimeLineMarker;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.util.TipsCalender;

import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyTypefaceSpan;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public class RecordsViewHolder extends RecyclerView.ViewHolder {
    private TimeLineMarker mMarker;
    private ImageView mType;
    private TextView mBrief;
    private TextView mTime;
    private TextView mTimeOther;

    public RecordsViewHolder(View itemView, int type) {
        super(itemView);

        mMarker = (TimeLineMarker) itemView.findViewById(R.id.item_time_line_view_color);
        mType = (ImageView) itemView.findViewById(R.id.item_time_line_img_type);
        mBrief = (TextView) itemView.findViewById(R.id.item_time_line_txt_brief);
        mTime = (TextView) itemView.findViewById(R.id.item_time_line_txt_time);
        mTimeOther = (TextView) itemView.findViewById(R.id.item_time_line_txt_time_other);

        if (type == ItemType.ATOM) {
            mMarker.setBeginLine(null);
            mMarker.setEndLine(null);
        } else if (type == ItemType.START) {
            mMarker.setBeginLine(null);
        } else if (type == ItemType.END) {
            mMarker.setEndLine(null);
        }

    }

    public void setData(RecordViewModel data) {
        // Set ID
        itemView.setTag(data.getId());

        // Set Values
        int type = data.getType();
        if (type == 1) {
            mType.setImageResource(R.mipmap.ic_icon_birthday_min);
        } else if (type == 2) {
            mType.setImageResource(R.mipmap.ic_icon_memorial_min);
        } else {
            mType.setImageResource(R.mipmap.ic_icon_future_min);
        }
        mMarker.setMarkerDrawable(data.getColor());

        mBrief.setText(data.getBrief());
        //mTime.setText(String.valueOf(data.getDayNow()));
        setTimeDay(mTime, String.valueOf(data.getDayNow()));

        TipsCalender date = data.getDate();
        if (type == RecordModel.TYPE_FUTURE)
            mTimeOther.setText(date.toSunString());
        else
            mTimeOther.setText(date.toSunNowString());
    }

    public UUID getId() {
        return (UUID) itemView.getTag();
    }


    private void setTimeDay(TextView textView, String day) {
        final Resources resources = textView.getResources();
        final String suffix = resources.getString(R.string.txt_day);
        final int dayLen = day.length();

        SpannableStringBuilder sBuilder = new SpannableStringBuilder();
        sBuilder.append(day) // Bold this
                .append('\n') // Default TextView font.
                .append(suffix); // Default TextView font.

        // Create the Typeface you want to apply to certain text
        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(TypefaceUtils.load(resources.getAssets(), "fonts/Hero.otf"));
        sBuilder.setSpan(typefaceSpan, 0, dayLen, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Font
        sBuilder.setSpan(new AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.font_24)),
                0, dayLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Color
        sBuilder.setSpan(new ForegroundColorSpan(resources.getColor(R.color.cyan_700)),
                0, dayLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(sBuilder, TextView.BufferType.SPANNABLE);
    }
}
