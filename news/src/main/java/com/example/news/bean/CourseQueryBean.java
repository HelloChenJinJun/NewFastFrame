package com.example.news.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/24     17:19
 * QQ:         1981367757
 */

public class CourseQueryBean {


    /**
     * xskbsfxstkzt : 0
     * kblx : 7
     * xsxx : {"XH_ID":"20141000341","XQMMC":"1","XNM":"2017","XH":"20141000341","XKKG":"1","XKKGXQ":"1~('12')","KXKXXQ":"('12')","XNMC":"2017-2018","XQM":"3","XM":"陈锦军"}
     * sjkList : []
     * kbList : [{"cd_id":"JS0128","cdmc":"教三楼703","jc":"1-2节","jcor":"1-2","jcs":"1-2","jgh_id":"20089046200","jgpxzd":"1","jxb_id":"17-1)-20813500-046200-1","jxbmc":"(2017-2018-1)-20813500-20089046200-1","kch_id":"20813500","kcmc":"信息系统项目模拟","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王小林","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"1","xqjmc":"星期一","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"1-7周","zyfxmc":"无方向"},{"cd_id":"JS0069","cdmc":"教二楼609","jc":"1-2节","jcor":"1-2","jcs":"1-2","jgh_id":"20089046200","jgpxzd":"1","jxb_id":"17-1)-21915700-046200-1","jxbmc":"(2017-2018-1)-21915700-20089046200-1","kch_id":"21915700","kcmc":"协同计算","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王小林","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"1","xqjmc":"星期一","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"10-13周","zyfxmc":"无方向"},{"cd_id":"JS0153","cdmc":"综合楼211","jc":"3-4节","jcor":"3-4","jcs":"3-4","jgh_id":"20089046232","jgpxzd":"1","jxb_id":"17-1)-20823100-046232-1","jxbmc":"(2017-2018-1)-20823100-20089046232-1","kch_id":"20823100","kcmc":"决策支持系统","khfsmc":"考试","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王广民","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"1","xqjmc":"星期一","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"1-6周","zyfxmc":"无方向"},{"cd_id":"JS0188","cdmc":"综合楼413","jc":"1-4节","jcor":"1-4","jcs":"1-4","jgh_id":"20089046200","jgpxzd":"1","jxb_id":"17-1)-20813500-046200-1A","jxbmc":"(2017-2018-1)-20813500-20089046200-1A","kch_id":"20813500","kcmc":"信息系统项目模拟","khfsmc":"未安排","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王小林","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"2","xqjmc":"星期二","xqm":"3","xqmc":"校本部","xsdm":"02","zcd":"1-7周","zyfxmc":"无方向"},{"cd_id":"JS0052","cdmc":"教二楼503","jc":"5-6节","jcor":"5-6","jcs":"5-6","jgh_id":"20089057155","jgpxzd":"1","jxb_id":"17-1)-21004702-057155-1","jxbmc":"(2017-2018-1)-21004702-20089057155-1","kch_id":"21004702","kcmc":"经济预测与决策B","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"刘伟","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"2","xqjmc":"星期二","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"9-17周","zyfxmc":"无方向"},{"cd_id":"JS0187","cdmc":"综合楼316","jc":"9-12节","jcor":"9-12","jcs":"9-12","jgh_id":"20089046232","jgpxzd":"1","jxb_id":"17-1)-20823100-046232-1A","jxbmc":"(2017-2018-1)-20823100-20089046232-1A","kch_id":"20823100","kcmc":"决策支持系统","khfsmc":"未安排","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王广民","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"2","xqjmc":"星期二","xqm":"3","xqmc":"校本部","xsdm":"02","zcd":"3-4周,6-8周","zyfxmc":"无方向"},{"cd_id":"JS0142","cdmc":"综合楼111","jc":"1-2节","jcor":"1-4","jcs":"1-2","jgh_id":"20089046258","jgpxzd":"1","jxb_id":"17-1)-20802500-046258-1","jxbmc":"(2017-2018-1)-20802500-20089046258-1","kch_id":"20802500","kcmc":"电子商务网站开发","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"江毅","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"3","xqjmc":"星期三","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"1-9周","zyfxmc":"无方向"},{"cd_id":"JS0188","cdmc":"综合楼413","jc":"1-4节","jcor":"1-4","jcs":"1-4","jgh_id":"20089046258","jgpxzd":"1","jxb_id":"17-1)-20802500-046258-1A","jxbmc":"(2017-2018-1)-20802500-20089046258-1A","kch_id":"20802500","kcmc":"电子商务网站开发","khfsmc":"未安排","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"江毅","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"3","xqjmc":"星期三","xqm":"3","xqmc":"校本部","xsdm":"02","zcd":"10-13周","zyfxmc":"无方向"},{"cd_id":"JS0152","cdmc":"综合楼210","jc":"3-4节","jcor":"1-4","jcs":"3-4","jgh_id":"20089046232","jgpxzd":"1","jxb_id":"17-1)-20823100-046232-1","jxbmc":"(2017-2018-1)-20823100-20089046232-1","kch_id":"20823100","kcmc":"决策支持系统","khfsmc":"考试","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王广民","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"3","xqjmc":"星期三","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"1-6周","zyfxmc":"无方向"},{"cd_id":"JS0138","cdmc":"综合楼107","jc":"1-2节","jcor":"1-2","jcs":"1-2","jgh_id":"20089046200","jgpxzd":"1","jxb_id":"17-1)-21915700-046200-1","jxbmc":"(2017-2018-1)-21915700-20089046200-1","kch_id":"21915700","kcmc":"协同计算","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王小林","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"4","xqjmc":"星期四","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"10-13周","zyfxmc":"无方向"},{"cd_id":"JS0052","cdmc":"教二楼503","jc":"3-4节","jcor":"3-4","jcs":"3-4","jgh_id":"20089057155","jgpxzd":"1","jxb_id":"17-1)-21004702-057155-1","jxbmc":"(2017-2018-1)-21004702-20089057155-1","kch_id":"21004702","kcmc":"经济预测与决策B","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"刘伟","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"4","xqjmc":"星期四","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"9-17周","zyfxmc":"无方向"},{"cd_id":"JS0025","cdmc":"教一楼307","jc":"1-2节","jcor":"1-2","jcs":"1-2","jgh_id":"20089046258","jgpxzd":"1","jxb_id":"17-1)-20802500-046258-1","jxbmc":"(2017-2018-1)-20802500-20089046258-1","kch_id":"20802500","kcmc":"电子商务网站开发","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"江毅","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"5","xqjmc":"星期五","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"1-9周","zyfxmc":"无方向"},{"cd_id":"JS0021","cdmc":"教一楼303","jc":"5-6节","jcor":"5-6","jcs":"5-6","jgh_id":"20089046200","jgpxzd":"1","jxb_id":"17-1)-20813500-046200-1","jxbmc":"(2017-2018-1)-20813500-20089046200-1","kch_id":"20813500","kcmc":"信息系统项目模拟","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"王小林","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"5","xqjmc":"星期五","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"1-7周","zyfxmc":"无方向"},{"cd_id":"JS0094","cdmc":"教三楼110","jc":"1-2节","jcor":"1-2","jcs":"1-2","jgh_id":"20089068159","jgpxzd":"1","jxb_id":"17-1)-7200373x-068159-1","jxbmc":"(2017-2018-1)-7200373x-20089068159-1","kch_id":"7200373x","kcmc":"成功心理学","khfsmc":"考查","listnav":"false","localeKey":"zh_CN","pageable":true,"pkbj":"1","queryModel":{"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0},"rangeable":true,"rsdzjs":0,"sxbj":"0","totalResult":"0","userModel":{"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false},"xm":"陈作国","xnm":"2017","xqdm":"0","xqh_id":"1","xqj":"7","xqjmc":"星期日","xqm":"3","xqmc":"校本部","xsdm":"01","xslxbj":"*","zcd":"6-17周","zyfxmc":"无方向"}]
     * xkkg : true
     * xqjmcMap : {"1":"星期一","2":"星期二","3":"星期三","4":"星期四","5":"星期五","6":"星期六","7":"星期日"}
     */

