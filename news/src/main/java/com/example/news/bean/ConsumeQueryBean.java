package com.example.news.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     17:10
 * QQ:         1981367757
 */

public class ConsumeQueryBean {

    /**
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * orderBy : JYSJ desc
     * startRow : 1
     * endRow : 10
     * total : 124
     * pages : 13
     * list : [{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513767852000,"SYJE":806,"ROW_ID":1,"XGH":"20141000341","JYJE":-300,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区食堂二楼"},{"FDID":"99225","JYSJ":1513767731000,"SYJE":2106,"ROW_ID":2,"XGH":"20141000341","JYJE":2000,"JYMC":"补助流水                                ","XM":"陈锦军"},{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513767731000,"SYJE":1106,"ROW_ID":3,"XGH":"20141000341","JYJE":-1000,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区食堂二楼"},{"FDID":"99225","JYSJ":1513766362000,"SYJE":106,"ROW_ID":4,"XGH":"20141000341","JYJE":2000,"JYMC":"银行转账                                ","XM":"陈锦军"},{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513476668000,"SYJE":106,"ROW_ID":5,"XGH":"20141000341","JYJE":-300,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区食堂二楼"},{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513476668000,"SYJE":406,"ROW_ID":6,"XGH":"20141000341","JYJE":-400,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区食堂二楼"},{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513420091000,"SYJE":806,"ROW_ID":7,"XGH":"20141000341","JYJE":-330,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区二食堂楼下高档"},{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513418868000,"SYJE":1136,"ROW_ID":8,"XGH":"20141000341","JYJE":-1000,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区食堂二楼"},{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513248817000,"SYJE":2136,"ROW_ID":9,"XGH":"20141000341","JYJE":-960,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区食堂二楼"},{"XQ_NAME":"主校区","FDID":"99225","JYSJ":1513162602000,"SYJE":3096,"ROW_ID":10,"XGH":"20141000341","JYJE":-1000,"JYMC":"持卡人消费                              ","XM":"陈锦军","SH_NAME":"北区食堂二楼"}]
     * firstPage : 1
     * prePage : 0
     * nextPage : 2
     * lastPage : 8
     * isFirstPage : true
     * isLastPage : false
     * hasPreviousPage : false
     * hasNextPage : true
     * navigatePages : 8
     * navigatepageNums : [1,2,3,4,5,6,7,8]
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private String orderBy;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private int firstPage;
    private int prePage;
    private int nextPage;
    private int lastPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * XQ_NAME : 主校区
         * FDID : 99225
         * JYSJ : 1513767852000
         * SYJE : 806
         * ROW_ID : 1
         * XGH : 20141000341
         * JYJE : -300
         * JYMC : 持卡人消费
         * XM : 陈锦军
         * SH_NAME : 北区食堂二楼
         */

        private String XQ_NAME;
        private String FDID;
        private long JYSJ;
        private int SYJE;
        private int ROW_ID;
        private String XGH;
        private int JYJE;
        private String JYMC;
        private String XM;
        private String SH_NAME;

        public String getXQ_NAME() {
            return XQ_NAME;
        }

        public void setXQ_NAME(String XQ_NAME) {
            this.XQ_NAME = XQ_NAME;
        }

        public String getFDID() {
            return FDID;
        }

        public void setFDID(String FDID) {
            this.FDID = FDID;
        }

        public long getJYSJ() {
            return JYSJ;
        }

        public void setJYSJ(long JYSJ) {
            this.JYSJ = JYSJ;
        }

        public int getSYJE() {
            return SYJE;
        }

        public void setSYJE(int SYJE) {
            this.SYJE = SYJE;
        }

        public int getROW_ID() {
            return ROW_ID;
        }

        public void setROW_ID(int ROW_ID) {
            this.ROW_ID = ROW_ID;
        }

        public String getXGH() {
            return XGH;
        }

        public void setXGH(String XGH) {
            this.XGH = XGH;
        }

        public int getJYJE() {
            return JYJE;
        }

        public void setJYJE(int JYJE) {
            this.JYJE = JYJE;
        }

        public String getJYMC() {
            return JYMC;
        }

        public void setJYMC(String JYMC) {
            this.JYMC = JYMC;
        }

        public String getXM() {
            return XM;
        }

        public void setXM(String XM) {
            this.XM = XM;
        }

        public String getSH_NAME() {
            return SH_NAME;
        }

        public void setSH_NAME(String SH_NAME) {
            this.SH_NAME = SH_NAME;
        }
    }
}
