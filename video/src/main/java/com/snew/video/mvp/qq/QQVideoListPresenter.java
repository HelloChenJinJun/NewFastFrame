package com.snew.video.mvp.qq;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.snew.video.api.VideoApi;
import com.snew.video.bean.QQVideoListBean;
import com.snew.video.bean.QQVideoTabListBean;
import com.snew.video.util.VideoUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/12     16:02
 */
public class QQVideoListPresenter extends RxBasePresenter<IView<BaseBean>, DefaultModel> {
    private int page = 0;

    public QQVideoListPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }


    //itype: -1
    //iyear: 2018
    //iarea: -1
    //iawards: -1
    public void getData(boolean isRefresh, int videoType, int type, int year, int area, int award, int sort) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=")
                .append(VideoUtil.getVideoVersion(videoType))
                .append("&tid=687&appkey=c8094537f5337021&appid=200010596")
                .append("&intfname=").append(VideoUtil.getVideoHeaderType(videoType));
        if (isRefresh) {
            page = 0;
            iView.showLoading(null);
        }
        page++;
        //        http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=10000&intfname=web_vip_movie_new&tid=687&appkey=c8094537f5337021&appid=200010596&sort=17&pagesize=2&offset=0
        stringBuilder.append("&sort=").append(type).append("&pagesize=").append(15).append("&offset=").append((page - 1) * 15)
                .append("&sourcetype=").append(VideoUtil.getSourceType(videoType))
                .append("&type=").append(videoType)
                .append("&itype=").append(type).append("&iyear=").append(year).append("&iarea=").append(area).append("&iawards=").append(award)
                .append("&sort=").append(sort);
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
                        List<QQVideoListBean.JsonvalueBean.ResultsBean> result = null;
                        if (qqVideoListBean.getJsonvalue() != null && qqVideoListBean.getJsonvalue().getResults() != null
                                && qqVideoListBean.getJsonvalue().getResults().size() > 0) {
                            result = new ArrayList<>(qqVideoListBean.getJsonvalue().getResults());
                        } else {
                            page--;
                        }
                        BaseBean baseBean = new BaseBean();
                        baseBean.setCode(200);
                        baseBean.setData(result);
                        baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_LIST_DATA);
                        iView.updateData(baseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                        page--;
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }


    public void getUpdateData(boolean isRefresh, int videoType, String classify, String area, String year, String sort) {
        if (isRefresh) {
            page = 0;
            iView.showLoading(null);
        }
        page++;

        //        href="/index.php/vod/show/by/hits/class/%E7%BD%91%E7%BB%9C%E7%94%B5%E5%BD%B1/id/1/"
        //        index.php/vod/show/area/%E9%A6%99%E6%B8%AF/by/hits/id/1/"
        //        /index.php/vod/show/by/hits/id/1/year/2011/"
        StringBuilder stringBuilder = new StringBuilder("http://m.bt361.cn/vod/show");
        stringBuilder.append("/by/");
        switch (sort) {
            case "按时间":
                stringBuilder.append("time");
                break;
            case "按人气":
                stringBuilder.append("hits");
                break;
            default:
                stringBuilder.append("score");
                break;
        }
        if (!classify.equals("全部")) {
            stringBuilder.append("/class/").append(classify);
        }
        if (!area.equals("全部")) {
            stringBuilder.append("/area/").append(area);
        }
        if (!year.equals("全部")) {
            stringBuilder.append("/year/").append(year);
        }

        stringBuilder.append("/id/").append(videoType);

        if (page > 1) {
            stringBuilder.append("/page/").append(page);
        }
        stringBuilder.append("/");
        Observable.just(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .map(s -> {
                    Document document = Jsoup.connect(s).get();
                    Elements videoList = document.select(".video-item");
                    List<QQVideoListBean.JsonvalueBean.ResultsBean> dataList = new ArrayList<>();
                    if (videoList.size() > 0) {
                        for (Element item :
                                videoList) {
                            Elements a = item.getElementsByTag("a");
                            String link = a.get(0).attr("href");
                            String id = link.substring(link.indexOf("id/") + 3, link.length() - 1);
                            String imageCover = a.get(0).attr("data-original");
                            String title = a.get(1).text();
                            String subTitle = null;
                            if (item.select(".video-duration").first() != null) {
                                subTitle = item.select(".video-duration").first().text();
                            }
                            QQVideoListBean.JsonvalueBean.ResultsBean bean = new QQVideoListBean.JsonvalueBean.ResultsBean();
                            QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean fieldsBean = new QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean();
                            fieldsBean.setTitle(title);
                            fieldsBean.setSecond_title(subTitle);
                            fieldsBean.setVertical_pic_url(imageCover);
                            bean.setId(id);
                            bean.setFields(fieldsBean);
                            dataList.add(bean);
                        }
                    }
                    return dataList;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<QQVideoListBean.JsonvalueBean.ResultsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(List<QQVideoListBean.JsonvalueBean.ResultsBean> resultsBeans) {
                        BaseBean baseBean = new BaseBean();
                        baseBean.setCode(200);
                        baseBean.setData(resultsBeans);
                        baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_LIST_DATA);
                        iView.updateData(baseBean);
                        if (resultsBeans == null || resultsBeans.size() == 0) {
                            page--;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                        page--;
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });

    }


    public void getHeaderListData(int videoUrlType, int videoType) {
        Observable<QQVideoTabListBean> observable;
        if (videoUrlType == VideoUtil.VIDEO_URL_TYPE_QQ) {
            observable = getQQHeaderData(videoType);
        } else {
            observable = getUpdateHeaderData(videoType);
        }
        observable.subscribe(new Observer<QQVideoTabListBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(QQVideoTabListBean qqVideoTabListBean) {
                BaseBean baseBean = new BaseBean();
                baseBean.setCode(200);
                baseBean.setData(qqVideoTabListBean);
                baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_LIST_HEADER);
                iView.updateData(baseBean);
            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage(), null);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private Observable<QQVideoTabListBean> getUpdateHeaderData(int videoType) {
        //        http://m.tbb361.com/index.php/vod/show/by/hits/id/1/
        StringBuilder stringBuilder = new StringBuilder("http://m.tbb361.com/index.php/vod/show/by/hits/id/").append(videoType).append("/");
        return Observable.just(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .map(s -> {
                    Document document = Jsoup.connect(s).get();
                    Elements elements = document.select(".con");
                    QQVideoTabListBean qqVideoTabListBean = new QQVideoTabListBean();
                    List<QQVideoTabListBean.IndexBean> indexBeanList = new ArrayList<>();
                    int itemSize = elements.size();
                    if (itemSize > 0) {
                        for (int j = 0; j < itemSize; j++) {
                            Element item = elements.get(j);
                            int size = item.children().size();
                            if (size > 0) {
                                QQVideoTabListBean.IndexBean b = new QQVideoTabListBean.IndexBean();
                                List<QQVideoTabListBean.IndexBean.OptionBean> list = new ArrayList<>();
                                for (int i = 0; i < size; i++) {
                                    Element child = item.child(i);
                                    QQVideoTabListBean.IndexBean.OptionBean o = new QQVideoTabListBean.IndexBean.OptionBean();
                                    if (i == 0) {
                                        b.setDisplay_name(child.text());
                                        o.setDisplay_name("全部");
                                    } else {
                                        o.setDisplay_name(child.text());
                                    }
                                    list.add(o);
                                }
                                b.setOption(list);
                                indexBeanList.add(b);
                            }
                        }
                    }

                    Elements sort = document.select(".show_by");

                    if (sort.first() != null && sort.first().children().size() > 0) {
                        List<QQVideoTabListBean.IndexBean.OptionBean> list = new ArrayList<>();
                        for (Element item :
                                sort.first().children()) {
                            QQVideoTabListBean.IndexBean.OptionBean o = new QQVideoTabListBean.IndexBean.OptionBean();
                            o.setDisplay_name(item.text().trim());
                            list.add(o);
                        }
                        QQVideoTabListBean.IndexBean b = new QQVideoTabListBean.IndexBean();
                        b.setOption(list);
                        indexBeanList.add(b);
                    }
                    qqVideoTabListBean.setIndex(indexBeanList);
                    Elements videoList = document.select(".video-item");
                    if (videoList.size() > 0) {
                        List<QQVideoListBean.JsonvalueBean.ResultsBean> dataList = new ArrayList<>();
                        for (Element item :
                                videoList) {
                            Elements a = item.getElementsByTag("a");
                            String link = a.get(0).attr("href");
                            String id = link.substring(link.indexOf("id/") + 3, link.length() - 1);
                            String imageCover = a.get(0).attr("data-original");
                            String title = a.get(1).text();
                            String subTitle = item.select(".video-duration").get(0).text();
                            QQVideoListBean.JsonvalueBean.ResultsBean bean = new QQVideoListBean.JsonvalueBean.ResultsBean();
                            QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean fieldsBean = new QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean();
                            fieldsBean.setTitle(title);
                            fieldsBean.setSecond_title(subTitle);
                            fieldsBean.setVertical_pic_url(imageCover);
                            bean.setId(id);
                            bean.setFields(fieldsBean);
                            dataList.add(bean);
                        }
                        Observable.just(dataList).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<List<QQVideoListBean.JsonvalueBean.ResultsBean>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        addDispose(d);
                                    }

                                    @Override
                                    public void onNext(List<QQVideoListBean.JsonvalueBean.ResultsBean> resultsBeans) {
                                        BaseBean baseBean = new BaseBean();
                                        baseBean.setCode(200);
                                        baseBean.setData(resultsBeans);
                                        baseBean.setType(VideoUtil.BASE_TYPE_VIDEO_LIST_DATA);
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
                    return qqVideoTabListBean;
                }).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<QQVideoTabListBean> getQQHeaderData(int videoType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://list.video.qq.com/fcgi-bin/list_select_cgi?platform=1&version=");
        stringBuilder.append(VideoUtil.getVideoVersion(videoType)).append("&otype=json&intfname=").append(VideoUtil.getVideoHeaderType(videoType));
        iView.showLoading(null);
        return baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getVideoListHeaderData(stringBuilder.toString())
                .subscribeOn(Schedulers.io())
                .map(responseBody -> {
                    String body = responseBody.string().replace("QZOutputJson=", "");
                    return BaseApplication.getAppComponent().getGson().fromJson(body.substring(0, body.length() - 1)
                            , QQVideoTabListBean.class);
                }).observeOn(AndroidSchedulers.mainThread());

    }
}
