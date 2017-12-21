package com.example.news.mvp.librarylogin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.ToastUtils;
import com.example.news.api.CugLibraryApi;
import com.example.news.util.NewsUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/18      15:22
 * QQ:             1981367757
 */

public class LibraryLoginPresenter extends BasePresenter<IView<Object>, LibraryLoginModel> {
    public LibraryLoginPresenter(IView<Object> iView, LibraryLoginModel baseModel) {
        super(iView, baseModel);
    }

    public void login(final String account, final String password, final String verify) {
        String cookie=BaseApplication.getAppComponent().getSharedPreferences().getString(NewsUtil.LIBRARY_COOKIE,null);
        if (cookie == null) {
            ToastUtils.showShortToast("登录失败");
            return;
        }
        baseModel.getRepositoryManager().getApi(CugLibraryApi.class)
                .verify(NewsUtil.getVerifyUrl(account, password, verify))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }
                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            String string=responseBody.string();
                            Document document=Jsoup.parse(string);
                            if (document.getElementsByTag("form") != null&&document.getElementsByTag("form").size()>0) {
                               Elements elements=document.getElementsByTag("tbody");
                                if (elements != null && elements.size() > 0
                                        &&elements.get(0)
                                        .children()!=null&&
                                        elements.get(0)
                                        .children().size()>0) {
                                    iView.updateData(elements.get(0).children().get(elements.get(0)
                                    .children().size()-1).text());
                                }
                            }else {
                                iView.updateData(null);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError( Throwable e) {

                        if (e != null && e instanceof HttpException) {
                            if (((HttpException) e).code() == 302) {
                                iView.updateData(null);
                                return;
                            }
                        }
                        iView.showError("登录失败", new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                login(account, password, verify);
                            }
                        });
                    }
                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }



    public void getCookie() {
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(CugLibraryApi.class)
                .getCookie(NewsUtil.LIBRARY_LOGIN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody ResponseBody) {
                        if (BaseApplication.getAppComponent().getSharedPreferences()
                                .getString(NewsUtil.LIBRARY_COOKIE, null) != null) {
                            iView.updateData(BaseApplication.getAppComponent().getSharedPreferences()
                                    .getString(NewsUtil.LIBRARY_COOKIE, null));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getCookie();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }

    public void getVerifyImage() {
        baseModel.getRepositoryManager().getApi(CugLibraryApi.class)
                .getVerifyImage(NewsUtil.LIBRARY_BERIFY_IMAGE_URL)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                addDispose(d);
            }

            @Override
            public void onNext(@NonNull ResponseBody responseBody) {
               Bitmap bitmap=BitmapFactory.decodeStream(responseBody.byteStream());
                if (bitmap != null) {
                    iView.updateData(bitmap);
                }else {
                    onError(null);
                }
            }

            @Override
            public void onError( Throwable e) {
                if (e == null) {
                    iView.showError("验证码获取失败",null);
                }else {
                    iView.showError(null,null);
                }
            }

            @Override
            public void onComplete() {
                iView.hideLoading();
            }
        });
    }
}
