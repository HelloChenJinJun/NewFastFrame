package com.snew.video.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     20:34
 */
public class SearchVideoBean implements Serializable {

    /**
     * head : {"ab_result":"1073741824","error":0,"key":"是是","mix":1,"num":2,"qid":"o_1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY_3Vjl721nhgOy00pxfTnxQ","report":"action=105&platform=10201&qid=o%5F1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY%5F3Vjl721nhgOy00pxfTnxQ&srh_platform=2&ival1=0&ival2=7&ival7=11&sval1=%E6%98%AF%E6%98%AF&sval2=&sval3=&sval8=145_&sval4=16250777660237649839+3274620206050852402","result_type":"251"}
     * item : [{"ar":"内地","class":"电视剧","da":2018,"dc":"http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/9/9w9tk4ybmnb77bz.jpg","direct_report":"action=101&platform=10201&qid=o%5F1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY%5F3Vjl721nhgOy00pxfTnxQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E6%98%AF%E6%98%AF&sval2=%E5%A4%A7%E7%BA%A6%E6%98%AF%E7%88%B1&sval3=1&sval8=145_&sval4=16250777660237649839+3274620206050852402","ee":18,"et":"About is love","ex":{"title":"更新至18集","typeid":2},"id":"9w9tk4ybmnb77bz","idType":2,"imgtag":"{\"tag_1\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_2\":{\"id\":\"-1\",\"param\":\"\",\"param2\":\"\",\"text\":\"\"},\"tag_3\":{\"id\":\"30001\",\"param\":\"figure_mask\",\"param2\":\"\",\"text\":\"更新至18集\"},\"tag_4\":{\"id\":\"40003\",\"param\":\"mark_sd\",\"param2\":\"\",\"text\":\"蓝光\"}}","itemType":1,"markLabelList":"","pa":"彦希 许晓诺 蔡乙嘉","pd":"许珮珊","ps":1,"report":"action=101&platform=10201&qid=o%5F1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY%5F3Vjl721nhgOy00pxfTnxQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E6%98%AF%E6%98%AF&sval2=%E5%A4%A7%E7%BA%A6%E6%98%AF%E7%88%B1&sval3=16250777660237649839&sval8=145_&sval4=16250777660237649839+3274620206050852402","sn":"爱情 都市 ","ss":1,"te":30,"title":"大约<em  class=\"c_txt3\">是<\/em>爱","tt":"大约是爱","url":"https://v.qq.com/x/cover/9w9tk4ybmnb77bz.html","word":"大约是爱"},{"class":"电视剧","ex":{"title":"","typeid":2},"id":"sdp00194eslovxh","idType":2,"itemType":1,"markLabelList":[{"cssText":"","markImageUrl":"http://i.gtimg.cn/qqlive/images/mark/mark_2.png","position":1,"primeText":"预告","type":2}],"ps":0,"report":"action=101&platform=10201&qid=o%5F1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY%5F3Vjl721nhgOy00pxfTnxQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E6%98%AF%E6%98%AF&sval2=%E7%9F%A5%E5%90%A6%E7%9F%A5%E5%90%A6%E5%BA%94%E6%98%AF%E7%BB%BF%E8%82%A5%E7%BA%A2%E7%98%A6&sval3=3274620206050852402&sval8=145_&sval4=16250777660237649839+3274620206050852402","title":"知否知否应<em  class=\"c_txt3\">是<\/em>绿肥红瘦","url":"https://v.qq.com/x/cover/sdp00194eslovxh.html","word":"知否知否应是绿肥红瘦"}]
     * result : {"code":0,"msg":""}
     */

