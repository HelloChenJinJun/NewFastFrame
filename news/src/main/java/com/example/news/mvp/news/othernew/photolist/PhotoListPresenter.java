package com.example.news.mvp.news.othernew.photolist;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.OtherNewsApi;
import com.example.news.bean.PictureBean;
import com.example.news.util.NewsUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/29      17:45
 * QQ:             1981367757
 */

public class PhotoListPresenter extends BasePresenter<IView<PictureBean>,PhotoListModel>{
    private int num=0;
    public PhotoListPresenter(IView<PictureBean> iView, PhotoListModel baseModel) {
        super(iView, baseModel);
    }

    public void getPhotoListData(final boolean isShowLoading, final boolean isRefresh) {
        if (iView == null) {
            return;
        }
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num=0;
        }
        num++;
        baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                .getPhotoListData(NewsUtil.getPhotoListUrl(10,num))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<PictureBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(@NonNull PictureBean pictureBean) {
                iView.updateData(pictureBean);
                if (pictureBean.getResults() == null || pictureBean.getResults().size() == 0) {
                    num--;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (!isRefresh) {
                    num--;
                }
                iView.showError(null, new EmptyLayout.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        getPhotoListData(isShowLoading, isRefresh);
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
