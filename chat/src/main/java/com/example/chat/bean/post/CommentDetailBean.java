package com.example.chat.bean.post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     14:48
 * QQ:         1981367757
 */

public class CommentDetailBean implements Serializable{
    private String content;
    private String replyContent;
    private String publicId;


    private Map<String,List<ReplyDetailContent>> conversationList;


    public Map<String, List<ReplyDetailContent>> getConversationList() {
        if (conversationList == null) {
            conversationList=new HashMap<>();
        }
        return conversationList;
    }

    public void setConversationList(Map<String, List<ReplyDetailContent>> conversationList) {
        this.conversationList = conversationList;
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

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

}