    private HeadBean head;
    private ResultBean result;
    private List<ItemBean> item;

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class HeadBean implements Serializable {
        /**
         * ab_result : 1073741824
         * error : 0
         * key : 是是
         * mix : 1
         * num : 2
         * qid : o_1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY_3Vjl721nhgOy00pxfTnxQ
         * report : action=105&platform=10201&qid=o%5F1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY%5F3Vjl721nhgOy00pxfTnxQ&srh_platform=2&ival1=0&ival2=7&ival7=11&sval1=%E6%98%AF%E6%98%AF&sval2=&sval3=&sval8=145_&sval4=16250777660237649839+3274620206050852402
         * result_type : 251
         */

        private String ab_result;
        private int error;
        private String key;
        private int mix;
        private int num;
        private String qid;
        private String report;
        private String result_type;

        public String getAb_result() {
            return ab_result;
        }

        public void setAb_result(String ab_result) {
            this.ab_result = ab_result;
        }

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getMix() {
            return mix;
        }

        public void setMix(int mix) {
            this.mix = mix;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getQid() {
            return qid;
        }

        public void setQid(String qid) {
            this.qid = qid;
        }

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getResult_type() {
            return result_type;
        }

        public void setResult_type(String result_type) {
            this.result_type = result_type;
        }
    }

    public static class ResultBean implements Serializable{
        /**
         * code : 0
         * msg :
         */

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class ItemBean implements Serializable {
        /**
         * ar : 内地
         * class : 电视剧
         * da : 2018
         * dc : http://i.gtimg.cn/qqlive/img/jpgcache/files/qqvideo/hori/9/9w9tk4ybmnb77bz.jpg
         * direct_report : action=101&platform=10201&qid=o%5F1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY%5F3Vjl721nhgOy00pxfTnxQ&srh_platform=2&ival1=1&ival2=1&ival7=11&sval1=%E6%98%AF%E6%98%AF&sval2=%E5%A4%A7%E7%BA%A6%E6%98%AF%E7%88%B1&sval3=1&sval8=145_&sval4=16250777660237649839+3274620206050852402
         * ee : 18
         * et : About is love
         * ex : {"title":"更新至18集","typeid":2}
         * id : 9w9tk4ybmnb77bz
         * idType : 2
         * imgtag : {"tag_1":{"id":"-1","param":"","param2":"","text":""},"tag_2":{"id":"-1","param":"","param2":"","text":""},"tag_3":{"id":"30001","param":"figure_mask","param2":"","text":"更新至18集"},"tag_4":{"id":"40003","param":"mark_sd","param2":"","text":"蓝光"}}
         * itemType : 1
         * markLabelList :
         * pa : 彦希 许晓诺 蔡乙嘉
         * pd : 许珮珊
         * ps : 1
         * report : action=101&platform=10201&qid=o%5F1c7EZ0xJmvSdN1ftB3ihIiu4yfaiQY%5F3Vjl721nhgOy00pxfTnxQ&srh_platform=2&ival1=1&ival2=7&ival7=11&sval1=%E6%98%AF%E6%98%AF&sval2=%E5%A4%A7%E7%BA%A6%E6%98%AF%E7%88%B1&sval3=16250777660237649839&sval8=145_&sval4=16250777660237649839+3274620206050852402
         * sn : 爱情 都市
         * ss : 1
         * te : 30
         * title : 大约<em  class="c_txt3">是</em>爱
         * tt : 大约是爱
         * url : https://v.qq.com/x/cover/9w9tk4ybmnb77bz.html
         * word : 大约是爱
         */

        private String ar;
        @SerializedName("class")
        private String classX;
        private int da;
        private String dc;
        private String direct_report;
        private int ee;
        private String et;
        private ExBean ex;
        private String id;
        private int idType;
        private String imgtag;
        private int itemType;
        private String markLabelList;
        private String pa;
        private String pd;
        private int ps;
        private String report;
        private String sn;
        private int ss;
        private int te;
        private String title;
        private String tt;
        private String url;
        private String word;

        public String getAr() {
            return ar;
        }

        public void setAr(String ar) {
            this.ar = ar;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public int getDa() {
            return da;
        }

        public void setDa(int da) {
            this.da = da;
        }

        public String getDc() {
            return dc;
        }

        public void setDc(String dc) {
            this.dc = dc;
        }

        public String getDirect_report() {
            return direct_report;
        }

        public void setDirect_report(String direct_report) {
            this.direct_report = direct_report;
        }

        public int getEe() {
            return ee;
        }

        public void setEe(int ee) {
            this.ee = ee;
        }

        public String getEt() {
            return et;
        }

        public void setEt(String et) {
            this.et = et;
        }

        public ExBean getEx() {
            return ex;
        }

        public void setEx(ExBean ex) {
            this.ex = ex;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIdType() {
            return idType;
        }

        public void setIdType(int idType) {
            this.idType = idType;
        }

        public String getImgtag() {
            return imgtag;
        }

        public void setImgtag(String imgtag) {
            this.imgtag = imgtag;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getMarkLabelList() {
            return markLabelList;
        }

        public void setMarkLabelList(String markLabelList) {
            this.markLabelList = markLabelList;
        }

        public String getPa() {
            return pa;
        }

        public void setPa(String pa) {
            this.pa = pa;
        }

        public String getPd() {
            return pd;
        }

        public void setPd(String pd) {
            this.pd = pd;
        }

        public int getPs() {
            return ps;
        }

        public void setPs(int ps) {
            this.ps = ps;
        }

        public String getReport() {
            return report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getSs() {
            return ss;
        }

        public void setSs(int ss) {
            this.ss = ss;
        }

        public int getTe() {
            return te;
        }

        public void setTe(int te) {
            this.te = te;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTt() {
            return tt;
        }

        public void setTt(String tt) {
            this.tt = tt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public static class ExBean {
            /**
             * title : 更新至18集
             * typeid : 2
             */

            private String title;
            private int typeid;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getTypeid() {
                return typeid;
            }

            public void setTypeid(int typeid) {
                this.typeid = typeid;
            }
        }
    }
}
