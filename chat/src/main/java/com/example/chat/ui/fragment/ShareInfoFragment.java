package com.example.chat.ui.fragment;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.example.chat.ChatApplication;
import com.example.chat.R;
import com.example.chat.adapter.ShareInfoAdapter;
import com.example.chat.base.Constant;
import com.example.chat.bean.ImageItem;
import com.example.chat.bean.PublicPostBean;
import com.example.chat.bean.User;
import com.example.chat.bean.post.PostDataBean;
import com.example.chat.bean.post.ShareTypeContent;
import com.example.chat.dagger.shareinfo.DaggerShareInfoComponent;
import com.example.chat.dagger.shareinfo.ShareInfoModule;
import com.example.chat.events.CommentEvent;
import com.example.chat.mvp.commentlist.CommentListActivity;
import com.example.chat.mvp.shareinfo.ShareInfoPresenter;
import com.example.chat.ui.BasePreViewActivity;
import com.example.chat.ui.EditShareMessageActivity;
import com.example.chat.ui.ImageDisplayActivity;
import com.example.chat.ui.UserDetailActivity;
import com.example.chat.util.PostUtil;
import com.example.chat.view.fab.FloatingActionButton;
import com.example.chat.view.fab.FloatingActionsMenu;
import com.example.commonlibrary.BaseApplication;
import com.example.commonlibrary.BaseFragment;
import com.example.commonlibrary.baseadapter.SuperRecyclerView;
import com.example.commonlibrary.baseadapter.empty.EmptyLayout;
import com.example.commonlibrary.baseadapter.foot.LoadMoreFooterView;
import com.example.commonlibrary.baseadapter.foot.OnLoadMoreListener;
import com.example.commonlibrary.baseadapter.listener.OnSimpleItemClickListener;
import com.example.commonlibrary.baseadapter.manager.WrappedLinearLayoutManager;
import com.example.commonlibrary.cusotomview.ListViewDecoration;
import com.google.gson.Gson;

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
    private FloatingActionsMenu mMenu;
    private FloatingActionButton normal,video,image;

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
        mMenu = (FloatingActionsMenu) findViewById(R.id.fam_share_info_menu);
        normal = (FloatingActionButton) findViewById(R.id.fab_share_info_normal);
        video = (FloatingActionButton) findViewById(R.id.fab_share_info_video);
        image = (FloatingActionButton) findViewById(R.id.fab_share_info_image);
        normal.setOnClickListener(this);
        video.setOnClickListener(this);
        image.setOnClickListener(this);
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
        mMenu.attachToRecyclerView(display);
//        display.addItemDecoration(new ListViewDecoration(getContext()));
        display.setAdapter(shareInfoAdapter);
        shareInfoAdapter.setOnItemClickListener(new OnSimpleItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                CommentListActivity.start(getActivity(), shareInfoAdapter.getData(position));
            }


            @Override
            public void onItemChildClick(int position, View view, int id) {
                if (id == R.id.tv_item_fragment_share_info_share) {
                    Intent intent=new Intent(getActivity(),EditShareMessageActivity.class);
                    intent.putExtra("destination","public");
                    intent.putExtra("type","share");
                    intent.putExtra("data",shareInfoAdapter.getData(position));
                    getActivity().startActivity(intent);
                } else if (id == R.id.tv_item_fragment_share_info_comment) {
                    CommentListActivity.start(getActivity(), shareInfoAdapter.getData(position));

                } else if (id == R.id.tv_item_fragment_share_info_like) {
//                    todo  首次点赞图片说说，刷新时图片没刷新出来，可能是ListImageView内部的Bug
                    presenter.addLike(shareInfoAdapter.getData(position).getObjectId());
                } else if (id == R.id.riv_item_fragment_share_info_avatar) {
                    UserDetailActivity.start(getActivity(), shareInfoAdapter.getData(position)
                            .getAuthor().getObjectId());

                } else if (id == R.id.iv_item_fragment_share_info_more) {

                } else if (id==R.id.iv_item_fragment_share_info_video_display){
                    Intent videoIntent = new Intent(getContext(), ImageDisplayActivity.class);
                    PublicPostBean publicPostBean=shareInfoAdapter.getData(position);
                    PostDataBean item=BaseApplication.getAppComponent()
                            .getGson().fromJson(publicPostBean.getContent(),PostDataBean.class);
                    if (item.getImageList()!=null&&item.getImageList().size()>1) {
                        videoIntent.putExtra("name", "photo");
                        videoIntent.putExtra("url", item.getImageList().get(0));
                        videoIntent.putExtra("videoUrl", item.getImageList().get(1));
                        videoIntent.putExtra("id", id);
                        startActivity(videoIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, "photo").toBundle());
                    }else if (item.getShareContent()!=null){
//                        点击的是分享内容中的视频
                        dealSharePostData(position);
                    }
                }else if (id==R.id.ll_item_fragment_share_info_share_image){
                           dealSharePostData(position);
                }else if (id==R.id.ll_item_fragment_share_info_share_container){
                   dealSharePostData(position);
                }else {
                    List<String> imageList = BaseApplication
                            .getAppComponent()
                            .getGson().fromJson(shareInfoAdapter.getData(position - shareInfoAdapter.getItemUpCount())
                                    .getContent(), PostDataBean.class).getImageList();
                    if (imageList != null && imageList.size() > 0) {
                        List<ImageItem> result = new ArrayList<>();
                        for (String str :
                                imageList) {
                            ImageItem imageItem = new ImageItem();
                            imageItem.setPath(str);
                            result.add(imageItem);
                        }
                        BasePreViewActivity.startBasePreview(getActivity(), result, id);
                    }else {
                        dealSharePostData(position);
                    }
                }

            }


        });
        presenter.registerEvent(PublicPostBean.class, new Consumer<PublicPostBean>() {
            @Override
            public void accept(PublicPostBean publicPostBean) throws Exception {
                shareInfoAdapter.addData(0, publicPostBean);
            }
        });
        presenter.registerEvent(CommentEvent.class, new Consumer<CommentEvent>() {
            @Override
            public void accept(CommentEvent likeEvent) throws Exception {
                if (likeEvent.getType() == CommentEvent.TYPE_LIKE) {
                    notifyLikeAdd(likeEvent.getId());
                } else {
                    notifyCommentAdd(likeEvent.getId());
                }
            }
        });
        ((HomeFragment) getParentFragment()).initActionBar("动态");
