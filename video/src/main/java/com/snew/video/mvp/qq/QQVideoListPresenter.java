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

import java.util.ArrayList;
import java.util.List;

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
        stringBuilder.append("http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=10000&tid=687&appkey=c8094537f5337021&appid=200010596")
                .append("&intfname=").append(VideoUtil.getVideoHeaderType(videoType));
        if (isRefresh) {
            page = 0;
            iView.showLoading(null);
        }
        page++;
        //        http://list.video.qq.com/fcgi-bin/list_common_cgi?otype=json&platform=1&version=10000&intfname=web_vip_movie_new&tid=687&appkey=c8094537f5337021&appid=200010596&sort=17&pagesize=2&offset=0
        stringBuilder.append("&sort=").append(type).append("&pagesize=").append(15).append("&offset=").append((page - 1) * 15)
                .append("&sourcetype=1")
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


    public void getHeaderListData(int videoType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://list.video.qq.com/fcgi-bin/list_select_cgi?platform=1&version=10000&otype=json&intfname=");
        stringBuilder.append(VideoUtil.getVideoHeaderType(videoType));
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getVideoListHeaderData(stringBuilder.toString())
                .subscribeOn(Schedulers.io())
                .map(responseBody -> {
                    String body = responseBody.string().replace("QZOutputJson=", "");
                    return BaseApplication.getAppComponent().getGson().fromJson(body.substring(0, body.length() - 1)
                            , QQVideoTabListBean.class);
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QQVideoTabListBean>() {
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
}
