package com.example.news.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:56
 * QQ:             1981367757
 */

public class NewListBean {
    private List<BannerBean>  bannerBeanList;
    private List<NewsItem>  newsItemList;

    public List<NewListBean.BannerBean> getBannerBeanList() {
        return bannerBeanList;
    }

    public void setBannerBeanList(List<NewListBean.BannerBean> bannerBeanList) {
        this.bannerBeanList = bannerBeanList;
    }

    public List<NewsItem> getNewsItemList() {
        return newsItemList;
    }

    public void setNewsItemList(List<NewListBean.NewsItem> newsItemList) {
        this.newsItemList = newsItemList;
    }

    public  static class BannerBean {
        private String thumb;
        private String contentUrl;
        private String description;
        private String title;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }
    }


    public static class NewsItem{
        private String title;
        private String description;
        private String contentUrl;
        private String thumb;
        private String time;
        private String from;

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }


        @Override
        public boolean equals(Object obj) {
            return obj!=null&&(obj instanceof NewsItem)
                    && ((NewsItem) obj).getContentUrl().equals(getContentUrl());
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
