package com.example.chat.mvp.HappyContentInfoTask;

import com.example.chat.api.HappyApi;
import com.example.chat.base.Constant;
import com.example.chat.bean.HappyContentResponse;
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
 * 创建时间:    2017/1/8      20:28
 * QQ:             1981367757
 */
public class HappyContentPresenter extends HappyContentContacts.Presenter {
        private Gson mGson = new Gson();
        private int mPage;

        public HappyContentPresenter(HappyContentContacts.View iView, HappyContentContacts.Model baseModel) {
                super(iView, baseModel);
        }

        @Override
        public void getHappyContentInfo(int page, final boolean showLoading) {
                mPage = page;
                if (mPage == 1) {
                        baseModel.clearAllCacheHappyContentData();
                    if (showLoading) {
                        iView.showLoading("正在加载数据，请稍候..........");
                    }
                }
                if (!AppUtil.isNetworkAvailable(BaseApplication.getInstance())) {
                        iView.hideLoading();
                        iView.showError("网络连接失败", new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                        getHappyContentInfo(mPage,showLoading);
                                }
                        });
                        return;
                }
                baseModel.getRepositoryManager().getApi(HappyApi.class).getHappyContentInfo(ChatUtil
                .getHappyContentUrl(page,20))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HappyContentResponse>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                        addDispose(d);
                                }

                                @Override
                                public void onNext(@NonNull HappyContentResponse happyContentResponse) {
                                        LogUtil.e("接收到的笑话数据" + mGson.toJson(happyContentResponse));
                                        if (happyContentResponse.getError_code() == 0) {
                                                iView.updateData(happyContentResponse.getResult().getData());
                                                baseModel.saveHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage, mGson.toJson(happyContentResponse));
                                        } else {
                                                LogUtil.e("服务器出错拉" + happyContentResponse.getReason() + happyContentResponse.getError_code());
                                                iView.updateData(baseModel.getHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage));
                                        }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                        iView.hideLoading();
                                        if (mPage == 1 && baseModel.getHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage) == null) {
                                                iView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                        @Override
                                                        public void onRetry() {
                                                                getHappyContentInfo(mPage,showLoading);
                                                        }
                                                });
                                        }
                                        iView.updateData(baseModel.getHappyContentInfo(Constant.HAPPY_CONTENT_KEY + mPage));
                                }

                                @Override
                                public void onComplete() {
                                        iView.hideLoading();
                                }
                        });
        }
}
