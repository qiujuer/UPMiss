package net.qiujuer.tips.factory.model.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by qiujuer
 * on 15/8/17.
 */
public class ProductModel {
    private String Name;
    private String Icon;
    private String Introduction;
    private String Details;
    private Date Published;
    private List<ProductVersionModel> Versions;

    public Date getPublished() {
        return Published;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPublished(Date published) {
        Published = published;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }

    public void setVersions(List<ProductVersionModel> versions) {
        Versions = versions;
    }

    public String getDetails() {
        return Details;
    }

    public String getIcon() {
        return Icon;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public List<ProductVersionModel> getVersions() {
        return Versions;
    }

    public static ProductModel fromJson(JSONObject json) {
        try {
            Gson gson = getRspGsonBuilder().create();
            return gson.fromJson(json.toString(), ProductModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static GsonBuilder getRspGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return gsonBuilder;
    }
}
