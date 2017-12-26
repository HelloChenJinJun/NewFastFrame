package com.example.news.mvp.consume;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.SystemInfoApi;
import com.example.news.bean.ConsumeQueryBean;
import com.example.news.util.NewsUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/21     17:37
 * QQ:         1981367757
 */

public class ConsumeQueryPresenter extends RxBasePresenter<IView<ConsumeQueryBean>,ConsumeQueryModel> {
    private int page=0;

    public ConsumeQueryPresenter(IView<ConsumeQueryBean> iView, ConsumeQueryModel baseModel) {
        super(iView, baseModel);
    }

    public void getQueryData(final boolean isRefresh) {
        if (isRefresh) {
            iView.showLoading(null);
            page=0;
        }
        page++;
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getConsumeData(NewsUtil.CONSUME_QUERY_URL,NewsUtil.getConsumeRequestBody(page,10))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ConsumeQueryBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ConsumeQueryBean consumeQueryBean) {
                          iView.updateData(consumeQueryBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getQueryData(isRefresh);
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
