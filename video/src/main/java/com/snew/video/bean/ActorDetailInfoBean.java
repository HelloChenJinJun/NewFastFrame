package com.snew.video.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     16:04
 */
public class ActorDetailInfoBean {
    private String avatar;
    private String name;
    private List<ActorVideoDetailBean> mActorVideoDetailBeans;


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

    public List<ActorVideoDetailBean> getActorVideoDetailBeans() {
        return mActorVideoDetailBeans;
    }

    public void setActorVideoDetailBeans(List<ActorVideoDetailBean> actorVideoDetailBeans) {
        mActorVideoDetailBeans = actorVideoDetailBeans;
    }

    public static class ActorVideoDetailBean {
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
