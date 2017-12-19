package com.example.news.mvp.score;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.SystemInfoApi;
import com.example.news.bean.ScoreBean;
import com.example.news.util.NewsUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/19     14:25
 * QQ:         1981367757
 */

public class ScoreQueryPresenter extends BasePresenter<IView<ScoreBean>,ScoreQueryModel>{
    private int page=0;
    private int pageNum=10;
    public ScoreQueryPresenter(IView<ScoreBean> iView, ScoreQueryModel baseModel) {
        super(iView, baseModel);
    }


    public void getScore(final boolean isRefresh, final String time, final String courseName){
//        iView.showLoading(null);
        if (isRefresh) {
            page=0;
        }
        page++;
        baseModel.getRepositoryManager()
                .getApi(SystemInfoApi.class).getScore(NewsUtil.SCORE_QUERY_URL,NewsUtil
        .getScoreQueryBody(time,page,pageNum,courseName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ScoreBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ScoreBean scoreBean) {
                        iView.updateData(scoreBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getScore(isRefresh, time, courseName);
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
