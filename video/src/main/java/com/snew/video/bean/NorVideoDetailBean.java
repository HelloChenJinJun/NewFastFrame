package com.snew.video.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/18     21:42
 */
public class NorVideoDetailBean {

    /**
     * PlaylistItem : {"asyncParam":"","btnList":[],"btnPlayUrl":"","btnTitle":"","displayType":1,"indexList":["1-38"],"name":"qq","needAsync":false,"payType":2,"pl_video_type":1,"realName":"腾讯视频","strIconUrl":"http://puui.qpic.cn/vupload/0/20180829_1535529048087_wqz3oac1t77.png/0","title":"","totalEpisode":10,"videoPlayList":[{"episode_number":"1","id":"v00291jnv66","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/v00291jnv66_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=v00291jnv66","title":"大约是爱_01","type":"1"},{"episode_number":"2","id":"l00298l8yoi","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/l00298l8yoi_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=l00298l8yoi","title":"大约是爱_02","type":"1"},{"episode_number":"3","id":"q0029c2hop1","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/q0029c2hop1_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=q0029c2hop1","title":"大约是爱_03","type":"1"},{"episode_number":"4","id":"q0029bpl500","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/q0029bpl500_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=q0029bpl500","title":"大约是爱_04","type":"1"},{"episode_number":"5","id":"z002947w011","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/z002947w011_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=z002947w011","title":"大约是爱_05","type":"1"},{"episode_number":"6","id":"e002922kpf5","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/e002922kpf5_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=e002922kpf5","title":"大约是爱_06","type":"1"},{"episode_number":"7","id":"e0029iymcwc","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/e0029iymcwc_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=e0029iymcwc","title":"大约是爱_07","type":"1"},{"episode_number":"8","id":"h0029gt3sy5","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/h0029gt3sy5_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=h0029gt3sy5","title":"大约是爱_08","type":"1"},{"episode_number":"9","id":"s0029mf6xkl","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/s0029mf6xkl_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=s0029mf6xkl","title":"大约是爱_09","type":"1"},{"episode_number":"10","id":"w002926b0vc","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/w002926b0vc_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=w002926b0vc","title":"大约是爱_10","type":"1"}]}
     * error : 0
     * msg :
     */

    private PlaylistItemBean PlaylistItem;
    private int error;
    private String msg;

    public PlaylistItemBean getPlaylistItem() {
        return PlaylistItem;
    }

