/**
 * CREATE TABLE Contact
 * (
 * Id INTEGER PRIMARY KEY AUTOINCREMENT,
 * QQ TEXT,
 * Gender INTEGER,
 * Mark TEXT,
 * Name TEXT,
 * Phone TEXT,
 * Relation INTEGER,
 * Status INTEGER,
 * Changed INTEGER
 * )
 */
package net.qiujuer.tips.factory.model.db;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.qiujuer.tips.factory.model.adapter.ContactViewModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@ModelContainer
@Table(database = AppDatabase.class, name = "Contact")
public class ContactModel extends BaseModel implements ModelStatus {
    public final static int GENDER_MAN = 1;
    public final static int GENDER_WOMAN = 0;

    public ContactModel() {
        super();
        changedTime = System.currentTimeMillis();
        create = new Date(changedTime);
        last = create;
        status = STATUS_ADD;
    }

    @PrimaryKey(autoincrement = true)
    @Column(name = "Id")
    @Expose
    private long id;

    /**
     * 标识
     */
    @SerializedName("Id")
    @Column(name = "Mark", typeConverter = UUIDConverter.class)
    private UUID mark;

    @SerializedName("Name")
    @Column(name = "Name")
    private String name;

    @SerializedName("Phone")
    @Column(name = "Phone")
    private String phone;

    @SerializedName("QQ")
    @Column(name = "QQ")
    private String QQNumber;

    @SerializedName("Create")
    @Column(name = "Created")
    private Date create;

    @SerializedName("Last")
    @Column(name = "Last")
    private Date last;

    @SerializedName("Sex")
    @Column(name = "Sex")
    private int sex;

    @SerializedName("Relation")
    @Column(name = "Relation")
    private int relation;

    @Expose
    @Column(name = "Status")
    private int status;

    @Expose
    @Column(name = "ChangedTime")
    private long changedTime;

    @Expose
    List<RecordModel> recordModels;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreate() {
        return create;
    }

    public Date getLast() {
        return last;
    }

    public UUID getMark() {
        return mark;
    }

    public void setStatus(int status) {
        this.status = status;
        this.changedTime = System.currentTimeMillis();
    }

    public int getStatus() {
        return status;
    }

    public boolean isDelete() {
        return status == STATUS_DELETE;
    }

    public boolean isEdit() {
        return status == STATUS_EDIT;
    }

    public boolean isAdd() {
        return status == STATUS_ADD;
    }

    public boolean isSynced() {
        return (status == STATUS_UPLOADED);
    }

    public String getName() {
        return name;
    }

    public void setMark(UUID id) {
        this.mark = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public void setLast(Date last) {
        this.last = last;
        this.changedTime = last.getTime();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQQNumber() {
        return QQNumber;
    }

    public void setQQNumber(String QQNumber) {
        this.QQNumber = QQNumber;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getColor() {
        return ContactViewModel.getNameColor(name);
    }

    public long getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(long changedTime) {
        this.changedTime = changedTime;
    }


    public static ContactModel get(UUID id) {
        // Mark = ?
        return SQLite.select()
                .from(ContactModel.class)
                .where(ContactModel_Table.Mark.is(id))
                .querySingle();
    }

    public static ContactModel get(long id) {
        //Id = ?
        return SQLite.select()
                .from(ContactModel.class)
                .where(ContactModel_Table.Id.is(id))
                .querySingle();
    }

    public static List<ContactModel> getAll() {
        // Status <> ?
        return SQLite.select()
                .from(ContactModel.class)
                .where(ContactModel_Table.Status.isNot(STATUS_DELETE))
                .queryList();
    }

    public static List<ContactModel> getAllUnSync() {
        // Status <> ?
        return SQLite.select()
                .from(ContactModel.class)
                .where(ContactModel_Table.Status.isNot(STATUS_UPLOADED))
                .queryList();
    }

    public static List<ContactModel> getAllChanged(long date) {
        // Changed > ?
        return SQLite.select()
                .from(ContactModel.class)
                .where(ContactModel_Table.ChangedTime.greaterThan(date))
                .queryList();
    }


    //@OneToMany(methods = {OneToMany.Method.ALL}, variableName = "recordModels")
    public List<RecordModel> records() {
        // get recordModels is this.Id
        if (recordModels == null || recordModels.isEmpty()) {
            recordModels = SQLite.select()
                    .from(RecordModel.class)
                    .where(RecordModel_Table.contact_Id.is(id))
                    .and(RecordModel_Table.Status.isNot(STATUS_DELETE))
                    .queryList();
        }
        return recordModels;
    }
}
