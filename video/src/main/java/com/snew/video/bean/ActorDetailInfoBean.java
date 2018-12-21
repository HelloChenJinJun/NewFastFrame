package com.snew.video.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     16:04
 */
public class ActorDetailInfoBean {
    private String avatar;
    private String name;
    private List<ActorVideoWrappedDetailBean> mActorVideoWrappedDetailBeanList;

    public List<ActorVideoWrappedDetailBean> getActorVideoWrappedDetailBeanList() {
        return mActorVideoWrappedDetailBeanList;
    }


    public void setActorVideoWrappedDetailBeanList(List<ActorVideoWrappedDetailBean> actorVideoWrappedDetailBeanList) {
        mActorVideoWrappedDetailBeanList = actorVideoWrappedDetailBeanList;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static class ActorVideoWrappedDetailBean implements Serializable {
        private List<ActorVideoDetailBean> mActorVideoDetailBeanList;

        private int videoType;

        public List<ActorVideoDetailBean> getActorVideoDetailBeanList() {
            return mActorVideoDetailBeanList;
        }


        public void setActorVideoDetailBeanList(List<ActorVideoDetailBean> actorVideoDetailBeanList) {
            mActorVideoDetailBeanList = actorVideoDetailBeanList;
        }

        public int getVideoType() {
            return videoType;
        }

        public void setVideoType(int videoType) {
            this.videoType = videoType;
        }


    }


    public static class ActorVideoDetailBean implements Serializable {
        private String image;
        private String title;
        private String url;

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


        public void setImage(String image) {
            this.image = image;
        }


        public String getImage() {
            return image;
        }
    }
}
