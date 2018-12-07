package com.example.commonlibrary.rxbus.event;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/12/4     13:54
 */
public class PlayStateEvent {
    private int playState;

    public PlayStateEvent(int playState) {
        this.playState = playState;
    }


    public int getPlayState() {
        return playState;
    }
}
