package com.example.chat.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:11
 * QQ:             1981367757
 */
public class PictureResponse implements Serializable {
        private boolean error;
        private List<PictureBean> results;

        public boolean isError() {
                return error;
        }

        public void setError(boolean error) {
                this.error = error;
        }

        public List<PictureBean> getResults() {
                return results;
        }

        public void setResults(List<PictureBean> results) {
                this.results = results;
        }
}
