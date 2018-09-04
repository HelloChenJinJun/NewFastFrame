package com.example.chat.mvp.commentnotify;

import com.example.chat.base.AppBasePresenter;
import com.example.chat.base.Constant;
import com.example.chat.bean.CommentNotifyBean;
import com.example.chat.bean.PostNotifyBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.manager.UserDBManager;
import com.example.chat.manager.UserManager;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.bean.chat.PostNotifyInfo;
import com.example.commonlibrary.mvp.model.DefaultModel;
import com.example.commonlibrary.mvp.view.IView;

import java.util.ArrayList;
import java.util.Arrays;
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

public class CommentNotifyPresenter extends AppBasePresenter<IView<List<PostNotifyBean>>, DefaultModel> {
    private int page = 0;

    public CommentNotifyPresenter(IView<List<PostNotifyBean>> iView, DefaultModel baseModel) {
        super(iView, baseModel);
    }

    public void getPostNotifyInfo(boolean isRefresh, ArrayList<PostNotifyInfo> list) {
        if (isRefresh) {
            iView.showLoading(null);
            page = 0;
        }
        page++;
        if (list != null) {

            List<String> idList = new ArrayList<>();
            for (PostNotifyInfo info :
                    list) {
                if (info.getType().equals(Constant.TYPE_COMMENT)
                        && info.getId().contains("&")) {
                    idList.addAll(Arrays.asList(info.getId().split("&")));
                } else {
                    idList.add(info.getId());
                }
            }

            BmobQuery<PostNotifyBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("toUser", new BmobPointer(UserManager.getInstance().getCurrentUser()));
            bmobQuery.addWhereContainedIn("objectId", idList);
            bmobQuery.setLimit(10);
            bmobQuery.order("-createdAt");
            bmobQuery.setSkip((page - 1) * 10);
            bmobQuery.include("relatedUser,publicPostBean,publicCommentBean,publicPostBean.author,publicCommentBean.post,publicCommentBean.user,publicCommentBean.post.author");
            addSubscription(bmobQuery.findObjects(new FindListener<PostNotifyBean>() {
                @Override
                public void done(List<PostNotifyBean> postNotifyBeans, BmobException e) {
                    if (e == null) {
                        iView.updateData(postNotifyBeans);
                        iView.hideLoading();
                    } else {
                        iView.showError(e.toString(), () -> getPostNotifyInfo(isRefresh, list));
                    }
                }
            }));
        } else {
            BmobQuery<PostNotifyBean> bmobQuery = new BmobQuery<>();
            bmobQuery.addWhereEqualTo("toUser", new BmobPointer(UserManager.getInstance().getCurrentUser()));
            bmobQuery.setLimit(10);
            bmobQuery.order("-createdAt");
            bmobQuery.setSkip((page - 1) * 10);
            bmobQuery.include("relatedUser,publicPostBean,publicCommentBean,publicPostBean.author,publicCommentBean.post,publicCommentBean.user,publicCommentBean.post.author");
            addSubscription(bmobQuery.findObjects(new FindListener<PostNotifyBean>() {
                @Override
                public void done(List<PostNotifyBean> postNotifyBeans, BmobException e) {
                    if (e == null) {
                        iView.updateData(postNotifyBeans);
                        iView.hideLoading();
                    } else {
                        iView.showError(e.toString(), () -> getPostNotifyInfo(isRefresh,null));
                    }
                }
            }));

        }
    }
}
