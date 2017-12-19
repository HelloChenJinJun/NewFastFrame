package com.example.news.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/19     14:23
 * QQ:         1981367757
 */

public class ScoreBean {


    /**
     * pageNum : 1
     * pageSize : 10
     * size : 10
     * orderBy : KCMC desc
     * startRow : 1
     * endRow : 10
     * total : 17
     * pages : 2
     * list : [{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"057241","CJBH":"2014100034116-2)-21608000-057241-1","KSXZM":"正常考试","KCCJ":88,"XH":"20141000341","XN":"2016-2017","KCMC":"经济管理应用文写作","RKJSGH":"057241","CJLRRQ":"20171031","XQM":"第二学期","SFCX":"否","JXBH":"16-2)-21608000-057241-1","XF":2,"ROW_ID":1,"KSXSM":"分散","JD":3.5,"KCH":"21608000"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"046252","CJBH":"2014100034116-1)-20811400-046252-1","KSXZM":"正常考试","KCCJ":83,"XH":"20141000341","XN":"2016-2017","KCMC":"生产与运作管理","RKJSGH":"046252","CJLRRQ":"20171031","XQM":"第一学期","SFCX":"否","JXBH":"16-1)-20811400-046252-1","XF":2.5,"ROW_ID":2,"KSXSM":"分散","JD":3,"KCH":"20811400"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"046182","CJBH":"2014100034116-1)-21915800-046182-1","KSXZM":"正常考试","KCCJ":84,"XH":"20141000341","XN":"2016-2017","KCMC":"物联网及其应用","RKJSGH":"046182","CJLRRQ":"20171031","XQM":"第一学期","SFCX":"否","JXBH":"16-1)-21915800-046182-1","XF":2,"ROW_ID":3,"KSXSM":"分散","JD":3,"KCH":"21915800"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"046233","CJBH":"2014100034116-2)-20810200-046233-1","KSXZM":"正常考试","KCCJ":84,"XH":"20141000341","XN":"2016-2017","KCMC":"模糊系统","RKJSGH":"046233","CJLRRQ":"20171031","XQM":"第二学期","SFCX":"否","JXBH":"16-2)-20810200-046233-1","XF":2,"ROW_ID":4,"KSXSM":"分散","JD":3,"KCH":"20810200"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"057200","CJBH":"2014100034116-1)-7160378x-057200-1","KSXZM":"正常考试","KCCJ":65,"XH":"20141000341","XN":"2016-2017","KCMC":"数码摄影与PHOTOSHOP处理","RKJSGH":"057200","CJLRRQ":"20171031","XQM":"第一学期","SFCX":"否","JXBH":"16-1)-7160378x-057200-1","XF":1.5,"ROW_ID":5,"KSXSM":"分散","JD":1.5,"KCH":"7160378x"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"046259","CJBH":"2014100034116-1)-21908000-046259-1","KSXZM":"正常考试","KCCJ":80,"XH":"20141000341","XN":"2016-2017","KCMC":"数据仓库与数据挖掘","RKJSGH":"046259","CJLRRQ":"20171031","XQM":"第一学期","SFCX":"否","JXBH":"16-1)-21908000-046259-1","XF":2,"ROW_ID":6,"KSXSM":"分散","JD":3,"KCH":"21908000"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"053280","CJBH":"2014100034116-1)-21102402-053280-1","KSXZM":"正常考试","KCCJ":81,"XH":"20141000341","XN":"2016-2017","KCMC":"地理信息系统B","RKJSGH":"053280","CJLRRQ":"20171031","XQM":"第一学期","SFCX":"否","JXBH":"16-1)-21102402-053280-1","XF":2.5,"ROW_ID":7,"KSXSM":"分散","JD":3,"KCH":"21102402"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"046159","CJBH":"2014100034116-2)-20823200-046159-1","KSXZM":"正常考试","KCCJ":82,"XH":"20141000341","XN":"2016-2017","KCMC":"商务智能","RKJSGH":"046159","CJLRRQ":"20171031","XQM":"第二学期","SFCX":"否","JXBH":"16-2)-20823200-046159-1","XF":2,"ROW_ID":8,"KSXSM":"集中","JD":3,"KCH":"20823200"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"057252","CJBH":"2014100034116-1)-20822800-057252-1","KSXZM":"正常考试","KCCJ":80,"XH":"20141000341","XN":"2016-2017","KCMC":"信息资源管理","RKJSGH":"057252","CJLRRQ":"20171031","XQM":"第一学期","SFCX":"否","JXBH":"16-1)-20822800-057252-1","XF":3,"ROW_ID":9,"KSXSM":"分散","JD":3,"KCH":"20822800"},{"CJLRSJ":"150651","KCDJCJM":"百级制","CJLRRH":"068179","CJBH":"2014100034116-1)-20823000-068179-1","KSXZM":"正常考试","KCCJ":85,"XH":"20141000341","XN":"2016-2017","KCMC":"信息系统项目管理","RKJSGH":"068179","CJLRRQ":"20171031","XQM":"第一学期","SFCX":"否","JXBH":"16-1)-20823000-068179-1","XF":3,"ROW_ID":10,"KSXSM":"集中","JD":3.5,"KCH":"20823000"}]
     * firstPage : 1
     * prePage : 0
     * nextPage : 2
     * lastPage : 2
     * isFirstPage : true
     * isLastPage : false
     * hasPreviousPage : false
     * hasNextPage : true
     * navigatePages : 8
     * navigatepageNums : [1,2]
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
         * CJLRSJ : 150651
         * KCDJCJM : 百级制
         * CJLRRH : 057241
         * CJBH : 2014100034116-2)-21608000-057241-1
         * KSXZM : 正常考试
         * KCCJ : 88
         * XH : 20141000341
         * XN : 2016-2017
         * KCMC : 经济管理应用文写作
         * RKJSGH : 057241
         * CJLRRQ : 20171031
         * XQM : 第二学期
         * SFCX : 否
         * JXBH : 16-2)-21608000-057241-1
         * XF : 2
         * ROW_ID : 1
         * KSXSM : 分散
         * JD : 3.5
         * KCH : 21608000
         */

