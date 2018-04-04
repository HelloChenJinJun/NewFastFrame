package com.example.chat.mvp.commentlist;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PostLikeBean;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.ReplyCommentListBean;
import com.example.chat.bean.post.ReplyDetailContent;
import com.example.chat.events.CommentEvent;
import com.example.chat.events.UpdatePostEvent;
import com.example.chat.listener.OnCreatePublicPostListener;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.bean.chat.PostCommentEntity;
import com.example.commonlibrary.bean.chat.PostCommentEntityDao;
import com.example.commonlibrary.bean.chat.PostLikeEntity;
import com.example.commonlibrary.bean.chat.ReplyCommentListEntity;
import com.example.commonlibrary.bean.chat.UserEntity;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:10
 * QQ:         1981367757
 */

public class CommentListPresenter extends AppBasePresenter<IView<List<PublicCommentBean>>, CommentListModel> {
    private Gson gson;


    public CommentListPresenter(IView<List<PublicCommentBean>> iView, CommentListModel baseModel) {
        super(iView, baseModel);
        gson = BaseApplication.getAppComponent().getGson();
    }

    public void getCommentListData(final String postId, final boolean isRefresh, final String time) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        Subscription s = MsgManager.getInstance().getCommentListData(postId, new FindListener<PublicCommentBean>() {
            @Override
            public void done(List<PublicCommentBean> list, BmobException e) {
                if (e == null || e.getErrorCode() == 101) {
                    if (list != null && list.size() > 0) {
                        List<PostCommentEntity> result = new ArrayList<>();
                        List<UserEntity> userEntityList = new ArrayList<>();
                        for (PublicCommentBean item :
                                list) {
                            PostCommentEntity entity = new PostCommentEntity();
                            entity.setUid(item.getUser().getObjectId());
                            if (!entity.getUid().equals(UserManager.getInstance().getCurrentUserObjectId()) && UserDBManager.getInstance().isStranger(entity.getUid())
                                    ) {
                                UserEntity userEntity = UserManager.getInstance().cover(item.getUser(), true);
                                userEntityList.add(userEntity);
                            }
                            entity.setPid(postId);
                            entity.setContent(item.getContent());
                            entity.setCreatedTime(TimeUtil.getTime(item.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
                            entity.setUpdatedTime(TimeUtil.getTime(item.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
                            entity.setCid(item.getObjectId());
                            result.add(entity);
                        }
                        if (result.size() > 0) {
                            baseModel.getRepositoryManager()
                                    .getDaoSession().getPostCommentEntityDao()
                                    .insertOrReplaceInTx(result);
                        }
                        if (userEntityList.size() > 0) {
                            baseModel
                                    .getRepositoryManager()
                                    .getDaoSession()
                                    .getUserEntityDao()
                                    .insertOrReplaceInTx(userEntityList);
                        }
                    }
                    iView.updateData(list);
                } else {
                    CommonLogger.e("评论错误" + e.toString());
                    long currentTime = TimeUtil.getTime(time, "yyyy-MM-dd HH:mm:ss");
                    QueryBuilder<PostCommentEntity> queryBuilder = baseModel.getRepositoryManager().getDaoSession()
                            .getPostCommentEntityDao()
                            .queryBuilder();
                    queryBuilder.where(PostCommentEntityDao.Properties.Pid.eq(postId));
                    if (isRefresh) {
                        queryBuilder.where(PostCommentEntityDao.Properties.CreatedTime.gt(currentTime));
                        if (!time.equals("0000-00-00 01:00:00")) {
                            queryBuilder.where(PostCommentEntityDao.Properties.UpdatedTime.gt(currentTime));
                        }
                    } else {
                        queryBuilder.where(PostCommentEntityDao.Properties.CreatedTime.lt(currentTime));
                    }
                    queryBuilder.orderDesc(PostCommentEntityDao.Properties.CreatedTime);
                    queryBuilder.limit(10);
                    List<PostCommentEntity> entityList
                            = queryBuilder.build().list();
                    List<PublicCommentBean> commentsList = null;
                    if (entityList.size() > 0) {
                        commentsList = new ArrayList<>();
                        for (PostCommentEntity entity :
                                entityList) {

                            commentsList.add(MsgManager.getInstance().cover(entity));
                        }
                    }
                    ToastUtils.showLongToast("获取缓存评论消息");
                    iView.updateData(commentsList);
                }
                iView.hideLoading();
            }
        }, isRefresh, time);
        addSubscription(s);
    }



    public void updatePublicPostBean(PublicPostBean data) {
        addSubscription(MsgManager.getInstance().updatePublicPostBean(data, new OnCreatePublicPostListener() {
            @Override
            public void onSuccess(PublicPostBean publicPostBean) {
                publicPostBean.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                UserDBManager.getInstance().addOrUpdatePost(publicPostBean);
                RxBusManager.getInstance().post(new UpdatePostEvent(publicPostBean));
            }

            @Override
            public void onFailed(String errorMsg, int errorCode, PublicPostBean publicPostBean) {
                publicPostBean.setSendStatus(Constant.SEND_STATUS_FAILED);
                UserDBManager.getInstance().addOrUpdatePost(publicPostBean);
                RxBusManager.getInstance().post(new UpdatePostEvent(publicPostBean));
            }
        }));
    }


    public void reSendPublicPostBean(PublicPostBean data, String objectId) {
        addSubscription(MsgManager.getInstance().reSendPublicPostBean(data, new OnCreatePublicPostListener() {
            @Override
            public void onSuccess(PublicPostBean publicPostBean) {
                publicPostBean.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                UserDBManager.getInstance()
                        .getDaoSession().getPublicPostEntityDao()
                        .deleteByKey(objectId);
                UserDBManager.getInstance().addOrUpdatePost(publicPostBean);
                RxBusManager.getInstance().post(new UpdatePostEvent(publicPostBean));
            }

            @Override
            public void onFailed(String errorMsg, int errorCode, PublicPostBean publicPostBean) {
                publicPostBean.setSendStatus(Constant.SEND_STATUS_FAILED);
                UserDBManager.getInstance().addOrUpdatePost(publicPostBean);
                RxBusManager.getInstance().post(new UpdatePostEvent(publicPostBean));
            }
        }));
    }



   

    public void sendCommentData(final PublicCommentBean publicCommentBean, final String postId, final String content) {
        final CommentDetailBean commentDetailBean = new CommentDetailBean();
        commentDetailBean.setContent(content);
        if (publicCommentBean != null) {
            CommentDetailBean originBean = gson.fromJson(publicCommentBean.getContent(), CommentDetailBean.class);
            if (originBean.getPublicId() != null) {
                commentDetailBean.setPublicId(originBean.getPublicId());
            } else {
                commentDetailBean.setPublicId(postId + "&" + publicCommentBean.getUser().getObjectId() + "&" +
                        UserManager.getInstance().getCurrentUserObjectId());
            }
            commentDetailBean.setReplyAvatar(publicCommentBean.getUser().getAvatar());
            commentDetailBean.setReplyName(publicCommentBean.getUser().getNick());
            commentDetailBean.setReplyContent(originBean.getContent());
        }
        final PublicCommentBean newBean = new PublicCommentBean();
        newBean.setContent(gson.toJson(commentDetailBean));
        newBean.setUser(UserManager.getInstance().getCurrentUser());
        PublicPostBean posterMessage = new PublicPostBean();
        posterMessage.setObjectId(postId);
        newBean.setPost(posterMessage);
        Subscription subscription = newBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    newBean.setObjectId(s);
                    PostCommentEntity publicCommentBeanEntity = new PostCommentEntity();
                    publicCommentBeanEntity.setCid(s);
                    publicCommentBeanEntity.setContent(newBean.getContent());
                    publicCommentBeanEntity.setPid(postId);
                    publicCommentBeanEntity.setUid(UserManager.getInstance().getCurrentUserObjectId());
                    baseModel.getRepositoryManager().getDaoSession()
                            .getPostCommentEntityDao().insertOrReplace(publicCommentBeanEntity);
                }
                if (e == null && commentDetailBean.getPublicId() != null) {
//                    属于回复评论操作
                    List<BmobObject> list = new ArrayList<>();
                    CommentDetailBean originBean = gson.fromJson(publicCommentBean.getContent(), CommentDetailBean.class);
                    if (originBean.getPublicId() == null) {
//                        首次回复评论，需要把回复的内容和原评论的内容上传到对话列表中
                        ReplyCommentListBean origin = new ReplyCommentListBean();
                        origin.setPublicId(commentDetailBean.getPublicId());
                        ReplyDetailContent originContent = new ReplyDetailContent();
                        originContent.setContent(originBean.getContent());
                        originContent.setTime(TimeUtil.severToLocalTime(publicCommentBean.getCreatedAt()));
                        origin.setContent(gson.toJson(originContent));
                        list.add(origin);
                    }
                    ReplyCommentListBean replyCommentListBean = new ReplyCommentListBean();
                    replyCommentListBean.setPublicId(commentDetailBean.getPublicId());
                    ReplyDetailContent replyDetailContent = new ReplyDetailContent();
                    replyDetailContent.setContent(content);
                    replyDetailContent.setTime(System.currentTimeMillis());
                    replyCommentListBean.setContent(gson.toJson(replyDetailContent));
                    list.add(replyCommentListBean);
                    Subscription subscription1 = new BmobBatch().insertBatch(list).doBatch(new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list1, BmobException e) {
                            if (e == null) {
                                List<ReplyCommentListEntity> listEntityList = new ArrayList<>();
                                for (int i = 0; i < list1.size(); i++) {
                                    if (list1.get(i).getError() == null) {
                                        CommonLogger.e("第" + i + "个评论上传成功");
                                        ReplyCommentListEntity entity = new ReplyCommentListEntity();
                                        ReplyCommentListBean bean = ((ReplyCommentListBean) list.get(i));
                                        entity.setContent(bean.getContent());
                                        entity.setPublicId(bean.getPublicId());
                                        entity.setRid(list1.get(i).getObjectId());
                                        listEntityList.add(entity);
                                    } else {
                                        CommonLogger.e("第" + i + "个评论上传失败" + list1.get(i)
                                                .getError().toString());
                                    }
                                }

                                if (listEntityList.size() > 0) {
                                    baseModel.getRepositoryManager().getDaoSession()
                                            .getReplyCommentListEntityDao()
                                            .insertOrReplaceInTx(listEntityList);
                                }
                                RxBusManager.getInstance().post(newBean);
                                iView.hideLoading();
                                PublicPostBean item = new PublicPostBean();
                                item.increment("commentCount");
                                Subscription subscription1 = item.update(postId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        RxBusManager.getInstance().post(new CommentEvent(postId,CommentEvent.TYPE_COMMENT,CommentEvent.ACTION_ADD));
                                        ToastUtils.showShortToast("评论成功");
                                    }
                                });
                                addSubscription(subscription1);
                            } else {
                                iView.showError(null, () -> sendCommentData(publicCommentBean, postId, content));
                            }
                        }
                    });
                    addSubscription(subscription1);
                } else if (e != null) {


                    iView.showError(null, () -> sendCommentData(publicCommentBean, postId, content));
                } else {
//                    单评论操作
                    RxBusManager.getInstance().post(newBean);
                    iView.hideLoading();
                    PublicPostBean item = new PublicPostBean();
                    item.increment("commentCount");
                    Subscription subscription1 = item.update(postId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            RxBusManager.getInstance().post(new CommentEvent( postId,CommentEvent.TYPE_COMMENT,CommentEvent.ACTION_ADD));
                            ToastUtils.showShortToast("评论成功");
                        }
                    });
                    addSubscription(subscription1);
                }
            }
        });
        addSubscription(subscription);
    }

    public void deleteShareInfo(PublicPostBean data, UpdateListener listener) {
        if (data.getSendStatus().equals(Constant.SEND_STATUS_SUCCESS)) {
            baseModel.getRepositoryManager()
                    .getDaoSession()
                    .getPublicPostEntityDao().deleteByKey(data.getObjectId());
            listener.done(null);
            return;
        }
        PublicPostBean publicPostBean = new PublicPostBean();
        publicPostBean.setObjectId(data.getObjectId());
        publicPostBean.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    UserDBManager.getInstance()
                            .deletePostEntity(data.getObjectId());
                    UserDBManager.getInstance().deleteCommentFromPost(data.getObjectId());
                    BmobQuery<PublicCommentBean> bmobQuery1 = new BmobQuery<>();
                    PublicPostBean item1 = new PublicPostBean();
                    item1.setObjectId(data.getObjectId());
                    bmobQuery1.addWhereEqualTo(Constant.POST, new BmobPointer(item1));
                    bmobQuery1.findObjects(new FindListener<PublicCommentBean>() {
                        @Override
                        public void done(List<PublicCommentBean> list, BmobException e) {
                            if (e == null) {
                                if (list != null && list.size() > 0) {
                                    List<BmobObject> list1 = new ArrayList<>();
                                    list1.addAll(list);
                                    new BmobBatch().deleteBatch(list1).doBatch(new QueryListListener<BatchResult>() {
                                        @Override
                                        public void done(List<BatchResult> list, BmobException e) {
                                            if (e == null) {
                                                CommonLogger.e("评论相关删除成功");
                                            } else {
                                                CommonLogger.e("评论相关删除失败" + e.toString());
                                            }
                                        }
                                    });
                                } else {
                                    CommonLogger.e("评论相关删除成功");
                                }
                            } else {
                                CommonLogger.e("评论相关删除失败" + e.toString());
                            }
                        }
                    });
                    if (data.getMsgType() == Constant.EDIT_TYPE_VIDEO
                            ||
                            data.getMsgType() == Constant.EDIT_TYPE_IMAGE) {
                        PostDataBean postDataBean = BaseApplication.getAppComponent().getGson().fromJson(data.getContent(), PostDataBean.class);
                        String[] temp=new String[postDataBean.getImageList().size()];
                        for (int i = 0; i < postDataBean.getImageList().size(); i++) {
                            temp[i]=postDataBean.getImageList().get(i);
                        }
                        BmobFile.deleteBatch(temp, new DeleteBatchListener() {
                            @Override
                            public void done(String[] strings, BmobException e) {
                                if (e == null) {
                                    CommonLogger.e("删除说说相关资源成功");
                                } else {
                                    CommonLogger.e("删除说说相关资源失败" + e.toString());
                                }
                            }
                        });
                    }
                }
                listener.done(e);
            }
        });
    }


    public void dealLike(final String objectId, final boolean isAdd) {
        BmobQuery<PublicPostBean> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        addSubscription(query.findObjects(new FindListener<PublicPostBean>() {
            @Override
            public void done(List<PublicPostBean> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {

                    PublicPostBean publicPostBean = list.get(0);
                    if (isAdd) {
                        publicPostBean.increment("likeCount");
                        if (!publicPostBean.getLikeList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
                            publicPostBean.getLikeList().add(UserManager.getInstance().getCurrentUserObjectId());
                        }
                    } else {
                        publicPostBean.increment("likeCount", -1);
                        if (publicPostBean.getLikeList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
                            publicPostBean.getLikeList().remove(UserManager.getInstance().getCurrentUserObjectId());
                        }
                    }
                    addSubscription(publicPostBean.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            iView.hideLoading();
                            if (e == null) {
//                                不管下面操作是否成功
                                UserDBManager.getInstance()
                                        .addOrUpdatePost(publicPostBean);
                                RxBusManager.getInstance().post(new CommentEvent(objectId, CommentEvent.TYPE_LIKE, isAdd?CommentEvent.ACTION_ADD:CommentEvent
                                        .ACTION_DELETE));
                            }else{
                                ToastUtils.showShortToast("点赞失败"+e.toString());
                            }
                        }
                    }));
                }
            }
        }));
    }
}