    private String xskbsfxstkzt;
    private int kblx;
    private XsxxBean xsxx;
    private boolean xkkg;
    private XqjmcMapBean xqjmcMap;
    private List<?> sjkList;
    private List<KbListBean> kbList;

    public String getXskbsfxstkzt() {
        return xskbsfxstkzt;
    }

    public void setXskbsfxstkzt(String xskbsfxstkzt) {
        this.xskbsfxstkzt = xskbsfxstkzt;
    }

    public int getKblx() {
        return kblx;
    }

    public void setKblx(int kblx) {
        this.kblx = kblx;
    }

    public XsxxBean getXsxx() {
        return xsxx;
    }

    public void setXsxx(XsxxBean xsxx) {
        this.xsxx = xsxx;
    }

    public boolean isXkkg() {
        return xkkg;
    }

    public void setXkkg(boolean xkkg) {
        this.xkkg = xkkg;
    }

    public XqjmcMapBean getXqjmcMap() {
        return xqjmcMap;
    }

    public void setXqjmcMap(XqjmcMapBean xqjmcMap) {
        this.xqjmcMap = xqjmcMap;
    }

    public List<?> getSjkList() {
        return sjkList;
    }

    public void setSjkList(List<?> sjkList) {
        this.sjkList = sjkList;
    }

