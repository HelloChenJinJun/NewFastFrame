package com.example.news.mvp.searchlibrary;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.news.api.CugLibraryApi;
import com.example.news.api.CugNewsApi;
import com.example.news.bean.SearchLibraryBean;
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
 * 创建时间:    2017/9/17      18:04
 * QQ:             1981367757
 */

public class LibraryPresenter extends BasePresenter<IView<List<SearchLibraryBean>>, LibraryModel> {
    private int num;

    public LibraryPresenter(IView<List<SearchLibraryBean>> iView, LibraryModel baseModel) {
        super(iView, baseModel);
    }

    public void searchBook(final boolean isShowLoading, final boolean isRefresh, final String text) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num = 0;
        }
        num++;
        baseModel.getRepositoryManager().getApi(CugNewsApi.class).searchBook(NewsUtil.getSearchLibraryUrl(text, num, 20))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            Document document = Jsoup.parse(responseBody.string());
                            Element element = document.getElementById("search_book_list");
                            List<SearchLibraryBean> searchLibraryBeanList = null;
                            if (element.children() != null) {
                                searchLibraryBeanList = new ArrayList<>();
                                for (Element item :
                                        element.children()) {
                                    SearchLibraryBean searchLibraryBean = new SearchLibraryBean();
                                    String url = item.getElementsByTag("a").attr("href");
                                    String bookName = item.getElementsByTag("a").text();
                                    searchLibraryBean.setBookName(bookName.substring(0, bookName.lastIndexOf("馆藏")));
                                    String from = item.getElementsByTag("p").text();
                                    searchLibraryBean.setContentUrl(NewsUtil.getRealSearchLibraryUrl(url));
                                    String[] strings = from.split(" ");
                                    searchLibraryBean.setTotalNum(strings[0]);
                                    searchLibraryBean.setEnableNum(strings[1]);
                                    StringBuilder author = new StringBuilder();
                                    for (int i = 2; i < strings.length - 3; i++) {
                                        author.append(strings[i]);
                                    }
                                    searchLibraryBean.setAuthor(author.toString());
                                    searchLibraryBean.setFrom(strings[strings.length - 3]);
                                    searchLibraryBeanList.add(searchLibraryBean);
                                }
                            }
                            if (searchLibraryBeanList != null && searchLibraryBeanList.size() > 0) {
                                iView.updateData(searchLibraryBeanList);
                            } else {
                                iView.updateData(null);
                                CommonLogger.e("数据空");
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
                                searchBook(isShowLoading, isRefresh, text);
                            }
                        });
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }


    public void searchNewBook(final boolean isShowLoading, final boolean isRefresh, final String time, final String type, final String place
            , final String className) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num = 0;
        }
        num++;
        baseModel.getRepositoryManager().getApi(CugLibraryApi.class)
                .getNewBook(NewsUtil.getSearchNewBookUrl(time, type, place, className, num))
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
                            Document document=Jsoup.parse(responseBody.string());
                            Elements elements=document.select(".list_books");
                            List<SearchLibraryBean>  list=new ArrayList<>();
                            for (Element item :
                                    elements) {
                                SearchLibraryBean bean=new SearchLibraryBean();
                                bean.setContentUrl(NewsUtil.getRealSearchLibraryUrl(item.getElementsByTag("a").attr("href")));
                                bean.setBookName(item.getElementsByTag("a").text());
                                String[] result=item.children().get(1).text().split(" ");
                                if (result.length > 0) {
                                    bean.setEnableNum(result[2]);
                                    bean.setAuthor(result[result.length-2]);
                                    bean.setFrom(result[result.length-1]);
                                }
                                list.add(bean);
                            }

                            iView.updateData(list);
                            if (list.size() <=0) {
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
                                searchNewBook(isShowLoading, isRefresh, time, type, place, className);
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
