package com.example.news.mvp;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.news.api.CugNewsApi;
import com.example.news.bean.NewListBean;

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
 * 创建时间:    2017/9/16      16:50
 * QQ:             1981367757
 */

public class NewsListPresenter extends BasePresenter<IView<NewListBean>, NewsListModel> {
    private int num = 0;
    private int totalPage=0;

    public NewsListPresenter(IView<NewListBean> iView, NewsListModel baseModel) {
        super(iView, baseModel);
        totalPage=144;
    }


    public void getCugNewsData(final boolean isShowLoading, final boolean isRefresh) {
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num = 0;
            getCugNewsBannerData();
        }
        num++;
        baseModel.getRepositoryManager().getApi(CugNewsApi.class)
                .getCugNewsData(isRefresh?"http://www.cug.edu.cn/index/ddyw.htm":getUrl()).subscribeOn(Schedulers.io())
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
//                            if (text!=null) {
//                                String num=text.substring(text.lastIndexOf("/")+1).trim();
//                                CommonLogger.e("num:"+num);
//                                totalPage= Integer.parseInt(num.trim());
//                            }
                            Element newElement=document.select(".col-news-list").first();
                            List<NewListBean.NewsItem> newsItemList=null;
                            if (newElement != null) {
                                newsItemList=new ArrayList<>();
                                for (Element child :
                                        newElement.children()) {
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
                            iView.updateData(newListBean);
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
                                getCugNewsData(isShowLoading, isRefresh);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }

    private String getHref(String href) {
        if (href!=null&& !href.startsWith("http")) {
           return "http://www.cug.edu.cn/"+href;
        }
        return href;
    }

    private String getUrl() {
        if (totalPage > 0) {
           return  "http://www.cug.edu.cn/index/ddyw/"+(totalPage-num+1)+".htm";
        }
        return "http://www.cug.edu.cn/index/ddyw.htm";
    }

    private void getCugNewsBannerData() {
        baseModel.getRepositoryManager().getApi(CugNewsApi.class)
                .getCugNewsData("http://www.cug.edu.cn/").subscribeOn(Schedulers.io())
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
                            Element element = document.getElementById("slider1");
                            Elements elements = element.children();
                            List<NewListBean.BannerBean> list =null;
                            if (elements!=null&&elements.size()>0) {
                                list = new ArrayList<>();
                                for (Element item :
                                        elements) {
                                    NewListBean.BannerBean bannerBean = new NewListBean.BannerBean();
                                    bannerBean.setContentUrl(item.getElementsByTag("a").attr("href"));
                                    bannerBean.setThumb(getHref(item.getElementsByTag("a").select("img").attr("src")));
                                    bannerBean.setTitle(item.select("div.btjj").select("a[href]").get(0).text());
                                    bannerBean.setDescription(item.select("div.btjj").select("a[href]").get(1).text());
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
//                        iView.showError(null,null);
                    }

                    @Override
                    public void onComplete() {
//                        iView.hideLoading();
                    }
                });
    }
}
