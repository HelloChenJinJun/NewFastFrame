package com.example.news.mvp.systeminfo;


import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.news.api.SystemInfoApi;
import com.example.news.bean.CardLoginBean;
import com.example.news.util.NewsUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/17     14:46
 * QQ:         1981367757
 */

public class SystemInfoLoginPresenter extends BasePresenter<IView<Object>,SystemInfoModel> {
    public SystemInfoLoginPresenter(IView iView, SystemInfoModel baseModel) {
        super(iView, baseModel);
    }



    public void login(final String account, final String pw){
//        if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_COOKIE, null) == null) {
            baseModel.getRepositoryManager().getApi(SystemInfoApi.class).getCookie(NewsUtil.SYSTEM_INFO_INDEX_URL)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDispose(d);
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                           Document document=null;
                            try {
                                String temp = responseBody.string().replace("&nbsp;", " ");
                                CommonLogger.e(temp);
                                document = Jsoup.parse(temp);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                          Element element=document.getElementById("lt");
                            if (element != null) {
                                String value=element.attr("value");
//                                iView.updateData(BaseApplication.getAppComponent().getSharedPreferences()
//                                        .getString(NewsUtil.SYSTEM_INFO_COOKIE,null));
                                BaseApplication.getAppComponent()
                                        .getSharedPreferences().edit().putString(NewsUtil.SYSTEM_INFO_LOGIN_LT,value).apply();
                                realLogin(account, pw,value);
                            }
                        }

                        @Override
                        public void onError(Throwable e){
                                iView.showError(null, new EmptyLayout.OnRetryListener() {
                                    @Override
                                    public void onRetry() {
                                        login(account,pw);
                                    }
                                });
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
//        }else {
//            realLogin(account,pw,BaseApplication.getAppComponent()
//            .getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_LOGIN_LT,null));
//        }

    }

    private void realLogin(final String account, final String pw, final String lt) {
        if (lt == null) {
            return;
        }
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .login(NewsUtil.getRealLoginUrl(BaseApplication.getAppComponent().getSharedPreferences()
                .getString(NewsUtil.SYSTEM_INFO_COOKIE,null)),NewsUtil.getSystemInfoRequestBody(account,pw,lt))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (BaseApplication.getAppComponent().getSharedPreferences()
                                .getString(NewsUtil.SYSTEM_INFO_GET_TICKET, null) != null) {
                            getTp_upCookie(BaseApplication.getAppComponent()
                            .getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_GET_TICKET,null));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                if (BaseApplication.getAppComponent().getSharedPreferences()
                                        .getString(NewsUtil.SYSTEM_INFO_GET_TICKET, null) != null) {
                                    getTp_upCookie(BaseApplication.getAppComponent()
                                            .getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_GET_TICKET,null));
                                }
                                return;
                            }
                        }
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                realLogin(account, pw, lt);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getTp_upCookie(final String ticketUrl) {
        BaseApplication.getAppComponent().getSharedPreferences()
                .edit().putString(NewsUtil.SYSTEM_INFO_TP_UP,null).apply();
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getCookie(ticketUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                            iView.updateData(null);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                iView.updateData(null);
                                return;
                            }
                        }
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getTp_upCookie(ticketUrl);
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
