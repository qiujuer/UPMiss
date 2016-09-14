package net.qiujuer.tips.factory.model.api;


import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SyncPullRspModel {
    @SerializedName("RecordAdd")
    private List<RecordTransModel> recordAdd;

    @SerializedName("RecordEdit")
    private List<RecordTransModel> recordEdit;

    @SerializedName("RecordDelete")
    private List<UUID> recordDelete;

    @SerializedName("ContactAdd")
    private List<ContactTransModel> contactAdd;

    @SerializedName("ContactEdit")
    private List<ContactTransModel> contactEdit;

    @SerializedName("Last")
    private Date last;

    public List<RecordTransModel> getRecordAdd() {
        return recordAdd;
    }

    public void setRecordAdd(List<RecordTransModel> recordAdd) {
        this.recordAdd = recordAdd;
    }

    public List<RecordTransModel> getRecordEdit() {
        return recordEdit;
    }

    public void setRecordEdit(List<RecordTransModel> recordEdit) {
        this.recordEdit = recordEdit;
    }

    public List<UUID> getRecordDelete() {
        return recordDelete;
    }

    public void setRecordDelete(List<UUID> recordDelete) {
        this.recordDelete = recordDelete;
    }

    public List<ContactTransModel> getContactAdd() {
        return contactAdd;
    }

    public void setContactAdd(List<ContactTransModel> contactAdd) {
        this.contactAdd = contactAdd;
    }

    public List<ContactTransModel> getContactEdit() {
        return contactEdit;
    }

    public void setContactEdit(List<ContactTransModel> contactEdit) {
        this.contactEdit = contactEdit;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    public boolean isNull() {
        return recordAdd.size() == 0
                && recordEdit.size() == 0
                && recordDelete.size() == 0
                && contactAdd.size() == 0
                && contactEdit.size() == 0;
    }

    public static Type getRspType() {
        return new TypeToken<RspModel<SyncPullRspModel>>() {
        }.getType();
    }
}
