package com.example.cootek.newfastframe.mvp.bottom;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.Constant;
import com.example.commonlibrary.utils.FileUtil;
import com.example.cootek.newfastframe.MusicManager;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.util.MusicUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
        MusicManager.getInstance().play(list, position, 0);
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
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> urlList = BaseApplication.getAppComponent().getGson().fromJson(BaseApplication.getAppComponent()
                .getSharedPreferences().getString(Constant.RECENT_SONG_URL_LIST, null), type);
        if (urlList != null && urlList.size() != 0) {
            List<MusicPlayBean> data = getBaseModel()
                    .getRepositoryManager().getDaoSession().getMusicPlayBeanDao()
                    .queryBuilder().where(MusicPlayBeanDao.Properties
                            .SongUrl.in(urlList)).build().list();
            int position = BaseApplication.getAppComponent()
                    .getSharedPreferences().getInt(Constant.MUSIC_POSITION, 0);
            CommonLogger.e("获取的位置:" + position);
            MusicManager.getInstance().play(data, position
                    , BaseApplication.getAppComponent().getSharedPreferences()
                            .getLong(Constant.SEEK, 0L));
        }
    }
}