    public List<KbListBean> getKbList() {
        return kbList;
    }

    public void setKbList(List<KbListBean> kbList) {
        this.kbList = kbList;
    }

    public static class XsxxBean {
        /**
         * XH_ID : 20141000341
         * XQMMC : 1
         * XNM : 2017
         * XH : 20141000341
         * XKKG : 1
         * XKKGXQ : 1~('12')
         * KXKXXQ : ('12')
         * XNMC : 2017-2018
         * XQM : 3
         * XM : 陈锦军
         */

        private String XH_ID;
        private String XQMMC;
        private String XNM;
        private String XH;
        private String XKKG;
        private String XKKGXQ;
        private String KXKXXQ;
        private String XNMC;
        private String XQM;
        private String XM;

        public String getXH_ID() {
            return XH_ID;
        }

        public void setXH_ID(String XH_ID) {
            this.XH_ID = XH_ID;
        }

        public String getXQMMC() {
            return XQMMC;
        }

        public void setXQMMC(String XQMMC) {
            this.XQMMC = XQMMC;
        }

        public String getXNM() {
            return XNM;
        }

        public void setXNM(String XNM) {
            this.XNM = XNM;
        }

        public String getXH() {
            return XH;
        }

        public void setXH(String XH) {
            this.XH = XH;
        }

        public String getXKKG() {
            return XKKG;
        }

        public void setXKKG(String XKKG) {
            this.XKKG = XKKG;
        }

        public String getXKKGXQ() {
            return XKKGXQ;
        }

        public void setXKKGXQ(String XKKGXQ) {
            this.XKKGXQ = XKKGXQ;
        }

        public String getKXKXXQ() {
            return KXKXXQ;
        }

        public void setKXKXXQ(String KXKXXQ) {
            this.KXKXXQ = KXKXXQ;
        }

        public String getXNMC() {
            return XNMC;
        }

        public void setXNMC(String XNMC) {
            this.XNMC = XNMC;
        }

        public String getXQM() {
            return XQM;
        }

        public void setXQM(String XQM) {
            this.XQM = XQM;
        }

        public String getXM() {
            return XM;
        }

        public void setXM(String XM) {
            this.XM = XM;
        }
    }

    public static class XqjmcMapBean {
        /**
         * 1 : 星期一
         * 2 : 星期二
         * 3 : 星期三
         * 4 : 星期四
         * 5 : 星期五
         * 6 : 星期六
         * 7 : 星期日
         */

        @SerializedName("1")
        private String _$1;
        @SerializedName("2")
        private String _$2;
        @SerializedName("3")
        private String _$3;
        @SerializedName("4")
        private String _$4;
        @SerializedName("5")
        private String _$5;
        @SerializedName("6")
        private String _$6;
        @SerializedName("7")
        private String _$7;

        public String get_$1() {
            return _$1;
        }

        public void set_$1(String _$1) {
            this._$1 = _$1;
        }

        public String get_$2() {
            return _$2;
        }

        public void set_$2(String _$2) {
            this._$2 = _$2;
        }

        public String get_$3() {
            return _$3;
        }

        public void set_$3(String _$3) {
            this._$3 = _$3;
        }

        public String get_$4() {
            return _$4;
        }

        public void set_$4(String _$4) {
            this._$4 = _$4;
        }

        public String get_$5() {
            return _$5;
        }

        public void set_$5(String _$5) {
            this._$5 = _$5;
        }

        public String get_$6() {
            return _$6;
        }

        public void set_$6(String _$6) {
            this._$6 = _$6;
        }

        public String get_$7() {
            return _$7;
        }

        public void set_$7(String _$7) {
            this._$7 = _$7;
        }
    }

