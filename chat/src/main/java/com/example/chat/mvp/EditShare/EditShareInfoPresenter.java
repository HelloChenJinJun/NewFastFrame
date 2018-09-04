package com.example.chat.mvp.EditShare;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.listener.OnCreatePublicPostListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.AppUtil;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.UUID;

import rx.Subscription;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     20:48
 * QQ:         1981367757
 */

public class EditShareInfoPresenter extends AppBasePresenter<IView<PublicPostBean>,DefaultModel>{
    public EditShareInfoPresenter(IView<PublicPostBean> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void updatePublicPostBean(PublicPostBean publicPostBean) {
        baseModel.getRepositoryManager()
                .getDaoSession()
                .getPublicPostEntityDao()
                .update(MsgManager.getInstance().cover(publicPostBean));
        iView.updateData(publicPostBean);
    }

    public void sendPublicPostBean(int type, String content, ArrayList<ImageItem> imageList, String videoPath, String thumbImage, PublicPostBean bean, String location) {
        Subscription subscription = MsgManager.getInstance().sendPublicPostMessage(type, content, location, imageList, videoPath, thumbImage, bean,
                 new OnCreatePublicPostListener() {

                     @Override
                     public void onSuccess(PublicPostBean publicPostBean) {
                         refreshData(publicPostBean);
                     }

                     @Override
                     public void onFailed(String errorMsg, int errorCode, PublicPostBean publicPostBean) {
                         refreshData(publicPostBean);
                     }
                 });
        if (subscription != null) {
            addSubscription(subscription);
        }
    }

    private void refreshData(PublicPostBean bean) {
        if (!AppUtil.isNetworkAvailable()) {
            CommonLogger.e("无网情况下出错,离线发布");
            ToastUtils.showLongToast("离线发布");
        }
        bean.setSendStatus(Constant.SEND_STATUS_FAILED);
        bean.setObjectId(UUID.randomUUID().toString());
        bean.setCreateTime(TimeUtil.getTime(TimeUtil.localToServerTime(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss"));
        bean.setUpdatedAt(TimeUtil.getTime(TimeUtil.localToServerTime(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss"));
        UserDBManager.getInstance()
                .addOrUpdatePost(bean);
        iView.updateData(bean);
    }
}
