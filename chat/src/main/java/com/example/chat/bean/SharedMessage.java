package com.example.chat.bean;


import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/20      10:45
 * QQ:             1981367757
 */

public class SharedMessage extends BmobObject implements MultipleItem {
        private List<String> inVisibleUserList;
        private List<String> likerList;
        private String belongId;
        private String content;
        private Integer msgType;
        private String createTime;
        private List<String> imageList;
        private Integer visibleType;
        private List<String> commentMsgList;
        private String address;
        /**
         * URL标题
         */
        private String urlTitle;
        /**
         * 连接列表（包括urlAvatar和url主体内容）
         */
        private List<String> urlList;

        public String getUrlTitle() {
                return urlTitle;
        }

        public void setUrlTitle(String urlTitle) {
                this.urlTitle = urlTitle;
        }

        public List<String> getUrlList() {
                if (urlList == null) {
                        urlList=new ArrayList<>();
                }
                return urlList;
        }

        public void setUrlList(List<String> urlList) {
                this.urlList = urlList;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public Integer getVisibleType() {
                return visibleType;
        }

        public void setVisibleType(Integer visibleType) {
                this.visibleType = visibleType;
        }

        public List<String> getImageList() {
                if (imageList == null) {
                        imageList = new ArrayList<>();
                }
                return imageList;
        }

        public void setImageList(List<String> imageList) {
                this.imageList = imageList;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }

        public Integer getMsgType() {
                return msgType;
        }

        public void setMsgType(Integer msgType) {
                this.msgType = msgType;
        }

        public String getCreateTime() {
                return createTime;
        }

        public void setCreateTime(String createTime) {
                this.createTime = createTime;
        }

        public String getBelongId() {
                return belongId;
        }

        public void setBelongId(String belongId) {
                this.belongId = belongId;
        }

        public List<String> getLikerList() {
                if (likerList == null) {
                        likerList = new ArrayList<>();
                }
                return likerList;
        }

        public void setLikerList(List<String> likerList) {
                this.likerList = likerList;
        }




        public List<String>getInVisibleUserList(){
                if (inVisibleUserList == null) {
                        inVisibleUserList = new ArrayList<>();
                }
                return inVisibleUserList;
        }

        public void setInVisibleUserList(List<String> visibleUserList) {
                this.inVisibleUserList = visibleUserList;
        }





        public List<String> getCommentMsgList() {
                if (commentMsgList == null) {
                        commentMsgList = new ArrayList<>();
                }
                return commentMsgList;
        }

        public void setCommentMsgList(List<String> commentMsgList) {
                this.commentMsgList = commentMsgList;
        }

        public void setSeverCreateTime(String time) {
                setCreatedAt(time);
        }

        @Override
        public boolean equals(Object obj) {
                return obj instanceof SharedMessage && ((SharedMessage) obj).getObjectId().equals(getObjectId());
        }

        @Override
        public int getItemViewType() {
                return getMsgType();
        }
}
