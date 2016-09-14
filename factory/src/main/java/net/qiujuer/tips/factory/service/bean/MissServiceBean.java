package net.qiujuer.tips.factory.service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import net.qiujuer.tips.common.utils.FixedList;
import net.qiujuer.tips.factory.model.adapter.ContactViewModel;
import net.qiujuer.tips.factory.model.adapter.NewestModel;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;
import net.qiujuer.tips.factory.model.db.RecordModel;

import java.util.ArrayList;
import java.util.List;


public class MissServiceBean implements Parcelable {
    private List<RecordViewModel> timeLine;
    private List<ContactViewModel> contacts;
    private FixedList<NewestModel> newest;
    private long birthdayCount = 0;
    private long memorialCount = 0;
    private long futureCount = 0;

    public MissServiceBean() {
        timeLine = new ArrayList<RecordViewModel>();
        contacts = new ArrayList<ContactViewModel>();
        newest = new FixedList<NewestModel>(5);
    }

    protected MissServiceBean(Parcel in) {
        timeLine = in.createTypedArrayList(RecordViewModel.CREATOR);
        contacts = in.createTypedArrayList(ContactViewModel.CREATOR);

        newest = new FixedList<NewestModel>(5);
        List<NewestModel> models = in.createTypedArrayList(NewestModel.CREATOR);
        newest.addAll(models);

        birthdayCount = in.readLong();
        memorialCount = in.readLong();
        futureCount = in.readLong();
    }

    public static final Creator<MissServiceBean> CREATOR = new Creator<MissServiceBean>() {
        @Override
        public MissServiceBean createFromParcel(Parcel in) {
            return new MissServiceBean(in);
        }

        @Override
        public MissServiceBean[] newArray(int size) {
            return new MissServiceBean[size];
        }
    };

    public void setTimeLine(List<RecordViewModel> timeLine) {
        this.timeLine = timeLine;
    }

    public void setContacts(List<ContactViewModel> models) {
        this.contacts = models;
    }

    public void setNewest(List<NewestModel> newest) {
        this.newest.clear();
        this.newest.addAll(newest);
    }

    public void setBirthdayCount(long birthdayCount) {
        this.birthdayCount = birthdayCount;
    }

    public void setMemorialCount(long memorialCount) {
        this.memorialCount = memorialCount;
    }

    public void setFutureCount(long futureCount) {
        this.futureCount = futureCount;
    }

    public List<RecordViewModel> getTimeLine() {
        return timeLine;
    }

    public List<ContactViewModel> getContacts() {
        return contacts;
    }

    public FixedList<NewestModel> getNewest() {
        return newest;
    }

    public long getBirthdayCount() {
        return birthdayCount;
    }

    public long getMemorialCount() {
        return memorialCount;
    }

    public long getFutureCount() {
        return futureCount;
    }

    public void add(int type) {
        if (type == RecordModel.TYPE_BIRTHDAY) {
            birthdayCount++;
        } else if (type == RecordModel.TYPE_MEMORIAL) {
            memorialCount++;
        } else {
            futureCount++;
        }
    }

    public void delete(int type) {
        if (type == RecordModel.TYPE_BIRTHDAY) {
            birthdayCount--;
        } else if (type == RecordModel.TYPE_MEMORIAL) {
            memorialCount--;
        } else {
            futureCount--;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(timeLine);
        dest.writeTypedList(contacts);
        dest.writeTypedList(newest);
        dest.writeLong(birthdayCount);
        dest.writeLong(memorialCount);
        dest.writeLong(futureCount);
    }

    @Override
    public String toString() {
        return "MissServiceBean{" +
                "birthdayCount=" + birthdayCount +
                ", memorialCount=" + memorialCount +
                ", futureCount=" + futureCount +
                '}';
    }
}
