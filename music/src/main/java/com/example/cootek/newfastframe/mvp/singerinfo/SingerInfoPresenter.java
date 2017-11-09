package com.example.cootek.newfastframe.mvp.singerinfo;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.cootek.newfastframe.MusicInfoProvider;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/9/3.
 */

public class SingerInfoPresenter extends BasePresenter<IView<List<MusicPlayBean>>, SingerInfoModel> {
    public SingerInfoPresenter(IView<List<MusicPlayBean>> iView, SingerInfoModel baseModel) {
        super(iView, baseModel);
    }

    public void getLocalSingerMusic(final String tingId) {
        MusicInfoProvider.getMusicForSinger(tingId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MusicPlayBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull List<MusicPlayBean> musicPlayBeen) {
                        iView.updateData(musicPlayBeen);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getLocalSingerMusic(tingId);
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
