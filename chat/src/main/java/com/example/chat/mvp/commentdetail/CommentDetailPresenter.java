package com.example.chat.mvp.commentdetail;

import com.example.chat.bean.User;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.ReplyDetailContent;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.google.gson.Gson;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     16:15
 * QQ:         1981367757
 */

public class CommentDetailPresenter extends BasePresenter<IView<List<ReplyDetailContent>>,DefaultModel>{
    private Gson gson;

    public CommentDetailPresenter(IView<List<ReplyDetailContent>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
        gson = BaseApplication.getAppComponent().getGson();
    }

    public void getCommentListDetailData(PublicCommentBean bean ,final boolean isRefresh) {
        if (isRefresh) {
            iView.showLoading("正在加载数据.........");
        }
        CommentDetailBean commentDetailBean=gson.fromJson(bean.getContent(),CommentDetailBean.class);
        String[] uidList = commentDetailBean.getPublicId().split("&");
        String replyUid;
        if (uidList[0].equals(bean.getUser().getObjectId())) {
            replyUid = uidList[1];
        } else {
            replyUid = uidList[0];
        }
        UserManager.getInstance().findUserById(replyUid, new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    iView.updateData(commentDetailBean.getConversationList()
                            .get(commentDetailBean.getPublicId()));
                    iView.hideLoading();
                }else {
                    iView.showError(e.toString(), () -> getCommentListDetailData(bean, isRefresh));
                }
            }
        });
    }
}
