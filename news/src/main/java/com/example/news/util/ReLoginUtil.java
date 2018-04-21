package com.example.news.util;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.rxbus.event.PwChangeEvent;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ConstantUtil;
import com.example.news.MainRepositoryManager;
import com.example.news.NewsApplication;
import com.example.news.api.SystemInfoApi;
import com.example.news.bean.ResetPwResult;
import com.example.news.bean.SystemUserBean;
import com.example.news.event.ReLoginEvent;
import com.example.news.mvp.systeminfo.SystemInfoModel;

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
    private CallBack callBack;
    private SystemUserBean systemUserBean;


    public interface CallBack {
        public void onSuccess(SystemUserBean systemUserBean);

        public void onFailed(String errorMessage);
    }


    public ReLoginUtil() {
        this.baseModel = new SystemInfoModel(NewsApplication
                .getNewsComponent().getRepositoryManager());
        this.compositeDisposable = new CompositeDisposable();
    }

    public void clear() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();//保证activity结束时取消所有正在执行的订阅
            compositeDisposable = null;
        }
    }


    public void resetPw(String old,String news){
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .resetPw(NewsUtil.PW_RESET_URL,NewsUtil.getResetPwRequestBody(old,news))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResetPwResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResetPwResult resetPwResult) {
                        PwChangeEvent pwChangeEvent=new PwChangeEvent();
                        pwChangeEvent.setOld(old);
                        pwChangeEvent.setNews(news);
                        pwChangeEvent.setSuccess(resetPwResult.isSuccess());
                        pwChangeEvent.setErrorMsg(resetPwResult.isSuccess()?null:resetPwResult.getMessage());
                        RxBusManager.getInstance().post(pwChangeEvent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        PwChangeEvent pwChangeEvent=new PwChangeEvent();
                        pwChangeEvent.setSuccess(false);
                        pwChangeEvent.setErrorMsg(e!=null?e.getMessage():"系统修改密码失败");
                        RxBusManager.getInstance().post(pwChangeEvent);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    public void login(final String account, final String pw, final ReLoginUtil.CallBack callBack) {
        this.account = account;
        this.pw = pw;
        this.callBack = callBack;
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
                        } else {
                            callBack.onFailed("lt为空");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailed(e != null ? e.getMessage() : null);
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
                        } else {
                            callBack.onFailed("system_info_get_ticket is null");
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
                                    return;
                                }
                            }
                        }
                        callBack.onFailed(e != null ? e.getMessage() : null);
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
                        getUserInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                getUserInfo();
                                return;
                            }
                        }
                        callBack.onFailed(e != null ? e.getMessage() : null);

                    }


                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void getUserInfo() {
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getUserInfo(NewsUtil.SYSTEM_USER_INFO_URL, NewsUtil
                        .getSystemUserRequestBody(account)).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SystemUserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SystemUserBean systemUserBean) {
                        ReLoginUtil.this.systemUserBean=systemUserBean;
                        getOtherLoginCookie();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailed(e != null ? e.getMessage() : null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getOtherUserInfo() {
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getOtherUserInfo(NewsUtil.getOtherUserInfoUrl(account))
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
                            document = Jsoup.parse(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (document != null) {
                            String major = document.getElementById("col_zyh_id").text().trim();
                            String classNumber = document.getElementById("col_bh_id").text().trim();
//                            临时找两个无效字段
                            systemUserBean.setADMISSIONS_PIC(major);
                            systemUserBean.setGRADUATION_PIC(classNumber);
                        }
                        callBack.onSuccess(systemUserBean);

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onFailed(e != null ? e.getMessage() : null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getOtherLoginCookie() {
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .verifyAccount(NewsUtil.COURSE_VERIFY_ACCOUNT_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getTempJsIdByTicket();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                getTempJsIdByTicket();
                                return;
                            }
                        }
                        callBack.onFailed(e != null ? e.getMessage() : null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getTempJsIdByTicket() {
        String url = BaseApplication.getAppComponent()
                .getSharedPreferences().getString(NewsUtil.COURSE_TICKET_URL, null);

        if (url == null) {
            return;
        }
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getTempJsessionIdByTicket(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getVerifyUrl();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                getVerifyUrl();
                                return;
                            }
                        }
                        callBack.onFailed(e != null ? e.getMessage() : null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getVerifyUrl() {
        String url = BaseApplication.getAppComponent()
                .getSharedPreferences().getString(NewsUtil.COURSE_JSESSION_URL, null);
        if (url == null) {
            return;
        }
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getTempJsessionIdByTicket(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getRealIdByVerifyUrl();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                getRealIdByVerifyUrl();
                                return;
                            }
                        }
                        callBack.onFailed(e != null ? e.getMessage() : null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getRealIdByVerifyUrl() {
        String url = BaseApplication.getAppComponent()
                .getSharedPreferences().getString(NewsUtil.COURSE_REAL_VERIFY_URL, null);
        if (url == null) {
            return;
        }
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getTempJsessionIdByTicket(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getOtherUserInfo();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                getOtherUserInfo();
                                return;
                            }
                        }
                        callBack.onFailed(e != null ? e.getMessage() : null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
