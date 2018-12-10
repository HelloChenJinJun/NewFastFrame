package com.example.video.mvp.news.othernew;

import android.text.TextUtils;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.video.api.OtherNewsApi;
import com.example.video.bean.NewInfoBean;
import com.example.video.bean.PhotoSetBean;
import com.example.video.util.NewsUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/24      18:18
 * QQ:             1981367757
 */

public class OtherNewsListPresenter extends BasePresenter<IView<List<NewInfoBean>>, DefaultModel> {
    private int num = 0;

    public OtherNewsListPresenter(IView<List<NewInfoBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getOtherNewsListData(final boolean isShowLoading, final boolean isRefresh, final String typeId) {
        if (iView == null) {
            return;
        }
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num = 0;
        }
        num++;
        baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                .getNewsList(typeId.equals("T1348647909107") ? "headline" : "list", typeId, (num - 1) * 20)
                .compose(iView.bindLife())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(stringListMap -> Observable.fromIterable(stringListMap.get(typeId))).flatMap((Function<NewInfoBean, ObservableSource<NewInfoBean>>) newInfoBean -> {
            if (NewsUtil.PHOTO_SET.equals(newInfoBean.getSkipType()) && (newInfoBean.getImgextra() == null
                    || newInfoBean.getImgextra().size() < 3)) {
                return baseModel.getRepositoryManager().getApi(OtherNewsApi.class)
                        .getPhotoSetData(clipPhotoSetId(newInfoBean.getPhotosetID())).subscribeOn(Schedulers.io()).flatMap((Function<PhotoSetBean, ObservableSource<NewInfoBean>>) photoSetBean -> {
                            if (photoSetBean.getPhotos() != null && photoSetBean.getPhotos().size() > 0) {
                                List<NewInfoBean.ImgextraEntity> list = new ArrayList<>();
                                for (PhotoSetBean.PhotosEntity entity :
                                        photoSetBean.getPhotos()) {
                                    NewInfoBean.ImgextraEntity item = new NewInfoBean.ImgextraEntity();
                                    item.setImgsrc(entity.getImgurl());
                                    list.add(item);
                                }
                                newInfoBean.setImgextra(list);
                            }
                            return Observable.fromArray(newInfoBean);
                        }).observeOn(AndroidSchedulers.mainThread());
            } else {
                return Observable.fromArray(newInfoBean);
            }
        })
                .toList().subscribe(new SingleObserver<List<NewInfoBean>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onSuccess(@NonNull List<NewInfoBean> newInfoBeen) {
                iView.updateData(newInfoBeen);
                if (newInfoBeen == null || newInfoBeen.size() == 0) {
                    num--;
                }
                iView.hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                CommonLogger.e(e);
                if (!isRefresh) {
                    num--;
                }
                iView.showError(null, new EmptyLayout.OnRetryListener() {
                    @Override
                    public void onRetry() {
                        getOtherNewsListData(isShowLoading, isRefresh, typeId);
                    }
                });
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
        //        KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(ph);
        if (TextUtils.isEmpty(photoId)) {
            return "";
        }
        int i = photoId.indexOf("|");
        if (i >= 4) {
            String result = photoId.replace('|', '/');
            String str = result.substring(i - 4);
            CommonLogger.e("photoId:" + str);
            return result.substring(i - 4);
        } else {
            CommonLogger.e("空空空空空空");
        }
        return null;
    }
}
