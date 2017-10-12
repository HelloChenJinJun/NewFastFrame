package com.example.chat.bean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      20:36
 * QQ:             1981367757
 */
public class HappyResponse {
        private int error_code;
        private String reason;
        private HappyData result;

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

        public HappyData getResult() {
                return result;
        }

        public void setResult(HappyData result) {
                this.result = result;
        }
}
