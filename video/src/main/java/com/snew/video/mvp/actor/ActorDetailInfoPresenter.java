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
                    Element movie = videoList.get(0);
                    if (movie != null && movie.children().size() > 0) {
                        List<ActorDetailInfoBean.ActorVideoDetailBean> beanList = new ArrayList<>();
                        for (Element child :
                                movie.children()) {
                            ActorDetailInfoBean.ActorVideoDetailBean item = new ActorDetailInfoBean.ActorVideoDetailBean();
                            item.setImage("https:" + child.getElementsByTag("img").first().attr("src"));
                            Element element = child.getElementsByTag("a").first();
                            item.setTitle(element.attr("title"));
                            item.setUrl("https:" + element.attr("href"));
                            beanList.add(item);
                        }
                        actorDetailInfoBean.setActorVideoDetailBeans(beanList);
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
}
