package com.example.commonlibrary.rxbus.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/14     0:06
 * QQ:         1981367757
 */

public class PwChangeEvent {
    private String old;
    private String news;
    private boolean isSuccess;

    @Override
    public String toString() {
        return "PwChangeEvent{" +
                "old='" + old + '\'' +
                ", news='" + news + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getOld() {
        return old;
    }

    public void setOld(String old) {
        this.old = old;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }
}
