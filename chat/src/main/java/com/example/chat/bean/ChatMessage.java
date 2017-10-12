package com.example.chat.bean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/9      12:35
 * QQ:             1981367757
 */


/**
 * 消息类
 * 上传到服务器中保存
 */
public class ChatMessage extends BaseMessage {


        //        /**
//         * 序列ID 用于在序列和反序列化的过程中保持数据传输的完整性
//         */
        private static final long serialVerisionUID = 1L;
        /**
         * 消息标签  ADD / AGREE / READED 等
         */
        private String tag;
        /**
         * 会话ID
         * 组成部分为 ： 发送者ID&接受者ID
         */
        private String conversationId;

//        /**
//         * 发送者头像
//         */
//        private String belongAvatar;
//        /**
//         * 发送者昵称
//         */
//        private String belongNick;
//        /**
//         * 发送者用户名
//         */
//        private String belongUserName;

//        /**
//         * 发送的消息内容
//         */
//        private String content;
//        /**
//         * 发送方的ID
//         */
//        private String belongId;

        /**
         * 接收方的ID
         */
        private String toId;

//        /**
//         * 消息的类型
//         */
//        private Integer msgType;

//        /**
//         * 消息读取的状态
//         */
//        private Integer readStatus;
//
//        /**
//         * 消息发送的状态
//         */
//        private Integer sendStatus;
//
//        /**
//         * 消息创建的时间
//         */
//        private String createTime;


        public String getTag() {
                return tag;
        }

        public void setTag(String tag) {
                this.tag = tag;
        }

        public String getConversationId() {
                return conversationId;
        }

        public void setConversationId(String conversationId) {
                this.conversationId = conversationId;
        }

//        public String getBelongAvatar() {
//                return belongAvatar;
//        }
//
//        public void setBelongAvatar(String belongAvatar) {
//                this.belongAvatar = belongAvatar;
//        }
//
//        public String getBelongNick() {
//                return belongNick;
//        }
//
//        public void setBelongNick(String belongNick) {
//                this.belongNick = belongNick;
//        }
//
//        public String getBelongUserName() {
//                return belongUserName;
//        }

//        public void setBelongUserName(String belongUserName) {
//                this.belongUserName = belongUserName;
//        }
//
//        public String getContent() {
//                return content;
//        }
//
//        public void setContent(String content) {
//                this.content = content;
//        }
//
//        public String getBelongId() {
//                return belongId;
//        }
//
//        public void setBelongId(String belongId) {
//                this.belongId = belongId;
//        }

        public String getToId() {
                return toId;
        }

        public void setToId(String toId) {
                this.toId = toId;
        }

//        public Integer getMsgType() {
//                return msgType;
//        }
//
//        public void setMsgType(Integer msgType) {
//                this.msgType = msgType;
//        }

//        public Integer getReadStatus() {
//                return readStatus;
//        }
//
//        public void setReadStatus(Integer readStatus) {
//                this.readStatus = readStatus;
//        }
//
//        public Integer getSendStatus() {
//                return sendStatus;
//        }
//
//        public void setSendStatus(Integer sendStatus) {
//                this.sendStatus = sendStatus;
//        }
//
//        public String getCreateTime() {
//                return createTime;
//        }
//
//        public void setCreateTime(String createTime) {
//                this.createTime = createTime;
//        }

}


