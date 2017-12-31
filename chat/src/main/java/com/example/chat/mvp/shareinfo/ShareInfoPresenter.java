package com.example.chat.mvp.shareinfo;

import com.example.chat.bean.PublicPostBean;
import com.example.chat.manager.MsgManager;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     17:19
 * QQ:         1981367757
 */

public class ShareInfoPresenter extends RxBasePresenter<IView<List<PublicPostBean>>, ShareInfoModel> {
    public ShareInfoPresenter(IView<List<PublicPostBean>> iView, ShareInfoModel baseModel) {
        super(iView, baseModel);
    }

    public void getAllPostData(final boolean isRefresh, final String time) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        MsgManager
                .getInstance().getAllPostData(isRefresh, time, new FindListener<PublicPostBean>() {
            @Override
            public void onSuccess(List<PublicPostBean> list) {
                iView.updateData(list);
                iView.hideLoading();
            }

            @Override
            public void onError(int i, String s) {
                if (i == 101) {
                    iView.updateData(null);
                    iView.hideLoading();
                }else {
                    iView.showError(s + i, new EmptyLayout.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            getAllPostData(isRefresh, time);
                        }
                    });
                }
            }
        });

    }
}
