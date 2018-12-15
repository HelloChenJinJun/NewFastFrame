package com.snew.video.mvp.search;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.snew.video.api.VideoApi;
import com.snew.video.bean.HotVideoBean;
import com.snew.video.bean.SearchVideoBean;
import com.snew.video.util.VideoUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/14     19:22
 */
public class SearchVideoPresenter extends RxBasePresenter<IView<BaseBean>, DefaultModel> {
    public SearchVideoPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getHotVideoData() {
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .getHotVideoData(VideoUtil.HOT_VIDEO_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotVideoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(HotVideoBean hotVideoBean) {
                        BaseBean baseBean = new BaseBean();
                        baseBean.setCode(200);
                        baseBean.setType(VideoUtil.BASE_TYPE_SEARCH_HOT);
                        baseBean.setData(hotVideoBean);
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


    public void preSearch(String content) {
        iView.showLoading(null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://s.video.qq.com/smartbox?num=10&otype=json&query=")
                .append(content);
        baseModel.getRepositoryManager().getApi(VideoApi.class)
                .searchVideo(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .flatMap((Function<ResponseBody, ObservableSource<SearchVideoBean>>) responseBody -> {
                    String body = responseBody.string().replace("QZOutputJson=", "");
                    SearchVideoBean searchVideoBean = BaseApplication.getAppComponent().getGson().fromJson(body.substring(0, body.length() - 1)
                            , SearchVideoBean.class);
                    return Observable.just(searchVideoBean);
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SearchVideoBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(SearchVideoBean searchVideoBean) {
                BaseBean baseBean = new BaseBean();
                baseBean.setCode(200);
                baseBean.setType(VideoUtil.BASE_TYPE_SEARCH_CONTENT);
                baseBean.setData(searchVideoBean);
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
