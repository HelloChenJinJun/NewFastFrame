package com.example.news.event;

import com.example.commonlibrary.bean.news.OtherNewsTypeBean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      23:29
 * QQ:             1981367757
 */

public class TypeNewsEvent {
    private List<OtherNewsTypeBean> data;


    public TypeNewsEvent(List<OtherNewsTypeBean> data) {
        this.data = data;
    }

    public List<OtherNewsTypeBean> getData() {
        return data;
    }

    public void setData(List<OtherNewsTypeBean> data) {
        this.data = data;
    }
}
