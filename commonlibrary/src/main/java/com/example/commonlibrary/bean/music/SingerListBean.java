package com.example.commonlibrary.bean.music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by COOTEK on 2017/9/2.
 */
@Entity
public class SingerListBean {

    private String name;
    private String artistId;
    private int count;
    @Id
    private String tingId;
    private String avatar;
    private String info;

    private String company;
    private boolean sex;
    private String country;
    private String birthDay;
    private String firstChat;


    @Generated(hash = 110137703)
    public SingerListBean(String name, String artistId, int count, String tingId, String avatar, String info,
                          String company, boolean sex, String country, String birthDay, String firstChat) {
        this.name = name;
        this.artistId = artistId;
        this.count = count;
        this.tingId = tingId;
        this.avatar = avatar;
        this.info = info;
        this.company = company;
        this.sex = sex;
        this.country = country;
        this.birthDay = birthDay;
        this.firstChat = firstChat;
    }

    @Generated(hash = 112188963)
    public SingerListBean() {
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getFirstChat() {
        return firstChat;
    }

    public void setFirstChat(String firstChat) {
        this.firstChat = firstChat;
    }

    public String getTingId() {
        return tingId;
    }

    public void setTingId(String tingId) {
        this.tingId = tingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SingerListBean{" +
                "name='" + name + '\'' +
                ", artistId='" + artistId + '\'' +
                ", count=" + count +
                ", tingId='" + tingId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", info='" + info + '\'' +
                ", company='" + company + '\'' +
                ", sex=" + sex +
                ", country='" + country + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", firstChat='" + firstChat + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && obj instanceof SingerListBean && ((SingerListBean) obj).getTingId().equals(getTingId()));
    }

    public boolean getSex() {
        return this.sex;
    }
}
