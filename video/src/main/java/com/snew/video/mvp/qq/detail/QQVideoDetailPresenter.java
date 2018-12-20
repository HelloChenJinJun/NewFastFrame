package com.snew.video.mvp.qq.detail;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.manager.video.VideoController;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.snew.video.api.VideoApi;
import com.snew.video.bean.NorVideoDetailBean;
import com.snew.video.bean.QQTVVideoDetailBean;
import com.snew.video.bean.QQVideoDetailBean;
import com.snew.video.bean.TVPlayBean;
import com.snew.video.bean.VarietyDetailResult;
import com.snew.video.bean.VarietyVideoDetailBean;
import com.snew.video.bean.VideoPlayDetailBean;
import com.snew.video.util.VideoUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     14:31
 */
public class QQVideoDetailPresenter extends RxBasePresenter<IView<BaseBean>, DefaultModel> {
    public QQVideoDetailPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }


    public Observable<String> commonParseUrl(String url) {
        //http://api.bbbbbb.me/yunjx/?url=
        return Observable.just(url).subscribeOn(Schedulers.io())
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        StringBuilder stringBuilder = new StringBuilder("http://api.bbbbbb.me/yunjxs/?url=").append(s);
                        Document document = Jsoup.connect(stringBuilder.toString()).get();
                        String content = document.outerHtml();
                        String parseContent = VideoUtil.getMd5Value(content);
                        StringBuilder body = new StringBuilder();
                        body.append("id=").append(s).append("&type=auto&siteuser=&md5=").append(parseContent)
                                .append("&hd=&lg=");
                        RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), body.toString());
                        return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                .postQQTVUrlInfo("http://api.bbbbbb.me/yunjxs/api.php", requestBody1)
                                .flatMap(new Function<QQTVVideoDetailBean, Observable<String>>() {
                                    @Override
                                    public Observable<String> apply(QQTVVideoDetailBean qqtvVideoDetailBean) throws Exception {
                                        String ext = qqtvVideoDetailBean.getExt();
                                        if (ext != null && (ext.equals("mp4") || ext.equals("m3u8"))) {
                                            return Observable.just(qqtvVideoDetailBean.getUrl());
                                        } else if (ext != null && ext.equals("link")) {
                                            String realUrl = qqtvVideoDetailBean.getUrl().substring(qqtvVideoDetailBean.getUrl().indexOf("url=") + 4);

                                            return commonParseUrl(realUrl);
                                        }
                                        return null;
                                    }
                                })
                                .subscribeOn(Schedulers.io());
                    }
                });
    }


    public void getData(String url) {
        commonParseUrl(url).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(String s) {
                        String decoderUrl = URLDecoder.decode(s);
                        BaseBean baseBean = new BaseBean();
                        baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_DETAIL_URL);
                        baseBean.setData(decoderUrl);
                        //                                用于标识当前请求的url
                        baseBean.setExtraInfo(url);
                        baseBean.setCode(200);
                        iView.updateData(baseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }


    public void getDetailData(String url, boolean isSwitch) {
        if (url.startsWith("http://m.bt361.cn")) {
            getUpdateDetailData(url);
            return;
        }
        Observable<String> observable;
        if (isSwitch) {
            observable = getDetailDataForTwo(url);
        } else {
            observable = getDetailDataForOne(url);
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(String string) {
                        CommonLogger.e("最终url" + string);
                        BaseBean baseBean = new BaseBean();
                        baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_DETAIL_URL);
                        if (string != null) {
                            string = URLDecoder.decode(string);
                        }
                        baseBean.setData(string);
                        baseBean.setCode(200);
                        baseBean.setExtraInfo(url);
                        iView.updateData(baseBean);

                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                        BaseBean baseBean = new BaseBean();
                        baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_DETAIL_URL);
                        baseBean.setCode(-1);
                        baseBean.setExtraInfo(url);
                        iView.updateData(baseBean);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });

    }

    private Observable<String> getDetailDataForOne(String url) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), "url=" + url);
        return baseModel.getRepositoryManager().getApi(VideoApi.class)
                .postUrlInfo("http://api.bbbbbb.me/zy/api.php", requestBody).subscribeOn(Schedulers.io())
                .flatMap((Function<QQVideoDetailBean, ObservableSource<String>>) qqVideoDetailBean -> {
                    if (qqVideoDetailBean.getCode() == 0) {
                        String tempUrl = null;
                        if (qqVideoDetailBean.getPlay().equals("url")) {
                            if (URLUtil.isValidUrl(qqVideoDetailBean.getUrl())) {
                                tempUrl = qqVideoDetailBean.getUrl();
                            } else {
                                tempUrl = "http://api.bbbbbb.me" + qqVideoDetailBean.getUrl();
                            }
                            Uri uri;
                            String key;
                            String type;
                            String cid;
                            uri = Uri.parse(tempUrl);
                            key = uri.getQueryParameter("url");
                            type = uri.getQueryParameter("type");
                            cid = uri.getQueryParameter("cid");
                            CommonLogger.e("url:" + uri.toString());
                            if (key != null && (key.contains(".mp4") || key.contains(".m3u8"))) {
                                return Observable.just(key);
                            }

                            String md5 = VideoUtil.getMd5Value(Jsoup.connect(tempUrl).get().outerHtml());
                            StringBuilder body = new StringBuilder();
                            if (tempUrl.contains("jiexi/")) {
                                return getDetailDataForThree(url);
                            } else if (tempUrl.contains("yunjxs")) {
                                body.append("id=").append(key).append("&type=").append(TextUtils.isEmpty(type) ? "auto" : type).append("&siteuser=&md5=").append(md5)
                                        .append("&hd=&lg=");
                            } else if (tempUrl.contains("anlehe")) {
                                return getDetailDataForThree(url);
                            }
                            RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), body.toString());
                            return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                    .postUrlInfo(tempUrl.substring(0, tempUrl.indexOf("?")) + "api.php", requestBody1)
                                    .map(qqVideoDetailBean1 -> qqVideoDetailBean1.getUrl()).subscribeOn(Schedulers.io());
                        } else if (qqVideoDetailBean.getPlay().equals("normal")) {
                            return Observable.just(qqVideoDetailBean.getUrl());
                        }
                    }
                    return getDetailDataForThree(url);
                });
    }

    private Observable<String> getDetailDataForTwo(String url) {
        return Observable.just(url).subscribeOn(Schedulers.io())
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        StringBuilder stringBuilder = new StringBuilder("http://all.baiyug.cn:2021/QQQ/index.php").append("?url=")
                                .append(s);
                        Document document = Jsoup.connect(stringBuilder.toString()).header("Referer", "http://app.baiyug.cn").get();
                        String content = document.outerHtml();
                        String start = "url:";
                        int startIndex = content.indexOf(start);
                        String end = "pic: pic";
                        String url1 = content.substring(startIndex + start.length(), content.lastIndexOf(end)).trim();
                        String realUrl = url1.substring(1, url1.length() - 2);
                        if (URLUtil.isValidUrl(realUrl)) {
                            return Observable.just(realUrl);
                        } else {
                            return getDetailDataForThree(url);
                        }
                    }
                });
    }

    private void getUpdateDetailData(String url) {
        Observable.just(url).subscribeOn(Schedulers.io())
                .map(s -> {
                    Document document = Jsoup.connect(s).get();
                    Elements elements = document.select(".swiper-slide");
                    //                        http://m.bt361.cn/vod/detail/id/45468/
                    List<VideoController.Clarity> list = new ArrayList<>();
                    if (elements.size() > 0) {
                        for (int i = 0; i < elements.size(); i++) {
                            Element item = elements.get(i);

                            String link = item.getElementsByTag("a").first().attr("href");
                            //                                http://m.tbb361.com/index.php/vod/play/id/60010/sid/2/nid/1/
                            String realLink = "http://m.bt361.cn" + link;
                            Document document1 = Jsoup.connect(realLink).get();
                            String content = document1.outerHtml();
                            String start = "\"url\":\"";
                            //                                ","link_next
                            String end = "\",\"url_next";
                            String url1 = content.substring(content.indexOf(start) + start.length(), content.indexOf(end));
                            String decoderUrl = URLDecoder.decode(url1);
                            VideoController.Clarity clarity = new VideoController.Clarity(item.text(), null, decoderUrl);
                            list.add(clarity);
                        }
                    }
                    return list;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VideoController.Clarity>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(List<VideoController.Clarity> clarities) {
                        if (clarities != null && clarities.size() > 0) {
                            ListVideoManager.getInstance().getCurrentPlayer().setClarity(clarities);
                            ListVideoManager.getInstance().updateUrl(clarities.get(0).getVideoUrl());
                        } else {
                            ListVideoManager.getInstance().error();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }


    public Observable<String> getDetailDataForThree(String url) {
        //       http://app.baiyug.cn:2019/vip/search/qq.php?url=https://v.qq.com/x/cover/og0eputlxwet1cn.html
        return Observable.just(url).subscribeOn(Schedulers.io())
                .flatMap((Function<String, ObservableSource<String>>) s -> {
                    StringBuilder stringBuilder = new StringBuilder("http://app.baiyug.cn:2019/vip/search/qq.php").append("?url=")
                            .append(s);
                    Document document;
                    try {
                        document = Jsoup.connect(stringBuilder.toString()).header("Referer", "http://app.baiyug.cn").get();
                        String src = document.getElementsByTag("iframe").first().attr("src");
                        String str;
                        if (URLUtil.isValidUrl(src)) {
                            Uri uri = Uri.parse(src);
                            str = uri.getQueryParameter("url");
                        } else {
                            str = src.substring(src.indexOf("url=") + 4);
                        }
                        if (str == null || str.contains("html")) {
                            return getDetailDataForFour(url);
                        } else {
                            return Observable.just(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return getDetailDataForFour(url);
                    }
                });
    }


    public Observable<String> getDetailDataForFour(String url) {
        return Observable.just(url).subscribeOn(Schedulers.io())
                .flatMap((Function<String, ObservableSource<String>>) s -> {
                    StringBuilder stringBuilder = new StringBuilder("http://all.baiyug.cn:2021/QQ/index.php").append("?url=")
                            .append(s);
                    Document document = Jsoup.connect(stringBuilder.toString()).header("Referer", "http://app.baiyug.cn").get();
                    String content = document.outerHtml();
                    String start = "url:";
                    int startIndex = content.indexOf(start);
                    String end = "pic: pic";
                    String url1 = content.substring(startIndex + start.length(), content.lastIndexOf(end)).trim();
                    String realUrl = url1.substring(1, url1.length() - 2);
                    if (URLUtil.isValidUrl(realUrl)) {
                        return Observable.just(realUrl);
                    } else {
                        return getDetailDataForThree(url);
                    }
                });
    }

    public void getDetailInfo(String id) {
        String url = VideoUtil.getVideoDetailUrl(id);
        Observable<VideoPlayDetailBean> observable;
        if (url.contains("detail/8/")) {
            observable = getVarietyDetailInfo(url, id);
        } else {
            observable = getNormalDetailInfo(url, id);
        }
        observable.subscribe(new Observer<VideoPlayDetailBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(VideoPlayDetailBean videoPlayDetailBean) {
                BaseBean baseBean = new BaseBean();
                baseBean.setCode(200);
                baseBean.setData(videoPlayDetailBean);
                baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_DETAIL_INFO);
                iView.updateData(baseBean);
            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage(), null);
            }

            @Override
            public void onComplete() {
                iView.hideLoading();
            }
        });
    }


    private Observable<VideoPlayDetailBean> getNormalDetailInfo(String url, String id) {
        StringBuilder stringBuilder = new StringBuilder("http://s.video.qq.com/get_playsource?type=4&range=1-200&otype=json&num_mod_cnt=20&id=");
        stringBuilder.append(id);
        return baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getContent(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .map(responseBody -> {
                    VideoPlayDetailBean videoPlayDetailBean = new VideoPlayDetailBean();
                    String body = responseBody.string().replace("QZOutputJson=", "");
                    NorVideoDetailBean norVideoDetailBean
                            = BaseApplication.getAppComponent().getGson().fromJson(body.substring(0, body.length() - 1)
                            , NorVideoDetailBean.class);

                    if (norVideoDetailBean.getPlaylistItem() != null && norVideoDetailBean.getPlaylistItem().getVideoPlayList() != null
                            && norVideoDetailBean.getPlaylistItem().getVideoPlayList().size() > 0) {
                        List<TVPlayBean> list = new ArrayList<>();
                        for (NorVideoDetailBean.PlaylistItemBean.VideoPlayListBean item :
                                norVideoDetailBean.getPlaylistItem().getVideoPlayList()) {
                            TVPlayBean tvPlayBean = new TVPlayBean();
                            tvPlayBean.setTitle(item.getEpisode_number());
                            tvPlayBean.setUrl(item.getPlayUrl());
                            list.add(tvPlayBean);
                        }
                        videoPlayDetailBean.setTVPlayBeans(list);
                    }
                    Document document = Jsoup.connect(url).get();
                    //                    介绍详情
                    Element detail = document.select(".detail_video").first();
                    videoPlayDetailBean.setScore(detail.select(".score_v").text());
                    videoPlayDetailBean.setTitle(detail.select(".video_title_cn").first().getElementsByTag("a").first().text());
                    Element subList = detail.select(".video_type.cf").first();
                    List<String> subTitleList = new ArrayList<>();
                    if (subList != null && subList.children().size() > 0) {
                        for (Element element :
                                subList.children()) {
                            subTitleList.add(element.text());
                        }
                    }

                    Element event = detail.select(".video_type video_type_even.cf").first();
                    if (event != null && event.children().size() > 0) {
                        for (Element element :
                                event.children()) {
                            subTitleList.add(element.text());
                        }
                    }
                    videoPlayDetailBean.setSubTitleList(subTitleList);
                    Elements tagList = detail.select(".tag_list");
                    if (tagList.first() != null && tagList.first().children().size() > 0) {
                        List<String> list = new ArrayList<>();
                        for (Element item :
                                tagList.first().children()) {
                            list.add(item.text());
                        }
                        videoPlayDetailBean.setTagList(list);
                    }
                    videoPlayDetailBean.setDesc(detail.select(".desc_txt").text());
                    Elements person = detail.select(".actor_list.cf");
                    if (person.first() != null && person.first().children().size() > 0) {
                        List<VideoPlayDetailBean.VideoPlayPerson> videoPlayPeople = new ArrayList<>();
                        Elements personChildren = person.first().children();
                        for (int i = 0; i < personChildren.size() - 1; i++) {
                            VideoPlayDetailBean.VideoPlayPerson bean = new VideoPlayDetailBean.VideoPlayPerson();
                            Element item = personChildren.get(i);
                            bean.setName(item.select(".actor_name").text());
                            bean.setUrl(item.getElementsByTag("a").first().attr("href"));
                            bean.setDetail(item.select(".actor_desc").text());
                            bean.setAvatar("https:" + item.getElementsByTag("img").first().attr("src"));
                            videoPlayPeople.add(bean);
                        }
                        videoPlayDetailBean.setVideoPlayPeople(videoPlayPeople);
                    }
                    return videoPlayDetailBean;
                }).observeOn(AndroidSchedulers.mainThread());

    }

    private Observable<VideoPlayDetailBean> getVarietyDetailInfo(String url, String id) {
        // http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=10000&intfname=web_integrated_lid_list&tid=543&
        // appkey=ebe7ee92f568e876&appid=20001174&listappid=10385&listappkey=10385&playright=2&lid=80464&pagesize=10&offset=0
        StringBuilder stringBuilder = new StringBuilder("http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=10000&intfname=web_integrated_lid_list&tid=543&" +
                "appkey=ebe7ee92f568e876&appid=20001174&listappid=10385&listappkey=10385&playright=2&pagesize=10&offset=0&lid=");
        stringBuilder.append(id);
        return baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getVarietyDetailInfo(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .map(responseBody -> {
                    VideoPlayDetailBean videoPlayDetailBean = new VideoPlayDetailBean();
                    String body = responseBody.string().replace("QZOutputJson=", "");
                    VarietyDetailResult varietyDetailResult
                            = BaseApplication.getAppComponent().getGson().fromJson(body.substring(0, body.length() - 1)
                            , VarietyDetailResult.class);

                    if (varietyDetailResult.getJsonvalue() != null &&
                            varietyDetailResult.getJsonvalue().getResults() != null
                            && varietyDetailResult.getJsonvalue().getResults().size() > 0) {
                        List<VarietyVideoDetailBean> varietyVideoDetailBeanList = new ArrayList<>();
                        for (VarietyDetailResult.JsonvalueBean.ResultsBean item :
                                varietyDetailResult.getJsonvalue().getResults()) {
                            VarietyVideoDetailBean bean = new VarietyVideoDetailBean();
                            bean.setUrl(VideoUtil.getParseUrl(item.getId(), VideoUtil.VIDEO_URL_TYPE_QQ));
                            bean.setTitle(item.getFields().getSecond_title());
                            bean.setTime(item.getFields().getPublish_date());
                            bean.setImage(item.getFields().getHorizontal_pic_url());
                            bean.setHot(item.getFields().getView_all_count());
                            varietyVideoDetailBeanList.add(bean);
                        }
                        videoPlayDetailBean.setVarietyVideoDetailBeans(varietyVideoDetailBeanList);
                    }

                    Document document = Jsoup.connect(url).get();
                    //                    介绍详情
                    Element detail = document.select(".detail_video").first();
                    videoPlayDetailBean.setScore(detail.select(".score_v").text());
                    videoPlayDetailBean.setTitle(detail.select(".video_title_cn").first().getElementsByTag("a").first().text());
                    //                    //                    video_type cf
                    Element subList = detail.select(".video_type.cf").first();
                    List<String> subTitleList = new ArrayList<>();
                    if (subList != null && subList.children().size() > 0) {
                        for (Element element :
                                subList.children()) {
                            subTitleList.add(element.text());
                        }
                    }

                    Element event = detail.select(".video_type video_type_even.cf").first();
                    if (event != null && event.children().size() > 0) {
                        for (Element element :
                                event.children()) {
                            subTitleList.add(element.text());
                        }
                    }
                    videoPlayDetailBean.setSubTitleList(subTitleList);
                    Elements tagList = detail.select(".tag_list");
                    if (tagList.first() != null && tagList.first().children().size() > 0) {
                        List<String> list = new ArrayList<>();
                        for (Element item :
                                tagList.first().children()) {
                            list.add(item.text());
                        }
                        videoPlayDetailBean.setTagList(list);
                    }
                    videoPlayDetailBean.setDesc(detail.select(".desc_txt").text());
                    Elements person = detail.select(".actor_list.cf");
                    if (person.first() != null && person.first().children().size() > 0) {
                        List<VideoPlayDetailBean.VideoPlayPerson> videoPlayPeople = new ArrayList<>();
                        Elements personChildren = person.first().children();
                        for (int i = 0; i < personChildren.size() - 1; i++) {
                            VideoPlayDetailBean.VideoPlayPerson bean = new VideoPlayDetailBean.VideoPlayPerson();
                            Element item = personChildren.get(i);
                            bean.setName(item.select(".actor_name").text());
                            bean.setDetail(item.select(".actor_desc").text());
                            bean.setUrl(item.getElementsByTag("a").first().attr("href"));
                            bean.setAvatar("https:" + item.getElementsByTag("img").first().attr("src"));
                            videoPlayPeople.add(bean);
                        }
                        videoPlayDetailBean.setVideoPlayPeople(videoPlayPeople);
                    }
                    return videoPlayDetailBean;
                }).observeOn(AndroidSchedulers.mainThread());
    }


}
