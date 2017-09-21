package com.example.news.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/21      23:07
 * QQ:             1981367757
 */

public class LibraryLoginEvent {
private String info;
    public LibraryLoginEvent(String info) {
        this.info=info;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
