package com.example.chat.mvp.PictureInfoTask;

import com.example.chat.api.PictureApi;
import com.example.chat.base.Constant;
import com.example.chat.bean.PictureBean;
import com.example.chat.bean.PictureResponse;
import com.example.chat.util.ChatUtil;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.utils.AppUtil;
import com.google.gson.Gson;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:07
 * QQ:             1981367757
 */
public class PicturePresenter extends PictureContacts.Presenter {
        private Gson mGson = new Gson();

        private int mPage;

        public PicturePresenter(PictureContacts.View iView, PictureContacts.Model baseModel) {
                super(iView, baseModel);
        }


        @Override
        public void getPictureInfo(int page, final boolean showLoading) {
                mPage = page;
                if (mPage == 1) {
                        baseModel.clearAllCacheData();
                        if (showLoading) {
                                iView.showLoading("正在加载数据，请稍候..........");
                        }
                }
                LogUtil.e("加载的页数" + page);
                if (!AppUtil.isNetworkAvailable(BaseApplication.getInstance())) {
                        iView.hideLoading();
                        iView.showError("网络连接失败", new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                        getPictureInfo(mPage,showLoading);
                                }
                        });
                        return;
                }

                baseModel.getRepositoryManager().getApi(PictureApi.class)
                        .getPictureInfo(ChatUtil.getPictureUrl(page))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<PictureResponse>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                        addDispose(d);
                                }

                                @Override
                                public void onNext(@NonNull PictureResponse pictureResponse) {
                                        LogUtil.e("接收到的图片数据" + mGson.toJson(pictureResponse));
                                        if (!pictureResponse.isError()) {
                                                iView.onUpdatePictureInfo(pictureResponse.getResults());
                                                for (PictureBean bean :
                                                        pictureResponse.getResults()) {
                                                        baseModel.savePictureInfo(bean.getUrl(), mGson.toJson(bean));
                                                }
                                                baseModel.savePictureInfo(Constant.PICTURE_KEY + mPage, mGson.toJson(pictureResponse));
                                        } else {
                                                LogUtil.e("服务器出错拉");
                                        }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                        iView.hideLoading();
//                                        下拉加载设置重试,下拉加载不设置
                                        if (mPage==1&&baseModel.getPictureInfo(Constant.PICTURE_KEY+mPage)==null) {
                                                iView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                        @Override
                                                        public void onRetry() {
                                                                getPictureInfo(mPage,showLoading);
                                                        }
                                                });
                                                iView.showError(e.getMessage(),null);
                                        }
                                        iView.onUpdatePictureInfo(baseModel.getPictureInfo(Constant.PICTURE_KEY + mPage));
                                }

                                @Override
                                public void onComplete() {
                                        iView.hideLoading();
                                }
                        });
        }
}
