package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     19:25
 * QQ:         1981367757
 */
@Entity
public class PostLikeEntity {
    //    点赞ID
    @Id
    private String lid;
    //    关联的点赞用户ID
    private String uid;
    //    关联的帖子ID
    private String pid;


    @Generated(hash = 2112807687)
    public PostLikeEntity(String lid, String uid, String pid) {
        this.lid = lid;
        this.uid = uid;
        this.pid = pid;
    }

    @Generated(hash = 660367003)
    public PostLikeEntity() {
    }


    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
