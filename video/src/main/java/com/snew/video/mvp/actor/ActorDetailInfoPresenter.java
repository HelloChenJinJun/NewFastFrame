package com.snew.video.mvp.actor;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.snew.video.bean.ActorDetailInfoBean;
import com.snew.video.util.VideoUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/19     15:16
 */
public class ActorDetailInfoPresenter extends RxBasePresenter<IView<BaseBean>, DefaultModel> {


    public ActorDetailInfoPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getData(boolean isRefresh, String url) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        Observable.just(url).subscribeOn(Schedulers.io())
                .map(s -> {
                    Document document = Jsoup.connect(s).get();
                    ActorDetailInfoBean actorDetailInfoBean = new ActorDetailInfoBean();
                    Element img = document.select(".star_pic").first().getElementsByTag("img").first();
                    String avatar = img.attr("src");
                    if (avatar != null) {
                        if (avatar.startsWith("http")) {
                            actorDetailInfoBean.setAvatar(avatar);
                        } else {
                            actorDetailInfoBean.setAvatar("https:" + avatar);
                        }
                    }
                    actorDetailInfoBean.setName(img.attr("alt"));
                    Elements videoList = document.select(".figure_list._content_list");
                    if (videoList.size() == 0) {

                        //                        之前的：http://v.qq.com/x/star/76698
                        //                        http://v.qq.com/doki/star?dataonly=1&id=133698&fantuanid=14555
                        StringBuilder stringBuilder = new StringBuilder("http://v.qq.com/doki/star?dataonly=1&fantuanid=14555&tabid=1");
                        stringBuilder.append("&id=").append(url.substring(url.lastIndexOf("/") + 1, url.length()));
                        document = Jsoup.connect(stringBuilder.toString().trim()).get();
                        videoList = document.select(".figure_list._content_list");
                    }


                    if (videoList.size() > 0) {
                        List<ActorDetailInfoBean.ActorVideoWrappedDetailBean> wrappedDetailBeans = new ArrayList<>();
                        for (Element item :
                                videoList) {
                            if (item != null && item.children().size() > 0) {
                                ActorDetailInfoBean.ActorVideoWrappedDetailBean actorVideoWrappedDetailBean = new ActorDetailInfoBean.ActorVideoWrappedDetailBean();
                                List<ActorDetailInfoBean.ActorVideoDetailBean> beanList = new ArrayList<>();
                                for (Element child :
                                        item.children()) {
                                    ActorDetailInfoBean.ActorVideoDetailBean bean = new ActorDetailInfoBean.ActorVideoDetailBean();
                                    bean.setImage("https:" + child.getElementsByTag("img").first().attr("src"));
                                    Element element = child.getElementsByTag("a").first();
                                    bean.setTitle(element.attr("title"));
                                    bean.setUrl("https:" + element.attr("href"));
                                    beanList.add(bean);
                                    if (actorVideoWrappedDetailBean.getVideoType() == 0) {
                                        actorVideoWrappedDetailBean.setVideoType(getVideoType(child.attr("_dokistat")));
                                    }
                                }
                                actorVideoWrappedDetailBean.setActorVideoDetailBeanList(beanList);
                                wrappedDetailBeans.add(actorVideoWrappedDetailBean);
                            }
                        }
                        actorDetailInfoBean.setActorVideoWrappedDetailBeanList(wrappedDetailBeans);
                    } else {

                    }
                    return actorDetailInfoBean;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ActorDetailInfoBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(ActorDetailInfoBean actorDetailInfoBean) {
                        BaseBean baseBean = new BaseBean();
                        baseBean.setCode(200);
                        baseBean.setData(actorDetailInfoBean);
                        baseBean.setType(VideoUtil.BASE_TYPE_ACTOR_VIDEO_DATA);
                        iView.updateData(baseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });


    }

    private int getVideoType(String content) {
        if (content.equals("movie")) {
            return VideoUtil.VIDEO_TYPE_QQ_CAMERA;
        } else if (content.equals("tv")) {
            return VideoUtil.VIDEO_TYPE_QQ_TV;
        } else if (content.equals("joinArts")) {
            return VideoUtil.VIDEO_TYPE_QQ_VARIETY;
        } else if (content.equals("mv")) {
            return VideoUtil.VIDEO_TYPE_QQ_MV;
        }
        return 0;
    }
}
