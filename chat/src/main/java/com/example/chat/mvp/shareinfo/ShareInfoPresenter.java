package com.example.chat.mvp.shareinfo;

import com.example.chat.base.Constant;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.events.CommentEvent;
import com.example.chat.manager.MsgManager;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

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
            public void done(List<PublicPostBean> list, BmobException e) {
                if (e == null||e.getErrorCode()==101) {
                    iView.updateData(list);
                    iView.hideLoading();
                    if (isRefresh&&!time.equals("0000-00-00 01:00:00")){
                        BaseApplication
                                .getAppComponent()
                                .getSharedPreferences()
                                .edit()
                                .putString(Constant.UPDATE_TIME_SHARE,time)
                        .apply();
                    }
                }else {
                    iView.showError(e.toString(), new EmptyLayout.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            getAllPostData(isRefresh, time);
                        }
                    });
                }
            }
        });

    }

    public void addLike(final String objectId) {
        PublicPostBean publicPostBean=new PublicPostBean();
        publicPostBean.increment("likeCount");
        publicPostBean.update(objectId,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    RxBusManager
                            .getInstance().post(new CommentEvent(objectId,CommentEvent.TYPE_LIKE));
                    ToastUtils.showShortToast("点赞成功");
                }else {
                    ToastUtils.showShortToast("点赞失败");
                }
            }
        });
    }
}
