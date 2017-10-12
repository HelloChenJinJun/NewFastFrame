package com.example.chat.bean;

import java.io.Serializable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/15      23:29
 * QQ:             1981367757
 */
public class RecentMsg implements Serializable {
        private static final long serialVersionUID = 1L;
        /**
         * 单聊：最近会话界面显示的用户ID
         * 群聊：群ID
         */
        private String belongId;
        /**
         * 单聊：最近会话界面显示的用户头像
         * 群聊：群头像
         */
        private String avatar;
        /**
         * 单聊：最近会话界面显示的用户昵称
         * 群聊：群昵称
         */
        private String nick;
        /**
         * 单聊：最近会话界面显示的用户用户名
         * 群聊：群名
         */
        private String name;
        /**
         * 单聊、群聊：最近会话消息创建时间
         */
        private String time;
        /**
         * 消息类型
         */
        private Integer msgType;
        /**
         * 消息内容
         */
        private String lastMsgContent;

        public String getBelongId() {
                return belongId;
        }

        public void setBelongId(String belongId) {
                this.belongId = belongId;
        }

        public String getAvatar() {
                return avatar;
        }

        public void setAvatar(String avatar) {
                this.avatar = avatar;
        }

        public String getNick() {
                return nick;
        }

        public void setNick(String nick) {
                this.nick = nick;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getTime() {
                return time;
        }

        public void setTime(String time) {
                this.time = time;
        }

        public Integer getMsgType() {
                return msgType;
        }

        public void setMsgType(Integer msgType) {
                this.msgType = msgType;
        }

        public String getLastMsgContent() {
                return lastMsgContent;
        }

        public void setLastMsgContent(String lastMsgContent) {
                this.lastMsgContent = lastMsgContent;
        }

        @Override
        public boolean equals(Object obj) {
                if (obj instanceof RecentMsg) {
                        RecentMsg recentMsg = (RecentMsg) obj;
                        return recentMsg.getBelongId().equals(getBelongId());
                }
                return false;
        }
}
