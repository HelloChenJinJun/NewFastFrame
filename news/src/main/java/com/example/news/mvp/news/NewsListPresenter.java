package com.example.news.mvp.news;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.news.api.CugNewsApi;
import com.example.news.bean.NewListBean;
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

import static com.example.news.util.NewsUtil.getHref;

/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:50
 * QQ:             1981367757
 */

public class NewsListPresenter extends BasePresenter<IView<NewListBean>, NewsListModel> {
    private int num = 0;
    private Integer totalPage=-1;

    public NewsListPresenter(IView<NewListBean> iView, NewsListModel baseModel) {
        super(iView, baseModel);
    }


    public void getCugNewsData(final boolean isShowLoading, final boolean isRefresh, final String url) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num = 0;
            totalPage=-1;
        }
        num++;
        if (totalPage != -1 && num > totalPage) {
            iView.updateData(null);
            iView.hideLoading();
            num--;
            return;
        }
        String realUrl;
            if (isRefresh&&NewsUtil.CUG_NEWS.equals(url)) {
                getCugNewsBannerData();
            }
            realUrl = isRefresh? url:NewsUtil.getRealNewsUrl(url,totalPage,num);
        baseModel.getRepositoryManager().getApi(CugNewsApi.class)
                .getCugNewsData(realUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }


                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                            NewListBean newListBean=new NewListBean();
                            try {
                                Document document = Jsoup.parse(responseBody.string());
                                Element element = document.getElementById("fanye177473");
                                String text=element.text();
                            if (text!=null) {
                                String num=text.substring(text.lastIndexOf("/")+1,text.length()-1);
                                totalPage= Integer.valueOf(num);
                            }else {
                                totalPage=1;
                            }
                                Element newElement=document.select(".col-news-list").first();
                                List<NewListBean.NewsItem> newsItemList=null;
                                if (newElement != null) {
                                    newsItemList=new ArrayList<>();
                                    for (Element child :
                                            newElement.children()) {
                                        if (!child.hasClass("list_item")) {
                                            continue;
                                        }
                                        NewListBean.NewsItem newsItem=new NewListBean.NewsItem();
                                        Element title=child.select(".news-title").first();
                                        if (title != null) {
                                            newsItem.setTitle(title.text());
                                            newsItem.setContentUrl(getHref(title.attr("href")));
                                        }
                                        Element date=child.select(".news-date").first();
                                        if (date != null) {
                                            newsItem.setTime(date.text());
                                        }
                                        newsItemList.add(newsItem);
                                    }
                                }
                                newListBean.setNewsItemList(newsItemList);
//                                if (newsItemList == null || newsItemList.size() == 0) {
//                                    iView.showEmptyView();
//                                }else {
                                    iView.updateData(newListBean);
//                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                CommonLogger.e("网络错误"+e.getMessage());
                            }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getCugNewsData(isShowLoading, isRefresh,url);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }



    private void getCugNewsBannerData() {
        baseModel.getRepositoryManager().getApi(CugNewsApi.class)
                .getCugNewsData(NewsUtil.CUG_INDEX).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        NewListBean newListBean=new NewListBean() ;
                        try {
                            Document document = Jsoup.parse(responseBody.string());
                            Element element = document.getElementById("slider");
                            Elements elements = element.children();
                            List<NewListBean.BannerBean> list =null;
                            if (elements!=null&&elements.size()>0) {
                                list = new ArrayList<>();
                                for (Element item :
                                        elements) {
                                    NewListBean.BannerBean bannerBean = new NewListBean.BannerBean();
                                    bannerBean.setContentUrl(NewsUtil.getHref(item.getElementsByTag("a").attr("href")));
                                    bannerBean.setThumb(NewsUtil.getHref(item.getElementsByTag("a").select("img").attr("src")));
                                    bannerBean.setTitle(item.select(".dtxt").text());
                                    list.add(bannerBean);
                                }
                            }
                            newListBean.setBannerBeanList(list);
                            iView.updateData(newListBean);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
