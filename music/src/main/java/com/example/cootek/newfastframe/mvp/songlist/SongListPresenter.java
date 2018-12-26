package com.example.cootek.newfastframe.mvp.songlist;

import android.text.TextUtils;

import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.bean.AlbumBean;
import com.example.cootek.newfastframe.bean.ArtistSongsBean;
import com.example.cootek.newfastframe.bean.DownLoadMusicBean;
import com.example.cootek.newfastframe.bean.RankListBean;
import com.example.cootek.newfastframe.bean.RelatedSongBean;
import com.example.cootek.newfastframe.bean.SongMenuBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class SongListPresenter extends BasePresenter<IView<Object>, DefaultModel> {
    private int num = 0;

    public SongListPresenter(IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
        num = 0;
    }


    public void getRankDetailInfo(final int type, final boolean isRefresh) {
        if (isRefresh) {
            num = 0;
        }
        if (isRefresh) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class).getRankList(type, 15, (num - 1) * 15)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(rankListBean -> {
            iView.updateData(rankListBean);
            if (rankListBean.getError_code() != 22000 || rankListBean.getSong_list() == null) {
                num--;
            }
        }).flatMap((Function<RankListBean, ObservableSource<DownLoadMusicBean>>) rankListBean -> {
            if (rankListBean.getError_code() == 22000 && rankListBean.getSong_list() != null) {
                List<Observable<DownLoadMusicBean>> list = new ArrayList<>();
                for (int i = 0; i < rankListBean.getSong_list().size(); i++) {
                    list.add(baseModel.getRepositoryManager().getApi(MusicApi.class).getDownLoadMusicInfo(rankListBean.getSong_list().get(i).getSong_id())
                            .subscribeOn(Schedulers.io()));
                }
                return Observable.mergeArray(list.toArray(new Observable[]{}));
            } else {
                return null;
            }
        }).map(downLoadMusicBean -> {
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
        }).toList(15)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MusicPlayBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onSuccess(List<MusicPlayBean> musicPlayBeans) {
                        iView.updateData(musicPlayBeans);
                        getBaseModel().getRepositoryManager().getDaoSession()
                                .getMusicPlayBeanDao().insertOrReplaceInTx(musicPlayBeans);
                        iView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                        num--;
                    }
                });
    }


    public void getSongMenuData(final String listId, final boolean isRefresh, final boolean isShowLoading) {
        if (isRefresh) {
            num = 0;
        }
        if (isShowLoading) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class).getSongMenuData(listId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongMenuBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull SongMenuBean songMenuBean) {
                        if (songMenuBean != null && songMenuBean.getError_code() == 22000) {
                            iView.updateData(songMenuBean);
                            for (SongMenuBean.ContentBean contentBean :
                                    songMenuBean.getContent()) {
                                //                                getMusicDetailInfo(contentBean.getSong_id());
                            }
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(e.getMessage(), null);
                        num--;
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });

    }


    public void getAlbumInfoData(final String albumId, final boolean isRefresh) {
        if (isRefresh) {
            num = 0;
        }
        if (isRefresh) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class).getAlbumData(albumId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(albumBean -> {
                    if (albumBean.getSonglist() == null || albumBean.getSonglist().size() == 0) {
                        num--;
                    }
                    iView.updateData(albumBean);
                })
                .flatMap((Function<AlbumBean, ObservableSource<DownLoadMusicBean>>) albumBean -> {
                    if (albumBean.getSonglist() != null && albumBean.getSonglist().size() > 0) {
                        List<Observable<DownLoadMusicBean>> list = new ArrayList<>();
                        for (int i = 0; i < albumBean.getSonglist().size(); i++) {
                            list.add(baseModel.getRepositoryManager().getApi(MusicApi.class).getDownLoadMusicInfo(albumBean.getSonglist().get(i).getSong_id())
                                    .subscribeOn(Schedulers.io()));
                        }
                        return Observable.mergeArray(list.toArray(new Observable[]{}));
                    } else {
                        return null;
                    }
                }).map(downLoadMusicBean -> {
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
        }).toList(20)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MusicPlayBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onSuccess(List<MusicPlayBean> musicPlayBeans) {
                        iView.updateData(musicPlayBeans);
                        getBaseModel().getRepositoryManager().getDaoSession()
                                .getMusicPlayBeanDao().insertOrReplaceInTx(musicPlayBeans);
                        iView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iView.showError(e.getMessage(), null);
                        num--;
                    }
                });

    }

    public void getRecommendData(String songId, boolean isRefresh) {
        if (isRefresh) {
            num = 0;
        }
        if (isRefresh) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class)
                .getReCommendSongList(songId, 20).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnNext(artistSongsBean -> {
            iView.updateData(artistSongsBean);
            if (artistSongsBean.getError_code() != 22000 || artistSongsBean.getResult() == null
                    || artistSongsBean.getResult().getList() == null || artistSongsBean.getResult()
                    .getList().size() == 0) {
                num--;
            }
        })
                .flatMap((Function<RelatedSongBean, ObservableSource<DownLoadMusicBean>>) relatedSongBean -> {
                    if (relatedSongBean.getResult() != null && relatedSongBean.getResult().getList() != null && relatedSongBean
                            .getResult().getList().size() > 0) {
                        List<Observable<DownLoadMusicBean>> list = new ArrayList<>();
                        for (int i = 0; i < relatedSongBean.getResult().getList().size(); i++) {
                            list.add(baseModel.getRepositoryManager().getApi(MusicApi.class).getDownLoadMusicInfo(relatedSongBean.getResult().getList().get(i).getSong_id())
                                    .subscribeOn(Schedulers.io()));
                        }
                        return Observable.mergeArray(list.toArray(new Observable[]{}));
                    } else {
                        return null;
                    }
                }).map(downLoadMusicBean -> {
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
        }).toList(20).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicPlayBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onSuccess(List<MusicPlayBean> musicPlayBeans) {
                iView.updateData(musicPlayBeans);
                getBaseModel().getRepositoryManager().getDaoSession()
                        .getMusicPlayBeanDao().insertOrReplaceInTx(musicPlayBeans);
                iView.hideLoading();
            }


            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage(), null);
                num--;
            }

        });
    }

    public void getSingerMusicData(String uid, boolean isRefresh) {
        if (isRefresh) {
            num = 0;
        }
        if (isRefresh) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class)
                .getArtistSongs(uid, (num - 1) * 10, 10)
                .subscribeOn(Schedulers.io())
                .flatMap((Function<ArtistSongsBean, ObservableSource<DownLoadMusicBean>>) artistSongsBean -> {
                    if (artistSongsBean.getSonglist() != null && artistSongsBean.getSonglist().size() > 0) {
                        List<Observable<DownLoadMusicBean>> list = new ArrayList<>();
                        for (int i = 0; i < artistSongsBean.getSonglist().size(); i++) {
                            list.add(baseModel.getRepositoryManager().getApi(MusicApi.class).getDownLoadMusicInfo(artistSongsBean.getSonglist().get(i).getSong_id())
                                    .subscribeOn(Schedulers.io()));
                        }
                        return Observable.mergeArray(list.toArray(new Observable[]{}));
                    } else {
                        return null;
                    }
                }).map(downLoadMusicBean -> {
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
        }).toList(20).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicPlayBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDispose(d);
            }

            @Override
            public void onSuccess(List<MusicPlayBean> musicPlayBeans) {
                iView.updateData(musicPlayBeans);
                getBaseModel().getRepositoryManager().getDaoSession()
                        .getMusicPlayBeanDao().insertOrReplaceInTx(musicPlayBeans);
                iView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage(), null);
                num--;
            }
        });
    }
}
