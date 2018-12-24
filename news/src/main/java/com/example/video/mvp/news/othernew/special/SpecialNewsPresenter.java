package com.example.video.mvp.news.othernew.special;

import android.text.TextUtils;

import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.video.api.OtherNewsApi;
import com.example.video.bean.PhotoSetBean;
import com.example.video.bean.RawSpecialNewsBean;
import com.example.video.bean.SpecialNewsBean;

import java.util.ArrayList;
import java.util.List;

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

public class SpecialNewsPresenter extends BasePresenter<ISpecialNewsView<List<SpecialNewsBean>>, DefaultModel> {
    public SpecialNewsPresenter(ISpecialNewsView<List<SpecialNewsBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getSpecialNewsData(final String specialId) {
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                .getSpecialNewsData(specialId)
                .subscribeOn(Schedulers.io())
                .map(stringRawSpecialNewsBeanMap -> stringRawSpecialNewsBeanMap.get(specialId))
                .doOnNext(new Consumer<RawSpecialNewsBean>() {
                    @Override
                    public void accept(RawSpecialNewsBean rawSpecialNewsBean) throws Exception {
                        addDispose(Observable.just(rawSpecialNewsBean.getBanner()).subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {
                                        iView.updateBanner(s);
                                    }
                                }));
                    }
                }).flatMap(rawSpecialNewsBean -> Observable.fromIterable(rawSpecialNewsBean.getTopics())
                .flatMap(topicsEntity -> Observable.fromIterable(topicsEntity.getDocs())
                        .map(SpecialNewsBean::new).startWith(new SpecialNewsBean(topicsEntity.getShortname()))))
                .flatMap((Function<SpecialNewsBean, ObservableSource<SpecialNewsBean>>) specialNewsBean -> {
                    if (specialNewsBean != null && SpecialNewsBean.TYPE_PHOTO_SET == specialNewsBean.getType()) {
                        return baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                                .getPhotoSetData(clipPhotoSetId(specialNewsBean.getBean().getSkipID())).flatMap((Function<PhotoSetBean, ObservableSource<SpecialNewsBean>>) photoSetBean -> {
                                    if (photoSetBean.getPhotos() != null && photoSetBean.getPhotos().size() > 0) {
                                        List<RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity> list = new ArrayList<>();
                                        for (PhotoSetBean.PhotosEntity entity :
                                                photoSetBean.getPhotos()) {
                                            RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity item = new RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity();
                                            item.setImgsrc(entity.getImgurl());
                                            list.add(item);
                                        }
                                        specialNewsBean.getBean().setImgextra(list);
                                    }
                                    return Observable.fromArray(specialNewsBean);
                                });
                    }
                    return Observable.fromArray(specialNewsBean);
                }).toList().observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<SpecialNewsBean>>() {
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
                iView.showError(null, () -> getSpecialNewsData(specialId));
            }
        });

    }

    /**
     * 裁剪图集ID
     *
     * @param photoId
     * @return
     */
    public String clipPhotoSetId(String photoId) {
        if (TextUtils.isEmpty(photoId)) {
            return photoId;
        }
        int i = photoId.indexOf("|");
        if (i >= 4) {
            String result = photoId.replace('|', '/');
            return result.substring(i - 4);
        }
        return null;
    }
}
