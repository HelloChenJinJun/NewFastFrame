package com.example.commonlibrary.bean.chat;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/17     17:00
 * QQ:         1981367757
 * 总共20字段
 */
@Entity
public class UserEntity {
//    被别人添加的黑名单类型
    public static final int BLACK_TYPE_OTHER=1;
//    添加别人的黑名单类型
    public static final int BLACK_TYPE_ADD=2;

    public static final int BLACK_TYPE_NORMAL=0;
//    用户ID
    @Id
    private String uid;
//    昵称
    private String nick;
//    头像
    private String avatar;


    private boolean isSex;
    //        封面
    private String titlePaper;

    /**
     * 地址
     */
    private String address;
    private String birthDay;
    //        真实姓名
    private String name;
    //        学校
    private String school;
    //        学院
    private String college;
    //        专业
    private String major;
    //        学历
    private String education;
    //        入学年份
    private String year;
    //        班号
    private String classNumber;
//    签名
    private String signature;
    //    是否为黑名单
    private boolean isBlack;
    //   黑名单类型
    private int blackType;

//    是否为陌生人
    private boolean isStranger;
//    用户名
    private String userName;


    private String phone;
    private String email;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isStranger() {
        return isStranger;
    }

    public void setStranger(boolean stranger) {
        isStranger = stranger;
    }

    public String getTitlePaper() {
        return titlePaper;
    }

    public void setTitlePaper(String titlePaper) {
        this.titlePaper = titlePaper;
    }

    public Boolean isSex() {
        return isSex;
    }

    public void setSex(Boolean sex) {
        isSex = sex;
    }

    private String createdTime;

    private String updatedTime;



    @Generated(hash = 1815163669)
    public UserEntity(String uid, String nick, String avatar, boolean isSex,
                      String titlePaper, String address, String birthDay, String name,
                      String school, String college, String major, String education,
                      String year, String classNumber, String signature, boolean isBlack,
                      int blackType, boolean isStranger, String userName, String phone,
                      String email, String createdTime, String updatedTime) {
        this.uid = uid;
        this.nick = nick;
        this.avatar = avatar;
        this.isSex = isSex;
        this.titlePaper = titlePaper;
        this.address = address;
        this.birthDay = birthDay;
        this.name = name;
        this.school = school;
        this.college = college;
        this.major = major;
        this.education = education;
        this.year = year;
        this.classNumber = classNumber;
        this.signature = signature;
        this.isBlack = isBlack;
        this.blackType = blackType;
        this.isStranger = isStranger;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }







    public int getBlackType() {
        return blackType;
    }

    public void setBlackType(int blackType) {
        this.blackType = blackType;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }




    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean getIsBlack() {
        return this.isBlack;
    }

    public void setIsBlack(boolean isBlack) {
        this.isBlack = isBlack;
    }



    public boolean getIsStranger() {
        return this.isStranger;
    }

    public void setIsStranger(boolean isStranger) {
        this.isStranger = isStranger;
    }

    public boolean getIsSex() {
        return this.isSex;
    }

    public void setIsSex(boolean isSex) {
        this.isSex = isSex;
    }

    @Override
    public boolean equals(Object obj) {
        return obj!=null&&obj instanceof UserEntity
                && ((UserEntity) obj).getUid()
                .equals(getUid());
    }
}
