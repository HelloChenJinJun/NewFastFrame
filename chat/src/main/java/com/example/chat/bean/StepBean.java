package com.example.chat.bean;

import android.content.Intent;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/6/18     12:16
 * QQ:1981367757
 */

public class StepBean extends BmobObject{

    private User user;

    private String time;
    private Integer stepCount;


    public User getUser() {
        return user;

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStepCount(Integer stepCount) {
        this.stepCount = stepCount;
    }

    public Integer getStepCount() {
        return stepCount;
    }

    public String getTime() {
        return time;
    }



    public void setTime(String time) {
        this.time = time;
    }
}

