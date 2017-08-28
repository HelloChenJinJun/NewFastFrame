package com.example.cootek.newfastframe;

import android.os.Build;

import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.mvp.BasePresenter;
import com.example.commonlibrary.mvp.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.api.DownLoadMusicBean;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.api.RankListBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class RankDetailPresenter extends BasePresenter<IView<Object>, RankDetailModel> {
    private int num = 0;

    public RankDetailPresenter(IView<Object> iView, RankDetailModel baseModel) {
        super(iView, baseModel);
        num = 0;
    }


    public void getRankDetailInfo(final int type, final boolean isRefresh, final boolean isShowLoading) {
        if (isRefresh) {
            num = 0;
        }
        if (isShowLoading) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class).getRankList(type, 15, (num - 1) * 15)
                .subscribeOn(Schedulers.io())
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
                            if (rankListBean.getSong_list() != null && rankListBean.getSong_list().size() > 0) {
                                getMusicDetailInfo(rankListBean.getSong_list());
                            }
                            iView.updateData(rankListBean);
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                num--;
                                getRankDetailInfo(type, isRefresh, isShowLoading);
                            }
                        });
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                        CommonLogger.e("onComplete");
                    }
                });
    }

    private void getMusicDetailInfo(List<RankListBean.SongListBean> songList) {
        for (RankListBean.SongListBean bean
                : songList
                ) {
            getMusicDetailInfo(bean);
        }
    }

    private void getMusicDetailInfo(RankListBean.SongListBean bean) {
        baseModel.getRepositoryManager().getApi(MusicApi.class).getDownLoadMusicInfo(bean.getSong_id())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownLoadMusicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull DownLoadMusicBean downLoadMusicBean) {
                        if (downLoadMusicBean.getError_code() == 22000) {
                            iView.updateData(downLoadMusicBean);
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
