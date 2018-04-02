package com.example.chat.bean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/16      17:14
 * QQ:             1981367757
 * 7
 */

public class GroupChatMessage extends BaseMessage {
        /**
         * 该消息所属的群组ID
         */
        private String groupId;

        public String getGroupId() {
                return groupId;
        }

        public void setGroupId(String groupId) {
                this.groupId = groupId;
        }


        @Override
        public String toString() {
                return super.toString()+"GroupChatMessage{" +
                        "groupId='" + groupId + '\'' +
                        '}';
        }
}
