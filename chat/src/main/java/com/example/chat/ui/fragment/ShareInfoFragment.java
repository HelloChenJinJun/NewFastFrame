package com.example.chat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.ShareInfoAdapter;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.dagger.shareinfo.DaggerShareInfoComponent;
import com.example.chat.dagger.shareinfo.ShareInfoModule;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.mvp.shareinfo.ShareInfoPresenter;
import com.example.chat.ui.BasePreViewActivity;
import com.example.chat.ui.EditShareMessageActivity;
import com.example.chat.view.fab.FloatingActionButton;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.example.commonlibrary.cusotomview.ToolBarOption;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/29     22:33
 * QQ:         1981367757
 */

public class ShareInfoFragment extends BaseFragment<List<PublicPostBean>, ShareInfoPresenter> implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {

    private SuperRecyclerView display;
        @Inject
    ShareInfoAdapter shareInfoAdapter;
    private SwipeRefreshLayout refresh;

    @Override
    protected boolean isNeedHeadLayout() {
        return false;
    }

    @Override
    protected boolean isNeedEmptyLayout() {
        return true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_share_info;
    }

    @Override
    protected void initView() {
        display = (SuperRecyclerView) findViewById(R.id.srcv_fragment_share_info_display);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_fragment_share_info_refresh);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_fragment_share_info_edit);
        floatingActionButton.setOnClickListener(this);
        refresh.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        DaggerShareInfoComponent
                .builder()
                .chatMainComponent(ChatApplication.getChatMainComponent())
                .shareInfoModule(new ShareInfoModule(this))
                .build().inject(this);
        display.setLayoutManager(new WrappedLinearLayoutManager(getContext()));
        display.setLoadMoreFooterView(new LoadMoreFooterView(getContext()));
        display.setOnLoadMoreListener(this);
        display.addItemDecoration(new ListViewDecoration(getContext()));
        display.setAdapter(shareInfoAdapter);
        shareInfoAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                CommentListActivity.start(getActivity(),shareInfoAdapter.getData(position));

            }


            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.tv_item_fragment_share_info_share) {

                } else if (id == R.id.tv_item_fragment_share_info_comment) {

                } else if (id == R.id.tv_item_fragment_share_info_like) {

                }else if (id==R.id.riv_item_fragment_share_info_avatar){

                } else if (id == R.id.iv_item_fragment_share_info_more) {

                }else {
                    List<String>  imageList= BaseApplication
                            .getAppComponent()
                            .getGson().fromJson(shareInfoAdapter.getData(position-shareInfoAdapter.getItemUpCount())
                                    .getContent(), PostDataBean.class).getImageList();
                    if (imageList != null && imageList.size() > 0) {
                        List<ImageItem>  result=new ArrayList<>();
                        for (String str :
                                imageList) {
                            ImageItem imageItem=new ImageItem();
                            imageItem.setPath(str);
                            result.add(imageItem);
                        }
                        BasePreViewActivity.startBasePreview(getActivity(),result,id);
                    }

                }

            }
        });
        presenter.registerEvent(PublicPostBean.class, new Consumer<PublicPostBean>() {
            @Override
            public void accept(PublicPostBean publicPostBean) throws Exception {
                shareInfoAdapter.addData(0,publicPostBean);
            }
        });
        ((HomeFragment) getParentFragment()).initActionBar("动态");
//        ToolBarOption toolBarOption=new ToolBarOption();
//        toolBarOption.setTitle("动态");
//        toolBarOption.setNeedNavigation(false);
//        setToolBar(toolBarOption);
    }

    @Override
    protected void updateView() {
        presenter.getAllPostData(true, "0000-00-00 01:00:00");
    }

    @Override
    public void updateData(List<PublicPostBean> publicPostBeans) {
        if (refresh.isRefreshing()) {
            shareInfoAdapter.addData(0, publicPostBeans);
        } else {
            shareInfoAdapter.addData(publicPostBeans);
        }
    }


    @Override
    public void showLoading(String loadingMsg) {
        if (!refresh.isRefreshing()) {
            refresh.setRefreshing(true);
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
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            super.showError(errorMsg, listener);
        } else {
            display.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    @Override
    public void onRefresh() {
        if (shareInfoAdapter.getData().size() > 0) {
            presenter.getAllPostData(true, shareInfoAdapter.getData(0)
                    .getCreatedAt());
        } else {
            presenter.getAllPostData(true, "0000-00-00 01:00:00");
        }
    }

    @Override
    public void loadMore() {
        if (shareInfoAdapter.getData().size() > 0) {
            presenter.getAllPostData(false, shareInfoAdapter.getData(shareInfoAdapter.getData().size() - 1).getCreatedAt());
        } else {
            presenter.getAllPostData(false, "0000-00-00 01:00:00");
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(),EditShareMessageActivity.class);
        intent.putExtra("destination","public");
        startActivity(intent);
    }
}
