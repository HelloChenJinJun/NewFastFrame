package com.snew.video.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     21:30
 */
public class QQTVVideoDetailBean {

    /**
     * msg : 200
     * ext : mp4
     * site : qqmtv
     * vid : 1006_43dc5ba04ae343ff97111d83b437f2f4
     * url : http://vwecam.tc.qq.com/1006_43dc5ba04ae343ff97111d83b437f2f4.f20.mp4?ptype=http&vkey=E3BAF93E1BA9A090F3B42C9831AB69EB3CA95C70FD02097BC80AFF143024595149A9B51D73ACC14A44B24BBCC6DD53A5120ECDD812DA272B
     */

    private String msg;
    private String ext;
    private String site;
    private String vid;
    private String url;


    @Override
    public String toString() {
        return "QQTVVideoDetailBean{" +
                "msg='" + msg + '\'' +
                ", ext='" + ext + '\'' +
                ", site='" + site + '\'' +
                ", vid='" + vid + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
