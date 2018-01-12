package com.example.commonlibrary.rxbus.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     15:45
 * QQ:         1981367757
 */

public class UserInfoEvent {
    private String account;
    private String password;
    private String from;
    private String avatar;
    private String name;
    private String nick;
    private Boolean sex;
//    本科生
    private String studentType;
//    学院
    private String college;

//    班号
    private String classNumber;



    private String school;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    //    专业
    private String major;

//    入学年份
    private String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private String allBg;
    private String halfBg;

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getAllBg() {
        return allBg;
    }

    public void setAllBg(String allBg) {
        this.allBg = allBg;
    }

    public String getHalfBg() {
        return halfBg;
    }

    public void setHalfBg(String halfBg) {
        this.halfBg = halfBg;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "UserInfoEvent{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", from='" + from + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", nick='" + nick + '\'' +
                ", sex=" + sex +
                ", studentType='" + studentType + '\'' +
                ", college='" + college + '\'' +
                '}';
    }
}
