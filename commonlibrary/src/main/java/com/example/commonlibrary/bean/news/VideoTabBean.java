package com.example.commonlibrary.bean.news;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/30     19:30
 */
@Entity
public class VideoTabBean {

    /**
     * category : video_new
     * name : 推荐
     */


    @Id
    private String category;
    private String name;



    @Generated(hash = 1205596710)
    public VideoTabBean(String category, String name) {
        this.category = category;
        this.name = name;
    }

    @Generated(hash = 2117373132)
    public VideoTabBean() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
