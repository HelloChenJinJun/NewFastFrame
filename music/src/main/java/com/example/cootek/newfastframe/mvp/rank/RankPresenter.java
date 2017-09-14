package com.example.cootek.newfastframe.mvp.rank;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.bean.RankListBean;


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

    public void getRankList(final int type,boolean isRefresh) {
        if (type == MusicUtil.RANK_TYPE_LIST[MusicUtil.RANK_TYPE_LIST.length - 1]&&!isRefresh) {
            iView.showLoading("");
        }
        CommonLogger.e("getRankList");
        baseModel.getRepositoryManager().getApi(MusicApi.class).getRankList(type, 3, 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        CommonLogger.e("onSubscribe");
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull RankListBean rankListBean) {
                        CommonLogger.e("onNext");
                        if (rankListBean.getError_code() == 22000) {
                            iView.updateData(rankListBean);
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        CommonLogger.e("1onError");
                        if (type == MusicUtil.RANK_TYPE_LIST[0]) {
                            iView.showError(e != null ? e.getMessage() : null, new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
//                                    getRankList(MusicUtil.RANK_TYPE_LIST[0]);
                                }
                            });
                        }
                       CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {
                        CommonLogger.e("onComplete");
                        if (type == MusicUtil.RANK_TYPE_LIST[MusicUtil.RANK_TYPE_LIST.length - 1]) {
                            iView.hideLoading();
                        }
                    }
                });
    }


}
