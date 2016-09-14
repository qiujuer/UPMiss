package net.qiujuer.tips.factory.model.adapter;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.db.ContactModel;

import java.util.UUID;

/**
 * Provide data for Adapter
 */
public class ContactViewModel implements Comparable<ContactViewModel>, Parcelable {
    private UUID id = Model.EMPTY_ID;
    private int color = Color.TRANSPARENT;
    private String name = "";
    private int relation = 1;

    public ContactViewModel() {

    }

    public void set(ContactModel model) {
        id = (model.getMark());
        name = (model.getName());
        relation = model.getRelation();
        color = getNameColor(name);
    }

    public UUID getId() {
        return id;
    }

    public int getColor() {
        return color;
    }


    public void setColor(int color) {
        this.color = color;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ContactViewModel another) {
        int lhs = this.relation;
        int rhs = another.relation;

        if (lhs == rhs) {
            return 0;
        } else if (lhs < rhs) {
            return -1;
        } else {
            return 1;
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

        dest.writeInt(relation);
        dest.writeString(name);
        dest.writeInt(color);
    }

    protected ContactViewModel(Parcel in) {
        long mostSigBits = in.readLong();
        long leastSigBits = in.readLong();

        id = new UUID(mostSigBits, leastSigBits);

        relation = in.readInt();
        name = in.readString();
        color = in.readInt();
    }

    public static final Creator<ContactViewModel> CREATOR = new Creator<ContactViewModel>() {
        @Override
        public ContactViewModel createFromParcel(Parcel in) {
            return new ContactViewModel(in);
        }

        @Override
        public ContactViewModel[] newArray(int size) {
            return new ContactViewModel[size];
        }
    };

    /**
     * 返回联系人姓名的图标背景色
     *
     * @param name 联系人的姓名
     */
    public static int getNameColor(String name) {
        int bgColor = Color.TRANSPARENT;
        int[] colorArray = Resource.Color.COLORS;
        int index = 0;
        if (!TextUtils.isEmpty(name))
            index = Math.abs(name.hashCode()) % (colorArray.length);
        if (index < colorArray.length)
            bgColor = colorArray[index];
        return bgColor;
    }
}
