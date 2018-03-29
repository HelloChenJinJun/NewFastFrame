package com.example.chat.events;

/**
 * 项目名称:    PostDemo
 * 创建人:      陈锦军
 * 创建时间:    2018/2/8     13:04
 * QQ:         1981367757
 */

public class NetStatusEvent {
    private boolean isConnected;

    private int type;


    public NetStatusEvent(boolean isConnected, int type) {
        this.isConnected = isConnected;
        this.type = type;
    }


    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
