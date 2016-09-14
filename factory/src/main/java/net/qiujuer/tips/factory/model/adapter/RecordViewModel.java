package net.qiujuer.tips.factory.model.adapter;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.util.TipsCalender;

import java.util.UUID;

/**
 * Provide data for Adapter
 */
public class RecordViewModel implements Comparable<RecordViewModel>, Parcelable {
    private UUID id = Model.EMPTY_ID;
    private int type = 1;
    private String brief = "";
    private int color = Color.TRANSPARENT;
    private TipsCalender date = new TipsCalender();
    private int dayNow = 0;

    public RecordViewModel() {

    }

    public void set(RecordModel model) {
        id = (model.getMark());
        color = (model.getColor());
        type = (model.getType());
        brief = (model.getBrief());
        date = (model.getDateCalender());
        // Day
        if (type == RecordModel.TYPE_FUTURE)
            this.dayNow = date.distanceNow();
        else
            this.dayNow = date.distanceNowSpecial();
    }

    public UUID getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TipsCalender getDate() {
        return date;
    }

    public void setDate(TipsCalender date) {
        this.date = date;
        //Day
        if (type == RecordModel.TYPE_FUTURE)
            this.dayNow = date.distanceNow();
        else
            this.dayNow = date.distanceNowSpecial();
    }

    public int getDayNow() {
        return dayNow;
    }

    @Override
    public int compareTo(RecordViewModel another) {
        int lhs = this.dayNow;
        int rhs = another.dayNow;

        if (lhs == rhs) {
            return 0;
        } else if (lhs < rhs) {
            if (lhs >= 0) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (rhs >= 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        long mostSigBits = id.getMostSignificantBits();
        long leastSigBits = id.getLeastSignificantBits();

        dest.writeLong(mostSigBits);
        dest.writeLong(leastSigBits);

        dest.writeInt(type);
        dest.writeString(brief);
        dest.writeInt(color);
        dest.writeParcelable(date, flags);
        dest.writeInt(dayNow);
    }

    protected RecordViewModel(Parcel in) {
        long mostSigBits = in.readLong();
        long leastSigBits = in.readLong();

        id = new UUID(mostSigBits, leastSigBits);

        type = in.readInt();
        brief = in.readString();
        color = in.readInt();
        date = in.readParcelable(TipsCalender.class.getClassLoader());
        dayNow = in.readInt();
    }

    public static final Creator<RecordViewModel> CREATOR = new Creator<RecordViewModel>() {
        @Override
        public RecordViewModel createFromParcel(Parcel in) {
            return new RecordViewModel(in);
        }

        @Override
        public RecordViewModel[] newArray(int size) {
            return new RecordViewModel[size];
        }
    };
}
