package com.example.chat.mvp.commentlist;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.bean.post.PublicCommentBean;
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
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

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

public class CommentListPresenter extends AppBasePresenter<IView<List<PublicCommentBean>>, DefaultModel> {
    private Gson gson;


    public CommentListPresenter(IView<List<PublicCommentBean>> iView, DefaultModel baseModel) {
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
                        long time = 0L;
                        for (PublicCommentBean bean :
                                list) {
                            long updateTime = TimeUtil.getTime(bean.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss");
                            if (updateTime > time) {
                                time = updateTime;
                            }
                        }
                        String strTime = TimeUtil.getTime(time, "yyyy-MM-dd HH:mm:ss");
                        String key = Constant.UPDATE_TIME_COMMENT + postId;
                        BaseApplication.getAppComponent()
                                .getSharedPreferences().edit()
                                .putString(key, strTime)
                                .apply();
                        UserDBManager.getInstance().addOrUpdateComment(list);
                    }
                    iView.updateData(list);
                } else {
                    CommonLogger.e("评论错误" + e.toString());
                    long currentTime = TimeUtil.getTime(time, "yyyy-MM-dd HH:mm:ss");
                    QueryBuilder<PostCommentEntity> queryBuilder = UserDBManager.getInstance().getDaoSession()
                            .getPostCommentEntityDao()
                            .queryBuilder();
                    queryBuilder.where(PostCommentEntityDao.Properties.Pid.eq(postId));
                    if (isRefresh) {
                        String key = Constant.UPDATE_TIME_COMMENT + postId;
                        String updateTime = BaseApplication
                                .getAppComponent().getSharedPreferences()
                                .getString(key, null);
                        queryBuilder.where(PostCommentEntityDao.Properties.CreatedTime.gt(currentTime));
                        if (updateTime != null && !time.equals(Constant.REFRESH_TIME)) {
                            long resultTime = TimeUtil.getTime(updateTime, "yyyy-MM-dd HH:mm:ss");
                            queryBuilder.where(PostCommentEntityDao.Properties.UpdatedTime.gt(resultTime));
                        } else {
                            queryBuilder.where(PostCommentEntityDao.Properties.UpdatedTime.gt(currentTime));
                        }
                        queryBuilder.where(PostCommentEntityDao.Properties.CreatedTime.gt(currentTime));
                    } else {
                        queryBuilder.where(PostCommentEntityDao.Properties.CreatedTime.lt(currentTime));
                    }
                    queryBuilder.orderDesc(PostCommentEntityDao.Properties.CreatedTime);
                    queryBuilder.limit(10);
                    List<PostCommentEntity> entityList
                            = queryBuilder.build().list();
                    List<PublicCommentBean>  result=new ArrayList<>(entityList.size());
                    for (PostCommentEntity item :
                            entityList) {
                        result.add(MsgManager.getInstance().cover(item));
                    }
                    iView.updateData(result);
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


    public void sendCommentData( PublicCommentBean newBean) {
        newBean.setSendStatus(Constant.SEND_STATUS_SUCCESS);
        addSubscription(newBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                iView.hideLoading();
                if (e == null) {
                    newBean.setSendStatus(Constant.SEND_STATUS_SUCCESS);
                    ToastUtils.showShortToast("评论成功");
                    PublicPostBean item = new PublicPostBean();
                    item.increment("commentCount");
                    addSubscription(item.update(newBean.getPost().getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            RxBusManager.getInstance().post(new CommentEvent(newBean.getPost().getObjectId(), CommentEvent.TYPE_COMMENT, CommentEvent.ACTION_ADD));
                        }
                    }));
                    addSubscription(MsgManager.getInstance().sendNotifyCommentInfo(newBean));
                }else {
                    newBean.setSendStatus(Constant.SEND_STATUS_FAILED);
                    ToastUtils.showShortToast("评论失败"+e.toString());
                }
                UserDBManager.getInstance()
                        .addOrUpdateComment(newBean);
                RxBusManager.getInstance().post(newBean);
            }
        }));
    }

    public void deleteShareInfo(PublicPostBean data, UpdateListener listener) {
        if (data.getSendStatus().equals(Constant.SEND_STATUS_SUCCESS)) {
            UserDBManager.getInstance()
                    .getDaoSession()
                    .getPublicPostEntityDao().deleteByKey(data.getObjectId());
            listener.done(null);
            return;
        }
        PublicPostBean publicPostBean = new PublicPostBean();
        publicPostBean.setObjectId(data.getObjectId());
        addSubscription(publicPostBean.delete(new UpdateListener() {
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
                        String[] temp = new String[postDataBean.getImageList().size()];
                        for (int i = 0; i < postDataBean.getImageList().size(); i++) {
                            temp[i] = postDataBean.getImageList().get(i);
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
        }));
    }


    public void dealLike(final String objectId, final boolean isAdd) {
        BmobQuery<PublicPostBean> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        query.include("author");
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
                                UserDBManager.getInstance()
                                        .addOrUpdatePost(publicPostBean);
                                RxBusManager.getInstance().post(new CommentEvent(objectId, CommentEvent.TYPE_LIKE, isAdd ? CommentEvent.ACTION_ADD : CommentEvent
                                        .ACTION_DELETE));
                                if (isAdd) {
                                    if (!publicPostBean
                                            .getAuthor()
                                            .getObjectId().equals(UserManager.getInstance()
                                            .getCurrentUserObjectId())) {
                                        addSubscription(MsgManager.getInstance().sendPostNotifyInfo(Constant.TYPE_LIKE,objectId,UserManager.getInstance()
                                                .getCurrentUserObjectId(),publicPostBean.getAuthor().getObjectId()));
                                    }
                                }
                            } else {
                                ToastUtils.showShortToast("点赞失败" + e.toString());
                            }



                        }
                    }));
                }else {
                    iView.hideLoading();
                }
            }
        }));
    }
}
