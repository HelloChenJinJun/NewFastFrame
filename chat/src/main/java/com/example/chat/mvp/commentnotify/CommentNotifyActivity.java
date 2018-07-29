package com.example.chat.mvp.commentnotify;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.R;
import com.example.chat.adapter.CommentNotifyAdapter;
import com.example.chat.base.Constant;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.bean.PostNotifyBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.PublicPostBean;
import com.example.chat.dagger.commentnotify.CommentNotifyModule;
import com.example.chat.dagger.commentnotify.DaggerCommentNotifyComponent;
import com.example.chat.events.UnReadPostNotifyEvent;
import com.example.chat.manager.UserDBManager;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.bean.chat.PostNotifyInfo;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.rxbus.RxBusManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      李晨
 * 创建时间:    2018/5/19     0:32
 */

public class CommentNotifyActivity extends SlideBaseActivity<List<PostNotifyBean>, CommentNotifyPresenter> implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private SuperRecyclerView display;
    private SwipeRefreshLayout refresh;
    private ArrayList<PostNotifyInfo>  data;


    @Inject
    CommentNotifyAdapter commentNotifyAdapter;


    @Override
    public void updateData(List<PostNotifyBean> publicCommentBeans) {
        if (refresh.isRefreshing()) {
            commentNotifyAdapter.refreshData(publicCommentBeans);
        } else {
            commentNotifyAdapter.addData(publicCommentBeans);
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_comment_notify;
    }

    @Override
    protected void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_comment_notify_refresh);
        refresh.setOnRefreshListener(this);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_comment_notify_display);
    }

    @Override
    protected void initData() {
        DaggerCommentNotifyComponent.builder()
                .chatMainComponent(getChatMainComponent())
                .commentNotifyModule(new CommentNotifyModule(this))
                .build().inject(this);
        data= (ArrayList<PostNotifyInfo>) getIntent().getSerializableExtra(Constant.DATA);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.setAdapter(commentNotifyAdapter);
        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
        display.setOnLoadMoreListener(this);
        commentNotifyAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                PublicPostBean bean=commentNotifyAdapter.getData(position)
                        .getPublicPostBean();
                if (bean == null) {
                    bean=commentNotifyAdapter.getData(position)
                            .getPublicCommentBean().getPost();
                }
                CommentListActivity.start(CommentNotifyActivity.this
                        , bean);
            }
        });
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setTitle("留言");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
        if (data!=null) {
            UserDBManager.getInstance().updateCommentReadStatus();
            RxBusManager.getInstance().post(new UnReadPostNotifyEvent(null));
        }
        runOnUiThread(() -> presenter.getPostNotifyInfo(true, data));
    }




    public static void start(Activity activity, ArrayList<PostNotifyInfo> commentIdList) {
        Intent intent = new Intent(activity, CommentNotifyActivity.class);
        intent.putExtra(Constant.DATA, commentIdList);
        activity.startActivity(intent);
    }


    @Override
    public void loadMore() {
        presenter.getPostNotifyInfo(false, data);
    }

    @Override
    public void onRefresh() {
        presenter.getPostNotifyInfo(true,data);

    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        super.showError(errorMsg, listener);
        refresh.setRefreshing(false);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        refresh.setRefreshing(false);
    }
}

