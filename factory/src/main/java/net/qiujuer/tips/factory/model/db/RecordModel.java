/**
 * CREATE TABLE Record
 * (
 * Id INTEGER PRIMARY KEY AUTOINCREMENT,
 * Brief TEXT,
 * Changed INTEGER,
 * Color INTEGER,
 * Contacts INTEGER REFERENCES Contact(Id) ON DELETE NO ACTION ON UPDATE NO ACTION,
 * Created INTEGER,
 * Date INTEGER,
 * Last INTEGER,
 * Mark TEXT,
 * Status INTEGER,
 * Type INTEGER
 * )
 */
package net.qiujuer.tips.factory.model.db;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.qiujuer.tips.factory.util.TipsCalender;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(database = AppDatabase.class, name = "Record")
public class RecordModel extends BaseModel implements ModelStatus {
    public final static int TYPE_BIRTHDAY = 1;
    public final static int TYPE_MEMORIAL = 2;
    public final static int TYPE_FUTURE = 3;

    public RecordModel() {
        super();
        mark = UUID.randomUUID();
        date = 201501010;
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
    @Column(name = "Mark",typeConverter = UUIDConverter.class)
    private UUID mark;

    @SerializedName("Brief")
    @Column(name = "Brief")
    private String brief;

    @SerializedName("Type")
    @Column(name = "Type")
    private int type;

    @SerializedName("Color")
    @Column(name = "Color")
    private int color;

    @SerializedName("Date")
    @Column(name = "Date")
    private long date;

    @SerializedName("Create")
    @Column(name = "Created")
    private Date create;

    @SerializedName("Last")
    @Column(name = "Last")
    private Date last;

    @ForeignKey()
    //@ForeignKeyReference(columnName = "Contact", columnType = , foreignKeyColumnName = )
    private ContactModel contact;

    @Expose
    @Column(name = "Status")
    private int status;

    @Expose
    @Column(name = "ChangedTime")
    private long changedTime;

    @Expose
    private transient TipsCalender mDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getMark() {
        return mark;
    }

    public void setMark(UUID mark) {
        this.mark = mark;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public TipsCalender getDateCalender() {
        if (this.mDate == null)
            this.mDate = new TipsCalender(date);
        else
            this.mDate.set(date);
        return this.mDate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        if (this.mDate != null)
            this.mDate.set(date);
    }

    public void setDate(TipsCalender date) {
        this.mDate = date;
        this.date = date.toLong();
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getLast() {
        return last;
    }

    public ContactModel getContact() {
        return contact;
    }

    public void setContact(ContactModel contact) {
        this.contact = contact;
    }

    public long getChangedTime() {
        return changedTime;
    }

    public void setChangedTime(long changedTime) {
        this.changedTime = changedTime;
    }

    public void setLast(Date last) {
        this.last = last;
        this.changedTime = last.getTime();
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

    public static List<RecordModel> getAll() {
        // Status <> STATUS_DELETE
        return SQLite.select()
                .from(RecordModel.class)
                .where(RecordModel_Table.Status.isNot(STATUS_DELETE))
                .queryList();
    }

    public static List<RecordModel> getAllChanged(long date) {
        // ChangedTime > date
        return SQLite.select()
                .from(RecordModel.class)
                .where(RecordModel_Table.ChangedTime.greaterThan(date))
                .queryList();
    }

    public static List<RecordModel> getAllUnSync() {
        // Status <> STATUS_DELETE
        return SQLite.select()
                .from(RecordModel.class)
                .where(RecordModel_Table.Status.isNot(STATUS_UPLOADED))
                .queryList();
    }

    public static RecordModel get(UUID id) {
        // Mark = ?
        return SQLite.select()
                .from(RecordModel.class)
                .where(RecordModel_Table.Mark.is(id))
                .querySingle();
    }

    public static RecordModel get(String id) {
        return get(UUID.fromString(id));
    }

    public static RecordModel get(long id) {
        // Id = ?
        return SQLite.select()
                .from(RecordModel.class)
                .where(RecordModel_Table.Id.is(id))
                .querySingle();
    }

    public static long getCount(int type) {
        // Status <> ? and Type = ?
        return SQLite.select()
                .from(RecordModel.class)
                .where(RecordModel_Table.Status.isNot(STATUS_DELETE))
                .and(RecordModel_Table.Type.is(type))
                .queryList().size();
    }

    public static List<RecordModel> getNewest(int count) {
        // Status <> ? orderBy Changed DESC limit count
        return SQLite.select()
                .from(RecordModel.class)
                .where(RecordModel_Table.Status.isNot(STATUS_DELETE))
                .orderBy(RecordModel_Table.ChangedTime, false)
                .limit(count)
                .queryList();
    }

    @Override
    public String toString() {
        return "RecordModel{" +
                "id=" + id +
                ", mark=" + mark +
                ", brief='" + brief + '\'' +
                ", type=" + type +
                ", color=" + color +
                ", date=" + date +
                ", create=" + create +
                ", last=" + last +
                ", contact=" + contact +
                ", status=" + status +
                ", changedTime=" + changedTime +
                ", mDate=" + mDate +
                '}';
    }
}
