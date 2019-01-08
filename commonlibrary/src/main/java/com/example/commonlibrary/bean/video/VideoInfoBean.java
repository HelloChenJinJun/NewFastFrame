package com.example.commonlibrary.bean.video;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2019/1/8     18:11
 */
@Entity
public class VideoInfoBean {
    @Id
    private String path;

    private String name;

    private String url;

    @Generated(hash = 983100739)
    public VideoInfoBean(String path, String name, String url) {
        this.path = path;
        this.name = name;
        this.url = url;
    }

    @Generated(hash = 2130650952)
    public VideoInfoBean() {
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
