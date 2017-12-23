package com.example.news.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/23     18:35
 * QQ:         1981367757
 */

public class SystemUserBean {


    /**
     * BIRTHDAY : 810144000000
     * IS_MAIN : 1
     * CARD_TYPE : 1
     * UNIT_NAME : 经济管理学院
     * ID_TYPE : 本科生
     * USER_TYPE : 1
     * vcardPath : uploadfiles/profile/vcard/1/20170707013922.png
     * RESOURCE_ID : 20170707013922
     * UNIT_UID : 108
     * USER_SEX : 男
     * IS_ACTIVE : 正常
     * ID_NUMBER : 学号
     * USER_PWD :密码
     * CARD_ID : 身份证
     * BEGIN_TIME : 1499412648000
     * MOBILE : 手机号码
     * USER_NAME : 陈锦军
     * GRADUATION_PIC : uploadfiles/profile/userExt/graduation/20141000341.jpg
     * END_TIME : 1893427200000
     * SCHOOL_PIC : uploadfiles/profile/userExt/school/20141000341.jpg
     * ADMISSIONS_PIC : uploadfiles/profile/userExt/admission/20141000341.jpg
     */

    private long BIRTHDAY;
    private String IS_MAIN;
    private String CARD_TYPE;
    private String UNIT_NAME;
    private String ID_TYPE;
    private String USER_TYPE;
    private String vcardPath;
    private String RESOURCE_ID;
    private String UNIT_UID;
    private String USER_SEX;
    private String IS_ACTIVE;
    private String ID_NUMBER;
    private String USER_PWD;
    private String CARD_ID;
    private long BEGIN_TIME;
    private String MOBILE;
    private String USER_NAME;
    private String GRADUATION_PIC;
    private long END_TIME;
    private String SCHOOL_PIC;
    private String ADMISSIONS_PIC;

    public long getBIRTHDAY() {
        return BIRTHDAY;
    }

    public void setBIRTHDAY(long BIRTHDAY) {
        this.BIRTHDAY = BIRTHDAY;
    }

    public String getIS_MAIN() {
        return IS_MAIN;
    }

    public void setIS_MAIN(String IS_MAIN) {
        this.IS_MAIN = IS_MAIN;
    }

    public String getCARD_TYPE() {
        return CARD_TYPE;
    }

    public void setCARD_TYPE(String CARD_TYPE) {
        this.CARD_TYPE = CARD_TYPE;
    }

    public String getUNIT_NAME() {
        return UNIT_NAME;
    }

    public void setUNIT_NAME(String UNIT_NAME) {
        this.UNIT_NAME = UNIT_NAME;
    }

    public String getID_TYPE() {
        return ID_TYPE;
    }

    public void setID_TYPE(String ID_TYPE) {
        this.ID_TYPE = ID_TYPE;
    }

    public String getUSER_TYPE() {
        return USER_TYPE;
    }

    public void setUSER_TYPE(String USER_TYPE) {
        this.USER_TYPE = USER_TYPE;
    }

    public String getVcardPath() {
        return vcardPath;
    }

    public void setVcardPath(String vcardPath) {
        this.vcardPath = vcardPath;
    }

    public String getRESOURCE_ID() {
        return RESOURCE_ID;
    }

    public void setRESOURCE_ID(String RESOURCE_ID) {
        this.RESOURCE_ID = RESOURCE_ID;
    }

    public String getUNIT_UID() {
        return UNIT_UID;
    }

    public void setUNIT_UID(String UNIT_UID) {
        this.UNIT_UID = UNIT_UID;
    }

    public String getUSER_SEX() {
        return USER_SEX;
    }

    public void setUSER_SEX(String USER_SEX) {
        this.USER_SEX = USER_SEX;
    }

    public String getIS_ACTIVE() {
        return IS_ACTIVE;
    }

    public void setIS_ACTIVE(String IS_ACTIVE) {
        this.IS_ACTIVE = IS_ACTIVE;
    }

    public String getID_NUMBER() {
        return ID_NUMBER;
    }

    public void setID_NUMBER(String ID_NUMBER) {
        this.ID_NUMBER = ID_NUMBER;
    }

    public String getUSER_PWD() {
        return USER_PWD;
    }

    public void setUSER_PWD(String USER_PWD) {
        this.USER_PWD = USER_PWD;
    }

    public String getCARD_ID() {
        return CARD_ID;
    }

    public void setCARD_ID(String CARD_ID) {
        this.CARD_ID = CARD_ID;
    }

    public long getBEGIN_TIME() {
        return BEGIN_TIME;
    }

    public void setBEGIN_TIME(long BEGIN_TIME) {
        this.BEGIN_TIME = BEGIN_TIME;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getGRADUATION_PIC() {
        return GRADUATION_PIC;
    }

    public void setGRADUATION_PIC(String GRADUATION_PIC) {
        this.GRADUATION_PIC = GRADUATION_PIC;
    }

    public long getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(long END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getSCHOOL_PIC() {
        return "http://xyfw.cug.edu.cn/tp_up/"+SCHOOL_PIC;
    }

    public void setSCHOOL_PIC(String SCHOOL_PIC) {
        this.SCHOOL_PIC = SCHOOL_PIC;
    }

    public String getADMISSIONS_PIC() {
        return ADMISSIONS_PIC;
    }

    public void setADMISSIONS_PIC(String ADMISSIONS_PIC) {
        this.ADMISSIONS_PIC = ADMISSIONS_PIC;
    }
}
