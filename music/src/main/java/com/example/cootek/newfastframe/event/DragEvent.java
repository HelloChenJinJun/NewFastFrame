package com.example.cootek.newfastframe.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     13:42
 */
public class DragEvent {
    private boolean intercepted;


    public DragEvent(boolean intercepted) {
        this.intercepted = intercepted;
    }

    public void setIntercepted(boolean intercepted) {
        this.intercepted = intercepted;
    }

    public boolean isIntercepted() {
        return intercepted;
    }
}
