package com.example.commonlibrary.bean.music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     20:28
 */
@Entity
public class SingerInfoBean {


    @Id
    private String tingId;


    //    歌手描述
    private String info;


    private String avatar;

    private String detailUrl;


    private String country;


    private String company;
    private String name;


    private String birthday;


    @Generated(hash = 838420988)
    public SingerInfoBean(String tingId, String info, String avatar,
            String detailUrl, String country, String company, String name,
            String birthday) {
        this.tingId = tingId;
        this.info = info;
        this.avatar = avatar;
        this.detailUrl = detailUrl;
        this.country = country;
        this.company = company;
        this.name = name;
        this.birthday = birthday;
    }


    @Generated(hash = 404280318)
    public SingerInfoBean() {
    }


    public String getTingId() {
        return this.tingId;
    }


    public void setTingId(String tingId) {
        this.tingId = tingId;
    }


    public String getInfo() {
        return this.info;
    }


    public void setInfo(String info) {
        this.info = info;
    }


    public String getAvatar() {
        return this.avatar;
    }


    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getDetailUrl() {
        return this.detailUrl;
    }


    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }


    public String getCountry() {
        return this.country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    public String getCompany() {
        return this.company;
    }


    public void setCompany(String company) {
        this.company = company;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getBirthday() {
        return this.birthday;
    }


    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


}
