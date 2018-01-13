package com.example.chat.mvp.ShareMessageTask;


import com.example.chat.MainRepositoryManager;
import com.example.chat.bean.SharedMessage;
import com.example.chat.listener.AddShareMessageCallBack;
import com.example.chat.listener.DealCommentMsgCallBack;
import com.example.chat.listener.DealMessageCallBack;
import com.example.chat.listener.LoadShareMessageCallBack;
import com.example.commonlibrary.mvp.model.BaseModel;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.presenter.RxBasePresenter;
import com.example.commonlibrary.mvp.view.IView;

import java.util.List;

import rx.Subscription;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/2      19:09
 * QQ:             1981367757
 */

public interface ShareMessageContacts {
        interface View<T> extends IView<T> {
                //                这里添加view更新数据的功能
//                添加一个说说item id 为objectId
                void updateShareMessageAdded(SharedMessage shareMessage);

                void updateShareMessageDeleted(String id);

                void updateLikerAdd(String id);

                void updateLikerDeleted(String id);

                /**
                 */
                void updateCommentAdded(String id, String content, int position);

                /**
                 * @param id      说说ID
                 * @param content 评论内容
                 */
                void updateCommentDeleted(String id, String content, int position);


                void updateAllShareMessages(List<SharedMessage> data, boolean isPullRefresh);
        }


        public abstract class Model extends BaseModel<MainRepositoryManager> {


                public Model(MainRepositoryManager repositoryManager) {
                        super(repositoryManager);
                }

                //                添加一个说说item id 为objectId
                abstract Subscription addShareMessage(SharedMessage shareMessage, AddShareMessageCallBack addShareMessageCallBack);

                // 删除一个说说item
                abstract Subscription deleteShareMessage(String id, DealMessageCallBack dealMessageCallBack);

                //                点赞
                abstract Subscription addLiker(String id, DealMessageCallBack dealMessageCallBack);

                //                取消赞
                abstract Subscription deleteLiker(String id, DealMessageCallBack dealMessageCallBack);

                //添加评论
                abstract Subscription addComment(String id, String content, DealCommentMsgCallBack dealCommentMsgCallBack);

                //删除评论
                abstract Subscription deleteComment(String id, int position, DealCommentMsgCallBack dealCommentMsgCallBack);


                //                加载所有的说说消息
                abstract Subscription loadAllShareMessages(boolean isPullRefresh, String time, LoadShareMessageCallBack loadShareMessageCallBack);

//                void loadAllMyShareMessages(boolean isPullRefresh, String time, LoadShareMessageCallBack loadShareMessageCallBack);

                abstract  Subscription loadShareMessages(String uid, boolean isPullRefresh, String time, LoadShareMessageCallBack loadShareMessageCallBack);
        }


        abstract class Presenter extends RxBasePresenter<View, Model> {
                public Presenter(View iView, Model baseModel) {
                        super(iView, baseModel);
                }

                //                添加一个说说item id 为objectId
                abstract void addShareMessage(SharedMessage shareMessage);

                // 删除一个说说item
                abstract void deleteShareMessage(String id);

                //                点赞
                abstract void addLiker(String id);

                //                取消赞
                abstract void deleteLiker(String id);

                //添加评论
                abstract void addComment(String id, String content);

                //删除评论
                abstract void deleteComment(String id, int position);

                //                加载所有的说说消息
                abstract void loadAllShareMessages(boolean isPullRefresh, String time);

                public abstract void loadShareMessages(String uid,boolean isPullRefresh,String time);

        }


}
