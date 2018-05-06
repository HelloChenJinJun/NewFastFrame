package com.example.chat.bean;

import com.example.commonlibrary.utils.AppUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      17:08
 * QQ:             1981367757
 */
public class User extends BmobUser implements Comparable<User> {
        private static final long serialVersionUID = 1L;
        /**
         * 地址
         */
        private String address;
        private String installId;//设备Id
        //        昵称
        private String nick;
        //        头像信息
        private String avatar;
        //        好友联系人
        private BmobRelation contacts;


        //        主动添加的黑名单
        private BmobRelation addBlack;

//        被动添加的黑名单
        private BmobRelation otherBlack;

        public BmobRelation getAddBlack() {
                return addBlack;
        }

        public void setAddBlack(BmobRelation addBlack) {
                this.addBlack = addBlack;
        }

        public BmobRelation getOtherBlack() {
                return otherBlack;
        }

        public void setOtherBlack(BmobRelation otherBlack) {
                this.otherBlack = otherBlack;
        }

        //        设备类型
        private String deviceType;

        private String birthDay;
        private String titleWallPaper;
        private String wallPaper;
        private String pw;

        public String getPw() {
                return pw;
        }

        public void setPw(String pw) {
                this.pw = pw;
        }

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

        public String getClassNumber() {
                return classNumber;
        }

        public void setClassNumber(String classNumber) {
                this.classNumber = classNumber;
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

        public String getTitleWallPaper() {
                return titleWallPaper;
        }

        public void setTitleWallPaper(String titleWallPaper) {
                this.titleWallPaper = titleWallPaper;
        }



        public String getWallPaper() {
                return wallPaper;
        }

        public void setWallPaper(String wallPaper) {
                this.wallPaper = wallPaper;
        }

        /**
         * 用于获取附近人的信息
         */
        private BmobGeoPoint location;

        public BmobGeoPoint getLocation() {
                return location;
        }

        public void setLocation(BmobGeoPoint location) {
                this.location = location;
        }

        private String signature;


        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public String getSignature() {
                return signature;
        }

        public void setSignature(String signature) {
                this.signature = signature;
        }

        public String getInstallId() {
                return installId;
        }

        public void setInstallId(String installId) {
                this.installId = installId;
        }

        /**
         * 性别
         */
        private Boolean sex;

        public String getBirthDay() {
                return birthDay;
        }

        public void setBirthDay(String birthDay) {
                this.birthDay = birthDay;
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

        public BmobRelation getContacts() {
                return contacts;
        }

        public void setContacts(BmobRelation contacts) {
                this.contacts = contacts;
        }

        public String getDeviceType() {
                return deviceType;
        }

        public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
        }



        public boolean isSex() {
                return sex;
        }

        public void setSex(boolean sex) {
                this.sex = sex;
        }

        @Override
        public int compareTo(User another) {
                return AppUtil.getSortedKey(getName()).compareTo(AppUtil.getSortedKey(another.getName()));
        }


        @Override
        public boolean equals(Object obj) {
                if (obj instanceof User) {
                        User user= (User) obj;
                        return user.getObjectId().equals(getObjectId());
                }
                return false;
        }

        public void setCreatedTime(String createdTime) {
                setCreatedAt(createdTime);
        }
}
