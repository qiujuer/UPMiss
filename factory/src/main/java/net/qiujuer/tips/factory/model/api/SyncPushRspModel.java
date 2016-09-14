package net.qiujuer.tips.factory.model.api;


import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SyncPushRspModel {
    @SerializedName("Records")
    public List<PushStateModel> records;
    @SerializedName("Contacts")
    public List<PushStateModel> contacts;
    @SerializedName("Last")
    public Date last;

    public List<PushStateModel> getRecords() {
        return records;
    }

    public void setRecords(List<PushStateModel> records) {
        this.records = records;
    }

    public List<PushStateModel> getContacts() {
        return contacts;
    }

    public void setContacts(List<PushStateModel> contacts) {
        this.contacts = contacts;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    public boolean isNull() {
        return (records == null || records.size() == 0)
                && (contacts == null || contacts.size() == 0);
    }

    public static Type getRspType() {
        return new TypeToken<RspModel<SyncPushRspModel>>() {
        }.getType();
    }

    public static class PushStateModel {
        public final static int TYPE_ADD = 1;
        public final static int TYPE_EDIT = 2;
        public final static int TYPE_DELETE = 4;

        @SerializedName("Id")
        private UUID id;
        @SerializedName("Last")
        private Date last;
        @SerializedName("Type")
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Date getLast() {
            return last;
        }

        public void setLast(Date last) {
            this.last = last;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }
    }
}