    public void setPlaylistItem(PlaylistItemBean PlaylistItem) {
        this.PlaylistItem = PlaylistItem;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class PlaylistItemBean {
        /**
         * asyncParam :
         * btnList : []
         * btnPlayUrl :
         * btnTitle :
         * displayType : 1
         * indexList : ["1-38"]
         * name : qq
         * needAsync : false
         * payType : 2
         * pl_video_type : 1
         * realName : 腾讯视频
         * strIconUrl : http://puui.qpic.cn/vupload/0/20180829_1535529048087_wqz3oac1t77.png/0
         * title :
         * totalEpisode : 10
         * videoPlayList : [{"episode_number":"1","id":"v00291jnv66","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/v00291jnv66_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=v00291jnv66","title":"大约是爱_01","type":"1"},{"episode_number":"2","id":"l00298l8yoi","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/l00298l8yoi_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=l00298l8yoi","title":"大约是爱_02","type":"1"},{"episode_number":"3","id":"q0029c2hop1","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/q0029c2hop1_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=q0029c2hop1","title":"大约是爱_03","type":"1"},{"episode_number":"4","id":"q0029bpl500","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/q0029bpl500_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=q0029bpl500","title":"大约是爱_04","type":"1"},{"episode_number":"5","id":"z002947w011","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/z002947w011_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=z002947w011","title":"大约是爱_05","type":"1"},{"episode_number":"6","id":"e002922kpf5","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/e002922kpf5_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=e002922kpf5","title":"大约是爱_06","type":"1"},{"episode_number":"7","id":"e0029iymcwc","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/e0029iymcwc_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=e0029iymcwc","title":"大约是爱_07","type":"1"},{"episode_number":"8","id":"h0029gt3sy5","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/h0029gt3sy5_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=h0029gt3sy5","title":"大约是爱_08","type":"1"},{"episode_number":"9","id":"s0029mf6xkl","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/s0029mf6xkl_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=s0029mf6xkl","title":"大约是爱_09","type":"1"},{"episode_number":"10","id":"w002926b0vc","markLabelList":[],"payType":0,"pic":"http://puui.qpic.cn/qqvideo_ori/0/w002926b0vc_360_204/0","playUrl":"http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=w002926b0vc","title":"大约是爱_10","type":"1"}]
         */

        private String asyncParam;
        private String btnPlayUrl;
        private String btnTitle;
        private int displayType;
        private String name;
        private boolean needAsync;
        private int payType;
        private int pl_video_type;
        private String realName;
        private String strIconUrl;
        private String title;
        private int totalEpisode;
        private List<?> btnList;
        private List<String> indexList;
        private List<VideoPlayListBean> videoPlayList;

        public String getAsyncParam() {
            return asyncParam;
        }

        public void setAsyncParam(String asyncParam) {
            this.asyncParam = asyncParam;
        }

        public String getBtnPlayUrl() {
            return btnPlayUrl;
        }

        public void setBtnPlayUrl(String btnPlayUrl) {
            this.btnPlayUrl = btnPlayUrl;
        }

        public String getBtnTitle() {
            return btnTitle;
        }

        public void setBtnTitle(String btnTitle) {
            this.btnTitle = btnTitle;
        }

        public int getDisplayType() {
            return displayType;
        }

        public void setDisplayType(int displayType) {
            this.displayType = displayType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isNeedAsync() {
            return needAsync;
        }

        public void setNeedAsync(boolean needAsync) {
            this.needAsync = needAsync;
        }

        public int getPayType() {
            return payType;
        }

        public void setPayType(int payType) {
            this.payType = payType;
        }

        public int getPl_video_type() {
            return pl_video_type;
        }

        public void setPl_video_type(int pl_video_type) {
            this.pl_video_type = pl_video_type;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getStrIconUrl() {
            return strIconUrl;
        }

        public void setStrIconUrl(String strIconUrl) {
            this.strIconUrl = strIconUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotalEpisode() {
            return totalEpisode;
        }

        public void setTotalEpisode(int totalEpisode) {
            this.totalEpisode = totalEpisode;
        }

        public List<?> getBtnList() {
            return btnList;
        }

        public void setBtnList(List<?> btnList) {
            this.btnList = btnList;
        }

        public List<String> getIndexList() {
            return indexList;
        }

        public void setIndexList(List<String> indexList) {
            this.indexList = indexList;
        }

        public List<VideoPlayListBean> getVideoPlayList() {
            return videoPlayList;
        }

        public void setVideoPlayList(List<VideoPlayListBean> videoPlayList) {
            this.videoPlayList = videoPlayList;
        }

        public static class VideoPlayListBean {
            /**
             * episode_number : 1
             * id : v00291jnv66
             * markLabelList : []
             * payType : 0
             * pic : http://puui.qpic.cn/qqvideo_ori/0/v00291jnv66_360_204/0
             * playUrl : http://v.qq.com/x/cover/9w9tk4ybmnb77bz.html?vid=v00291jnv66
             * title : 大约是爱_01
             * type : 1
             */

            private String episode_number;
            private String id;
            private int payType;
            private String pic;
            private String playUrl;
            private String title;
            private String type;
            private List<?> markLabelList;

            public String getEpisode_number() {
                return episode_number;
            }

            public void setEpisode_number(String episode_number) {
                this.episode_number = episode_number;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getPayType() {
                return payType;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getPlayUrl() {
                return playUrl;
            }

            public void setPlayUrl(String playUrl) {
                this.playUrl = playUrl;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<?> getMarkLabelList() {
                return markLabelList;
            }

            public void setMarkLabelList(List<?> markLabelList) {
                this.markLabelList = markLabelList;
            }
        }
    }
}
