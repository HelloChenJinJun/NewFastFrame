package com.snew.video.mvp.qq.detail;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.snew.video.api.VideoApi;
import com.snew.video.bean.QQTVVideoDetailBean;
import com.snew.video.bean.QQVideoDetailBean;
import com.snew.video.bean.TVPlayBean;
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

    public void getDetailData(String url) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), "url=" + url);
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .postUrlInfo("http://api.bbbbbb.me/zy/api.php", requestBody).subscribeOn(Schedulers.io())
                .flatMap(new Function<QQVideoDetailBean, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(QQVideoDetailBean qqVideoDetailBean) throws Exception {
                        if (qqVideoDetailBean.getCode() == 0) {
                            Uri uri = Uri.parse(qqVideoDetailBean.getUrl());
                            String key = uri.getQueryParameter("url");
                            String type = uri.getQueryParameter("type");
                            String cid = uri.getQueryParameter("cid");
                            if (qqVideoDetailBean.getPlay().equals("url")) {
                                if (key.endsWith(".m3u8")) {

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
                                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
                                    return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                            .postQQTVUrlInfo(VideoUtil.QQ_TV_URL, requestBody).subscribeOn(Schedulers.io());
                                } else if (key.endsWith("html") || key.contains(".jsp") || !TextUtils.isEmpty(cid)) {
                                    String realUrl = null;
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
                                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
                                    return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                            .postQQTVUrlInfo("http://api.bbbbbb.me/yunjx2/api.php", requestBody).subscribeOn(Schedulers.io());
                                }
                            } else if (qqVideoDetailBean.getPlay().equals("normal") || qqVideoDetailBean.getPlay().equals("hls")) {

                            }
                        }
                        return Observable.just(qqVideoDetailBean);
                    }
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
                            ListVideoManager.getInstance().updateUrl(deocderUrl);
                        } else if (object instanceof QQTVVideoDetailBean) {
                            QQTVVideoDetailBean qqtvVideoDetailBean = (QQTVVideoDetailBean) object;
                            CommonLogger.e(qqtvVideoDetailBean.toString());
                            String deocderUrl = URLDecoder.decode(qqtvVideoDetailBean.getUrl());
                            CommonLogger.e("deocderUrl:" + deocderUrl);
                            ListVideoManager.getInstance().updateUrl(deocderUrl);
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
        //        http://app.baiyug.cn:2019/vip/akey.php?url=https://v.qq.com/x/cover/f1m28hyp9ebwrrs.html
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(VideoUtil.URL_ONE).append("?url=").append(url);
        Observable.just(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .map((Function<String, Object>) s -> {
                    Document document = Jsoup.connect(s).get();
                    Elements bodyContent = document.getElementsByTag("iframe");
                    String src = bodyContent.attr("src");
                    String url1 = src.substring(src.indexOf("?url=") + 5, src.length() - 1);
                    if (url1.contains("html")) {

                    } else if (url1.endsWith(".m3u8")) {
                        return url1;
                    }
                    return null;
                }).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(Object o) {
                if (o instanceof String) {
                    ListVideoManager.getInstance().updateUrl((String) o);
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

    public void getDetailInfo(String id) {
        Observable.just(VideoUtil.getVideoDetailUrl(id)).subscribeOn(Schedulers.io())
                .map(new Function<String, VideoPlayDetailBean>() {
                    @Override
                    public VideoPlayDetailBean apply(String s) throws Exception {
                        Document document = Jsoup.connect(s).get();
                        Elements elements = document.select(".mod_episode");

                        VideoPlayDetailBean videoPlayDetailBean = new VideoPlayDetailBean();
                        if (elements.first() != null && elements.first().children().size() > 0) {
                            List<TVPlayBean> result = new ArrayList<>();
                            for (int i = 0; i < elements.first().children().size(); i++) {
                                TVPlayBean tvPlayBean = new TVPlayBean();
                                Element item = elements.first().child(i);
                                Elements a = item.getElementsByTag("a");
                                if (a.first() != null) {
                                    String url = a.first().attr("href");
                                    tvPlayBean.setUrl(url);
                                }
                                tvPlayBean.setTitle((i + 1) + "");
                                result.add(tvPlayBean);
                            }
                            videoPlayDetailBean.setTVPlayBeans(result);
                        }
                        Element detail = document.select(".detail_video").first();
                        videoPlayDetailBean.setScore(detail.select(".score_v").text());
                        videoPlayDetailBean.setTitle(detail.select(".video_title_collect.cf").text());
                        videoPlayDetailBean.setSubTitle(detail.select(".video_type.cf").text());
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
                                bean.setAvatar("https:" + item.getElementsByTag("img").first().attr("src"));
                                videoPlayPeople.add(bean);
                            }
                            videoPlayDetailBean.setVideoPlayPeople(videoPlayPeople);
                        }
                        return videoPlayDetailBean;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoPlayDetailBean>() {
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
}
