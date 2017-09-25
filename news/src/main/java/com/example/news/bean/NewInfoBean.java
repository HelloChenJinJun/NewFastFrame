package com.example.news.bean;

import com.example.commonlibrary.baseadapter.baseitem.MultipleItem;
import com.example.news.util.NewsUtil;

import java.util.List;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      11:45
 * QQ:             1981367757
 */

public class NewInfoBean implements MultipleItem {


    /**
     * imgextra : [{"imgsrc":"http://cms-bucket.nosdn.127.net/3da0c64f6627454183d701e6b422e29020170923224700.jpeg"},{"imgsrc":"http://cms-bucket.nosdn.127.net/8f18e3a1e2484d09ab0d5a9670f9408420170923224654.jpeg"}]
     * template : normal1
     * skipID : 00AJ0003|641010
     * lmodify : 2017-09-24 05:53:41
     * postid : PHOTJHVI000300AJ
     * source : 网易娱乐
     * title : Angelababy出席活动穿蕾丝裙似粉嫩公主
     * mtime : 2017-09-24 05:53:41
     * hasImg : 1
     * topic_background : http://img2.cache.netease.com/m/newsapp/reading/cover1/C1348646712614.jpg
     * digest :
     * photosetID : 00AJ0003|641010
     * boardid : ent2_bbs
     * alias : Top News
     * hasAD : 1
     * imgsrc : http://cms-bucket.nosdn.127.net/4bb6b733588f4b28887ad1e5b0aef4b420170923224655.jpeg
     * ptime : 2017-09-23 22:35:03
     * daynum : 17433
     * hasHead : 1
     * order : 1
     * votecount : 2155
     * hasCover : false
     * docid : 9IG74V5H00963VRO_0003set641010updateDoc
     * tname : 头条
     * priority : 81
     * ads : [{"subtitle":"","skipType":"photoset","skipID":"00AP0001|2276259","tag":"photoset","title":"湄洲妈祖金身抵达台湾 接驾信众超千人","imgsrc":"http://cms-bucket.nosdn.127.net/de2e5dbef1e944b8b48f7aef3fd2dc7b20170924110002.jpeg","url":"00AP0001|2276259"},{"subtitle":"","skipType":"photoset","skipID":"00AP0001|2276240","tag":"photoset","title":"钱江潮掀起十多米高巨浪 游客大饱眼福","imgsrc":"http://cms-bucket.nosdn.127.net/7151de385e1442dfb347023636a80fe420170924082739.jpeg","url":"00AP0001|2276240"},{"subtitle":"","skipType":"photoset","skipID":"00AP0001|2276230","tag":"photoset","title":"孤独症患儿被母亲遗弃 每天看照片叫妈","imgsrc":"http://cms-bucket.nosdn.127.net/dd687660548c4165a5704b1c471bbd3220170924100010.jpeg","url":"00AP0001|2276230"},{"subtitle":"","skipType":"photoset","skipID":"00AO0001|2276183","tag":"photoset","title":"巴西黑帮火拼警方出动 民众淡定围观","imgsrc":"http://cms-bucket.nosdn.127.net/d523ae58b61f4f4a8b54a8330b92f4dd20170923165211.jpeg","url":"00AO0001|2276183"},{"subtitle":"","skipType":"photoset","skipID":"00AO0001|2276227","tag":"photoset","title":"哈里王子造访加拿大 不忘\"调戏\"小狗","imgsrc":"http://cms-bucket.nosdn.127.net/ee9d78491cae45678dad5df396c307de20170924033234.jpeg","url":"00AO0001|2276227"}]
     * ename : androidnews
     * replyCount : 2345
     * imgsum : 4
     * hasIcon : false
     * skipType : photoset
     * cid : C1348646712614
     * url_3w : http://news.163.com/17/0923/21/CV22FGOG0001875O.html
     * url : http://3g.163.com/news/17/0923/21/CV22FGOG0001875O.html
     * ltitle : 加拿大失联中国留学生被证实身亡 警方删寻人公告
     * subtitle :
     * articleType : webview
     * TAG : 合作
     * TAGS : 合作
     */

