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
 *
 * 9
 */
public class ChatMessage extends BaseMessage {


        //        /**
//         * 序列ID 用于在序列和反序列化的过程中保持数据传输的完整性
//         */
        private static final long serialVerisionUID = 1L;

        public static final int MESSAGE_TYPE_ADD=1;
        public static final int MESSAGE_TYPE_AGREE=2;
        public static final int MESSAGE_TYPE_READED=3;
        public static final int MESSAGE_TYPE_ADD_BLACK=5;
        public static final int MESSAGE_TYPE_CANCEL_BLACK=6;
        public static final int MESSAGE_TYPE_NORMAL = 4;

    /**
         * 消息类型  ADD / AGREE / READED 等
         */
        private Integer messageType;

        public Integer getMessageType() {
                return messageType;
        }

        public void setMessageType(Integer messageType) {
                this.messageType = messageType;
        }

        /**
         * 会话ID
         * 组成部分为 ： 发送者ID&接受者ID
         */
        private String conversationId;
        /**
         * 接收方的ID
         */
        private String toId;





        public String getConversationId() {
                return conversationId;
        }

        public void setConversationId(String conversationId) {
                this.conversationId = conversationId;
        }
        public String getToId() {
                return toId;
        }

        public void setToId(String toId) {
                this.toId = toId;
        }


    @Override
    public boolean equals(Object obj) {
            if (obj!=null&&obj instanceof ChatMessage){
                ChatMessage chatMessage= (ChatMessage) obj;
                return chatMessage.getConversationId()
                        .equals(getConversationId())
                        &&chatMessage.getCreateTime()
                        .equals(getCreateTime())
                        &&chatMessage.getMessageType().equals(getMessageType());
            }
        return false ;
    }


    @Override
    public String toString() {
          String str=super.toString();
        return str+"ChatMessage{" +
                "messageType=" + messageType +
                ", conversationId='" + conversationId + '\'' +
                ", toId='" + toId + '\'' +
                '}';
    }
}