//        ToolBarOption toolBarOption=new ToolBarOption();
//        toolBarOption.setTitle("动态");
//        toolBarOption.setNeedNavigation(false);
//        setToolBar(toolBarOption);
    }

    private void dealSharePostData(int position) {
        Gson gson=BaseApplication.getAppComponent()
                .getGson();
        PostDataBean bean=gson.fromJson(shareInfoAdapter.getData(position).getContent(),PostDataBean.class);
//                            分享文章的ID
        PublicPostBean publicPostBean=new PublicPostBean();
        ShareTypeContent shareTypeContent=bean.getShareContent();
//        while (shareTypeContent.getPostDataBean() == PostUtil.LAYOUT_TYPE_SHARE) {
//            shareTypeContent=shareTypeContent.getPostDataBean().getShareContent();
//        }
        User author=new User();
        author.setAvatar(shareTypeContent.getAvatar());
        author.setNick(shareTypeContent.getNick());
        author.setObjectId(shareTypeContent.getUid());
        author.setSex(shareTypeContent.isSex());
        author.setAddress(shareTypeContent.getAddress());
        publicPostBean.setAuthor(author);
        publicPostBean.setContent(gson.toJson(shareTypeContent.getPostDataBean()));
        publicPostBean.setMsgType(bean.getShareType());
        publicPostBean.setLikeCount(shareTypeContent.getLikeCount());
        publicPostBean.setCommentCount(shareTypeContent.getCommentCount());
        publicPostBean.setShareCount(shareTypeContent.getShareCount());
        publicPostBean.setObjectId(shareTypeContent.getPid());
        publicPostBean.setUpdatedAt(shareTypeContent.getCreateAt());
        CommentListActivity.start(getActivity(),publicPostBean);
    }

    private void notifyCommentAdd(String id) {
        PublicPostBean bean = shareInfoAdapter.getPublicPostDataById(id);
        bean.setCommentCount(bean.getCommentCount() + 1);
        shareInfoAdapter.addData(bean);
    }

    private void notifyLikeAdd(String id) {
        PublicPostBean bean = shareInfoAdapter.getPublicPostDataById(id);
        bean.setLikeCount(bean.getLikeCount() + 1);
        shareInfoAdapter.addData(bean);
    }

    @Override
    protected void updateView() {
        presenter.getAllPostData(true, getRefreshTime(true));
    }


    private String getRefreshTime(boolean isRefresh) {
        if (isRefresh) {
            if (shareInfoAdapter.getData().size() == 0) {
                return "0000-00-00 01:00:00";
            }
            if (shareInfoAdapter.getData().size() > 10) {
                return shareInfoAdapter.getData(9).getCreatedAt();
            } else {
                return shareInfoAdapter.getData(shareInfoAdapter.getData().size() - 1)
                        .getCreatedAt();
            }
        } else {
            if (shareInfoAdapter.getData(shareInfoAdapter.getData().size() - 1) != null) {
                return shareInfoAdapter.getData(shareInfoAdapter.getData().size() - 1).getCreatedAt();
            } else {
                return "0000-00-00 01:00:00";
            }
        }
    }


    @Override
    public void updateData(List<PublicPostBean> publicPostBeans) {
        if (refresh.isRefreshing()) {
            if (shareInfoAdapter.getData().size() > 10) {
                shareInfoAdapter.removeEndData(shareInfoAdapter.getData().size()-10);
            }
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
            presenter.getAllPostData(true, getRefreshTime(true));
        } else {
            presenter.getAllPostData(true, getRefreshTime(true));
        }
    }

    @Override
    public void loadMore() {
        if (shareInfoAdapter.getData().size() > 0) {
            presenter.getAllPostData(false, getRefreshTime(false));
        } else {
            presenter.getAllPostData(false, getRefreshTime(false));
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (mMenu.isExpanded()) {
            mMenu.collapse();
        }
        Intent intent=new Intent(getActivity(),EditShareMessageActivity.class);
        intent.putExtra("destination","public");
        if (id == R.id.fab_share_info_video) {
            intent.putExtra("type","video");
        } else if (id == R.id.fab_share_info_normal) {
            intent.putExtra("type","normal");
        } else{
            intent.putExtra("type","image");
        }
        getActivity().startActivity(intent);
    }

    public static ShareInfoFragment instance() {
        return new ShareInfoFragment();
    }
}
