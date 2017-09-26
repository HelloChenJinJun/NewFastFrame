package com.example.news.mvp.news.othernew.detail;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.OtherNewsApi;
import com.example.news.bean.OtherNewsDetailBean;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/26      15:28
 * QQ:             1981367757
 */

public class OtherNewsDetailPresenter extends BasePresenter<IView<OtherNewsDetailBean>,OtherNewsDetailModel> {
    public OtherNewsDetailPresenter(IView<OtherNewsDetailBean> iView, OtherNewsDetailModel baseModel) {
        super(iView, baseModel);
    }

    public void getOtherNewsDetailData(final String postId) {
        baseModel.getRepositoryManager()
                .getApi(OtherNewsApi.class)
                .getNewsDetail(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<String,OtherNewsDetailBean>, OtherNewsDetailBean>() {
                    @Override
                    public OtherNewsDetailBean apply(@NonNull Map<String, OtherNewsDetailBean> stringOtherNewsDetailBeanMap) throws Exception {
                        return stringOtherNewsDetailBeanMap.get(postId);
                    }
                })
                .doOnNext(new Consumer<OtherNewsDetailBean>() {
                    @Override
                    public void accept(@NonNull OtherNewsDetailBean otherNewsDetailBean) throws Exception {
                        if (otherNewsDetailBean.getImg()!=null&&otherNewsDetailBean
                                .getImg().size()>0) {
                            String body = otherNewsDetailBean.getBody();
                            for (OtherNewsDetailBean.ImgEntity imgEntity : otherNewsDetailBean.getImg()) {
                                String ref = imgEntity.getRef();
                                String src = imgEntity.getSrc();
                                String img = "<img src=\"http\" />".replace("http", src);
                                body = body.replaceAll(ref, img);
                            }
                            otherNewsDetailBean.setBody(body);
                        }
                    }
                }).subscribe(new Observer<OtherNewsDetailBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(@NonNull OtherNewsDetailBean otherNewsDetailBean) {
                    iView.updateData(otherNewsDetailBean);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getOtherNewsDetailData(postId);
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
