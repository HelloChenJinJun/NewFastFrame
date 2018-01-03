package com.example.chat.mvp.commentdetail;

import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.CommentListDetailBean;
import com.example.chat.bean.post.ReplyCommentListBean;
import com.example.chat.bean.post.ReplyDetailContent;
import com.example.chat.manager.MsgManager;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.presenter.BasePresenter;
import com.example.commonlibrary.mvp.view.IView;
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
        gson= BaseApplication.getAppComponent().getGson();
    }

    public void getCommentListDetailData(final String publicId, final boolean isRefresh) {
        if (isRefresh) {
            iView.showLoading(null);
        }
        MsgManager.getInstance().getCommentListDetailData(publicId, new FindListener<ReplyCommentListBean>() {
            @Override
            public void done(List<ReplyCommentListBean> list, BmobException e) {
                if (e == null||e.getErrorCode()==101) {
                    List<CommentListDetailBean>  result=null;
                    if (list != null && list.size() > 0) {
                        result=new ArrayList<>();
                        int size=list.size();
                        for (int i = 0; i < size; i++) {
                            ReplyCommentListBean item=list.get(i);
                            ReplyDetailContent content=gson.fromJson(item.getContent(),ReplyDetailContent.class);
                            CommentListDetailBean bean=new CommentListDetailBean();
                            bean.setContent(content.getContent());
                            bean.setTime(content.getTime());
                            result.add(bean);
                        }
                    }
                    iView.updateData(result);
                    iView.hideLoading();
                }else {
                    iView.showError(null, new EmptyLayout.OnRetryListener() {
                        @Override
                        public void onRetry() {
                            getCommentListDetailData(publicId, isRefresh);
                        }
                    });
                }
            }
        });


    }
}
