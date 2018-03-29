package com.example.chat.events;

import com.amap.api.services.nearby.NearbyInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/19     13:16
 * QQ:         1981367757
 */

public class LocationEvent implements Serializable {
    private double longitude;
    private double latitude;
    private String location;


    private String country;
    private String province;
    private String city;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private List<NearbyInfo>  nearbyInfoList;

    public List<NearbyInfo> getNearbyInfoList() {
        return nearbyInfoList;
    }

    public void setNearbyInfoList(List<NearbyInfo> nearbyInfoList) {
        this.nearbyInfoList = nearbyInfoList;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
