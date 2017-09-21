package com.example.news.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/21      12:51
 * QQ:             1981367757
 */

public class CardPayHistoryBean {

    /**
     * issucceed : false
     * name : null
     * total : 6
     * tranamt : 0
     * tranamt1 : 0
     * tranamt2 : 0
     * parm1 : null
     * parm2 : null
     * trannum : 0
     * rows : [{"RO":1,"OCCTIME":"2017-09-21 12:39:53","MERCNAME":"","TRANAMT":1,"TRANNAME":"银行转账                                ","CARDBAL":9.51,"JDESC":"","JNUM":15506,"MACCOUNT":"","F1":"0","F2":"00","F3":"1"},{"RO":2,"OCCTIME":"2017-09-21 12:31:50","MERCNAME":"","TRANAMT":1,"TRANNAME":"银行转账                                ","CARDBAL":9.51,"JDESC":"","JNUM":15378,"MACCOUNT":"","F1":"0","F2":"00","F3":"1"},{"RO":3,"OCCTIME":"2017-09-21 11:22:22","MERCNAME":"","TRANAMT":2,"TRANNAME":"补助流水                                ","CARDBAL":19.81,"JDESC":"###","JNUM":359463,"MACCOUNT":"","F1":"0","F2":"00","F3":"0"},{"RO":4,"OCCTIME":"2017-09-21 11:22:22","MERCNAME":"北区食堂二楼                            ","TRANAMT":-10.3,"TRANNAME":"持卡人消费                              ","CARDBAL":9.51,"JDESC":"","JNUM":359464,"MACCOUNT":1000108,"F1":"1","F2":"11","F3":"2"},{"RO":5,"OCCTIME":"2017-09-21 09:21:50","MERCNAME":"","TRANAMT":1,"TRANNAME":"银行转账                                ","CARDBAL":17.81,"JDESC":"","JNUM":10287,"MACCOUNT":"","F1":"0","F2":"00","F3":"1"},{"RO":6,"OCCTIME":"2017-09-21 09:20:22","MERCNAME":"","TRANAMT":1,"TRANNAME":"银行转账                                ","CARDBAL":17.81,"JDESC":"","JNUM":10280,"MACCOUNT":"","F1":"0","F2":"00","F3":"1"}]
     * footer : null
     */

    private boolean issucceed;
    private Object name;
    private int total;
    private int tranamt;
    private int tranamt1;
    private int tranamt2;
    private Object parm1;
    private Object parm2;
    private int trannum;
    private Object footer;
    private List<RowsEntity> rows;

    public boolean isIssucceed() {
        return issucceed;
    }

    public void setIssucceed(boolean issucceed) {
        this.issucceed = issucceed;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTranamt() {
        return tranamt;
    }

    public void setTranamt(int tranamt) {
        this.tranamt = tranamt;
    }

    public int getTranamt1() {
        return tranamt1;
    }

    public void setTranamt1(int tranamt1) {
        this.tranamt1 = tranamt1;
    }

    public int getTranamt2() {
        return tranamt2;
    }

    public void setTranamt2(int tranamt2) {
        this.tranamt2 = tranamt2;
    }

    public Object getParm1() {
        return parm1;
    }

    public void setParm1(Object parm1) {
        this.parm1 = parm1;
    }

    public Object getParm2() {
        return parm2;
    }

    public void setParm2(Object parm2) {
        this.parm2 = parm2;
    }

    public int getTrannum() {
        return trannum;
    }

    public void setTrannum(int trannum) {
        this.trannum = trannum;
    }

    public Object getFooter() {
        return footer;
    }

    public void setFooter(Object footer) {
        this.footer = footer;
    }

    public List<RowsEntity> getRows() {
        return rows;
    }

    public void setRows(List<RowsEntity> rows) {
        this.rows = rows;
    }

    public static class RowsEntity {
        /**
         * RO : 1
         * OCCTIME : 2017-09-21 12:39:53
         * MERCNAME :
         * TRANAMT : 1
         * TRANNAME : 银行转账
         * CARDBAL : 9.51
         * JDESC :
         * JNUM : 15506
         * MACCOUNT :
         * F1 : 0
         * F2 : 00
         * F3 : 1
         */

        private int RO;
        private String OCCTIME;
        private String MERCNAME;
        private int TRANAMT;
        private String TRANNAME;
        private double CARDBAL;
        private String JDESC;
        private int JNUM;
        private String MACCOUNT;
        private String F1;
        private String F2;
        private String F3;

        public int getRO() {
            return RO;
        }

        public void setRO(int RO) {
            this.RO = RO;
        }

        public String getOCCTIME() {
            return OCCTIME;
        }

        public void setOCCTIME(String OCCTIME) {
            this.OCCTIME = OCCTIME;
        }

        public String getMERCNAME() {
            return MERCNAME;
        }

        public void setMERCNAME(String MERCNAME) {
            this.MERCNAME = MERCNAME;
        }

        public int getTRANAMT() {
            return TRANAMT;
        }

        public void setTRANAMT(int TRANAMT) {
            this.TRANAMT = TRANAMT;
        }

        public String getTRANNAME() {
            return TRANNAME;
        }

        public void setTRANNAME(String TRANNAME) {
            this.TRANNAME = TRANNAME;
        }

        public double getCARDBAL() {
            return CARDBAL;
        }

        public void setCARDBAL(double CARDBAL) {
            this.CARDBAL = CARDBAL;
        }

        public String getJDESC() {
            return JDESC;
        }

        public void setJDESC(String JDESC) {
            this.JDESC = JDESC;
        }

        public int getJNUM() {
            return JNUM;
        }

        public void setJNUM(int JNUM) {
            this.JNUM = JNUM;
        }

        public String getMACCOUNT() {
            return MACCOUNT;
        }

        public void setMACCOUNT(String MACCOUNT) {
            this.MACCOUNT = MACCOUNT;
        }

        public String getF1() {
            return F1;
        }

        public void setF1(String F1) {
            this.F1 = F1;
        }

        public String getF2() {
            return F2;
        }

        public void setF2(String F2) {
            this.F2 = F2;
        }

        public String getF3() {
            return F3;
        }

        public void setF3(String F3) {
            this.F3 = F3;
        }
    }
}
