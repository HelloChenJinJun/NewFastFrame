package com.example.live.mvp.video;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.live.api.LiveApi;
import com.example.live.bean.LiveRoomBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/15      21:32
 * QQ:             1981367757
 */

public class VideoPresenter extends BasePresenter<IView<LiveRoomBean>, VideoModel> {
    public VideoPresenter(IView<LiveRoomBean> iView, VideoModel baseModel) {
        super(iView, baseModel);
    }

    public void enterRoom(final String uid) {
        baseModel.getRepositoryManager().getApi(LiveApi.class)
                .enterRoom(uid).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LiveRoomBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull LiveRoomBean liveRoomBean) {
                        CommonLogger.e(liveRoomBean.toString());
                        iView.updateData(liveRoomBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                enterRoom(uid);
                            }
                        });
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }
}
