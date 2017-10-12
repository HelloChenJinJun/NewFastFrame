package com.example.chat.bean;

import java.io.Serializable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/6      23:25
 * QQ:             1981367757
 */
public class WinXinBean implements Serializable {
        private String ctime;
        private String description;
        private String picUrl;
        private String url;
        private String title;


        public String getCtime() {
                return ctime;
        }

        public void setCtime(String ctime) {
                this.ctime = ctime;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getPicUrl() {
                return picUrl;
        }

        public void setPicUrl(String picUrl) {
                this.picUrl = picUrl;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }
}
