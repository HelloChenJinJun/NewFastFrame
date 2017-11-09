package com.example.cootek.newfastframe.mvp.songlist;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.music.SingerListBean;
import com.example.commonlibrary.bean.music.SingerListBeanDao;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.cootek.newfastframe.bean.AlbumBean;
import com.example.cootek.newfastframe.bean.ArtistSongsBean;
import com.example.cootek.newfastframe.bean.DownLoadMusicBean;
import com.example.cootek.newfastframe.bean.RankListBean;
import com.example.cootek.newfastframe.bean.SongMenuBean;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/8/16.
 */

public class SongListPresenter extends BasePresenter<IView<Object>, SongListModel> {
    private int num = 0;

    public SongListPresenter(IView<Object> iView, SongListModel baseModel) {
        super(iView, baseModel);
        num = 0;
    }


    public void getRankDetailInfo(final int type, final boolean isRefresh, final boolean isShowLoading) {
        if (isRefresh) {
            num = 0;
        }
        if (isShowLoading) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class).getRankList(type, 15, (num - 1) * 15)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankListBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        CommonLogger.e("onSubscribe");
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull RankListBean rankListBean) {
                        CommonLogger.e("onNext");
                        if (rankListBean.getError_code() == 22000) {
                            if (rankListBean.getSong_list() != null) {
                                for (RankListBean.SongListBean songLitBean :
                                        rankListBean.getSong_list()) {
                                    getMusicDetailInfo(songLitBean.getSong_id());
                                }
                            }
                            iView.updateData(rankListBean);
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                num--;
                                getRankDetailInfo(type, isRefresh, isShowLoading);
                            }
                        });
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                        CommonLogger.e("onComplete");
                    }
                });
    }


    private void getMusicDetailInfo(String songId) {
        baseModel.getRepositoryManager().getApi(MusicApi.class).getDownLoadMusicInfo(songId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownLoadMusicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull DownLoadMusicBean downLoadMusicBean) {
                        if (downLoadMusicBean == null) {
                            CommonLogger.e("为空拉拉阿拉");
                        }
                        if (downLoadMusicBean != null) {
                            iView.updateData(downLoadMusicBean);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        CommonLogger.e(e);
                    }

                    @Override
                    public void onComplete() {

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
                                getMusicDetailInfo(contentBean.getSong_id());
                            }
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                num--;
                                getSongMenuData(listId, isRefresh, isShowLoading);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });

    }


    public void getAlbumInfoData(final String albumId, final boolean isRefresh, final boolean isShowLoading) {
        if (isRefresh) {
            num = 0;
        }
        if (isShowLoading) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class).getAlbumData(albumId)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AlbumBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull AlbumBean albumBean) {
                        if (albumBean != null && albumBean.getSonglist() != null) {
                            iView.updateData(albumBean);
                            for (AlbumBean.SonglistBean bean :
                                    albumBean.getSonglist()) {
                                getMusicDetailInfo(bean.getSong_id());
                            }
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                num--;
                                getAlbumInfoData(albumId, isRefresh, isShowLoading);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });

    }

    public void getSingerSongs(final String tingId, final boolean isRefresh, final boolean isShowLoading) {
        if (isRefresh) {
            num = 0;
        }
        if (isShowLoading) {
            iView.showLoading("");
        }
        num++;
        baseModel.getRepositoryManager().getApi(MusicApi.class).getArtistSongs(tingId, (num - 1) * 10, 10)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistSongsBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ArtistSongsBean artistSongsBean) {
                        if (artistSongsBean != null && artistSongsBean.getSonglist() != null) {
                            List<SingerListBean> result = baseModel.getRepositoryManager().getDaoSession().getSingerListBeanDao()
                                    .queryBuilder().where(SingerListBeanDao.Properties.TingId.eq(tingId)).list();
                            if (result.size() > 0) {
//                                更新歌手信息
                                iView.updateData(result.get(0));
                            }
//                            更新歌手歌曲信息
                            for (ArtistSongsBean.SonglistBean bean :
                                    artistSongsBean.getSonglist()) {
                                getMusicDetailInfo(bean.getSong_id());
                            }
                        } else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                num--;
                                getSingerSongs(tingId, isRefresh, isShowLoading);
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }
}
