package net.qiujuer.tips.factory.model.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.db.RecordModel;

import java.util.UUID;

public class NewestModel implements Parcelable {
    private UUID id = Model.EMPTY_ID;
    private String brief = "";
    private int color;

    public NewestModel() {

    }

    public NewestModel(RecordModel model) {
        brief = (model.getBrief());
        color = (model.getColor());
        id = (model.getMark());
    }


    protected NewestModel(Parcel in) {

        long mostSigBits = in.readLong();
        long leastSigBits = in.readLong();

        id = new UUID(mostSigBits, leastSigBits);

        brief = in.readString();
        color = in.readInt();
    }

    public static final Creator<NewestModel> CREATOR = new Creator<NewestModel>() {
        @Override
        public NewestModel createFromParcel(Parcel in) {
            return new NewestModel(in);
        }

        @Override
        public NewestModel[] newArray(int size) {
            return new NewestModel[size];
        }
    };

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public UUID getId() {
        return id;
    }

    public String getBrief() {
        return brief;
    }

    public int getColor() {
        return color;
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

        dest.writeString(brief);
        dest.writeInt(color);
    }
}
