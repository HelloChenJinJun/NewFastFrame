package com.example.chat.mvp.WinXinInfoTask;

import com.example.chat.api.TxApi;
import com.example.chat.base.Constant;
import com.example.chat.bean.TxResponse;
import com.example.chat.bean.WinXinBean;
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
 * 创建时间:    2017/1/7      0:14
 * QQ:             1981367757
 */

public class WinXinInfoPresenter extends WinXinInfoContacts.Presenter {
        private Gson mGson = new Gson();
        private int mPage;

        public WinXinInfoPresenter(WinXinInfoContacts.View iView, WinXinInfoContacts.Model baseModel) {
                super(iView, baseModel);
        }

        @Override
        public void getWinXinInfo(int page, final boolean showLoading) {
                this.mPage=page;
                if (page == 1) {
                    if (showLoading) {
                        iView.showLoading("正在加载..........");
                    }
                    baseModel.clearAllData();
                }

                baseModel.getRepositoryManager().getApi(TxApi.class)
                        .getWinXinInfo(ChatUtil.getWinXinUrl(page))
                        .compose(iView.<TxResponse>bindLife())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TxResponse>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                        addDispose(d);
                                }

                                @Override
                                public void onNext(@NonNull TxResponse txResponse) {
                                        LogUtil.e("接收到的数据" + mGson.toJson(txResponse));
                                        if (txResponse.getCode() == 200) {
                                                iView.updateData(txResponse.getNewslist());
//                                                这个缓存用来存储读取状态
                                                for (WinXinBean bean :
                                                        txResponse.getNewslist()) {
                                                        baseModel.saveCacheWeiXinInfo(bean.getUrl(), mGson.toJson(bean));
                                                }
                                                baseModel.saveCacheWeiXinInfo(Constant.WEI_XIN_KEY + mPage, mGson.toJson(txResponse));
                                        } else {
                                                LogUtil.e("服务器解析数据出错" + txResponse.getCode());
                                        }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                        LogUtil.e(e.getMessage());
                                        iView.hideLoading();
                                        iView.showError(e.getMessage(), new EmptyLayout.OnRetryListener() {
                                                @Override
                                                public void onRetry() {
                                                        getWinXinInfo(mPage,showLoading);
                                                }
                                        });
                                        iView.updateData(baseModel.getCacheWeiXinInfo(Constant.WEI_XIN_KEY + mPage));
                                }

                                @Override
                                public void onComplete() {
                                                        iView.hideLoading();
                                }
                        });
        }
}
