package com.example.cootek.newfastframe.mvp.search;

import android.text.TextUtils;

import com.example.commonlibrary.bean.BaseBean;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.bean.AlbumWrappedBean;
import com.example.cootek.newfastframe.bean.ArtistInfo;
import com.example.cootek.newfastframe.bean.DownLoadMusicBean;
import com.example.cootek.newfastframe.bean.SearchMusicBean;
import com.example.cootek.newfastframe.bean.SearchResultBean;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/26     9:51
 */
public class SearchMusicPresenter extends RxBasePresenter<IView<BaseBean>, DefaultModel> {
    public SearchMusicPresenter(IView<BaseBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void searchContent(String content) {
        iView.showLoading(null);
        baseModel.getRepositoryManager().getApi(MusicApi.class)
                .search(content).subscribeOn(Schedulers.io())
                .flatMap(searchMusicBean -> {
                    List<Observable> list = new ArrayList<>();
                    if (searchMusicBean.getSong() != null && searchMusicBean.getSong().size() > 0) {
                        for (int i = 0; i < searchMusicBean.getSong().size(); i++) {
                            list.add(baseModel.getRepositoryManager().getApi(MusicApi.class).getDownLoadMusicInfo(searchMusicBean.getSong().get(i).getSongid()));
                        }
                    }
                    if (searchMusicBean.getAlbum() != null && searchMusicBean.getAlbum().size() > 0) {
                        for (int i = 0; i < searchMusicBean.getAlbum().size(); i++) {
                            list.add(Observable.just(searchMusicBean.getAlbum().get(i)));
                        }
                    }
                    if (searchMusicBean.getArtist() != null && searchMusicBean.getArtist().size() > 0) {
                        for (int i = 0; i < searchMusicBean.getArtist().size(); i++) {
                            list.add(baseModel.getRepositoryManager().getApi(MusicApi.class).getArtistInfo(searchMusicBean.getArtist().get(i).getArtistid())
                                    .subscribeOn(Schedulers.io()));
                        }
                    }
                    return Observable.mergeArray(list.toArray(new Observable[]{}));
                }).toList(200)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onSuccess(List<Object> list) {
                        if (list != null && list.size() > 0) {
                            List<MusicPlayBean> songList = new ArrayList<>();
                            List<AlbumWrappedBean> albumBeans = new ArrayList<>();
                            List<ArtistInfo> artistInfoList = new ArrayList<>();
                            for (Object item :
                                    list) {
                                if (item instanceof DownLoadMusicBean) {
                                    songList.add(cover((DownLoadMusicBean) item));
                                } else if (item instanceof SearchMusicBean.AlbumBean) {
                                    albumBeans.add(cover(((SearchMusicBean.AlbumBean) item)));
                                } else {
                                    artistInfoList.add((ArtistInfo) item);
                                }
                            }
                            SearchResultBean searchResultBean = new SearchResultBean();
                            searchResultBean.setAlbumBeans(albumBeans);
                            searchResultBean.setArtistInfoList(artistInfoList);
                            searchResultBean.setMusicPlayBeanList(songList);
                            BaseBean baseBean = new BaseBean();
                            baseBean.setType(MusicUtil.BASE_TYPE_SEARCH_CONTENT);
                            baseBean.setData(searchResultBean);
                            iView.updateData(baseBean);
                        }
                        iView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), () -> searchContent(content));
                    }
                });
    }

    private AlbumWrappedBean cover(SearchMusicBean.AlbumBean item) {
        AlbumWrappedBean albumWrappedBean = new AlbumWrappedBean();
        albumWrappedBean.setAlbumId(item.getAlbumid());
        albumWrappedBean.setImageUrl(item.getArtistpic());
        albumWrappedBean.setTitle(item.getAlbumname());
        albumWrappedBean.setSubTitle(item.getArtistname());
        return albumWrappedBean;
    }

    private MusicPlayBean cover(DownLoadMusicBean downLoadMusicBean) {
        MusicPlayBean musicPlayBean = new MusicPlayBean();
        musicPlayBean.setIsLocal(false);
        musicPlayBean.setSongId(Long.parseLong(downLoadMusicBean.getSonginfo().getSong_id()));
        musicPlayBean.setAlbumId(Long.parseLong(downLoadMusicBean.getSonginfo().getAlbum_id()));
        musicPlayBean.setAlbumName(downLoadMusicBean.getSonginfo().getAlbum_title());

        if (!TextUtils.isEmpty(downLoadMusicBean.getSonginfo().getPic_huge())) {
            musicPlayBean.setAlbumUrl(downLoadMusicBean.getSonginfo().getPic_huge());
        } else if (!TextUtils.isEmpty(downLoadMusicBean.getSonginfo().getPic_premium())) {
            musicPlayBean.setAlbumUrl(downLoadMusicBean.getSonginfo().getPic_premium());
        } else if (!TextUtils.isEmpty(downLoadMusicBean.getSonginfo().getPic_big())) {
            musicPlayBean.setAlbumUrl(downLoadMusicBean.getSonginfo().getPic_big());
        } else if (!TextUtils.isEmpty(downLoadMusicBean.getSonginfo().getPic_small())) {
            musicPlayBean.setAlbumUrl(downLoadMusicBean.getSonginfo().getPic_small());
        } else if (!TextUtils.isEmpty(downLoadMusicBean.getSonginfo().getPic_radio())) {
            musicPlayBean.setAlbumUrl(downLoadMusicBean.getSonginfo().getPic_radio());
        }
        musicPlayBean.setArtistId(downLoadMusicBean.getSonginfo().getArtist_id());
        musicPlayBean.setArtistName(downLoadMusicBean.getSonginfo().getAuthor());
        musicPlayBean.setLrcUrl(downLoadMusicBean.getSonginfo().getLrclink());
        musicPlayBean.setSongUrl(downLoadMusicBean.getBitrate().getFile_link());
        musicPlayBean.setSongName(downLoadMusicBean.getSonginfo().getTitle());
        musicPlayBean.setTingId(downLoadMusicBean.getSonginfo().getTing_uid());
        CommonLogger.e(downLoadMusicBean.toString());
        return musicPlayBean;
    }
}
