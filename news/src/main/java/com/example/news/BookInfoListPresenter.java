package com.example.news;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.news.api.CugLibraryApi;
import com.example.news.bean.BookInfoBean;
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

public class BookInfoListPresenter extends BasePresenter<IView<List<BookInfoBean>>,BookInfoListModel>{
    private int num=0;

    public BookInfoListPresenter(IView<List<BookInfoBean>> iView, BookInfoListModel baseModel) {
        super(iView, baseModel);
    }


    public void getBorrowBookInfo(final boolean isShowLoading, final boolean isRefresh, final boolean isHistory) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        String url;
        if (isHistory) {
            if (isRefresh) {
                num=0;
            }
            num++;
            url=NewsUtil.LIBRARY_BORROW_BOOK_HISTORY_LIST_URL+"?page="+num;
        }else {
            url=NewsUtil.LIBRARY_BORROW_BOOK_LIST_URL;
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
                            List<BookInfoBean> bookInfoBeanList = null;
                            if (elements != null && elements.first() != null) {
                                bookInfoBeanList = new ArrayList<>();
                                for (int i = 1; i < elements.first().children().size(); i++) {
                                    Element element = elements.first().children().get(i);

                                    BookInfoBean bookInfoBean;
                                    if (element.children() != null) {
                                        if (element.children().size() < 7) {
                                            onError(null);
                                            return;
                                        }
                                        bookInfoBean = new BookInfoBean();
                                        bookInfoBean.setBookName(element.children().get(1).text());
                                        bookInfoBean.setStartTime(element.children().get(2).text());
                                        bookInfoBean.setEndTime(element.children().get(3).text());
                                        bookInfoBean.setEnableNum(element.children().get(4).text());
                                        bookInfoBean.setContentUrl(NewsUtil.getRealBorrowLibraryUrl(element.children().get(1).getElementsByTag("a").attr("href")));
                                        String value=element.children().get(7).getElementsByTag("input").attr("onclick");
                                        String realValue=value.substring(value.indexOf("(")+1,value.indexOf(")"));
                                        String[] array=realValue.split(",");
                                        bookInfoBean.setNumber(array[0].substring(1,array[0].length()-1));
                                        bookInfoBean.setCheck(array[1].substring(1,array[1].length()-1));
                                        if (element.children().get(7).getElementsByTag("input").hasAttr("disabled")) {
                                            bookInfoBean.setEnableBorrow(false);
                                        }else {
                                            bookInfoBean.setEnableBorrow(true);
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
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
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


}
