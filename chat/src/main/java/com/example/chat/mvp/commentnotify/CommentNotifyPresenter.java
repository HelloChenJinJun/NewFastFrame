package com.example.chat.mvp.commentnotify;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.bean.CommentNotifyBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.mvp.view.IView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/19     11:07
 */

public class CommentNotifyPresenter extends AppBasePresenter<IView<List<PublicCommentBean>>,CommentNotifyModel>{
    private int page=0;

    public CommentNotifyPresenter(IView<List<PublicCommentBean>> iView, CommentNotifyModel baseModel) {
        super(iView, baseModel);
    }

    public void getNotifyCommentList(boolean isRefresh, ArrayList<String> commentIdList) {
        if (isRefresh) {
            iView.showLoading(null);
            page=0;
        }
        page++;
        if (commentIdList != null) {
            BmobQuery<PublicCommentBean>  bmobQuery=new BmobQuery<>();
            bmobQuery.addWhereContainedIn("objectId",commentIdList);
            bmobQuery.setLimit(10);
            bmobQuery.order("-createdAt");
            bmobQuery.setSkip((page-1)*10);
            bmobQuery.include("user,post,post.author");
            addSubscription(bmobQuery.findObjects(new FindListener<PublicCommentBean>() {
                @Override
                public void done(List<PublicCommentBean> list, BmobException e) {
                    if (e == null) {
                        iView.updateData(list);
                        UserDBManager.getInstance().addOrUpdateComment(list);
                        iView.hideLoading();
                    }else {
                        iView.showError(e.toString(), () -> getNotifyCommentList(isRefresh, commentIdList));
                    }
                }
            }));
        }else {
            BmobQuery<CommentNotifyBean>  bmobQuery=new BmobQuery<>();
            bmobQuery.addWhereEqualTo("user",new BmobPointer(UserManager.getInstance().getCurrentUser()));
            bmobQuery.setLimit(10);
            bmobQuery.order("-createdAt");
            bmobQuery.setSkip((page-1)*10);
            bmobQuery.include("publicCommentBean,user,publicCommentBean.post,publicCommentBean.user" +
                    ",publicCommentBean.post.author");
            addSubscription(bmobQuery.findObjects(new FindListener<CommentNotifyBean>() {
                @Override
                public void done(List<CommentNotifyBean> list, BmobException e) {
                    if (e == null) {
                        List<PublicCommentBean>  result=new ArrayList<>(list.size());
                        for (CommentNotifyBean item :
                                list) {
                            result.add(item.getPublicCommentBean());
                        }
                        UserDBManager.getInstance().addOrUpdateComment(result);
                        iView.updateData(result);
                        iView.hideLoading();
                    }else {
                        iView.showError(e.toString(), () -> getNotifyCommentList(isRefresh, commentIdList));
                    }
                }
            }));

        }
    }
}
