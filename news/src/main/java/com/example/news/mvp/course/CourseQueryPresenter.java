package com.example.news.mvp.course;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.SystemInfoApi;
import com.example.news.bean.CourseQueryBean;
import com.example.news.util.NewsUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/25     19:48
 * QQ:         1981367757
 */

public class CourseQueryPresenter extends BasePresenter<IView<CourseQueryBean>, CourseQueryModel> {
    private String year;
    private int examNum;


    public CourseQueryPresenter(IView<CourseQueryBean> iView, CourseQueryModel baseModel) {
        super(iView, baseModel);
    }


    public void getQueryCourseData(final String year, final int examNum) {
        this.year = year;
        this.examNum = examNum;
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .verifyAccount(NewsUtil.COURSE_VERIFY_ACCOUNT_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
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
                        iView.showError("登录失败", new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getQueryCourseData(year, examNum);
                            }
                        });
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
                        addDispose(d);
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
                        iView.showError("登录失败", new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getTempJsIdByTicket();
                            }
                        });
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
                        addDispose(d);
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
                        iView.showError("登录失败", new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getVerifyUrl();
                            }
                        });
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
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        getRealCourseData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                getRealCourseData();
                                return;
                            }
                        }
                        iView.showError("登录失败", new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getRealIdByVerifyUrl();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getRealCourseData() {
        String id = BaseApplication.getAppComponent()
                .getSharedPreferences().getString(NewsUtil.COURSE_JSESSION_ID, null);
        if (id == null) {
            return;
        }
        baseModel.getRepositoryManager().getApi(SystemInfoApi.class)
                .getCourseQueryData(NewsUtil.COURSE_QUERY_URL, NewsUtil.getCourseQueryRequestBody(year, examNum))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CourseQueryBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(CourseQueryBean courseQueryBean) {
                        iView.updateData(courseQueryBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError("获取失败", new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getRealCourseData();
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
