package com.example.chat.mvp.ShareMessageTask;


import com.example.chat.bean.SharedMessage;
import com.example.chat.listener.AddShareMessageCallBack;
import com.example.chat.listener.DealCommentMsgCallBack;
import com.example.chat.listener.DealMessageCallBack;
import com.example.chat.listener.LoadShareMessageCallBack;
import com.example.chat.util.LogUtil;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/2      20:26
 * QQ:             1981367757
 */

public class ShareMessagePresenter extends ShareMessageContacts.Presenter {

        public ShareMessagePresenter(ShareMessageContacts.View iView, ShareMessageContacts.Model baseModel) {
                super(iView, baseModel);
        }

        @Override
        public void addShareMessage(final SharedMessage shareMessage) {
                iView.showLoading("正在发送说说......请稍候.........");
                baseModel.addShareMessage(shareMessage, new AddShareMessageCallBack() {
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
        }

        @Override
        public void deleteShareMessage(String id) {
                iView.showLoading("正在删除说说.......请稍后.........");
                baseModel.deleteShareMessage(id, new DealMessageCallBack() {
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

        }

        @Override
        public void addLiker(String id) {
//                这里的id为说说的id
                baseModel.addLiker(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                iView.updateLikerAdd(id);
                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                iView.showError("添加点赞消息失败" + errorMsg + errorId, null);
                        }
                });
        }

        @Override
        public void deleteLiker(String id) {
                baseModel.deleteLiker(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                iView.updateLikerDeleted(id);

                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                iView.showError("删除点赞失败" + errorMsg + errorId, null);
                        }
                });

        }

        @Override
        public void addComment(String id, String content) {
                iView.showLoading("正在添加评论.....");
                baseModel.addComment(id, content, new DealCommentMsgCallBack() {
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

        }

        @Override
        public void deleteComment(String id, int position) {
                iView.showLoading("正在删除评论");
                baseModel.deleteComment(id, position, new DealCommentMsgCallBack() {
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
        }

        @Override
        public void loadAllShareMessages(final boolean isPullRefresh, String time) {
                LogUtil.e("加载所有的说说消息");
//                mView.showLoading("正在加载......请稍后.......");
                baseModel.loadAllShareMessages(isPullRefresh, time, new LoadShareMessageCallBack() {
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
        }

        @Override
        public void loadShareMessages(String uid, final boolean isPullRefresh, String time) {
                baseModel.loadShareMessages(uid, isPullRefresh, time, new LoadShareMessageCallBack() {
                        @Override
                        public void onSuccess(List<SharedMessage> data) {
                                iView.hideLoading();
                                iView.updateAllShareMessages(data, isPullRefresh);
                        }

                        @Override
                        public void onFailed(String errorMsg, int errorId) {
                                iView.hideLoading();
                                iView.showError(errorMsg + errorId,null);
                        }
                });
        }
}
