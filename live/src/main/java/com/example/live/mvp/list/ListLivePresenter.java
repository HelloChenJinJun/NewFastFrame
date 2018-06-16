package com.example.live.mvp.list;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.live.api.LiveApi;
import com.example.live.bean.ListLiveBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      21:01
 * QQ:             1981367757
 */

public class ListLivePresenter extends BasePresenter<IView<ListLiveBean>,ListLiveModel>{
    public ListLivePresenter(IView<ListLiveBean> iView, ListLiveModel baseModel) {
        super(iView, baseModel);
    }

    public void getCategoryItemData(final boolean isShowLoading, final boolean isRefresh, final String slug) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        baseModel.getRepositoryManager().getApi(LiveApi.class)
                .getCategoryItemData(slug)
                .compose(iView.<ListLiveBean>bindLife()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ListLiveBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ListLiveBean listLiveBean) {
                        iView.updateData(listLiveBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getCategoryItemData(isShowLoading, isRefresh,slug);
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
