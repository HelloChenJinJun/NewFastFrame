package com.example.chat.mvp.commentdetail;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.CommentDetailAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.bean.post.ReplyDetailContent;
import com.example.chat.dagger.commentdetail.CommentDetailModule;
import com.example.chat.dagger.commentdetail.DaggerCommentDetailComponent;
import com.example.chat.base.SlideBaseActivity;
import com.example.chat.mvp.UserDetail.UserDetailActivity;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:42
 * QQ:         1981367757
 */

public class CommentListDetailActivity extends SlideBaseActivity<List<ReplyDetailContent>, CommentDetailPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {


    @Inject
    CommentDetailAdapter adapter;
    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    private PublicCommentBean data;


    @Override
    public void updateData(List<ReplyDetailContent> listDetailBeans) {
        addOtherData(listDetailBeans);
        if (refresh.isRefreshing()) {
            adapter.refreshData(listDetailBeans);
        }else {
            adapter.addData(listDetailBeans);
        }
    }

    private void addOtherData(List<ReplyDetailContent> listDetailBeans) {
        if (listDetailBeans != null) {
            int size=listDetailBeans.size();
            for (int i = 0; i < size; i++) {
                ReplyDetailContent item=listDetailBeans.get(i);
                if (i % 2 == 0) {
                    item.setMsgType(ReplyDetailContent.TYPE_RIGHT);
                }else {
                    item.setMsgType(ReplyDetailContent.TYPE_LEFT);
                }
            }
        }
    }

    @Override
    protected boolean isNeedHeadLayout() {
        return true;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_comment_list_detail;
    }

    @Override
    protected void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_activity_comment_list_detail_refresh);
        display = (SuperRecyclerView) findViewById(R.id.srcv_activity_comment_list_detail_display);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerCommentDetailComponent.builder().chatMainComponent(ChatApplication
                .getChatMainComponent())
                .commentDetailModule(new CommentDetailModule(this))
                .build().inject(this);
        data = (PublicCommentBean) getIntent().getSerializableExtra(Constant.DATA);
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
        display.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {

            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.riv_comment_detail_left_avatar
                        ||id == R.id.riv_comment_detail_right_avatar) {
                    UserDetailActivity.start(CommentListDetailActivity.this
                            ,adapter
                    .getData(position).getUid());
                }
            }
        });
        runOnUiThread(() -> presenter.getCommentListDetailData(data,true));
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("对话列表");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    public static void start(Activity activity, PublicCommentBean data) {
        Intent intent = new Intent(activity, CommentListDetailActivity.class);
        intent.putExtra(Constant.DATA, data);
        activity.startActivity(intent);
    }


    @Override
    public void showLoading(String loadMessage) {
        refresh.setRefreshing(true);
    }


    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        }
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        presenter.getCommentListDetailData( data,true);
    }

    @Override
    public void loadMore() {
        presenter.getCommentListDetailData(data,false);

    }





}
