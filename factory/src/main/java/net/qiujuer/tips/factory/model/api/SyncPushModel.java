package net.qiujuer.tips.factory.model.api;


import com.google.gson.annotations.SerializedName;

import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.util.http.HttpKit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyncPushModel extends AccessModel {
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

    public SyncPushModel() {
        super();
        recordAdd = new ArrayList<RecordTransModel>();
        recordEdit = new ArrayList<RecordTransModel>();
        recordDelete = new ArrayList<UUID>();
        contactAdd = new ArrayList<ContactTransModel>();
        contactEdit = new ArrayList<ContactTransModel>();
    }

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

    public JSONObject toJson() {
        try {
            return new JSONObject(HttpKit.getGsonBuilder().create().toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static SyncPushModel build() {
        SyncPushModel rsp = new SyncPushModel();
        boolean isNull = true;

        List<ContactModel> contactModels = ContactModel.getAllUnSync();
        if (contactModels != null && contactModels.size() > 0) {
            for (ContactModel model : contactModels) {
                ContactTransModel trans = new ContactTransModel(model);
                if (model.isEdit())
                    rsp.contactEdit.add(trans);
                else
                    rsp.contactAdd.add(trans);
            }
            isNull = false;
        }

        List<RecordModel> recordModels = RecordModel.getAllUnSync();
        if (recordModels != null && recordModels.size() > 0) {
            for (RecordModel model : recordModels) {
                if (model.isDelete())
                    rsp.recordDelete.add(model.getMark());
                else {
                    RecordTransModel trans = new RecordTransModel(model);
                    if (model.isEdit())
                        rsp.recordEdit.add(trans);
                    else
                        rsp.recordAdd.add(trans);
                }
            }
            isNull = false;
        }

        if (isNull)
            return null;
        else {
            return rsp;
        }
    }
}
