package com.snew.video.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/20     16:08
 */
public class VideoUpLoadDataBean extends BmobObject {
    private String url;
    private String title;
    private List<String> playList;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getPlayList() {
        if (playList == null) {
            playList=new ArrayList<>();
        }
        return playList;
    }

    public void setPlayList(List<String> playList) {
        this.playList = playList;
    }
}
