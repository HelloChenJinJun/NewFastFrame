package com.snew.video.mvp.preview;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.snew.video.bean.QQVideoListBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2019/1/9     16:13
 */
public class PreViewVideoPresenter extends RxBasePresenter<IView<BaseBean>, DefaultModel> {
    public PreViewVideoPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getPreVideoData() {
        Observable.just("http://v.sigu.me/first.php?page=1")
                .subscribeOn(Schedulers.io())
                .flatMap((Function<String, ObservableSource<List<QQVideoListBean.JsonvalueBean.ResultsBean>>>) s -> {
                    Document document = Jsoup.connect(s).get();
                    Element element = document.select(".tv-list.clearfix").first();
                    List<QQVideoListBean.JsonvalueBean.ResultsBean> list = new ArrayList<>();
                    if (element.children().size() > 0) {
                        for (Element item :
                                element.children()) {
                            Element tag = item.getElementsByTag("img").first();
                            if (tag != null) {
                                QQVideoListBean.JsonvalueBean.ResultsBean videoBean = new QQVideoListBean.JsonvalueBean.ResultsBean();
                                QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean fieldsBean=new QQVideoListBean.JsonvalueBean.ResultsBean.FieldsBean();
                                fieldsBean.setVertical_pic_url(tag.attr("data-src").trim());
                                Element a = item.getElementsByTag("a").first();
                                if (a != null) {
                                    videoBean.setId("http://v.sigu.me/" + a.attr("href").trim());
                                    fieldsBean.setTitle(a.attr("title"));
                                    videoBean.setFields(fieldsBean);
                                    list.add(videoBean);
                                }
                            }
                        }
                    }
                    return Observable.just(list);
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<QQVideoListBean.JsonvalueBean.ResultsBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(List<QQVideoListBean.JsonvalueBean.ResultsBean> videoBean) {
                        BaseBean baseBean = new BaseBean();
                        baseBean.setCode(200);
                        baseBean.setData(videoBean);
                        iView.updateData(baseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseBean baseBean = new BaseBean();
                        baseBean.setCode(-1);
                        baseBean.setDesc(e.getMessage());
                        iView.updateData(baseBean);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }
}
