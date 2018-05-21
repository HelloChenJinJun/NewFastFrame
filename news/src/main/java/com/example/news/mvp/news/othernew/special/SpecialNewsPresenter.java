package com.example.news.mvp.news.othernew.special;

import android.text.TextUtils;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.news.api.OtherNewsApi;
import com.example.news.bean.NewInfoBean;
import com.example.news.bean.PhotoSetBean;
import com.example.news.bean.RawSpecialNewsBean;
import com.example.news.bean.SpecialNewsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
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
                .map(stringRawSpecialNewsBeanMap -> stringRawSpecialNewsBeanMap.get(specialId))
                .doOnNext(rawSpecialNewsBean -> iView.updateBanner(rawSpecialNewsBean.getBanner())).flatMap(rawSpecialNewsBean -> Observable.fromIterable(rawSpecialNewsBean.getTopics())
                        .flatMap(topicsEntity -> Observable.fromIterable(topicsEntity.getDocs())
                                .map(SpecialNewsBean::new).startWith(new SpecialNewsBean(topicsEntity.getShortname())))).toList().subscribe(new SingleObserver<List<SpecialNewsBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onSuccess(@NonNull List<SpecialNewsBean> specialNewsBeen) {
                iView.updateData(specialNewsBeen);
                if (specialNewsBeen != null && specialNewsBeen.size() > 0) {
                    for (SpecialNewsBean bean :
                            specialNewsBeen) {
                        if (bean.getType() == SpecialNewsBean.TYPE_PHOTO_SET) {
                            getExtraImage(bean);
                        }
                    }
                }
                iView.hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                iView.showError(null, () -> getSpecialNewsData(specialId));
            }
        });

    }

    private void getExtraImage(SpecialNewsBean bean) {
        baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                .getPhotoSetData(clipPhotoSetId(bean.getBean().getSkipID()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PhotoSetBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(PhotoSetBean photoSetBean) {
                        if (photoSetBean.getPhotos() != null && photoSetBean.getPhotos().size() > 0) {
                            List<RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity> list=new ArrayList<>();
                            for (PhotoSetBean.PhotosEntity entity :
                                    photoSetBean.getPhotos()) {
                                RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity item = new RawSpecialNewsBean.TopicsEntity.DocsEntity.ImgextraEntity();
                                item.setImgsrc(entity.getImgurl());
                                list.add(item);
                            }
                            bean.getBean().setImgextra(list);
                            List<SpecialNewsBean> result=new ArrayList<>();
                            result.add(bean);
                            iView.updateData(result);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    /**
     * 裁剪图集ID
     *
     * @param photoId
     * @return
     */
    public  String clipPhotoSetId(String photoId) {
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
