package com.example.chat.mvp.commentlist;

import com.example.chat.bean.PublicPostBean;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.ReplyCommentListBean;
import com.example.chat.bean.post.ReplyDetailContent;
import com.example.chat.events.CommentEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:10
 * QQ:         1981367757
 */

public class CommentListPresenter extends RxBasePresenter<IView<List<PublicCommentBean>>, CommentListModel> {
    private Gson gson;


    public CommentListPresenter(IView<List<PublicCommentBean>> iView, CommentListModel baseModel) {
        super(iView, baseModel);
        gson = BaseApplication.getAppComponent().getGson();
    }

    public void getCommentListData(final String postId, final boolean isRefresh, final String time) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        MsgManager
                .getInstance().getCommentListData(postId, new FindListener<PublicCommentBean>() {
            @Override
            public void done(List<PublicCommentBean> list, BmobException e) {
                if (e == null||e.getErrorCode()==101) {
                    iView.updateData(list);
                    iView.hideLoading();
                } else {
                    iView.showError(null, new EmptyLayout.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            getCommentListData(postId, isRefresh,time);
                        }
                    });
                }
            }
        }, isRefresh, time);

    }







    public void sendCommentData(final PublicCommentBean publicCommentBean, final String postId, final String content) {
        final CommentDetailBean commentDetailBean = new CommentDetailBean();
        commentDetailBean.setContent(content);
        if (publicCommentBean != null) {
            CommentDetailBean originBean = gson.fromJson(publicCommentBean.getContent(), CommentDetailBean.class);
            commentDetailBean.setPublicId(originBean.getPublicId());
            commentDetailBean.setReplyAvatar(publicCommentBean.getUser().getAvatar());
            commentDetailBean.setReplyName(publicCommentBean.getUser().getNick());
            commentDetailBean.setReplyContent(originBean.getContent());
        }
        final PublicCommentBean newBean = new PublicCommentBean();
        newBean.setContent(gson.toJson(commentDetailBean));
        newBean.setUser(UserManager.getInstance().getCurrentUser());
        PublicPostBean publicPostBean = new PublicPostBean();
        publicPostBean.setObjectId(postId);
        newBean.setPost(publicPostBean);
        newBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null&&commentDetailBean.getPublicId() != null) {
                    ReplyCommentListBean replyCommentListBean=new ReplyCommentListBean();
                    replyCommentListBean.setPublicId(commentDetailBean.getPublicId());
                    ReplyDetailContent replyDetailContent=new ReplyDetailContent();
                    replyDetailContent.setContent(content);
                    replyDetailContent.setTime(System.currentTimeMillis());
                    replyCommentListBean.setContent(gson.toJson(replyDetailContent));
                    replyCommentListBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                List<PublicCommentBean> publicCommentBeans=new ArrayList<>();
                                publicCommentBeans.add(newBean);
                                iView.updateData(publicCommentBeans);
                                iView.hideLoading();
                                PublicPostBean item=new PublicPostBean();
                                item.increment("commentCount");
                                item.update(postId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        RxBusManager.getInstance().post(new CommentEvent(postId,CommentEvent.TYPE_COMMENT));
                                        ToastUtils.showShortToast("评论成功");
                                    }
                                });
                            }else {
                                iView.showError(null, new EmptyLayout.OnRetryListener() {
                                    @Override
                                    public void onRetry() {
                                        sendCommentData(publicCommentBean, postId, content);
                                    }
                                });
                            }
                        }
                    });
                }else if (e!=null){
                    iView.showError(null, new EmptyLayout.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            sendCommentData(publicCommentBean, postId, content);
                        }
                    });
                }else {
                    List<PublicCommentBean> publicCommentBeans=new ArrayList<>();
                    publicCommentBeans.add(newBean);
                    iView.updateData(publicCommentBeans);
                    iView.hideLoading();
                    PublicPostBean item=new PublicPostBean();
                    item.increment("commentCount");
                    item.update(postId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            RxBusManager.getInstance().post(new CommentEvent(postId,CommentEvent.TYPE_COMMENT));
                            ToastUtils.showShortToast("评论成功");
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
