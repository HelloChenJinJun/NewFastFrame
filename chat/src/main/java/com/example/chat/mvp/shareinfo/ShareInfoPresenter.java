package com.example.chat.mvp.shareinfo;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.events.UpdatePostEvent;
import com.example.chat.listener.OnCreatePublicPostListener;
import com.example.chat.manager.UserDBManager;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.events.CommentEvent;
import com.example.chat.manager.MsgManager;
import com.example.chat.manager.UserManager;
import com.example.chat.util.TimeUtil;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.rxbus.RxBusManager;
import com.example.commonlibrary.utils.CommonLogger;
import com.example.commonlibrary.utils.ToastUtils;

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

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/30     17:19
 * QQ:         1981367757
 */

public class ShareInfoPresenter extends AppBasePresenter<IView<List<PublicPostBean>>, ShareInfoModel> {
    public ShareInfoPresenter(IView<List<PublicPostBean>> iView, ShareInfoModel baseModel) {
        super(iView, baseModel);
    }

    public void getAllPostData(final boolean isRefresh, final String uid, final String time) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        MsgManager
                .getInstance().getAllPostData(isRefresh,uid,time, new FindListener<PublicPostBean>() {
            @Override
            public void done(List<PublicPostBean> list, BmobException e) {
                if (e == null || e.getErrorCode() == 101) {
                    if (list != null && list.size() > 0) {
                        long time = 0L;
                        for (PublicPostBean bean :
                                list) {
                            long updateTime = TimeUtil.getTime(bean.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss");
                            if (updateTime > time) {
                                time = updateTime;
                            }
                        }
                        String strTime = TimeUtil.getTime(time, "yyyy-MM-dd HH:mm:ss");
                        String key=Constant.UPDATE_TIME_SHARE;
                        if (uid != null) {
                            key=key+uid;
                        }
                        BaseApplication.getAppComponent()
                                .getSharedPreferences().edit()
                                .putString(key, strTime)
                                .apply();
                    }
                    iView.updateData(list);
                    iView.hideLoading();
                } else {
                    iView.showError(e.toString(), new EmptyLayout.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            getAllPostData(isRefresh, uid, time);
                        }
                    });
                }
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
                }else {
                    iView.hideLoading();
                    ToastUtils.showShortToast("点赞失败"+(e!=null?e.toString():""));
                }
            }
        }));
    }

    public void deleteShareInfo(PublicPostBean data, UpdateListener listener) {
        if (!data.getSendStatus().equals(Constant.SEND_STATUS_SUCCESS)) {
            UserDBManager.getInstance()
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
}
