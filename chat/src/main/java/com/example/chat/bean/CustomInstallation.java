package com.example.chat.bean;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/9      16:10
 * QQ:             1981367757
 */
public class CustomInstallation extends BmobInstallation {
        private static final Long serialVerisionUID = 1L;
        /**
         * 用户用户名,用于和设备进行绑定
         */
        private String uid;

        public CustomInstallation() {

        }

        public String getUid() {
                return uid;
        }

        public void setUid(String uid) {
                this.uid = uid;
        }
}
