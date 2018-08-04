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


/**
 * 项目名称:    NewFastFrame
 * 创建人:        陈锦军
 * 创建时间:    2017/9/16      16:50
 * QQ:             1981367757
 */

public class NewsListPresenter extends BasePresenter<IView<NewListBean>, NewsListModel> {
    private int num = 0;
    private Integer totalPage = -1;

    public NewsListPresenter(IView<NewListBean> iView, NewsListModel baseModel) {
        super(iView, baseModel);
    }


    public void getCugNewsData(final boolean isShowLoading, final boolean isRefresh, final String url) {
        if (iView == null) {
            return;
        }
        if (isShowLoading) {
            iView.showLoading(null);
        }
        if (isRefresh) {
            num = 0;
            totalPage = -1;
        }
        num++;
        if (totalPage != -1 && num > totalPage) {
            iView.updateData(null);
            iView.hideLoading();
            num--;
            return;
        }
        if (isRefresh) {
            if (NewsUtil.CUG_NEWS.equals(url) || NewsUtil.DK_INDEX_URL.equals(url)
                    || NewsUtil.JG_INDEX_URL.equals(url)
                    || NewsUtil.CUG_VOICE_INDEX.equals(url)
                    || NewsUtil.GG_INDEX_URL.equals(url)
                    || NewsUtil.JSJ_INDEX_URL.equals(url)
                    || NewsUtil.WY_INDEX_URL.equals(url)
                    || NewsUtil.DY_INDEX_URL.equals(url)
                    || NewsUtil.XY_INDEX_URL.equals(url)
                    || NewsUtil.ZDH_INDEX_URL.equals(url)
                    || NewsUtil.ZY_INDEX_URL.equals(url)
                    || NewsUtil.CH_INDEX_URL.equals(url)
                    || NewsUtil.GC_INDEX_URL.equals(url)
                    || NewsUtil.HJ_INDEX_URL.equals(url)
                    || NewsUtil.DWK_INDEX_URL.equals(url)
                    || NewsUtil.JD_INDEX_URL.equals(url)
                    || NewsUtil.HY_INDEX_URL.equals(url)
                    || NewsUtil.SL_INDEX_URL.equals(url)
                    || NewsUtil.MY_INDEX_URL.equals(url)
                    ) {
                getCugNewsBannerData(NewsUtil.getBaseUrl(url));
            }
        }
        String realUrl = isRefresh ? url : NewsUtil.getCollegeNewsUrl(url, totalPage, num);
        baseModel.getRepositoryManager().getApi(CugNewsApi.class)
                .getCugNewsData(realUrl).subscribeOn(Schedulers.io())
                .compose(iView.<ResponseBody>bindLife())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        NewListBean newListBean = new NewListBean();
                        Document document = null;
                        try {
                            String temp = responseBody.string().replace("&nbsp;", " ");
                            CommonLogger.e(temp);
                            document = Jsoup.parse(temp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        parseNewsData(document, url, newListBean);
                        iView.updateData(newListBean);
                        if (newListBean.getNewsItemList() == null
                                || newListBean.getNewsItemList().size() == 0) {
                            if (!isRefresh) {
                                num--;
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e != null) {
                            CommonLogger.e(e);
                        }
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getCugNewsData(isShowLoading, isRefresh, url);
                            }
                        });
                        if (!isRefresh) {
                            num--;
                        }
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }


