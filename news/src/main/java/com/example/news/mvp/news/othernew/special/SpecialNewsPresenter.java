package com.example.news.mvp.news.othernew.special;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.news.api.OtherNewsApi;
import com.example.news.bean.RawSpecialNewsBean;
import com.example.news.bean.SpecialNewsBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/25      18:45
 * QQ:             1981367757
 */

public class SpecialNewsPresenter extends BasePresenter<ISpecialNewsView<List<SpecialNewsBean>>,SpecialNewsModel>{
    public SpecialNewsPresenter(ISpecialNewsView<List<SpecialNewsBean>> iView, SpecialNewsModel baseModel) {
        super(iView, baseModel);
    }

    public void getSpecialNewsData(final String specialId) {
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                .getSpecialNewsData(specialId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<String,RawSpecialNewsBean>, RawSpecialNewsBean>() {
                    @Override
                    public RawSpecialNewsBean apply(@NonNull Map<String, RawSpecialNewsBean> stringRawSpecialNewsBeanMap) throws Exception {
                        return stringRawSpecialNewsBeanMap.get(specialId) ;
                    }
                })
                .doOnNext(new Consumer<RawSpecialNewsBean>() {
                    @Override
                    public void accept(@NonNull RawSpecialNewsBean rawSpecialNewsBean) throws Exception {
                        iView.updateBanner(rawSpecialNewsBean.getBanner());
                    }
                }).flatMap(new Function<RawSpecialNewsBean, ObservableSource<SpecialNewsBean>>() {
            @Override
            public ObservableSource<SpecialNewsBean> apply(@NonNull RawSpecialNewsBean rawSpecialNewsBean) throws Exception {
                return Observable.fromIterable(rawSpecialNewsBean.getTopics())
                        .flatMap(new Function<RawSpecialNewsBean.TopicsEntity, ObservableSource<SpecialNewsBean>>() {
                            @Override
                            public ObservableSource<SpecialNewsBean> apply(@NonNull RawSpecialNewsBean.TopicsEntity topicsEntity) throws Exception {
                                return Observable.fromIterable(topicsEntity.getDocs())
                                        .map(new Function<RawSpecialNewsBean.TopicsEntity.DocsEntity, SpecialNewsBean>() {
                                            @Override
                                            public SpecialNewsBean apply(@NonNull RawSpecialNewsBean.TopicsEntity.DocsEntity docsEntity) throws Exception {
                                                return new SpecialNewsBean(docsEntity);
                                            }
                                        }).startWith(new SpecialNewsBean(topicsEntity.getShortname()));
                            }
                        });
            }
        }).toList().subscribe(new SingleObserver<List<SpecialNewsBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onSuccess(@NonNull List<SpecialNewsBean> specialNewsBeen) {
                iView.updateData(specialNewsBeen);
                iView.hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                iView.showError(null, new EmptyLayout.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        getSpecialNewsData(specialId);
                    }
                });
            }
        });

    }
}
