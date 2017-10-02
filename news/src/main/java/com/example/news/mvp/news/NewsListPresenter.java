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
        String realUrl;

        if (isRefresh) {
            if (NewsUtil.CUG_NEWS.equals(url) || NewsUtil.DK_INDEX_URL.equals(url)
                    || NewsUtil.JG_INDEX_URL.equals(url)
                    || NewsUtil.GG_INDEX_URL.equals(url)
                    || NewsUtil.JSJ_INDEX_URL.equals(url)
                    ||NewsUtil.WY_INDEX_URL.equals(url)) {
                getCugNewsBannerData(NewsUtil.getBaseUrl(url));
            }
        }
        if (url.startsWith(NewsUtil.CUG_INDEX)) {
            realUrl = isRefresh ? url : NewsUtil.getRealNewsUrl(url, totalPage, num);
        } else {
            realUrl = isRefresh ? url : NewsUtil.getCollegeNewsUrl(url, totalPage, num);
        }
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
                        NewListBean newListBean = new NewListBean();
                        Document document = null;
                        try {
//                            byte[] bytes=IOUtils.toByteArray(responseBody.byteStream());
//                            String result=new String(bytes,Charset.forName("utf-8"));
                            String temp = responseBody.string().replace("&nbsp;", " ");
                            CommonLogger.e(temp);
                            document = Jsoup.parse(temp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        parseNewsData(document, url, newListBean);
                        iView.updateData(newListBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getCugNewsData(isShowLoading, isRefresh, url);
                            }
                        });
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
            Element element = null;
            try {
                Element item = document.select(".a2").get(1);
                element = item.children().get(3).children().first()
                        .children().first().children()
                        .get(2).children().get(1)
                        .children().first()
                        .children().get(1)
                        .children().get(1);
            } catch (NullPointerException e) {
                CommonLogger.e("空指针异常" + e.getMessage());
                e.printStackTrace();
            }
            if (element != null && element.children().size() > 0) {
                List<NewListBean.NewsItem> result = new ArrayList<>();
                for (Element item :
                        element.children()) {
                    NewListBean.NewsItem newsItem = new NewListBean.NewsItem();
                    Elements temp = item.getElementsByTag("a");
                    if (temp.size() == 1) {
                        Element href = temp.first();
                        if (href != null) {
                            newsItem.setTitle(href.text());
                            newsItem.setContentUrl(NewsUtil.getRealUrl(href.attr("href"), NewsUtil.JG_BASE_URL));
                            String rootContent = item.text();
                            newsItem.setTime(rootContent.substring(rootContent.indexOf(newsItem.getTitle()) + newsItem.getTitle().length()));
                            newsItem.setFrom("经济管理学院");
                        }
                        result.add(newsItem);
                    }
                }
                newListBean.setNewsItemList(result);
            }
        } else if (url.startsWith(NewsUtil.GG_BASE_URL)) {
            Elements item = document.select(".sort-log");
            if (item.size() > 0 && item.first().children().size() > 0) {
                List<NewListBean.NewsItem> result = new ArrayList<>();
                for (Element element : item.first().children()) {
                    NewListBean.NewsItem bean = new NewListBean.NewsItem();
                    Element image = element.getElementsByTag("img").first();
                    bean.setFrom("公共管理学院");
                    if (image != null) {
                        bean.setThumb(NewsUtil.getRealUrl(image.attr("data-src"), NewsUtil.GG_BASE_URL));
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
//            fanye177293
            Element page = document.getElementById("fanye177293");
            String text = page.text();
            if (text != null) {
                String num = text.substring(text.lastIndexOf("/") + 1, text.length()).trim();
                totalPage = Integer.valueOf(num);
            } else {
                totalPage = 1;
            }
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
            Elements element=document.select(".list");
            List<NewListBean.NewsItem> result=new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Element banner=element.get(i);
                if (banner != null && banner.children().size() > 0) {
                    for (Element item :
                            banner.children()) {
                        NewListBean.NewsItem bean=new NewListBean.NewsItem();
                        if (item.children().size() > 0) {
                            bean.setTime(item.children().first().text());
                        }
                        Elements a=item.getElementsByTag("a");
                        if (a.size() > 0) {
                            bean.setContentUrl(NewsUtil.getRealUrl(a.first().attr("href"),url));
                            bean.setTitle(a.first().attr("title"));
                        }
                        bean.setFrom("外国语学院");
                        result.add(bean);
                    }
                }
            }
            newListBean.setNewsItemList(result);
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
            Elements element = document.getElementsByTag("script");
            String content;
            List<NewListBean.BannerBean> bannerList = null;
            if (element.size() > 0) {
                content = element.first().html();
                String start = "linkarr[1]";
                String end = "for(i=1;i<picarr";
                content = content.substring(content.indexOf(start), content.indexOf(end));
                List<String> list = new ArrayList<>();
                bannerList = new ArrayList<>();
                String[] array = content.split(";");
                for (String string :
                        array) {
                    if (string.contains("\"")) {
                        list.add(string.substring(string.indexOf("\"") + 1, string.lastIndexOf("\"")));
                    }
                }
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String item = list.get(i);
                    if (i % 3 == 0) {
                        NewListBean.BannerBean bean = new NewListBean.BannerBean();
                        bean.setContentUrl(NewsUtil.getRealUrl(item, url));
                        bannerList.add(bean);
                    } else {
                        if (item.endsWith(".jpg")) {
                            bannerList.get(i / 3).setThumb(NewsUtil.getRealUrl(item, url));
                        } else {
                            bannerList.get(i / 3).setTitle(item);
                        }
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
            Element tempElement=document.getElementById("picture1");
            Elements element = tempElement.getElementsByTag("script");
            String content;
            List<NewListBean.BannerBean> bannerList = null;
            if (element.size() > 0) {
                content = element.first().html();
                String end = "var";
                content = content.substring(0, content.indexOf(end));
                List<String> list = new ArrayList<>();
                bannerList = new ArrayList<>();
                String[] array = content.split(";");
                for (String string :
                        array) {
                    if (string.contains("\"")) {
                        list.add(string.substring(string.indexOf("\"") + 1, string.lastIndexOf("\"")));
                    }
                }
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    String item = list.get(i);
                    if (i % 3 == 0) {
                        NewListBean.BannerBean bean = new NewListBean.BannerBean();
                        bean.setContentUrl(NewsUtil.getRealUrl(item, url));
                        bannerList.add(bean);
                    } else {
                        if (item.endsWith(".jpg")) {
                            bannerList.get(i / 3).setThumb(NewsUtil.getRealUrl(item, url));
                        } else {
                            bannerList.get(i / 3).setTitle(item);
                        }
                    }
                }
            }
        }
    }
}
