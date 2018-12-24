package com.example.chat.mvp.feedback;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.ChatBean;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名称:    SecondhandMarket
 * 创建人:      李晨
 * 创建时间:    2018/4/26     17:09
 * QQ:         1981367757
 */

public class ChatListPresenter extends AppBasePresenter<IView<List<ChatBean>>, DefaultModel> {
    public ChatListPresenter(IView<List<ChatBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getData() {
        iView.showLoading(null);
        String uid = UserManager.getInstance().getCurrentUserObjectId();
        BmobQuery<ChatBean> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("uid", uid);
        List<BmobQuery<ChatBean>> list = new ArrayList<>();
        list.add(bmobQuery);
        BmobQuery<ChatBean> other = new BmobQuery<>();
        other.addWhereEqualTo("toId", uid);
        list.add(other);
        BmobQuery<ChatBean> mainQuery = new BmobQuery<>();
        mainQuery.or(list);
        addSubscription(mainQuery.findObjects(new FindListener<ChatBean>() {
            @Override
            public void done(List<ChatBean> list, BmobException e) {
                if (e == null) {
                    iView.updateData(list);
                } else {
                    ToastUtils.showShortToast("获取留言失败" + e.toString());
                }
                iView.hideLoading();
            }
        }));
    }

    public void sendChatBean(String content) {
        ChatBean chatBean = new ChatBean();
        chatBean.setUid(UserManager.getInstance().getCurrentUserObjectId());
        chatBean.setContent(content);
        chatBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                iView.hideLoading();
                if (e == null) {
                    RxBusManager.getInstance().post(chatBean);
                } else {
                    ToastUtils.showShortToast("上传信息失败" + e.toString());
                }
            }
        });
    }
}
