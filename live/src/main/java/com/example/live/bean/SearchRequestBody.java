package com.example.live.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      10:49
 * QQ:             1981367757
 */

public class SearchRequestBody {
    private String v = "3.0.1";

    private String os = "1";

    private String ver = "4";

    private P p;

    public static SearchRequestBody getInstance(P p) {
        return new SearchRequestBody(p);
    }

    public SearchRequestBody() {

    }

    public SearchRequestBody(P p) {
        this.p = p;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public P getP() {
        return p;
    }

    public void setP(P p) {
        this.p = p;
    }

    public static class P {
        public static final int DEFAULT_SIZE = 10;

        private int page;

        private String key;

        private int categoryId;

        private int size = DEFAULT_SIZE;

        public P() {

        }

        public P(int page, String key) {
            this.page = page;
            this.key = key;
        }

        public P(int page, String key, int size) {
            this.page = page;
            this.key = key;
            this.size = size;
        }

        public P(int page, String key, int categoryId, int size) {
            this.page = page;
            this.key = key;
            this.categoryId = categoryId;
            this.size = size;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "P{" +
                    "page=" + page +
                    ", key='" + key + '\'' +
                    ", categoryId=" + categoryId +
                    ", size=" + size +
                    '}';
        }
    }
}
