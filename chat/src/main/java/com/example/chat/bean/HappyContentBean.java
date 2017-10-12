package com.example.chat.bean;

import java.io.Serializable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:27
 * QQ:             1981367757
 */
public class HappyContentBean implements Serializable {
        private String content;
        private String hashId;
        private long unixtime;
        private String updatetime;


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

        public long getUnixtime() {
                return unixtime;
        }

        public void setUnixtime(long unixtime) {
                this.unixtime = unixtime;
        }

        public String getUpdatetime() {
                return updatetime;
        }

        public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
        }
}
