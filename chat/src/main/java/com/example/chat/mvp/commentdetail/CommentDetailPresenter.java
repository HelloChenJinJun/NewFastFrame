package com.example.chat.mvp.commentdetail;

import com.example.chat.bean.post.CommentListDetailBean;
import com.example.chat.bean.post.ReplyCommentListBean;
import com.example.chat.bean.post.ReplyDetailContent;
import com.example.chat.manager.MsgManager;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.chat.ReplyCommentListEntity;
import com.example.commonlibrary.bean.chat.ReplyCommentListEntityDao;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
import com.example.commonlibrary.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     16:15
 * QQ:         1981367757
 */

public class CommentDetailPresenter extends BasePresenter<IView<List<CommentListDetailBean>>,CommentDetailModel>{
    private Gson gson;

    public CommentDetailPresenter(IView<List<CommentListDetailBean>> iView, CommentDetailModel baseModel) {
        super(iView, baseModel);
        gson = BaseApplication.getAppComponent().getGson();
    }

    public void getCommentListDetailData(final String publicId, final boolean isRefresh) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        MsgManager.getInstance().getCommentListDetailData(publicId, new FindListener<ReplyCommentListBean>() {
            @Override
            public void done(List<ReplyCommentListBean> list, BmobException e) {
                if (e == null || e.getErrorCode() == 101) {
                    List<CommentListDetailBean> result = null;
                    if (list != null && list.size() > 0) {
                        result = new ArrayList<>();
                        int size = list.size();
                        List<ReplyCommentListEntity> replyCommentListEntityList = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            ReplyCommentListBean item = list.get(i);
                            ReplyDetailContent content = gson.fromJson(item.getContent(), ReplyDetailContent.class);
                            CommentListDetailBean bean = new CommentListDetailBean();
                            bean.setContent(content.getContent());
                            bean.setTime(content.getTime());
                            result.add(bean);
                            ReplyCommentListEntity entity = new ReplyCommentListEntity();
                            entity.setRid(item.getObjectId());
                            entity.setPublicId(item.getPublicId());
                            entity.setContent(item.getContent());
                            replyCommentListEntityList.add(entity);
                        }
                        baseModel.getRepositoryManager()
                                .getDaoSession()
                                .getReplyCommentListEntityDao()
                                .insertOrReplaceInTx(replyCommentListEntityList);
                    }
                    iView.updateData(result);
                } else {
                    List<ReplyCommentListEntity> result =
                            baseModel.getRepositoryManager().getDaoSession()
                                    .getReplyCommentListEntityDao().queryBuilder().where(ReplyCommentListEntityDao
                                    .Properties.PublicId.eq(publicId)).limit(50).build().list();
                    List<CommentListDetailBean> beanList = null;
                    if (result != null && result.size() > 0) {
                        beanList = new ArrayList<>();
                        for (ReplyCommentListEntity entity :
                                result) {
                            ReplyDetailContent content = gson.fromJson(entity.getContent(), ReplyDetailContent.class);
                            CommentListDetailBean bean = new CommentListDetailBean();
                            bean.setContent(content.getContent());
                            bean.setTime(content.getTime());
                            beanList.add(bean);
                        }
                    }
                    iView.updateData(beanList);
                    ToastUtils.showLongToast("获取缓存数据");
//                    iView.showError(null, () -> getCommentListDetailData(publicId, isRefresh));
                }
                iView.hideLoading();
            }
        });


    }
}
