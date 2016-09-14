package net.qiujuer.tips.factory.service.bean;


import net.qiujuer.tips.factory.model.Model;
import net.qiujuer.tips.factory.model.adapter.RecordViewModel;

import java.util.UUID;

public class MissWidgetListData {
    private UUID id;
    private int color;
    private String title;
    private int day;

    public MissWidgetListData() {
        id = Model.EMPTY_ID;
        title = "";
    }

    public MissWidgetListData(RecordViewModel model) {
        color = (model.getColor());
        title = (model.getBrief());
        day = (model.getDayNow());
        id = (model.getId());
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public UUID getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public int getDay() {
        return day;
    }

    public String getTitle() {
        return title;
    }
}