    public static class KbListBean {
        /**
         * cd_id : JS0128
         * cdmc : 教三楼703
         * jc : 1-2节
         * jcor : 1-2
         * jcs : 1-2
         * jgh_id : 20089046200
         * jgpxzd : 1
         * jxb_id : 17-1)-20813500-046200-1
         * jxbmc : (2017-2018-1)-20813500-20089046200-1
         * kch_id : 20813500
         * kcmc : 信息系统项目模拟
         * khfsmc : 考查
         * listnav : false
         * localeKey : zh_CN
         * pageable : true
         * pkbj : 1
         * queryModel : {"currentPage":1,"currentResult":0,"entityOrField":false,"limit":15,"offset":0,"pageNo":0,"pageSize":15,"showCount":10,"totalCount":0,"totalPage":0,"totalResult":0}
         * rangeable : true
         * rsdzjs : 0
         * sxbj : 0
         * totalResult : 0
         * userModel : {"monitor":false,"roleCount":0,"roleKeys":"","roleValues":"","status":0,"usable":false}
         * xm : 王小林
         * xnm : 2017
         * xqdm : 0
         * xqh_id : 1
         * xqj : 1
         * xqjmc : 星期一
         * xqm : 3
         * xqmc : 校本部
         * xsdm : 01
         * xslxbj : *
         * zcd : 1-7周
         * zyfxmc : 无方向
         */

        private String cd_id;
        private String cdmc;
        private String jc;
        private String jcor;
        private String jcs;
        private String jgh_id;
        private String jgpxzd;
        private String jxb_id;
        private String jxbmc;
        private String kch_id;
        private String kcmc;
        private String khfsmc;
        private String listnav;
        private String localeKey;
        private boolean pageable;
        private String pkbj;
        private QueryModelBean queryModel;
        private boolean rangeable;
        private int rsdzjs;
        private String sxbj;
        private String totalResult;
        private UserModelBean userModel;
        private String xm;
        private String xnm;
        private String xqdm;
        private String xqh_id;
        private String xqj;
        private String xqjmc;
        private String xqm;
        private String xqmc;
        private String xsdm;
        private String xslxbj;
        private String zcd;
        private String zyfxmc;

        public String getCd_id() {
            return cd_id;
        }

        public void setCd_id(String cd_id) {
            this.cd_id = cd_id;
        }

        public String getCdmc() {
            return cdmc;
        }

        public void setCdmc(String cdmc) {
            this.cdmc = cdmc;
        }

        public String getJc() {
            return jc;
        }

        public void setJc(String jc) {
            this.jc = jc;
        }

        public String getJcor() {
            return jcor;
        }

        public void setJcor(String jcor) {
            this.jcor = jcor;
        }

        public String getJcs() {
            return jcs;
        }

        public void setJcs(String jcs) {
            this.jcs = jcs;
        }

        public String getJgh_id() {
            return jgh_id;
        }

        public void setJgh_id(String jgh_id) {
            this.jgh_id = jgh_id;
        }

        public String getJgpxzd() {
            return jgpxzd;
        }

        public void setJgpxzd(String jgpxzd) {
            this.jgpxzd = jgpxzd;
        }

        public String getJxb_id() {
            return jxb_id;
        }

        public void setJxb_id(String jxb_id) {
            this.jxb_id = jxb_id;
        }

        public String getJxbmc() {
            return jxbmc;
        }

        public void setJxbmc(String jxbmc) {
            this.jxbmc = jxbmc;
        }

        public String getKch_id() {
            return kch_id;
        }

        public void setKch_id(String kch_id) {
            this.kch_id = kch_id;
        }

        public String getKcmc() {
            return kcmc;
        }

        public void setKcmc(String kcmc) {
            this.kcmc = kcmc;
        }

        public String getKhfsmc() {
            return khfsmc;
        }

        public void setKhfsmc(String khfsmc) {
            this.khfsmc = khfsmc;
        }

        public String getListnav() {
            return listnav;
        }

        public void setListnav(String listnav) {
            this.listnav = listnav;
        }

        public String getLocaleKey() {
            return localeKey;
        }

        public void setLocaleKey(String localeKey) {
            this.localeKey = localeKey;
        }

        public boolean isPageable() {
            return pageable;
        }

        public void setPageable(boolean pageable) {
            this.pageable = pageable;
        }

        public String getPkbj() {
            return pkbj;
        }

        public void setPkbj(String pkbj) {
            this.pkbj = pkbj;
        }

        public QueryModelBean getQueryModel() {
            return queryModel;
        }

        public void setQueryModel(QueryModelBean queryModel) {
            this.queryModel = queryModel;
        }

        public boolean isRangeable() {
            return rangeable;
        }

        public void setRangeable(boolean rangeable) {
            this.rangeable = rangeable;
        }

        public int getRsdzjs() {
            return rsdzjs;
        }

        public void setRsdzjs(int rsdzjs) {
            this.rsdzjs = rsdzjs;
        }

