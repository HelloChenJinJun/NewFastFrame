package com.example.cootek.newfastframe.mvp.bottom;

import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.bean.music.MusicSortBean;
import com.example.commonlibrary.bean.music.MusicSortBeanDao;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.FileUtil;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by COOTEK on 2017/8/14.
 */

public class BottomPresenter extends RxBasePresenter<IView<Object>, DefaultModel> {


    public BottomPresenter(IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void playOrPause() {
        MusicManager.getInstance().playOrPause();

    }

    public void next() {
        MusicManager.getInstance().next();
    }

    public void previous() {
        MusicManager.getInstance().previous();
    }


    public void play(List<MusicPlayBean> list, int position) {
        MusicManager.getInstance().play(list, position);
    }


    public void setMode(int mode) {
        MusicManager.getInstance().setPlayMode(mode);

    }


    public void getLrcContent(long songId, String lrcUrl) {
        baseModel.getRepositoryManager().getApi(MusicApi.class)
                .getLrcContent(lrcUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            FileUtil.writeToFile(MusicUtil.getLyricPath(songId), responseBody.string());
                            iView.updateData(songId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }

    public void loadData() {
        List<MusicSortBean> musicSortBeanList = getBaseModel()
                .getRepositoryManager()
                .getDaoSession().getMusicSortBeanDao().queryBuilder().limit(10).orderDesc(MusicSortBeanDao.Properties
                        .PlayTime).list();
        if (musicSortBeanList.size() != 0) {

            List<String> urlList = new ArrayList<>(musicSortBeanList.size());
            for (MusicSortBean bean :
                    musicSortBeanList) {
                urlList.add(bean.getUrl());
            }
            List<MusicPlayBean> data = getBaseModel()
                    .getRepositoryManager().getDaoSession().getMusicPlayBeanDao()
                    .queryBuilder().where(MusicPlayBeanDao.Properties
                            .SongUrl.in(urlList)).build().list();
            MusicManager.getInstance().play(data, 0);
        }
    }
}
