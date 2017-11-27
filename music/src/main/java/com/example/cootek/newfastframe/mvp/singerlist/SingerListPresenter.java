package com.example.cootek.newfastframe.mvp.singerlist;

import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.cootek.newfastframe.MusicInfoProvider;
import com.example.cootek.newfastframe.bean.ArtistInfo;
import com.example.cootek.newfastframe.api.MusicApi;
import com.example.commonlibrary.bean.music.SingerListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by COOTEK on 2017/9/2.
 */

public class SingerListPresenter extends BasePresenter<IView<Object>, SingerListModel> {
    public SingerListPresenter(IView<Object> iView, SingerListModel baseModel) {
        super(iView, baseModel);
    }


    public void getSingerListData() {
        MusicInfoProvider.getAllMusic(false).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MusicPlayBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull List<MusicPlayBean> musicPlayBeen) {
                        if (musicPlayBeen != null) {
                            Map<String, SingerListBean> result = new HashMap<>();
                            for (MusicPlayBean bean :
                                    musicPlayBeen) {
                                CommonLogger.e(bean.toString());
                                if (bean.getArtistId() == null) {
                                    continue;
                                }
                                if (result.containsKey(bean.getArtistId())) {
                                    result.get(bean.getArtistId()).setCount(result.get(bean.getArtistId()).getCount() + 1);
                                    continue;
                                }
                                SingerListBean singerListBean = new SingerListBean();
                                singerListBean.setArtistId(bean.getArtistId());
                                singerListBean.setName(bean.getArtistName());
                                singerListBean.setTingId(bean.getTingId());
                                singerListBean.setCount(1);
                                result.put(bean.getArtistId(), singerListBean);
                                getArtistInfo(singerListBean);
                            }
                            iView.updateData(new ArrayList<>(result.values()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                            @Override
                            public void onRetry() {
                                getSingerListData();
                            }
                        });
                    }

                    @Override
                    public void onComplete() {
                        iView.hideLoading();
                    }
                });
    }

    private void getArtistInfo(final SingerListBean singerListBean) {
        baseModel.getRepositoryManager().getApi(MusicApi.class).getArtistInfo(singerListBean.getTingId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDispose(d);
                    }

                    @Override
                    public void onNext(@NonNull ArtistInfo artistInfo) {
                        singerListBean.setAvatar(artistInfo.getAvatar_s180());
                        singerListBean.setCompany(artistInfo.getCompany());
                        singerListBean.setBirthDay(artistInfo.getBirth());
                        singerListBean.setFirstChat(artistInfo.getFirstchar());
                        singerListBean.setInfo(artistInfo.getIntro());
                        singerListBean.setSex("0".equals(artistInfo.getGender()));
                        CommonLogger.e("歌手信息" + singerListBean.toString());
                        baseModel.getRepositoryManager().getDaoSession().getSingerListBeanDao()
                                .insertOrReplace(singerListBean);
                        iView.updateData(singerListBean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
