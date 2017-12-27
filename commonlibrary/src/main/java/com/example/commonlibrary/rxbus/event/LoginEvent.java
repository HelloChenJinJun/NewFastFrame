package com.example.commonlibrary.rxbus.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/27     13:42
 * QQ:         1981367757
 */

public class LoginEvent {
    private boolean isSuccess=false;
    private UserInfoEvent userInfoEvent;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public UserInfoEvent getUserInfoEvent() {
        return userInfoEvent;
    }

    public void setUserInfoEvent(UserInfoEvent userInfoEvent) {
        this.userInfoEvent = userInfoEvent;
    }
}
