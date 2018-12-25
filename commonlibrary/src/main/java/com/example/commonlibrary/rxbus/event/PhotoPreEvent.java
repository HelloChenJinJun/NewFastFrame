package com.example.commonlibrary.rxbus.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/24     17:55
 */
public class PhotoPreEvent {
    private int index;
    private int flag;

    public PhotoPreEvent(int flag, int index) {
        this.index = index;
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public int getIndex() {
        return index;
    }
}
