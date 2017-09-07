package com.example.cootek.newfastframe.mvp.recommend;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.bean.RecommendSongBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/9/1.
 */

public class RecommendPresenter extends BasePresenter<IView<RecommendSongBean>, RecommendModel> {


    public RecommendPresenter(IView<RecommendSongBean> iView, RecommendModel baseModel) {
        super(iView, baseModel);
    }


    public void getRecommendData() {
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(MusicApi.class).getRecommendData("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecommendSongBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull RecommendSongBean recommendSongBean) {
                                if (recommendSongBean!=null&&recommendSongBean.getError_code()==22000){
                                    iView.updateData(recommendSongBean);
                                }else {
                                    onError(null);
                                }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getRecommendData();
                            }
                        });

                        if (e != null && e.getStackTrace() != null) {
                            if (e.getCause() != null) {
                                CommonLogger.e("cause:"+ e.getCause().toString());
                            }
                            CommonLogger.e("message:"+e.getMessage());
                            for (StackTraceElement item :
                                    e.getStackTrace()) {
                                CommonLogger.e(item.toString());
                            }
                        }

                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }

}
