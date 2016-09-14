package net.qiujuer.tips.factory.service.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by qiujuer
 * on 15/8/18.
 */
public class SyncChangeModel {
    private List<UUID> contactAdd;
    private List<UUID> contactEdit;

    private List<UUID> recordAdd;
    private List<UUID> recordEdit;
    private List<UUID> recordDelete;

    public SyncChangeModel() {
        contactAdd = new ArrayList<UUID>();
        contactEdit = new ArrayList<UUID>();
        recordAdd = new ArrayList<UUID>();
        recordEdit = new ArrayList<UUID>();
        recordDelete = new ArrayList<UUID>();
    }

    public List<UUID> getContactAdd() {
        return contactAdd;
    }

    public List<UUID> getContactEdit() {
        return contactEdit;
    }

    public List<UUID> getRecordAdd() {
        return recordAdd;
    }

    public List<UUID> getRecordDelete() {
        return recordDelete;
    }

    public List<UUID> getRecordEdit() {
        return recordEdit;
    }

    public void setContactAdd(List<UUID> contactAdd) {
        this.contactAdd = contactAdd;
    }

    public void setContactEdit(List<UUID> contactEdit) {
        this.contactEdit = contactEdit;
    }

    public void setRecordAdd(List<UUID> recordAdd) {
        this.recordAdd = recordAdd;
    }

    public void setRecordDelete(List<UUID> recordDelete) {
        this.recordDelete = recordDelete;
    }

    public void setRecordEdit(List<UUID> recordEdit) {
        this.recordEdit = recordEdit;
    }

    public void addContactAdd(UUID id) {
        contactAdd.add(id);
    }

    public void addContactEdit(UUID id) {
        contactEdit.add(id);
    }

    public void addRecordAdd(UUID id) {
        recordAdd.add(id);
    }

    public void addRecordEdit(UUID id) {
        recordEdit.add(id);
    }

    public void addRecordDelete(UUID id) {
        recordDelete.add(id);
    }
}
