package com.example.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      0:07
 * QQ:             1981367757
 */

public class TxResponse implements Serializable {
        private String msg;
        private int code;
        private List<WinXinBean> newslist;


        public String getMsg() {
                return msg;
        }

        public void setMsg(String msg) {
                this.msg = msg;
        }


        public int getCode() {
                return code;
        }

        public void setCode(int code) {
                this.code = code;
        }


        public List<WinXinBean> getNewslist() {
                return newslist;
        }

        public void setNewslist(List<WinXinBean> newslist) {
                this.newslist = newslist;
        }
}
