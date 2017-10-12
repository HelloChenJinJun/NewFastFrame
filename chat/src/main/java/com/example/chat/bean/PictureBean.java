package com.example.chat.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:04
 * QQ:             1981367757
 */
public class PictureBean implements Serializable {
        private String _id;
        private Date createdAt;
        private String desc;
        private Date publishedAt;
        private String type;
        private String url;
        private boolean used;
        private String who;


        public String get_id() {
                return _id;
        }

        public void set_id(String _id) {
                this._id = _id;
        }

        public Date getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
                this.createdAt = createdAt;
        }

        public String getDesc() {
                return desc;
        }

        public void setDesc(String desc) {
                this.desc = desc;
        }

        public Date getPublishedAt() {
                return publishedAt;
        }

        public void setPublishedAt(Date publishedAt) {
                this.publishedAt = publishedAt;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public boolean isUsed() {
                return used;
        }

        public void setUsed(boolean used) {
                this.used = used;
        }

        public String getWho() {
                return who;
        }

        public void setWho(String who) {
                this.who = who;
        }
}
