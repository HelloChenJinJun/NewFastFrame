package com.example.chat.bean;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/6/13      12:39
 * QQ:             1981367757
 */

public class CrashMessage extends BmobObject {
        private String errorMessage;




        public String getErrorMessage() {
                return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
        }
}
