package com.example.chat.events;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/6/2      10:24
 * QQ:             1981367757
 */

public class GroupInfoEvent {
        public static final int TYPE_GROUP_NICK=0;
        public static final int TYPE_GROUP_NAME=1;
        public static final int TYPE_GROUP_DESCRIPTION=2;
        public static final int TYPE_GROUP_NOTIFICATION=3;
        public static final int TYPE_GROUP_AVATRA=4;
        public static final int TYPE_GROUP_NUMBER=5;
        private String content;
        private int type=-1;

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }


        public GroupInfoEvent(String content, int type) {
                this.content = content;
                this.type = type;
        }

        public int getType() {
                return type;
        }

        public void setType(int type) {
                this.type = type;
        }
}
