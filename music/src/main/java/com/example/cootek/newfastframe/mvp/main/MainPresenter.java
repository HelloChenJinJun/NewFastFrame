package com.example.cootek.newfastframe.mvp.main;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicInfoProvider;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/8/11.
 */

public class MainPresenter extends RxBasePresenter<IView, MainModel> {
    private int num;


    public MainPresenter(IView iView, MainModel baseModel) {
        super(iView, baseModel);
        num = 0;
    }


    public void getAllMusic(final boolean isRefresh, final boolean isShowLoading) {
        if (isRefresh) {
            num = 0;
        }
        num++;
        if (isShowLoading) {
            iView.showLoading("正在加载");
        }
        MusicInfoProvider.getAllMusic(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MusicPlayBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull List<MusicPlayBean> musics) {
                        iView.updateData(musics);
                        if (musics == null) {
                            num--;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        CommonLogger.e("数据为出错");
                        String message = "";
                        if (e != null) {
                            message = e.getMessage();
                        }
                        CommonLogger.e(message);
                        iView.showError(message, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getAllMusic(isRefresh, isShowLoading);
                            }
                        });
                        num--;
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }
}
