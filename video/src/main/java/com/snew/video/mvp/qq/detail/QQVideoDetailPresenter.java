package com.snew.video.mvp.qq.detail;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
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
import com.snew.video.bean.OtherVideoDetailBean;
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


    public void commonParseUrl(String url) {
//http://api.bbbbbb.me/yunjx/?url=


//
//        Observable.just(url).subscribeOn(Schedulers.io())
//                .map(new Function<String, Object>() {
//                    @Override
//                    public Object apply(String s) throws Exception {
//                        return null;
//                    }
//                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
//        StringBuilder stringBuilder=new StringBuilder("http://api.bbbbbb.me/yunjx/?url=").append(url);
//        baseModel.getRepositoryManager().getApi(VideoApi.class)
//                .postUrlInfo(stringBuilder.toString())


    }


    public void getDetailData(String url, boolean isSwitch) {
        if (url.startsWith("http://m.bt361.cn")) {
            getUpdateDetailData(url);
            return;
        }

        if (isSwitch) {
            getDetailDataForOther(url);
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), "url=" + url);
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .postUrlInfo("http://api.bbbbbb.me/zy/api.php", requestBody).subscribeOn(Schedulers.io())
                .flatMap(qqVideoDetailBean -> {
                    if (qqVideoDetailBean.getCode() == 0) {
                        Uri uri;
                        String key;
                        String type;
                        String cid;
                        if (URLUtil.isValidUrl(qqVideoDetailBean.getUrl())) {
                            uri = Uri.parse(qqVideoDetailBean.getUrl());
                            key = uri.getQueryParameter("url");
                            type = uri.getQueryParameter("type");
                            cid = uri.getQueryParameter("cid");
                        } else {
                            return Observable.just(qqVideoDetailBean);
                        }
                        if (qqVideoDetailBean.getPlay().equals("url")) {
                            if (key.contains(".m3u8") || key.contains(".mp4")) {
                                if (!qqVideoDetailBean.getUrl().startsWith("http")) {
                                    qqVideoDetailBean.setUrl(key);
                                }

                            } else if ("qqmtv".equals(type)) {
                                String content = Jsoup.connect(qqVideoDetailBean.getUrl()).get().outerHtml();
                                String s = "\"\\x24\\x28\\x27\\x23\\x68\\x64\\x4d\\x64\\x35\\x27\\x29\\x2e\\x76\\x61\\x6c\\x28\\x27\\x63\\x34\\x35\\x36\\x30\\x62\\x34\\x35\\x38\\x33\\x62\\x39\\x30\\x65\\x35\\x39\\x36\\x36\\x34\\x37\\x35\\x36\\x31\\x34\\x66\\x35\\x35\\x38\\x30\\x61\\x34\\x34\\x27\\x29\\x3b\"";
                                String start = "eval(";
                                int index = content.indexOf(start);
                                String coreString = content.substring(index + start.length(), index + start.length() + s.length());
                                String parseContent = VideoUtil.getSignedValue(coreString);
                                CommonLogger.e("parseContent" + parseContent);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("id=").append(key).append("&type=qqmtv&siteuser=&md5=").append(parseContent)
                                        .append("&hd=&lg=");
                                RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
                                return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                        .postQQTVUrlInfo(VideoUtil.QQ_TV_URL, requestBody1).subscribeOn(Schedulers.io());
                            } else if (key.endsWith("html") || key.contains(".jsp") || !TextUtils.isEmpty(cid)) {
                                String realUrl;
                                if (qqVideoDetailBean.getUrl().startsWith("https://api.bbbbbb.me")) {
                                    realUrl = qqVideoDetailBean.getUrl();
                                    if (TextUtils.isEmpty(uri.getQueryParameter("type"))) {
                                        realUrl += "&type=";
                                    }
                                } else {
                                    realUrl = key;
                                }
                                CommonLogger.e("realUrl:" + realUrl);
                                String content = Jsoup.connect(realUrl).header("referer", qqVideoDetailBean.getUrl()).get().outerHtml();
                                String s = "\"\\x24\\x28\\x27\\x23\\x68\\x64\\x4d\\x64\\x35\\x27\\x29\\x2e\\x76\\x61\\x6c\\x28\\x27\\x63\\x34\\x35\\x36\\x30\\x62\\x34\\x35\\x38\\x33\\x62\\x39\\x30\\x65\\x35\\x39\\x36\\x36\\x34\\x37\\x35\\x36\\x31\\x34\\x66\\x35\\x35\\x38\\x30\\x61\\x34\\x34\\x27\\x29\\x3b\"";
                                String start = "eval(";
                                int index = content.indexOf(start);
                                String coreString = content.substring(index + start.length(), index + start.length() + s.length());
                                String parseContent = VideoUtil.getSignedValue(coreString);
                                CommonLogger.e("parseContent" + parseContent);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("id=").append(key).append("&type=auto&siteuser=&md5=").append(parseContent)
                                        .append("&hd=&lg=");
                                RequestBody requestBody1 = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
                                return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                        .postQQTVUrlInfo("http://api.bbbbbb.me/yunjx2/api.php", requestBody1).subscribeOn(Schedulers.io());
                            }
                        } else if (qqVideoDetailBean.getPlay().equals("normal") || qqVideoDetailBean.getPlay().equals("hls")) {

                        }
                    }
                    return Observable.just(qqVideoDetailBean);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(Object object) {
                        if (object instanceof QQVideoDetailBean) {
                            QQVideoDetailBean qqVideoDetailBean = (QQVideoDetailBean) object;
                            CommonLogger.e(qqVideoDetailBean.toString());
                            String deocderUrl = URLDecoder.decode(qqVideoDetailBean.getUrl());
                            if (!URLUtil.isValidUrl(deocderUrl) && deocderUrl.endsWith(".m3u8")) {
                                deocderUrl = deocderUrl.substring(deocderUrl.indexOf("url=") + 4, deocderUrl.length());
                            }
                            CommonLogger.e("deocderUrl:" + deocderUrl);
                            if (!deocderUrl.startsWith("http://api.bbbbbb.me") && URLUtil.isValidUrl(deocderUrl)) {
                                BaseBean baseBean = new BaseBean();
                                baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_DETAIL_URL);
                                baseBean.setData(deocderUrl);
                                //                                用于标识当前请求的url
                                baseBean.setExtraInfo(url);
                                baseBean.setCode(200);
                                iView.updateData(baseBean);
                            } else {
                                getDetailDataForOther(url);
                            }
                        } else if (object instanceof QQTVVideoDetailBean) {
                            QQTVVideoDetailBean qqtvVideoDetailBean = (QQTVVideoDetailBean) object;
                            CommonLogger.e(qqtvVideoDetailBean.toString());
                            String deocderUrl = URLDecoder.decode(qqtvVideoDetailBean.getUrl());
                            CommonLogger.e("deocderUrl:" + deocderUrl);
                            if (!deocderUrl.startsWith("http://api.bbbbbb.me") && URLUtil.isValidUrl(deocderUrl)) {
                                BaseBean baseBean = new BaseBean();
                                baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_DETAIL_URL);
                                baseBean.setData(deocderUrl);
                                baseBean.setCode(200);
                                baseBean.setExtraInfo(url);
                                iView.updateData(baseBean);
                            } else {
                                getDetailDataForOther(url);
                            }
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


    public void getDetailDataForOther(String url) {
        //        http://baiyug.php20.scutsource.cn/vip_cc/index.php?url=https://v.qq.com/x/cover/8uc2yffueif7n39.html
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(VideoUtil.URL_ONE).append("?url=").append(url);
        Observable.just(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .flatMap((Function<String, ObservableSource<OtherVideoDetailBean>>) str -> {
                    Document document = Jsoup.connect(str).header("Referer", "http://app.baiyug.cn").get();

                    Elements bodyContent = document.getElementsByTag("iframe");
                    String src = bodyContent.attr("src");
                    String url1 = src.substring(src.indexOf("?url=") + 5, src.length() - 1);


                    int num = 1;
                    while (!URLUtil.isValidUrl(url1) && num < 5) {
                        url1 = new String(Base64.decode(url1, Base64.DEFAULT));
                        num++;
                    }


                    String content = document.outerHtml();

                    String keyStart = "key\":\"";
                    String keyEnd = "\", \"url";
                    int keyIndex = content.indexOf(keyStart);
                    String key;
                    if (keyIndex > 0) {
                        key = content.substring(keyIndex + keyStart.length(), content.indexOf(keyEnd));
                    } else {
                        String s = "\"\\x24\\x28\\x27\\x23\\x68\\x64\\x4d\\x64\\x35\\x27\\x29\\x2e\\x76\\x61\\x6c\\x28\\x27\\x63\\x34\\x35\\x36\\x30\\x62\\x34\\x35\\x38\\x33\\x62\\x39\\x30\\x65\\x35\\x39\\x36\\x36\\x34\\x37\\x35\\x36\\x31\\x34\\x66\\x35\\x35\\x38\\x30\\x61\\x34\\x34\\x27\\x29\\x3b\"";
                        String start = "eval(";
                        int index = content.lastIndexOf(start);
                        String coreString = content.substring(index + start.length(), index + start.length() + s.length());
                        key = VideoUtil.getSignedValue(coreString);
                    }
                    StringBuilder bodyInfo = new StringBuilder();
                    bodyInfo.append("key=").append(key).append("&url=").append(url);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), bodyInfo.toString());
                    return baseModel.getRepositoryManager().getApi(VideoApi.class)
                            .postBaiYuUrl("http://baiyug.php20.scutsource.cn/vip_cc/api.php", requestBody);
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<OtherVideoDetailBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(OtherVideoDetailBean o) {

                BaseBean baseBean = new BaseBean();
                baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_DETAIL_URL);
                baseBean.setData(o.getUrl());
                baseBean.setCode(200);
                baseBean.setExtraInfo(url);
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
        StringBuilder stringBuilder = new StringBuilder("http://s.video.qq.com/get_playsource?type=4&range=1-20&otype=json&num_mod_cnt=20&id=");
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
