package com.example.cootek.newfastframe.mvp.recent;

import com.example.commonlibrary.bean.music.MusicPlayBean;
import com.example.commonlibrary.bean.music.MusicPlayBeanDao;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

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

    public void getData(boolean isRefresh) {
        if (isRefresh) {
            page = 0;
            iView.showLoading(null);
        }
        page++;
        List<MusicPlayBean> list = baseModel.getRepositoryManager().getDaoSession().getMusicPlayBeanDao()
                .queryBuilder().where(MusicPlayBeanDao.Properties.UpdateTime.notEq(0)).orderDesc(MusicPlayBeanDao.Properties.UpdateTime)
                .offset(10 * (page - 1)).limit(10).list();
        if (list.size() > 0) {
            iView.updateData(list);
        } else {
            iView.updateData(null);
            page--;
        }
        iView.hideLoading();

    }
}
