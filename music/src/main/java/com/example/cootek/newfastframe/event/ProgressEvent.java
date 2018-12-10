package com.example.cootek.newfastframe.event;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/8     9:28
 */
public class ProgressEvent {
    private int progress;
    public ProgressEvent(int progress) {
        this.progress=progress;
    }

    public int getProgress() {
        return progress;
    }
}
