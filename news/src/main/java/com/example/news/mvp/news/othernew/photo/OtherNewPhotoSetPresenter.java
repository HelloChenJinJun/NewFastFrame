package com.example.news.mvp.news.othernew.photo;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.OtherNewsApi;
import com.example.news.bean.PhotoSetBean;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      16:43
 * QQ:             1981367757
 */

public class OtherNewPhotoSetPresenter extends BasePresenter<IView<PhotoSetBean>,OtherNewPhotoSetModel>{
    public OtherNewPhotoSetPresenter(IView<PhotoSetBean> iView, OtherNewPhotoSetModel baseModel) {
        super(iView, baseModel);
    }

    public void getOtherNewPhotoSetData(final String photoSetId) {
        baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                .getPhotoSetData(photoSetId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<PhotoSetBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(@NonNull PhotoSetBean photoSetBean) {
                iView.updateData(photoSetBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                iView.showError(null, new EmptyLayout.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        getOtherNewPhotoSetData(photoSetId);
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
