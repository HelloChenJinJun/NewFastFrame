package com.snew.video.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     15:57
 */
public class QQVideoDetailBean {

    @Override
    public String toString() {
        return "QQVideoDetailBean{" +
                "code=" + code +
                ", play='" + play + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    /**
     * code : 0
     * play : url
     * url : /sigu/dp2.php?url=https%3A%2F%2Fsohu.zuida-163sina.com%2F20180214%2FnOivnvob%2Findex.m3u8
     */



    private int code;
    private String play;
    private String url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
