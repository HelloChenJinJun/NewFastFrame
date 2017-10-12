package com.example.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      20:42
 * QQ:             1981367757
 */
public class HappyData implements Serializable {
        private List<HappyBean> data;

        public List<HappyBean> getData() {
                return data;
        }

        public void setData(List<HappyBean> data) {
                this.data = data;
        }
}
