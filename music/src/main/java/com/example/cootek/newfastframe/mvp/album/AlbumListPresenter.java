package com.example.cootek.newfastframe.mvp.album;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.bean.AlbumWrappedBean;
import com.example.cootek.newfastframe.bean.SingerAlbumBean;
import com.example.cootek.newfastframe.util.MusicUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     15:45
 */
public class AlbumListPresenter extends RxBasePresenter<IView<BaseBean>, DefaultModel> {
    private int page = 0;

    public AlbumListPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getAlbumListData(boolean isRefresh, String artistId) {
        StringBuilder stringBuilder = new StringBuilder(" http://music.taihe.com/data/user/getalbums?");
        if (isRefresh) {
            page = 0;
            iView.showLoading(null);
        }
        page++;
        stringBuilder.append("start=").append((page - 1) * 12).append("&size=12").append("&order=time")
                .append("&ting_uid=").append(artistId);
        //        //    http://music.taihe.com/data/user/getalbums?start=0&size=12&ting_uid=2517&order=time
        baseModel.getRepositoryManager().getApi(MusicApi.class)
                .getSingerAlbumInfo(stringBuilder.toString()).subscribeOn(Schedulers.io())
                .map(new Function<SingerAlbumBean, List<AlbumWrappedBean>>() {
                    @Override
                    public List<AlbumWrappedBean> apply(SingerAlbumBean singerAlbumBean) throws Exception {
                        Document document = Jsoup.parse(singerAlbumBean.getData().getHtml());
                        Elements elements = document.select(".album-list");
                        List<AlbumWrappedBean> list = new ArrayList<>();
                        if (elements.first() != null && elements.first().children().size() > 0) {
                            for (Element item :
                                    elements.first().children()) {
                                if (item.getElementsByTag("img").first() != null) {
                                    AlbumWrappedBean albumWrappedBean = new AlbumWrappedBean();
                                    albumWrappedBean.setImageUrl(item.getElementsByTag("img").first().attr("src"));
                                    String href = item.getElementsByTag("a").first().attr("href");
                                    albumWrappedBean.setAlbumId(href.substring(href.lastIndexOf("/") + 1, href.length()));
                                    albumWrappedBean.setSubTitle(item.select(".publishtime").text().trim());
                                    String content = item.text().trim();
                                    albumWrappedBean.setTitle(content.substring(0, content.length() - albumWrappedBean.getSubTitle().length()));
                                    list.add(albumWrappedBean);
                                }
                            }
                        }
                        return list;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AlbumWrappedBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(List<AlbumWrappedBean> o) {
                        BaseBean baseBean = new BaseBean();
                        baseBean.setData(o);
                        baseBean.setType(MusicUtil.BASE_TYPE_ALBUM_CONTENT);
                        iView.updateData(baseBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                        page--;
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }
}
