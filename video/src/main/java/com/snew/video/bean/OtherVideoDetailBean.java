package com.snew.video.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/18     14:16
 */
public class OtherVideoDetailBean {

    /**
     * success : 1
     * play : mp4
     * ext : dp
     * url : http://ltssjy.qq.com/jx.anlehe.com/n0026guwflf.mp4?vkey=E2FB4C792D08D0978EA70CADD88C411B6E73293631949460FD6C6DECBDDC89545E71D366D589735F33808A9705636A0EC555D163305C9EEA6706A05D12F9E51E884A621B24DEB582D70A3378122A61BC8A3E0A2658A2D43EEABB8709F4A08C1B48B62391B94E21797A482EF19B025570612079758C2C4A70
     */

    private int success;
    private String play;
    private String ext;
    private String url;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
