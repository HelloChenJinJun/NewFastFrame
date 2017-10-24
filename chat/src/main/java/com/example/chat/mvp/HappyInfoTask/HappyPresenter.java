package com.example.chat.mvp.HappyInfoTask;

import com.example.chat.api.HappyApi;
import com.example.chat.base.Constant;
import com.example.chat.bean.HappyBean;
import com.example.chat.bean.HappyResponse;
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
 * 创建时间:    2017/1/7      19:12
 * QQ:             1981367757
 */

public class HappyPresenter extends HappyContacts.Presenter {

        private Gson mGson = new Gson();
        private int mPage;

    public HappyPresenter(HappyContacts.View iView, HappyContacts.Model baseModel) {
        super(iView, baseModel);
    }

    @Override
        public void getHappyInfo(int page) {
                mPage = page;
                if (mPage == 1) {
                        baseModel.clearAllCacheHappyData();
                        iView.showLoading("正在加载数据，请稍候..........");

                }
                LogUtil.e("加载的页数" + mPage);
                if (!AppUtil.isNetworkAvailable(BaseApplication.getInstance())) {
                      iView.hideLoading();
                        iView.showError("网络连接失败", new EmptyLayout.OnRetryListener() {
                                @Override
                                public void onRetry() {
                                        getHappyInfo(mPage);
                                }
                        });
                        return;
                }
                baseModel.getRepositoryManager().getApi(HappyApi.class).
                        getHappyInfo(ChatUtil.getHappyUrl(page,20))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<HappyResponse>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                addDispose(d);
                            }

                            @Override
                            public void onNext(@NonNull HappyResponse happyResponse) {
                                LogUtil.e("接收到的笑话数据" + mGson.toJson(happyResponse));
                                        if (happyResponse.getError_code() == 0) {
                                                iView.onUpdateHappyInfo(happyResponse.getResult().getData());
                                                for (HappyBean happyBean :
                                                        happyResponse.getResult().getData()) {
                                                        baseModel.saveHappyInfo(happyBean.getUrl(), mGson.toJson(happyBean));
                                                }
                                                baseModel.saveHappyInfo(Constant.HAPPY_KEY + mPage, mGson.toJson(happyResponse));
                                        } else {
                                                LogUtil.e("服务器出错拉" + happyResponse.getReason() + happyResponse.getError_code());
                                        }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                iView.hideLoading();
                                if (mPage == 1 && baseModel.getHappyInfo(Constant.HAPPY_KEY + mPage) == null) {
                                                iView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                        @Override
                                                        public void onRetry() {
                                                                getHappyInfo(mPage);
                                                        }
                                                });
                                        } else {
                                                iView.showError(e.getMessage(), null);
                                        }
                                        iView.onUpdateHappyInfo(baseModel.getHappyInfo(Constant.HAPPY_KEY + mPage));
                                }

                            @Override
                            public void onComplete() {
                                            iView.hideLoading();
                            }
                        });
        }
}
