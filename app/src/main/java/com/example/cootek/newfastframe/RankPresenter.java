package com.example.cootek.newfastframe;

import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.mvp.BasePresenter;
import com.example.commonlibrary.mvp.IView;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.api.RankListBean;


import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class RankPresenter extends BasePresenter<IView<RankListBean>, RankModel> {


    public RankPresenter(IView<RankListBean> iView, RankModel baseModel) {
        super(iView, baseModel);
    }

    public void getRankList(final int type) {
        baseModel.getRepositoryManager().getApi(MusicApi.class).getRankList(type, 3, 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull RankListBean rankListBean) {
                        if (rankListBean.getError_code() == 22000) {
                            iView.updateData(rankListBean);
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(e != null ? e.getMessage() : null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getRankList(type);
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
