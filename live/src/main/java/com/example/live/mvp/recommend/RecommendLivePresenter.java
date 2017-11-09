package com.example.live.mvp.recommend;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.live.api.LiveApi;
import com.example.live.bean.RecommendLiveBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      17:21
 * QQ:             1981367757
 */

public class RecommendLivePresenter extends BasePresenter<IView<RecommendLiveBean>,RecommendLiveModel>{

    public RecommendLivePresenter(IView<RecommendLiveBean> iView, RecommendLiveModel baseModel) {
        super(iView, baseModel);
    }

    public void getRecommendLiveData(final boolean isShowLoading, final boolean isRefresh) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        baseModel.getRepositoryManager().getApi(LiveApi.class)
                .getRecommendLiveData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommendLiveBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull RecommendLiveBean recommendLiveBean) {
                            iView.updateData(recommendLiveBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getRecommendLiveData(isShowLoading,isRefresh);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                            iView.hideLoading();
                    }
                });
    }
}
