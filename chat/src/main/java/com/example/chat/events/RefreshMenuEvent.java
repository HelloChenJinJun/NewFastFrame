package com.example.chat.events;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/28     14:41
 * QQ:         1981367757
 * 刷新侧滑菜单事件
 */

public class RefreshMenuEvent {
    private int position;

    public RefreshMenuEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
