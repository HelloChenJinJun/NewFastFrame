package com.example.cootek.newfastframe.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     13:49
 */
public class ServiceStateEvent {
    private boolean connected;

    public ServiceStateEvent(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
