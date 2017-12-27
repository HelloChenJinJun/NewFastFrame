package com.example.news.mvp.booklist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.news.api.CugLibraryApi;
import com.example.news.bean.BookInfoBean;
import com.example.news.event.LibraryLoginEvent;
import com.example.news.util.NewsUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/19      11:00
 * QQ:             1981367757
 */

public class BookInfoListPresenter extends RxBasePresenter<IView<Object>, BookInfoListModel> {
    private int num = 0;

    public BookInfoListPresenter(IView<Object> iView, BookInfoListModel baseModel) {
        super(iView, baseModel);
    }


    public void getBorrowBookInfo(final boolean isShowLoading, final boolean isRefresh, final boolean isHistory) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        String url;
        if (isHistory) {
            if (isRefresh) {
                num = 0;
            }
            num++;
            url = NewsUtil.LIBRARY_BORROW_BOOK_HISTORY_LIST_URL + "?page=" + num;
        } else {
            url = NewsUtil.LIBRARY_BORROW_BOOK_LIST_URL;
        }
        baseModel.getRepositoryManager().getApi(CugLibraryApi.class)
                .getBorrowBookInfo(url)
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
                            Document document = Jsoup.parse(responseBody.string());
                            Elements elements = document.getElementsByTag("tbody");
                            if (elements.size() == 0
                                    || (elements.first().children().size() > 0 &&
                                    elements.first().children().get(0).children().size() < 7)) {
                                if (elements == null || elements.size() == 0) {
                                    RxBusManager.getInstance().post(new LibraryLoginEvent("cookie过期"));
                                } else {
                                    iView.updateData(null);
                                }
                                return;
                            }
                            List<BookInfoBean> bookInfoBeanList = null;
                            if (elements != null && elements.first() != null) {
                                bookInfoBeanList = new ArrayList<>();
                                for (int i = 1; i < elements.first().children().size(); i++) {
                                    Element element = elements.first().children().get(i);

                                    BookInfoBean bookInfoBean;
                                    if (element.children() != null) {
                                        bookInfoBean = new BookInfoBean();
                                        if (!isHistory) {
                                            bookInfoBean.setBookName(element.children().get(1).text());
                                            bookInfoBean.setStartTime(element.children().get(2).text());
                                            bookInfoBean.setEndTime(element.children().get(3).text());
                                            bookInfoBean.setEnableNum(element.children().get(4).text());
                                            bookInfoBean.setContentUrl(NewsUtil.getRealBorrowLibraryUrl(element.children().get(1).getElementsByTag("a").attr("href")));
                                            String value = element.children().get(7).getElementsByTag("input").attr("onclick");
                                            String realValue = value.substring(value.indexOf("(") + 1, value.indexOf(")"));
                                            String[] array = realValue.split(",");
                                            bookInfoBean.setNumber(array[0].substring(1, array[0].length() - 1));
                                            bookInfoBean.setCheck(array[1].substring(1, array[1].length() - 1));
                                            if (element.children().get(7).getElementsByTag("input").hasAttr("disabled")) {
                                                bookInfoBean.setEnableBorrow(false);
                                            } else {
                                                bookInfoBean.setEnableBorrow(true);
                                            }
                                        } else {
                                            bookInfoBean.setBookName(element.children().get(2).text() + "/" + element.children().get(3).text());
                                            bookInfoBean.setStartTime(element.children().get(4).text());
                                            bookInfoBean.setEndTime(element.children().get(5).text());
                                            bookInfoBean.setEnableNum("");
                                            bookInfoBean.setContentUrl(NewsUtil.getRealBorrowLibraryUrl(element.children().get(2).getElementsByTag("a").attr("href")));
                                            bookInfoBean.setEnableBorrow(false);
                                        }
                                        bookInfoBeanList.add(bookInfoBean);
                                    }
                                }
                            }
                            iView.updateData(bookInfoBeanList);
                            if ((bookInfoBeanList == null || bookInfoBeanList.size() == 0) && isHistory) {
                                num--;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null,
                                new EmptyLayout.OnRetryListener() {
                                    @Override
                                    public void onRetry() {
                                        getBorrowBookInfo(isShowLoading, isRefresh, isHistory);
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
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(CugLibraryApi.class)
                .getVerifyImage(NewsUtil.LIBRARY_BORROW_VERIFY_URL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
                        if (bitmap != null) {
                            iView.updateData(bitmap);
                        } else {
                            iView.updateData("获取验证码失败,请点击重试");
                            iView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.updateData("获取验证码失败,请点击重试");
                        iView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                            iView.hideLoading();
                    }
                });
    }

    public void borrowBook(String verify, String number, String check) {
//        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(CugLibraryApi
        .class).borrowBook(NewsUtil.getBorrowBookUrl(verify,number,check))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Document document = null;
                        try {
                            document = Jsoup.parse(responseBody.string());
                            Element element=document.getElementsByTag("font").first();
                            if (element != null) {
                                iView.updateData(element.text());
                            }else {
                                iView.updateData("续借失败");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.updateData("续借失败");
                        iView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });




    }
}
