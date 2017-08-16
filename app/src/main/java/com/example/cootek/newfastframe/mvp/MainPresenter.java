package com.example.cootek.newfastframe.mvp;

import com.example.commonlibrary.baseadapter.EmptyLayout;
import com.example.commonlibrary.mvp.BaseModel;
import com.example.commonlibrary.mvp.BasePresenter;
import com.example.commonlibrary.mvp.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.DaoSession;
import com.example.cootek.newfastframe.MainApplication;
import com.example.cootek.newfastframe.Music;
import com.example.cootek.newfastframe.MusicInfoProvider;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.MusicPlayInfo;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/8/11.
 */

public class MainPresenter extends BasePresenter<IView, MainModel> {
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
        List<Music> list = ((DaoSession) baseModel.getRepositoryManager().getDaoSession())
                .getMusicDao().queryBuilder().offset((num - 1) * 10).limit(10).list();
        if (list.size() == 0) {
            MusicInfoProvider.searchMusic(MainApplication.getInstance(), "z")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Music>>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                            addDispose(d);
                        }

                        @Override
                        public void onNext(@NonNull List<Music> musics) {
                            iView.updateData(musics);
                            if (musics != null) {
                                ((DaoSession) baseModel.getRepositoryManager().getDaoSession()).getMusicDao().insertOrReplaceInTx(musics);
                            } else {
                                CommonLogger.e("数据为空?");
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
                                    getAllMusic(isRefresh,isShowLoading);
                                }
                            });
                            num--;
                        }

                        @Override
                        public void onComplete() {
                            iView.hideLoading();
                        }
                    });
        } else {
            CommonLogger.e("数据为空?");
            iView.updateData(list);
            iView.hideLoading();
        }
    }
}
