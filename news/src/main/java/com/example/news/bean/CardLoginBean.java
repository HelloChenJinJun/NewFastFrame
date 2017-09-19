package com.example.news.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      20:54
 * QQ:             1981367757
 */

public class CardLoginBean {

    /**
     * IsSucceed : true
     * Msg : null
     * RMsg : null
     * Obj : 99225
     * Obj2 : null
     */

    private boolean IsSucceed;
    private Object Msg;
    private Object RMsg;
    private int Obj;
    private Object Obj2;

    public boolean isIsSucceed() {
        return IsSucceed;
    }

    public void setIsSucceed(boolean IsSucceed) {
        this.IsSucceed = IsSucceed;
    }

    public Object getMsg() {
        return Msg;
    }

    public void setMsg(Object Msg) {
        this.Msg = Msg;
    }

    public Object getRMsg() {
        return RMsg;
    }

    public void setRMsg(Object RMsg) {
        this.RMsg = RMsg;
    }

    public int getObj() {
        return Obj;
    }

    public void setObj(int Obj) {
        this.Obj = Obj;
    }

    public Object getObj2() {
        return Obj2;
    }

    public void setObj2(Object Obj2) {
        this.Obj2 = Obj2;
    }
}
