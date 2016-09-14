package net.qiujuer.tips.factory.model.code;

import com.google.gson.Gson;

import net.qiujuer.tips.factory.model.db.RecordModel;
import net.qiujuer.tips.factory.util.http.HttpKit;


public class SimpleRecordModel {
    private long date;
    private int color;
    private String brief;
    private int type;

    public SimpleRecordModel() {

    }

    public SimpleRecordModel(RecordModel recordModel) {
        this.date = recordModel.getDate();
        this.color = recordModel.getColor();
        this.brief = recordModel.getBrief();
        this.type = recordModel.getType();
    }

    public RecordModel toRecord() {
        RecordModel model = new RecordModel();
        model.setColor(this.color);
        model.setDate(this.date);
        model.setBrief(this.brief);
        model.setType(this.type);
        return model;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static SimpleRecordModel fromJson(String str) {
        try {
            return HttpKit.getRspGsonBuilder().create().fromJson(str, SimpleRecordModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
