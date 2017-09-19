package com.example.news.bean;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      22:35
 * QQ:             1981367757
 */

public class BankCardInfoBean {


    /**
     * IsSucceed : false
     * Msg : {"query_card":{}}
     * RMsg : null
     * Obj : null
     * Obj2 : null
     */

    private boolean IsSucceed;
    private BankCardItem Msg;
    private Object RMsg;
    private Object Obj;
    private Object Obj2;

    public boolean isIsSucceed() {
        return IsSucceed;
    }

    public void setIsSucceed(boolean IsSucceed) {
        this.IsSucceed = IsSucceed;
    }

    public boolean isSucceed() {
        return IsSucceed;
    }

    public BankCardItem getMsg() {
        return Msg;
    }

    public void setMsg(BankCardItem msg) {
        Msg = msg;
    }

    public void setSucceed(boolean succeed) {
        IsSucceed = succeed;
    }

    public Object getRMsg() {
        return RMsg;
    }

    public void setRMsg(Object RMsg) {
        this.RMsg = RMsg;
    }

    public Object getObj() {
        return Obj;
    }

    public void setObj(Object Obj) {
        this.Obj = Obj;
    }

    public Object getObj2() {
        return Obj2;
    }

    public void setObj2(Object Obj2) {
        this.Obj2 = Obj2;
    }
}
