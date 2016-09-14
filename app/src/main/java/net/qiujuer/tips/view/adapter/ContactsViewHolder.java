package net.qiujuer.tips.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.qiujuer.tips.R;
import net.qiujuer.tips.common.widget.TimeLineMarker;
import net.qiujuer.tips.factory.model.adapter.ContactViewModel;

import java.util.UUID;


public class ContactsViewHolder extends RecyclerView.ViewHolder {
    private TextView mName;
    private TimeLineMarker mMarker;

    public ContactsViewHolder(View itemView, int type) {
        super(itemView);
        mMarker = (TimeLineMarker) itemView.findViewById(R.id.item_time_line_view);
        mName = (TextView) itemView.findViewById(R.id.item_txt_name);

        if (type == ItemType.ATOM) {
            mMarker.setBeginLine(null);
            mMarker.setEndLine(null);
        } else if (type == ItemType.START) {
            mMarker.setBeginLine(null);
        } else if (type == ItemType.END) {
            mMarker.setEndLine(null);
        }

    }

    public void setData(ContactViewModel data) {
        itemView.setTag(data.getId());
        mName.setText(data.getName());
        mMarker.setMarkerDrawable(data.getColor());
    }

    public UUID getId() {
        return (UUID) itemView.getTag();
    }
}