        public String getSxbj() {
            return sxbj;
        }

        public void setSxbj(String sxbj) {
            this.sxbj = sxbj;
        }

        public String getTotalResult() {
            return totalResult;
        }

        public void setTotalResult(String totalResult) {
            this.totalResult = totalResult;
        }

        public UserModelBean getUserModel() {
            return userModel;
        }

        public void setUserModel(UserModelBean userModel) {
            this.userModel = userModel;
        }

        public String getXm() {
            return xm;
        }

        public void setXm(String xm) {
            this.xm = xm;
        }

        public String getXnm() {
            return xnm;
        }

        public void setXnm(String xnm) {
            this.xnm = xnm;
        }

        public String getXqdm() {
            return xqdm;
        }

        public void setXqdm(String xqdm) {
            this.xqdm = xqdm;
        }

        public String getXqh_id() {
            return xqh_id;
        }

        public void setXqh_id(String xqh_id) {
            this.xqh_id = xqh_id;
        }

        public String getXqj() {
            return xqj;
        }

        public void setXqj(String xqj) {
            this.xqj = xqj;
        }

        public String getXqjmc() {
            return xqjmc;
        }

        public void setXqjmc(String xqjmc) {
            this.xqjmc = xqjmc;
        }

        public String getXqm() {
            return xqm;
        }

        public void setXqm(String xqm) {
            this.xqm = xqm;
        }

        public String getXqmc() {
            return xqmc;
        }

        public void setXqmc(String xqmc) {
            this.xqmc = xqmc;
        }

        public String getXsdm() {
            return xsdm;
        }

        public void setXsdm(String xsdm) {
            this.xsdm = xsdm;
        }

        public String getXslxbj() {
            return xslxbj;
        }

        public void setXslxbj(String xslxbj) {
            this.xslxbj = xslxbj;
        }

        public String getZcd() {
            return zcd;
        }

        public void setZcd(String zcd) {
            this.zcd = zcd;
        }

        public String getZyfxmc() {
            return zyfxmc;
        }

        public void setZyfxmc(String zyfxmc) {
            this.zyfxmc = zyfxmc;
        }

        public static class QueryModelBean {
            /**
             * currentPage : 1
             * currentResult : 0
             * entityOrField : false
             * limit : 15
             * offset : 0
             * pageNo : 0
             * pageSize : 15
             * showCount : 10
             * totalCount : 0
             * totalPage : 0
             * totalResult : 0
             */

            private int currentPage;
            private int currentResult;
            private boolean entityOrField;
            private int limit;
            private int offset;
            private int pageNo;
            private int pageSize;
            private int showCount;
            private int totalCount;
            private int totalPage;
            private int totalResult;

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getCurrentResult() {
                return currentResult;
            }

            public void setCurrentResult(int currentResult) {
                this.currentResult = currentResult;
            }

            public boolean isEntityOrField() {
                return entityOrField;
            }

            public void setEntityOrField(boolean entityOrField) {
                this.entityOrField = entityOrField;
            }

            public int getLimit() {
                return limit;
            }

            public void setLimit(int limit) {
                this.limit = limit;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getShowCount() {
                return showCount;
            }

            public void setShowCount(int showCount) {
                this.showCount = showCount;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }

            public int getTotalPage() {
                return totalPage;
            }

            public void setTotalPage(int totalPage) {
                this.totalPage = totalPage;
            }

            public int getTotalResult() {
                return totalResult;
            }

            public void setTotalResult(int totalResult) {
                this.totalResult = totalResult;
            }
        }

        public static class UserModelBean {
            /**
             * monitor : false
             * roleCount : 0
             * roleKeys :
             * roleValues :
             * status : 0
             * usable : false
             */

            private boolean monitor;
            private int roleCount;
            private String roleKeys;
            private String roleValues;
            private int status;
            private boolean usable;

            public boolean isMonitor() {
                return monitor;
            }

            public void setMonitor(boolean monitor) {
                this.monitor = monitor;
            }

            public int getRoleCount() {
                return roleCount;
            }

            public void setRoleCount(int roleCount) {
                this.roleCount = roleCount;
            }

            public String getRoleKeys() {
                return roleKeys;
            }

            public void setRoleKeys(String roleKeys) {
                this.roleKeys = roleKeys;
            }

            public String getRoleValues() {
                return roleValues;
            }

            public void setRoleValues(String roleValues) {
                this.roleValues = roleValues;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public boolean isUsable() {
                return usable;
            }

            public void setUsable(boolean usable) {
                this.usable = usable;
            }
        }
    }
}
