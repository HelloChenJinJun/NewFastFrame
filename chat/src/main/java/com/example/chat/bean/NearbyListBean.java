package com.example.chat.bean;


import com.example.chat.events.LocationEvent;

import java.io.Serializable;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/28     9:37
 * QQ:         1981367757
 */

public class NearbyListBean implements Serializable {

    private boolean isCheck;

    private LocationEvent locationEvent;


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof NearbyListBean) {
            NearbyListBean nearbyListBean = (NearbyListBean) obj;
            return nearbyListBean.getLocationEvent().getTitle() != null
                    && nearbyListBean.getLocationEvent().getTitle()
                    .equals(getLocationEvent().getTitle());
        }
        return false;
    }

    public LocationEvent getLocationEvent() {
        return locationEvent;
    }

    public void setLocationEvent(LocationEvent locationEvent) {
        this.locationEvent = locationEvent;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