    private void parseNewsData(Document document, String url, NewListBean newListBean) {
        if (url.startsWith(NewsUtil.CUG_INDEX)) {
            Element element = document.getElementById("fanye177473");
            String text = element.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length() - 1);
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            Element newElement = document.select(".col-news-list").first();
            List<NewListBean.NewsItem> newsItemList = null;
            if (newElement != null) {
                newsItemList = new ArrayList<>();
                for (Element child :
                        newElement.children()) {
                    if (!child.hasClass("list_item")) {
                        continue;
                    }
                    NewListBean.NewsItem newsItem = new NewListBean.NewsItem();
                    Element title = child.select(".news-title").first();
                    if (title != null) {
                        newsItem.setTitle(title.text());
                        newsItem.setContentUrl(NewsUtil.getRealUrl(title.attr("href"), NewsUtil.CUG_INDEX));
                    }
                    Element date = child.select(".news-date").first();
                    if (date != null) {
                        newsItem.setTime(date.text());
                    }
                    newsItem.setFrom("中国地质大学");
                    newsItemList.add(newsItem);
                }
            }
            newListBean.setNewsItemList(newsItemList);
        } else if (url.startsWith(NewsUtil.JG_BASE_URL)) {
            Element page = document.getElementById("fanye181138");
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
//            line_u5_1
            List<NewListBean.NewsItem> list = new ArrayList<>();
            for (int i = 0; i < 19; i++) {
                String id = "line_u8_" + i;
                Element item = document.getElementById(id);
                String temp = null;
                if (item != null) {
                    temp = item.text();
                }
                if (item != null && (temp == null || !temp.contains("none"))) {
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.JG_BASE_URL));
                        bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                        bean.setFrom("经管学院");
                        list.add(bean);
                    }
                }
            }
            newListBean.setNewsItemList(list);
        } else if (url.startsWith(NewsUtil.GG_BASE_URL)) {

            Element page = document.getElementById("fanye200934");
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }


            Elements item = document.select(".sort-log");
            if (item.size() > 0 && item.first().children().size() > 0) {
                List<NewListBean.NewsItem> result = new ArrayList<>();
                for (Element element : item.first().children()) {
                    NewListBean.NewsItem bean = new NewListBean.NewsItem();
                    Element image = element.getElementsByTag("img").first();
                    bean.setFrom("公共管理学院");
                    if (image != null) {
                        bean.setThumb(NewsUtil.getRealUrl(image.attr("src"), NewsUtil.GG_BASE_URL));
                    }
                    Element content = element.select(".desc-box").first();
                    if (content != null && content.children().size() > 1) {
                        bean.setTitle(content.children().first().text());
                        bean.setContentUrl(NewsUtil.getRealUrl(content.children().first().attr("href"), NewsUtil.GG_BASE_URL));
                        bean.setTime(content.children().get(1).text());
                    }
                    result.add(bean);
                }
                newListBean.setNewsItemList(result);
            }
        } else if (url.startsWith(NewsUtil.JSJ_BASE_URL)) {
            Elements item = document.select("ul.mni09.mni89");
            if (item.size() > 0 && item.first().getElementsByTag("li").size() > 0) {
                List<NewListBean.NewsItem> result = new ArrayList<>();
                for (Element element : item.first().getElementsByTag("li")) {
                    NewListBean.NewsItem bean = new NewListBean.NewsItem();
                    bean.setFrom("计算机学院");
                    Elements a = element.getElementsByTag("a");
                    if (a.size() > 0 && a.first().children().size() > 1) {
                        bean.setTitle(a.first().children().first().text());
                        bean.setTime(a.first().children().get(1).text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.first().attr("href"), NewsUtil.JSJ_BASE_URL));
                    }
                    result.add(bean);
                }
                newListBean.setNewsItemList(result);
            }
        } else if (url.startsWith(NewsUtil.DK_BASE_URL)) {
//fanye177091
            Element page = document.getElementById(NewsUtil.getDKPage(url));
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            Elements item = document.select(".subNews_li");
            if (item.size() > 0 && item.first().getElementsByTag("li").size() > 0) {
                List<NewListBean.NewsItem> result = new ArrayList<>();
                for (Element element : item.first().getElementsByTag("li")) {
                    NewListBean.NewsItem bean = new NewListBean.NewsItem();
                    bean.setFrom("地球科学学院");
                    Elements a = element.getElementsByTag("a");
                    bean.setTime(element.children().first().text());
                    if (a.size() > 0) {
                        bean.setTitle(a.first().text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.first().attr("href"), NewsUtil.DK_BASE_URL));
                    }
                    result.add(bean);
                }
                newListBean.setNewsItemList(result);
            }


        } else if (url.startsWith(NewsUtil.WY_BASE_URL)) {
            Elements element = document.select(".class_listcnt").select(".list");
            List<NewListBean.NewsItem> result = new ArrayList<>();
            for (int i = 0; i < element.size(); i++) {
                Element banner = element.get(i);
                if (banner != null && banner.children().size() > 0) {
                    for (Element item :
                            banner.children()) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        if (item.children().size() > 0) {
                            bean.setTime(item.children().first().text());
                            if (bean.getTime().contains("(") && bean.getTime().contains(")")) {
                                bean.setTime(bean.getTime().substring(bean.getTime().indexOf("(") + 1, bean.getTime().indexOf(")")));
                            }
                        }
                        Elements a = item.getElementsByTag("a");
                        if (a.size() > 0) {
                            bean.setContentUrl(NewsUtil.getRealUrl(a.first().attr("href"), NewsUtil.WY_BASE_URL));
                            bean.setTitle(a.first().attr("title"));
                        }
                        bean.setFrom("外国语学院");
                        result.add(bean);
                    }
                }
            }
            newListBean.setNewsItemList(result);
        } else if (url.startsWith(NewsUtil.DY_BASE_URL)) {

//winstyle136539
//            page id fanye136539
            Element page = document.getElementById("fanye136539");
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            Elements list = document.select(".c136539");
            Elements time = document.select(".timestyle136539");
            if (list.size() > 0 && list.size() == time.size()) {
                List<NewListBean.NewsItem> beanList = new ArrayList<>();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    Element item = list.get(i);
                    NewListBean.NewsItem bean = new NewListBean.NewsItem();
                    bean.setTitle(item.attr("title"));
                    bean.setContentUrl(NewsUtil.getRealUrl(item.attr("href"), NewsUtil.DY_BASE_URL));
                    bean.setTime(time.get(i).text());
                    beanList.add(bean);
                }
                newListBean.setNewsItemList(beanList);
            }
        } else if (url.startsWith(NewsUtil.XY_BASE_URL)) {
//            fanye176177  page id
            Element element = document.getElementById("content");
            Element page = document.getElementById("fanye176177");
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            if (element != null) {
                List<NewListBean.NewsItem> list = new ArrayList<>();
                Elements children = element.select(".post-6215.post.type-post.status-publish.format-standard.hentry.category-index_top1");
                int size = children.size();
                for (int i = 0; i < size - 2; i++) {
                    Element item = children.get(i);
//                        String style=item.attr("style");
//                        if (style ==null ||style.equals("")) {
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.attr("title"));
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.XY_BASE_URL));
                        if (item.select(".cat-time").first() != null) {
                            bean.setTime(item.select(".cat-time").first().text());
                        }
                        bean.setFrom("信息工程学院");
                        list.add(bean);
                    }
