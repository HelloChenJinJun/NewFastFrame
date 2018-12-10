package com.example.cootek.newfastframe.mvp.lrc;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.FileUtil;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/7     18:05
 */
public class LrcListPresenter extends RxBasePresenter<IView<Object>, DefaultModel> {

    public LrcListPresenter(IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
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
}
