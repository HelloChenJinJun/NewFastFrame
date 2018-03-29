package com.rationalTiger.poster.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/2/7     20:21
 * QQ:         1981367757
 */
@Entity
public class ReplyCommentListEntity {

//    评论列表itemID
    @Id
    private String rid;

    //   公共ID
    private String publicId;
    //    内容
    private String content;


    @Generated(hash = 2079495397)
    public ReplyCommentListEntity(String rid, String publicId, String content) {
        this.rid = rid;
        this.publicId = publicId;
        this.content = content;
    }

    @Generated(hash = 223877422)
    public ReplyCommentListEntity() {
    }


    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