//                        }
                }
                newListBean.setNewsItemList(list);
            }
        } else if (url.startsWith(NewsUtil.ZDH_BASE_URL)) {
//            page id fanye176276
            Element page = document.getElementById("fanye176276");
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
//            line_u5_1
            List<NewListBean.NewsItem> list = new ArrayList<>();
            for (int i = 0; i < 19; i++) {
                String id = "line_u5_" + i;
                Element item = document.getElementById(id);
                String temp = null;
                if (item != null) {
                    temp = item.text();
                }
                if (item != null && (temp == null || !temp.contains("none"))) {
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.ZDH_BASE_URL));
                        bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                        bean.setFrom("自动化学院");
                        list.add(bean);
                    }
                }
            }
            newListBean.setNewsItemList(list);
        } else if (url.startsWith(NewsUtil.ZY_BASE_URL)) {
//            page id fanye193184
//            本科生和研究生   page id fanye193939
            Element page = document.getElementById(NewsUtil.getZYPage(url));
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
//            line_u5_1
            List<NewListBean.NewsItem> list = new ArrayList<>();
            for (int i = 0; i < 29; i++) {
                String id = "line_u5_" + i;
                Element item = document.getElementById(id);
                String temp = null;
                if (item != null) {
                    temp = item.text();
                }
                if (item != null && (temp == null || !temp.contains("none"))) {
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.ZY_BASE_URL));
                        bean.setTime(item.text().substring(0, item.text().indexOf(bean.getTitle())));
                        bean.setFrom("资源学院");
                        list.add(bean);
                    }
                }
            }
            newListBean.setNewsItemList(list);
        } else if (url.startsWith(NewsUtil.CH_BASE_URL)) {
//             page id =fanye176770
            Elements container = document.select(".tzgg-list");
            Element page = document.getElementById("fanye176770");
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            if (container.size() > 0 && container.first().children().size() > 0) {
                List<NewListBean.NewsItem> list = new ArrayList<>();
                Elements chidren = container.first().children();
                for (Element item :
                        chidren) {
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.CH_BASE_URL));
                        bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                        bean.setFrom("材料与化学学院");
                        list.add(bean);
                    }
                }
                newListBean.setNewsItemList(list);
            }
        } else if (url.startsWith(NewsUtil.GC_BASE_URL)) {
//             class right_list
            Element element = document.select(".right_list").first();
            if (element != null && element.children().size() > 0
                    && element.children().first().children().size() > 0) {
                Elements children = element.children().first().children();
                List<NewListBean.NewsItem> list = new ArrayList<>();
                for (Element item :
                        children) {
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.GC_BASE_URL));
                        bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                        bean.setFrom("工程学院");
                        list.add(bean);
                    }
                }
                newListBean.setNewsItemList(list);
            }
        } else if (url.startsWith(NewsUtil.HJ_BASE_URL)) {
            Elements container = document.select(".list.newslist");
            if (container.size() > 0 && container.first().children().size() > 0) {
                List<NewListBean.NewsItem> list = new ArrayList<>();
                Elements children = container.first().children();
                for (Element item :
                        children) {
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.HJ_BASE_URL));
                        bean.setFrom("环境学院");
                        list.add(bean);
                    }
                }
                newListBean.setNewsItemList(list);
            }
        } else if (url.startsWith(NewsUtil.DWK_BASE_URL)) {
//            newslist
//            page id =fanye192802
            Elements container = document.select(".newslist");
            Element page = document.getElementById("fanye192802");
            String text = null;
            if (page != null) {
                text = page.text();
            }
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            if (container.size() > 0) {
                Elements elements = container.first().getElementsByTag("li");
                if (elements.size() > 0) {
                    List<NewListBean.NewsItem> list = new ArrayList<>();
                    for (Element item :
                            elements) {
                        Element a = item.getElementsByTag("a").first();
                        if (a != null) {
                            NewListBean.NewsItem bean = new NewListBean.NewsItem();
                            bean.setTitle(a.text());
                            bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.DWK_BASE_URL));
                            bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                            bean.setFrom("地空学院");
                            list.add(bean);
                        }
                    }
                    newListBean.setNewsItemList(list);
                }
            }
        } else if (url.startsWith(NewsUtil.JD_BASE_URL)) {
//            n_listxx1
//            page id= fanye163147
            Elements container = document.select(".n_listxx1");
            Element page = document.getElementById("fanye163147");
            String text = null;
            if (page != null) {
                text = page.text();
            }
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            if (container.size() > 0) {
                Elements elements = container.first().getElementsByTag("li");
                if (elements.size() > 0) {
                    List<NewListBean.NewsItem> list = new ArrayList<>();
                    for (Element item :
                            elements) {
                        Element a = item.getElementsByTag("a").first();
                        if (a != null) {
                            NewListBean.NewsItem bean = new NewListBean.NewsItem();
                            bean.setTitle(a.text());
                            bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.JD_BASE_URL));
                            bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                            bean.setFrom("机电学院");
                            list.add(bean);
                        }
                    }
                    newListBean.setNewsItemList(list);
                }
            }
        } else if (url.startsWith(NewsUtil.HY_BASE_URL)) {
//            class name =post-list

            Element page = document.getElementById("fanye202668");
            String text = null;
            if (page != null) {
                text = page.text();
            }
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }

            Element content = document.select(".list").first();
            if (content != null) {
                Elements children = content.children();
                int size = children.size();
                List<NewListBean.NewsItem> newsItemList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Element item = children.get(i);
                    NewListBean.NewsItem bean = new NewListBean.NewsItem();

                    if (item.select(".list_time.fl").first() != null) {
                        bean.setTime(item.select(".list_time.fl").first().text());
                    }
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.HY_BASE_URL));
                        bean.setTitle(a.text());
                    }
                    newsItemList.add(bean);
                }
                newListBean.setNewsItemList(newsItemList);
            }
        } else if (url.startsWith(NewsUtil.SL_BASE_URL)) {
//            page id fanye176363
            Element page = document.getElementById("fanye204952");
            String text = null;
            if (page != null) {
                text = page.text();
            }
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
//            line_u5_1
            List<NewListBean.NewsItem> list = new ArrayList<>();
            Element content = document.select(".col-news-list").first();
            if (content != null) {
                Elements children = content.children();
                int size = children.size();
                for (int i = 0; i < size; i++) {
                    Element item = children.get(i);
                    Element a = item.getElementsByTag("a").first();
                    if (a != null) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        bean.setTitle(a.text());
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.SL_BASE_URL));
                        bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                        bean.setFrom("数理学院");
                        list.add(bean);
                    }
                }
            }
            newListBean.setNewsItemList(list);
        } else if (url.startsWith(NewsUtil.YM_BASE_URL)) {
            Element page = null;
            if (url.equals(NewsUtil.YM_INDEX_URL)) {
                page = document.getElementById("fanye206626");
            } else if (url.equals(NewsUtil.YM_NOTICE_URL)) {
                page = document.getElementById("fanye206612");
            }else {
                page = document.getElementById("fanye206617");
            }
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
//            line_u5_1
            List<NewListBean.NewsItem> list = new ArrayList<>();
            for (int i = 0; i < 29; i++) {
                String id = "line_u5_" + i;
                Element item = document.getElementById(id);
                String temp = null;
                if (item != null) {
                    temp = item.text();
                }
                if (item != null && (temp == null || !temp.contains("none"))) {
                    Element img = item.getElementsByTag("img").first();
                    NewListBean.NewsItem bean = new NewListBean.NewsItem();
                    if (img != null) {
                        bean.setThumb(NewsUtil.getRealUrl(img.attr("src"),NewsUtil.YM_BASE_URL));
                    }
                    bean.setFrom("艺媒学院");
                    if (url.equals(NewsUtil.YM_INDEX_URL)||url.equals(NewsUtil.YM_NOTICE_URL)) {
                        Element body=item.select(".media-body").first();
                        Element a=body.getElementsByTag("a").first();
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"),NewsUtil.YM_BASE_URL));
                        bean.setTitle(a.text());
                        bean.setTime(body.text().substring(0, body.text().indexOf(bean.getTitle())));
                    }else {
                        Element a=item.getElementsByTag("a").first();
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"),NewsUtil.YM_BASE_URL));
                        bean.setTitle(a.text());
                    }

                    list.add(bean);
                }
            }
            newListBean.setNewsItemList(list);
        } else if (url.startsWith(NewsUtil.MY_BASE_URL)) {
            Elements container = document.select(".tpxw_con.list_l");
            Element page = document.getElementById("fanye206430");
            String text = null;
            if (page != null) {
                text = page.text();
            }
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
            if (container.size() > 0) {
                Elements elements = container.first().getElementsByTag("li");
                if (elements.size() > 0) {
                    List<NewListBean.NewsItem> list = new ArrayList<>();
                    for (Element item :
                            elements) {
                        NewListBean.NewsItem bean = new NewListBean.NewsItem();
                        Element a=item.getElementsByTag("a").first();
                        if (a != null) {
                            bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"),NewsUtil.MY_BASE_URL));
                            bean.setTitle(a.text());
                        }
                        Element time=item.select(".fr").first();
                        if (time!=null) {
                            bean.setTime(time.text());
                        }
                        bean.setFrom("马克思主义学院");
                        list.add(bean);
                    }
                    newListBean.setNewsItemList(list);
                }
            }
        } else if (url.startsWith(NewsUtil.VOICE_BASE_URL)) {
            if (url.equals(NewsUtil.CUG_VOICE_INDEX) || url.equals(NewsUtil.CUG_VOICE_NOTIFY)) {
                Element page = document.getElementById("fanye193729");
                String text = page.text();
                if (text != null) {
                    String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                    totalPage = Integer.valueOf(num);
                } else {
                    totalPage = 1;
                }
                List<NewListBean.NewsItem> list = new ArrayList<>();
//            line_u8_0
                for (int i = 0; i < 29; i++) {
                    String id = "line_u8_" + i;
                    Element item = document.getElementById(id);
                    String temp;
                    if (item != null) {
                        temp = item.text();
                    } else {
                        continue;
                    }
                    if ((temp == null || !temp.contains("none"))) {
                        Element a = item.getElementsByTag("a").first();
                        if (a != null) {
                            NewListBean.NewsItem bean = new NewListBean.NewsItem();
                            bean.setTitle(a.text());
                            bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.VOICE_BASE_URL));
                            bean.setTime(item.text().substring(item.text().indexOf(bean.getTitle()) + bean.getTitle().length()));
                            bean.setFrom("地大之声");
                            list.add(bean);
                        }
                    }
                }
                newListBean.setNewsItemList(list);
            } else {
                Element page = document.getElementById("fanye193742");
                String text = page.text();
                if (text != null) {
                    String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                    totalPage = Integer.valueOf(num);
                } else {
                    totalPage = 1;
                }
                List<NewListBean.NewsItem> list = new ArrayList<>();
//            line_u8_0
                for (int i = 0; i < 39; i++) {
                    String id = "line_u6_" + i;
                    Element item = document.getElementById(id);
                    String temp;
                    if (item != null) {
                        temp = item.text();
                    } else {
                        continue;
                    }
                    if ((temp == null || !temp.contains("none"))) {
                        Element a = item.getElementsByTag("a").first();
                        if (a != null) {
                            NewListBean.NewsItem bean = new NewListBean.NewsItem();
                            bean.setTitle(item.text());
                            Element image = a.getElementsByTag("img").first();
                            if (image != null) {
                                bean.setThumb(NewsUtil.getRealUrl(image.attr("src"), NewsUtil.VOICE_BASE_URL));
                            }
                            bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.VOICE_BASE_URL));
                            bean.setFrom("地大之声");
                            list.add(bean);
                        }
                    }
                }
                newListBean.setNewsItemList(list);
            }
        }
    }


    private void getCugNewsBannerData(final String url) {
        baseModel.getRepositoryManager().getApi(CugNewsApi.class)
                .getCugNewsData(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        NewListBean newListBean = new NewListBean();
                        try {
                            Document document = Jsoup.parse(responseBody.string());
                            parseBannerNewData(url, document, newListBean);
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

    private void parseBannerNewData(String url, Document document, NewListBean newListBean) {
        if (url.equals(NewsUtil.CUG_INDEX)) {
            Element element = document.getElementById("slider");
            Elements elements = element.children();
            List<NewListBean.BannerBean> list = null;
            if (elements != null && elements.size() > 0) {
                list = new ArrayList<>();
                for (Element item :
                        elements) {
                    NewListBean.BannerBean bannerBean = new NewListBean.BannerBean();
                    bannerBean.setContentUrl(NewsUtil.getRealUrl(item.getElementsByTag("a").attr("href"), NewsUtil.CUG_INDEX));
                    bannerBean.setThumb(NewsUtil.getRealUrl(item.getElementsByTag("a").select("img").attr("src"), NewsUtil.CUG_INDEX));
                    bannerBean.setTitle(item.select(".dtxt").text());
                    list.add(bannerBean);
                }
            }
            newListBean.setBannerBeanList(list);
        } else if (NewsUtil.DK_BASE_URL.equals(url)) {
            Element element = document.getElementById("imgs");
            Elements elements = null;
            if (element != null) {
                elements = element.children();
            }
            List<NewListBean.BannerBean> list = null;
            if (elements != null && elements.size() > 0) {
                list = new ArrayList<>();
                for (Element item :
                        elements) {
                    NewListBean.BannerBean bannerBean = new NewListBean.BannerBean();
                    bannerBean.setContentUrl(NewsUtil.getRealUrl(item.getElementsByTag("a").attr("href"), url));
                    bannerBean.setThumb(NewsUtil.getRealUrl(item.getElementsByTag("img").attr("src"), url));
                    bannerBean.setTitle(item.text());
                    list.add(bannerBean);
                }
            }
            newListBean.setBannerBeanList(list);
        } else if (NewsUtil.JG_BASE_URL.equals(url)) {

            /**
             linkarr[1]= "shownew.asp?id=1942";
             picarr[1]  = "uploadfile/2017-7/201771111205.jpg";
             textarr[1]  = "2017旅游管理专业赴丹霞山产学研基地开展教学实习";
             linkarr[2] = "";
             picarr[2]  = "uploadfile/2017-7/2017711152651.jpg";
             textarr[2]  = "2017年7月我院495位本科生，202位研究生毕业！";
             linkarr[3] = "http://www.cug.edu.cn/new/Show.aspx?ID=3112&cid=000102";
             picarr[3]  = "uploadfile/2017-7/2017711154640.jpg";
             textarr[3]  = "第十六届武汉电子商务国际会议召开（2017）";
             linkarr[4] = "";
             picarr[4]  = "uploadfile/2017-7/2017711115522.jpg";
             textarr[4]  = "第二届全国“互联网+”大学生创新创业大赛我院摘得湖北省银奖";
             linkarr[5] = "http://jgxy.cug.edu.cn/shownew.asp?id=1971";
             picarr[5]  = "uploadfile/2017-9/2017921102639.jpg";
             textarr[5]  = "人福医药与我院签订产学研合作协议";
             *
             * */
            Elements script = document.getElementsByTag("script");
            String content = null;
            for (Element temp :
                    script) {
                if (temp.html().contains("ImageChangeNews")) {
                    content = temp.html();
                    break;
                }
            }
            if (content == null) {
                return;
            }
            String start = "{";
            String end = "u_u4_icn.changeimg(0)";
            content = content.substring(content.indexOf(start) + start.length(), content.indexOf(end)).replace("amp;", "");
            String str = "\\);";
            String[] array = content.split(str);
            List<NewListBean.BannerBean> bannerList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            for (String string :
                    array) {
                if (string.contains("\"")) {
                    String[] temp = string.split("\"");
                    list.add(temp[1]);
                    list.add(temp[3]);
                    if (temp[5].contains("<span") && temp.length >= 7 && temp[7].contains("<")) {
                        list.add(temp[7].substring(1, temp[7].lastIndexOf("<")));
                    } else {
                        list.add((temp[5]));
                    }
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String item = list.get(i);
                if (i % 3 == 0) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    bannerList.add(bean);
                } else {
                    if (i % 3 == 1) {
                        bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    } else {
                        bannerList.get(i / 3).setTitle(item);
                    }
                }
            }
            newListBean.setBannerBeanList(bannerList);
        } else if (NewsUtil.GG_BASE_URL.equals(url)) {
//            swiper-wrapper
            Elements item = document.select(".swiper-wrapper");
            if (item.size() > 0 && item.first().children().size() > 0) {
                List<NewListBean.BannerBean> list = new ArrayList<>();
                for (Element element :
                        item.first().children()) {
                    NewListBean.BannerBean bannerBean = new NewListBean.BannerBean();
                    bannerBean.setContentUrl(NewsUtil.getRealUrl(element.getElementsByTag("a").attr("href"), url));
                    bannerBean.setThumb(NewsUtil.getRealUrl(element.getElementsByTag("img").attr("src"), url));
                    bannerBean.setTitle(item.text());
                    list.add(bannerBean);
                }
                newListBean.setBannerBeanList(list);
            }
        } else if (NewsUtil.JSJ_BASE_URL.equals(url)) {
            //        JQ-slide-content
            Elements item = document.select(".JQ-slide-content");
            if (item.size() > 0 && item.first().children().size() > 0) {
                List<NewListBean.BannerBean> list = new ArrayList<>();
                for (Element element :
                        item.first().children()) {
                    NewListBean.BannerBean bannerBean = new NewListBean.BannerBean();
                    bannerBean.setThumb(NewsUtil.getRealUrl(element.getElementsByTag("img").attr("src"), url));
                    bannerBean.setTitle(element.getElementsByTag("img").attr("title"));
                    bannerBean.setContentUrl(NewsUtil.getRealUrl(element.getElementsByTag("a").attr("href"), url));
                    list.add(bannerBean);
                }
                newListBean.setBannerBeanList(list);
            }
        } else if (NewsUtil.WY_BASE_URL.equals(url)) {
            /**
             *
             imgUrl1="uploadfile/jpg/2017-9/201798164557152.jpg";
             imgtext1="外国语学院举行2017级新生开学典礼"
             imgLink1=escape("E_ReadNews.asp?newsid=3230");
             imgUrl2="uploadfile/jpg/2017-9/201794135155543.jpg";
             imgtext2="外国语学院圆满完成2017年迎接新生各项工作"
             imgLink2=escape("E_ReadNews.asp?newsid=3229");
             imgUrl3="uploadfile/jpg/2017-6/201762912947895.jpg";
             imgtext3="最美外语人【2017】第2期（总第7期）：附小.."
             imgLink3=escape("E_ReadNews.asp?newsid=3223");
             imgUrl4="uploadfile/jpg/2017-6/201761282236218.jpg";
             imgtext4="外国语学院第三次团员学生代表大会成功举行"
             imgLink4=escape("E_ReadNews.asp?newsid=3208");
             imgUrl5="uploadfile/jpg/2017-6/201769181858421.jpg";
             imgtext5="外国语学院召开2016级年级大会"
             imgLink5=escape("E_ReadNews.asp?newsid=3207");
             var focus_width=370
             var focus_height=340
             var text_height=16
             var swf_height = focus_height+text_height
             *
             *
             * */
            Element tempElement = document.getElementById("picture1");
            Elements element = null;
            if (tempElement!=null) {
                element = tempElement.getElementsByTag("script");
            }
            String content;
            List<NewListBean.BannerBean> bannerList = null;
            if (element!=null&&element.size() > 0) {
                content = element.first().html();
                String end = "var";
                content = content.substring(0, content.indexOf(end));
                List<String> list = new ArrayList<>();
                bannerList = new ArrayList<>();
                String[] array = content.split(";");
                for (String string :
                        array) {
                    if (string.contains("\"")) {
                        String[] temp = string.split("\"");
                        list.add(temp[1]);
                        if (temp.length > 3) {
                            list.add(temp[3]);
                        }
                    }
                }
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String item = list.get(i);
                    if (i % 3 == 0) {
                        NewListBean.BannerBean bean = new NewListBean.BannerBean();
                        bean.setThumb(NewsUtil.getRealUrl(item, url));
                        bannerList.add(bean);
                    } else {
                        if (i % 3 == 1) {
                            bannerList.get(i / 3).setTitle(item);
                        } else {
                            bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, url));
                        }
                    }
                }
                newListBean.setBannerBeanList(bannerList);
            }
        } else if (url.equals(NewsUtil.DY_BASE_URL)) {
            /**
             *
             var u_u5_icn = new ImageChangeNews("u_u5_", 290, 190, 4, 2.0, true, false, false, true);
             //初始化图片, 并启动定时
             function u_u5_init_img()
             {
             u_u5_icn.addimg("\/__local\/4\/09\/8D\/5A1D48114BA24BA79A24CA583B5_DCFF4BFE_8AC2.jpg", "info\/1519\/2897.htm", "【毕业典礼】首届地球科学菁英班毕业典礼暨...", "");

             u_u5_icn.addimg("\/__local\/0\/66\/F0\/48D2A167AD63008E2E2DC027521_60E5DE34_67CB.jpg", "info\/1519\/3486.htm", "【名师面对面】做一个快乐的大学生——院长...", "");

             u_u5_icn.addimg("\/__local\/5\/C0\/1E\/C00FF92AD9DE372D74FA4D59F40_9509D619_6CBB.jpg", "info\/1519\/3464.htm", "【学做创】我院组织学生在“五四”青年节期...", "");

             u_u5_icn.addimg("\/__local\/0\/91\/A4\/715438F0C2198B148C9C99F58D9_8737CF6A_9A53.jpg", "info\/1519\/3457.htm", "【聆听&amp;发现】邹宗平女士为我院学子讲述李四...", "");

             u_u5_icn.addimg("\/__local\/A\/D5\/DF\/EDA0D6309077EB74EFB0FCFAD59_8F2856EB_88BA.jpg", "info\/1519\/3429.htm", "【学院新闻】外国语学院来我院调研交流", "");

             u_u5_icn.changeimg(0);
             }
             *
             *
             * */
            Elements script = document.getElementsByTag("script");
            String content = null;
            for (Element temp :
                    script) {
                if (temp.html().contains("ImageChangeNews")) {
                    content = temp.html();
                    break;
                }
            }
            if (content == null) {
                return;
            }
            String start = "{";
            String end = "u_u5_icn.changeimg(0)";
            content = content.substring(content.indexOf(start) + start.length(), content.indexOf(end)).replace("amp;", "");
            String str = "\\);";
            String[] array = content.split(str);
            List<NewListBean.BannerBean> bannerList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            for (String string :
                    array) {
                if (string.contains("\"")) {
                    String[] temp = string.split("\"");
                    list.add(temp[1]);
                    list.add(temp[3]);
                    list.add((temp[5]));
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String item = list.get(i);
                if (i % 3 == 0) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    bannerList.add(bean);
                } else {
                    if (i % 3 == 1) {
                        bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    } else {
                        bannerList.get(i / 3).setTitle(item);
                    }
                }
            }
            newListBean.setBannerBeanList(bannerList);
        } else if (url.equals(NewsUtil.XY_BASE_URL)) {
            Element element = document.select(".pic").first();
            if (element != null && element.getElementsByTag("li").size() > 0) {
                Elements elements = element.getElementsByTag("li");
                List<NewListBean.BannerBean> bannerList = new ArrayList<>();
                for (Element item :
                        elements) {
                    NewListBean.BannerBean bannerBean = new NewListBean.BannerBean();
                    bannerBean.setThumb(NewsUtil.getRealUrl(item.getElementsByTag("img").attr("src"), url));
                    bannerBean.setTitle(item.getElementsByTag("img").attr("alt"));
                    bannerBean.setContentUrl(NewsUtil.getRealUrl(item.getElementsByTag("a").attr("href"), url));
                    bannerList.add(bannerBean);
                }
                newListBean.setBannerBeanList(bannerList);
            }
        } else if (url.equals(NewsUtil.ZDH_BASE_URL)) {
            Elements script = document.getElementsByTag("script");
            String content = null;
            for (Element temp :
                    script) {
                if (temp.html().contains("ImageChangeNews")) {
                    content = temp.html();
                    break;
                }
            }
            if (content == null) {
                return;
            }
            String start = "{";
            String end = "u_u2_icn.changeimg(0)";
            if (content.indexOf(end)>0) {
                content = content.substring(content.indexOf(start) + start.length(), content.indexOf(end)).replace("amp;", "");
            }
            String str = "\\);";
            String[] array = content.split(str);
            List<NewListBean.BannerBean> bannerList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            for (String string :
                    array) {
                if (string.contains("\"")) {
                    String[] temp = string.split("\"");
                    list.add(temp[1]);
                    list.add(temp[3]);
                    list.add((temp[5]));
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String item = list.get(i);
                if (i % 3 == 0) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    bannerList.add(bean);
                } else {
                    if (i % 3 == 1) {
                        bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    } else {
                        bannerList.get(i / 3).setTitle(item);
                    }
                }
            }
            newListBean.setBannerBeanList(bannerList);
        } else if (url.startsWith(NewsUtil.ZY_BASE_URL)) {
            Elements script = document.getElementsByTag("script");
            String content = null;
            for (Element temp :
                    script) {
                if (temp.html().contains("ImageChangeNews")) {
                    content = temp.html();
                    break;
                }
            }
            if (content == null) {
                return;
            }
            String start = "{";
            String end = "u_u11_icn.changeimg(0)";
            content = content.substring(content.indexOf(start) + start.length(), content.indexOf(end)).replace("amp;", "");
            String str = "\\);";
            String[] array = content.split(str);
            List<NewListBean.BannerBean> bannerList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            for (String string :
                    array) {
                if (string.contains("\"")) {
                    String[] temp = string.split("\"");
                    list.add(temp[1]);
                    list.add(temp[3]);
                    list.add((temp[5]));
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String item = list.get(i);
                if (i % 3 == 0) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item, NewsUtil.ZY_BASE_URL).replace("\\", ""));
                    bannerList.add(bean);
                } else {
                    if (i % 3 == 1) {
                        bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, NewsUtil.ZY_BASE_URL).replace("\\", ""));
                    } else {
                        bannerList.get(i / 3).setTitle(item);
                    }
                }
            }
            newListBean.setBannerBeanList(bannerList);
        } else if (url.equals(NewsUtil.CH_BASE_URL)) {
            Elements script = document.getElementsByTag("script");
            String content = null;
            for (Element temp :
                    script) {
                if (temp.html().contains("ImageChangeNews")) {
                    content = temp.html();
                    break;
                }
            }
            if (content == null) {
                return;
            }
            String start = "{";
            String end = "u_u4_icn.changeimg(0)";
            content = content.substring(content.indexOf(start) + start.length(), content.indexOf(end)).replace("amp;", "");
            String str = "\\);";
            String[] array = content.split(str);
            List<NewListBean.BannerBean> bannerList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            for (String string :
                    array) {
                if (string.contains("\"")) {
                    String[] temp = string.split("\"");
                    list.add(temp[1]);
                    list.add(temp[3]);
                    list.add((temp[5]));
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String item = list.get(i);
                if (i % 3 == 0) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    bannerList.add(bean);
                } else {
                    if (i % 3 == 1) {
                        bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    } else {
                        bannerList.get(i / 3).setTitle(item);
                    }
                }
            }
            newListBean.setBannerBeanList(bannerList);
        } else if (url.equals(NewsUtil.GC_BASE_URL)) {
            Element container = document.getElementById("D1pic1");
            if (container != null && container.children().size() > 0) {
                List<NewListBean.BannerBean> list = new ArrayList<>();
                for (Element item :
                        container.children()) {
                    if (item.text().equals("")) {
                        continue;
                    }
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    if (item.select(".banner_title").first() != null) {
                        bean.setTitle(item.select(".banner_title").first().text());
                    }
                    Elements a = item.getElementsByTag("a");
                    if (a.size() > 0) {
                        bean.setContentUrl(NewsUtil.getRealUrl(a.first().attr("href"), NewsUtil.GC_BASE_URL));
                    }
                    Elements image = item.getElementsByTag("img");
                    if (image.size() > 0) {
                        bean.setThumb(NewsUtil.getRealUrl(image.first().attr("src"), NewsUtil.GC_BASE_URL));
                    }
                    list.add(bean);
                }
                newListBean.setBannerBeanList(list);
            }
        } else if (url.equals(NewsUtil.HJ_BASE_URL)) {
            Element container = document.getElementById("D1pic1");
            if (container != null && container.children().size() > 0) {
                List<NewListBean.BannerBean> list = new ArrayList<>();
                for (Element item :
                        container.children()) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setTitle(item.text());
                    Elements a = item.getElementsByTag("a");
                    if (a.size() > 0) {
                        bean.setContentUrl(NewsUtil.getRealUrl(a.first().attr("href"), url));
                    }
                    Elements image = item.getElementsByTag("img");
                    if (image.size() > 0) {
                        bean.setThumb(NewsUtil.getRealUrl(image.first().attr("src"), url));
                    }
                    list.add(bean);
                }
                newListBean.setBannerBeanList(list);
            }
        } else if (url.equals(NewsUtil.DWK_BASE_URL)) {
            Element element = document.select(".news-pic").first();
            if (element != null) {
                Elements elements = element.getElementsByTag("li");
                if (elements.size() > 0) {
                    List<NewListBean.BannerBean> list = new ArrayList<>();
                    int size = elements.size();
                    for (int i = 0; i < size; i++) {
                        Element item = elements.get(i);
                        Elements a = item.getElementsByTag("a");
                        Elements image = item.getElementsByTag("img");
                        if (a.size() > 0) {
                            if (image.size() > 0) {
                                NewListBean.BannerBean bean = new NewListBean.BannerBean();
                                bean.setContentUrl(NewsUtil.getRealUrl(a.first().attr("href"), url));
                                bean.setThumb(NewsUtil.getRealUrl(image.first().attr("src"), url));
                                list.add(bean);
                            } else {
                                list.get(i % list.size()).setTitle(item.text());
                            }
                        }
                    }
                    newListBean.setBannerBeanList(list);
                }
            }
        } else if (url.equals(NewsUtil.JD_BASE_URL)) {
            Elements script = document.getElementsByTag("script");
            String content = null;
            for (Element temp :
                    script) {
                if (temp.html().contains("ImageChangeNews")) {
                    content = temp.html();
                    break;
                }
            }
            if (content == null) {
                return;
            }
            String start = "{";
            String end = "u_u5_icn.changeimg(0)";
            content = content.substring(content.indexOf(start) + start.length(), content.indexOf(end)).replace("amp;", "");
            String str = "\\);";
            String[] array = content.split(str);
            List<NewListBean.BannerBean> bannerList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            for (String string :
                    array) {
                if (string.contains("\"")) {
                    String[] temp = string.split("\"");
                    list.add(temp[1]);
                    list.add(temp[3]);
                    list.add((temp[5]));
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String item = list.get(i);
                if (i % 3 == 0) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    bannerList.add(bean);
                } else {
                    if (i % 3 == 1) {
                        bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    } else {
                        bannerList.get(i / 3).setTitle(item);
                    }
                }
            }
            newListBean.setBannerBeanList(bannerList);
        } else if (url.equals(NewsUtil.HY_BASE_URL)) {
//            class name col col-3 slide-box
            Element content = document.select(".bd").first();
            if (content != null) {
                Elements children = content.getElementsByTag("li");
                int size = children.size();
                List<NewListBean.BannerBean> bannerBeanList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    Element item = children.get(i);
                    Element a = item.getElementsByTag("a").first();
                    bean.setTitle(item.text());
                    if (a != null) {
                        bean.setContentUrl(NewsUtil.getRealUrl(a.attr("href"), NewsUtil.HY_BASE_URL));
                    }
                    Element image = item.getElementsByTag("img").first();
                    if (image != null) {
                        bean.setThumb(NewsUtil.getRealUrl(image.attr("src"), NewsUtil.HY_BASE_URL));
                    }
                    bannerBeanList.add(bean);
                }
                newListBean.setBannerBeanList(bannerBeanList);
            }
        } else if (url.equals(NewsUtil.SL_BASE_URL)) {
            Element content = document.getElementById("slider1");
            if (content != null) {
                Elements children = content.children();
                int size = children.size();
                List<NewListBean.BannerBean> bannerBeanList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Element item = children.get(i);
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setContentUrl(NewsUtil.getRealUrl(item.getElementsByTag("a").attr("href"), NewsUtil.SL_BASE_URL));
                    bean.setThumb(NewsUtil.getRealUrl(item.getElementsByTag("img").attr("src"), NewsUtil.SL_BASE_URL));
                    bean.setTitle(item.getElementsByTag("a").text());
                    bannerBeanList.add(bean);
                }
                newListBean.setBannerBeanList(bannerBeanList);
            }
        } else if (url.equals(NewsUtil.YM_BASE_URL)) {
            Elements container = document.select(".pic");
            if (container.size() > 0 && container.first()
                    .getElementsByTag("img").size() > 0) {
                List<NewListBean.BannerBean> list = new ArrayList<>();
                Elements elements = container.first().getElementsByTag("img");
                for (Element item :
                        elements) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item.attr("src"), url));
                    list.add(bean);
                }
                newListBean.setBannerBeanList(list);
            }

        } else if (url.equals(NewsUtil.MY_BASE_URL)) {
            Elements script = document.getElementsByTag("script");
            String content = null;
            for (Element temp :
                    script) {
                if (temp.html().contains("ImageChangeNews")) {
                    content = temp.html();
                    break;
                }
            }
            if (content == null) {
                return;
            }
            String start = "{";
            String end = "u_u6_icn.changeimg(0)";
            content = content.substring(content.indexOf(start) + start.length(), content.indexOf(end)).replace("amp;", "");
            String str = "\\);";
            String[] array = content.split(str);
            List<NewListBean.BannerBean> bannerList = new ArrayList<>();
            List<String> list = new ArrayList<>();
            for (String string :
                    array) {
                if (string.contains("\"")) {
                    String[] temp = string.split("\"");
                    list.add(temp[1]);
                    list.add(temp[3]);
                    list.add((temp[5]));
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String item = list.get(i);
                if (i % 3 == 0) {
                    NewListBean.BannerBean bean = new NewListBean.BannerBean();
                    bean.setThumb(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    bannerList.add(bean);
                } else {
                    if (i % 3 == 1) {
                        bannerList.get(i / 3).setContentUrl(NewsUtil.getRealUrl(item, url).replace("\\", ""));
                    } else {
                        bannerList.get(i / 3).setTitle(item);
                    }
                }
            }
            newListBean.setBannerBeanList(bannerList);
        } else if (url.equals(NewsUtil.VOICE_BASE_URL)) {
            Element content = document.getElementById("KinSlideshow");
            if (content != null) {
                Elements children = content.children();
                int size = children.size();
                List<NewListBean.BannerBean> bannerBeanList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    NewListBean.BannerBean item = new NewListBean.BannerBean();
                    Element element = children.get(i);
                    item.setContentUrl(NewsUtil.getRealUrl(element.attr("href"), NewsUtil.VOICE_BASE_URL));
                    Element image = element.getElementsByTag("img").first();
                    if (image != null) {
                        item.setThumb(NewsUtil.getRealUrl(image.attr("src"), NewsUtil.VOICE_BASE_URL));
                        item.setTitle(image.attr("alt"));
                    }
                    bannerBeanList.add(item);
                }
                newListBean.setBannerBeanList(bannerBeanList);
            }
        }
    }

}
