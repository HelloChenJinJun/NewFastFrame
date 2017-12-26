package com.example.news.util;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.news.MainRepositoryManager;
import com.example.news.api.SystemInfoApi;
import com.example.news.bean.SystemUserBean;
import com.example.news.event.ReLoginEvent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/26     17:10
 * QQ:         1981367757
 */

public class ReLoginUtil {
    private String account;
    private BaseModel<MainRepositoryManager> baseModel;
    private CompositeDisposable compositeDisposable;
    private String pw;


    public ReLoginUtil(BaseModel<MainRepositoryManager> baseModel, CompositeDisposable compositeDisposabl) {
        this.baseModel = baseModel;
        this.compositeDisposable = compositeDisposabl;
        account=BaseApplication.getAppComponent().getSharedPreferences().getString(ConstantUtil
        .ACCOUNT,null);
        pw=BaseApplication.getAppComponent().getSharedPreferences().getString(ConstantUtil.PASSWORD,null);
    }

    public void login() {
//        if (BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_COOKIE, null) == null) {
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class).getCookie(NewsUtil.SYSTEM_INFO_INDEX_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Document document = null;
                        try {
                            String temp = responseBody.string().replace("&nbsp;", " ");
                            CommonLogger.e(temp);
                            document = Jsoup.parse(temp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Element element = document.getElementById("lt");
                        if (element != null) {
                            String value = element.attr("value");
                            BaseApplication.getAppComponent()
                                    .getSharedPreferences().edit().putString(NewsUtil.SYSTEM_INFO_LOGIN_LT, value).apply();
                            realLogin(account, pw, value);
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

    private void realLogin(final String account, final String pw, final String lt) {
        if (lt == null) {
            return;
        }
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .login(NewsUtil.getRealLoginUrl(BaseApplication.getAppComponent().getSharedPreferences()
                        .getString(NewsUtil.SYSTEM_INFO_COOKIE, null)), NewsUtil.getSystemInfoRequestBody(account, pw, lt))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        if (BaseApplication.getAppComponent().getSharedPreferences()
                                .getString(NewsUtil.SYSTEM_INFO_GET_TICKET, null) != null) {
                            getTp_upCookie(BaseApplication.getAppComponent()
                                    .getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_GET_TICKET, null));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                if (BaseApplication.getAppComponent().getSharedPreferences()
                                        .getString(NewsUtil.SYSTEM_INFO_GET_TICKET, null) != null) {
                                    getTp_upCookie(BaseApplication.getAppComponent()
                                            .getSharedPreferences().getString(NewsUtil.SYSTEM_INFO_GET_TICKET, null));
                                }
                                return;
                            }
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getTp_upCookie(final String ticketUrl) {
        BaseApplication.getAppComponent().getSharedPreferences()
                .edit().putString(NewsUtil.SYSTEM_INFO_TP_UP, null).apply();
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getCookie(ticketUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
//                        getUserInfo();
                        ReLoginEvent reLoginEvent = new ReLoginEvent("login");
                        reLoginEvent.setSuccess(true);
                        RxBusManager.getInstance()
                                .post(reLoginEvent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
//                                getUserInfo();
                                ReLoginEvent reLoginEvent = new ReLoginEvent("login");
                                reLoginEvent.setSuccess(true);
                                RxBusManager.getInstance()
                                        .post(reLoginEvent);
                                return;
                            }
                        }

                    }


                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void getUserInfo() {
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getUserInfo(NewsUtil.SYSTEM_USER_INFO_URL, NewsUtil
                        .getSystemUserRequestBody(account)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SystemUserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SystemUserBean systemUserBean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
