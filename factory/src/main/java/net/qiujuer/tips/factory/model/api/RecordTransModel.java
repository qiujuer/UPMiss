package net.qiujuer.tips.factory.model.api;


import net.qiujuer.tips.factory.model.db.ContactModel;
import net.qiujuer.tips.factory.model.db.RecordModel;

import java.util.Date;
import java.util.UUID;

public class RecordTransModel {
    public RecordTransModel() {
    }

    public RecordTransModel(RecordModel model) {
        this.Id = model.getMark();
        this.Brief = model.getBrief();
        this.Type = model.getType();
        this.Color = model.getColor();
        this.Date = model.getDate();
        this.Create = model.getCreate();
        this.Last = model.getLast();
        ContactModel contactModel = model.getContact();
        if (contactModel != null) {
            this.Contact = contactModel.getMark();
        }
    }

    public RecordModel create() {
        RecordModel model = new RecordModel();
        model.setMark(this.Id);
        model.setBrief(this.Brief);
        model.setType(this.Type);
        model.setColor(this.Color);
        model.setDate(this.Date);
        model.setCreate(this.Create);
        model.setStatus(RecordModel.STATUS_UPLOADED);
        model.setLast(this.Last);
        if (Contact != null) {
            model.setContact(ContactModel.get(this.Contact));
        }
        return model;
    }

    public void edit(RecordModel model) {
        if (model.isDelete() || model.isEdit())
            return;
        model.setBrief(this.Brief);
        model.setType(this.Type);
        model.setColor(this.Color);
        model.setDate(this.Date);
        model.setCreate(this.Create);
        model.setStatus(RecordModel.STATUS_UPLOADED);
        model.setLast(this.Last);
        if (this.Contact == null) {
            model.setContact(null);
        } else {
            model.setContact(ContactModel.get(this.Contact));
        }
    }

    public UUID Id;
    public UUID Contact;
    public String Brief;
    public int Type;
    public int Color;
    public long Date;
    public Date Create;
    public Date Last;

    public void setId(UUID id) {
        Id = id;
    }

    public void setContact(UUID id) {
        Contact = id;
    }

    public void setBrief(String brief) {
        Brief = brief;
    }

    public void setType(int type) {
        Type = type;
    }

    public void setColor(int color) {
        Color = color;
    }

    public void setDate(long date) {
        Date = date;
    }

    public void setCreate(java.util.Date create) {
        Create = create;
    }

    public void setLast(java.util.Date last) {
        Last = last;
    }

    public UUID getId() {
        return Id;
    }

    public UUID getContact() {
        return Contact;
    }

    public String getBrief() {
        return Brief;
    }

    public int getType() {
        return Type;
    }

    public int getColor() {
        return Color;
    }

    public long getDate() {
        return Date;
    }

    public java.util.Date getCreate() {
        return Create;
    }

    public java.util.Date getLast() {
        return Last;
    }
}
