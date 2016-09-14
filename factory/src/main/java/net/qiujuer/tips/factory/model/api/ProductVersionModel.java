package net.qiujuer.tips.factory.model.api;

import java.util.Date;

/**
 * Created by qiujuer
 * on 15/8/17.
 */
public class ProductVersionModel {
    private int AddrType;
    private String Address;
    private int VerCode;
    private String Content;
    private String VerName;
    private Date Published;

    public Date getPublished() {
        return Published;
    }

    public int getVerCode() {
        return VerCode;
    }

    public String getVerName() {
        return VerName;
    }


    public void setPublished(Date published) {
        Published = published;
    }

    public void setVerCode(int verCode) {
        VerCode = verCode;
    }

    public void setVerName(String verName) {
        VerName = verName;
    }

    public int getAddrType() {
        return AddrType;
    }

    public String getAddress() {
        return Address;
    }

    public String getContent() {
        return Content;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setAddrType(int addrType) {
        AddrType = addrType;
    }

    public void setContent(String content) {
        Content = content;
    }
}
