package com.example.cootek.newfastframe.mvp.recent;

import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.cootek.newfastframe.MusicInfoProvider;
import com.example.cootek.newfastframe.util.MusicUtil;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/12/11     13:54
 */
public class RecentPlayListPresenter extends RxBasePresenter<IView<List<MusicPlayBean>>, DefaultModel> {
    private int page = 0;

    public RecentPlayListPresenter(IView<List<MusicPlayBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getData(boolean isRefresh, int from) {
        if (isRefresh) {
            page = 0;
            iView.showLoading(null);
        }
        page++;
        if (from == MusicUtil.FROM_RECENT) {
            List<MusicPlayBean> list;
            list = baseModel.getRepositoryManager().getDaoSession().getMusicPlayBeanDao()
                    .queryBuilder().where(MusicPlayBeanDao.Properties.UpdateTime.notEq(0)).orderDesc(MusicPlayBeanDao.Properties.UpdateTime)
                    .offset(10 * (page - 1)).limit(10).list();
            if (list.size() > 0) {
                iView.updateData(list);
            } else {
                iView.updateData(null);
                page--;
            }
            iView.hideLoading();
        } else {
            MusicInfoProvider.getAllMusic(true).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<MusicPlayBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDispose(d);
                        }

                        @Override
                        public void onNext(List<MusicPlayBean> musicPlayBeans) {
                            iView.updateData(musicPlayBeans);
                            if (musicPlayBeans == null || musicPlayBeans.size() == 0) {
                                page--;
                            }
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
}
