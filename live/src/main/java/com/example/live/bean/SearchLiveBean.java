package com.example.live.bean;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      10:33
 * QQ:             1981367757
 */

public class SearchLiveBean {

    /**
     * code : 200
     * error :
     * data : {"items":[{"no":4041550,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/4041550-1494311643-542.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/zIRe101-normal","title":"啊啊啊！总有刁民想害我！不拿一血算我输！","is_shield":false,"nick":"lol小胖777","uid":4041550,"view":"86","category_id":1,"slug":"","category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":2291142,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/277255-1494311643-61.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/FXK8201-normal","title":"LOL走起","is_shield":false,"nick":"崽崽的弟弟","uid":277255,"view":"72","category_id":1,"slug":"s7410","category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":11935228,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/9767855-1494311643-252.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/42kV101-normal","title":"一个喜欢唱歌的LOL主播！！！","is_shield":false,"nick":"Aai洋洋洋","uid":9767855,"view":"65","category_id":1,"slug":"aaa9767855aaa","category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":15052121,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/1499045422-1494311642-393.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/lDLt101-normal","title":"新版本试水LOL","is_shield":false,"nick":"小小窜天猴","uid":1499045422,"view":"64","category_id":1,"slug":null,"category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":14021622,"category_name":"坦克世界","thumb":"http://snap.quanmin.tv/1230751544-1494311457-699.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/R1xY101-normal","title":"LOL、TK娱乐直播","is_shield":false,"nick":"老侯娱乐","uid":1230751544,"view":"4","category_id":36,"slug":null,"category_slug":"tank","play_status":false,"frame":"","landscape":1},{"no":13296128,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/1878893862-1494248432-563.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/HhFY103-normal","title":"男爵匹配可以上车","is_shield":false,"nick":"LOL_芒果","uid":1878893862,"view":"3","category_id":1,"slug":"lolmg","category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":10688220,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/8549649-1494308718-104.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://image.quanmin.tv/avatar/9934be9168cb2fd27733f253ec0327b5jpg?imageView2/2/w/300/","title":"lol、","is_shield":false,"nick":"直播是我的梦想","uid":8549649,"view":"2","category_id":1,"slug":null,"category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":15972081,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/1553660782-1494000723-985.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/Rlq7201-normal","title":"LOL主播 野哥的直播间","is_shield":false,"nick":"LOL主播 野哥","uid":1553660782,"view":"1","category_id":1,"slug":null,"category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":10940428,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/8801728-1494177663-725.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://image.quanmin.tv/avatar/01b1f717345d4e33f8f2d993042c830djpg?imageView2/2/w/300/","title":"最强青铜,主玩劫。","is_shield":false,"nick":"LOL小十七","uid":8801728,"view":"1","category_id":1,"slug":null,"category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":7946685,"category_name":"棋牌游戏","thumb":"http://snap.quanmin.tv/7946685-1493985237-487.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://image.quanmin.tv/avatar/2366f142a0980b5448293952c71c12ddjpg?imageView2/2/w/300/","title":"重温小时候玩的（血战上海滩）","is_shield":false,"nick":"小邢LOL","uid":7946685,"view":"1","category_id":42,"slug":"","category_slug":"qipai","play_status":false,"frame":"","landscape":1}],"total":1216,"pageNb":122}
     */

