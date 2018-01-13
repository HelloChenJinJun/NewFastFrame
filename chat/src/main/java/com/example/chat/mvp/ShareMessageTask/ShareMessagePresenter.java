package com.example.chat.mvp.ShareMessageTask;


import com.example.chat.bean.SharedMessage;
import com.example.chat.bean.User;
import com.example.chat.listener.AddShareMessageCallBack;
import com.example.chat.listener.DealCommentMsgCallBack;
import com.example.chat.listener.DealMessageCallBack;
import com.example.chat.listener.LoadShareMessageCallBack;
import com.example.chat.util.LogUtil;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.rxbus.RxBusManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/2      20:26
 * QQ:             1981367757
 */

public class ShareMessagePresenter extends ShareMessageContacts.Presenter {



        private Set<Subscription> subscriptionList;

        public ShareMessagePresenter(ShareMessageContacts.View iView, ShareMessageContacts.Model baseModel) {
                super(iView, baseModel);
                subscriptionList=new HashSet<>();
        }

        @Override
        public void addShareMessage(final SharedMessage shareMessage) {
                iView.showLoading("正在发送说说......请稍候.........");
             Subscription subscription=   baseModel.addShareMessage(shareMessage, new AddShareMessageCallBack() {
                        @Override
                        public void onSuccess(SharedMessage shareMessage) {
                                iView.hideLoading();
                                iView.updateShareMessageAdded(shareMessage);
                        }

                        @Override
                        public void onFailed(int errorId, String errorMsg) {
                                iView.hideLoading();
                                iView.showError("发送说说消息失败" + errorMsg + errorId, null);
                        }
                });
             subscriptionList.add(subscription);
        }

        @Override
        public void deleteShareMessage(String id) {
                iView.showLoading("正在删除说说.......请稍后.........");
              Subscription subscription=  baseModel.deleteShareMessage(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                iView.hideLoading();
                                iView.updateShareMessageDeleted(id);
                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                iView.hideLoading();
                                iView.showError("删除说说(ObjectId：" + id + ")消息失败" + errorMsg + errorId, null);
                        }
                });
                subscriptionList.add(subscription);

        }

        @Override
        public void addLiker(String id) {
//                这里的id为说说的id
               Subscription subscription= baseModel.addLiker(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                iView.updateLikerAdd(id);
                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                iView.showError("添加点赞消息失败" + errorMsg + errorId, null);
                        }
                });
                subscriptionList.add(subscription);
        }

        @Override
        public void deleteLiker(String id) {
              Subscription subscription=  baseModel.deleteLiker(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                iView.updateLikerDeleted(id);

                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                iView.showError("删除点赞失败" + errorMsg + errorId, null);
                        }
                });
                subscriptionList.add(subscription);

        }

        @Override
        public void addComment(String id, String content) {
                iView.showLoading("正在添加评论.....");
              Subscription subscription=  baseModel.addComment(id, content, new DealCommentMsgCallBack() {
                        @Override
                        public void onSuccess(String shareMessageId, String content, int position) {
                                iView.hideLoading();
                                iView.updateCommentAdded(shareMessageId, content, position);
                        }

                        @Override
                        public void onFailed(String shareMessageId, String content, int position, int errorId, String errorMsg) {
                                iView.hideLoading();
                                iView.showError("添加评论消息失败:位置为" + position + errorMsg + errorId, null);
                        }
                });
                subscriptionList.add(subscription);

        }

        @Override
        public void deleteComment(String id, int position) {
                iView.showLoading("正在删除评论");
               Subscription subscription= baseModel.deleteComment(id, position, new DealCommentMsgCallBack() {
                        @Override
                        public void onSuccess(String shareMessageId, String content, int position) {
                                iView.hideLoading();
                                LogUtil.e("删除评论成功");
                                iView.updateCommentDeleted(shareMessageId, content, position);
                        }

                        @Override
                        public void onFailed(String shareMessageId, String content, int position, int errorId, String errorMsg) {
                                iView.hideLoading();
                                LogUtil.e("删除评论失败");
                                LogUtil.e("评论的内容是" + content);
                                iView.showError("删除评论失败:位置为" + position + errorMsg + errorId, null);
                        }
                });
                subscriptionList.add(subscription);
        }

        @Override
        public void loadAllShareMessages(final boolean isPullRefresh, String time) {
                LogUtil.e("加载所有的说说消息");
//                mView.showLoading("正在加载......请稍后.......");
              Subscription subscription=  baseModel.loadAllShareMessages(isPullRefresh, time, new LoadShareMessageCallBack() {
                        @Override
                        public void onSuccess(List<SharedMessage> data) {
                                iView.hideLoading();
                                iView.updateAllShareMessages(data, isPullRefresh);
                        }

                        @Override
                        public void onFailed(String errorMsg, int errorId) {
                                iView.hideLoading();
                                iView.showError(errorMsg + errorId, null);
                        }
                });
                subscriptionList.add(subscription);
        }

        @Override
        public void loadShareMessages(String uid, final boolean isPullRefresh, String time) {
              Subscription subscription=  baseModel.loadShareMessages(uid, isPullRefresh, time, new LoadShareMessageCallBack() {
                        @Override
                        public void onSuccess(List<SharedMessage> data) {
                                iView.updateAllShareMessages(data, isPullRefresh);
                                iView.hideLoading();
                        }

                        @Override
                        public void onFailed(String errorMsg, int errorId) {
                                iView.showError(errorMsg + errorId,null);
                        }
                });
                subscriptionList.add(subscription);
        }

        public void getUserInfo(final String uid) {
                iView.showLoading(null);
                BmobQuery<User> query=new BmobQuery<>();
                query.addWhereEqualTo("objectId",uid);
              Subscription subscription=  query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                                if (e == null) {
                                        if (list != null && list.size() > 0) {
                                                RxBusManager.getInstance().post(list.get(0));
                                                loadShareMessages(uid,true,"0000-00-00 01:00:00");
                                        }else {
                                                iView.updateData(null);
                                                iView.hideLoading();
                                        }
                                }else {
                                        iView.showError(null, new EmptyLayout.OnRetryListener() {
                                                @Override
                                                public void onRetry() {
                                                        getUserInfo(uid);
                                                }
                                        });
                                }
                        }
                });
                subscriptionList.add(subscription);
        }


        @Override
        public void onDestroy() {
                super.onDestroy();
                if (subscriptionList != null) {
                        for (Subscription item :
                                subscriptionList) {
                                if (!item.isUnsubscribed()) {
                                        item.unsubscribe();
                                }
                        }
                        subscriptionList.clear();
                subscriptionList=null;
                }
        }
}
