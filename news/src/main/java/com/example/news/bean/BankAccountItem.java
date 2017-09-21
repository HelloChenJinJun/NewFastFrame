package com.example.news.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/20      20:31
 * QQ:             1981367757
 */

public class BankAccountItem {

    /**
     * retcode : 0
     * errmsg : 查询成功！
     * card :
     */

    private String retcode;
    private String errmsg;
    private List<CardEntity> card;

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public void setCard(List<CardEntity> card) {
        this.card = card;
    }

    public String getRetcode() {
        return retcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public List<CardEntity> getCard() {
        return card;
    }

    public static class CardEntity {
        /**
         * account :
         * name :
         * unsettle_amount : 0
         * db_balance : 1781
         * acc_status : 0
         * lostflag : 0
         * freezeflag : 0
         * barflag : 0
         * idflag : 1
         * expdate : 20180910
         * cardtype : 800
         * cardname : 正式卡
         * bankacc :
         * sno :
         * phone :
         * certtype :
         * cert :
         * createdate : 20140825
         * autotrans_limite : 2000
         * autotrans_amt : 5000
         * autotrans_flag : 1
         * mscard : 0
         * scard_num : 0
         */

        private String account;
        private String name;
        private String unsettle_amount;
        private String db_balance;
        private String acc_status;
        private String lostflag;
        private String freezeflag;
        private String barflag;
        private String idflag;
        private String expdate;
        private String cardtype;
        private String cardname;
        private String bankacc;
        private String sno;
        private String phone;
        private String certtype;
        private String cert;
        private String createdate;
        private String autotrans_limite;
        private String autotrans_amt;
        private String autotrans_flag;
        private String mscard;
        private String scard_num;

        public void setAccount(String account) {
            this.account = account;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUnsettle_amount(String unsettle_amount) {
            this.unsettle_amount = unsettle_amount;
        }

        public void setDb_balance(String db_balance) {
            this.db_balance = db_balance;
        }

        public void setAcc_status(String acc_status) {
            this.acc_status = acc_status;
        }

        public void setLostflag(String lostflag) {
            this.lostflag = lostflag;
        }

        public void setFreezeflag(String freezeflag) {
            this.freezeflag = freezeflag;
        }

        public void setBarflag(String barflag) {
            this.barflag = barflag;
        }

        public void setIdflag(String idflag) {
            this.idflag = idflag;
        }

        public void setExpdate(String expdate) {
            this.expdate = expdate;
        }

        public void setCardtype(String cardtype) {
            this.cardtype = cardtype;
        }

        public void setCardname(String cardname) {
            this.cardname = cardname;
        }

        public void setBankacc(String bankacc) {
            this.bankacc = bankacc;
        }

        public void setSno(String sno) {
            this.sno = sno;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setCerttype(String certtype) {
            this.certtype = certtype;
        }

        public void setCert(String cert) {
            this.cert = cert;
        }

        public void setCreatedate(String createdate) {
            this.createdate = createdate;
        }

        public void setAutotrans_limite(String autotrans_limite) {
            this.autotrans_limite = autotrans_limite;
        }

        public void setAutotrans_amt(String autotrans_amt) {
            this.autotrans_amt = autotrans_amt;
        }

        public void setAutotrans_flag(String autotrans_flag) {
            this.autotrans_flag = autotrans_flag;
        }

        public void setMscard(String mscard) {
            this.mscard = mscard;
        }

        public void setScard_num(String scard_num) {
            this.scard_num = scard_num;
        }

        public String getAccount() {
            return account;
        }

        public String getName() {
            return name;
        }

        public String getUnsettle_amount() {
            return unsettle_amount;
        }

        public String getDb_balance() {
            return db_balance;
        }

        public String getAcc_status() {
            return acc_status;
        }

        public String getLostflag() {
            return lostflag;
        }

        public String getFreezeflag() {
            return freezeflag;
        }

        public String getBarflag() {
            return barflag;
        }

        public String getIdflag() {
            return idflag;
        }

        public String getExpdate() {
            return expdate;
        }

        public String getCardtype() {
            return cardtype;
        }

        public String getCardname() {
            return cardname;
        }

        public String getBankacc() {
            return bankacc;
        }

        public String getSno() {
            return sno;
        }

        public String getPhone() {
            return phone;
        }

        public String getCerttype() {
            return certtype;
        }

        public String getCert() {
            return cert;
        }

        public String getCreatedate() {
            return createdate;
        }

        public String getAutotrans_limite() {
            return autotrans_limite;
        }

        public String getAutotrans_amt() {
            return autotrans_amt;
        }

        public String getAutotrans_flag() {
            return autotrans_flag;
        }

        public String getMscard() {
            return mscard;
        }

        public String getScard_num() {
            return scard_num;
        }
    }
}
