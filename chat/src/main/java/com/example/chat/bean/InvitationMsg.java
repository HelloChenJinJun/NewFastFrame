package com.example.chat.bean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/15      19:02
 * QQ:             1981367757
 */

/**
 * 好友邀请消息实体
 */

public class InvitationMsg {
        /**
         * 消息所属的用户ID
         */
        private String belongId;
        /**
         * 头像
         */
        private String avatar;
        /**
         * 用户名
         */
        private String name;
        /**
         * 昵称
         */
        private String nick;
        /**
         * 消息的读取状态
         */
        private Integer readStatus;

        /**
         * 消息的接收方id
         */
        private String toId;
        /**
         * 消息的内容
         */
        private String content;
        /**
         * 消息内容的类型
         */
        private Integer msgType;


        /**
         * 消息的创建时间
         */
        private String time;

        public String getTime() {
                return time;
        }

        public void setTime(String time) {
                this.time = time;
        }

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

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getNick() {
                return nick;
        }

        public void setNick(String nick) {
                this.nick = nick;
        }

        public Integer getReadStatus() {
                return readStatus;
        }

        public void setReadStatus(Integer readStatus) {
                this.readStatus = readStatus;
        }


        public String getToId() {
                return toId;
        }

        public void setToId(String toId) {
                this.toId = toId;
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

        @Override
        public boolean equals(Object obj) {
                if (obj instanceof InvitationMsg) {
                        InvitationMsg invitationMsg = (InvitationMsg) obj;
                        return invitationMsg.getBelongId().equals(getBelongId()) &&invitationMsg.getReadStatus().equals(getReadStatus());
                }
                return false;
        }
}