        private String CJLRSJ;
        private String KCDJCJM;
        private String CJLRRH;
        private String CJBH;
        private String KSXZM;
        private int KCCJ;
        private String XH;
        private String XN;
        private String KCMC;
        private String RKJSGH;
        private String CJLRRQ;
        private String XQM;
        private String SFCX;
        private String JXBH;
        private float XF;
        private int ROW_ID;
        private String KSXSM;
        private double JD;
        private String KCH;

        public String getCJLRSJ() {
            return CJLRSJ;
        }

        public void setCJLRSJ(String CJLRSJ) {
            this.CJLRSJ = CJLRSJ;
        }

        public String getKCDJCJM() {
            return KCDJCJM;
        }

        public void setKCDJCJM(String KCDJCJM) {
            this.KCDJCJM = KCDJCJM;
        }

        public String getCJLRRH() {
            return CJLRRH;
        }

        public void setCJLRRH(String CJLRRH) {
            this.CJLRRH = CJLRRH;
        }

        public String getCJBH() {
            return CJBH;
        }

        public void setCJBH(String CJBH) {
            this.CJBH = CJBH;
        }

        public String getKSXZM() {
            return KSXZM;
        }

        public void setKSXZM(String KSXZM) {
            this.KSXZM = KSXZM;
        }

        public int getKCCJ() {
            return KCCJ;
        }

        public void setKCCJ(int KCCJ) {
            this.KCCJ = KCCJ;
        }

        public String getXH() {
            return XH;
        }

        public void setXH(String XH) {
            this.XH = XH;
        }

        public String getXN() {
            return XN;
        }

        public void setXN(String XN) {
            this.XN = XN;
        }

        public String getKCMC() {
            return KCMC;
        }

        public void setKCMC(String KCMC) {
            this.KCMC = KCMC;
        }

        public String getRKJSGH() {
            return RKJSGH;
        }

        public void setRKJSGH(String RKJSGH) {
            this.RKJSGH = RKJSGH;
        }

        public String getCJLRRQ() {
            return CJLRRQ;
        }

        public void setCJLRRQ(String CJLRRQ) {
            this.CJLRRQ = CJLRRQ;
        }

        public String getXQM() {
            return XQM;
        }

        public void setXQM(String XQM) {
            this.XQM = XQM;
        }

        public String getSFCX() {
            return SFCX;
        }

        public void setSFCX(String SFCX) {
            this.SFCX = SFCX;
        }

        public String getJXBH() {
            return JXBH;
        }

        public void setJXBH(String JXBH) {
            this.JXBH = JXBH;
        }

        public float getXF() {
            return XF;
        }

        public void setXF(float XF) {
            this.XF = XF;
        }

        public int getROW_ID() {
            return ROW_ID;
        }

        public void setROW_ID(int ROW_ID) {
            this.ROW_ID = ROW_ID;
        }

        public String getKSXSM() {
            return KSXSM;
        }

        public void setKSXSM(String KSXSM) {
            this.KSXSM = KSXSM;
        }

        public double getJD() {
            return JD;
        }

        public void setJD(double JD) {
            this.JD = JD;
        }

        public String getKCH() {
            return KCH;
        }

        public void setKCH(String KCH) {
            this.KCH = KCH;
        }
    }
}