    private String template;
    private String skipID;
    private String lmodify;
    private String postid;
    private String source;
    private String title;
    private String mtime;
    private int hasImg;
    private String topic_background;
    private String digest;
    private String photosetID;
    private String boardid;
    private String alias;
    private int hasAD;
    private String imgsrc;
    private String ptime;
    private String daynum;
    private int hasHead;
    private int order;
    private int votecount;
    private boolean hasCover;
    private String docid;
    private String tname;
    private int priority;
    private String ename;
    private int replyCount;
    private int imgsum;
    private boolean hasIcon;
    private String skipType;
    private String cid;
    private String url_3w;
    private String url;
    private String ltitle;
    private String subtitle;
    private String articleType;
    private String TAG;
    private String TAGS;
    private List<ImgextraEntity> imgextra;
    private List<AdsEntity> ads;


    private String specialID;

    public String getSpecialID() {
        return specialID;
    }

    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSkipID() {
        return skipID;
    }

    public void setSkipID(String skipID) {
        this.skipID = skipID;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public int getHasImg() {
        return hasImg;
    }

    public void setHasImg(int hasImg) {
        this.hasImg = hasImg;
    }

    public String getTopic_background() {
        return topic_background;
    }

    public void setTopic_background(String topic_background) {
        this.topic_background = topic_background;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getPhotosetID() {
        return photosetID;
    }

    public void setPhotosetID(String photosetID) {
        this.photosetID = photosetID;
    }

    public String getBoardid() {
        return boardid;
    }

    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getHasAD() {
        return hasAD;
    }

    public void setHasAD(int hasAD) {
        this.hasAD = hasAD;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getDaynum() {
        return daynum;
    }

    public void setDaynum(String daynum) {
        this.daynum = daynum;
    }

    public int getHasHead() {
        return hasHead;
    }

    public void setHasHead(int hasHead) {
        this.hasHead = hasHead;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }

    public boolean isHasCover() {
        return hasCover;
    }

    public void setHasCover(boolean hasCover) {
        this.hasCover = hasCover;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getImgsum() {
        return imgsum;
    }

    public void setImgsum(int imgsum) {
        this.imgsum = imgsum;
    }

    public boolean isHasIcon() {
        return hasIcon;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public String getSkipType() {
        return skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUrl_3w() {
        return url_3w;
    }

    public void setUrl_3w(String url_3w) {
        this.url_3w = url_3w;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLtitle() {
        return ltitle;
    }

    public void setLtitle(String ltitle) {
        this.ltitle = ltitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getTAGS() {
        return TAGS;
    }

    public void setTAGS(String TAGS) {
        this.TAGS = TAGS;
    }

    public List<ImgextraEntity> getImgextra() {
        return imgextra;
    }

    public void setImgextra(List<ImgextraEntity> imgextra) {
        this.imgextra = imgextra;
    }

    public List<AdsEntity> getAds() {
        return ads;
    }

    public void setAds(List<AdsEntity> ads) {
        this.ads = ads;
    }


    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_NORMAL = 0;


    @Override
    public int getItemViewType() {
        return NewsUtil.PHOTO_SET.equals(getSkipType()) ? TYPE_PHOTO : TYPE_NORMAL;
    }

    public static class ImgextraEntity {
        /**
         * imgsrc : http://cms-bucket.nosdn.127.net/3da0c64f6627454183d701e6b422e29020170923224700.jpeg
         */

        private String imgsrc;

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }
    }

    public static class AdsEntity {
        /**
         * subtitle :
         * skipType : photoset
         * skipID : 00AP0001|2276259
         * tag : photoset
         * title : 湄洲妈祖金身抵达台湾 接驾信众超千人
         * imgsrc : http://cms-bucket.nosdn.127.net/de2e5dbef1e944b8b48f7aef3fd2dc7b20170924110002.jpeg
         * url : 00AP0001|2276259
         */

        private String subtitle;
        private String skipType;
        private String skipID;
        private String tag;
        private String title;
        private String imgsrc;
        private String url;

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getSkipType() {
            return skipType;
        }

        public void setSkipType(String skipType) {
            this.skipType = skipType;
        }

        public String getSkipID() {
            return skipID;
        }

        public void setSkipID(String skipID) {
            this.skipID = skipID;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
