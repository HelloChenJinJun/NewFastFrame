package com.example.chat.mvp.commentdetail;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.CommentDetailAdapter;
import com.example.chat.bean.post.CommentDetailBean;
import com.example.chat.bean.post.CommentListDetailBean;
import com.example.chat.bean.post.PublicCommentBean;
import com.example.chat.dagger.commentdetail.CommentDetailModule;
import com.example.chat.dagger.commentdetail.DaggerCommentDetailComponent;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.ui.SlideBaseActivity;
import com.example.chat.ui.UserDetailActivity;
import com.example.commonlibrary.BaseActivity;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemChildClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ToolBarOption;
import com.example.commonlibrary.utils.ToastUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/1/3     15:42
 * QQ:         1981367757
 */

public class CommentListDetailActivity extends SlideBaseActivity<List<CommentListDetailBean>, CommentDetailPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {


    @Inject
    CommentDetailAdapter adapter;


    private SwipeRefreshLayout refresh;
    private SuperRecyclerView display;
    private String publicId;
    private PublicCommentBean data;
    private CommentDetailBean commentDetailBean;


    @Override
    public void updateData(List<CommentListDetailBean> listDetailBeans) {
        addOtherData(listDetailBeans);
        if (refresh.isRefreshing()) {
            adapter.refreshData(listDetailBeans);
        }else {
            adapter.addData(listDetailBeans);
        }
    }

    private void addOtherData(List<CommentListDetailBean> listDetailBeans) {
        if (listDetailBeans != null) {
            int size=listDetailBeans.size();
            for (int i = 0; i < size; i++) {
                CommentListDetailBean item=listDetailBeans.get(i);
                if (i % 2 == 0) {
                    item.setMsgType(CommentListDetailBean.TYPE_RIGHT);
                    item.setAvatar(commentDetailBean.getReplyAvatar());
                    item.setName(commentDetailBean.getReplyName());
                }else {
                    item.setMsgType(CommentListDetailBean.TYPE_LEFT);
                    item.setAvatar(data.getUser().getAvatar());
                    item.setName(data.getUser().getNick());
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
        data = (PublicCommentBean) getIntent().getSerializableExtra("data");
        commentDetailBean= BaseApplication.getAppComponent()
                .getGson().fromJson(data.getContent(), CommentDetailBean.class);
        publicId=commentDetailBean.getPublicId();
        display.setLayoutManager(new WrappedLinearLayoutManager(this));
//        display.setLoadMoreFooterView(new LoadMoreFooterView(this));
//        display.setOnLoadMoreListener(this);
        display.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnSimpleItemChildClickListener() {

            @Override
            public void onItemChildClick(int position, View view, int id) {
                String str[]=publicId.split("&");
                if (id == R.id.riv_comment_detail_left_avatar) {
                            UserDetailActivity.start(CommentListDetailActivity.this
                            ,str[2]);
                } else if (id == R.id.riv_comment_detail_right_avatar) {
                    UserDetailActivity.start(CommentListDetailActivity.this
                            ,str[1]);
                }
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.getCommentListDetailData(publicId,true);
            }
        });
        ToolBarOption toolBarOption=new ToolBarOption();
        toolBarOption.setTitle("对话列表");
        toolBarOption.setNeedNavigation(true);
        setToolBar(toolBarOption);
    }

    public static void start(Activity activity, PublicCommentBean data) {
        Intent intent = new Intent(activity, CommentListDetailActivity.class);
        intent.putExtra("data", data);
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
        }else {
//            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
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
        presenter.getCommentListDetailData( publicId,true);
    }

    @Override
    public void loadMore() {
        presenter.getCommentListDetailData( publicId,false);

    }

//    private String getRefreshTime(boolean isRefresh) {
//        if (isRefresh) {
//            if (adapter.getData(0) == null) {
//                return "0000-00-00 01:00:00";
//            }
//            return adapter.getData(0).getCreatedAt();
//        }else {
//            if (adapter.getData(adapter.getData().size() - 1) != null) {
//                return adapter.getData(adapter.getData().size() - 1).getCreatedAt();
//            }else {
//                return "0000-00-00 01:00:00";
//            }
//        }
//    }



}
