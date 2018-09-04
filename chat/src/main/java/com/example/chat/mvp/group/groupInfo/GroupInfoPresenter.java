package com.example.chat.mvp.group.groupInfo;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.GroupTableMessage;
import com.example.chat.events.GroupTableEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.bean.chat.GroupTableEntity;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/30     10:44
 * QQ:         1981367757
 */

public class GroupInfoPresenter extends AppBasePresenter<IView<Object>,DefaultModel>{
    public GroupInfoPresenter(IView<Object> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void exitGroup(String groupId,String uid) {
        iView.showLoading("正在退群中 。。。。。。。。。");
        MsgManager.getInstance().exitGroup(groupId,uid,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                iView.hideLoading();
                if (e == null) {
                    ToastUtils.showShortToast("退群成功");
                    RxBusManager
                            .getInstance()
                            .post(new GroupTableEvent(groupId,GroupTableEvent.TYPE_GROUP_NUMBER
                            ,GroupTableEvent.ACTION_DELETE,uid));
                    iView.updateData(groupId);
                }else {
                    ToastUtils.showShortToast("退群失败");
                }
            }
        });
        iView.showLoading("正在退群中..........");

    }
}
