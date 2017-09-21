package com.example.news.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/17      18:01
 * QQ:             1981367757
 */

public class SearchLibraryBean {
    private String bookName;
    private String from;
    private String author;
    private String totalNum;
    private String enableNum;
    private String contentUrl;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getEnableNum() {
        return enableNum;
    }

    public void setEnableNum(String enableNum) {
        this.enableNum = enableNum;
    }


    @Override
    public String toString() {
        return "SearchLibraryBean{" +
                "bookName='" + bookName + '\'' +
                ", from='" + from + '\'' +
                ", author='" + author + '\'' +
                ", totalNum='" + totalNum + '\'' +
                ", enableNum='" + enableNum + '\'' +
                '}';

    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
