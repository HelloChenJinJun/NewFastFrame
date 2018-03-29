package com.rationalTiger.poster.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/1/17     17:00
 * QQ:         1981367757
 */
@Entity
public class UserEntity {
//    被别人添加的黑名单类型
    public static final int BLACK_TYPE_OTHER=1;
//    添加别人的黑名单类型
    public static final int BLACK_TYPE_ADD=2;
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


//    是否为陌生人
    private boolean isStranger;


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

    public boolean isSex() {
        return isSex;
    }

    public void setSex(boolean sex) {
        isSex = sex;
    }

    private String createdTime;

    private String updatedTime;

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

    //    是否为黑名单
    private boolean isBlack;
//   黑名单类型
    private int blackType;


    private int avatarType;


    public int getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(int avatarType) {
        this.avatarType = avatarType;
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

    @Generated(hash = 722382895)
    public UserEntity(String uid, String nick, String avatar, boolean isSex,
            String titlePaper, boolean isStranger, String createdTime,
            String updatedTime, boolean isBlack, int blackType, int avatarType) {
        this.uid = uid;
        this.nick = nick;
        this.avatar = avatar;
        this.isSex = isSex;
        this.titlePaper = titlePaper;
        this.isStranger = isStranger;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.isBlack = isBlack;
        this.blackType = blackType;
        this.avatarType = avatarType;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
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

    public boolean getIsSex() {
        return this.isSex;
    }

    public void setIsSex(boolean isSex) {
        this.isSex = isSex;
    }

    public boolean getIsStranger() {
        return this.isStranger;
    }

    public void setIsStranger(boolean isStranger) {
        this.isStranger = isStranger;
    }
}
