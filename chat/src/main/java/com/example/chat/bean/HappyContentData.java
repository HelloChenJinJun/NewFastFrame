package com.example.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:38
 * QQ:             1981367757
 */
public class HappyContentData implements Serializable {
        private List<HappyContentBean> data;

        public List<HappyContentBean> getData() {
                return data;
        }

        public void setData(List<HappyContentBean> data) {
                this.data = data;
        }
}
