package com.example.live.mvp.main;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.live.api.LiveApi;
import com.example.live.bean.CategoryLiveBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/14      16:56
 * QQ:             1981367757
 */

public class MainPresenter extends BasePresenter<IView<List<CategoryLiveBean>>,MainModel> {


    public MainPresenter(IView<List<CategoryLiveBean>> iView, MainModel baseModel) {
        super(iView, baseModel);
    }


    public void getAllCategories(){
        baseModel.getRepositoryManager().getApi(LiveApi.class)
                .getAllCategories().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CategoryLiveBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull List<CategoryLiveBean> categoryLiveBeen) {
                        if (categoryLiveBeen!=null) {
                            iView.updateData(categoryLiveBeen);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                            iView.showError(null, new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                    getAllCategories();
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
