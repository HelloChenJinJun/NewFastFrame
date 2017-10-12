package com.example.chat.bean;

import java.io.Serializable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:35
 * QQ:             1981367757
 */
public class HappyContentResponse implements Serializable {
        private int error_code;
        private String reason;
        private HappyContentData result;

        public int getError_code() {
                return error_code;
        }

        public void setError_code(int error_code) {
                this.error_code = error_code;
        }

        public String getReason() {
                return reason;
        }

        public void setReason(String reason) {
                this.reason = reason;
        }

        public HappyContentData getResult() {
                return result;
        }

        public void setResult(HappyContentData result) {
                this.result = result;
        }
}
