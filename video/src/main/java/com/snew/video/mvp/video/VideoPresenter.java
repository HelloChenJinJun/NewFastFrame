package com.snew.video.mvp.video;


import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.manager.video.ListVideoManager;
import com.example.commonlibrary.manager.video.VideoController;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.snew.video.api.VideoApi;
import com.snew.video.bean.VideoBean;
import com.snew.video.bean.VideoDetailBean;
import com.snew.video.bean.VideoListBean;
import com.snew.video.util.JSEngine;
import com.snew.video.util.VideoUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/27     15:44
 */
public class VideoPresenter extends RxBasePresenter<IView<List<VideoBean>>, DefaultModel> {
    private long time = 0;
    private JSEngine mJSEngine;

    public VideoPresenter(IView<List<VideoBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
        mJSEngine = new JSEngine("parse.js");
    }

    public void getData(boolean isRefresh, String type) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        getRealData(isRefresh, type);
    }


    private void getRealData(boolean isRefresh, String type) {
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getVideoListData(getUrl(isRefresh, type)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<VideoListBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(VideoListBean videoListBean) {
                CommonLogger.e(videoListBean.toString());
                List<VideoBean> list = new ArrayList<>();
                if (videoListBean.getMessage().equals("success")) {
                    for (VideoListBean.DataBean dataBean :
                            videoListBean.getData()) {
                        VideoBean videoBean = new VideoBean(dataBean.getTitle(), 0, dataBean.getImage_url(), getVideoUrl(dataBean.getGroup_id()));
                        list.add(videoBean);
                    }
                    time = videoListBean.getNext().getMax_behot_time();
                }
                iView.updateData(list);
            }

            @Override
            public void onError(Throwable e) {
                CommonLogger.e("onError");
                iView.showError(e.getMessage(), null);
            }

            @Override
            public void onComplete() {
                CommonLogger.e("onComplete");
                iView.hideLoading();
            }
        });
    }

    private void getCookie(String url) {
        baseModel.getRepositoryManager().getApi(VideoApi.class).getCookie("http://toutiao.iiilab.com/")
                .subscribeOn(Schedulers.io())
                .flatMap((Function<ResponseBody, ObservableSource<?>>) responseBody -> {
                    ObservableSource<ResponseBody> responseBodyObservable = baseModel
                            .getRepositoryManager().getApi(VideoApi.class)
                            .getOtherCookie("http://service0.iiilab.com/sponsor/getByPage", getOtherBody());
                    return responseBodyObservable;
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(Object o) {
                getDetailData(url);
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

    private RequestBody getOtherBody() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("page=toutiao");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
        return requestBody;
    }

    private String getVideoUrl(String grouopId) {
        //        https://www.365yg.com/a6626618978854765069/
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(VideoUtil.BASE_VIDEO_URL).append("a").append(grouopId).append("/");
        return stringBuilder.toString();
    }

    private String getUrl(boolean isRefresh, String type) {
        if (isRefresh) {
            time = 0;
        }
        StringBuilder stringBuilder = new StringBuilder();
        //        http://www.ixigua.com/api/pc/feed/?max_behot_time=0&category=subv_xg_music
        //        http://www.365yg.com/api/pc/feed/?max_behot_time=
        stringBuilder.append(VideoUtil.BASE_VIDEO_URL).append("api/pc/feed/?max_behot_time=")
                .append(time).append("&category=").append(type);
        return stringBuilder.toString();
    }


    public void getDetailData(String url) {
        boolean needGetCookie = BaseApplication.getAppComponent().getSharedPreferences()
                .getString(VideoUtil.GSP, null) == null;
        if (needGetCookie) {
            getCookie(url);
            return;
        }
        String result = mJSEngine.runScript(url,"getRelatedParams");
        String[] strings = result.split("@");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("link=").append(url)
                .append("&r=").append(strings[0]).append("&s=").append(strings[1]);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8"), stringBuilder.toString());
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getVideoDetailData("http://service0.iiilab.com/video/web/toutiao"
                        , requestBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(VideoDetailBean videoDetailBean) {
                        if (videoDetailBean.getRetCode() == 300) {
                            getCookie(url);
                        } else if (videoDetailBean.getRetCode() == 200) {
                            if (videoDetailBean != null) {
                                if (videoDetailBean.getRetCode() != 200) {
                                    ListVideoManager.getInstance().error();
                                } else {
                                    List<VideoController.Clarity> list = new ArrayList<>();
                                    for (VideoDetailBean.DataBean.VideoBean.LinkBean dataBean :
                                            videoDetailBean.getData().getVideo().getLink()) {
                                        VideoController.Clarity clarity = new VideoController.Clarity(dataBean.getType().substring(0, 2)
                                                , dataBean.getType().substring(2, dataBean.getType().length()), dataBean.getUrl());
                                        list.add(clarity);
                                    }
                                    ListVideoManager.getInstance().getCurrentPlayer().setClarity(list);
                                    ListVideoManager.getInstance().updateUrl(videoDetailBean.getData().getVideo().getLink().get(0).getUrl());
                                }
                            }
                            CommonLogger.e(videoDetailBean.toString());
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

}
