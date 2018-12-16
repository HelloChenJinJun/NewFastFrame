package com.snew.video.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/16     13:27
 */
public class VideoPlayDetailBean {
    private int videoType;
    private List<TVPlayBean> mTVPlayBeans;
    private String title;
    private String subTitle;
    private List<String> tagList;
    private String desc;
    private List<VideoPlayPerson> mVideoPlayPeople;


    private String score;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }



    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public List<TVPlayBean> getTVPlayBeans() {
        return mTVPlayBeans;
    }

    public void setTVPlayBeans(List<TVPlayBean> TVPlayBeans) {
        mTVPlayBeans = TVPlayBeans;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<VideoPlayPerson> getVideoPlayPeople() {
        return mVideoPlayPeople;
    }

    public void setVideoPlayPeople(List<VideoPlayPerson> videoPlayPeople) {
        mVideoPlayPeople = videoPlayPeople;
    }

    public int getVideoType() {
        return videoType;
    }

    public static class VideoPlayPerson {


        private String avatar;
        private String name;


        private String detail;


        public String getDetail() {
            return detail;
        }


        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
