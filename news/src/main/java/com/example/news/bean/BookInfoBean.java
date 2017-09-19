package com.example.news.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      16:31
 * QQ:             1981367757
 */

public class BookInfoBean {
    private String bookName;
    private String startTime;
    private String endTime;
    private String enableNum;
    private String contentUrl;

    private boolean isEnableBorrow;


    public boolean isEnableBorrow() {
        return isEnableBorrow;
    }

    public void setEnableBorrow(boolean enableBorrow) {
        isEnableBorrow = enableBorrow;
    }

    private String number;
    private String check;


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEnableNum() {
        return enableNum;
    }

    public void setEnableNum(String enableNum) {
        this.enableNum = enableNum;
    }
}