    private int code;
    private String error;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * items : [{"no":4041550,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/4041550-1494311643-542.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/zIRe101-normal","title":"啊啊啊！总有刁民想害我！不拿一血算我输！","is_shield":false,"nick":"lol小胖777","uid":4041550,"view":"86","category_id":1,"slug":"","category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":2291142,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/277255-1494311643-61.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/FXK8201-normal","title":"LOL走起","is_shield":false,"nick":"崽崽的弟弟","uid":277255,"view":"72","category_id":1,"slug":"s7410","category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":11935228,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/9767855-1494311643-252.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/42kV101-normal","title":"一个喜欢唱歌的LOL主播！！！","is_shield":false,"nick":"Aai洋洋洋","uid":9767855,"view":"65","category_id":1,"slug":"aaa9767855aaa","category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":15052121,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/1499045422-1494311642-393.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/lDLt101-normal","title":"新版本试水LOL","is_shield":false,"nick":"小小窜天猴","uid":1499045422,"view":"64","category_id":1,"slug":null,"category_slug":"lol","play_status":true,"frame":"","landscape":1},{"no":14021622,"category_name":"坦克世界","thumb":"http://snap.quanmin.tv/1230751544-1494311457-699.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/R1xY101-normal","title":"LOL、TK娱乐直播","is_shield":false,"nick":"老侯娱乐","uid":1230751544,"view":"4","category_id":36,"slug":null,"category_slug":"tank","play_status":false,"frame":"","landscape":1},{"no":13296128,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/1878893862-1494248432-563.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/HhFY103-normal","title":"男爵匹配可以上车","is_shield":false,"nick":"LOL_芒果","uid":1878893862,"view":"3","category_id":1,"slug":"lolmg","category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":10688220,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/8549649-1494308718-104.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://image.quanmin.tv/avatar/9934be9168cb2fd27733f253ec0327b5jpg?imageView2/2/w/300/","title":"lol、","is_shield":false,"nick":"直播是我的梦想","uid":8549649,"view":"2","category_id":1,"slug":null,"category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":15972081,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/1553660782-1494000723-985.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://a.img.shouyintv.cn/Rlq7201-normal","title":"LOL主播 野哥的直播间","is_shield":false,"nick":"LOL主播 野哥","uid":1553660782,"view":"1","category_id":1,"slug":null,"category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":10940428,"category_name":"英雄联盟","thumb":"http://snap.quanmin.tv/8801728-1494177663-725.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://image.quanmin.tv/avatar/01b1f717345d4e33f8f2d993042c830djpg?imageView2/2/w/300/","title":"最强青铜,主玩劫。","is_shield":false,"nick":"LOL小十七","uid":8801728,"view":"1","category_id":1,"slug":null,"category_slug":"lol","play_status":false,"frame":"","landscape":1},{"no":7946685,"category_name":"棋牌游戏","thumb":"http://snap.quanmin.tv/7946685-1493985237-487.jpg?imageView2/2/w/390/","screen":0,"avatar":"http://image.quanmin.tv/avatar/2366f142a0980b5448293952c71c12ddjpg?imageView2/2/w/300/","title":"重温小时候玩的（血战上海滩）","is_shield":false,"nick":"小邢LOL","uid":7946685,"view":"1","category_id":42,"slug":"","category_slug":"qipai","play_status":false,"frame":"","landscape":1}]
         * total : 1216
         * pageNb : 122
         */

        private int total;
        private int pageNb;
        private List<LiveInfo> items;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNb() {
            return pageNb;
        }

        public void setPageNb(int pageNb) {
            this.pageNb = pageNb;
        }

        public List<LiveInfo> getItems() {
            return items;
        }

        public void setItems(List<LiveInfo> items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "total=" + total +
                    ", pageNb=" + pageNb +
                    ", items=" + items +
                    '}';
        }

        public static class LiveInfo {
            private String no;
            private String nick;
            private String avatar;
            private int follow;
            private String id;
            private String uid;
            private String title;
            private String category_id;
            private String slug;
            private String intro;
            private String announcement;
            private String cover;
            private String play_at;
            private String last_play_at;
            private String view;
            private String status;
            private String priv;
            private String landscape;
            private String position;
            private String weight;
            private String check;
            private String recommend_image;
            private String videoQuality;
            private String category_name;
            private int screen;
            private String start_time;
            private String stream;
            private String thumb;
            private String video;
            private String app_shuffling_image;
            private String categoryId;
            private boolean hidden;
            private boolean play_status;
            private String icontext;
            private String category_slug;
            private String love_cover;

            public LiveInfo(String no, String nick, String avatar, int follow, String id,
                            String uid, String title, String category_id, String slug, String intro,
                            String announcement, String cover, String play_at, String last_play_at,
                            String view, String status, String priv, String landscape,
                            String position, String weight, String check, String recommend_image,
                            String videoQuality, String category_name, int screen,
                            String start_time, String stream, String thumb, String video,
                            String app_shuffling_image, String categoryId, boolean hidden,
                            boolean play_status, String icontext, String category_slug,String love_cover) {
                this.no = no;
                this.nick = nick;
                this.avatar = avatar;
                this.follow = follow;
                this.id = id;
                this.uid = uid;
                this.title = title;
                this.category_id = category_id;
                this.slug = slug;
                this.intro = intro;
                this.announcement = announcement;
                this.cover = cover;
                this.play_at = play_at;
                this.last_play_at = last_play_at;
                this.view = view;
                this.status = status;
                this.priv = priv;
                this.landscape = landscape;
                this.position = position;
                this.weight = weight;
                this.check = check;
                this.recommend_image = recommend_image;
                this.videoQuality = videoQuality;
                this.category_name = category_name;
                this.screen = screen;
                this.start_time = start_time;
                this.stream = stream;
                this.thumb = thumb;
                this.video = video;
                this.app_shuffling_image = app_shuffling_image;
                this.categoryId = categoryId;
                this.hidden = hidden;
                this.play_status = play_status;
                this.icontext = icontext;
                this.category_slug = category_slug;
                this.love_cover = love_cover;
            }

            public LiveInfo() {
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public int getFollow() {
                return follow;
            }

            public void setFollow(int follow) {
                this.follow = follow;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCategory_id() {
                return category_id;
            }

            public void setCategory_id(String category_id) {
                this.category_id = category_id;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getAnnouncement() {
                return announcement;
            }

            public void setAnnouncement(String announcement) {
                this.announcement = announcement;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getPlay_at() {
                return play_at;
            }

            public void setPlay_at(String play_at) {
                this.play_at = play_at;
            }

            public String getLast_play_at() {
                return last_play_at;
            }

            public void setLast_play_at(String last_play_at) {
                this.last_play_at = last_play_at;
            }

            public String getView() {
                return view;
            }

            public void setView(String view) {
                this.view = view;
            }



            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPriv() {
                return priv;
            }

            public void setPriv(String priv) {
                this.priv = priv;
            }

            public String getLandscape() {
                return landscape;
            }

            public void setLandscape(String landscape) {
                this.landscape = landscape;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getCheck() {
                return check;
            }

            public void setCheck(String check) {
                this.check = check;
            }

            public String getRecommend_image() {
                return recommend_image;
            }

            public void setRecommend_image(String recommend_image) {
                this.recommend_image = recommend_image;
            }

            public String getVideoQuality() {
                return videoQuality;
            }

            public void setVideoQuality(String videoQuality) {
                this.videoQuality = videoQuality;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public int getScreen() {
                return screen;
            }

            public void setScreen(int screen) {
                this.screen = screen;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getStream() {
                return stream;
            }

            public void setStream(String stream) {
                this.stream = stream;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getVideo() {
                return video;
            }

            public void setVideo(String video) {
                this.video = video;
            }

            public String getApp_shuffling_image() {
                return app_shuffling_image;
            }

            public void setApp_shuffling_image(String app_shuffling_image) {
                this.app_shuffling_image = app_shuffling_image;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public boolean isHidden() {
                return hidden;
            }

            public void setHidden(boolean hidden) {
                this.hidden = hidden;
            }

            public boolean isPlay_status() {
                return play_status;
            }

            public void setPlay_status(boolean play_status) {
                this.play_status = play_status;
            }

            public String getIcontext() {
                return icontext;
            }

            public void setIcontext(String icontext) {
                this.icontext = icontext;
            }

            public String getCategory_slug() {
                return category_slug;
            }

            public void setCategory_slug(String category_slug) {
                this.category_slug = category_slug;
            }

            public boolean getHidden() {
                return this.hidden;
            }

            public boolean getPlay_status() {
                return this.play_status;
            }

            public String getLove_cover() {
                return love_cover;
            }

            public void setLove_cover(String love_cover) {
                this.love_cover = love_cover;
            }

            @Override
            public String toString() {
                return "LiveInfo{" +
                        "no='" + no + '\'' +
                        ", nick='" + nick + '\'' +
                        ", avatar='" + avatar + '\'' +
                        ", follow=" + follow +
                        ", id='" + id + '\'' +
                        ", uid='" + uid + '\'' +
                        ", title='" + title + '\'' +
                        ", category_id='" + category_id + '\'' +
                        ", slug='" + slug + '\'' +
                        ", intro='" + intro + '\'' +
                        ", announcement='" + announcement + '\'' +
                        ", cover='" + cover + '\'' +
                        ", play_at='" + play_at + '\'' +
                        ", last_play_at='" + last_play_at + '\'' +
                        ", view='" + view + '\'' +
                        ", status='" + status + '\'' +
                        ", priv='" + priv + '\'' +
                        ", landscape='" + landscape + '\'' +
                        ", position='" + position + '\'' +
                        ", weight='" + weight + '\'' +
                        ", check='" + check + '\'' +
                        ", recommend_image='" + recommend_image + '\'' +
                        ", videoQuality='" + videoQuality + '\'' +
                        ", category_name='" + category_name + '\'' +
                        ", screen=" + screen +
                        ", start_time='" + start_time + '\'' +
                        ", stream='" + stream + '\'' +
                        ", thumb='" + thumb + '\'' +
                        ", video='" + video + '\'' +
                        ", app_shuffling_image='" + app_shuffling_image + '\'' +
                        ", categoryId='" + categoryId + '\'' +
                        ", hidden=" + hidden +
                        ", play_status=" + play_status +
                        ", icontext='" + icontext + '\'' +
                        ", category_slug='" + category_slug + '\'' +
                        ", love_cover='" + love_cover + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "code=" + code +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}
