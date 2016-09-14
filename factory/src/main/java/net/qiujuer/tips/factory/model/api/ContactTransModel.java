package net.qiujuer.tips.factory.model.api;


import net.qiujuer.tips.factory.model.db.ContactModel;

import java.util.Date;
import java.util.UUID;

public class ContactTransModel {
    public ContactTransModel() {
    }

    public ContactTransModel(ContactModel model) {
        this.Id = model.getMark();
        this.Name = model.getName();
        this.Phone = model.getPhone();
        this.QQ = model.getQQNumber();
        this.Sex = model.getSex();
        this.Relation = model.getRelation();
        this.Create = model.getCreate();
        this.Last = model.getLast();
    }

    public ContactModel create() {
        ContactModel model = new ContactModel();
        model.setMark(this.Id);
        model.setName(this.Name);
        model.setPhone(this.Phone);
        model.setQQNumber(this.QQ);
        model.setSex(this.Sex);
        model.setRelation(this.Relation);
        model.setCreate(this.Create);
        model.setStatus(ContactModel.STATUS_UPLOADED);
        model.setLast(this.Last);
        return model;
    }

    public void edit(ContactModel model) {
        if (model.isDelete() || model.isEdit())
            return;
        model.setName(this.Name);
        model.setPhone(this.Phone);
        model.setQQNumber(this.QQ);
        model.setSex(this.Sex);
        model.setRelation(this.Relation);
        model.setCreate(this.Create);
        model.setStatus(ContactModel.STATUS_UPLOADED);
    }

    public UUID Id;
    public String Name;
    public String Phone;
    public String QQ;
    public int Sex;
    public int Relation;
    public Date Create;
    public Date Last;

    public void setId(UUID id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public void setRelation(int relation) {
        Relation = relation;
    }

    public void setCreate(Date create) {
        Create = create;
    }

    public void setLast(Date last) {
        Last = last;
    }

    public UUID getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getQQ() {
        return QQ;
    }

    public int getSex() {
        return Sex;
    }

    public int getRelation() {
        return Relation;
    }

    public Date getCreate() {
        return Create;
    }

    public Date getLast() {
        return Last;
    }
}
