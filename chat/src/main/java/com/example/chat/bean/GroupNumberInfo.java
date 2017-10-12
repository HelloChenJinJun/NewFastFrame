package com.example.chat.bean;

import java.io.Serializable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/1      23:04
 * QQ:             1981367757
 */

public class GroupNumberInfo implements Serializable {
        private String groupNick;
        private User mUser;

        public User getUser() {
                return mUser;
        }

        public void setUser(User user) {
                mUser = user;
        }

        public String getGroupNick() {
                return groupNick;
        }

        public void setGroupNick(String groupNick) {
                this.groupNick = groupNick;
        }
}
