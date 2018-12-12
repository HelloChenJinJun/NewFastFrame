package com.snew.video.mvp.qq;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.snew.video.api.VideoApi;
import com.snew.video.bean.QQTVVideoDetailBean;
import com.snew.video.bean.QQVideoDetailBean;
import com.snew.video.bean.QQVideoListBean;
import com.snew.video.bean.VideoBean;
import com.snew.video.util.VideoUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
 * 创建时间:    2018/12/12     16:02
 */
public class QQVideoListPresenter extends RxBasePresenter<IView<List<VideoBean>>, DefaultModel> {
    private int page = 0;

    public QQVideoListPresenter(IView<List<VideoBean>> iView, DefaultModel baseModel) {
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
                            if (getRealPlayUrl(qqVideoDetailBean.getUrl()).endsWith("qqmtv")) {
                                Document document = Jsoup.connect(qqVideoDetailBean.getUrl()).get();
                                String content = document.outerHtml();
                                String start = "{\"id\": \"";
                                String end = "\",\"type\"";
                                String id = content.substring(content.indexOf(start) + start.length(), content.indexOf(end));
                                //                                        id=1006_43dc5ba04ae343ff97111d83b437f2f4&type=qqmtv&siteuser=&md5=ab595b8cad8ab5d6d6c0cafa665eloij&hd=&lg=
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("id=").append(id).append("&type=qqmtv&siteuser=&md5=").append(VideoUtil.getSignedValue())
                                        .append("&hd=&lg=");
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
                                return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                        .postQQTVUrlInfo(VideoUtil.QQ_TV_URL, requestBody).subscribeOn(Schedulers.io());
                                //                                return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                //                                        .getContent(qqVideoDetailBean.getUrl())
                                //                                        .subscribeOn(Schedulers.io()).flatMap(new Function<ResponseBody, ObservableSource<?>>() {
                                //                                            @Override
                                //                                            public ObservableSource<?> apply(ResponseBody responseBody) throws Exception {
                                //                                                String content = Jsoup.parse(responseBody.string()).outerHtml();
                                //                                                String start = "{\"id\": \"";
                                //                                                String end = "\",\"type\"";
                                //                                                String id = content.substring(content.indexOf(start) + start.length(), content.indexOf(end));
                                //                                                //                                        id=1006_43dc5ba04ae343ff97111d83b437f2f4&type=qqmtv&siteuser=&md5=ab595b8cad8ab5d6d6c0cafa665eloij&hd=&lg=
                                //                                                StringBuilder stringBuilder = new StringBuilder();
                                //                                                stringBuilder.append("id=").append(id).append("&type=qqmtv&siteuser=&md5=").append(VideoUtil.getSignedValue())
                                //                                                        .append("&hd=&lg=");
                                //                                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
                                //                                                return baseModel.getRepositoryManager().getApi(VideoApi.class)
                                //                                                        .postQQTVUrlInfo(VideoUtil.QQ_TV_URL, requestBody).subscribeOn(Schedulers.io());
                                //                                            }
                                //                                        });
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
                            ListVideoManager.getInstance().updateUrl(getRealPlayUrl(qqVideoDetailBean.getUrl()));
                        } else if (object instanceof QQTVVideoDetailBean) {
                            QQTVVideoDetailBean qqtvVideoDetailBean = (QQTVVideoDetailBean) object;
                            CommonLogger.e(qqtvVideoDetailBean.toString());
                            ListVideoManager.getInstance().updateUrl(getRealPlayUrl(qqtvVideoDetailBean.getUrl()));
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


    private String getRealPlayUrl(String url) {
        CommonLogger.e("url:" + url);
        if (url.contains("?")) {
            String realUrl = url.substring(url.indexOf("=") + 1);
            CommonLogger.e("realUrl:" + realUrl);
            String decodeUrl = URLDecoder.decode(realUrl);
            CommonLogger.e("decodeUrl:" + decodeUrl);
            if (decodeUrl.contains("https")) {
                return decodeUrl.replace("https", "http");
            } else {
                return decodeUrl;
            }
        }
        return null;
    }

    public void getData(boolean isRefresh, int type) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(VideoUtil.QQ_VIDEO_BASE_URL);

        if (isRefresh) {
            page = 0;
            iView.showLoading(null);
        }
        page++;
        //        http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=10000&intfname=web_vip_movie_new&tid=687&appkey=c8094537f5337021&appid=200010596&sort=17&pagesize=2&offset=0
        stringBuilder.append("&sort=").append(type).append("&pagesize=").append(10).append("&offset=").append((page - 1) * 10);
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getQQVideoListBean(stringBuilder.toString())
                .map(responseBody -> {
                    String body = responseBody.string().replace("QZOutputJson=", "");
                    QQVideoListBean qqVideoListBean
                            = BaseApplication.getAppComponent().getGson().fromJson(body.substring(0, body.length() - 1)
                            , QQVideoListBean.class);
                    return qqVideoListBean;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QQVideoListBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(QQVideoListBean qqVideoListBean) {
                        if (qqVideoListBean.getJsonvalue() != null && qqVideoListBean.getJsonvalue().getResults() != null
                                && qqVideoListBean.getJsonvalue().getResults().size() > 0) {
                            List<VideoBean> list = new ArrayList<>();
                            for (QQVideoListBean.JsonvalueBean.ResultsBean dataBean :
                                    qqVideoListBean.getJsonvalue().getResults()) {
                                VideoBean videoBean = new VideoBean(dataBean.getFields().getTitle(), 0, dataBean.getFields().getHorizontal_pic_url(), getVideoUrl(dataBean.getId()));
                                list.add(videoBean);
                            }
                            iView.updateData(list);
                        } else {
                            CommonLogger.e("加载数据出错");
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

    private String getVideoUrl(String id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://v.qq.com/x/cover/").append(id).append(".html");
        //        "https://v.qq.com/x/cover/h0meep6p766jgqh.html"
        return stringBuilder.toString();
    }
}
