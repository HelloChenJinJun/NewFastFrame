package com.example.chat.bean;

import java.io.Serializable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      18:49
 * QQ:             1981367757
 */
public class HappyBean implements Serializable {
        private String content;
        private String hashId;
        private long unixtime;
        private String updatetime;
        private String url;


        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }

        public String getHashId() {
                return hashId;
        }

        public void setHashId(String hashId) {
                this.hashId = hashId;
        }

        public String getUpdatetime() {
                return updatetime;
        }

        public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
        }

        public long getUnixtime() {
                return unixtime;
        }

        public void setUnixtime(long unixtime) {
                this.unixtime = unixtime;
        }

        public String getUrl() {
                return url;
        }

        public void setUrl(String url) {
                this.url = url;
        }
}
