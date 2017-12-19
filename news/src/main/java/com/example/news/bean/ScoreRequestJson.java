package com.example.news.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/19     14:36
 * QQ:         1981367757
 */

public class ScoreRequestJson  implements Serializable {

    /**
     * draw : 1
     * order : [{"column":3,"dir":"desc","name":"KCMC"}]
     * pageNum : 1
     * pageSize : 10
     * start : 0
     * length : 10
     * xn : 2016-2017
     * mapping : getcourseScoreList
     * coursename :
     */

    private int draw;
    private int pageNum;
    private int pageSize;
    private int start;
    private int length;
    private String xn;
    private String mapping;
    private String coursename;
    private List<OrderBean> order;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getXn() {
        return xn;
    }

    public void setXn(String xn) {
        this.xn = xn;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public List<OrderBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderBean> order) {
        this.order = order;
    }

    public static class OrderBean implements Serializable{
        /**
         * column : 3
         * dir : desc
         * name : KCMC
         */

        private int column;
        private String dir;
        private String name;

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
